package org.example.service;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.example.pojo.News;
import org.example.pojo.ResultModel;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService{

    public final static Integer PAGE_SIZE = 20;
    public ResultModel query(String queryString,int page)throws Exception{
        // 封装需要使用的对象
        ResultModel resultModel = new ResultModel();
        // 从第几条开始查询，
        int start = (page - 1) * PAGE_SIZE;
        // 查询多少条为止
        int end = page * PAGE_SIZE;
        // 创建分词器
        Analyzer analyzer = new IKAnalyzer();

        //从多个域查询
        String[] fields = {"title","source","category","keyword","content"};
        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fields,analyzer);

        // 创建组合查询对象
    //    BooleanQuery.Builder builder = new BooleanQuery.Builder();
        // 根据查询关键字封装查询对象
       // QueryParser queryParser = new QueryParser("content",analyzer);
        Query query = null;
        if(StringUtils.isEmpty(queryString)){
           query = queryParser.parse("*:*");   //查询条件为空则查询所有
        }
        else{
            query = queryParser.parse(queryString);
        }
//        // 根据价格范围封装查询对象  0-500 ~~~~
//        Query query1 = null;
//        if(!StringUtils.isEmpty(price)){
//            String[] str = price.split("-");
//            query1 = IntPoint.newRangeQuery("price",Integer.parseInt(str[0]),Integer.parseInt(str[1]));
//            builder.add(query1, BooleanClause.Occur.MUST);
//        }
       // builder.add(query, BooleanClause.Occur.MUST);

        // 创建 Directory 对象，指定索引库的位置
        Directory directory = FSDirectory.open(Paths.get("E:\\dir"));
        // 创建输入流对象，将索引库读入
        IndexReader indexReader = DirectoryReader.open(directory);
        // 创建搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        // 搜索并获取搜索结果
        TopDocs topDocs = indexSearcher.search(query,end);   // 查询到 end为止
        // 获取查询到的总数
         resultModel.setRecordCount((int)topDocs.totalHits);
        // 获取查询到的结果集
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        System.out.println(topDocs.totalHits+"总数");
        List<News> newsList = new ArrayList<>();
        // 遍历结果集封装返回数据
        if(scoreDocs != null){
//            for (ScoreDoc scoreDoc : scoreDocs) {
//               Document document = indexReader.document(scoreDoc.doc);
//            }
            for(int i = start; i < end; i++){
                // 通过查询到的文档编号找到对应的文档
               Document document = indexReader.document(scoreDocs[i].doc);
               // 封装 Sku 对象
                News news = new News();
                  news.setTitle(document.get("title"));
                  news.setHead_img(document.get("head_img"));
                  news.setUrl(document.get("url"));
                  news.setContent(document.get("content"));
                  news.setDescription(document.get("description"));
                  news.setKeyword(document.get("keyword"));
                  news.setCategory(document.get("category"));
                  news.setSource(document.get("source"));
                  news.setTag(document.get("tag"));

                  newsList.add(news);
            }
        }
      //   封装 结果集
        resultModel.setNewsList(newsList);
        // 封装当前页
        resultModel.setCurPage(page);
        // 总页数
        Long pageCount = topDocs.totalHits % PAGE_SIZE > 0 ? (topDocs.totalHits/PAGE_SIZE)+1:topDocs.totalHits/PAGE_SIZE;
        resultModel.setPageCount(pageCount.intValue());
        return resultModel;
       // return newsList;
    }


}

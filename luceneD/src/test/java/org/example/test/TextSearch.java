package org.example.test;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.index.BaseCompositeReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

// 搜索
public class TextSearch {
    @Test
    public void testIndexSearch() throws Exception{
        // 1.创建分词器 对搜索的关键词进行分词使用
        // 分词器要和创建索引时使用的分词器一样
        Analyzer analyzer = new StandardAnalyzer();
        // 2.创建查询对象   // 默人查询域，分词器
        QueryParser queryParser = new QueryParser("name",analyzer);
        // 3.设置搜索关键词

        // **** queryParser只可以对文本进行搜索，不可以对数值进行搜索

        // Query query = queryParser.parse("华为 AND 手机");("华为 OR 手机")
       Query query = queryParser.parse("华为手机");   // 从 name中查询
        //queryParser.parse("brandName:华为手机");   //从brandName中查询
        // 4. 创建directory ，指定索引库的位置
        Directory dir = FSDirectory.open(Paths.get("D:\\dir"));
        // 5. 创建输入流对象  indexReader: 抽象对象，不能直接new
        IndexReader indexReader = DirectoryReader.open(dir);
        // 6. 创建搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        // 7. 搜索，返回结果集   返回10条数据进行展示，分页使用
       TopDocs topDocs = indexSearcher.search(query,10);
       //获取查询的结果集总数
        System.out.println("count："+ topDocs.totalHits);
        // 8. 获取结果集
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        // 9. 遍历结果集
        if(scoreDocs != null){
            for (ScoreDoc scoreDoc : scoreDocs) {
              int docID = scoreDoc.doc;   //文档ID  lucene在创建文档时自动分配的
              // 通过文档ID 读取文档
             Document doc = indexSearcher.doc(docID);
                System.out.println("*******************");
                // 通过域名，从文档中获取域值
                System.out.println("***id***" + doc.get("id"));
                System.out.println("***price***" + doc.get("price"));
            }
        }
        // 10.释放资源
    }


    // 数值范围查询
    @Test
    public void testRangeQuery() throws Exception{
        Analyzer analyzer = new StandardAnalyzer();
        // 创建查询对象
       Query query = IntPoint.newRangeQuery("price",100,9999);
       Directory directory = FSDirectory.open(Paths.get("D:\\dir"));
       // 输入流对象
       IndexReader indexReader = DirectoryReader.open(directory);
       // 搜索对象
       IndexSearcher indexSearcher = new IndexSearcher(indexReader);
       TopDocs topDocs = indexSearcher.search(query,10);
        System.out.println("count："+ topDocs.totalHits);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        if(scoreDocs!=null){
            for (ScoreDoc scoreDoc : scoreDocs) {
                int docID = scoreDoc.doc;
                Document doc = indexSearcher.doc(docID);
                System.out.println("*******************");
                // 通过域名，从文档中获取域值
                System.out.println("***id***" + doc.get("id"));
                System.out.println("***price***" + doc.get("price"));
            }
        }
    }

    // 高级查询
    //组合查询  根据关键字进行查询 又想根据价格进行查询
    @Test
    public void testBooleanQuery() throws Exception{
        Analyzer analyzer = new StandardAnalyzer();
        Directory directory = FSDirectory.open(Paths.get("E:\\dir"));
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        Query query1 = IntPoint.newRangeQuery("price",100,9999);
        QueryParser queryParser = new QueryParser("name",analyzer);
        Query query2 = queryParser.parse("华为手机");

        // 创建组合查询对象
                                         // 构建器
        BooleanQuery.Builder query = new BooleanQuery.Builder();

        // occur.must: and    should：or     must_not : not(非)
        // 查询条件都是 must_not,或者只有一个查询条件且是must_not,则查询不出任何数据
        query.add(query1, BooleanClause.Occur.MUST_NOT);  // 查询华为手机不包含价格在100-9999范围内
        query.add(query2, BooleanClause.Occur.MUST);
        // and or

        TopDocs topDocs = indexSearcher.search(query.build(),10);
        System.out.println("count："+ topDocs.totalHits);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        if(scoreDocs!=null){
            for (ScoreDoc scoreDoc : scoreDocs) {
                int docID = scoreDoc.doc;
                Document doc = indexSearcher.doc(docID);
                System.out.println("*******************");
                // 通过域名，从文档中获取域值
                System.out.println("***id***" + doc.get("id"));
                System.out.println("***price***" + doc.get("price"));
            }
        }
    }

    @Test
    public void testIndexSearch2() throws Exception{
        // 1.创建分词器 对搜索的关键词进行分词使用
        // 分词器要和创建索引时使用的分词器一样
        Analyzer analyzer = new StandardAnalyzer();

        //根据多个域进行查询
        String[] fileds = {"name","brandName"};

        //人为影响相关度的排序
        Map<String, Float> boots = new HashMap<>();
        //将分类的权重设为最大
        boots.put("categoryName", 10000000f);




       //从多个域查询
        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fileds,analyzer,boots);

        // 2.创建查询对象   // 默人查询域，分词器
        //QueryParser queryParser = new QueryParser("name",analyzer);
        // 3.设置搜索关键词

        // queryParser只可以对文本进行搜索，不可以对数值进行搜索

        // Query query = queryParser.parse("华为 AND 手机");("华为 OR 手机")
        Query query = queryParser.parse("华为手机");   // 从 name中查询
        //queryParser.parse("brandName:华为手机");   //从brandName中查询
        // 4. 创建directory ，指定索引库的位置
        Directory dir = MMapDirectory.open(Paths.get("D:\\dir"));
        // 5. 创建输入流对象  indexReader: 抽象对象，不能直接new
        IndexReader indexReader = DirectoryReader.open(dir);
        // 6. 创建搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        // 7. 搜索，返回结果集   返回10条数据进行展示，分页使用
        TopDocs topDocs = indexSearcher.search(query,10);
        //获取查询的结果集总数
        System.out.println("count："+ topDocs.totalHits);
        // 8. 获取结果集
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        // 9. 遍历结果集
        if(scoreDocs != null){
            for (ScoreDoc scoreDoc : scoreDocs) {
                int docID = scoreDoc.doc;   //文档ID  lucene在创建文档时自动分配的
                // 通过文档ID 读取文档
                Document doc = indexSearcher.doc(docID);
                System.out.println("*******************");
                // 通过域名，从文档中获取域值
                System.out.println("***id***" + doc.get("id"));
                System.out.println("***price***" + doc.get("price"));
            }
        }
        // 10.释放资源
    }

}

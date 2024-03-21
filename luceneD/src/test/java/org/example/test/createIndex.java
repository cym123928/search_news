package org.example.test;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.example.dao.NewsDao;
import org.example.dao.NewsDaoImpl;
import org.example.pojo.News;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class createIndex {

    @Test
    public void createIndexTest() throws Exception {
        // 1.采集对象
        NewsDao newsDao = new NewsDaoImpl();
        List<News> newsList = newsDao.queryNewsList();
        System.out.println(newsList.size() + "newslist size");
        List<Document> docList = new ArrayList<>();

        // 2.创建文档对象
        for (News news : newsList) {
            Document document = new Document();

            // 是否分词  否，id 分词后没有意义，
            // 是否索引   是，根据id查询，必须索引
            // 是否存储   是，因为id，可以确定唯一的一条数据,存储后才可以获取到id具体的内容

            //创建域对象并且放入文档对象中   域名，域值，是否存储
            //  document.add(new TextField("id",sku.getId(), Field.Store.YES));
            // stringField： N,Y,Y/N
            document.add(new TextField("title", news.getTitle(), Field.Store.YES));
            // 是否分词  是，分词后有意义，
            // 是否索引   是，根据name查询，必须索引
            // 是否存储   是 页面展示名称，所以存储
            // TextField: Y,Y,Y
            document.add(new StringField("source", news.getSource(), Field.Store.YES));
            document.add(new TextField("description", news.getDescription(), Field.Store.YES));
            document.add(new TextField("content", news.getContent(), Field.Store.YES));
            // 是否分词   是，lucene底层算法规定 根据范围查询必须分词   分词后没有意义，
            // 是否索引   是，需要根据交个进行范围查询
            // 是否存储    是
            // document.add(new TextField("price",String.valueOf(sku.getPrice()), Field.Store.YES));
            // IntPoint:不存储
            document.add(new StringField("category", news.getCategory(), Field.Store.YES));
            document.add(new StringField("keyword", news.getKeyword(), Field.Store.YES));
            document.add(new StringField("tag", news.getTag(), Field.Store.YES));
            // 存储
            //document.add(new StoredField("price", sku.getPrice()));
            // 存储的是图片地址  无需索引，无需分词  存储
            document.add(new StoredField("head_img", news.getHead_img()));
            document.add(new StoredField("url", news.getUrl()));
            //分类名称
            // 是否分词   否，因为分类是专有名词
            // 是否索引   是
            // 是否存储   是
            //document.add(new StringField("categoryName", sku.getCategoryName(), Field.Store.YES));
            //根据品牌进行查询
            // 是否分词   否，专有名词
            // 是否索引    是
            // 是否存储    是
           // document.add(new StringField("brandName", sku.getBrandName(), Field.Store.YES));

            docList.add(document);
        }

        // 3.创建分词器
        Analyzer analyzer = new StandardAnalyzer();  // 标准分词，单字分词，一个字成为一个词

        // 4.创建目录对象（Directory,目录对象表示索引库的位置
        Directory dir = FSDirectory.open(Paths.get("D:\\dir"));
        // 5.创建IndexWriterConfig 对象 执行切分词指定的分词器
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        // 6.IndexWriter：输出流对象，指定输出的位置和config初始化对象  输出到E/dir
        IndexWriter indexWriter = new IndexWriter(dir, config);
        // 7.写入文档到索引库
        for (Document doc : docList) {
            indexWriter.addDocument(doc);
        }
        // 8.释放资源
        indexWriter.close();
    }

}

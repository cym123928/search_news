package org.example.test;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;
import org.example.dao.NewsDao;
import org.example.dao.NewsDaoImpl;
import org.example.pojo.News;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// IK_analyser:
// 扩展词典：放专有名词
// 停用词典：过滤

// 索引库维护
public class TestIndexManager {

    // 创建索引库
    @Test
    public void createIndexTest() throws Exception {
        //1.采集数据
        NewsDao newsDao = new NewsDaoImpl();
        List<News> newsList = newsDao.queryNewsList();

        //2.创建文档对象
        List<Document> docList = new ArrayList<>();
        for (News news : newsList) {
            Document document = new Document();
            //域的分析
            // 是否分词(分词有无意义)——--->是否索引(是否要根据此进行查询)--->是否存储(展示在前端)
            document.add(new TextField("title", news.title, Field.Store.YES));

            //不分词（专有名词），索引，存储
            document.add(new StringField("source", news.source, Field.Store.YES));
            document.add(new StringField("category", news.category, Field.Store.YES));
            document.add(new TextField("keyword", news.keyword, Field.Store.YES));
            document.add(new TextField("tag", news.tag, Field.Store.YES));
            document.add(new TextField("description", news.description, Field.Store.YES));
            document.add(new TextField("content", news.content, Field.Store.YES));
            docList.add(document);
        }

        //3.创建分词器对象
        //中文单个分词
        Analyzer analyzer = new IKAnalyzer();

        //4.创建Directory对象，表示索引库的位置
        Directory directory = FSDirectory.open(Paths.get("D:\\dir"));

        //5.创建输出流初始化对象  指定写道索引库时使用什么分词器
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        //6.创建输出流对象
        IndexWriter writer = new IndexWriter(directory, config);

        //7.写入文档到数据库
        for (Document document : docList) {
            writer.addDocument(document);
        }

        //释放资源
        writer.close();

    }






    // 索引库修改操作
    @Test
    public void updateIndexTest() throws Exception {

        //修改文档
        Document document = new Document();
        document.add(new StringField("id", "1232323232", Field.Store.YES));
        document.add(new TextField("name", "xxxxx", Field.Store.YES));
        document.add(new StringField("categoryName", "手机", Field.Store.YES));
        document.add(new StringField("brandName", "华为", Field.Store.YES));


        // 3.创建分词器
        Analyzer analyzer = new IKAnalyzer();  // 标准分词，单字分词，一个字成为一个词

        // 4.创建目录对象（Directory,目录对象表示索引库的位置
        Directory dir = FSDirectory.open(Paths.get("D:\\dir"));
        // 5.创建IndexWriterConfig 对象 执行切分词指定的分词器
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        // 6.IndexWriter：输出流对象，指定输出的位置和config初始化对象  输出到E/dir
        IndexWriter indexWriter = new IndexWriter(dir, config);
        // 7.修改  修改条件， 修改成的内容
        // 根据业务的主键id进行修改        词对象
        indexWriter.updateDocument(new Term("id", "1232323232"), document);

        // 8.释放资源
        indexWriter.close();
    }


    // 根据测试条件删除   删除某一个内容 // 删除所有
    @Test
    public void deleteIndexTest() throws Exception {
        Analyzer analyzer = new IKAnalyzer();
        Directory dir = FSDirectory.open(Paths.get("D:\\dir"));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(dir, config);
        //测试根据条件删除
        indexWriter.deleteDocuments(new Term("id", "1232323232"));

        //测试删除所有内容
        //indexWriter.deleteAll();

        indexWriter.close();
    }
//    //增删改时从段中找到原来文档文件，删除，之后会在末尾添加文件  减少了磁盘的随机IO
//    //cfs ,cfe : 复合索引文件： 文档文件+索引文件
//    // 索引文件： 关键字，文档号，出现位置
//    // 词典数据结构：跳跃表。FST，字典树  存储关键字
//    // 跳跃表
//    // 跳跃表分层进行 随机找出几个节点构成第二层
//    // 查询时从最高层开始查找
//
//    // FST:状态机  内存占用小， 结构较复杂
//
//
//    // lucene 优化
//    // 1.解决大量磁盘IO
//    // 内存缓冲区满了，写进段中  批量处理
//    // 将多个文档合并，写进段中
//
//    // 2.分词器优化
//    // standard 比 IK 快
//
//    // 3.索引库创建位置
//    // FSDirectory:通过操作硬盘
//    // MMapDirectory : 内存映射  第一次查询从硬盘中查，查到的数据加载到内存中，之后查询就从内存中查询
//    // 查询速度很快  324->18
//
//    // 4.搜索api选择
//    // 使用TermQuery 代替 QueryParser
//    // 避免大范围的日期范围
//
//
//    //相关度排序
//    // tf:某个词在文档中出现次数越多，越重要； df：多少文档包括词，越大，词越不重要
//    // 人为影响，设置域的权重，从而影响查询结果
//
//
//
    // 测试创建索引速度优化
    @Test
    public void createIndexTest2() throws Exception {
        // 1.采集对象
        NewsDao newsDao = new NewsDaoImpl();
        List<News> newsList = newsDao.queryNewsList();

        List<Document> docList = new ArrayList<>();

        // 2.创建文档对象
        for (News news : newsList) {
            Document document = new Document();
            //域的分析
            // 是否分词(分词有无意义)——--->是否索引(是否要根据此进行查询)--->是否存储(展示在前端)
            document.add(new TextField("title", news.title, Field.Store.YES));

            //不分词（专有名词），索引，存储
            document.add(new StringField("source", news.source, Field.Store.YES));
            document.add(new StringField("category", news.category, Field.Store.YES));
            document.add(new TextField("keyword", news.keyword, Field.Store.YES));
            document.add(new TextField("tag", news.tag, Field.Store.YES));
            document.add(new TextField("description", news.description, Field.Store.YES));
            document.add(new TextField("content", news.content, Field.Store.YES));
            docList.add(document);
        }


        long start = System.currentTimeMillis(); //获取当前时间的毫秒数

        // 3.创建分词器
        Analyzer analyzer = new IKAnalyzer();  // 标准分词，单字分词，一个字成为一个词

        // 4.创建目录对象（Directory,目录对象表示索引库的位置

        //内存映射
        Directory dir = MMapDirectory.open(Paths.get("D:\\dir"));

        //Directory dir = FSDirectory.open(Paths.get("E:\\dir"));
        // 5.创建IndexWriterConfig 对象 执行切分词指定的分词器
         // 未优化 创建索引需要7s
          // 若设置数值过大，会过度消耗内存，会提升写入磁盘速度
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        // 磁盘IO优化
         //第一种：设置在内存中多少个文档向磁盘中批量写入一次数据
        config.setMaxBufferedDocs(100000);

        // 6.IndexWriter：输出流对象，指定输出的位置和config初始化对象  输出到E/dir
        IndexWriter indexWriter = new IndexWriter(dir, config);
        // 7.写入文档到索引库

        // 第二种：设置多个文档合并成一个段；
        // 数值越大，索引的速度越快，搜索的速度越慢
        indexWriter.forceMerge(100);
        for (Document doc : docList) {
            indexWriter.addDocument(doc);
        }
        // 8.释放资源
        indexWriter.close();
        long end = System.currentTimeMillis();
        System.out.println("消耗时间为："+ (end-start)+"ms");
    }

}

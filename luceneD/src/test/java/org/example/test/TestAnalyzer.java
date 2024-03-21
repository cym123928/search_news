package org.example.test;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.nio.file.Paths;

public class TestAnalyzer {
    // 中文语义分析，提供停用词典和扩展词典
    @Test
    public void TestIKAnalyzer() throws Exception{
        Analyzer analyzer = new IKAnalyzer();
        Directory directory = FSDirectory.open(Paths.get("E:\\dir"));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(directory,config);
        Document doc = new Document();
        doc.add(new TextField("name","vivo X23 8GB+128GB 幻夜蓝，水滴屏全面屏，游戏手机，移动联通电信全网通4G手机", Field.Store.YES));
        indexWriter.addDocument(doc);
        indexWriter.close();
    }
}

//package com.yr.util;
//
//import java.io.File;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//import org.apache.lucene.analysis.Analyzer;
//import org.apache.lucene.document.Document;
//import org.apache.lucene.document.Field;
//import org.apache.lucene.index.DirectoryReader;
//import org.apache.lucene.index.IndexWriter;
//import org.apache.lucene.index.IndexWriterConfig;
//import org.apache.lucene.search.IndexSearcher;
//import org.apache.lucene.search.Query;
//import org.apache.lucene.search.ScoreDoc;
//import org.apache.lucene.search.TopDocs;
//import org.apache.lucene.search.highlight.Highlighter;
//import org.apache.lucene.search.highlight.QueryScorer;
//import org.apache.lucene.search.highlight.SimpleFragmenter;
//import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
//import org.apache.lucene.store.Directory;
//import org.apache.lucene.store.FSDirectory;
//import org.wltea.analyzer.lucene.IKAnalyzer;
//
///**
// * 执行
// * @author zxy
// *
// * 2018年6月12日 下午7:14:44
// *
// */
//public class LuceneMain {
//	public static void main(String[] args) throws Exception {
////		String filePath = "G:/lucene_bigDate/deposit"; // 从中将path读出放入索引再从索引中找寻值
//		String indexPath = "G:/lucene_bigDate"; // 存入索引(指定索引库位置)
//		LuceneDeposit ld = new LuceneDeposit(); // 存数据
//		LuceneTake lk = new LuceneTake(); // 取数据
//		Path docDirPath = Paths.get(indexPath, new String[0]);
//		Directory directory = FSDirectory.open(docDirPath); // 指定创建索引的位置
//		Analyzer analyzer = new IKAnalyzer(); // StandardAnalyzer: 默认(基本)的分词器,将文档内容切词 	IK分词器与默认分词器分词方式有些不同
//		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer); // 创建写入对象(创建索引的配置信息)
//		indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND); // 创建或追加
//		
//		IndexWriter indexWriter = new IndexWriter(directory,indexWriterConfig); // 创建写索引库核心对象
//		Document document = new Document(); // 创建文档对象
//		Field f;
////		f = ld.BinaryDocValField(); // ---
////		f = ld.IntField();
////		f = ld.StringField();
////		f = ld.FloatFiled();
////		f = ld.DoubleField();
////		f = ld.LongField();
////		f = ld.DoubleDovValField(); // ---
////		f = ld.FloatDocValField();
////		f = ld.SortedNumericDocValField();
////		f = ld.SortedSetDocValField();
////		f = ld.SortedDocValField();
////		f = ld.StoreField();
////		f = ld.BinaryPointTest(); // ----
////		f = ld.DoubleRangeTest();
////		f = ld.FloatRangeTest();
////		f = ld.IntRangeTest();
////		f = ld.LongRangeTest();
////		f = ld.textField();
//		document.add(f); // 设置文档域字段
//		indexWriter.addDocument(document); // 添加索引库(写入索引库)
////		indexWriter.deleteDocuments(document); // 删除索引库
////		indexWriter.updateDocument(document); // 执行更新,会把所有符合条件的Document删除,再新增.
//		indexWriter.commit();//提交
////		indexWriter.deleteAll(); // 全部删除
//        indexWriter.close(); // 释放资源
//		
//        
//        
//        // 创建检索的对象,需要指定从哪里检索
////		Directory indexDir = FSDirectory.open( Paths.get(indexPath));
////        DirectoryReader directoryReader = DirectoryReader.open(indexDir); // 读取索引库索引
////        IndexSearcher searcher = new IndexSearcher(directoryReader); // 创建查询索引库核心对象
//        
////        Query  query;
////        query = lk.BinaryDocValField();
////        query = lk.IntField();
////        query = lk.StringField();
////        query = lk.FlotField();
////        query = lk.DoubleField();
////        query = lk.LongField();
////        query = lk.DoubleDovValField();
////        query = lk.FloatDocValField();
////        query = lk.SortedNumericDocValField();
////        lk.SortedSetDocValField();
////        lk.SortedDocValField();
////        query = lk.StoreField();
////        query = lk.BinaryPointTest();
////        query = lk.DoubleRangeTest();
////        query = lk.FloatRangeTest();
////        query = lk.IntRangeTest();
////        query = lk.LongRangeTest();
////        query = lk.TextField();
//        
//        // 高亮(有查询结果,说明一定有高亮) -- 高亮在查询结果的基础上
////        QueryScorer scorer = new QueryScorer(query);
//////        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
////        TopDocs topDocs = searcher.search(query, 100); // 通过search方法检索
////        ScoreDoc[] docs = topDocs.scoreDocs;
////        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color='red'>","</font></b>"); // 使用于页面上的显示(其中标签可以修改)
////        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);
////        highlighter.setTextFragmenter(new SimpleFragmenter(20));//设置每次返回的字符数
////        
////        File file = new File(indexPath); // 获取文件路径
////        String fileName = file.getName(); // 获取文件名
////        
////        for(int i=0;i<docs.length;i++){     
////            Document doc = searcher.doc(docs[i].doc);     
////            String str = highlighter.getBestFragment(analyzer, fileName, doc.get("content")); // 分词器,文件名,取哪里(content)的内容
////            System.out.println(str);     
////        }
//        
//        
////        TopDocs topDocs = searcher.search(query, 100); // 通过search方法检索
////        int docsNum = (int) topDocs.totalHits; // 获取查询文档总记录数
////        ScoreDoc[] docs = topDocs.scoreDocs; // 获取文档id,文档得分数组
////        for (int i = 0; i < docsNum; i++) { // 遍历检索结果	// 获取指定内容 -- doc.get("content");
////        	System.out .println("BinaryDocValuesField: -- " + searcher.doc(docs[i].doc).get("bdValuef"));
////        	System.out .println("IntPoint: -- " + searcher.doc(docs[i].doc).get("intFiled"));
////        	System.out .println("StringField: -- " + searcher.doc(docs[i].doc).get("string"));
////        	System.out .println("FloatPoint: -- " + searcher.doc(docs[i].doc).get("floatFiled"));
////        	System.out .println("DoublePoint: -- " + searcher.doc(docs[i].doc).get("doubleFiled"));
////        	System.out .println("LongPoint: -- " + searcher.doc(docs[i].doc).get("longFiled"));
////        	System.out .println("DoubleDocValuesField: -- " + searcher.doc(docs[i].doc).get("ddvf"));
////        	System.out .println("FloatDocValuesField: -- " + searcher.doc(docs[i].doc).get("fdvf"));
////        	System.out .println("SortedNumericDocValuesField: -- " + searcher.doc(docs[i].doc).get("sndvf"));
////        	System.out .println("SortedSetDocValuesField: -- " + searcher.doc(docs[i].doc).get("ssdvf"));
////        	System.out .println("SortedDocValuesField: -- " + searcher.doc(docs[i].doc).get("sdvf"));
////        	System.out .println("StoreField: -- " + searcher.doc(docs[i].doc).get("sf"));
////        	System.out .println("BinaryPoint: -- " + searcher.doc(docs[i].doc).get("bp"));
////        	System.out .println("DoubleRange: -- " + searcher.doc(docs[i].doc).get("doubleRange"));
////        	System.out .println("FloatRange: -- " + searcher.doc(docs[i].doc).get("floatRange"));
////        	System.out .println("IntRange: -- " + searcher.doc(docs[i].doc).get("intRange"));
////        	System.out .println("LongRange: -- " + searcher.doc(docs[i].doc).get("longRange"));
////        	System.out .println("TextField: -- " + searcher.doc(docs[i].doc).get("content"));
////        }
//	}
//	
//}

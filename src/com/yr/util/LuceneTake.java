package com.yr.util;

import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * 取数据
 * 
 * @author zxy
 *
 * 2018年6月12日 下午5:30:08
 *
 */
public class LuceneTake {

	public Query TextField(String val) {
		try {
			QueryParser parser = new QueryParser("fileName",new IKAnalyzer()); // 指定对哪个字段检索并且指定使用哪个分词器
	        Query query = parser.parse(val); // 解析关键词进行分词
	        return query;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Query fieldUrl(String fileUrl) {
		try {
			QueryParser parser = new QueryParser("fileUrl",new IKAnalyzer()); // 指定对哪个字段检索并且指定使用哪个分词器
	        Query query = parser.parse(fileUrl); // 解析关键词进行分词
	        return query;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Query textContent(String fileContent) {
		try {
			QueryParser parser = new QueryParser("fileContent",new IKAnalyzer()); // 指定对哪个字段检索并且指定使用哪个分词器
	        Query query = parser.parse(fileContent); // 解析关键词进行分词
	        return query;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
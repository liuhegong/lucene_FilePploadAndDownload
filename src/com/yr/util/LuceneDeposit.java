package com.yr.util;

import java.util.Iterator;
import java.util.List;

import com.yr.entity.Entiy;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

/**
 * 存数据
 * @author zxy
 *
 * 2018年6月12日 下午5:29:54
 *
 */
public class LuceneDeposit {
	
	public Field LongField(String fileSize) {
		StringField sf = new StringField("fileSize", fileSize, Field.Store.YES);
		return sf;
	}
	
	public Field textField(String fileName) {
		try {
			Field content = new TextField("fileName", fileName,Field.Store.YES);
	        return content;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public Field fieldUrl(String fileUrl) {
		try {
			Field content = new TextField("fileUrl", fileUrl,Field.Store.YES);
	        return content;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public Field textContent(String fileContent) {
		try {
			Field content = new TextField("fileContent", fileContent,Field.Store.YES);
	        return content;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
}

package com.yr.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.yr.entity.FileCollect;
import com.yr.entity.FileManager;
import com.yr.entity.Page;
import com.yr.util.LuceneDeposit;
import com.yr.util.LuceneTake;

import net.sf.json.JSONObject;

@Controller
public class UploadController {
	
	/**
	 * 遍历电脑所有盘幅
	 * 
	 * @author zxy
	 * 
	 * 2018年6月15日 下午2:42:47
	 * 
	 * @param map 将值返回到页面显示
	 * @param list 将值装入集合
	 * @return
	 */
	public String listRoots(Map<String, Object> map, List<FileManager> list) {
        File[] paths;
        paths = File.listRoots();
        for (File path : paths) {
            
        	FileManager user = new FileManager();
            user.setName(String.valueOf(path));
            user.setMark(0);
            list.add(user);
        }
        List<FileManager> list1 = new ArrayList<>();
        FileManager user1 = new FileManager();
        user1.setName("计算机");
        user1.setMark(1);
        list1.add(user1);
        map.put("fileList1", list1);
        map.put("fileList", list);
        return "upload";
    }

	/**
	 * 文件下载
	 */
	@RequestMapping(value="/download")
	public ResponseEntity<byte[]> download(@RequestParam(value = "loadFilePath",required = false) String loadFilePath) throws IOException {
		loadFilePath= new String(loadFilePath.getBytes("iso-8859-1"),"utf-8");
		File file = new File(loadFilePath.trim());
		String fileName=file.getName();
		HttpHeaders headers = new HttpHeaders();
		String download = new String(fileName.getBytes("UTF-8"), "iso-8859-1");// 为了解决中文名称乱码问题
		headers.setContentDispositionFormData("attachment", download);
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
	}
	
	/**
	 * 添加索引
	 * @author zxy
	 * 
	 * 2018年6月15日 下午2:44:36
	 * 
	 * @param path
	 * @throws Exception
	 */
	public static void storage(String path)throws Exception{
		String fileDepositUrl = "G:/lucene_bigDate"; // 文件存放路径
		File file = new File(path);
		String[] files = file.list();
		if(null == files || "".equals(files)){
			return;
		}
		for (int i = 0; i < files.length; i++) {
			String fileName = files[i]; //文件名
			files[i] = file.getPath() + file.separator + files[i]; //获取文件路劲
			boolean isFile= new File(files[i]).isDirectory(); //判断文件是否是文件夹
			if(isFile){
				storage(files[i]);
			}else{
				boolean isTxtFile = files[i].endsWith(".txt"); //判断文件是否是txt文件
				if(isTxtFile){
					String fileUrl = files[i]; // 文件路径
					Long fileSize = new File(fileUrl).length(); //文件大小
					String fileContent = readTxtFile(fileUrl); //文件内容
					LuceneDeposit ld = new LuceneDeposit(); // 存数据
					Path docDirPath = Paths.get(fileDepositUrl, new String[0]);
					Directory directory = FSDirectory.open(docDirPath); // 指定创建索引的位置
					Analyzer analyzer = new IKAnalyzer(); // StandardAnalyzer: 默认(基本)的分词器,将文档内容切词 	IK分词器与默认分词器分词方式有些不同
					IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer); // 创建写入对象(创建索引的配置信息)
					indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND); // 创建或追加 (如果是CREATE会有遗漏,丢失部分内容)
					
					IndexWriter indexWriter = new IndexWriter(directory,indexWriterConfig); // 创建写索引库核心对象
					Document document = new Document(); // 创建文档对象
					Field fs = ld.LongField(fileSize.toString());
					Field fn = ld.textField(fileName);
					Field fu = ld.fieldUrl(fileUrl);
					Field fc = ld.textContent(fileContent);
					document.add(fs); // 设置文档域字段
					document.add(fn);
					document.add(fc);
					document.add(fu);
					indexWriter.addDocument(document); // 添加索引库(写入索引库)
					indexWriter.commit();//提交
					indexWriter.close(); // 释放资源
				}
			}
		}
	}

	/**
	 * 读取文件内容
     * 功能：Java读取txt文件的内容
     * 步骤：1：先获得文件句柄
     * 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取
     * 3：读取到输入流后，需要读取生成字节流
     * 4：一行一行的输出。readline()。
     * 备注：需要考虑的是异常情况
     * @param filePath
     */
    public static String readTxtFile(String filePath){
    	String value = "";
        try {
                String encoding="GBK";
                File file=new File(filePath);
                if(file.isFile() && file.exists()) { //判断文件是否存在
                    InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file),encoding);//考虑到编码格式
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTxt = null;
                    while((lineTxt = bufferedReader.readLine()) != null){
                    	value+=lineTxt;
                    }
                    read.close();
		        } else { 
		            System.out.println("找不到指定的文件");
		        }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return value;
    }
	
	LuceneDeposit ld = new LuceneDeposit(); // 存数据
	static LuceneTake lk = new LuceneTake(); // 取数据
	
	/**
	 * 单个取值
	 */
//	@RequestMapping(value="/pageValue")
//	public String pageValue(HttpServletRequest request,Map<String, Object> mso) {
////	public static void pageValue() {
//		try {
//			// 创建检索的对象,需要指定从哪里检索
//			String value = request.getParameter("value");
////			String value = "变量";
//			if(!value.equals("") && value != null){
//				Directory indexDir = FSDirectory.open( Paths.get("G:/lucene_bigDate"));
//				DirectoryReader directoryReader = DirectoryReader.open(indexDir); // 读取索引库索引
//				IndexSearcher searcher = new IndexSearcher(directoryReader); // 创建查询索引库核心对象
////				Query  query = lk.TextField(value);
//				Query  querr = lk.textContent(value);
//				TopDocs /*topDocs = searcher.search(query, 9000);*/ // 通过search方法检索
//						topDocs = searcher.search(querr, 9080);
//				int docsNum = (int) topDocs.totalHits; // 获取查询文档总记录数
//				ScoreDoc[] docs = topDocs.scoreDocs; // 获取文档id,文档得分数组
//				List<String> list = new ArrayList<>();
//				for (int i = 0; i < docsNum; i++) {
//					/*String name = searcher.doc(docs[i].doc).get("fileName");*/
//					String content = searcher.doc(docs[i].doc).get("fileContent");
//					String sub = content.substring(0,100);
//					/*if(null == name){
//						name=searcher.doc(docs[i].doc).get("fileName");
//					}*/
//					if(null == content){
//						content = searcher.doc(docs[i].doc).get("fileContent");
//					}
//					if( !content.equals("")){
////						list.add("文件名:  " + name);
//						list.add("文件内容:  " + sub + ".....");
//					}
//				}
//				System.out.println(list);
//				mso.put("fileList", list);
//			}else{
//				mso.put("error", "value值不能为空");
//			}
//			return "upload";
//		} catch (Exception e) {
//			mso.put("error", "异常错误");
//			return "upload";
//		}
//	}
	
	
	
  /**
    * 组合取值 高亮显示 (注释的是未分页的显示)
    * 
    * "组合查询"搜索—BooleanQuery
    * BooleanQuery也是实际开发过程中经常使用的一种Query。
    * 它其实是一个组合的Query，在使用时可以把各种Query对象添加进去并标明它们之间的逻辑关系。
    * 在本节中所讨论的所有查询类型都可以使用BooleanQuery综合起来。
    * BooleanQuery本身来讲是一个布尔子句的容器，它提供了专门的API方法往其中添加子句，
    * 并标明它们之间的关系，以下代码为BooleanQuery提供的用于添加子句的API接口：
    * @throws Exception
    */
	@RequestMapping(value="/pageValue")
	public String pageValue(HttpServletRequest request,Map<String, Object> map) throws Exception{
		try {
			String value = request.getParameter("value");
//			List<FileCollect> list = new ArrayList<>();
//			if(!value.equals("") && value != null){
//				Analyzer analyzer = new IKAnalyzer();
//				Directory indexDir = FSDirectory.open( Paths.get("G:/lucene_bigDate"));
//				DirectoryReader directoryReader = DirectoryReader.open(indexDir); // 读取索引库索引
//				IndexSearcher searcher = new IndexSearcher(directoryReader);
//				int a = 1;
//				int b = 99;
//				Query query1 = lk.TextField(value); // value 检索内容
//				Query query2 = lk.textContent(value);
//				BooleanQuery.Builder  builder = new BooleanQuery.Builder(); // 多条件查询的必须条件(组合查询)
//				// 1．MUST和MUST：取得连个查询子句的交集。 and
//				// 2．MUST和MUST_NOT：表示查询结果中不能包含MUST_NOT所对应得查询子句的检索结果。 查询到must中的内容不能包含must_not中的内容(eg: must:abc  must_not:bdd  b不能查询出来)
//				// 3．SHOULD与MUST_NOT：连用时，功能同MUST和MUST_NOT。
//				// 4．SHOULD与MUST连用时，结果为MUST子句的检索结果,但是SHOULD可影响排序。-----
//				// 5．SHOULD与SHOULD：表示“或”关系，最终检索结果为所有检索子句的并集。 or
//				// 6．MUST_NOT和MUST_NOT：无意义，检索无结果。 
//				builder.add(query1, BooleanClause.Occur.SHOULD); // 将条件加入
//				builder.add(query2, BooleanClause.Occur.SHOULD);
//				BooleanQuery booleanQuery = builder.build();
//				TopDocs hits = searcher.search(booleanQuery, 10000);
//				System.out.println("匹配 " + value + "And" + a + "——" + b + ",总共查询到" + hits.totalHits + " 个文档");
//				if(hits.totalHits != 0){
//					SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color='red'>","</font></b>");
//					Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new  QueryScorer(query1));
//					Highlighter highlighter1 = new Highlighter(simpleHTMLFormatter, new  QueryScorer(query2));
//					highlighter.setTextFragmenter(new SimpleFragmenter(20));//设置每次返回的字符数
//					highlighter1.setTextFragmenter(new SimpleFragmenter(100));
//					for(ScoreDoc scoreDoc:hits.scoreDocs){
//						Document doc = searcher.doc(scoreDoc.doc);
//						FileCollect fc = new FileCollect();
//						fc.setFileUrl(doc.get("fileUrl"));
//						String fileNameLg = doc.get("fileName");
//						String size = doc.get("fileSize");
//						Long longSize = Long.parseLong(size);
//						if(longSize < 1024) {
//							longSize = (long) 1;
//						} else if(longSize == 0){
//							longSize = (long) 0;
//						} else {
//							longSize = (long) longSize / 1024;
//						}
//						size = longSize.toString();
//						fc.setFileSize(size + "kb");
//						String fileConSub = doc.get("fileContent");
//						
//						String str = highlighter.getBestFragment(analyzer, value, fileNameLg); // 高亮查询
//						if (null == str) { // 如果没有查询到就没有数据 ,赋入没有高亮的内容
//							str = fileNameLg;
//						}
//						fc.setFileName(str);
//						
//						if(fileConSub.length() > 100){
//							String str2 = highlighter1.getBestFragment(analyzer, value, fileConSub);
//							if (null == str2) {
//								str2 = fileConSub.substring(0, 100) + "...";
//							}
//							fc.setFileContent(str2);
//						}else if(fileConSub.length() <= 100){
//							String str2 = highlighter1.getBestFragment(analyzer, value, fileConSub);
//							if (null == str2) {
//								str2 = fileConSub;
//							}
//							fc.setFileContent(str2);
//						}
//						list.add(fc);
//						System.out.println(fc.toString());
//					}
//					map.put("fileList", list);
//				}else{
//					map.put("error", "没有匹配到对应值");
//				}
//			}else{
//				map.put("error", "关键字搜索不能为空");
//			}
			map.put("value", value);
			return "upload";
		} catch (Exception e) {
			map.put("error", "异常错误");
			e.printStackTrace();
			return "upload";
		}
	}
	
	  /** 
	    * 对搜索返回的前n条结果进行 分页显示 (带高亮)
	    * @param keyWord       查询关键词 
	    * @param page          分页实体类 
	    * @throws Exception 
	    */ 
	   public static Map<String, Object> selectQuery(String keyWord, Page<FileCollect> page) throws Exception {
		   Map<String, Object> map = new HashMap<String, Object>();
		   List<FileCollect> list = new ArrayList<FileCollect>();//创建List<FileManager>用来接取搜索的结果
		   if (null == keyWord) {//如果没有查询条件，就不查询
			   return null;
		   }
		   Analyzer analyzer=new IKAnalyzer(); // IK分词器
		   Directory dir = FSDirectory.open( Paths.get("G:/lucene_bigDate"));
		   DirectoryReader reader = DirectoryReader.open(dir); // 读取索引库索引
		   IndexSearcher is = new IndexSearcher(reader);
	       String fileName="fileName";               //查询文件名字段
	       String fileContents="fileContent";       //查询文件内容字段
	       
	       Query query1 = lk.TextField(keyWord); // value 检索内容
	       Query query2 = lk.textContent(keyWord);
	       
	       BooleanQuery.Builder  builder = new BooleanQuery.Builder();//多条件查询，组合条件查询必需
	       //  1．MUST和MUST：取得连个查询子句的交集。
	       //  2．MUST和MUST_NOT：表示查询结果中不能包含MUST_NOT所对应得查询子句的检索结果。
	       // 3．SHOULD与MUST_NOT：连用时，功能同MUST和MUST_NOT。
	       // 4．SHOULD与MUST连用时，结果为MUST子句的检索结果,但是SHOULD可影响排序。
	       // 5．SHOULD与SHOULD：表示“或”关系，最终检索结果为所有检索子句的并集。
	       // 6．MUST_NOT和MUST_NOT：无意义，检索无结果。
	       builder.add(query1, BooleanClause.Occur.SHOULD);
	       builder.add(query2, BooleanClause.Occur.SHOULD);
	       BooleanQuery  booleanQuery=builder.build();
		     
		   ScoreDoc sd;//进行分页的条件
		     
		    if(page.getPage() == 1) {  
		        sd = null;  
		    }else {  
		        int num = page.getPageSize()*(page.getPage()-1);//获取上一页的最后是多少    
		        TopDocs td = is.search(booleanQuery, num);    
		        sd = td.scoreDocs[num-1];    
		    }  
		     
		    //高亮显示
		    SimpleHTMLFormatter simpleHtmlFormatter = new SimpleHTMLFormatter("<font style='color:red'>","</font>");//设定高亮显示的格式，也就是对高亮显示的词组加上前缀后缀  
		    Highlighter highlighter = new Highlighter(simpleHtmlFormatter,new QueryScorer(query1));  
		    highlighter.setTextFragmenter(new SimpleFragmenter(50));//设置每次返回的字符数.想必大家在使用搜索引擎的时候也没有一并把全部数据展示出来吧，当然这里也是设定只展示部分数据  
		    
		    Highlighter highlighter1 = new Highlighter(simpleHtmlFormatter,new QueryScorer(query2));  
		    highlighter1.setTextFragmenter(new SimpleFragmenter(100));//设置每次返回的字符数.想必大家在使用搜索引擎的时候也没有一并把全部数据展示出来吧，当然这里也是设定只展示部分数据
//		    int start = (currentPage - 1) * pageSize;
	        // 查询数据， 结束页面自前的数据都会查询到，但是只取本页的数据
//	        TopDocs topDocs = is.search(booleanQuery, start);
	        //获取到上一页最后一条
//	        ScoreDoc preScore = topDocs.scoreDocs[start-1];

		    // 核心方法  
		   TopDocs top = is.searchAfter(sd, booleanQuery, page.getPageSize());// 这里的page...像hibernate，是在这一页上查12条  
		 //计算总页数
	       page.setPageSizeCount(Integer.valueOf(String.valueOf(top.totalHits)));
		   System.out.println("查询:" + keyWord +" 得到" + top.totalHits + "条记录");  
		   ScoreDoc[] scoreDocs = top.scoreDocs;  
		   if(scoreDocs != null && scoreDocs.length > 0){  
		       for(ScoreDoc scoreDoc : scoreDocs) {
		    	   FileCollect fileMa = new FileCollect();
		    	   Document doc = is.doc(scoreDoc.doc);
		    	   String filePath = doc.get("fileUrl");
		    	   String fileSize = doc.get("fileSize");
		    	   String fileN = doc.get(fileName);
		    	   String fileC = doc.get(fileContents);
		           String name = highlighter.getBestFragment(analyzer, keyWord, fileN);//设置高亮
		           int lens = fileN.length();
		           if (null == name) {//如果没有高亮条件,就进入
		        	   name = fileN;
		        	   if(lens > 100){//如果字符总数超过一百，就加上...
		        		   name = fileN.substring(0, 100) + "...";
		        	   }
		           }
		           if(lens > 100){//如果字符总数超过一百，就加上...
	        		   name = name + "...";
	        	   }
		           String content = highlighter1.getBestFragment(analyzer, keyWord, fileC);//设置高亮
		           int len = fileC.length();
		           if (null == content) {//如果没有高亮条件,就存前一百字符
		        	   content = fileC;
		        	   if(len > 100){//如果字符总数超过一百，就加上...
		        		   content = fileC.substring(0, 100) + "...";
		        	   }
		           }
		           if(len > 100){//如果字符总数超过一百，就加上...
	        		   content = content + "...";
	        	   }
		           //将搜索内容存进实体类中
		           fileMa.setFileName(name);
		           fileMa.setFileContent(content);
		           fileMa.setFileUrl(filePath);
		           fileMa.setFileSize(fileSize);
		           list.add(fileMa);//将搜索内容存进List里
		           System.out.println("文件大小:" + fileSize);
		           System.out.println("文件路径:" + filePath);
		           System.out.println("文件名:" + name);
		           System.out.println("文件内容:" + content);
		       }  
		   }
		   page.setPt(list);
	       map.put("page", page);
		   return map;
	   }
	
	   /**
	     * 分页查询
	     * String
	     * 2018年6月15日下午5:09:15
	     */
	    @ResponseBody
	    @RequestMapping(value="/testList", method=RequestMethod.GET, produces = "text/json;charset=UTF-8")
	    public String testList(String select, Page<FileCollect> page) throws Exception {
	    	if("".equals(page.getPage())){
	            page.setPage(1);
	        }
	        if("".equals(page.getPageSize())){
	            page.setPageSize(10);
	        }
	    	Map<String, Object> map = UploadController.selectQuery(select, page);
	    	JSONObject json = JSONObject.fromObject(map);
	    	System.out.println(json);
	    	String test = json.toString();
	    	String test1 = null;
	        try{
	            test1 = new String(test.getBytes("UTF-8"),"UTF-8");
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        }
	        if (null == test1) {
	        	test1 = "1";
	        }
	    	return test1;
	    }
	
	
	public static void main(String[] args) throws Exception {
		String indexPath = "G:/"; // 创建索引
		storage(indexPath);
	}
}

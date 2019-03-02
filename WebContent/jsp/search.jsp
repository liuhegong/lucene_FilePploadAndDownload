<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>搜索</title>
<style type="text/css">
	.sc{
		height: 30px;
		width: 200px;
		margin-top:30px;
		margin-left:10px;
		line-height: 30px;
		background-color:transparent;
	}
	
	:-webkit-input-placeholder { /* WebKit, Blink, Edge */  
    	color:    red;  
	}  
	:-moz-placeholder { /* Mozilla Firefox 4 to 18 */  
	   color:    red;  
	   opacity:  1;  
	}  
	::-moz-placeholder { /* Mozilla Firefox 19+ */  
	   color:    red;  
	   opacity:  1;  
	}  
	:-ms-input-placeholder { /* Internet Explorer 10-11 */  
	   color:    red;  
	}
	
	.btn {
		 background-color: white;
		 color: black;
		 border: 2px solid #e7e7e7;
		 background:none;border:none;
		 padding-top:3px;
		 border-top:1px solid #708090;
		 border-right:1px solid #708090;
		 border-left:1px solid #708090; 
		 width:55px;
		 height:30px;
		 text-align: center;
    	 text-decoration: none;
		 display: inline-block;
		 margin: 4px 2px;
		 cursor: pointer;
		 transition-duration: 0.4s;
		 border-radius: 4px;
	}
	
	.bto:hover {background-color: #e7e7e7;}
	
	.dv{
		margin-left:150px;
	}
</style>
</head>
<body background="<%=request.getContextPath()%>/jsp/banner.jpg">
	<div class="dv">
		<form action="<%=request.getContextPath()%>/pageValue" name="myform" target="u">
			<font color="#8B8989">请输入要搜索的关键字:</font> <input type="text" class="sc" placeholder="关键词" onfocus="this.placeholder=''" onblur="this.placeholder='请输入关键词'" name="value" ></input>
			<input type="submit" value="搜索" class="btn bto" id="seach"></input>
		</form>
	</div>
</body>
</html>
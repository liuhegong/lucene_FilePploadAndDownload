<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>显示</title>
<!--  
	SpringMVC 处理静态资源:
	1. 为什么会有这样的问题:
	优雅的 REST 风格的资源URL 不希望带 .html 或 .do 等后缀
	若将 DispatcherServlet 请求映射配置为 /, 
	则 Spring MVC 将捕获 WEB 容器的所有请求, 包括静态资源的请求, SpringMVC 会将他们当成一个普通请求处理, 
	因找不到对应处理器将导致错误。
	2. 解决: 在 SpringMVC 的配置文件中配置 <mvc:default-servlet-handler/>
-->
<style type="text/css">
	.sty{
		padding-left: 150px;
	}
	
	a:{font-size:16px}
	a:link {color: #8B8989; text-decoration:none;} /* 未访问：蓝色、无下划线 */
	a:active:{color: #8DB6CD; } /* 激活：红色 */
	a:visited {color:purple;text-decoration:none;} /* 已访问：紫色、无下划线 */
	a:hover {color: #8B8989; text-decoration:underline;} /* 鼠标移近：红色、下划线 */
	
	.dv{
		margin-left:150px;
	}
</style>
</head>
<script src="<%=request.getContextPath()%>/js/jquery-3.3.1.js"></script>
<script type="text/javascript">
	function bb(t) {
		var href = $(t).attr("href");
		document.getElementById("loadFilePath").value = href;
		$("#download").submit();
	}

	//搜索查询
	function Search(){
	    var pageSize6 = $("#pageSize").val();
	    var page6 = 1;
	    var pageCount6 = 0;
	    var pageSelect6 = 1;
	    var select6 = $("#va").val();
	    Data(page6, pageSize6, pageCount6, pageSelect6, select6);
	}
	function Page(page3, pageSize3, pageCount3, pageSelect3, select3){
	    if(0 == page3){
	        page3 = 1;
	    }
	    if(0 == pageSelect3){
	        pageSize3 = $("#pageSize").val();
	    }
	    if(1 == pageSize3){
	        pageSize3 = $("#pageSize").val();
	    }
	    select3 = $("#va").val();
	    Data(page3, pageSize3, pageCount3, pageSelect3, select3);
	}
	
	// 初始化(搜索)分页查询
	function Data(page4, pageSize4, pageCount4, pageSelect4, select4){
	    var page2 = page4;
	    if("" == page2){
	        page2 = 1;
	    }
	    var pageSize2 = pageSize4;
	    if("" == pageSize2 || null == pageSize2){
	        pageSize2 = 10;
	    }
	    select4 = $("#va").val();
	    var select2 = select4;
	    var b='\\\\';
	    
	    $.ajax({
	        type:"get", //请求方式     对应form的  method请求
	        url:"<%=request.getContextPath()%>/testList", // 请求路径  对应 form的action路径
	        cache: false,  //是否缓存，false代表拒绝缓存
	        data:{"page":page2,"pageSize":pageSize2, "select":select2},  //传参
	        dataType: "json",   //返回值类型 
	        success:function(data){
	            if("1" == data){
	            }else{
	                var list = data.page.pt;
	                var html = "";
	                $("#t_body").empty();
	                for(var i in list){
	                    html += "<a href='"+list[i].fileUrl+"' onclick='bb(this)'>" + list[i].fileName + "</a>&nbsp;&nbsp;&nbsp;&nbsp;" +
	                    "<span>"+ list[i].fileSize +"</span><br />" + 
	                    "<span>"+ list[i].fileContent +"</span><br /><hr>"
	                    ;
	                }
	                $("#t_body").append(html);
	                var page1 = "";
	                page1 = "总记录数:<span>" + data.page.pageSizeCount + "</span>&nbsp;&nbsp;&nbsp;&nbsp;" +
	                        "总页数:<span>" + data.page.pageCount + "</span>&nbsp;&nbsp;&nbsp;&nbsp;" +
	                        "当前页:<span id='inPage'>" + data.page.page + "</span>&nbsp;&nbsp;&nbsp;&nbsp;" +
	                        "每页记录数:<select name='pageSize' id='pageSize' onchange='Page(" + data.page.page + ", 1, " + data.page.pageCount + ",0, " + '' + ")'>" +
	                        "<option value='5' >5</option>" + 
	                        "<option value='10' >10</option>" +
	                        "<option value='20' >20</option>" +
	                        "<option value='50' >50</option>" +
	                        "</select><br />"
	                ;
	                if(data.page.page > 1){
	                    page1 = page1 + "<button onclick='Page(1," + pageSize2 + ", " + data.page.pageCount + ",1, " + '' + ")'>首页</button>&nbsp;&nbsp;";
	                    page1 = page1 + "<button onclick='Page(" + (data.page.page-1) + "," + pageSize2 + ", " + data.page.pageCount + ",1, " + '' + ")'>上一页</button>&nbsp;&nbsp;";
	                }
	                for (var c = 1; c < data.page.pageCount+1; c++) {
	                    if(c == data.page.page){
	                        page1 = page1 + "<span>"+ c + "</span>&nbsp;&nbsp;";
	                    }else{
	                        
	                        page1 = page1 + "<button onclick='Page(" + c + "," + pageSize2 + ", " + data.page.pageCount + ",1, " + '' + ")'>"+ c + "</button>&nbsp;&nbsp;";
	                    }
	                }
	                if(data.page.page < data.page.pageCount){
	                    page1 = page1 + "<button onclick='Page(" + (data.page.page+1) + "," + pageSize2 + ", " + data.page.pageCount + ",1, " + '' + ")'>下一页</button>&nbsp;&nbsp;";
	                    page1 = page1 + "<button onclick='Page(" + data.page.pageCount + "," + pageSize2 + ", " + data.page.pageCount + ",1, " + '' + ")'>尾页</button>&nbsp;&nbsp;";
	                }
	                
	                $("#divPage").html(page1);
	                $("#pageSize").val(pageSize2);// 设置下拉框默认显示值
	                
	            }
	        },
	        error: function(XMLHttpRequest, textStatus, errorThrown) {
	            alert(XMLHttpRequest.status);
	            alert(XMLHttpRequest.readyState);
	            alert(textStatus);
	        }
	    });
	}

</script>
<body background="<%=request.getContextPath()%>/jsp/banner.jpg">
	<form action="user" method="POST" id="user">
		<input type="hidden" name="filePath" id="filePath" />
	</form>
	<form action="download" method="POST" id="download">
		<input type="hidden" name="loadFilePath" id="loadFilePath" />
	</form>

	<c:if test="${!empty requestScope.error}">
		<font color="#8B8989" class="sty">${error }</font>
	</c:if>
	
	<c:if test="${empty requestScope.fileList1 && empty requestScope.fileList && empty requestScope.error }">
		<font color="#8B8989" class="sty">请按"回车"键发起检索</font>
	</c:if>

	<input type="hidden" value="${value}" id="va">
	<div id="t_body">
    </div>
    <div id="divPage">
    </div>
	
	<c:if test="${!empty value}">
		<script>
			Search();
		</script>
	</c:if>
</body>
</html>

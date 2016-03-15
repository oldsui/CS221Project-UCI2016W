<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
		<style>
			p {
    			text-align: center;
    			color: black;
			} 
			form {
    			text-align: center;
    			color: black;
			} 
		</style>
	
  </head>
  
  <body>
    <p>ICS Search Engine: <p> <br>
    <form method = "post" name = "search" action = "SearchServlet">  	
    	Search: <input type = "text" name = "query"/>   	
    	<input type = submit name = "GO" value = "GO"/>   
    	<input type = submit name = "NDCG_TEST" value = "NDCG_TEST"/>
    </form>
  </body>
</html>

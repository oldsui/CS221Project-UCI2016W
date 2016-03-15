<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core"  %> 

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Display</title>
    
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
		</style>


  </head>
  
  <body>  
    <p>NDCG Test: </p>
    <br>
    <td> Ideal NDCG sum of the 10 queres is 50</td><br>
    <td> Our NDCG sum of the 10 queries is <span name="ndcgTotal" >${sessionScope.totalNDCG }</span></td><br><br>
    <c:forEach var="i" begin="0" end="9">
   		<td>Query: <span name="query" >${sessionScope.googleQueries[i] }</span></td><br>
   		<td>Ideal top 5 urls from Google:</td><br>	
		<c:forEach var="idealUrl" items = "${sessionScope.idealUrlArr[i] }">
			<td>Url from Google: <span name="idealUrl" >${idealUrl }</span></td><br>
   		</c:forEach>
   		<br>
   		<td>NDCG:</td><br>	
		<c:forEach var="j" begin="0" end="4">	
				<td><span name="ndcg" >${sessionScope.NDCG[i][j] }, </span></td>
		</c:forEach>
		<td>NDCG Sum: <span name="ndcgSum" >${sessionScope.NDCG[i][5] }</span></td><br><br>
   		
		<td>Top 5 urls retrieved:</td><br>	
		<c:forEach var="page" items = "${sessionScope.myResultUrls[i] }">
			<td >url: <a href="${page.url} ">${page.url}</a></td><br>
			<%-- 
			<td>TotalScore: <span name="totalScore" >${page.totalScore }</span></td><br>
			<td>TFIDF: <span name="tfidf" >${page.tfidf }</span></td><br>
			<td>PageRank: <span name="pr" >${page.pr }</span></td><br>
			<td>TitleScore: <span name="titleScore" >${page.titleScore }</span></td><br>
			--%>
		</c:forEach>
		<br>
		
		
	</c:forEach>
         
    <br>
  </body>
</html>

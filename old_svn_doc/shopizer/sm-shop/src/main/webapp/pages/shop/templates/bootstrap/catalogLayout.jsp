<%
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<c:set var="lang" scope="request" value="${requestScope.locale.language}"/> 
 
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-CN"> 
     <head>
       <meta charset="utf-8">
       <meta http-equiv="X-UA-Compatible" content="IE=edge">
       <meta name="viewport" content="width=device-width, initial-scale=1">
       <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
 			<title><c:out value="${requestScope.PAGE_INFORMATION.pageTitle}" /></title>
 			<meta name="description" content="<c:out value="${requestScope.PAGE_INFORMATION.pageDescription}" />">
 			<meta name="author" content="<c:out value="${requestScope.MERCHANT_STORE.storename}"/>">
			
            <jsp:include page="/pages/shop/templates/bootstrap/sections/shopLinks.jsp" />
 	</head>
 
 	<body>
	<div id="pageContainer" class="container">
				<tiles:insertAttribute name="header" ignore="true"/>

				<tiles:insertAttribute name="navbar" ignore="true"/>

				<tiles:insertAttribute name="body" ignore="true"/>

				<tiles:insertAttribute name="footer" ignore="true"/>
	</div>
	<!-- end container -->
	   <jsp:include page="/pages/shop/templates/bootstrap/sections/jsLinks.jsp" />

 	</body>
 
 </html>
 

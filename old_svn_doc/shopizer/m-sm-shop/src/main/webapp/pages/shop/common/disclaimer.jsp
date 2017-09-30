<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
 request.setCharacterEncoding("UTF-8");
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>
<%@ taglib uri="/WEB-INF/shopizer-functions.tld" prefix="display" %> 

<link href="<c:url value="/resources/templates/bootstrap3/css/style.css" />" rel="stylesheet" type="text/css">
<html xmlns="http://www.w3.org/1999/xhtml">  
 
     <head>
        	 	<meta charset="utf-8">
    			<title><c:out value="${requestScope.PAGE_INFORMATION.pageTitle}" /></title>
				<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
				<meta name="viewport" content="width=device-width, maximum-scale=1, initial-scale=1, user-scalable=0">
    			<meta name="keywords" content="<c:out value="${requestScope.PAGE_INFORMATION.pageKeywords}" />">
    			<meta name="description" content="<c:out value="${requestScope.PAGE_INFORMATION.pageDescription}" />">
    			<meta name="author" content="<c:out value="${requestScope.MERCHANT_STORE.storename}"/>">

				<!-- mobile settings -->

                
                 <jsp:include page="/pages/shop/templates/bootstrap3/sections/shopLinks.jsp" />
 
                 <br>
                 <br>
<div class="box">
	<h2 style="text-align:center">您的浏览器版本太低，请升级您的浏览器版本，谢谢！</h2>
   	
   	 友情提示：
   	 
   	  	<li><a  href='http://rj.baidu.com/soft/detail/14744.html?ald'>谷歌浏览器下载</a></li>
   	  	<li><a  href='http://se.360.cn/'>360浏览器下载</a></li>
   	  	<li><a  href='http://liulanqi.baidu.com/?sid=11100016'>百度浏览器下载</a></li>
   	  	<li><a  href='http://browser.qq.com/?adtag=SEM1'>QQ浏览器</a></li>
   	  	<li><a  href='http://windows.microsoft.com/zh-cn/internet-explorer/download-ie'>IE浏览器下载</a></li>
   	 
</div>
<div style="text-align: center;"><span class="nav-ezybio-copyright nav-ezybio-font"> Copyright<s:message code="label.copyright.sigh" text="All Rights Reserved"/> 2015-2016 www.bettbio.com <s:message code="all.rights.reserved" text="All Rights Reserved"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="nav-ezybio-copyright nav-ezybio-font"><s:message code="label.bettbio.baknum" text="buyer" /></span></div>


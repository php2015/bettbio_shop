<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
 request.setCharacterEncoding("UTF-8");
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>
<%@ taglib uri="/WEB-INF/shopizer-functions.tld" prefix="display"%>

<link
	href="<c:url value="/resources/templates/bootstrap3/css/style.css" />"
	rel="stylesheet" type="text/css">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta charset="utf-8">
<title><c:out value="${requestScope.PAGE_INFORMATION.pageTitle}" /></title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width, maximum-scale=1, initial-scale=1, user-scalable=0">
<meta name="keywords"
	content="<c:out value="${requestScope.PAGE_INFORMATION.pageKeywords}" />">
<meta name="description"
	content="<c:out value="${requestScope.PAGE_INFORMATION.pageDescription}" />">
<meta name="author"
	content="<c:out value="${requestScope.MERCHANT_STORE.storename}"/>">


<style type="text/css">
.icoStyle {
	color: #fafafa;
	font-family: 微软雅黑;
	padding: 30px 40px;
}

.neikuang {
	border-radius: 6px;
	border: 2px solid #FAFAFA;
	line-height: 30px;
	font-size: 22px;
	display: inline-block;
}

.browseName {
	margin-top: 8.6%;
	font-size: 20px;
	color: #fafafa;
}


.download {
	margin-top: 10%
}


    
    html,body{
    height:100%;
    margin:0px; 
   
}

</style>




<div style="height:100%">
<div style="position:absolute;font-size: 30px;margin-top: 2%;width:100%;z-index:999;text-align: center;">
		<span style="color:#fafafa;">为保证最佳浏览效果，推荐使用 360极速模式，IE10+,
			Chrome</span>
	</div>
	<div style="position:absolute;width:100%;z-index:999;text-align: center;bottom:2%;">
			<div style="font-size: 16px;color: #fafafa;">请点击 &nbsp;<span><img src="<c:url value="/resources/img/shandian.png" />"></span>&nbsp;图标进行切换到极速模式 </div>
			
			<div  ><img style=width:32% src="<c:url value="/resources/img/banbentaidi1.png" />" /></div>
				<a style="font-size: 16px;color: #fafafa;margin-top: 20PX;" href="/sm-shop/shop/">切换极速模式后，请返回首页</a>
			</div>			
<table style="width:100%;height:100%;z-index:-1;" border="0" cellpadding="0" cellspacing="0"  >
	<tr>
		<td align="center"  style="width:20%;height:100%;background-color:#f19a2a;"> 
			<div class="imgheight">
				<img  src="<c:url value="/resources/img/huohu.png" />" />
				<div class="browseName" >Firefox</div>
				<div class="download" >
					<span class="neikuang"><a href=http://www.firefox.com.cn/ class="icoStyle">下载</a></span>
					<style>a{text-decoration:none}</style>
				</div>
			</div>
		</td>
		<td align="center"  style="width:20%;height:100%;background-color:#f2b635;"> 
			<div class="imgheight">
				<img   src="<c:url value="/resources/img/guge.png" />" />
				<div class="browseName" >Chrome</div>
				<div class="download" >
					<span class="neikuang"><a href='http://rj.baidu.com/soft/detail/14744.html?ald' class="icoStyle">下载</a></span>
					<style>a{text-decoration:none}</style>
				</div>				
			</div>		
		</td>
		<td align="center"  style="width:20%;height:100%;background-color:#5abf5b;"> 
			<div class="imgheight">
				<img  src="<c:url value="/resources/img/360.png" />" />
				<div class="browseName" >360极速模式</div>
				<div class="download" >
					<span class="neikuang"><a  href='http://se.360.cn/' class="icoStyle">下载</a></span>
					<style>a{text-decoration:none}</style>
				</div>	
			</div>
		</td>
		<td align="center"  style="width:20%;height:100%;background-color:#00caff;"> 
			<div class="imgheight">
				<img  src="<c:url value="/resources/img/IE9.png" />" />
				<div class="browseName" >Internet explorer</div>
				<div class="download" >
					<span class="neikuang"> 
					<a href='http://windows.microsoft.com/zh-cn/internet-explorer/download-ie'  class="icoStyle">下载</a></span>
					<style>a{text-decoration:none}</style>
				</div>
			</div>
		</td>
		<td align="center"  style="width:20%;height:100%;background-color:#f25648;"> 
			<div class="imgheight" >
				<img  src="<c:url value="/resources/img/oupeng.png" />" />
				<div class="browseName" >Opera</div>
				<div class="download" >
					<span class="neikuang"><a href='http://www.oupeng.com/tor' class="icoStyle">下载</a></span>
						<style>a{text-decoration:none}</style>
		</div>
	</div>
		</td>
	</tr>
</table>

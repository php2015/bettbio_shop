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
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %> 
 <link href="<c:url value="/resources/templates/bootstrap3/css/path-css.css" />" rel="stylesheet" type="text/css">
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<style>
.center {
 width: auto;
 display: table;
 margin-left: auto;
 margin-right: auto;
}
</style>
<div  style="padding:1%">
 	<div class="box">
		<div style="margin-left:-120px">
			<img src="<c:url value="/resources/img/4.png"/>"
				style="width: 100%;padding-left: 176px" />
		</div>
	</div>
	<div style="padding:3%" class="center">
		<div style="width: 50px;height: 50px;float: left;margin-right:5px">
			<img src="<c:url value='/resources/img/pdSuccess.png'/>" width="50" height="50"/>
		</div>
		<div style="width: 285px;margin-top:2px;line-height:20px; ">
			<span style="color:#4285f4">新密码设置成功 </span><br/>
			<span style=" font-weight:bold">请牢记您设置的密码&nbsp;&nbsp;&nbsp;&nbsp;<a href="${pageContext.request.contextPath}/shop" style="color:#4285f4">返回首页</a></span>
		</div
	</div>
</div>
</div>
		
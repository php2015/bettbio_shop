<%
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
String type = (String)request.getAttribute("c");
String ptype = request.getParameter("c");
type = type==null?"":type;
ptype = ptype==null?type:ptype;
//System.out.println(type+"-----"+ptype);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>
<%@ taglib uri="/WEB-INF/shopizer-functions.tld" prefix="display" %> 

<!-- TT Typeahead js files -->
<!-- script src="<c:url value="/resources/templates/bootstrap3/js/bloodhound.min.js" />"></script>
<script src="<c:url value="/resources/templates/bootstrap3/js/typeahead.bundle.min.js" />"></script-->
<link href="<c:url value="/resources/templates/bootstrap3/css/tabs.css" />" rel="stylesheet" type="text/css">
<script src="<c:url value="/resources/templates/bootstrap3/js/tabs.js" />"></script>

<c:set var="req" value="${request}" />
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<div class="head-navbar-left" style="height:60px;">
	<div class="container-fluid">
		<div class="pull-left" id="nav-image"><a href="/sm-shop/"><img style="height:56px;" src="<c:url value="/resources/img/biglogo.png" />"></a></div>
		<div class="form-group">
	    <div class="col-md-10" >
	    <c:set var="ptype" value="<%=ptype %>"/>
	    <c:if test="${ ptype=='customer'}">
	     <div class="col-md-2" style="width:500px;margin-top: 32px;size: 16px"><strong><span style="font-size:20px;color:#333333;">欢迎买家用户注册</span></strong><%-- <strong>${param.c=='customer'?"欢迎您进入普通用户注册":"欢迎您进入卖家注册"}</strong> --%></div>
	     <div class="pull-right" style="margin-top: 35px;size: 16px">
	     <a href="<c:url value="storeinsertAdmin.html?c=store"/>" style="color:#4285f4">
	     <strong style="color:#4285f4;font-size:16px;"> 切换至${ptype=='customer'?'卖家用户':'买家用户'}注册
	       <img src="<c:url value="/resources/ad/jiantou.png"/>" style="width:16px;height:16px;"/></a></c:if>
	     <c:if test="${ptype=='store'}">
	     <div class="col-md-2" style="width:500px;margin-top: 32px;size: 16px"><strong><span style="font-size:20px;color:#333333;">欢迎卖家用户注册</span></strong><%-- <strong>${param.c=='customer'?"欢迎您进入普通用户注册":"欢迎您进入卖家注册"}</strong> --%></div>
	     <div class="pull-right" style="margin-top: 35px;size: 16px">
	     <a  href="<c:url value="registration.html?c=customer"/>" style="color:#4285f4">
	     <strong style="color:#4285f4;font-size:16px;"> 切换至${ptype=='customer'?'卖家用户':'买家用户'}注册
	       <img src="<c:url value="/resources/ad/jiantou.png"/>" style="width:16px;height:16px;"/></a>
	     </c:if>
	     </strong></div>
	   </div>
	 </div>
	</div>
</div>
<div style="width:100%;margin-top:12px;border: 1px solid #eeeeee;"></div>


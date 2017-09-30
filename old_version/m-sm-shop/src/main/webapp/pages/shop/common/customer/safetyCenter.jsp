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
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<!-- requires functions.jsp -->
<script src="<c:url value="/resources/js/shop-customer.js" />"></script>
<script src="<c:url value="/resources/js/check-functions.js" />"></script>
<style type="text/css">
.line {
	
	border-bottom: 1px solid #ccc;
	
	line-height:25px;
}
.spaninfo {
	width:50px;
	height:30px;
	color:green;
	font-size:20px;
}
.line strong {
	font-size:20px;
}
</style>

<div class="container-fluid"> 
	<div class="customer-box row">
	<div class="container-fluid" style="padding-bottom:10px;">
				<div class="pull-left" style="font-size:15px;padding-top:10px;"><s:message code="menu.safetyCenter" text="Safety Center"/></div>
			<div class="pull-right"><a href="#" onClick="javascript:location.href='<c:url value="/shop/customer/dashboard.html" />';"><span aria-hidden="true" class="carticon fa fa-reply fa-2x icon icon-style" style="background:#1E90FF;"></span></a></div>									
			</div>			
	<div class=" box main-padding-lr">
		<div class="line row" style="padding-bottom:10px;">
			<div class="pull-left" >
				<span class="glyphicon glyphicon-ok spaninfo" aria-hidden="true"></span><strong><s:message code="label.customer.safety.pw" text="Safety Password"/></strong>
			</div>			
			<div class="pull-right">
				<a class="btn btn-default" href='<c:url value="/shop/customer/password.html"/>'><s:message code="label.generic.modify" text="Modify"/></a>
			</div>
			<div class="col-xs-12">
				<s:message code="label.customer.safety.pw.info" text="change your password at period"/>
			</div>
		</div>
		
		<div class="line row" style="padding-top:10px;">
			<div class="pull-left" >
				<span class="glyphicon glyphicon-ok spaninfo" aria-hidden="true"></span><strong><s:message code="label.customer.safety.email" text="Safety Email"/></strong>
			</div>			
			<div class="pull-right">
				<a class="btn btn-default" href='<c:url value="/shop/customer/email.html"/>'><s:message code="label.generic.modify" text="Modify"/></a>
			</div>
			<div class="col-xs-12">
				<s:message code="label.customer.safety.email.info" text="change your Email"/><span style="font-weight:bold">${CUSTOMER.emailAddress }</span>
			</div>
		</div>
		
		<div class="line row" style="padding-top:10px;padding-bottom:10px;">
			<div class="pull-left" >
				<span class="glyphicon glyphicon-ok spaninfo" aria-hidden="true"></span><strong><s:message code="label.customer.safety.phone" text="Safety Phone"/></strong>
			</div>			
			<div class="pull-right">
				<a class="btn btn-default" href='<c:url value="/shop/customer/phone.html"/>'><s:message code="label.generic.modify" text="Modify"/></a>
			</div>
			<div class="col-xs-12">
				<s:message code="label.customer.safety.phone.info" text="change your Phone"/><span style="font-weight:bold">${CUSTOMER.phone }</span>
			</div>
		</div>
	</div>
	</div>					
</div>
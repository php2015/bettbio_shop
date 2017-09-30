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
<div  style="padding:1%">
 	<div class="box">
		<div >
		<nav>
			<ol class="cd-multi-steps text-bottom count ">
				<li class="font-normal"><em class="font-normal"><s:message code="label.custmoer.write" text="Write" /><s:message code="label.generic.amount" text="count" /></em></li>
				<li class="font-normal" ><em class="font-normal"><s:message code="label.custmoer.virity" text="count" /></em></li>
				<li class="font-normal"><em class="font-normal"><s:message code="label.generic.set" text="Write" /><s:message code="label.generic.newpassword" text="count" /></em></li>
				<li class="current font-normal"><em class="font-normal"><s:message code="label.custmoer.password.done" text="count" /></em></li>
			</ol>
		</nav>
	</div>
	<div style="padding:3%" class="text-center">
		<img src="<c:url value="/resources/img/icon_success.png"/>" width="40"/>&nbsp;<strong><s:message code="message.success" text="Success" /></strong>
	</div>
</div>
</div>
		
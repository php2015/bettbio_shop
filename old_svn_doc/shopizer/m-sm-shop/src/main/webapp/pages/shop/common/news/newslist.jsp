<%
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<script src="<c:url value="/resources/js/findCommon.js" />"></script>
<style>
.title {
	color:green;
	font-size:16px;
	font-weight:bold;
	line-height:25px;
}
.brief {
	margin-top:10px;
	line-height: 22px;
	color:#ccc;
}
.h4 a{
	color:#0078B6;
}
.h4 a:hover{
	color:#FF6C00;
}
.list img {
	height: auto;
}
</style> 	 
<script>
$(document).ready( function() {
});		

</script>
<!-- begin body -->
<div class="container-fluid">
<c:set var="findAction" value="${pageContext.request.contextPath}/shop/news/list.html" scope="request"/>
<form:form method="post" action="${requestScope.findAction}" id="ezybioForm" commandName="criteria">
<input type="hidden" name="page" id="page" value="1"/>
<c:forEach items="${newslist}" var="news" varStatus="status">
	<div class="row list" style="border-bottom:1px solid #ccc;padding-top:5px;">
		<div class="col-xs-3 col-md-4" style="padding-left:1%;padding-right:1%;">
			<c:choose>
				<c:when test="${not empty news.image}"><img style="border:1px solid #eee;padding:1px;width:100%;" src='${news.image }' alt="${news.linkText }"/></c:when>
				<c:otherwise><img style="border:1px solid #eee;width:100%;" src='<c:url value="/resources/img/bettbio.png"/>' alt="${news.linkText }"/>	</c:otherwise>
			</c:choose>
		</div>
		<div class="col-xs-9 col-md-8">
			<span class="h4 title" style="color:green!important;cursor:pointer"><a href='<c:url value="/shop/news/view/${news.id }.html"/>' target="blank">${news.linkText }</a></span><br/>
			<span style="color:#ccc" class="pull-left"><s:message code="label.news.publishdt" text="Pulish Date:"/><fmt:formatDate value="${news.publishdt }" pattern="yyyy-MM-dd"/></span>
			<span style="color:#ccc" style="margin-left:50px;" class="pull-right"><s:message code="label.news.source" text="Pulish Source:"/>${news.type.name }</span><br/>
			<div class="brief"><a href='<c:url value="/shop/news/view/${news.id }.html"/>' target="blank">${news.summary }</a></div>
		</div>
	</div>
</c:forEach>
<div class="row" style="padding-top:7px;">
<c:set var="paginationData" value="${paginationData}" scope="request"/>
<jsp:include page="/common/paginationFind.jsp"/>  
</div>
</form:form>
</div><!-- end body -->          
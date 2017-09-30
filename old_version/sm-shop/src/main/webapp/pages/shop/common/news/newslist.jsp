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
}
.brief {
	margin-top:10px;
	line-height: 25px;
}
.h3 a{
	color:#0078B6;
}
.h3 a:hover{
	color:#FF6C00;
}
.row img {
	height: auto;
	max-width: 100%;
}
</style> 	 
<script>
$(document).ready( function() {
});		

var count=0;
</script>
<!-- begin body -->
<div class="container-fluid" style="margin-top:20px;">
	<div class="row">
		<div class="col-xs-1 col-md-1"></div>
		<div class="col-xs-10 col-md-10">
			<c:set var="findAction" value="${pageContext.request.contextPath}/shop/news/list.html" scope="request"/>
			<form:form method="post" action="${requestScope.findAction}" id="ezybioForm" commandName="criteria">
			<input type="hidden" name="page" id="page" value="1"/>
			<c:forEach items="${newslist}" var="news" varStatus="status">
			<div class="row" style="color:#888;border:1px solid #ccc;border-bottom:0px;padding:7px;">
				<div class="col-md-2">
					<div style="max-height:130px;">
					<c:choose>
						<c:when test="${not empty news.image}"><img style="width:160px" src='${news.image }' alt="${news.linkText }"/></c:when>
						<c:otherwise><img style="width:160px;" src='<c:url value="/resources/img/bettbio.png"/>' alt="${news.linkText }"/>	</c:otherwise>
					</c:choose>
					</div>
				</div>
				<div class="col-md-9">
					<span class="h3" style="color:green!important;cursor:pointer"><a href='<c:url value="/shop/news/view/${news.id }.html"/>' target="blank">${news.linkText }</a></span><br/>
					<span><s:message code="label.news.publishdt" text="Pulish Date:"/><fmt:formatDate value="${news.publishdt }" pattern="yyyy-MM-dd"/></span>
					<span style="margin-left:50px;"><s:message code="label.news.source" text="Pulish Source:"/>${news.type.name }</span><br/>
					<div class="brief"><a href='<c:url value="/shop/news/view/${news.id }.html"/>' target="blank">${news.summary }</a></div>
				</div>
				<div class="col-md-1" style="vertical-align: middle;">
					
				</div>
			</div>
			</c:forEach>
			<div class="row" style="border-top:1px solid #ccc;padding-top:7px;">
			<c:set var="paginationData" value="${paginationData}" scope="request"/>
			<jsp:include page="/common/paginationFind.jsp"/>  
			</div>
			</form:form>
	</div>
	<div class="col-xs-1 col-md-1"></div>
	</div>
</div><!-- end body -->          
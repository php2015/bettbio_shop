
<%
	response.setCharacterEncoding("UTF-8");
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", -1);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<script type="text/javascript" charset="utf-8">
	function findByStatus(status) {
		$("#status").val(status)
		$('#ezybioForm').submit();
	};
	function doPage(page) {
		$('#page').val(page);
		$('#ezybioForm').submit();
	}
</script>
<style>
</style>


<div class="customer-box" style="min-height:400px">
	<div class="pull-right">
		<a href="#"
			onClick="javascript:location.href='<c:url value="/shop/customer/dashboard.html" />';"><span
			aria-hidden="true" class="carticon fa fa-reply fa-2x icon icon-style"
			style="background:#1E90FF;"></span></a>
	</div>
	<div class="pull-left"
		style="font-size:17px;padding-top:10px;margin-left: 5%;">
		<s:message code="menu.member-point-see" text="member-point-see" />
	</div>
	<br> <br>
	<HR style="FILTER: alpha(opacity=100,finishopacity=0,style=3);"
		width="98%" color=#987cb9 SIZE=3>
	<c:forEach items="${emberPointList}" var="emberPoin">
		<div class="tab"
			style="position:relative;width: 96%;border:none;margin-left:3%;border-radius:6px;min-height:120px;margin-top: 15px;background-color: #33CCFF;">
			<!-- <div style="position:absolute;width:35%;border: 1px solid red;min-height:120px;border-left: none;border-top:none;border-bottom: none; float:left;"></div -->
			<div
				style="position:absolute;width:30%;color: white;font-size: 13px;margin-top: 15%;margin-left: 46%;font-weight:bold;font-family:宋体";>${emberPoin.type }</div>
			<div
				style="position:absolute; width:25%;margin-top: 4%;margin-left:8%;font-weight:bold;font-family:仿宋;font-size:60px;color: white;">${emberPoin.value }</div>
			<div
				style="position:absolute; width:30%;margin-top: 12%;margin-left: 76%;font-size: 18px;color:yellow; font-weight:bold;transform:rotate(13deg);font-family:宋体">
				<c:choose>
					<c:when test="${emberPoin.statas =='0'}">
						<s:message code="label.customer.ACCOUNT" text="ACCOUNT" />
					</c:when>
					<c:when test="${emberPoin.statas =='1'}">
						<s:message code="label.customer.NOTACCOUNT" text="NOTACCOUNT" />
					</c:when>
					<c:when test="${emberPoin.statas =='2'}">
						<s:message code="label.customer.REVOKE" text="REVOKE" />
					</c:when>
				</c:choose>
			</div>
			<div
				style="position:absolute; width:50%;margin-left:42%;margin-top: 22%;color: white;font-size: 14px;">${emberPoin.dateValid }</div>
		</div>
	</c:forEach>
	<HR style="FILTER: alpha(opacity=100,finishopacity=0,style=1;"
		width="98%" color=#FCFCFC SIZE=1>

	<div style="padding-top: 5px">
		<div style="float:right;margin-top:40px;">
			<span class="p-title-text"> <c:if
					test="${not empty emberPointList }">
					<s:message code="label.entitylist.paging"
						arguments="${(paginationData.offset)};${paginationData.countByPage};${paginationData.totalCount}"
						htmlEscape="false" argumentSeparator=";" text="" />
					<script>
						cuurentPage = ${paginationData.currentPage};
					</script>
				</c:if>
			</span>
		</div>
		<div style="float:right;margin-right:-100px;">
			<c:set var="pagesAction"
				value="${pageContext.request.contextPath}/shop/customer/memberPoint.html"
				scope="request" />
			<c:set var="paginationData" value="${paginationData}" scope="request" />
			<jsp:include page="/common/pagination.jsp" />
		</div>
	</div>
</div>
<form:form method="get"
	action="${pageContext.request.contextPath}/shop/customer/memberPoint.html"
	id="ezybioForm" commandName="criteria" tyle="display:none">
	<fieldset>
		<input type="hidden" name="page" id="page" value="1" /> <input
			type="hidden" path="status" name="status" id="status"
			<c:if test="${not empty criteria.status}"> value="${criteria.status}" </c:if> />
	</fieldset>
</form:form>


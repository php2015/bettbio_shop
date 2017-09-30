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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>


<div class="customer-box" >				
	        	
              <c:if test="${not empty customerOrders && not empty customerOrders.ezybioOrder}">
                     
              	<div >

			<!-- HISTORY TABLE -->
			<table class="table table-bordered">
				<!-- table head -->
				<jsp:include page="/common/orderTitlel.jsp"/>
				<!-- /HISTORY TABLE -->
				<tbody>
				<c:set var="customerStoreOrder" value="${pageContext.request.contextPath}/shop/customer/order.html" scope="request"/>
				<jsp:include page="/common/orderDetail.jsp"/>
					
				</tbody>
			</table>
			<div class="pull-left">
			
					<span class="p-title-text">
					<c:if test="${not empty customerOrders && not empty customerOrders.ezybioOrder}">
					
						<s:message code="label.entitylist.paging"
					       arguments="${(paginationData.offset)};${paginationData.countByPage};${paginationData.totalCount}"
					       htmlEscape="false"
					       argumentSeparator=";" text=""/>
					<script>
						cuurentPage=${paginationData.currentPage};
					</script>
					</c:if>  </span>
				
			</div>
			<c:set var="pagesAction" value="${pageContext.request.contextPath}/shop/customer/orders.html" scope="request"/>
			<c:set var="paginationData" value="${paginationData}" scope="request"/>
			<jsp:include page="/common/pagination.jsp"/>

		</div>
	</c:if>
	<c:if test="${empty customerOrders || empty customerOrders.ezybioOrder}">
		<div class="container-fluid">
		<div class="row">
			<div class="col-sm-12"><h3><s:message code="message.no.items" text="No Items"/></h3></div>
			</div>
		</div>
	</c:if>
		<br><br>
</div>				
				<form:form method="post" action="${pageContext.request.contextPath}/shop/customer/orders.html" id="ezybioForm" commandName="criteria" tyle="display:none">
	<fieldset>
	<input type="hidden" name="page" id="page" value="1"/>
	<input type="hidden" name="status" id="status" <c:if test="${not empty criteria.status}"> value="${criteria.status}" </c:if>/>
	<input type="hidden" name="findName" id="findName" <c:if test="${not empty criteria.findName}"> value="${criteria.findName}" </c:if>/>
	<input type="hidden" name="beginDatePurchased" id="beginDatePurchased" <c:if test="${not empty criteria.beginDatePurchased}"> value="${criteria.beginDatePurchased}" </c:if>/>
	<!-- input type="hidden" name="splitSubOrder" id="splitSubOrder" /-->
	</fieldset>
</form:form>
			  

			

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
<!-- From src\main\webapp\pages\shop\common\customer\orders.jsp -->
<c:set var="ordersAction" value="${pageContext.request.contextPath}/shop/customer/orders.html"/>
<script src="<c:url value='/resources/js/orders.js' />?v=<%=new java.util.Date().getTime()%>"></script>	
<script src="<c:url value="/resources/js/bootstrap/bootstrap-datepicker.js" />"></script>
<script type="text/javascript" charset="utf-8">
var cuurentPage=1;
var subOrderid;
function addorder(tr,data){
	tr.id=data.id;	
	var tdname=	tr.insertCell(0);
	tdname.innerHTML =data.name;
	tdname.width= "80%";
	tr.insertCell(1).innerHTML=data.quantity;
	return tr;
}
function setStatus(strStatus){
	switch(strStatus)
	{
		case "SHIPPED" :
			$("#status_"+subOrderid).html('<s:message code="menu.order" text="Order"/><s:message code="label.entity.status" text="Status"/>:<s:message code="label.order.SHIPPED" text="Status"/>');
		break;
	}
}
var findholder ='<s:message	code="label.generic.find.holde" text="holde" />';
</script>
<!--最外层div  -->

<!--最外层div  -->
<div class="out">

	<div class="customer-box" >
		<div class="" style="height: 44px;">	
			<ul class="nav nav-pills pull-left">		
				<li class="dropdown"> 
					<c:choose>
						<c:when test="${ empty criteria.beginDatePurchased || criteria.beginDatePurchased=='3'}">
							<a data-toggle="dropdown" class="dropdown-toggle" href="#" id="yearStatus">
								<s:message code="label.generic.year.three" text="thress"/><s:message code="label.order.title" text="Order"/><span class="caret"></span>
							</a>
						</c:when>			  
						<c:otherwise>
							<a data-toggle="dropdown" class="dropdown-toggle" href="#" id="batchStatus">
								${criteria.beginDatePurchased}<s:message code="label.generic.year" text="Order"/><s:message code="label.generic.year.this" text="Order"/>
								<span class="caret"></span>
							</a>
						</c:otherwise>
					</c:choose>	  
					<ul class="dropdown-menu" aria-labelledby="yearStatus">
						<li>
							<a href="javascript:void(0);" onclick="findByYear('3')">
								<s:message code="label.generic.year.three" text="thress"/><s:message code="label.order.title" text="Order"/>
							</a>
						</li>
						<c:if test="${not empty years}">
							<li class="divider"></li>
							<c:forEach items="${years}" var="year" varStatus="yaerStatus">
								<li><a href="javascript:void(0);" onclick="findByYear('${year}')">${year}<s:message code="label.order.title" text="Order"/></a></li>
							</c:forEach>
						</c:if>
					</ul>
				</li>	
			</ul>  

			<ul class="nav nav-pills pull-right form-inline">
				<li <c:if test="${empty cstatus}"> class="active"</c:if>>
					<a  href="javascript:void(0);" onclick="findByStatus('','')"><s:message code="label.generic.all" text="All"/></a>
				</li>
				<c:forEach items="${OrderStatus}" var="stauts" varStatus="orderStatus">
					<li <c:if test="${not empty cstatus && cstatus==stauts[0]}"> class="active"</c:if>>
						<a href="javascript:void(0);" onclick="findByStatus('${stauts[0]}','${stauts[1]}')"><s:message code="order.status.pay_first.${stauts[0]}" text="${stauts[0]}"/></a>
					</li>
				</c:forEach>
				<li >
					<div class="input-group" style="padding-top:3px;width:250px">      
						<input  id="findbyname" type="text" class="form-control " 
								placeholder="<s:message code='label.generic.find.holde' text='holde' />"
								<c:if test="${not empty criteria.findName}"> 
									value="${criteria.findName}" 
								</c:if>
							/>
						<a href="javascript:void(0);" onclick="findByName()" class="input-group-addon" >
							<span class="glyphicon glyphicon-search" aria-hidden="true"  ></span>
						</a>
					</div>
				</li>

			</ul>
		</div>	 <!-- <%-- 订单头部信息结束 --%> -->
		<c:if test="${not empty customerOrders && not empty customerOrders.ezybioOrder}">
			<div >
				<!-- HISTORY TABLE -->
				<div class="account_orders">
					<c:set var="customerStoreOrder" value="${pageContext.request.contextPath}/shop/customer/order.html" scope="request"/>
					<jsp:include page="/common/orderDetail.jsp"/>
				</div>
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
						</c:if>
					</span>

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
			<input type="hidden" name="secondStatus" id="secondStatus" <c:if test="${not empty criteria.secondStatus}"> value="${criteria.secondStatus}" </c:if>/>
			<input type="hidden" name="findName" id="findName" <c:if test="${not empty criteria.findName}"> value="${criteria.findName}" </c:if>/>
			<input type="hidden" name="beginDatePurchased" id="beginDatePurchased" 
				<c:if test="${not empty criteria.beginDatePurchased}"> 
					value="${criteria.beginDatePurchased}" 
				</c:if>
			/>
			<!-- input type="hidden" name="splitSubOrder" id="splitSubOrder" /-->
		</fieldset>
	</form:form>
</div>


													
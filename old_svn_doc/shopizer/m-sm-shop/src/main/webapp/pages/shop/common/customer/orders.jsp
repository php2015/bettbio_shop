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
<c:set var="ordersAction" value="${pageContext.request.contextPath}/shop/customer/orders.html"/>
<script src="<c:url value="/resources/js/orders.js" />"></script>
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


			<div class="customer-box" >		
			 <div class="input-group" style="padding-top:3px;">      
							      <input  id="findbyname" type="text" class="form-control " placeholder=<s:message	code="label.generic.find.holde" text="holde" /> <c:if test="${not empty criteria.findName}"> value="${criteria.findName}" </c:if>>
							      <a  href="javascript:void(0);" onclick="findByName()" class="input-group-addon" ><span class="glyphicon glyphicon-search" aria-hidden="true"  ></span></a>
							      <div class="pull-right"><a href="#" onClick="javascript:location.href='<c:url value="/shop/customer/dashboard.html" />';"><span aria-hidden="true" class="carticon fa fa-reply fa-2x icon icon-style" style="background:#1E90FF;"></span></a></div>									
						    </div>			
				        	<div class="">						
	
					<ul class="nav nav-pills">
						<li <c:if test="${empty cstatus}"> class="active"</c:if>><a  href="<c:url value="/shop/customer/orders.html"/>" ><s:message code="label.generic.all" text="All"/></a></li>
						<c:forEach items="${OrderStatus}" var="stauts" varStatus="orderStatus">
							<li <c:if test="${not empty cstatus && cstatus==stauts}"> class="active"</c:if>><a href="<c:url value="/shop/customer/orders.html?status=${stauts}"/>" ><s:message code="label.order.${stauts}" text="${stauts}"/></a></li>
						</c:forEach>						
						
					</ul>
	
				</div>	
                 <c:if test="${not empty customerOrders && not empty customerOrders.ezybioOrder}">
                        
                 	<div class="container-fluid">
                 	<div class="row">
						<c:forEach items="${customerOrders.ezybioOrder}" var="order" varStatus="orderStatus">
							<div class="col-xs-12" style="background-color:#F1F5FB;font-weight:bold">[${orderStatus.index+1 }]&nbsp;&nbsp;&nbsp;&nbsp;<a href="${pageContext.request.contextPath}/shop/customer/order.html?orderId=${order.orderID}"><fmt:formatDate type="both" pattern="yyyy-MM-dd HH:mm:ss" value="${order.datePurchased}" /> &nbsp;<s:message code="menu.order" text="Order"/><s:message code="menu.order.no" text="No"/>:
			${order.orderID}&nbsp;<s:message code="menu.order" text="Order"/><s:message code="label.entity.status" text="Status"/>:<s:message code="label.order.${order.orderStatus}" text="label [${order.orderStatus}] not found"/>&nbsp;<s:message code="label.order.titles" text="Total"/>:
			<sm:monetary value="${order.total}"/></a></div>
							<c:forEach items="${order.subOrders}" var ="store">
								<c:forEach items="${store.cartItems}" var ="product" >
									<div class="col-xs-3">
										<c:choose>
											<c:when test="${product.image!=null}">
												<img width="50" src="<c:url value="${product.image}"/>">
											</c:when>
											<c:otherwise>
												<img width="50" src="<c:url value="/resources/img/pimg.jpg" />">
											</c:otherwise>
										</c:choose>		
									</div>
									<div class="col-xs-9"  >
										<div class="row">
											<div class="col-xs-12" style="border-left:none;word-break: break-all; word-wrap:break-word;" >
												<a href='<c:url value="/shop/product/${product.friendlyUrl}.html"/>' target="blank"><span style="font-weight:bold">${product.name}</span>
												<c:if test="${not empty product.enName }">
													<br/><s:message code="label.productedit.productenname" text="English Name"/>&nbsp;:&nbsp;<span >${product.enName}</span>
												</c:if>
												</a>								
											</div>
											<div class="col-xs-4">
												${product.specs}
											</div>
											<div class="col-xs-4">
												<sm:monetary value="${product.price}"/>
											</div>	
											<div class="col-xs-4">
												x${product.quantity}
											</div>	
											<div class="col-xs-12">
												<c:choose>
													<c:when test="${product.reviewed==1 }">
														<a class="btn btn-default btn-xs btn-block" target="_blank" href="<c:url value="/shop/customer/review.html"/>?productId=${product.productId}&orderProductId=${product.id}">
															<s:message code="label.product.view.review" text="View Review"/>
														</a>
													</c:when>
													<c:otherwise>
														<a class="btn btn-warning btn-xs btn-block" target="_blank" href="<c:url value="/shop/customer/review.html"/>?productId=${product.productId}&orderProductId=${product.id}">
															<s:message code="label.product.rate" text="Review"/>
														</a>
													</c:otherwise>
												</c:choose>
											</div>							
										</div>
									</div>
								</c:forEach>
								<div class="col-xs-6">
									${store.merchantStore.storename}																	
								</div>
								<div class="col-xs-6">
									<s:message code="label.subtotal" text="Store"/>:<sm:monetary value="${store.total}"/>									
								</div>
								<div class="col-xs-12">
									<c:if test="${store.status == 'SHIPPED'}">
										<a class="btn btn-info btn-xs btn-block" href="javascript:confirmReceived('${order.orderID }', '${store.id}')"><s:message code="label.order.btn.confirm" text="confirm order"/></a>
									</c:if>									
								</div>
							</c:forEach>
						</c:forEach>						
							<div  class="col-xs-12 text-left " >
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
							<div  class="col-xs-12 text-left " >
								<c:set var="pagesAction" value="${pageContext.request.contextPath}/shop/customer/orders.html" scope="request"/>
									<c:set var="paginationData" value="${paginationData}" scope="request"/>
									<jsp:include page="/common/pagination.jsp"/>
							</div>
					</div>
					</div>
				</c:if>
				<c:if test="${empty customerOrders || empty customerOrders.ezybioOrder}">
					<div class="container-fluid">
					<div class="row">
						<div class="col-xs-12"><h3><s:message code="message.no.items" text="No Items"/></h3></div>
						</div>
					</div>
				</c:if>
	 </div>				
		<form:form method="post" action="${pageContext.request.contextPath}/shop/customer/orders.html" id="ezybioForm" commandName="criteria" tyle="display:none">
			<fieldset>
					<input type="hidden" name="page" id="page" <c:if test="${not empty paginationData}"> value="${paginationData.currentPage}" </c:if> />
					<input type="hidden" name="status" id="status" <c:if test="${not empty criteria.status}"> value="${criteria.status}" </c:if>/>
					<input type="hidden" name="findName" id="findName" <c:if test="${not empty criteria.findName}"> value="${criteria.findName}" </c:if>/>
					<input type="hidden" name="beginDatePurchased" id="beginDatePurchased" <c:if test="${not empty criteria.beginDatePurchased}"> value="${criteria.beginDatePurchased}" </c:if>/>
					<!-- input type="hidden" name="splitSubOrder" id="splitSubOrder" /-->
			</fieldset>
</form:form>
			  

			

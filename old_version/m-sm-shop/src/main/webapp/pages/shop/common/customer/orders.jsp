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
 
 // order action
 var orderActionUrl="<c:url value='/shop/order/action/'/>";
function doOrderAction(orderId, subOrderId, actionName){
	if (actionName == "cancel_order"){
		cancelOrder(subOrderId);
	}else if (actionName == "confirm_receipt"){
		confirmReceiptOrder(subOrderId);
	}else if (actionName == "upload_pay_proof"){
		confirmPaid(subOrderId);
	}else{
		alert("系统目前暂不支持该项操作");
	}
}
function invokeOrderAction(subOrderId, actionName){
	$('#order_detail_list_div').showLoading();
	$.ajax({  
		type: 'POST',
		url: orderActionUrl+"actionEvent.html",
		data:"soid="+subOrderId+"&actionName="+actionName,
		dataType:"json",
		success: function(response) {
			$('#order_detail_list_div').hideLoading();
			if (typeof response.response == 'undefined'){
				alert("操作失败，请重新登录");
				return;
			}
			response = response.response;
			if (response.status == 0){
				doAction();
				return;
			}
			if (response.message != null){
				alert("操作失败: "+response.message);
				return;
			}
			if (response.statusMessage != null){
				alert("操作失败: "+response.statusMessage);
				return;
			}
			var errMsg="操作失败。\n  代码"+response.code+", 类型"+response.messageKey+"\n  参数[";
			for(var i=1;i<10;i++){
				if (typeof response['messageParam'+i] == "string"){
					errMsg+=response['messageParam'+i]+",";
				}
			}
			errMsg += "]";
			alert(errMsg);
		} ,
		error: function( textStatus, errorThrown) {
			$('#order_detail_list_div').hideLoading();
			alert("操作失败，请检查是否有权限操作此订单");
		}
	});
}
function confirmPaid(subOrderId){
	if (!confirm("建议您在PC上上传支付凭证后再确认付款。\n确认订单 "+subOrderId+" 已付款么？")){
		return;
	}
	invokeOrderAction(subOrderId, "upload_pay_proof");
}
function cancelOrder(subOrderId){
	if (!confirm("确认取消订单 "+subOrderId+" 么？")){
		return;
	}
	invokeOrderAction(subOrderId, "cancel_order");
}
function confirmReceiptOrder(subOrderId){
	if (!confirm("确认订单 "+subOrderId+" 已收货么？")){
		return;
	}
	invokeOrderAction(subOrderId, "confirm_receipt");
}
 </script>


			<div class="customer-box" >		
			 <div class="input-group" style="padding-top:3px;">      
							      <input  id="findbyname" type="text" class="form-control " placeholder=<s:message	code="label.generic.find.holde" text="holde" /> <c:if test="${not empty criteria.findName}"> value="${criteria.findName}" </c:if>>
							      <a  href="javascript:void(0);" onclick="findByName()" class="input-group-addon" ><span class="glyphicon glyphicon-search" aria-hidden="true"  ></span></a>
							      <div class="pull-right"><a href="#" onClick="javascript:location.href='<c:url value="/shop/customer/dashboard.html" />';"><span aria-hidden="true" class="carticon fa fa-reply fa-2x icon icon-style" style="background:#1E90FF;"></span></a></div>									
						    </div>			
				        	<div class="">						
	
					<ul class="nav nav-pills">
						<li <c:if test="${empty cstatus}"> class="active"</c:if>>
							<a  href="<c:url value="/shop/customer/orders.html"/>" ><s:message code="label.generic.all" text="All"/></a>
						</li>
						<c:forEach items="${OrderStatus}" var="stauts" varStatus="orderStatus">
							<li <c:if test="${not empty cstatus && cstatus==stauts[0]}"> class="active"</c:if>>
								<a href="<c:url value="/shop/customer/orders.html?status=${stauts[0]}&secondStatus=${stauts[1]}"/>" >
									<s:message code="order.status.pay_first.${stauts[0]}" text="${stauts[0]}"/>
								</a>
							</li>
						</c:forEach>						
						
					</ul>
	
				</div>	
                 <c:if test="${not empty customerOrders && not empty customerOrders.ezybioOrder}">
                        
                 	<div class="container-fluid">
<!------------------------------------------------------------------
	begin of order details  
  ------------------------------------------------------------------>
<style>
.pay_first_order {
	color: red;
}
.ship_first_order {
	color: blue;
}
.adjust_price_affected {
    font-weight: bold;
    color: #23527c;
}
.subOrderSummary {
	color: #23527c;
}
</style>
		<div class="row" id="order_detail_list_div">
			<c:forEach items="${customerOrders.ezybioOrder}" var="order" varStatus="orderStatus">
			<c:set var="rawOrder" value="${order.rawOrder}"/>
				<div class="col-xs-12" style="background-color:#F1F5FB;font-weight:bold;margin-top:10px;">[${orderStatus.index+1 }]&nbsp;&nbsp;&nbsp;&nbsp;
					<c:forEach items= "${rawOrder.subOrders}" var="sorder" begin="0" end="0">
						<c:set var="soid" value="${sorder.id}"/>
					</c:forEach>
					<a href="${pageContext.request.contextPath}/shop/customer/order.html?orderId=${soid}&w=${order.orderID}">
						<fmt:formatDate type="both" pattern="yyyy-MM-dd HH:mm:ss" value="${order.datePurchased}" /> &nbsp;
						<c:if test="${rawOrder.processType == 'pay_first'}">
							<span class="pay_first_order"><s:message code="order.processType.pay_first" text="付款后发货" /></span>
						</c:if>
						<c:if test="${rawOrder.processType != 'pay_first'}">
							<span class="ship_first_order"><s:message code="order.processType.ship_first" text="先发货后付款" /></span>
						</c:if>
						<span class="pull-right">
							<s:message code="label.order.titles" text="Total"/>:<sm:monetary value="${rawOrder.total}"/>
						</span>
					</a>
				</div>
				<c:forEach items="${rawOrder.subOrders}" var ="subOrder">
					<!-- summary -->
					<div class="col-xs-12 subOrderSummary">
						订单编号: ${subOrder.id}
						<!-- <span style="padding-left: 20px;"> -->
						<span class="pull-right">
							<c:if test="${rawOrder.processType == 'pay_first'}">
								<s:message code="order.status.pay_first.${subOrder.status}" text="${status.status}" />
							</c:if>
							<c:if test="${rawOrder.processType != 'pay_first'}">
								<s:message code="order.status.ship_first.${subOrder.status}" text="${status.status}" />
							</c:if>
						</span>
					</div>
					<c:forEach items="${subOrder.orderProducts}" var ="product" >
						
						<!-- product Image -->
						<div class="col-xs-3">
							<c:choose>
								<c:when test="${product.productImageUrl!=null}">
									<img width="50" src="<c:url value="${product.productImageUrl}"/>">
								</c:when>
								<c:otherwise>
									<img width="50" src="<c:url value="/resources/img/pimg.jpg" />">
								</c:otherwise>
							</c:choose>		
						</div>
						<!-- name, spec, price and operation -->
						<div class="col-xs-9">
							<div class="row">
								<div class="col-xs-12" style="border-left:none;word-break: break-all; word-wrap:break-word;" >
									<a href='<c:url value="/shop/product/${product.productID}.html"/>' target="blank">
										<span style="font-weight:bold">
											${product.productCode}<br/>
											${product.productName}
										</span>
										<c:if test="${not empty product.productEnName }">
											<br/><s:message code="label.productedit.productenname" text="English Name"/>&nbsp;:&nbsp;<span >${product.productEnName}</span>
										</c:if>
									</a>								
								</div>
								<div class="col-xs-4">
									${product.specifications}
								</div>
								<div class="col-xs-6">
									<c:if test="${not empty product.finalPrice}">
										<span class="adjust_price_affected"><sm:monetary value="${product.finalPrice}"/></span>
										<s style="color:gray;"><sm:monetary value="${product.price}"/></s>
									</c:if>
									<c:if test="${empty product.finalPrice}">
										<sm:monetary value="${product.price}"/>
									</c:if>
								</div>	
								<div class="col-xs-2">
									x${product.productQuantity}
								</div>	
							</div>
						</div>
						<!-- review -->
						<div class="col-xs-12">
							<c:if test="${fn:contains(subOrder.actionList,'review_order')}">
								<c:choose>
									<c:when test="${not empty product.productReview}">
										<a class="btn btn-default btn-xs btn-block" target="_blank" 
												href="<c:url value='/shop/customer/review.html'/>?productId=${product.productID}&orderProductId=${product.id}">
											<s:message code="label.product.view.review" text="View Review"/>
										</a>
									</c:when>
									<c:otherwise>
										<a class="btn btn-warning btn-xs btn-block" target="_blank" 
												href="<c:url value='/shop/customer/review.html'/>?productId=${product.productID}&orderProductId=${product.id}">
											<s:message code="label.product.rate" text="Review"/>
										</a>
									</c:otherwise>
								</c:choose>
							</c:if>
						</div>	
					</c:forEach>
					<div class="col-xs-6">
						${subOrder.merchant.storename}																	
					</div>
					<div class="col-xs-6">
						<s:message code="label.subtotal" text="Store"/>:
						<c:if test="${not empty subOrder.finalTotal}">
							<span class="adjust_price_affected"><sm:monetary value="${subOrder.finalTotal}"/></span>
							<s style="color:gray;"><sm:monetary value="${subOrder.total}"/></s>
						</c:if>
						<c:if test="${empty subOrder.finalTotal}">
							<sm:monetary value="${subOrder.total}"/>
						</c:if>
					</div>
					<!-- shipping info -->
					<c:if test="${not empty subOrder.deliveryNumber}">
						<div class="col-xs-12">
							<a href="<c:url value='http://www.baidu.com/s' >
									<c:param name='wd' value='快递 ${subOrder.deliveryNumber} ${subOrder.deliveryCode}' />
									</c:url>" target="_blank" class="btn-link">
								物流信息：${subOrder.deliveryCode} ${subOrder.deliveryNumber} 
							</a>
						</div>
					</c:if>
					<!-- actions -->
					<div class="col-xs-12">
						<c:forEach items="${subOrder.actionList}" var="action" begin="0">
							<c:choose>
								<c:when test="${action == 'review_order'}">
								</c:when>
								<c:when test="${action == 'upload_pay_proof'}">
									<c:if test="${subOrder.status != 'PAID'}">
										<a class="btn btn-info btn-xs btn-block" href="javascript:doOrderAction(${rawOrder.id}, ${subOrder.id}, '${action}')">
										确认付款
									</a>
									</c:if>
								</c:when>
								<c:otherwise>
									<a class="btn btn-info btn-xs btn-block" href="javascript:doOrderAction(${rawOrder.id}, ${subOrder.id}, '${action}')">
										<s:message code="order.action.${action}" text="${action}" />
									</a>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</div>
				</c:forEach>
				
			</c:forEach>
				<br/>
				<div  class="col-xs-12 text-left "  style="padding-top:5px;">
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
				
				<div  class="col-xs-12 text-left ">
					<c:set var="pagesAction" value="${pageContext.request.contextPath}/shop/customer/orders.html" scope="request"/>
						<c:set var="paginationData" value="${paginationData}" scope="request"/>
						<jsp:include page="/common/pagination.jsp"/>
				</div>
		</div>
<!-- 
	----------------------------------------------------------------
	end of order details  
	----------------------------------------------------------------
-->
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
					<input type="hidden" name="secondStatus" id="status" <c:if test="${not empty criteria.status}"> value="${criteria.secondStatus}" </c:if>/>
					<input type="hidden" name="findName" id="findName" <c:if test="${not empty criteria.findName}"> value="${criteria.findName}" </c:if>/>
					<input type="hidden" name="beginDatePurchased" id="beginDatePurchased" <c:if test="${not empty criteria.beginDatePurchased}"> value="${criteria.beginDatePurchased}" </c:if>/>
					<!-- input type="hidden" name="splitSubOrder" id="splitSubOrder" /-->
			</fieldset>
</form:form>
			  

			

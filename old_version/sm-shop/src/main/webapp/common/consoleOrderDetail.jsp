<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>

<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page session="false" %>
<!-- from src\main\webapp\common\consoleOrderDetail.jsp -->
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

</style>

<script type="text/javascript">
var savePriceUrl="<c:url value='/admin/order/savePrice.html'/>";
var orderActionUrl="<c:url value='/admin/order/action/'/>";
function doOrderAction(subOrderId, actionName){
	if (actionName == "confirm_pay"){
		confirmOrderPaid(subOrderId);
	}else if (actionName == "reset_order"){
		resetOrder(subOrderId);
	}else if (actionName == "close_order"){
		closeOrder(subOrderId);
	}else if (actionName == "cancel_order"){
		cancelOrder(subOrderId);
	}else if (actionName == "confirm_receipt"){
		confirmReceiptOrder(subOrderId);
	}else if (actionName == "ship_order"){
		doDelivery(subOrderId);
	}else if (actionName == "confirm_order"){
		confirmOrderProcessed(subOrderId);
	}else{
		alert("Do " + actionName + " on " + subOrderId);
	}
}
function invokeOrderAction(subOrderId, actionName){
	$.ajax({  
		type: 'POST',
		url: orderActionUrl+"actionEvent.html",
		data:"soid="+subOrderId+"&actionName="+actionName,
		dataType:"json",
		success: function(response) {
			if (typeof response.response == 'undefined'){
				activeFaild("操作失败，请重新登录");
				return;
			}
			response = response.response;
			if (response.status == 0){
				//location.replace(location.href);
				doAction();
				return;
			}
			if (response.message != null){
				activeFaild("操作失败: "+response.message);
				return;
			}
			if (response.statusMessage != null){
				activeFaild(response.statusMessage);
				return;
			}
			activeFaild();
		} ,
		error: function( textStatus, errorThrown) {
			activeFaild("操作失败，请检查是否有权限操作此订单");
		}
	});
}
function confirmOrderPaid(subOrderId){
	if (!confirm("确认已经收到订单 "+subOrderId+" 的货款了么？")){
		return;
	}
	invokeOrderAction(subOrderId, "confirm_pay");
}
function resetOrder(subOrderId){
	if (!confirm("确认重置订单 "+subOrderId+" 么？")){
		return;
	}
	invokeOrderAction(subOrderId, "reset_order");
}
function closeOrder(subOrderId){
	if (!confirm("确认直接关闭订单 "+subOrderId+" 么？")){
		return;
	}
	invokeOrderAction(subOrderId, "close_order");
}
function cancelOrder(subOrderId){
	if (!confirm("确认取消订单 "+subOrderId+" 么？")){
		return;
	}
	invokeOrderAction(subOrderId, "cancel_order");
}
function confirmReceiptOrder(subOrderId){
	if (!confirm("注意！此操作是代替买家进行的。\n确认订单 "+subOrderId+" 已收货了么？")){
		return;
	}
	invokeOrderAction(subOrderId, "confirm_receipt");
}
function confirmOrderProcessed(subOrderId){
	if (!confirm("注意！确认后订单将不再允许改价。\n确认订单 " +subOrderId+" 么？")){
		return;
	}
	invokeOrderAction(subOrderId, "confirm_order");
}
function showHideMoreAction(aObj){
	var titleObj = $(aObj).text();
	var actionDiv = $(aObj).prev();
	if (titleObj == "收起<<"){
		$(actionDiv).css('display','none');
		$(aObj).text("更多>>");
	}else{
		$(actionDiv).css('display','inline');
		$(aObj).text("收起<<");
	}
}
</script>
<span id="sp_adminName" hidden="hidden">
   <sec:authentication property="principal.username" />
</span>
<c:forEach items="${customerOrders.ezybioOrder}" var="order" varStatus="orderStatus">
		<c:set var="rawOrder" value="${order.rawOrder}"/>
		<tr class="order-detail-title" ><!-- order -->
		  <td colspan="5" style="padding-left: 5px; margin-left: 0px;font-weight:bold;background-color:#F1F5FB">
		    <div class="row">
		      <div class="col-sm-12">
		         <c:forEach var="sub" items="${order.subOrders}">
		          <input type="text" hidden="hidden" id="suborderid" value="${sub.id}"/>
		          </c:forEach>
				  <c:forEach items= "${order.rawOrder.subOrders}" var="sorder" begin="0" end="0">
					<c:set var="soid" value="${sorder.id}"/>
				  </c:forEach>
			      <a href="${requestScope.customerStoreOrder}?orderId=${soid}&w=${order.orderID}" >
					<fmt:formatDate type="date" pattern="yyyy-MM-dd HH:mm:ss" dateStyle="default" value="${order.datePurchased}" /> &nbsp;
					<c:if test="${order.rawOrder.processType=='pay_first'}">
						<span class="pay_first_order">
						<s:message code="menu.order" text="Order"/>类型:&nbsp;
						<s:message code="order.processType.pay_first" text="收货后付款" />
						</span>
					</c:if>
					<c:if test="${order.rawOrder.processType!='pay_first'}">
						<span class="ship_first_order">
						<s:message code="menu.order" text="Order"/>类型:&nbsp;
						<s:message code="order.processType.ship_first" text="付款后发货" />
						</span>
						
					</c:if>
					<%--
					<fmt:parseDate value="2017-04-01" var="oldDate" pattern="yyyy-MM-dd"/>
					<c:if test="${rawOrder.datePurchased < oldDate}">
						(旧订单编号:<s>${rawOrder.id}</s>)
					</c:if>
					--%>
					<s:message code="label.customer.shipping.firstname" text="Name"/>:${order.billName}
					<c:if test="${not empty order.buyerCompany}">
						买家单位：${order.buyerCompany}
					</c:if>
				  </a>
			  </div>
			 
              </div>
            
		  </td>
		</tr>

		<c:forEach items="${rawOrder.subOrders}" var ="subOrder">
			<c:forEach items="${subOrder.orderProducts}" var="orderProduct" varStatus="opState">
				<tr><!-- order -->
					<td width="59%" style="word-break: break-all; word-wrap:break-word;"> <!-- 产品名称-->
						<a  href="<c:url value='/shop/product/' />${orderProduct.productID}.html">
							<c:if test="${not empty orderProduct.productCode}">
								${orderProduct.productCode} | 
							</c:if>
							${orderProduct.productName}
						</a>
						<c:if test="${not empty orderProduct.specifications }">
							<br/>(${orderProduct.specifications})
						</c:if>
						<c:if test="${not empty orderProduct.productEnName }">
							<br/><s:message code="label.productedit.productenname" text="English Name"/>&nbsp;:&nbsp;<span >${orderProduct.productEnName}</span>
						</c:if>
					</td>
					<td width="7%"> <!-- 单价-->
						<c:if test="${not empty orderProduct.finalPrice}">
							<span class='adjust_price_affected'><sm:monetary value="${orderProduct.finalPrice}"/></span>
							<br/>
							<s style="color: gray;"><sm:monetary value="${orderProduct.price}"/></s>
						</c:if>
						<c:if test="${empty orderProduct.finalPrice}">
							<sm:monetary value="${orderProduct.price}"/>
						</c:if>
					</td>
					<td width="7%">${orderProduct.productQuantity}</td> <!-- 数量-->
					<td width="7%"> <!-- 单条商品小计 -->
						<c:if test="${not empty orderProduct.finalTotal}">
							<span class='adjust_price_affected'><sm:monetary value="${orderProduct.finalTotal}"/></span>
							<br/>
							<s style="color: gray;"><sm:monetary value="${orderProduct.oneTimeCharge}"/></s>
						</c:if>
						<c:if test="${empty orderProduct.finalTotal}">
							<sm:monetary value="${orderProduct.oneTimeCharge}"/>
						</c:if>
					</td>
					<c:if test="${opState.index == 0}">
						<td width="20%" rowspan="${subOrder.orderProducts.size()}">
							<strong><s:message code="menu.order" text="Order"/><s:message code="menu.order.no" text="No"/>:${subOrder.id}</strong><br/>
							<s:message code="label.subtotal" text="Store"/>:
							<c:if test="${not empty subOrder.finalTotal}">
								<span class='adjust_price_affected'><sm:monetary value="${subOrder.finalTotal}"/></span>
								<s style="color: gray;"><sm:monetary value="${subOrder.total}"/></s>
							</c:if>
							<c:if test="${empty subOrder.finalTotal}">
								<sm:monetary value="${subOrder.total}"/>
							</c:if>
							<br/>
							<span id="status_${subOrder.id}">
								<s:message code="menu.order" text="Order"/><s:message code="label.entity.status" text="Status"/>:
								<c:if test="${rawOrder.processType=='pay_first'}">
									<s:message code="order.status.pay_first.${subOrder.status}" text="${status.status}" />&nbsp;
								</c:if>
								<c:if test="${rawOrder.processType!='pay_first'}">
									<s:message code="order.status.ship_first.${subOrder.status}" text="${status.status}" />&nbsp;
								</c:if>
								<c:if test="${not empty subOrder.payProof}">
									<a href="<c:url value='/orderstatic/'/>${subOrder.payProof}" target="blank">
										<img src="<c:url value='/resources/img/pay_proof.png'/>" height="16px" />
									</a>
								</c:if>
							</span>
							<c:if test="${subOrder.status=='ORDERED' }">
								<c:if test="${subOrder.orderProducts.size()>1}">
									<br/><a href="javascript:void(0);" onclick="showSplit(${subOrder.id})"><s:message code="label.order.split" text="Spilt"/></a>
								</c:if>
							</c:if>
							<c:if test="${not empty subOrder.actionList}">
								<div>
									<c:set var="loopedTimes" value="0"/>
									<c:forEach items="${subOrder.actionList}" var="action" >
										<c:if test="${loopedTimes == 2 and action != 'upload_pay_proof' and action != 'review_order'}">
											<div style="display: none;">
										</c:if>
										<c:if test="${action == 'adjust_price'}">
											<c:set var="loopedTimes" value="${loopedTimes + 1}"/>
											<a href="${requestScope.customerStoreOrder}?orderId=${subOrder.id}&w=${order.orderID}" >
												改价
											</a>
											&nbsp;
										</c:if>
										<c:if test="${action != 'adjust_price' && action != 'upload_pay_proof' && action != 'review_order'}">
											<c:set var="loopedTimes" value="${loopedTimes + 1}"/>
											<a href="javascript:void(0)" onclick="doOrderAction(${subOrder.id}, '${action}')"/>
												<s:message code="order.action.${action}" text="${action}" />
											</a>
											&nbsp;
										</c:if>
										
									</c:forEach>
									<c:if test="${loopedTimes >= 3}">
										</div>
										<a href="javascript:void(0);" onclick="showHideMoreAction(this)" style="color: black;">
											<span>更多&gt;&gt;</span>
										</a>
									</c:if>
									
								</div>
							</c:if>
						</td>
					</c:if>
				</tr>
			</c:forEach>
		</c:forEach>
		
	</c:forEach>
	<div class="modal fade" id="confirmPaid" tabindex="-1" role="dialog"  data-backdrop= "static" aria-hidden="true">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 id="cartModalLabel"><img src="<c:url value="/resources/img/important-icon.png"/>" width="40"/>&nbsp;<strong id="modeltitle">确认付款</strong></h4>
	      </div>
	      <div class="modal-footer">
	       		 <a href="javascript:void(0);" id="closeOrder" class="btn btn-info" ><strong><s:message code="button.label.submit2" text="Submit"/></strong></a>
				<a href="#" class="btn btn-default" data-dismiss="modal"><strong><s:message code="cart.close" text="Close"/></strong></a>
	      </div>
	    </div>
	  </div>
</div>
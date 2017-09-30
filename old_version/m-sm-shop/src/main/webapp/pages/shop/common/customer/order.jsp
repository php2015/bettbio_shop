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
<script type="text/javascript">

function deliveryInfo(code,type){
	var url="http://api.kuaidi.com/openapi.html?id=e2c7412a9ebd75d93655b82b8c161ba6&com="+type+"&nu="+code;
	if(code==null){
		$('#deliveryInfomation').html('<s:message code="label.order.delivery.erro" text="erro" />');
		$('#deliveryInfo').modal('show');
		return;
	}	
	$.ajax({  
		type: 'POST',
		  url: url,
		  success: function(defaultdata) {
			  if(defaultdata !=null){
				  if(defaultdata.success==true){
					  var info='<div class="row">'
						  $.each(defaultdata.data, function(i, p) {
							  info+='<div class="col-sm-4">'+p.time+'</div><div class="col-sm-8">'+p.context+'</div>';
						  });
						  info+='</div>'
							  $('#deliveryInfomation').html(info);
				  }else{
					  $('#deliveryInfomation').html('<s:message code="label.order.delivery.erro" text="erro" />');
				  }
			  }else{
				  $('#deliveryInfomation').html('<s:message code="label.order.delivery.erro" text="erro" />');
			  }
			  $('#deliveryInfo').modal('show');
			 } ,
		 error: function( textStatus, errorThrown) {
			 $('#deliveryInfomation').html('<s:message code="label.order.delivery.erro" text="erro" />');
			 $('#deliveryInfo').modal('show');
			 
		 }
		 	});
};
</script>
<script src="<c:url value="/resources/js/jquery.printElement.min.js" />"></script>
	<div class="box ">
	<div class="row">
			<div class="col-xs-12">
				<h4><s:message code="label.order.details" text="Order details" />&nbsp;#&nbsp;
				<%-- <s:message code="menu.order" text="Order" /><s:message code="menu.order.no" text="No" />:<c:out value="${order.orderID}"/> --%>

				<c:if test="${order.rawOrder.processType == 'pay_first'}">
					<span class="pay_first_order"><s:message code="order.processType.pay_first" text="付款后发货" /></span>
				</c:if>
				<c:if test="${order.rawOrder.processType != 'pay_first'}">
					<span class="ship_first_order"><s:message code="order.processType.ship_first" text="先发货后付款" /></span>
				</c:if>
				</h4>
				
<hr style="border-top:solid 1px #0080ff;">
			</div>	
			
				
						
			<div class="col-xs-12" >
				<fmt:formatDate type="both" dateStyle="medium" value="${order.datePurchased}" />&nbsp;&nbsp;
					
			</div>			
			
			<div class="col-xs-12" style="padding:10px;">
				<c:if test="${not empty order.customerAdress}">
				<strong><s:message code="label.customer.shippinginformation" text="Billing address" /></strong>
				<br>
					<c:set var="address" value="${order.customerAdress}" scope="request" />
					<jsp:include page="/common/customerAddress.jsp"/>
				
				</c:if>
			</div>	
			<div class="col-xs-12"style="padding:10px;">
				<c:if test="${not empty order.customerInvoice}">
				<strong><s:message code="label.customer.billinginformation" text="Shipping address" /></strong>
				<br>
					<c:set var="delivery" value="${order.customerInvoice}" scope="request" />
					<jsp:include page="/common/customerInvoice.jsp"/>
				
				</c:if>
			</div>			
		<div class="col-xs-12" style="padding:10px;">
			<c:if test="${not empty order.invoiceAddress}">
			<strong><s:message code="label.customer.invoiceaddress" text="Shipping address" /></strong>
				<br>
				<c:set var="address" value="${order.invoiceAddress}" scope="request" />
				<jsp:include page="/common/customerAddress.jsp"/>
			
			</c:if>
		</div>		
					
		
					
				<c:forEach items="${order.subOrders}" var="subOrder">
					<div class="col-xs-8" style="background-color: #e6f6f3;">
						订单号:${subOrder.id} (${subOrder.merchantStore.storename})
					</div>	
					<div class="col-xs-4" style="background-color: #e6f6f3;">
						<span>
							<c:if test="${order.rawOrder.processType == 'pay_first'}">
								<s:message code="order.status.pay_first.${subOrder.status}" text="${status.status}" />
							</c:if>
							<c:if test="${order.rawOrder.processType != 'pay_first'}">
								<s:message code="order.status.ship_first.${subOrder.status}" text="${status.status}" />
							</c:if>
						</span>
						<c:if test="${ subOrder.status != 'ORDERED' }">
							&nbsp;<a href="javascript:void(0);"  onclick='deliveryInfo("${subOrder.deliveryNum}","${subOrder.deliveryName}")' >
								<s:message code="label.order.delivery.info" text="Info"/>
							</a>
						</c:if>
					</div>
					<c:forEach items="${subOrder.cartItems}" var="product">
					<%-- find raw data first--%>
								<c:forEach items="${order.rawOrder.subOrders}" var="rawSubOrder">
									<c:if test="${subOrder.id == rawSubOrder.id}">
										<c:set var="orgSubOrder" value="${rawSubOrder}"/>
										<c:forEach items = "${rawSubOrder.orderProducts}" var="orgItem">
											<c:if test="${product.id == orgItem.id}">
												<c:set var="orgProduct" value="${orgItem}"/>
											</c:if>
										</c:forEach>
									</c:if>
								</c:forEach>
					<div class="row">
						<div class="col-xs-3">
							<c:choose>
								<c:when test="${product.image !=null}">
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
								<a href='<c:url value="/shop/product/${product.productId}.html"/>' target="blank"><span style="font-weight:bold">${product.name}</span>
								${product.productCode}
								<c:if test="${not empty product.enName }">
									<br/><span>${product.enName}</span>
								</c:if>
								</a>								
							</div>
							<div class="col-xs-4">
								${product.specs}
							</div>
							<div class="col-xs-4">
							
								
								<c:if test="${not empty orgProduct.finalPrice}">
									<span class="adjust_price_affected"><sm:monetary value="${orgProduct.finalPrice}"/></span>
									<s style="color:gray;"><sm:monetary value="${orgProduct.price}"/></s>
								</c:if>
								<c:if test="${empty orgProduct.finalPrice}">
									<sm:monetary value="${orgProduct.price}"/>
								</c:if>
							</div>	
							<div class="col-xs-4">
								x${product.quantity}
							</div>								
						</div>
					</div>
					</div>
					</c:forEach>
					<div class="col-xs-12" style="text-align:right;">
						<s:message code="menu.order" text="Order"/><s:message code="label.subtotal" text="Store"/>:
						<c:if test="${not empty orgSubOrder.finalTotal}">
							<span class="adjust_price_affected"><sm:monetary value="${orgSubOrder.finalTotal}"/></span>
							<s style="color:gray;"><sm:monetary value="${orgSubOrder.total}"/></s>
						</c:if>
						<c:if test="${empty orgSubOrder.finalTotal}">
							<sm:monetary value="${orgSubOrder.total}"/>
						</c:if>

					</div>									
				</c:forEach>
			
			<div class="col-xs-12" style="text-align:right;">
					<font color="red"><s:message code="label.order.titles" text="Total"/>:</font>
					<span><strong><font color="red"><sm:monetary value="${order.total}" /></font></strong></span>
				</div>
						<!-- /cart total -->

		
	
	</div>
	<!--close .container "main-content" -->
	<div class="modal fade" id="deliveryInfo" tabindex="-1" role="dialog"  data-backdrop= "static" aria-hidden="true">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 id="cartModalLabel"><img src="<c:url value="/resources/img/important-icon.png"/>" width="40"/>&nbsp;<strong id="modeltitle"><s:message code="label.order.delivery.info" text="Delivery"/></strong></h4>
	      </div>
	      <div class="modal-body" id="deliveryInfomation">	      		
	       						
	      </div>
	      <div class="modal-footer">	      		
				<a href="#" class="btn" data-dismiss="modal"><strong><s:message code="cart.close" text="Close"/></strong></a>
	      </div>
	    </div>
	  </div>
</div>

<%-- <s:message code="menu.order" text="Order"/><s:message code="label.entity.status" text="Status"/>:
					<s:message code="label.order.${order.orderStatus}" text="${order.orderStatus}" />	
<s:message code="label.order.${subOrder.status}" text="${subOrder.status}" />
<c:if test="${ subOrder.status != 'ORDERED' }">
			&nbsp;<a href="javascript:void(0);"  onclick='deliveryInfo("${subOrder.deliveryNum}","${subOrder.deliveryName}")' ><s:message code="label.order.delivery.info" text="Info"/></a>
		</c:if>
		--%>
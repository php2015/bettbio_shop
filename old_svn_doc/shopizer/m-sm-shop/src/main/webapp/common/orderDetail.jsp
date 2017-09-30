<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page session="false" %>
<c:forEach items="${customerOrders.ezybioOrder}" var="order" varStatus="orderStatus">
		<tr class="order-detail-title" ><!-- order -->
			<td colspan="6" style="padding-left: 5px; margin-left: 0px;background-color:#F1F5FB;font-weight:bold">[${orderStatus.index+1 }]&nbsp;&nbsp;&nbsp;&nbsp;<a href="${requestScope.customerStoreOrder}?orderId=${order.orderID}"><fmt:formatDate type="both" pattern="yyyy-MM-dd HH:mm:ss" value="${order.datePurchased}" /> &nbsp;<s:message code="menu.order" text="Order"/><s:message code="menu.order.no" text="No"/>:
			${order.orderID}&nbsp;<s:message code="menu.order" text="Order"/><s:message code="label.entity.status" text="Status"/>:<s:message code="label.order.${order.orderStatus}" text="label [${order.orderStatus}] not found"/>&nbsp;<s:message code="label.order.titles" text="Total"/>:
			<sm:monetary value="${order.total}"/></a></td>
		</tr>
		<c:forEach items="${order.subOrders}" var ="store">
			<c:forEach items="${store.cartItems}" var ="product" begin="0" end="0">
			<tr><!-- order -->
				<td width="56%" style="word-break: break-all; word-wrap:break-word;"><a href="<c:url value="/shop/product/" />${product.friendlyUrl}.html<sm:breadcrumbParam/>"><span style="word-break: break-all;">${product.name}</span></a>
					<c:if test="${not empty product.enName }">
						<br/><s:message code="label.productedit.productenname" text="English Name"/>&nbsp;:&nbsp;<span >${product.enName}</span>
					</c:if>
				</td>
					<td width="7%"><sm:monetary value="${product.price}"/></td>
					<td width="7%">${product.quantity}</td>					
					<td width="25%" rowspan="${store.cartItems.size()}">
					<div style="float:left;"><s:message code="menu.store" text="Store"/>:<br>
					${store.merchantStore.storename}<br>
					<s:message code="label.subtotal" text="Store"/>:<sm:monetary value="${store.total}"/><br>
					<span style="color:#FF6C00;">
					<s:message code="menu.order" text="Order"/><s:message code="label.entity.status" text="Status"/>:<s:message code="label.order.${store.status}" text="label [${store.status}] not found"/>
					</span>
					</div>
						<c:if test="${store.status == 'SHIPPED'}">
							<div style="float:right;margin:3px;">
								<a class="btn btn-info btn-sm" href="javascript:confirmReceived('${order.orderID }', '${store.id}')"><s:message code="label.order.btn.confirm" text="confirm order"/></a>
							</div>
						</c:if>
					</td>	
					<td width="5%">
						<c:choose>
							<c:when test="${product.reviewed==1 }">
								<a class="btn btn-default btn-sm" target="_blank" href="<c:url value="/shop/customer/review.html"/>?productId=${product.productId}&orderProductId=${product.id}">
									<s:message code="label.product.view.review" text="View Review"/>
								</a>
							</c:when>
							<c:otherwise>
								<a class="btn btn-warning btn-sm" target="_blank" href="<c:url value="/shop/customer/review.html"/>?productId=${product.productId}&orderProductId=${product.id}">
									<s:message code="label.product.rate" text="Review"/><span class="badge" style="margin:2px;padding:3px;font-size:5px;min-width:5px;line-height:5px;">?</span>
								</a>
							</c:otherwise>
						</c:choose>
						
					</td>
			</tr>
			</c:forEach>
			<c:forEach items="${store.cartItems}" var ="product" begin="1">
				<tr>
					<td width="56%"><a href="<c:url value="/shop/product/" />${product.friendlyUrl}.html<sm:breadcrumbParam/>"><span>${product.name}</span></a>
						<c:if test="${not empty product.enName }">
							<br/><s:message code="label.productedit.productenname" text="English Name"/>&nbsp;:&nbsp;<span>${product.enName}</span>
						</c:if>
					</td>
					<td width="7%"><sm:monetary value="${product.price}"/></td>
					<td width="7%">${product.quantity}</td>					
					<td width="5%">
						
						<c:choose>
							<c:when test="${product.reviewed==1 }">
								<a class="btn btn-default btn-sm" target="_blank" href="<c:url value="/shop/customer/review.html"/>?productId=${product.productId}&orderProductId=${product.id}">
									<s:message code="label.product.view.review" text="View Review"/>
								</a>
							</c:when>
							<c:otherwise>
								<a class="btn btn-warning btn-sm" target="_blank" href="<c:url value="/shop/customer/review.html"/>?productId=${product.productId}&orderProductId=${product.id}">
									<s:message code="label.product.rate" text="Review"/><span class="badge" style="margin:2px;padding:3px;font-size:5px;min-width:5px;line-height:5px;">?</span>
								</a>
							</c:otherwise>
						</c:choose>
					</td>																				
				</tr>
			</c:forEach>
		</c:forEach>
	</c:forEach>
<div class="modal fade" id="confirmReceived" tabindex="-1" role="dialog"  data-backdrop= "static" aria-hidden="true">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 id="cartModalLabel"><img src="<c:url value="/resources/img/important-icon.png"/>" width="40"/>&nbsp;<strong id="modeltitle"><s:message code="label.order.btn.confirm" text="Delivery"/></strong></h4>
	      </div>
	      <div class="modal-footer">
	       		 <a href="javascript:void(0);" id="ordersubmit" class="btn btn-info" ><strong><s:message code="button.label.submit2" text="Submit"/></strong></a>
				<a href="#" class="btn btn-default" data-dismiss="modal"><strong><s:message code="cart.close" text="Close"/></strong></a>
	      </div>
	    </div>
	  </div>
</div>

<script>
/**
 * confirm  product received
 */
function confirmReceived(oid, soid){
	$('#confirmReceived').modal('show');
	$('#ordersubmit').bind('click', function(){
		$('#confirmReceived').modal('hide');
		$.ajax({  
			type: 'POST',
			url: '<c:url value="/shop/customer/confirmReceived.html"/>',
			data:{
				orderid:oid,
				suborderid:soid
			},
			success: function(response) {
				if(response.response.status==0) {
					alertSuccess();
					window.location.href = '<c:url value="/shop/customer/orders.html"/>';
				}else {
					activeFaild(response.response.statusMessage);
				}
			} ,
			error: function( textStatus, errorThrown) {
				activeFaild(textStatus);
			 }
		});
	});
}
</script>
						
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" %>  
<%
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
%>
<%@ page session="false" %>
<c:forEach items="${customerOrders.ezybioOrder}" var="order" varStatus="orderStatus">
		
		<div style="width:100%;height:38px;background-color: #d9d9d9;line-height:38px;">
				<a href="${requestScope.customerStoreOrder}?orderId=${order.orderID}">
					<span style="padding-left:24px;"><fmt:formatDate type="both" pattern="yyyy-MM-dd" value="${order.datePurchased}" /></span>
					<span style="padding-left:32px;">订单号：${order.orderID}</span>
					<span style="padding-left:32px;">您订单中的商品在不同库房或属不同商家，故拆分为以下订单分开配送</span>
					<span style="padding-left:32px;">订单金额：<sm:monetary value="${order.total}"/></span>
				</a>
			</div>
		<c:forEach items="${order.subOrders}" var ="store">
			<c:forEach items="${store.cartItems}" var ="product" begin="0" end="0">
			<div style="width:100%;height:138px;border:1px solid #d9d9d9;font-size:14px;">
				<div style="margin-top:20px;margin-left:22px;float:left;">
					<a href='<c:url value="/shop/product/" />${product.productId}.html<sm:breadcrumbParam/>'>
						<img style="width:98px;height:98px;" src='<c:url value="${product.image}"/>'/>
					</a>
				</div>
				<div style="margin-top:63px;margin-left:28px;float:left;">
					<div>${product.name}</div>
					<div>500mL</div>
				</div>
				<div style="margin-top:63px;margin-left:64px;float:left;">${product.quantity}</div>
				<div style="margin-top:63px;margin-left:80px;float:left;"><sm:monetary value="${product.price}"/></div>
				<div style="margin-top:63px;margin-left:64px;float:left;"><sm:monetary value="${product.subTotal}"/></div>
				<div style="margin-top:63px;margin-left:62px;float:left;">
					<div><s:message code="label.order.${store.status}" text="label [${store.status}] not found"/></div>
					<div><a href="#">查看物流</a></div>
				</div>
				<div style="margin-top:63px;margin-left:72px;float:left;">
					<c:choose>
							<c:when test="${product.reviewed==1 }">
								<a class="btn btn-default btn-sm" target="_blank" href="<c:url value="/shop/customer/review.html"/>?productId=${product.productId}&orderProductId=${product.id}">
									<s:message code="label.product.view.review" text="View Review"/>
								</a>
							</c:when>
							<c:otherwise>
							    <c:if test="${store.status=='SHIPPED'}">
							   	 <a class="btn btn-info btn-sm" href="javascript:confirmReceived(${order.orderID},${store.id})">确认收货</a>
							    </c:if>
								<c:if test="${store.status != 'ORDERED' and store.status!='SHIPPED'}">
									<a class="btn btn-warning btn-sm" target="_blank" href="<c:url value="/shop/customer/review.html"/>?productId=${product.productId}&orderProductId=${product.id}">
										<s:message code="label.product.rate" text="Review"/><span class="badge" style="margin:2px;padding:3px;font-size:5px;min-width:5px;line-height:5px;">?</span>
									</a>
								</c:if>
							</c:otherwise>
						</c:choose>
				<!-- <a href="#">查看评价</a> -->
				</div>
			</div>
			</c:forEach>
			<c:forEach items="${store.cartItems}" var ="product" begin="1">
				<tr>
					<td width="49%"><a href="<c:url value="/shop/product/" />${product.productId}.html<sm:breadcrumbParam/>"><span>${product.name}</span></a>
						<c:if test="${not empty product.enName }">
							<br/><s:message code="label.productedit.productenname" text="English Name"/>&nbsp;:&nbsp;<span>${product.enName}</span>
						</c:if>
					</td>
					<td width="7%"><sm:monetary value="${product.price}"/></td>
					<td width="7%">${product.quantity}</td>
					<td width="7%"><sm:monetary value="${product.subTotal}"/></td>
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
					//alertSuccess();
					alert("success");
					window.location.href = '<c:url value="/shop/customer/orders.html"/>';
				}else {
					activeFaild(response.response.statusMessage);
				}
				location.reload();
			} ,
			error: function( textStatus, errorThrown) {
				activeFaild(textStatus);
			 }
		});
	});
}
</script>
						
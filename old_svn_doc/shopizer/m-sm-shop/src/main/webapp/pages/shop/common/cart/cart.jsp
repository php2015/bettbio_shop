<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>

<script src="<c:url value="/resources/js/jquery.alphanumeric.pack.js" />"></script>
<script src="<c:url value="/resources/js/cart.js" />"></script>

<script>
	var count=0;
</script>

<c:url value="/shop/cart/removeShoppingCartItem.html"
	var="removeShoppingCartItemUrl" />



<div class="container-fluid" >

<h5 class="cart-title"><s:message code="label.cart.revieworder" text="Review your order" /><br>
<s:message code="label.generic.current" text="Current"/><s:message code="message.order.left" text="Grade"/><strong ><sm:monetary value="${left}" /></strong></h5>
<div id="store.error" class="alert-danger" style="display:none;"><s:message code="message.error.shoppingcart.update" text="An error occurred while updating the shopping cart"/></div>
<br/>
<div class="box">
 	<c:choose>
	     <c:when test="${not empty cart}">
	
		   <c:choose>
		     <c:when test="${not empty cart.subOrders }">
		     	<c:forEach items="${cart.subOrders}" var="subOrders" varStatus="itemStatus">
			     	
							 <c:if test="${itemStatus.index !=0}">
							 	<br>
							</c:if>
							
						
		     			<div class="row" style="background-color: #F3F3FA;">
		     			<div class="col-xs-2" style="text-align:left;" id="${subOrders.merchantStore.id}"><input type="checkbox" checked="checked" class="storecheckbox" id="store_${subOrders.merchantStore.id}"></div>
		     			<div class="col-xs-10">${subOrders.merchantStore.storename}</div>
		     		</div>
		     		<c:forEach items="${subOrders.shoppingCartItems}" var="shoppingCartItem" varStatus="itemStatus">
		     			<form:form action="${updateShoppingCartItemUrl}" id="shoppingCartLineitem_${shoppingCartItem.id}">
		     			<div class="row" style="padding-top:10px">
							<script>
							count++;
							</script>
							<div class="col-xs-2" id="${shoppingCartItem.id}" >
								<input type="checkbox" class="everycheckbox" name="check_${subOrders.merchantStore.id}" id="${subOrders.merchantStore.id}" checked="checked" >
								</div>
							<div class="col-xs-3">
								<c:choose>
									<c:when test="${shoppingCartItem.image!=null}">
										<img width="50" src="<c:url value="${shoppingCartItem.image}"/>">
									</c:when>
									<c:otherwise>
										<img width="50" src="<c:url value="/resources/img/pimg.jpg" />">
									</c:otherwise>
								</c:choose>		
							</div>
							<div class="col-xs-7"  >
								<div class="row">
									<div class="col-xs-12" style="border-left:none;word-break: break-all; word-wrap:break-word;" >
										<a href='<c:url value="/shop/product/${shoppingCartItem.friendlyUrl}.html"/>' target="blank"><span style="font-weight:bold">${shoppingCartItem.name}</span>
										<c:if test="${not empty shoppingCartItem.enName }">
											<br/><span>${shoppingCartItem.enName}</span>
										</c:if>
										</a>								
									</div>
									<div class="col-xs-6">
										${shoppingCartItem.specs}
									</div>
									<div class="col-xs-6">
										<strong name="price" id="price_${shoppingCartItem.id}">${shoppingCartItem.price}</strong>
									</div>									
								</div>
								
							</div>
							<div class="col-xs-7" style="text-align:left;">
										<div class="input-group input-group-sm">
									      <span class="input-group-addon">
									        <button class="btn btn-block btn-xs"  type="button" onclick="minus(${shoppingCartItem.id})"><span class=" fa fa-minus icon"/></button>
									      </span>
									      <input type="text" class="form-control" placeholder="<s:message code="label.quantity" text="Quantity"/>" value="${shoppingCartItem.quantity}" name="quantity" id="quantity_${shoppingCartItem.id}">
									      <span class="input-group-addon">
									       <span class="input-group-btn"><button class="btn btn-block btn-xs" type="button" onclick="doplus(${shoppingCartItem.id})" ><span class=" fa fa-plus icon"/></button></span>
									      </span>
									    </div>
									</div>
									<div class="col-xs-5" style="text-align:right;">
										<button type="button" class="btn btn-danger"	onclick="javascript:updateLineItem('${shoppingCartItem.id}','${removeShoppingCartItemUrl}');"><span aria-hidden="true" class="carticon fa fa-trash-o fa icon" ></span></button>
									</div>
									<input type="hidden" name="lineItemId" id="lineItemId" value="${shoppingCartItem.id}"/>							
							</div>	
						</form:form>	
					
		     		</c:forEach>
		     	</c:forEach>
		     </c:when>
		     <c:otherwise>
		     	<div class="text-center">
		     		<s:message code="cart.empty" text="Your Shopping cart is empty" />
		     	</div>
		     </c:otherwise>
		    </c:choose>
		   </c:when> 
	    <c:otherwise>
		     	<div class="text-center">
		     		<s:message code="cart.empty" text="Your Shopping cart is empty" />
		     	</div>
		     </c:otherwise>
		     </c:choose> 

</div>


<c:if test="${not empty cart}">
	<c:if test="${not empty cart.subOrders}">
	<form  action="<c:url value="/shop/order/checkout.html"/>" id="checkout" method="post">
	<input type="hidden" name="selecteditem" id="selecteditem" value="cdceab"/>
		<div class="navbar-fixed-bottom container-fluid" style="bottom:40px;border: 1px solid #FFF3EE;background:#FCFCFC;z-index:9999">
		<div class="row">
			<div class="col-xs-2">
				<input type="checkbox" id="selectall" checked="checked">
			</div>
			<div class="col-xs-5" >
							<span ><strong><s:message code="order.total.total" text="sum"/></strong>:<span name="totalmoneyculc"><strong >${cart.total}</strong></span></span>
			</div>
			<div class="col-xs-5 " style="text-align:right;">
				<button type="button" class="btn btn-success" onClick="javascript:dosubmit();"><s:message code="label.cart.placeorder" text="Place your order" /></button>
			</div>
			</div>
		</div>		
	</<form >	
	</c:if>
</c:if>
<br>
<br>
</div>

<c:if test="${empty cart}">
<!-- load cart with cookie -->
<script>
  $(document).ready(function(){
		var cartCode=getCartCode();
		if(cartCode!=null) {
			console.log('cart code ' + cartCode);
			location.href='<c:url value="/shop/cart/shoppingCartByCode.html" />?shoppingCartCode=' + cartCode;
		}
		

   });
</script>
</c:if>

<script>
	 
	  var msgCart='<s:message code="message.order.selected" text="please select products"/>';


</script>



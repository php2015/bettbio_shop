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



<div class="box main-padding-lr" >

<h2 class="cart-title"><s:message code="label.cart.revieworder" text="Review your order" />&nbsp;&nbsp;
<s:message code="label.generic.current" text="Current"/><s:message code="message.order.left" text="Grade"/><strong ><sm:monetary value="${left}" /></strong></h2>
<div id="store.error" class="alert-danger" style="display:none;"><s:message code="message.error.shoppingcart.update" text="An error occurred while updating the shopping cart"/></div>
<br/>
<table id="mainCartTable" class="table table-bordered table-striped table-font" style="word-wrap:break-word;">

	<c:if test="${not empty cart}">
	   <c:choose>
	     <c:when test="${not empty cart.subOrders }">
	     			<thead>
						<tr>
							<th width="2%"><input type="checkbox" id="selectall" checked="checked"> </th>
							<th colspan="2" width="45%"><s:message code="label.generic.item.title" text="Item"/></th>
							<th width="18%"><s:message code="label.product.specification" text="Specs"/></th>
							<th colspan="2" width="5%"><s:message code="label.quantity" text="Quantity"/></th>
							<th width="15%"><s:message code="label.generic.price" text="Price"/></th>
							<th width="15%"><s:message code="label.order.total" text="Total"/></th>
						</tr>
					</thead>
					<tbody>
	     	<c:forEach items="${cart.subOrders}" var="subOrders" varStatus="itemStatus">
			<tr>
				<td width="2%" id="${subOrders.merchantStore.id}"><input type="checkbox" checked="checked" class="storecheckbox" id="store_${subOrders.merchantStore.id}"> </td>
				<td colspan="7" width="98%"><s:message code="menu.store" text="Store"/>:${subOrders.merchantStore.storename}</td>
			</tr>
			<c:forEach items="${subOrders.shoppingCartItems}" var="shoppingCartItem" varStatus="itemStatus">
				<form:form action="${updateShoppingCartItemUrl}" id="shoppingCartLineitem_${shoppingCartItem.id}">
				<script>
				count++;
				</script>
					<tr >
						<td width="2%"> <input type="checkbox" class="everycheckbox" name="check_${subOrders.merchantStore.id}" id="${subOrders.merchantStore.id}" checked="checked" ></td>
						<td width="10%">
							<c:if test="${shoppingCartItem.image!=null}">
								<img width="60" src="<c:url value="${shoppingCartItem.image}"/>">
							</c:if>
						</td>

						<td style="border-left:none;word-break: break-all; word-wrap:break-word;" width="35%">
								<a href='<c:url value="/shop/product/${shoppingCartItem.productId}.html"/>' target="blank"><span style="font-weight:bold">
								${shoppingCartItem.productCode}<br/>
								${shoppingCartItem.name}</span></a>
								<c:if test="${not empty shoppingCartItem.enName }">
									<br/><s:message code="label.productedit.productenname" text="English Name"/>&nbsp;:&nbsp;<span>${shoppingCartItem.enName}</span>
								</c:if>	
						
						</td>
						<td width="18%">
							${shoppingCartItem.specs}
						</td>
						<td width="3%">
							<div class="input-group" style="width:30px;">
								  <span class="input-group-btn"><button class="btn btn-info" type="button" onclick="minus(${shoppingCartItem.id})"><span class=" fa fa-minus icon"/></button></span>
								<input type="text" style="height:28px;width:30px;text-align:center;" placeholder="<s:message code="label.quantity" text="Quantity"/>"
									value="${shoppingCartItem.quantity}" name="quantity" id="quantity_${shoppingCartItem.id}">
									 <span class="input-group-btn"><button class="btn btn-success" type="button" onclick="doplus(${shoppingCartItem.id})" ><span class=" fa fa-plus icon"/></button></span>
							</div>
						</td>
						<td style="border-left:none;" width="2%"><button class="close"
								onclick="javascript:updateLineItem('${shoppingCartItem.id}','${removeShoppingCartItemUrl}');">&times;</button>
						</td>

						<td  width="15%"><strong name="price">${shoppingCartItem.price}</strong></td>
						<td  width="15%"><strong name="totalmeony">${shoppingCartItem.subTotal}</strong></td>


						<input type="hidden" name="lineItemId" id="lineItemId"
							value="${shoppingCartItem.id}"/>
					</tr>
					</form:form>
				</c:forEach>
			</c:forEach>
			<!-- c:forEach items="${cart.totals}" var="total"-->
				<tr class="subt">
					<td colspan="6">&nbsp;</td>
					<td colspan="1"><strong><s:message code="order.total.total" text="sum"/></strong></td>
					<td name="totalmoneyculc"><strong >${cart.total}</strong></td>
				</tr>
			<!-- /c:forEach-->
			
		</c:when>
		 <c:otherwise>
		   <tr><td><s:message code="cart.empty" text="Your Shopping cart is empty" /></td></tr>
		 </c:otherwise>
	   </c:choose>
	</c:if>


	</tbody>
</table>
<c:if test="${not empty cart}">
	<c:if test="${not empty cart.subOrders}">
	<form  action="<c:url value="/shop/order/checkout.html"/>" id="checkout" method="post">
	<input type="hidden" name="selecteditem" id="selecteditem" value="cdceab"/>
		<div class="pull-right">
			<div class="form-actions">
				<button type="button" class="btn btn-success" onClick="javascript:dosubmit();"><s:message code="label.cart.placeorder" text="Place your order" /></button>
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
			//console.log('cart code ' + cartCode);
			location.href='<c:url value="/shop/cart/shoppingCartByCode.html" />?shoppingCartCode=' + cartCode;
		}
		

   });
</script>
</c:if>

<script>
	 
	  var msgCart='<s:message code="message.order.selected" text="please select products"/>';


</script>



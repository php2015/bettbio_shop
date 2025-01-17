<%-- 
	not use order.jsp any more. Now for new admin order-page
	
<jsp:include page="/pages/shop/common/customer/order.jsp"/>
--%>
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
<!-- From src\main\webapp\pages\shop\common\customer\order.jsp -->
<script src="<c:url value="/resources/js/jquery.printElement.min.js" />"></script>
<script src="<c:url value="/resources/js/browser.js" />"></script>
<script src="<c:url value='/resources/js/editOrder.js' />?v=<%=new java.util.Date().getTime()%>"></script>
<script SRC="<c:url value='/resources/js/selectList.js' />"></script>

<script type="text/javascript">
var subOrderid;
function print(Print) {
	console.log('Print');
	$("#printableOrder").hide();
	$("#e").hide();
	$("#f").hide();
	$('#pageContext').printElement();
	$("#printableOrder").show();
	$("#e").show();
	$("#f").show();
	//$('#printableOrder').printElement();
}
function doDelivery(oid){
	subOrderid=oid;
	$('#delivery').modal('show');
}
function getContextPath() {
   return "${pageContext.request.contextPath}";
}
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

function deliveryOrder(_url){
	$('#delivery').modal('hide');
	$('#order_detail_div').showLoading();
	$.ajax({  
		type: 'POST',
		  url: _url,
		  data:"suborderid="+ subOrderid+"&dCode="+$('#searchstorename').val()+"&dNo="+$('#deliveryNo').val(),
		  success: function(response) {
			  if(response.response.status>=1) {
				  location.replace(location.href);
			  }else {
				  //activeFaild();
				  alert("操作失败，请检查输入的信息是否正确");
			  }
			  $('#order_detail_div').hideLoading();
			 } ,
		 error: function( textStatus, errorThrown) {
			$('#order_detail_div').hideLoading();			 
			activeFaild();
		 }
		 	});
}
var savePriceUrl="<c:url value='/admin/order/savePrice.html'/>";
var orderActionUrl="<c:url value='/admin/order/action/'/>";
OrderHandler.setOrderId(${order.rawOrder.id});

$(function() {	
	var url=getContextPath()+'/admin/orders/stores.html'
	getStores(url,"自定义");
});
</script>
<script type="text/javascript">

</script>
<style>
.pay_first_order {
	color: red;
}
.ship_first_order {
	color: blue;
}
.order_status_span {
	padding-left: 100px;
}
.subtotal_div{
	display: inline;
}
.adjust_price_affected {
	border: 1px solid red;
}
.adjust_price_modified{
	background-color: red;
	color: white;
}
</style>
	<div style="background-color: blue; position: fix; top: 120px; right: 20px; display: none;"><pre id="debug_pre"></pre></div>
	<div id="order_detail_div" style="padding: 40px;">
		<!-- 订单概览  -->
	<c:set var="rawOrder" value="${order.rawOrder}"/>
	<c:if test="${rawOrder.processType == 'pay_first'}">
		<h2 class="pay_first_order">
			<%--<s:message code="menu.order" text="Order" /><s:message code="menu.order.no" text="No" />:<c:out value="${rawOrder.id}"/>--%>
			订单类型：
			<s:message code="order.processType.pay_first" text="付款后发货" />
			
	</c:if>
	<c:if test="${rawOrder.processType != 'pay_first'}">
		<h2 class="ship_first_order">
			<%-- <s:message code="menu.order" text="Order" /><s:message code="menu.order.no" text="No" />:<c:out value="${rawOrder.id}"/>--%>
			(<s:message code="order.processType.ship_first" text="收货后付款" />)
	</c:if>
		<%--
			<label class="order_status_span"><s:message code="label.entity.status" text="Status"/>:<s:message code="order.status.${rawOrder.processType}.${rawOrder.status}" text="${rawOrder.status}" /></label>
		--%>
			<span id="order_total_span" style="float:right;" data-old-value="${rawOrder.total}" data-cur-value="${rawOrder.total}">
				¥<fmt:formatNumber value="${rawOrder.total}" pattern="#,#00.00#"/>
			</span>
			<span style="float: right;">总价格: &nbsp;&nbsp;</span>
		</h2>
		<hr style="border-top:solid 1px #0080ff;"/>
		下单时间：<fmt:formatDate type="both" dateStyle="long" value="${rawOrder.datePurchased}" />
		<c:if test="${not empty rawOrder.lastModified}">
		&nbsp;
		最后更新时间：<fmt:formatDate type="both" dateStyle="long" value="${rawOrder.lastModified}" />
		</c:if>
		<%--
		<span class="pull-right">
	        <input id="printableOrder" type="button" onclick="print();" value="<s:message code="label.generic.print" text="Print" />" name="Print" class="btn btn-info"/>
	    </span>
		--%>
		<hr style="border-top:solid 1px #0080ff;"/>
		
		<!-- 收货信息  -->
		<table style="width:100%;height:100%;z-index:-1;border-color: #fff" border="1"  cellpadding="0" cellspacing="0">
	        <tr>
	            <c:if test="${not empty order.customerAdress}">
					<td  style="border-color: #fff;">
						<div style="font-size:13px;font-weight:bold;">
							收货信息
						</div>
	               </td>
	            </c:if>
	            <c:if test="${not empty order.customerInvoice}">
					<td  style="border-color: #fff;">
						<div style="font-size:13px;font-weight:bold;"> 
							<s:message code="label.customer.billinginformation" text="Billing" />
						</div>
					</td>
	            </c:if>
	            <c:if test="${not empty order.invoiceAddress}">
					<td  style="border-color: #fff;">
						<div style="font-size:13px;font-weight:bold;">
							<s:message code="label.customer.invoiceaddress" text="Invoice address" />
						</div>
					</td>
	            </c:if>
	        </tr>
	        <tr>
	            <c:if test="${not empty order.customerAdress}">
					<td style="border-color: #fff;padding-top: 0px;">
						<c:set var="address" value="${order.customerAdress}" scope="request" />
						<jsp:include page="/common/customerAddress.jsp"/>
					</td>
	            </c:if>
	           
	            <c:if test="${not empty order.customerInvoice}">
					<td  style="border-color: #fff;padding-top: 0px;"> 
						<div>
							<c:set var="delivery" value="${order.customerInvoice}" scope="request" />
							<jsp:include page="/common/customerInvoice.jsp"/>
						</div>
					</td>
	            </c:if>
	            <c:if test="${not empty order.invoiceAddress}">
					<td  style="border-color: #fff;padding-top: 0px;">
						<div>
							<c:set var="address" value="${order.invoiceAddress}" scope="request" />
							<jsp:include page="/common/customerAddress.jsp"/>
						</div>
					</td>
	            </c:if>
	        </tr>
	    </table>
		
		<br/>

		<!-- 订单详情  -->
		<table  class="table table-bordered table-striped table-hover" 
				style="width:100%;height:100%;z-index:-1;border-color:#fff;" 
				border="1" cellpadding="0" cellspacing="0">

	<c:forEach items="${rawOrder.subOrders}" var="subOrder">
		<c:if test="${subOrder.actionList != null}">
			<tr style="background-color: #337ab7; color: white; font-size: 16px;">
				<td colspan="6">
					<label>订单号：${subOrder.id}</label>
					<label>(${subOrder.storeName})</label>
					<label class="order_status_span"><s:message code="label.entity.status" text="Status"/>:<s:message code="order.status.${rawOrder.processType}.${subOrder.status}" text="${subOrder.status}" /></label>
					<c:if test="${not empty subOrder.payProof}">
						<a href="<c:url value='/orderstatic/'/>${subOrder.payProof}" target="blank">
							<img src="<c:url value='/resources/img/pay_proof.png'/>" height="16px" />
						</a>
					</c:if>
					
				</td>
			</tr>
			<tr>
				<th colspan="2" width="40%"><s:message code="label.productedit.productname" text="Product name" /></th>
				<th width="20%"><s:message code="label.product.specification" text="Specs"/></th>
				<th width="10%">数量<%-- <s:message code="label.quantity" text="Quantity" /> --%></th>
				<th width="10%">单价<%-- <s:message code="label.generic.price" text="Price" /> --%></th>
				<th width="10%">金额<%-- <s:message code="order.total.subtotal" text="Sub-total" /> --%></th>	
			</tr>
		<tbody id="tbody_suborder_${subOrder.id}">
			<c:forEach items="${subOrder.orderProducts}" var="item">
			<tr>
				<td width="10%"> <!-- 商品图片 -->
					<c:if test="${not empty item.productImageUrl}">
						<img width="60" src='<c:url value="${item.productImageUrl}"/>'/>
					</c:if>
					
				</td>
				<td> <!-- 商品信息 -->
					<a target="blank" href="<c:url value="/shop/product/" /><c:out value="${item.productID}"/>.html">
						<span style="font-weight:bold"><c:out value="${item.productCode}"/></span>
						<br/>
						<span style="font-weight:bold"><c:out value="${item.productName}"/></span>
					</a>
					<c:if test="${not empty item.productEnName }">
						<br/><s:message code="label.productedit.productenname" text="English Name"/>&nbsp;:&nbsp;<span>${item.productEnName}</span>
					</c:if>
				</td>
				<td><c:out value="${item.specifications}"/></td><!-- 商品规格 -->
				<td><c:out value="${item.productQuantity}"/></td><!-- 商品数量 -->
				<td><!-- 单价 -->
					<c:if test="${not empty item.finalPrice}">
						<span class="adjust_price_affected">¥<fmt:formatNumber value="${item.finalPrice}" pattern="#,#00.00#"/></span>
						<br/>
						<s style="color: gray;"><s:message code="order.label.price.orignal" text="原价"/>: ¥<fmt:formatNumber value="${item.price}" pattern="#,#00.00#"/></s>
					</c:if>
					<c:if test="${empty item.finalPrice}">
						<span>¥<fmt:formatNumber value="${item.price}" pattern="#,#00.00#"/></span>
					</c:if>
				</td>
				<td><!-- 小计 -->
					<c:if test="${not empty item.finalTotal}">
						<span class="adjust_price_affected">¥<fmt:formatNumber value="${item.finalTotal}" pattern="#,#00.00#"/></span>
						<br/>
						<s style="color: gray;"><s:message code="order.label.price.orignal" text="原价"/>: ¥<fmt:formatNumber value="${item.oneTimeCharge}" pattern="#,#00.00#"/></s>
					</c:if>
					<c:if test="${empty item.finalTotal}">
						<span>¥<fmt:formatNumber value="${item.oneTimeCharge}" pattern="#,#00.00#"/></span>
					</c:if>
				</td>
			</tr>
			</c:forEach>
		</tbody>
			<tr>
				<td colspan="4"></td>
				<td colspan="2">
					<label style="float: rihgt;">合计：</label>
					<div class="subtotal_div">
						<c:if test="${not empty subOrder.finalTotal}">
							<span class="adjust_price_affected">¥<fmt:formatNumber value="${subOrder.finalTotal}" pattern="#,#00.00#"/></span>
							<br/>
							<s style="color: gray;"><s:message code="order.label.price.orignal" text="原价"/>: ¥<fmt:formatNumber value="${subOrder.total}" pattern="#,#00.00#"/></s>
						</c:if>
						<c:if test="${empty subOrder.finalTotal}">
							<span>¥<fmt:formatNumber value="${subOrder.total}" pattern="#,#00.00#"/></span>
						</c:if>
					</div>
				</td>
			</tr>
			
		</c:if>
		
	</c:forEach>
	
		</table>
		
		<c:if test="${not empty logList}">
		<hr style="border-top:solid 1px #0080ff;"/>
		<div><label>订单记录</label></div>

		<div style="max-height: 400px; overflow: auto;">
			<table class="table table-hover">
				<tr><th>日期</th><th>状态</th><th>子订单</th><th>说明</th></tr>
				<c:forEach items="${logList}" var="log">
				<tr>
					<td><fmt:formatDate type="both" dateStyle="long" value="${log.dateAdded}" /></td>
					<td><s:message code="order.status.${rawOrder.processType}.${log.status}"/></td>
					<td>${log.subOrderId}</td>
					<td>${log.comments}</td>
				</tr>
				</c:forEach>
			</table>
		</div>
		</c:if>
	</div>
	
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


	<!--close .container "main-content" -->

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

<script src="<c:url value="/resources/js/jquery.printElement.min.js" />"></script>
<script src="<c:url value="/resources/js/browser.js" />"></script>

<script type="text/javascript">
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
<script type="text/javascript">
  $(function(){
     //伪算计
    /*  var total =$("#subtotal").html()
     .substr($("#subtotal").html()
     .indexOf("￥")+1,
     $("#subtotal").html().length);
     
     var temp = total.split(",");
     var teval="";
     for(var i=0;i<temp.length;i++)
     {
        teval+=""+temp[i]+"";
     }
     total = parseFloat(teval); */
     var fprice =0;
     var ftotalprice=0;
     $("#e td input").focus(function(){
        var tempprice = $(this).attr("title");
        if(tempprice=="nowprice")
        {
           fprice =$(this).val();
        }
        else
        {
           ftotalprice =$(this).val();
        }
       
     });
 /*      */
     $("#e td input").keyup(function(){
        var tempprice = $(this).attr("title");
        if(tempprice=="nowprice")
        {
           var num = $(this).parent().next().children("#num").html();
           num=parseFloat(num);
           if(parseFloat($(this).val())>=parseFloat(fprice))
           {
               $(this).parent().next().next()
              .children("#nowtotalprice")
              .val(""+(parseFloat($(this).val())*num).toFixed(2)+"");
               var temptotalprice ="";
		       $("#nowtotalprice").each(function(){
		            temptotalprice+=parseFloat($(this).val()).toFixed(2);
		       });
              $("#subtotal").html("￥"+temptotalprice+"");
           }
           else
           {
              $(this).parent().next()
              .next().children("#nowtotalprice")
              .val(""+(parseFloat($(this).val())*num).toFixed(2)+"");
               var temptotalprice =""
		       $("#nowtotalprice").each(function(){
		            temptotalprice+=parseFloat($(this).val()).toFixed(2);
		       });
              $("#subtotal").html("￥"+temptotalprice+"");
           }
        }
        else
        {
           var num = $(this).parent().prev().children("#num").html();
           num=parseFloat(num);
           if(parseFloat($(this).val())>=parseFloat(ftotalprice)){
              $(this).parent().prev().prev()
              .children("#nowprice").val(""+(parseFloat($(this).val())/num).toFixed(2)+"");
               var temptotalprice =0;
		       $("#nowtotalprice").each(function(){
		            temptotalprice+=parseFloat($(this).val()).toFixed(2);
		       });
		       var fex = temptotalprice;
	           if(parseFloat(temptotalprice.substr(0,1))==0){
	               fex = fex.substr(1,temptotalprice.length);
	           }
               $("#subtotal").html(""+fex+"");
           }
           else
           {
              $(this).parent().prev()
              .prev().children("#nowprice").val(""+(parseFloat($(this).val())/num).toFixed(2)+"");
               var temptotalprice =""
		       $("#nowtotalprice").each(function(){
		            temptotalprice+=parseFloat($(this).val()).toFixed(2);
		       });
		       var fex = temptotalprice;
	           if(parseFloat(temptotalprice.substr(0,1))==0){
	               fex = fex.substr(1,temptotalprice.length);
	           }
               $("#subtotal").html(""+fex+"");
           }
        }
     });
     $("#e td button").click(function(){
        if($(this).html()=="修改"){
         $(this).parent().
         parent().children().
         children("#price_product").
         hide();
         
         $(this).parent().
         parent().children().
         children("#nowprice").show().
         val(""+$(this).parent().
         parent().children().
         children("#price_product").html()+"").
         focus(function(){
          $(this).parent().
          parent().children().
          children("#updateOrderByMoney").
          html("完成");
         });
         
         $(this).parent().
         parent().children().
         children("#product_sub").hide();
         
         $(this).parent().
         parent().children().
         children("#nowtotalprice").show().
         val(""+$(this).parent().
         parent().children().
         children("#product_sub").html()+"").
         focus(function(){
            $(this).parent().parent().
            children().
            children("#updateOrderByMoney").
            html("完成");
         });
        }
     });
     $("#e td button").click(function(){
       if($(this).html()=="完成"){
         $(this).html('修改');
         var pid =$(this).parent().parent().
            children().
            children("#orderProductId").
            val();
         var nowp=$(this).parent().parent().
            children().
            children("#nowprice").
            val();
         var nowtotal=$(this).parent().parent().
            children().
            children("#nowtotalprice").
            val();
         $.ajax({
              url:'/sm-shop/admin/orders/updateMoney.html',
			  type:'post',
			  data:{
	                   "orderproductid":""+pid+"",
	                   "orderprice":""+nowp+"",
	                   "nowtotalprice":""+nowtotal+""
	                },
              dataType:'json',
              async:true,
              cache:true,
              success:function(data){
                 if(data.response.status == 2 || data.response.status=="2")
                 {
                     alert("对不起，您不是该订单的商家!");
                     $("#e td").each(function(){
	                     $(this).children("#nowprice").hide();
	                     $(this).children("#nowtotalprice").hide();
	                     $(this).children("#product_sub").show();
	                     $(this).children("#price_product").show(); 
                    });
                 }
                 else if(data.response.status == 1 || data.response.status=="1")
                 {
                     alert("订单价格修改失败!");
                     $("#e td").each(function(){
	                     $(this).children("#nowprice").hide();
	                     $(this).children("#nowtotalprice").hide();
	                     $(this).children("#product_sub").show();
	                     $(this).children("#price_product").show(); 
                    });
                 }
                 else
                 {
                      alert("订单价格修改成功!");
                      window.location=""+window.location.href+"";
                 }
              }
             
         });
         }
     });
  });
</script>
	<%-- <div class="customer-box row">

			<div class="col-sm-12">
				<h2><s:message code="label.order.details" text="Order details" />&nbsp;#&nbsp;<s:message code="menu.order" text="Order" /><s:message code="menu.order.no" text="No" />:<c:out value="${order.orderID}"/></h2>
				
                <hr style="border-top:solid 1px #0080ff;">
			</div>	
				
						
			<div class="col-sm-10">
				<fmt:formatDate type="both" dateStyle="long" value="${order.datePurchased}" />&nbsp;&nbsp;<s:message code="menu.order" text="Order"/><s:message code="label.entity.status" text="Status"/>:
					<s:message code="label.order.${order.orderStatus}" text="${order.orderStatus}" />	
					
			</div>
			<div class="col-sm-2 text-right ">
				<input id="printableOrder" type="button" onclick="print();" value="<s:message code="label.generic.print" text="Print" />" name="Print" class="btn btn-info">
			</div>
			
			<div class="col-sm-12">
			<hr style="border-top:solid 1px #0080ff;">
			</div>
			 
			<div class="col-sm-4">
				<c:if test="${not empty order.customerAdress}">
				<h5><strong>收货信息</strong></h5>
				
					<c:set var="address" value="${order.customerAdress}" scope="request" />
					<jsp:include page="/common/customerAddress.jsp"/>
				
				</c:if>
			</div>	
			<div class="col-sm-4">
				<c:if test="${not empty order.customerInvoice}">
				<h5><strong><s:message code="label.customer.billinginformation" text="Shipping address" /></strong></h5>
				
					<c:set var="delivery" value="${order.customerInvoice}" scope="request" />
					<jsp:include page="/common/customerInvoice.jsp"/>
				
				</c:if>
			</div>			
		<div class="col-sm-4">
			<c:if test="${not empty order.invoiceAddress}">
			<h5><strong><s:message code="label.customer.invoiceaddress" text="Shipping address" /></strong></h5>
				<c:set var="address" value="${order.invoiceAddress}" scope="request" />
				<jsp:include page="/common/customerAddress.jsp"/>
			
			</c:if>
		</div>		
					
					
			

				
	<div class="col-sm-4">

					<div id="orderTableTitle">
						<h4>
						<s:message code="label.entity.details" text="Details"/>
						</h4>
					</div>
		</div>
					<!-- PRODUCTS TABLE -->
					<div id="cartContent" style="padding:10px">
						<!-- cart header -->
						<table class="table table-bordered table-striped table-hover" >
							<tr>
								<th colspan="2" width="40%"><s:message code="label.productedit.productname" text="Product name" /></th>
								<th width="20%"><s:message code="label.product.specification" text="Specs"/></th>
								<th width="10%"><s:message code="label.quantity" text="Quantity" /></th>
								<th width="15%"><s:message code="label.generic.price" text="Price" /></th>
								<th width="15%"><s:message code="order.total.subtotal" text="Sub-total" /></th>								
							</tr>
						
						<c:forEach items="${order.subOrders}" var="subOrder">
							<c:forEach items="${subOrder.cartItems}" var="product">
								<tr>
									<td width="10%"><c:if test="${not empty product.image}">
											<img width="60" src='<c:url value="${product.image}"/>'/>
										</c:if>
									</td>
									<td>
										<a target="blank" href="<c:url value="/shop/product/" /><c:out value="${product.friendlyUrl}"/>.html"><span style="font-weight:bold"><c:out value="${product.name}"/></span></a>
										<c:if test="${not empty product.enName }">
											<br/><s:message code="label.productedit.productenname" text="English Name"/>&nbsp;:&nbsp;<span>${product.enName}</span>
										</c:if>
										<br/>
										<c:if test="${product.shoppingCartAttributes !=null}">
											<ul>
											<c:forEach items="${product.shoppingCartAttributes}" var="attribute">
												<li><c:out value="${attribute.attributeName}"/>:&nbsp;<c:out value="${attribute.attributeValue}"/></li>
											</c:forEach>
											</ul>
										</c:if>
									</td>
									<td><c:out value="${product.specs}"/></td>
									<td><c:out value="${product.quantity}"/></td>
									<td><c:out value="${product.price}"/></td>
									<td><c:out value="${product.subTotal}"/></td>
								</tr>
							</c:forEach>
							<tr>
								<td colspan="5"><s:message code="menu.store" text="Store"/>:${subOrder.merchantStore.storename}&nbsp;
								<s:message code="label.entity.status" text="Status"/>:<s:message code="label.order.${subOrder.status}" text="${subOrder.status}" />
									<c:choose>	  							   		 
										<c:when test="${ subOrder.status != 'ORDERED' }">
											&nbsp;&nbsp;<a href="javascript:void(0);"  onclick='deliveryInfo("${subOrder.deliveryNum}","${subOrder.deliveryName}")' class="btn" ><strong><s:message code="label.order.delivery.info" text="Info"/></strong></a>
										</c:when>
										</c:choose>
								</td>
								<td colspan="1"><s:message code="menu.order" text="Order"/><s:message code="label.subtotal" text="Store"/>:<sm:monetary value="${subOrder.total}"/></td>
							</tr>						
						</c:forEach>
					</table>
					<div class="total pull-right">
								<small class="totalItem">
									<font color="red"><s:message code="label.order.titles" text="Total"/>:</font>
									<span><strong><font color="red"><sm:monetary value="${order.total}" /></font></strong></span>
								</small>
						</div>
						<!-- /cart total -->

		
	</div>
	</div> --%>
	<div id="pageContext" style="padding-right:10px;"> 
	  <table style="width:100%;height:100%;z-index:-1;border-color: #ffffff" border="1"  cellpadding="0" cellspacing="0" id="c">
	     <tr>
	       <td  style="border-color: #fff;">
	          <h2>
	             <s:message code="label.order.details" text="Order details" />&nbsp;#&nbsp;<s:message code="menu.order" text="Order" />
	             <s:message code="menu.order.no" text="No" />:<c:out value="${order.orderID}"/>
	          </h2>
	          <hr style="border-top:solid 1px #0080ff;" id="e"/>
	       </td>
	     </tr>
	     <tr>
	       <td  style="border-color: #fff;">
	         <span class="pull-left">
		         <fmt:formatDate type="both" dateStyle="long" value="${order.datePurchased}" />
		         &nbsp;&nbsp;<s:message code="menu.order" text="Order"/>
		         <s:message code="label.entity.status" text="Status"/>:
				 <s:message code="label.order.${order.orderStatus}" text="${order.orderStatus}" />
			 </span>
	         <span class="pull-right">
	           <input id="printableOrder" type="button" onclick="print();" value="<s:message code="label.generic.print" text="Print" />" name="Print" class="btn btn-info"/>
	         </span>
	       </td>
	     </tr>
	     <tr>
	       <td  style="border-color: #fff;">
	        <hr style="border-top:solid 1px #0080ff;" id="f"/>
	       </td>
	     </tr>
	     <tr>
	       <td  style="border-color: #fff;">
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
	                   <s:message code="label.customer.billinginformation" text="Shipping address" />
	                 </div>
	               </td>
	             </c:if>
	             <c:if test="${not empty order.invoiceAddress}">
	               <td  style="border-color: #fff;">
	                 <div style="font-size:13px;font-weight:bold;">
	                  <s:message code="label.customer.invoiceaddress" text="Shipping address" />
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
	         </td>
	        </tr>
	     <tr>
	       <td  style="border-color: #fff;">
	         <table  class="table table-bordered table-striped table-hover" style="width:100%;height:100%;z-index:-1;border-color:#fff;" border="1" cellpadding="0" cellspacing="0" id="d'">
				<tr>
					<th colspan="2" width="40%"><s:message code="label.productedit.productname" text="Product name" /></th>
					<th width="20%"><s:message code="label.product.specification" text="Specs"/></th>
					<th width="10%">单价<%-- <s:message code="label.generic.price" text="Price" /> --%></th>
					<th width="10%">数量<%-- <s:message code="label.quantity" text="Quantity" /> --%></th>
					<th width="10%">金额<%-- <s:message code="order.total.subtotal" text="Sub-total" /> --%></th>	
				</tr>
				
				<c:forEach items="${order.subOrders}" var="subOrder">
					<c:forEach items="${subOrder.cartItems}" var="product">
						<tr id="e">
							<td width="10%"><c:if test="${not empty product.image}">
									<img width="60" src='<c:url value="${product.image}"/>'/>
								</c:if>
							</td>
							<td>
								<a target="blank" href="<c:url value="/shop/product/" /><c:out value="${product.friendlyUrl}"/>.html"><span style="font-weight:bold"><c:out value="${product.name}"/></span></a>
								<c:if test="${not empty product.enName }">
									<br/><s:message code="label.productedit.productenname" text="English Name"/>&nbsp;:&nbsp;<span>${product.enName}</span>
								</c:if>
								<br/>
								<c:if test="${product.shoppingCartAttributes !=null}">
									<ul>
									<c:forEach items="${product.shoppingCartAttributes}" var="attribute">
										<li><c:out value="${attribute.attributeName}"/>:&nbsp;<c:out value="${attribute.attributeValue}"/></li>
									</c:forEach>
									</ul>
								</c:if>
							</td>
							<td><c:out value="${product.specs}"/><input id="orderProductId" hidden="hidden" value="${product.id}"/></td>
							<td><span id="price_product"><c:out value="${product.price}"/></span><input style="border:1px solid #d9d9d9;border-radius:5px" type="text" hidden="hidden" value="" id="nowprice" title="nowprice"/></td>
							<td><span id="num"><c:out value="${product.quantity}"/></span></td>
							<td><span id="product_sub"><c:out value="${product.subTotal}"/></span><input style="border-radius:5px;border:1px solid #d9d9d9;" type="text" hidden="hidden" value="" id="nowtotalprice" title="nowtotalprice"/></td>
					</c:forEach>
					<tr>
						<td colspan="5">
						 <div style="line-height: 30px">
							 <s:message code="menu.store" text="Store"/>:${subOrder.storeName}&nbsp;
							 <s:message code="label.entity.status" text="Status"/>:<s:message code="label.order.${subOrder.status}" text="${subOrder.status}" />
								<c:choose>	  							   		 
									<c:when test="${ subOrder.status != 'ORDERED' }">
										&nbsp;&nbsp;<a href="javascript:void(0);"  onclick='deliveryInfo("${subOrder.deliveryNum}","${subOrder.deliveryName}")' class="btn" ><strong><s:message code="label.order.delivery.info" text="Info"/></strong></a>
									</c:when>
								</c:choose>
						</div>
						</td>
					    <td colspan="2">
					     <div >
					                  订单总价格：
					       <span style="color:#ff313b;" id="subtotal">
					          <sm:monetary value="${subOrder.total}"/>
					       </span>
					       &nbsp;
					       <%-- <sec:authorize access="!hasRole('ADMIN') and fullyAuthenticated">
					         <button id="updateOrderByMoney" type="button" class="btn btn-default">修改</button>
					         <button id="updateOrder" type="button" class="btn btn-default">完成</button>
					      </sec:authorize> --%>
					     </div>
					    </td>
					</tr>
				</c:forEach>
				
			</table>
	       </td>
	     </tr>
	     <%-- <tr>
	       <td>
	         <div style="width:100%;background-color: #fafafa;height: 40px;line-height: 40px;border: 1px solid #d9d9d9;border-top: 0px;font-size:16px;margin-top:-1px;">
				<small class="totalItem">
					<font color="red"><s:message code="label.order.titles" text="Total"/>:</font>
					<span><strong><font color="red"><sm:monetary value="${order.total}" /></font></strong></span>
				     <div class="total pull-right">
				        <span style="color:#333333;padding-right:40px">订单总价格：<strong><font color="red"><sm:monetary value="${order.total}" /></font></strong></span>
				     </div>
				</small>
			</div>
	       </td>
	     </tr> --%>
	  </table>
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

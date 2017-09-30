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

<span id="sp_adminName" hidden="hidden">
   <sec:authentication property="principal.username" />
</span>
<c:forEach items="${customerOrders.ezybioOrder}" var="order" varStatus="orderStatus">

		<tr class="order-detail-title" ><!-- order -->
		  <td colspan="5" style="padding-left: 5px; margin-left: 0px;font-weight:bold;background-color:#F1F5FB">
		    <div class="row">
		      <div class="col-sm-12">
		         <c:forEach var="sub" items="${order.subOrders}">
		          <input type="text" hidden="hidden" id="suborderid" value="${sub.id}"/>
		          </c:forEach>
			      <a href="${requestScope.customerStoreOrder}?orderId=${order.orderID}"><fmt:formatDate type="date" pattern="yyyy-MM-dd HH:mm:ss" dateStyle="default" value="${order.datePurchased}" /> &nbsp;<s:message code="menu.order" text="Order"/><s:message code="menu.order.no" text="No"/>:
				     ${order.orderID}&nbsp;<s:message code="menu.order" text="Order"/><s:message code="label.entity.status" text="Status"/>:<s:message code="label.order.${order.orderStatus}" text="label [${order.orderStatus}] not found"/>&nbsp;<s:message code="label.customer.shipping.firstname" text="Name"/>:${order.billName}</a>
			  </div>
			 <%--  <div class="col-sm-2">
			  <sec:authorize access="!hasRole('ADMIN') and fullyAuthenticated">
			    <div style="padding-left:80px;width: auto">
			      <button id="wt" type="button" class="btn btn-warning" title="订单编号:${order.orderID}"  
				      data-container="body" data-toggle="popover" data-placement="left" data-html="true"
				      data-content="">
				                    修改订单总额
				  </button>
				</div>
			  </sec:authorize>
			  </div> --%>
              </div>
            <%--   <input id="totalMoney" hidden="hidden" type="text" value="${order.total}"/>
			   <script>
			      $(function () 
			      {
			         $("[data-toggle='popover']").popover();
			         $("button").attr("data-content","总金额:￥"+$("#totalMoney").val()+"<br/>调整额度:<div class='input-group' style='padding-bottom:5px'><span class='input-group-addon'>￥</span><input type='text' id='nowMoney' class='form-control'><span class='input-group-addon'>.00</span></div><div style='padding-left:120px'><button type='button' class='btn btn-default' id='submit'>确认</button>&nbsp;&nbsp;&nbsp;<button id='reset' type='button' class='btn btn-default'>重置</button></div>");
			         //修订总金额点击事件:
			         $("#wt").on('shown.bs.popover', function () {
			         $("#submit").click(function(){
			            if($("#suborderid").val() != '' && new RegExp("^[0-9]{1,}$").test(parseInt($("#suborderid").val()))){
			             $.ajax({
			                url:'/sm-shop/admin/orders/updateMoney.html',
			                type:'post',
			                data:{
			                   "suborderid":""+$("#suborderid").val()+"",
			                   "totalMoney":""+$("#nowMoney").val()+"",
			                   "userName":""+$("#sp_adminName").html()+""
			                },
			                dataType:'json',
			                async:true,
			                cache:true,
			                success:function(data){
			                   if(data.response.status == 2 || data.response.status=="2")
			                   {
			                      $("#wt").hide();
			                      alert("对不起，您不是该订单的商家!");
			                      return false;
			                   }
			                   else if(data.response.status == 1 || data.response.status=="1")
			                   {
			                       $("#wt").hide();
			                       alert("订单价格修改失败!");
			                       return false;
			                   }
			                   else
			                   {
			                        alert("订单价格修改成功!");
			                        window.location=""+window.location.href+"";
			                        return true;
			                   }
			                },
			             
			             });
			             }
			             else
			             {
			                alert("请输入修改后的金额!");
			             }
			         });
			         $("#reset").click(function(){
			             $("#nowMoney").val("");
			         });
			         });
			      });
			    </script> --%>
		  </td>
		</tr>
		
		<c:forEach items="${order.subOrders}" var ="store">
			<c:forEach items="${store.cartItems}" var ="product" begin="0" end="0">
			<tr><!-- order -->
				<td width="59%" style="word-break: break-all; word-wrap:break-word;"><a  href="<c:url value="/shop/product/" />${product.productId}.html<sm:breadcrumbParam/>">${product.name}</a>
				<c:if test="${not empty product.enName }">
					<br/><s:message code="label.productedit.productenname" text="English Name"/>&nbsp;:&nbsp;<span >${product.enName}</span>
				</c:if>
				</td>
					<td width="7%"><sm:monetary value="${product.price}"/></td>
					<td width="7%">${product.quantity}</td>
					<td width="7%"><sm:monetary value="${product.subTotal}"/></td>
					<td width="20%"rowspan="${store.cartItems.size()}">
				<s:message code="label.subtotal" text="Store"/>:<sm:monetary value="${store.total}"/><br>
				<span id="status_${store.id}">
					<s:message code="menu.order" text="Order"/><s:message code="label.entity.status" text="Status"/>:<s:message code="label.order.${store.status}" text="label [${order.orderStatus}] not found"/>
					<c:if test="${store.status=='ORDERED' }">
						<c:if test="${store.cartItems.size()>1}">
							<br>
								<a href="javascript:void(0);" onclick="showSplit(${store.id})"><s:message code="label.order.split" text="Spilt"/></a>
						</c:if>
							<br>
						<a href="javascript:void(0);" onclick="doDelivery(${store.id})"><s:message code="label.order.delivery" text="Delivery"/></a>
					</c:if>
					<c:if test="${store.status=='PAID' }">
					    <%--<c:if test="${store.cartItems.size()>1}">
							<br>
								<a href="javascript:void(0);" onclick="showSplit(${store.id})"><s:message code="label.order.split" text="Spilt"/></a>
						</c:if>--%>
						<br/>
						<a href="javascript:void(0);" onclick="confirmPaid(${order.orderID }, ${store.id})">确认付款</a>
					</c:if>
				</span>
				</td>	
			</tr>
			</c:forEach>
			<c:forEach items="${store.cartItems}" var ="product" begin="1">
				<tr>
					<td width="59%"><a href="<c:url value="/shop/product/" />${product.productId}.html<sm:breadcrumbParam/>">${product.name}</a>
						<c:if test="${not empty product.enName }">
							<br/><s:message code="label.productedit.productenname" text="English Name"/>&nbsp;:&nbsp;<span>${product.enName}</span>
						</c:if>
					</td>
					<td width="7%"><sm:monetary value="${product.price}"/></td>
					<td width="7%">${product.quantity}</td>
					<td width="7%"><sm:monetary value="${product.subTotal}"/></td>																				
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
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
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%> 

<script type="text/javascript">

$(document).ready(function() {
	
	//removeCart();
});

</script>






	<div class="box main-padding-lr" >
		<h2><s:message code="label.checkout.confirmation" text="Order completed" /></h2>
		

          <p class="lead" style="color: black;">
				
				谢谢您从百图生物平台下单。
				<c:if test="${order.rawOrder.subOrders.size() > 1}">
				由于您购买的商品在不同库房或属不同卖家，因此您的订单已被拆分，订单号为
				</c:if>
				<c:if test="${order.rawOrder.subOrders.size() == 1}">
				您的订单号为
				</c:if>
				<c:forEach items= "${order.rawOrder.subOrders}" var="sorder">
					<c:set var="soid" value="${sorder.id}"/>
				</c:forEach>
				<a href="${pageContext.request.contextPath}/shop/customer/order.html?orderId=${soid}&w=${orderid}">
					<c:forEach items="${order.rawOrder.subOrders}" var="subOrder">
					${subOrder.id} &nbsp;
					</c:forEach>
				</a>
			</p>
			<%-- <p><c:out value="${orderemail}" /> </p> --%>
          

          
         
          
          <p class="muted" style="color: black;"><s:message code="label.checkout.additionaltext" text="If you have any comments or suggestions for us, please send us an email with your order id. We value your feedback."/></p>
          
          
           
            
          
          
      </div>
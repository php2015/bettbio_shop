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






	<div class="main-padding-lr" style="width:100%;margin:0;padding:20px;position:relative;">
		<h2><s:message code="label.checkout.confirmation" text="Order completed" /></h2>
		
			 


          <p class="lead"><a href="${pageContext.request.contextPath}/shop/customer/order.html?orderId=${orderid}"><c:out value="${ordermessage}" /></a></p>
          <p><c:out value="${orderemail}" /></p>
          

          
         
          
          <p class="muted"><s:message code="label.checkout.additionaltext" text="If you have any comments or suggestions for us, please send us an email with your order id. We value your feedback."/></p>
          
          
           
            
          
          
      </div>
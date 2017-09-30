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
<c:set var="customerOrder" value="${pageContext.request.contextPath}/shop/customer/order.html"/>
<div class="container-fluid customer-background">
	<div class="row " >
		 <div class="col-xs-2 text-center"><a style="color:#FFFFFF" href="<c:url value="/shop/customer/basicInfo.html" />">
			  <span aria-hidden="true" class="carticon fa fa-user fa-4x icon icon-style" ></span></a>
		  </div>
		  <div class="col-xs-6" style="color:#FFFFFF">
		  	<br>		  
		  		<c:if test="${not empty requestScope.CUSTOMER.nick}">
					<strong><c:out value="${sessionScope.CUSTOMER.nick}" /></strong><br/>
				</c:if>
				<c:forEach begin="0" end="${customer.grade}">
					<img alt="go to order" class="pull-left" src="<c:url value="/resources/img/stars/star-on.png" />">
				</c:forEach>				
		 </div>
		<br>
		  <div class="col-xs-4  text-right" >
		  		 <a style="color:#FFFFFF;" onClick="javascript:location.href='<c:url value="/shop/customer/j_spring_security_logout" />';" href="#" >
			   		<span class="carticon fa fa-sign-out fa-4x icon"/>
			    </a>
		  </div>		
	</div>	
	<div class="row "  style="background-color:rgba(0,0,0,0.2);color:#FFFFFF;padding-top:10px;padding-bottom:10px; ">
		  <div class="col-xs-4 text-center">	
			<c:out value="${quantity}"/><br>
			<s:message code="label.mycart" text="My Cart" />				
		</div>
		<div class="col-xs-4 text-center">	
			<c:out value="${orders}"/><br>
			<s:message code="label.order.titles" text="Orders" />	
		</div>
		<div class="col-xs-4 text-center">	
			<sm:monetary value="${total}" /><br>
			<s:message code="label.order.totals" text="Total Buied" />	
		</div>
	</div>
</div>
<br>
<div class="container-fluid " style="background-color: #f6f6f6;">
	<div class="row" style="padding:10px;background-color: #ffffff;border-bottom:#f6f6f6 1px solid;border-top:#f6f6f6 1px solid;">
		 <div class="pull-left">
		 	<span style="font-size:15px;"> <s:message code="menu.update-basicinfo" text="Info"/></span>
		 </div>
	</div>
	<div class="row "  style="padding:10px;background-color: #ffffff;border-bottom:#f6f6f6 1px solid;">
		  <div class="col-xs-6 ">
		  				<s:message code="label.customer.grade" text="Grade"/><sm:monetary value="${left}" />
		</div>
		<div class="col-xs-6 ">
		  				<s:message code="label.customer.points" text="Point"/>:&nbsp;<c:out value="${queryByMemberPoints}"/>
		  			
		</div>		
	</div>
	<div class="row" style="padding:10px;background-color: #ffffff;border-bottom:#f6f6f6 1px solid;border-top:#f6f6f6 1px solid;">
		 <div class="pull-left">
		 	<a href="<c:url value="/shop/customer/orders.html"/>"><span style="font-size:15px;"> <s:message code="label.generic.receive" text="ALL"/></span></a>	
		 </div>
		 <div class="pull-right" style="padding-top:5px;">
		  <a href="<c:url value="/shop/customer/orders.html"/>"><span class="carticon fa fa-book fa icon"/><span style="font-size:12px;"> <s:message code="label.customer.moreitems" text="ALL"/><s:message code="label.order.title" text="Order"/></span></a>				 	
		</div>
	</div>
	
	<!-- 我的账户 -->
	<div class="row "  style="padding:10px;background-color: #ffffff;border-bottom:#f6f6f6 1px solid;">
		<div class="col-xs-3 text-center">
				 <a style="color:rgb(22, 145, 190)" href="<c:url value="/shop/customer/orders.html?status=ORDERED"/>"><div><span class="carticon fa fa-gift fa-3x icon"/></div>
		  			<div>
		  				<s:message code="order.status.pay_first.ORDERED" text="ORDERED"/>
		  			</div> </a>		
		</div>
		<div class="col-xs-3 text-center">
				 <a style="color:#800080" href="<c:url value="/shop/customer/orders.html?status=SHIPPED"/>"><div><span class="carticon fa fa-truck fa-3x icon"/></div>
		  			<div>
		  				<s:message code="order.status.pay_first.SHIPPED" text="SHIPPED"/>
		  			</div> </a>		
		</div>
		<div class="col-xs-3 text-center">
				 <a style="color:#FF8C00" href="<c:url value="/shop/customer/orders.html?status=PROCESSED&secondStatus=RECEIPT"/>"><div><span class="carticon fa fa-credit-card fa-3x icon"/></div>
		  			<div>
		  				<s:message code="order.status.pay_first.PROCESSED" text="PROCESSED"/>
		  			</div> </a>		
		</div>
		<div class="col-xs-3 text-center">
			 <a style="color:#B22222" href="<c:url value="/shop/customer/orders.html?status=CLOSE"/>"><div><span class="carticon fa fa-comments fa-3x icon"/></div>
		  			<div>
		  				<s:message code="order.status.pay_first.CLOSE" text="CLOSE"/>
		  			</div> </a>		
		</div>
	</div>
	<div class="row" style="padding:10px;background-color: #ffffff;border-bottom:#f6f6f6 1px solid;border-top:#f6f6f6 1px solid;">
		 <div class="pull-left">
		 	<span style="font-size:15px;"> <s:message code="label.customer.myaccount" text="Account"/></span>
		 </div>		
	</div>
	<div class="row "  style="padding:10px;background-color: #ffffff;border-bottom:#f6f6f6 1px solid;">
		  <div class="col-xs-3 text-center">
				 <a style="color:#1E90FF" href="<c:url value="/shop/customer/basicInfo.html"/>"><div><span class="carticon fa fa-edit fa-3x icon"/></div>
		  			<div>
		  				<s:message code="label.customer.info" text="Info"/>
		  			</div> </a>		
		</div>
		<div class="col-xs-3 text-center">
				 <a style="color:#20B2AA" href="<c:url value="/shop/customer/address.html"/>"><div><span class="carticon fa fa-tags fa-3x icon"/> </div>
		  			<div>
		  				<s:message code="label.customer.address" text="Address"/>
		  			</div> </a>		
		</div>
		<div class="col-xs-3 text-center" >
			 <a style="color:#804040" href="<c:url value="/shop/customer/billing.html"/>" ><div><span class="carticon fa fa-inbox fa-3x icon"/></div>
		  			<div>
		  				<s:message code="label.customer.billinginformation" text="Billing"/>
		  			</div> </a>		
		</div>
		<div class="col-xs-3 text-center">
				 <a style="color:#228B22" href="<c:url value="/shop/customer/safetyCenter.html"/>"><div><span class="carticon fa fa-lock fa-3x icon"/></div>
		  			<div>
		  				<span class="fsize12"> <s:message code="menu.safetyCenter" text="Safety Center"/>
		  			</div> </a>		
		</div>	
	</div>
	
	<div class="row "  style="padding:10px;background-color: #ffffff;border-bottom:#f6f6f6 1px solid;">
	
		<div class="col-xs-3 text-center">
				 <a style="color:#F4A460" href="<c:url value="/shop/customer/memberPoint.html"/>"><div><span class="carticon fa fa-tags fa-3x icon"/> </div>
		  			<div>
		  				<s:message code="label.customer.giftSee" text="积分查看"/>
		  			</div> </a>		
		</div>
		<div class="col-xs-3 text-center">
				 <a style="color:#8FBC8F" href="<c:url value="/shop/customer/giftInfo.html"/>"><div><span class="carticon fa fa-tasks fa-3x icon"/></div>
		  			<div>
		  				<s:message code="label.customer.giftInfo" text="兑换信息"/>
		  			</div> </a>		
		</div>
		
	</div>		
	
</div>

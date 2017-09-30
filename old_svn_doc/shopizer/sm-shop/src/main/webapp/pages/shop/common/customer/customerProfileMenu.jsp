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
<!-- ul class="nav nav-list"-->
<ul id="leftNavigation">
	<li <c:if test="${activeMenu=='dashboard'}"> class="active"</c:if>><a href="<c:url value="/shop/customer/dashboard.html"/>"><span class="carticon fa fa-dashboard fa-2x icon"/><span class="fsize12"> <s:message code="label.customer.myaccount" text="My account"/></span></a></li>
	<li <c:if test="${activeMenu=='billing'}"> class="active"</c:if>>
	   <a href="<c:url value="/shop/customer/billing.html"/>">
	   		<span class="carticon fa fa-tags fa-2x icon"/> <span class="fsize12">开票和收货信息</span>
	    </a>
	 </li>
	 <li <c:if test="${activeMenu=='password'}"> class="active"</c:if>><a href="<c:url value="/shop/customer/password.html"/>"><span class="carticon fa fa-key fa-2x icon"/><span class="fsize12"> <s:message code="menu.change-password" text="Change password"/></span></a></li>
	 <li <c:if test="${activeMenu=='basicinfo'}"> class="active"</c:if>><a href="<c:url value="/shop/customer/basicInfo.html"/>"><span class="carticon fa fa-cogs fa-2x icon"/> <span class="fsize12"><s:message code="menu.update-basicinfo" text="Change password"/></span></a></li>
	 <li <c:if test="${activeMenu=='orders'}"> class="active"</c:if>> <a href="<c:url value="/shop/customer/orders.html"/>"><span class="carticon fa fa-briefcase fa-2x icon"/><span class="fsize12"> <s:message code="label.order.recent" text="Recent orders"/></span></a></li>
</ul>
<!-- /ul-->
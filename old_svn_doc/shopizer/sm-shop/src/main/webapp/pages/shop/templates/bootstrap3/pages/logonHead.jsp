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
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>
<%@ taglib uri="/WEB-INF/shopizer-functions.tld" prefix="display" %> 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<nav class="nav-ezybio">
	<div class="container-fluid main-padding-lr" >
		<div class="pull-left" id="logoimg">
          	<a href="<c:url value="/shop/"/>"><c:if test="${not empty requestScope.MERCHANT_STORE.storeLogo}"><img class="logoImage" src="<sm:storeLogo/>" height="70"/></c:if></a> 		</div>
		 <ul class="nav nav-pills pull-right" style="padding-top:25px;font-size: 15px;" id="topmenulist">
			<li><a class="nav-ezybio-font" href="<c:url value="/shop"/>"> <span class="carticon fa fa-home  icon"></span><s:message	code="menu.home" text="Home" /></a></li>
			
			<li><a href="<c:url value="/shop/customer/registration.html" />" class="nav-ezybio-font" > <span class="carticon fa fa-key  icon"></span><s:message code="button.label.register" text="Register" /></a>	</li>
			
				<c:if test="${not fn:contains(requestScope['javax.servlet.forward.servlet_path'], 'order') && not fn:contains(requestScope['javax.servlet.forward.servlet_path'], 'cart')}">
	 				<li class="dropdown" id="open-cart" style="z-index:998" ><a href="#" onclick="viewShoppingCartPage();"	role="button" class="dropdown-toggle noboxshadow nav-ezybio-font"	data-toggle="dropdown"><span class="carticon fa fa-shopping-cart  icon">
	 					</span><s:message code="label.mycart" text="My cart" /><b class="caret"></b><jsp:include page="/pages/shop/common/cart/minicartinfo.jsp" /></a>
	 				<div id="dropcartlist"	class="dropdown-menu pull-right" style="z-index:1001;width:600px">	
						<jsp:include page="/pages/shop/common/cart/minicart.jsp" />
					</div></li>
				</c:if>
						
			<li><a href="<c:url value="/shop/news/list.html"/>" class="nav-ezybio-font"> <span class="glyphicon glyphicon-sunglasses"></span> <s:message code="label.common.news" text="News" /></a></li>
			<li><a href="<c:url value="/shop/marketpoints/list.html"/>" class="nav-ezybio-font"> <span class="glyphicon glyphicon-blackboard"></span> <s:message code="label.points.market" text="Market Points" /></a></li>
			<li id="softlist"><a href="<c:url value="/shop/software/list.html"/>" class="nav-ezybio-font"> <span class="glyphicon glyphicon-download-alt"></span> <s:message	code="label.common.software" text="Common software" /></a></li>
		</ul>
	</div>	
</nav>	
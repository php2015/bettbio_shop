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

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<c:set var="lang" scope="request" value="${requestScope.locale.language}"/> 
 
 <html xmlns="http://www.w3.org/1999/xhtml" style="overflow:auto;"> 
     <head>
        	 	<meta charset="utf-8">
    			<title><c:out value="${requestScope.PAGE_INFORMATION.pageTitle}" /></title>
    			<meta name="viewport" content="width=device-width, initial-scale=1.0">
    			<meta name="description" content="<c:out value="${requestScope.PAGE_INFORMATION.pageDescription}" />">
    			<meta name="author" content="<c:out value="${requestScope.MERCHANT_STORE.storename}"/>">
				 <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 

				<!-- mobile settings -->
				<meta name="viewport" content="width=device-width, maximum-scale=1, initial-scale=1, user-scalable=0">
				
                <jsp:include page="/pages/shop/templates/bootstrap3/sections/shopLinks.jsp" />
<script>
var _hmt = _hmt || [];
(function() {
	if(isPC()==false){
		window.location.href = GetUrlRelativePath();
	}
  var hm = document.createElement("script");
  hm.src = "//hm.baidu.com/hm.js?30fe1a520ed07f83a8a57bfd58fd152f";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();
$(document).ready(function(){
	var winHei = $(window).width(); //窗口高度
	var heig = winHei - 1200;	
	var dist = heig/2;
	dist = Math.round(dist);
	//alert(dist);
	$("#nav_bar_out").css("margin-left",dist+"px");
	$("#contentLeft").css("left",dist+"px");
});
</script>
<link href="<c:url value="/resources/css/personCenter.css" />" rel="stylesheet"></link>
 	</head>
 	<body>
 	<div id="pageContainer">
				<tiles:insertAttribute name="header" ignore="true"/>
				<tiles:insertAttribute name="navbar" ignore="true"/>
				<div id="nav_bar_out"  style="background-color:#fafafa;  width:1200px;">
					<div id="contentLeft" class="nav_bar">
						 	<ul id="leftNavigation">
									<li class="li_actived" <c:if test="${activeMenu=='dashboard'}"> class="li_actived"</c:if>>
										<a href="<c:url value="/shop/customer/dashboard.html"/>">
											<span> 
												<s:message code="label.customer.myaccount" text="My account"/>
											</span>
										</a>
									</li>
									<li class="li_static"  <c:if test="${activeMenu=='basicinfo'}"> class="active"</c:if>>
									 	<a href="<c:url value="/shop/customer/basicInfo.html"/>">
									 	<span class=""><s:message code="menu.update-basicinfo" text="Update BasicInfo"/></span>
									 	</a>
									 </li>
									 <li style="display:none" class="li_static"  <c:if test="${activeMenu=='orders'}"> class="active"</c:if>>
										  <a href="<c:url value="/shop/customer/orders.html"/>">
										  	<span class=""> <s:message code="label.order.recent" text="Recent orders"/></span>
										  </a>
									  </li>
									  <li class="li_static"  <c:if test="${activeMenu=='memberpoint'}"> class="active"</c:if>>
										  <a href="<c:url value="/shop/customer/memberPoint.html"/>">
										  	<span class=""> <s:message code="label.customer.memberpoints" text="Member Points"/></span>
										  </a>
									  </li>
									<li class="li_static"  <c:if test="${activeMenu=='billing'}"> class="active"</c:if>>
									   <a href="<c:url value="/shop/customer/billing.html"/>">
									   		 <span class="">开票和收货信息</span>
									    </a>
									 </li>
									 <li class="li_static"  <c:if test="${activeMenu=='safetyCenter'}"> class="active"</c:if>>
									 	<a href="<c:url value="/shop/customer/safetyCenter.html"/>">
									 	<span class=""> <s:message code="menu.safetyCenter" text="Safety Center"/></span>
									 	</a>
									 </li>
									<%--  <li class="li_static"  <c:if test="${activeMenu=='giftInfo'}"> class="active"</c:if>> 
									 	<a href="<c:url value="/shop/customer/giftInfo.html"/>">
									 		<span class=""> <s:message code="label.customer.giftinfo" text="Gift Info"/></span>
									 	</a>
									 </li> --%>	
								</ul>
					</div>	 
					 <div id="contentRight" style="min-height:300px;background-color: #fafafa;">
					 	<tiles:insertAttribute name="body" ignore="true"/>
					 </div>
				</div>	
				<tiles:insertAttribute name="footer" ignore="true" />
	</div>
	<%-- <div id="pageContainer">
				<tiles:insertAttribute name="header" ignore="true"/>

				<tiles:insertAttribute name="navbar" ignore="true"/>
				<div class="main-padding-lr box" style="background: #F2F2F2;">
					<div id="contentLeft">
						 	<ul id="leftNavigation">
									<li <c:if test="${activeMenu=='dashboard'}"> class="active"</c:if>><a href="<c:url value="/shop/customer/dashboard.html"/>"><span class="carticon fa fa-dashboard fa-2x icon"/><span class="fsize12"> <s:message code="label.customer.myaccount" text="My account"/></span></a></li>
									<li <c:if test="${activeMenu=='billing'}"> class="active"</c:if>>
									   <a href="<c:url value="/shop/customer/billing.html"/>">
									   		<span class="carticon fa fa-tags fa-2x icon"/> <span class="fsize12">开票和收货信息</span>
									    </a>
									 </li>
									 <li <c:if test="${activeMenu=='safetyCenter'}"> class="active"</c:if>><a href="<c:url value="/shop/customer/safetyCenter.html"/>"><span class="carticon fa fa-key fa-2x icon"/><span class="fsize12"> <s:message code="menu.safetyCenter" text="Safety Center"/></span></a></li>
									 <li <c:if test="${activeMenu=='basicinfo'}"> class="active"</c:if>><a href="<c:url value="/shop/customer/basicInfo.html"/>"><span class="carticon fa fa-cogs fa-2x icon"/> <span class="fsize12"><s:message code="menu.update-basicinfo" text="Update BasicInfo"/></span></a></li>
									 <li <c:if test="${activeMenu=='orders'}"> class="active"</c:if>> <a href="<c:url value="/shop/customer/orders.html"/>"><span class="carticon fa fa-briefcase fa-2x icon"/><span class="fsize12"> <s:message code="label.order.recent" text="Recent orders"/></span></a></li>
									 <li <c:if test="${activeMenu=='memberpoint'}"> class="active"</c:if>> <a href="<c:url value="/shop/customer/memberPoint.html"/>"><span class="carticon fa fa-credit-card  fa-2x icon"/><span class="fsize12"> <s:message code="label.customer.memberpoints" text="Member Points"/></span></a></li>
									 <li <c:if test="${activeMenu=='giftInfo'}"> class="active"</c:if>> <a href="<c:url value="/shop/customer/giftInfo.html"/>"><span class="carticon fa fa-gift  fa-2x icon"/><span class="fsize12"> <s:message code="label.customer.giftinfo" text="Gift Info"/></span></a></li>	
								</ul>
					</div>	 
					 <div id="contentRight" style="min-height:300px;">
					 	<tiles:insertAttribute name="body" ignore="true"/>
					 </div>
				</div>	
				<tiles:insertAttribute name="footer" ignore="true" />
	</div> --%>
	<!-- end container -->
	    <jsp:include page="/pages/shop/templates/bootstrap3/sections/jsLinks.jsp" />
		<div class="modal fade" id="showReust" tabindex="-1" role="dialog"  aria-hidden="true">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="resultTitle"></h4>
		      </div>
		    </div>
		  </div>
		</div>
 	</body>
 </html>
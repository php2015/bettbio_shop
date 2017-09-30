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
<meta name="viewport" content="width=device-width, initial-scale=1" />  
<div class="nav-ezybio container-fluid"> 
	<div class="row">
		<div class="pull-left"><a href="#"><img  height="50" style="margin-top: 2px" src="<c:url value="/resources/ad/d.png" />" /></a></div>
		<div class="pull-right" style="padding-top:20px;padding-right:5px;font-size:12px;">
			<sec:authorize access="hasRole('AUTH_CUSTOMER') and fullyAuthenticated">
									<!-- logged in user -->
				<c:if test="${requestScope.CUSTOMER!=null}">
					<a href="#" onClick="javascript:location.href='<c:url value="/shop/customer/dashboard.html" />';"	role="button" class="dropdown-toggle noboxshadow nav-ezybio-font"	data-toggle="dropdown"><span class="carticon fa fa-user  icon"></span>					
					   	<c:if test="${not empty requestScope.CUSTOMER.nick}">
			                  <c:out value="${sessionScope.CUSTOMER.nick}" />
			            </c:if>   
			         </a>	
				</c:if>
			</sec:authorize>
			<sec:authorize access="!hasRole('AUTH_CUSTOMER') and fullyAuthenticated">
				
				<a class="nav-ezybio-font" href="javascript:void(0);"   onclick=""> <span class="carticon fa fa-user  icon"></span><s:message code="label.security.loggedinas" text="You are logged in as"/> [<sec:authentication property="principal.username"/>] <s:message code="label.security.nologinacces.store" text="We can't display store logon box"/></a>
			</sec:authorize>
			<sec:authorize access="!hasRole('AUTH_CUSTOMER') and !fullyAuthenticated">
				<a href="#" 	role="button" class="dropdown-toggle noboxshadow nav-ezybio-font"	data-toggle="dropdown" onclick="longinPage()"><span class="carticon fa fa-user  icon"></span><s:message code="button.label.signin"
				text="Signin" /></a>
			</sec:authorize>
			<c:if test="${not fn:contains(requestScope['javax.servlet.forward.servlet_path'], 'order') && not fn:contains(requestScope['javax.servlet.forward.servlet_path'], 'cart')}">
	 				<a href="#" onclick="displayMiniCart();"	role="button" class="dropdown-toggle noboxshadow nav-ezybio-font"	data-toggle="dropdown"><span class="carticon fa fa-shopping-cart  icon">
	 					</span><jsp:include page="/pages/shop/common/cart/minicartinfo.jsp" /></a>
	 				
			</c:if>					
		</div>
	</div>
</div>

<!-- Modal -->
<div class="modal fade" id="cartModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog" role="document" >  
		<div class="modal-content">
			<div class="modal-header"><button class="close" type="button" data-dismiss="modal">×</button>
				<h4 id="cartModalLabel"><img src="<c:url value="/resources/img/icon_success.png"/>" width="30"/>&nbsp;<strong><s:message code="cart.addsuccess" text="The product has be added"/></strong></h4>
			</div>
			<div class="modal-body">
					
					 			<button type="button" class="btn btn-success  btn-block" onclick="viewShoppingCartPage()"><strong><s:message code="cart.pay" text="The product has be added"/></strong></button>
					
					 			<br>
					 			<button type="button" class="btn btn-info  btn-block" data-dismiss="modal"><s:message code="cart.continue" text="The product has be added"/></button>
		    </div>
		    <div class="modal-footer">
							<a href="#" class="btn" data-dismiss="modal"><strong><s:message code="cart.close" text="Close"/></strong></a>
			</div>
	   </div>
	</div> 
</div>


			
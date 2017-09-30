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
<style>
	 #left_head li a{color:#fafafa !important;}
	#right_head li a{color:#fafafa !important;}
	#signinComponent li a{color:#fafafa !important;}
</style>
 <nav class="navbar navbar-inverse " style="height:40px;margin-top:-10px;padding-top:5px;border-radius:0px;">
	  <div style="width:1200px;margin-left: auto;margin-right: auto;">	  		
  			<div class="pull-left " style="text-align:center;">
  				<ul id="left_head" class="nav navbar-nav">
  					<%-- <li ><span style="color:#fafafa;padding-top:17px;display:inline-block;"><s:message code="label.welcome" text="Welcome to BettBio" /></span></li> --%>
				 	<li ><a style="padding: 15px 0px;" href='<c:url value="/shop/store/contactus.html"/>'>
						<img src="<c:url value='/resources/img/phoneCall.jpg'/>"/>
						<span style="padding-left: 10px;">021-6155-2750</span>
						</a>
					</li>
					<li ><img class="foot-li" style="padding-top:17px;" src="<c:url value="/resources/img/split.png"/>" /></li>	
					<li ><a style="padding: 15px 0px;" href='<c:url value="/shop/store/contactus.html"/>'>
						<img src="<c:url value='/resources/img/sendEmail.jpg'/>"/>
						<span style="padding-left: 10px;">order@bettbio.com</span>
						</a>
					</li>
					<%--
				 	<li ><img class="foot-li" style="padding-top:17px;" src="<c:url value="/resources/img/split.png"/>" /></li>		
				 	<li> <a class="foot-li" href="<c:url value="/shop/software/list.html"/>"> <s:message	code="label.common.software" text="Common software" /></a></li>
					--%>
  				</ul>
  			</div>
  			<div class="pull-right " style="text-align:center;" >
  				<ul id="right_head" class="nav navbar-nav">	  					
	  				<li ><a class="foot-li" href='<c:url value="/"/>'><s:message code="menu.home" text="Bettbio Home" /></a> </li>
				 	<li><img class="foot-li" style="padding-top:17px;" src="<c:url value="/resources/img/split.png"/>" /></li>
				 	<sec:authorize access="hasRole('AUTH_CUSTOMER') and fullyAuthenticated">
								<!-- logged in user -->
							<c:if test="${requestScope.CUSTOMER!=null}">
							<li class="dropdown" style="z-index:9999" >
								<a class="foot-li" href="#" id=""	role="button" class="dropdown-toggle noboxshadow nav-ezybio-font"	data-toggle="dropdown">
								[<s:message code="label.generic.welcome" text="Welcome" />
								   	<c:if test="${not empty requestScope.CUSTOMER.nick}">
						                  <c:out value="${sessionScope.CUSTOMER.nick}" />
						            </c:if>   
						         <b class="caret"></b>]</a>	
						         <ul id="signinComponent"  class="dropdown-menu " style="z-index:1000">
										<li>
											<a style="color:#333 !important" onClick="javascript:location.href='<c:url value="/shop/customer/dashboard.html" />';" href="#"><i class="fa fa-user"></i><s:message code="label.customer.myaccount" text="My account"/></a>
										</li>
										<li class="divider"></li>
										<li>
											<a style="color:#333 !important" onClick="javascript:location.href='<c:url value="/shop/customer/j_spring_security_logout" />';" href="#"><i class="fa fa-power-off"></i><s:message code="button.label.logout" text="Logout"/></a>
										</li>
									</ul>
						      </li>	
							</c:if>
					</sec:authorize>
					<sec:authorize access="!hasRole('AUTH_CUSTOMER') and fullyAuthenticated">									
							<!-- regist --><li><a class="foot-li" class="nav-ezybio-font" href="<c:url value="/admin/j_spring_security_logout" />"> [退出卖家登录 ]</a></li>
					</sec:authorize>
					<sec:authorize access="!hasRole('AUTH_CUSTOMER') and !fullyAuthenticated">
						<!-- login -->
						<li class="dropdown" style="z-index:9999" ><a href="#" id="signin"	role="button" class="dropdown-toggle noboxshadow nav-ezybio-font" style="height:42px"	data-toggle="dropdown" onclick="longinPage()">[<s:message code="button.label.signin"
							text="Signin" />]</a></li>
							
					</sec:authorize>					 	
				   	<li style="padding-top:18px;color:#9d9d9d"><span >,</span></li>
				   	<li ><a class="foot-li" href='<c:url value="/shop/customer/registration.html?c=customer"/>'>[<s:message code="button.label.register" text="Register" />]</a> </li>
				    <li><img class="foot-li" style="padding-top:17px;" src="<c:url value="/resources/img/split.png"/>" /></li>
				   	<c:if test="${not fn:contains(requestScope['javax.servlet.forward.servlet_path'], 'order') && not fn:contains(requestScope['javax.servlet.forward.servlet_path'], 'cart')}">
			 				<li class="dropdown" id="open-cart" >
			 				    <a class="foot-li" style="height: 42px" href="#" onclick="displayMiniCart();"	role="button" class="dropdown-toggle noboxshadow nav-ezybio-font"	data-toggle="dropdown">
			 				      <span style="color:#4285f4" class="carticon fa fa-shopping-cart  icon"></span><span style="padding-left:10px;"><s:message code="label.cart" text="Cart" /><b class="caret"></b><span style="padding-left:6px;"><jsp:include page="/pages/shop/common/cart/minicartinfo.jsp" /></span></span>
			 				    </a>
			 					<div id="dropcartlist"	class="dropdown-menu pull-right" style="z-index:9999999;width:500px;margin-left: 30px">	
								   <jsp:include page="/pages/shop/common/cart/minicart.jsp" />
							    </div>
			 				</li>
			 				
					 </c:if>		
					 <li><img style="padding-top:17px;" class="foot-li" src="<c:url value="/resources/img/split.png"/>" /></li>
					 <li style="padding-left:10px;"><img  style="padding-top:19px;width:12px;" src="<c:url value="/resources/img/qqImg.png"/>" /></li>
					 <li><a class="foot-li" target="_blank" href="http://wpa.b.qq.com/cgi/wpa.php?ln=1&key=XzgwMDA2NTQ0MF80MDQ3OThfODAwMDY1NDQwXzJf"><s:message code="label.onlineChat" text="Online Chat" /></a></li>
					 
					 <sec:authorize access="hasRole('AUTH') and !fullyAuthenticated">
					   <li><img style="padding-top:17px;" class="foot-li" src="<c:url value="/resources/img/split.png"/>" /></li>	
					   <li style="padding-left:10px;"><img  style="padding-top:19px;width:12px;" src="<c:url value="/resources/img/shopcenter.png"/>" /></li>
					   <li><a class="foot-li" href="<c:url value="/shop/customer/customLogon.html?d=p"/>"><s:message	code="label.storeadministration" text="Store Center" /></a></li>
					 </sec:authorize>
					 <sec:authorize access="hasRole('AUTH') and fullyAuthenticated">
					  <li><a class="foot-li" href="<c:url value="/admin/home.html"/>"><s:message	code="label.storeadministration" text="Store Center" /></a></li>
					 </sec:authorize>
					 
  				</ul>
  			</div>
		</div>
	</nav>



<!-- Modal -->
<div class="modal fade" id="cartModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog" role="document" >  
		<div class="modal-content">
			<div class="modal-header"><button class="close" type="button" data-dismiss="modal">×</button>
				<h3 id="cartModalLabel"><img src="<c:url value="/resources/img/icon_success.png"/>" width="40"/>&nbsp;<strong><s:message code="cart.addsuccess" text="The product has be added"/></strong></h3>
			</div>
			<div class="modal-body">
					
					 			<button type="button" class="btn btn-success btn-lg btn-block" onclick="viewShoppingCartPage()"><strong><s:message code="cart.pay" text="The product has be added"/></strong></button>
					
					 			<br>
					 			<button type="button" class="btn btn-info btn-lg btn-block" data-dismiss="modal"><s:message code="cart.continue" text="The product has be added"/></button>
		    </div>
		    <div class="modal-footer">
							<a href="#" class="btn" data-dismiss="modal"><strong><s:message code="cart.close" text="Close"/></strong></a>
			</div>
	   </div>
	</div> 
</div>


			
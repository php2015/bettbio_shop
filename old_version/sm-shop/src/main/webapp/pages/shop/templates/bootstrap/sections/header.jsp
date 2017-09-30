
<%
	response.setCharacterEncoding("UTF-8");
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", -1);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<!-- header -->
<div id="mainmenu" class="row-fluid">

	<div class="pull-left">
						<nav class="logo">
							 <c:choose>
	                		<c:when test="${not empty requestScope.MERCHANT_STORE.storeLogo}">
	                			<a href="<c:url value="/shop/"/>"><img class="logoImage" src="<sm:storeLogo/>"/></a> 
	                		</c:when>
	                		<c:otherwise>
	                			<h1>
	                			<a href="<c:url value="/shop/"/>">
	                				<c:out value="${requestScope.MERCHANT_STORE.storename}"/>
	                			</a>  
	                			</h1>
	                		</c:otherwise>
	                	  </c:choose>  
						</nav>
				</div>
		<div class="nav nav-pills pull-right " >
						<li><a href="<c:url value="/admin"/>"><s:message	code="label.storeadministration" text="Store" /></a></li>
				</div>
	<!-- c:if
		test="${not fn:contains(requestScope['javax.servlet.forward.servlet_path'], 'order') && not fn:contains(requestScope['javax.servlet.forward.servlet_path'], 'cart')}"-->
		<c:if 
		test="${not fn:contains(requestScope['javax.servlet.forward.servlet_path'], 'cart')}">
		<!-- not displayed in checkout (order) and cart -->
		<div id="miniCart" 
			class="btn-group pull-right logon-box">
			&nbsp;<i class="icon-shopping-cart icon-black"></i> <a
				style="box-shadow:none;color:FF8C00;" href="#"
				data-toggle="dropdown" class="open noboxshadow dropdown-toggle"
				id="open-cart"><s:message code="label.mycart" text="My cart" /></a>
			<jsp:include page="/pages/shop/common/cart/minicartinfo.jsp" />

			<ul class="dropdown-menu minicart" id="minicartComponent">
				<li><jsp:include page="/pages/shop/common/cart/minicart.jsp" />
				</li>
			</ul>
		</div>
	</c:if>

	<!-- If display customer section -->
	<c:if test="${requestScope.CONFIGS['displayCustomerSection'] == true}">
		<sec:authorize
			access="hasRole('AUTH_CUSTOMER') and fullyAuthenticated">
			<!-- logged in user -->
			<c:if test="${requestScope.CUSTOMER!=null}">
				<div class="nav nav-pills pull-right " >
					<li id="fat-menu" class="dropdown"><a
						class="dropdown-toggle noboxshadow" data-toggle="dropdown"
						href="#"> <s:message code="label.generic.welcome"
								text="Welcome" /> <c:if
								test="${not empty requestScope.CUSTOMER.nick}">
								<c:out value="${sessionScope.CUSTOMER.nick}" />
							</c:if><b class="caret"></b>
					</a>
						<ul class="dropdown-menu">
							<li><a
								onClick="javascript:location.href='<c:url value="/shop/customer/dashboard.html" />';"
								href="#"><i class="fa fa-user"></i>
								<s:message code="label.customer.myaccount" text="My account" /></a>
							</li>
							<li class="divider"></li>
							<li><a
								onClick="javascript:location.href='<c:url value="/shop/customer/j_spring_security_logout" />';"
								href="#"><i class="fa fa-power-off"></i>
								<s:message code="button.label.logout" text="Logout" /></a></li>
						</ul></li>
				</div>
			</c:if>
		</sec:authorize>
		<sec:authorize
			access="!hasRole('AUTH_CUSTOMER') and fullyAuthenticated">
			<!-- no dual login -->
			<div class="nav nav-pills pull-right" style="margin-top: 17px;">
				<li ><s:message code="label.security.loggedinas"
						text="You are logged in as" /> [<sec:authentication
						property="principal.username" />]. <s:message
						code="label.security.nologinacces.store"
						text="We can't display store logon box" /></li>
			</div>
		</sec:authorize>
		<sec:authorize
			access="!hasRole('AUTH_CUSTOMER') and !fullyAuthenticated">
			<!-- login box -->
			<div class="nav nav-pills pull-right" >
			<li class="dropdown" ><a
							onClick="javascript:location.href='<c:url value="/shop/customer/registration.html" />';"
							href="#" role="button" data-toggle="modal"><s:message
								code="button.label.register"
								text="Register" /></a>
							</li>
			</div>
			<div class="nav nav-pills pull-right">
				<li class="dropdown" style="z-index:999" ><a href="#" id="signinDrop"
					role="button" class="dropdown-toggle noboxshadow"
					data-toggle="dropdown"><s:message code="button.label.signin"
							text="Signin" /><b class="caret"></b></a>					<div id="signinPane" class="dropdown-menu"
						style="z-index:1000">
						<div id="loginError" class="alert alert-danger"
							style="display:none;"></div>
						<!-- form id must be login, form fields must be userName, password and storeCode -->
						<form id="login" method="post" accept-charset="UTF-8">
							<div class="control-group">
								<label><s:message code="label.longin.username" text="Username" /></label>
								<div class="controls">
									<input id="signin_userName" style="margin-bottom: 15px;"
										type="text" name="userName" size="30" />
								</div>
							</div>
							<div class="control-group">
								<label><s:message code="label.generic.password" text="Password" /></label>
								<div class="controls">
									<input id="signin_password" style="margin-bottom: 15px;"
										type="password" name="password" size="30" />
								</div>
							</div>
							<input id="signin_storeCode" name="storeCode" type="hidden"
								value="<c:out value="${requestScope.MERCHANT_STORE.code}"/>" />
							<button type="submit" style="width:100%" class="btn btn-large"
								id="login-button">
								<s:message code="button.label.login" text="Login" />
							</button>

						</form>	
							
						<a
							onClick="javascript:location.href='<c:url value="/shop/customer/registration.html" />';"
							href="" role="button" data-toggle="modal"><s:message
								code="label.register.notyetregistered"
								text="Not yet registered ?" /></a>
					</div></li>
			</div>
		</sec:authorize>
	</c:if>
<div class="nav nav-pills pull-right" >
		<li><a href="<c:url value="/shop"/>"><s:message
					code="menu.home" text="Home" /></a></li>
					<c:set var="topCategory"
							value="${requestScope.TOP_Reagent}" />
							<c:if test="${topCategory != null}">
							<c:choose>
								<c:when test= "${topCategory.categories != null && fn:length(topCategory.categories)>0 }">
							<li class="dropdown" style="z-index:999";><a href="<c:url value="/shop/category/${topCategory.description.seUrl}.html"/>" 
									role="button" class="dropdown-toggle noboxshadow"
									data-toggle="dropdown">${topCategory.description.name}<b class="caret"></b></a>									
										<div class="dropdown-menu" style="z-index:9999;">
										<c:forEach items="${topCategory.categories}" var="sonCategory">
											<table style="width:620px;">
												<tr>
													<td><div class="second_menu"><a href="#"
														onClick="javascript:location.href='<c:url value="/shop/category/${sonCategory.description.seUrl}.html"/>';"
														 role="button" data-toggle="modal" style="color: #0080c0;">${sonCategory.description.name}</a></div>
													</td>
													<c:if	test="${sonCategory.categories != null && fn:length(sonCategory.categories)>0 }">
													<td style="width:500px">
													<ul style="list-style:none;">
														<c:forEach items="${sonCategory.categories}" var="thirdCategory" varStatus="status">																																		
															<li class="ezy_menu_li"><a href="#" 
															 onClick="javascript:location.href='<c:url value="/shop/category/${thirdCategory.description.seUrl}.html"/>';" role="button" data-toggle="modal">${thirdCategory.descinfo}</a>
															 </li>
														</c:forEach>
													</ul>	
														</td>
													</c:if>
												</tr>
											</table>
										</c:forEach>
										</div>
										</c:when>
										<c:otherwise>
										<li class="dropdown">
										<a href="<c:url value="/shop/category/${topCategory.description.seUrl}.html"/>" 
															role="button" class="noboxshadow">
															${topCategory.description.name}</a>
										 </c:otherwise>		
									</c:choose>											
							</c:if>	
				<c:set var="topCategory"
							value="${requestScope.TOP_Instrument}" />
							<c:if test="${topCategory != null}">
							<c:choose>
								<c:when test= "${topCategory.categories != null && fn:length(topCategory.categories)>0 }">
							<li class="dropdown" style="z-index:999";><a href="<c:url value="/shop/category/${topCategory.description.seUrl}.html"/>" 
									role="button" class="dropdown-toggle noboxshadow"
									data-toggle="dropdown">${topCategory.description.name}<b class="caret"></b></a>									
										<div class="dropdown-menu" style="z-index:9999;">
										<c:forEach items="${topCategory.categories}" var="sonCategory">
											<table>
												<tr>
													<td><div class="second_menu"><a href="#"
														onClick="javascript:location.href='<c:url value="/shop/category/${sonCategory.description.seUrl}.html"/>';"
														 role="button" data-toggle="modal" style="color: #0080c0;">${sonCategory.description.name}</a></div>
													</td>
													<c:if	test="${sonCategory.categories != null && fn:length(sonCategory.categories)>0 }">
														<c:forEach items="${sonCategory.categories}" var="thirdCategory">
															<td>															
															<a href="#"
																onClick="javascript:location.href='<c:url value="/shop/category/${thirdCategory.description.seUrl}.html"/>';"
																role="button" data-toggle="modal">|&nbsp;${thirdCategory.descinfo}</a>															
															</td>	
														</c:forEach>
													</c:if>
												</tr>
											</table>
										</c:forEach>
										</div>
										</c:when>
										<c:otherwise>
										<li class="dropdown">
										<a href="<c:url value="/shop/category/${topCategory.description.seUrl}.html"/>" 
															role="button" class="noboxshadow">
															${topCategory.description.name}</a>
										 </c:otherwise>		
									</c:choose>											
							</c:if>
							<c:set var="topCategory"
							value="${requestScope.TOP_Supplies}" />
							<c:if test="${topCategory != null}">
							<c:choose>
								<c:when test= "${topCategory.categories != null && fn:length(topCategory.categories)>0 }">
							<li class="dropdown" style="z-index:999";><a href="<c:url value="/shop/category/${topCategory.description.seUrl}.html"/>" 
									role="button" class="dropdown-toggle noboxshadow"
									data-toggle="dropdown">${topCategory.description.name}<b class="caret"></b></a>									
										<div class="dropdown-menu" style="z-index:9999;">
										<c:forEach items="${topCategory.categories}" var="sonCategory">
											<table>
												<tr>
													<td><div class="second_menu"><a href="#"
														onClick="javascript:location.href='<c:url value="/shop/category/${sonCategory.description.seUrl}.html"/>';"
														role="button" data-toggle="modal" style="color: #0080c0;">${sonCategory.description.name}</a></div>
													</td>
													<c:if	test="${sonCategory.categories != null && fn:length(sonCategory.categories)>0 }">
														<c:forEach items="${sonCategory.categories}" var="thirdCategory">
															<td>															
															<a href="#"/>"
																onClick="javascript:location.href='<c:url value="/shop/category/${thirdCategory.description.seUrl}.html"/>';"
																role="button" data-toggle="modal">|&nbsp;${thirdCategory.descinfo}</a>															
															</td>	
														</c:forEach>
													</c:if>
												</tr>
											</table>
										</c:forEach>
										</div>
										</c:when>
										<c:otherwise>
										<li class="dropdown">
										<a href="<c:url value="/shop/category/${topCategory.description.seUrl}.html"/>" 
															role="button" class="noboxshadow">
															${topCategory.description.name}</a>
										 </c:otherwise>		
									</c:choose>											
							</c:if>
							<c:set var="topCategory"
							value="${requestScope.TOP_Others}" />
							<c:if test="${topCategory != null}">
							<c:choose>
								<c:when test= "${topCategory.categories != null && fn:length(topCategory.categories)>0 }">
							<li class="dropdown" style="z-index:999";><a href="<c:url value="/shop/category/${topCategory.description.seUrl}.html"/>" 
									role="button" class="dropdown-toggle noboxshadow"
									data-toggle="dropdown">${topCategory.description.name}<b class="caret"></b></a>									
										<div class="dropdown-menu" style="z-index:9999;">
										<c:forEach items="${topCategory.categories}" var="sonCategory">
											<table>
												<tr>
													<td><div class="second_menu"><a href="#"
														onClick="javascript:location.href='<c:url value="/shop/category/${sonCategory.description.seUrl}.html"/>';"
														role="button" data-toggle="modal" style="color: #0080c0;">${sonCategory.description.name}</a></div>
													</td>
													<c:if	test="${sonCategory.categories != null && fn:length(sonCategory.categories)>0 }">
														<c:forEach items="${sonCategory.categories}" var="thirdCategory">
															<td>															
															<a href="#"
																onClick="javascript:location.href='<c:url value="/shop/category/${thirdCategory.description.seUrl}.html"/>';"
																role="button" data-toggle="modal">|&nbsp;${thirdCategory.descinfo}</a>															
															</td>	
														</c:forEach>
													</c:if>
												</tr>
											</table>
										</c:forEach>
										</div>
										</c:when>
										<c:otherwise>
										<li class="dropdown">
										<a href="<c:url value="/shop/category/${topCategory.description.seUrl}.html"/>" 
															role="button" class="noboxshadow">
															${topCategory.description.name}</a>
										 </c:otherwise>		
									</c:choose>											
							</c:if>		
	</div>


</div>
<!-- Modal -->
<div class="modal hide fade" id="cartModal" tabindex="-1" role="dialog">
	<div class="modal-header"><button class="close" type="button" data-dismiss="modal">×</button>
		<h3 id="cartModalLabel"><img src="<c:url value="/resources/img/icon_success.png"/>" width="40"/>&nbsp;<strong><s:message code="cart.addsuccess" text="The product has be added"/></strong></h3>
	</div>
	<div class="modal-body">
			<div class="alert alert-success textCent" role="alert" >
			 			<a href="javascript:void(0);" class="alert-link" onclick="viewShoppingCartPage()"><strong><s:message code="cart.pay" text="The product has be added"/></strong></a>
			</div>
			<div class="alert alert-info textCent" role="alert" >
			 			<a href="#" class="alert-link" data-dismiss="modal"><s:message code="cart.continue" text="The product has be added"/></a>
			</div>
			<div class="modal-footer">
					<a href="#" class="btn" data-dismiss="modal"><strong><s:message code="cart.close" text="Close"/></strong></a>
			</div>
    </div> 
</div>
<!-- End main menu -->

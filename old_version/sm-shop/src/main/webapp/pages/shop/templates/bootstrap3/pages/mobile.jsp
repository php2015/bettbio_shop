
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
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>
<link href="<c:url value="/resources/templates/bootstrap3/css/style.css" />" rel="stylesheet" type="text/css">
<c:set var="req" value="${request}" />
					
		<div class="container-fluid">
			<div class="row nav-ezybio">
				<div class="col-xs-4">
					<a href="<c:url value="/"/>"><c:if test="${not empty requestScope.MERCHANT_STORE.storeLogo}"><img class="logoImage" src="<sm:storeLogo/>" height="50" alt='<c:out value="${requestScope.PAGE_INFORMATION.pageTitle}" />'/></c:if></a>
				</div>
				<div class="col-xs-8">
					<sec:authorize access="hasRole('AUTH_CUSTOMER') and fullyAuthenticated">
									<!-- logged in user -->
									<c:if test="${requestScope.CUSTOMER!=null}">
									<li class="dropdown" style="z-index:9999" >
										<a href="#" id=""	role="button" class="dropdown-toggle noboxshadow nav-ezybio-font"	data-toggle="dropdown"><span class="carticon fa fa-user  icon"></span>
										<s:message code="label.generic.welcome" text="Welcome" />
										   	<c:if test="${not empty requestScope.CUSTOMER.nick}">
								                  <c:out value="${sessionScope.CUSTOMER.nick}" />
								            </c:if>   
								         <b class="caret"></b></a>	
								         <ul id="signinComponent"  class="dropdown-menu " style="z-index:1000">
												<li>
													<a onClick="javascript:location.href='<c:url value="/shop/customer/dashboard.html" />';" href="#"><i class="fa fa-user"></i><s:message code="label.customer.myaccount" text="My account"/></a>
												</li>
												<li class="divider"></li>
												<li>
													<a onClick="javascript:location.href='<c:url value="/shop/customer/j_spring_security_logout" />';" href="#"><i class="fa fa-power-off"></i><s:message code="button.label.logout" text="Logout"/></a>
												</li>
											</ul>
								      </li>	
									</c:if>
							</sec:authorize>
							<sec:authorize access="!hasRole('AUTH_CUSTOMER') and fullyAuthenticated">
									<!-- no dual login -->
									<li><a class="nav-ezybio-font" href="javascript:void(0);"   onclick=""> <span class="carticon fa fa-user  icon"></span><s:message code="label.security.loggedinas" text="You are logged in as"/> [<sec:authentication property="principal.username"/>] <s:message code="label.security.nologinacces.store" text="We can't display store logon box"/></a></li>
							</sec:authorize>
							<sec:authorize access="!hasRole('AUTH_CUSTOMER') and !fullyAuthenticated">
								<li class="dropdown" style="z-index:9999" ><a href="#" id="signin"	role="button" class="dropdown-toggle noboxshadow nav-ezybio-font"	data-toggle="dropdown" onclick="longinPage()"><span class="carticon fa fa-user  icon"></span><s:message code="button.label.signin"
							text="Signin" /></a></li>
								</sec:authorize>
				</div>
				
			</div>
			<div class="row"> 		
				
			<div class="col-xs-12  title-margin pagefont text-center" >
				  <h1>
						<label style="color:#0080c0">Better Buy</label>
					  <span style="color:#d79806">Better Bio</span>
				  </h1>
			  </div>
			  
			   <div class="col-xs-12 inputfont ">
				<form id="searchForm"  method="post" action="<c:url value="/shop/search/search.html"/>" >
					<div class="input-group input-group-lg">
						<input id ="searchInput" name="q" type="text" class="form-control" style="border: 1px solid #3c78d8;" placeholder="<s:message code="label.search.searchQuery" text="Search query" />" autocomplete="off" spellcheck="false" dir="auto" value="<c:out value="${q}"/>"/>
						 <span class="input-group-btn">
							<button class="btn btn-default"  style="border: 1px solid #3c78d8;" type="button" onClick="doEzybioSearch()"><img src="<c:url value="/resources/img/Search.png"/>"/></button>
						  </span>
					</div>	  
				</form>
			</div>
			
			 <div class="col-xs-12 imgfont">
				  <c:set var="topg" value="${requestScope.topCateGory}" />
				  <c:if test="${topg != null}">
					  	<c:forEach items="${topg}" var="category">
					  			<div class="col-xs-3" >
								   <div class="ih-item circle colored effect10 top_to_bottom text-center"><a href='<c:url value="/shop/category/category/${category.id}.html"/>'>
									  <div class="img text-center" ><img src="<c:url value="/resources/img/${category.code}.png" />" style="background-color:#cfbf8c"></div>
									  <div class="info">
										<h5 class="nav-ezybio-font">${category.description.name}</h5>
									  </div></a></div>
									  </div>
								
					  	</c:forEach>
				 </c:if>
			  </div>
			  </div>			
		</div>
		<div id="" style="z-index:99;padding:1px 0px;" class="imgfont">
     	<div class="container-fluid">
     		<div class="row" style="text-align:center">
     			<div class="col-xs-3 col-md-1"><a href='<c:url value="/shop/customer/registration.html"/>'><s:message code="label.bettbio.register" text="Register" /></a></div>							 
				<div class="col-xs-3 col-md-1"><a href='<c:url value="/shop/customer/protocol.html"/>'><s:message code="label.content.userprotocol" text="protocol" /></a></div>
				<div class="col-xs-3 col-md-1"><a href='<c:url value="/shop/noviceguidelines.html"/>'><s:message code="label.bettbio.buyer" text="buyer" /></a></div>
				<div class="col-xs-3 col-md-1"><a href='<c:url value="/shop/singleorder.html"/>'><s:message code="label.bettbio.justbuy" text="just buy" /></a></div>
				<div class="col-xs-3 col-md-1"><a href='<c:url value="/shop/quality.html"/>'><s:message code="label.bettbio.buy.title2" text="Contact" /></a></div>
				<div class="col-xs-3 col-md-1"><a href='<c:url value="/shop/detectService.html"/>'><s:message code="label.bettbio.buy.title3" text="Contact" /></a></div>
				<div class="col-xs-3 col-md-1"><a href='<c:url value="/shop/marketpoints/list.html"/>'><s:message code="label.bettbio.buy.shop1" text="Contact" /></a></div>
				<div class="col-xs-3 col-md-1"><a href='<c:url value="/shop/pointChange.html"/>'><s:message code="label.bettbio.buy.shop2" text="Contact" /></a></div>
				<div class="col-xs-3 col-md-1"><a href='<c:url value="/shop/store/contactus.html"/>'><s:message code="label.customer.contactus" text="Contact" /></a></div>
				<div class="col-xs-3 col-md-1"><a href='<c:url value="/shop/aboutUS.html"/>'><s:message code="label.bettbio.buy.about" text="Contact" /></a></div>							
				<div class="col-xs-3 col-md-1"><a href='<c:url value="/shop/privacyPolicy.html"/>'><s:message code="label.content.privacy" text="Contact" /></a></div>
				<div class="col-xs-3 col-md-1"><a href="http://www.kuaidihelp.com/" target="_blank"><s:message code="label.product.delivery.link" text="Contact" /></a></div>
     		</div>
     		<div class="row text-center" style="padding-top:10px;">
     			<div class="col-sm-12 col-md-12" ><span class="nav-ezybio-copyright nav-ezybio-font"><s:message code="label.bettbio.baknum" text="buyer" /></span></div>
     		</div>
     		<div class="row text-center">
			<div><span class="nav-ezybio-copyright nav-ezybio-font"> Copyright<s:message code="label.copyright.sigh" text="All Rights Reserved"/> 2015-2016 www.bettbio.com <s:message code="all.rights.reserved" text="All Rights Reserved"/></span>
			</div>
		 </div>
     	</div>
     </div>
		
			
 

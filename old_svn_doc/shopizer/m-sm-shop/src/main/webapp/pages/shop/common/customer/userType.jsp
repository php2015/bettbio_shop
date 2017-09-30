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

<div class="container-fluid">

	<div class="row imgfont text-center" >
	<br><br>
		<div class="col-xs-12col-xs-offset-0" style="padding:20px;" >
			<a href='<c:url value="/shop/customer/registration.html" />'>
					<span aria-hidden="true" class="carticon fa fa-user fa-3x icon icon-style"	style="background:#800080;width:50px;height:50px;"></span>	
					<span style="font-size:27px;"><s:message code="label.bettbio.register" text="Customer Register"/></span>					 
						  </a>
		</div>
		<div class="col-xs-12col-xs-offset-0" style="padding:27px;" >
			<a href='<c:url value="/shop/customer/storeRegistration.html" />'>						
							<span aria-hidden="true"
								class="carticon fa fa-laptop fa-3x icon icon-style"	style="background: rgb(208, 101, 3);width:50px;height:50px;"></span>
						<span style="font-size:27px;"><s:message code="label.store.register" text="Store Register"/>	</span>					
					</a>	
		
		</div>
		
	</div>
	
	
	 
</div>
					



		

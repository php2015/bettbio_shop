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
<script src="<c:url value="/resources/js/login.js" />"></script>
  <script type="text/javascript" charset="utf-8">
 	var userholder ='<s:message	code="label.longin.username" text="holde" />';
 	
 </script>


<div class="container-fluid">

	<div class="row">
		<div class="col-xs-12col-xs-offset-0" style="padding:20px;" >
			
			<form id="login" method="post" accept-charset="UTF-8">
		
		<span id="loginError" class="alert alert-danger" style="display:none;"></span>
		<br>
		
		<div class="control-group">
			<br>
			<div class="controls input-group">
				<span class="input-group-addon"> <span class=" glyphicon glyphicon-user" aria-hidden="true"  ></span></span>
				<input id="signin_userName" class="form-control" type="text" name="userName" size="18" placeholder=<s:message	code="label.longin.username" text="holde" /> />
			</div>
		</div>
		<br>
		<div class="control-group">
			<div class="controls input-group">
				<span class="input-group-addon"> <span class=" glyphicon glyphicon-lock" aria-hidden="true"  ></span></span>
				<input id="signin_password" class="form-control" type="password" name="password" size="18" />
			</div>
		</div>
		<div class="control-group">
			<br>
			<div class="controls">
				<label class=" btn-block">
   					 <input type="checkbox" id="remember" >
												<s:message code="label.logonform.rememberusername"
													text="Remember my user name" />
  					</label>
			</div>
		</div>
		
  <br>
  <div class="control-group">
			<button id="login-button" type="button" class="btn btn-default btn-info btn-block" ripple><s:message code="button.label.login" text="Login" /></button>	
			</div>
		<input id="signin_storeCode" name="storeCode" type="hidden"
			value="<c:out value="${requestScope.MERCHANT_STORE.code}"/>" />
			
		

	</form>	
	<div class="logon-password-box">
		<div class="pull-left"><a onClick="javascript:location.href='<c:url value="/shop/customer/registration.html" />';" href="" role="button" data-toggle="modal"><s:message code="label.register.notyetregistered" text="Not yet registered ?" /></a></div>
		<div class="pull-right"><a	onClick="javascript:location.href='<c:url value="/shop/customer/resetPassword.html" />';" href="" role="button" data-toggle="modal"><s:message code="label.custmoer.forget.password" text="Get Password" /></a></div>
	</div>
		</div>
	</div>
</div>
					



		

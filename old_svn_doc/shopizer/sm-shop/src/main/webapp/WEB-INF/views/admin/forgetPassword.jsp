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
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ page import="java.util.Calendar" %>
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
 

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<c:set var="lang" scope="request" value="${requestScope.locale.language}"/> 
 <html xmlns="http://www.w3.org/1999/xhtml"> 
     <head>
       <meta charset="utf-8">
       <meta http-equiv="X-UA-Compatible" content="IE=edge">
       <meta name="viewport" content="width=device-width, initial-scale=1">        	 		
	   <title><s:message code="label.storeadministration" text="Store administration" /></title>    			
	   <meta name="keywords" content='<s:message code="label.website.keywords" text="bettbio.com"/>'>
	   <meta name="description" content='<s:message code="label.website.descirption" text="bettbio.com"/>'>
	   <meta name="author" content="bettbio.com">
    		            <!--[if lte IE 9]>		
		 <script type="text/javascript">
				window.location.href='<c:url value="/shop/nonsupport.html"/>';
		</script>	
	<![endif]-->
    			<script src="<c:url value="/resources/js/jquery-2.0.3.min.js" />"></script> 
    			<script src="<c:url value="/resources/js/jquery.friendurl.min.js" />"></script>
                <jsp:include page="/common/adminLinks.jsp" />
 	</head>

<script src="<c:url value="/resources/js/pwstrength.js" />"></script>
<script src="<c:url value="/resources/js/shop-customer.js" />"></script>

<script type="text/javascript">
$(function() {
	isFormValid();
	$("input[type='text']").on("change keyup paste", function(){
		isFormValid();
	});
	
});
function validateEmail($email) {
	  var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
	  if ( $email.length > 0 && emailReg.test($email)) {
	    return true;
	  } else {
	    return false;
	  }
}
function changeValidateImg(obj){
	if(obj==null||obj=='undefined'||obj=='') obj = '';
	var imgSrc = $("#imgObj"+obj);
	var src = imgSrc.attr("src");
	imgSrc.attr("src", chgValidateImgUrl(src));
}
function chgValidateImgUrl(url) {
	var timestamp = (new Date()).valueOf();
	url = url.substring(0, 29);
	if ((url.indexOf("&") >= 0)) {
		url = url + "×tamp=" + timestamp;
	} else {
		url = url + "?timestamp=" + timestamp;
	}
	return url;
}
function emptyString($value) {
	return !$value || !/[^\s]+/.test($value);
}
function getInvalidEmailMessage() {
	return '<s:message code="messages.invalid.email" text="Invalid email address"/>';
}
function isFormValid() {
	
	if($('.alert-error').is(":visible")) {
		return true;
	}
	
	if($('.alert-success').is(":visible")) {
		return true;
	}	
		
	
	$('#registrationError').hide();//reset error message
	
	var msg = isCustomerFormValid($('#adminForgetForm'));
	
	if(msg == null ){
		$('#submitRegistration').removeClass('btn-disabled');
		$('#submitRegistration').prop('disabled', false);
		$('#registrationError').hide();
		return true;
	}else{
		$('#submitRegistration').addClass('btn-disabled');
		$('#submitRegistration').prop('disabled', true);
		$('#registrationError').html(msg);
		$('#registrationError').show();
		return false;
	}
}
</script>
<c:url var="setn" value="/admin/setnewp.html" />
<div class="container-fluid">
	<div class="col-sm-12 text-center">
				 <div class="col-sm-3 col-sm-offset-2">
							<p class="lead">
								<s:message code="label.custmoer.forget.password" text="Forget" />
							</p>
						</div>
						<div class="col-sm-5 col-sm-offset-2">
							<%-- <img alt="go to ezybio"
								src="<c:url value="/resources/img/shopizer_small.png" />"> --%>
								<p class="lead">
							<a onClick="javascript:location.href='<c:url value="/admin/logon.html"/>';" href="#"><s:message code="button.label.store.login" text="Login Now" /></a>
						</p>
						</div>
	</div>	
</div>
<hr style="height:2px;border:none;border-top:1px solid #3498DB; ">
<div class="container">
	<div class="row">
		<div>
		<div class="col-sm-12 text-center">
		<form:form method="POST" commandName="user" action="${setn}" class="form-horizontal" id="adminForgetForm" >
		<form:errors path="*" cssClass="alert alert-danger" element="div" />
		<div id="store.success" class="alert alert-success"
			style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>">
			<s:message code="message.success" text="Request successfull" />
		</div>
		<form:input type="hidden" path="adminPassword" value="resetpwd" />
		<div id="registrationError"  class="alert alert-warning common-row text-center" style="display:none;"></div>	
				<div class="form-group ">
				<label class="col-sm-2 control-label"><s:message
						code="label.generic.username" text="User name" />*</label>
				<div class="col-sm-10">
				<s:message code="NotEmpty.customer.userName" text="User name is required" var="msgUserName"/>
					<form:input cssClass="form-control" path="adminName"	title="${msgUserName}"/>
					<span class="help-inline"><div id="checkCodeStatus"	style="display:none;"></div>
						<form:errors path="adminName" cssClass="error" /></span>
				</div>
			</div>	
			<div class="form-group form-group-lg">
				<label class="col-sm-2 control-label"><s:message
						code="label.generic.email" text="Email" />*</label>
				<div class="col-sm-10">
					<s:message code="NotEmpty.store.email" text="User name is required" var="msgEmail"/>
					<form:input cssClass="form-control email" path="adminEmail" title="${msgEmail}" />
					<span class="help-inline"><form:errors path="adminEmail"
							cssClass="error" /></span>
				</div>
			</div>
			<div class="form-group ">
         		 <label class="col-sm-2 control-label">验证码*</label>
         		  <div class="col-sm-5">			              		
             					 <s:message code="NotEmpty.store.validatecode" text="validatecode is required" var="validatecodeName"/>
						   <input type="text" Class="required company form-control " id="captchaResponseField" name="captchaResponseField" title="${validatecodeName}"/>
            		</div>
            		<div class="col-sm-5" style="line-height:35px;">	
            			 <a style="cursor: pointer;" onclick="changeValidateImg()"><img id="imgObj" src="<c:url value="/shop/store/code.html"/>" style="margin-top:7px;"/>&nbsp;&nbsp;<s:message code="label.generic.change.validatecode" text="Change"/></a>
            		</div>
        	 </div>
        	 <div class="form-actions">					
			            <div class="col-sm-2 col-sm-offset-10">
     									<button type="submit" class="btn btn-success" id="submitRegistration">
								<s:message code="button.label.submit" text="Submit" />
							</button>
  							 		 </div>  
					</div>
        	</form:form> 
		</div>
	</div>
</div>

</div>
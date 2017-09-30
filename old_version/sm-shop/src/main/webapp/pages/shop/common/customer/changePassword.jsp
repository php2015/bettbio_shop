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

<!-- requires functions.jsp -->
<script src="<c:url value="/resources/js/shop-customer.js" />"></script>
<script src="<c:url value="/resources/js/check-functions.js" />"></script>
<script src="<c:url value="/resources/js/pwstrength.js" />"></script>

<script type="text/javascript">

$(document).ready(function() {
	

	isFormValid();
	//bind fields to be validated
	$("input[type='text']").on("change keyup paste", function(){
		resetGlobalErrors();
		isFormValid();
	});
	
	$("input[type='password']").on("change keyup paste", function(){
		resetGlobalErrors();
		isFormValid();
	});
	
	$("#submitChangePassword").click(function(e) {
		e.preventDefault();//do not submit form
		resetGlobalErrors();
		if(isFormValid()) {
			$('#changePasswordForm').submit();
		}
    });

	
});

function isFormValid() {
	if($('.alert-success').is(":visible")) {
		return true;
	}
	
	var msg = isCustomerFormValid($('#changePasswordForm'));
	
	if(msg!=null) {//disable submit button
		$('#submitChangePassword').addClass('btn-disabled');
		$('#submitChangePassword').prop('disabled', true);
		$('#formError').html(msg);
		$('#formError').show();
		return false;
	} else {
		$('#submitChangePassword').removeClass('btn-disabled');
		$('#submitChangePassword').prop('disabled', false);
		$('#formError').hide();
		return true;
	}
}
function showErro(status){
	 if(status==-2){
		 $("#messageFail").html('<s:message code="NotEmpty.contact.captchaResponseField" text="Message" />');
	 }else if(status==-3){
		 $("#messageFail").html('<s:message code="validaion.recaptcha.not.matched" text="Message" />');
		 changeValidateImg();
	 }else{
		 $("#messageFail").html('<s:message code="label.generic.phone.message.fail" text="Message" />');
	 }
	 $("#messageFail").show();
}
function resetGlobalErrors() {
	
}

</script>

	<div class="customer-box row">
				<div class="col-sm-8 col-sm-offset-0">
					<c:url var="changePassword" value="/shop/customer/changePassword.html"/>					
					<form:form method="post" action="${changePassword}" id="changePasswordForm" cssClass="form-horizontal" commandName="password">
						   <form:errors path="*" cssClass="alert-danger" element="div" />
							<fieldset>
							<div class="form-group">
								<label class="col-sm-3 control-label">
								<strong style="font-size:20px;font-weight:bold"><s:message code="label.generic.changepassword" text="Change password"/></strong></label>
								<div class="col-sm-9">
								</div>
							 </div>
							<div class="form-group">
								<label class="col-sm-3 control-label" for="currentPassword"><s:message code="label.customer.currentpassword" text="Current password"/>:</label>
								<div class="col-sm-9">
								   <s:message code="currentpassword.not.empty" text="Current password is required " var="msgCurrentPassword"/>
								   <form:password path="currentPassword" cssClass="required form-control " id="currentPassword" title="${msgCurrentPassword}"/>
								   <span class="help-inline"><form:errors path="currentPassword" cssClass="error" /></span>								
								</div>
							 </div>
							 <div class="form-group">
								<label class="col-sm-3 control-label" for="password"><s:message code="label.customer.newpassword" text="New password"/>:</label>
								<div class="col-sm-9">
								   <s:message code="newpassword.not.empty" text="New password is required" var="msgPassword"/>
								   <form:password path="password" cssClass="required password form-control " id="password" title="${msgPassword}" onkeyup="pwStrength(this.value);" onblur="pwStrength(this.value);"/>
								    <span class="help-inline"><form:errors path="currentPassword" cssClass="error" /></span>								
								</div>
							 </div>
							 <div class="form-group">
			                        <label class="col-sm-3 control-label"></label>
			                        <div class="col-sm-9">
			                        	 <table style="width:120px;" cellspacing="0" cellpadding="1" bordercolor="#eeeeee" height="22">  
											<tr align="center" bgcolor="#f5f5f5">  
											<td width="33%" id="strength_L" style="padding:3px"><s:message code="password.strendth.low" text="Strength Low"/></td>  
											<td width="33%" id="strength_M" style="padding:3px"><s:message code="password.strendth.middle" text="Strength Middle"/></td>  
											<td width="33%" id="strength_H" style="padding:3px"><s:message code="password.strendth.high" text="Strength High"/></td> 
											</tr>  
										</table> 
										<s:message code="password.strength.rule" text="Strength password"/>
			                        </div>			                       		                        
				            </div>
							 <div class="form-group">
								<label class="col-sm-3 control-label" for="repeatPassword"><s:message code="label.customer.repeatpassword" text="Repeat password"/></label>
								<div class="col-sm-9">
								   <s:message code="label.customer.repeatpassword" text="Current password is required" var="msgRepeatPassword"/>
								   <form:password path="checkPassword" cssClass="required checkPassword form-control " id="checkPassword" title="${msgRepeatPassword}"/>
								    <span class="help-inline"><form:errors path="currentPassword" cssClass="error" /></span>								
								</div>
							 </div>
							<div class="form-group">
				         		 <label class="col-sm-3 control-label"><s:message code="label.generic.validatecode" text="Validatecode"/>(*):</label>
				         		  <div class="col-sm-5">			              		
			              					 <s:message code="NotEmpty.store.validatecode" text="validatecode is required" var="validatecodeName"/>
										   <input type="text" Class="required company form-control " id="captchaResponseField" name="captchaResponseField" title="${validatecodeName}"/>
				            		</div>
				            		<div class="col-sm-4" style="line-height:35px;">	
				            			 <a style="cursor: pointer" onclick="changeValidateImg()"><img id="imgObj" src="<c:url value="/shop/store/code.html"/>" style="margin-top:7px;"/>&nbsp;&nbsp;<s:message code="label.generic.change.validatecode" text="Change"/></a>
				            		</div>
				         </div>
							<div class="form-group">
											<label class="control-label col-sm-3"><s:message code="label.generic.phone.short" text="Phone"/><s:message code="label.generic.validatecode" text="Validatecode"/>(*):</label>
											<div class="col-sm-5">
											   <s:message code="NotEmpty.store.validatecode" text="validatecode is required" var="validatecodeName"/>
											    <input type="text" Class="required company form-control " name="phonevalidate" title="${validatecodeName}"/>
											  
											  <div id="messageSucces" style="display:none;">
											  <s:message code="label.generic.phone.message.before" text="Message"/><span id="messageSecond"></span>
											  <s:message code="label.generic.phone.message.after" text="Message"/></div>
											  <div id="messageFail" style="display:none;color:red"><s:message code="label.generic.phone.message.fail" text="Message" /></div>	                                        	
	                                        </div>
	                                     <div class="col-sm-4">
	                                        		<input id="btnSendCode" class="btn btn-info" type="button" value="<s:message code="label.generic.phone.message" text="Phone"/>" onclick="sendPhonemsgForPW()" />
	                                        	</div>   
							</div>							
							<div class="form-group">
								<div class="col-sm-5 col-sm-offset-3">
									<input id="submitChangePassword" class="btn  btn-disabled btn-info" type="submit" name="changePassword" value="<s:message code="menu.change-password" text="Change password"/>" disabled="">
								</div>
							</div>			
							</fieldset>				</form:form>
				</div>			
		<div class="col-sm-4 " style="padding-top:45px;">
			<div id="store.success" class=" alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>
			<div id="formError"  class="alert alert-warning " style="display:none;"></div>
		</div>
</div>
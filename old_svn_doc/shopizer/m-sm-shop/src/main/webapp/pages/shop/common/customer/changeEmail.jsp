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
	
	$("#submitChangeEmail").click(function(e) {
		e.preventDefault();//do not submit form
		resetGlobalErrors();
		if(isFormValid()) {
			$('#changeEmailForm').submit();
		}
    });

	
});

function isFormValid() {
	if($('.alert-success').is(":visible")) {
		return true;
	}
	
	var msg = isCustomerFormValid($('#changeEmailForm'));
	
	if(msg!=null) {//disable submit button
		$('#submitChangeEmail').addClass('btn-disabled');
		$('#submitChangeEmail').prop('disabled', true);
		$('#formError').html(msg);
		$('#formError').show();
		return false;
	} else {
		$('#submitChangeEmail').removeClass('btn-disabled');
		$('#submitChangeEmail').prop('disabled', false);
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
<div class="container-fluid">
	<div class="customer-box row">
		<div class="container-fluid" style="padding-bottom:10px;">
				<div class="pull-left" style="font-size:15px;padding-top:10px;"><s:message code="label.customer.safety.email" text="Change email"/></div>
			<div class="pull-right"><a href="<c:url value="/shop/customer/safetyCenter.html"/>" ><span aria-hidden="true" class="carticon fa fa-reply fa-2x icon icon-style" style="background:#1E90FF;"></span></a></div>									
			</div>	
				<div class="col-xs-12 box main-padding-lr">
					<c:url var="changeEmail" value="/shop/customer/changeEmail.html"/>					
					<form method="post" action="${changeEmail}" id="changeEmailForm" class="form-horizontal">
						   <form:errors path="*" cssClass="alert-danger" element="div" />
						   
							<fieldset>
							<div class="form-group">								
								<div class="col-xs-9">
									<c:if test="${not empty errors }">
									   	<div class="alert-danger">
									   	<c:forEach var="error" items="${errors }">
									   	${error }<br/>
									   	</c:forEach>
									   	</div>
									   </c:if>
								</div>
							 </div>						
							<div class="form-group">
								<label class="col-sm-3 control-label" for="currentEmail"><s:message code="label.customer.currentemail" text="Current Email"/>:</label>
								<div class="col-sm-9">
								   <span style="line-height:30px;font-size:14px">${email }</span>						
								</div>
							 </div>
							 <div class="form-group">
								<label class="col-sm-3 control-label" for="newEmail"><s:message code="label.customer.newemail" text="New Email"/>:</label>
								<div class="col-sm-9">
									<s:message code="NotEmpty.customer.emailAddress" text="Email address is required" var="msgEmail"/>
								   <input type="text" name="newEmail" id="newEmail" class="required email form-control" title="${msgEmail }"/>						
								</div>
							 </div>
							
							<div class="form-group">
				         		 <label class="col-sm-3 control-label"><s:message code="label.generic.validatecode" text="Validatecode"/>(*):</label>
				         		  <div class="col-sm-5">			              		
			              					 <s:message code="NotEmpty.store.validatecode" text="validatecode is required" var="validatecodeName"/>
										   <input type="text" Class="required company form-control " id="captchaResponseField" name="captchaResponseField" title="${validatecodeName}"/>
				            		</div>
				            		<div class="col-sm-4" style="line-height:35px;">	
				            			 <a style="cursor: pointer;" onclick="changeValidateImg()"><img id="imgObj" src="<c:url value="/shop/store/code.html"/>" style="margin-top:7px;"/>&nbsp;&nbsp;<s:message code="label.generic.change.validatecode" text="Change"/></a>
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
	                                        		<input id="btnSendCode" class="btn btn-info btn-block" type="button" value="<s:message code="label.generic.phone.message" text="Phone"/>" onclick="sendPhonemsgForPW()" />
	                                        	</div>   
							</div>							
							<div class="form-group">
								<div class="col-sm-5 col-sm-offset-3">
									<input id="submitChangeEmail" class="btn  btn-disabled btn-info btn-block" type="submit" name="changeEmail" value="<s:message code="button.label.submit2" text="Change Email"/>" disabled="">
								</div>
							</div>			
							</fieldset>				
						</form>
				</div>			
		<div class="col-xs-12 ">
			<div id="store.success" class=" alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>
					<div id="formError"  class="alert alert-warning " style="display:none;position:fixed; top:60px;right:10px;"></div>
		</div>
		</div>
</div>
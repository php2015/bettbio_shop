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
	
	$("#submitChangePhone").click(function(e) {
		e.preventDefault();//do not submit form
		resetGlobalErrors();
		if(isFormValid()) {
			$('#changePhoneForm').submit();
		}
    });

	
});

function isFormValid() {
	if($('.alert-success').is(":visible")) {
		return true;
	}
	
	var msg = isCustomerFormValid($('#changePhoneForm'));
	
	if(msg!=null) {//disable submit button
		$('#submitChangePhone').addClass('btn-disabled');
		$('#submitChangePhone').prop('disabled', true);
		$('#formError').html(msg);
		$('#formError').show();
		return false;
	} else {
		$('#submitChangePhone').removeClass('btn-disabled');
		$('#submitChangePhone').prop('disabled', false);
		$('#formError').hide();
		return true;
	}
}
function showErro(status,obj){
	if(obj==null||obj=='undefined'||obj=='') obj = '';
	 if(status==-2){
		 $("#messageFail"+obj).html('<s:message code="NotEmpty.contact.captchaResponseField" text="Message" />');
	 }else if(status==-3){
		 $("#messageFail"+obj).html('<s:message code="validaion.recaptcha.not.matched" text="Message" />');
		 changeValidateImg();
	 }else{
		 $("#messageFail"+obj).html('<s:message code="label.generic.phone.message.fail" text="Message" />');
	 }
	 $("#messageFail"+obj).show();
}
function resetGlobalErrors() {
	
}

</script>

	<div class="customer-box row">
				<div class="col-sm-8 col-sm-offset-0">
					<c:url var="changePhone" value="/shop/customer/changePhone.html"/>					
					<form method="post" action="${changePhone}" id="changePhoneForm" class="form-horizontal">
						   <form:errors path="*" cssClass="alert-danger" element="div" />
						   
							<fieldset>
							<div class="form-group">
								<label class="col-sm-3 control-label">
								<strong style="font-size:20px;font-weight:bold"><s:message code="label.customer.safety.phone" text="Change phone"/></strong></label>
								<div class="col-sm-9">
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
								<label class="col-sm-3 control-label" for="currentPhone"><s:message code="label.customer.currentphone" text="Current Phone"/>:</label>
								<div class="col-sm-9">
								   <span style="line-height:30px;font-size:14px">${phone }</span>						
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
                                       		<input id="btnSendCode" class="btn btn-info" type="button" value="<s:message code="label.generic.phone.message" text="Phone"/>" onclick="sendPhonemsgForPW()" />
                                       	</div>   
							</div>
							
							<hr/>	
							
							 <div class="form-group">
								<label class="col-sm-3 control-label" for="newPhone"><s:message code="label.customer.newphone" text="New Phone"/>:</label>
								<div class="col-sm-5">
									<s:message code="messages.invalid.phone" text="Phone is required" var="msgPhone"/>
								   <input type="text" name="phone2" id="phone2" class="required phone form-control" title="${msgPhone }"/>						
								</div>
							 </div>
							
							<div class="form-group">
				         		 <label class="col-sm-3 control-label"><s:message code="label.generic.validatecode" text="Validatecode"/>(*):</label>
				         		  <div class="col-sm-5">			              		
			              					 <s:message code="NotEmpty.store.validatecode" text="validatecode is required" var="validatecodeName2"/>
										   <input type="text" Class="required company form-control " id="captchaResponseField2" name="captchaResponseField2" title="${validatecodeName2}"/>
				            		</div>
				            		<div class="col-sm-4" style="line-height:35px;">	
				            			 <a style="cursor: pointer;" onclick="changeValidateImg('2')"><img id="imgObj2" src="<c:url value="/shop/store/code.html?flag=2"/>" style="margin-top:7px;"/>&nbsp;&nbsp;<s:message code="label.generic.change.validatecode" text="Change"/></a>
				            		</div>
				         </div>
							<div class="form-group">
											<label class="control-label col-sm-3"><s:message code="label.generic.phone.short" text="Phone"/><s:message code="label.generic.validatecode" text="Validatecode"/>(*):</label>
											<div class="col-sm-5">
											   <s:message code="NotEmpty.store.validatecode" text="validatecode is required" var="validatecodeName2"/>
											    <input type="text" Class="required company form-control " name="phonevalidate2" title="${validatecodeName2}"/>
											  
											  <div id="messageSucces2" style="display:none;">
											  <s:message code="label.generic.phone.message.before" text="Message"/><span id="messageSecond2"></span>
											  <s:message code="label.generic.phone.message.after" text="Message"/></div>
											  <div id="messageFail2" style="display:none;color:red"><s:message code="label.generic.phone.message.fail" text="Message" /></div>	                                        	
	                                        </div>
	                                     <div class="col-sm-4">
	                                        		<input id="btnSendCode2" class="btn btn-info" type="button" value="<s:message code="label.generic.newphone.message" text="Phone"/>" onclick="sendPhoneMessage('2')" />
	                                        	</div>   
							</div>
							
													
							<div class="form-group">
								<div class="col-sm-5 col-sm-offset-3">
									<input id="submitChangePhone" class="btn  btn-disabled btn-info" type="submit" name="changePhone" value="<s:message code="button.label.submit2" text="Change Phone"/>" disabled="">
								</div>
							</div>			
							</fieldset>				
						</form>
				</div>			
		<div class="col-sm-4 " style="padding-top:90px;">
			<div id="store.success" class=" alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>
					<div id="formError"  class="alert alert-warning " style="display:none;"></div>
		</div>
</div>
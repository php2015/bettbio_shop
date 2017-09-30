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
 <link href="<c:url value="/resources/templates/bootstrap3/css/path-css.css" />" rel="stylesheet" type="text/css">
  <script src="<c:url value="/resources/js/check-functions.js" />"></script>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<c:url var="verifyPhone" value="/shop/customer/verifyPhone.html"/>
<script type="text/javascript">

$(document).ready(function() {
	$("input[type='text']").on("change keyup paste", function(){
		isEzybioFormValid();
	});
	if($('.alert-error').is(":visible")) {
		return true;
	}
	isEzybioFormValid("${erroInfo}");
	
});
function showErro(status){
	 if(status==-2){
		 $("#messageFail").html('<s:message code="NotEmpty.contact.captchaResponseField" text="Message" />');
	 }else if(status==-3){
		 $("#messageFail").html('<s:message code="validaion.recaptcha.not.matched" text="Message" />');
		 
	 }else if(status ==-4){
		 $("#messageFail").html('<s:message code="message.username.notfound" text="Message" />');
	 } else{
		 $("#messageFail").html('<s:message code="label.generic.phone.message.fail" text="Message" />');
	 }
	 changeValidateImg();
	 $("#messageFail").show();
}
 </script>

 <div  style="padding:1%">
 	<div class="box">
		<div >
			<nav>
				<ol class="cd-multi-steps text-bottom count ">
					<li class="font-normal"><em class="font-normal"><s:message code="label.custmoer.write" text="Write" /><s:message code="label.generic.amount" text="count" /></em></li>
					<li class="current font-normal" ><em class="font-normal"><s:message code="label.custmoer.virity" text="count" /></em></li>
					<li class=" font-normal"><em class="font-normal"><s:message code="label.generic.set" text="Write" /><s:message code="label.generic.newpassword" text="count" /></em></li>
					<li ><em class="font-normal"><s:message code="label.custmoer.password.done" text="count" /></em></li>
				</ol>
			</nav>
		</div>
		<div style="padding-top:2%" class="row">
			<div class="col-sm-9 "  >
				<form action="${verifyPhone}" method="POST" id="ezybioForm" class="form-horizontal" name="ezybioForm" >
		                <fieldset>
		                	<div class="form-group">
					             <label class="col-sm-2 control-label col-sm-offset-2" ><s:message code="label.username" text="UserName" />:</label>
					           		 <div class="col-sm-6 ">			              		
					  				  ${nick}
					 				  </div>
					      	</div>
					      	<div class="form-group">
					             <label class="col-sm-2 control-label col-sm-offset-2" ><s:message code="label.generic.phone" text="Phone"/>:</label>
					           		 <div class="col-sm-6 ">			              		
					  				 ${phone}
					 				  </div>
					      	</div>
					      	<div class="form-group">
				         		 <label class="col-sm-2 control-label col-sm-offset-2"><s:message code="label.generic.validatecode" text="Validatecode"/>(*):</label>
				         		  <div class="col-sm-4">			              		
			              					 <s:message code="NotEmpty.store.validatecode" text="validatecode is required" var="validatecodeName"/>
										   <input type="text" Class="required company form-control " id="captchaResponseField" name="captchaResponseField" title="${validatecodeName}"/>
				            		</div>
				            		<div class="col-sm-4" style="line-height:35px;">	
				            			 <a style="cursor: pointer;" onclick="changeValidateImg()"><img id="imgObj" src="<c:url value="/shop/store/code.html"/>" style="margin-top:7px;"/>&nbsp;&nbsp;<s:message code="label.generic.change.validatecode" text="Change"/></a>
				            		</div>
				         </div>
					      	<div class="form-group">
								<label class="control-label col-sm-2 col-sm-offset-2"><s:message code="label.generic.phone.short" text="Phone"/><s:message code="label.generic.validatecode" text="Validatecode"/>(*):</label>
								<div class="col-sm-4">
								   <s:message code="NotEmpty.store.validatecode" text="validatecode is required" var="validatephone"/>
								    <input type="text" Class="required company form-control " name="phonecode" title="${validatephone}"/>
								   
								  </div>
								  <div class="col-sm-4">
									   <input id="btnSendCode" type="button" value="<s:message code="label.generic.phone.message" text="Phone"/>" onclick="sendPhoneNum()" />
									  <div id="messageSucces" style="display:none;">
									  <s:message code="label.generic.phone.message.before" text="Message"/><span id="messageSecond"></span>
									  <s:message code="label.generic.phone.message.after" text="Message"/></div>
									  <div id="messageFail" style="display:none;"><s:message code="label.generic.phone.message.fail" text="Message" /></div>
		                        </div>
							</div>
					      	<div class="form-group">
					  				<div class="col-sm-6 col-sm-offset-4">
									<input id="Ezybiosubmit" type="submit" value="<s:message code="button.label.submit2" text="Send"/>" name="register" class="btn btn-large">
				 				</div>
							</div>	
		                </fieldset>
		         </form>  
			</div>
			<div class="col-sm-2">
			        <div id="store.error" class="alert-error" style="display:none;background-color:#0080c0;min-height:20px;color:ffffff;paddingt:10px;"></div>
			   </div>
			
		</div>
	</div>	
</div>
		
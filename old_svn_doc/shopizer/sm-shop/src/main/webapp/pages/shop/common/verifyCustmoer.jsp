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
<style>
.col-sm-offset-2{
text-align: right;
}
</style>
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

function sendSms(){
	$.post("${pageContext.request.contextPath}/shop/sendSMSCode.html",{phone:$("#phone").text()},function(data){
		if(data.response.status==0){
			$("#btnSendCode").attr("disabled","disabled");
			setTimeout(function(){
				$("#btnSendCode").removeAttr("disabled");
				},1000*59);
			alert("手机验证码发送成功");
		}else{
			alert("手机验证码发送失败");
		}
	});
	
}
 </script>

 <div  style="padding:1%">
 	<div class="box">
		<div style="margin-left:-120px">
					<img src="<c:url value="/resources/img/2.png"/>" style="width: 100%;padding-left: 176px"/>
		</div>
		<div class="row" style="font-size: 14px;margin-top: -105px;margin-left: 0px;margin-bottom: 10px;">找回密码</div>
		<div style="padding-top:2%" class="row">
			<div class="col-sm-9 " style="margin:140px;" >
				<form action="${verifyPhone}" method="POST" id="ezybioForm" class="form-horizontal" name="ezybioForm" >
		                <fieldset>
					      	<div class="form-group">
					             <label class="col-sm-2 control-label col-sm-offset-2" style="text-align: right;"><s:message code="label.generic.phone" text="Phone"/>:</label>
					           		 <div class="col-sm-6 " style="margin-top:7px" id="phone">${phone}</div>
					      	</div>
					      	<div class="form-group" style="padding-top:20px">
								<label class="control-label col-sm-2 col-sm-offset-2" style="text-align: right;"><s:message code="label.generic.validatecode" text="Validatecode"/>:</label>
								<div class="col-sm-4">
								   <s:message code="NotEmpty.store.validatecode" text="validatecode is required" var="validatephone"/>
								    <input type="text" Class="required company form-control " name="phonecode" title="${validatephone}"/>
								   
								  </div>
								  <div class="col-sm-4">
									   <input id="btnSendCode" type="button" value="<s:message code="label.generic.phone.message" text="Phone"/>" class="btn btn-small" onclick="sendSms()" />
									  <div id="messageSucces" style="display:none;">
									  <s:message code="label.generic.phone.message.before" text="Message"/><span id="messageSecond"></span>
									  <s:message code="label.generic.phone.message.after" text="Message"/></div>
									  <div id="messageFail" style="display:none;"><s:message code="label.generic.phone.message.fail" text="Message" /></div>
		                        </div>
							</div>
					      	<div class="form-group">
					  				<div class="col-sm-6 col-sm-offset-4">
									<input id="Ezybiosubmit" type="submit" value="<s:message code="button.label.submit2" text="Send" />" name="register" class="btn btn-large">
				 				</div>
							</div>	
		                </fieldset>
		         </form>  
			</div>
		</div>
	</div>	
</div>
		
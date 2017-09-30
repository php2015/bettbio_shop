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
<script src="<c:url value="/resources/js/jquery.maskedinput.min.js" />"></script>
<script src="<c:url value="/resources/js/shop-customer.js" />"></script>
<script src="<c:url value="/resources/js/address.js" />"></script>
<script src="<c:url value="/resources/js/shopping-cart.js" />"></script>
<script src="<c:url value="/resources/js/check-functions.js" />"></script>
<script src="<c:url value="/resources/js/pwstrength.js" />"></script>


<script type="text/javascript">
var RecaptchaOptions = {
	    theme : 'clean'
};
$(function() {
	//isFormValid();
	$("input[type='text']").on("change keyup paste", function(){
		isFormValid();
	});
	
	$("input[type='password']").on("change keyup paste", function(){
		isFormValid();
	});
	
	$("#userProtocolCheckbox").click(function(){
		isFormValid();
	});
	
	//手机号验证
    $(".storemobile").keyup(function(){
    	var re=/^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])\d{8}$/i;
    	var storemobile=$(this).val();
		if (storemobile.length < 11){
			return;
		}
		$("#registrationError").hide();
    	if(storemobile!=''){
    		if(re.test(storemobile)){
		    	 $.ajax({
		          url:'${pageContext.request.contextPath}/shop/customer/valitationUser.html',
		          type:'post',
		          data:{
		              index:1,
		              account:storemobile
		          },
		          dataType:'json',
		          success:function(data){
		          	if(data.response.status==0){//验证是否成功
			          	$(".storemobile").attr("success",true);
						$("#btnSendCode").removeAttr("disabled");
		          	}else{
		          			$(".storemobile").attr("success",false);
    	    	  			$("#registrationError").text("该手机已存在,不能重复注册");
							$("#registrationError").show();
		          	}
		          }
		       });
    		}else{
    			  $(".storemobile").attr("success",false);
    	    	  $("#registrationError").text("请输入正确手机号码");
				  $("#registrationError").show();
    		}
    	}else{
    		$("#registrationError").text("请输入正确手机号码");
			$("#registrationError").show();
    	}
    	
    }).focus(function(){
    	//$(".storemobile").attr("success",false);
    	if($(this).val()=="建议完善个人联络方式"){
    		$(this).val("");
    	}
    });
	
	var isSendSMSCOde=false;
    //发送手机验证码
    $("#btnSendCode").click(function(){
    	//获取手机号
    	var phone=$(".storemobile").val();
    	var success=$(".storemobile").attr("success");
		$("#registrationError").hide();
    	if(phone!=""&&success){
    		$.post("${pageContext.request.contextPath}/shop/customer/sendSMSCode.html",{phone:phone},function(data){
    			if(data.response.status==0){//验证码发送成功
    				$("#btnSendCode").attr("disabled","disabled");
    				isSendSMSCOde=true;
    				setTimeout(function(){
    				$("#btnSendCode").removeAttr("disabled");
    				},1000*59);
    				if (typeof data.response._randomNumber == "string"){
    					alert("验证码发送成功: " + data.response._randomNumber);
						$('#phoneValation').val(data.response._randomNumber);
    				}else{
    					alert("验证码发送成功,请查看手机获得短信验证码");
    				}
    				
    			}else{
    				$("#registrationError").text("验证码发送失败");
    				$("#registrationError").show();
    			}
    		},"json");
    	}else{
			$("#registrationError").text("请填写正确的手机号");
			$("#registrationError").show();
    	}
    });
    
    //校验手机验证码
    $("#phoneValation").focus(function(){
    	$("#phoneValation").attr("success",false);
    	$("#phoneValation").parent().siblings(".success").hide();
    	$("#phoneValation_tips").find("img").attr("src",getContextPath()+"/resources/ad/icon.png");
    	$("#phoneValation_tips").css('visibility','visible');
    	$("#phoneValation_title").text("请输入手机验证码");
    }).blur(function(){
    	var smsCode=$(this).val();
		$("#registrationError").hide();
    	if(smsCode==""){//验证码是否为空
    		$("#registrationError").text("请填写手机验证码");
			$("#registrationError").show();
    		return false;
    	}
    	//判断是否发送验证码 
    	if(isSendSMSCOde==false){
      		$("#registrationError").text("未发送手机验证码");
			$("#registrationError").show();
    		return false;
    	}
    	//发送校验请求
    	$.post("${pageContext.request.contextPath}/shop/customer/valitationBySMSCode.html",{smsCode:smsCode},function(data){
    		if(data.response.status==0){//验证是否成功
    		  $("#phoneValation").attr("success",true);
    		}else{
    		  $("#phoneValation").attr("success",false);
        	  $("#registrationError").text.text("验证码不正确");
			  $("#registrationError").show();
    		}
    	},"json"
   )});
});

function validateCode() {
	$('#checkCodeStatus').html(
			'<img src="<c:url value="/resources/img/ajax-loader.gif" />');
	$('#checkCodeStatus').show();
	var adminName = $("#adminName").val();
	var id = $("#id").val();
	checkCode(adminName, id,
			'checkUserCode.html','checkCodeStatus');
}
function validateStoreCode() {
	$('#checkCodeStatus').html(
			'<img src="<c:url value="/resources/img/ajax-loader.gif" />');
	$('#checkCodeStatus').show();
	var adminName = $("#storename").val();
	var id = $("#id").val();
	checkCode(adminName, id,
			'checkStoreCode.html','checkStoreCodeStatus');
}

function callBackCheckCode(msg, code,msgelement) {

	if (code == 0) {
		$('.btn').removeClass('disabled');
	}
	if (code == 9999) {

		$('#'+msgelement)
				.html(
						'<font color="green"><s:message code="message.code.available" text="This code is available"/></font>');
		$('#'+msgelement).show();
		$('.btn').removeClass('disabled');
	}
	if (code == 9998) {

		$('#'+msgelement)
				.html(
						'<font color="red"><s:message code="message.code.exist" text="This code already exist"/></font>');
		$('#'+msgelement).show();
		$('.btn').addClass('disabled');
	}

}

function isFormValid() {
	
	if($('.alert-error').is(":visible")) {
		return true;
	}
	
	if($('.alert-success').is(":visible")) {
		return true;
	}
	var userProtol = false;
	
	if($("#userProtocolCheckbox").attr('checked')){
		userProtol =true;
	}
		
	if ($("#phoneValation").attr("success") == false){
		return false;
	}
	$('#registrationError').hide();//reset error message
	
	var msg = isCustomerFormValid($('#adminRegisterForm'));
	
	if(msg == null && userProtol== true){
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

 function showErro(status){
	 if(status==-2){
		 $("#messageFail").html('<s:message code="NotEmpty.contact.captchaResponseField" text="Message" />');
	 }else if(status==-3){
		 $("#messageFail").html('<s:message code="validaion.recaptcha.not.matched" text="Message" />');
		 changeValidateImg();
	 }
	 $("#messageFail").show();
 }
 
 function checkCode(code, id, url,msgelement) {
		$.ajax({
				type: 'POST',
				dataType: "json",
				url: url,
				data: "code="+ code + "&id=" + id,
				success: function(response) { 
					var msg = response.response.statusMessage ;
					var status = response.response.status ;
					callBackCheckCode(msg,status,msgelement);
				},
				error: function(jqXHR,textStatus,errorThrown) { 
				}
		});
	}
 
 function getInvalidEmailMessage() {
		return '<s:message code="messages.invalid.email" text="Invalid email address"/>';
	}

	function getInvalidUserNameMessage() {
		return '<s:message code="registration.username.length.invalid" text="User name must be at least 6 characters long"/>';
	}		

	function getInvalidPasswordMessage() {
		return '<s:message code="message.password.length" text="Password must be at least 6 characters long"/>';
	}

	function getWeekPasswordMessage() {
		return '<s:message code="newpassword.not.week" text="Password is too week"/>';
	}

	function getInvalidCheckPasswordMessage() {
		return '<s:message code="message.password.checkpassword.identical" text="Both password must match"/>';
	}
 </script>
<c:set var="register_url" value="${pageContext.request.contextPath}/shop/customer/registerStore.html"/>

	<div class="box main-padding-lr row" >			
			<div class="col-sm-7 col-sm-offset-1">
			<h3><s:message code="label.store.register" text="Store Register" /></h3>
				<form:form method="POST" commandName="user" action="${register_url}" class="form-horizontal" id="adminRegisterForm" >
		<form:errors path="*" cssClass="alert alert-danger" element="div" />
		<div id="store.success" class="alert alert-success"
			style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>">
			<s:message code="message.success" text="Request successfull" />
		</div>
		<div id="registrationError"  class="alert alert-warning common-row text-center" style="display:none;"></div>
	<form:input type="hidden" path="defaultLanguage.id" value="${user.defaultLanguage.id}" />
		
		<div class="form-group form-group-lg">
			<label class="col-sm-2 control-label"><s:message
					code="label.generic.username" text="User name" />(*)</label>
			<div class="col-sm-10">
			<s:message code="NotEmpty.customer.userName" text="User name is required" var="msgUserName"/>
				<form:input cssClass="form-control" path="adminName"
					onblur="validateCode()" title="${msgUserName}"/>
				<span class="help-inline"><div id="checkCodeStatus"
						style="display:none;"></div>
					<form:errors path="adminName" cssClass="error" /></span>
			</div>
		</div>

		<div class="form-group form-group-lg">
			<label class="col-sm-2 control-label"><s:message
					code="label.store.title" text="Store" />(*) </label>
			<div class="col-sm-10 ">
			<s:message code="NotEmpty.store.storename" text="User name is required" var="msgStoreName"/>
					<input type="text" class="form-control "id="storename" name="storename" title="${msgStoreName}" onblur="validateStoreCode()" />
				<span class="help-inline"><div id="checkStoreCodeStatus"
						style="display:none;"></div><form:errors path="merchantStore" cssClass="error" /></span>
				</div>
			</div>

					<div class="form-group form-group-lg">
						<label class="col-sm-2 control-label"><s:message
								code="label.generic.email" text="Email" />(*)</label>
						<div class="col-sm-10">
							<s:message code="NotEmpty.store.email" text="User name is required" var="msgEmail"/>
							<form:input cssClass="form-control email" path="adminEmail" title="${msgEmail}"/>
							<span class="help-inline"><form:errors path="adminEmail"
									cssClass="error" /></span>
						</div>
					</div>
					<div class="form-group form-group-lg">
						<label class="col-sm-2 control-label"><s:message
								code="label.generic.firstname" text="First name" /> </label>
						<div class="col-sm-10">
							<form:input cssClass="form-control" path="firstName" />
							<span class="help-inline"><form:errors path="firstName"
									cssClass="error" /></span>
						</div>
					</div>
					<!-- 手机 -->
					<div class="form-group form-group-lg">
						<label class="col-sm-2 control-label">手机号(*)</label>
						<div class="col-sm-10">
							<form:input cssClass="form-control storemobile required" path="mobile" title="请输入手机号"/>
							<span class="help-inline"><form:errors path="mobile"
									cssClass="error" /></span>
						</div>
					</div>
					<!-- 手机验证码 -->
					<div class="form-group form-group-lg">
						<label class="col-sm-2 control-label">短信验证码(*)</label>
						<div class="col-sm-10">
							<input id="phoneValation" class="form-control required" name="smsCode" style="width: 50%; display: inline-block;" title="请输入短信验证码"/>
							<input id="btnSendCode" type="button" class="form-control btn btn-info btn-block pull-right" 
									style="background-color:#5bc0de !important;width:40%; display:inline-block;"  
									value="获取手机验证码" disabled='disabled' ></input>
							
						</div>
					</div>
						  
						<div class="form-group form-group-lg">
							<label class="col-sm-2 control-label"><s:message
									code="label.emailconfig.password" text="Password" />(*)</label>
							<div class="col-sm-10">
								<form:password cssClass="form-control password" path="adminPassword" id="newPassword" onkeyup="pwStrength(this.value);" onblur="pwStrength(this.value);"/>
								<span class="help-inline"><form:errors
										path="adminPassword" cssClass="error" /></span>
							</div>

						</div>
						
					<div class="form-group form-group-lg">
		                        <label class="col-sm-2 control-label"></label>
		                        <div class="col-sm-10">
		                        	 <table width="180" border="1" cellspacing="0" cellpadding="1" bordercolor="#eeeeee" height="22" style='display:inline;'>  
										<tr align="center" bgcolor="#f5f5f5">  
										<td width="33%" id="strength_L" style="padding:3px"><s:message code="password.strendth.low" text="Strength Low"/></td>  
										<td width="33%" id="strength_M" style="padding:3px"><s:message code="password.strendth.middle" text="Strength Middle"/></td>  
										<td width="33%" id="strength_H" style="padding:3px"><s:message code="password.strendth.high" text="Strength High"/></td> 
										</tr>  
									</table> 
									<s:message code="password.strength.rule" text="Strength password"/>
		                        </div>			                       		                        
			            </div>
				            
						<div class="form-group form-group-lg">
							<label class="col-sm-2 control-label"><s:message
									code="label.generic.repeatpassword" text="Rep Password" />(*)</label>
							<div class="col-sm-10">
								<input type="password" class="form-control password" id="passwordAgain" onkeyup="repeatPwd(this.value);" onblur="repeatPwd(this.value);"/>
								<span class="help-inline"><form:errors
										path="adminPassword" cssClass="error" /></span>
							</div>

						</div>
						 <div class="form-group form-group-lg" id="repeatpwd" style="display:none;">
				                       <label class="col-sm-2 control-label"></label>
					                        <div class="col-sm-10">
					                        		 <label style="color:red"><s:message code="password.notequal" text="Repeat new password"/></label>
					                        </div>
				                  </div>
					<div class="form-group form-group-lg">
				         		 <label class="col-sm-2 control-label"><s:message code="label.generic.validatecode" text="Validatecode"/>(*):</label>
				         		  <div class="col-sm-5">			              		
			              					 <s:message code="NotEmpty.store.validatecode" text="validatecode is required" var="validatecodeName"/>
										   <input type="text" Class="required company form-control " id="captchaResponseField" name="captchaResponseField" title="${validatecodeName}"/>
				            		</div>
				            		<div class="col-sm-5" style="line-height:35px;">	
				            			 <a style="cursor: pointer;" onclick="changeValidateImg()"><img id="imgObj" src="<c:url value="/shop/store/code.html"/>" style="margin-top:7px;"/>&nbsp;&nbsp;<s:message code="label.generic.change.validatecode" text="Change"/></a>
				            		</div>
				         </div>
					
					<form:hidden path="id" />					
					<c:if test="${user.id!=null && user.id>0}">
						<form:hidden path="adminPassword" />
					</c:if>

					<div class="form-actions">
					 <div class="col-sm-5 col-sm-offset-2">
			                        		<input type="checkbox" id="userProtocolCheckbox" checked="checked"/>
			                        		<span style="height:30px;line-height:30px;font-size:14px;margin:10px;"><s:message code="label.custmoer.agree" text="Agree"/>
			                        		</span><a style="font-size:14px;color:blue;" target="blank" href="<c:url value="/shop/customer/protocol.html"/>">《<s:message code="label.custmoer.protocol" text="Protocal"/>》</a>
			                        		<div id="agreeMsg" style="display:none;color:red"><s:message code="label.custmoer.agree.info" text="Please Agree Information"/></div>
			                        </div>
			            <div class="col-sm-3 col-sm-offset-2">
     									<button type="submit" class="btn btn-info btn-block" id="submitRegistration">
								<s:message code="button.label.submit" text="Submit" />
							</button>
  							 		 </div>  
					</div>
	</form:form>
				<!-- end registration form--> 
				
			</div>
			<div class="col-sm-3 col-sm-offset-1" style="position:fixed; top:170px;right:10px;">
					<div id="registrationError"  class="alert alert-warning common-row text-center" style="display:none;"></div><br>
			</div>
	</div>
	
	<!--close .container "main-content" -->
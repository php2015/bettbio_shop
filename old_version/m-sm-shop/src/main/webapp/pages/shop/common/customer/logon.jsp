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

<script src="<c:url value='/resources/js/login.js'/>?v=<%=new java.util.Date().getTime()%>"></script>
<script type="text/javascript" charset="utf-8">
 	var userholder ='<s:message	code="label.longin.username" text="holde" />';
 	var urlVerifyImgCode = "<c:url value='/shop/customer/valitationByImgCode.html'/>";
	var smsSendUrl = "<c:url value='/shop/customer/sendLogonSMSCode.html'/>";
 </script>
<style>
.login_mode_label {
	border-radius:8px 8px 0px 0px;
	width: 50%;
	text-align: center;
	v-align: center;
	font-size: 14px;
	padding: 5px 20px;
	color: white;
	background-color: #2672c8;
	position: absolute;
	bottom: -5px;
}
.login_mode_selected {
	color: black;
	font-size: 14px;
	font-weight: bold;
	background-color: #FFF;
	z-index: 1000;
}
.sa{
	background-color: #31b0d5;
	border-color: #269abc;
}
</style>
<div class="container-fluid" style="background: #2672c8;">
	<div id="div_selectLogonMode" style="position: relative; height: 40px;">
		<a href="#" onclick="showLogonMode('sms')"><label  id="label_loginBySms" class="login_mode_label login_mode_selected  pull-left" style="left: 10px;">短信登录</label></a>
		<a href="#" onclick="showLogonMode('password')"><label id="label_loginByPassword" class="login_mode_label pull-right" style="right: 10px;">账号密码登录</label></a>
	</div>
</div>
<div class="container-fluid">

	<div class="row">
	
		<div class="col-xs-12col-xs-offset-0" style="padding:20px;" >

			<!-- 使用密码登录的窗口 -->
			<div id="div_loginByPassword" style="display:none;">
				<form id="login" method="post" accept-charset="UTF-8">
					<span id="loginError" class="alert alert-danger" style="display:none;"></span>
					<br/>
					<div class="control-group">
						<br/>
						<div class="controls input-group">
							<span class="input-group-addon"> <span class=" glyphicon glyphicon-user" aria-hidden="true"  ></span></span>
							<input id="signin_userName" class="form-control" type="text" name="userName" size="18" 
									placeholder="<s:message	code='label.longin.username' text='User Name' />" >
							</input>
						</div>
					</div>
					<br/>
					<div class="control-group">
						<div class="controls input-group">
							<span class="input-group-addon"> <span class=" glyphicon glyphicon-lock" aria-hidden="true"  ></span></span>
							<input id="signin_password" class="form-control" type="password" name="password" size="18"
									placeholder="登录密码">
							</input>
						</div>
					</div>
		
					<div class="control-group">
						<br/>
						<div class="controls">
							<label class=" btn-block">
								<input type="checkbox" id="remember" ><s:message code="label.logonform.rememberusername" text="Remember my user name" />
							</label>
						</div>
					</div>
		
					<br/>
					<div class="control-group">
						<button id="login-button" type="button" class="btn btn-default btn-info btn-block" ripple><s:message code="button.label.login" text="Login" /></button>	
					</div>
					<input id="signin_storeCode" name="storeCode" type="hidden" value="<c:out value="${requestScope.MERCHANT_STORE.code}"/>" />

				</form>
			</div>
			<!-- 使用短信登录的窗口 -->
			<div id="div_loginBySms">
				<form id="form_loginBySms" method="post" accept-charset="UTF-8">
					<span id="loginErrorSms" class="alert alert-danger" style="display:none;"></span>
					<br/>
					<!-- mobile number  -->
					<div class="control-group">
						<br/>
						<div class="controls input-group">
							<span class="input-group-addon"> <span class=" glyphicon glyphicon-phone" aria-hidden="true"  ></span></span>
							<input id="input_login_mobile" class="form-control" type="text" name="mobile" size="18" 
									onKeyUp="onMobileChanged()" onChange="onMobileChanged()"
									placeholder="手机号码" >
							</input>
						</div>
					</div>
					<br/>
					<!-- recaptcha -->
					<div class="control-group">
                        <div class="controls input-group">
                            <span class="input-group-addon"> <span class=" glyphicon glyphicon-question-sign" aria-hidden="true" id="j_imgCode_help" ></span></span>
                            <input  id="input_imgCode" class="form-control" type="text" name="imgCode" size="18" 
									placeholder="验证码" style="width: 50%;" 
									onKeyUp="onImgCodeChange()"></input>
							<img id="imgObj" class="pull-right" src="<c:url value='/shop/store/code.html'/>" 
									style="width:40%;height:40px;margin-right:10px;" onclick="changeValidateImg()"/>
                        </div>
						<div class="pull-right">
							<a id="prompt_get_imgCode" href="javascript:changeValidateImg();" style="width:120px;height:40px;margin-right:10px;">看不清?点击图像换一个</a>
						</div>
                    </div>
					
					<!-- SMS code -->
                    <div class="control-group">
						<br/>
                        <div class="controls input-group">
                            <span class="input-group-addon"> <span class=" glyphicon glyphicon-lock" aria-hidden="true" id="j_password_help" ></span></span>
                            <input id="input_smsCode" class="form-control" type="text" name="smsCode" size="18" placeholder="短信验证码" style="width: 50%;" />
							<button id="btn_sendSms" type="button" class="pull-right btn btn-info" style="width:40%;height:40px;margin-right:10px;" onclick="sendSmsCode();return false;" >获取验证码</button>
                        </div>
						<div class="pull-right" id="prompt_resend_sms" style="height:40px;margin-right:10px;"> </div>
                    </div>
					<br/>
					
						
					<br/>
					<div class="control-group">
						<button id="btn_loginBySms" type="button" class="btn btn-default btn-info btn-block" ripple onclick="loginBySms()">
							<s:message code="button.label.login" text="Login" />
						</button>	
					</div>
					<input id="sms_storeCode" name="storeCode" type="hidden" value="<c:out value="${requestScope.MERCHANT_STORE.code}"/>" />
					<input name="logonMode" type="hidden" value="sms" />

				</form>
			</div>
			<br/>
	<div class="logon-password-box">
		<div class="pull-left"><a onClick="javascript:location.href='<c:url value="/shop/customer/registration.html" />';" href="" role="button" data-toggle="modal"><s:message code="label.register.notyetregistered" text="Not yet registered ?" /></a></div>
		<div class="pull-right"><a	onClick="javascript:location.href='<c:url value="/shop/customer/resetPassword.html" />';" href="" role="button" data-toggle="modal"><s:message code="label.custmoer.forget.password" text="Get Password" /></a></div>
	</div>
		</div>
	</div>
</div>
					



		

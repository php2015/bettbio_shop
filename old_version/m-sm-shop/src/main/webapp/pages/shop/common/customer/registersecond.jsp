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
<script src="<c:url value="/resources/js/check-functions.js" />"></script>
<script type="text/javascript">
var phone="${customer.phone}";
$(document).ready( function() {
	getPhoneNum();
	messageInterValObj = setInterval(SetRemainTime, 1000);
	
	//setInterval(alert("aaa"), 10000);
});
/**
 * 提交注册前的校验控制
 */

function getPhoneNum(){
	
	var phone1 = phone.substring(0,3)+'******'+phone.substring(9);
	$("#phonenum").html(phone1);
}
function showErro(status){
	 if(status==-2){
		 $("#messageFail").html('<s:message code="NotEmpty.contact.captchaResponseField" text="Message" />');
	 }else if(status==-3){
		 $("#messageFail").html('<s:message code="validaion.recaptcha.not.matched" text="Message" />');
		 changeValidateImg();
	 }else if(status==-4){
		 $("#messageFail").html('<s:message code="label.generic.phone.message.fail" text="Message" />');
	 }else {
		 $("#messageFail").html('<s:message code="registration.phone.already.exists" text="Message" />');
	 }
	 $("#messageFail").show();
}

 </script>
 <script type="text/javascript">
    $(function(){
       var msg ="";
       $("#checkPhonecode").blur(function(){
          if($(this).val()=="" || $(this).val()==null)
          {
               $("#messageFail").html("验证码不能为空").css({"color":"red","padding":"8px 35px","font-size":"14px"});
               $("#next_submit").css({"color":"#999","background-color":"#d9d9d9"});
               msg ="s";
               return false;
          }
          else
          {
               $("#next_submit").css({"color":"#fff","background-color":"#5a8de2"});
               msg ="";
          }
       }).focus(function(){
           if($(this).val()!="" || $(this).val()!=null)
           {
              $("#next_submit").css({"color":"#fff","background-color":"#5a8de2"});
              msg ="";
           }
       });
       
       $("#next_submit").click(function(){
           if(msg=="")
           {
              $("#registrationForm").submit();
           }else{
             return false;
           }
       });
       
    });
 </script>
  <style>
 	@media screen and (min-width:320px )and (max-width:375px){
 	  #yd{
 	   padding-left: 28px!important;
 	  }
 	  #yd span{
 	    font-size: 15px!important;
 	  }
 	  #registrationForm div span{
 	  	padding-left: 24px;
 	  }
 	  #registrationForm div input{
 	   width: 40%;
 	  }
 	}
 </style>
 <input type="text" value="${msg}" id="msg" hidden="hidden"/>
<c:set var="register_url" value="${pageContext.request.contextPath}/shop/customer/rsecond.html"/>
	<div style="background: #eeeeee;height:100%">
		<div class="nav-ezybio container-fluid text-center">
			<div class="row" style="padding:11px 10px;">
				<div class="pull-left" style="width:30%;text-align:left;"><a href="#" onclick="window.history.go(-1)"><img src="<c:url value="/resources/img/left.png"/>" height="28"/></a></div>
				<div class="pull-left" style="width:40%;font-size:20px;color:#ffffff;">个人注册</div>
				<div class="pull-left" style="width:30%;text-align:right;line-height: 30px;"><a href="<c:url value="/shop/customer/storeRegistration.html"/>" ><span style="color:#ffffff;">切换卖家注册</span></a></div>
			</div>
		</div>
		<div style="background:#ffffff;padding-top:24px; height:72px;">
			<div style="padding-left:10px;" id="yd">
					<span style="font-size:16px;">1.输入手机号</span>
				<span class="padding-img" ><img height="20" src="<c:url value="/resources/img/right.png"/>" /></span>
				<span class="padding-img" style="font-size:16px;color:#5a8de2;font-weight:bold">2.输入验证码</span>
				<span class="padding-img" ><img height="20" src="<c:url value="/resources/img/right.png"/>" /></span>
				
				<span class="padding-img" style="font-size:16px;">3.设置密码</span>
				<span class="padding-img"><img height="20" src="<c:url value="/resources/img/right.png"/>" /></span>
			</div>
					
		</div>
		<div class="text-center" style="padding-top:24px; height:66px;font-size:14px;">
			<span>验证码已发送至到 </span><span id="phonenum"></span>
		</div>
		<form:form method="post" action="${register_url}" id="registrationForm" class="form-horizontal" commandName="customer" onsubmit="return checkInfo();" >
			
			<div style="background:#ffffff;padding-top:20px;padding-bottom:20px;font-size:16px;">
				<span class="padding-register" style="font-size:16px;">请输入验证码：</span><input type="text" style="width:100px;border: 0px;height:30px;" name="checkPhonecode"  id="checkPhonecode"/>
				 
				 <button style="float:right;padding-right:5px;background: #ffffff;margin-top:-5px;" id="btnSendCode" type="button" class="btn " disabled value="<s:message code="label.generic.phone.message" text="Phone"/>" onclick="sendPhoneNumtwo()" >
				 	<span id="messageSecond"></span>重新发送
				 </button>
					<div id="messageFail" style="display:none;color:red"></div>					  
			</div>
			<form:input path="phone" id="phone" type="hidden"/>
			<div style="margin-top:10px;">
				<div class="text-center" style="padding:20px;">
				<button type="submit"  id="next_submit" style="background:#5a8de2;" class="btn btn-lg btn-block"> <span style="color:#ffffff">提交验证码</span></button></div>
			</div>
		</form:form>	
		
	</div>

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
/**
 * 提交注册前的校验控制
 */
$(function(){
   $("#phone").focus();
   $(window).load(function(){
        if($("#msg").val() !=null || $("#msg").val() !=""){
         $("#sp_yz").html(""+$("#msg").val()+"").css({"color":"red","padding":"8px 35px","font-size":"14px"});
        }
   });
   var isMob=/^((\+?86)|(\(\+86\)))?(1[0-9]{10})$/;
   $("#phone").blur(function(){
        if($("#phone").val()=='') {
	   	   $("#sp_phone").html("手机号码不能为空!").css({"color":"red","padding":"8px 35px","font-size":"14px"});
	   	   $("#green_info").hide();
	   	   return false;
		}else if(!isMob.test($("#phone").val())){
			$("#sp_phone").html("手机号码格式输入错误!").css({"color":"red","padding":"8px 35px","font-size":"14px"});
			$("#green_info").hide();
			return false;
		}
        $.post("${pageContext.request.contextPath}/shop/customer/checkPhone.html",{phone:$("#phone").val()},function(data){
        	if(data.response.status==0){//验证是否成功
    		   $("#sp_phone").html("");
    		   $("#green_info").show();
    		   $("#phone").attr("success","true");
        	}else{
    			$("#sp_phone").html("该手机已注册!").css({"color":"red","padding":"8px 35px","font-size":"14px"});
    			$("#green_info").hide();
    			return false;
        	}
        },"json");
   }).focus(function(){
	   $(this).attr("success","false");
   }).change(function(){
	   $("#checkPhonecode").attr("success","false");
	   $("#smsCode").css("visibility","visible").text("请重新获取验证码");
   });
   
   $("#captchaResponseField").blur(function(){
        if($("#captchaResponseField").val()=='') {
		  $("#sp_yz").html("验证码不能为空!").css({"color":"red","padding":"8px 35px","font-size":"14px"});
		  return false;
		}
        $.post("${pageContext.request.contextPath}/shop/customer/valitationByImgCode.html",{imgCode:$(this).val()},function(data){
        	if(data.response.status==0){//验证是否成功
        		$("#sp_yz").html("");
    		   $("#captchaResponseField").attr("success","true");
        	}else{
    			$("#sp_yz").html("验证码不正确!").css({"color":"red","padding":"8px 35px","font-size":"14px"}).show();
    			return false;
        	}
        },"json");
   }).focus(function(){
	   $(this).attr("success","false");
   });
   //发送验证码
   $("#messageSecond").click(function(){
        var phoneSuccess=$("#phone").attr("success");
        if("true"==phoneSuccess){//判断手机号是否正确
        	$.post("${pageContext.request.contextPath}/shop/customer/sendSMSCode.html",{phone:$("#phone").val()},function(data){
    			if(data.response.status==0){//验证码发送成功
    				$("#smsCode").text("短信验证码发送成功");
    			   $("#smsCode").css("visibility","visible").text("短信验证码发送成功");;
    			
    			}else{
    				$("#smsCode").text("短信验证码发送失败");
    				 $("#smsCode").css("visibility","visible");
    			}
        	},"json");
        }else{
        	$("#smsCode").text("手机号码不正确").css("visibility","visible");
        }
   });
   //校验手机验证码
   $("#checkPhonecode").blur(function(){
	   var val= $(this).val();
	   if(val==''){
		   $("#smsCode").text("手机验证码不能为空").css("visibility","visible");
		   return false;
	   }
   	$.post("${pageContext.request.contextPath}/shop/customer/valitationBySMSCode.html",{smsCode:$(this).val(),phone:$("#phone").val()},function(data){
		if(data.response.status==0){//短信验证码校验成功
		   $("#smsCode").css("visibility","hidden");
		   $("#checkPhonecode").attr("success","true");
		}else{
			$("#smsCode").text("短信校验码不正确");
			 $("#smsCode").css("visibility","visible");
		}
	},"json");
	   
   }).focus(function(){
	   $(this).attr("success","false");
   });
   $("#next_submit").click(function(){
	   if(changeBtnStatus()){
		   $("#registrationForm").submit();
	   }
	   return false
   });
});
//改变按钮状态
function changeBtnStatus(){
	var returnValue=true;
    $.each($(".sminput"),function(){
    	if($(this).attr("success")=="false"){
    		returnValue=false;
    	}
    });
	return returnValue;
}
 </script>
 <style>
 	@media (max-width:374px) and (min-width: 320px) {
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
 	  #btnSendCode{
 	  margin-top:-30px!important;
 	  }
 	}
 	@media(max-width: 320px){
 	 .padding-register{
 	 	padding-left: 10px!important;
 	 }
 	 #btnSendCode{
 		margin-top:-30px!important
 	 }
 	}
 	@media(max-width: 375px) and (min-width:360px){
 	 	  #btnSendCode{
 	  margin-top:0px!important;
 	  
 	  }
 	 #registrationForm div input{
 	   width: 35%;
 	  } 	  
 	}
 </style>
 <input type="text" value="${msg}" id="msg" hidden="hidden"/>
<c:set var="register_url" value="${pageContext.request.contextPath}/shop/customer/rfirst.html"/>
	<div style="background: #eeeeee;height:100%">
		<div class="nav-ezybio container-fluid text-center">
			<div class="row" style="padding:11px 10px;">
				<div class="pull-left" style="width:30%;text-align:left;"><a href="<c:url value="/shop/customer/registrationChoice.html"/>"><img src="<c:url value="/resources/img/left.png"/>" height="28"/></a></div>
				<div class="pull-left" style="width:40%;font-size:20px;color:#ffffff;">个人注册</div>
				<div class="pull-left" style="width:30%;text-align:right;line-height: 30px;"><a href="<c:url value="/shop/customer/storeRegistration.html"/>" ><span style="color:#ffffff;">切换商家注册</span></a></div>
			</div>
		</div>
		<div style="background:#ffffff;padding-top:24px; height:72px;">
			<div style="padding-left:10px;" id="yd">
					<span style="font-size:16px;color:#5a8de2;font-weight:bold">1.输入手机号</span>
				<span ><img height="20" src="<c:url value="/resources/img/right.png"/>" /></span>
				<span class="padding-img" style="font-size:16px;">2.设置密码</span>
				<span class="padding-img" ><img height="20" src="<c:url value="/resources/img/right.png"/>" /></span>
				<span class="padding-img" style="font-size:16px;">3.个人资料</span>
				<span class="padding-img"><img height="20" src="<c:url value="/resources/img/right.png"/>" /></span>
			</div>					
		</div>
		<form:form method="post" action="${register_url}" id="registrationForm" class="form-horizontal" commandName="customer">
			<div style="margin-top:20px;background:#ffffff;padding-top:20px;padding-bottom:20px;font-size:16px;">				
				<span class="padding-register" style="font-size:16px;">请输入手机号：</span><form:input path="phone" id="phone" style="border: 0px;height:30px;" success="false" cssClass="sminput" />
				<div id="green_info" style="float: right;padding-right:30px;padding-top:5px;"  hidden="hidden"><img src="<c:url value="/resources/img/zq.png"/>" width="24px" height="24px"/></div>
			</div>
			<div id="sp_phone" style="height: 8px;" ></div>
			<div style="margin-top:20px;background:#ffffff;padding-top:20px;padding-bottom:20px;font-size:16px;">
				<span class="padding-register" style="font-size:16px;">请输入验证码：</span>
				<input type="text" style="width:25%;border: 0px;height:30px;"  id="captchaResponseField" name="captchaResponseField" title="${validatecodeName}" success="false" class="sminput"/>
				<a style="cursor: pointer;float:right;padding-right:5px;" onclick="chgValidateImgStore()"> 
				<img id="imgObj" style="border: 0px;" src="<c:url value="/shop/store/code.html"/>" height="40px" width="100px" />
				</a>
			</div>
			<div id="sp_yz" style="height: 8px;margin-bottom: 20px;">
			</div>
		    <div style="background:#ffffff;padding-top:20px;padding-bottom:20px;font-size:16px;">
				<span class="padding-register" style="font-size:16px;">请输入短信码：</span><input type="text" style="width:100px;border: 0px;height:30px;" name="checkPhonecode"  id="checkPhonecode" success="false" class="sminput"/>
				 
				 <button style="float:right;padding-right:5px;background: #ffffff;margin-top:-5px;" id="btnSendCode" type="button" class="btn " disabled value="<s:message code="label.generic.phone.message" text="Phone"/>" onclick="sendPhoneNumtwo()" >
				 	<span id="messageSecond">点击发送</span>
				 </button>
			</div>
			<div id="smsCode" style="height: 8px;padding-left:35px;color: red;padding-top: 10px;font-size: 14px;visibility:hidden;">
				短信验证码不能为空！
			</div>
			<div style="margin-top:10px;">
				<div class="text-center" style="padding:20px;">
				<button type="submit" id="next_submit" style="background:#5a8de2;" class="btn btn-lg btn-block"> <span style="color:#ffffff">下一步</span></button></div>
		    </div>
		</form:form>	
		<div >
			<div style="padding:20px;">
				<img src="<c:url value="/resources/img/gou.png"/>" /><span style="padding-left:5px;"><s:message code="label.custmoer.agree" text="Agree"/>
			     </span>
			       <a style="font-size:14px;color:blue;" target="blank" href="<c:url value="/shop/customer/protocol.html"/>">《百图用户协议》</a>
			</div>                        		
		</div>
	</div>
	<!--close .container "main-content" -->
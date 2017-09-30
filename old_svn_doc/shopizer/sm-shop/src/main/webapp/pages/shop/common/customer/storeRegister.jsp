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

<script src="<c:url value="/resources/js/jquery.maskedinput.min.js" />"></script>
<script src="<c:url value="/resources/js/jquery.friendurl.min.js" />"></script>

<script src="<c:url value="/resources/js/shopping-cart.js" />"></script>
<script src="<c:url value="/resources/js/pwstrength.js" />"></script>
<script src="<c:url value="/resources/js/shop-customer.js" />"></script><script src="<c:url value="/resources/js/address.js" />"></script>
<script src="<c:url value="/resources/js/validphone.js" />"></script>
<script src="<c:url value="/resources/js/check-functions.js" />"></script>

<script type="text/javascript">
var msg1 = "";
var msg2 = "";
var RecaptchaOptions = {
	    theme : 'clean'
};

$(function() {
	 $(window).load(function(){
	      //$("#login_div").css({"margin-left":"1140px","font-size":"15px"});
	 });
	 
    $(window).resize(function(){
       $("#login_div").css("marginLeft",""+(parseInt($(window).width())-220)+"px");
    });
    
    //默认未校验通过
    $(".valueInput").each(function(){
    	if($(this).hasClass("qqNum")||$(this).hasClass("storephone")){
    		$(this).attr("success",true);
    	}else{
    		$(this).attr("success",false);
    	}
    });
    //验证用名
    $("#adminName").blur(function(){
      var andrminName=$("#adminName").val();
      var re=/^[0-9a-zA-Z]{6,20}$/;
      if(andrminName!=''){
       if(re.test(andrminName)){
           $.ajax({
               url:'${pageContext.request.contextPath}/shop/validtion.html',
               type:'post',
               data:{
                   param:'adminName',
                   value:andrminName
               },
               dataType:'json',
               success:function(data){
                if(data.response.status != '0' || data.response.status !="0" || data.response.status != 0){
                       $("#adminName_tips").find("img").attr("src",getContextPath()+"/resources/img/error.png");
                 	   $("#adminName_title").text("当前用户名已存在");
                }else{
             	       $("#adminName").parent().siblings(".success").show();
             	       $('#adminName_tips').css('visibility','hidden');
             	       $("#adminName").attr("success",true);
             	  	   
                }
               }
            });
       }else{
    	   $("#adminName_tips").find("img").attr("src",getContextPath()+"/resources/img/error.png");
    	   $("#adminName_title").text("输入格式不正确");
       }
      }else{
    	  $(this).val("您的账户名");
    	  $('#adminName_tips').css('visibility','hidden');
    	  $("")
      }
    }).focus(function(){
    	$("#adminName").attr("success",false);
    	$("#adminName").parent().siblings(".success").hide();
    	if($(this).val()=="您的账户名"){
    		$(this).val("");
    	}
    	$("#adminName_tips").find("img").attr("src",getContextPath()+"/resources/ad/icon.png");
    	$("#adminName_title").text("英文、数字的组合,6-20个字符");
    	$("#adminName_tips").css('visibility','visible')
    });
    //onfocus="javascript:if(this.value=='您的账户名') ;" 
    //验证商家
    $("#storename").blur(function(){
     if($("#storename").val()!=''){
     $.ajax({
          url:'${pageContext.request.contextPath}/shop/validtion.html',
          type:'post',
          data:{
              param:'storename',
              value:''+$("#storename").val()+''
          },
          dataType:'json',
          success:function(data){
               if(data.response.status != '0' || data.response.status !="0" || data.response.status != 0){
                   $("#storename_title").html("当前商家已注册了.").css("color","red");
                   msg2+="v";
	           }else{
	        	   $("#storename").attr("success",true);
	        	   $("#storename").parent().siblings(".success").show();
	        	   $('#storename_tips').css('visibility','hidden');
	           }
          }
       });
      }else{
    	  $(this).val("请填写公司名称");
    	  $('#storename_tips').css('visibility','hidden');
      }
    }).focus(function(){
    	$("#storename").attr("success",false);
    	$("#storename_title").text("填写公司信息，让资料更完善").css("color","#666");
    });
    //姓名校验
    $("#firstName").blur(function(){
    	var re=/^[a-zA-Z\u2E80-\u9FFF]{2,20}$/;
    	var firstName=$(this).val();
    	if(firstName!=''){
    		if(re.test(firstName)){
    			$("#firstName").attr("success",true);
    			$("#firstName").parent().siblings(".success").show();
    			$("#firstName_tips").css('visibility','hidden')
    		}else{
    			  $("#firstName_tips").find("img").attr("src",getContextPath()+"/resources/img/error.png");
    	    	  $("#firstName_title").text("只能包含汉字或英文");
    		}
    	}else{
    		$(this).val("请填写真实姓名");
    		$("#firstName_tips").css('visibility','hidden')
    	}
    	
    }).focus(function(){
    	$("#firstName").attr("success",false);
    	$("#firstName").parent().siblings(".success").hide();
    	if($(this).val()=="请填写真实姓名"){
    		$(this).val("");
    	}
    	$("#firstName_tips").find("img").attr("src",getContextPath()+"/resources/ad/icon.png");
    	$("#firstName_title").text("请填写真实姓名");
    	$("#firstName_tips").css('visibility','visible')
    });
    
    //手机号验证
    $(".storemobile").blur(function(){
    	var re=/^(13[0-9]|14[0-9]|15[0-9]|18[0-9])\d{8}$/i;
    	var storemobile=$(this).val();
    	if(storemobile!=''){
    		if(re.test(storemobile)){
		    	 $.ajax({
		          url:'${pageContext.request.contextPath}/shop/valitationUser.html',
		          type:'post',
		          data:{
		              index:1,
		              account:storemobile
		          },
		          dataType:'json',
		          success:function(data){
		          	if(data.response.status==0){//验证是否成功
			          	$(".storemobile").attr("success",true);
		    			$(".storemobile").parent().siblings(".success").show();
		    			$("#mobile_tips").css('visibility','hidden')
		          	}else{
		          			$(".storemobile").attr("success",false);
		          		    $("#mobile_tips").find("img").attr("src",getContextPath()+"/resources/img/error.png");
    	    	  			$("#mobile_title").text("该手机已存在,不能重复注册");
		          	}
		          }
		       });
    		}else{
    			  $(".storemobile").attr("success",false);
    			  $("#mobile_tips").find("img").attr("src",getContextPath()+"/resources/img/error.png");
    	    	  $("#mobile_title").text("请输入正确手机号码");
    		}
    	}else{
    		$(this).val("建议完善个人联络方式");
    		$("#mobile_tips").css('visibility','hidden')
    	}
    	
    }).focus(function(){
    	$(".storemobile").attr("success",false);
    	$(".storemobile").parent().siblings(".success").hide();
    	if($(this).val()=="建议完善个人联络方式"){
    		$(this).val("");
    	}
    	$("#mobile_tips").find("img").attr("src",getContextPath()+"/resources/ad/icon.png");
    	$("#mobile_title").text("建议完善个人联络方式");
    	$("#mobile_tips").css('visibility','visible')
    });
    
    //公司电话验证
    $(".storephone").blur(function(){
    	if($(this).val()==""){
    		$(".storephone").attr("success",true);
    	}
    	var storephone=$(this).val();
    	var re=/^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$/;
    	if(storephone!=''){
    		if(re.test(storephone)){
    			$(".storephone").attr("success",true);
        		$(".storephone").parent().siblings(".success").show();
        		$("#tel_tips").css('visibility','hidden')
    		}else{
    			  $("#tel_tips").find("img").attr("src",getContextPath()+"/resources/img/error.png");
    	    	  $("#tel_title").text("请输入正确座机号码");
    		}
    	}else{
    		$(this).val("建议完善公司联络方式");
    		$("#tel_tips").css('visibility','hidden')
    	}
    	
    }).focus(function(){
    	$(".storephone").attr("success",true);
    	$(".storephone").parent().siblings(".success").hide();
    	if($(this).val()=="建议完善公司联络方式"){
    		$(this).val("");
    	}
    	$("#tel_tips").find("img").attr("src",getContextPath()+"/resources/ad/icon.png");
    	$("#tel_title").text("建议完善公司联络方式");
    	$("#tel_tips").css('visibility','visible')
    }); 
    
    //邮箱验证
    $("#adminEmail").blur(function(){
    	var adminEmail=$(this).val();
    	var re=/^(\w)+(\.\w+)*@(\w)+((\.\w{2,3}){1,3})$/;
    	if(adminEmail!=''){
    		if(re.test(adminEmail)){
        		$.ajax({
		          url:'${pageContext.request.contextPath}/shop/valitationUser.html',
		          type:'post',
		          data:{
		              index:0,
		              account:adminEmail
		          },
		          dataType:'json',
		          success:function(data){
		          	if(data.response.status==0){//验证是否成功
			    			$("#adminEmail").attr("success",true);
			        		$("#adminEmail").parent().siblings(".success").show();
			        		$("#adminEmail_tips").css('visibility','hidden');
		          	}else{
		          			  $("#adminEmail").attr("success",false);
			    			  $("#adminEmail_tips").find("img").attr("src",getContextPath()+"/resources/img/error.png");
			    	    	  $("#adminEmail_title").text("该邮箱已存在,不能重复注册");
		          	}
		          }
		       });
        		
        		
    		}else{
    			  $("#adminEmail_tips").find("img").attr("src",getContextPath()+"/resources/img/error.png");
    	    	  $("#adminEmail_title").text("请输入正确邮箱地址");
    		}
    	}else{
    		$(this).val("建议使用常用邮箱");
    		$("#adminEmail_tips").css('visibility','hidden')
    	}
    	
    }).focus(function(){
    	$("#adminEmail").attr("success",false);
    	$("#adminEmail").parent().siblings(".success").hide();
    	if($(this).val()=="建议使用常用邮箱"){
    		$(this).val("");
    	}
    	$("#adminEmail_tips").find("img").attr("src",getContextPath()+"/resources/ad/icon.png");
    	$("#adminEmail_title").text("验证完成后,可以通过邮箱找回密码");
    	$("#adminEmail_tips").css('visibility','visible')
    }); 
    
    //QQ校验
    $(".qqNum").blur(function(){
    	$(".qqNum").attr("success",true);
    	var qqNum=$(this).val();
    	if(qqNum!=''){
        		$(".qqNum").parent().siblings(".success").show();
        		$("#qq_tips").css('visibility','hidden')
    		
    	}else{
    		$(this).val("建议使用常用QQ");
    		$("#qq_tips").css('visibility','hidden')
    	}
    	
    }).focus(function(){
    	$(".qqNum").attr("success",true);
    	$(".qqNum").parent().siblings(".success").hide();
    	if($(this).val()=="建议使用常用QQ"){
    		$(this).val("");
    	}
    	$("#qq_tips").find("img").attr("src",getContextPath()+"/resources/ad/icon.png");
    	$("#qq_title").text("建议完善公司客服QQ");
    	$("#qq_tips").css('visibility','visible')
    }); 
    
          var pwdReg =/^(?=[0-9]+[a-z]+)|(?=[a-z]+[0-9]+)|(?=[0-9]+[A-Z]+)|(?=[A-Z]+[0-9]+)|(?=[a-z]+[A-Z]+)|(?=[A-Z]+[a-z]+).{6,20}$/;
          var numEnReg =/^[a-zA-Z0-9]{6,20}$/;
     //密码
    $("#adminPassword").blur(function(){
    	var adminPassword=$(this).val();
    	if(adminPassword!=''){
    		if(pwdReg.test(adminPassword)&&numEnReg.test(adminPassword)){
    			$("#adminPassword").attr("success",true);
        		$("#adminPassword").parent().siblings(".success").show();
        		$("#adminPassword_tips").css('visibility','hidden')
     		}else{
     			 $("#adminPassword_tips").find("img").attr("src",getContextPath()+"/resources/img/error.png");
   	    	  	$("#adminPassword_title").text("请输入正确格式密码");
     		}
    	}else{
    		$(this).val("请输入密码");
    		$(this).attr("type","input");
    		$("#adminPassword_tips").css('visibility','hidden')
    	}
    	
    }).focus(function(){
    	$("#adminPassword").attr("success",false);
    	$("#adminPassword").parent().siblings(".success").hide();
    	if($(this).val()=="请输入密码"){
    		$(this).val("");
    	}
    	$(this).attr("type","password");
    	$("#adminPassword_tips").find("img").attr("src",getContextPath()+"/resources/ad/icon.png");
    	$("#adminPassword_tips").css('visibility','visible')
    	$("#adminPassword_title").text("包含英文字母、大写英文字母、数字任意两种,6-20位字符");
    }); 
     
    //确认密码
    $("#passwordAgain").blur(function(){
    	var passwordAgain=$(this).val();
    	if(passwordAgain!=''){
    		if(pwdReg.test(passwordAgain)&&numEnReg.test(passwordAgain)){
    			if(passwordAgain==$("#adminPassword").val()){
    				$("#passwordAgain").attr("success",true);
            		$("#passwordAgain").parent().siblings(".success").show();
            		$("#passwordAgain_tips").css('visibility','hidden')
    			}else{
    				 $("#passwordAgain_tips").find("img").attr("src",getContextPath()+"/resources/img/error.png");
    	   	    	 $("#passwordAgain_title").text("两次密码不一致");
    			}
     		}else{
     			 $("#passwordAgain_tips").find("img").attr("src",getContextPath()+"/resources/img/error.png");
   	    	  	$("#passwordAgain_title").text("请输入正确格式密码");
     		}
    	}else{
    		$(this).val("再次输入密码");
    		$(this).attr("type","input");
    		$("#passwordAgain_tips").css('visibility','hidden')
    	}
    	
    }).focus(function(){
    	$("#passwordAgain").parent().siblings(".success").hide();
    	$("#passwordAgain").attr("success",false);
    	$("#passwordAgain").parent().siblings(".success").hide();
    	if($(this).val()=="再次输入密码"){
    		$(this).val("");
    	}
    	$(this).attr("type","password");
    	$("#passwordAgain_tips").find("img").attr("src",getContextPath()+"/resources/ad/icon.png");
    	$("#passwordAgain_tips").css('visibility','visible')
    	$("#passwordAgain_title").text("包含英文字母、大写英文字母、数字任意两种,6-20位字符");
    }); 
    //验证码校验
    $("#captchaResponseField").blur(function(){
    	var imgCode=$(this).val();
    	if(imgCode!=""){
    		$.post("${pageContext.request.contextPath}/shop/valitationByImgCode.html",{imgCode:imgCode},function(data){
    			if(data.response.status==0){//验证是否成功
    				$("#captchaResponseField").attr("success",true);
    			  $("#captchaResponseField").parent().siblings(".success").show();
    			  $("#captchaResponseField_tips").css('visibility','hidden');
    			}else{
    			  $("#captchaResponseField").parent().siblings(".success").hide();
    				 $("#captchaResponseField_tips").find("img").attr("src",getContextPath()+"/resources/img/error.png");
    	   	    	 $("#captchaResponseField_title").text("验证码不正确");
    	   	    	
    			}
    		},"json");
    	}else{
    		$("#captchaResponseField_tips").css('visibility','hidden');
    	}
    }).focus(function(){
    	$("#captchaResponseField").attr("success",false);
    	$("#captchaResponseField_tips").find("img").attr("src",getContextPath()+"/resources/ad/icon.png");
    	$("#captchaResponseField_tips").css('visibility','visible');
    	$("#captchaResponseField_title").text("请输入右侧验证码");
    });
    
    var isSendSMSCOde=false;
    //发送手机验证码
    $("#btnSendCode").click(function(){
    	//获取手机号
    	var phone=$(".storemobile").val();
    	var success=$(".storemobile").attr("success");
    	if(phone!=""&&success){
    		$.post("${pageContext.request.contextPath}/shop/sendSMSCode.html",{phone:phone},function(data){
    			if(data.response.status==0){//验证码发送成功
    				$("#btnSendCode").attr("disabled","disabled");
    				isSendSMSCOde=true;
    				setTimeout(function(){
    				$("#btnSendCode").removeAttr("disabled");
    				},1000*59);
    				$(".alertMsg").text("验证码发送成功");
    				$("#alertDialog").modal({backdrop: 'static'});
    			}else{
    				$(".alertMsg").text("验证码发送失败");
    				$("#alertDialog").modal({backdrop: 'static'});
    			}
    		},"json");
    	}else{
			$(".alertMsg").text("请填写手机号");
			$("#alertDialog").modal({backdrop: 'static'});
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
    	if(smsCode==""){//验证码是否为空
    		$("#phoneValation_tips").find("img").attr("src",getContextPath()+"/resources/img/error.png");
    		$("#phoneValation_title").text("请填写手机验证码");
    		return false;
    	}
    	//判断是否发送验证码 
    	if(isSendSMSCOde==false){
      		$("#phoneValation_tips").find("img").attr("src",getContextPath()+"/resources/img/error.png");
    		$("#phoneValation_title").text("未发送手机验证码");
    		return false;
    	}
    	//发送校验请求
    	$.post("${pageContext.request.contextPath}/shop/valitationBySMSCode.html",{smsCode:smsCode},function(data){
    		if(data.response.status==0){//验证是否成功
    		  $("#phoneValation").attr("success",true);
  			  $("#phoneValation").parent().siblings(".success").show();
  			  $("#phoneValation_tips").css('visibility','hidden');
    		}else{
    		  $("#phoneValation").attr("success",false);
    		  $("#phoneValation_tips").find("img").attr("src",getContextPath()+"/resources/img/error.png");
        	  $("#phoneValation_title").text("验证码不正确");
    		}
    	},"json"
   )});
     
	$("#submitRegistration").click(function(){
		
		return isSubmitForm();
	});
    //是否可以点击注册
    function isSubmitForm(){
    	var isSubmit=true;
    	$(".valueInput").each(function(){
    		if($(this).attr("success")=="false"){
    			isSubmit=false;
    		}
    	});
		//判断同意按钮
		if(!$("#userProtocolCheckbox").is(":checked")){
			isSubmit=false;
		}
    	return isSubmit;
    }
      //悬浮商家和用户名是否已存在
      //内容重置
     $("#CustomerReset").click(function(){
        $("#adminName").css("backgroundColor","rgb(255, 255, 204)").val("");
        $("#storename").css("backgroundColor","rgb(255, 255, 204)").val("");
        $("#adminEmail").css("backgroundColor","rgb(255, 255, 204)").val("");
        $("#adminPassword").css("backgroundColor","rgb(255, 255, 204)").val("");
        $("#passwordAgain").css("backgroundColor","rgb(255, 255, 204)").val("");
        $("#captchaResponseField").css("backgroundColor","rgb(255, 255, 204)").val("");
        $("#firstName").css("backgroundColor","rgb(255, 255, 204)").val("");
     });

	$("#btnSendCode").css("background-color","#5bc0de");
});
 
function isCustomerFormValid(formId) {
	var $inputs = $(formId).find(':input');
	var errorMessage = null;
	$inputs.each(function() {
		var flag = false; //判断输入框里的内容
		var tempid = $(this).attr("id");
		//if($(this).hasClass('required')) {
			var fieldValid = isFieldValid($(this));
			if(!fieldValid) {
				if(errorMessage==null) {
					if($(this).attr('title')) {
						errorMessage = $(this).attr('title');
						$("#"+tempid+"_title").html(""+errorMessage+"").css("color","gray");
					}
				}
			}
		//}
		
		//user name
		if($(this).hasClass('userName')) {	
			if($(this).val().length<1) {
				if(errorMessage==null) {
					errorMessage = getInvalidUserNameMessage();
					$("#"+tempid+"_title").html(""+errorMessage+"").css("color","red");
				}
			}
			if ($(this).val()=="您的账户名") {
				flag = true;
				$("#"+tempid+"_title").html("支持中文、英文、数字的组合,6-20字符").css("color","gray");
			}
		}
		
		//stroeName
		if($(this).hasClass('storename')) {	
			if($(this).val().length<1) {
				if(errorMessage==null) {
					errorMessage = getInvalidUserNameMessage();
					$("#"+tempid+"_title").html(""+errorMessage+"").css("color","red");
				}
			}
			if ($(this).val()=="请填写公司名称") {
				flag = true;
				$("#"+tempid+"_title").html("填写公司信息，让资料更完善").css("color","gray");
			}
		}
		
		
		//if has class email
		if($(this).hasClass('email')) {	
			var emailValid =validateEmail($(this).val());
			if(!emailValid) {
				if(errorMessage==null) {
					errorMessage = getInvalidEmailMessage();
					$("#"+tempid+"_title").html(""+errorMessage+"").css("color","red");
				}
			}
		}
		//if has class storephone
		if($(this).hasClass('storephone')) {	
			var isPhone = /^0\d{2,3}-?\d{7,8}$/;
			if(!isPhone.test($(this).val())){
				//alert("请输入正确格式的电话号码");
				$('#submitRegistration').addClass('btn-disabled');
				$('#submitRegistration').prop('disabled', true);
				$("#tel_title").html("请输入正确格式的电话号码").css("color","red");
			}else{
				$('#submitRegistration').removeClass('btn-disabled');
				$('#submitRegistration').prop('disabled', false);
				$("#tel_title").html("电话号码格式正确").css("color","green");
			}
			/* var emailValid =validphone($(this).val());
			if(!emailValid) {
				if(errorMessage==null) {
					errorMessage = getInvalidEmailMessage();
					$("#"+tempid+"_title").html(""+errorMessage+"").css("color","red");
				}
			} */
		}
		//if has first name
		if($(this).hasClass('firstName')) {	
			if($(this).val().length<1) {
				if(errorMessage==null) {
					errorMessage = getInvalidUserNameMessage();
					$("#"+tempid+"_title").html(""+errorMessage+"").css("color","red");
				}
			}
			if ($(this).val()=="请填写真实姓名") {
				flag = true;
				$("#"+tempid+"_title").html("支持中文、英文、数字的组合").css("color","gray");
			}
		}
		//password rules
		if($(this).hasClass('password')) {	
			if(checkStrong($(this).val())<2) {
				if(errorMessage==null) {
					errorMessage = getWeekPasswordMessage();
					$("#"+tempid+"_title").html(""+errorMessage+"").css("color","red");
				}
			}
		}
		//repeat password
		if($(this).hasClass('checkPassword')) {	
				var pass = $('.password').val();
				if($(this).val().length<6 || ($(this).val()!=pass)) {
					if(errorMessage==null) {
						errorMessage = getInvalidCheckPasswordMessage();
						$("#"+tempid+"_title").html(""+errorMessage+"").css("color","red");
					}
				}
		}
		if(errorMessage == null){
		  if(msg1!=""){
		      $("#adminName_title").html("当前用户名已存在.").css("color","red");
		  }else if(msg2!=""){
		      $("#storename_title").html("当前商家已注册了.").css("color","red");
		  }
		  if (!flag) {
			  $("#"+tempid+"_title").html(""+$("#"+tempid+"_title1").html()+"").css("color","green");
		  }
		  
		}
		
	});
	
	return errorMessage;
 }
 
 
 function isFieldValid(field) {
	if(field.is(":hidden")) {
		return true;
	}
	var value = field.val();
	if(!emptyString(value)) {
		field.css('background-color', '#FFF');
		return true;
	} else {
		field.css('background-color', '#FFF');
		return false;
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

/**
 * 提交注册前的校验控制
 */
function checkInfo() {
  
	//是否确认同意注册协议
	if(!$("#userProtocolCheckbox").is(":checked")) {
		$("#agreeMsg").show();
		return false;
	} else {
		$("#agreeMsg").hide();
	}
}

function isFieldValid(field) {
	if(field.is(":hidden")) {
		return true;
	}
	var value = field.val();
	if(!emptyString(value)) {
		field.css('background-color', '#FFF');
		return true;
	} else {
		field.css('background-color', '#FFF');
		return false;
	} 
}
</script>
<style type="text/css">
	.head-navbar-left{
	padding-left:12%!important;
	}
	.fam{
	/* font-family: '微软雅黑'; */
	font-size: 18px;
	font-weight:bolder ;
	padding-left:36px;
}
label{
	font-size:14px;
	font-weight:bolder  ! important;
	align : left;
	width:160px;
}
.pic{
	width:32px;
	height:32px;
}
.reg{
	font-size:16px;
	color:#fafafa;
	margin-left:-30px;
	border:none;
	width:344px;
	height:40px;
	margin-left:2px;
	background:url(<c:url value="/resources/ad/reg.png"/>) no-repeat;
}
.star{
	width:8px;
	height:8px;
	margin-left:-340px;
	margin-top:8px;
}
/*商家注册*/
input{
 width: 340px;
 padding:8px 0px 8px 10px;
 font-size: 14px;
 border: 1px solid #d9d9d9;
}
.form-group{
margin-bottom: 23px;
}
.message{
 margin-top: 20px!important;
 margin-left: 142px!important;
 font-size: 12px;
 visibility: hidden;
}
.message img{
margin-right: 13px;
}
.success{
  display: none;
}
</style>
<c:set var="register_url" value="${pageContext.request.contextPath}/admin/registerUser.html"/>
<br/>
<br/>
<div style="width:55%;padding-bottom:30px;padding-left:50px;float:left;padding-top:42px;">
<!-- <div class="row" style="border: 1px solid #d9d9d9;margin:10px 25px;padding-top:10px;"> -->
<!-- <div id="registrationError"  class="alert alert-warning common-row text-center" style=""></div>
 -->
 <div class="head-navbar-left head-navbar-right row" >
   <form:form method="post" action="${register_url}" id="adminRegisterForm" class="form-horizontal" commandName="user">
	<form:errors path="*" cssClass="alert-danger alert-danger form-group text-center" element="div" />
	 <div id="store.success" class="alert alert-success"
			style="visibility:hidden; <c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise></c:otherwise></c:choose>">
			<s:message code="message.success" text="Request successfull" />
	</div>
	<form:input type="hidden" path="defaultLanguage.id" value="${user.defaultLanguage.id}" />
	
	<!-- 主体内容 -->
	<div class="form-group" style="margin-left:-30px;width:800px;">
	    <label for="storename" class="col-sm-2 control-label">商家</label>
	    <img class="star" src="<c:url value="/resources/ad/star.png"/>">
	    <div class="col-sm-5">
	      <input type="text" class="storename valueInput"  value="请填写公司名称" 
	        onfocus="javascript:if(this.value=='请填写公司名称')this.value='';$('#storename_tips').css('visibility','visible');$('#storename').parent().siblings('.success').hide();" 
	      id="storename" name="storename" title="${msgStoreName}" maxlength="100"/>
		   <form:errors path="merchantStore" cssClass="error" /></span>
	    </div><br/><br/>
	    <img class="success" src="<c:url value="/resources/img/success.png"/>" width="20" height="20" style="margin-left:37px;margin-top:-25px;"/>
	    <div id="storename_tips" class="col-sm-5 message" style="margin-top:10px;margin-left:130px;">
	      <img src="<c:url value="/resources/ad/icon.png"/>">&nbsp;&nbsp;
	      <span id="storename_title">填写公司信息，让资料更完善</span>
	    </div>
	 </div>
	
	<div class="form-group" style="margin-left:-30px;width:800px;">
	    <label for="adminName" class="col-sm-2 control-label">用户名</label>
	    <img class="star" src="<c:url value="/resources/ad/star.png"/>">
	    <div class="col-sm-5">
	      <form:input cssClass="userName valueInput"   
	      value="您的账户名" 
	       type="text" path="adminName" id="adminName" title="${msgUserName}" maxlength="80"/>
		  <span><form:errors path="adminName" cssClass="error" /></span>
	    </div><br/><br/>
	    <img class="success" src="<c:url value="/resources/img/success.png"/>" width="20" height="20" style="margin-left:37px;margin-top:-25px;"/>
	    <div id="adminName_tips" class="col-sm-5 message" style="margin-top:10px;margin-left:130px;">
	      <img src="" width="15" height="15">&nbsp;&nbsp;
	      <span id="adminName_title"></span>
	    </div>
	 </div>
	 
	 <div class="form-group" style="margin-left:-30px;width:800px;">
	    <label for="firstName" class="col-sm-2 control-label">姓名</label>
	    <img class="star" src="<c:url value="/resources/ad/star.png"/>">
	    <div class="col-sm-5" >
	      <form:input  path="firstName" cssClass="firstName valueInput"  name="firstName" 
	      	value="请填写真实姓名"  
	        id="firstName" title="${firstName}"  cssStyle="backgroundColor:#FFFFFF;" maxlength="100"/>
		  <span><form:errors path="firstName" cssClass="error" /></span>
	    </div><br/><br/>
	    <img class="success" src="<c:url value="/resources/img/success.png"/>" width="20" height="20" style="margin-left:37px;margin-top:-25px;"/>
	    <div id="firstName_tips" class="col-sm-5 message" style="margin-top:10px;margin-left:130px;">
	      <img src="" width="15" height="15">&nbsp;&nbsp;
	      <span id="firstName_title"></span>
	    </div>
	 </div>
	 
	  <div class="form-group" style="margin-left:-30px;width:800px;">
	    <label for="merchantStore.storemobile" class="col-sm-2 control-label">手机号</label>
	    <img class="star" src="<c:url value="/resources/ad/star.png"/>">
	    <div class="col-sm-5">
	      <form:input  path="merchantStore.storemobile" cssClass="storemobile valueInput"  
	      	value="建议完善个人联络方式"
	      	name="merchantStore.storemobile"  id="merchantStore.storemobile" title="${merchantStore.storemobile}"  cssStyle="backgroundColor:#FFFFFF" maxlength="80"/>
		  <span><form:errors path="merchantStore.storemobile" cssClass="error" /></span>
	    </div><br/><br/>
	    <img class="success" src="<c:url value="/resources/img/success.png"/>" width="20" height="20" style="margin-left:37px;margin-top:-25px;"/>
	    <div id="mobile_tips" class="col-sm-5 message" style="margin-top:10px;margin-left:130px;">
	      <img width="15" height="15" src="">&nbsp;&nbsp;
	      <span id="mobile_title"></span>
	    </div>
	 </div>
	 
	  <!--手机验证码-->
	  <div class="form-group" style="margin-left:-30px;width:800px; margin-bottom: 20px;">
	    <label for="checkPhonecode" class="col-sm-2 control-label">手机验证码</label>
	    <img class="star" src="<c:url value="/resources/ad/star.png"/>">
	    <div class="col-sm-3">
	     <input type="text" Class="company valueInput" style="width:208px;padding-left:10px;"  name="checkPhonecode" id="phoneValation" title="${validatecodeName}"/>
	     <div id="messageFail" style="color:red"></div>
	    </div>
	    <div class="col-sm-2">
	      <input id="btnSendCode" type="button" class="btn btn-info btn-block" style="background-color:#5bc0de !important;width:132px;height:40px;margin-left: 8px;"  value="获取手机验证码" />
	    </div>
	    <img class="success" src="<c:url value="/resources/img/success.png"/>" width="20" height="20" style="margin-left:365px;margin-top:10px;"/>
	    <div id="phoneValation_tips" class="col-sm-5 message" style="margin-left:130px;">
	      <img src="" height="15" width="15">&nbsp;&nbsp;
	      <span id="phoneValation_title">建议完善公司联络方式</span>
	    </div>
	  </div>
	  
	  <div class="form-group" style="margin-left:-30px;width:800px;">
	    <label for="merchantStore.storephone" class="col-sm-2 control-label">电话号码</label>
	    <div class="col-sm-5">
	      <form:input  path="merchantStore.storephone" cssClass="storephone valueInput"  
	      	value="建议完善公司联络方式" 
	      	name="merchantStore.storephone"  id="merchantStore.storephone" title="${merchantStore.storephone}"  cssStyle="backgroundColor:#FFFFFF" maxlength="80"/>
		  <span><form:errors path="merchantStore.storephone" cssClass="error" /></span>
	    </div><br/><br/>
	    <img class="success" src="<c:url value="/resources/img/success.png"/>" width="20" height="20" style="margin-left:37px;margin-top:-25px;"/>
	    <div id="tel_tips" class="col-sm-5 message" style="margin-left:130px;">
	      <img src="" height="15" width="15">&nbsp;&nbsp;
	      <span id="tel_title">建议完善公司联络方式</span>
	    </div>
	 </div>
	 
	 <div class="form-group" style="margin-left:-30px;width:800px;">
	    <label for="adminEmail" class="col-sm-2 control-label padding-30">邮箱</label>
	    <img class="star" src="<c:url value="/resources/ad/star.png"/>">
	    <div class="col-sm-5">
	      <form:input  path="adminEmail" cssClass="email valueInput"  
	      	value="建议使用常用邮箱" 
	      	name="adminEmail"  id="adminEmail" title="${adminEmail}"  cssStyle="backgroundColor:#FFFFFF;" maxlength="80"/>
		  <span><form:errors path="adminEmail" cssClass="error" /></span>
	    </div><br/><br/>
	    <img class="success" src="<c:url value="/resources/img/success.png"/>" width="20" height="20" style="margin-left:37px;margin-top:-25px;"/>
	    <div id="adminEmail_tips" class="col-sm-5 message" style="margin-top:10px;margin-left:130px;">
	      <img height="15" width="15">&nbsp;&nbsp;
          <span id="adminEmail_title">完成验证后，可以通过邮箱找回密码</span>
	    </div>
	 </div>
	 
	 <div class="form-group" style="margin-left:-30px;width:800px;">
	    <label for="merchantStore.qqNum" class="col-sm-2 control-label padding-30">QQ</label>
	    <%-- <img class="star" src="<c:url value="/resources/ad/star.png"/>"> --%>
	    <div class="col-sm-5">
	      <form:input  path="merchantStore.qqNum" cssClass="qqNum valueInput"  
	      	value="建议使用常用QQ" 
	      	name="merchantStore.qqNum"  id="merchantStore.qqNum" title="${merchantStore.qqNum}"  cssStyle="backgroundColor:#FFFFFF" maxlength="80"/>
		  <span><form:errors path="merchantStore.qqNum" cssClass="error" /></span>
	    </div><br/><br/>
	    <img class="success" src="<c:url value="/resources/img/success.png"/>" width="20" height="20" style="margin-left:37px;margin-top:-25px;"/>
	    <div id="qq_tips" class="col-sm-5 message" style="margin-top:10px;margin-left:130px;">
	      <img src="" height="15" width="15">&nbsp;&nbsp;
	      <span id="qq_title"></span>
	    </div>
	 </div>
	 
	
	 
	 <div class="form-group" style="margin-left:-30px;width:800px;">
	    <label for="adminPassword" class="col-sm-2 control-label padding-30">密码</label>
	    <img class="star" src="<c:url value="/resources/ad/star.png"/>">
	    <div class="col-sm-5">
	      <form:input  path="adminPassword" cssClass="password valueInput"  value="请输入密码"
	        name="adminPassword"   id="adminPassword" title="${adminPassword}"  cssStyle="backgroundColor:#FFFFFF" maxlength="50"/>
		  <span><form:errors path="adminPassword" cssClass="error" /></span>
	    </div><br/><br/>
	    <img class="success" src="<c:url value="/resources/img/success.png"/>" width="20" height="20" style="margin-left:37px;margin-top:-25px;"/>
	    <div id="adminPassword_tips" class="col-sm-5 message"  style="margin-top:10px;margin-left:130px;">
	      <img src="" width="15" width="15">&nbsp;&nbsp;
	      <span id="adminPassword_title"></span>
	    </div>
	 </div>
	 
	 <div class="form-group" style="margin-left:-30px;width:800px;">
	    <label for="passwordAgain" class="col-sm-2 control-label padding-30">确认密码</label>
	    <img class="star" src="<c:url value="/resources/ad/star.png"/>">
	    <div class="col-sm-5 ">
	      <input type="input" Class=" checkPassword  valueInput" id="passwordAgain" value="再次输入密码" 
	      	 name="passwordAgain" maxlength="50" />
	    </div><br/><br/>
	    <img class="success" src="<c:url value="/resources/img/success.png"/>" width="20" height="20" style="margin-left:37px;margin-top:-25px;"/>
	    <div id="passwordAgain_tips" class="col-sm-5 message"  style="margin-top:10px;margin-left:130px;">
	      <img src="" height="15" width="15">&nbsp;&nbsp;
	      <span id="passwordAgain_title"></span>
	    </div>
	 </div>
	 
	 <div class="form-group" style="margin-left:-30px;width:800px;margin-bottom: 0px!important;">
	    <label for="captchaResponseField" class="col-sm-2 control-label">验证码</label>
	    <img style="width:8px;height:8px;margin-left:-398px;margin-top:8px;" src="<c:url value="/resources/ad/star.png"/>">
	    <div class="col-sm-4">
	     <input type="text" Class="company valueInput" id="captchaResponseField" name="captchaResponseField" 
	     title="${validatecodeName}" style="width:220px;height:40px;padding-left:10px;"/>
	    </div>
	    <div class="col-sm-6 pull-center" style="width:124px !important;">
	    	<a style="cursor: pointer;" onclick="changeValidateImg()">
	    		<img id="imgObj" src="<c:url value="/shop/store/code.html"/>" style="height:40px;width:121px;margin-left:-46px;border: 1px solid #d9d9d9"/>
	    			<div style="margin-left: -16px;">
	    			    <br/>
	    				<s:message code="label.generic.change.validatecode" text="Change"/>
	    			</div>
	    		</a>
	    </div>
	    <img class="success" src="<c:url value="/resources/img/success.png"/>" width="20" height="20" style="margin-left:365px;margin-top:10px;"/>
	    <div id="captchaResponseField_tips" class="col-sm-5"  style="margin-bottom:20px;margin-left:150px; visibility: hidden;">
	      <img src="" height="15" width="15">&nbsp;&nbsp;
	      <span id="captchaResponseField_title"></span>
	    </div>
	 </div>
	 	
	 <div class="form-group" style="margin-left:-30px;width:800px;">
	 	<div class="col-sm-5" style="padding-left:145px;width:600px;">
	     <div style="width:400px !important;">
	     <input type="checkbox" style="width: 20px;height: 20px" id="userProtocolCheckbox" checked="checked"/></div>
	     <div style="margin-top:10px;margin-left: 20px;width:400px ;">&nbsp;&nbsp;&nbsp;阅读和同意
	     	<a href="protocol.html">《百图注册协议》</a>
	     </div>
       </div>
	    <div class="col-sm-2 col-sm-offset-2" style="padding-top: 20px;">
	      <input type="submit" name="submitRegistration" class="reg"  id="submitRegistration" value="立即注册">
	    </div> 
	    <!-- <div class="col-sm-2">
	      <input class="btn btn-info btn-block" type="button" value="重置" id="CustomerReset">
	    </div> -->
	    
      </div>
      
   </form:form>
  </div>
  </div>
<div class="modal fade" id="alertDialog" tabindex="-1" role="dialog" aria-labelledby="myAlertModalLabel">
			  <div class="modal-dialog modal-sm" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <h4 class="modal-title" id="myAlertModalLabel">提示</h4>
			      </div>
			      <div class="modal-body">
			      	   <span class="alertMsg"></span>
			      </div>
			      <div class="modal-footer">
			        <button type="button"  class="btn btn-primary" data-dismiss="modal">确认</button>
			      </div>
			    </div>
			  </div>
</div>
<div style="float:left;width:35%;padding-top:42px;">
	<img alt="二维码" style="width:320px;height:528px;" src="<c:url value="/resources/ad/erweima.png"/>">
	<p>
	<a  href="<c:url value="customLogon.html"/>" class="fam">已有账号，请登录</a>
	</p>
</div>
</div>
<div style="clear:both"></div>
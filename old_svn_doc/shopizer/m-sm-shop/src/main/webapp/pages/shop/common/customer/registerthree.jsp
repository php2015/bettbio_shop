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
<script src="<c:url value="/resources/js/pwstrength.js" />"></script>
<script type="text/javascript"> 
/**
 * 提交注册前的校验控制
 */
 $(function(){
	//是否校验成功
    var pwdReg =/^(?=[0-9]+[a-z]+)|(?=[a-z]+[0-9]+)|(?=[0-9]+[A-Z]+)|(?=[A-Z]+[0-9]+)|(?=[a-z]+[A-Z]+)|(?=[A-Z]+[a-z]+).{6,20}$/;
    var numEnReg =/^[a-zA-Z0-9]{6,20}$/;
    $("#newPassword").focus();
    $("#newPassword").blur(function(){
    	var adminPassword=$(this).val();
    	if(adminPassword!=''){
    		if(pwdReg.test(adminPassword)&&numEnReg.test(adminPassword)){
    		      $("#green_info").css("visibility","visible");
    		      $("#error_password").css("visibility","visible");
    		      $(this).attr("success","true");
     		}else{
     	       $("#error_password").html('密码格式输入错误!').css({"color":"red","padding":"8px 35px","font-size":"14px"});
    	       $("#green_info").css("visibility","hidden");
    	       $(this).attr("success","false");
    	       return false;
     		}
    	}else{
            $("#error_password").html('密码不能为空!').css({"color":"red","padding":"8px 35px","font-size":"14px"});
            $("#green_info").css("visibility","hidden");
            $(this).attr("success","false");
            return false;	
    	}
    }).focus(function(){
    	 $("#green_info").css("visibility","hidden");
    	  $(this).attr("success","false");
    });
    //查看密码
    var click_count=0;
    $("#img_url").click(function(){
      if($("#newPassword").attr("type")=="text"){
    	  $("#newPassword").attr("type","password");
      }else{
    	  $("#newPassword").attr("type","text");
      }
    
    });
    $("#next_submit").click(function(){
   	 if($("#newPassword").attr("success")=="true"){
   		 return true;
   	 }
   	 return false;
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
<c:set var="register_url" value="${pageContext.request.contextPath}/shop/customer/rthree.html"/>
	<div style="background: #eeeeee;height:100%">
		<div class="nav-ezybio container-fluid text-center">
			<div class="row" style="padding:11px 10px;">
				<div class="pull-left" style="width:30%;text-align:left;"><a href="#" onclick="window.history.go(-1)"><img src="<c:url value="/resources/img/left.png"/>" height="28"/></a></div>
				<div class="pull-left" style="width:40%;font-size:20px;color:#ffffff;">个人注册</div>
				<div class="pull-left" style="width:30%;text-align:right;line-height: 30px;"><a href="<c:url value="/shop/customer/storeRegistration.html"/>" ><span style="color:#ffffff;">切换商家注册</span></a></div>
			</div>
		</div>
		<div style="background:#ffffff;padding-top:24px; height:72px;">
			<div style="padding-left:10px;" id="yd">
				<span class="padding-img" style="font-size:16px;">1.输入手机号</span>
				<span ><img height="20" src="<c:url value="/resources/img/right.png"/>" /></span>
				<span style="font-size:16px;color:#5a8de2;font-weight:bold" style="font-size:16px;">2.设置密码</span>
				<span  ><img height="20" src="<c:url value="/resources/img/right.png"/>" /></span>
				<span class="padding-img" style="font-size:16px;">3.个人资料</span>
				<span class="padding-img"><img height="20" src="<c:url value="/resources/img/right.png"/>" /></span>
			</div>
					
		</div>
		<form:form method="post" action="${register_url}" id="registrationForm" class="form-horizontal" commandName="customer" >
			<div style="margin-top:20px;background:#ffffff;padding-top:20px;padding-bottom:20px;">
				<span class="padding-register" style="font-size:16px;">请输入密码：</span>
				<form:input type="password" success="false" path="password" name="password"  id="newPassword" style="border: 0px;height:30px;font-size:16px;width:25%!important"/>
				<div id="green_info" style="float: right;padding-right:20px;padding-top:5px;visibility:hidden;"><img src="<c:url value="/resources/img/zq.png"/>" width="24px" height="24px"/></div>
				<div id="img_url" style="float:right;padding-top:5px;padding-right: 10px;"><img width="24px" height="24px" src="<c:url value="/resources/img/lookpassword.png"/>"/></div>
			</div>
				<div id="error_password" style="height: 8px;padding-left:35px;color: red;padding-top: 10px;font-size: 14px;">
				</div>
			<div style="margin-top:10px;">
				<div class="text-center" style="padding:20px;">
				<button type="submit" id="next_submit"  style="background:#5a8de2;" class="btn btn-lg btn-block"> <span style="color:#ffffff">提交密码</span></button></div>
			</div>
		</form:form>
		
	</div>
	<!--close .container "main-content" -->
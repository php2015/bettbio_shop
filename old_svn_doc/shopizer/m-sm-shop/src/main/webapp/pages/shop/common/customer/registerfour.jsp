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
	 	 var temp=/^(\w)+(\.\w+)*@(\w)+((\.\w{2,3}){1,3})$/;
		 $("#emailAddress").blur(function(){
		     if($(this).val() == null ||$(this).val()==''){
		        $("#emailAddress_tips").text("邮箱地址不能为空");
			    return false;
		     }else if(temp.test($(this).val())){
		        $("#emailAddress_tips").text("邮箱地址格式输出有误!");
			    return false;
		     }
		    	$.post("${pageContext.request.contextPath}/shop/customer/valitationEmailOrPhone.html",{mail:$("#emailAddress").val(),phone:""},function(data){
		    		if(data.response.status==0){//邮箱校验成功
		    			$("#emailAddress_tips").text("");
		    			$("#emailAddress").attr("success","true");
		    			changeBtnStatus();
		    		}else{
		    			 $("#emailAddress_tips").text("邮箱地址已存在");
		    		}
		    	},"json");
			    
		 }).focus(function(){
			 $(this).attr("success","false");
		 });
		 //用户名
		 $("#userName").blur(function(){
			 if($(this).val()!=''){
				 $(this).attr("success","true");
				 $("#userName_tips").text("");
			 }else{
				 $("#userName_tips").text("用户名不能为空");
			 }
		 }).focus(function(){
			 $(this).attr("success","false");
		 });
		 //课程组 部门
		 $("#project").blur(function(){
			 if($(this).val()!=''){
				 $(this).attr("success","true");
				 $("#project_tips").text("");
			 }else{
				 $("#project_tips").text("课题组/部门不能为空");
			 }
		 }).focus(function(){
			 $(this).attr("success","false");
		 });
		 //学校 公司
		 $("#company").blur(function(){
			 if($(this).val()!=''){
				 $(this).attr("success","true");
				 $("#company_tips").text("");
			 }else{
				 $("#company_tips").text("学校/公司不能为空");
			 }
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
	    $.each($(".sminput"),function(index,obj){
	    	if($(this).attr("success")=="false"){
	    		returnValue=false;
	    	}
	    });
		return returnValue;
 }
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
<c:set var="register_url" value="${pageContext.request.contextPath}/shop/customer/register.html"/>
	<div style="background: #eeeeee;height:100%">
		<div class="nav-ezybio container-fluid text-center">
			<div class="row" style="padding:11px 10px;">
				<div class="pull-left" style="width:30%;text-align:left;"><a href="#" onclick="javascript:window.history.go(-1)"><img src="<c:url value="/resources/img/left.png"/>" height="28"/></a></div>
				<div class="pull-left" style="width:40%;font-size:20px;color:#ffffff;">注册</div>
				<div class="pull-left" style="width:30%;text-align:right;line-height: 30px;"><a href="<c:url value="/shop/customer/storeRegistration.html"/>" ><span style="color:#ffffff;">切换商家注册</span></a></div>
			</div>
		</div>	
		<div style="background:#ffffff;padding-top:24px; height:72px;">
			 <div style="padding-left:10px;" id="yd">
				<span class="padding-img"  style="font-size:16px;">1.输入手机号</span>
				<span ><img height="20" src="<c:url value="/resources/img/right.png"/>" /></span>
				<span class="padding-img" style="font-size:16px;">2.设置密码</span>
				<span class="padding-img" ><img height="20" src="<c:url value="/resources/img/right.png"/>" /></span>
				<span  style="font-size:16px;color:#5a8de2;font-weight:bold">3.个人资料</span>
				<span class="padding-img"><img height="20" src="<c:url value="/resources/img/right.png"/>" /></span>
			</div>					
		</div>
		<form:form method="post" action="${register_url}" id="registrationForm" class="form-horizontal" commandName="customer" >
			<div style="margin-top:20px;background:#ffffff;padding-top:20px;padding-bottom:20px;">
				<span class="padding-register" style="font-size:16px;">姓名：</span><form:input path="userName" success="false" id="userName" style="border: 0px;height:30px;font-size:16px;" placeholder="请输入您的姓名" cssClass="sminput"/>
			</div>
			<div id="userName_tips" style="height: 8px;padding-left:35px;color: red;padding-top: 10px;font-size: 14px;padding-bottom: 5px;"></div>
			<div style="margin-top:20px;background:#ffffff;padding-top:20px;padding-bottom:20px;">
				<span class="padding-register" style="font-size:16px;">邮箱：</span><form:input success="false" path="emailAddress" id="emailAddress" style="border: 0px;height:30px;font-size:16px;width:75%" placeholder="请输入您的邮箱" cssClass="sminput"/>
				<div id="green_info" style="float: right;padding-right:30px;padding-top:5px;"  hidden="hidden"><img src="<c:url value="/resources/img/zq.png"/>" width="24px" height="24px"/></div>
			</div>
			<div id="emailAddress_tips" style="height: 8px;padding-left:35px;color: red;padding-top: 10px;font-size: 14px;padding-bottom: 5px;"></div>
			<div style="margin-top:20px;background:#ffffff;padding-top:20px;padding-bottom:20px;">
				<span class="padding-register" style="font-size:16px;">课题组/部门：</span><form:input success="false" path="project" id="project" style="border: 0px;height:30px;font-size:16px;" cssClass="sminput"/>
				<div><form:errors path="project" cssClass="project" /></div>
			</div>
			<div id="project_tips" style="height: 8px;padding-left:35px;color: red;padding-top: 10px;font-size: 14px;padding-bottom: 5px;"></div>
			<div style="margin-top:20px;background:#ffffff;padding-top:20px;padding-bottom:20px;">
				<span class="padding-register" style="font-size:16px;">学校/公司：</span><form:input success="false" path="company" id="company" style="border: 0px;height:30px;font-size:16px;" cssClass="sminput"/>
				<div><form:errors path="company" /></div>
			</div>
			<div id="company_tips" style="height: 8px;padding-left:35px;color: red;padding-top: 10px;font-size: 14px;padding-bottom: 5px;"></div>
			<div style="margin-top:10px;">
				<div class="text-center" style="padding:20px;">
				<button type="submit"  id="next_submit" style="background:#5a8de2;" class="btn btn-lg btn-block"> <span style="color:#ffffff">提交资料</span></button></div>
			</div>
		</form:form>
	</div>
	<!--close .container "main-content" -->
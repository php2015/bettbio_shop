
<%
	response.setCharacterEncoding("UTF-8");
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", -1);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>
<link
	href="<c:url value="/resources/templates/bootstrap3/css/path-css.css" />"
	rel="stylesheet" type="text/css">
<script src="<c:url value="/resources/js/check-functions.js" />"></script>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<style>
 #contentDiv{
 	padding: 12% 0px 0px 15%!important;
 }	
</style>
<div class="container-fluid">
	<c:url var="resetpassword" value="/shop/customer/resetPass.html" />
	<script type="text/javascript">
		$(document).ready(function() {
		   $("input[type='text']").focus(function(){
			  $(this).attr("success",false);
			  var id=$(this).attr("id")
			  $("#"+id+"_tips").css("visibility","visible");
							   
		   }).blur(function(){
			   var val=$(this).val();
			   var id=$(this).attr("id")
			   if($(this).hasClass("signin_userName")){
				   if(val!=""){
					   $(this).attr("success",true);
					   $(this).parent().siblings(".success").show();
					   $("#"+id+"_tips").css("visibility","hidden");
				   }else{
					   $("#"+id+"_tips").find("img").attr("src",getContextPath()+"/resources/img/error.png");
	    			   $("#"+id+"title").text("请填写手机号或邮箱地址");
				   }
			   }else{
				   if(val!=""){
					   $.post("${pageContext.request.contextPath}/shop/valitationByImgCode.html",{imgCode:val},function(data){
			    			if(data.response.status==0){//验证是否成功
			    			  $(this).attr("success",true);
			    			  $("#"+id).parent().siblings(".success").show();
			    			  $("#"+id+"_tips").css('visibility','hidden');
			    			}else{
			    				 $("#"+id+"_tips").find("img").attr("src",getContextPath()+"/resources/img/error.png");
			    				 $("#"+id+"title").text("验证码不正确");
			    			}
			    		},"json");
				   }
			   }
			   
		   });
		   //提交
		   $("#Ezybiosubmit").click(function(){
			   $("input[type='text']").each(function(){
				   if($(this).attr("success")=="false"){
					   $("#"+$(this).attr("id")+"_tips").find("img").attr("src",getContextPath()+"/resources/img/error.png");
					   return false;
				   }
			   });
		   });
		});
	</script>

	<div style="height: 430px;">
		<div style="padding:1%;">
			<div class="box" style="margin-top: 40px;">
				<div style="margin-left:-120px">
					<img src="<c:url value="/resources/img/1.png"/>" style="width: 100%;padding-left: 176px"/>
				</div>

				<div class="row" style="font-size: 14px;margin-top: -105px;margin-left: 0px;margin-bottom: 10px;">找回密码</div>
					<div  class="row" id="contentDiv">
						<div class="col-sm-9 ">
							<form action="${resetpassword}" method="POST" id="ezybioForm"
								class="form-horizontal" name="ezybioForm">
								<fieldset>
									<div class="form-group" style="margin-bottom: 20px;">
										<label class="col-sm-2 control-label col-sm-offset-2" style="text-align: right;">账户名:</label>
										<div class="col-sm-4 ">
											<s:message code="NotEmpty.custmoer.account"
												text="account is required" var="accountName" />
											<input id="signin_userName" class="form-control signin_userName"
												type="text" name="signin_userName" title="${accountName}" style="width: 320px;"/>
										</div>
										<br/>
									    <img class="success" src="<c:url value="/resources/img/success.png"/>" width="20" height="20" style="margin-left:120px;margin-top:-9px;display: none;"/>
									    <div id="signin_userName_tips" class="col-sm-5 message" style="margin-top:10px;margin-left:280px;visibility:hidden;">
									      <img src="<c:url value="/resources/ad/icon.png"/>" height="15" width="15">&nbsp;&nbsp;
									      <span id="signin_userName_title">请填写手机号或邮箱地址</span>
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label col-sm-offset-2" style="text-align: right;"><s:message
												code="label.generic.validatecode" text="Validatecode" />:</label>
										<div class="col-sm-2">
											<s:message code="NotEmpty.store.validatecode"
												text="validatecode is required" var="validatecodeName" />
											<input Class="form-control captchaResponseField" type="text"
												name="captchaResponseField" id="captchaResponseField" style="width: 240px;z-index: 999"
												title="${validatecodeName}" />
										</div>
										<div class="col-sm-2" style="margin-bottom: 40px;">
											<a href="#" onclick="changeValidateImg()"><img
												id="imgObj" src="<c:url value="/shop/store/code.html"/>"  style="width: 80px;margin-left: 122px;min-height: 39px;z-index: 888"/></a>
										</div>
										 <label class="col-sm-2 control-label " style="margin-left:87px;margin-top:6px"><a href="#"
											onclick="changeValidateImg()"><s:message
													code="label.generic.change.validatecode" text="Change" /></a>:</label>
										<br/>
									    <img class="success" src="<c:url value="/resources/img/success.png"/>" width="20" height="20" style="margin-left:197px;margin-top:-19px;display: none;"/>
									    <div id="captchaResponseField_tips" class="col-sm-5 message" style="margin-top:-20px;margin-left:280px;visibility:hidden;">
									      <img src="<c:url value="/resources/ad/icon.png"/>" height="15" width="15">&nbsp;&nbsp;
									      <span id="captchaResponseField_title">请填写验证码</span>
									    </div>
									</div>
									<div class="form-group">
										<div class="col-sm-4 col-sm-offset-4">
											<input id="Ezybiosubmit" type="submit" style="width: 120px;"
												value="<s:message code="button.label.submit2" text="Send"/>"
												name="register" class="btn btn-info btn-block">
										</div>
									</div>
								</fieldset>
							</form>
						</div>
					</div>
			</div>
		</div>
	</div>
</div>

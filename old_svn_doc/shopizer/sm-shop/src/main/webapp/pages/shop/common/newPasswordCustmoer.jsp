
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
<c:url var="verifyPhone" value="/shop/customer/verifyPhone.html" />
<style>
.col-sm-offset-2 {
	text-align: right;
}
</style>

<script type="text/javascript">

$(document).ready(function() {
	var re=/^(?=.*[a-z].*)(?=.*[A-Z].*)(?=.*[0-9].*).{6,20}$/;
	$("#password").blur(function(){
		 var val=$(this).val();
		if(val!=""&&re.test(val)){
			$(this).attr("success",true);
			$(this).parent().siblings(".success").show();
			$(".error").hide();
		}else{
			$(".error").css("color","red");
		}
		return false;
	});
	$("#checkPassword").blur(function(){
		 var val=$(this).val();
		 if($(this).val()==$("#password").val()){
				$(this).attr("success",true);
				$(this).parent().siblings(".success").show();
				$(".error2").hide();
			}else{
				$(".error2").css("color","red");
		}
	});
	$(".form-control").focus(function(){
		$(this).attr("success",false);
		$(this).parent().siblings(".success").hide();
		return false;
	});
	
});

 </script>
<c:url var="donePass" value="/shop/customer/donePassword.html"/>
<div style="padding:1%">
	<div class="box">
		<div style="margin-left:-120px">
			<img src="<c:url value="/resources/img/3.png"/>"
				style="width: 100%;padding-left: 176px" />
		</div>
		<div class="row"
			style="font-size: 14px;margin-top: -105px;margin-left: 0px;margin-bottom: 10px;">找回密码</div>
		<div style="padding-top:2%" class="row">
			<div class="col-sm-9 " style="margin:140px;">
				<form action="${donePass}" method="POST" id="ezybioForm"
					class="form-horizontal" name="ezybioForm">
					<fieldset>
						<div class="form-group">
							<label class="col-sm-2 control-label col-sm-offset-2"
								style="text-align: right;" for="password"><s:message
									code="label.customer.newpassword" text="New password" />:</label>
							<div class="col-sm-4">
								<s:message code="newpassword.not.empty"
									text="New password is required" var="msgPassword" />
								<input type="password" class="required password form-control"
									name="passwords" title="${msgPassword}" id="password"/>
							</div>
							 <img class="success" src="<c:url value="/resources/img/success.png"/>" width="20" height="20" style="margin-left:37px;margin-top:8px;display: none"/>
						</div>
						<div class="form-group error">
							<label class="col-sm-2 col-sm-offset-2 control-label"></label>
							<div class="col-sm-6">
								必须包含数字、大写字母、小写字母,6-20位字符
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label col-sm-offset-2"
								style="text-align: right;" for="repeatPassword"><s:message
									code="label.customer.repeatpassword" text="Repeat password" /></label>
							<div class="col-sm-4">
								<s:message code="repeatpassword.not.empty"
									text="Current password is required" var="msgRepeatPassword" />
								<input type="password"
									class="required checkPassword form-control " id="checkPassword"
									name="checkPassword" title="${msgRepeatPassword}" />
							</div>
							<img class="success" src="<c:url value="/resources/img/success.png"/>" width="20" height="20" style="margin-left:37px;margin-top:8px;display: none"/>
						</div>
						 <div class="form-group error2">
							<label class="col-sm-2 col-sm-offset-2 control-label"></label>
							<div class="col-sm-6 ">
								请再次输入密码
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-6 col-sm-offset-4">
								<input id="Ezybiosubmit" type="submit"
									value="<s:message code="button.label.submit2" text="Send" />"
									name="register" class="btn btn-large">
							</div>
						</div>
					</fieldset>
				</form>
			</div>
		</div>
	</div>
</div>

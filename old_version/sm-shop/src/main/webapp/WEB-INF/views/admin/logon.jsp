 <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
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

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<head>
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="expires" content="0">
	<title><s:message code="label.storeadministration" text="Store administration" /> </title>
   <meta name="keywords" content='<s:message code="label.website.keywords" text="bettbio.com"/>'>
   <meta name="description" content='<s:message code="label.website.descirption" text="bettbio.com"/>'>
   <meta name="author" content="bettbio.com">
   <meta http-equiv="X-UA-Compatible" content="IE=edge">
   <meta name="renderer" content="webkit">
<style type=text/css>
#logon {
	margin: 0px auto;
	width: 550px
}
#login-box {
	width: 333px;
	height: 352px;
	padding: 58px 76px 0 76px;
	color: #ebebeb;
	font: 12px Arial, Helvetica, sans-serif;
	background: url('<c:url value="/resources/img/admin/login-box-backg.png" />') no-repeat left top;
}
#login-box h2 {
	padding: 0;
	margin: 0;
	color: #ebebeb;
	font: bold 36px "Calibri", Arial;
	border-bottom: 2px solid;
	padding-bottom: 0px;
}
#login-box {
	margin-left: 30px;
}

#controls {
	margin-left: -50px;
	margin-top: 30px;
}
</style>
<link
	href="<c:url value="/resources/css/bootstrap/css/sm-bootstrap.css" />"
    rel="stylesheet" />
<style type=text/css>
.sm label {
	color: #EBEBEB;
	font-size: 16px;
}

.sm a {
	color: #EBEBEB;
	font-size: 16px;
}
</style>
<script src="<c:url value="/resources/js/bootstrap/jquery.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/js/jquery-cookie.js"/>"></script>
<script src="<c:url value="/resources/js/bootstrap/bootstrap-modal.js" />"></script>




<script language="javascript">
function enterSubmit(src,e)
 {
        if(window.event)
            keyPressed = window.event.keyCode; // IE
        else
            keyPressed = e.which; // Firefox
        if(keyPressed==13)
        { 			             
            $("#formSubmitButton").click();            
         return false;
       }
   }
				
$(document)
.ready(
		function() {
			var username = $.cookie('usernamecookie');
			if (username != null && username != '') {
				$('#j_username').val(username);
				$('#remember').attr('checked', true);
			}

$("#formSubmitButton")
 .click(
	function() {
		var hasError = false;
		$('#j_username_help').html("");
		$('#j_password_help').html("");
		
		
		if ($('#remember').attr('checked')) {
			$.cookie('usernamecookie', $(
					'#j_username').val(), {
				expires : 1024,
				path : '/'
			});
		} else {
			$.cookie('usernamecookie',
					null, {
						expires : 1024,
						path : '/'
					});
		}
		if ($.trim($('#j_username').val()) == '') {
			hasError = true;
			$('#j_username_help')
					.html(
							"<font color='red' size='4'><strong>*</strong></font>");
		}

		if ($.trim($('#j_password').val()) == '') {
			hasError = true;
			$('#j_password_help')
					.html(
							"<font color='red' size='4'><strong>*</strong></font>");
		}

		if (!hasError) {
			$("#logonForm").submit();
		}

	});
});
</script>
</head>
	<body>
		<div id="tabbable" class="sm">
			<br />
			<br />
			<div id=logon>
				<div class="row">
					<c:if test="${not empty param.login_error}">
						<div class="alert alert-error">
							<s:message code="errors.invalidcredentials"
								text="Invalid username or password" />
						</div>
					</c:if>
				</div>
				<div id="login-box">
					<div class="row">
						<div style="float: left; width: 180px;">
							<p class="lead">
								<s:message code="button.label.logon" text="Logon" />
							</p>
						</div>
						<div style="float: right;">
							<img alt="go to ezybio"
								src="<c:url value="/resources/img/shopizer_small.png" />">
							<p class="lead">Bettbio.com</p>
						</div>
					</div>
					<div class="row">
						<div id="controls">

							<form method="post" id="logonForm" class="form-horizontal" action="<c:url value="/admin/j_spring_security_check"/>">
								<div class="control-group">
									<label class="control-label" for="inputUser">
										<s:message code="label.username" text="Username" />
									</label>
									<div class="controls">
										<input type="text" id="j_username" name="j_username"
											placeholder="<s:message code="label.username" text="Username"/>">
											<span id="j_username_help" class="help-inline"></span>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="inputPassword">
										<s:message code="label.password" text="Password" />
									</label>

									<div class="controls">
										<input type="password" id="j_password" name="j_password"
											placeholder="<s:message code="label.password" text="Password"/>" onkeydown="return enterSubmit(this,event);"/>
											<span id="j_password_help" class="help-inline"></span>
									</div>
								</div>
								<div class="control-group">
									<div class="controls">
										<label class="checkbox">
											<input type="checkbox" id="remember">
												<s:message code="label.logonform.rememberusername"
													text="Remember my user name" />
													<a href="<c:url value="/admin/forget.html" />" class="pull-right" > <s:message
												code="label.custmoer.forget.password" text="Forget" /></a>
												<br/><br/>
										</label>
										<a href="#" class="btn btn-block" id="formSubmitButton"> <s:message
												code="button.label.logon" text="button.label.submit2" /></a><a href="<c:url value="/admin/register.html" />" class="pull-right" > <s:message
												code="label.register.notyetregistered" text="Register" /></a>
									</div>
								</div>
							</form>
						</div>
					</div>
				  </div>
				</form>
			</div>
		</div>
	</body>
	
</html>

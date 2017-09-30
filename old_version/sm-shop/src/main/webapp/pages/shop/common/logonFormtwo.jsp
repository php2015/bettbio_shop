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
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>
<%@ taglib uri="/WEB-INF/shopizer-functions.tld" prefix="display" %> 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
  <script src="<c:url value='/resources/js/login.js'/>?v=<%=new java.util.Date().getTime()%>"></script>
  <script type="text/javascript" charset="utf-8">
 	var userholder ='<s:message	code="label.longin.username" text="holde" />';
 </script>
<script type="text/javascript" src="<c:url value="/resources/js/jquery-cookie.js"/>"></script>
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
		$('#remember1').attr('checked', true);
	}
 
$("#formSubmitButton")
			.click(
					function() {
		var hasError = false;
		$('#j_username_help').html("");
		$('#j_password_help').html("");
		
		
		if ($('#remember1').attr('checked')) {
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
	
 	//验证用户名或密码是否有错误
	$(window).load(function(){
	   var url=window.location.href;
	   if(url.indexOf('?')!=-1){
	      $("#storeActive").addClass("active");
	      $("#customerActive").removeClass("active");
	      $("#customerlogin").hide();
	      $("#stroelogin").show();
	   }else{
	      $("#storeActive").removeClass("active");
	      $("#customerActive").addClass("active");
	      $("#customerlogin").show();
	      $("#stroelogin").hide();
	   }
	   
	}); 
	
	  $("#customerActive").click(function(){
          $("#stroelogin").hide();
          $("#customerlogin").show();
          $("#storeError").hide();
          
   });
   
   $("#storeActive").click(function(){
          $("#stroelogin").show();
          $("#customerlogin").hide();
           
   }); 

});
</script>
<style>
.head-navbar-left{
padding-left:12%!important;
}
</style>

<div style="padding-bottom:0px;" id="login">
	  <ul class="nav nav-tabs" role="tablist">
	    <li role="presentation" class="active" id="customerActive" style="width:150px;text-align:center;font-weight: 40px;"><a href="#customerlogin" aria-controls="home" role="tab" data-toggle="tab">买家用户</a></li>
	    <li role="presentation" class="" id="storeActive" style="width:150px;text-align:center;"><a href="#stroelogin" aria-controls="profile" role="tab" data-toggle="tab" >卖家用户</a></li>
	  </ul>
	<div id="customer_div">
    <form id="login" method="post" accept-charset="UTF-8">
     <div class="tab-content">  
        <div class="tab-pane active" id="customerlogin">  
          <span id="loginError"  style="display:none;color:red;"></span>
		 <div class="control-group">
			<br>
			<div class="controls input-group">
				<span class="input-group-addon"> <span class=" glyphicon glyphicon-user" aria-hidden="true"  ></span></span>
				<input id="signin_userName" class="form-control" type="text" name="userName" size="18" placeholder=<s:message	code="label.longin.username" text="holde" /> />
			</div>
		</div>
		<br>
		
		<!-- userpassword  -->
		<div class="control-group">
			<div class="controls input-group">
				<%-- <span class="input-group-addon"> <span class=" glyphicon glyphicon-lock" aria-hidden="true"  ></span></span>
				<input id="signin_password" class="form-control" type="password" name="password" size="18" /> --%>
				<span class="input-group-addon"> <span class=" glyphicon glyphicon-lock" aria-hidden="true"  ></span></span>
				<input id="signin_password" class="form-control" type="password" name="password" size="18" />
			</div>
		</div>
		<!-- checkbox -->
		<div class="control-group">
			<br>
			<div class="controls">
				<label class=" btn-block">
   					 <div style="margin-top:0px;"><input type="checkbox" id="remember"/></div><div style="padding-top:3px;font-size:12px;color:gray">记住用户名</div>
					 <div class="pull-right" style="margin-top: -18px"><a	onClick="javascript:location.href='<c:url value="/shop/customer/resetPassword.html" />';" href="" role="button" data-toggle="modal"><s:message code="label.custmoer.forget.password" text="Get Password" /></a></div>						
  					</label>
			</div>
		</div>
  <br>
  <!-- login -->
  <div class="control-group">
			<button id="login-button" type="button" class="btn btn-default btn-info btn-block" ripple><s:message code="button.label.login" text="Login" /></button>	
			</div>
		<input id="signin_storeCode" name="storeCode" type="hidden"
			value="<c:out value="${requestScope.MERCHANT_STORE.code}"/>" />
	</form>	
	<!-- register -->
	<br/>
	<div class="logon-password-box"  >
		<div class="pull-left"><a onClick="javascript:location.href='<c:url value="/shop/customer/registration.html?c=customer" />';" href="" role="button" data-toggle="modal">新用户注册</a></div>
	</div>
    </div>  
    <!-- 卖家登录 -->
    <div id="stroelogin" class="tab-pane"> <!-- <c:url value="/admin/j_spring_security_check"/> -->
		<form method="post" id="logonForm" class="form-horizontal" action="<c:url value="/admin/j_spring_security_check"/>">
		 <div><span id="storeError"  style="color:red;">${param.login_error == null ? "" :"用户名或密码输入错!" }</span></div>
		<div class="control-group">
		 <div class="control-group">
			<br>
			<div class="controls input-group">
				<span class="input-group-addon"> <span class=" glyphicon glyphicon-user" aria-hidden="true" id="j_username_help" ></span></span>
				<input  id="j_username" class="form-control" type="text" name="j_username" size="18" placeholder=<s:message code="label.username" text="Username" /> />
			</div>
		</div>
		<br>
		
		<!-- userpassword  -->
		<div class="control-group">
			<div class="controls input-group">
				<span class="input-group-addon"> <span class=" glyphicon glyphicon-lock" aria-hidden="true" id="j_password_help" ></span></span>
				<input id="j_password" class="form-control" type="password" name="j_password" size="18" placeholder="<s:message code="label.password" text="Password"/>" onkeydown="return enterSubmit(this,event);"/>
			</div>
		</div>
		<!-- checkbox -->
		<div class="control-group">
			<br>
			<div class="controls">
				<label class=" btn-block">
   					 <div style="margin-top:0px;"><input type="checkbox" id="remember1"/></div><div style="padding-top:3px;font-size:12px;color:gray">记住用户名</div>
					 <div class="pull-right" style="margin-top: -18px"><a	onClick="javascript:location.href='<c:url value="/admin/forget.html" />';" href="" role="button" data-toggle="modal"><s:message code="label.custmoer.forget.password" text="Get Password" /></a></div>						
  					</label>
			</div>
		</div>
  <br>
  <!-- storelogin -->
  <div class="control-group">
			<button id="formSubmitButton" type="button" class="btn btn-default btn-info btn-block" ripple><s:message code="button.label.login" text="Login" /></button>	
			</div>
		<input id="formSubmitButton" name="storeCode" type="hidden"
			value="<c:out value="${requestScope.MERCHANT_STORE.code}"/>" />
	</form>	
	<!-- register -->
	<br/>
	<div class="logon-password-box"  >
		<div class="pull-left"><a id="formSubmitButton" onClick="javascript:location.href='<c:url value="/shop/customer/storeinsertAdmin.html" />';" href="" role="button" data-toggle="modal">新用户注册</a></div>
	</div>
    </div>  
	</div>
  </div> 
  </div> 
</div>
<br/>
<br/>
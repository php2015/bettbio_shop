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
     var userholder ='<s:message    code="label.longin.username" text="holde" />';
 </script>
<script type="text/javascript" src="<c:url value="/resources/js/jquery-cookie.js"/>"></script>
<script language="javascript">
<!-- from pages\shop\common\logonFormSms.jsp -->

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

var urlVerifyImgCode = "<c:url value='/shop/valitationByImgCode.html'/>";
var smsSendUrl = "<c:url value='/shop/sendLogonSMSCode.html'/>";
var smsResendCount = 0;
var smsResendHandler;
var preLogonType = "${sessionScope.logonType}";
var sellerLogonError = "${sessionScope.sellerLogonError}";
$(document).ready(function() {
    var username = $.cookie('usernamecookie');
    if (username != null && username != '') {
        $('#j_username').val(username);
        $('#remember1').attr('checked', true);
    }
 
	$("#formSubmitButton").click(function() {
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
       if(preLogonType == "sms" || url.indexOf('?t=sms')!=-1){
          $("#smsActive").addClass("active");
          $("#customerActive").removeClass("active");
          $("#customerlogin").hide();
          $("#smslogin").show();
       }else{
          $("#smsActive").removeClass("active");
          $("#customerActive").addClass("active");
          $("#customerlogin").show();
          $("#smslogin").hide();
       }
       
    }); 
    
    $("#customerActive").click(function(){
          $("#smslogin").hide();
          $("#customerlogin").show();
          $("#storeError").hide();
          
	});
   
	$("#smsActive").click(function(){
          $("#smslogin").show();
          $("#customerlogin").hide();
           
	});
	
	$("#j_username").keyup(function(){
		verifySmsPhone();
	});
	$("#j_username").blur(function(){
		verifySmsPhone();
	});
	
	$("#j_imgCode").keyup(function(){
		verifySmsImgCode();
	});
	$("#j_imgCode").blur(function(){
		verifySmsImgCode();
	});
	
	verifySmsPhone();
	verifySmsImgCode();
	refreshSmsUiState();
	
	if(sellerLogonError == "maybe_buyer_no_seller" || sellerLogonError == "no_buyer_no_seller"){
		$("#buyerLogonAsSellerFailModal").modal("show");
	}
});
</script>
<style>
.head-navbar-left{
padding-left:12%!important;
}
</style>

<div style="padding-bottom:0px;" id="login">
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active" id="customerActive" style="width:150px;text-align:center;font-weight: 40px;">
            <a href="#customerlogin" aria-controls="home" role="tab" data-toggle="tab">账号密码登录</a>
        </li>
        <li role="presentation" class="" id="smsActive" style="width:150px;text-align:center;">
            <a href="#stroelogin" aria-controls="profile" role="tab" data-toggle="tab" >手机短信登录</a>
        </li>
    </ul>
    <div id="customer_div">
        <div class="tab-content">  
            <div class="tab-pane active" id="customerlogin">
                <span id="loginError"  style="color:red;">${param.login_error == null ? "" :"用户名密码错误!" }</span>
                <form id="accountLoginForm" method="post" accept-charset="UTF-8" action="<c:url value='/admin/j_spring_security_check'/>">
                    <!-- user name or phonenumber or email -->
                    <div class="control-group">
                        <br>
                        <div class="controls input-group">
                            <span class="input-group-addon"> <span class=" glyphicon glyphicon-user" aria-hidden="true"  ></span></span>
                            <input id="signin_userName" class="form-control" type="text" name="j_username" size="18" placeholder=<s:message    code="label.longin.username" text="holde" /> />
                        </div>
                    </div>
                    <br>
        
                    <!-- userpassword  -->
                    <div class="control-group">
                        <div class="controls input-group">
                            <span class="input-group-addon"> <span class=" glyphicon glyphicon-lock" aria-hidden="true"  ></span></span>
                            <input id="signin_password" class="form-control" type="password" name="j_password" size="18" />
                        </div>
                    </div>
                    <!-- checkbox -->
                    <div class="control-group">
                        <br>
                        <div class="controls">
                            <label class=" btn-block">
                                <div style="margin-top:0px;">
                                    <input type="checkbox" id="remember"/>
                                </div>
                                <div style="padding-top:3px;font-size:12px;color:gray">记住用户名</div>
                                <div class="pull-right" style="margin-top: -18px">
                                    <a  onClick="javascript:$('#forgotPwdSelectionModal').modal('show')" href="" role="button" data-toggle="modal">
                                        <s:message code="label.custmoer.forget.password" text="Get Password" />
                                    </a>
                                </div>                        
                            </label>
                        </div>
                    </div>
                    <br>
                    <!-- login button-->
                    <div class="control-group">
                        <button type="button" class="btn btn-default btn-info btn-block" ripple onclick="showLogonSelectionModal(); return false;">
                            <s:message code="button.label.login" text="Login" />
                        </button>    
                    </div>
                    <input id="signin_storeCode" name="storeCode" type="hidden"    value="<c:out value="${requestScope.MERCHANT_STORE.code}"/>" />
					<input id="signin_logonType" type="hidden" name="logonType" value="account"></input>
                </form>    
    
                <!-- register -->
                <br/>
                <div class="logon-password-box">
                    <div class="pull-left">
                        <a onClick="javascript:location.href='<c:url value="/shop/customer/registration.html?c=customer" />';" href="" role="button" data-toggle="modal">买家注册</a>
                    </div>
                    <div class="pull-right">
                        <a class="pull-right" onClick="javascript:location.href='<c:url value="/shop/customer/storeinsertAdmin.html?c=store" />';" href="" role="button" data-toggle="modal">
                            卖家注册
                        </a>
                    </div>
                </div>
            </div> 
            
            <!-- 短信验证码登录 -->
            <div id="smslogin" class="tab-pane"> <!-- <c:url value="/admin/j_spring_security_check"/> -->
                <div>
                    <span id="storeError"  style="color:red;">${param.login_error == null ? "" :"短信验证码错误!" }</span>
                </div>
                <form method="post" id="logonForm" class="form-horizontal" action="<c:url value='/admin/j_spring_security_check'/>">
                    <!-- mobile number  -->
                    <div class="control-group">
                        <br/>
                        <div class="controls input-group">
                            <span class="input-group-addon"> <span class=" glyphicon glyphicon-phone" aria-hidden="true" id="j_username_help" ></span></span>
                            <input  id="j_username" class="form-control" type="text" name="j_username" size="18" placeholder="手机号" />
                        </div>
                    </div>
                    <br/>
					
					<!-- recaptcha -->
					<div class="control-group">
                        <div class="controls input-group">
                            <span class="input-group-addon"> <span class=" glyphicon glyphicon-question-sign" aria-hidden="true" id="j_imgCode_help" ></span></span>
                            <input  id="j_imgCode" class="form-control" type="text" name="j_imgCode" size="18" placeholder="验证码" style="width: 120px;"/>
							<img id="imgObj" class="pull-right" src="<c:url value='/shop/store/code.html'/>" 
									style="width:120px;height:40px;margin-right:10px;" onclick="changeValidateImg()"/>
                        </div>
						<div class="pull-right">
							<a id="prompt_get_imgCode" href="javascript:changeValidateImg();" style="width:120px;height:40px;margin-right:10px;">看不清，换一个</a>
						</div>
                    </div>
                    <br/>
					
					<!-- SMS code -->
                    <div class="control-group">
						<br/>
                        <div class="controls input-group">
                            <span class="input-group-addon"> <span class=" glyphicon glyphicon-lock" aria-hidden="true" id="j_password_help" ></span></span>
                            <input id="j_password" class="form-control" type="text" name="j_password" size="18" placeholder="短信验证码" style="width: 120px;" />
							<button id="j_password_send" type="button" class="pull-right btn btn-info" style="width:120px;height:40px;margin-right:10px;" onclick="sendSmsCode();return false;" >发送短信验证码</button>
                        </div>
						<div class="pull-right" id="prompt_resend_sms" style="width:220px;height:40px;margin-right:10px;"> </div>
                    </div>
					<br/>
					
					
					<!-- storelogin -->
					<div class="control-group">
						<button id="smsFormSubmitButton" type="button" class="btn btn-default btn-info btn-block" ripple
							onclick="showLogonSmsSelectionModal();return false;">
							<s:message code="button.label.login" text="Login" />
						</button>    
					</div>
					<input id="j_logonType" type="hidden" name="logonType" value="sms"></input>
					<input name="storeCode" type="hidden" value="<c:out value='${requestScope.MERCHANT_STORE.code}'/>" />
                </form>    
    
	
            </div>  
        </div>
    </div> 
</div> 
<br/>
<br/>
<!-- logon selection modal -->
<div class="modal fade" id="logonSelectionModal" tabindex="-1" role="dialog" style="position: fix; top: 200px;">
    <div class="modal-dialog modal-bg" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <button type="button" class="btn btn-default btn-info btn-block" ripple onclick="loginAsBuyer()">
                            登录到买家页面
                </button>
                <br/>
                <button type="button" class="btn btn-default btn-info btn-block" ripple onclick="loginAsSeller()">
                            登录到卖家页面
                </button>                
            </div>
        </div>
  </div>
</div>
<!-- seller login fail modal-->
<div class="modal fade" id="buyerLogonAsSellerFailModal" tabindex="-1" role="dialog" style="position: fix; top: 200px;">
    <div class="modal-dialog modal-bg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myAlertModalLabel">提示</h4>
            </div>
            <div class="modal-body">
                <span class="lead">
					您登录为卖家失败。<br/>
					请联系我们的客服人员，为您注册一个卖家账号<br/>
				</span>
            </div>
			
            <div class="modal-footer">
				<a class="pull-left btn btn-primary" onClick="javascript:location.href='<c:url value="/shop/store/contactus.html" />';" href="" role="button" data-toggle="modal">
                    联系我们
                </a>
                <button type="button"  class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<!-- logon selection modal: sms -->
<div class="modal fade" id="logonSmsSelectionModal" tabindex="-1" role="dialog" style="position: fix; top: 200px;">
    <div class="modal-dialog modal-bg" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <button type="button" class="btn btn-default btn-info btn-block" ripple onclick="loginSmsAsBuyer()">
                            登录到买家页面
                </button>
                <br/>
                <button type="button" class="btn btn-default btn-info btn-block" ripple onclick="loginSmsAsSeller()">
                            登录到卖家页面
                </button>                
            </div>
        </div>
    </div>
</div>
<!-- buyer login fail modal：101 -->
<div class="modal fade" id="sellerLogonAsBuyerFailModal" tabindex="-1" role="dialog" style="position: fix; top: 200px;">
    <div class="modal-dialog modal-bg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myAlertModalLabel">提示</h4>
            </div>
            <div class="modal-body">
                <span class="lead">您登录为买家失败。<br/>系统检测到您有卖家账号，现在去注册一个买家账号么？</span>
            </div>
            <div class="modal-footer">
                <a class="pull-left btn btn-primary" onClick="javascript:location.href='<c:url value="/shop/customer/registration.html?c=customer" />';" href="" role="button" data-toggle="modal">
                    好的，注册一个买家
                </a>
                <button type="button"  class="btn btn-default" data-dismiss="modal">不，暂时不要</button>
            </div>
        </div>
    </div>
</div>
<!-- buyer login fail modal：102 -->
<div class="modal fade" id="sellerLogonAsBuyerNotFoundModal" tabindex="-1" role="dialog" style="position: fix; top: 200px;">
    <div class="modal-dialog modal-bg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myAlertModalLabel">提示</h4>
            </div>
            <div class="modal-body">
                <span class="lead">您登录为买家失败。<br/>现在去注册一个买家账号么？</span>
            </div>
            <div class="modal-footer">
                <a class="pull-left btn btn-primary" onClick="javascript:location.href='<c:url value="/shop/customer/registration.html?c=customer" />';" href="" role="button" data-toggle="modal">
                    好的，注册一个买家
                </a>
                <button type="button"  class="btn btn-default" data-dismiss="modal">不，暂时不要</button>
            </div>
        </div>
    </div>
</div>
<!-- forget password selection modal -->
<div class="modal fade" id="forgotPwdSelectionModal" tabindex="-1" role="dialog" style="position: fix; top: 200px;">
    <div class="modal-dialog modal-bg" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <a type="button" class="btn btn-default btn-info btn-block" ripple href="<c:url value='/shop/customer/resetPassword.html' />">
                            我忘记了买家密码
                </a>
                <br/>
                <a type="button" class="btn btn-default btn-info btn-block" ripple href="<c:url value='/admin/forget.html' />">
                            我忘记了卖家密码
                </a>                
            </div>
        </div>
    </div>
</div>

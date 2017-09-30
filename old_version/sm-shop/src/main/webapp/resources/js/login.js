var customerLogonType;

    $(function() {
    	if(!placeholderSupport()){   
    	    $('[placeholder]').focus(function() {
    	        var input = $(this);
    	        if (input.val() == input.attr('placeholder')) {
    	            input.val('');
    	            input.removeClass('placeholder');
    	        }
    	    }).blur(function() {
    	        var input = $(this);
    	        if (input.val() == '' || input.val() == input.attr('placeholder')) {
    	            input.addClass('placeholder');
    	            input.val(input.attr('placeholder'));
    	        }
    	    }).blur();
    	};
    	var username = $.cookie('usernamecookie');
		if (username != null && username != '') {
			$('#signin_userName').val(username);
			$('#remember').attr('checked', true);
		}
    	    	
    	$("#signin").click(function(e){
    		$("#loginError").hide();
    		e.preventDefault();
    	});
    	
    	$("#remember").click(function(e){    		
        	e.stopPropagation();    	});
    	
       
        $('#registerLink').click(function(e) {
        	e.preventDefault();
        	e.stopPropagation();
        });
        
        $("#login-button").click(function(e) {
        	//e.stopPropagation();
        	login();
        	
        	
        });
        $('#signin_password').keydown(function(e){
        	e.stopPropagation();
        	if(e.keyCode==13){
        		//login();
				showLogonSelectionModal();
        	}
        	});

    });
    
    function longinPage(){
    	window.location.href=getContextPath() + '/shop/customer/customLogon.html';
    }
    /**
     * 页面登录
     * */
    
function login() {
//$("#login").submit(function(e) {
	//e.preventDefault();//do not submit form
	
	$("#loginError").hide();
	
	var userName = $('#signin_userName').val();
	var password = $('#signin_password').val();
	if(userName==userholder){
		userName=='';
	}
	
	var storeCode = $('#signin_storeCode').val();
	if(userName=='' || password=='') {
		 $("#loginError").html(getLoginErrorLabel());
		 $("#loginError").show();
		 return;
	}
	customerLogonType = "account";
	
	//$('#login').showLoading();
	/**
	 * Ajax
	 * */
	$.ajax({
		 type: "POST",
		 //my version
		 url: getContextPath() + "/shop/customer/logon.html",
		 data: "userName=" + userName + "&password=" + password + "&storeCode=" + storeCode+"&logonType=account",
		 cache:false,
		 dataType:'json',
		 'success': onLogonSuccess
	});
	return false;
//});
}

function onLogonSuccess (response) {
            
//$('#login').hideLoading();
$("#logonSelectionModal").modal("hide");
// 参见 CustomerLoginController 的logon()注释，了解错误码定义
	 if (response.response.status==0) {//success
	   //SHOPPING_CART
	   //console.log(response.response.SHOPPING_CART);
	   if(response.response.SHOPPING_CART!=null && response.response.SHOPPING_CART != ""){
		  //console.log('saving cart ' + response.response.SHOPPING_CART);
		  /** save cart in cookie **/
		  var cartCode = buildCartCode(response.response.SHOPPING_CART);
		  $.cookie('cart',cartCode, { expires: 1024, path:'/' });
		  
	   }
	   if ($('#remember').attr('checked')) {
			$.cookie('usernamecookie', $(
					'#signin_userName').val(), {
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
	   //console.log('href -> ' + $(location).attr('href'));
	   //location.href=  $(location).attr('href');
//                	   location.replace(location);
	   gotoPageAfterLogin();
	}else if(response.response.status==-2){
		$("#loginError").html(getFreezeErrorLabel());
		$("#loginError").show();
	}else if(response.response.status==-101){
		$('#sellerLogonAsBuyerFailModal').modal("show");
	}else if(response.response.status==-102){ 
		//$("#loginError").html(getFreezeErrorLabel());
		//$("#loginError").show();
		$('#sellerLogonAsBuyerNotFoundModal').modal("show");	
	}else if(response.response.status==-103){ 
		$('#sellerLogonAsBuyerFailModal').modal("show");					   
	}else if(response.response.status==-2){ 
		//$("#signinDrop").dropdown("toggle");
		$("#loginError").html(getFreezeErrorLabel());
		$("#loginError").show();
	}else {
		$("#loginError").html("用户名密码错误！");
		$("#loginError").show();
	}

}



function placeholderSupport() {
	return 'placeholder' in document.createElement('input');
}
function showLogonError(msg){
	$('#loginError').text(msg);
	$('#loginError').show();
}
function showLogonSelectionModal(){
	var logonName = $('#signin_userName').val();
	if ($.trim(logonName)==''){
		return showLogonError("必须填写用户名和密码");
	}
	var logonPwd = $('#signin_password').val();
	if (logonPwd == ''){
		return showLogonError("必须填写用户名和密码");
	}
	$("#logonSelectionModal").modal({backdrop: 'static'});
}

function showBuyerLogonAsSellerFailModal(){
	$('#buyerLogonAsSellerFailModal').modal("show");
}
function loginAsBuyer(){
	$("#logonSelectionModal").modal("hide");
	login();
}
function loginAsSeller(){
	$("#logonSelectionModal").modal("hide");
	sellerLogin();
}
function sellerLogin(){
	$("#loginError").hide();
	
	var userName = $('#signin_userName').val();
	var password = $('#signin_password').val();
	if(userName==userholder){
		userName=='';
	}
	
	var storeCode = $('#signin_storeCode').val();
	if(userName=='' || password=='') {
		 $("#loginError").html(getLoginErrorLabel());
		 $("#loginError").show();
		 return;
	}
	$("#accountLoginForm").submit();
}

function loginSmsAsBuyer(){
	$("#logonSmsSelectionModal").modal("hide");
	smsLogin();
}
function loginSmsAsSeller(){
	$("#logonSmsSelectionModal").modal("hide");
	sellerSmsLogin();
}
function SetRemainTime(){
	smsResendCount--;
	if (smsResendCount <= 0){
		smsResendCount = 0;
		$("#prompt_resend_sms").text(" ");
		window.clearInterval(smsResendHandler);
	}else{
		$("#prompt_resend_sms").text(smsResendCount+"秒后可以重新发送验证码");
	}
	refreshSmsUiState();
}
function sendSmsCode() {
	if (!verifyCanSendSmsCode()){
		alert("图形验证错误或需正确完成图形验证");
		return;
	}
	$.ajax({  
		type: 'POST',
			url: smsSendUrl+"?phone="+$('#j_username').val(),
			cache: false,
			dataType: 'json',
			success: function(response) {
				if(response.response.status==0) {
					smsResendCount = 60;
					smsVerifyData.sendSms = true;
		    		smsResendHandler = window.setInterval(SetRemainTime, 1000);
					if ( typeof response.response._randomNumber == "string"){
						alert("短信发送成功，验证码是:"+response.response._randomNumber);
						$("#j_password").val(response.response._randomNumber);
					}else{
						alert("短信发送成功, 请查看手机获得验证码");
					}
					$('#storeError').hide();

				}else{
					smsVerifyData.sendSms = false;
					$('#storeError').text("短信发送失败，请确认你的手机号再试");
					$('#storeError').show();
				}
				refreshSmsUiState();
			} ,
			error: function( textStatus, errorThrown) {
				$('#storeError').text(textStatus);
				$('#storeError').show();
			}
		}
	);
}
function verifyCanSendSmsCode(){
	return smsVerifyData.phone && smsVerifyData.imgCode && smsResendCount <= 0;
}

var smsVerifyData = {
	phone: false,
	imgCode: false,
	sendSms: false
}
//$('#button').attr('disabled',"true");添加disabled属性
//$('#button').removeAttr("disabled")
function disableSmsSend(){
	$('#j_password_send').attr('disabled',"true");
}
function enableSmsSend(){
	$('#j_password_send').removeAttr("disabled")
}
function disableSmsLogon(){
	$('#smsFormSubmitButton').attr('disabled',"true");
}
function enableSmsLogon(){
	$('#smsFormSubmitButton').removeAttr("disabled")
}
function refreshSmsUiState(){
	if (!smsVerifyData.phone || !smsVerifyData.imgCode || smsResendCount > 0){
		disableSmsSend();
	}else{
		enableSmsSend();
	}
	
	if (smsVerifyData.sendSms){
		enableSmsLogon();
	}else{
		disableSmsLogon();
	}
}

function verifySmsPhone(){
	smsVerifyData.phone = false;
	var phoneNumber = $('#j_username').val();
	var phonePtn = /^1[3-9]\d{9}$/;
	if (phoneNumber!='' && phoneNumber.length==11){
		if (phonePtn.test(phoneNumber)){
			$('#storeError').hide();
			smsVerifyData.phone = true;
			refreshSmsUiState();
			return true;
		}
	}
	if (phoneNumber.length<11) return false;
	$('#storeError').text("请输入正确的手机号");
	$('#storeError').show();
	refreshSmsUiState();
	return false;
}

function verifySmsImgCode(){
	smsVerifyData.imgCode = false;
	var imgCode = $('#j_imgCode').val();
	if (imgCode!='' && imgCode.length==4){
		$.post(urlVerifyImgCode,{imgCode:imgCode},function(data){
			if(data.response.status==0){//验证是否成功
				smsVerifyData.imgCode = true;
				$('#storeError').hide();
			}else{
				smsVerifyData.imgCode = false;
				$('#storeError').text("验证码不正确");
				$('#storeError').show();
			}
			refreshSmsUiState();
		},"json");
	}
}

function verifySmsCode(){
	var smsCode = $("#j_password").val();
	if (smsCode == ""){
		$('#storeError').text("请输入短信验证码");
		$('#storeError').show();
		return false;
	}
	return true;
}
function showLogonSmsSelectionModal(){
	if (!verifySmsPhone()){
		return;
	}
	if (!verifySmsCode()){
		return;
	}
	$("#logonSmsSelectionModal").modal({backdrop: 'static'});
}
function sellerSmsLogin(){
	$("#storeError").hide();
	
	var userName = $('#j_username').val();
	var password = $('#j_password').val();
	if(userName==userholder){
		userName=='';
	}
	
	var storeCode = $('#signin_storeCode').val();
	if(userName=='' || password=='') {
		 $("#loginError").html(getLoginErrorLabel());
		 $("#loginError").show();
		 return;
	}
	$('#logonForm').submit();
}
function smsLogin(){
	$("#storeError").hide();
	
	var userName = $('#j_username').val();
	var password = $('#j_password').val();
	if(userName==userholder){
		userName=='';
	}
	
	var storeCode = $('#signin_storeCode').val();
	if(userName=='' || password=='') {
		 $("#loginError").html(getLoginErrorLabel());
		 $("#loginError").show();
		 return;
	}
	customerLogonType = "sms";
	
	$.ajax({
		 type: "POST",
		 url: getContextPath() + "/shop/customer/logon.html",
		 data: "userName=" + userName + "&password=" + password + "&storeCode=" + storeCode+"&logonType=sms",
		 cache:false,
		 dataType:'json',
		 'success': onLogonSmsSuccess
	});
	return false;
}
function onLogonSmsSuccess(response){
	$("#logonSmsSelectionModal").modal("hide");
	// 参见 CustomerLoginController 的logon()注释，了解错误码定义
	 if (response.response.status==0) {//success
		if(response.response.SHOPPING_CART!=null && response.response.SHOPPING_CART != ""){
			/** save cart in cookie **/
			var cartCode = buildCartCode(response.response.SHOPPING_CART);
			$.cookie('cart',cartCode, { expires: 1024, path:'/' });
		  
		}
	    gotoPageAfterLogin();
	}else if(response.response.status==-2){
		$("#storeError").html(getFreezeErrorLabel());
		$("#storeError").show();
	}else if(response.response.status==-101){
		$('#sellerLogonAsBuyerFailModal').modal("show");
	}else if(response.response.status==-102){ 
		//$("#storeError").html(getFreezeErrorLabel());
		//$("#storeError").show();
		$('#sellerLogonAsBuyerNotFoundModal').modal("show");
	}else if(response.response.status==-103){ 
		$('#sellerLogonAsBuyerFailModal').modal("show");
	}else { // login failed, -1
		window.clearInterval(smsResendHandler);
		$("#prompt_resend_sms").text(" ");
		changeValidateImg();
		smsResendCount = 0;
		$('#j_imgCode').val("");
		$('#j_password').val("");
		verifySmsImgCode();
		refreshSmsUiState();
		$("#storeError").html("短信验证码错误!");
		$("#storeError").show();
	}
}

function gotoPageAfterLogin(){
		//var loacalUrl = window.location.href;
		//if(loacalUrl.lastIndexOf('customLogon.html')>-1 || history.length < 1){
		//	window.location.href=getContextPath() + '/shop';  
		//}else{
		//	history.go(0);
		//}
		window.location.href=getContextPath() + '/shop';  
	}
var preLogonType = "sms";
var canSendSms = true;
var imgCodeVerified = false;
var smsResendCount = 60;
var smsResendHandler = null;

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
    	
        $('.dropdown-menu').click(function(e) {
        	e.preventDefault();
        	e.stopPropagation();
        });
        
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
        		login();
        	}
        	});

		restLoginBySmsData();
		
		var url=window.location.href;
		if(preLogonType == "sms" || url.indexOf('?t=sms')!=-1){
			showLogonMode("sms");
		}else{
			showLogonMode("password");
		}
		
    });
    
    function longinPage(){
    	window.location.href=getContextPath() + '/shop/customer/customLogon.html';
    }
    
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
        	
        	$('#login').showLoading();
        	
            $.ajax({
                 type: "POST",
                 //my version
                 url: getContextPath() + "/shop/customer/logon.html",
                 data: "userName=" + userName + "&password=" + password + "&storeCode=" + storeCode +"&type=password",
                 cache:false,
              	 dataType:'json',
                 'success': function(response) {
            
              $('#login').hideLoading();
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
                    	//$("#signinDrop").dropdown("toggle");
                    	$("#loginError").html(getFreezeErrorLabel());
                        $("#loginError").show();
                    }else if(response.response.status==-3){ 
                    	//$("#signinDrop").dropdown("toggle");
                    	$("#loginError").html("未找到用户，请先注册");
                        $("#loginError").show();
                    }else {
              
                    	//$("#signin").dropdown("toggle");
                        $("#loginError").html(getLoginErrorLabel());
                        $("#loginError").show();
                    }
				
                }
            });
            return false;
        //});
        }
    function placeholderSupport() {
        return 'placeholder' in document.createElement('input');
    }
	
	function showLogonMode(modeName){
		preLogonType = modeName;
		if (modeName=="sms"){
			$('#div_loginByPassword').hide();
			$('#div_loginBySms').show();
			$('#label_loginByPassword').removeClass('login_mode_selected');
			$('#label_loginBySms').addClass('login_mode_selected');
		}else{
			$('#div_loginBySms').hide();
			$('#div_loginByPassword').show();
			$('#label_loginByPassword').addClass('login_mode_selected');
			$('#label_loginBySms').removeClass('login_mode_selected');
		}
	}
	
	function changeValidateImg(obj){
		imgCodeVerified=false;
		setSendSmsBtnDisable(true);
		$('#btn_loginBySms').prop('disabled', true);
		if(obj==null||obj=='undefined'||obj=='') obj = '';
		var imgSrc = $("#imgObj"+obj);
		var src = imgSrc.attr("src");
		imgSrc.attr("src", chgValidateImgUrl(src));
	}

	function chgValidateImgUrl(url) {
		imgCodeVerified = false;
		var timestamp = (new Date()).valueOf();
		url = url.substring(0, 29);
		if ((url.indexOf("&") >= 0)) {
			url = url + "×tamp=" + timestamp;
		} else {
			url = url + "?timestamp=" + timestamp;
		}
		return url;
	}
	function restLoginBySmsData(){
		$('#input_imgCode').val('');
		$('#input_smsCode').val('');
		setSendSmsBtnDisable(true);
		$('#btn_loginBySms').prop('disabled', true);
	}
	function onMobileChanged(){
		var phoneNumber = $('#input_login_mobile').val();
		if (phoneNumber.length < 11){
			return;
		}
		verifySmsPhone();
	}
	function onImgCodeChange(){
		var imgCode = $('#input_imgCode').val();
		if (imgCode.length == 4){
			if (!verifySmsPhone()){
				return;
			}
		}else{
			return;
		}
		if (imgCode!='' && imgCode.length==4){
			$.post(urlVerifyImgCode,{imgCode:imgCode},function(data){
				if(data.response.status==0){//验证是否成功
					imgCodeVerified = true;
					$('#loginErrorSms').hide();
					setSendSmsBtnDisable(!canSendSms);
				}else{
					setSendSmsBtnDisable(true);
					$('#loginErrorSms').text("图形验证错误或需正确完成图形验证");
					//changeValidateImg();
					$('#loginErrorSms').show();
				}
			},"json");
		}
	}
	function setSendSmsBtnDisable(disabled){
		
		if (disabled){
			$('#btn_sendSms').prop('disabled', true);
			$('#btn_sendSms').css("backgroundColor", "gray");
			$('#btn_sendSms').css("border-color", "gray");
		}else{
			$('#btn_sendSms').prop('disabled', false);
			$('#btn_sendSms').css("backgroundColor", "#5bc0de");
			$('#btn_sendSms').css("border-color", "#46b8da");
		}
	}
	function sendSmsCode(){
		canSendSms = false;
		$.ajax({  
			type: 'POST',
				url: smsSendUrl+"?phone="+$('#input_login_mobile').val(),
				cache: false,
				dataType: 'json',
				success: function(response) {
					if(response.response.status==0) {
						smsResendCount = 30;
						smsResendHandler = window.setInterval(SetRemainTime, 1000);
						canSendSms = false;
						setSendSmsBtnDisable(true);
						if ( typeof response.response._randomNumber == "string"){
							alert("短信发送成功，验证码是:"+response.response._randomNumber);
							$("#input_smsCode").val(response.response._randomNumber);
						}else{
							alert("短信发送成功, 请查看手机获得验证码");
						}
						$('#loginErrorSms').hide();
						if (verifySmsPhone() && imgCodeVerified){
							$('#btn_loginBySms').prop('disabled', false);
						}else{
							$('#btn_loginBySms').prop('disabled', true);
						}
					}else{
						$('#loginErrorSms').text("短信发送失败，请确认你的手机号再试");
						$('#loginErrorSms').show();
					}
				} ,
				error: function( textStatus, errorThrown) {
					canSendSms = true;
					$('#loginErrorSms').text(textStatus);
					$('#loginErrorSms').show();
				}
			}
		);
	}
	function SetRemainTime(){
		smsResendCount--;
		if (smsResendCount <= 0){
			smsResendCount = 0;
			$("#prompt_resend_sms").text(" ");
			window.clearInterval(smsResendHandler);
			canSendSms = true;
			if (verifySmsPhone()){
				setSendSmsBtnDisable(false);
			}else{
				setSendSmsBtnDisable(true);
			}
		}else{
			$("#prompt_resend_sms").text(smsResendCount+"秒后可以重新发送验证码");
		}
	}
	function loginBySms(){
        $("#loginErrorSms").hide();
        	
		var mobile = $('#input_login_mobile').val();
		var smsCode = $('#input_smsCode').val();
		var storeCode = $('#sms_storeCode').val();
		
		$('#form_loginBySms').showLoading();
        	
        $.ajax({
            type: "POST",
            url: getContextPath() + "/shop/customer/logon.html",
            data: "mobile=" + mobile + "&smsCode=" + smsCode + "&storeCode=" + storeCode +"&type=sms",
            cache:false,
            dataType:'json',
            'success': function(response) {
                $('#form_loginBySms').hideLoading();
				if (response.response.status==0) {
                	if(response.response.SHOPPING_CART!=null && response.response.SHOPPING_CART != ""){
						var cartCode = buildCartCode(response.response.SHOPPING_CART);
       					$.cookie('cart',cartCode, { expires: 1024, path:'/' });
                	}
					   
					gotoPageAfterLogin();
                	                  	   
				}else if(response.response.status==-2){ 
					$("#loginErrorSms").html(getFreezeErrorLabel());
					$("#loginErrorSms").show();
				}else if(response.response.status==-3){ 
					$("#loginErrorSms").html("未找到用户，请先注册");
					$("#loginErrorSms").show();
				}else {
					$("#loginErrorSms").html("短信验证码不正确，请重新登录");
					$("#loginErrorSms").show();
				}
				
            }
        });
        return false;
	}
	function verifySmsPhone(){
		var phoneNumber = $('#input_login_mobile').val();
		var phonePtn = /^1[3-9]\d{9}$/;
		if (phoneNumber!='' && phoneNumber.length==11){
			if (phonePtn.test(phoneNumber)){
				$('#loginErrorSms').hide();
				return true;
			}
		}
		if (phoneNumber.length<11) return false;
		$('#loginErrorSms').text("请输入正确的手机号");
		$('#loginErrorSms').show();
		return false;
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
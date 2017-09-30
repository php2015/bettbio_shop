
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

function chgValidateImgStore() {
	var timestamp = (new Date()).valueOf();
	var imgSrc = $("#imgObj");
	var src = getContextPath()+'/shop/store/code.html?timestamp='+timestamp;
	imgSrc.attr("src", src);
	
}

var messageInterValObj;
var messagecount = 60; 
var curMessageCount=60;
/**
 * pass
 */
function sendPhoneNum(){
	var captchaResponseField = $("#captchaResponseField").val();
	if(captchaResponseField ==null || captchaResponseField==''){
		showErro(-2);
		return ;
	}
	
	//curMessageCount = messagecount;
    $("#btnSendCode").attr("disabled", "true");
    var _url = getContextPath() + '/shop/store/phoneCodeNum.html'+'?captchaResponseField='+ $("#captchaResponseField").val()+"&phone="+$("#phone").val();
    $.ajax({  
		type: 'POST',
		  url: _url,
		  cache: false,
		  dataType: 'json',
		  success: function(response) {
			  if(response.response.status==0) {
		    		 messageInterValObj = window.setInterval(SetRemainTime(), 1000); 
		    		 $("#messageSucces").show();
		    		 $("#messageFail"+obj).hide();
		    	 }else{
		    		 showErro(response.response.status);;
		    		 window.clearInterval(messageInterValObj);
		             $("#btnSendCode"+obj).removeAttr("disabled");
		    	 }
			 } ,
		 error: function( textStatus, errorThrown) {
			
			 $("#messageFail").show();
	    	 window.clearInterval(messageInterValObj);
	         $("#btnSendCode").removeAttr("disabled");
	         //alert(textStatus); 
		 }
		 	});

}

/**
 * pass
 */
function sendPhoneNumtwo(){
	
	//curMessageCount = messagecount;
    $("#btnSendCode").attr("disabled", "true");
    var _url = getContextPath() + '/shop/store/phoneCodeNumTwo.html'+'?phone='+phone;
    $.ajax({  
		type: 'POST',
		  url: _url,
		  cache: false,
		  dataType: 'json',
		  success: function(response) {
			  if(response.response.status==0) {
		    		 messageInterValObj = window.setInterval(SetRemainTime(), 1000); 
		    		 $("#messageSucces").show();
		    		 $("#messageFail"+obj).hide();
		    	 }else{
		    		 showErro(response.response.status);;
		    		 window.clearInterval(messageInterValObj);
		             $("#btnSendCode"+obj).removeAttr("disabled");
		    	 }
			 } ,
		 error: function( textStatus, errorThrown) {
			
			 $("#messageFail").show();
	    	 window.clearInterval(messageInterValObj);
	         $("#btnSendCode").removeAttr("disabled");
	         //alert(textStatus); 
		 }
		 	});

}
/**
 * 用户注册，获取手机验证码，手机号码从页面输入获取
 * @returns
 */
function sendPhoneMessage(obj) {
	if(obj==null||obj=='undefined'||obj=='') obj = '';
	var captchaResponseField = $("#captchaResponseField"+obj).val();
	if(captchaResponseField ==null || captchaResponseField==''){
		showErro(-2,obj);
		return ;
	}
	if(!validatePhone($("#phone"+obj).val())) {
		alert(getInvalidPhoneMessage());
		return ;
	}
  　	curMessageCount = messagecount;
     $("#btnSendCode"+obj).attr("disabled", "true");
     var _url = getContextPath() + '/shop/store/phoneCode.html'+'?captchaResponseField='+ $("#captchaResponseField"+obj).val()+"&phone="+$("#phone"+obj).val();
     if(obj!='') _url += '&flag='+obj; //标识设定变量名称
     $.ajax({  
		type: 'POST',
		  url: _url,
		  cache: false,
		  dataType: 'json',
		  success: function(response) {
			  if(response.response.status==0) {
		    		 messageInterValObj = window.setInterval(SetRemainTime(obj), 1000); 
		    		 $("#messageSucces"+obj).show();
		    		 $("#messageFail"+obj).hide();
		    	 }else{
		    		 showErro(response.response.status,obj);;
		    		 window.clearInterval(messageInterValObj);
		             $("#btnSendCode"+obj).removeAttr("disabled");
		    	 }
			 } ,
		 error: function( textStatus, errorThrown) {
			
			 $("#messageFail"+obj).show();
	    	 window.clearInterval(messageInterValObj);
	         $("#btnSendCode"+obj).removeAttr("disabled");
	        // alert(textStatus); 
		 }
		 	});
}
/**
 * 为修改密码而重新请求发送校验码，手机号码从后台用户信息获取
 * @returns
 */
function sendPhonemsgForPW() {
	var captchaResponseField = $("#captchaResponseField").val();
	if(captchaResponseField ==null || captchaResponseField==''){
		showErro(-2);
		return ;
	}
	
  　	curMessageCount = messagecount;
     $("#btnSendCode").attr("disabled", "true");
　　 	 $.ajax({  
		type: 'POST',
		  url: getContextPath() + '/shop/customer/phoneCode.html',
		  cache: false,
		  dataType: 'json',
		  success: function(response) {
			  if(response.response.status==0) {
		    		 messageInterValObj = window.setInterval(SetRemainTime, 1000); 
		    		 $("#messageSucces").show();
		    		 $("#messageFail").hide();
		    	 }else{
		    		 showErro(response.response.status);;
		    		 window.clearInterval(messageInterValObj);
		             $("#btnSendCode").removeAttr("disabled");
		    	 }
			 } ,
		 error: function( textStatus, errorThrown) {
			
			 $("#messageFail").show();
	    	 window.clearInterval(messageInterValObj);
	         $("#btnSendCode").removeAttr("disabled");
	         //alert(textStatus); 
		 }
		 	});
}
function showModal(){
	$('#pageContainer').showLoading();
	
};

function SetRemainTime(obj) {
	if(obj==null||obj=='undefined'||obj=='') obj = '';
            if (curMessageCount == 0) {                
                window.clearInterval(messageInterValObj);
                $("#messageSecond"+obj).html('');
                $("#btnSendCode"+obj).removeAttr("disabled");
            }
            else {
            	curMessageCount--;
                $("#messageSecond"+obj).html(curMessageCount);
            }
        }
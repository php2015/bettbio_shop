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
<%@page import="com.salesmanager.web.entity.customer.SecuredShopPersistableCustomer" %>
<!-- requires functions.jsp -->
<script src="<c:url value="/resources/js/jquery.maskedinput.min.js" />"></script>
<%-- <script src="<c:url value="/resources/js/shop-customer.js" />"></script> --%>
<script src="<c:url value="/resources/js/address.js" />"></script>
<script src="<c:url value="/resources/js/shopping-cart.js" />"></script>
<script src="<c:url value="/resources/js/check-functions.js" />"></script>
<script src="<c:url value="/resources/js/pwstrength.js" />"></script>
<link href="<c:url value="/resources/css/image/dropify.css" />" rel="stylesheet"></link>
<script src="<c:url value="/resources/js/tools/dropify.js" />"></script>

<script type="text/javascript">
var inputPorp = {
		project:{
			hint:"请输入您所在课题组教授/负责人的名称",
			err:"课题组不能为空"
		},
		company:{
			hint:"请输入您所在学校/公司名称",
			err:"学校/公司不能为空"
		},
		phone:{
			hint:"请输入您的手机号",
			err:"手机号格式不正确"
		},
		userName:{
			hint:"请输入您的姓名",
			err:"姓名不能为空"
		},
		email:{
			hint:"请输入您的有效邮箱",
			err:"邮箱格式不正确"
		},
		password:{
			hint:"请输入您的密码",
			hint1:"请在次输入您的密码",
			err:"支持数字与大小写字母两种符号组合,6-20个字符",
			err1:"密码格式不正确",
			err2:"两次密码不一致"
		},
		authCode:{
			hint:"请输入验证码",
			err:"验证码不正确"
		},
		phoneAuthCode:{
			hint:"请输入您的手机收到的验证码",
			err:"手机验证码不正确"
		}
}
var msg1 ="";
var msg2 ="";
var RecaptchaOptions = {
	    theme : 'clean'
};
var reg_info = <%=session.getAttribute("reg_info") %>;
$(document).ready(function() {
	initVal();
	if (reg_info != null && reg_info != '' && reg_info != 'null') {
		//alert(reg_info);
	} else {
		//$("#project").val("yo ga da11");
	}
	 var drEvent = $('.dropify-event').dropify();

    drEvent.on('dropify.beforeClear', function(event, element){
  	  var rdel=confirm('<s:message code="label.entity.remove.confirm" text="Delete"/>');
  	  if(rdel == true){
  		  //if(${product.productImage} != null){
  			  removeImage(${store.id});
  		  //}
  	  }
        return rdel;
    });
    
  //默认未校验通过
    $(".valueInput").each(function(){
    		$(this).attr("success",false);
    });
  
    //验证课题组
   $("#project").blur(function(){
	  var project_name = $.trim($("#project").val());
	  var re = /^(\w|[\u4E00-\u9FA5])*$/;
	  if(project_name!=""){
		  //if(re.test(project_name)){
		  $("#project").parent().siblings(".gogo").show();
		  $("#project_tips").css('visibility','hidden');
		  $("#project").attr("success",true);
	    //}
	  }else $(this).val(inputPorp.project.hint);
   }).focus(function(){
   	    $("#project").attr("success",false);
	   	$("#project").parent().siblings(".gogo").hide();
		if($(this).val()==inputPorp.project.hint) $(this).val("");
   	});
   
    
    //验证学校和公司
      $("#company").blur(function(){
    	  var company_name = $.trim($("#company").val());
    	  var re = /^[0-9a-zA-Z\u2E80-\u9FFF]{6,20}$/;
		  if(company_name!=""){
			  //if(re.test(company_name)){
			  $("#company").parent().siblings(".gogo").show();
			  $('#company_tips').css('visibility','hidden');
			  $("#company").attr("success",true);
		  }else $(this).val(inputPorp.company.hint);
	   }).focus(function(){
	   	    $("#company").attr("success",false);
		   	$("#company").parent().siblings(".gogo").hide();
			if($(this).val()==inputPorp.company.hint) $(this).val("");
	   });   	
    
    
      //手机号验证
      $("#phone").blur(function(){
      	var re=/^(13[0-9]|14[0-9]|15[0-9]|18[0-9])\d{8}$/i;
      	var storemobile=$.trim($(this).val());
      	if(storemobile!=''){
      		if(re.test(storemobile)){
      	         $.ajax({
      	            url:"/sm-shop/shop/customer/validtion.html",
      	            type:'post',
      	            data:{
      	              phone:$("#phone").val(),
      	              emailAddress:"null"
      	            },
      	            dataType:'json',
      	            timeout:3000,
      	            cache:true,
      	            async:true,
      	            success:function(data){
      	               if(data.response.status != '0' || data.response.status !="0" || data.response.status != 0){
      	               $("#phone_tips").css('visibility','').find("img").attr("src",getContextPath()+"/resources/img/error.png");
      	               $("#phone").parent().siblings(".gogo").hide();
      	               $("#phone_title").text("当前电话号码已被使用了").css("color","red");
      	               msg1+="v";
      	               }else{
      	            	 $("#phone").attr("success",true);
	      	      			$("#phone").parent().siblings(".gogo").show();
	      	      			$("#phone_tips").css('visibility','hidden');
      	               }
      	            }
      	         });
      		}else{
      			  $("#phone_tips").css('visibility','').find("img").attr("src",getContextPath()+"/resources/img/error.png");
      	    	  $("#phone_title").text(inputPorp.phone.err);
      		}
      	}else $(this).val(inputPorp.phone.hint),$("#phone_tips").css('visibility','hidden');
      }).focus(function(){
      	$("#phone").attr("success",false);
      	$("#phone").parent().siblings(".gogo").hide();
		if($(this).val()==inputPorp.phone.hint) $(this).val("");
      });
      
     //悬浮电话和邮箱是否已存在
/* 	 //验证手机号
	 $("#phone").blur(function(){
		var phone_name = $("#phone").val();
		var re = /^(13[0-9]|14[0-9]|15[0-9]|18[0-9])\d{8}$/;
        if(phone_name!=""){
          if(re.test(phone_name)){
         $.ajax({
            url:"/sm-shop/shop/customer/validtion.html",
            type:'post',
            data:{
              phone:$("#phone").val(),
              emailAddress:"null"
            },
            dataType:'json',
            timeout:3000,
            cache:true,
            async:true,
            success:function(data){
               if(data.response.status != '0' || data.response.status !="0" || data.response.status != 0){
                   $("#phone_title").html("当前电话号码已被使用了").css("color","red");
                   msg1+="v";
               }else{
                   msg1="";
                   //$("#phone_title").html("完成验证后，可以结合手机号，找回密码").css("color","green");
                   $("#phone").parent().siblings(".gogo").show();
         	       $('#phone_tips').css('visibility','hidden');
         	      $("#phone").attr("success",true);
               }
            }
         });
         }else{
        	 $("#phone_tips").find("img").attr("src",getContextPath()+"/resources/img/chacha.png");
	     	 $("#phone_title").text("输入格式不正确"); 
           }
          }else{
        	 $("#phone").parent().siblings(".gogo").hide();
   	         $('#phone_tips').css('visibility','');
         }
        }).focus(function(){
	   	     $("#phone").attr("success",false);
		   	  $("#phone").parent().siblings(".gogo").hide();
			   	 if($(this).val()=="请填写课题名和项目名称！"){
			   		$(this).val("");
			   	}
		   	$("#phone").find("img").attr("src",getContextPath()+"/resources/ad/icon.png");
		   	$("#phone").text("支持中文、英文、数字的组合,6-20个字符");
		   	$("#phone").css('visibility','visible'); 
        });
      */
     //验证姓名
          $("#userName").blur(function(){
        	var user_name = $.trim($("#userName").val());
      		if(user_name!=""){
				 $("#userName").attr("success",true);
			     $("#userName").parent().siblings(".gogo").show();
			     $("#userName_tips").css('visibility','hidden');
		 	}else $(this).val(inputPorp.userName.hint);  
         }).focus(function(){
        	$("#userName").attr("success",false);
         	$("#userName").parent().siblings(".gogo").hide();
    		if($(this).val()==inputPorp.userName.hint) $(this).val("");
         });
     
           //验证邮箱
           $("#email").blur(function(){
        	   var email_name = $.trim($("#email").val());
        	   var re=/[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/;
	           if(email_name!=""){
	        	  if(re.test(email_name)){
			         $.ajax({
			            url:"/sm-shop/shop/customer/validtion.html",
			            type:'post',
			            data:{
			              phone:"null",
			              emailAddress:$("#email").val()
			            },
			            dataType:'json',
			            timeout:3000,
			            cache:true,
			            async:true,
			            success:function(data){
			               if(data.response.status != '0' || data.response.status !="0" || data.response.status != 0){
		             	       $("#email_tips").css('visibility','');
			            	   $("#email_title").html("当前邮箱已被使用了").css("color","red");
			                   msg2+="v";
			               }else{
		                       msg2 ="";
		                       $("#email").parent().siblings(".gogo").show();
		             	       $("#email_tips").css('visibility','hidden');
		             	       $("#email").attr("success",true);
		                  }
			            }
			         }); 
	           }else{
	         	  $("#email_tips").css('visibility','');
	        	  $("#email_tips").find("img").attr("src",getContextPath()+"/resources/img/chacha.png");
		     	  $("#email_title").text(inputPorp.email.err);
	        	}
	        }else{
	        	$("#email").parent().siblings(".gogo").hide();
       	        $("#email_tips").css('visibility','hidden');
       	        $(this).val(inputPorp.email.hint)
	        }
	       }).focus(function(){
	        	$("#email").attr("success",false);
	         	$("#email").parent().siblings(".gogo").hide();
	    		if($(this).val()==inputPorp.email.hint) $(this).val("");
	         });	

           var pwdReg =/^(?=[0-9]+[a-z]+)|(?=[a-z]+[0-9]+)|(?=[0-9]+[A-Z]+)|(?=[A-Z]+[0-9]+)|(?=[a-z]+[A-Z]+)|(?=[A-Z]+[a-z]+).{6,20}$/;
           var numEnReg =/^[a-zA-Z0-9]{6,20}$/;
           
           //验证密码
            $("#newPassword").blur(function(){
              var new_Password = $("#newPassword").val();                           
			  if(new_Password!=""){
				  if(pwdReg.test(new_Password)&&numEnReg.test(new_Password)){
					  $("#newPassword").attr("success",true);
					  $("#newPassword").parent().siblings(".gogo").show();
					  $("#newPassword_tips").css('visibility','hidden');
				  }else{
					  $("#newPassword_tips").find("img").attr("src",getContextPath()+"/resources/img/chacha.png");
			     	  $("#newPassword_title").text(inputPorp.password.err1);
				  }
			  } else{
		    	 $("#newPassword_tips").css('visibility','hidden')
				 /*  $("#newPassword").parent().siblings(".gogo").hide();
				  $("#newPassword_tips").css('visibility',''); */
			  }
         	}).focus(function(){
		   	    $("#newPassword").attr("success",false);
			   	$("#newPassword").parent().siblings(".gogo").hide();
			   	$("#newPassword_tips").find("img").attr("src",getContextPath()+"/resources/ad/icon.png");
			   	$("#newPassword_tips").css('visibility','visible'); 
			   	$("#newPassword_title").text(inputPorp.password.err);  	
	         });	
            
           
           //验证确认密码
              $("#passwordAgain").blur(function(){
            	  var passwordAgain =  $("#passwordAgain").val();
            	  var new_Password = $("#newPassword").val();
				  if(passwordAgain!=""){
					  if(pwdReg.test(passwordAgain)&&numEnReg.test(passwordAgain)){
						  if(passwordAgain==new_Password){
							  $("#passwordAgain").attr("success",true);
							  $("#passwordAgain").parent().siblings(".gogo").show();
							  $("#passwordAgain_tips").css('visibility','hidden');
						  }else{
							  $("#passwordAgain_tips").find("img").attr("src",getContextPath()+"/resources/img/error.png");
					     	  $("#passwordAgain_title").text(inputPorp.password.err2);
						  }
				  } else{
					  $("#passwordAgain_tips").find("img").attr("src",getContextPath()+"/resources/img/error.png");
		   	    	  $("#passwordAgain_title").text(inputPorp.password.err1);
					 /*  $("#passwordAgain").parent().siblings(".gogo").hide();
					  $("#passwordAgain_tips").css('visibility',''); */
				     }
				  }else{
			    	  $("#passwordAgain_tips").css('visibility','hidden')
				  }
	         }).focus(function(){
	        	    $("#passwordAgain").parent().siblings(".success").hide();
		   	        $("#passwordAgain").attr("success",false);
			   	    $("#passwordAgain").parent().siblings(".gogo").hide();
				   	$("#passwordAgain_tips").find("img").attr("src",getContextPath()+"/resources/ad/icon.png");
					$("#passwordAgain_tips").css('visibility','visible'); 
				   	$("#passwordAgain_title").text(inputPorp.password.err);
	         });	
           
              //验证码校验
              $("#captchaResponseField").blur(function(){
              	var imgCode=$.trim($(this).val());
              	if(imgCode!=""){
              		$.post("${pageContext.request.contextPath}/shop/valitationByImgCode.html",{imgCode:imgCode},function(data){
              			if(data.response.status==0){//验证是否成功
              			  $("#captchaResponseField").attr("success",true);
              			  $("#captchaResponseField").parent().siblings(".gogo").show();
              			  $("#captchaResponseField_tips").css('visibility','hidden');
              			}else{
                			 $("#captchaResponseField_tips").css('visibility','');
              			     $("#captchaResponseField").parent().siblings(".gogo").hide();
              				 $("#captchaResponseField_tips").find("img").attr("src",getContextPath()+"/resources/img/error.png");
              	   	    	 $("#captchaResponseField_title").text("验证码不正确");
              			}
              		},"json");
              	}else{
              		$("#captchaResponseField_tips").css('visibility','hidden');
    	    		$(this).val(inputPorp.authCode.hint);
              	}
              }).focus(function(){
              	$("#captchaResponseField").attr("success",false);
              	$("#captchaResponseField_tips").find("img").attr("src",getContextPath()+"/resources/ad/icon.png");
	    		if($(this).val()==inputPorp.authCode.hint) $(this).val("");
          		$("#captchaResponseField_tips").css('visibility','hidden');
              });
           
              
              
           
          /*  //验证验证码
              $("#captchaResponseField").blur(function(){
			  if($("#captchaResponseField").val()!=""){
				  $("#captchaResponseField").parent().siblings(".gogo").show();
				  $("#captchaResponseField").attr("success",true);
			  } else{
				  
			  }
         }).focus(function(){
	   	      $("#captchaResponseField").attr("success",false);
		   	  $("#captchaResponseField").parent().siblings(".gogo").hide();
			   	 if($(this).val()=="请填写课题名和项目名称！"){
			   		$(this).val("");
			   	}
			   	$("#captchaResponseField").find("img").attr("src",getContextPath()+"/resources/ad/icon.png");
			   	$("#captchaResponseField").text("支持中文、英文、数字的组合,6-20个字符");
			   	$("#captchaResponseField").css('visibility','visible'); 
           });
            */
           
            var isSendSMSCOde=false;
            //发送手机验证码
            $("#btnSendCode").click(function(){
            	//获取手机号
            	var phone=$("#phone").val();
            	var success=$("#phone").attr("success");
            	//if(phone!=""&&success){
            	if(success!="false"){
            		$.post("${pageContext.request.contextPath}/shop/sendSMSCode.html",{phone:phone},function(data){
            			if(data.response.status==0){//验证码发送成功
            				$("#btnSendCode").attr("disabled","disabled");
            				isSendSMSCOde=true;
            				setTimeout(function(){
            				$("#btnSendCode").removeAttr("disabled");
            				},1000*59);
            				$(".alertMsg").text("验证码发送成功");
            				$("#alertDialog").modal({backdrop: 'static'});
            			}else{
            				$(".alertMsg").text("验证码发送失败");
            				$("#alertDialog").modal({backdrop: 'static'});
            			}
            		},"json");
            	}else{
        			$(".alertMsg").text("请填写手机号");
        			$("#alertDialog").modal({backdrop: 'static'});
            	}
            });
            
            
           
            //校验手机验证码
    $("#phoneValation").focus(function(){
    	$("#phoneValation").attr("success",false);
    	$("#phoneValation").parent().siblings(".gogo").hide();
    	$("#phoneValation_tips").find("img").attr("src",getContextPath()+"/resources/ad/icon.png");
    	$("#phoneValation_tips").css('visibility','visible');
    	$("#phoneValation_title").text("请输入手机验证码");
    }).blur(function(){
    	var smsCode=$(this).val();
    	if(smsCode==""){//验证码是否为空
    		$("#phoneValation_tips").find("img").attr("src",getContextPath()+"/resources/img/chacha.png");
    		$("#phoneValation_title").text("请填写手机验证码");
    		return false;
    	}
    	//判断是否发送验证码 
    	if(isSendSMSCOde==false){
      		$("#phoneValation_tips").find("img").attr("src",getContextPath()+"/resources/img/chacha.png");
    		$("#phoneValation_title").text("未发送手机验证码");
    		return false;
    	}
    	//发送校验请求
    	$.post("${pageContext.request.contextPath}/shop/valitationBySMSCode.html",{smsCode:smsCode},function(data){
    		if(data.response.status==0){//验证是否成功
    		  $("#phoneValation").attr("success",true);
  			  $("#phoneValation").parent().siblings(".gogo").show();
  			  $("#phoneValation_tips").css('visibility','hidden');
    		}else{
    		  $("#phoneValation").attr("success",false);
    		  $("#phoneValation_tips").find("img").attr("src",getContextPath()+"/resources/img/chacha.png");
        	  $("#phoneValation_title").text("验证码不正确");
  			  $("#phoneValation_tips").css('visibility','');
    		}
    	},"json")
    	});

 //验证手机验证码
/*               $("#phoneValation").blur(function(){
			  if($("#phoneValation").val()!=""){
				  $("#phoneValation").parent().siblings(".gogo").show();
				  $("#phoneValation").css('visibility','hidden');
				  $("#phoneValation").attr("success",true);
			  } else{
				  $("#phoneValation").parent().siblings(".gogo").hide();
			  }
         }).focus(function(){
	   	      $("#phoneValation").attr("success",false);
		   	  $("#phoneValation").parent().siblings(".gogo").hide();
			   	if($(this).val()=="请填写课题名和项目名称！"){
			   		$(this).val("");
			   	}
			   	$("#phoneValation").find("img").attr("src",getContextPath()+"/resources/ad/icon.png");
			   	$("#phoneValation").text("支持中文、英文、数字的组合,6-20个字符");
			   	$("#phoneValation").css('visibility','visible');
           }); */
           
              $(".valueInput").blur(function(){
            	  setTimeout(function(){
          	    	var isSubmit=isSubmitForm();
          	    	if(isSubmit){
          	    		$("#submitRegistration").removeAttr("disabled");
          	    	}else{
          	    		$("#submitRegistration").attr("disabled","disabled");
          	    	}
                  },1000)
              });
              
              //判断选中
              $("#userProtocolCheckbox").change(function(){
                  	var isSubmit=isSubmitForm();
                  	if(isSubmit){
                  		$("#submitRegistration").removeAttr("disabled");
                  	}else{
                  		$("#submitRegistration").attr("disabled","disabled");
                  	}	
              });
              //是否可以点击注册
              function isSubmitForm(){
              	var isSubmit=true;
              	$(".valueInput").each(function(){
              		if($(this).attr("success")=="false"){
              			isSubmit=false;
              		}
              	});
          		//判断同意按钮
          		if(!$("#userProtocolCheckbox").is(":checked")){
          			isSubmit=false;
          		}
              	return isSubmit;
              }
            
     //内容重置
     $("#CustomerReset").click(function(){
        $("#phone").css("backgroundColor","rgb(255, 255, 204)").val("");
        $("#userName").css("backgroundColor","rgb(255, 255, 204)").val("");
        $("#email").css("backgroundColor","rgb(255, 255, 204)").val("");
        $("#compnay").css("backgroundColor","rgb(255, 255, 204)").val("");
        $("#company").css("backgroundColor","rgb(255, 255, 204)").val("");
        $("#newPassword").css("backgroundColor","rgb(255, 255, 204)").val("");
        $("#passwordAgain").css("backgroundColor","rgb(255, 255, 204)").val("");
        $("#relationshipTel").css("backgroundColor","rgb(255, 255, 204)").val("");
        $("#captchaResponseField").css("backgroundColor","rgb(255, 255, 204)").val("");
        $("#phoneValation").css("backgroundColor","rgb(255, 255, 204)").val("");
     });
	//isFormValid();
	$("input[type='text']").on("change keyup paste", function(){
		//isFormValid();
	});
	
	$("input[type='password']").on("change keyup paste", function(){
		//isFormValid();
	}); 
	
	$("#userProtocolCheckbox").click(function(){
		//isFormValid();
	});

	$("#registration_country").change(function() {
		//isFormValid();	
	});
	
	$("#phone_title").html("完成验证后，可以结合手机号，找回密码").css("color","#666");
	$("#company_title").html("建议完善学校或公司名").css("color","#666");
});


function removeImage(obj){
	$("#store.error").show();
	$.ajax({
	  type: 'POST',
	  url: '<c:url value="/admin/store/licence/removeImage.html"/>',
	  dataType: 'json',
	  data: {id:obj},
	  success: function(response){
			var status = response.response.status;
			if(status==0 || status ==9999) {
				
				$(".alert-success").show();
				
			} else {
				//display message
				$(".alert-error").show();
			}
	  },
	  error: function(xhr, textStatus, errorThrown) {
	  	//alert('error ' + errorThrown);
	  }
	  
	});
}

function isFormValid() {
	
	if($('.alert-error').is(":visible")) {
		return true;
	}
	
	if($('.alert-success').is(":visible")) {
		return true;
	}
	var userProtol = false;
	
	if($("#userProtocolCheckbox").attr('checked')){
		userProtol =true;
	}
		
	
	 $('#registrationError').hide();//reset error message
	var msg = isCustomerFormValid($('#registrationForm'));
	
	 if(msg == null && userProtol== true){
		$('#submitRegistration').removeClass('btn-disabled');
		$('#submitRegistration').prop('disabled', false);
		$('#registrationError').hide();
		return true;
	}else{
		$('#submitRegistration').addClass('btn-disabled');
		$('#submitRegistration').prop('disabled', true);
		$('#registrationError').html(msg);
		$('#registrationError').show();
		return false;
	}  
}


 function showErro(status){
	 if(status==-2){
		 $("#messageFail").html('<s:message code="NotEmpty.contact.captchaResponseField" text="Message" />');
	 }else if(status==-3){
		 $("#messageFail").html('<s:message code="validaion.recaptcha.not.matched" text="Message" />');
		 changeValidateImg();
	 }else if(status==-4){
		 $("#messageFail").html('<s:message code="label.generic.phone.message.fail" text="Message" />');
	 }else {
		 $("#messageFail").html('<s:message code="registration.phone.already.exists" text="Message" />');
	 }
	 $("#messageFail").show();
 }
 function isCustomerFormValid(formId) {
		var $inputs = $(formId).find(':input');
		var errorMessage = null;
		$inputs.each(function() {
		var tempid = $(this).attr("id");
			if(/* $(this).hasClass('required') */true) {
				
				//console.log($(this).attr('id') + ' - ' + $(this).css('display'));

				var fieldValid = isFieldValid($(this));
				if(!fieldValid) {
					if(errorMessage==null) {
						if($(this).attr('title')) {
							errorMessage = $(this).attr('title');
							$("#"+tempid+"_title").html(""+errorMessage+"").css("color","gray");
						}
					}
				}
			}
			
			//if has class email
			if($(this).hasClass('email')) {	
				var emailValid = validateEmail($(this).val());
				//console.log('Email is valid ? ' + emailValid);
				if(!emailValid) {
					if(errorMessage==null) {
						errorMessage = getInvalidEmailMessage();
						$("#"+tempid+"_title").html(""+errorMessage+"").css("color","red");
					}
				}
			}
			
			//if has clsaa phone
			if($(this).hasClass('phone')) {	
				var phoneValid = validatePhone($(this).val());
				//console.log('Email is valid ? ' + emailValid);
				if(!phoneValid) {
					if(errorMessage==null) {
						errorMessage = getInvalidPhoneMessage();
						$("#"+tempid+"_title").html(""+errorMessage+"").css("color","red");
					}
				}
			}
			
			//user name
			if($(this).hasClass('userName')) {	
				if($(this).val().length<1) {
					if(errorMessage==null) {
						errorMessage = getInvalidUserNameMessage();
						$("#"+tempid+"_title").html(""+errorMessage+"").css("color","red");
					}
				}
			}
			
			//password rules
			if($(this).hasClass('password')) {	
				if(checkStrong($(this).val())<2) {
					if(errorMessage==null) {
						errorMessage = getWeekPasswordMessage();
						$("#"+tempid+"_title").html(""+errorMessage+"").css("color","red");
					}
				}
			}
			
			//repeat password
			if($(this).hasClass('checkPassword')) {	
				    //console.log('In check p[assword ' + + $(this).val().length)
					var pass = $('.password').val();
					if($(this).val().length<6 || ($(this).val()!=pass)) {
						if(errorMessage==null) {
							errorMessage = getInvalidCheckPasswordMessage();
							$("#"+tempid+"_title").html(""+errorMessage+"").css("color","red");
						}
					}
			}
			
			if(errorMessage == null){
				if(msg1!=""){
		            $("#phone_title").html("当前电话号码已被使用了").css("color","red");
		        } else {
		        	$("#phone_title").html("完成验证后，可以结合手机号，找回密码").css("color","green");
		        }
		        if(msg2!=""){
		            $("#email_title1").html("当前的邮箱已被注册了").css("color","green");
		        }
		        $("#"+tempid+"_title").html(""+$("#"+tempid+"_title1").html()+"").css("color","green");
			}
			
		});
		
		return errorMessage;
 }
 
 
 function isFieldValid(field) {
		if(field.is(":hidden")) {
			return true;
		}
		var value = field.val();
		if(!emptyString(value)) {
			/* if(value=='请填写课题名或项目名称'){
				return false;
			} else if(value=='请填写学校名或公司名'){
				return false;
			} else if(value=='您的账户名登录名'){
				return false;
			} else {
				field.css('background-color', '#FFF');
				return true;
			} */
			//field.css('background-color', '#FFF');
			return true;
		} else {
			field.css('background-color', '#FFF');
			return false;
		} 
}
 
 function setCountrySettings(prefix, countryCode) {
		//add masks to your country
		//console.log('Apply mask ' + countryCode);
		
		var phoneSelector = '.' + prefix + '-phone';
		var postalCodeSelector = '.' + prefix + '-postalCode';
		
		if(countryCode=='CA') {//mask for canada
			$(phoneSelector).mask("?(999) 999-9999");
			$(postalCodeSelector).mask("?*** ***");
			return;
		}
		if(countryCode=='US') {// mask for united states
			$(phoneSelector).mask("?(999) 999-9999");
			$(postalCodeSelector).mask("?99999");
			return;
		}
		
		$(phoneSelector).unmask();
		$(postalCodeSelector).unmask();

		
}
 
/*  function changeValidateImg(obj){
		if(obj==null||obj=='undefined'||obj=='') obj = '';
		var imgSrc = $("#imgObj"+obj);
		var src = imgSrc.attr("src");
		imgSrc.attr("src", chgValidateImgUrl(src));
	}
  
/**
 * 提交注册前的校验控制
 */
function checkInfo() {
	//是否确认同意注册协议
	if(!$("#userProtocolCheckbox").is(":checked")) {
		$("#agreeMsg").show();
		return false;
	} else {
		$("#agreeMsg").hide();
	}
}

//输入框提示信息
function initVal() {
	if($("#project").val()=='') $("#project").val(inputPorp.project.hint);
	if($("#company").val()=='') $("#company").val(inputPorp.company.hint);
	if($("#phone").val()=='') $("#phone").val(inputPorp.phone.hint); 
	if($("#userName").val()=='')  $("#userName").val(inputPorp.userName.hint); 
	if($("#email").val()=='') $("#email").val(inputPorp.email.hint);
	if($("#captchaResponseField").val()=='') $("#captchaResponseField").val(inputPorp.authCode.hint);
}
 
/*  function isFieldValid(field) {
		if(field.is(":hidden")) {
			return true;
		}
		var value = field.val();
		if(!emptyString(value)) {
			field.css('background-color', '#FFF');
			return true;
		} else {
			field.css('background-color', '#FFF');
			return false;
		} 
} */	
 </script>

<style>
	.head-navbar-left{
	padding-left:12%!important;
	}
.fam{
	/* font-family: '微软雅黑'; */
	font-size: 18px;
	font-weight:bolder ;
	padding-left:36px;
}
.impTip{
	color:red;
	font-size:18px;
	padding-left:10px;
}
.xuxian{
	width:100%;
	height:0;
	border-bottom:#d9d9d9 2px dashed;
	padding : 5px;
}
label{
	font-size:14px;
	font-weight:bolder  ! important;
	align : left;
	width:160px;
}

.pic{
	width:32px;
	height:32px;
}
.reg{
	font-size:16px;
	color:#fafafa;
	margin-left:-30px;
	border:none;
	width:344px;
	height:40px;
	background:url(<c:url value="/resources/ad/reg.png"/>) no-repeat;
}
.star{
	width:8px;
	height:8px;
	margin-left:-340px;
	margin-top:8px;
}
.gogo{
    display: none;
	width:22px;
	height:22px;
	margin-left:12px;
	margin-top:-26px;
}
.chacha{
   display: none;
	width:22px;
	height:22px;
	margin-left:12px;
	margin-top:-26px;
}
</style>
<c:set var="register_url" value="${pageContext.request.contextPath}/shop/customer/register.html"/>
<br>
<br>
<br>
<div>
<div style="width:55%;padding-bottom:30px;padding-left:50px;float:left">
<div class="<!-- head-navbar-left head-navbar-right row -->" style="width:100%;margin-top:10px;" >
   <form:form method="post" action="${register_url}" id="registrationForm" class="form-horizontal  change" commandName="customer" onsubmit="return checkInfo();" enctype="multipart/form-data">
	<form:errors path="*" cssClass="alert-danger alert-danger form-group text-center" element="div" />
	<!-- <fieldset> -->
<div>
<div style="margin-left:-15px;">
<img class="pic" src="<c:url value="/resources/ad/jigou.png"/>"/><span class="fam">机构信息</span>
<br/><div class="xuxian"></div><br/>
</div>
<!--课题组-->
	  <div class="form-group" style="margin-left:-30px;width:800px;">
	    <label for="project" class="col-sm-2 control-label " >课题组：</label>
	    <img class="star" src="<c:url value="/resources/ad/star.png"/>">
	    <div class="col-sm-5">
	      <form:input path="project" cssClass="project valueInput" style="width:304px;height:40px;padding-left:20px;" id="project" title="${msgProject}" maxlength="255"/>
		  <span><form:errors path="project" cssClass="error" /></span>
	    </div><br/><br/>
	    <img class="gogo"  src="<c:url value="/resources/ad/gogo.png"/>">
	    <div id="project_tips" class="col-sm-5" style="margin-top:10px;margin-left:130px;visibility: hidden;">
	      <img  src="<c:url value="/resources/ad/icon.png"/>"   width="12px" height="12px">&nbsp;&nbsp;
	      <span id="project_title" >输入课题组名有助于我们更好的服务</span>
	      <span style="display:none" id="project_title1">输入课题组名有助于我们更好的服务</span>
	    </div>
	    
	  </div>
	   <!--学校/公司-->
	  <div class="form-group" style="margin-left:-30px;width:800px;">
	    <label for="company" class="col-sm-2 control-label">学校/公司：</label>
	    <img class="star" src="<c:url value="/resources/ad/star.png"/>">
	    <div class="col-sm-5">
	     <form:input path="company" cssClass=" company  valueInput" style="width:304px;height:40px;padding-left:20px;"  id="company" title="${msgCompany}" maxlength="255"/>
		 <span><form:errors path="company" cssClass="error" /></span> 
	    </div><br/><br/>
	    <img class="gogo"  src="<c:url value="/resources/ad/gogo.png"/>">
	    <div id="company_tips" class="col-sm-5" style="margin-top:10px;margin-left:130px;visibility: hidden;">
	      <img src="<c:url value="/resources/ad/icon.png"/>"  width="12px" height="12px">&nbsp;&nbsp;
	      <span id="company_title" style="color:#666">建议完善学校或公司名</span>
	      <span style="display:none" id="company_title1">建议完善学校或公司名</span>
	    </div>
	    
	  </div>
</div>
<div>
<div style="margin-left:-15px;">
<img class="pic"  src="<c:url value="/resources/ad/geren.png"/>"/><span class="fam">个人信息</span>
<br/><div class="xuxian"></div><br/>
</div>
<!-- 手机号 -->
	  <div class="form-group" style="margin-left:-30px;width:800px;">
	    <label for="phone" class="col-sm-2 control-label">手机号：</label>
	    <img class="star" src="<c:url value="/resources/ad/star.png"/>">
	    <div class="col-sm-5">
	      <form:input  path="phone" cssClass=" phone valueInput" style="width:304px;height:40px;padding-left:20px;"  name="phone" id="phone" title="${msgPhone}"  
	      	 maxlength="11"/>
		  <span><form:errors path="phone" cssClass="error" /></span>
	    </div><br/><br/>
	    <img class="gogo"  src="<c:url value="/resources/ad/gogo.png"/>">
	    <div id="phone_tips" class="col-sm-5" style="margin-top:10px;margin-left:130px;visibility: hidden;">
	      <img src=""  width="12px" height="12px">&nbsp;&nbsp;
	      	<span id="phone_title"></span>
	    </div>
	    
	  </div>
	  <!-- 姓名 -->
	  <div class="form-group" style="margin-left:-30px;width:800px;">
	    <label for="inputEmail3" class="col-sm-2 control-label">姓名：</label>
	    <img class="star" src="<c:url value="/resources/ad/star.png"/>">
	    <div  class="col-sm-5">
							<form:input path="userName" cssClass=" userName valueInput"
								style="width:304px;height:40px;padding-left:20px;" id="userName"
								title="${msgUserName}"
								 maxlength="96" />
							<span><form:errors path="userName" cssClass="error" /></span>
	    </div> <br/><br/>
	   <div id="userName_tips" class="col-sm-5" style="margin-top:10px;margin-left:130px;visibility: hidden;">
	      <img src="<c:url value="/resources/ad/icon.png"/>"  width="12px" height="12px">&nbsp;&nbsp;
	      <span id="userName_title">支持中文、英文、数字的组合</span>
	      <span style="display:none" id="userName_title1">支持中文、英文、数字的组合</span>
	    </div> 
	    <img class="gogo"  src="<c:url value="/resources/ad/gogo.png"/>">
	  </div>
	  <!-- 邮箱 -->
	  <div class="form-group" style="margin-left:-30px;width:800px;">
	    <label for="emailAddress" class="col-sm-2 control-label">邮箱：</label>
	    <img class="star" src="<c:url value="/resources/ad/star.png"/>">
	    <div class="col-sm-5">
	      <form:input path="emailAddress" cssClass=" email  valueInput"  style="width:304px;height:40px;padding-left:20px;"  name="emailAddress" id="email" title="${msgEmail}" 
	      	 maxlength="96"/>
		  <span><form:errors path="emailAddress" cssClass="error" /></span>
	    </div><br/><br/>
	    <div id="email_tips" class="col-sm-5" style="margin-top:10px;margin-left:130px;visibility: hidden;">
	      <img src="<c:url value="/resources/ad/icon.png"/>"  width="12px" height="12px">&nbsp;&nbsp;
	      	<span id="email_title">完成验证后，可以通过邮箱找回密码</span>
	      	<span style="display:none" id="email_title1">完成验证后，可以通过邮箱找回密码</span>
	    </div>
	    <img class="gogo"  src="<c:url value="/resources/ad/gogo.png"/>">
	  </div>
	   
	   <!--密码-->
	  <div class="form-group" style="margin-left:-30px;width:800px;">
	    <label for="password" class="col-sm-2 control-label">密码：</label>
	    <img class="star" src="<c:url value="/resources/ad/star.png"/>">
	    <div class="col-sm-5">
	     <form:password path="password" class=" password  valueInput" style="width:304px;height:40px;padding-left:20px;"  id="newPassword" title="${msgPassword}" 
	     	 onclick="javascript:$('#newPassword_tips').fadeIn('fast');" onkeyup="pwStrength(this.value);" maxlength="50"/>
		 <span><form:errors path="password" cssClass="error" /></span>
	    </div><br/><br/>
	     <img class="gogo"  src="<c:url value="/resources/ad/gogo.png"/>">
	    <div id="newPassword_tips" class="col-sm-7" style="margin-top:10px;margin-left:130px;visibility: hidden;">
	      <img src="<c:url value="/resources/ad/icon.png"/>"  width="12px" height="12px">&nbsp;&nbsp;
	      	<span id="newPassword_title"></span>
	      	<span style="display:none" id="newPassword_title1"></span>
	    </div>
	    
	  </div>
	 
	   <!--确认密码-->
	  <div class="form-group" style="margin-left:-30px;width:800px;">
	    <label for="passwordAgain" class="col-sm-2 control-label">确认密码：</label>
	    <img class="star" src="<c:url value="/resources/ad/star.png"/>">
	    <div class="col-sm-5">
	      <form:password path="checkPassword" class=" checkPassword  valueInput" style="width:304px;height:40px;padding-left:20px;"  id="passwordAgain" title="${msgRepeatPassword}" 
	      	onclick="javascript:$('#passwordAgain_tips').fadeIn('fast');" onkeyup="repeatPwd(this.value);" maxlength="50"/>
		<span><form:errors path="checkPassword" cssClass="error" /></span>
	    </div><br/><br/>
	    <div id="passwordAgain_tips"  class="col-sm-5" style="margin-top:10px;margin-left:130px;visibility: hidden;">
	      <img src="<c:url value="/resources/ad/icon.png"/>"  width="12px" height="12px">&nbsp;&nbsp;
	      <span id="passwordAgain_title"></span>
	      <span style="display:none;" id="passwordAgain_title1"></span>
	    </div>
	    <img class="gogo"  src="<c:url value="/resources/ad/gogo.png"/>">
	  </div>
	  
	   <!--验证码-->
	  <div class="form-group" style="margin-left:-30px;width:800px;">
	    <label for="captchaResponseField" class="col-sm-2 control-label">验证码：</label>
	    <img style="width:8px;height:8px;margin-left:-465px;margin-top:8px;" src="<c:url value="/resources/ad/star.png"/>">
	    <div class="col-sm-5" >
	     <input type="text" Class=" company  valueInput" style="width:212px;height:40px;padding-left:20px;"  id="captchaResponseField" name="captchaResponseField" title="${validatecodeName}"/>
	    </div>
	    <div  class="col-sm-6 pull-center" style="width:124px !important;">
	    	<a style="cursor: pointer;" onclick="changeValidateImg()">
	    		<img id="imgObj" src="<c:url value="/shop/store/code.html"/>" style="width:90px;height:40px;margin-left:-121px;"/>&nbsp;&nbsp;
	    			<div style="margin-left:-25px;margin-top:-22px;width:124px;">
	    				<s:message code="label.generic.change.validatecode" text="Change"/>
	    			</div>
	    	</a>
	    </div>
	    <img class="gogo"  src="<c:url value="/resources/ad/gogo.png"/>"  style="margin-left:415px;margin-bottom:-40px;"/>
	    <div id="captchaResponseField_tips" class="col-sm-5"  style="margin-top: 10px;margin-left: 130px; visibility: hidden;">
	      <img src="" height="15" width="15">&nbsp;&nbsp;
	      <span id="captchaResponseField_title"></span>
	    </div>
	  </div>
	 
	   <!--手机验证码-->
	  <div class="form-group" style="margin-left:-30px;width:800px;">
	    <label for="checkPhonecode" class="col-sm-2 control-label">手机验证码：</label>
	    <img class="star" src="<c:url value="/resources/ad/star.png"/>">
	    <div class="col-sm-3">
	     <input type="text" Class=" company  valueInput" style="width:175px;height:40px;padding-left:20px;"  name="checkPhonecode" id="phoneValation" title="${validatecodeName}"/>
	     <div id="messageFail" style="color:red"></div>
	    </div>
	    <div  class="col-sm-2">
	      <input id="btnSendCode" type="button" style="width:130px;height:40px;margin-left:-25px;" class="btn btn-info btn-block" value="<s:message code="label.generic.phone.message" text="Phone"/>"  />
	    </div>
	   <img class="gogo"  src="<c:url value="/resources/ad/gogo.png"/>"   style="margin-left:365px;margin-bottom:-40px;"/>
	   <div id="phoneValation_tips" class="col-sm-5 message" style="margin-top:10px;margin-left:130px;visibility: hidden;">
	    <img src="" height="15" width="15" >&nbsp;&nbsp;
	      <span id="phoneValation_title">建议完善公司联络方式</span>
	    </div>
	  </div>
	  <!-- 注册 -->
	  <div class="form-group" style="margin-left:-30px;width:800px;">
	  <div class="col-sm-5" style="padding-left:145px;width:800px;">
	     <input type="checkbox" id="userProtocolCheckbox" checked="checked"/>
	     	<div style="margin-top:3px;">&nbsp;&nbsp;&nbsp;阅读和同意	
	     		<a href="protocol.html">《百图注册协议》</a>
	     	</div>
	    </div>
       	 <div class="col-sm-2 col-sm-offset-2" style="padding-top: 20px;">
	      <input type="submit" name="register" class="reg" id="submitRegistration" value="立即注册"  disabled="disabled" >
	    </div>
       </div>
       
	     
	    <!-- <div class="col-sm-2">
	      <input class="btn btn-info btn-block" type="button" value="重置" id="CustomerReset">
	    </div> -->
</div>


	  
       <!-- </fieldset> -->
	</form:form>
   </div></div>
<div style="float:left;width:35%;padding-top:12px;">
	
	<div role="disteic"  style="padding-top:35px;padding-left:40px;">
	       <a style="width:200px;" href="<c:url value="customLogon.html"/>" class="fam">已有账号，请登录</a>
	</div>
	<img alt="二维码"  style="width:320px;height:528px;margin-top:120px;" src="<c:url value="/resources/ad/erweima.png"/>">
</div>
</div>
<div style="clear:both"></div>
<div class="modal fade" id="alertDialog" tabindex="-1" role="dialog" aria-labelledby="myAlertModalLabel">
			  <div class="modal-dialog modal-sm" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <h4 class="modal-title" id="myAlertModalLabel">提示</h4>
			      </div>
			      <div class="modal-body">
			      	   <span class="alertMsg"></span>
			      </div>
			      <div class="modal-footer">
			        <button type="button"  class="btn btn-primary" data-dismiss="modal">确认</button>
			      </div>
			    </div>
			  </div>
</div>

<!--close .container "main-content" -->
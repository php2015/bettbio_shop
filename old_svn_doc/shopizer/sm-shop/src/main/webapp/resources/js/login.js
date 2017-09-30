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
        		login();
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
        	
        	$('#login').showLoading();
        	/**
        	 * Ajax
        	 * */
            $.ajax({
                 type: "POST",
                 //my version
                 url: getContextPath() + "/shop/customer/logon.html",
                 data: "userName=" + userName + "&password=" + password + "&storeCode=" + storeCode,
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
                	   var loacalUrl = window.location.href;
                	   if(loacalUrl.lastIndexOf('customLogon.html')>-1){
                		   window.location.href=getContextPath() + '/shop';  
                	   }else{
                		   history.go(0);
                	   }
                	                  	   
                    }else if(response.response.status==-2){ 
                    	//$("#signinDrop").dropdown("toggle");
                    	$("#loginError").html(getFreezeErrorLabel());
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
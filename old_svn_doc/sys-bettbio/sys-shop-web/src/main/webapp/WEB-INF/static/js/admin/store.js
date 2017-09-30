$(function(){
	var verdict={
			errInput : function(_this){
				_this.closest('.form-group').addClass('has-warning');
				_this.closest('.form-group').find('.text-warning').removeClass('hide');
				_this.attr("success","false");
			},
			sucInput : function(_this){
				_this.closest('.form-group').removeClass('has-warning');
				_this.closest('.form-group').find('.text-warning').addClass('hide');
				_this.attr("success","true");
			}
	}
	
	$("#name").blur(function(){
		var _val = $.trim($(this).val());
		if(_val == ""){
			verdict.errInput($(this));
		}else{
			verdict.sucInput($(this));
		}
	})
	
	//座机校验
	$("#landline").blur(function(){
		var landline = $("#landline").val();
		var re=/^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$/;
		_this = $(this);
		if(landline != ""){
			if(!re.test(landline)){
				verdict.errInput(_this);
			}else{
				verdict.sucInput(_this);
			}
		}else{
			verdict.sucInput(_this);
		}
	});
	//手机校验
	$("#phone").blur(function(){
		var phone = $("#phone").val();
		var re=/^(13[0-9]|14[0-9]|15[0-9]|18[0-9])\d{8}$/i;
		_this = $(this);
		if(phone != ""){
			if(!re.test(phone)){
				verdict.errInput(_this);
			}else{
				verdict.sucInput(_this);
			}
		}else{
			verdict.sucInput(_this);
		}
	});
	
	$("#check_isSeedUser").click(function(){
		if($("input[name='check_isSeedUser']").is(':checked')){
			$("#isSeedUser").val("0");
		}else{
			$("#isSeedUser").val("1");
		}
	})
	$("#createForm").submit(function(){
		var isSubmit=true;
      	$(".valueInput").each(function(){
      		if($(this).attr("success")=="false"){
      			verdict.errInput($(this));
      			isSubmit = false;
      		}
      	})
      	return isSubmit;
	})
	
	$("#updateForm").submit(function(){
		var isSubmit=true;
      	$(".valueInput").each(function(){
      		if($(this).attr("success")=="false"){
      			verdict.errInput($(this));
      			isSubmit = false;
      		}
      	})
      	return isSubmit;
	})
	
	
})
function validphone(phone){
	if(phone==null || phone=='' || phone.length==0){
		$('#bettSumbit').removeClass('btn-disabled');
		$('#bettSumbit').prop('disabled', false);
		return;
	}
	var isPhone = /^([0-9]{3,4}-)?[0-9]{7,8}$/;
	if(!isPhone.test(phone)){
		alert("请输入正确格式的电话号码");
		$('#bettSumbit').addClass('btn-disabled');
		$('#bettSumbit').prop('disabled', true);
	}else{
		$('#bettSumbit').removeClass('btn-disabled');
		$('#bettSumbit').prop('disabled', false);
	}	
}

function validmobile(mobile){
	if(mobile==null ||mobile=='' || mobile.length==0){
		$('#bettSumbit').removeClass('btn-disabled');
		$('#bettSumbit').prop('disabled', false);
		return;
	}
	var isMob=/^((\+?86)|(\(\+86\)))?(13[012356789][0-9]{8}|15[012356789][0-9]{8}|18[02356789][0-9]{8}|147[0-9]{8}|1349[0-9]{7})$/;
	if(!isMob.test(mobile)){
		alert("请输入正确格式的手机号码");
		$('#bettSumbit').addClass('btn-disabled');
		$('#bettSumbit').prop('disabled', true);
	}else{
		$('#bettSumbit').removeClass('btn-disabled');
		$('#bettSumbit').prop('disabled', false);
	}
}

function validEmail(email){
	if(email==null ||email=='' || email.length==0){
		$('#bettSumbit').removeClass('btn-disabled');
		$('#bettSumbit').prop('disabled', false);
		return;
	}
	var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
	  if ( $email.length > 0 && emailReg.test(email)) {
		  $('#bettSumbit').removeClass('btn-disabled');
			$('#bettSumbit').prop('disabled', false);
	  } else {
		  alert("请输入正确格式的电子邮件");
			$('#bettSumbit').addClass('btn-disabled');
			$('#bettSumbit').prop('disabled', true);
	  }
}
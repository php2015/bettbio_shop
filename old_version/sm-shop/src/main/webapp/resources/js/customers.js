function aCustmoer(code,url){	
	$('#CustmoersTable').showLoading();
	$.ajax({  
		type: 'POST',
		  url: url,
		  data:"customerId="+ code,
		  success: function(response) {
			  if(response.response.status>0) {
				  $("#custmoer_name_"+code).removeAttr("style");
				  setActived(code,response.response.status);
			  }else {
				  activeFaild();
			  }
			  $('#CustmoersTable').hideLoading();
			 } ,
		 error: function( textStatus, errorThrown) {
			$('#CustmoersTable').hideLoading();
			alertSuccess(); 
			 
		 }
		 	});
};
function resetPassword(code){	
	$('#CustmoersTable').showLoading();
	$.ajax({  
		type: 'POST',
		  url: "resetPassword.html",
		  data:"customerId="+ code,
		  success: function(response) {
			  if(response.response.status>=0) {
				  $("#custmoer_name_"+code).removeAttr("style");
				  alertSuccess();
			  }else {
				  activeFaild();
			  }
			  $('#CustmoersTable').hideLoading();
			 } ,
		 error: function( textStatus, errorThrown) {
			$('#CustmoersTable').hideLoading();			 
			activeFaild();
		 }
		 	});
};
function showGrade(custmoerid,custmoergrade){
	var cuurentGrade=getUserGrade();
	for(var i =0; i<custmoergrade;i++){
		cuurentGrade= cuurentGrade+getUserGrade();
	}
	$("#cuurentGrade").html(cuurentGrade);
	setUserGradeSubmit(custmoerid,custmoergrade);
	$('#setGrade').modal('show');
}

function setGrade(custmoerid,custmoergrade){
	$('#setGrade').modal('hide');
	$('#CustmoersTable').showLoading();
	$.ajax({  
		type: 'POST',
		  url: "setGrade.html",
		  data:"customerId="+ custmoerid+"&garde="+custmoergrade,
		  success: function(response) {
			  if(response.response.status>=0) {
				  alertSuccess();
				  var cuurentGrade=getUserGrade();
					for(var i =0; i<custmoergrade;i++){
						cuurentGrade= cuurentGrade+getUserGrade();
					}
					$("#custmoer_Grade_"+custmoerid).html(cuurentGrade);
					showCustmoerGrade(custmoerid,custmoergrade);
			  }else {
				  activeFaild();
			  }
			  $('#CustmoersTable').hideLoading();
			 } ,
		 error: function( textStatus, errorThrown) {
			$('#CustmoersTable').hideLoading();			 
			activeFaild();
		 }
		 	});
}
function batchResetPasswor(){
	 var i =0;
	 var items = new Array();
  	  $(".everycheckbox:checked").each(function(){
  		items[i]=$(this).attr("id");
  				  i++;
  	});
  	  if(i==0) return ;
  	$.ajax({  
		type: 'POST',
		  url: 'batchResetPassword.html',
		  data:"itmes="+ items,
		  success: function(response) {
			  $(".everyTR").each(function(){
				  $(this).removeAttr("style");

			});
			  
			  if(response.response.status==0) {
				  if(response.response.erronames !=null &&　response.response.erronames　!=""){
					  alertPartSuccess(response.response.erronames);
				  }else{
					  alertSuccess();  
				  }
			  }else {
				  activeFaild();
			  }
			  $('#CustmoersTable').hideLoading();
			 } ,
		 error: function( textStatus, errorThrown) {
			$('#CustmoersTable').hideLoading();
			alertSuccess(); 
			 
		 }
		 	});
  	  
}
function batchActive(url,type){
	 var i =0;
	 var items = new Array();
 	  $(".everycheckbox:checked").each(function(){
 		items[i]=$(this).attr("id");
 				  i++;
 	});
 	  if(i==0) return ;
 	$.ajax({  
		type: 'POST',
		  url: url,
		  data:"itmes="+ items,
		  success: function(response) {
			  $(".everyTR").each(function(){
				  $(this).removeAttr("style");

			});
			  
			  if(response.response.status==0) {
				  if(response.response.erronames !=null &&　response.response.erronames　!=""){
					  alertPartSuccess(response.response.erronames);
				  }else{
					  alertSuccess();  
				  }
				  if(response.response.succesname !=null &&　response.response.succesname　!=""){
					  var strs=response.response.succesname.split(",");
					  for (i=0;i<strs.length ;i++ ) 
					  { 
						  setActived(strs[0],type);
					  }
				  }
				 
			  }else {
				  activeFaild();
			  }
			  $('#CustmoersTable').hideLoading();
			 } ,
		 error: function( textStatus, errorThrown) {
			$('#CustmoersTable').hideLoading();
			alertSuccess(); 
			 
		 }
		 	});
 	  
}

function doAction(page){	
	$("#page").val(page) 
	$('#ezybioForm').submit();
	
};
function findByStatus(status){	
	$("#anonymous").val(status) 
	$('#ezybioForm').submit();
	
};
/**
function bulidCustmoersList(custmers) {
		var table = document.getElementById("CustmoersTable");
		
		if(table.getElementsByTagName('tbody')[0] != null){
			table.removeChild(table.getElementsByTagName('tbody')[0]);
		}
		var tbody = document.createElement("tbody");
		for (var i = 0; i < custmers.length; i++) {
			var tr = tbody.insertRow(i) ;
			setcellcell(tr,custmers[i]);
		}
		
		if(tbody !=null){			
			table.appendChild(tbody); 
		}
		
}

function setcellcell(tr,data){
	tr.id=data.id;
	tr.insertCell(0).innerHTML=data.nick;
	tr.insertCell(1).innerHTML=data.compnay;
	tr.insertCell(2).innerHTML=data.project;
	tr.insertCell(3).innerHTML=data.phone;
	tr.insertCell(4).innerHTML=data.emailAddress;
	tr.insertCell(5).innerHTML=data.grade;
	tr.insertCell(6).innerHTML=data.gender;
	return tr;
}
*/

function showSetRate(customerNick, cId, oldRate){
	var curRate = oldRate;
	if (oldRate == null){
		curRate = "无";
	}
	$("#curentNickName").text(customerNick);
	$("#input_currentCustomerNick").val(customerNick);
	$("#curentDiscountRate").html(curRate);
	$('#input_newDiscountRate').val(curRate);
	$('#input_currentCustomerId').val(cId);
	$('#setDiscountRate').modal("show");
}

function setNewDiscountRate(){
	var newRate = $('#input_newDiscountRate').val();
	newRate = parseFloat(newRate);
	if (isNaN(newRate)){
		alert("折扣率必须是0.0~1.0的数字");
		return;
	}
	if (newRate<0 || newRate>1){
		alert("折扣率必须是0.0~1.0的数字");
		return;
	}
	if (!confirm("用户的折扣率将被设置为"+newRate+", 你确定要更改么？")){
		return;
	}
	var custmoerid=$('#input_currentCustomerId').val();
	var customerNick=$("#input_currentCustomerNick").val();
	$('#setDiscountRate').modal('hide');
	$('#CustmoersTable').showLoading();
	$.ajax({  
		type: 'POST',
		  url: "setRate.html",
		  data:"customerId="+ custmoerid+"&rate="+newRate,
		  success: function(response) {
			  if(response.response.status>=0) {
				  alertSuccess();
				  $("#custmoer_discountRate_"+custmoerid).html('<a href="#" onclick="showSetRate(\''+customerNick+'\','+custmoerid+','+newRate+')">'+newRate+'</a>');
			  }else {
				  activeFaild();
			  }
			  $('#CustmoersTable').hideLoading();
			 } ,
		 error: function( textStatus, errorThrown) {
			$('#CustmoersTable').hideLoading();			 
			activeFaild();
		 }
		});
}
function removeUserDiscountRate(){
	if (!confirm("你确定要删除用户的折扣么？")){
		return;
	}
	var custmoerid=$('#input_currentCustomerId').val();
	var customerNick=$("#input_currentCustomerNick").val();
	$('#setDiscountRate').modal('hide');
	$('#CustmoersTable').showLoading();
	$.ajax({  
		type: 'POST',
		  url: "setRate.html",
		  data:"customerId="+ custmoerid,
		  success: function(response) {
			  if(response.response.status>=0) {
				  alertSuccess();
				  $("#custmoer_discountRate_"+custmoerid).html('<a href="#" onclick="showSetRate(\''+customerNick+'\','+custmoerid+',null)">无</a>');
			  }else {
				  activeFaild();
			  }
			  $('#CustmoersTable').hideLoading();
			 } ,
		 error: function( textStatus, errorThrown) {
			$('#CustmoersTable').hideLoading();			 
			activeFaild();
		 }
		});
}

function whenUserSegmentsOnFocus(){
	$("#input_accountState_C_4accountType").prop("checked","checked");
}
function setAccountTypeChecked(accountType){
	$('#select_accountType').val(accountType);
}
function setAccountStateChecked(accountState){
	$('input[name="input_accountState"][value="' + accountState +'"]').prop("checked","checked");
}
function showSetAccountType(customerId, customerNick, accountState, accountType){
	$('#input_currentCustomerId_4accountType').val(customerId);
	$('#input_currentCustomerNick_4accountType').val(customerNick);
	$('#curentAccountName').text(customerNick);
	var typeName = getAccountTypeDisplayName(accountState, accountType);
	$('#curentAccountType').text(typeName);
	setAccountTypeChecked(accountType);
	setAccountStateChecked(accountState);
	$('#setAccountType').modal("show");
}
function setNewAccountType(){
	var newAccountState = $("input[name='input_accountState']:checked").val();
	var newAccountType = $('#select_accountType').val();
	var newAccountDisplayName = getAccountTypeDisplayName(newAccountState, newAccountType);
	if (!confirm("用户"+$('#input_currentCustomerNick_4accountType').val()+"的账户类型将会被设置为 "+newAccountDisplayName+". 确认操作么？")){
		return;
	}
	var cId= $('#input_currentCustomerId_4accountType').val();
	$('#setAccountType').modal('hide');
	$('#CustmoersTable').showLoading();
	$.ajax({  
		type: 'POST',
		  url: "setAccountType.html",
		  data:"customerId="+ cId+"&accountState="+newAccountState+"&accountType="+newAccountType,
		  success: function(response) {
			  if(response.response.status>=0) { 
				  var newContent = "<a href='javascript:void(0);' onclick='showSetAccountType(";
				  newContent+= '"' + cId + '","' + $('#input_currentCustomerNick_4accountType').val() + '","' +newAccountState + '","' + newAccountType + '"';
				  newContent+= ")'>" + newAccountDisplayName +"</a>";
				  $('#custmoer_accountType_'+cId).html(newContent);
				  alertSuccess();
			  }else {
				  activeFaild();
			  }
			  $('#CustmoersTable').hideLoading();
			 } ,
		 error: function( textStatus, errorThrown) {
			$('#CustmoersTable').hideLoading();			 
			activeFaild();
		 }
		});
}

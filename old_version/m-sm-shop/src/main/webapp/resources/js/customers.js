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








function delinvoice(code,type){
	var url = '/shop/customer/removeInvoice.html';
	if(type==1){
		url = '/shop/customer/defaultInvoice.html';
	}
	$('#pageContainer').showLoading();
	$.ajax({  
		type: 'POST',
		  url: getContextPath() + url,
		  data: "id="+ code,
		  success: function(defaultdata) {
			  if(defaultdata!=null && defaultdata.othersInvoices != null && defaultdata.othersInvoices.length>0){
				  invoiceslist(defaultdata);				  
			  }else{
				  location.reload();
				  $('#pageContainer').hideLoading();
				  $('#addresstotal').text(10);
			  }
			 
			 } ,
		 error: function( textStatus, errorThrown) {
			$('#pageContainer').hideLoading();
			//alert(textStatus); 
			 
		 }
		 	});
};  
function updateaddress(code){	
	$('#pageContainer').showLoading();
	$.ajax({  
		type: 'POST',
		  url: getContextPath() +'/shop/customer/updateAddress.html',
		  data: "id="+ code,
		  success: function(defaultdata) {
			  if(defaultdata!=null ){
				 var addressform = document.getElementById("ezybioForm");
				 if(defaultdata.name != null && defaultdata.name !="") addressform.elements['name'].value= defaultdata.name;
				 if(defaultdata.streetAdress != null && defaultdata.streetAdress !="") addressform.elements['streetAdress'].value= defaultdata.streetAdress;
				 if(defaultdata.city != null && defaultdata.city !="") addressform.elements['city'].value= defaultdata.city;
				 if(defaultdata.company != null && defaultdata.company !="") addressform.elements['company'].value= defaultdata.company;
				 if(defaultdata.postCode != null && defaultdata.postCode !="") addressform.elements['postCode'].value= defaultdata.postCode;
				 if(defaultdata.state != null && defaultdata.state !="") addressform.elements['state'].value= defaultdata.state;
				 if(defaultdata.telephone != null && defaultdata.telephone !="") addressform.elements['telephone'].value= defaultdata.telephone;
				 if(defaultdata.zone != null && defaultdata.zone !="") addressform.elements['zone'].value= defaultdata.zoneCode;
				 if(defaultdata.memo != null && defaultdata.memo !="") addressform.elements['memo'].value= defaultdata.memo;
				 addressform.elements['id'].value= defaultdata.id;
				 $('#addnew').modal('show');
				 isFormValid();
			  }
			  $('#pageContainer').hideLoading();	
			 } ,
		 error: function( textStatus, errorThrown) {
			$('#pageContainer').hideLoading();
			//alert(textStatus); 
		 }
		 	});
};  

function updateinvoice(code){	
	$('#pageContainer').showLoading();
	$.ajax({  
		type: 'POST',
		  url: getContextPath() +'/shop/customer/updateInvoice.html',
		  data: "id="+ code,
		  success: function(defaultdata) {
			  if(defaultdata!=null ){
				 var addressform = document.getElementById("changeInvoiceForm");
				 if(defaultdata.type != null ){
					 addressform.elements['type'].value= defaultdata.type;
					 if(defaultdata.type == 0){
						 $("#special").hide();
						 $("#normal").show();
						 if(defaultdata.company != null && defaultdata.company !="")  $("#ncompanyname").val(defaultdata.company);
					 }else{
						 $("#special").show();
						 $("#normal").hide();
						 if(defaultdata.company != null && defaultdata.company !="")  $("#scompanyname").val(defaultdata.company);
						 if(defaultdata.companyAddress != null && defaultdata.companyAddress !="") addressform.elements['companyAddress'].value= defaultdata.companyAddress;
						 if(defaultdata.companyTelephone != null && defaultdata.companyTelephone != "") addressform.elements['companyTelephone'].value= defaultdata.companyTelephone;
						 if(defaultdata.bankName != null && defaultdata.bankName != "") addressform.elements['bankName'].value= defaultdata.bankName;
						 if(defaultdata.bankAccount != null && defaultdata.bankAccount != "") addressform.elements['bankAccount'].value= defaultdata.bankAccount;
						 if(defaultdata.taxpayerNumber != null && defaultdata.taxpayerNumber != "") addressform.elements['taxpayerNumber'].value= defaultdata.taxpayerNumber;
						 if(defaultdata.memo != null && defaultdata.memo != "") addressform.elements['memo'].value= defaultdata.memo;
					 }
					 cname=defaultdata.company;
					 addressform.elements['id'].value= defaultdata.id;
				 }
				
				 $('#addnewinvoice').modal('show');
				 isInvoiceFormValid();
			  }
			  $('#pageContainer').hideLoading();	
			 } ,
		 error: function( textStatus, errorThrown) {
			$('#pageContainer').hideLoading();
			//alert(textStatus); 
		 }
		 	});
};  
function deleteObj(code,type){
	$("#confirmHref").unbind();
	if(type==0){
		 $("#confirmHref").bind("click",function(){
			 setinvoice(code,0);
//			 window.location.href=+"/sm-shop/shop/customer/billing.html";
		 });
	}else{
		$("#confirmHref").bind("click",function(){
			delinvoice(code,0);
		 });
	}
	$('#delcfmModel').modal('show');
}

function setinvoice(code,type){	
	var url = '/shop/customer/removeAddress.html';
	if(type==1){
		url = '/shop/customer/defaultAddress.html';
	}else if(type ==2 ){
		url = '/shop/customer/defaultInvoiceAddress.html';
	}
	$('#pageContainer').showLoading();
	$.ajax({  
		type: 'POST',
		  url: getContextPath() + url,
		  data: "id="+ code,
		  dataType:'text',
		  success: function(defaultdata) {
			    window.location.href="/sm-shop/shop/customer/billing.html";
			 } ,
		 error: function( textStatus, errorThrown) {
			$('#pageContainer').hideLoading();
			//alert(textStatus); 
			 
		 }
		 	});
};  

function addnewinvoice(){	
	$('#pageContainer').showLoading();
	$.ajax({  
		type: 'POST',
		  url: getContextPath() + '/shop/customer/allinvoices.html',
		  data:$("#changeInvoiceForm").serialize(),
		  success: function(defaultdata) {
			  invoiceslist(defaultdata);
			  document.getElementById("changeInvoiceForm").reset();
			 } ,
		 error: function( textStatus, errorThrown) {
			$('#pageContainer').hideLoading();
			//alert(errorThrown); 
			 
		 }
		 	});
} 
function addnewaddres(){	
	$('#pageContainer').showLoading();
	$.ajax({  
		type: 'POST',
		  url: getContextPath() + '/shop/customer/alladdress.html',
		  data:$("#ezybioForm").serialize(),
		  success: function(defaultdata) {
			 addresslist(defaultdata);
			  document.getElementById("ezybioForm").reset();
			 } ,
		 error: function( textStatus, errorThrown) {
			$('#pageContainer').hideLoading();
			//alert(textStatus); 
			 
		 }
		 	});
}
function cleartbody(tableid){
	var table = document.getElementById(tableid);
	if(table.getElementsByTagName('tbody')[0] != null){
		table.removeChild(table.getElementsByTagName('tbody')[0]);
	}
	return table;
}
function setaddresscell(tr,data){
	tr.id=data.id;
	
	tr.insertCell(0).innerHTML=data.name;
	tr.insertCell(1).innerHTML=data.telephone;
	tr.insertCell(2).innerHTML=data.company;
	tr.insertCell(3).innerHTML=data.zone;
	tr.insertCell(4).innerHTML=data.city;
	tr.insertCell(5).innerHTML=data.streetAdress;
	tr.insertCell(6).innerHTML=data.postCode;
	tr.insertCell(7).innerHTML=data.memo;
	return tr;
}
function setinvoicecell(tr,data){
	tr.id=data.id;
	tr.insertCell(0).innerHTML=data.company;
	tr.insertCell(1).innerHTML=data.companyAddress;
	tr.insertCell(2).innerHTML=data.companyTelephone;
	tr.insertCell(3).innerHTML=data.bankName;
	tr.insertCell(4).innerHTML=data.bankAccount;
	tr.insertCell(5).innerHTML=data.taxpayerNumber;
	tr.insertCell(6).innerHTML=data.memo;
	return tr;
}
function hidemodal(){
	 $('#pageContainer').hideLoading();	 
}


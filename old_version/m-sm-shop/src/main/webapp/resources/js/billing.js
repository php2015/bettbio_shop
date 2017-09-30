
function updateaddress(code){	
	$('#pageContainer').showLoading();
	$.ajax({  
		type: 'POST',
		  url: getContextPath() +'/shop/customer/getAddress.html',
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
		  url: getContextPath() +'/shop/customer/getInvoice.html',
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

function deletbillAddress(id){
	$("#confirmHref").unbind();
	 $("#confirmHref").bind("click",function(){
		 window.location.href =getContextPath()+'/shop/customer/removeAddress.html?id='+id;
	 });
	 $('#delcfmModel').modal('show');
}
function deletbillInvoice(id){
	$("#confirmHref").unbind();
	 $("#confirmHref").bind("click",function(){
		 window.location.href =getContextPath()+'/shop/customer/removeInvoice.html?id='+id;
	 });
	 $('#delcfmModel').modal('show');
}


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


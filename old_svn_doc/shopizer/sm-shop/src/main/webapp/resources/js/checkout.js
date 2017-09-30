   function showModal(){
	$('#pageContainer').showLoading();
	$.ajax({  
		type: 'POST',
		  url: getContextPath() + '/shop/order/defautAddressInvoice.html',
		  cache: false,
		  dataType: 'json',
		  success: function(defaultdata) {
			  modaladdresstbale(defaultdata);
			 } ,
		 error: function( textStatus, errorThrown) {
			$('#pageContainer').hideLoading();
			//alert(textStatus); 
			 
		 }
		 	});
};
function addnewaddres(){	
	$('#pageContainer').showLoading();
	$.ajax({  
		type: 'POST',
		  url: getContextPath() + '/shop/customer/alladdress.html',
		  data:$("#ezybioForm").serialize(),
		  success: function(defaultdata) {
			  if(defaultdata!=null && defaultdata.defaultAddress != null){
			    var selecta = document.getElementById("selectaddress");
			    if(selecta !=null){
			    	selecta.innerHTML="";					
			    }else{
			    	var table = document.getElementById("defaultaddresstable");
			    	var thead= addresshead();
			    	table.appendChild(thead);
			    	var tbody = document.createElement("tbody");
			    	selecta = tbody.insertRow(0) ;
			    	table.appendChild(tbody);
			    }
			    selecta=setaddresscell(selecta,defaultdata.defaultAddress);
			    selecta.id="selectaddress";	
			    $("#addressid").val(defaultdata.defaultAddress.id);
			    if ($('#invoicesend').is(':checked')) {
			    	$('#sendaddress').hide();
			    	$("#invoicesendid").val(defaultdata.defaultAddress.id);
			    }
			  }
			  $('#pageContainer').hideLoading();
				 $('#addnew').modal('hide');
				 document.getElementById("ezybioForm").reset();
				 ischeckoutFormValid();
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
			  if(defaultdata!=null && defaultdata.defaultInvoice != null){
			    var selecta = document.getElementById("selectinvoice");
			    if(selecta !=null){
			    	selecta.innerHTML="";					
			    }else{
			    	var table = document.getElementById("defaultinvoicetable");
			    	var thead= invoicehead();
			    	table.appendChild(thead);
			    	var tbody = document.createElement("tbody");
			    	selecta = tbody.insertRow(0) ;
			    	table.appendChild(tbody);
			    }
			    selecta=setinvoicecell(selecta,defaultdata.defaultInvoice);
			    selecta.id="selectinvoice";		
			    $("#invoiceid").val(defaultdata.defaultInvoice.id);
			  }
			  $('#pageContainer').hideLoading();
				 $('#addnewinvoice').modal('hide');
				 document.getElementById("changeInvoiceForm").reset();
				 ischeckoutFormValid();
			 } ,
		 error: function( textStatus, errorThrown) {
			$('#pageContainer').hideLoading();
			//alert(textStatus); 
			 
		 }
		 	});
};
function showInvoice(){
	$('#pageContainer').showLoading();
	$.ajax({  
		type: 'POST',
		  url: getContextPath() + '/shop/order/defautInvoice.html',
		  cache: false,
		  dataType: 'json',
		  success: function(defaultdata) {
			  modalinvoicetbale(defaultdata);
			 } ,
		 error: function( textStatus, errorThrown) {
			$('#pageContainer').hideLoading();
			//alert(textStatus); 
			 
		 }
		 	});
};

function showModaladdress(){
	$('#pageContainer').showLoading();
	$.ajax({  
		type: 'POST',
		  url: getContextPath() + '/shop/order/defautAddressInvoice.html',
		  cache: false,
		  dataType: 'json',
		  success: function(defaultdata) {
			  modalsendadress(defaultdata);
			 } ,
		 error: function( textStatus, errorThrown) {
			$('#pageContainer').hideLoading();
			//alert(textStatus); 
			 
		 }
		 	});
};
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
function invoicelist(defaultdata){
	var table = cleartbody();
	var tbody = document.createElement("tbody");		
	for (var i = 0; i < defaultdata.othersInvoices.length; i++) {			
		var tr = tbody.insertRow(i) ;
		tr = setinvoicecell(tr,defaultdata.othersInvoices[i]);
	}
	if(tbody !=null){			
		table.appendChild(tbody); 
	}
	return table;
};
function cleartbody(){
	var table = document.getElementById("modaltable");
	if(table.getElementsByTagName('tbody')[0] != null){
		table.removeChild(table.getElementsByTagName('tbody')[0]);
	}
	return table;
}
function setaddresscell(tr,data){
	tr.id=data.id;
	tr.insertCell(0).innerHTML=data.name;
	tr.insertCell(1).innerHTML=data.company;
	tr.insertCell(2).innerHTML=data.zone;
	tr.insertCell(3).innerHTML=data.city;
	tr.insertCell(4).innerHTML=data.streetAdress;
	tr.insertCell(5).innerHTML=data.postCode;
	tr.insertCell(6).innerHTML=data.telephone;
	tr.insertCell(7).innerHTML=data.memo;
	return tr;
}

function addresslist(defaultdata){
	var table = cleartbody();
	var tbody = document.createElement("tbody");
	for (var i = 0; i < defaultdata.othersAddresss.length; i++) {			
		var tr = tbody.insertRow(i) ;
		tr = setaddresscell(tr,defaultdata.othersAddresss[i]);		
	}
	if(tbody !=null){			
		table.appendChild(tbody); 
	}
	return table;
}
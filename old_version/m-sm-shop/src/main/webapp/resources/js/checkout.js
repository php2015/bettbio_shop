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

function invoicelist(defaultdata){
	cleartbody();
	if(defaultdata !=null && defaultdata.othersInvoices !=null &&　defaultdata.othersInvoices.length>0){
		var tbody ='';
		for (var i = 0; i < defaultdata.othersInvoices.length; i++) {			
			tbody += '<div class="selecttr" id="'+defaultdata.othersInvoices[i].id+'"><div class="col-xs-6" style="border-top: 1px solid #E5E7E9;padding-top:10px;">'+defaultdata.othersInvoices[i].company+'</div>';
			tbody += '<div class="col-xs-6" style="border-top: 1px solid #E5E7E9;padding-top:10px;">'+defaultdata.othersInvoices[i].companyTelephone+'</div>';
			tbody += '<div class="col-xs-12" style="word-wrap: break-word;word-break:break-all;">'+defaultdata.othersInvoices[i].companyAddress+'</div>';
			tbody += '<div class="col-xs-8">'+defaultdata.othersInvoices[i].bankName+'</div><div class="col-xs-4">'+defaultdata.othersInvoices[i].memo+'</div>';			
			tbody += '<div class="col-xs-6" >'+defaultdata.othersInvoices[i].bankAccount+'</div><div class="col-xs-6" >'+defaultdata.othersInvoices[i].taxpayerNumber+'</div>';		
			tbody += '</div>';
		}
		return tbody;
	}
	
	return '';	
	
};
function cleartbody(){
	$('#modaltable').html('');	
}

function addresslist(defaultdata){
	cleartbody();
	if(defaultdata !=null && defaultdata.othersAddresss !=null &&　defaultdata.othersAddresss.length>0){
		var tbody ='';
		for (var i = 0; i < defaultdata.othersAddresss.length; i++) {	
			tbody += '<div class="selecttr" id="'+defaultdata.othersAddresss[i].id+'"><div class="col-xs-6" style="border-top: 1px solid #E5E7E9;padding-top:10px;">'+defaultdata.othersAddresss[i].name+'</div>';
			tbody += '<div class="col-xs-6" style="border-top: 1px solid #E5E7E9;padding-top:10px;">'+defaultdata.othersAddresss[i].telephone+'</div>';
			tbody += '<div class="col-xs-8">'+defaultdata.othersAddresss[i].company+'</div><div class="col-xs-4" style="word-wrap: break-word;word-break:break-all;">'+defaultdata.othersAddresss[i].memo+'</div>';
			tbody += '<div class="col-xs-4">'+defaultdata.othersAddresss[i].zone+'</div><div class="col-xs-4">'+defaultdata.othersAddresss[i].city+'</div><div class="col-xs-4">'+defaultdata.othersAddresss[i].postCode+'</div>';
			tbody += '<div class="col-xs-12" style="word-wrap: break-word;word-break:break-all;">'+defaultdata.othersAddresss[i].streetAdress+'</div></div>';
		}
		return tbody;
	}

	return '';	
}
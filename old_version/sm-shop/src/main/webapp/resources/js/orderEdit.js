   function showModal(){
	$('#pageContainer').showLoading();
	$.ajax({  
		type: 'POST',
		  url: getContextPath() + '/shop/order/defautAddressInvoice.html',
		  cache: false,
		  dataType: 'json',
		  success: function(defaultdata) {
			  $('#editorder').modal('show');			 
			 } ,
		 error: function( textStatus, errorThrown) {
			$('#pageContainer').hideLoading();
			//alert(textStatus); 
			 
		 }
		 	});
};

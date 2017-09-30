function addgroup(){
	$.ajax({
		type: 'POST',
		dataType: "json",
		url: 'addgroup.html',
		data:'code='+$('#groupcode').val(),
		success: function(resp) {
			if(resp.response.status==0){
				alertSuccess();
				window.location.href='list.html';
			}else if(resp.response.status ==9998){
				alertexist();
			}else if(resp.response.status ==-1){
				alertBlank();
			}
		},
		error: function(jqXHR,textStatus,errorThrown) { 
			//$(divProductsContainer).hideLoading();
			//alert('Error ' + jqXHR + "-" + textStatus + "-" + errorThrown);
		}
		
		
	});
}



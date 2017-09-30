
function findByName(){	
	if(findholder == $("#findbyname").val()){
		$("#findbyname").val('');
	}
	$("#findName").val($("#findbyname").val());
	dosubmit();
	
};
function dosubmit(){
	$('#ezybioForm').submit();
}

function deletestores(){
	var i =0;
	var items = new Array();
	  $(".everycheckbox:checked").each(function(){
		items[i]=$(this).attr("id");
				  i++;
	});
	  if(i==0) return ;
	  $("#confirmHref").unbind();
	  $("#confirmHref").bind("click",function(){
		  deletep(items,1);
		 });
	  $('#delcfmModel').modal('show');
}

function doAction(){	
	 $('#page').val(cuurentPage);
	 dosubmit();
	
};

function deletep(ids){
	var datas;
	if(type==0){
		$(showloading).showLoading();
		datas="productId="+ids;	}
	else{
		getPecent();
	}
	 
	$("#p-title").hide();
	$.ajax({
		type: 'POST',
		dataType: "json",
		data: datas,
		url: 'remove.html',
		success: function(resp) {
			 $(".everyTR").each(function(){
				  $(this).removeAttr("style");

			});
			
			if(resp.response.status>1){
				alertSuccess();
				if(type>0){
					doAction(page);
				}
			}else if(resp.response.status ==-1){
				activeFaild();
			}else if(resp.response.status ==1){
				if(response.response.erronames !=null &&　response.response.erronames　!=""){
					alertPartSuccess(response.response.erronames);
				}
			}
			
		},
		error: function(jqXHR,textStatus,errorThrown) { 
			if(type==0){
				$(showloading).hideLoading();
			 }
			//alert('Error ' + jqXHR + "-" + textStatus + "-" + errorThrown);
		}
		
		
	});
}
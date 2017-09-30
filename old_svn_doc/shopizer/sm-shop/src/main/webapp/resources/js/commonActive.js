
function initStatus(status){
	if(status==1){
		$('#allAv').prop('indeterminate', true);
	}else if(status==2){
		$('#allAv').prop('checked', 'checked');
	}
	$('#allAv').unbind();
	$('#allAv').bind("click",function(){
		findByAvliable(status);
	});
}
function findByAvliable(status){
	 switch (status) {
	    case 0:
	    	$("#avaiable").val(1);
	    	break;
	      // indeterminate, going checked
	    case 1:
	    	$("#avaiable").val(2);
	      break;
	      // checked, going unchecked
	    default:
	    	$("#avaiable").val(0);
	  }
	
	dosubmit();
}

function changeav(para){	
	$(showloading).showLoading();
	$.ajax({
		type: 'POST',
		dataType: "json",
		data: 'id='+para.id+'&active='+para.checked,
		url: 'available.html',
		success: function(resp) {
			if(resp.response.status==0){
				alertSuccess();	
				$("#av_"+para.id).html(''+para.checked);
			}else{
				activeFaild();
				var checked="";
		        if(para.checked == false) {
		        	checked="checked";
		        }
		        para.checked = checked;		       
			}
			$(showloading).hideLoading();
		},
		error: function(jqXHR,textStatus,errorThrown) { 
			$(prodcutTable).hideLoading();
			//alert('Error ' + jqXHR + "-" + textStatus + "-" + errorThrown);
		}
		
		
	});
}
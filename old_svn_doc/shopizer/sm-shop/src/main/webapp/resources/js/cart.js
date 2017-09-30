$(function(){
	$('.quantity').numeric();
    $("input[type='text']").keyup(function(e){
    	recuculc();
        if (e.which == 13){
        	e.preventDefault();	        	
        }
    });
    $("#selectall").click(function(){	
    	var isChecked = $(this).prop("checked");
    	 $(".everycheckbox").prop("checked", isChecked);
    	 $(".storecheckbox").prop("checked", isChecked);
        recuculc();
	});
    $(".storecheckbox").click(function(){	
    	var isChecked = $(this).prop("checked");
    	var storeid =$(this).parent().attr('id'); 
    	 $("input[name=check_"+storeid+"]").prop("checked", isChecked);
    	 doselectall(isChecked,storeid);
	});
    $(".everycheckbox").click(function(){
    	var storeid =$(this).attr('id');
    	doselectall($(this).prop("checked"),storeid);
    }); 

});


function doselectall(isChecked,storeid){
	  if(isChecked == false){
		 $("#selectall").prop("checked", isChecked);
		 $("#store_"+storeid).prop("checked", isChecked);
	  }else{
	    	 var i =0;
	   	  $(".everycheckbox:checked").each(function(){
	   				  i++;
	   			});
	   	  if (count!=0 && i==count){
	   		  $("#selectall").prop("checked", isChecked);
	   		$("#store_"+storeid).prop("checked", isChecked);
	   	  }
	}
	 recuculc();
}


function dosubmit(){
	  var items = new Array();
	  var cartCode = getCartCode();
	  var i=0;
	  $(":checkbox:checked").each(function(){
		  var item = new Object();
			  var tablerow = $(this).parent().parent("tr").find("[name='quantity']");
			  var qty = tablerow.val();
				if(qty>0) {
					var id = tablerow.attr("id");
					item.id = id;
					item.quantity = qty;
					item.code=cartCode;
					items[i] = item;
					i++;
				}
			});
	  if(items !=null && items.length>0 && cartCode!=null){
	  	document.getElementById("selecteditem").value=JSON.stringify(items);
	  	$("#checkout").submit();
	  }else {
		  alert(msgCart);
	  }
	  
}

function doplus(id){
	var qu =$("#quantity_"+id).val();
	qu++;
	$("#quantity_"+id).val(qu);
	 recuculc();
}
function minus(id){
	var qu =$("#quantity_"+id).val();
	if(qu !='0'){
		qu--;
	}
	
	$("#quantity_"+id).val(qu);
	 recuculc();
}
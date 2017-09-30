$(function(){
	$('.quantity').numeric();
    $("input[type='text']").keyup(function(e){
    	recuculctoal();
        if (e.which == 13){
        	e.preventDefault();	        	
        }
    });
    $("#selectall").click(function(){	
    	var isChecked = $(this).prop("checked");
    	 $(".everycheckbox").prop("checked", isChecked);
    	 $(".storecheckbox").prop("checked", isChecked);
    	 recuculctoal();
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

function recuculctoal(){
	
	var summeony=0.00;
	$(".everycheckbox:checked").each(function(){
		
		var item = new Object();
		
		var cartID=$(this).parent().attr("id");
		var tablequnity = $("#quantity_"+cartID);
		
		var tableprice =$("#price_"+cartID).html();
		 
			    var priceVal = parseFloat(tableprice.replace(/[^0-9-.]/g, '')); // 12345.99
			    var everytotal = priceVal*tablequnity.val();
			 
			  summeony += everytotal;
		
		});
	var totalmoneys = document.getElementsByName("totalmoneyculc");
	
	if(totalmoneys !=null) {
		for(var j = 0; j< totalmoneys.length; j++) {
			totalmoneys[j].innerHTML='<strong >' + summeony.formatMoney() +'</strong >';
		} 
	}
};

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
	  recuculctoal();
}


function dosubmit(){
	  var items = new Array();
	  var cartCode = getCartCode();
	  var i=0;
	  $(".everycheckbox:checked").each(function(){
		  var item = new Object();
		  var cartID=$(this).parent().attr("id");
		  var tablequnity = $("#quantity_"+cartID).val();
			if(tablequnity>0) {
				item.id = cartID;
				item.quantity = tablequnity;
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
	recuculctoal();
}
function minus(id){
	var qu =$("#quantity_"+id).val();
	if(qu !='0'){
		qu--;
	}
	
	$("#quantity_"+id).val(qu);
	recuculctoal();
}
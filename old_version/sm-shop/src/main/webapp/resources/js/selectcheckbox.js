$(function(){
if(!placeholderSupport()){   
    $('[placeholder]').focus(function() {
        var input = $(this);
        if (input.val() == input.attr('placeholder')) {
            input.val('');
            input.removeClass('placeholder');
        }
    }).blur(function() {
        var input = $(this);
        if (input.val() == '' || input.val() == input.attr('placeholder')) {
            input.addClass('placeholder');
            input.val(input.attr('placeholder'));
        }
    }).blur();
};
$('#findbyname').bind('keypress',function(event){
    if(event.keyCode == "13")    
    {
    	findByName();
    }
});
})
function placeholderSupport() {
    return 'placeholder' in document.createElement('input');
}		
function initCheckbox(){
	$("#selectall").click(function(){	
    	var isChecked = $(this).prop("checked");
    	 $(".everycheckbox").prop("checked", isChecked);
	});
	$("#diamond_selectall").click(function(){	
    	var isChecked = $(this).prop("checked");
    	 $(".everyUnmarkCheckbox").prop("checked", isChecked);
	});

 $(".everycheckbox").click(function(){
    	doselectall($(this).prop("checked"));
    });
 $(".everyUnmarkCheckbox").click(function(){
    	doselectallUnmark($(this).prop("checked"));
    });
}

function doselectall(isChecked){
	  if(isChecked == false){
		 $("#selectall").prop("checked", isChecked);
	  }else{
	    	 var i =0;
	   	  $(".everycheckbox:checked").each(function(){
	   				  i++;
	   			});
	   	  if (count!=0 && i==count){
	   		  $("#selectall").prop("checked", isChecked);
	   	  }
	}
}

function doselectallUnmark(isChecked){
	  if(isChecked == false){
		 $("#diamond_selectall").prop("checked", isChecked);
	  }else{
	    	 var i =0;
	   	  $(".everyUnmarkCheckbox:checked").each(function(){
	   				  i++;
	   			});
	   	  if (count!=0 && i==count){
	   		  $("#diamond_selectall").prop("checked", isChecked);
	   	  }
	}
}
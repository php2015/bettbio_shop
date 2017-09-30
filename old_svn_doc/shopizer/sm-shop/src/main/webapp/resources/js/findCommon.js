var progressstatus=true;
function findByName(){	
	if(findholder == $("#findName").val()){
		$("#findName").val('');
	}
	//$("#findName").val($("#findbyname").val());
	dosubmit();
	
};
function dosubmit(){
	$('#ezybioForm').submit();
}

function deletes(){
	var i =0;
	var items = new Array();
	  $(".everycheckbox:checked").each(function(){
		items[i]=$(this).attr("id");
				  i++;
	});
	  if(i==0) return ;
	  $("#confirmHref").unbind();
	  $("#confirmHref").bind("click",function(){
		  deletemuti(items);
		 });
	  $('#delcfmModel').modal('show');
}

function doAction(num){		
	 $('#page').val(num);
	 dosubmit();
	
};
function deleteall(){
	var l = $("tr[class=everyTR]").length;
	//如果没有记录，则不做后续任何动作
	if(l<=0) return;
	$("#confirmHref").unbind();
	$("#confirmHref").bind("click",function(){
			deletemuti();
		 });
	$('#delcfmModel').modal('show');
}
function getPecent(){
	$('#showprogress').modal('show');
	$.ajax({
		type: 'POST',
		dataType: "json",
		url: 'percent.html',
		success: function(resp) {
			$("div[class=progress-bar]").css("width", resp.response.status+"%");	
			$("div[class=progress-bar]").html(resp.response.status+"%");			
			if(resp.response.status ==-1 ){
				if(retry<9){
					retry=0;
				}else{
					setTimeout("getPecent()", 2000);
				}
				
			}else if(resp.response.status ==100){
				progressstatus=true;
			}else{
				  setTimeout("getPecent()", 2000);  
				  progressstatus=false;
			}
		},
		error: function(jqXHR,textStatus,errorThrown) { 
			//alert('Error ' + jqXHR + "-" + textStatus + "-" + errorThrown);
		}
		
		
	});
}

function delEntity(id){
	$("#confirmHref").unbind();
	$("#confirmHref").bind("click",function(){
			delConform(id);
		 });
	$('#delcfmModel').modal('show');
}
function delConform(id){
	$(showloading).showLoading();
	$.ajax({
		type: 'POST',
		dataType: "json",
		data: "entity="+id,
		url: 'remove.html',
		success: function(resp) {
			 $(".everyTR").each(function(){
				  $(this).removeAttr("style");
			});
			if(resp.response.status==0||resp.response.status==9999){
				alertSuccess();
				doAction(page);				
			} else {
				if(resp.response.statusMessage!=null||resp.response.statusMessage!=''){
					activeFaild(resp.response.statusMessage);
				} else {
					activeFaild();
				}
			}
			$(showloading).hideLoading();
		},
		error: function(jqXHR,textStatus,errorThrown) { 
			$(showloading).hideLoading();			
			//alert('Error ' + jqXHR + "-" + textStatus + "-" + errorThrown);
		}
		
		
	});
}
function deletemuti(ids){
	//getPecent();
	var datas;
	if(progressstatus==true){
		if(ids!=undefined && ids !=null){
			datas="entitis="+ids;
		}else {
			ids = new Array();
		}	
		$.ajax({
			type: 'POST',
			dataType: "json",
			data: datas,
			url: 'removes.html',
			success: function(resp) {
				 $(".everyTR").each(function(){
					  $(this).removeAttr("style");

				});
				if(resp.response.status>1){
					alertSuccess();
					doAction(page);
					
				}else if(resp.response.status ==-1){
					activeFaild();
				}else if(resp.response.status ==1){
					if(resp.response.erronames !=null &&　resp.response.erronames　!=""){
						alertPartSuccess(resp.response.erronames,ids);
					}
				}
				$('#showprogress').modal('hide');
			},
			error: function(jqXHR,textStatus,errorThrown) { 
				if(type==0){
					$(showloading).hideLoading();
				 }
				//alert('Error ' + jqXHR + "-" + textStatus + "-" + errorThrown);
			}
			
			
		});
	}
}
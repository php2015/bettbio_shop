var pid;
var lessName=new Array();
$(function(){
	$('.thumbImg').click(function (e) {
		img = $(this).find('img').clone();
		$(img).css("width","100%");
		$('#mainImg').html(img);
		setImageZoom(img.attr('id'));
	});
  


	});

function setImageZoom(id) {
    $('#' + id).elevateZoom({
   			zoomType	: "lens",
   			lensShape : "square",
   			lensSize    : 240
  		}); 
}

function getAuditInfo(isFree,audit){
	getQuality();
	
	if(audit<1){
		writeNonFree(isFree);
	}
	
}

function getQuality(){
$("#view").showLoading();
	
	$.ajax({
		type: 'POST',
		dataType: "json",
		data: 'pid='+pid,
		url: 'getquality.html',
		success: function(quality) {
			if(quality !=null){
				writeQuality(quality);
			}else{
				alert("No valid data");
			}
			$("#view").hideLoading();
		},
		error: function(jqXHR,textStatus,errorThrown) { 
			$("#view").hideLoading();
			alert("No valid data");
		}
		
		
	});
}

function doSubmit(isFree,audit){
	if(isFree ==false && audit<1){
		if($('#dateStart').val()==null || $('#dateStart').val()=='' || $('#dateEnd').val()==null || $('#dateEnd').val()==''){
			alert("Please select a date");
		}else{
			var beginDate=$("#dateStart").val();  
			 var endDate=$("#dateEnd").val();  
			 var d1 = new Date(beginDate.replace(/\-/g, "\/"));  
			 var d2 = new Date(endDate.replace(/\-/g, "\/")); 
			  if(beginDate!=""&&endDate!=""&&d1 >=d2)  
			 {  
				  alert("End date do not early start date");  
			 }else{				
				var data = "pid="+pid+"&starDate="+$("#dateStart").val()+"&endDate="+$("#dateEnd").val()+"&score="+$('#score').html();
				 setCharge(data)
			 }
		}
	}else{
		var data = "pid="+pid+"&score="+$('#score').html();
			
		if(lessName!=null && lessName!=''){
			data +="&less="+lessName;
		}
		
		setQuality(data);
		
	}
	
}

function getLessName(){
	
	var i=lessName.length;
	$("input:checkbox").not("input:checked").each(function(){
		lessName[i]=	$(this).attr("id");			
		});
	
	return lessName;
}
function setQuality(datas){
$("#modal-body").showLoading();
	
	$.ajax({
		type: 'POST',
		dataType: "json",
		data: datas,
		url: 'auditquality.html',
		success: function(resp) {
			if(resp.response.status>=0){
				alert("success");
				writeFreeResult(resp.response.status);
				$("#audit-button")
				$('#audit-button').addClass('btn-disabled');
				$('#audit-button').prop('disabled', true);	
				$('#aduit').modal('hide');
			}else{
				alert("failed");
			}
			$("#modal-body").hideLoading();
		},
		error: function(jqXHR,textStatus,errorThrown) { 
			$("#modal-body").hideLoading();
			alert("failed");
		}
		
		
	});
}

function recuculc(total){
	
	//var total=0;
	$(":checkbox:checked").each(function(){
		total += parseInt($('#score_'+$(this).attr("id")).html());		
		});
	$('#score').html(total);
};

function setCharge(datas){
	$("#modal-body").showLoading();
	
	$.ajax({
		type: 'POST',
		dataType: "json",
		data: datas,
		url: 'auditcharge.html',
		success: function(resp) {
			if(resp.response.status==0){
				alert("success");
				writeChageResult();
				$("#audit-button")
				$('#audit-button').addClass('btn-disabled');
				$('#audit-button').prop('disabled', true);	
				$('#aduit').modal('hide');
			}else{
				alert("failed");
			}
			$("#modal-body").hideLoading();
		},
		error: function(jqXHR,textStatus,errorThrown) { 
			$("#modal-body").hideLoading();
			alert("failed");
		}
		
		
	});
}

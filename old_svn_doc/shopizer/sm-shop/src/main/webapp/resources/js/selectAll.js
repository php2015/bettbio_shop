var cookiesId = new Array();
var cookiesVal = new Array();
function setStoreValue(sid,sname){
	cookiesId.push(""+sid.toString().trim()+"");
	cookiesVal.push(""+sname.toString().trim()+"");
	
	$("#storenamehidden").val(""+cookiesId+"");
	$("#searchstorename").val(""+cookiesVal+"");
	
	$('#collapseExample').collapse('hide');
}
function doselfdifine(){
	$("#searchstorename").removeAttr("readonly");
	$("#searchstorename").focus();
}
function setreadonly(){
	$("#searchstorename").attr("readonly","readonly");
}
function getStores(url,self) {
	if(url==null || url==''){
		url='stores.html';
	}
	$("#storeNameList").html("");
	$.ajax({
		type: 'GET',
		dataType: 'json',
		url: url,
		contentType:"application/x-www-form-urlencoded",
		success: function(storemenu) {
			var body = '<ul class="nav nav-tabs" role="tablist">';
			var detail = ' <div class="tab-content">';
			if (storemenu != null && storemenu.length > 0) {
				
				$.each(storemenu,function(i, m) {
									body += '<li role="presentation"';
									detail += ' <div role="tabpanel" class="tab-pane ';
									if (i == 0) {
										body += 'class="active"';
										detail += 'active';
									}
									body += '><a href="#'+m.code +'" aria-controls="'+m.code+'" role="tab" data-toggle="tab">'
											+ m.code + '</a></li>';
									detail += '" id="' + m.code
											+ '">';
									var lett = '';
									$
											.each(
													m.lists,
													function(j, k) {
														if (k.pinyin != lett) {
															if (j != 0) {
																detail += '</div></div>';
															}
															detail += '<br/><br/><br/><div class="row"><div class="col-sm-1">';
															detail += k.pinyin;
															detail += '</div><div class="col-sm-11"><div class="col-sm-2"><a href="javascript:void(0);" onclick="setStoreValue(';
															detail +=k.id+',&quot;'+k.name+'&quot;)">';
															detail += k.name
																	+ '</div>';
															lett = k.pinyin;
														} else {
															detail += '<div class="col-sm-2"><a href="javascript:void(0);" onclick="setStoreValue(';
															detail +=k.id+',&quot;'+k.name+'&quot;)">';
															detail += k.name
																	+ '</div>';
														}

													});
									detail += '</div></div></div>';
								});
				
			}else{
				detail ='';
			}
			/*if(self!=null){
				body +='<li role="presentation"><a href="javascript:void(0);" onclick="doselfdifine()" aria-controls="settings" role="tab" data-toggle="tab"><span style="color:red">'+self+'</span</a></li>';
			}*/
			body += '</ul>' + detail;
			$("#storeNameList").html(body);
		},
		error: function(jqXHR,textStatus,errorThrown) { 
			$("#storeNameList").html("");
		}
});
}
$(function(){
	//添加授权
    $("#bntSubmit1").click(function(){
       var  idsid=$("#storenamehidden").val();
       var idsval = $("#searchstorename").val();
       $("#form").attr("onsubmit","return "+saveAuth(1,idsid,idsval)+";");
    });
    
    $("#bntSubmit2").click(function(){
    	var  idsid=$("#storenamehidden").val();
        var idsval = $("#searchstorename").val();
        $("#form").attr("onsubmit","return "+saveAuth(2,idsid,idsval)+";");
     });
});

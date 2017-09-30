function setStoreValue(sid,sname){
		$("#storenamehidden").val(sid);
		$("#searchstorename").val(sname);
		$('#collapseExample').collapse('hide');
	}
function doselfdifine(){
	$("#searchstorename").removeAttr("readonly");
	$('#collapseExample').collapse('hide');
	$("#searchstorename").focus();
}
function setreadonly(){
	$("#searchstorename").attr("readonly","readonly");
}
function getStores(url,self,code) {
	$("#storeNameList").html("");
	$.ajax({
		type: 'GET',
		dataType: 'json',
		url: url,
		data: "code="+ code ,
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
									$.each(m.lists,function(j, k) {
														if (k.pinyin != lett) {
															if (j != 0) {
																detail += '</div></div>';
															}
															detail += '<div class="row"><div class="col-sm-1">';
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
			if(self!=null){
				body +='<li role="presentation"><a href="javascript:void(0);" onclick="doselfdifine()" aria-controls="settings" role="tab" data-toggle="tab"><span style="color:red">'+self+'</span</a></li>';
			}
			body += '</ul>' + detail;
			$("#storeNameList").html(body);
		},
		error: function(jqXHR,textStatus,errorThrown) { 
			$("#storeNameList").html("");
		}
});
	
}
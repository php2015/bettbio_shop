var progressstatus=true;
var retry =0;
function getTreeData(roottext){
	$("#showloading").showLoading();
	$.ajax({
		type: 'POST',
		dataType: "json",
		url: 'categorys.html',
		success: function(productList) {
			var treeData = new Array();
			var root = new Object();
			 root.text=roottext;
			 root.id=-1;
			 root.onclick="postByCatagory(-1)";
			 root.state= {
					 selected: true
				  };
			 var nodeData = new Array()
			for (var i = 0; i < productList.categorys.length; i++) {
				nodeData[i]= getNode(productList.categorys[i]);
			}
			 root.nodes=nodeData;
			 treeData[0]=root;
			$('#cataTree').treeview({
		        color: "#428bca",
		        showBorder: true,
		        data: treeData,
		        levels: 2,
		        enableLinks:true
		    });
			postByCatagory(-1);
			
			$("#showloading").hideLoading();
		},
		error: function(jqXHR,textStatus,errorThrown) { 
			$("#showloading").hideLoading();
			
		}
		
		
	});
}

function findByName(){
	if(findholder == $("#findbyname").val()){
		$("#findbyname").val('');
	}
	var criteria = getcriteria() ;
	getProducts(criteria);
}
function getbystatus(stauts){
	avstatus=stauts;
	findByName();
}
function getcriteria(){
	var criteria = 'categoryId=' + categoryId ;
	criteria += '&findname=' + $("#findbyname").val() ;
	criteria += '&avstatus=' +avstatus ;
	return criteria;
}
function createTable(ps){
	$("#prodcutTbody").html("");
	$.each(ps, function(i, p) {
        var tbBody = "";
        var checked="";
        if(p.available == true) {
        	checked='checked="checked"';
        }
        var quality=''
        if(p.qualitysocre>0){
        	var modNum = p.qualitysocre%20;
			var intNum = parseInt(p.qualitysocre/20);			
			for(var index=0;index<intNum;index++){
				quality += '<img src="'+qualitystar+'">'
			}
			if(modNum>0){
				quality +='<img src="'+qualityhalfstar+'">'
			}
        }
        tbBody += '<tr id="result_name_'+p.id+'" class="everyTR"><td> <input type="checkbox" class="everycheckbox" id="'+p.id+'"></td>';
        tbBody += '<td style=" word-break: break-all;"><a href="#" onclick="editProduct(' + p.id + ','+p.audit+','+p.service+')">' ;
        tbBody +=  p.productName + '</a></td><td style=" word-break: break-all;">'+p.productEnName+'</td><td style=" word-break: break-all;">' + p.sku + '</td><td style=" word-break: break-all;">' + p.code + '</td><td>'+getAudit(p.audit)+'</td><td>'+quality+'</td><td><span id=av_'+p.id+'>' + p.available + '</span><div class="pull-right"><input type="checkbox" onclick="changeav(this)" id='+p.id+' '+checked+'></div></td><td><span>'+p.availableDate+'</span></td><td>'+ p.storeName  
        tbBody +=  '</td><td>'+'<a href="javascript:void(0);" onclick="delproduct('+p.id+')">'+deltile+'</a></td></tr>';
        $("#prodcutTbody").append(tbBody);
    });
}

function getProducts(datas){
	$("#showloading").showLoading();
	$("#p-title").hide();
	$.ajax({
		type: 'POST',
		dataType: "json",
		data: datas,
		url: 'paging.html',
		success: function(productList) {
			if(productList !=null && productList.products.length>0){
				createTable(productList.products);
				writePaging(productList.paginationData);
				writeptile(productList.paginationData.offset,productList.paginationData.countByPage,productList.paginationData.totalCount);
				initCheckbox();
				count=productList.products.length;
			}else{
				$("#prodcutTbody").html("");
				$("#pagination").html("");
				writeptile(0,0,0);
			}
			 $("#selectall").prop("checked", false);
			$("#p-title").show();
			$("#showloading").hideLoading();
		},
		error: function(jqXHR,textStatus,errorThrown) { 
			$("#showloading").hideLoading();
			//alert('Error ' + jqXHR + "-" + textStatus + "-" + errorThrown);
		}
		
		
	});
}



function postByCatagory(cid){	
	categoryId = cid;
	findByName();
	
}
function deleteps(){
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
function delproduct(ids){
	$("#confirmHref").unbind();
	$("#confirmHref").bind("click",function(){
		  deletep(ids,0);
		 });
	$('#delcfmModel').modal('show');
}
function deleteall(){
	$("#confirmHref").unbind();
	$("#confirmHref").bind("click",function(){
		deletemuti();
		 });
	$('#delcfmModel').modal('show');
}
function deletep(ids){
	$(showloading).showLoading();
	$.ajax({
		type: 'POST',
		dataType: "json",
		data: "productId="+ids,
		url: 'remove.html',
		success: function(resp) {
			 $(".everyTR").each(function(){
				  $(this).removeAttr("style");
			});
			if(resp.response.status>=0){
				alertSuccess();
				doAction(page);				
			}else {
				activeFaild();
			}
			$(showloading).hideLoading();
		},
		error: function(jqXHR,textStatus,errorThrown) { 
			$(showloading).hideLoading();			
			//alert('Error ' + jqXHR + "-" + textStatus + "-" + errorThrown);
		}
		
		
	});
}

function getNode(nodes){
	var item = new Object();
		item.text=nodes.categoryName;
		item.id=nodes.categoryID;
		item.onclick="postByCatagory(" + nodes.categoryID +")";
	if(nodes.categorys !=null && nodes.categorys.length>0 ){
		var sonNode = new Array();
		for(var i=0; i<nodes.categorys.length;i++){
			sonNode[i]=getNode(nodes.categorys[i]);
		}		
		item.nodes=sonNode;
	}
	return item;
}

function doAction(pagenum){
	page = pagenum;
	var criteria = getcriteria() ;
	criteria += '&page=' + pagenum ; ;
	getProducts(criteria);
	
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

function deletemuti(ids){
	getPecent();
	var datas;
	if(progressstatus==true){
		if(ids !=null){
			datas="productId="+ids;
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
					if(response.response.erronames !=null &&　response.response.erronames　!=""){
						alertPartSuccess(response.response.erronames);
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
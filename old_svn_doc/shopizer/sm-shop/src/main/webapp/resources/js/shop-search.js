function addFilter(id,pname,type){
	categoryIds=id;
	loadProducts(getcriteria(),pname,type);
	$("#categoryPanel").hide();
	writeBread(pname);
}

function writeSearchCate(cateList){
	if(cateList !=null && cateList.length>0){
		var tbBody ='<div class="search_nav5">';	
		
		$.each(cateList, function(i, p) {				
			if(p.productCount>0){
				if(i>0){
					tbBody +='</div><div class="search_nav5">';
					$("#catMore").html( '<a role="button" style="border: 1px solid #E5E7E9;padding: 4px 6px;" href="javascript:void(0);" onclick="showMore(&quot;subCatePanel&quot;)">'+more+'<span class="glyphicon glyphicon-menu-down ico-style" aria-hidden="true"/></a>');
				}
				tbBody+='<search_bar style="padding-right:30px;"><strong><a style="color:#4285f4;" href="javascript:void(0);" onclick="addFilter('+p.id+',&apos;'+p.name+'&apos;)">'+p.name;
				tbBody +=	'</a></strong>&nbsp;('+p.productCount+')';			 
				 tbBody +="</search_bar>";	
				 if(p.sonCategory !=null){
					 $.each(p.sonCategory, function(i, s){
						 tbBody+='<search_bar style="margin-right:65px;"><a href="javascript:void(0);" onclick="addFilter('+s.id+',&apos;'+s.name+'&apos;)">'+s.name;
							tbBody +=	'</a>&nbsp;('+s.productCount+')';			 
							 tbBody +="</search_bar>";	
					 });
				 }
			} 
		});
		
		tbBody +='</div >';
		$("#subCatePanel").html(tbBody);
		if($("#subCatePanel").height()>25){
			$("#subCatePanel").addClass("search_fixed_height")
			$("#catMore").html( '<a role="button" style="border: 1px solid #E5E7E9;padding: 4px 6px;" href="javascript:void(0);" onclick="showMore(&quot;subCatePanel&quot;)">'+more+'<span class="glyphicon glyphicon-menu-down ico-style" aria-hidden="true"/></a>');
		}
		$("#categoryPanel").show();
	}else{
		$("#categoryPanel").hide();
	}
}

function writeSearchButton(cateList,total){
	if(cateList !=null && cateList.length>0){
		var tbBody =' <li role="presentation" id="li_0" class=" active';		
		tbBody+=' nopadding-nomargin" ><a href="javascript:void(0);" onclick="doSearchButton(0,&quot;全部结果&quot;)">全部结果('+total+')</a></li>';
		
		$.each(cateList, function(i,p) {
			tbBody +=' <li role="presentation" id="li_'+p.id+'" class=" ';			
			tbBody +=' nopadding-nomargin" ><a href="javascript:void(0);" onclick="doSearchButton('+p.id+',&quot'+p.name+'&quot)">'+p.name+'('+p.productCount+')</a></li>';
		});
		$("#search_cateGory").html(tbBody);
	}
}

function doSearchButton(cid,pname,orderDes,_this){
	$(".nopadding-nomargin").removeClass("active");
	$(_this).closest('li').addClass("active");
	orderBy=orderDes;
	if(cid==0){
		dosearch(pname);
	}else{
		addFilter(cid,pname,'first');
	}
}

function doSearchCheckBox(pname,_this){
	auth = "";
	
	var auth1 = $(":input[name='auth1']",_this.closest('ul'));
	var auth2 = $(":input[name='auth2']",_this.closest('ul'));
	if(auth1.is(':checked')){
		auth += "auth1=1";
	}
	if(auth2.is(':checked')){
		if(auth.length>0){
			auth += "&";
		}
		auth += "auth2=1";
	}
	
	dosearch(pname);
}
function dosearch(pname) { 
	categoryIds=null;
	loadProducts(getcriteria(),pname);	
}

function getcriteria(){
	var q = $("#searchInput").val();
	var qt = $("#queryType").val();
	if(q==null || q=='') {
		return;
	}	
	var criteria="query="+q;
	criteria += "&queryType="+qt;
	if (categoryIds != null  && categoryIds !=""){
		criteria+='&categoryId='+categoryIds;
	}		
	
	if(manufacturers !=null && manufacturers.length>0){
		criteria += '&manufacturers=' + manufacturers ;	
	}
	if(!qualitys !=null && qualitys.length>0){
		criteria += '&qulity=' +qualitys;
	}
	if(orderBy !=null && orderBy.length>0){
		criteria += '&orderBy=' +orderBy;
	}
	if(auth !=null && auth.length>0)
		criteria += '&' + auth;
	return criteria;
}
function writeBread(pname){
	
	var tbBody='<li class="active" ><a href="'+getContextPath()+'">'+home+'</a></li>';	
	tbBody+='<b style="padding:0 6px;">&gt;</b><li><a href="javascript:void(0);" onclick=" dosearch(&quot;全部结果&quot;);" >'+'全部结果'+'</a></li>'; 
	if(pname != null && pname !='' && pname!='全部结果'){
		tbBody+='<b style="padding:0 6px;">&gt;</b><li class="active" ><a href="javascript:void(0);" ">'+pname+'</a></li>'; 
	}
	
	$("#breadcrumb").html(tbBody);
}


function loadProducts(data,pname,type) {
	
	$("#pageContainer").showLoading();
	
	$.ajax({
  			cache: false,
  			type: 'POST',
			dataType: "json",
  			url: getContextPath()+'/services/public/search/products/list.html',
  			data:data,
			success: function(productList) {
				
					if(productList != null && productList.productList != null && productList.productList.listProducts != null && productList.productList.listProducts.length>0){
						buildProductsList(productList.productList.listProducts);
						writeptile(productList.productList.paginationData.offset,productList.productList.paginationData.countByPage,productList.productList.paginationData.totalCount);
						writePaging(productList.productList.paginationData,2);
						writeManCate(productList.manufacturerList,"man");
						writeSearchCate(productList.categoryList);
						if(typeof(type) == "undefined"){
							writeSearchButton(productList.categoryList,productList.productList.paginationData.totalCount)
						}						
						$("#categoryBody").show();
						$("#tableshow").show();
					}else{
						$("#categoryBody").hide();
						$("#prodcutTbody").html('');
						$("#tableshow").hide();
					}
					
					callBackSearchProducts(productList.productList);
					writeBread(pname);
				
								
			},
			error: function(jqXHR,textStatus,errorThrown) { 
				$("#pageContainer").hideLoading();
				$("#prodcutTbody").html('');
				$("#categoryBody").hide();
				$("#tableshow").hide();
				//alert('Error ' + jqXHR + "-" + textStatus + "-" + errorThrown);
			}
			
	});
}
function addFilter(id){
	categoryIds=id;
	//getSubCategory(id);
	loadProducts(getcriteria());
}
/**
 * 商品列表页面，加载更多的商品
 */
function loadmore(id) {
	categoryIds=id;
	if (page==null&&page=='') {
		page = 1;
	} else {
		page++;
	}
	var url = getcriteria();
	url += '&page=' + page ;
	$.ajax({
		type: 'POST',
		dataType: "json",
		url: getContextPath()+'/services/public/products/list.html',
		data: url,
		success: function(productList) {	
			if(productList != null && productList.productList != null && productList.productList.listProducts != null && productList.productList.listProducts.length>0){
				buildProductsList(productList.productList.listProducts);
			}else{
				//没有查询到，则提示已经加载完毕
				loadEnd();
			}
		},
		error: function(jqXHR,textStatus,errorThrown) { 
			//$("#pageContainer").hideLoading();
			//alert('Error ' + jqXHR + "-" + textStatus + "-" + errorThrown);
		}
		
	});
}
function getMan(data){
		$.ajax({
			type: 'POST',
			dataType: "json",
			url: getContextPath()+'/services/public/products/qualitys.html',
			data: data,
			success: function(cateList) {
				if(cateList != null){
					writeManCate(cateList.manufacturerList,"man");
				}
				$("#categoryBody").hideLoading();
			},
			error: function(jqXHR,textStatus,errorThrown) { 
				$("#categoryBody").hideLoading();
				//alert('Error ' + jqXHR + "-" + textStatus + "-" + errorThrown);
			}
			
	});
}

function getSubCategory(id){	
	$("#pageContainer").showLoading();
	categoryIds=id;
	$.ajax({
			type: 'POST',
			dataType: "json",
			url: getContextPath()+'/services/public/products/categorys.html',
			data: 'categoryId='+id,
			success: function(cateList) {
				if(cateList != null){
					writeCate(cateList.categoryList);
					writeManCate(cateList.manufacturerList,"man");
					writeBread(cateList.breadcrumb.breadCrumbs)
				}
				$("#pageContainer").hideLoading();
			},
			error: function(jqXHR,textStatus,errorThrown) { 
				$("#pageContainer").hideLoading();
				//alert('Error ' + jqXHR + "-" + textStatus + "-" + errorThrown);
			}
			
	});
}

function writeBread(bread){
	var tbBody='<li class="active" ><a href="'+getContextPath()+'">'+home+'</a></li>';
	if(bread != null){
		$.each(bread, function(i, p) {
			tbBody+=' <li class="active" ><a href="'+getContextPath()+'/shop/category/category/'+p.id+'.html">'+p.label+'</a></li>';
		});
	}
	
	$("#breadcrumb").html(tbBody);
}

function getcriteria(){
	var criteria = 'categoryId=' + categoryIds ;
	if(manufacturers !=null && manufacturers.length>0){
		criteria += '&manufacturers=' + manufacturers ;	
	}
	if(!qualitys !=null && qualitys.length>0){
		criteria += '&qulity=' +qualitys;
	}
	return criteria;
}


function loadProducts(url) {
	$("#pageContainer").showLoading();

	$.ajax({
			type: 'POST',
			dataType: "json",
			url: getContextPath()+'/services/public/products/list.html',
			data: url,
			success: function(productList) {	
				if(productList != null && productList.productList != null && productList.productList.listProducts != null && productList.productList.listProducts.length>0){
					buildProductsList(productList.productList.listProducts);
					//writeptile(productList.productList.paginationData.offset,productList.productList.paginationData.countByPage,productList.productList.paginationData.totalCount);
					//writePaging(productList.productList.paginationData,2);						
					//writeManCate(productList.manufacturerList,"man");
					//writeCate(productList.categoryList);
					$("#categoryBody").show();
				}else{
					$("#categoryBody").hide();
				}
				//$("#categoryPanel").show();
				//writeBread(productList.breadcrumb.breadCrumbs)
				callBackLoadProducts(productList.productList);
			},
			error: function(jqXHR,textStatus,errorThrown) { 
				$("#pageContainer").hideLoading();
				//alert('Error ' + jqXHR + "-" + textStatus + "-" + errorThrown);
			}
			
	});
	
	
	
}

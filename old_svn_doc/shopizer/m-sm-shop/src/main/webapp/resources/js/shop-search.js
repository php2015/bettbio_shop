function addFilter(id,pname){
	categoryIds=id;
	loadProducts(getcriteria(),pname);
	$("#categoryPanel").hide();
	//writeBread(pname);
}

function dosearch() { 
	categoryIds=null;
	loadProducts(getcriteria());
}
/**
 * 商品列表页面，加载更多的商品
 */
function searchmore() {
	if (page==null&&page=='') {
		page = 1;
	} else {
		page++;
	}
	var data = getcriteria();
	data += '&page=' + page ;
	$.ajax({
		type: 'POST',
		dataType: "json",
		url: getContextPath()+'/services/public/search/products/list.html',
		data: data,
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
function getcriteria(){
	var q =$("#searchInput").val();
	if(q==null || q=='') {
		return;
	}	
	var criteria="query="+q;
	if (categoryIds != null  && categoryIds !=""){
		criteria+='&categoryId='+categoryIds;
	}
		
	
	if(manufacturers !=null && manufacturers.length>0){
		criteria += '&manufacturers=' + manufacturers ;	
	}
	if(!qualitys !=null && qualitys.length>0){
		criteria += '&qulity=' +qualitys;
	}
	return criteria;
}
function writeBread(pname){
	
	var tbBody='<li class="active" ><a href="'+getContextPath()+'">'+home+'</a></li>';
	tbBody+='<li class="active" ><a href="javascript:void(0);" onclick="dosearch()">'+searchAll+'</a></li>'
	if(pname != null && pname !=''){
		tbBody+='<li class="active" ><a href="javascript:void(0);" ">'+pname+'</a></li>'; 
	}
	
	$("#breadcrumb").html(tbBody);
}


function loadProducts(data,pname) {
	
	$("#pageContainer").showLoading();
	/**
    //category facets
	//var facets = '\"facets\" : { \"categories\" : { \"terms\" : {\"field\" : \"categories\"}}}';
    var highlights = null;
	var queryStart = '{';

	var query = '\"query\":{\"query_string\" : {\"fields\" : [\"pname^3\", \"simpledesc\", \"tags\"], \"query\" : \"*' + q + '* and othert \", \"use_dis_max\" : true ';
	if(filter!=null && filter!='') {
		//query = '\"query\":{\"filtered\":{\"query\":{\"text\":{\"_all\":\"' + q + '\"}},' + filter + '}}';
		//query = '\"query\":{\"filtered\":{\"query\":{\"text\":{\"_all\":\"' + q + '\"}},'  + '\"filter\": { \"term\" : { \"code\" : \"'+filter + '\"}}';
		//query = query + ',' + '\"filter\": { \"term\" : { \"code\" : \"'+filter + '\"}}';
		//query = query + ',' + '\"filters\": [{ \"filter\" : {\"term\" : { \"code\" : \"'+filter + '\"}}}]';
		//query = query + ',' + '\"filter\": { \"term\" : { \"code\" : \"'+filter + '\"}}';
		//query = '\"query\": { \"match\" : { \"pname\": \"*'+ q +'*\"}}';
		//query = query + ', \"post_filter\" : {' +' \"term\" :{\"pname\" :\"DNA\"'; 
		//+ filter + '\"';
		query = query +'}}';
		query = query + ', \"post_filter\" : {' +' \"term\" :{\"categories\" :\"'+ filter +'\"}}';		
	}else{
		query = query +'}}';
	}

	//if(facets!=null && facets!='') {
		//query = query + ',' + facets;
	//}

	//add highlight
	query = query + ',\"highlight\" : {\"pre_tags\":[\"<span style=\'background:#6633ff;color:#fff\'>\"],\"post_tags\":[\"</span>\"],' +' \"fields\" :{\"pname\" :{},\"simpledesc\" :{}}}';
	var queryEnd = '}';
	
	query = queryStart + query + queryEnd;	*/
	$.ajax({
  			cache: false,
  			type: 'POST',
			dataType: "json",
  			url: getContextPath()+'/services/public/search/products/list.html',
  			data:data,
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
					
					callBackSearchProducts(productList.productList);
					//writeBread(pname);
				
								
			},
			error: function(jqXHR,textStatus,errorThrown) { 
				$("#pageContainer").hideLoading();
				//alert('Error ' + jqXHR + "-" + textStatus + "-" + errorThrown);
			}
			
	});
}
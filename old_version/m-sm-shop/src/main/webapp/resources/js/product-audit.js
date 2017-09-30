
function findByName(){
	if(findholder == $("#findbyname").val()){
		$("#findbyname").val('');
	}
	var criteria = getcriteria() ;
	getProducts(criteria);
}

function getcriteria(){
	
	var criteria = 'findname=' + $("#findbyname").val() ;
	return criteria;
}
function createTable(ps){
	$("#prodcutTbody").html("");
	$.each(ps, function(i, p) {
        var tbBody = "";
               
        tbBody += '<tr id="result_name_'+p.id+'" class="everyTR"><td>' ;
        tbBody +=  p.productName + '</td><td>'+p.productEnName+'</td><td>' + p.sku + '</td><td>' + p.code + '</td><td>'+ p.storeName  
        tbBody +=  '</td><td>'+'<a href="javascript:void(0);" onclick="auditproduct('+p.id+')">'+audittile+'</a></td></tr>';
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
			$(prodcutTable).hideLoading();
			//alert('Error ' + jqXHR + "-" + textStatus + "-" + errorThrown);
		}
		
		
	});
}

function auditproduct(ids){	
	window.location.href = productURL+ids;
}



function doAction(pagenum){
	page = pagenum;
	var criteria = getcriteria() ;
	criteria += '&page=' + pagenum ; ;
	getProducts(criteria);
	
}

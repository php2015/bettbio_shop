<script>
var START_COUNT_PRODUCTS = 1;
var CURRENT_PRODUCTS =1;
var MAX_PRODUCTS = 15;

function onClickPage(ccurrent){
	CURRENT_PRODUCTS=ccurrent;
	search();
}



function setpage(totalCount){	
	//START_COUNT_PRODUCTS = START_COUNT_PRODUCTS + MAX_PRODUCTS-1;
	var pageCount = parseInt(totalCount/MAX_PRODUCTS);
	//once 5 times
	var pageNum =MAX_PRODUCTS;	
	if(totalCount%MAX_PRODUCTS>0){
		pageCount =pageCount +1;
	}	
	if(totalCount > MAX_PRODUCTS) {
		$("#pageitem").show();
		
		var pagesitme=document.getElementById("pages");
		pagesitme.innerHTML="";
		var li= document.createElement("li");
		li.innerHTML='<a href="javascript:void(0);" onclick="onClickPage('+(CURRENT_PRODUCTS-1)+')">&laquo;</a>';
		//first page not need pre		
		if(CURRENT_PRODUCTS ==1){	
				li.className="disabled";
				li.innerHTML='<a href="javascript:void(0);">&laquo;</a>';
		}
		
		if(CURRENT_PRODUCTS<START_COUNT_PRODUCTS){
			START_COUNT_PRODUCTS = START_COUNT_PRODUCTS -5;
			
		}else if((START_COUNT_PRODUCTS+5)==CURRENT_PRODUCTS){
			START_COUNT_PRODUCTS = START_COUNT_PRODUCTS +5;
		}
		
		pagesitme.appendChild(li);
		
		if(pageCount<5){
			pageNum = pageCount;
		}
		//page change			
		if((START_COUNT_PRODUCTS+4)>pageCount){
			pageNum = pageCount;
		}else{
			pageNum = START_COUNT_PRODUCTS+4 ;
		}
				
		 for (var count=START_COUNT_PRODUCTS; count<=pageNum;count++) {
			 var li1= document.createElement("li"); 
			 if(count==CURRENT_PRODUCTS){
				 li1.className="active";
				 li1.innerHTML='<a href="javascript:void(0);">'+'<span class="sr-only">'+count+'</span></a>';
			 }else{
				 li1.innerHTML='<a href="javascript:void(0);" onclick="onClickPage('+count+')">'+count+'</a>';
			 }	 
			 pagesitme.appendChild(li1);
		 }
		var linext= document.createElement("li");
		linext.innerHTML='<a href="javascript:void(0);" onclick="onClickPage('+(CURRENT_PRODUCTS+1)+')">&raquo;</a>';		
		if(CURRENT_PRODUCTS==pageCount){
			linext.className="disabled";
			linext.innerHTML='<a href="javascript:void(0);">&raquo;</a>';
		}		
		pagesitme.appendChild(linext);
				
	} else {
		$("#pageitem").hide();		
	}
}

function search() {
	//products.fetchData();
	
	//alert(url);
	//$.ajax({
		//	type: 'POST',
			//dataType: "json",
			//url: 'http://localhost:8080/sm-shop/admin/products/paging.html',
			//success: function(productList) {				
				//buildProductsList(productList,divProductsContainer);
				//callBackLoadProducts(productList);
				//setpage(productList);
				//alert("dd");

			//},
			//error: function(jqXHR,textStatus,errorThrown) { 
				//$(divProductsContainer).hideLoading();
				//alert('Error ' + jqXHR + "-" + textStatus + "-" + errorThrown);
			//}
			
			
	//});
	//alert(categoryTree.getSelectedRecord().categoryId);
	itemList.fetchData({startRow:CURRENT_PRODUCTS,categoryId:categoryTree.getSelectedRecord().categoryId});
}

</script>
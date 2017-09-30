$(function(){
	//添加至购物车
	$("#addShoppingCart").click(function(){
		    if($("#isLogin").val()!=0){
				$.app.alert({message:"由于您未进行登录,系统将自动为您跳转至登录界面",title:"加入购物车提示",ok:function(){
					$.app.location(ctxPath+"/login");
				}});
		        return false;		    	
		    }
			var _productCode=$("#productCode").val();
			var _productNameCh=$("#productNameCh").val();
			var _productNameEn=$("#productNameEn").val();
			var _storeName=$("#storeName").val();
			var _storeCode=$("#storeCode").val();
			var _productImg=$("#productImg").val();
			
			var i=0;
			var count=0;
			$(".productNumber").each(function(){
				var _num=$(this).val();
				if(_num>0){
					count++;
					var _price=$(this).parents("tr").find(".price").text();
					var _spec=$(this).parents("tr").find(".spec").text();
					$("#addShoppingCartForm").append("<input type='hidden' name='carts["+i+"].productCode'   value='"+_productCode+"' />")
											 .append("<input type='hidden' name='carts["+i+"].productNameCh' value='"+_productNameCh+"' />")
											 .append("<input type='hidden' name='carts["+i+"].productNameEn' value='"+_productNameEn+"' />")
											 .append("<input type='hidden' name='carts["+i+"].storeName'     value='"+_storeName+"' />")
											 .append("<input type='hidden' name='carts["+i+"].storeCode'     value='"+_storeCode+"' />")
											 .append("<input type='hidden' name='carts["+i+"].productImg'    value='"+_productImg+"' />")
											 .append("<input type='hidden' name='carts["+i+"].number'   value='"+$(this).val()+"' />")
											 .append("<input type='hidden' name='carts["+i+"].productSpec'   value='"+_spec+"' />")
											 .append("<input type='hidden' name='carts["+i+"].unitPrice'   value='"+_price+"'  />");										 
				}
			});
			if(count!=0){//总数不等于0
				$.post(ctxPath+"/shoppingCart/add",decodeURIComponent($("#addShoppingCartForm").serialize()),function(data){
					if(data.success==true){
						$("#addShoppingCartForm").empty();
						$("#cartModal").modal("show");
					}
				},"json");
			}
	});
	
	$("#productAddShopCart").mousedown(function(){
			 $.app.location(ctxPath+"/shoppingCart/shopcart");
	});
});
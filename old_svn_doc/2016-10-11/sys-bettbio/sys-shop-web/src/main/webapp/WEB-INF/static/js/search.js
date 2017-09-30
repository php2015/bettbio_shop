$(function() {
	$(".search-group li").click(function(){
		$(this).addClass('active').siblings().removeClass('active');
		$("#QT").val($(this).index());
		$("#searchInput").attr("placeholder", $(this).data("prop"));
	})
	
	$('.sl-more').click(function() {
		var _sl = $(this).closest('.sl-item');
		_sl.toggleClass('open').removeClass('multiple');
	})
	$('.sl-plus').click(function() {
		var _sl = $(this).closest('.sl-item');
		_sl.toggleClass('multiple').addClass('open');
	})
	$('.sl-btn-cancel').click(function() {
		var _sl = $(this).closest('.sl-item');
		_sl.removeClass('multiple').removeClass('open');
		_sl.find('li').removeClass('selected');
	})
	$('.sl-value a').click(function(e) {
		var _item = $(this).closest('.sl-item');
		if(_item.hasClass('multiple')) {
			e.preventDefault();
			$(this).find('.fa').toggleClass('fa-square-o');
			$(this).closest('li').toggleClass('selected');
			if(_item.find('li.selected').length>0)
				_item.find('.sl-btn-ok').removeClass('disabled');
			else
				_item.find('.sl-btn-ok').addClass('disabled');
		}
	})
	$(".sl-btn-ok").click(function(){
		var _this = $(this);
		if(_this.hasClass('disabled')) return;
		var _item = $(this).closest('.sl-item');
		var arrs = [];
		_item.find("li.selected a").each(function(i,n){
			arrs.push($(n).data("id"));
		})
		$.app.paramMap[_this.data("type")] = arrs.join(","); 
		
		$.app.location("?"+$.app.serializeMap($.app.paramMap));
	})
	
	var _tdMap = {};
	var _pids = [];
	$(".b-table td.b-item-pga").each(function(i,n){
		var _this = $(this);
		var _key = _this.data("id");
		_tdMap[_key] = _this;
		_pids.push(_key);
	})
	if(_pids.length > 0){
		search.getPrice({ids : _pids.join(",")},function(dataMap){
			search.bindPrice(_tdMap, dataMap);
		});
	}
})

var search = {
		getPrice : function(data,callback){
			$.ajax({  
				 type: 'post',
				 url: ctxPath + '/price',
				 data: data,
				 dataType:"json",
				 error: function(e) { },
				 success: function(data) {
					 callback(data);
				} 
			});
		},
		bindPrice : function(viewMap,dataMap){
			var f,n,c,p;
			for(var id in dataMap){
				console.log(dataMap);
				p = dataMap[id].productPrices;
				f = p.length > 3;
				n = f ? 3 : p.length;
				c = "";
				for (var i = 0; i < n; i++) {
					var pr = p[i];
					c += '<div class="b-pitem"><div class="b-pspec">{spec}</div><div class="b-rmb">{price}</div><i class="fa fa-shopping-cart icon-red b-cart"></i><input type="hidden" id="code" name="code" value="'+dataMap[id].code+'"><input type="hidden" id="price" name="price" value="'+pr.price+'"><input type="hidden" id="storeCode" name="storeCode" value="'+dataMap[id].store.code+'"></div>';
					c = c.replace("{spec}",pr.specifications)
						 .replace("{price}","CNY"+$.app.formatCurrencyTenThou(pr.price,2));
				}
				if(f){
					c += '<div class="b-pitem"><a href="{url}" class="b-more">更多...</a></div>';
					c = c.replace("{url}",ctxPath+"/product-"+id+".html");
				}
				$(viewMap[id]).append(c);
				search.bindEvent();
			}
		},
		bindEvent : function(){
			$('.b-cart').unbind();
			$('.b-cart').click(function(even){
					if($("#isLogin").val()!=0){
						$.app.alert({message:"由于您未进行登录,系统将自动为您跳转至登录界面",title:"加入购物车提示",ok:function(){
							$.app.location(ctxPath+"/login");
						}});
				        return false;		    	
				    }
					var _tr=$(this).parents("tr");
					var _productCode=$(this).parents(".b-pitem").find("#code").val();
					var _productSpec=$(this).parents(".b-pitem").find(".b-pspec").text();
					var _productPrice=$(this).parents(".b-pitem").find("#price").val();
					var _productNameCh=_tr.find(".b-pname").text();
					var _productNameEn=_tr.find(".b-enname").text();
					var _storeName=_tr.find(".b-comp").text();
					var _storeCode=$(this).parents(".b-pitem").find("#storeCode").val();
					var _productImg=_tr.find(".b-img").text();
					console.log(_productCode+"-->"+_productSpec+"-->"+_productPrice+"-->"+_productNameCh+"-->"+_productNameEn+"-->"+_storeName+"-->"+_storeCode+"-->"+_productImg);
					$("#addShoppingCartForm").append("<input type='hidden' name='carts[0].productCode'   value='"+_productCode+"' />")
					 .append("<input type='hidden' name='carts[0].productNameCh' value='"+_productNameCh+"' />")
					 .append("<input type='hidden' name='carts[0].productNameEn' value='"+_productNameEn+"' />")
					 .append("<input type='hidden' name='carts[0].storeName'     value='"+_storeName+"' />")
					 .append("<input type='hidden' name='carts[0].storeCode'     value='"+_storeCode+"' />")
					 .append("<input type='hidden' name='carts[0].productImg'    value='"+_productImg+"' />")
					 .append("<input type='hidden' name='carts[0].number'   value='1' />")
					 .append("<input type='hidden' name='carts[0].productSpec'   value='"+_productSpec+"' />")
					 .append("<input type='hidden' name='carts[0].unitPrice'   value='"+_productPrice+"'  />");
					 $.post(ctxPath+"/shoppingCart/add",decodeURIComponent($("#addShoppingCartForm").serialize()),function(data){
						if(data.success==true){
							$("#addShoppingCartForm").empty();
							$("#cartModal").modal("show");
						}else{
							$("#addShoppingCartForm").empty();
						}
					 },"json");
			})
		}
}

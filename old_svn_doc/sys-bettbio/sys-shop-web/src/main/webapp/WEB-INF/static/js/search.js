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
				p = dataMap[id].productPrices;
				f = p.length > 3;
				n = f ? 3 : p.length;
				c = "";
				for (var i = 0; i < n; i++) {
					var pr = p[i];
					c += '<div class="b-pitem"><div class="b-pspec">{spec}</div><div class="b-rmb">{price}</div><i class="fa fa-shopping-cart icon-red b-cart"></i></div>';
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
			$('.b-cart').click(function(){
				if($(this).hasClass('disabled')) return;
				$("#cartModal").modal('show')
			})
		}
}

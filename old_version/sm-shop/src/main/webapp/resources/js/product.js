
	function initProduct(productId, div) {
		
		calculatePrice(productId, div);

		$(div + ' select').change(function(e) { // select element changed
			calculatePrice(productId, div);
		});

		$(div + ' :radio').change(function(e) { // radio changed
			calculatePrice(productId, div);
		});
		
		$(div + ' :checkbox').change(function(e) { // radio changed
			calculatePrice(productId, div);
		});
		
	}

	function getProductPrice(pid){
		$.ajax({  
			 type: 'POST',  
			 url: getContextPath() + '/shop/product/' + pid + '/price.html',  
			 dataType: 'json', 			 
			 cache:false,
			 error: function(e) { 
				 $("#rprice").html('<span style="color:blue;">询价</span>');
					// $('#specification').hide();
					 $('#quantity').hide();				 
			 },
			 success: function(price) {
				 if(price !=null && price.length>0){
					var flag=false;
					var prtext='<table style="background-color: #f5f5f5;width:100%;font-size:14px;" ><thead style="background-color: #c5c5c5;height:40px;line-height:2" ><th style=" padding-left:10px;background-color:#649af4;color:#FFFFFF;">规格</th><th style="background-color:#649af4;color:#FFFFFF;">价格</th><th style="background-color:#649af4;color:#FFFFFF;">供货周期</th><th style="background-color:#649af4;color:#FFFFFF;">数量</th></thead>';
		
					 $.each(price, function(i, p) {							
							prtext +='<tr style=" height:40px"><td style="padding-left:10px;">'+p.name+'</td><td>';
							var qulitytext='';
							if(p.price=='¥0.00' || p.price=='CNY0.00'){
								prtext +='询价';
							}else{
								if (p.discountPrice != null){
									prtext += p.discountPrice;
									prtext += '<br/><s style="font-size: 10px; color: #bbb;">原价 ' + p.price+'</s>';
								}else{
									prtext +=p.price;
								}
								
								flag=true;
								qulitytext='<input type="text" id="'+pid+'_'+p.id+'" name="quantity"  class="spinnerBettbio"/>';
							}
							prtext +='</td><td>';
							if(p.period !=null ){
								prtext +=p.period;
							}
							prtext +='</td><td>'+qulitytext+'</td></tr>'
							
					 });
					 prtext +='</table>';
					
					 $("#rprice").html(prtext);	
					
					 $('.spinnerBettbio').spinner({},price.length);
				 }else{
					 $("#rprice").html('<div style="padding-top:22px;padding-left:10px;font-size:16px;background-color: #f5f5f5;">询价</div>');	
				 } 
			 }
		});
	}

	function getServerPrice(pid){
		$.ajax({  
			 type: 'POST',  
			 url: getContextPath() + '/shop/product/' + pid + '/price.html',  
			 dataType: 'json', 			 
			 cache:false,
			 error: function(e) { 
				 $("#rprice").html('<span style="color:blue;">询价</span>');
				 $('bntRel').hide();
			 },
			 success: function(price) {
				 if(price !=null && price.length>0){
					var flag=false;
					var prtext='百图价:'
					 $.each(price, function(i, p) {							
							var qulitytext='';
							if(p.price=='￥0.00' || p.price=='CNY0.00'){
								prtext +='询价';
							}else{
								prtext +=p.price;
								flag=true;
							}
							
					 });
					 $("#rprice").html(prtext);	
					 $('.spinnergif').spinner({},price.length);
				 }else{
					 $("#rprice").html('<div style="padding-top:22px;padding-left:10px;font-size:16px;background-color: #f5f5f5;">询价</div>');	
				 } 
			 }
		});
	}
	
	function getProductFile(pid){
		$.ajax({  
			 type: 'POST',  
			 url: getContextPath() + '/shop/product/' + pid + '/files.html',  
			 dataType: 'json',
			 error: function(e) {
					$('#filedown').addClass("disabled");
					$('bntRel').hide();
			 },
			 success: function(price) {
				 if(price !=null && price.length>0){
					
					var prtext='';
		
					 $.each(price, function(i, p) {	
							prtext +='<a style="padding-right:5px;" id="fileLink" href="'+p.url+'">'+p.fileName+'</a>';							
							
					 });
					 $('#filedown').removeClass("disabled");
					 $('bntRel').show();
					 $("#collapseDwon").html(prtext);	
					
				 } 
			 }
		});
	}

	function addcart(){
		var items = new Array();
		var cartCode = getCartCode(); 		
		 var addflag=false;
		 var cartCode = getCartCode();
		 var i=0;
		$(".spinnerBettbio").each(function(){
			var qulity = $(this).val();
			if(qulity>0){
				var item = new Object();
				var ids= $(this).attr("id");
				var code = ids.split('_');
				addflag=true;
				//cart item
				item.productId=code[0];
				item.priceId=code[1];
				item.quantity=qulity;
				if(cartCode!=null && cartCode != '') {
					item.code= cartCode ;
				}
				items[i] = item;
				i++;
			}
			
		});	
		
		
			//if(items !=null && items.length>0 &&cartCode!=null) {
		
			//update cart
			
		
		//提交到购物车
		if(addflag==true){
			$('#pageContainer').showLoading();
			json_data = JSON.stringify(items);
			$.ajax({  
				 type: 'POST',  
				 url: getContextPath() + '/shop/cart/addShoppingCart.html',  
				 data: json_data, 
				 contentType: 'application/json;charset=utf-8',
				 dataType: 'json', 
				 cache:false,
				 error: function(e) { 
					 $('#pageContainer').hideLoading();
					 if(e.statusText == "OK"){
						 $('#cartModal').modal('show');
					 }else{
						// console.log('Error while adding to cart');
							$('#pageContainer').hideLoading();
							//alert('failure'); 
					 }
					 
				 },
				 success: function(cart) {
					 $('#pageContainer').hideLoading();
					 if (cart != null){
						 saveCart(cart.code);
					     
					     if(cart.message!=null) { 
					    	 //TODO error message
					    	 console.log('Error while adding to cart ' + cart.message);
					     }				 
						 displayShoppigCartItems(cart,'#shoppingcartProducts');
						 displayTotals(cart);
						 $('#cartModal').modal('show');
					 }
				 } 
			});
		}
	}
    function calculatePrice(productId, div) {

		
		var values = new Array();
		i = 0;
		$(div).find(':input').each(function(){
			if($(this).hasClass('attribute')) {
		        if($(this).is(':checkbox')) {
		        	var checkboxSelected = $(this).is(':checked');
		        	if(checkboxSelected==true) {
						values[i] = $(this).val();
						//console.log('checkbox ' + values[i]);
						i++;
					}
		        	
				} else if ($(this).is(':radio')) {
					var radioChecked = $(this).is(':checked');
					if(radioChecked==true) {
						values[i] = $(this).val(); 
						//console.log('radio ' + values[i]);
						i++;
					}
				} else {
				   if($(this).val()) {
				       values[i] = $(this).val(); 
				       //console.log('select ' + values[i]);
				       i++;
			       }

				}
			}
			
	});
		
	if(values.length==0) {
		return;
	}	
	
	$(div).showLoading();
		
	$.ajax({  
		 type: 'POST',  
		 url: getContextPath() + '/shop/product/' + productId + '/calculatePrice.html',  
		 dataType: 'json', 
		 data:{"attributeIds":values},
		 cache:false,
		 error: function(e) { 
			$(div).hideLoading();
			console.log('Error while loading price');
			 
		 },
		 success: function(price) {
			 $(div).hideLoading();
			 //console.log('product price ' + price.finalPrice);
			 var displayPrice = '<span itemprop="price">' + price.finalPrice + '</span>';
			 if(price.discounted) {
				 displayPrice = '<del>' + price.originalPrice + '</del>&nbsp;<span class="specialPrice"><span itemprop="price">' + price.finalPrice + '</span></span>';
			 }
			 $('#productPrice').html(displayPrice);
		 } 
	});

	
}
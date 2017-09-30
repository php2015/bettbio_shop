
 

	$(function(){
		
		initBindings();
		initMiniCart();
		//initSearch();

	});
	
	function initMiniCart() {
		var cartCode = getCartCode();
		//log('Cart code ' + cartCode);
		if(cartCode!=null) {
			displayMiniCartSummary(cartCode);
		}
		
	}
	
	function removeCart() {
		
		var cartCode = getCartCode();
		if(cartCode!=null) {
			emptyCartLabel();
			$.cookie('cart',null, { expires: 1, path:'/' });
		}
		
	}
	function initSearch(){
		//search();
		$("#searchInput").focus();
			
	}
	function doEzybioSearch(){
		$( "#searchForm").submit();
	}
	
	function doCategorySearch(cid){
		$( "#q").val("bababa");
		$( "#categoryid").val(cid);
		$( "#searchForm").submit();
	}
	function initBindings() {
		
		/** add to cart **/
		/**
		$(".addToCart").click(function(){
			addToCart($(this).attr("productId"));
	    });
		*/
    	$("#open-cart").hover(function(e) {
    		//log('Open cart');
    		displayMiniCart();
    	});    	   	
    	//$("#mychekbox").click(function () {  
    		//alert("aaa");
    		  
            //$("#mychekbox").removeAttr("checked");  
      
     //    });  
      
		
	}
	
	/**
	 * Function used for adding a product to the Shopping Cart
	 */
	function addToCart(sku,priceid) {
		$('#pageContainer').showLoading();
		var qty = '#qty-productId-' + sku + '-' + priceid;
		var quantity = $(qty).val();
		if(!quantity || quantity==null || quantity==0) {
			quantity = 1;
		}

		var formId = '#input-' + sku;
		//var $inputs = $(formId); 
		var $inputs = $(formId).find(':input');
		
		var values = new Array();
		if($inputs.length>0) {//check for attributes
			i = 0;
			$inputs.each(function() { //attributes
				if($(this).hasClass('attribute')) {
				    //if($(this).hasClass('required') && !$(this).is(':checked')) {
					//   		$(this).parent().css('border', '1px solid red'); 
				    //}
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
		}

		var cartCode = getCartCode();

		
		/**
		 * shopping cart code identifier is <cart>_<storeId>
		 * need to check if the cookie is for the appropriate store
		 */
		
		//cart item
		var prefix = "{";
		var suffix = "}";
		var shoppingCartItem = '';

		if(cartCode!=null && cartCode != '') {
			shoppingCartItem = '"code":' + '"' + cartCode + '"'+',';
		}
		var shoppingCartItem = shoppingCartItem + '"quantity":' + quantity + ',';
		var shoppingCartItem = shoppingCartItem + '"productId":' + sku + ',';
		var shoppingCartItem = shoppingCartItem + '"priceId":' + priceid;
		
		
		var attributes = null;
		//cart attributes
		if(values.length>0) {
			attributes = '[';
			for (var i = 0; i < values.length; i++) {
				var shoppingAttribute= prefix + '"attributeId":' + values[i] + suffix ;
				if(values.length>1 && i < values.length-1){
					shoppingAttribute = shoppingAttribute + ',';
				}
				attributes = attributes + shoppingAttribute;
			}
			attributes = attributes + ']';
		}
		
		if(attributes!=null) {
			shoppingCartItem = shoppingCartItem + ',"shoppingCartAttributes":' + attributes;
		}
		
		var scItem = prefix + shoppingCartItem + suffix;

		/** debug add to cart **/
		//console.log(scItem);

		$.ajax({  
			 type: 'POST',  
			 url: getContextPath() + '/shop/cart/addShoppingCartItem.html',  
			 data: scItem, 
			 contentType: 'application/json;charset=utf-8',
			 dataType: 'json', 
			 cache:false,
			 error: function(e) { 
				 $('#pageContainer').hideLoading();
				 if(e.statusText == "OK"){
					 $('#cartModal').modal('show');
				 }else{
					 console.log('Error while adding to cart');
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
	
function removeLineItem(lineItemId){
	$( "#shoppingCartRemoveLineitem_"+lineItemId).submit();		
}

function updateLineItem(lineItemId,actionURL){
	$("#shoppingCartLineitem_"+lineItemId).attr('action', actionURL);
	$( "#shoppingCartLineitem_"+lineItemId).submit();	
}

//update full cart
function updateCart(cartDiv) {
	$('.alert-error').hide();
	$('.quantity').removeClass('required');
	$('#mainCartTable').showLoading();
	
	var items = new Array();
	var cartCode = getCartCode();
	
		var i=0;
		$(":checkbox:checked").each(function(){
		var item = new Object();
		  var tablerow = $(this).parent().parent("tr").find("[name='quantity']");
		  var qty = tablerow.val();
			if(qty>0) {
				var id = tablerow.attr("id");
				item.id = id;
				item.quantity = qty;
				item.code=cartCode;
				items[i] = item;
				i++;
			}
		});
	
	
		if(items !=null && items.length>0 &&cartCode!=null) {
	
		//update cart
		json_data = JSON.stringify(items);

		$.ajax({  
			 type: 'POST',  
			 url: getContextPath() + '/shop/cart/updateShoppingCartItem.html',
			 data: json_data,
			 contentType: 'application/json;charset=utf-8',
			 dataType: 'json', 
			 cache:false,
			 error: function(e) { 
				 console.log('error ' + e);
				 $('#mainCartTable').hideLoading();
			 },
			 success: function(tabledata) {
				 initShopingTable(tabledata);
				 $('#mainCartTable').hideLoading();
			} 
		});
		
	}	
}

function displayMiniCart(){
	var cartCode = getCartCode();
	
	//log('Display cart content');
	
	
	
	
	$('#shoppingcartProducts').html('');
	$('#cart-box').addClass('loading-indicator-overlay');/** manage manually cart loading**/
	$('#cartShowLoading').show();

	$.ajax({  
		 type: 'GET',  
		 url: getContextPath() + '/shop/cart/displayMiniCartByCode.html?shoppingCartCode='+cartCode,  
		 cache:false,
		 error: function(e) { 
			 $('#cart-box').removeClass('loading-indicator-overlay');/** manage manually cart loading**/
			 $('#cartShowLoading').hide();
			 console.log('error ' + e);
			 //nothing
			 
		 },
		 success: function(miniCart) {			
			 if($.isEmptyObject(miniCart)){
				 emptyCartLabel();
			 }
			 else{
				 displayShoppigCartItems(miniCart,'#shoppingcartProducts');//cart content
				 displayTotals(miniCart);//header
			 }
			 $('#cart-box').removeClass('loading-indicator-overlay');/** manage manually cart loading**/
			 $('#cartShowLoading').hide();
		} 
	});
}



 /**
  * JS function responsible for removing give line item from
  * the Cart.
  * For more details see MiniCartController.
  * 
  * Controller will return JSON as response and it will be parsed to update
  * mini-cart section.
  * @param lineItemId
  */
function removeItemFromMinicart(lineItemId){
	
	shoppingCartCode = getCartCode();
	$.ajax({  
		 type: 'GET',
		 cache:false,
		 url: getContextPath() + '/shop/cart/removeMiniShoppingCartItem.html?lineItemId='+lineItemId + '&shoppingCartCode=' + shoppingCartCode,  
		 error: function(e) { 
			 console.log('error ' + e);
			 
		 },
		 success: function(miniCart) {
			 if(miniCart==null) {
				 emptyCartLabel();
			 } else {
				 displayShoppigCartItems(miniCart,'#shoppingcartProducts');
				 displayTotals(miniCart);
			 }
		} 
	});
}

function displayMiniCartSummary(code){
	$.ajax({  
		 type: 'GET',  
		 url: getContextPath() + '/shop/cart/displayMiniCartByCode.html?shoppingCartCode='+code,  
		 error: function(e) { 
			// do nothing
			// alert(e);
			console('error while getting cart');
			 
		 },
		 success: function(cart) {
			 if(cart==null || cart=='') {
					emptyCartLabel();
					$.cookie('cart',null, { expires: 1, path:'/' });
			 } else {
				 displayTotals(cart);
			 }
		} 
	});
}





function viewShoppingCartPage(){
	window.location.href=getContextPath() + '/shop/cart/shoppingCart.html';
	
}

 
function displayShoppigCartItems(cart, div) {
	
	 
	//set cart contextPath
	cart.contextPath=getContextPath(); 
	var template = Hogan.compile(document.getElementById("miniShoppingCartTemplate").innerHTML);
    
	 $(div).html('');
	 if(cart.subOrders==null) {
		 emptyCartLabel();
		 return;
	 }
	 for(var i=0; i<cart.subOrders.length;i++){
		 $(div).append(template.render(cart.subOrders[i]));
	 }
	 
	 $('#cartMessage').hide();
	 $('#shoppingcart').show();

	 //call template defined in template directory
	 //$(div).append(template.render(cart));


}

function displayTotals(cart) {
	if(cart.quantity==0) {
		emptyCartLabel();
	} else {
		cartInfoLabel(cart);
		$('#total-box').html(cartSubTotal(cart));
	}


}


/** returns the cart code **/
function getCartCode() {
	var cart = $.cookie("cart"); //should be [storecode_cartid]
	var code = new Array();
	
	if(cart!=null) {
		code = cart.split('_');
		if(code[0]==getMerchantStoreCode()) {
			return code[1];
		}
	}
}

function buildCartCode(code) {
	var cartCode = getMerchantStoreCode() + '_' + code;
	return cartCode;
}

function saveCart(code) {
	var cartCode = buildCartCode(code);
	$.cookie('cart',cartCode, { expires: 1024, path:'/' });
}

function initShopingTable(data){
	if(null != data){
		var carItems = data.shoppingCartItems;
		if(null != carItems && carItems.length>0){
			var table = document.getElementById("mainCartTable");
			
			if(table.getElementsByTagName('tbody')[0] != null){
				table.removeChild(table.getElementsByTagName('tbody')[0]);
			}
			
			var tbody = document.createElement("tbody");		
			for (var i = 0; i < carItems.length; i++) {			
				var tr = tbody.insertRow(i) ;
				tr.innerHTML='<td> <input type="checkbox" name="check" checked="checked"></td><td width="10%">';
				if(carItems[i].image!=null){
					tr.innerHTML = tr.innerHTML + '<img width="60" src="<c:url value="'+carItems[i].image+'"/>">';
				}
				tr.innerHTML = tr.innerHTML +'</td><td style="border-left:none;"><strong>' + carItems[i].name + '</strong>';
				if(carItems[i].shoppingCartAttributes.length>0){
					tr.innerHTML = tr.innerHTML + '<br/><ul>';
					for(j=0; j< carItems[i].shoppingCartAttributes.length;j++){
						tr.innerHTML = tr.innerHTML + '<li>' + carItems[i].shoppingCartAttributes[j].optionName +'-' + carItems[i].shoppingCartAttributes[j].optionValue + '</li>';
					}
					tr.innerHTML = tr.innerHTML + '</ul></td>';
				}
				tr.innerHTML = tr.innerHTML + '<td><input type="text" class="input-small quantity form-control" placeholder="<s:message code="label.quantity" text="Quantity"/>"';
				tr.innerHTML = tr.innerHTML + 'value="' + carItems[i].quantity + '" name="quantity" id="' + carItems[i].id +'"';
				
				tr.innerHTML = tr.innerHTML + '></td><td style="border-left:none;"><button class="close" onclick="javascript:updateLineItem(' + carItems[i].id + ',"<c:url value="/shop/cart/removeShoppingCartItem.html" />"'; 
				tr.innerHTML = tr.innerHTML + '");">&times;</button></td><td><strong>' + carItems[i].price + '</strong></td><td><strong>';
				tr.innerHTML = tr.innerHTML + carItems[i].subTotal + '</strong></td>';
				
			}
			var totals = data.totals;
			for(var k=0; k<totals.length; k++){
				var tr = tbody.insertRow(carItems.length + k) ;
				tr.innerHTML = '<tr class="subt"><td colspan="5">&nbsp;</td><td colspan="1"><strong><s:message code="'+ totals[k].code +'" text="label [' + totals[k].code ;
				tr.innerHTML = tr.innerHTML + '] not found" /></strong></td><td><strong><sm:monetary value="' + totals[k].value + '" /></strong></td></tr>';
			}
			
			if(tbody !=null){			
				table.appendChild(tbody); 
			}
		}
	}
}
function recuculc(){
	
	var summeony=0.00;
	$(":checkbox:checked").each(function(){
		
		var item = new Object();
		  var tablerow = $(this).parent().parent("tr").find("[name='totalmeony']");
		  var tablequnity = $(this).parent().parent("tr").find("[name='quantity']");
		  var tableprice = $(this).parent().parent("tr").find("[name='price']");
		  var qty = tablerow.text();
		  if(null != qty && qty.length>0){
			  // Remove non-numeric chars (except decimal point/minus sign):
			    var priceVal = parseFloat(tableprice.text().replace(/[^0-9-.]/g, '')); // 12345.99
			    var everytotal = priceVal*tablequnity.val();
			    tablerow.text(everytotal.formatMoney());
			  summeony += everytotal;
			  //i++;
		  }	
		});
	var totalmoneys = document.getElementsByName("totalmoneyculc");
	
	if(totalmoneys !=null) {
		for(var j = 0; j< totalmoneys.length; j++) {
			totalmoneys[j].innerHTML='<strong >' + summeony.formatMoney() +'</strong >';
		} 
	}
};

Number.prototype.formatMoney = function (places, symbol, thousand, decimal) {
    places = !isNaN(places = Math.abs(places)) ? places : 2;
    symbol = symbol !== undefined ? symbol : "￥";
    thousand = thousand || ",";
    decimal = decimal || ".";
    var number = this,
        negative = number < 0 ? "-" : "",
        i = parseInt(number = Math.abs(+number || 0).toFixed(places), 10) + "",
        j = (j = i.length) > 3 ? j % 3 : 0;
    return symbol + negative + (j ? i.substr(0, j) + thousand : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + thousand) + (places ? decimal + Math.abs(number - i).toFixed(places).slice(2) : "");
};

function isEzybioFormValid(firstErrorMessage) {
	var $inputs = $('#ezybioForm').find(':input');
	var valid = true;
	if( firstErrorMessage != null && firstErrorMessage!=''){
		$(".alert-error").html(firstErrorMessage);
		$(".alert-error").show();
		return false; 
	}
	$inputs.each(function() {
		
		//if has clsaa phone
		if($(this).hasClass('phone')) {	
			var phoneValid = validatePhone($(this).val());
			//console.log('Email is valid ? ' + emailValid);
			if(!phoneValid) {
				$(".alert-error").html(getInvalidPhoneMessage());
				valid = false;
				return false;
			}
		}
		
		//if has class email
		if($(this).hasClass('email')) {	
			var emailValid = validateEmail($(this).val());
			if(!emailValid) {
				$(".alert-error").html(getInvalidEmailMessage());
				valid = false;
				return false;
			}
		}
		//password rules
		if($(this).hasClass('password')) {				
			if(checkStrong($(this).val())<3) {
				$(".alert-error").html(getWeekPasswordMessage());
				valid = false;
				return false;
			}
		}
		
		if($(this).hasClass('required')) {				
			var fieldValid = isEzybioFieldValid($(this));
			if(!fieldValid) {
				$(".alert-error").html($(this).attr('title'));
				valid = false;
				return false; 
			}
		}
		
		
	});
	
	
	if(valid==false) {//disable submit button
		$('#Ezybiosubmit').addClass('btn-disabled');
		$('#Ezybiosubmit').prop('disabled', true);
		$(".alert-error").show();
		$(".alert-success").hide();
	} else {
		$('#Ezybiosubmit').removeClass('btn-disabled');
		$('#Ezybiosubmit').prop('disabled', false);
		$(".alert-error").hide();
		
	}
}


function isEzybioFieldValid(field) {
	var validateField = true;
	
	var fieldId = field.prop('id');
	var value = field.val();
	
	if(!emptyString(value)) {
		field.css('background-color', '#FFF');
		return true;
	} else {
		field.css('background-color', '#FFC');
		return false;
	} 
}

function validateEmail($email) {
	  var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
	  if ( $email.length > 0 && emailReg.test($email)) {
	    return true;
	  } else {
	    return false;
	  }
}

function validatePhone($phone){
	var  phoneReg = /^1\d{10}$/;
	if ($phone.length > 0 && phoneReg.test($phone)) {
		return true;
	}else{
		return false;
	}
}

function emptyString($value) {
	return !$value || !/[^\s]+/.test($value);
}

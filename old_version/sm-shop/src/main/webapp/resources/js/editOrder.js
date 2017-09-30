var OrderHandler = new function() {
	var me = this;
	var adjustmentData = {suborder:{}};
	var hasInvalidPrice = false;
	function dumpData(){
		//alert(JSON.stringfiy(adjustmentData, "  ", null));
		$('#debug_pre').text(JSON.stringify(adjustmentData, null, "  "));
	}
	function formatPrice(price){
		s = parseFloat((price + "").replace(/[^\d\.-]/g, "")).toFixed(2) + "";
		var l = s.split(".")[0].split("").reverse(), r = s.split(".")[1];
		t = "";
		for (i = 0; i < l.length; i++) {
			t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
		}
		return t.split("").reverse().join("") + "." + r;
	}
	
	function getSubOrderRecord(sId){
		if (adjustmentData.suborder[sId]){
			return adjustmentData.suborder[sId];
		}
		adjustmentData.suborder[sId] = {id:sId, priceType:'NORMAL', value:0, items:{}};
		return adjustmentData.suborder[sId];
	}
	function getItemRecord(sId, itemId){
		var subOrderRecord = getSubOrderRecord(sId);
		if (subOrderRecord.items[itemId]){
			return subOrderRecord.items[itemId];
		}
		subOrderRecord.items[itemId] = {id:itemId, priceType:'NORMAL', value:0};
		return subOrderRecord.items[itemId];
	}
	
	function setItemChangeType(subOrderId, itemId, changeType, newValue){
		var itemRecord = getItemRecord(subOrderId, itemId);
		itemRecord.priceType = changeType;
		itemRecord.value = newValue;
	}
	function setSubOrderChangeType(subOrderId, changeType, newValue){
		var subOrderRecord = getSubOrderRecord(subOrderId);
		subOrderRecord.priceType = changeType;
	}
	function setSubOrderChangeValue(subOrderId, newValue){
		var subOrderRecord = getSubOrderRecord(subOrderId);
		subOrderRecord.value = newValue;
	}
	function refreshItemTotal(itemId, newTotal){
		var inptObj = $('#itemTotalPrice_'+itemId+'_input');
		var oldValue = parseFloat(inptObj.data("init-value"));
		inptObj.val(formatPrice(newTotal));
		if (oldValue == newTotal){
			inptObj.removeClass("adjust_price_affected");
			inptObj.removeClass("adjust_price_modified");
		}else{
			inptObj.addClass("adjust_price_affected");
			inptObj.removeClass("adjust_price_modified");
		}
		inptObj.data("cur-value", newTotal);
	}
	function refreshSubOrderTotal(sId){
		var itemInputs = $('#tbody_suborder_'+sId).find(".itemTotal");
		var total = 0;
		for(var i=0;i<itemInputs.length;i++){
			var totalElem = $(itemInputs[i]);
			var itemTotal = totalElem.data("cur-value");
			total += parseFloat(itemTotal);
		}
		var subTotalElem = $('#subTotal_'+sId+'_input');
		subTotalElem.val(formatPrice(total));
		var oldValue = parseFloat(subTotalElem.data("init-value"));
		var lastValue = subTotalElem.data("cur-value");
		subTotalElem.data("cur-value", total);
		
		if (oldValue == total){
			setSubOrderChangeType(sId, "NORMAL");
			subTotalElem.removeClass('adjust_price_affected');
			subTotalElem.removeClass('adjust_price_modified');
		}else{
			subTotalElem.addClass('adjust_price_affected');
			subTotalElem.removeClass('adjust_price_modified');
			setSubOrderChangeType(sId, "CALCULATED");
		}
		return parseFloat(lastValue);
	}
	function refreshOrderTotal(subOrderId, lastValue){
		var curOrderTotal = parseFloat($('#order_total_span').data("cur-value"));
		var subTotalElem = $('#subTotal_'+subOrderId+'_input');
		var newSubTotal = parseFloat(subTotalElem.data("cur-value"));
		curOrderTotal = curOrderTotal+newSubTotal-lastValue;
		$('#order_total_span').data("cur-value", curOrderTotal);
		$('#order_total_span').text("¥"+formatPrice(curOrderTotal));
	}
	
	me.formatMoney = function(inputObj){
		
		var value = $(inputObj).val();
		value = value.replace(/,/g, '');
		if (value == '' || isNaN(value)){
			hasInvalidPrice = true;
			alert("必须输入一个正确的金额");
			setTimeout(function () {$(inputObj).focus();},100);
			return false;
		}
		value = parseFloat(value);
		if (value < 0){
			hasInvalidPrice = true;
			alert("目前订单中的金额不能是负数");
			setTimeout(function () {$(inputObj).focus();},100);
			return false;
		}
		$(inputObj).val(formatPrice(value));
		return true;
	}
	me.onAdjustUnitPrice = function(subOrderId, itemId){
		//alert("Change Item "+subOrderId+"==("+itemId);
		var inptObj = $('#unitPrice_'+itemId+'_input');
		var value = inptObj.val();
		value = value.replace(/,/g, '');
		if (value == '' || isNaN(value)){
			return;
		}
		hasInvalidPrice = false;
		value = parseFloat(value);
		var oldValue = parseFloat(inptObj.data("init-value"));
		
		// 1. update item-total, set as 'affected'
		// 2. set this item as "adjusted"
		// 3. recalculate suborder total
		// 4. recalculate order total
		// 5. set suborder as 'calculated'
		// 6. remember new item-total
		var qty = inptObj.data('qty');
		var newTotal = value * qty;
		if (oldValue == value){
			inptObj.removeClass("adjust_price_affected");
			inptObj.removeClass("adjust_price_modified");
			setItemChangeType(subOrderId, itemId, 'NORMAL', value);
		}else{
			inptObj.removeClass("adjust_price_affected");
			inptObj.addClass("adjust_price_modified");
			setItemChangeType(subOrderId, itemId, 'CALCULATED', value);
		}
		refreshItemTotal(itemId, newTotal);
		var lastValue = refreshSubOrderTotal(subOrderId);
		refreshOrderTotal(subOrderId, lastValue);
				
		dumpData();
	}
	me.onAdjustItemTotalPrice = function(subOrderId, itemId){
		//alert("Change item-total "+subOrderId+"==("+itemId);
		var inptObj = $('#itemTotalPrice_'+itemId+'_input');
		var value = inptObj.val();
		value = value.replace(/,/g, '');
		if (value == '' || isNaN(value)){
			return;
		}
		hasInvalidPrice = false;
		value = parseFloat(value);
		var oldValue = parseFloat(inptObj.data("init-value"));
		var qty = inptObj.data('qty');
		
		// 清除单价修改
		var inptUnitObj = $('#unitPrice_'+itemId+'_input');
		inptUnitObj.val(formatPrice(parseFloat(inptUnitObj.data("init-value"))));
		inptUnitObj.removeClass("adjust_price_affected");
		inptUnitObj.removeClass("adjust_price_modified");
		
		// 本身值更新
		inptObj.data("cur-value", value);
		if (oldValue == value){
			inptObj.removeClass("adjust_price_affected");
			inptObj.removeClass("adjust_price_modified");
			setItemChangeType(subOrderId, itemId, 'NORMAL', value);
		}else{
			inptObj.removeClass("adjust_price_affected");
			inptObj.addClass("adjust_price_modified");
			setItemChangeType(subOrderId, itemId, 'ADJUSTED', value);
		}
		var lastValue = refreshSubOrderTotal(subOrderId);
		refreshOrderTotal(subOrderId, lastValue);
				
		dumpData();
	}
	me.onAdjustSubtoal = function(subOrderId){
		//alert("Change suborder "+subOrderId);
		var inptObj = $('#subTotal_'+subOrderId+'_input');
		var value = inptObj.val();
		value = value.replace(/,/g, '');
		if (value == '' || isNaN(value)){
			return;
		}
		hasInvalidPrice = false;
		value = parseFloat(value);
		var oldValue = parseFloat(inptObj.data("init-value"));
		var lastValue = parseFloat(inptObj.data("cur-value"));
		
		// 清除所有的条目改价
		var itemInputs = $('#tbody_suborder_'+subOrderId).find(".item");
		var total = 0;
		for(var i=0;i<itemInputs.length;i++){
			var itemElem = $(itemInputs[i]);
			itemElem.removeClass("adjust_price_affected");
			itemElem.removeClass("adjust_price_modified");
			var itemId = itemElem.data("item-id");
			var initValue = parseFloat(itemElem.data("init-value"));
			itemElem.val(formatPrice(initValue));
			setItemChangeType(subOrderId, itemId, 'NORMAL', initValue);
		}
		
		// 本身值更新
		inptObj.data("cur-value", value);
		if (value == oldValue){
			inptObj.removeClass("adjust_price_affected");
			inptObj.removeClass("adjust_price_modified");
			setSubOrderChangeType(subOrderId, "NORMAL");
		}else{
			inptObj.removeClass("adjust_price_affected");
			inptObj.addClass("adjust_price_modified");
			setSubOrderChangeType(subOrderId, "ADJUSTED");
			setSubOrderChangeValue(subOrderId,value);
		}
		refreshOrderTotal(subOrderId, lastValue);
				
		dumpData();
	}
	
	me.setOrderId = function(id){
		adjustmentData.oid = id;
	}
	me.savePrice = function(){
		var subOrders = adjustmentData.suborder;
		if ($.isEmptyObject(subOrders)){
			alert("你还没有任何价格被调整过");
			return;
		}
		if (hasInvalidPrice){
			//alert("请先输入正确的价格金额");
			return;
		}
		$('#order_detail_div').showLoading();
		$.ajax({  
			type: 'POST',
			url: savePriceUrl,
			mimeType:"application/json; charset=UTF-8",
			contentType: 'application/json',
			dataType: 'json',
			data:JSON.stringify(adjustmentData),
			success: function(response) {
				//alert("OK");
				$('#order_detail_div').hideLoading();
				location.replace(location.href);
			} ,
			error: function( textStatus, errorThrown) {
				$('#order_detail_div').hideLoading();
				alert("更新价格失败，请检查是否有权限操作此订单");
			}
		});
	}
	function invokeOrderAction(subOrderId, actionName){
		$('#order_detail_div').showLoading();
		$.ajax({  
			type: 'POST',
			url: orderActionUrl+"actionEvent.html",
			data:"soid="+subOrderId+"&actionName="+actionName,
			dataType:"json",
			success: function(response) {
				$('#order_detail_div').hideLoading();
				if (typeof response.response == 'undefined'){
					alert("操作失败，请重新登录");
					return;
				}
				response = response.response;
				if (response.status == 0){
					location.replace(location.href);
					return;
				}
				if (response.message != null){
					alert("操作失败: "+response.message);
					return;
				}
				if (response.statusMessage != null){
					alert("操作失败: "+response.statusMessage);
					return;
				}
				var errMsg="操作失败。\n  代码"+response.code+", 类型"+response.messageKey+"\n  参数[";
				for(var i=1;i<10;i++){
					if (typeof response['messageParam'+i] == "string"){
						errMsg+=response['messageParam'+i]+",";
					}
				}
				errMsg += "]";
				alert(errMsg);
			} ,
			error: function( textStatus, errorThrown) {
				$('#order_detail_div').hideLoading();
				alert("操作失败，请检查是否有权限操作此订单");
			}
		});
	}
	function confirmOrderPaid(subOrderId){
		if (!confirm("确认已经收到订单 "+subOrderId+" 的货款了么？")){
			return;
		}
		invokeOrderAction(subOrderId, "confirm_pay");
	}
	function resetOrder(subOrderId){
		if (!confirm("确认重置订单 "+subOrderId+" 么？")){
			return;
		}
		invokeOrderAction(subOrderId, "reset_order");
	}
	function cancelOrder(subOrderId){
		if (!confirm("确认取消订单 "+subOrderId+" 么？")){
			return;
		}
		invokeOrderAction(subOrderId, "cancel_order");
	}
	function closeOrder(subOrderId){
		if (!confirm("确认立即关闭订单 "+subOrderId+" 么？")){
			return;
		}
		invokeOrderAction(subOrderId, "close_order");
	}
	function confirmReceiptOrder(subOrderId){
		if (!confirm("注意！此操作是代替买家进行的。\n确认订单 "+subOrderId+" 已收货了么？")){
			return;
		}
		invokeOrderAction(subOrderId, "confirm_receipt");
	}
	function confirmOrder(subOrderId){
		if (!confirm("注意！确认订单后将不能再修改价格。\n确认订单 "+subOrderId+" 么？")){
			return;
		}
		invokeOrderAction(subOrderId, "confirm_order");
	}
	me.doOrderAction = function(subOrderId, actionName){
		if (hasInvalidPrice){
			return;
		}
		if (actionName == "confirm_pay"){
			confirmOrderPaid(subOrderId);
		}else if (actionName == "reset_order"){
			resetOrder(subOrderId);
		}else if (actionName == "cancel_order"){
			cancelOrder(subOrderId);
		}else if (actionName == "close_order"){
			closeOrder(subOrderId);
		}else if (actionName == "confirm_receipt"){
			confirmReceiptOrder(subOrderId);
		}else if (actionName == "confirm_order"){
			confirmOrder(subOrderId);
		}else if (actionName == "ship_order"){
			doDelivery(subOrderId);
		}else{
			alert("Do " + actionName + " on " + subOrderId);
		}
	}
}
var BPOUtils =  new function() {
	var me = this;
	var passedWaitTime = 0;
	
	me.getRadioValue = function(inputName){
		return $('input[name="'+inputName+'"]:checked').val();
	}
	me.getStringArray = function (inputId){
		return $.trim($("#"+inputId).val()).split(/ *[\r\n]+ */)
	}
	me.logJson = function(jsonStr){
		$('#debug_div').html("<pre>" + JSON.stringify(jsonStr,"","  ")+"</pre>");
	}
	
	me.showLoading = function () {
		$('#modal-loading').modal("show");
	}
	me.closeLoading = function () {
		$('#modal-loading').modal("hide");
	}
	me.startsWith = function (strInput, strFind){
		return strInput.substr(0, strFind.length) == strFind;
	}
	me.getTimeText = function(timeSecs){
		var result = "";
		var secs = Math.floor(timeSecs) % 60;
		var mins = Math.floor(Math.floor(timeSecs / 60) % 60);
		var hours = Math.floor(timeSecs/3600);
		result = hours +":"+(mins>=10?mins:"0"+mins)+":"+(secs>=10?secs:"0"+secs);
		return result;
	}
	me.calcLeftTime = function(total, processed, passedTime, skippedNumber){
		if ((processed-skippedNumber) <= 0){
			passedWaitTime = passedTime;
		}

		if (total == 0){
			return "0:00:00";
		}
		if ((processed-skippedNumber)/total < 0.005 || passedTime < 5000){
			var estimateTime = me.getTimeText(total/ 2048000 * 9000 - passedTime/1000);
			return estimateTime;
		}
		var leftNum = total-processed;
		var passedRate = (processed-skippedNumber)/(passedTime-passedWaitTime+1);
		var leftTimeMs = leftNum / passedRate;
		return me.getTimeText(leftTimeMs/1000);
	}
}

var BatchInvalidProducts = new function(){
	var me = this;
	var pollingTimer;

	me.initProcess = function(){
		$("#batch_invalid_products_form")[0].reset();
		me.showOptionType();
	}
	
	me.getConfirmMsg = function() {
		return "注意：确认执行将会下架符合条件的产品。\r\n 请仔细核对列出的产品数和商家的产品数。\r\n\r\n确认下架这些产品么？";
	}
	me.verifyOperationSettings = function(successCallBack){
		var args = {
			brandOperation: BPOUtils.getRadioValue("bip_arg_brand_action"),
			merchantOperation: BPOUtils.getRadioValue("bip_arg_merchant_action"),
			brandNames : BPOUtils.getStringArray("arg_remove_brands"),
			merchantNames : BPOUtils.getStringArray("arg_remove_merchant")
		};
		BPOUtils.logJson(args);
		if (args.brandNames.length == 1 && args.brandNames[0] == ''){
			alert("必须输入至少一个品牌名称");
			return;
		}
		if (args.merchantNames.length == 1 && args.merchantNames[0] == ''){
			alert("必须输入至少一个商家名称");
			return;
		}
		BPOUtils.showLoading();
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/batch/verifyBIPArgs.html',
			cache: false,
			mimeType:"application/json; charset=UTF-8",
			'contentType': 'application/json',
			'dataType': 'json',
			data: JSON.stringify(args),
			success: function(bodyStr) {
			  
			  if (bodyStr.response.status == -2){
				  BPOUtils.closeLoading();
				  var errMsg = bodyStr.response.statusMessage;
				  if (typeof bodyStr.response.invalid_brands == "string"){
					  errMsg = errMsg +"\n无效的品牌: " + bodyStr.response.invalid_brands;
				  }
				  if (typeof bodyStr.response.invalid_merchants == "string"){
					  errMsg = errMsg +"\n无效的商家: " + bodyStr.response.invalid_merchants;
				  }
				  alert(errMsg);
				  return;
			  }else if (bodyStr.response.status == -1){
				  BPOUtils.closeLoading();
				  alert("暂不支持该操作");
				  return;
			  }else if (bodyStr.response.status == 0){
				  successCallBack();
				  return;
			  }
			} ,
			error: function( textStatus, errorThrown) {
				BPOUtils.closeLoading();
				alert(JSON.stringify(textStatus));
			}
		});
	}
	
	me.showOptionType = function() {
		var typeBrand = BPOUtils.getRadioValue("bip_arg_brand_action");
		var typeMerchant = BPOUtils.getRadioValue("bip_arg_merchant_action");
		var operationType = "";
		if (typeBrand == "when" && typeMerchant== "except"){
			operationType = "下架除指定商家外的，所有其他商家的指定品牌的产品。结果是只有指定商家才有该品牌的产品";
		}else if (typeBrand == "when" && typeMerchant== "when"){
			operationType = "下架指定商家的，指定品牌的产品。结果是指定的商家将不再有该品牌的产品";
		}else if (typeBrand == "except" && typeMerchant== "except"){
			operationType = "下架除指定商家以外的，除指定品牌以外的产品。结果是其他商家都只有这个品牌的产品了。通常很少使用。";
		}else { // except & when
			operationType = "下架指定商家的，除指定品牌外的产品。即该商家将只有指定品牌的产品，其他产品全部下架";
		}
		$('#bip_mode_description').text(operationType);
		$('#sumary_biptype').text(operationType);
	}
	
	me.viewBatchOperationSummary = function(){

		var args = {
			brandOperation: BPOUtils.getRadioValue("bip_arg_brand_action"),
			merchantOperation: BPOUtils.getRadioValue("bip_arg_merchant_action"),
			brandNames : $.trim($("#arg_remove_brands").val()).split(/ *[\r\n]+/),
			merchantNames : $.trim($("#arg_remove_merchant").val()).split(/ *[\r\n]+/)
		};
		$('#sumary_error').text("无");
		$('#sumary_totalNum').text("未知");
		$('#sumary_invalidNum').text("未知");
		$('#sumary_each_merchant').html("");
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/batch/showSummary.html',
			cache: false,
			'contentType': 'application/json',
			'dataType': 'json',
			data: JSON.stringify(args),
			success: function(bodyStr) {
				BPOUtils.closeLoading();
				if (bodyStr.response.status == 0){
					$('#sumary_error').text(bodyStr.response.ERROR || "无");
					$('#sumary_totalNum').text(bodyStr.response.totalProducts || "未知");
					$('#sumary_invalidNum').text(bodyStr.response.affectedProducts || "未知");
					var tbodyObj = $('#sumary_each_merchant');
					tbodyObj.html("");
					var data = bodyStr.response;
					for(var merchantName in data){
						if (merchantName == 'ERROR' || merchantName == 'totalProducts' || merchantName == 'affectedProducts' || merchantName == 'status'){
							continue;
						}
						var trObj = $('<tr><td>'+merchantName+'</td><td>'+data[merchantName]+'</td></tr>');
						tbodyObj.append(trObj);
					}
					return;
				}else{
				  alert("暂不支持该操作");
				  return;
				}
			} ,
			error: function( textStatus, errorThrown) {
				BPOUtils.closeLoading();
				alert(JSON.stringify(textStatus));
			}
		});
	}
	
	function pollingExecuteStatus() {
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/batch/queryExxcuteStatus.html',
			cache: false,
			'contentType': 'application/json',
			'dataType': 'json',
			data: {},
			success: function(bodyStr) {
				var total = parseFloat(bodyStr.response.total);
				$("#status_total").text(bodyStr.response.total);
				var proed = parseFloat(bodyStr.response.processed);
				$("#status_processed").text(bodyStr.response.processed);
				var width = 365 * proed / total;
				$('#status_percent_bar').css('width', width);
				if (bodyStr.response.status == 9999){
					window.clearInterval(pollingTimer);
					$('#status_title').text("操作完成");
					$("#bipbtn_close").attr("disabled", false);
					return;
				}else{
					$('#status_title').text("正在下架产品。。。");
				  
				  return;
				}
			} ,
			error: function( textStatus, errorThrown) {
				alert(JSON.stringify(textStatus));
			}
		});
	}
	
	me.executeBatchOperation = function(){
		$("#status_total").text("0");
		$("#status_processed").text("0");
		var args = {
			brandOperation: BPOUtils.getRadioValue("bip_arg_brand_action"),
			merchantOperation: BPOUtils.getRadioValue("bip_arg_merchant_action"),
			brandNames : $.trim($("#arg_remove_brands").val()).split(/ *[\r\n]+/),
			merchantNames : $.trim($("#arg_remove_merchant").val()).split(/ *[\r\n]+/)
		};
		BPOUtils.showLoading();
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/batch/bipexecution.html',
			cache: false,
			'contentType': 'application/json',
			'dataType': 'json',
			data: JSON.stringify(args),
			success: function(bodyStr) {
				BPOUtils.closeLoading();
				if (bodyStr.response.status == 9997){
					pollingTimer = window.setInterval(pollingExecuteStatus, 500);
					$("#bipbtn_close").attr("disabled", true);
					return;
				}else{
					alert("还有其他操作未完成");
					return;
				}
			} ,
			error: function( textStatus, errorThrown) {
				BPOUtils.closeLoading();
				alert(JSON.stringify(textStatus));
			}
		});
	}
}
var BatchSqlInvalidProducts = new function(){
	var me = this;
	var pollingTimer;
	
	function getOperationArg() {
		return {
			operation: BPOUtils.getRadioValue("sql_arg_operation"),
			sql_input : $('#sql_arg_statement').val().replace(/[ \r\n\t]+/g, " ")
		};
	}
	me.getConfirmMsg = function() {
		return "注意：确认执行将会对产品数据进行操作。\r\n请仔细核对列出的产品数和商家的产品数。\r\n\r\n确认执行操作么？";
	}
	me.initProcess = function(){
		$("#batch_sql_invalid_products_form")[0].reset();
	}
	
	me.verifyOperationSettings = function(successCallBack){
		var args = getOperationArg();
		BPOUtils.logJson(args);
		BPOUtils.showLoading();
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/batch/verifySQLStatment.html',
			cache: false,
			mimeType:"application/json; charset=UTF-8",
			'contentType': 'application/json',
			'dataType': 'json',
			data: JSON.stringify(args),
			success: function(bodyStr) {
			  
				if (bodyStr.response.status == -1){
					BPOUtils.closeLoading();
					var errMsg = bodyStr.response.ERROR;
					if (typeof bodyStr.response.stacktrace == "string"){
						errMsg = errMsg +"\n" + bodyStr.response.stacktrace;
					}
					alert(errMsg);
					return;
				}else if (bodyStr.response.status == 0){
					BPOUtils.closeLoading();
					$('#sql_sumary_optype').text(args.operation);
					$('#sql_sumary_error').text(bodyStr.response.ERROR || "无");
					$('#sql_sumary_totalNum').text(bodyStr.response.totalProducts || "未知");
					if (typeof bodyStr.response.stacktrace == "string"){
						$('#sql_sumary_exception').text(bodyStr.response.stacktrace);
						$('#sql_sumary_exception').show();
					}else{
						$('#sql_sumary_exception').hide();
					}
					successCallBack();
					return;
				}
			} ,
			error: function( textStatus, errorThrown) {
				BPOUtils.closeLoading();
				alert(JSON.stringify(textStatus));
			}
		});
	}
	
	me.viewBatchOperationSummary = function(){}
	
	function pollingExecuteStatus() {
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/batch/queryExxcuteStatus.html',
			cache: false,
			'contentType': 'application/json',
			'dataType': 'json',
			data: {},
			success: function(bodyStr) {
				var total = parseFloat(bodyStr.response.total);
				$("#sql_status_total").text(bodyStr.response.total);
				var proed = parseFloat(bodyStr.response.processed);
				$("#sql_status_processed").text(bodyStr.response.processed);
				var width = 365;
				if (total != 0) {
					width = 365 * proed / total;
				}
				$('#sql_status_percent_bar').css('width', width);
				if (bodyStr.response.status == 9999){
					window.clearInterval(pollingTimer);
					$('#sql_status_title').text("操作完成");
					$("#sqlbtn_close").attr("disabled", false);
					return;
				}else{
					$('#sql_status_title').text("正在操纵产品数据。。。");
				  
				  return;
				}
			} ,
			error: function( textStatus, errorThrown) {
				alert(JSON.stringify(textStatus));
			}
		});
	}
	me.executeBatchOperation = function(){
		$("#bsd_status_total").text("0");
		$("#bsd_status_processed").text("0");
		var args = getOperationArg();
		BPOUtils.showLoading();
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/batch/executeSBO.html',
			cache: false,
			'contentType': 'application/json',
			'dataType': 'json',
			data: JSON.stringify(args),
			success: function(bodyStr) {
				BPOUtils.closeLoading();
				if (bodyStr.response.status == 9997){
					pollingTimer = window.setInterval(pollingExecuteStatus, 500);
					$("#sqlbtn_close").attr("disabled", true);
					return;
				}else{
					alert("还有其他操作未完成");
					return;
				}
			} ,
			error: function( textStatus, errorThrown) {
				BPOUtils.closeLoading();
				alert(JSON.stringify(textStatus));
			}
		});
	}
}
var BatchSetDiamond = new function(){
	var me = this;
	var pollingTimer;

	me.initProcess = function(){
		$("#batch_set_diamond_form")[0].reset();
	}
	
	function getOperationArg() {
		return {
			isDiamond: BPOUtils.getRadioValue("bsd_arg_isDiamond") == "true",
			brandNames : BPOUtils.getStringArray("bsd_arg_brands"),
			merchantNames : BPOUtils.getStringArray("bsd_arg_merchants")
		};
	}
	
	me.getConfirmMsg = function() {
		return "注意：确认执行将会修改产品的钻石产品标志。\r\n请仔细核对列出的产品数和商家的产品数。\r\n\r\n确认修改这些产品么？";
	}
	
	me.verifyOperationSettings = function(successCallBack){
		var args = getOperationArg();
		BPOUtils.logJson(args);
		BPOUtils.showLoading();
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/batch/verifyBSDArgs.html',
			cache: false,
			mimeType:"application/json; charset=UTF-8",
			'contentType': 'application/json',
			'dataType': 'json',
			data: JSON.stringify(args),
			success: function(bodyStr) {
			  
				if (bodyStr.response.status == -1){
					BPOUtils.closeLoading();
					var errMsg = bodyStr.response.ERROR;
					if (typeof bodyStr.response.invalid_brands == "string"){
						errMsg = errMsg +"\n无效的品牌: " + bodyStr.response.invalid_brands;
					}
					if (typeof bodyStr.response.invalid_merchants == "string"){
						errMsg = errMsg +"\n无效的商家: " + bodyStr.response.invalid_merchants;
					}
					alert(errMsg);
					return;
				}else if (bodyStr.response.status == 0){
					BPOUtils.closeLoading();
					if (BPOUtils.getRadioValue("bsd_arg_isDiamond") == "true"){
						$('#bsd_sumary_optype').text("将一些产品设置为钻石产品");
					}else{
						$('#bsd_sumary_optype').text("取消一些产品的钻石产品，变成普通产品");
					}
					$('#bsd_sumary_error').text(bodyStr.response.ERROR || "无");
					$('#bsd_sumary_totalNum').text(bodyStr.response.totalProducts || "未知");
					var tbodyObj = $('#bsd_sumary_each_merchant');
					tbodyObj.html("");
					var data = bodyStr.response;
					for(var merchantName in data){
						if (!BPOUtils.startsWith(merchantName, "store_")){
							continue;
						}
						var trObj = $('<tr><td>'+merchantName.substr(6,100)+'</td><td>'+data[merchantName]+'</td></tr>');
						tbodyObj.append(trObj);
					}
					successCallBack();
					return;
				}
			} ,
			error: function( textStatus, errorThrown) {
				BPOUtils.closeLoading();
				alert(JSON.stringify(textStatus));
			}
		});
	}
	
	me.viewBatchOperationSummary = function(){}
	
	function pollingExecuteStatus() {
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/batch/queryExxcuteStatus.html',
			cache: false,
			'contentType': 'application/json',
			'dataType': 'json',
			data: {},
			success: function(bodyStr) {
				var total = parseFloat(bodyStr.response.total);
				$("#bsd_status_total").text(bodyStr.response.total);
				var proed = parseFloat(bodyStr.response.processed);
				$("#bsd_status_processed").text(bodyStr.response.processed);
				var width = 365 * proed / total;
				$('#bsd_status_percent_bar').css('width', width);
				if (bodyStr.response.status == 9999){
					window.clearInterval(pollingTimer);
					$('#bsd_status_title').text("操作完成");
					$("#bsdbtn_close").attr("disabled", false);
					return;
				}else{
					$('#bsd_status_title').text("正在修改钻石产品。。。");
				  
				  return;
				}
			} ,
			error: function( textStatus, errorThrown) {
				alert(JSON.stringify(textStatus));
			}
		});
	}
	
	me.executeBatchOperation = function(){
		$("#bsd_status_total").text("0");
		$("#bsd_status_processed").text("0");
		var args = getOperationArg();
		BPOUtils.showLoading();
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/batch/executeBSDOperation.html',
			cache: false,
			'contentType': 'application/json',
			'dataType': 'json',
			data: JSON.stringify(args),
			success: function(bodyStr) {
				BPOUtils.closeLoading();
				if (bodyStr.response.status == 9997){
					pollingTimer = window.setInterval(pollingExecuteStatus, 500);
					$("#bsdbtn_close").attr("disabled", true);
					return;
				}else{
					alert("还有其他操作未完成");
					return;
				}
			} ,
			error: function( textStatus, errorThrown) {
				BPOUtils.closeLoading();
				alert(JSON.stringify(textStatus));
			}
		});
	}
}
var BatchValidProduct = new function(){
	var me = this;
	var pollingTimer;

	me.initProcess = function(){
		$("#batch_valid_product_form")[0].reset();
	}
	
	function getOperationArg() {
		return {
			brandNames : BPOUtils.getStringArray("bvp_arg_brands"),
			merchantNames : BPOUtils.getStringArray("bvp_arg_merchants")
		};
	}
	
	me.getConfirmMsg = function() {
		return "注意：确认执行将会将符合条件的商品上架。\r\n请仔细核对列出的产品数和商家的产品数。\r\n\r\n确认上架产品么？";
	}
	
	me.verifyOperationSettings = function(successCallBack){
		var args = getOperationArg();
		BPOUtils.logJson(args);
		BPOUtils.showLoading();
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/batch/verifyBVPArgs.html',
			cache: false,
			mimeType:"application/json; charset=UTF-8",
			'contentType': 'application/json',
			'dataType': 'json',
			data: JSON.stringify(args),
			success: function(bodyStr) {
			  
				if (bodyStr.response.status == -1){
					BPOUtils.closeLoading();
					var errMsg = bodyStr.response.ERROR;
					if (typeof bodyStr.response.invalid_brands == "string"){
						errMsg = errMsg +"\n无效的品牌: " + bodyStr.response.invalid_brands;
					}
					if (typeof bodyStr.response.invalid_merchants == "string"){
						errMsg = errMsg +"\n无效的商家: " + bodyStr.response.invalid_merchants;
					}
					alert(errMsg);
					return;
				}else if (bodyStr.response.status == 0){
					BPOUtils.closeLoading();
					$('#bvp_sumary_error').text(bodyStr.response.ERROR || "无");
					$('#bvp_sumary_totalNum').text(bodyStr.response.totalProducts || "未知");
					var tbodyObj = $('#bvp_sumary_each_merchant');
					tbodyObj.html("");
					var data = bodyStr.response;
					for(var merchantName in data){
						if (!BPOUtils.startsWith(merchantName, "store_")){
							continue;
						}
						var trObj = $('<tr><td>'+merchantName.substr(6,100)+'</td><td>'+data[merchantName]+'</td></tr>');
						tbodyObj.append(trObj);
					}
					successCallBack();
					return;
				}
			} ,
			error: function( textStatus, errorThrown) {
				BPOUtils.closeLoading();
				alert(JSON.stringify(textStatus));
			}
		});
	}
	
	me.viewBatchOperationSummary = function(){}
	
	function pollingExecuteStatus() {
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/batch/queryFullIndexStatus.html',
			cache: false,
			'contentType': 'application/json',
			'dataType': 'json',
			data: {},
			success: function(bodyStr) {
				var total = parseInt(bodyStr.response.total);
				var indexed = parseInt(bodyStr.response.indexed);
				var failed = parseInt(bodyStr.response.failed);
				var ignored = parseInt(bodyStr.response.ignored);
				var passedTime = parseInt(bodyStr.response.passedTime);
				var running = bodyStr.response.running == 'true';
				var processedNum = indexed+failed+ignored;
				$('#bvp_status_processed').text(processedNum);
				$('#bvp_status_total').text(total);
				$('#bvp_status_indexed').text(indexed);
				$('#bvp_status_failed').text(failed);
				$('#bvp_status_ignored').text(ignored);
				$('#bvp_status_runtime').text(BPOUtils.getTimeText(passedTime/1000));
				$('#bvp_status_lefttime').text(BPOUtils.calcLeftTime(total,processedNum,passedTime,0));
				var width = 365;
				if (total > 0){
					width = processedNum / total * 365;
				}
				$('#bvp_status_percent_bar').css('width', width);
				if (!running){
					
					$('#bvp_status_title').text("操作完成");
					$("#bvp_close").attr("disabled", false);
					window.clearTimeout(pollingTimer);
				}else{
					pollingTimer = window.setTimeout(pollingExecuteStatus, 1300);
				}
			} ,
			error: function( textStatus, errorThrown) {
				alert(JSON.stringify(textStatus));
			}
		});
	}
	
	me.executeBatchOperation = function(){
		$("#fullindex_status_total").text("0");
		$("#fullindex_status_processed").text("0");
		var args = getOperationArg();
		BPOUtils.showLoading();
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/batch/executeBVP.html',
			cache: false,
			'contentType': 'application/json',
			'dataType': 'json',
			data: JSON.stringify(args),
			success: function(bodyStr) {
				BPOUtils.closeLoading();
				if (bodyStr.response.status == 9997){
					pollingTimer = window.setTimeout(pollingExecuteStatus, 1000);
					$("#bvp_close").attr("disabled", true);
					return;
				}else{
					pollingTimer = window.setTimeout(pollingExecuteStatus, 1000);
					alert(bodyStr.response.statusMessage);
					
					return;
				}
			} ,
			error: function( textStatus, errorThrown) {
				BPOUtils.closeLoading();
				alert(JSON.stringify(textStatus));
			}
		});
	}
}
var BatchRemoveProduct = new function() {
	var me = this;
	var allUsedSqls = {};
	
	me.initProcess = function(){
		//alert("BatchRemoveProduct.initProcess()");
		BPOUtils.showLoading();
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/batch/queryAllUsedSqls.html',
			cache: false,
			'contentType': 'application/json',
			'dataType': 'json',
			data: JSON.stringify({}),
			success: function(resp) {
				BPOUtils.closeLoading();
				if (resp.exception != null){
					$('#brp_sumary_exception').text(resp.exception);
					return;
				}
				if (resp.length == 0){
					alert("目前还没有任何SQL的历史记录，请手工输入");
					return;
				}
				
				var data = resp.data;
				var selectElem = $('#brp_select_usedSql');
				selectElem.empty();
				$("<option></option>").val(0).text("请选择：").appendTo(selectElem);
				$.each(data, function(i, item) {
					$("<option></option>")
						.val(i+1)
						.text(item)
						.appendTo(selectElem);
				});
			} ,
			error: function( textStatus, errorThrown) {
				BPOUtils.closeLoading();
				alert(JSON.stringify(textStatus));
			}
		});
	}
	
	me.getConfirmMsg = function() {
		return "确认开始删除商品么？";
	}
	
	function getOperationArg() {
		return {
			sqlNote : $('#brp_arg_statement_note').val(),
			sqlStatement : $('#brp_arg_statement').val(),
			totalCount: parseInt($('#brp_sumary_totalNum').text())
		};
	}
	me.verifyOperationSettings = function(successCallBack){
		//alert("BatchRemoveProduct.verifyOperationSettings()");
		var args = getOperationArg();
		BPOUtils.logJson(args);
		BPOUtils.showLoading();
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/batch/verifyBRPArgs.html',
			cache: false,
			mimeType:"application/json; charset=UTF-8",
			'contentType': 'application/json',
			'dataType': 'json',
			data: JSON.stringify(args),
			success: function(resp) {
				BPOUtils.closeLoading();
				if (resp.exception != null){
					$('#brp_sumary_exception').text(resp.exception);
					$('#brp_sumary_exception').show();
				}else{
					$('#brp_sumary_exception').text("");
					$('#brp_sumary_exception').hide();
				}
				$('#brp_sumary_totalNum').text(resp.length);
				successCallBack();
			} ,
			error: function( textStatus, errorThrown) {
				BPOUtils.closeLoading();
				alert(JSON.stringify(textStatus));
			}
		});
	}
	function updateStatusInfo(data){
		$('#brp_status_sql').text(data.Sql);
		$('#brp_status_startTime').text(data.StartTime);
		$('#brp_status_endTime').text(data.EndTime);
		$('#brp_status_running').text(data.Running);
		$('#brp_status_total').text(data.TotalPlanned);
		$('#brp_status_scheduled').text(data.TotalScheduled);
		$('#brp_status_deleted').text(data.TotalDeleted);
	}
	me.executeBatchOperation = function(){
		var args = getOperationArg();
		BPOUtils.logJson(args);
		BPOUtils.showLoading();
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/batch/executeBRP.html',
			cache: false,
			mimeType:"application/json; charset=UTF-8",
			'contentType': 'application/json',
			'dataType': 'json',
			data: JSON.stringify(args),
			success: function(resp) {
				BPOUtils.closeLoading();
				if (resp.exception != null){
					$('#brp_sumary_exception').text(resp.exception);
					$('#brp_sumary_exception').show();
				}else{
					$('#brp_sumary_exception').text("");
					$('#brp_sumary_exception').hide();
				}
				updateStatusInfo(resp.status);
			} ,
			error: function( textStatus, errorThrown) {
				BPOUtils.closeLoading();
				alert(JSON.stringify(textStatus));
			}
		});
	}
	
	me.viewBatchOperationSummary = function(){
		//alert("BatchRemoveProduct.viewBatchOperationSummary()"); 
	}
	
	// 本操作使用的函数
	me.fillWithUsedSql = function(){
		var selectVal = $("#brp_select_usedSql").find("option:selected").text();
		var args={note:selectVal};
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/batch/getBrpSqlByNote.html',
			cache: false,
			mimeType:"application/json; charset=UTF-8",
			'contentType': 'application/json',
			'dataType': 'json',
			data: JSON.stringify(args),
			success: function(resp) {
				BPOUtils.closeLoading();
				if (resp.exception != null){
					$('#brp_sumary_exception').text(resp.exception);
					return;
				}
				if (resp.length == 0){
					alert("目前还没有任何SQL的历史记录，请手工输入");
					return;
				}
				$('#brp_arg_statement').val(resp.data);
			} ,
			error: function( textStatus, errorThrown) {
				BPOUtils.closeLoading();
				alert(JSON.stringify(textStatus));
			}
		});
		$('#brp_arg_statement_note').val(selectVal);
	}
	
	me.queryStatistics = function(){
		var args = getOperationArg();
		BPOUtils.showLoading();
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/batch/queryBRPStatus.html',
			cache: false,
			mimeType:"application/json; charset=UTF-8",
			'contentType': 'application/json',
			'dataType': 'json',
			data: JSON.stringify(args),
			success: function(resp) {
				BPOUtils.closeLoading();
				updateStatusInfo(resp.status);
			} ,
			error: function( textStatus, errorThrown) {
				BPOUtils.closeLoading();
				alert(JSON.stringify(textStatus));
			}
		});
	}
	me.stopCurrentJob = function(){
		var args = getOperationArg();
		BPOUtils.showLoading();
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/batch/stopBRP.html',
			cache: false,
			mimeType:"application/json; charset=UTF-8",
			'contentType': 'application/json',
			'dataType': 'json',
			data: JSON.stringify(args),
			success: function(resp) {
				BPOUtils.closeLoading();
				updateStatusInfo(resp.status);
			} ,
			error: function( textStatus, errorThrown) {
				BPOUtils.closeLoading();
				alert(JSON.stringify(textStatus));
			}
		});
	}
}
var BatchFullIndex = new function(){
	var me = this;
	var pollingTimer;
	var passedWaitTime;
	var skippedNumber;

	me.initProcess = function(){}
	
	me.getConfirmMsg = function() {
		return "本操作期间，网站功能将会因为索引数据不全而有异常。请确定在维护窗口期进行本操作。\r\n\r\n确认开始更新索引么？";
	}
	
	me.verifyOperationSettings = function(successCallBack){
		successCallBack();
	}
	
	
	me.viewBatchOperationSummary = function(){
		BPOUtils.showLoading();
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/batch/verifyFullIndex.html',
			cache: false,
			'contentType': 'application/json',
			'dataType': 'json',
			data: {},
			success: function(bodyStr) {
				BPOUtils.closeLoading();
				$('#fullindex_sumary_totalNum').text(bodyStr.response.totalProducts);
				if (bodyStr.response.status == 0){
					var total = parseFloat(bodyStr.response.totalProducts);
					var estimateTime = BPOUtils.calcLeftTime(total,0,0,0);
					$('#fullindex_sumary_estime').text(estimateTime);
					$('#fullindex_sumary_exception').hide();
					return;
				}else{
					var errElem = $('#fullindex_sumary_exception');
					errElem.text(bodyStr.response.statusMessage);
					errElem.show();
					return;
				}
			} ,
			error: function( textStatus, errorThrown) {
				BPOUtils.closeLoading();
				alert(JSON.stringify(textStatus));
			}
		});
	}
	
	
	
	function pollingExecuteStatus() {
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/batch/queryFullIndexStatus.html',
			cache: false,
			'contentType': 'application/json',
			'dataType': 'json',
			data: {},
			success: function(bodyStr) {
				var total = parseInt(bodyStr.response.total);
				var indexed = parseInt(bodyStr.response.indexed);
				var failed = parseInt(bodyStr.response.failed);
				var ignored = parseInt(bodyStr.response.ignored);
				if (skippedNumber == 0){
					skippedNumber = ignored;
				}
				var passedTime = parseInt(bodyStr.response.passedTime);
				var running = bodyStr.response.running == 'true';
				var processedNum = indexed+failed+ignored;
				$('#fullindex_status_processed').text(processedNum);
				$('#fullindex_status_total').text(total);
				$('#fullindex_status_indexed').text(indexed);
				$('#fullindex_status_failed').text(failed);
				$('#fullindex_status_ignored').text(ignored);
				$('#fullindex_status_runtime').text(BPOUtils.getTimeText(passedTime/1000));
				$('#fullindex_status_lefttime').text(BPOUtils.calcLeftTime(total,processedNum,passedTime,skippedNumber));
				var width = 365;
				if (total > 0){
					width = processedNum / total * 365;
				}
				$('#fullindex_status_percent_bar').css('width', width);
				if (!running){
					
					$('#fullindex_status_title').text("操作完成");
					$("#fullindexbtn_close").attr("disabled", false);
					window.clearTimeout(pollingTimer);
				}else{
					pollingTimer = window.setTimeout(pollingExecuteStatus, 1300);
				}
			} ,
			error: function( textStatus, errorThrown) {
				alert(JSON.stringify(textStatus));
			}
		});
	}
	
	me.executeBatchOperation = function(){
		$("#fullindex_status_total").text("0");
		$("#fullindex_status_processed").text("0");
		skippedNumber = 0;
		BPOUtils.showLoading();
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/batch/executeFullIndex.html',
			cache: false,
			'contentType': 'application/json',
			'dataType': 'json',
			data: {},
			success: function(bodyStr) {
				BPOUtils.closeLoading();
				if (bodyStr.response.status == 9997){
					pollingTimer = window.setTimeout(pollingExecuteStatus, 1000);
					$("#fullindexbtn_close").attr("disabled", true);
					return;
				}else{
					pollingTimer = window.setTimeout(pollingExecuteStatus, 1000);
					alert(bodyStr.response.statusMessage);
					
					return;
				}
			} ,
			error: function( textStatus, errorThrown) {
				BPOUtils.closeLoading();
				alert(JSON.stringify(textStatus));
			}
		});
	}
}
var BatchDataOp = new function() {
	var me = this;
	var batchWorker;
	
	function setActiveStep(stepNo){
		for(var i=1;i<=4;i++){
			if (i<stepNo){
				document.getElementById("div_step_"+i).className = "process_done";
			}else if (i == stepNo){
				document.getElementById("div_step_"+i).className = "process_doing";
			}else{
				document.getElementById("div_step_"+i).className = "process_not_done";
			}
		}
	}
	
	me.showProcessStart = function(){
		var selValue = $('#select_operation').val();
		if (selValue == "invalid-products"){
			batchWorker = BatchInvalidProducts;
		}else if(selValue == "sql-invalid-products"){
			batchWorker = BatchSqlInvalidProducts;
		}else if(selValue == "set-diamond"){
			batchWorker = BatchSetDiamond;
		}else if (selValue == "full-index"){
			batchWorker = BatchFullIndex;
		}else if (selValue == "valid-product"){
			batchWorker = BatchValidProduct;
		}else if (selValue == "batch-remove-product"){
			batchWorker = BatchRemoveProduct;
		}else {
			alert("快实现 "+selValue);
			return;
		}
		
		batchWorker.initProcess();
		$('#div_select_operation').hide();
		var nextDivId = "#process_"+selValue;
		$(nextDivId).show();
		setActiveStep(2);
	}
	
	me.restartProcess = function(){
		var selValue = $('#select_operation').val();
		$('#div_select_operation').show();
		var nextDivId = "#process_"+selValue;
		$(nextDivId).hide();
		$("#result_"+selValue).hide();
		setActiveStep(1);
	}
	
	me.backtoProcessStart = function(){
		var selValue = $('#select_operation').val();
		var nowDivId = "#check_"+selValue;
		$(nowDivId).hide();
		var nextDivId = "#process_"+selValue;
		$(nextDivId).show();
		setActiveStep(2);
	}
	
	me.verifyOperationSettings = function(){
		batchWorker.verifyOperationSettings(me.viewBatchOperationSummary);
	}
	
	me.viewBatchOperationSummary = function(){
		batchWorker.viewBatchOperationSummary();
		var selValue = $('#select_operation').val();
		$('#process_'+selValue).hide();
		$('#check_'+selValue).show();
		setActiveStep(3);
	}
	
	me.executeBatchOperation = function(){
		if (!confirm(batchWorker.getConfirmMsg())){
			return;
		}
		batchWorker.executeBatchOperation();
		var selValue = $('#select_operation').val();
		$('#check_'+selValue).hide();
		$('#result_'+selValue).show();
		setActiveStep(4);
	}
};
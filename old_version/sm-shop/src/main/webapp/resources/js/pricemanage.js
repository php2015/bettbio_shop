var PriceMngUtils =  new function() {
	var me = this;
	var passedWaitTime = 0;
	
	me.markIgnorecase = function(orgStr, findStr){
		var icOrgStr = orgStr.toLowerCase();
		var icFindStr = findStr.toLowerCase();
		var startPos = icOrgStr.indexOf(icFindStr);
		if (startPos < 0){
			return orgStr;
		}
		return orgStr.substr(0,startPos)+"<span style='color:red;'>"+orgStr.substr(startPos, findStr.length)+"</span>"+orgStr.substr(startPos+findStr.length, 100);
	}
};

var BrandDiscountManager = new function() {
	var me = this;
	var brandDisplayName = "";
	var curPageNo = 0;
	var dataPerPage = 15;
	
	me.onBrandSelected = function(aObj, brandId, storeId, rate, existed){
		//alert("Add " + $(aObj).html()+", brandId="+brandId+", storeId="+storeId);
		$('#span_add_brand_displayName').text($(aObj).html().replace(/<.*?>/g, "").replace(/\/[\d\.]*/g, ""));
		brandDisplayName = $('#span_add_brand_displayName').text();
		$('#input_add_brandId').val(brandId);
		$('#input_add_storeId').val(storeId);
		$('#input_add_discount').val(rate);
		if (!existed){
			$('#btn_delete_discount').addClass('disabled');
		}else{
			$('#btn_delete_discount').removeClass('disabled');
		}
		me.closeBrandList();
	}
	
	function resetEditDatas() {
		$('#input_add_brandId').val("");
		$('#input_add_storeId').val("");
		$('#input_add_discount').val("0.0");
		$('#btn_delete_discount').addClass('disabled');
		$('#span_add_brand_displayName').text("无");
	}
	me.closeBrandList = function(){
		$("#brand_and_store_list_div").hide();
	}
	function updateEditingBrandsList(brandList) {
		$("#brand_and_store_list").html("");
		if ((typeof brandList != "array" && typeof brandList != "object")|| brandList.length == 0){
			me.closeBrandList();
			return;
		}
		
		for(var i=0;i<brandList.length;i++){
			var data = brandList[i];
			var displayName = data.brandName+"("+data.storeName+")";
			var highlightName = displayName;
			
			var brandName = $('#input_add_brand_name').val();
			if (brandName != ""){
				highlightName = PriceMngUtils.markIgnorecase(data.brandName,brandName);
			}else{
				highlightName = data.brandName;
			}
			var storeName = $('#input_add_store_name').val();
			if (storeName != ""){
				highlightName = highlightName +"("+ PriceMngUtils.markIgnorecase(data.storeName,storeName);
			}else{
				highlightName =  highlightName +"(" + data.storeName;
			}
			if (data.existed){
				highlightName = highlightName +"/" + data.rate+")";
			}else{
				highlightName = highlightName+")";
			}
			var aObj = $("<div class='brandNameListItem'><a href='#' onclick='BrandDiscountManager.onBrandSelected(this,"+data.brandId+","+data.storeId+","+data.rate+","+data.existed+")'>"+highlightName+"</a></div>");
			$("#brand_and_store_list").append(aObj);
		}
		if (brandList.length >= 200){
			var aObj = $("<div class='brandNameListItem'><a href='#' onclick='alert(\"请输入更多信息以缩小查找范围\")'>MORE......</a></div>");
			$("#brand_and_store_list").append(aObj);
		}
		$("#brand_and_store_list_div").show();
	}
	
	function addBrandDiscount(brandId, storeId, discountRate){
		var args = {
			brandId: brandId,
			storeId: storeId,
			discountRate: discountRate
		};
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/price/saveBrandDiscount.html',
			cache: false,
			mimeType:"application/json; charset=UTF-8",
			'contentType': 'application/json',
			'dataType': 'json',
			data: JSON.stringify(args),
			success: function(bodyStr) {
			  
				if (bodyStr.response.status == 0){
					alert("操作成功");
					resetEditDatas();
					me.refreshDiscountList();
					return;
				}else{
					alert(bodyStr.response.statusMessage);
					return;
				}
			} ,
			error: function( textStatus, errorThrown) {
				//alert(JSON.stringify(textStatus));
				alert("请重新登录");
			}
		});
		
	}
	
	function deleteBrandDiscount(brandId, storeId){
		var args = {
			brandId: brandId,
			storeId: storeId
		};
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/price/deleteBrandDiscount.html',
			cache: false,
			mimeType:"application/json; charset=UTF-8",
			'contentType': 'application/json',
			'dataType': 'json',
			data: JSON.stringify(args),
			success: function(bodyStr) {
			  
				if (bodyStr.response.status == 0){
					alert("操作成功");
					resetEditDatas();
					me.refreshDiscountList();
					return;
				}else{
					alert(bodyStr.response.statusMessage);
					return;
				}
			} ,
			error: function( textStatus, errorThrown) {
				//alert(JSON.stringify(textStatus));
				alert("请重新登录");
			}
		});
	}

	me.onAddDiscountKeyUp = function(){
		var args = {
			brandName: $('#input_add_brand_name').val(),
			storeName: $('#input_add_store_name').val()
		};
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/price/getDiscountByBrandStoreName.html',
			cache: false,
			mimeType:"application/json; charset=UTF-8",
			'contentType': 'application/json',
			'dataType': 'json',
			data: JSON.stringify(args),
			success: function(bodyStr) {
			  
			  if (bodyStr.status == 0){
				  var brandList = bodyStr.listStr;
				  updateEditingBrandsList(brandList);
				  return;
			  }else{
				  alert(bodyStr.response.statusMessage);
				  return;
			  }
			} ,
			error: function( textStatus, errorThrown) {
				//alert(JSON.stringify(textStatus));
				alert("请重新登录");
			}
		});
	}
	
	me.beforeAddDiscount = function(){
		var brandId = $('#input_add_brandId').val();
		var storeId = $('#input_add_storeId').val();
		var discountRate = $('#input_add_discount').val();
		if(brandId=='' || storeId=='' || brandDisplayName==''){
			alert("必须选定品牌");
			return;
		}
		if (discountRate == ''){
			alert("必须指定折扣率");
			return;
		}
		if (isNaN(discountRate)){
			alert("折扣率必须为0.0~1.0的数字");
			return;
		}
		discountRate = parseFloat(discountRate);
		if (discountRate<0.0 || discountRate > 1.0){
			alert("折扣率必须为0.0~1.0的数字");
			return;
		}
		addBrandDiscount(brandId, storeId, discountRate);
	}
	
	function updateDiscountTable(pageData, discountData) {
		// create table first
		$('#tbody_brand_discount').html("");
		$('#select_all_discount').prop('checked', false);
		for(var i=0;i<discountData.length;i++){
			var data = discountData[i];
			var trObj = $("<tr class='everyTR'></tr>");
			trObj.append("<td><input type='checkbox' class='select_editrate' value='"+data.id+"'></input>");
			trObj.append("<td>"+data.brandName+" ("+data.brandId+")</td>");
			trObj.append("<td>"+data.storeName+"</td>");
			trObj.append("<td><input id='input_editrate_"+data.id+"' style='border: 0px;' value='"+data.rate+"' onkeyup='BrandDiscountManager.onRateChanged("+data.id+","+data.rate+")'></input></td>");
			trObj.append("<td><a href='#' onclick='BrandDiscountManager.deleteById("+data.id+")'>删除</a> "
				+ "<a href='#' id='save_editrate_"+data.id+"' style='display:none;' onclick='BrandDiscountManager.saveById("+data.id+")'>保存</a></td>");
			$('#tbody_brand_discount').append(trObj);
		}
		
		// then update pagingation
		var totalPage = Math.ceil(pageData.total/dataPerPage);
		var startPage = Math.floor(pageData.pageNo/5)*5+1;
		var showPages = Math.min(totalPage-startPage+1, 5);
		var paginationData = {
			startPages: startPage,
			showPages: showPages,
			currentPage: pageData.pageNo
		};
		
		curPageNo = pageData.pageNo;
		$('#pagination_sumary').text(pageData.fromNo+"~"+pageData.toNo+" of " + pageData.total);
		writePaging(paginationData, null);
	}
	me.refreshDiscountList = function(){
		var args = {
			brandName: $('#input_filter_brand_name').val(),
			storeName: $('#input_filter_store_name').val(),
			pageSize: dataPerPage,
			pageNo : curPageNo
		};
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/price/paginationBrandDiscount.html',
			cache: false,
			mimeType:"application/json; charset=UTF-8",
			'contentType': 'application/json',
			'dataType': 'json',
			data: JSON.stringify(args),
			success: function(bodyStr) {
			  
				if (bodyStr.status == 0){
					updateDiscountTable(bodyStr.pageData, bodyStr.discountData);
					return;
				}else{
					alert(bodyStr.response.statusMessage);
					return;
				}
			} ,
			error: function( textStatus, errorThrown) {
				//alert(JSON.stringify(textStatus));
				alert("请重新登录");
			}
		});
	}
	
	me.onRateChanged = function(bdId, oldRate){
		var newValue = $('#input_editrate_'+bdId).val();
		if (isNaN(newValue)){
			return;
		}
		var newRate = parseFloat(newValue);
		if (newValue != oldRate){
			$('#save_editrate_'+bdId).show();
		}else{
			$('#save_editrate_'+bdId).hide();
		}
	}
	me.beforeDeleteDiscount = function(){
		var brandId = $('#input_add_brandId').val();
		var storeId = $('#input_add_storeId').val();
		var discountRate = $('#input_add_discount').val();
		if(brandId=='' || storeId=='' || brandDisplayName==''){
			alert("必须选定品牌");
			return;
		}
		
		deleteBrandDiscount(brandId, storeId);
	}
	me.onFilterDiscountKeyUp = function() {
		me.refreshDiscountList();
	}
	
	me.gotoPage = function(pageNo){
		curPageNo = pageNo;
		me.refreshDiscountList();
	}
	
	me.deleteById = function(bdId){
		var args = {
			bdId: bdId
		};
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/price/deleteDiscountById.html',
			cache: false,
			mimeType:"application/json; charset=UTF-8",
			'contentType': 'application/json',
			'dataType': 'json',
			data: JSON.stringify(args),
			success: function(bodyStr) {
			  
				if (bodyStr.response.status == 0){
					me.refreshDiscountList();
					return;
				}else{
					alert(bodyStr.response.statusMessage);
					return;
				}
			} ,
			error: function( textStatus, errorThrown) {
				//alert(JSON.stringify(textStatus));
				alert("请重新登录");
			}
		});
	}
	me.saveById = function(bdId) {
		var newValue = $('#input_editrate_'+bdId).val();
		if (isNaN(newValue)){
			return;
		}
		var newRate = parseFloat(newValue);
		if (newRate < 0 || newRate > 1){
			alert("折扣率必须为0.0～1.0");
			return;
		}
		var args = {
			bdId: bdId,
			rate: newRate
		};
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/price/updateDiscount.html',
			cache: false,
			mimeType:"application/json; charset=UTF-8",
			'contentType': 'application/json',
			'dataType': 'json',
			data: JSON.stringify(args),
			success: function(bodyStr) {
			  
				if (bodyStr.response.status == 0){
					var tdObj = $('#input_editrate_'+bdId).parent();
					var htmlStr = "<input id='input_editrate_"+bdId+"' style='border: 0px;' value='"+newRate+"' onkeyup='BrandDiscountManager.onRateChanged("+bdId+","+newRate+")'></input>";
					tdObj.html(htmlStr);
					$('#save_editrate_'+bdId).hide();
					return;
				}else{
					alert(bodyStr.response.statusMessage);
					return;
				}
			} ,
			error: function( textStatus, errorThrown) {
				//alert(JSON.stringify(textStatus));
				alert("请重新登录");
			}
		});
	}
	
	me.deleteAllDiscount = function(){
		if (!confirm("这个操作将会删除所有的品牌折扣，你确定要删除么？")){
			return;
		}
		var args={};
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/price/deleteAllDiscount.html',
			cache: false,
			mimeType:"application/json; charset=UTF-8",
			'contentType': 'application/json',
			'dataType': 'json',
			data: JSON.stringify(args),
			success: function(bodyStr) {
			  
				if (bodyStr.response.status == 0){
					me.refreshDiscountList();
					return;
				}else{
					alert(bodyStr.response.statusMessage);
					return;
				}
			} ,
			error: function( textStatus, errorThrown) {
				//alert(JSON.stringify(textStatus));
				alert("请重新登录");
			}
		});
	}
	
	me.onSelectAllDiscount = function(){
		var mark = false;
		if ($('#select_all_discount').prop('checked')){
			mark = true;
		}
		$('.select_editrate').each(function(){
			$(this).prop('checked', mark);
		});
	}
	
	me.deleteSelectedDiscount = function() {
		var selectedIds = new Array();
		$('.select_editrate').each(function(){
			if ($(this).prop('checked')){
				selectedIds.push(parseInt($(this).val()));
			}
		});
		if (selectedIds.length == 0){
			alert("未选中任何品牌折扣");
			return;
		}
		var confirmMsg = "你将一次删除" + selectedIds.length+"个品牌折扣, 确认删除么？";
		if (!confirm(confirmMsg)){
			return;
		}
		var args={
			ids : selectedIds
		};
		$.ajax({  
			type: 'POST',
			url: getContextPath() + '/admin/price/deleteDiscountByIdList.html',
			cache: false,
			mimeType:"application/json; charset=UTF-8",
			'contentType': 'application/json',
			'dataType': 'json',
			data: JSON.stringify(args),
			success: function(bodyStr) {
			  
				if (bodyStr.response.status == 0){
					me.refreshDiscountList();
					return;
				}else{
					alert(bodyStr.response.statusMessage);
					return;
				}
			} ,
			error: function( textStatus, errorThrown) {
				//alert(JSON.stringify(textStatus));
				alert("请重新登录");
			}
		});
	}
};
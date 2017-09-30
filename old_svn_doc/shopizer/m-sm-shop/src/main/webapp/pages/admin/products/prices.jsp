<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>				
<c:set value="${product.id}" var="productId" scope="request"/>
<style>
.row {
	margin: 15px 0px;
}
.table {
	line-height: 25px;
	width: 100%;
}
.cellstyle {
	background-color: blue;
	color:red;
}
.btnclass {
	margin:5px;
	padding:5px;
	background-color:green;
	color:#fff;
}
.del {
	background-color:#ccc;
}
</style>
<link href="<c:url value="/resources/js/handsontable-0.20.2/handsontable.min.css" />" rel="stylesheet">
<link href="<c:url value="/resources/js/handsontable-0.20.2/pikaday/pikaday.css" />" rel="stylesheet">
<script src="<c:url value="/resources/js/handsontable-0.20.2/pikaday/pikaday.js" />"></script>
<script src="<c:url value="/resources/js/handsontable-0.20.2/moment/moment.js" />"></script>
<script src="<c:url value="/resources/js/handsontable-0.20.2/zeroclipboard/ZeroClipboard.js" />"></script>
<script src="<c:url value="/resources/js/handsontable-0.20.2/handsontable.min.js" />"></script>
<script src="<c:url value="/resources/js/json2.js" />"></script>

<div class ="row">
	<div id="showloading" class="alert alert-success" style="display:none"></div>
	<!-- begin middle main content -->
	<div style="padding: 10px 30px;margin: 10px auto;">
		<div id="pricescontainer"></div>
	</div><!-- end middle main content -->
	<div class="row">
		
		<div class="col-sm-1 col-sm-offset-10"><a class="btn btn-success" onclick="savePrices()"><s:message code="button.label.submit" text="Save" /></a></div>
	</div>
</div>
<script>
//esc keyup for close modal
$(document).keyup(function(event){
  switch(event.keyCode) {
    case 27:
      window.parent.window.closemodal();
  }
});
var productId = '${product.id}';
var pagingUrl = '<c:url value="/admin/products/prices/paging.html?productId=${product.id}"/>';
var editUrl = '<c:url value="/admin/products/price/edit.html?productId=${product.id}"/>';
var saveUrl = '<c:url value="/admin/products/prices/doSave.html?productId=${product.id}"/>';
var removesUrl = '<c:url value="/admin/products/price/removes.html?productId=${product.id}"/>';
var container = document.getElementById("pricescontainer");
var tab_width = $("#pricescontainer").width();
var hot;
function loadPriceTable(data) {
	var w = tab_width/8;
	hot = new Handsontable(container, {
		  data: data,
		  startRows: 1,
		  startCols: 2,
		  colHeaders: ['<s:message code="label.product.specification" text="Name" />', '<s:message code="label.generic.price" text="Price" />', 
		               '<s:message code="label.product.price.period" text="Price Period" />', '<s:message code="label.product.price.special" text="Price special" />',
		               '<s:message code="label.product.price.special.startdate" text="Special start date" />','<s:message code="label.product.price.special.enddate" text="Special end date" />',
		               '<s:message code="label.generic.operate" text="Operator" />'],
		  rowHeaders:true,
		  rowHeights: 30,
		  className: 'table htMiddle',
		  columnSorting: true,
		  contextMenu: true,
		  minSpareRows: 1,
		  colWidths: w,
		  maxRows: 9,
		  //autoColumnSize: true,
		  columns: [
		    {   data: 'name',
			 	className: 'cellstyle htMiddle'},
		    {
		      data: 'price',
		      type: 'numeric',
		      format: '0,0.00'
		    },
		    {
		      data: 'period'
		      // 2nd cell is simple text, no special options here
		    },
		    {
		      data: 'specialPrice',
		      type: 'numeric',
		      format: '0,0.00'
		    },
		    {
		      data: 'begindt',
		      type: 'date',
		      dateFormat: 'YYYY-MM-DD',
		      correctFormat: true,
		      defaultDate: '1900-01-01'
		    },
		    {
		      data: 'enddt',
		      type: 'date',
		      dateFormat: 'YYYY-MM-DD',
		      correctFormat: true,
		      defaultDate: '1900-01-01'
		    },
		    {
		      renderer: customerRenderer,
		      data: 'priceId'	
		    }
		  ]
		});
}
$(function(){
	init();
});
function init() {
	$.post(pagingUrl, function(result){
		if(result.response.status == 0) {//RESPONSE_STATUS_SUCCESS
			var list = result.response.data;
			loadPriceTable(list);
		}
	});
}
function customerRenderer(instance, td, row, col, prop, value, cellProperties){
	/* var save = document.createElement('a');
	$(save).attr("class", "btn btn-success btn-sm");
	$(save).css("margin", "5px 10px");
	$(save).html('<s:message code="button.label.submit" text="Save" />');
	$(save).bind("click", function(){
		var data = hot.getSourceDataAtRow(row);
		if(data.name!=null) {
			$.post(saveUrl, {name:data.name,price:data.price,period:data.period,specialPrice:data.specialPrice,begindt:data.begindt,enddt:data.enddt,priceId:data.priceId}, function(result){
				if(result.response.status==0) {
					$("#showloading").html('<s:message code="label.generic.success" text="Operator Success" />').attr("class", "alert alert-success").css("display","block");
				} else {
					$("#showloading").html(result.response.statusMessage).attr("class", "alert alert-warning").css("display","block");
				}
			});
		} else {
			$("#showloading").html('<s:message code="NotEmpty.generic.name" text="Name not empty" />').attr("class", "alert alert-warning").css("display","block");
		}
		setTimeout(function(){
			$("#showloading").css("display", "none");
			hot.destroy();
			init();
		},2000);
	}); */
	
	var del = document.createElement('a');
	$(del).attr("class", "btn btn-info btn-sm");
	$(del).css("width", "90%").css("margin", "5px 10px 5px 10px");
	$(del).html('<s:message code="label.generic.remove" text="Remove" />');
	if(value==null)return;
	$(del).bind("click", function(){
		if(confirm('<s:message code="label.entity.remove.confirm" text="Remove confirm" />')){
			$.post(removesUrl, {priceId: value}, function(result){
				if(result.response.status == 9999) {//RESPONSE_OPERATION_COMPLETED
					hot.destroy();
					init();
				} else {
					$("#showloading").html('<s:message code="label.entity.actived.failed" text="Failed" />').attr("class", "alert alert-warning").css("display","block");
				}
			});
		}
	});
	Handsontable.Dom.empty(td);
	//td.appendChild(save);
	td.appendChild(del);
	return td;
}

function savePrices() {
	var datas = hot.getSourceData();
	var dataArray = new Array(); 
	for(var i=0;i<datas.length;i++) {
		var data = datas[i];
		if(data.name==null||data.name==''){
			continue;
		}
		dataArray.push({name:data.name,price:data.price,period:data.period,specialPrice:data.specialPrice,begindt:data.begindt,enddt:data.enddt,priceId:data.priceId});
	}
	
	var _data = JSON.stringify(dataArray);
	if(dataArray.length!=null&&dataArray.length>0) {
		$.ajax({
			url:saveUrl, 
			type:"POST",
			data:_data,
			contentType: "application/json",
			dataType: "json",
			success: function(result){
				if(result.response.status==0) {
					$("#showloading").html('<s:message code="label.generic.success" text="Operator Success" />').attr("class", "alert alert-success").css("display","block");
				} else {
					$("#showloading").html(result.response.statusMessage).attr("class", "alert alert-warning").css("display","block");
				}
			},
			error: function(e) { 
				console.log('Error while loading price');
				//alert(e);
			}
		});
	} else {
		$("#showloading").html('<s:message code="NotEmpty.generic.name" text="Name not empty" />').attr("class", "alert alert-warning").css("display","block");
	}
	setTimeout(function(){
		$("#showloading").css("display", "none");
		hot.destroy();
		init();
	},2000);
}
</script>
  					
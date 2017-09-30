<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>
<style>
#brand_and_store_list_div {
	max-height: 450px;
	max-width:90%;
	background-color: #e9e9e9;
	border: 3px solid white;
	position: absolute;
	top: 195px;
	left: 60px;
	overflow-x:hidden;
	overflow-y:auto;
	display: none;
}

.brandNameListItem {
	margin: 3px 20px 0px 0px;
	display: inline-block;
}

.table_brand_discount {
	width: 100%;
}
</style>
<script type="text/javascript">
var priceFormatMessage = '<s:message code="message.price.cents" text="Wrong format" />';
</script>
<jsp:include page="/resources/js/functions.jsp" />
<script src="<c:url value="/resources/js/jquery.formatCurrency-1.4.0.js" />"></script>
<script src="<c:url value="/resources/js/jquery.alphanumeric.pack.js" />"></script>
<script src="<c:url value="/resources/js/pagingation.js" />"></script>
<script src="<c:url value="/resources/js/pricemanage.js" />"></script>		
				
<script>
	$(document).ready(function() {
		$('#input_add_brand_name').keyup(BrandDiscountManager.onAddDiscountKeyUp);
		$('#input_add_store_name').keyup(BrandDiscountManager.onAddDiscountKeyUp);
		
		$('#input_filter_brand_name').keyup(BrandDiscountManager.onFilterDiscountKeyUp);
		$('#input_filter_store_name').keyup(BrandDiscountManager.onFilterDiscountKeyUp);
		
		$('#select_all_discount').click(BrandDiscountManager.onSelectAllDiscount);
		
		BrandDiscountManager.refreshDiscountList();
	});

	function doAction(pageNo){
		var pageNumber = parseInt(pageNo);
		if (pageNumber <= 1){
			pageNumber = 1;
		}
		if (pageNo == ''){
			pageNumber = -1;
		}
		BrandDiscountManager.gotoPage(pageNumber);
	}
</script>

<div class="tabbable">
	<jsp:include page="/common/adminTabs.jsp" />
  	<div class="tab-content">
		<div class="tab-pane active" id="brands_price_div">
			<div class="sm-ui-component">	
				<!-- h3><s:message code="menu.price-brands" text="Brands Discount" /></h3 -->
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-md-5" >
			<fieldset >
				<legend>添加/编辑品牌折扣</legend>
				<label>Step1：选择品牌</label>
				<div style="display: inline-block;padding-left: 50px;">
					<label>品牌名称</label><input id="input_add_brand_name"></input>
					<label>商家名称</label><input id="input_add_store_name"></input>
				</div>
				
				<br/>
				<label>Step2：设置折扣</label>
				<div style="display: inline-block;padding-left: 50px;">
					<label>所选品牌：</label><span id="span_add_brand_displayName">无</span>
					<br/>
					<label>折扣率：</label><input id="input_add_discount" value="0.0"></input>(0.0~1.0)
					<input id="input_add_brandId" type="hidden"></input>
					<input id="input_add_storeId" type="hidden"></input>
				</div>
				<br/>
				<button class="btn btn-success" onclick="BrandDiscountManager.beforeAddDiscount()">确定</button>
				<button id="btn_delete_discount" class="btn btn-success disabled" onclick="BrandDiscountManager.beforeDeleteDiscount()">删除</button>
			</fieldset>
		</div>
		<div class="col-md-7">
			<fieldset>
				<legend>品牌折扣列表</legend>
				<label>搜索：</label>
				<div style="display: inline-block;padding-left: 50px;">
					<label>品牌名称</label><input id="input_filter_brand_name"></input>
					<label>商家名称</label><input id="input_filter_store_name"></input>
				</div>
				<a href="#" class="pull-right" style="padding-left: 10px;" onclick="BrandDiscountManager.deleteAllDiscount()">删除所有</a>
				<a href="#" class="pull-right" onclick="BrandDiscountManager.deleteSelectedDiscount()">删除被选</a>
				<br/>
				<table class="table table table-striped table-bordered table-hover">
					<thead>
						<tr>
							<th><input type="checkbox" id="select_all_discount"></input></th>
							<th>品牌名称(ID)</th>
							<th>商家名称</th>
							<th>折扣率</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody id="tbody_brand_discount">
					</tbody>
				</table>
				<br/>
				<label id="pagination_sumary"></label>
				<div id="pagination" class="pull-right">
					<jsp:include page="/common/paginationFind.jsp"/>
				</div>
			</fieldset>
		</div>
	</div>
	<div id="brand_and_store_list_div">
		<button class="pull-right" onclick="BrandDiscountManager.closeBrandList()">x</button>
		<div id="brand_and_store_list" ></div>
	</div>
</div>
  
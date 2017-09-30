
<%
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %> 

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>



<script>
 
 var START_COUNT_PRODUCTS = 1;
 var CURRENT_PRODUCTS =1;
 var MAX_PRODUCTS = 5;
 var url = '<%=request.getContextPath()%>/services/public/search/<c:out value="${requestScope.MERCHANT_STORE.code}"/>/<c:out value="${requestScope.LANGUAGE.code}"/>/' + CURRENT_PRODUCTS + '/' + MAX_PRODUCTS + '/term.html';
 var myArray=new Array();
 

 $(function(){		
	 search();

 });
 
 
	<jsp:include page="/pages/shop/templates/bootstrap/sections/shop-listing.jsp" />
	 
	function dosearch(){
		START_COUNT_PRODUCTS =1;
		CURRENT_PRODUCTS =1;
		search();
	}
 
 	function search() { 	
 	url = '<%=request.getContextPath()%>/services/public/search/<c:out value="${requestScope.MERCHANT_STORE.code}"/>/<c:out value="${requestScope.LANGUAGE.code}"/>/' + CURRENT_PRODUCTS + '/' + MAX_PRODUCTS + '/term.html';
 		$('#productsTable').eq(0).nextAll().remove();
 		searchProducts(url, '#productsTable', '<c:out value="${q}"/>', null);
	}

 	function dofilter(filter) {	 
 		START_COUNT_PRODUCTS =1;
		CURRENT_PRODUCTS =1;
 	 	url = '<%=request.getContextPath()%>/services/public/search/<c:out value="${requestScope.MERCHANT_STORE.code}"/>/<c:out value="${requestScope.LANGUAGE.code}"/>/' + CURRENT_PRODUCTS + '/' + MAX_PRODUCTS + '/term.html';
 	 		$('#productsTable').eq(0).nextAll().remove();	
 	 		searchProducts(url, '#productsTable', '<c:out value="${q}"/>', myArray[filter]);
 		}
 	
	function callBackSearchProducts(productList) {		
		//facets
		if (productList.categoryFacets != null) {
		$(categoriesFacets).empty();
			var activemenu = productList.activeMenu;
			var showallproducts = showallproducts='<li>';
			if (activemenu == null){
				showallproducts='<li class="active">';
			}	
			showallproducts = showallproducts + ' <a href="javascript:void(0);" onclick="dosearch();return false;">';
			showallproducts = showallproducts +' <s:message code="menu.products-products" text="Show all products" /></a></li> ';
			    $(categoriesFacets).append(showallproducts)
			for (var i = 0; i < productList.categoryFacets.length; i++) {
				var categoryFacets = '<li>';				
				myArray[i] = productList.categoryFacets[i].code;
				if(activemenu != null && myArray[i] == activemenu){
					categoryFacets = '<li class="active">';
				}
				categoryFacets = categoryFacets
						+ '<a href="javascript:void(0);" onclick="dofilter('+i+');return false;">'
						+ productList.categoryFacets[i].description.name;						
				if (productList.categoryFacets[i].productCount > 0) {
					categoryFacets = categoryFacets
							+ '&nbsp;<span class="countItems">('
							+ productList.categoryFacets[i].productCount
							+ ')</span>'
				}
				categoryFacets = categoryFacets + '</a>';
				categoryFacets = categoryFacets + '</li>';				
				$(categoriesFacets).append(categoryFacets);
			}
		}

		$('#productsTable').hideLoading();
		if(productList.productCount==0){
			var productQty = '&nbsp;'				+ productList.productCount				+ ' <s:message code="label.search.items.found" text="item(s) found" />';
			$('#productsTable').html(productQty);
		}

	}
</script>

<jsp:include
	page="/pages/shop/templates/bootstrap/sections/breadcrumb.jsp" />
<div class="row-fluid">
	<div class="span12">

		<table width="100%" border="1" frame=vsides class="product-list-table">
			<tr>
				<!-- left column -->
				<!--Search facets-->
				<td valign="top" width="170px">
					<div id="searchlist" >
						<div class="sidebar-nav">
							<ul id="categoriesFacets" class="nav nav-list">
							</ul>
						</div>
					</div>
				</td>
				<td valign="top">
					<!-- right column -->
					<div style="width:100%;height:100% ;">
						<div id="slectitem" class="pull-left"  style="display:none;">
							<select id="selects" style="width:45px;height:35px;" onChange="onClickSelect(this.options[this.options.selectedIndex].value)">
								<option>5</option>
								<option>10</option>
								<option>15</option>
								<option>20</option>
								<option>25</option>
							</select>
						</div>
						<div id="pageitem" style="display:none;">
							<ul id="pages" class="pagination pull-right">								
							</ul>
						</div>
						<table id="productsTable"
							class="table table-condensed table-hover table-striped">
							<thead><tr><th width="15%" data-column-id="image" ><s:message code="label.product.image" text="Image" /></th>
							<th width="50%" data-column-id="name"><s:message code="label.productedit.productname" text="Name" /></th>
							<th width="15%" data-column-id="quality"><s:message code="label.product.quality" text="Quality" /></th>
							<th width="10%" data-column-id="price" data-type="numeric" data-order="desc"><s:message code="label.product.price" text="Price" /></th>
							<th width="10%" data-column-id="operate"><s:message code="label.product.operate" text="Operate" /></th></tr></thead>
						</table>

					</div>
				</td>
			</tr>
		</table>
	</div>
	<!-- 12 -->

</div>
<!-- row fluid -->
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
 
 var filter = null;
 var filterValue = null;
 
 var START_COUNT_PRODUCTS = 1;
 var CURRENT_PRODUCTS =1; 
 var MAX_PRODUCTS = 5; 
 var url = '<%=request.getContextPath()%>/services/public/products/page/' + CURRENT_PRODUCTS + '/' + MAX_PRODUCTS + '/<c:out value="${requestScope.MERCHANT_STORE.code}"/>/<c:out value="${requestScope.LANGUAGE.code}"/>/<c:out value="${category.description.friendlyUrl}"/>.html';
 

 $(function(){
	
	
	search();

 });
 
 
	<jsp:include page="/pages/shop/templates/bootstrap/sections/shop-catalog.jsp" />
	 
	  
	 
 	function search() {
 		url = '<%=request.getContextPath()%>/services/public/products/page/' + CURRENT_PRODUCTS + '/' + MAX_PRODUCTS + '/<c:out value="${requestScope.MERCHANT_STORE.code}"/>/<c:out value="${requestScope.LANGUAGE.code}"/>/<c:out value="${category.description.friendlyUrl}"/>.html';
	 	//alert(url);
 		if(filter!=null) {
 			url = url + '/filter=' + filter + '/filter-value=' + filterValue +'';
 		}
 		$('#productsTable').eq(0).nextAll().remove();
 		loadProducts(url,'#productsTable');
 	}
 	
 	function filterCategory(filterType,filterVal) {
	 		//reset product section
	 		//$('#productsTable').html('');
	 		$('#productsTable').eq(0).nextAll().remove();
	 		START_COUNT_PRODUCTS = 1;
	 		filter = filterType;
	 		filterValue = filterVal;
	 		search();
 	}
 	
 
	function callBackLoadProducts(productList) {
						
			$('#productsTable').hideLoading();
			if(productList.productCount==0){
			var productQty = productList.productCount + ' <s:message code="label.search.items.found" text="item(s) found" />';
			$('#productsTable').html(productQty);
				}
	}
 
</script>

       <jsp:include page="/pages/shop/templates/bootstrap/sections/breadcrumb.jsp" />

	<div class="row-fluid">   
        <div class="span12">

		<table width="100%" border="1" frame=vsides class="product-list-table">
			<tr>
				<!-- left column -->
				<!--Search facets-->
				<td valign="top" width="170px">
					<div class="sidebar-nav">
            <ul class="nav nav-list">
              <c:if test="${parent!=null}">
              	<li class="nav-header"><c:out value="${parent.description.name}" /> </li>
              </c:if>
              <c:forEach items="${subCategories}" var="subCategory">
              	<li>
              		<a href="<c:url value="/shop/category/${subCategory.description.friendlyUrl}.html"/><sm:breadcrumbParam categoryId="${subCategory.id}"/>"><c:out value="${subCategory.description.name}" />
              			<c:if test="${subCategory.productCount>0}">&nbsp;<span class="countItems">(<c:out value="${subCategory.productCount}" />)</span></c:if></a></li>
              </c:forEach>
              <c:if test="${fn:length(manufacturers) > 10}">
          		<br/>
          		 <li class="nav-header"><s:message code="label.manufacturer.brand" text="Brands" /></li>
              <c:forEach items="${manufacturers}" var="manufacturer" end="10">
              	<li>
              		<a href="javascript:filterCategory('BRAND','${manufacturer.id}')"><c:out value="${manufacturer.description.name}" /></a></li>
              </c:forEach>             
              </c:if>
              <c:if test="${fn:length(manufacturers) < 11}">
          		<br/>
          		 <li class="nav-header"><s:message code="label.manufacturer.brand" text="Brands" /></li>
              <c:forEach items="${manufacturers}" var="manufacturer" >
              	<li>
              		<a href="javascript:filterCategory('BRAND','${manufacturer.id}')"><c:out value="${manufacturer.description.name}" /></a></li>
              </c:forEach>              
              </c:if>
            </ul>
          </div>
				</td>
				<td valign="top">
					<!-- right column -->
					<div style="width:100%;height:100% ;">
						<div id="slectitem" class="pull-left"  style="display:none;">
							<select id="selects" style="width:50px;height:35px;" onChange="onClickSelect(this.options[this.options.selectedIndex].value)">
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
        
      </div><!-- row fluid -->
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
</style>
<jsp:include page="/common/adminTabs.jsp" />
<div class ="row">
	<!-- begin left menu content -->
	<div style="float:left;width:15%;padding-right:5px;">
		<jsp:include page="/pages/admin/products/product-menu.jsp" >
			<jsp:param name="active" value="related" />
		</jsp:include>
	</div><!-- end left menu content -->
	<div id="showloading"></div>	
	<!-- begin middle main content -->
	<div style="float:left;width:85%;" id="p-title">
		<h3><s:message code="label.product.related.title" text="Related items" /></h3> 
		<br/>
		<strong><c:out value="${product.sku}"/></strong>			
		<br/>
		<div class="alert alert-info">
			<s:message code="label.product.related.meassage" text="Drag and drop product from product list to related items box"/>
		</div>	
		
		<br/>
		<!-- Listing grid include -->
		 <c:set value="/admin/products/all.html" var="pagingUrl" scope="request"/>
		 <c:set value="/admin/catalogue/related/paging.html?productId=${productId}" var="containerFetchUrl" scope="request"/>
		 <c:set value="/admin/catalogue/related/removeItem.html?baseProductId=${productId}" var="containerRemoveUrl" scope="request"/>
		 <c:set value="RELATED" var="removeEntity" scope="request"/>
		 <c:set value="/admin/catalogue/related/addItem.html?baseProductId=${productId}" var="containerAddUrl" scope="request"/>
		 <c:set value="/admin/catalogue/related/update.html" var="containerUpdateUrl" scope="request"/>
		 <c:set value="/admin/products/editProduct.html" var="editUrl" scope="request"/>
		 <c:set value="/admin/catalogue/related/list.html?id=${productId}" var="reloadUrl" scope="request"/>
		 <c:set var="componentTitleKey" value="label.product.related.title" scope="request"/>
		 <!-- same headers than featured -->
		 <c:set var="gridHeader" value="/pages/admin/products/featured-gridHeader.jsp" scope="request"/>
		 <c:set var="gridHeaderContainer" value="/pages/admin/products/featured-gridHeader.jsp" scope="request"/>
		 <c:set var="canRemoveEntry" value="true" scope="request"/>
		 <c:set var="gridwidth" value="80%" scope="request"/>
		 <c:set var="gridheiht" value="60%" scope="request"/>

          	 <jsp:include page="/pages/admin/components/product-container.jsp"></jsp:include> 
		 <!-- End listing grid include -->
	</div><!-- end middle main content -->
</div>

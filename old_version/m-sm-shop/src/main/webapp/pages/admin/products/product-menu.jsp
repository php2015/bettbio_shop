<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>
<ul class="nav nav-pills nav-stacked">
	<li id="product"><a href="<c:url value="/admin/products/editProduct.html" />?id=<c:out value="${productId}"/>"><s:message code="label.product.details" text="Product details" /></a></li>
	<li id="prices"><a href="<c:url value="/admin/products/prices.html" />?id=<c:out value="${productId}"/>"><s:message code="label.product.prices" text="Product prices" /></a></li>
	<!-- <li id="attributes"><a href="<c:url value="/admin/products/attributes/list.html" />?id=<c:out value="${productId}"/>"><s:message code="label.product.attributes" text="Attributes" /></a></li> -->
	<li id="images"><a href="<c:url value="/admin/products/images/list.html" />?id=<c:out value="${productId}"/>"><s:message code="menu.catalogue-products-images" text="Product images" /></a></li>
	<li id="reviews"><a href="<c:url value="/admin/products/reviews.html" />?id=<c:out value="${productId}"/>"><s:message code="label.product.customer.reviews" text="Reviews" /></a></li>
	<!-- <li id="related"><a href="<c:url value="/admin/catalogue/related/list.html" />?id=<c:out value="${productId}"/>"><s:message code="label.product.related.title" text="Related items" /></a></li> -->
	<%-- <li id="keywords"><a href="<c:url value="/admin/products/product/keywords.html" />?id=<c:out value="${productId}"/>"><s:message code="label.product.searchkeywords" text="Search keywords" /></a></li>--%>
	<li id="digitalProduct"><a href="<c:url value="/admin/products/digitalProduct.html" />?id=<c:out value="${productId}"/>"><s:message code="label.product.digitalproduct" text="Digital product" /></a></li> 
	<li id="productToCategories"><a href="<c:url value="/admin/products/displayProductToCategories.html" />?id=<c:out value="${productId}"/>"><s:message code="menu.product.category" text="Associate to categories" /></a></li>
	<li id="certificates"><a href="<c:url value="/admin/products/certificates.html" />?id=<c:out value="${productId}"/>"><s:message code="label.product.certificates" text="Product certificates" /></a></li>
	<li id="proofs"><a href="<c:url value="/admin/products/proofs.html" />?id=<c:out value="${productId}"/>"><s:message code="label.product.proofs" text="Product proofs" /></a></li>
	<li id="thirdproofs"><a href="<c:url value="/admin/products/thirdproofs.html" />?id=<c:out value="${productId}"/>"><s:message code="label.product.thirdproofs" text="Product thirdproofs" /></a></li>
	<li id="selfproofs"><a href="<c:url value="/admin/products/selfproofs.html" />?id=<c:out value="${productId}"/>"><s:message code="label.product.selfproofs" text="Product selfproofs" /></a></li>
</ul>
<script>
$(function(){
	//active css of menu 
	var active = '${param.active}';
	$("#"+active).attr("class", "active");
})
</script>
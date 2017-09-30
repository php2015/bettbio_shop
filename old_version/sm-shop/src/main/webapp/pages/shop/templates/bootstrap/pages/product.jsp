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

<script src="<c:url value="/resources/js/jquery.elevateZoom-3.0.8.min.js" />"></script>
<script src="<c:url value="/resources/js/jquery.raty.min.js" />"></script>



			<jsp:include page="/pages/shop/templates/bootstrap/sections/productbreadcrumb.jsp" />
            
            <div class="row-fluid">

                <div itemscope class="span12" itemtype="http://data-vocabulary.org/Product">
                    	<!-- Image column -->
						<div id="img" class="span4 productMainImage">
							<c:if test="${product.image!=null}">
							<span id="mainImg"><img id="im-<c:out value="${product.image.id}"/>" alt="<c:out value="${product.description.name}"/>" src="<c:url value="${product.image.imageUrl}"/>" data-zoom-image="<sm:shopProductImage imageName="${product.image.imageName}" sku="${product.sku}" size="LARGE"/>"></span>												
							<script>
								$(function() {
									setImageZoom('im-<c:out value="${product.image.id}"/>');
								});	
							</script>
							<c:if test="${product.images!=null && fn:length(product.images)>1}">
								<ul id="imageGallery" class="thumbnails small">
									<c:forEach items="${product.images}" var="thumbnail">								
									<li class="span2">
										<a href="#img" class="thumbImg" title="<c:out value="${thumbnail.imageName}"/>"><img id="im-<c:out value="${thumbnail.id}"/>" src="<c:url value="${thumbnail.imageUrl}"/>" data-zoom-image="<sm:shopProductImage imageName="${thumbnail.imageName}" sku="${product.sku}" size="LARGE"/>" alt="<c:url value="${thumbnail.imageName}"/>" ></a>
									</li>
									</c:forEach>								
								</ul>
							</c:if>
							</c:if>
						</div>
						
						<!-- Google rich snippets (http://blog.hubspot.com/power-google-rich-snippets-ecommerce-seo-ht) -->
						<!-- Product description column -->
						<div class="span8">
							<p class="lead"><strong>${product.description.name}</strong></p>
							
							
							<!-- product rating -->
							<jsp:include page="/pages/shop/common/catalog/rating.jsp" />
							
							
							<address>
								<strong><s:message code="label.product.brand" text="Brand"/></strong> <span itemprop="brand"><c:out value="${product.manufacturer.description.name}" /></span><br>
								<strong><s:message code="label.product.code" text="Product code"/></strong> <span itemprop="identifier" content="mpn:${product.sku}">${product.sku}</span><br>								
							</address>
							<span itemprop="offerDetails" itemscope itemtype="http://data-vocabulary.org/Offer">
							<meta itemprop="seller" content="${requestScope.MERCHANT_STORE.storename}"/>
							<meta itemprop="currency" content="<c:out value="${requestScope.MERCHANT_STORE.currency.code}" />" />
							<h3 id="productPrice">
									<c:choose>
										<c:when test="${product.discounted}">
												<del><c:out value="${product.originalPrice}" /></del>&nbsp;<span class="specialPrice"><span itemprop="price"><c:out value="${product.finalPrice}" /></span></span>
										</c:when>
										<c:otherwise>
												<span itemprop="price"><c:out value="${product.finalPrice}" /></span>
										</c:otherwise>
									</c:choose>
							</h3>
							
							<address>
								<strong><s:message code="label.product.available" text="Availability"/></strong> <span><c:choose><c:when test="${product.quantity>0}"><span itemprop="availability" content="in_stock">${product.quantity}</span></c:when><c:otherwise><span itemprop="availability" content="out_of_stock"><s:message code="label.product.outofstock" text="Out of stock" /></c:otherwise></c:choose></span><br>								
							</address>
							
							</span>
							<p>
								<jsp:include page="/pages/shop/common/catalog/addToCartProduct.jsp" />
							</p>
						</div>

					</div>
			 </div>
			 <div class="row-fluid">
                    <div class="span12">

							<ul class="nav nav-tabs" id="productTabs">
								<li class="active"><a href="#description"><s:message code="label.productedit.productdesc" text="Product description" /></a></li>
								<c:if test="${attributes!=null}"><li><a href="#specifications"><s:message code="label.product.attribute.specifications" text="Specifications" /></a></li></c:if>								
								<c:if test="${product.productCertificates != null}"><li><a href="#certificates"><s:message code="label.product.certificates" text="Certificates" /></a></li></c:if>
								<c:if test="${product.productProofs != null}"><li><a href="#productProofs"><s:message code="label.product.proofs" text="ProductProofs" /></a></li></c:if>
								<c:if test="${product.thirdProofs != null}"><li><a href="#thirdProofs"><s:message code="label.product.thirdproofs" text="ThirdProofs" /></a></li></c:if>
								<li><a href="#reviews"><s:message code="label.product.customer.reviews" text="Customer reviews" /></a></li>
							</ul>							 
							<div class="tab-content">
								<div class="tab-pane active" id="description">
									<c:out value="${product.description.description}" escapeXml="false"/>
								</div>	
								<c:if test="${product.productCertificates != null}">
									<div class="tab-pane" id="certificates">
										<table id="certiTable" class="table table-condensed table-hover table-striped">
											<thead>
						                        <tr>
						                            <th data-column-id="name" ><s:message code="label.product.certificate.name" text="Name" /></th>
						                            <th data-column-id="baseinfo"><s:message code="label.product.certificate.baseinfo" text="Basicinfo" /></th>
						                            <th data-column-id="docUrl"><s:message code="label.product.certificate.docurl" text="DocURL" /></th>
						                            <th data-column-id="certificateImage"> <s:message code="label.product.certificate.image" text="Image" /></th>
						                            <th data-column-id="title"><s:message code="label.product.certificate.title" text="Memo" /></th>
						                            <th data-column-id="description"><s:message code="label.product.certificate.description" text="Description" /></th>
						                        </tr>
						                         </thead>
						                        <c:forEach items="${product.productCertificates}" var="certificate" varStatus="status">
								                    <tr>
			                        					<td><c:out value="${certificate.name}"/> </td>
														<td><c:out value="${certificate.baseinfo}" /></td>
														<td><c:out value="${certificate.docUrl}"/></td>														
														<td>
														<img alt="<c:out value="${certificate.name}"/>" src="<c:url value="${certificate.rimage}"/>"/></td>											
														<td><c:out value="${certificate.title}"/></td>
														<td><c:out value="${certificate.description}" escapeXml="false"/></td>
													</tr>
						                        </c:forEach>						                   
										</table>
									</div>
								</c:if>
								<c:if test="${product.productProofs != null}">
									<div class="tab-pane" id="productProofs">
										<table id="proofTable" class="table table-condensed table-hover table-striped">
											<thead>
						                        <tr>
						                            <th data-column-id="buyer" ><s:message code="label.product.proof.buyer" text="Buyer"/></th>
						                            <th data-column-id="dateBuyed"><s:message code="label.product.proof.dateBuyed" text="DateBuyed"/></th>
						                            <th data-column-id="description"><s:message code="label.product.proof.description" text="Description"/></th>
						                            <th data-column-id="proofImage"> <s:message code="label.product.proof.image" text="Image"/></th>
						                            <th data-column-id="title"><s:message code="label.product.proof.title" text="Memo"/></th>						                            
						                        </tr>
						                           </thead>
						                        <c:forEach items="${product.productProofs}" var="proof" varStatus="status">
								                    <tr>
			                        					<td><c:out value="${proof.buyer}"/> </td>
														<td><c:out value="${proof.dateBuyed}" /></td>
														<td><c:out value="${proof.description}" escapeXml="false"/></td>														
														<td>
														<img alt="<c:out value="${proof.buyer}"/>" src="<c:url value="${proof.rimage}"/>"/></td>
														<td><c:out value="${proof.title}"/></td>												
													</tr>
						                        </c:forEach>						                 
										</table>
									</div>
								</c:if>
								<c:if test="${product.thirdProofs != null}">
									<div class="tab-pane" id="thirdProofs">
										<table id="thirdProofs" class="table table-condensed table-hover table-striped">
											<thead>
						                        <tr>
						                            <th data-column-id="name" ><s:message code="label.product.thirdproof.thirddetection" text="Name"/></th>
						                            <th data-column-id="description"><s:message code="label.product.thirdproof.description" text="Description"/></th>						                            
						                            <th data-column-id="thirdproofImage"> <s:message code="label.product.thirdproof.image" text="Image"/></th>
						                            <th data-column-id="title"><s:message code="label.product.thirdproof.title" text="Memo"/></th>						                            
						                        </tr>
						                           </thead>
						                        <c:forEach items="${product.thirdProofs}" var="tproof" varStatus="status">
								                    <tr>
			                        					<td><c:out value="${tproof.name}"/> </td>
														<td><c:out value="${tproof.description}" escapeXml="false"/></td>
														<td>
														<img alt="<c:out value="${tproof.name}"/>" src="<c:url value="${tproof.rimage}"/>"/></td>
														<td><c:out value="${tproof.title}"/></td>												
													</tr>
						                        </c:forEach>						                 
										</table>
									</div>
								</c:if>
								<div class="tab-pane" id="specifications">
									<!--  read only properties -->
									<c:if test="${attributes!=null}">
										<table>
										<c:forEach items="${attributes}" var="attribute" varStatus="status">
										<tr>
	                        				<td><c:out value="${attribute.name}"/> : </td>
											<td><c:out value="${attribute.readOnlyValue.description}" /></td>
										</tr>
									</c:forEach>
									</table>
								  </c:if>
								</div>
								<div class="tab-pane" id="reviews">

									<!-- reviews -->
									<jsp:include page="/pages/shop/common/catalog/reviews.jsp" />


								</div>						
                        </div>	
                        <br/>
                        <br/>
                        <!-- Related items -->
                        <c:if test="${relatedProducts!=null}">	
                        			<h1><s:message code="label.product.related.title" text="Related items"/></h1>				
									<ul class="thumbnails product-list">
										<!-- Iterate over featuredItems -->
                         				<c:set var="ITEMS" value="${relatedProducts}" scope="request" />
	                         			<jsp:include page="/pages/shop/templates/bootstrap/sections/productBox.jsp" />
									</ul>
						</c:if>
						
						
                    </div>
                </div>

            

		<script>
			
			$(function () {
				$('#productTabs a:first').tab('show');
				$('#productTabs a').click(function (e) {
					e.preventDefault();
					$(this).tab('show');
				})
				$('.thumbImg').click(function (e) {
					img = $(this).find('img').clone();
					$('#mainImg').html(img);
					setImageZoom(img.attr('id'));
				})
			})
			function setImageZoom(id) {
			    $('#' + id).elevateZoom({
		    			zoomType	: "lens",
		    			lensShape : "square",
		    			lensSize    : 240
		   		}); 
			}
			
			
		</script>
    

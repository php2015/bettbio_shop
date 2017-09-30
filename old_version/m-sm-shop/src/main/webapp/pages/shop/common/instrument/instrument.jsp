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
<link href="<c:url value="/resources/templates/bootstrap3/css/product.css" />" rel="stylesheet">
<script src="<c:url value="/resources/js/jquery.raty.min.js" />"></script>
<script src="<c:url value="/resources/js/bootstrap/bootstrap.min.js"/>"></script>
<script src="<c:url value="/resources/js/stickUp.min.js"/>"></script>
<!-- begin body -->
<div class="body">
	<!-- begin breadcrumb -->
	
		<div class="container-fluid">
		<ul class="breadcrumb">
		  <c:forEach items="${requestScope.BREADCRUMB.breadCrumbs}" var="breadcrumb" varStatus="count">
			  <li class="active">
			    <a href="${breadcrumb.url}<sm:breadcrumbParam/>">${breadcrumb.label}</a>
			  </li>
		  </c:forEach>
		</ul>
	</div>
	
	<!-- begin product header -->
	<div class="row product-header">
        	<!-- Image column -->
			<div id="img" class="col-xs-12" style="padding:0px;margin:0px;">
				<c:if test="${product.image!=null}">
					<span id="mainImg"><img class="product-img" style="width:100%" id="im-<c:out value="${product.image.id}"/>" alt="<c:out value="${product.description.name}"/>" src="<c:url value="${product.image.imageUrl}"/>" data-zoom-image="<sm:shopProductImage imageName="${product.image.imageName}" sku="${product.sku}" size="LARGE"/>"></span>												
					</c:if>
			</div>
						
			<!-- Product description column -->
			<div class="col-xs-12">
					<div class="row">
						<div class="col-xs-12">
							<label><s:message code="label.productedit.productname" text="Name"/>:</label><span itemprop="brand" style="color:#000; word-break: break-all;line-height:20px">${product.description.name}</span>
						</div>
					</div>
					<c:if test="${not empty product.description.enName }">
						<div class="row">
						<div class="col-xs-12">
							<label><s:message code="label.productedit.productenname" text="English Name"/>:</label><span itemprop="brand" style="color:#000; word-break: break-all;line-height:20px"> ${product.description.enName}</span>
						</div>
					</div>
					</c:if>					
					<div class="row">
						<div class="col-xs-12">
							<label><s:message code="label.productedit.productsimpdesc" text="Simple Description"/>:</label><span style="color:#E3393C;word-break: break-all;line-height:20px">${product.description.simpleDescription}</span>
						</div>
					</div>
					<c:if test="${not empty product.description.storecondDescription }">
						<div class="row">
						<div class="col-xs-12">
							<label><s:message code="label.productedit.productstoreconddesc" text="Contact Pattern"/>:</label><span itemprop="brand" style="color:#000"> ${product.description.storecondDescription}</span>
						</div>
					</div>
					</c:if>					
					<div class="row">
						<div class="col-xs-6">
								<label><s:message code="label.product.quality" text="Quality"/>:</label>
								<c:forEach begin="20" end="${product.qualitysocre}"  step="20">
									<img  src="<c:url value="/resources/img/stars/star-on.png" />">
									</c:forEach>
									<c:if test="${product.qualitysocre%20 > 0 }">
										<img  src="<c:url value="/resources/img/stars/star-half.png" />">
									</c:if><br>
								<label><s:message code="label.contentImages.store" text="Store"/>:</label>&nbsp;&nbsp;&nbsp;&nbsp;${product.store.storename}<br>
								<label><s:message code="label.generic.city" text="City"/>:</label>&nbsp;&nbsp;&nbsp;&nbsp;${product.store.storecity}<br>
								<label><s:message code="label.tax.storeaddress" text="Address"/>:</label>&nbsp;&nbsp;&nbsp;&nbsp;${product.store.storeaddress}<br>
								<label><s:message code="label.generic.postalcode" text="Postalcode"/>:</label>&nbsp;&nbsp;&nbsp;&nbsp;${product.store.storepostalcode}<br>	
								<c:if test="${product.store.useQQ == true}">
									<c:if test="${product.store.qqNum != null}">										
										<label><a target="_blank" href="mqqwpa://im/chat?chat_type=wpa&uin=${product.store.qqNum}&version=1&src_type=web&web_src=oicqzone.com"><span style="line-height:25px;"><s:message code="label.store.qqOnline" text="QQ Online"/>:</span><img border="0" src="http://wpa.qq.com/pa?p=2:${product.store.qqNum}:52" /></a></label><br>
									</c:if>
								</c:if>
								<c:if test="${not empty product.store.storephone}">
									<label>联系电话: </label><a href="tel://${product.store.storephone}"><span style="padding-left: 10px; color:blue;">${product.store.storephone}</span></a>
								</c:if>								
						</div>
						<div class="col-xs-6">
								<label><s:message code="label.product.brand" text="Brand"/>:</label>&nbsp;&nbsp;&nbsp;&nbsp; <span itemprop="brand" style="color:#000"><c:out value="${product.manufacturer.description.name}" /></span><br/>
								<label><s:message code="label.product.code" text="Product code"/>:</label>&nbsp;&nbsp;&nbsp;&nbsp; <span itemprop="identifier" style="color:#000;word-break: break-all;" content="mpn:${product.code}">${product.code}</span><br/>
								<jsp:include page="/pages/shop/common/catalog/rating.jsp" />
								<label><s:message code="label.instrumentedit.batchnum" text="Type"/>:</label>&nbsp;&nbsp;&nbsp;&nbsp;<span itemprop="brand" style="color:#000"><c:out value="${product.batchnum}" /></span><br/> 
								<label><s:message code="label.instrumentedit.cas" text="Made"/>:</label>&nbsp;&nbsp;&nbsp;&nbsp;<span itemprop="brand" style="color:#000"><c:out value="${product.cas}" /></span><br> 			
						</div>
					</div>
			</div>
		
				
	</div><!-- end product header -->
	<div class="row main-padding-lr">
			<div class="col-xs-12">
				<table class="table table-bordered table-hover table-condensed product-table" style="font-size:12px;padding-left:10px;">
					<tr style="font-weight:bold;background-color: #3498db;color:#fff">
						<td><s:message code="label.product.specification" text="Specification"/></td>
						<td><s:message code="label.order.price" text="Price"/></td>
						<td><s:message code="label.product.price.special" text="Special"/></td>
						<td><s:message code="label.product.price.period" text="Special"/></td>
						<td><s:message code="label.quantity" text="Quantity"/></td>
						<td></td>
					</tr>
					<c:forEach items="${product.prices }" var="price" varStatus="vstatus">
						<c:choose>
			                <c:when test="${vstatus.index%2==0}">
			      				<tr class="info" id="price_${price.id }">
			                </c:when>
			                <c:otherwise>
			        			<tr id="price_${price.id }">
			                </c:otherwise>
			            </c:choose>
			            	<td style="line-height:25px!important">${price.name }</td>
							<td style="line-height:25px!important">
								<c:choose>
									<c:when test="${not empty price.special}">
										<s>${price.price }</s>
									</c:when>
									<c:when test="${price.priceValue == '0'}">
										<span style="color:blue;">询价</span>
									</c:when>
									<c:otherwise>
										
										${price.price }
									</c:otherwise>
								</c:choose>
							</td>
							<td style="line-height:25px!important">${price.special }</td>
							<td style="line-height:25px!important">${price.period }</td>
							<td style="line-height:25px!important ;width:45px;padding:2px;margin:0px;" class="text-center">
                                <div class="input-group text-center" name="size_quantity">
									<input type="number" style="width:45px;height:28px" placeholder="<s:message code="label.quantity" text="Quantity"/>"
											value="1" name="quantity" id="qty-productId-${product.id}-${price.id }">
								</div>
							</td>
							<td class="text-center" style="line-height:25px!important">
								<c:choose>
									<c:when test="${price.priceValue == '0'}">
										<div>
											<span aria-hidden="true" class="carticon fa fa-shopping-cart fa-2x" style="color: #aaa;"></span>
										</div>
									</c:when>
									<c:otherwise>
										<div productId='<c:out value="${product.id}" />' style="cursor:pointer;" onclick="addToCart('${product.id}', '${price.id }')">
											<span aria-hidden="true" class="carticon fa fa-shopping-cart fa-2x" style="color: #449d44;"></span>
										</div>
									</c:otherwise>
								</c:choose>
								</td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
	<!-- begin product main block -->
	<div class="product-main main-padding-lr">
	<!-- begin product-main content -->
	<div>
		<!-- begin description block -->
		<div id="description" class="productdesc featurette">
			<div>
				<div class="productdesc-title"><s:message code="label.productedit.productdesc" text="Postalcode"/></div>
			</div>
			<div>
			${product.description.description}
			</div>
		</div><!-- end description block -->
		
		<!-- begin thirdproof block -->
		<div id="thirdproof" class="productdesc featurette">
			<div>
				<div class="productdesc-title"><s:message code="label.product.thirdproof" text="Thirdproof"/></div>
			</div>
			<div style="margin:0px 20px;">
				<c:choose>
					<c:when test="${not empty  product.thirdProofs}">
						<div class="row">
							<c:forEach items="${product.thirdProofs }" var="thirdProof" varStatus="vstatus">
							<div class="col-sm-6" style="font-weight:bold;font-size:14px;">
								<div><s:message code="label.product.thirdproof.thirddetection" text="Thirdproof"/>:${thirdProof.displayName }</div>
								<div><s:message code="label.product.thirdproof.description" text="Thirdproof"/>:<br/><div style="font-weight:normal;">${thirdProof.description }</div></div>
								<div>
									<a href="<c:url value="/shop/dispalyImage.html?ipath=${thirdProof.rimage}"/>" target="blank">
										<img style="width:80%" alt="<c:out value="${thirdProof.name}"/>" src="<c:url value="${thirdProof.rimage}"/>"/>
									</a>
								</div>
							</div>
							</c:forEach>
						</div>
					</c:when>
					<c:otherwise>
						<div>&nbsp;</div>
					</c:otherwise>
				</c:choose>
				
			</div>
		</div><!-- end thirdproof block -->
		
		<!-- begin proof block -->
		<div id="proof" class="productdesc featurette productdesc-even-background">
			<div>
				<div class="productdesc-title"><s:message code="label.product.proof" text="Thirdproof"/></div>
			</div>
			<div style="margin:0px 20px;">
				<c:choose>
					<c:when test="${not empty  product.productProofs}">
						<div class="row">
							<c:forEach items="${product.productProofs }" var="proof" varStatus="vstatus">
							<div class="col-sm-6" style="font-weight:bold;font-size:14px;">
								<div><s:message code="label.product.proof.buyer" text="Thirdproof"/>:${proof.buyer }</div>
								<div><s:message code="label.product.proof.dateBuyed" text="Thirdproof"/></div>
								<div><s:message code="label.product.proof.description" text="Thirdproof"/>:<br/><div style="font-weight:normal;">${proof.description }</div></div>
								<div>
									<a href="<c:url value="/shop/dispalyImage.html?ipath=${proof.rimage}"/>" target="blank">
										<img style="width:80%" alt="<c:out value="${proof.displayName}"/>" src="<c:url value="${proof.rimage}"/>"/>
									</a>
								</div>
							</div>
							</c:forEach>
						</div>
					</c:when>
					<c:otherwise>
						<div>&nbsp;</div>
					</c:otherwise>
				</c:choose>
				
			</div>
		</div><!-- end proof block -->
		
		<!-- begin review block -->
		<div id="reviews" class="productdesc featurette">
			<div>
				<div class="productdesc-title"><s:message code="label.product.customer.reviews" text="Thirdproof"/></div>
			</div>
			<jsp:include page="/pages/shop/common/catalog/reviews.jsp" />
		</div><!-- end review block -->
	</div><!-- end product-main content -->

	</div><!-- end product main block -->
</div><!-- end body -->            
<script>
jQuery(function($) {
  $(document).ready( function() {
	  $('.thumbImg').click(function (e) {
			img = $(this).find('img').clone();
			$(img).css("width","100%");
			$('#mainImg').html(img);			
		});
  });
});			


function writeBread(bread){
	var tbBody='';
	if(bread != null){
		$.each(bread, function(i, p) {
			tbBody+=' <li class="active" ><a href="'+p.url+'">'+p.label+'</a></li>';
		});
	}
	
	$("#breadcrumb").html(tbBody);
}	
function doplus(id){
	var qu =$("#qty-productId-"+id).val();
	qu++;
	$("#qty-productId-"+id).val(qu);
}
function minus(id){
	var qu =$("#qty-productId-"+id).val();
	if(qu !='0'){
		qu--;
	}
	$("#qty-productId-"+id).val(qu);
}
			
</script>
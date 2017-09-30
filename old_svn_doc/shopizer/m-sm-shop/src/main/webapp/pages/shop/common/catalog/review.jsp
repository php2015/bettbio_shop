<%
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<script src="<c:url value="/resources/js/jquery.raty.min.js" />"></script>

<div class="container-fluid" style="margin:5px 10px;border:1px solid #aaa;padding:10px 10px;">
	<!-- begin review header -->
	<div class="row">
		<div><span style="font-weight:bold;font-size:20px;color:#666;line-height:1.5em;padding-left:12px;"><s:message code="label.product.rate"/></span></div>
	</div>
	<div class="row" style="margin:0px 20px;padding:10px; ">
		<div class="col-sm-12 alert-success" style="padding:10px; <c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>">
			<s:message code="message.productreview.created" text="You have successfully created a product review" />
		</div>
	</div><!-- end review header -->
	
	<!-- begin product information -->
	<div class="row">
		<c:if test="${product.image!=null}">
		<div class="col-sm-4"><img style="width:100%" src="<c:url value="${product.image.imageUrl}"/>"></div>
		</c:if>
		<div class="col-sm-5">
			<span style="font-weight:bold;font-size:20px;color:#666;line-height:1.5em"><a href="<c:url value='/shop/product/${product.description.friendlyUrl }.html'/>">${product.description.name}</a></span>
			<br/>
			<span style="color:#E3393C;word-break: break-all;line-height:20px">${product.description.simpleDescription}</span>
			<br/><br/>
			<c:set var="HIDEACTION" value="TRUE" scope="request" />
			<!-- product rating -->
			<jsp:include page="/pages/shop/common/catalog/rating.jsp" />
		</div>
	</div><!-- end product information -->
	
	<!-- begin review evaluate -->
	<div class="row">
		<sec:authorize access="hasRole('AUTH_CUSTOMER') and fullyAuthenticated">
			<c:choose>
				<c:when test="${customerReview!=null}">
				<div style="margin:5px 15px" id="reviews">					
						<s:message code="label.product.reviews.evaluated" text="You have evaluated this product"/>
						<div id="customerRating" style="width: 160px !important;"></div>
						<br/>
						<blockquote>
 							<p><span style="color:#000"><c:out value="${customerReview.description}" escapeXml="false" /></span></p>
 							<small><c:out value="${customerReview.customer.hidenick}" />&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${customerReview.customer.project}" />&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${customerReview.date}" />
 							</small>
	 					</blockquote>
	 				<script>
					  	$(function() {
							$('#customerRating').raty({ 
								readOnly: true, 
								half: true,
								path : '<c:url value="/resources/img/stars/"/>',
								score: <c:out value="${customerReview.rating}" />
							});
					  	});
	  			   </script>
	  			</div>
				</c:when>
			<c:otherwise>
			<div style="margin:5px 15px">	
			<c:url var="submitReview" value="/shop/customer/review/submit.html"/>
		    <form:form method="POST" commandName="review" action="${submitReview}">
		        <form:errors path="*" cssClass="alert alert-danger" element="div" />
		    	<form:hidden id="rating" path="rating"/>
		    	<form:hidden path="productId"/>
		    	<form:hidden path="orderProductId"/>
				    <label><s:message code="label.generic.youropinion" text="Your opinion" /></label>
				    <form:textarea name="" rows="6" class="form-control" path="description"/>
					<label>&nbsp;</label>
				    <span class="help-block"><s:message code="label.product.clickrate" text="Product rating (click on the stars to activate rating)" /></span>
				    <div id="rateMe" style="width: 160px !important;"></div>
							<script>
							$(function() {
								$('#rateMe').raty({ 
									readOnly: false, 
									half: true,
									path : '<c:url value="/resources/img/stars/"/>',
									score: 5,
									click: function(score, evt) {
										    $('#rating').val(score);
								    }
								});
								//初始化
								$('#rating').val('5');
							});	
							</script>
					<br/>
				    <button type="submit" class="btn btn-info"><s:message code="button.label.submit2" text="Submit"/></button>
		    </form:form>
			</div>
			</c:otherwise>
			</c:choose>
		</sec:authorize>
	</div>
	
	<div class="row">
		<sec:authorize access="!hasRole('AUTH_CUSTOMER') and !fullyAuthenticated">
			<p class="muted"><s:message code="label.product.reviews.logon.write" text="You have to be authenticated to write a review" /></p>
		</sec:authorize>
	</div>
</div>


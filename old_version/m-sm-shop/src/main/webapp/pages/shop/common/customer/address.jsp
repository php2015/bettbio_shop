
<%
    response.setCharacterEncoding( "UTF-8" );
    response.setHeader( "Cache-Control", "no-cache" );
    response.setHeader( "Pragma", "no-cache" );
    response.setDateHeader( "Expires", -1 );
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>
<%@ page session="false" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<script src="<c:url value="/resources/js/billing.js" />"></script>
<script>
$(document).ready(function() {	
	
	$("input[type='text']").on("change keyup paste", function(){
		$('.alert-error').hide();
		$('.alert-success').hide();	
		isEzybioFormValid();
	});
	});
function addadress() {
	$('#changeAddressForm input').val(""); 
	$('#submitAddress').val('<s:message code="menu.editaddress" text="Change address"/>'); 
	//$("input[type='text']").on("change keyup paste", function(){		
		
	//});
	 $('#addnew').modal('show');
	}

</script>
<div class="container-fluid">
	<div class="customer-box row">
		<div class="container-fluid" style="padding-bottom:10px;">
				<div class="pull-left" style="font-size:15px;padding-top:10px;"><s:message code="label.customer.address" text="Address"/><s:message code="label.bettbio.management" text="Management"/></div>
		<div class="pull-right">
			<a href="#" onClick="javascript:location.href='<c:url value="/shop/cart/shoppingCart.html" />';" class='btn btn-block btn-info'>
				<%--<span aria-hidden="true" class="carticon fa fa-reply fa-2x icon icon-style" style="background:#1E90FF;"></span>--%>
				去购物车
			</a>
		</div>									
			
		</div>	
		<div class="row box main-padding-lr">
			<c:if test="${not empty customer}">
				<c:forEach items="${addresss}" var="address" varStatus="addressStatus">
					<c:choose>
						 <c:when test="${addressStatus.index ==0}">
						 	<div class="col-xs-6">${address.name}</div><div class="col-xs-6">${address.telephone}</div>
						</c:when>
						<c:otherwise>
							<div class="col-xs-6" style="border-top: 1px solid #E5E7E9;padding-top:10px;">${address.name}</div>
							<div class="col-xs-6" style="border-top: 1px solid #E5E7E9;padding-top:10px;">${address.telephone}</div>
						</c:otherwise>
					</c:choose>	
					<div class="col-xs-8">${address.company}</div><div class="col-xs-4" style="word-wrap: break-word;word-break:break-all;">${address.memo}</div>
					<div class="col-xs-4">${address.zone}</div><div class="col-xs-4">${address.city}</div><div class="col-xs-4">${address.postCode}</div>
					<div class="col-xs-12" style="word-wrap: break-word;word-break:break-all;">${address.streetAdress}</div>
					<div class="col-xs-6" style="font-size:15px;" >
						<c:choose>
							 <c:when test="${customer.addressDefault==address.id}">
								<span style="font-weight: bold;">
							 	<s:message code="label.generic.default" text="Default"/><s:message code="label.shipping.shippingaddress" text="Default Address"/>
								</span>
							 </c:when>
							 <c:otherwise> 
							 	<a style="color:#FF6C00;" href="<c:url value="/shop/customer/defaultAddress.html?id=${address.id}"/>"  ><s:message code="label.generic.set" text="Set"/><s:message code="label.generic.default" text="Default"/><s:message code="label.shipping.shippingaddress" text="Default Address"/></a>
							</c:otherwise>				  										
					</c:choose>
					</div><div class="col-xs-3"  >
						<a style="color:#FF6C00;" href="javascript:void(0);" onclick="updateaddress(${address.id})"><span aria-hidden="true" class="carticon fa fa-edit fa-2x icon" ></span></a>
					</div>
					<div class="col-xs-3" >
						<a style="color:#FF6C00;" href="javascript:void(0);"  onclick="deletbillAddress(${address.id})"><span aria-hidden="true" class="carticon fa fa-trash-o fa-2x icon" ></span></a>
					</div>
				</c:forEach>
			</c:if>
			<c:if test="${ empty addresss || totaladdress<maxaddress}">
				<div class="col-xs-12" id="addresslink">
					<a href="javascript:void(0);" onclick="addadress()" class="btn  btn-info btn-block" role="button"><span class="carticon fa fa-plus icon"/></a>
				</div>
			</c:if>
		</div>
	</div>
</div>	

			<jsp:include page="/pages/shop/common/checkout/address-Modal.jsp" />	
			<div class="modal fade" id="addnew" tabindex="-1" role="dialog" data-backdrop= 'static' aria-hidden="true" >
			<div class="modal-dialog" role="document" >  
		    	<div class="modal-content">  
		     		 <div class="modal-header"><button class="close" type="button" data-dismiss="modal">×</button>
						<h3 ><img src="<c:url value="/resources/img/important-icon.png"/>" width="40"/>&nbsp;<strong><s:message code="label.customer.address" text="Address"/></strong></h3>
					</div>
					<div class="modal-body" >
						<jsp:include page="/pages/shop/common/customer/editAddress.jsp" />	
					</div>
		    	</div>
		    </div>	 
		</div>		
<!--
<!--close .container "main-content" -->
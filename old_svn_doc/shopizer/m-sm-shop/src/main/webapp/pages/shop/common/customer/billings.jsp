
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
<c:url var="editAddress" value="/shop/customer/billing.html"/>
<script>
$(document).ready(function() {	
	radiocheck(${invoice.type});
	$("input[type='text']").on("change keyup paste", function(){
		$('.alert-error').hide();
		$('.alert-success').hide();	
		isInvoiceFormValid();
	});
	});

function addinvoice() {
	 $('#addnewinvoice').modal('show');
	 
		$('#submitInvoice').addClass('btn-disabled');
		$('#submitInvoice').prop('disabled', true);
	}

</script>
<div class="container-fluid">
	<div class="customer-box row">
		<div class="container-fluid" style="padding-bottom:10px;">
				<div class="pull-left" style="font-size:15px;padding-top:10px;"><s:message code="label.customer.address" text="Address"/><s:message code="label.bettbio.management" text="Management"/></div>
		<div class="pull-right"><a href="#" onClick="javascript:location.href='<c:url value="/shop/customer/dashboard.html" />';"><span aria-hidden="true" class="carticon fa fa-reply fa-2x icon icon-style" style="background:#1E90FF;"></span></a></div>									
			
		</div>	
		<div class="row box main-padding-lr">
			<c:if test="${not empty customer}">
				<c:forEach items="${invoices}" var="invoice" varStatus="invoiceStatus">
					<c:choose>
						 <c:when test="${invoiceStatus.index ==0}">
						 	<div class="col-xs-6">${invoice.company}</div><div class="col-xs-6">${invoice.companyTelephone}</div>
						</c:when>
						<c:otherwise>
							<div class="col-xs-6" style="border-top: 1px solid #E5E7E9;padding-top:10px;">${invoice.company}</div>
							<div class="col-xs-6" style="border-top: 1px solid #E5E7E9;padding-top:10px;">${invoice.companyTelephone}</div>							
						</c:otherwise>
					</c:choose>	
					<div class="col-xs-12" style="word-wrap: break-word;word-break:break-all;">${invoice.companyAddress}</div>
					<div class="col-xs-8">${invoice.bankName}</div><div class="col-xs-4">${invoice.memo}</div>
					
					<div class="col-xs-6" >${invoice.bankAccount}</div><div class="col-xs-6" >${invoice.taxpayerNumber}</div>
					<div class="row" >
					<div class="col-xs-6" style="color:#FF6C00;font-size:15px;" >
						<c:choose>
							 <c:when test="${customer.invoiceDefault==invoice.id}">
							 	<s:message code="label.generic.default" text="Default"/><s:message code="label.customer.billinginformation" text="Default Billing"/>
							 </c:when>
							 <c:otherwise> 
							 	<a style="color:#FF6C00;" href="<c:url value="/shop/customer/defaultInvoiceAddress.html?id=${invoice.id}"/>"  ><s:message code="label.generic.set" text="Set"/><s:message code="label.generic.default" text="Default"/><s:message code="label.customer.billinginformation" text="Default Billing"/></a>
							</c:otherwise>				  										
					</c:choose>
					</div><div class="col-xs-3"  >
						<a style="color:#FF6C00;" href="javascript:void(0);" onclick="updateinvoice(${invoice.id})"><span aria-hidden="true" class="carticon fa fa-edit fa-2x icon" ></span></a>
					</div>
					<div class="col-xs-3" >
						<a style="color:#FF6C00;" href="javascript:void(0);"  onclick="deletbillInvoice(${invoice.id})"><span aria-hidden="true" class="carticon fa fa-trash-o fa-2x icon" ></span></a>
					</div>
					</div>
				</c:forEach>
			</c:if>
			<c:if test="${ empty addresss || totaladdress<maxaddress}">
				<div class="col-xs-12" id="addresslink">
					<a href="javascript:void(0);" onclick="addinvoice()" class="btn  btn-info btn-block" role="button"><span class="carticon fa fa-plus icon"/></a>
				</div>
			</c:if>
		</div>
	</div>
</div>

			<jsp:include page="/pages/shop/common/checkout/address-Modal.jsp" />	
			<div class="modal fade" id="addnewinvoice" tabindex="-1" role="dialog" data-backdrop= 'static'  aria-hidden="true">
		<div class="modal-dialog" role="document" >  
    	<div class="modal-content">  
     		 <div class="modal-header">  <button class="close" type="button" data-dismiss="modal">×</button>
				<h3 id="cartModalLabel"><img src="<c:url value="/resources/img/important-icon.png"/>" width="40"/>&nbsp;<strong ><s:message code="label.customer.billinginformation" text="Address"/></strong></h3>
			</div>
			<div class="modal-body" >
				<jsp:include page="/pages/shop/common/customer/editInvoice.jsp" />
    		</div> 
		</div>
	</div>
	</div>	
<!--
<!--close .container "main-content" -->
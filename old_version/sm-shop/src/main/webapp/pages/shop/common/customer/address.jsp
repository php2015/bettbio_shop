
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
<%@ page session="false"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<script src="<c:url value="/resources/js/billing.js" />"></script>
<c:url var="editAddress" value="/shop/customer/billing.html" />
<script>
$(document).ready(function() {	
	radiocheck(${invoice.type});
	$("input[type='text']").on("change keyup paste", function(){
		$('.alert-error').hide();
		$('.alert-success').hide();	
		isInvoiceFormValid();
	});
	/**
    }*/
	});
function addadress() {
	$('#changeAddressForm input').val(""); 
	$('#submitAddress').val('<s:message code="menu.editaddress" text="Change address"/>'); 
	 $('#addnew').modal('show');
	}
function addinvoice() {	
	updateinvoice(-1);
}
function addresslist(data){	
	var table=cleartbody("modaltable");
	var tbody = document.createElement("tbody");
	if(data.othersAddresss.length>0){
		 $('#modaltable').show();
		$('#addressthead').show();
		var invoicetatal=${maxaddress}-data.othersAddresss.length;
		$('#addresstotal').text(invoicetatal);
		if(invoicetatal<=0){
			$('#addresslink').text('');
		}else{
			$('#addresslink').html('<a href="javascript:void(0);" onclick="addadress()" class="btn btn-info pull-right" role="button"><span class="carticon fa fa-plus icon"/><s:message code="label.generic.add" text="Add"/></a>');
		}
	}	
	
	for (var i = 0; i < data.othersAddresss.length; i++) {			
		var tr = tbody.insertRow(i) ;
		tr = setaddresscell(tr,data.othersAddresss[i]);
		tr.deleteCell(0);
		tr.insertCell(0).innerHTML='<a href="javascript:void(0);" onclick="updateaddress('+data.othersAddresss[i].id+')">'+data.othersAddresss[i].name+'</a>';
		
		tr.insertCell(8).innerHTML='<a href="javascript:void(0);" onclick="deleteObj(' + data.othersAddresss[i].id + ',0);"> <s:message code="label.generic.remove" text="Remove"/></a>';
		
		if(data.defaultAddress !=null && data.defaultAddress.id==data.othersAddresss[i].id){
			tr.insertCell(9).innerHTML='<s:message code="label.generic.default" text="Default"/><s:message code="label.customer.address" text="Address"/>';
		}else{
			tr.insertCell(9).innerHTML='<a href="javascript:void(0);" onclick="setinvoice(' + data.othersAddresss[i].id + ',1);"><s:message code="label.generic.set" text="Set"/><s:message code="label.generic.default" text="Default"/><s:message code="label.customer.address" text="Address"/></a>';
		}
		if(data.defaultAddresInvice != null &&data.defaultAddresInvice ==data.othersAddresss[i].id){
			tr.insertCell(10).innerHTML='<s:message code="label.customer.invoice.default" text="Default Invoice"/>'
		}else{
			tr.insertCell(10).innerHTML='<a href="javascript:void(0);" onclick="setinvoice(' + data.othersAddresss[i].id + ',2);"><s:message code="label.generic.set" text="Set"/><s:message code="label.customer.invoice.default" text="Default Invoice"/></a>';
		}		
	}
	if(tbody !=null){			
		table.appendChild(tbody); 
	}
	hidemodal();
	$('#addnew').modal('hide');
}
function invoiceslist(data){
	var table=cleartbody("invoicestable");
	var tbody = document.createElement("tbody");	
	if(data.othersInvoices.length>0){
		 $('#invoicestable').show();
		$('#invoicethead').show();
		var invoicetatal=${maxinvoice}-data.othersInvoices.length;
		$('#invoicetotal').text(invoicetatal);
		if(invoicetatal<=0){
			$('#invoicelink').text('');
		}else{
			$('#invoicelink').html('<a href="javascript:void(0);" onclick="addinvoice()" class="btn btn-info pull-right" role="button"><span class="carticon fa fa-plus icon"/><s:message code="label.generic.add" text="Add"/></a>');
		}
	}
	for (var i = 0; i < data.othersInvoices.length; i++) {			
		var tr = tbody.insertRow(i) ;
		tr = setinvoicecell(tr,data.othersInvoices[i]);
		tr.deleteCell(0);
		tr.insertCell(0).innerHTML='<a href="javascript:void(0);" onclick="updateinvoice('+data.othersInvoices[i].id+')">'+data.othersInvoices[i].company+'</a>';		
		tr.insertCell(7).innerHTML='<a href="javascript:void(0);" onclick="deleteObj(' + data.othersInvoices[i].id +',1);"> <s:message code="label.generic.remove" text="Remove"/></a>';
		if(data.defaultInvoice != null && data.defaultInvoice.id == data.othersInvoices[i].id){
			tr.insertCell(8).innerHTML = '<s:message code="label.customer.invoiceaddress.default" text="Default"/>';
		}else{
			tr.insertCell(8).innerHTML = '<a href="javascript:void(0);" onclick="delinvoice(' + data.othersInvoices[i].id +',1);"><s:message code="label.generic.set" text="Set"/><s:message code="label.customer.invoiceaddress.default" text="Default"/></a>';
		}
	}
	if(tbody !=null){			
		table.appendChild(tbody); 
	}
	hidemodal();
	$('#addnewinvoice').modal('hide');
}

</script>


<c:if test="${not empty customer}">

	<div class="customer-box">
		<table class="table table-bordered table-striped table-hover"
			id="modaltable">
			<caption class="table-out-title address-title"
				style="padding-right:10px;">
				<s:message code="label.customer.address"
					text="Billing Address" />
				&nbsp;
				<s:message code="message.customer.addressinvoice"
					text="Billing Address" />
				<span id="addresstotal">${maxaddress-totaladdress}</span>
				<s:message code="message.customer.address" text="Billing Address" />
				&nbsp;
				
				<!-- 添加地址 -->
				<c:if test="${ empty addresss || totaladdress<maxaddress}">
					<span id="addresslink"><a href="javascript:void(0);"
						onclick="addadress()" class="btn pull-right button-color"
						role="button"><span class="carticon fa fa-plus icon" />
						<s:message code="label.generic.add" text="Add" /></a></span>
				</c:if>
			</caption>
			<c:choose>
				<c:when test="${not empty addresss}">
					<thead id="addressthead">
				</c:when>
				<c:otherwise>
					<thead id="addressthead" style="display:none;">
				</c:otherwise>
			</c:choose>
			<tr>
				<th><s:message code="label.customer.shipping.firstname"
						text="Name" /></th>
				<th><s:message code="label.customer.telephone" text="Phone" /></th>
				<th><s:message code="label.customer.companyname" text="Company" /></th>
				<th><s:message code="label.customer.zone"
						text="State / Province" /></th>
				<th><s:message code="label.customer.city" text="City" /></th>
				<th><s:message code="label.customer.streetaddress"
						text="Street Address" /></th>
				<th><s:message code="label.generic.postalcode"
						text="Postal code" /></th>
				<th><s:message code="label.product.proof.title" text="Memo" /></th>
				<th><s:message code="label.generic.remove" text="Remove" /></th>
				<th><s:message code="label.generic.default" text="Default" />
					<s:message code="label.customer.address" text="Address" /></th>
				<th><s:message code="label.customer.invoice.default"
						text="Default Invoice" /></th>
			</tr>
			</thead>
			<c:if test="${not empty addresss}">
				<!-- /HISTORY TABLE -->
				<tbody>
					<c:forEach items="${addresss}" var="address"
						varStatus="addressStatus">
						<tr>
							<!-- item -->
							<td><a href="javascript:void(0);"
								onclick="updateaddress(${address.id})">${address.name}</a></td>
							<td>${address.telephone}</td>
							<td>${address.company}</td>
							<td>${address.zone}</td>
							<td>${address.city}</td>
							<td>${address.streetAdress}</td>
							<td>${address.postCode}</td>
							<td>${address.memo}</td>
							<td><a id="fulsh_table" href="javascript:void(0);"
								onclick="deleteObj(${address.id},0)"> <s:message
										code="label.generic.remove" text="Remove" /></a></td>
							<td><c:choose>
									<c:when test="${customer.addressDefault == address.id}">
										<s:message code="label.generic.default" text="Default" />
										<s:message code="label.customer.address" text="Address" />
									</c:when>
									<c:otherwise>
										<a href="javascript:void(0);"
											onclick="setinvoice(${address.id},1)"><s:message
												code="label.generic.set" text="Set" />
											<s:message code="label.generic.default" text="Default" />
											<s:message code="label.customer.address" text="Address" /></a>
									</c:otherwise>
								</c:choose></td>
							<td><c:choose>
									<c:when test="${customer.invoiceaddressdefault==address.id}">
										<s:message code="label.customer.invoice.default"
											text="Default Invoice" />
									</c:when>
									<c:otherwise>
										<a href="javascript:void(0);"
											onclick="setinvoice(${address.id},2)"><s:message
												code="label.generic.set" text="Set" />
											<s:message code="label.customer.invoice.default"
												text="Default Invoice" /></a>
									</c:otherwise>
								</c:choose></td>
						</tr>
					</c:forEach>
				</tbody>
			</c:if>
		</table>
		<hr style="border-top:solid 1px #0080ff;">
		<table class="table table-bordered table-striped table-hover"
			id="invoicestable">
			<caption class="table-out-title address-title"
				style="padding-right:10px;">
				<s:message code="label.customer.billinginformation"
					text="Shipping Address" />
				&nbsp;
				<s:message code="message.customer.addressinvoice"
					text="Billing Address" />
				<span id="invoicetotal">${maxinvoice-totalinvoice}</span>
				<s:message code="message.customer.invoce" text="Billing Address" />
				&nbsp;
				<c:if test="${ empty invoices || totalinvoice<maxinvoice}">
					<span id="invoicelink"><a href="javascript:void(0);"
						onclick="addinvoice()" class="btn button-color pull-right"
						role="button"><span class="carticon fa fa-plus icon" />
						<s:message code="label.generic.add" text="Add" /></a></span>
				</c:if>
			</caption>
			<c:choose>
				<c:when test="${not empty invoices}">
					<thead id="invoicethead">
				</c:when>
				<c:otherwise>
					<thead id="invoicethead" style="display:none;">
				</c:otherwise>
			</c:choose>
			<tr>
				<th><s:message code="label.customer.invoice.company"
						text="Company" /></th>
				<th><s:message code="label.customer.billingregister"
						text="Address" /></th>
				<th><s:message code="label.customer.billingphone" text="Phone" /></th>
				<th><s:message code="label.customer.billingbank" text="Bank" /></th>
				<th><s:message code="label.customer.billingaccount"
						text="Account" /></th>
				<th><s:message code="label.customer.billingtaxnumber"
						text="Tax Number" /></th>
				<th><s:message code="label.product.proof.title" text="Memo" /></th>
				<th><s:message code="label.generic.operate" text="Operate" /></th>
				<th><s:message code="label.customer.invoiceaddress.default"
						text="Defult" /></th>
			</tr>
			</thead>
			<c:if test="${not empty invoices}">
				<!-- /HISTORY TABLE -->
				<tbody>
					<c:forEach items="${invoices}" var="invoice"
						varStatus="invoiceStatus">
						<tr>
							<!-- item -->
							<td><a href="javascript:void(0);"
								onclick="updateinvoice(${invoice.id})">${invoice.company}</a></td>
							<td>${invoice.companyAddress}</td>
							<td>${invoice.companyTelephone}</td>
							<td>${invoice.bankName}</td>
							<td>${invoice.bankAccount}</td>
							<td>${invoice.taxpayerNumber}</td>
							<td>${invoice.memo}</td>
							<td><a href="javascript:void(0);"
								onclick="deleteObj(${invoice.id},1);"><s:message
										code="label.generic.remove" text="Remove" /></a></td>
							<td><c:choose>
									<c:when test="${customer.invoiceDefault==invoice.id}">
										<s:message code="label.customer.invoiceaddress.default"
											text="Default" />
									</c:when>
									<c:otherwise>
										<a href="javascript:void(0);"
											onclick="delinvoice(${invoice.id},1);"><s:message
												code="label.generic.set" text="Set" />
											<s:message code="label.customer.invoiceaddress.default"
												text="Default" /></a>
									</c:otherwise>
								</c:choose></td>
						</tr>
					</c:forEach>
				</tbody>
			</c:if>
		</table>
	</div>
	<br />
</c:if>
<jsp:include page="/pages/shop/common/checkout/address-Modal.jsp" />
<!--
<!--close .container "main-content" -->
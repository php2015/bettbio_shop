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
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %> 
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<script src="<c:url value="/resources/js/jquery.maskedinput.min.js" />"></script>
<script src="<c:url value="/resources/js/checkout.js" />"></script>
<jsp:include page="/pages/shop/templates/bootstrap3/sections/shopLinks.jsp" />
<!-- subtotals template -->
<script type="text/html" id="subTotalsTemplate">
		{{#subTotals}}
			<tr class="subt"> 
				<td colspan="3">{{title}}</td> 
				<td><strong>{{total}}</strong></td> 
			</tr>
		{{/subTotals}}
</script>

<!-- total template -->
<script type="text/html" id="totalTemplate">
		<span class="total-box-grand-total">
			<font class="total-box-label">
			  <s:message code="order.total.total" text="Total"/>
			  <font class="total-box-price">{{grandTotal}}</font>
			</font>
		</span>
</script>

<script>

<!-- checkout form id -->
var checkoutFormId = '#checkoutForm';
var formErrorMessageId = '#formErrorMessage';

function ischeckoutFormValid() {
	$(formErrorMessageId).hide();//reset error message
	
	var valid = true;
	var firstErrorMessage = null;
	if($("#invoicesendid").val() == "-1"){
		valid = false;
		firstErrorMessage='<s:message code="message.order.invoiceaddress" text="The order can be completed"/>';
	}
	if($("#invoiceid").val() == "-1"){
		valid = false;
		firstErrorMessage='<s:message code="message.order.invoiceinfo" text="The order can be completed"/>';
	}
	if($("#addressid").val() == "-1"){
		valid = false;
		firstErrorMessage='<s:message code="message.order.billingaddress" text="The order can be completed"/>';
	}
	
	if(valid==false) {//disable submit button
		if(firstErrorMessage!=null) {
			
			$(formErrorMessageId).html('<img src="<c:url value="/resources/img/icon_error.png"/>" width="20"/>&nbsp;<strong><font color="red">' + firstErrorMessage + '</font></strong>');
			$(formErrorMessageId).show();
		}
		$('#submitOrder').addClass('btn-disabled');
		$('#submitOrder').prop('disabled', true);
	} else {
		
		$(formErrorMessageId).html('<img src="<c:url value="/resources/img/icon_success.png"/>" width="20"/>&nbsp;<strong ><s:message code="message.order.canprocess" text="The order can be completed"/></strong></span>');
		$(formErrorMessageId).show();
		$('#submitOrder').removeClass('btn-disabled');
		$('#submitOrder').prop('disabled', false);
	}
}

function isFieldValid(field) {
	var validateField = true;
	var fieldId = field.prop('id');
	var value = field.val();
	if (fieldId.indexOf("creditcard") >= 0) {
		validateField = false;	//ignore credit card number field
	}
	if(!field.is(':visible')) {
		validateField = false; //ignore invisible fields
	}
	
	if(!validateField) {
		return true;
	}
	if(!emptyString(value)) {
		field.css('background-color', '#FFF');
		return true;
	} else {
		field.css('background-color', '#FFC');
		return false;
	}
}

function addadress() {
	 $('#addnew').modal('show');
	}
function addinvoice() {
	isInvoiceFormValid();
	radiocheck(0);
	 $('#addnewinvoice').modal('show');	
	 
	}
function showResponseErrorMessage(message) {
	
	
	$('#checkoutError').addClass('alert-danger');
	$('#checkoutError').html(message);
	
}

function resetErrorMessage() {
	
	$('#checkoutError').html('');
	
	$('#checkoutError').removeClass('alert-danger');
	$('.error').html('');
	
}

function bindActions() {
	$("#submitOrder").click(function(e) {
		e.preventDefault();//do not submit form
		resetErrorMessage();
		$('#pageContainer').showLoading();
		
			$('#pageContainer').hideLoading();
			$('#checkoutForm').submit();
		
    });
}

</script>
<div class="container-fluid" >
	<div class="box " >
		<div style="padding-bottom: 10px;">
			<span ><s:message code="label.checkout" text="Checkout" /></span>
			<sec:authorize access="!hasRole('AUTH_CUSTOMER') and !fullyAuthenticated">
						<p class="muted common-row"><s:message code="label.checkout.logon" text="Logon or signup to simplify the online purchase process!"/></p>
			</sec:authorize>
		</div>
	   <c:set var="commitUrl" value="${pageContext.request.contextPath}/shop/order/commitOrderV2.html"/>
   <form:form id="checkoutForm" method="POST" enctype="multipart/form-data" commandName="order" action="${commitUrl}">

		
				<c:if test="${errorMessages!=null}">
					<div id="checkoutError" style="padding:10px;" class="<c:if test="${errorMessages!=null}">alert-danger </c:if>">
					</div>
				<c:out value="${errorMessages}" />
				</c:if>
			<div class="row">
					<!-- Shipping box -->
					<div class="col-xs-12" style="padding-top: 3px;padding-bottom: 3px;height:auto;border-top:solid 1px #0080ff;"  >
										<span><s:message code="label.customer.shippinginformation" text="Shipping information"/></span>
					</div>
					<c:if test="${defautAddress!=null  }">
						<input type="hidden" name="addressid" id="addressid" value="${defautAddress.id}"/>
						<input type="hidden" name="invoicesendid" id="invoicesendid" value="${defautAddress.id}"/>
					</c:if>		
					<c:if test="${defautAddress==null  }">
						<input type="hidden" name="addressid" id="addressid" value="-1"/>
						<input type="hidden" name="invoicesendid" id="invoicesendid" value="-1"/>
					</c:if>	
					<div id="selectaddress">
						<c:if test="${defautAddress!=null  }">
							<div class="col-xs-8">${defautAddress.name}</div>
							<div class="col-xs-4">${defautAddress.telephone}</div>
							<div class="col-xs-8">${defautAddress.company}</div>
							<div class="col-xs-4" style="word-wrap: break-word;word-break:break-all;">${defautAddress.memo}</div>
							<div class="col-xs-4">${defautAddress.zone}</div><div class="col-xs-4">${defautAddress.city}</div><div class="col-xs-4">${defautAddress.postCode}</div>
							<div class="col-xs-12" style="word-wrap: break-word;word-break:break-all;">${defautAddress.streetAdress}</div>
						</c:if>		
					</div>
					<div class="col-xs-8"></div>		
					<div class="col-xs-2"  >
						<a style="display: none; color:#FF6C00;" href="javascript:void(0);" onclick="showModal()"><span aria-hidden="true" class="carticon fa fa-edit fa-2x icon" ></span></a>
					</div>
					<div class="col-xs-2" >
						<a style="color:#FF6C00;" href="javascript:void(0);"  onclick="addadress()"><span aria-hidden="true" class="carticon fa fa-plus fa-2x icon" ></span></a>
					</div>
				
					<!-- Billing box -->			
					<div class="col-xs-12" style="padding-top: 3px;padding-bottom: 3px;height:auto;border-top:solid 1px #0080ff;" >
										<span ><s:message code="label.customer.billinginformation" text="Billing information"/></span></div>	
					<c:if test="${defautInvoice!=null  }">
						<input type="hidden" name="invoiceid" id="invoiceid" value="${defautInvoice.id}"/>
					</c:if>				
					<c:if test="${defautInvoice==null  }">
						<input type="hidden" name="invoiceid" id="invoiceid" value="-1"/>												
					</c:if>	
					<div id="selectinvoice">	
						<c:if test="${defautInvoice!=null  }">
							<div class="col-xs-8">${defautInvoice.company}</div>
							<div class="col-xs-4">${defautInvoice.companyTelephone}</div>
							<div class="col-xs-12" style="word-wrap: break-word;word-break:break-all;">${defautInvoice.companyAddress}</div>
							<div class="col-xs-8">${defautInvoice.bankName}</div>
							<div class="col-xs-4">${defautInvoice.memo}</div>					
							<div class="col-xs-6" >${defautInvoice.bankAccount}</div>
							<div class="col-xs-6" >${defautInvoice.taxpayerNumber}</div>
						</c:if>				
					</div>
					<div class="col-xs-8"> <input type="checkbox" id="invoicesend" name="checkbox" checked="checked" ><s:message code="label.customer.billinginsendformation" text="Same with delivery"/></input></div>		
					<div class="col-xs-2"  >
						<a style="display:none; color:#FF6C00;" href="javascript:void(0);" onclick="showInvoice()"><span aria-hidden="true" class="carticon fa fa-edit fa-2x icon" ></span></a>
					</div>
					<div class="col-xs-2" >
						<a style="color:#FF6C00;" href="javascript:void(0);"  onclick="addinvoice()"><span aria-hidden="true" class="carticon fa fa-plus fa-2x icon" ></span></a>
					</div>
					<!-- delivery box -->
					<div id="sendbox" style="display:none;">
						<div class="col-xs-12" style="padding-top: 3px;padding-bottom: 3px;height:auto;border-top:solid 1px #0080ff;"  >
											<span><s:message code="label.customer.invoiceaddress" text="Shipping information"/></span></div>												
						<div id="sendaddresstr"></div>						
					</div>	
					<!-- payment box -->					
						<div class="col-xs-6" style="padding-top: 3px;padding-bottom: 3px;height:auto;border-top:solid 1px #0080ff;"><s:message code="label.payment.module.title" text="Payment method" /></div>
									
						<div class="col-xs-6" style="padding-top: 3px;padding-bottom: 3px;height:auto;border-top:solid 1px #0080ff;"><s:message code="label.pay.month" text="Payment method" /></div>	
					<!-- detail -->
					<div class="col-xs-12" style="padding-top: 3px;padding-bottom: 3px;height:auto;border-top:solid 1px #0080ff;" >
										<span ><s:message code="label.order.summary" text="Order summary" /></span></div>
					<c:forEach items="${cart.subOrders}" var="subOrders" varStatus="itemStatus">
						<div class="col-xs-12" >
							<s:message code="menu.store" text="Store"/>:${subOrders.merchantStore.storename}
						</div>						
						<c:forEach items="${subOrders.shoppingCartItems}" var="shoppingCartItem" varStatus="itemStatus">													
							<div class="col-xs-6" style="word-break: break-all; word-wrap:break-word;">
								${shoppingCartItem.productCode}<br/>
								${shoppingCartItem.name}
								<c:if test="${not empty shoppingCartItem.enName }">
												<br/><span>${shoppingCartItem.enName}</span>
											</c:if>
							</div>
							<div class="col-xs-2" >
								${shoppingCartItem.specs}
							</div>
							<div class="col-xs-1" >
								x${shoppingCartItem.quantity}
							</div>
							<div class="col-xs-3" >
								${shoppingCartItem.price}
							</div>												
						</c:forEach>
					</c:forEach>	
					<c:forEach items="${cart.totals}" var="total">
							<div id="totalRow" class="col-xs-12" style="text-align:right;">
									<s:message code="${total.code}" text="label [${total.code}] not found"/>
									<font class="total-box-price"><sm:monetary value="${total.value}" /></font>
								
							</div>			
					</c:forEach>					
						<div  class="col-xs-6" >
							<div id="formErrorMessage"  style="padding-top:20px;"></div>
						</div>
					<!-- Submit -->
						<div class="form-actions col-xs-6" style="text-align:right;">
							
								<button id="submitOrder" type="button" class="btn btn-success 
								<c:if test="${errorMessages!=null}"> btn-disabled</c:if>" 
								<c:if test="${errorMessages!=null}"> disabled="true"</c:if>
								><s:message code="button.label.submitorder" text="Submit order"/></button>
							
						</div> 	
				</div>				
	</form:form>
	</div>
</div>	
	<jsp:include page="/pages/shop/common/checkout/checkoutModal.jsp" />	
	<jsp:include page="/pages/shop/common/checkout/address-Modal.jsp" />
	<!-- Modal -->
	
	<script type="text/javascript">
		$(document).ready(function(){
			bindActions();
				$('#sendbox').hide();
				 $("#invoicesend").click(function() {
					 if ($('#invoicesend').is(':checked')) {
					    	$('#sendbox').hide();
					    	$("#invoicesendid").val($("#addressid").val());
					    	ischeckoutFormValid();
					    	
					    } else{
					    	showModaladdress();					    
					    	ischeckoutFormValid();
					    }
				    });
			;
			 $('#checkoutModal').on('hide.bs.modal', function () {
				  if($("#addressid").val()==$("#invoicesendid").val()){
			    	  $("#invoicesend").prop("checked", true); 
			      } else {
			    	  $("#invoicesend").prop("checked", false); 
			      }    
			 });
			})
	
	
	
</script>

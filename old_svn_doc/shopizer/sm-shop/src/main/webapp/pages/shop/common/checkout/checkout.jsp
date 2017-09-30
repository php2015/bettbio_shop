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
	//console.log('Form is valid ? ' + valid);
	if(valid==false) {//disable submit button
		if(firstErrorMessage!=null) {
			$(formErrorMessageId).addClass('alert-danger');
			$(formErrorMessageId).removeClass('alert-success');
			$(formErrorMessageId).html('<img src="<c:url value="/resources/img/icon_error.png"/>" width="40"/>&nbsp;<strong><font color="red">' + firstErrorMessage + '</font></strong>');
			$(formErrorMessageId).show();
		}
		$('#submitOrder').addClass('btn-disabled');
		$('#submitOrder').prop('disabled', true);
	} else {
		$(formErrorMessageId).removeClass('alert-danger');
		$(formErrorMessageId).addClass('alert-success');
		$(formErrorMessageId).html('<img src="<c:url value="/resources/img/icon_success.png"/>" width="40"/>&nbsp;<span style="height: 3em;line-height: 3em;"><strong ><s:message code="message.order.canprocess" text="The order can be completed"/></strong></span>');
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
			if(confirm('您真的要离开了吗?')){
			    $('#checkoutForm').submit();
			}
			
		
    });
}

</script>

	<div class="box main-padding-lr row" >
		<h2 class="cart-title"><s:message code="label.checkout" text="Checkout" /></h2>
		<sec:authorize access="!hasRole('AUTH_CUSTOMER') and !fullyAuthenticated">
					<p class="muted common-row"><s:message code="label.checkout.logon" text="Logon or signup to simplify the online purchase process!"/></p>
		</sec:authorize>
	
	   <c:set var="commitUrl" value="${pageContext.request.contextPath}/shop/order/commitOrder.html"/>
   <form:form id="checkoutForm" method="POST" enctype="multipart/form-data" commandName="order" action="${commitUrl}">

		<div  id="checkout">
				


					<!-- If error messages -->
					<div id="checkoutError" style="padding:10px;margin:5px;" class="<c:if test="${errorMessages!=null}">alert-danger </c:if>">
						<c:if test="${errorMessages!=null}">
						<c:out value="${errorMessages}" />
						</c:if>
					</div>
					<!--alert-error-->

					<!-- row fluid span -->
					
					<!-- left column -->
					<div class="col-sm-7">
								<!-- Shipping box -->
								<div id="deliveryBox" class="box">
									<table class="table table-condensed table-hover table-font" id="defaultaddresstable">
									<caption class="table-out-title row"><div class="col-sm-2" style="padding-top: 3px;padding-bottom: 3px;height:auto;" >
										<span >收货信息</span></div>
										<div class="col-sm-10" style="text-align:right;" >
													 <a href="javascript:void(0);" onclick="showModal()" id="getalladdreslink" class="btn  btn-info " role="button"><span class="glyphicon glyphicon-th" aria-hidden="true"> <s:message code="label.generic.all" text="ALL"/></a>
											   <a href="javascript:void(0);" onclick="addadress()" class="btn  btn-info " role="button"><span class="glyphicon glyphicon-plus" aria-hidden="true"> <s:message code="label.generic.add" text="Add"/></a></div>
									</caption>
									<c:if test="${defautAddress!=null  }">
									<input type="hidden" name="addressid" id="addressid" value="${defautAddress.id}"/>
									<input type="hidden" name="invoicesendid" id="invoicesendid" value="${defautAddress.id}"/>
											
											<thead>
												<tr>
													<th><s:message code="label.customer.shipping.firstname" text="Name"/></th>
													<th><s:message code="label.customer.companyname" text="Company"/></th>
													<th><s:message code="label.customer.zone" text="State / Province"/></th>
													<th><s:message code="label.customer.city" text="City"/></th>
													<th><s:message code="label.customer.streetaddress" text="Street Address"/></th>
													<th><s:message code="label.generic.postalcode" text="Postal code"/></th>
													<th><s:message code="label.customer.telephone" text="Phone"/></th>
													<th><s:message code="label.product.proof.title" text="Memo"/></th>
												</tr>
											</thead>
									        		<tr id="selectaddress">
														<td>${defautAddress.name}</td>								
														<td>${defautAddress.company}</td>										
														<td >${defautAddress.zone}</td>
														<td>${defautAddress.city}</td>
														<td>${defautAddress.streetAdress}</td>
														<td>${defautAddress.postCode}</td>	
														<td>${defautAddress.telephone}</td>
														<td>${defautAddress.memo}</td>
													</tr>											
									</c:if>
									</table>
									<c:if test="${defautAddress==null  }">
										<input type="hidden" name="addressid" id="addressid" value="-1"/>
									<input type="hidden" name="invoicesendid" id="invoicesendid" value="-1"/>
									</c:if>
								</div>
									<br/>	
										<!-- Billing box -->
										<div id="shippingBox" class="box">
											<table class="table table-condensed table-hover table-font" id="defaultinvoicetable">
											<caption class="table-out-title row">
												<div class="col-sm-2" style="padding-top: 3px;padding-bottom: 3px;height:auto;"><s:message code="label.customer.billinginformation" text="Billing information"/><div></div></div>
 												<div class="col-sm-3" style="text-align:left;padding-top: 0px;padding-bottom: 0px;"><label class="btn " style="font-size:10px;" >
   																 <input type="checkbox" id="invoicesend" name="checkbox" checked="checked" ><s:message code="label.customer.billinginsendformation" text="Same with delivery"/></input> </label></div>
												 <div class="col-sm-7" style="text-align:right;padding-top: 0px;padding-bottom: 0px;">
												 
												 <a href="javascript:void(0);" onclick="showInvoice()" class="btn btn-info  " role="button"><span class="glyphicon glyphicon-th" aria-hidden="true"> <s:message code="label.generic.all" text="ALL"/></a>
												  <a href="javascript:void(0);" onclick="addinvoice()" class="btn   btn-info " role="button"><span class="glyphicon glyphicon-plus" aria-hidden="true"> <s:message code="label.generic.add" text="Add"/></a></div>
											</caption>
											<c:if test="${defautInvoice!=null  }">
											<input type="hidden" name="invoiceid" id="invoiceid" value="${defautInvoice.id}"/>
														<thead>
															<tr>
																<th><s:message code="label.customer.invoice.company" text="Company"/></th>
																<th><s:message code="label.customer.billingregister" text="Address"/></th>
																<th><s:message code="label.customer.billingphone" text="Phone"/></th>
																<th><s:message code="label.customer.billingbank" text="Bank"/></th>
																<th><s:message code="label.customer.billingaccount" text="Account"/></th>
																<th><s:message code="label.customer.billingtaxnumber" text="Tax Number"/></th>
																<th><s:message code="label.product.proof.title" text="Memo"/></th>
															</tr>
														</thead>
															<tr id="selectinvoice">
																<td>${defautInvoice.company}</td>
																<td>${defautInvoice.companyAddress}</td>
																<td>${defautInvoice.companyTelephone}</td>
																<td>${defautInvoice.bankName}</td>
																<td>${defautInvoice.bankAccount}</td>	
																<td>${defautInvoice.taxpayerNumber}</td>	
																<td>${defautInvoice.memo}</td>
																<!-- td><s:message code="label.customer.invoiceaddress.default" text="Default"/></td-->										
															</tr>
														</c:if>	
													</table>
											<!-- c:if test="${defautInvoice!=null  }"-->
											<table class="table table-condensed table-hover table-font" id="sendaddress">
											<thead>
												<tr>
													<th><s:message code="label.customer.shipping.firstname" text="Name"/></th>
													<th><s:message code="label.customer.companyname" text="Company"/></th>
													<th><s:message code="label.customer.zone" text="State / Province"/></th>
													<th><s:message code="label.customer.city" text="City"/></th>
													<th><s:message code="label.customer.streetaddress" text="Street Address"/></th>
													<th><s:message code="label.generic.postalcode" text="Postal code"/></th>
													<th><s:message code="label.customer.telephone" text="Phone"/></th>
													<th><s:message code="label.product.proof.title" text="Memo"/></th>
												</tr>
											</thead>
									        		<tr id="sendaddresstr">
														<td></td>								
														<td></td>										
														<td></td>
														<td></td>
														<td></td>
														<td></td>	
														<td></td>
													</tr>
											</table>
											<!-- /c:if-->
											<c:if test="${defautInvoice==null  }">
												<input type="hidden" name="invoiceid" id="invoiceid" value="-1"/>												
											</c:if>
									</div>
									<!-- end billing box -->
									
									<br/>
									
									<!-- c:if test="${fn:length(paymentMethods)>0}"-->
									<!-- payment box -->
									<div class="box">
										<span class="table-top-title">
											<p class="p-title" style="color:000000"><s:message code="label.payment.module.title" text="Payment method" /></p>
										</span>
										<div>发票随货寄送。特殊情况下，发票在您收货后另外寄送。收到发票后，请您及时进行报销处理。（适用于科研院所客户）</div>
									</div>
								
					</div>
				
					<div class="col-sm-5">
									
										<div class="box">
																						<table id="summary-table" class="table table-condensed table-hover table-font">
											<caption class="table-out-title"><s:message code="label.order.summary" text="Order summary" />
											</caption>
												<thead> 
													<tr> 
														<th width="45%"><s:message code="label.order.item" text="Item" /></th> 
														<th width="15%"><s:message code="label.product.specification" text="Specs" /></th>
														<th width="10%"><s:message code="label.quantity" text="Quantity" /></th> 
														<th width="15%"><s:message code="label.order.price" text="Price" /></th>
														<th width="15%"><s:message code="label.order.total" text="Total" /></th>  
													</tr> 
												</thead> 
									
												<tbody id="summaryRows"> 
												<c:forEach items="${cart.subOrders}" var="subOrders" varStatus="itemStatus">
													<tr>
														<td colspan="5"><s:message code="menu.store" text="Store"/>:${subOrders.merchantStore.storename}</td>
													</tr>
													<c:forEach items="${subOrders.shoppingCartItems}" var="shoppingCartItem" varStatus="itemStatus">													
													<tr class="item"> 
														<td>
															${shoppingCartItem.name}
															<c:if test="${fn:length(shoppingCartItem.shoppingCartAttributes)>0}">
															<br/>
																<ul>
																	<c:forEach items="${shoppingCartItem.shoppingCartAttributes}" var="option">
																	<li>${option.optionName} - ${option.optionValue}</li>
																	</c:forEach>
																</ul>
															</c:if>
														</td> 
														<td>${shoppingCartItem.specs}</td>
														<td >${shoppingCartItem.quantity}</td> 
														<td><strong>${shoppingCartItem.price}</strong></td> 
														<td><strong>${shoppingCartItem.subTotal}</strong></td> 
													</tr>
													</c:forEach>
													</c:forEach>
												</tbody> 
											</table>
											<c:forEach items="${cart.totals}" var="total">
												<div id="totalRow" class="total-box">
													<span class="total-box-grand-total">
														<font class="total-box-label">
														<s:message code="${total.code}" text="label [${total.code}] not found"/>
														<font class="total-box-price"><sm:monetary value="${total.value}" /></font>
														</font>
													</span>
												</div>			
										</c:forEach>
															
										</div>
										<!--  end order summary box -->
										<br/>
										<div id="formErrorMessage" class="" style="line-height:40px;height:40px;font-size:14px;">
										</div>
										<!-- Submit -->
										<div class="form-actions">
											<div class="pull-right"> 
												<button id="submitOrder" type="button" class="btn btn-large btn-success 
												<c:if test="${errorMessages!=null}"> btn-disabled</c:if>" 
												<c:if test="${errorMessages!=null}"> disabled="true"</c:if>
												><s:message code="button.label.submitorder" text="Submit order"/></button>

											</div>
										</div> 
			
					</div>
		</div>		
	</form:form>
	</div>
	<jsp:include page="/pages/shop/common/checkout/checkoutModal.jsp" />	
	<jsp:include page="/pages/shop/common/checkout/address-Modal.jsp" />
	<!-- Modal -->
	
	<script type="text/javascript">
		$(document).ready(function(){
			bindActions();
				$('#sendaddress').hide();
				 $("#invoicesend").click(function() {
					 if ($('#invoicesend').is(':checked')) {
					    	$('#sendaddress').hide();
					    	$("#invoicesendid").val($("#addressid").val());
					    	ischeckoutFormValid();
					    	
					    } else{
					    	showModaladdress();					    
					    	ischeckoutFormValid();
					    }
				    });
				//apply mask
					setCountrySettings('customer',$('#customer_country').val());
					//populate zones combo
					getZones($('#customer_country').val(),'<c:out value="${address.zone}" />',isFormValid);
					$("#customer_country").change(function() {
							getZones($(this).val(),'<c:out value="${address.zone}" />',isFormValid);
							setCountrySettings('customer',$('#customer_country').val());
					});
					$("#customer_zones").change(function() {
						if($("#customer_zones").val()=='HU'){
							document.getElementById("customer_city").value = "上海";
						}else if($("#customer_zones").val()=='JING'){
							document.getElementById("customer_city").value = "北京";
						}else if($("#customer_zones").val()=='JIN'){
							document.getElementById("customer_city").value = "天津";
						}else if($("#customer_zones").val()=='YU'){
							document.getElementById("customer_city").value = "重庆";
						}else{
							document.getElementById("customer_city").value = "";
						}
				});
				 ischeckoutFormValid();				  
				    $("#checkoutForm input[type='text']").on("change keyup paste", function(){
						$('#formErrorMessage').hide();
						$('.alert-success').hide();	
						isInvoiceFormValid();
					});
				    radiocheck(${invoice.type});
			})
	
	 $(function () { 
		 $('#checkoutModal').on('hide.bs.modal', function () {
		  if($("#addressid").val()==$("#invoicesendid").val()){
	    	  $("#invoicesend").prop("checked", true); 
	      } else {
	    	  $("#invoicesend").prop("checked", false); 
	      }    
	 })
	   });
	
</script>

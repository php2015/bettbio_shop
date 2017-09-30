<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>
<%@ page session="false" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<!-- requires functions.jsp -->
<script src="<c:url value="/resources/js/jquery.maskedinput.min.js" />"></script>
<script type="text/javascript">
var cname='${invoice.company}';
var inittype=${invoice.type};
$(document).ready(function() {
	//triggers form validation
	//isFormValid();
	$("input[type='text']").on("change keyup paste", function(){
		$('.alert-error').hide();
		$('.alert-success').hide();
		isInvoiceFormValid();
	});
	isInvoiceFormValid();
});
	function radiocheck(radiotype)
	{
		
		if (radiotype ==0){
			 $("#special").hide();
			 $("#normal").show();
			 $("#scompanyname").val('');
			 if(inittype==radiotype){
				 $("#ncompanyname").val(cname);
			 }
		}else {
			 $("#special").show();
			 $("#normal").hide();
			 $("#ncompanyname").val('');
			 if(inittype==radiotype){
				 $("#scompanyname").val(cname);
			 }
		}
		isInvoiceFormValid();
		$('#submitInvoice').addClass('btn-disabled');
		$('#submitInvoice').prop('disabled', true);
		
	};
	
	function isInvoiceFormValid() {
		
		if($('.alert-error').is(":visible")) {
			return true;
		}
		
		if($('.alert-success').is(":visible")) {
			return true;
		}	
		
		var msg = isCustomerFormValid($('#changeInvoiceForm'));

		if(msg!=null) {//disable submit button
			$('#submitInvoice').addClass('btn-disabled');
			$('#submitInvoice').prop('disabled', true);
			$('#formError').html(msg);
			$('#formError').show();
			return false;
		} else {
			$('#submitInvoice').removeClass('btn-disabled');
			$('#submitInvoice').prop('disabled', false);
			$('#formError').hide();
			return true;
		}
	}

</script>
<c:url var="updateInvoice" value="/shop/customer/updateInvoice.html"/>
					
				
					<div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>
					<div id="formError"  class="alert alert-warning " style="display:none;"></div>
					<form:form method="POST" id="changeInvoiceForm" commandName="invoice" action="${updateInvoice}" cssClass="form-horizontal">
				         <!-- TODO REMOVE -->				         
				       <form:hidden path="id"/>
						<form:errors id="invoice.error" path="*" cssClass="alert-danger" element="div" />
							<div class="form-group">
			                        <label class="col-sm-2 control-label"><s:message code="label.customer.billingtype" text="Transaction type"/>:</label>
			                        <div class="col-sm-10">
			                        	<div class="row">
			                        		<div class="col-sm-12"><form:radiobutton id="invoiceType" path="type" value="0" onclick="javascript:radiocheck(0)"  />&nbsp;<s:message code="label.customer.billingnormal" text="Normal" />		</div>
			                        		<div class="col-sm-12"><form:radiobutton id="invoiceType" path="type" value="1" onclick="javascript:radiocheck(1)" />&nbsp;<s:message code="label.customer.billingspecial" text="Special" /></div>
			                        	</div>
			                        		<s:message code="NotEmpty.invoice.type" text="Type is required" var="msgType"/>
				                                <span class="help-inline"><form:errors path="type" cssClass="error" /></span>
			                        </div>			                        
				            </div>
							
						<div id="normal" style="display:none;">
							<div class="form-group">
			                        <label class="col-sm-2 control-label"><s:message code="label.customer.billingtitle" text="Company Title"/>:</label>
			                        <div class="col-sm-10">
			                        	<s:message code="NotEmpty.customer.billingtitle" text="Company is required" var="msgCompanyTitle"/>		              		
					              		<form:input  cssClass="required form-control "  maxlength="100" path="company" title="${msgCompanyTitle}" id="ncompanyname"/>	
					              		<span class="help-inline"><form:errors path="company" cssClass="error" /></span>
			                        </div>
				            </div>
				        </div>
				          <div id="special" style="display:none;" >
				          	<div class="form-group">
			                        <label class="col-sm-2 control-label"><s:message code="label.customer.companyname" text="Company"/>:</label>
			                        <div class="col-sm-4">
			                        	<s:message code="NotEmpty.customer.billingCompany" text="Company is required" var="msgCompanyName"/>		              		
					              		<form:input  cssClass="required form-control "  maxlength="100" path="company" title="${msgCompanyName}" id="scompanyname"/>	
					              		<span class="help-inline"><form:errors path="company" cssClass="error" /></span>
			                        </div>
			                        <label class="col-sm-2 control-label"><s:message code="label.customer.billingregister" text="Address"/>:</label>
			                        <div class="col-sm-4">			              		
			              				<s:message code="NotEmpty.customer.billingStreetAddress" text="Company is required" var="msgCompanyAddress"/>
					              		<form:input  cssClass="required form-control "  maxlength="256" path="companyAddress" title="${msgCompanyAddress}"/>
					              		<span class="help-inline"><form:errors path="companyAddress" cssClass="error" /></span>	
				            		</div>
				            </div>
				            <div class="form-group">
			                        <label class="col-sm-2 control-label"><s:message code="label.customer.billingbank" text="Bank"/>:</label>
			                        <div class="col-sm-4">
			                        	<s:message code="NotEmpty.customer.billingbank" text="Company is required" var="msgCompanyBank"/>
					              		<form:input  cssClass="required form-control "  maxlength="100" path="bankName" title="${msgCompanyBank}"/>	
					              		<span class="help-inline"><form:errors path="bankName" cssClass="error" /></span>
			                        </div>
			                        <label class="col-sm-2 control-label"><s:message code="label.customer.billingaccount" text="Account"/>:</label>
			                        <div class="col-sm-4">			              		
			              				<s:message code="NotEmpty.customer.billingbankaccount" text="Company is required" var="msgCompanyBankAccount"/>
					              		<form:input  cssClass="required form-control "  maxlength="100" path="bankAccount" title="${msgCompanyBankAccount}"/>
					              		<span class="help-inline"><form:errors path="bankAccount" cssClass="error" /></span>	
				            		</div>
				            </div>
				            <div class="form-group">
			                        <label class="col-sm-2 control-label"><s:message code="label.customer.billingphone" text="Telephone"/>:</label>
			                        <div class="col-sm-4">
			                        	<s:message code="NotEmpty.customer.billingphone" text="Company is required" var="msgCompanyPhone"/>
						              	<form:input  cssClass="required form-control "  maxlength="32" path="companyTelephone" title="${msgCompanyPhone}"/>	
						              	<span class="help-inline"><form:errors path="companyTelephone" cssClass="error" /></span>
			                        </div>
			                        <label class="col-sm-2 control-label"><s:message code="label.customer.billingtaxnumber" text="Account"/>:</label>
			                        <div class="col-sm-4">			              		
			              				<s:message code="NotEmpty.customer.billingtaxnumber" text="Company is required" var="msgCompanyNumber"/>
						              	<form:input  cssClass="required form-control "  maxlength="100" path="taxpayerNumber" title="${msgCompanyNumber}"/>
						              	<span class="help-inline"><form:errors path="taxpayerNumber" cssClass="error" /></span>	
				            		</div>
				            </div>
				            <div class="form-group">
			                        <label class="col-sm-2 control-label"><s:message code="label.product.proof.title" text="Memo"/>:</label>
			                        <div class="col-sm-4">
			                        	<div class="controls">	
						              		<form:input  cssClass="form-control"  maxlength="256" path="memo"/>
							            </div>
			                        </div>			                        
				            </div>
				       </div>
				            <div class="form-group">
				            	<div class="col-sm-3 col-sm-offset-2">
				            		<input type="checkbox" id="setdefaultinvoice"  name="setdefaultinvoice" checked="checked"><s:message code="label.generic.set" text="Set"/><s:message code="label.generic.default" text="Default"/><s:message code="label.customer.billinginformation" text="Invoice"/></input>
				           		 </div>
			                        <div class="col-sm-3 col-sm-offset-2">
			                       		 <input id="submitInvoice" class="btn btn-info btn-disabled pull-right" type="button" onClick="javascript:addnewinvoice();" name="submitInvoice" value="<s:message code="menu.editinvoice" text="Change Invoice"/>" disabled="">
			                        </div>			                        		                        
				            </div>   
				        				            
										</form:form>
						

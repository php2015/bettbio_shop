<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>
<%@ page session="false" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<!-- requires functions.jsp -->
<script src="<c:url value="/resources/js/jquery.maskedinput.min.js" />"></script>
<script src="<c:url value="/resources/js/shop-customer.js" />"></script>
<script src="<c:url value="/resources/js/address.js" />"></script>



<script type="text/javascript">

$(document).ready(function() {
	//triggers form validation
	//isFormValid();
	$("input[type='text']").on("change keyup paste", function(){
		$('.alert-error').hide();
		$('.alert-success').hide();
		isEzybioFormValid();
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
		isEzybioFormValid();
})
	
});

function isFormValid() {
	if($('.alert-error').is(":visible")) {		
		return true;
		
	}
	if($('.alert-success').is(":visible")) {		
		return true;		
	}
	
	var msg = isCustomerFormValid($('#changeAddressForm'));
	if(msg!=null) {//disable submit button
		$('#submitAddress').addClass('btn-disabled');
		$('#submitAddress').prop('disabled', true);
		$('#formError').html(msg);
		$('#formError').show();
		return false;
	} else {
		$('#submitAddress').removeClass('btn-disabled');
		$('#submitAddress').prop('disabled', false);
		$('#formError').hide();
		return true;
	}
}

</script>

<c:url var="updateAddress" value="/shop/customer/updateAddress.html"/>

					<div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>
					<div id="formError"  class="alert alert-error alert-warning" style="display:none;"></div>
					<form:form method="POST" id="ezybioForm" commandName="address" action="${updateAddress}" cssClass="form-horizontal">
				         <!-- TODO REMOVE -->				         
						<form:errors id="address.error" path="*" cssClass="alert-danger" element="div" />
						<form:hidden path="id"/>
						    <div class="form-group">
			                        <label class="col-sm-2 control-label"><s:message code="label.customer.shipping.firstname" text="First Name"/>:</label>
			                        <div class="col-sm-4">
			                        		<s:message code="NotEmpty.customer.firstName" text="客户名不能为空" var="msgFirstName"/>
			                        		<form:input  cssClass="required form-control "  maxlength="64"  path="name" title="${msgFirstName}"/>
			                                <span class="help-inline"><form:errors path="name" cssClass="error" /></span>
			                        </div>
			                        <label class="col-sm-2 control-label"><s:message code="label.customer.companyname" text="Company"/>:</label>
			                        <div class="col-sm-4">			              		
			              				<form:input  cssClass="form-control "  maxlength="100" path="company"/>	
				            		</div>
				            </div>
				            <div class="form-group">		
				            		<label class="col-sm-2 control-label"><s:message code="label.customer.telephone" text="Phone"/>:</label>
				            		<div class="col-sm-4">
		                        			<s:message code="NotEmpty.customer.billing.phone" text="Phone number is required" var="msgPhone"/>
		                                    <form:input cssClass="required form-control phone"  maxlength="32" path="telephone" title="${msgPhone}"/>
		                                    <span class="help-inline"><form:errors path="telephone" cssClass="error" /></span>
		                       		 </div>
		                       		 <label class="col-sm-2 control-label"><s:message code="label.customer.country" text="Country"/>:</label>
		                       		 <div class="col-sm-4"> 				       							
		       							<form:select cssClass="billing-country-list form-control " path="country" id="customer_country">
			  								<form:options items="${countries}" itemValue="isoCode" itemLabel="name"/>
		       							</form:select>
	                                 	<span class="help-inline"><form:errors path="country" cssClass="error" /></span>
		                        	</div> 
		                      </div>
		                      <div class="form-group">  	
		                        	<label class="col-sm-2 control-label"><s:message code="label.customer.zone" text="State / Province"/>:</label>
		                        	<div class="col-sm-4">		       							
		       							<form:select cssClass="billing-zone-list form-control " path="zone" id="customer_zones"/>
		       							<s:message code="NotEmpty.customer.stateProvince" text="State / Province is required" var="msgStateProvince"/>
	                      				<form:input  class="form-control " id="hidden_zones" maxlength="100"  name="state" path="state" title="${msgStateProvince}" />		       							
	                                 	<span class="help-inline"><form:errors path="zone" cssClass="error" /></span>
		                       		</div>
		                       		<label class="col-sm-2 control-label"><s:message code="label.customer.city" text="City"/>:</label>
		                       		<div class="col-sm-4">
				            			<s:message code="NotEmpty.customer.city" text="City is required" var="msgCity"/>
					 					<form:input  cssClass="required form-control "  maxlength="100" path="city" title="${msgCity}" id="customer_city"/>
				            		</div>
				            </div>
				            <div class="form-group">		
				            		<label class="col-sm-2 control-label"><s:message code="label.generic.postalcode" text="Postal code"/>:</label>
				            		<div class="col-sm-4">
				 						<s:message code="NotEmpty.customer.billing.postalCode" text="Postal code is required" var="msgPostalCode"/>
				 						<form:input id="billingPostalCode" cssClass="customer-postalCode form-control " maxlength="20"  path="postCode" title="${msgPostalCode}"/>
				 						<span class="help-inline"><form:errors path="postCode" cssClass="error" /></span>
				            		</div>
				            		<label class="col-sm-2 control-label"><s:message code="label.customer.streetaddress" text="Street Address"/>:</label>
				            		<div class="col-sm-4">			            	
			            				<s:message code="NotEmpty.customer.address" text="Address is required" var="msgAddress"/>
				 						<form:input  cssClass="required form-control"  maxlength="256"  path="streetAdress" title="${msgAddress}"/>		 				
			           		 		</div>
			           		 </div>
			           		 <div class="form-group">		
			           		 		<label class="col-sm-2 control-label"><s:message code="label.product.proof.title" text="Memo"/>:</label>
			           		 		<div class="col-sm-10">	
						              		<form:input  cssClass="input-large form-control form-control-md"  maxlength="256" path="memo"/>						              		
							         </div>
			            	</div>	
			               <div class="form-group">
    							<div class="col-sm-3 col-sm-offset-2">
     								 <input type="checkbox" id="setdefaultaddress"  name="setdefaultaddress" checked="checked"><s:message code="label.generic.set" text="Set"/><s:message code="label.generic.default" text="Default"/><s:message code="label.customer.address" text="Address"/></input>
  							  </div>
  							  <div class="col-sm-3 col-sm-offset-2">
     								 <input id="Ezybiosubmit" class="btn btn-info btn-disabled " type="button" onClick="javascript:addnewaddres();" name="submitAddress" value="<s:message code="menu.editaddress" text="Change address"/>" disabled="">
  							  </div>
  						</div>
				</form:form>
						
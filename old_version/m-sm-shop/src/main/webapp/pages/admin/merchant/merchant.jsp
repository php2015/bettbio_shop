<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>  

<%@ page session="false" %>


    <link href="<c:url value="/resources/css/bootstrap/css/datepicker.css" />" rel="stylesheet"></link>
	<script src="<c:url value="/resources/js/bootstrap/bootstrap-datepicker.js" />"></script>
	<script src="<c:url value="/resources/js/validphone.js" />"></script>

<script src="<c:url value="/resources/js/selectcheckbox.js" />"></script> 
<link href="<c:url value="/resources/css/image/dropify.css" />" rel="stylesheet"></link>
<script src="<c:url value="/resources/js/tools/dropify.js" />"></script>
<script>


$(document).ready(function() {
	
	if($("#code").val()=="") {
		$('.btn').addClass('disabled');
	}
//$('#phoneFix').attr('placeholder','021-61552790');
//$('#phonefax').attr('placeholder','021-61552790');
	
	<c:choose>
	<c:when test="${store.storestateprovince!=null && store.storestateprovince!=''}">
		$('.zone-list').hide();          
		$('#storestateprovince').show(); 
		$('#storestateprovince').val('<c:out value="${store.storestateprovince}"/>');
	</c:when>
	<c:otherwise>
		$('.zone-list').show();           
		$('#storestateprovince').hide();
		getZones('<c:out value="${store.country.isoCode}" />'); 
	</c:otherwise>
	</c:choose>

	$(".country-list").change(function() {
		getZones($(this).val());
    })

    var drEvent = $('.dropify-event').dropify();

    drEvent.on('dropify.beforeClear', function(event, element){
  	  var rdel=confirm('<s:message code="label.entity.remove.confirm" text="Delete"/>');
  	  if(rdel == true){
  		  //if(${product.productImage} != null){
  			  removeImage(${store.id});
  		  //}
  	  }
        return rdel;
    });
});

$.fn.addItems = function(data) {
    $(".zone-list > option").remove();
        return this.each(function() {
            var list = this;
            $.each(data, function(index, itemData) {
                var option = new Option(itemData.name, itemData.code);
                list.add(option);
            });
     });
};

function getZones(countryCode){
	$.ajax({
	  type: 'POST',
	  url: '<c:url value="/admin/reference/provinces.html"/>',
	  data: 'countryCode=' + countryCode,
	  dataType: 'json',
	  success: function(response){

			var status = response.response.status;
			if(status==0 || status ==9999) {
				
				var data = response.response.data;
				if(data && data.length>0) {
					
					$('.zone-list').show();  
					$('#storestateprovince').hide();
					$(".zone-list").addItems(data);
					<c:if test="${store.zone!=null}">
						$('.zone-list').val('<c:out value="${store.zone.code}"/>');
						$('#storestateprovince').val('');
					</c:if>
				} else {
					$('.zone-list').hide();             
					$('#storestateprovince').show();
					<c:if test="${store.storestateprovince!=null}">
						$('#storestateprovince').val('<c:out value="${store.storestateprovince}"/>');
					</c:if>
				}
			} else {
				$('.zone-list').hide();             
				$('#storestateprovince').show();
			}

	  
	  },
	  error: function(xhr, textStatus, errorThrown) {
	  	//alert('error ' + errorThrown);
	  }
	  
	});
}


function removeImage(obj){
	$("#store.error").show();
	$.ajax({
	  type: 'POST',
	  url: '<c:url value="/admin/store/licence/removeImage.html"/>',
	  dataType: 'json',
	  data: {id:obj},
	  success: function(response){
			var status = response.response.status;
			if(status==0 || status ==9999) {
				
				$(".alert-success").show();
				
			} else {
				//display message
				$(".alert-error").show();
			}
	  },
	  error: function(xhr, textStatus, errorThrown) {
	  	//alert('error ' + errorThrown);
	  }
	  
	});
}
</script>


<div class="">


				<jsp:include page="/common/adminTabs.jsp" />
					<c:url var="merchant" value="/admin/store/save.html"/>
	<br>
					<form:form method="POST" commandName="store" action="${merchant}" class="form-horizontal" enctype="multipart/form-data">
					
						<form:errors path="*" cssClass="alert alert-danger" element="div" />
						<div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>    
							<c:if test="${not empty store.code}">
							 <div class="form-group form-group-lg">
		                        <label class="col-sm-2 control-label"><s:message code="label.storecode" text="Store code"/></label>
		                         <div class="col-sm-10">
		                        					<label class="uneditable-input control-label">${store.code}</label><form:hidden path="code" />
		                        			<span class="help-inline"><div id="checkCodeStatus" style="display:none;"></div><form:errors path="code" cssClass="error" /></span>
		                        </div>
		                 	</div>
			                </c:if>
			                  
			      			<div class="form-group form-group-lg">
			                        <label class="col-sm-2 control-label"><s:message code="label.storename" text="Name"/></label>
			                        <div class="col-sm-10">
			                        		<form:input cssClass="form-control" path="storename" />
			                                    <span class="help-inline"><form:errors path="storename" cssClass="error" /></span>
			                        </div>
			                  </div>
			                  
			                  <div class="form-group form-group-lg">
			                        <label class="col-sm-2 control-label"><s:message code="label.store.contacts" text="Contacts"/></label>
			                        <div class="col-sm-10">
			                        		<form:input cssClass="form-control" path="storecontacts" />
			                        		<span class="help-inline"><form:errors path="storecontacts" cssClass="error" /></span>
			                        </div>
			                  </div>
			                  
			                  <div class="form-group form-group-lg">
			                        <label class="col-sm-2 control-label"><s:message code="label.storephone" text="Phone"/></label>
			                         <div class="col-sm-10">
			                                    <form:input id="phoneFix" cssClass="form-control" placeholder="021-61552790" path="storephone" onblur="validphone(this.value)"/>
			                                    <span class="help-inline"><form:errors path="storephone" cssClass="error" /></span>
			                        </div>
			
			                  </div>
			                  
			                  <div class="form-group form-group-lg">
			                        <label class="col-sm-2 control-label"><s:message code="label.store.mobile" text="mobile"/></label>
			                        <div class="col-sm-10">
			                        		<form:input cssClass="form-control" path="storemobile" onblur="validmobile(this.value)" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/>
			                        </div>
			                  </div>
			                  
			                  <div class="form-group form-group-lg">
			                        <label class="col-sm-2 control-label"><s:message code="label.store.fax" text="fax"/></label>
			                        <div class="col-sm-10">
			                        		<form:input if="phonefax" cssClass="form-control" placeholder="021-61552790" path="storefax" onblur="validphone(this.value)"/>
			                        </div>
			                  </div>
			                  
			                  <div class="form-group form-group-lg">
			                        <label class="col-sm-2 control-label"><s:message code="label.storeemailaddress" text="Email"/></label>
			                         <div class="col-sm-10">
			                                    <form:input cssClass="form-control" path="storeEmailAddress" onblur="validEmail(this.value)"/>
			                                    <span class="help-inline"><form:errors path="storeEmailAddress" cssClass="error" /></span>
			                        </div>
			                  </div>
			            
			                 <div class="form-group form-group-lg">
			                        <label class="col-sm-2 control-label"><s:message code="label.storeaddress" text="Address"/></label>
			                         <div class="col-sm-10">
			                                    <form:input cssClass="form-control" path="storeaddress" />
			                                    <span class="help-inline"><form:errors path="storeaddress" cssClass="error" /></span>
			                        </div>
			                  </div>
			                  
			                  
			                  <div class="form-group form-group-lg">
			                        <label class="col-sm-2 control-label"><s:message code="label.storecity" text="City"/></label>
			                         <div class="col-sm-10">
			                                    <form:input cssClass="form-control" path="storecity" />
			                                    <span class="help-inline"><form:errors path="storecity" cssClass="error" /></span>
			                        </div>
			                  </div>
			                  
			                   <div class="form-group form-group-lg">
			                        <label class="col-sm-2 control-label"><s:message code="label.storecountry" text="Store Country"/></label>
			                         <div class="col-sm-10">
			                        					
			                        					<form:select cssClass="country-list form-control" path="country.isoCode">
							  								<form:options items="${countries}" itemValue="isoCode" itemLabel="name"/>
						       							</form:select>
			                                   			<span class="help-inline"><form:errors path="country" cssClass="error" /></span>
			                        </div>
			                  </div>
			                  
		
			                 <div class="form-group form-group-lg">
			                        <label class="col-sm-2 control-label"><s:message code="label.storezone" text="Store state / province"/></label>
			                         <div class="col-sm-10">
			                        					<form:select cssClass="zone-list form-control" path="zone.code"/>
			                        					<input type="text" class="input-large form-control" id="storestateprovince" name="storestateprovince" value="${store.storestateprovince}" /> 
			                                   			<span class="help-inline"><form:errors path="zone.code" cssClass="error" /></span>
			                        </div>
			                  </div>
		               
		                
			                  
				                  <div class="form-group form-group-lg">
				                        <label class="col-sm-2 control-label"><s:message code="label.storepostalcode" text="Postal code"/></label>
				                         <div class="col-sm-10">
				                                    <form:input cssClass="form-control" path="storepostalcode" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/>
				                                    <span class="help-inline"><form:errors path="storepostalcode" cssClass="error" /></span>
				                        </div>
				                  </div>
				                  
								<div class="form-group form-group-lg">
				                        <label class="col-sm-2 control-label"><s:message code="label.store.introduce" text="label.store.introduce"/></label>
				                         <div class="col-sm-10">
				                         	<form:textarea name="" rows="6" class="form-control" path="introduce"/>
				                        </div>
				                </div>
				                <div class="form-group form-group-lg">
				                        <label class="col-sm-2 control-label"><s:message code="label.store.enintroduce" text="label.store.enintroduce"/></label>
				                         <div class="col-sm-10">
				                         	<form:textarea name="" rows="6" class="form-control" path="enintroduce" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" onafterpaste="value=value.replace(/[^\w\.\/]/ig,'')"/>
				                        </div>
				                </div>
				                  				                  
				                   <div class="form-group" style="display:none;">
				                        <label class="col-sm-2 control-label"><s:message code="label.supportedlanguages" text="Supported languages"/></label>
				                         <div class="col-sm-10">
			
				                        					<form:checkboxes cssClass="highlight" items="${languages}" itemValue="code" itemLabel="code" path="languages" /> 
				                                   			<span class="help-inline"><form:errors path="languages" cssClass="error" /></span>
				                        </div>
				                  </div>
				                  
				                  
				                  <div class="form-group" style="display:none;">
				                        <label class="col-sm-2 control-label"><s:message code="label.defaultlanguage" text="Default language"/></label>
				                         <div class="col-sm-10">
			
				                        					<form:select items="${languages}" itemValue="id" itemLabel="code" path="defaultLanguage.id"/> 
				                                   			<span class="help-inline"></span>
				                        </div>
				                  </div>
				                  
				                  <div class="form-group" style="display:none;">
				                        <label class="col-sm-2 control-label"><s:message code="label.currency" text="Currency"/></label>
				                         <div class="col-sm-10">
			
				                        					<form:select items="${currencies}" itemValue="id" itemLabel="code"  path="currency.id"/> 
				                                   			<span class="help-inline"></span>
				                        </div>
				                  </div>
				                  
				                  
				                  
				                  <div class="form-group form-group-lg">
				                        <label class="col-sm-2 control-label"><s:message code="label.store.businesslicence" text="business licence"/></label>
				                         <div class="col-sm-10" id="imageControl">
				                        	<c:choose>
				                     		<c:when test="${store.businesslicence==null || store.businesslicence==''}">				                                 
				                                 <input type="file" name="licenceimg" id="licenceimg"  class="dropify-event" >
				                             </c:when>
				                             <c:otherwise>
				                             	<input type="file" name="image" id="image"  class="dropify-event" data-default-file="<sm:contentImage imageName="${store.businesslicence}" imageType="STORELICENCE"/>" style="width:300px">
				                              </c:otherwise>
				                            </c:choose>
				                        </div>
				                  </div>
				                  
                   	
				                  <sec:authorize access="hasRole('ADMIN') and fullyAuthenticated">
				                  <div class="form-group form-group-lg">
				                        <label class="col-sm-2 control-label"><s:message code="label.store.inbusinesssince" text="Web site operating since"/></label>
				                         <div class="col-sm-10">
                        					<input id="dateBusinessSince" name="dateBusinessSince" value="${store.dateBusinessSince}" class="form-control" type="text" data-date-format="<%=com.salesmanager.core.constants.Constants.DEFAULT_DATE_FORMAT%>" data-datepicker="datepicker"> 
                                   			 <span class="help-inline"><form:errors path="dateBusinessSince" cssClass="error" /></span>
				                        </div>
				                  </div>
				                  <div class="form-group form-group-lg">
				                        <label class="col-sm-2 control-label"><s:message code="label.store.useCache" text="Use cache"/></label>
				                         <div class="col-sm-10">
			
				                        					<form:checkbox path="useCache" class="form-control"/> 
				                                   			<span class="help-inline"></span>
				                        </div>
				                  </div>
				                  
				                  <div class="form-group form-group-lg">
		                      	  <label class="col-sm-2 control-label"><s:message code="label.store.baseurl" text="Store base url"/></label>
		                    	     <div class="col-sm-10">
		                    	     	<div class="input-group">
		                                    <span class="input-group-addon">http://</span>
											<form:input cssClass="form-control" path="domainName" />
		                                    <span class="help-inline"><form:errors path="domainName" cssClass="error" /></span>
		                                  </div> 
		                     	   </div>
		                 		 </div>
		                 		 </sec:authorize>
		                  <form:hidden path="id" />
		                  <form:hidden path="storeLogo" />
		                  <form:hidden path="storeTemplate" />
	
					      <div class="form-actions">
		                  		<div class="pull-right">
		                  			<button type="submit" id="bettSumbit" class="btn btn-success"><s:message code="button.label.submit2" text="Submit"/></button>
		                  		</div>
		            	 </div>
	
	
	      					
					</form:form>
		
</div>
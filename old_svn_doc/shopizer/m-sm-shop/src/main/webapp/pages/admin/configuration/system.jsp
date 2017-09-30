<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>		



<div class="tabbable">

  					
 					<jsp:include page="/common/adminTabs.jsp" />
  					
  						
							<br/>
							
							<c:url var="saveSystemConfiguration" value="/admin/configuration/saveSystemConfiguration.html"/>
							<form:form method="POST" commandName="configuration" action="${saveSystemConfiguration}" class="form-horizontal">

      							
      								<form:errors path="*" cssClass="alert alert-danger" element="div" />
									<div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>    

		                        	   <div class="form-group form-group-lg">
	                        				<label class="col-sm-2 control-label"><s:message code="label.customer.displaycustomersection" text="Display customer section" /></label>
	                        				<div class="col-sm-10">
	                        					<form:checkbox path="displayCustomerSection" cssClass="form-control" /> 
	                                   			<span class="help-inline"></span>
	                        				</div>
	                  				   </div>
	                  				   
	                  				   
	                  				  <div class="form-group form-group-lg">
	                        				<label class="col-sm-2 control-label"><s:message code="label.store.displaycontactussection" text="Display contact us page" /></label>
	                        				<div class="col-sm-10">
	                        					<form:checkbox path="displayContactUs" cssClass="form-control" /> 
	                                   			<span class="help-inline"></span>
	                        				</div>
	                  				   </div>
	                  				   
	                  				   <div class="form-group form-group-lg">
	                        				<label class="col-sm-2 control-label"><s:message code="label.store.displaystoreaddress" text="Display store address" /></label>
	                        				<div class="col-sm-10">
	                        					<form:checkbox path="displayStoreAddress"  cssClass="form-control"/> 
	                                   			<span class="help-inline"></span>
	                        				</div>
	                  				   </div>
	                  				   
	                  				   <c:forEach items="${store.languages}" var="language">
	                  				   
	                  				   <div class="form-group form-group-lg">
	                        				<label class="col-sm-2 control-label"><s:message code="label.configuration.useglobalsearch_suggestions" text="Use global pre-defined search suggestions" /> (${language.code})</label>
	                        				<div class="col-sm-10">
	                        					<form:checkbox path="useDefaultSearchConfig['${language.code}']" value="on" cssClass="form-control"/> 
	                        					<br/>
	                        					<s:message code="label.configuration.globalsearch_suggestions_path" text="Global pre-defined search suggestions file path" /> (${language.code})<br/>
	                        					<form:input cssClass="form-control" path="defaultSearchConfigPath['${language.code}']" />
	                                   			<span class="help-inline"></span>
	                        				</div>
	                  				   </div>
	                  				   
	                  				   </c:forEach>
	                  				   
	                  				   <div class="form-group form-group-lg">
	                        				<label class="col-sm-2 control-label"><s:message code="label.store.displayaddtocartfeatured" text="Allow add to cart on featured items" /></label>
	                        				<div class="col-sm-10">
	                        					<form:checkbox path="displayAddToCartOnFeaturedItems" cssClass="form-control"/> 
	                                   			<span class="help-inline"></span>
	                        				</div>
	                  				   </div>
	                  				   
	                  				 <div class="form-actions">
                  						<div class="pull-right">
                  							<button type="submit" class="btn btn-success"><s:message code="button.label.submit" text="Submit"/></button>
                  						</div>
            	 					</div>
					                  

            	 			</form:form>

   				
				</div>
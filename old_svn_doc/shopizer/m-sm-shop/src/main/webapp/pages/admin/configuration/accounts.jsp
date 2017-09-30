<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>				
				
<script>
	

	
</script>


<div class="tabbable">
	<jsp:include page="/common/adminTabs.jsp" />
		
					<br/>
						<c:url var="saveAccountsConfiguration" value="/admin/configuration/saveConfiguration.html"/>
							<form:form method="POST" modelAttribute="configuration" action="${saveAccountsConfiguration}" class="form-horizontal">
								<form:errors path="*"  cssClass="alert alert-danger" element="div" />
									<div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>
									<c:forEach var="merchantConfig" items="${configuration.merchantConfigs}" varStatus="counter"> 

		                        		
		                        	   <div class="form-group form-group-lg">
	                        				<label class="required col-sm-2 control-label"><s:message code="label.configuration.${merchantConfig.key}" text="** Label for [label.configuration.${merchantConfig.key}] not found **" /> &nbsp;:&nbsp;</label>
					                        <div class="col-sm-10">
					                        		<form:input  path="merchantConfigs[${counter.index}].value" cssClass="form-control" />
											        <form:hidden  path="merchantConfigs[${counter.index}].key" />
											        <form:hidden  path="merchantConfigs[${counter.index}].id" />
					                                <span class="help-inline"><form:errors path="merchantConfigs[${counter.index}].key" cssClass="error" /></span>
					                        </div>
	                  				   </div>
		                        		
		                        		
	                        		</c:forEach>

	                        		<div class="form-actions">
                  						<div class="pull-right">
                  							<button type="submit" class="btn btn-success"><s:message code="button.label.submit" text="Submit"/></button>
                  						</div>
            	 					</div>
					                  

            	 			</form:form>
   		
</div>
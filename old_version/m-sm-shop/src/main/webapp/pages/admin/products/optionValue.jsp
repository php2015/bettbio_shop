<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>
<link href="<c:url value="/resources/css/image/dropify.css" />" rel="stylesheet"></link>
<script src="<c:url value="/resources/js/tools/dropify.js" />"></script>
<%@ page session="false" %>		



<script type="text/javascript">
$(document).ready(function() {
	 var drEvent = $('.dropify-event').dropify();

	    drEvent.on('dropify.beforeClear', function(event, element){
	  	  var rdel=confirm('<s:message code="label.entity.remove.confirm" text="Delete"/>');
	  	  if(rdel == true){
	  		  //if(${product.productImage} != null){
	  			  removeImage(${optionValue.id});
	  		  //}
	  	  }
	        return rdel;
	    });
});
	
	
	function removeImage(id){
			$("#store.error").show();
			$.ajax({
			  type: 'POST',
			  url: '<c:url value="/admin/optionsvalues/removeImage.html"/>',
			  dataType: 'json',
			  data: 'optionId=' + id,
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
		
				


<div class="tabbable">

		<jsp:include page="/common/adminTabs.jsp" />	
		
				<br/>

				<c:url var="optionSave" value="/admin/options/saveOptionValue.html"/>


				<form:form method="POST" enctype="multipart/form-data" commandName="optionValue" action="${optionSave}" class="form-horizontal">

      							
      				<form:errors path="*" cssClass="alert alert-danger" element="div" />
					<div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>    
				
				 <div class="form-group form-group-lg">
                        <label class="col-sm-2 control-label"><s:message code="label.entity.code" text="Option value code"/></label>
                        <div class="col-sm-10">
                                    <form:input cssClass="form-control" id="code" path="code"/>
                                    <span class="help-inline"><form:errors path="code" cssClass="error" /></span>
                        </div>
                  </div>
							
                 <c:forEach items="${optionValue.descriptionsSettoList}" var="description" varStatus="counter">
                 
	                 <div class="form-group form-group-lg">
	                        <label class=" col-sm-2 control-label required"><s:message code="label.product.productoptions.name" text="Option name"/> (<c:out value="${description.language.code}"/>)</label>
	                        <div class="col-sm-10">
	                        			<form:input cssClass="form-control" id="name${counter.index}" path="descriptionsList[${counter.index}].name"/>
	                        			<span class="help-inline"><form:errors path="descriptionsList[${counter.index}].name" cssClass="error" /></span>
	                        </div>
	
	                  </div>
	                 
	                  <form:hidden path="descriptionsList[${counter.index}].language.code" />
	                  <form:hidden path="descriptionsList[${counter.index}].id" />
                  
                  </c:forEach>
                  

                 <div class="form-group form-group-lg">
                        <label class="col-sm-2 control-label"><s:message code="label.product.image" text="Image"/>&nbsp;</label>
                        <div class="col-sm-10" id="imageControl">
                        		<c:choose>
	                        		<c:when test="${optionValue.productOptionValueImage==null || optionValue.productOptionValueImage==''}">
	                                    <input type="file" name="image" id="image"  class="dropify-event" >
	                                </c:when>
	                                <c:otherwise>	                                	
	                                	<input type="file" name="image" id="image"  class="dropify-event" data-default-file="<sm:contentImage imageName="${optionValue.productOptionValueImage}" imageType="PROPERTY"/>" style="width:80">
	                                </c:otherwise>
                                </c:choose>
                        </div>
                  </div>
                  
                  <form:hidden path="productOptionValueImage" />
                  <form:hidden path="id" />
			
			      <div class="form-actions">
                  		<div class="pull-right">
                  			<button type="submit" class="btn btn-success"><s:message code="button.label.submit" text="Submit"/></button>
                  		</div>
            	 </div>
 
            	 </form:form>
      		</div>
      		
   			      			     
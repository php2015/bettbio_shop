<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>   
<link href="<c:url value="/resources/css/image/dropify.css" />" rel="stylesheet"></link>
<script src="<c:url value="/resources/js/tools/dropify.js" />"></script>
<%@ page session="false"%>



<script type="text/javascript">
$(document).ready(function() {
	 var drEvent = $('.dropify-event').dropify();

	    drEvent.on('dropify.beforeClear', function(event, element){
	  	  var rdel=confirm('<s:message code="label.entity.remove.confirm" text="Delete"/>');
	  	  if(rdel == true){
	  		  //if(${product.productImage} != null){
	  			  removeImage();
	  		  //}
	  	  }
	        return rdel;
	    });
});
	
	function removeImage(){
			$("#store.error").show();
			$.ajax({
			  type: 'POST',
			  url: '<c:url value="/admin/store/removeImage.html"/>',
			  dataType: 'json',
			  success: function(response){
		
					var status =response.response.status;
					if(status==0 || status ==9999) {
						$(".alert-success").show();
						
					} else {
						$(".alert-error").show();
					}
		
			  
			  },
			  error: function(xhr, textStatus, errorThrown) {
			  	//alert('error ' + errorThrown);
			  }
			  
			});
	}
	
</script>

<jsp:include page="/common/adminTabs.jsp" />
<div class="row">
	<br>
				<c:url var="saveBrandingImage" value="/admin/store/saveBranding.html" />
				<form:form method="POST" enctype="multipart/form-data" commandName="contentImages" action="${saveBrandingImage}" class="form-horizontal">

					<form:errors path="*" cssClass="alert alert-danger" element="div" />
					<div id="store.success" class="alert alert-success"
						style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>">
						<s:message code="message.success" text="Request successfull" />
					</div>
					

				
					<!-- hidden when creating the product -->
					<div class="form-group form-group-lg">
						<label class="col-sm-2 control-label"><s:message code="label.storelogo" text="Store logo"/>&nbsp;</label>
						 <div class="col-sm-6" id="imageControl">
						
							   <c:choose>
	                        		<c:when test="${store.storeLogo==null}">	                                   
	                                     <input type="file" name="file[0]" id="file"  class="dropify-event" >
	                                    <br/>
	                                </c:when>
	                                <c:otherwise>
	                                	<input type="file" name="image" id="image"  class="dropify-event" data-default-file="<sm:contentImage imageName="${store.storeLogo}" imageType="LOGO"/>" style="width:200px">	                                	
	                                </c:otherwise>
	                            </c:choose>
						</div>
					</div>
					<div class="form-actions row">
						<div class="pull-right">
							<button type="submit" class="btn btn-success btn-lg">
								<s:message code="button.label.submit2" text="Submit" />
							</button>
						</div>
					</div>
				</form:form>
				
							
			</div>
		</div>
	</div>	
				

				
				
				
				



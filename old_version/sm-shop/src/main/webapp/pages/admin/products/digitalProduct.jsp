<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>   

<%@ page session="false"%>
<c:set value="${product.id}" var="productId" scope="request"/>			
<style>
.row {
	margin: 15px 0px;
}
</style>
<script type="text/javascript">
	
	function removeFile(fileId){
			$("#store.error").show();
			$.ajax({
			  type: 'POST',
			  url: '<c:url value="/admin/products/product/removeDigitalProduct.html"/>?fileId=' + fileId,
			  dataType: 'json',
			  success: function(response){
		
					var status = response.response.status;
					if(status==0 || status ==9999) {
						
						//remove delete
						$("#productControlRemove").html('');
						$("#fileLink").hide();
						//add field
						$("#fileControl").html('<input class=\"input-file btn btn-lg btn-info\" id=\"file\" name=\"file\" type=\"file\">');
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
<c:url var="saveProductFile" value="/admin/products/product/saveDigitalProduct.html" />
<jsp:include page="/common/adminTabs.jsp" />
<div class ="row">
	<!-- begin left menu content -->
	<div style="float:left;width:15%;padding-right:5px;">
		<jsp:include page="/pages/admin/products/product-menu.jsp">
			<jsp:param name="active" value="digitalProduct" />
		</jsp:include>
	</div><!-- end left menu content -->
	
	<div id="showloading"></div>	
	<!-- begin middle main content -->
	<div style="float:left;width:85%" id="p-title">
		<h3><s:message code="label.product.digitalproduct" text="Digital product"/></h3> 
		<form:form method="POST" enctype="multipart/form-data" commandName="productFiles" action="${saveProductFile}" cssClass="form-horizontal">
			<form:errors path="*" cssClass="alert alert-danger" element="div" />
			<div id="store.success" class="alert alert-success"
				style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>">
				<s:message code="message.success" text="Request successfull" />
			</div>
			<input type="hidden" name="product.id" value="${product.id}" />
		
			<!-- hidden when creating the product -->
			<div class="control-group">
				<label>
					<s:message code="label.product.digitalproduct" text="Digital product"/>&nbsp;
						<c:if test="${digitalProduct!=null}"><span id="productControlRemove"> - <a href="#" onClick="removeFile('${digitalProduct.id}')"><s:message code="label.generic.remove" text="Remove"/></a></span></c:if>
				</label>
				<div class="controls" id="fileControl">
					<c:choose>
               			<c:when test="${digitalProduct==null}">
                           <input class="input-file btn btn-primary" id="file" name="file" type="file">				                                
                       	</c:when>
                       	<c:otherwise>
                       		<a id="fileLink" href="<sm:adminProductDownload digitalProduct="${digitalProduct}" />">${digitalProduct.productFileName}</a>
                       	</c:otherwise>
                  	</c:choose>
				</div>
			</div>
			<div class="row">
				<div class="pull-right" style="padding-right:60px">
					<button type="submit" class="btn btn-primary">
						<s:message code="button.label.submit2" text="Submit" />
					</button>
				</div>
			</div>
		</form:form>
	</div><!-- end middle main content -->
</div>

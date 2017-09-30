<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%> 
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>
<%@ page session="false" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<link href="<c:url value="/resources/css/bootstrap/css/datepicker.css" />" rel="stylesheet"></link>
<script src="<c:url value="/resources/js/bootstrap/bootstrap-datepicker.js" />"></script>
<script src="<c:url value="/resources/js/adminFunctions.js" />"></script>
<script src="<c:url value="/resources/js/ckeditor/ckeditor.js" />"></script>
<script SRC="<c:url value="/resources/js/selectList.js" />"></script>	
<link href="<c:url value="/resources/css/image/dropify.css" />" rel="stylesheet"></link>
<script src="<c:url value="/resources/js/tools/dropify.js" />"></script>
<script>
var self='<s:message code="label.bettbio.selfdifine" text="Edit product" />';
$(function(){
	getStores(null,self);
	 var drEvent = $('.dropify-event').dropify();

     drEvent.on('dropify.beforeClear', function(event, element){
   	  var rdel=confirm("你真的要删除 \"" + element.filename + "\"文件吗?");
   	  if(rdel == true){
   		  //if(${product.productImage} != null){
   			  removeImage(${proof.id});
   		  //}
   	  }
         return rdel;
     });
});
function removeImage(proofId){
	if(proofId==null || proofId=='' || typeof(proofId) == "undefined"){
		return ;
	}
			$("#store.error").show();
			$.ajax({
			  type: 'POST',
			  url: '<c:url value="/admin/products/proof/removeImage.html"/>',
			  data: 'proofId=' + proofId,
			  dataType: 'json',
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
<style>
.row {
	margin: 15px 0px;
}
</style>
<c:url var="saveProductProof" value="/admin/products/proof/save.html" />
<c:set value="${product.id}" var="productId" scope="request"/>
<div class="row">
	<div id="showloading"></div>
	<!-- begin middle main content -->
	<div id="p-title">
		  <a class="btn btn-default" href="<c:url value="/admin/products/proofs.html?id=${product.id}"/>"><s:message code="button.label.returnlist" text="Return List" /></a>
		<c:if test="${proof!=null&&proof.id!=null}">
			<a class="btn btn-primary" href="<c:url value="/admin/products/proof/create.html?productId=${product.id}"/>"><s:message code="label.product.proof.create" text="Create proof" /></a>	
		</c:if>
		<br/>

		<!-- begin edit block -->
		<form:form method="POST" commandName="proof" enctype="multipart/form-data" action="${saveProductProof}" cssClass="form-horizontal">
			<form:errors path="*" cssClass="alert alert-danger" element="div" />
			<div id="store.success" class="alert alert-success"
				style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>">
				<s:message code="message.success" text="Request successfull" />
			</div>
			
			<div class="row">
				<div class="col-sm-6">
				 	<label><s:message code="label.product.proof.buyer" text="proof buyer"/></label>                   
		       		<div class="input-group">
	                             <input type="text" class="form-control highlight"	id="searchstorename" name="searchstorename" readonly value="<c:out value="${proofname}"/>" />
	                             <span class="input-group-addon dropdown-toggle" id="sns" data-toggle="collapse" aria-haspopup="true" onclick="setreadonly()"	href="#collapseExample" aria-expanded="false"	aria-controls="collapseExample"> 
	                             <span	class="glyphicon glyphicon-th " aria-hidden="true"></span>	</span>
	                           </div>	
	                     		  <form:input type="hidden" path="basedataType.id" name="storename" id="storenamehidden" /> 
				</div>
				<div style="width:50%;float:left;padding:0px 15px;">
					<label><s:message code="label.product.proof.dateBuyed" text="Date buyed"/></label>
					<input id="dateBuyed" name="dateBuyed" class="form-control highlight" value="<fmt:formatDate value="${proof.dateBuyed}" pattern="yyyy-MM-dd"/>" type="text" data-date-format="yyyy-MM-dd" data-datepicker="datepicker"> 
	                <span class="help-inline"><form:errors path="dateBuyed" cssClass="error" /></span>
				</div>
				<div class="collapse" id="collapseExample">
									<div class="well" id="storeNameList"></div>
								</div>
			</div>
			
						
			<div class="row">
				<div class="col-sm-12">
					<label><s:message code="label.product.proof.image" text="Image"/>&nbsp;
                    </label>
                   	<div class="controls" id="imageControl">
                   		<c:choose>
                    		<c:when test="${proof.proofImage==null || proof.proofImage==''}">                               
                                <input type="file" name="image" id="image"  class="dropify-event" >
                            </c:when>
                            <c:otherwise>                            	
                            	<input type="file" name="image" id="image"  class="dropify-event" data-default-file="<sm:productImage imageName="${proof.proofImage}" product="${product}" productRelated="proof"/>">
                            </c:otherwise>
                       	</c:choose>
                    </div>
                    <div style="font-style:italic;color:#aaa;margin:5px;"><a href='<c:url value="/shop/software/list.html"/>' target="blank"><s:message code="label.common.software.tip" text="Common Software Download"/></a></div>
				</div>
			</div>
			
			<div class="row">
				<div class="col-sm-12">
					<label class="required"><s:message code="label.product.proof.description" text="proof description"/> </label>
                    <textarea cols="30" id="description" name="description">
                    	<c:out value="${proof.description}"/>
                    </textarea>
                    <script type="text/javascript">
					//<![CDATA[
						CKEDITOR.replace('description',
						{
							skin : 'office2003',
							toolbar : 
							[
								['Source','-','Save','NewPage','Preview'], 
								['Cut','Copy','Paste','PasteText','-','Print'], 
								['Undo','Redo','-','Find','-','SelectAll','RemoveFormat'], '/', 
								['Bold','Italic','Underline','Strike','-','Subscript','Superscript'], 
								['NumberedList','BulletedList','-','Outdent','Indent','Blockquote'], 
								['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'], 
								['Link','Unlink','Anchor'], 
								['Image','Flash','Table','HorizontalRule','SpecialChar','PageBreak'], '/', 
								['Styles','Format','Font','FontSize'], ['TextColor','BGColor'], 
								['Maximize', 'ShowBlocks'] 
							],
							
							filebrowserWindowWidth : '720',
       						filebrowserWindowHeight : '740',
							filebrowserImageBrowseUrl :    '<c:url value="/admin/content/fileBrowser.html"/>'
						});

					//]]>
					</script>
				</div>
			</div>
			
			<!-- default one time -->
			<form:hidden path="id" />
           	<form:hidden path="product.id" />
			<div class="row">
				<div class="pull-right">
					<button type="submit" class="btn btn-primary">
						<s:message code="button.label.submit" text="Submit" />
					</button>
				</div>
			</div>
		</form:form>
		<!-- end edit block -->
	</div>
	<!-- end middle main content -->
</div>

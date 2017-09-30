<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>			


<%@ page session="false" %>			

<link href="<c:url value="/resources/css/bootstrap/css/datepicker.css" />" rel="stylesheet"></link>
<script src="<c:url value="/resources/js/bootstrap/bootstrap-datepicker.js" />"></script>
<script src="<c:url value="/resources/js/ckeditor/ckeditor.js" />"></script>
<script src="<c:url value="/resources/js/jquery.formatCurrency-1.4.0.js" />"></script>
<script src="<c:url value="/resources/js/jquery.alphanumeric.pack.js" />"></script>
<script src="<c:url value="/resources/js/adminFunctions.js" />"></script>

<script type="text/javascript">

	
	$(function(){		
		$('#order').numeric();
	});

	
	function removeImage(imageId){
			$("#store.error").show();
			$.ajax({
			  type: 'POST',
			  url: '<c:url value="/admin/manufacturers/manufacturer/removeImage.html"/>',
			  data: 'imageId=' + imageId,
			  dataType: 'json',
			  success: function(response){
		
					var status = response.response.status;
					if(status==0 || status ==9999) {
						
						//remove delete
						$("#imageControlRemove").html('');
						//add field
						$("#imageControl").html('<input class=\"input-file\" id=\"image\" name=\"image\" type=\"file\">');
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
								
				<c:url var="manufacturerSave" value="/admin/catalogue/manufacturer/save.html"/>
 
				<form:form method="POST" commandName="manufacturer" action="${manufacturerSave}" class="form-horizontal">

      				<form:hidden path="manufacturer.id" /> 			
      				

                    <form:errors path="*" cssClass="alert alert-danger" element="div" />
                    <div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>   
                    <div id="store.error" class="alert alert-danger" style="display:none;"><s:message code="message.error" text="An error occured"/></div>
	
					
					<c:forEach items="${manufacturer.descriptions}" var="description" varStatus="counter">
					       
						<div class="form-group form-group-lg">
	                        <label class="required col-sm-2 control-label"><s:message code="label.manufactureredit.manufacturername" text="Manufacturer Name"/> (<c:out value="${description.language.code}"/>)</label>
	                        <div class="col-sm-10">
	                                  <form:input cssClass="form-control" id="name${counter.index}" path="descriptions[${counter.index}].name"/>
	                                  <span  class="help-inline"><form:errors path="descriptions[${counter.index}].name" cssClass="error" /></span>
	                        </div>
	                  	</div> 

						<div class="form-group form-group-lg">
	                        <label class="col-sm-2 control-label"><s:message code="label.manufactureredit.manufacturertitle" text="Manufacturer Title"/> (<c:out value="${description.language.code}"/>)</label>
	                        <div class="col-sm-10">
	                                  <form:input cssClass="form-control" id="title${counter.index}" path="descriptions[${counter.index}].title"/>
	                                  <span  class="help-inline"><form:errors path="descriptions[${counter.index}].title" cssClass="error" /></span>
	                        </div>
	                  	</div> 

 	                  	<div class="form-group form-group-lg">
	                        <label class="col-sm-2 control-label"><s:message code="label.manufactureredit.manufacturerurl" text="URL"/> (<c:out value="${description.language.code}"/>)</label>
	                        <div class="col-sm-10">
	                                  <form:input cssClass="form-control" id="url${counter.index}" path="descriptions[${counter.index}].url"/>
	                                  <span  class="help-inline"><form:errors path="descriptions[${counter.index}].url" cssClass="error" /></span>
	                        </div>
	                  	</div>	
                  
	                    <div class="form-group form-group-lg">
	                            <label class="required col-sm-2 control-label"><s:message code="label.manufactureredit.manufacturerdescription" text="Manufacturer Description"/> (<c:out value="${description.language.code}"/>)</label>
	                            <div class="col-sm-10">
	                     	 
	                        			 <textarea cols="30" id="descriptions${counter.index}.description" name="descriptions[${counter.index}].description" class="form-control">
	                        				<c:out value="${manufacturer.descriptions[counter.index].description}"/>
	                        			 </textarea>
	                            </div>	                  	
	                            <script type="text/javascript">
						//<![CDATA[

									CKEDITOR.replace('descriptions[${counter.index}].description',
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
						
						 <form:hidden path="descriptions[${counter.index}].language.id" />
                         <form:hidden path="descriptions[${counter.index}].language.code" />
						 <form:hidden path="descriptions[${counter.index}].id" />
	                  	                 		                  		                  	 
                	</c:forEach>
            	    <div class="form-actions">
                            <div class="pull-right">
                                    <button type="submit" class="btn btn-success"><s:message code="button.label.submit2" text="Submit"/></button>
                            </div>
                    </div>
                   
            	</form:form>
          </div>
           
                    
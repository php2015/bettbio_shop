<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>				
				

<script src="<c:url value="/resources/js/ckeditor/ckeditor.js" />"></script>
<script src="<c:url value="/resources/js/jquery.alphanumeric.pack.js" />"></script>


	<script type="text/javascript">
	

	
	$(function(){
		$('#order').numeric();
		if($("#code").val()=="") {
			$('.btn').addClass('disabled');
		}
		<c:forEach items="${category.descriptions}" var="description" varStatus="counter">		
			$("#name${counter.index}").friendurl({id : 'seUrl${counter.index}'});
		</c:forEach>
	});
	
	
	function validateCode() {
		$('#checkCodeStatus').html('<img src="<c:url value="/resources/img/ajax-loader.gif" />');
		$('#checkCodeStatus').show();
		var code = $("#code").val();
		var id = $("#id").val();
		checkCode(code,id,'<c:url value="/admin/categories/checkCategoryCode.html" />');
	}
	
	function callBackCheckCode(msg,code) {
		console.log(code);
		if(code==0) {
			$('.btn').removeClass('disabled');
		}
		if(code==9999) {

			$('#checkCodeStatus').html('<font color="green"><s:message code="message.code.available" text="This code is available"/></font>');
			$('#checkCodeStatus').show();
			$('.btn').removeClass('disabled');
		}
		if(code==9998) {

			$('#checkCodeStatus').html('<font color="red"><s:message code="message.code.exist" text="This code already exist"/></font>');
			$('#checkCodeStatus').show();
			$('.btn').addClass('disabled');
		}
		
	}
	
	
	</script>



<div class="">

					<jsp:include page="/common/adminTabs.jsp" />
  													
				<br/>

				<c:url var="categorySave" value="/admin/categories/save.html"/>


				<form:form method="POST" commandName="category" action="${categorySave}" class="form-horizontal">

      							
      				<form:errors path="*" cssClass="alert alert-danger" element="div" />
					<div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>    
					
					      			 <div class="form-group form-group-lg">
                    	    <label class="col-sm-2 control-label"><s:message code="label.category.parentcategory" text="Category vsible"/></label>
	                        <div class="col-sm-10">
	                        		<s:message code="label.category.root" text="Root" var="rootVar"/>			
	                        		<form:select path="parent.id" cssClass="form-control">	                        			
					  					<form:options items="${categories}" itemValue="id" itemLabel="descriptions[0].name"/>
				       				</form:select>
	                                <span class="help-inline"><form:errors path="parent.id" cssClass="error" /></span>
	                        </div>
	                  </div>	
					                    
	                  <div class="form-group form-group-lg">
	                        <label class="col-sm-2 control-label"><s:message code="label.category.code" text="Category code"/></label>
		                        <div class="col-sm-10">
		                        		<form:input cssClass="form-control" path="code" onblur="validateCode()"/>
		                                <span class="help-inline"><div id="checkCodeStatus" style="display:none;"></div><form:errors path="code" cssClass="error" /></span>
		                        </div>
	                  </div>
	                  <div class="form-group form-group-lg">
                        <label class="col-sm-2 control-label"><s:message code="label.entity.order" text="Sort order"/></label>
                        <div class="col-sm-10">
                                    <form:input id="order" cssClass="form-control" path="sortOrder"/>
                                    <span class="help-inline"><form:errors path="sortOrder" cssClass="error" /></span>
                        </div>
                 	 </div>
                 	 <div class="form-group form-group-lg">
	                        <label class="col-sm-2 control-label"><s:message code="label.entity.visible" text="Visible"/></label>
	                        <div class="col-sm-10">
	                                    <form:checkbox path="visible" cssClass="form-control"/>
	
	                        </div>
	                  </div>
	
                  
                 	<c:forEach items="${category.descriptions}" var="description" varStatus="counter">
                 
	                 <div class="form-group form-group-lg">
	                        <label class="col-sm-2 control-label"><s:message code="label.productedit.categoryname" text="Category name"/> (<c:out value="${description.language.code}"/>)</label>
	                        <div class="col-sm-10">
	                        			<form:input cssClass="form-control" id="name${counter.index}" path="descriptions[${counter.index}].name"/>
	                        			<span class="help-inline"><form:errors path="descriptions[${counter.index}].name" cssClass="error" /></span>
	                        </div>
	                  </div>
                  
	                  <div class="form-group form-group-lg">
	                        <label class="col-sm-2 control-label"><s:message code="label.category.highlight" text="Category highlight"/> (<c:out value="${description.language.code}"/>)</label>
	                        <div class="col-sm-10">
	                        			<form:input cssClass="form-control" id="categoryHighlight${counter.index}" path="descriptions[${counter.index}].categoryHighlight"/>
	                        			<span class="help-inline"><form:errors path="descriptions[${counter.index}].categoryHighlight" cssClass="error" /></span>
	                        </div>
	                  </div>
                  
	                  <div class="form-group form-group-lg">
	                        <label class="col-sm-2 control-label"><s:message code="label.sefurl" text="SEF Url"/> (<c:out value="${description.language.code}"/>)</label>
	                        <div class="col-sm-10">
	                        			<form:input cssClass="form-control" id="seUrl${counter.index}" path="descriptions[${counter.index}].seUrl"/>
	                        			<span class="help-inline"><form:errors path="descriptions[${counter.index}].seUrl" cssClass="error" /></span>
	                        </div>
	
	                  </div>
	                
	                  
                 
                  		<div class="form-group form-group-lg">
                        <label class="col-sm-2 control-label"><s:message code="label.category.title" text="Metatag title"/> (<c:out value="${description.language.code}"/>)</label>
                        <div class="col-sm-10">
                        			<form:input path="descriptions[${counter.index}].metatagTitle" cssClass="form-control"/>
                        			<span class="help-inline"><form:errors path="descriptions[${counter.index}].metatagTitle" cssClass="error" /></span>
                        </div>
	                  </div>
	                  
	                  <div class="form-group form-group-lg">
	                        <label class="col-sm-2 control-label"><s:message code="label.metatags.keywords" text="Metatag keywords"/> (<c:out value="${description.language.code}"/>)</label>
	                        <div class="col-sm-10">
	                        			<form:input path="descriptions[${counter.index}].metatagKeywords" cssClass="form-control"/>
	                        			<span class="help-inline"><form:errors path="descriptions[${counter.index}].metatagKeywords" cssClass="error" /></span>
	                        </div>
	                  </div>
	                  
	                 <div class="form-group form-group-lg">
	                        <label class="col-sm-2 control-label"><s:message code="label.metatags.description" text="Metatag description"/> (<c:out value="${description.language.code}"/>)</label>
	                        <div class="col-sm-10">
	                        			<form:input path="descriptions[${counter.index}].metatagDescription" cssClass="form-control"/>
	                        			<span class="help-inline"><form:errors path="descriptions[${counter.index}].metatagDescription" cssClass="error" /></span>
	                        </div>
	                  </div>
                 
                
                  	<div class="form-group form-group-lg">
                        <label class="col-sm-2 control-label"><s:message code="label.category.categorydescription" text="Category description"/> (<c:out value="${description.language.code}"/>)</label>
                        <div class="col-sm-10">
                        

                        
                        <textarea cols="30" id="descriptions[${counter.index}].description"  name="descriptions[${counter.index}].description" Class="form-control">
                        		<c:out value="${category.descriptions[counter.index].description}"/>
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
                  
                  
                  <form:hidden path="descriptions[${counter.index}].language.code" />
                  <form:hidden path="descriptions[${counter.index}].id" />
                  
                  </c:forEach>
                  
                    
                  
                  <form:hidden path="id" />
			
			      <div class="form-actions">

                  		<div class="pull-right">

                  			<button type="submit" class="btn btn-success"><s:message code="button.label.submit" text="Submit"/></button>
                  			

                  		</div>

            	 </div>
 
            	 </form:form>
            	 
</div>
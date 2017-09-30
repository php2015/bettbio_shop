<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>				
<script src="<c:url value="/resources/js/selectcheckbox.js" />"></script> 
<script src="<c:url value="/resources/js/findCommon.js" />"></script> 
<script src="<c:url value="/resources/js/customProductGroup.js" />"></script> 	
<script src="<c:url value="/resources/js/commonActive.js" />"></script> 		
<script type="text/javascript">
$(function () {
	initCheckbox();
	$('#findName').attr('placeholder','<s:message code="label.entity.code" text="hodle"/>');
	initStatus(${criteria.avaiable});
	var findholder ='<s:message	code="label.entity.code" text="holde" />';
});
var count=0;
var page=1;
function alertBlank(){
	$("#resultTitle").html('<img src="<c:url value="/resources/img/icon_error.png"/>" width="40"/>&nbsp;<strong><s:message code="label.entity.actived.failed" text="Failed" /><s:message code="message.group.required" text="Failed" /></strong>');
	$('#showReust').modal('show');
}
function alertexist(){
	$("#resultTitle").html('<img src="<c:url value="/resources/img/icon_error.png"/>" width="40"/>&nbsp;<strong><s:message code="label.entity.actived.failed" text="Failed" /><s:message code="message.group.alerady.exists" text="Failed" /></strong>');
	$('#showReust').modal('show');
}
</script>


<div class="table" id="showloading">

 					<jsp:include page="/common/adminTabs.jsp" />
 					<jsp:include page="/common/findControls.jsp"/>  
		<div id="shop">			
			
			<ul class="nav nav-pills pull-right form-inline">
				<li><a  href="javascript:void(0);" onclick="$('#addgroup').modal('show');" ><s:message code="label.product.customgroup.add" text="Add custom group" /></a></li>
			</ul>
			<!-- HISTORY TABLE -->
			<table id ="storelist" class="table table table-striped table-bordered table-hover">
				<!-- table head -->
				<thead>
					<tr >
						<th><input type="checkbox" id="selectall"> </th>
						<th ><s:message code="label.entity.code" text="Code"/></th>
						<th ><s:message code="label.entity.active" text="Enabled"/><div class="pull-right"><input type="checkbox" id="allAv"></th>
						<th><s:message code="label.generic.remove" text="Del"/></th>
					</tr>
				</thead>
				<c:if test="${not empty ships && not empty ships.ships}">
				<tbody>
					<c:forEach items="${ships.ships}" var="ship" varStatus="usersStatus">
						<script type="text/javascript">
						count++;
						</script>
						<tr id="result_name_${ship.id}" class="everyTR">
							<td> <input type="checkbox" class="everycheckbox" id="${ship.id}"></td>
							<td >${ship.name}</td>	
							<td><span id="av_${ship.id}">${ship.active}</span><div class="pull-right"><input type="checkbox" onclick="changeav(this)" id="${ship.id}" <c:if test="${ship.active == true}"> checked="checked"</c:if>></div></td>							
							<td ><a href="javascript:void(0);" onclick="delEntity(${ship.id})" ><s:message code="label.generic.remove" text="Del" /></a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<c:set var="paginationData" value="${paginationData}" scope="request"/>
			<jsp:include page="/common/paginationFind.jsp"/>
		</div>
	</c:if>
</div>
<div class="modal fade" id="addgroup" tabindex="-1" role="dialog"  aria-hidden="true" data-backdrop= 'static'>
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><img src="<c:url value="/resources/img/important-icon.png"/>" width="40"/>&nbsp;<strong ><s:message code="label.product.customgroup.add" text="Add custom group" /></strong></h4>  
      </div>
       <c:url var="saveProductGrop" value="/admin/products/groups/save.html"/>
	      <div class="modal-body">  	
						<div class="control-group">
		                        <label class="required"><s:message code="label.product.customgroup.code" text="Custom product group code"/></label>
		                        <div class="controls">
		                                    <input id="groupcode" cssClass="highlight" />
		                                    
		                        </div>
		                 </div>			      
	      </div>
	      <div class="modal-footer">  
	         <input type="hidden" id="url"/>  
	         <button type="button" class="btn btn-default" data-dismiss="modal"><s:message code="button.label.cancel" text="Cancel"/></button>  
	        	<a href="javascript:void(0);"   onclick="addgroup()" class="btn btn-success" data-dismiss="modal" ><s:message code="button.label.ok" text="Confirm"/></a>    
	      </div>
      
    </div>
  </div>
</div>   
  <jsp:include page="/common/delForm.jsp"/>   
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>				

<script src="<c:url value="/resources/js/featured.js" />"></script>  
<script src="<c:url value="/resources/js/tree/bootstrap-treeview.js" />"></script>  
<script src="<c:url value="/resources/js/pagingation.js" />"></script> 
<script src="<c:url value="/resources/js/selectcheckbox.js" />"></script> 
<script src="<c:url value="/resources/js/commonActive.js" />"></script>
 <!--[if IE]>
		<script type="text/javascript" src="/resources/js/tree/ietree.js"></script>
	<![endif]-->				
<script>
	
$(function () {
	getTreeData('<s:message code="label.category.root" text="Root" />');
	var $check = $("#availableall"),	el;

	$check
	.data('checked', 0)
	.click(function(e) {

	  el = $(this);

	  switch (el.data('checked')) {
	    case 0:
	      el.data('checked', 1);
	      el.prop('indeterminate', true);
	      getbystatus(null);
	      break;

	      // indeterminate, going checked
	    case 1:
	      el.data('checked', 2);
	      el.prop('indeterminate', false);
	      el.prop('checked', true);
	      getbystatus(true);
	      break;

	      // checked, going unchecked
	    default:
	      el.data('checked', 0);
	      el.prop('indeterminate', false);
	      el.prop('checked', false);
	      getbystatus(false);
	  }
	});
});
	var productURL='<c:url value="/admin/products/editProduct.html" />?id=';
	var deltile='<s:message code="label.generic.remove" text="Del" />';
	function writeptile(offset,countByPage,totalCount){
		$("#ptitle").html('<s:message code="label.entitylist.paging" arguments="'+offset+';'+countByPage+';'+totalCount+'" htmlEscape="false" argumentSeparator=";" text=""/>');
	}
	var categoryId=-1;
	var page=1;
	var count=0;
	var avstatus=null;
	var findholder ='<s:message	code="label.generic.product.find.holde" text="holde" />';
	
</script>


<div class="tabbable" id="showloading">

  					
					<jsp:include page="/common/adminTabs.jsp" />

<div class ="row">

    <div class="col-lg-2 padding-left padding-right">
	      <div id="cataTree" class="padding-left padding-right">
	      </div>
     </div>
     <div id="showloading">
	  	 <div class="col-lg-10 padding-left " id="p-title">
	  	 <ul class="nav nav-pills pull-right form-inline">
	  	 		<!-- li><a href="javascript:void(0);" onclick="importf()"><s:message code="label.product.list.import" text="import"/></a></li-->
	  	 		<li class="dropdown" id="featured"> 
						
			 </li>	
	  	 		<li><a href="javascript:void(0);" onclick="addgroup()"><s:message code="label.product.list.selecte" text="Selected"/><s:message code="button.label.addgroup" text="Selected"/></a></li>
	  	 		<li><a href="javascript:void(0);" onclick="removeFeatures()"><s:message code="label.product.list.selecte" text="Selected"/><s:message code="button.label.removegroup" text="Selected"/></a></li>
				 <li >
				     <div class="input-group" style="padding-top:3px;width:250px">      
					      <input  id="findbyname" type="text" class="form-control " placeholder=<s:message	code="label.generic.product.find.holde" text="holde" />>
					      <a  href="javascript:void(0);" onclick="findByName()" class="input-group-addon" ><span class="glyphicon glyphicon-search" aria-hidden="true"  ></span></a>
				    </div>
				</li>
				
			</ul>
	   	 	<table class="table table-striped table-bordered table-hover " id="CustmoersTable">	
				<thead id ="prodcutTable">											 
					<tr class="cubox">
						<th><input type="checkbox" id="selectall"> </th>
						<th><s:message code="label.entity.name" text="Name"/></th>
						<th><s:message code="label.product.sku" text="Sku"/></th>
						<th><s:message code="label.productedit.code" text="Code"/></th>
						<th><s:message code="label.product.available" text="Available"/><div class="pull-right"><input type="checkbox" id=availableall></div></th>
						<th><s:message code="label.storename" text="Store"/></th>
						<th><s:message code="button.label.removegroup" text="Store"/></th>
					</tr>
				</thead>
				<tbody id ="prodcutTbody" >
				</tbody>
			</table>
			<div class="">
					<div class="col-lg-3 padding-top-20" >
						<span class="p-title-text" id="ptitle"></span>
						
					</div>
					<div id="pagination" class="pull-right " ></div>
			</div>
	     </div>
     </div>
               	 
</div>

  					
				</div>
				<div class="modal fade" id="addgroup" tabindex="-1" role="dialog"  aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><img src="<c:url value="/resources/img/important-icon.png"/>" width="40"/>&nbsp;<strong ><s:message code="label.product.list.selecte" text="Selected"/><s:message code="button.label.addgroup" text="Selected"/></strong></h4>  
      </div>
      <div class="modal-body">  
        <form class="form-inline">
			   <div id="groupModal"></div> 
		</form>
        
      </div>
       <div class="modal-footer">  
         <input type="hidden" id="url"/>  
         <button type="button" class="btn btn-default" data-dismiss="modal"><s:message code="button.label.cancel" text="Cancel"/></button>  
        	<a href="javascript:void(0);"   onclick="" class="btn btn-success" data-dismiss="modal" id="addgroupHref"><s:message code="button.label.ok" text="Confirm"/></a>  
      </div>  
    </div>
  </div>
</div>
<div class="modal fade" id="removeModel" tabindex="-1" role="dialog"  aria-hidden="true" data-backdrop= 'static'>
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><img src="<c:url value="/resources/img/important-icon.png"/>" width="40"/>&nbsp;<strong ><s:message code="button.label.removegroup" text="Remove"/><s:message code="label.generic.confirm" text="Confirm"/>!</strong></h4>  
      </div>
      <div class="modal-body">  
        <p><s:message code="button.label.removegroup.confirm" text="Confirm"/></p>  
      </div>
      <div class="modal-footer">  
         <input type="hidden" id="url"/>  
         <button type="button" class="btn btn-default" data-dismiss="modal"><s:message code="button.label.cancel" text="Cancel"/></button>  
        	<a href="javascript:void(0);"   onclick="" class="btn btn-success" data-dismiss="modal" id="removegroupHref"><s:message code="button.label.ok" text="Confirm"/></a>  
      </div>  
    </div>
  </div>
</div>   	
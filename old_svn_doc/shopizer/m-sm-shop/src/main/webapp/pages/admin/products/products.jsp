<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>				
				
<script src="<c:url value="/resources/js/tree/bootstrap-treeview.js" />"></script>  
<script src="<c:url value="/resources/js/products.js" />"></script>  
<script src="<c:url value="/resources/js/pagingation.js" />"></script> 
<script src="<c:url value="/resources/js/selectcheckbox.js" />"></script> 
<script src="<c:url value="/resources/js/commonActive.js" />"></script> 
 <!--[if IE]>
		<script type="text/javascript" src="/resources/js/tree/ietree.js"></script>
	<![endif]-->
<script type="text/javascript">
$(function () {
	getTreeData('<s:message code="label.category.root" text="Root" />');
	$("#p-title").hide();	
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
var qualitystar='<c:url value="/resources/img/stars/star-on.png" />';
var qualityhalfstar='<c:url value="/resources/img/stars/star-half.png" />';
var deltile='<s:message code="label.generic.remove" text="Del" />';
function writeptile(offset,countByPage,totalCount){
	$("#ptitle").html('<s:message code="label.entitylist.paging" arguments="'+offset+';'+countByPage+';'+totalCount+'" htmlEscape="false" argumentSeparator=";" text=""/>');
}
var categoryId=-1;
var page=1;
var count=0;
var avstatus=null;
var findholder ='<s:message	code="label.generic.product.find.holde" text="holde" />';
function getAudit(au){
	switch (au){
		case -2:return '<s:message code="label.bettbio.product.failed.audit" text="Failed audit" />';
		case -1:return '<s:message code="label.bettbio.product.fail.audit" text="Failed audit" />';
		case 1: return '<s:message code="label.bettbio.product.after.audit" text="Audited" />';
		default : return '<s:message code="label.bettbio.product.pre.audit" text="Wait audit" />';
	}
		
}

//点击产品信息
function editProduct(id,audit,isService) {
	$("#edit_pid").val(id);
	$("#edit_audit").val(audit);
	$("#editproduct").modal("show");
	$('#myTab a:first').tab("show");
	var eaudit=$("#edit_audit").val();
	
	$("#editAudit").css("display","block");
	$("#editAudit").html(' 	<a href="javascript:void(0);"  onclick=""  aria-controls="new_audit" role="tab" data-toggle="tab" id="auditHref"><s:message code="button.label.submit2" text="Submit" /><s:message code="menu.audit" text="Audit" /></a>');
	$("#auditHref").bind('click', function(){
		confrom($("#edit_pid").val());
	});
	if(isService==true){
		$("#certificates").css('display','none');
		$("#selfproofs").css('display','none');
		$("#certsli").hide();
		$("#selfsli").hide();
		$("#proofsa").html('<s:message code="label.service.proofs" text="Product proofs" />');
	}else{
		$("#certificates").css('display','');
		$("#selfproofs").css('display',''); 
		$("#certsli").show();
		$("#selfsli").show();
		$("#proofsa").html('<s:message code="label.product.proofs" text="Product proofs" />');
	}
	
	$("#details_iframe").attr("src",'<c:url value="/admin/products/editProduct.html" />?id='+id);
}
</script>



  					
<jsp:include page="/common/adminTabs.jsp" />
<div class ="row" >

    <div class="col-lg-2 padding-left padding-right">
	      <div id="cataTree" class="padding-left padding-right">
	      </div>
     </div>
     <div >
	  	 <div class="col-lg-10 padding-left " id="p-title">
	  	 <ul class="nav nav-pills pull-right form-inline">
	  	 		<!-- li><a href="javascript:void(0);" onclick="importf()"><s:message code="label.product.list.import" text="import"/></a></li-->
	  	 		<li><a href="javascript:void(0);" onclick="deleteall()"><s:message code="label.generic.remove" text="Delete"/><s:message code="label.product.list.all" text="All"/></a></li>
	  	 		<li><a href="javascript:void(0);" onclick="deleteps()"><s:message code="label.generic.remove" text="Delete"/><s:message code="label.product.list.selecte" text="Selected"/></a></li>
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
						<th width="2%"><input type="checkbox" id="selectall"> </th>
						<th width="12%"><s:message code="label.entity.name" text="Name"/></th>
						<th width="11%"><s:message code="label.productedit.productenname" text="English Name"/></th>
						<th width="15%"><s:message code="label.product.sku" text="Sku"/></th>
						<th width="10%"><s:message code="label.productedit.code" text="Code"/></th>
						<th width="7%"><s:message code="label.bettbio.product.audit" text="Audit"/></th>
						<th width="10%"><s:message code="label.product.quality" text="Audit"/></th>
						<th width="9%"><s:message code="label.product.available" text="Available"/><div class="pull-right"><input type="checkbox" id=availableall></div></th>
						<th width="9%"><s:message code="label.product.availabledate" text="Date available"/></th>
						<th width="10%"><s:message code="label.storename" text="Store"/></th>
						<th width="6%"><s:message code="label.generic.remove" text="Del"/></th>
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
<div class="modal fade" id="showprogress" tabindex="-1" role="dialog"  aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
      </div>
      <div class="modal-body">  
        <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 2%;" id="precent">0% </div>  
      </div>
    </div>
  </div>
</div>
<input type="file" name="file" class="hidden" value="" />
<jsp:include page="/common/delForm.jsp"/> 

<!-- Modal -->
<div class="modal fade" id="editproduct" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document" style="width:95%">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">
          <span id="productlabel"></span>&nbsp;&nbsp;
          <a target="blank" id="producttitle" style="color:green"></a>
        </h4>
      </div>
      <div class="modal-body">
        <jsp:include page="/pages/admin/products/editproduct.jsp" />
      </div>
    </div>
  </div>
</div> 
 	
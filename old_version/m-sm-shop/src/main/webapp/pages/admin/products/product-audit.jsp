<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>				
				

<script src="<c:url value="/resources/js/product-audit.js" />"></script>  
<script src="<c:url value="/resources/js/pagingation.js" />"></script> 
<script src="<c:url value="/resources/js/selectcheckbox.js" />"></script> 
<script src="<c:url value="/resources/js/commonActive.js" />"></script> 
 <!--[if IE]>
		<script type="text/javascript" src="/resources/js/tree/ietree.js"></script>
	<![endif]-->
<script type="text/javascript">
$(function () {
	getProducts();
	$("#p-title").hide();	
	
});

function writeptile(offset,countByPage,totalCount){
	$("#ptitle").html('<s:message code="label.entitylist.paging" arguments="'+offset+';'+countByPage+';'+totalCount+'" htmlEscape="false" argumentSeparator=";" text=""/>');
}

var page=1;
var count=0;
var audittile='<s:message code="label.bettbio.product.audit" text="Audit" />';
var productURL='<c:url value="/admin/audit/audit.html" />?pid=';
var findholder ='<s:message	code="label.generic.product.find.holde" text="holde" />';
</script>



  					
					<jsp:include page="/common/adminTabs.jsp" />
<div  id="showloading" class ="row">
     
	  	 <div class="col-lg-12" id="p-title">
	  	 <ul class="nav nav-pills pull-right form-inline">
				 <li >
				     <div class="input-group" style="padding-top:3px;width:250px">      
					      <input  id="findbyname" type="text" class="form-control " placeholder=<s:message	code="label.generic.product.find.holde" text="holde" />>
					      <a  href="javascript:void(0);" onclick="findByName()" class="input-group-addon" ><span class="glyphicon glyphicon-search" aria-hidden="true"  ></span></a>
				    </div>
				</li>
			</ul>
			</div> 
	   	 	<table class="table table-striped table-bordered table-hover " id="CustmoersTable">	
				<thead id ="prodcutTable">											 
					<tr class="cubox">
						<!-- th><input type="checkbox" id="selectall"> </th-->
						<th><s:message code="label.entity.name" text="Name"/></th>
						<th><s:message code="label.productedit.productenname" text="English Name"/></th>
						<th><s:message code="label.product.sku" text="Sku"/></th>
						<th><s:message code="label.productedit.code" text="Code"/></th>
						<th><s:message code="label.storename" text="Store"/></th>
						<th><s:message code="label.bettbio.product.audit" text="Audit"/></th>
					</tr>
				</thead>
				<tbody id ="prodcutTbody" >
				</tbody>
			</table>
			<div class="row">
					<div class="col-lg-3 padding-top-20" >
						<span class="p-title-text" id="ptitle"></span>
					</div>
					<div id="pagination" class="pull-right " ></div>
			</div>
       	 
</div>
<div class="modal fade" id="audit" tabindex="-1" role="dialog"  aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
      </div>
      <div class="modal-body">  
       		<jsp:include page="/common/delForm.jsp"/>   
      </div>
    </div>
  </div>
</div>

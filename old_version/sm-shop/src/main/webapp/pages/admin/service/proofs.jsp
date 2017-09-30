<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>				
<c:set value="${product.id}" var="productId" scope="request"/>			
<style>
.row {
	margin: 15px 0px;
}
</style>
<script src="<c:url value="/resources/js/selectcheckbox.js" />"></script>
<script>
var productId = '${product.id}';
var pagingUrl = '<c:url value="/admin/products/proofs/paging.html?productId=${product.id}"/>';
var editUrl = '<c:url value="/admin/products/proof/edit.html?productId=${product.id}"/>';
var removesUrl = '<c:url value="/admin/products/proof/removes.html?productId=${product.id}"/>';
$(function(){
	initCheckbox();
	loadDataList();
});
function loadDataList() {
	$("#showloading").showLoading();
	$("#p-title").hide();
	$.post(pagingUrl, {}, function(result){
		if(result.response.status == 0) {//RESPONSE_STATUS_SUCCESS
			var list = result.response.data;
			$("#proofsTbody").empty();
			$(list).each(function(index){
				var tbBody="";
				tbBody += '<tr id="result_name_'+this.proofId+'" class="everyTR">';
				tbBody += '<td><input type="checkbox" class="everycheckbox" id="'+this.proofId+'"></td>';
				tbBody += '<td><a href="'+editUrl+'&id='+this.proofId+'">'+this.buyer+'</a></td>';
				tbBody += '<td>'+this.dateBuyed+'</td>';				
		        tbBody += '<td>'+'<a href="javascript:void(0);" onclick="removefun('+this.proofId+')"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a></td>';
		        tbBody += '</tr>';
				$("#proofsTbody").append(tbBody);
			});
		} else {
			//loading data unsuccessfully
			$("#p-title").html(result.response.statusMessage).addClass("alert alert-danger").attr("role", "alert");
		}
		$("#showloading").hideLoading();
		$("#p-title").show();
	});
}
function removefun(proofId) {
	
	$("#confirmHref").unbind("click").bind("click",function(){
		$.post(removesUrl, {proofId: proofId}, function(result){
			if(result.response.status == 9999) {//RESPONSE_OPERATION_COMPLETED
				loadDataList();
			} else {
				$("#p-title").empty().html(result.response.statusMessage).addClass("alert alert-danger").attr("role", "alert");
			}
		});
	});
	$('#delcfmModel').modal('show');
}
function deleteps(){
	var len = $(".everycheckbox:checked").length;
	var ids = "";
	if(len==0) {
		$("#deleteps").popover({
			container: 'body',
			title: 'Tips',
			placement: 'top',
			content: 'no item selected, please check it again!',
			trigger: 'focus'
		}).popover('toggle');
		$(".popover-title").css('background-color','#992266').css('color','#fff');
		$("#deleteps").on('hidden.bs.popover', function(){
			$('#deleteps').popover('destroy');
		});
		return;
	} else {
		$(".everycheckbox:checked").each(function(index, item){
			ids += item.id + ",";
		});
		removefun(ids);
	} 
}
</script>					
<div class ="row">
	<div id="showloading"></div>	
	<!-- begin middle main content -->
	<div id="p-title">
		<a class="btn btn-primary" href="<c:url value="/admin/products/proof/create.html?productId=${product.id}&availabilityId=${availability.id}"/>"><s:message code="label.product.proof.create" text="Create proof" /></a>	
		<br/>
		<!-- begin search box -->
		<ul class="nav nav-pills pull-right form-inline">
  	 		<li><a href="javascript:void(0);" onclick="deleteps()" id="deleteps" class="btn btn-primary" role="button"><s:message code="label.generic.remove" text="Delete"/><s:message code="label.product.list.selecte" text="Selected"/></a></li>
		</ul><!-- end search box -->
		<!-- begin content list -->
		<table class="table table-striped table-bordered table-hover "
			id="CustmoersTable">
			<thead id="proofsTable">
				<tr class="cubox">
					<th><input type="checkbox" id="selectall"></th>
					<th><s:message code="label.service.proof.buyer" text="Buyer"/></th>
					<th><s:message code="label.service.proof.dateBuyed" text="DateBuyed"/></th>					
					<th><s:message code="label.generic.remove" text="Del" /></th>
				</tr>
			</thead>
			<tbody id="proofsTbody">
			</tbody>
		</table>
		<!-- end content list -->
	</div><!-- end middle main content -->
</div>
<jsp:include page="/common/delForm.jsp"/>

	      			     
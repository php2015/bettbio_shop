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
var pagingUrl = '<c:url value="/admin/products/selfproofs/paging.html?productId=${product.id}"/>';
var editUrl = '<c:url value="/admin/products/selfproof/edit.html?productId=${product.id}"/>';
var removesUrl = '<c:url value="/admin/products/selfproof/removes.html?productId=${product.id}"/>';
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
			$("#selfproofsTbody").empty();
			$(list).each(function(index){
				var tbBody="";
				tbBody += '<tr id="result_name_'+this.selfproofId+'" class="everyTR">';
				tbBody += '<td><input type="checkbox" class="everycheckbox" id="'+this.selfproofId+'"></td>';
				tbBody += '<td><a href="'+editUrl+'&id='+this.selfproofId+'">'+this.title+'</a></td>';
				tbBody += '<td>'+this.order+'</td>';
		        tbBody += '<td>'+'<a href="javascript:void(0);" onclick="removefun('+this.selfproofId+')"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a></td>';
		        tbBody += '</tr>';
				$("#selfproofsTbody").append(tbBody);
			});
		} else {
			//loading data unsuccessfully
			$("#p-title").html(result.response.statusMessage).addClass("alert alert-danger").attr("role", "alert");
		}
		$("#showloading").hideLoading();
		$("#p-title").show();
	});
}
function removefun(selfproofId) {
	
	$("#confirmHref").unbind("click").bind("click",function(){
		$.post(removesUrl, {selfproofId: selfproofId}, function(result){
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
		<a class="btn btn-primary" href="<c:url value="/admin/products/selfproof/create.html?productId=${product.id}&availabilityId=${availability.id}"/>"><s:message code="label.product.selfproof.create" text="Create selfproof" /></a>	
		<br/>
		<!-- begin search box -->
		<ul class="nav nav-pills pull-right form-inline">
  	 		<li><a href="javascript:void(0);" onclick="deleteps()" id="deleteps" class="btn btn-primary" role="button"><s:message code="label.generic.remove" text="Delete"/><s:message code="label.product.list.selecte" text="Selected"/></a></li>
		</ul><!-- end search box -->
		<!-- begin content list -->
		<table class="table table-striped table-bordered table-hover "
			id="CustmoersTable">
			<thead id="selfproofsTable">
				<tr class="cubox">
					<th><input type="checkbox" id="selectall"></th>
					<th><s:message code="label.product.selfproof.title" text="Title"/></th>
					<th><s:message code="label.entity.order" text="Order"/></th>
					<th><s:message code="label.generic.remove" text="Del" /></th>
				</tr>
			</thead>
			<tbody id="selfproofsTbody">
			</tbody>
		</table>
		<!-- end content list -->
	</div><!-- end middle main content -->
</div>
<jsp:include page="/common/delForm.jsp"/>	


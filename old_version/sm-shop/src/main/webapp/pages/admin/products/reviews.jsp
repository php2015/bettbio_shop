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
var pagingUrl = '<c:url value="/admin/products/reviews/paging.html?productId=${product.id}"/>';
var removesUrl = '<c:url value="/admin/products/review/removes.html?productId=${product.id}"/>';
$(function(){
	loadDataList();
});
function loadDataList() {
	$("#showloading").showLoading();
	$("#p-title").hide();
	$.post(pagingUrl, {findname: $("#findbyname").val()}, function(result){
		if(result.response.status == 0) {//RESPONSE_STATUS_SUCCESS
			var list = result.response.data;
			$("#reviewsTbody").empty();
			$(list).each(function(index){
				var tbBody="";
				tbBody += '<tr id="result_name_'+this.reviewId+'" class="everyTR">';
				tbBody += '<td>'+this.description+'</a></td>';
				tbBody += '<td>'+this.rating+'</td>';
		        tbBody += '</tr>';
				$("#reviewsTbody").append(tbBody);
			});
		} else {
			//loading data unsuccessfully
			$("#p-title").html(result.response.statusMessage).addClass("alert alert-danger").attr("role", "alert");
		}
		$("#showloading").hideLoading();
		$("#p-title").show();
	});
}

</script>

<div class ="row">
	<div id="showloading"></div>	
	<!-- begin middle main content -->
	<div id="p-title">
		<!-- begin content list -->
		<table class="table table-striped table-bordered table-hover "
			id="CustmoersTable">
			<thead id="reviewsTable">
				<tr class="cubox">
					<th class="col-sm-10"><s:message code="label.entity.details" text="Name"/></th>
					<th class="col-sm-2"><s:message code="label.product.reviews.rating" text="Rating"/></th>
				</tr>
			</thead>
			<tbody id="reviewsTbody">
			</tbody>
		</table>
		<!-- end content list -->
	</div><!-- end middle main content -->
</div>
<jsp:include page="/common/delForm.jsp"/>				


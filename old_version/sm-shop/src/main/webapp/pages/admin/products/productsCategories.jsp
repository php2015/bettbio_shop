<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>
<c:set value="${product.id}" var="productId" scope="request"/>
<c:url var="addCategory" value="/admin/products/addProductToCategories.html" />
<style>
.row {
	margin: 15px 0px;
}
</style>
<script>
var productId = '${product.id}';
var pagingUrl = '<c:url value="/admin/product-categories/paging.html?productId=${product.id}"/>';
var removesUrl = '<c:url value="/admin/product-categories/remove.html?productId=${product.id}"/>';
$(function(){
	loadDataList();
});
function loadDataList() {
	$("#showloading").showLoading();
	$("#p-title").hide();
	$.post(pagingUrl, {findname: $("#findbyname").val()}, function(result){
		if(result.response.status == 0) {//RESPONSE_STATUS_SUCCESS
			var list = result.response.data;
			$("#categoriesTbody").empty();
			$(list).each(function(index){
				var tbBody="";
				tbBody += '<tr id="result_name_'+this.categoryId+'" class="everyTR">';
				tbBody += '<td>'+this.categoryId+'</td>';
				tbBody += '<td>'+this.name+'</td>';
		        tbBody += '<td>'+'<a href="javascript:void(0);" onclick="removefun('+this.categoryId+')"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a></td>';
		        tbBody += '</tr>';
				$("#categoriesTbody").append(tbBody);
			});
		} else {
			//loading data unsuccessfully
			$("#p-title").html(result.response.statusMessage).addClass("alert alert-danger").attr("role", "alert");
		}
		$("#showloading").hideLoading();
		$("#p-title").show();
	});
}
function removefun(categoryId) {
	
	$("#confirmHref").unbind("click").bind("click",function(){
		$.post(removesUrl, {categoryId: categoryId}, function(result){
			if(result.response.status == 9999) {//RESPONSE_OPERATION_COMPLETED
				loadDataList();
			} else {
				$("#p-title").empty().html(result.response.statusMessage).addClass("alert alert-danger").attr("role", "alert");
			}
		});
	});
	$('#delcfmModel').modal('show');
}

</script>
<jsp:include page="/common/adminTabs.jsp" />
<div class ="row">
	<!-- begin left menu content -->
	<div style="float:left;width:15%;padding-right:5px;">
		<jsp:include page="/pages/admin/products/product-menu.jsp" >
			<jsp:param name="active" value="productToCategories" />
		</jsp:include>
	</div><!-- end left menu content -->
	<div id="showloading"></div>	
	<!-- begin middle main content -->
	<div style="float:left;width:85%;" id="p-title">
		<h3><s:message code="label.product.category.association" text="Associate to categories" /></h3> 
		
		<form:form method="POST" enctype="multipart/form-data" commandName="product" action="${addCategory}" cssClass="form-horizontal">
			<form:errors path="*" cssClass="alert alert-danger" element="div" />
			<div id="store.success" class="alert alert-success"	style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>">
					<s:message code="message.success" text="Request successfull" />
			</div>	
			
			<div class="row">
				<div class="col-sm-6">
					<label><s:message code="label.productedit.categoryname" text="Category"/></label>
                    <form:select path="id" cssClass="form-control">
	  					<form:options items="${categories}" itemValue="id" itemLabel="descriptions[0].name"/>
       				</form:select>
                    <span class="help-inline"><form:errors path="id" cssClass="error" /></span>
				</div>
				<div class="col-sm-6">
				</div>
			</div>
			<div class="row">
				<div class="pull-right" style="padding-right:60px">
					<button type="submit" class="btn btn-primary">
						<s:message code="button.label.submit" text="Submit" />
					</button>
				</div>
			</div>
			<input type="hidden" name="productId" value="${product.id}">
		</form:form>
		<br />
		
		<table class="table table-striped table-bordered table-hover "
			id="CustmoersTable">
			<thead id="categoriesTable">
				<tr class="cubox">
					<th><s:message code="label.entity.id" text="Id" /></th>
					<th><s:message code="label.entity.name" text="Name" /></th>
					<th><s:message code="label.generic.remove" text="Del" /></th>
				</tr>
			</thead>
			<tbody id="categoriesTbody">
			</tbody>
		</table>
		
	</div><!-- end middle main content -->
</div>
<jsp:include page="/common/delForm.jsp"/>

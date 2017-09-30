<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>
<c:set value="${product.id}" var="productId" scope="request"/>
<c:url var="addKeyword" value="/admin/products/product/addKeyword.html" />
<style>
.row {
	margin: 15px 0px;
}
</style>
<script>
var productId = '${product.id}';
var pagingUrl = '<c:url value="/admin/products/product/keywords/paging.html?id=${product.id}"/>';
var removesUrl = '<c:url value="/admin/products/product/removeKeyword.html?id=${product.id}"/>';
$(function(){
	loadDataList();
});
function loadDataList() {
	$("#showloading").showLoading();
	$("#p-title").hide();
	$.post(pagingUrl, {findname: $("#findbyname").val()}, function(result){
		if(result.response.status == 0) {//RESPONSE_STATUS_SUCCESS
			var list = result.response.data;
			$("#keywordsTbody").empty();
			$(list).each(function(index){
				var tbBody="";
				tbBody += '<tr id="result_name_'+this.categoryId+'" class="everyTR">';
				tbBody += '<td>'+this.language+'</td>';
				tbBody += '<td>'+this.keyword+'</td>';
		        tbBody += '<td>'+'<a href="javascript:void(0);" onclick="removefun(\''+this.code+'\')"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a></td>';
		        tbBody += '</tr>';
				$("#keywordsTbody").append(tbBody);
			});
		} else {
			//loading data unsuccessfully
			$("#p-title").html(result.response.statusMessage).addClass("alert alert-danger").attr("role", "alert");
		}
		$("#showloading").hideLoading();
		$("#p-title").show();
	});
}
function removefun(code) {
	$("#confirmHref").unbind("click").bind("click",function(){
		$.post(removesUrl, {code: code}, function(result){
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
			<jsp:param name="active" value="keywords" />
		</jsp:include>
	</div><!-- end left menu content -->
	<div id="showloading"></div>	
	<!-- begin middle main content -->
	<div style="float:left;width:85%;" id="p-title">
		<h3><s:message code="label.product.searchkeywords" text="Search keywords" /></h3> 
		<br/>
		<strong><c:out value="${product.sku}"/></strong>			
		<br/>
		
		<form:form method="POST" enctype="multipart/form-data" commandName="productKeyword" action="${addKeyword}" cssClass="form-horizontal">
			<form:errors path="*" cssClass="alert alert-danger" element="div" />
			<div id="store.success" class="alert alert-success"	style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>">
				<s:message code="message.success" text="Request successfull" />
			</div>
			
			<div class="row">
				<div class="col-sm-3">
					<label><s:message code="label.generic.language" text="Language"/></label>
					<form:select path="languageCode" cssClass="form-control">
	  					<form:options items="${store.languages}" itemValue="code" itemLabel="code"/>
       				</form:select>
                    <span class="help-inline"><form:errors path="languageCode" cssClass="error" /></span>
				</div>
				<div class="col-sm-9">
				</div>
			</div>
			
			<div class="row">
				<div class="col-sm-6">
					<label><s:message code="label.generic.keyword" text="Keyword"/></label>
					<form:input id="keyword" cssClass="highlight form-control" path="keyword"/>
                    <span class="help-inline"><form:errors path="keyword" cssClass="error" /></span>
				</div>
				<div class="col-sm-6">
				</div>
			</div>
			
			<div class="row">
				<div class="pull-right" style="padding-right:60px">
					<button type="submit" class="btn btn-success btn-lg">
						<s:message code="button.label.submit" text="Submit" />
					</button>
				</div>
			</div>
			<input type="hidden" name="productId" value="${product.id}">
		</form:form>
		
		<br/>
		<table class="table table-striped table-bordered table-hover "
			id="CustmoersTable">
			<thead id="keywordsTable">
				<tr class="cubox">
					<th><s:message code="label.generic.language" text="Language"/></th>
					<th><s:message code="label.generic.keyword" text="Keyword"/></th>
					<th><s:message code="label.generic.remove" text="Del" /></th>
				</tr>
			</thead>
			<tbody id="keywordsTbody">
			</tbody>
		</table>
	</div><!-- end middle main content -->
</div>
<jsp:include page="/common/delForm.jsp"/>	

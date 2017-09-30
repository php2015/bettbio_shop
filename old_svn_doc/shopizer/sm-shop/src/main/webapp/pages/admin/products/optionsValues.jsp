<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>				
<script src="<c:url value="/resources/js/selectcheckbox.js" />"></script> 
<script src="<c:url value="/resources/js/findCommon.js" />"></script> 		
<script type="text/javascript">
$(function () {
	initCheckbox();
	$('#findName').attr('placeholder','<s:message code="label.entity.name" text="hodle"/>');
});
var count=0;
var page=1;
var findholder ='<s:message	code="label.entity.name" text="holde" />';
</script>


<div class="table" id="showloading">

 					<jsp:include page="/common/adminTabs.jsp" />
  					<jsp:include page="/common/findControls.jsp"/>
  	<c:if test="${not empty values && not empty values.values}">
		<div id="shop">
			  
			<!-- HISTORY TABLE -->
			<table id ="storelist" class="table table table-striped table-bordered table-hover">
				<!-- table head -->
				<thead>
					<tr >
						<th><input type="checkbox" id="selectall"> </th>
						<th ><s:message code="label.entity.name" text="Name"/></th>
						<th ><s:message code="label.product.image" text="Image"/></th>
						<th><s:message code="label.generic.remove" text="Del"/></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${values.values}" var="option" varStatus="usersStatus">
						<script type="text/javascript">
						count++;
						</script>
						<tr id="result_name_${option.id}" class="everyTR">
							<td> <input type="checkbox" class="everycheckbox" id="${option.id}"></td>
							<td ><a href="<c:url value="/admin/options/editOption.html?id=" />${option.id}">${option.name}</a></td>
								<td >${option.img}</td>
								<td ><a href="javascript:void(0);" onclick="delEntity(${option.id})" ><s:message code="label.generic.remove" text="Del" /></a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<c:set var="paginationData" value="${paginationData}" scope="request"/>
			<jsp:include page="/common/paginationFind.jsp"/>
		</div>
	</c:if>
</div>
<c:set var="findAction" value="${pageContext.request.contextPath}/admin/optionvalues/list.html" scope="request"/>
  <jsp:include page="/common/delForm.jsp"/>   
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
	$('#findName').attr('placeholder','<s:message code="label.store.find.holde" text="hodle"/>');
});
var count=0;
var page=1;
var findholder ='<s:message	code="label.store.find.holde" text="holde" />';
</script>

<div class="table" id="showloading">

 					<jsp:include page="/common/adminTabs.jsp" />
  		<jsp:include page="/common/findControls.jsp"/>			
  	<c:if test="${not empty stores && not empty stores.stores}">		<div id="shop">
			  
			<!-- HISTORY TABLE -->
			<table id ="storelist" class="table table table-striped table-bordered table-hover">
				<!-- table head -->
				<thead>
					<tr >
						<th><input type="checkbox" id="selectall"> </th>
						<th ><s:message code="label.entity.code" text="Code"/></th>
						<th ><s:message code="label.entity.name" text="Name"/></th>
						<th ><s:message code="label.generic.email" text="Email"/></th>
						<th><s:message code="label.generic.remove" text="Del"/></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${stores.stores}" var="store" varStatus="storeStatus">
						<script type="text/javascript">
						count++;
						</script>
						<tr id="result_name_${store.id}" class="everyTR">
							<td> <input type="checkbox" class="everycheckbox" id="${store.id}"></td>
							<td ><a href="<c:url value="/admin/store/editStore.html?id=" />${store.id}">${store.code}</a></td>
								<td >${store.storename}</td>
								<td >${store.storeEmailAddress}</td>
								<td ><a href="javascript:void(0);" onclick="delEntity(${store.id})" ><s:message code="label.generic.remove" text="Del" /></a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<c:set var="paginationData" value="${paginationData}" scope="request"/>
			<jsp:include page="/common/paginationFind.jsp"/>
		</div>
	</c:if>
</div>
	
<c:set var="findAction" value="${pageContext.request.contextPath}/admin/store/list.html" scope="request"/>
  <jsp:include page="/common/delForm.jsp"/>   
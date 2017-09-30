<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>				
<script src="<c:url value="/resources/js/selectcheckbox.js" />"></script> 
<script src="<c:url value="/resources/js/findCommon.js" />"></script> 
<script src="<c:url value="/resources/js/commonActive.js" />"></script> 		
<script type="text/javascript">
$(function () {
	initCheckbox();
	$('#findName').attr('placeholder','<s:message code="label.user.find.holde" text="hodle"/>');
	initStatus(${criteria.avaiable});
});
var count=0;
var page=1;
var findholder ='<s:message	code="label.user.find.holde" text="holde" />';
</script>


<div class="table" id="showloading">

 		<jsp:include page="/common/adminTabs.jsp" />
 		<jsp:include page="/common/findControls.jsp"/> 
		<div id="shop">
			 
			<!-- HISTORY TABLE -->
			<table id ="storelist" class="table table table-striped table-bordered table-hover">
				<!-- table head -->
				<thead>
					<tr >
						<th><input type="checkbox" id="selectall"> </th>
						<th ><s:message code="label.generic.username" text="Name"/></th>
						<th ><s:message code="label.generic.email" text="Email"/></th>
						<th ><s:message code="label.entity.active" text="Active"/><div class="pull-right"><input type="checkbox" id="allAv"></th>
						<th ><s:message code="label.storename" text="StoreName"/></th>
						<th><s:message code="label.generic.remove" text="Del"/></th>
					</tr>
				</thead>
				<c:if test="${not empty users && not empty users.users}">
				<tbody>
					<c:forEach items="${users.users}" var="user" varStatus="usersStatus">
						<script type="text/javascript">
						count++;
						</script>
						<tr id="result_name_${user.userid}" class="everyTR">
							<td> <input type="checkbox" class="everycheckbox" id="${user.userid}"></td>
							<td ><a href="<c:url value="/admin/users/displayStoreUser.html?id=" />${user.userid}">${user.userName}</a></td>
								<td >${user.userEmail}</td>
								<td ><span id="av_${user.userid}">${user.active}</span><div class="pull-right"><input type="checkbox" onclick="changeav(this)" id="${user.userid}" <c:if test="${user.active == true}"> checked="checked"</c:if>></div></td>
								<td >${user.storeName}</td>
								<td ><a href="javascript:void(0);" onclick="delEntity(${user.userid})" ><s:message code="label.generic.remove" text="Del" /></a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<c:set var="paginationData" value="${paginationData}" scope="request"/>
			<jsp:include page="/common/paginationFind.jsp"/>
		</div>
	</c:if>
</div>
	
<c:set var="findAction" value="${pageContext.request.contextPath}/admin/users/list.html" scope="request"/>
<jsp:include page="/common/delForm.jsp"/>   
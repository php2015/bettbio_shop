<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<%@ page session="false" %>				
<script src="<c:url value="/resources/js/selectcheckbox.js" />"></script> 
<script src="<c:url value="/resources/js/findCommon.js" />"></script> 	
<script src="<c:url value="/resources/js/commonActive.js" />"></script> 	
<script type="text/javascript">
$(function () {
	initCheckbox();
	$('#findName').attr('placeholder','<s:message code="label.category.find.holde" text="hodle"/>');
	initStatus(${criteria.avaiable});
});
var count=0;
var page=1;
var findholder ='<s:message	code="label.category.find.holde" text="holde" />';
</script>


<div class="table" id="showloading">

 					<jsp:include page="/common/adminTabs.jsp" />
 					<jsp:include page="/common/findControls.jsp"/>
		<div id="shop">
			
			<table id ="storelist" class="table table table-striped table-bordered table-hover">
				<!-- table head -->
				<thead>
					<tr >
						<th><input type="checkbox" id="selectall"> </th>
						<th ><s:message code="label.entity.name" text="Name"/></th>
						<th ><s:message code="label.entity.code" text="Code"/></th>
						<th ><s:message code="label.entity.visible" text="Visible"/>
							<sec:authorize access="hasRole('ADMIN') and fullyAuthenticated">
								<div class="pull-right"><input type="checkbox" id="allAv"></div>
							</sec:authorize>
						</th>
						<sec:authorize access="hasRole('ADMIN') and fullyAuthenticated">
						<th><s:message code="label.generic.remove" text="Del"/></th>
						</sec:authorize>
					</tr>
				</thead>
				<c:if test="${not empty cats && not empty cats.cats}">
				<tbody>
					<c:forEach items="${cats.cats}" var="cat" varStatus="usersStatus">
						<script type="text/javascript">
						count++;
						</script>
						<tr id="result_name_${cat.id}" class="everyTR">
							<td> <input type="checkbox" class="everycheckbox" id="${cat.id}"></td>
							<td>
								<sec:authorize access="hasRole('ADMIN') and fullyAuthenticated">
									<a href="<c:url value="/admin/categories/editCategory.html?id=" />${cat.id}">
								</sec:authorize>
								${cat.name}
								<sec:authorize access="hasRole('ADMIN') and fullyAuthenticated">
									</a>
								</sec:authorize>
							</td>
							<td >${cat.code}</td>
							<td ><span id="av_${cat.id}">${cat.visible}</span>
								<sec:authorize access="hasRole('ADMIN') and fullyAuthenticated">
									<div class="pull-right">
											<input type="checkbox" onclick="changeav(this)" id="${cat.id}">
									</div>
								</sec:authorize>
							</td>								
							<sec:authorize access="hasRole('ADMIN') and fullyAuthenticated">
								<td ><a  href="javascript:void(0);" onclick="delEntity(${cat.id})" ><s:message code="label.generic.remove" text="Del" /></a></td>
							</sec:authorize>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<c:set var="paginationData" value="${paginationData}" scope="request"/>
			<jsp:include page="/common/paginationFind.jsp"/>
		</div>
	</c:if>
</div>
	
<c:set var="findAction" value="${pageContext.request.contextPath}/admin/categories/list.html" scope="request"/>
  <jsp:include page="/common/delForm.jsp"/>   
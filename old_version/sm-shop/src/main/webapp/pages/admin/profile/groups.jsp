<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>				
				



<div class="tabbable">

 					<jsp:include page="/common/adminTabs.jsp" />
  					<br>
  						<div id="shop">
  							<table id ="storelist" class="table table table-striped table-bordered table-hover">
								<!-- table head -->
								<thead>
									<tr >
									
										<th ><s:message code="label.group.groupId" text="ID"/></th>
										<th ><s:message code="label.entity.name" text="Name"/></th>
										<th ><s:message code="label.permissions.title" text="Permission"/></th>
									</tr>
								</thead>
								<c:if test="${not empty groups }">
									<tbody>
										<c:forEach items="${groups}" var="group" varStatus="usersStatus">
											
											<tr >
													<td >${group.id}</a></td>
													<td >${group.groupName}</td>
													<td ><s:message code="security.group.description.${group.groupName}" text="Permission"/></td>
											</tr>
										</c:forEach>
									</tbody>
								</c:if>
							</table>
  						</div>

				</div>		      			     
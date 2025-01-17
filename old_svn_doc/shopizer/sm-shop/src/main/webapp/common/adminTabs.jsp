<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<meta http-equiv="content-type" content="text/html;charset=utf-8">	
<%@ page session="false" %>
<style>
.row {
	margin: 15px 0px;
}
</style>
<c:if test="${fn:length(currentMenu.menus)>0}">
  					 <div class="padding-left row" >
     					<ul class="nav nav-tabs padding-left">
							<c:forEach items="${currentMenu.menus}" var="menu" varStatus="z">
	  							<sec:authorize access="hasRole('${menu.role}') and fullyAuthenticated">
	  							<c:choose>
	  							    <c:when test="${fn:length(menu.menus)==0}">
	  									<li id="${menu.code}-tab" <c:if test="${activeMenus[menu.code]!=null}"> class="active"</c:if>><a href="#" id="${menu.code}-link" data-toggle="tab"><s:message code="menu.${menu.code}" text="${menu.code}"/></a></li>
	  							    </c:when>
	  							    <c:otherwise>
 											<li class="dropdown <c:if test="${activeMenus[menu.code]!=null}"> active</c:if>" style="z-index:500000;position:relative"> 
  										<a href="#" class="dropdown-toggle" data-toggle="dropdown"><s:message code="menu.${menu.code}" text="${menu.code}"/><b class="caret"></b></a>
  										<ul class="dropdown-menu"> 
  											<c:forEach items="${menu.menus}" var="submenu">
  												<sec:authorize access="hasRole('${submenu.role}') and fullyAuthenticated">
  													<li><a href="#" id="${submenu.code}-link" data-toggle="tab"><s:message code="menu.${submenu.code}" text="${submenu.code}"/></a></li>
  												</sec:authorize>
  											</c:forEach>
  										</ul> 
  									</li>
	  							    </c:otherwise>
	  							</c:choose>
	  							</sec:authorize>
	  						</c:forEach>
	  						</ul>
  						</div>
</c:if>  					
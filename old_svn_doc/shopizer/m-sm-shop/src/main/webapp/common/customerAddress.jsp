<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>



						
							<div class="col-xs-12" >
								<c:if test="${not empty requestScope.address.company}">
									${requestScope.address.company}	
								</c:if>
							</div>
							<div class="col-xs-6" >
								<c:if test="${not empty requestScope.address.name}"> ${requestScope.address.name} </c:if>	
							</div>
							<div class="col-xs-6" >	
								<c:if test="${not empty requestScope.address.telephone}"> ${requestScope.address.telephone} </c:if>
							</div>					
							<div class="col-xs-12" >
								<c:if test="${not empty requestScope.address.streetAdress}">${requestScope.address.streetAdress} </c:if>
							</div>
							<div class="col-xs-3" >
								<c:if test="${not empty requestScope.address.city}"> ${requestScope.address.city}</c:if>
							</div>
							<div class="col-xs-3" >
								<c:if test="${not empty requestScope.address.zone}">${requestScope.address.zone}</c:if>
							</div>
							<div class="col-xs-3" >
								<c:if test="${not empty requestScope.address.country}">${requestScope.address.country}</c:if>
							</div>
							<div class="col-xs-3" >
								<c:if test="${not empty requestScope.address.postCode}">${requestScope.address.postCode}</c:if>
							</div>
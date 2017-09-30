<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>



						
							
								<c:if test="${not empty requestScope.address.company}">	${requestScope.address.company}<br/>								</c:if>
							<c:if test="${not empty requestScope.address.name}"> ${requestScope.address.name} <br />							</c:if>							
							<c:if test="${not empty requestScope.address.telephone}"> ${requestScope.address.telephone} <br />							</c:if>
							<c:if test="${not empty requestScope.address.streetAdress}">${requestScope.address.streetAdress} <br />							</c:if>
							<c:if test="${not empty requestScope.address.city}"> ${requestScope.address.city}<br />							</c:if>
							<c:if test="${not empty requestScope.address.zone}">${requestScope.address.zone}</c:if>
							<c:if test="${not empty requestScope.address.country}">${requestScope.address.country}</c:if>
							<c:if test="${not empty requestScope.address.postCode}">${requestScope.address.postCode}</c:if>
						
					

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>



					
							
								<c:if test="${not empty requestScope.delivery.company}">	${requestScope.delivery.company}<br/>								</c:if>
							<c:if test="${not empty requestScope.delivery.companyAddress}"> ${requestScope.delivery.companyAddress} <br />							</c:if>							
							<c:if test="${not empty requestScope.delivery.companyTelephone}"> ${requestScope.delivery.companyTelephone} <br />							</c:if>
							<c:if test="${not empty requestScope.delivery.bankName}">${requestScope.delivery.bankName} <br />							</c:if>
							<c:if test="${not empty requestScope.delivery.bankAccount}"> ${requestScope.delivery.bankAccount}<br />							</c:if>
							<c:if test="${not empty requestScope.delivery.taxpayerNumber}">${requestScope.delivery.taxpayerNumber}</c:if>
					

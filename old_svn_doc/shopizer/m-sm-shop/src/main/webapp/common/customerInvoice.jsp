<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>



					<div class="col-xs-12" >
						<c:if test="${not empty requestScope.delivery.company}">	${requestScope.delivery.company}</c:if>
					</div>
					<div class="col-xs-12" >
						<c:if test="${not empty requestScope.delivery.companyAddress}"> ${requestScope.delivery.companyAddress}</c:if>	
					</div>		
					<div class="col-xs-6" >
						<c:if test="${not empty requestScope.delivery.companyTelephone}"> ${requestScope.delivery.companyTelephone}</c:if>
					</div>
					<div class="col-xs-6" >
						<c:if test="${not empty requestScope.delivery.bankName}">${requestScope.delivery.bankName}</c:if>
					</div>
					<div class="col-xs-6" >
						<c:if test="${not empty requestScope.delivery.bankAccount}"> ${requestScope.delivery.bankAccount}</c:if>
					</div>
					<div class="col-xs-6" >
						<c:if test="${not empty requestScope.delivery.taxpayerNumber}">${requestScope.delivery.taxpayerNumber}</c:if>
					</div>			
													
							
							
							
							
					

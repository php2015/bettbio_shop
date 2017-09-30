<%
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
 <script language=javascript>
 	var home='<c:url value="/shop"/>';
 	if(${fromadmin}==true){
 		home='<c:url value="/admin"/>';
 	}
          setTimeout('window.location="'+home+'"',5000)
      </script>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
  
 <%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  
 <c:set var="lang" scope="request" value="${requestScope.locale.language}"/> 
 
 
 <html xmlns="http://www.w3.org/1999/xhtml"> 
 
 
     <head>
        	 	<meta charset="utf-8">
    			<title><s:message code="message.error" text="An error occured !"/></title>
    			<meta name="viewport" content="width=device-width, initial-scale=1.0">
    			<link href="<c:url value="/resources/templates/bootstrap/css/bootstrap.min.css" />" rel="stylesheet">

 	</head>
 
 	<body>
 	 	
	<div class="container box main-padding-lr">
	
			<table>
			<tr>
				<td><img src="<c:url value="/resources/img/icon_error.png"/>" width="50"></td>
				<td><h3><s:message code="message.access.denied" text="Access denied" /></h3></td>
				<td><h3><s:message code="message.access.change" text="Access denied" /></h3></td>
				<c:choose>
						<c:when test="${fromadmin==true}">
							<td><a  href="<c:url value="/admin"/>"> <s:message	code="menu.home" text="Home" /></a></td>
						</c:when>
						<c:otherwise>
							<td><a  href="<c:url value="/shop"/>"> <s:message	code="menu.home" text="Home" /></a></td>
						</c:otherwise>
						
				</c:choose>
			</tr>

			</table>

	</div>

 	</body>
 
 </html>
 

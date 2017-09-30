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
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ page import="java.util.Calendar" %>
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
 

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<c:set var="lang" scope="request" value="${requestScope.locale.language}"/> 
 <html xmlns="http://www.w3.org/1999/xhtml"> 
     <head>
       <meta charset="utf-8">
       <meta http-equiv="X-UA-Compatible" content="IE=edge">
       <meta name="viewport" content="width=device-width, initial-scale=1">        	 		
	   <title><s:message code="label.storeadministration" text="Store administration" /></title>    			
	   <meta name="keywords" content='<s:message code="label.website.keywords" text="bettbio.com"/>'>
	   <meta name="description" content='<s:message code="label.website.descirption" text="bettbio.com"/>'>
	   <meta name="author" content="bettbio.com">
    		            <!--[if lte IE 9]>		
		 <script type="text/javascript">
				window.location.href='<c:url value="/shop/nonsupport.html"/>';
		</script>	
	<![endif]-->
    			<script src="<c:url value="/resources/js/jquery-2.0.3.min.js" />"></script> 
    			<script src="<c:url value="/resources/js/jquery.friendurl.min.js" />"></script>
                <jsp:include page="/common/adminLinks.jsp" />
 	</head>

<script src="<c:url value="/resources/js/pwstrength.js" />"></script>
<script src="<c:url value="/resources/js/shop-customer.js" />"></script>

<div class="container-fluid">
	<div class="col-sm-12 text-center">
				 <div class="col-sm-10 col-sm-offset-2">
							<p class="lead">
								<c:out value="${information}" />
							</p>
						</div>
	</div>	
</div>
<hr style="height:2px;border:none;border-top:1px solid #3498DB; ">

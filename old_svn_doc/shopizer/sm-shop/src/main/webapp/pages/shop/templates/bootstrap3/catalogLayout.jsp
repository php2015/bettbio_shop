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
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 
  
 <c:set var="lang" scope="request" value="${requestScope.locale.language}"/> 
 
 
 <html xmlns="http://www.w3.org/1999/xhtml" style="overflow:auto;">  
 
     <head>
        	 	
        	 	<meta charset="utf-8">
    			<title><c:out value="${requestScope.PAGE_INFORMATION.pageTitle}" /></title>
				<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
				<meta name="viewport" content="width=device-width, maximum-scale=1, initial-scale=1, user-scalable=0">
    			<meta name="keywords" content="<c:out value="${requestScope.PAGE_INFORMATION.pageKeywords}" />">
    			<meta name="description" content="<c:out value="${requestScope.PAGE_INFORMATION.pageDescription}" />">
    			<meta name="author" content="<c:out value="${requestScope.MERCHANT_STORE.storename}"/>">
    			<meta name="renderer" content="webkit">
 <jsp:include page="/pages/shop/templates/bootstrap3/sections/shopLinks.jsp" />
 <!--[if lte IE 8]>		
		 <script type="text/javascript">
			
				
				alert('<s:message code="label.ie.low" text="All Rights Reserved"/>');
				window.location.href='<c:url value="/shop/nonsupport.html"/>';
			
		</script>	
	<![endif]--> 
				<!-- mobile settings -->

             
              
<script>
var _hmt = _hmt || [];
(function() {
	if(isPC()==false){
		window.location.href = GetUrlRelativePath();
	}
  
})();
</script>

 	</head>
 
 	<body >
 	
	<div id="pageContainer" >
				<tiles:insertAttribute name="header" ignore="true"/>
					<tiles:insertAttribute name="navbar" ignore="true" />
				<div style="width:1200px;border: 0px solid #804040;margin-left: auto;margin-right: auto;" id="navbar">
					
					<tiles:insertAttribute name="body" ignore="true"/>
				</div>
				<tiles:insertAttribute name="footer" ignore="true"/>
	</div>
	<!-- end container -->
	   <jsp:include page="/pages/shop/templates/bootstrap3/sections/jsLinks.jsp" />
   
 	</body>
 <script>
	 var hm = document.createElement("script");
	 hm.src = "//hm.baidu.com/hm.js?30fe1a520ed07f83a8a57bfd58fd152f";
	 var s = document.getElementsByTagName("script")[0]; 
	 s.parentNode.insertBefore(hm, s);
 </script>
 </html>
 

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
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %> 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

 

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<c:set var="lang" scope="request" value="${requestScope.locale.language}"/> 
 
 <html xmlns="http://www.w3.org/1999/xhtml"> 
     <head>
        	 	<meta charset="utf-8">
    			<title><c:out value="${requestScope.PAGE_INFORMATION.pageTitle}" /></title>
    			<meta name="viewport" content="width=device-width, initial-scale=1.0">
    			<meta name="description" content="<c:out value="${requestScope.PAGE_INFORMATION.pageDescription}" />">
    			<meta name="author" content="<c:out value="${requestScope.MERCHANT_STORE.storename}"/>">
				 <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 

				<!-- mobile settings -->
				<meta name="viewport" content="width=device-width, maximum-scale=1, initial-scale=1, user-scalable=0">
                <jsp:include page="/pages/shop/templates/bootstrap3/sections/shopLinks.jsp" />
<script>
var _hmt = _hmt || [];
(function() {
	if(isPC()==true){
		window.location.href = GetUrlRelativePath();
	}
  var hm = document.createElement("script");
  hm.src = "//hm.baidu.com/hm.js?30fe1a520ed07f83a8a57bfd58fd152f";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();
</script>

 	</head>
 	<body>
	<div id="pageContainer">
				
					 	<tiles:insertAttribute name="body" ignore="true"/>
					 	<div style="padding-top: 70px;">
					<tiles:insertAttribute name="footer" ignore="true"/>
					</div>
				</div>
				
	
	<!-- end container -->
	    <jsp:include page="/pages/shop/templates/bootstrap3/sections/jsLinks.jsp" />
		<div class="modal fade" id="showReust" tabindex="-1" role="dialog"  aria-hidden="true">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="resultTitle"></h4>
		      </div>
		    </div>
		  </div>
		</div>
 	</body>
 <script>
 function alertSuccess(){
		$("#resultTitle").html('<img src="<c:url value="/resources/img/icon_success.png"/>" width="40"/>&nbsp;<strong><s:message code="message.success" text="Success" /></strong>');
		$('#showReust').modal('show');
	}
 function activeFaild(msg){
		if(msg==undefined||msg=='') {
			$("#resultTitle").html('<img src="<c:url value="/resources/img/icon_error.png"/>" width="40"/>&nbsp;<strong><s:message code="label.entity.actived.failed" text="Failed" /></strong>');
		} else {
			$("#resultTitle").html('<img src="<c:url value="/resources/img/icon_error.png"/>" width="40"/>&nbsp;<strong>'+msg+'</strong>');
		}
		$('#showReust').modal('show');
	}
 </script>
 </html>
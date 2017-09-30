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
<%@ page import="java.util.Calendar" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
 
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
		<meta name="description" content="">
		<meta name="author" content="">
		<link href="<c:url value="/resources/templates/bootstrap3/css/bootstrap.css" />" rel="stylesheet">
  	    <link href="<c:url value="/resources/templates/bootstrap3/css/essentials.css" />" rel="stylesheet">
	    
		<link href="<c:url value="/resources/css/shopizer-admin.css" />" rel="stylesheet">
		<link href="<c:url value="/resources/css/showLoading.css" />" rel="stylesheet">

		<!-- script src="<c:url value="/resources/js/bootstrap/jquery.js" />"></script-->
		<script src="<c:url value="/resources/js/jquery-2.0.3.min.js" />"></script> 
		<script src="<c:url value="/resources/js/jquery.friendurl.min.js" />"></script>
	    <script src="<c:url value="/resources/js/jquery.showLoading.min.js" />"></script>   
		
 	</head>
 	<body style="padding:0px">
 	<div class="container-fluid" id="tableShow" backdrop="true"> 
		<!-- div class="box-width"-->	
        <tiles:insertAttribute name="body"/>
		<!-- /div-->
	</div> 
	
	<br/>
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
    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="<c:url value="/resources/templates/bootstrap3/js/bootstrap.js" />"></script>
	<script>
	function activeFaild(msg){
		if(msg==undefined||msg=='') {
			$("#resultTitle").html('<img src="<c:url value="/resources/img/icon_error.png"/>" width="40"/>&nbsp;<strong><s:message code="label.entity.actived.failed" text="Failed" /></strong>');
		} else {
			$("#resultTitle").html('<img src="<c:url value="/resources/img/icon_error.png"/>" width="40"/>&nbsp;<strong>'+msg+'</strong>');
		}
		$('#showReust').modal('show');
	}
	function alertSuccess(){
		$("#resultTitle").html('<img src="<c:url value="/resources/img/icon_success.png"/>" width="40"/>&nbsp;<strong><s:message code="message.success" text="Success" /></strong>');
		$('#showReust').modal('show');
	}
	
	</script>	
 </html>
 

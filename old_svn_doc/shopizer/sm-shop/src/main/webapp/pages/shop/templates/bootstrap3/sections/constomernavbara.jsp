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
<%@ taglib uri="/WEB-INF/shopizer-functions.tld" prefix="display" %> 

<!-- TT Typeahead js files -->
<script src="<c:url value="/resources/js/jquery.maskedinput.min.js" />"></script>
<script src="<c:url value="/resources/templates/bootstrap3/js/bloodhound.min.js" />"></script>
<script src="<c:url value="/resources/templates/bootstrap3/js/typeahead.bundle.min.js" />"></script>
<link href="<c:url value="/resources/templates/bootstrap3/css/tabs.css" />" rel="stylesheet" type="text/css">
<script src="<c:url value="/resources/templates/bootstrap3/js/tabs.js" />"></script>

<c:set var="req" value="${request}" />

 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<script type="text/javascript">
  $(function(){
  
     $(window).load(function(){
       $("#cssA").css({"color":"blue","size":"16px"});
     });
 
     $("#cssA").mouseover(function(){
        $(this).css("text-decoration","underline");
     }).mouseout(function(){
        $(this).css("text-decoration","none");
     });

  });

</script>
<div class="head-navbar-left" style="height:60px;">
	<div class="container-fluid">
		<div class="pull-left" id="nav-image"><a href="/sm-shop/"><img style="height:56px;" src="<c:url value="/resources/img/biglogo.png"/>"></a></div>
		<%--<div class="pull-reight" > <s:message code="label.betbio.login" text="Search" / ></div>--%>
	  </div
	</div>
</div>
<div style="width:100%;border: 1px solid #eeeeee;"></div>


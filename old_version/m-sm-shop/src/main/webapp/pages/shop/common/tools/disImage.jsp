<%
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
%>

	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %> 
 <!--[if IE]>
		 <script src="<c:url value="/resources/js/html5shiv.min.js" />"></script> 		
	<![endif]-->
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<link href="<c:url value="/resources/css/imageviewer.css" />" rel="stylesheet" type="text/css">
<script src="<c:url value="/resources/js/jquery-1.10.2.min.js" />"></script>
<script src="<c:url value="/resources/js/tools/imageviewer.min.js" />"></script>
<div style="text-align:center;color:red">tips:单击图片，使用鼠标滚轮可以缩放和平移图片。</div>
<div id="image-gallery-3" class="cf">    <img src="<c:url value="${pid}" />" id="imgs" width="50%"  data-high-res-src="<c:url value="${pid}" />" alt="" class="gallery-items" onload="adapt();">   
   </div>            
<!-- img width="100%" src="<c:url value="${pid}" />"-->

<script type="text/javascript">
		$(function () {
            var viewer = ImageViewer();
            $('.gallery-items').click(function () {
                var imgSrc = this.src,
                    highResolutionImage = $(this).data('high-res-img');

                viewer.show(imgSrc, highResolutionImage);
            });
        });
		function adapt(){ 
			var tableWidth = $("#image-gallery-3").width(); 
			var img = new Image(); 
			img.src =$('#imgs').attr("src") ; 
			var imgWidth = img.width; 
			if(imgWidth<tableWidth){ 
			$('#imgs').attr("style","width: auto"); 
			}else{ 
			$('#imgs').attr("style","width: 100%"); 
			} 
			} 
	</script>
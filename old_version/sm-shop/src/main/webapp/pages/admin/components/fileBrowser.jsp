<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
 request.setCharacterEncoding("UTF-8");
%>
<%@ page session="false" %>
<script src="<c:url value="/resources/js/jquery-1.10.2.min.js" />"></script>
<link href="<c:url value="/resources/css/image/dropify.css" />" rel="stylesheet"></link>
<script src="<c:url value="/resources/js/image/dropify.js" />"></script>
<script src="<c:url value="/resources/js/ckeditor/ckeditor.js" />"></script>
<script src="<c:url value="/resources/js/ajaxfileupload.js" />"></script>
 <!--[if IE]>
		 <script src="<c:url value="/resources/js/html5shiv.min.js" />"></script> 		
	<![endif]-->


<script language="javascript">
	var psize=7;
	var pages=0;
	var images=null;
	$(document).ready(function(){
		$.ajax({  
			type: 'POST',
			  url: '<c:url value="/admin/content/images/paging.html" />',			 
			  success: function(is) {
				  images=is;
				  pages = parseInt(images.length/psize);
					if(images.length%psize>0) {
						pages++;
					}
					writeImage(pages);
				 }
			 	});
		 
	});
	function selectImage(img) {
		
		var image = '<c:url value="/static/${requestScope.ADMIN_STORE.code}/IMAGE/"/>' + img;
		window.opener.CKEDITOR.tools.callFunction(2, image);
		window.close();
	}

	function saveImage(file,filename){
		$.ajaxFileUpload({
			type: 'POST',
			  url: '<c:url value="/admin/content/image/upload.html" />',				  
			  fileElementId:file,
	            dataType: 'json', 
	            secureuri : false,
	            data:{pid:file,fname:filename},
			  success: function(is) {
				  if(is.response.status == -2){
					  alert("图片的格式不正常，格式必须为 jpg|jpeg|bmp|gif|png");
				  }else if(is.response.status == -1){
					  alert("图片保存失败，请再试一试");
				  }
				 }
			 	});
		}
	function removeImage(filename)	{
		$.ajax({
			type: 'POST',
			  url: '<c:url value="/admin/content/removeImage.html" />',	
	            dataType: 'json', 	           
	            data:"name="+filename,
			  success: function(is) {
				  if(is.response.status < 0){
					  alert("图片删除失败，请再试一试");
				  }
				 }
			 	});
	}
	
	function getImageName(filename){
		var index = filename.lastIndexOf(".");
		var myDate=new Date().getTime();
		return Math.random().toString(6).substr(2)+myDate.toString().substr(2)+filename.substr(index);
	}
	function writeImage(page){
		 $('#showImage').html('');
		var body='<div class="col-sm-2" style="height:200px;"><input type="file" name="upload_-1" id="upload_-1"  class="dropify-event"></div>';
		var lastsize=images.length;
		if((page*psize+psize)<images.length){
			lastsize=page*psize+psize;
		}
		for(var i=lastsize-1;i>=page*psize;i--){
		//for(var i=page*psize; i<lastsize;i++){			
			body+=' <div class="col-sm-2" style="height:200px;"><input type="file" name="upload_'+i+'" id="upload_'+i+'" class="dropify-event"  data-default-file="<c:url value="/static/${requestScope.ADMIN_STORE.code}/IMAGE/'+images[i]+'"/>"/> </div>'
		}
		if(pages>0){
			var pagenum='<nav><ul class="pagination">';
			for(var j=0;j<pages;j++){
				pagenum +='<li';
				if(j==page){
					pagenum +=' class="active"'
				}
				pagenum +='><a href="#" onclick="writeImage('+j+')">'+(j+1)+'</a></li>';
			}
			$('#paging').html(pagenum);
			
		}
		 $('#showImage').html(body);
		 init();
	}
	function init() {
		  var drEvent = $('.dropify-event').dropify();

         drEvent.on('dropify.beforeClear', function(event, element){
             return confirm("你真的要删除 \"" + element.filename + "\"文件吗?");
         });

         /**
         drEvent.on('dropify.afterClear', function(event, element){
             alert('File deleted');
         });*/
	}
</script>
<div class="container-fluid">
<div id="paging" class="col-sm-12"></div>
<div class="row">
	<div id="showImage" >
</div>

</div>
</div>
 
        

			      			     
			      			     

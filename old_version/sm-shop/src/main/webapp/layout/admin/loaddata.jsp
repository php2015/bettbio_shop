
<%
	response.setCharacterEncoding("UTF-8");
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", -1);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ page import="java.util.Calendar"%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html xmlns="http://www.w3.org/1999/xhtml">


<head>
<meta http-equiv="content-type" content="text/html;charset=utf-8">
<title><s:message code="label.storeadministration"
		text="Store administration" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">

	<style>
.buttoncss {
	background: blue;
	border: 1px #003399 solid;
	color: white;
	font: normal 12pt "Tahoma", "宋体";
	font-weight: bold; height : 30px;
	width: 80px;
	padding: 5px;
	height: 30px;
	display:inline;
}
</style>
	<script src="<c:url value="/resources/js/jquery-1.10.2.min.js" />"></script>
	<script src="<c:url value="/resources/js/jquery.friendurl.min.js" />"></script>
	<script src="<c:url value="/resources/js/json2.js" />"></script>
	<script src="<c:url value="/resources/js/adminFunctions.js" />"></script>
	<jsp:include page="/common/adminLinks.jsp" />
</head>

<body class="body">
	<div style="margin:30px auto;text-align:center;width:200px">
	<h2>&nbsp;欢迎进入秘密操作</h2>

	<div>
		<div>
			<span id="resultspan"></span>
		</div>
		<div>
			<select name="flag" id="flag">
				<option value="impproduct"/>商品导入
				<option value="updateindex"/>更新索引
				<option value="relaod"/>内存更新
				<option value="man"/>内存更新				
			</select>			
			<br />
			<input class="buttoncss" type="button" value="确定"
				onclick="dosubmit('<c:url value="/admin/loaddata/impproducts.html" />')" />
		</div>
	</div>
	</div>
	<script>
		function dosubmit(url) {
			$("#resultspan").html("");
			$.ajax({
				type : 'POST',
				dataType : "json",
				url : url,
				data : {
					flag : $("#flag").val(),
					filename: $("#filename").val()
				},
				success : function(response) {
					var msg = response.response.statusMessage ;
					var status = response.response.status;
					$("#resultspan").html("msg:" + msg + "<br/>status:" + status);
				},
				error : function(jqXHR, textStatus, errorThrown) {
					//alert(jqXHR + "-" + textStatus + "-" + errorThrown);
				}

			});

		}
	</script>
</body>

</html>


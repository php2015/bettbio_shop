<%
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<style>
.title {
	color:green;
	font-size:16px;
	font-weight:bold;
}
.brief {
	margin-top:10px;
	line-height: 25px;
}
</style>
<script>
function down(id, filename) {
	var _url = '<c:url value="/shop/software/click.html"/>?id='+id;
	//下载计数
	$.post(_url, function(result){
		var r = JSON.parse(result);
		if(r.response.status==9999) {
			var msg = r.response.statusMessage;
			setTimeout(function(){$("#clicknum"+id).html(msg)}, 2000);
		}
	});
	//下载
	var elemIF = document.createElement("iframe");  
  	elemIF.src = '<c:url value="/resources/software/"/>'+filename;//文件路径
  	elemIF.style.display = "none";  
  	document.body.appendChild(elemIF); 
}
</script>
<div class="container-fluid" style="margin:20px 30px;padding:5px 50px;">
	<ul class="list-group">
	<c:forEach items="${softwares}" var="software" varStatus="status">
	<li class='list-group-item <c:if test="${status.index%2==0 }">list-group-item-info</c:if>'>
	<div class="row" style="color:#888">
		<div class="col-md-2">
			<c:choose>
				<c:when test="${not empty software.iconname}"><img style="width:160px;" src='<c:url value="/resources/software/img/${software.iconname }"/>'/></c:when>
				<c:otherwise><img style="width:160px;" src='<c:url value="/resources/software/img/default.png"/>'/>	</c:otherwise>
			</c:choose>
		</div>
		<div class="col-md-8">
			<span class="h3" style="color:green!important;cursor:pointer" onclick="javascript:down('${software.id}','${software.filename }')">${software.name }</span><br/>
			<span><s:message code="label.generic.size" text="Size:"/>${software.size }</span><br/>
			<div class="brief">${software.brief }</div>
		</div>
		<div class="col-md-2" style="vertical-align: middle;">
			<button class="btn btn-success" type="button" onclick="down('${software.id}','${software.filename }')">
			  <s:message code="button.label.download" text="DownLoad"/> <span class="badge" id="clicknum${software.id}">${software.clicknum }</span>
			</button>
		</div>
	</div>
	</li>
	</c:forEach>
	</ul>
</div>
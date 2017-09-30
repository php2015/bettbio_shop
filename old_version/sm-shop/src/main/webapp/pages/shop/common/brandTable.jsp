<%
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
/* out.clear();
out = pageContext.pushBody(); */
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>


<style>
@charset "UTF-8";
/* CSS Document */

* {
	margin: 0;
	padding: 0;
}
/*html, body, ul, li, ol, dl, dd, dt, p, h1, h2, h3, h4, h5, h6, form, fieldset, legend, img {
	margin: 0;
	padding: 0
}*/
fieldset, img {
	border: 0
}
/*img {
	display: block
}*/

ul, ol {
	list-style: none
}
body {
	font-family: "微软雅黑";
}
.box1 {
	width: 1200px;
	/*height: 3000px;*/
	/*	background-color: #000000;*/
	
	margin: 10px auto 60px auto;
}
.box1 ul li {
	width: 192px;
	height: 76px;
	border: solid 1px #d9d9d9;
	float: left;
	margin-right: 8px;
	margin-top: 4px;
}
.box1 ul li:nth-child(n+6) {
	margin-right: 4px
}
.box1 ul li:nth-child(n) {
	margin-right: 4px
}
.box1 ul li:hover {
	border: solid 1px #5385ec;
}
.box1 ul li div {
	width: 192px;
	height: 76px;
	color: #5385ec;
	font-size: 20px;
	display: none;
	text-align: center;
	line-height: 76px;
	
}
.box1 ul li:hover div {
	width: 192px;
	height: 76px;
	color: #5385ec;
	font-size: 14px;
	display: block
}

.box1 ul li:hover img{ display:none}


</style>
<c:if test="${not empty brandBanners}">
		<div style="text-align: center; margin-top:60px; margin-bottom:50px;">
			<a href="#brand_table">
				<img width="600px;" src="<c:url value='/resources/img/ruzhu_wenan.png'/>"/>
			</a>
		</div>
</c:if>
	
	<div class="box1">
	<a name="brand_table"></a>
		<ul style="padding: 10px 0px; overflow: hidden;">

    <c:if test="${not empty brandBanners}">
		<c:forEach items="${brandBanners}" var="banner" varStatus="idx">
			<li>
				<a href="<c:url value='/shop/search/search.html'>
								<c:param name='q' value='${banner.searchKey}'/>
								<c:param name='queryType' value='1'/>
							</c:url>" title="${banner.brandName}">
						<img src="<c:url value='${banner.imgUrl}'/>" width="190" height="74" alt=""/>
						<div>${banner.brandName}</div>
				</a>
			</li>
		</c:forEach>
	</c:if>
		</ul>
	</div>


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


.content{
	width:100%;
	padding : 0 250px;
}
p.put_center{
	text-align:center;
	font-size:16px;
	padding:20px 0;
	font-weight:bolder;
}
p.put_left{
	text-indent:2em;
	text-align:left;
	font-size:16px;
	padding:8px 0;
}
p.put_normal{
	text-align:left;
	font-size:14px;
	padding:10px 0 20px 0;
}
.code{
	width:130px;
	position:absolute;
	left : 1050px;
	top : 150px;
	border : 1px solid #d9dadc;
	text-align :center;
	padding : 15px;
}
.bannerContent{
	float: left;
	height: 120px;
	width: 198px;
	margin: 30px 15px;
	text-align:center;
}
#show a{color:#607fa6}
</style>
<script>
</script>
<br>
<br>
<div>
<div class="content">
	<%-- 搜狗推广，做以下修改 --%>
	<%-- 正式产品应该是 --%>
	<%--
	<h2 style="font-size: 48px; color: red;	font-weight: bolder;">优势产品脱销，百图有秘器！</h2>
	--%>
	<%-- 现在是：--%>
	<h2 style="font-size: 48px; font-weight: bolder;">入驻品牌精选！</h2>
	<%-- 搜狗推广，做以下修改:end --%>
	<hr/>
	<p id="show" style="font-size:18px;padding-bottom:5px;padding-left:10px;">2016-12-08 
		<!-- <a href="javascript:void(0)" onclick="show();">白图生物bettbio</a> -->
	</p>
	<c:if test="${not empty brandBanners}">
		<div style="width:720px;padding:0 10px; overflow: hidden;">
			<c:forEach items="${brandBanners}" var="banner" varStatus="idx">
				<div class="bannerContent">
					<a href="<c:url value='/shop/search/search.html'>
								<c:param name='q' value='${banner.brandName}'/>
								<c:param name='queryType' value='1'/>
							</c:url>" >
						<img src="<c:url value='${banner.imgUrl}'/>" width="100%" height="100%"/>
						<span>${banner.brandName}</span>
					</a>
				</div>
			</c:forEach>
		</div>
	</c:if>
</div>
</div>
<br>



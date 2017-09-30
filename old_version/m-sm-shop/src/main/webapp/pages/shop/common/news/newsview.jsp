<%
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
 
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
	line-height: 22px;
}
.content{
}
.content p{
	color:#000;
	font-size:14px;
}
.content img {
	height: auto;
	max-width: 100%;
}
</style> 	 
<script>
$(document).ready( function() {
});		

var count=0;
</script>
<!-- begin body -->
<div class="container-fluid box main-padding-lr" style="margin-top:10px;">
	<div class="row">
		<div class="col-xs-12">
			<div class="h4" style="color:#000;margin-bottom:20px">${news.linkText }</div>
		
			<div class="row" style="line-height: 22px;">
				<div class="col-xs-6 pull-left">
					<s:message code="label.news.publishdt" text="Pulish Date:"/><fmt:formatDate value="${news.publishdt }" pattern="yyyy-MM-dd"/>
				</div>
				<div class="col-xs-6 pull-right">
					<s:message code="label.news.source" text="Pulish Source:"/>${news.type.name }
				</div>
			</div>
			<div class="row" style="line-height: 22px;">
				<div class="col-xs-12">
					<!-- JiaThis Button BEGIN -->
					<div class="pull-left"><s:message code="label.news.share" text="Share:"/>&nbsp;</div>
					<div class="jiathis_style">
						<a class="jiathis_button_qzone"></a>
						<a class="jiathis_button_weixin"></a>
						<a class="jiathis_button_tsina"></a>
						<a class="jiathis_button_tqq"></a>
						<a href="http://www.jiathis.com/share" class="jiathis jiathis_txt jtico jtico_jiathis" target="_blank">
							<s:message code="label.product.moreitems" text="More"></s:message>
						</a>
					</div>
					<script type="text/javascript" src="http://v3.jiathis.com/code/jia.js?uid=" charset="utf-8"></script>
					<!-- JiaThis Button END -->
				</div>
			</div>
			<div class="brief alert alert-info">
				${news.summary }
			</div>
			<div class="content">
			${news.content }
			</div>
		</div>
	</div>
</div><!-- end body -->          

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
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>
<script
	src="<c:url value="/resources/templates/bootstrap3/js/jquery-migrate-1.2.1.min.js" />"></script>
<script
	src="<c:url value="/resources/templates/bootstrap3/js/bundle.js" />"></script>
<script
	src="<c:url value="/resources/templates/bootstrap3/js/main.js" />"></script>
<link
	href="<c:url value="/resources/templates/bootstrap3/css/style.css" />"
	rel="stylesheet" type="text/css">
<c:set var="req" value="${request}" />
<!--[if IE]>
		 <script src="<c:url value="/resources/js/html5shiv.min.js" />"></script> 		
	<![endif]-->

<div>
	<jsp:include
		page="/pages/shop/templates/bootstrap3/sections/header.jsp" />
	<div class="container-fluid">
		<div class="row  title-margin pagefont text-center">
			<h1>
				<label style="color:#0080c0">Better Buy</label> <span
					style="color:#d79806">Better Bio</span>
			</h1>
		</div>
		<div class="row">
			<div class="col-xs-12 col-xs-offset-0 inputfont ">
				<form id="searchForm" method="post"
					action="<c:url value="/shop/search/search.html"/>">
					<div class="input-group input-group-lg">
						<input id="searchInput" name="q" type="text" class="form-control"
							style="border: 1px solid #3c78d8;"
							placeholder="<s:message code="label.search.searchQuery" text="Search query" />"
							autocomplete="off" spellcheck="false" dir="auto"
							value="<c:out value="${q}"/>" /> <span class="input-group-btn">
							<button class="btn btn-default"
								style="border: 1px solid #3c78d8;" type="button"
								onClick="doEzybioSearch()">
								<img src="<c:url value="/resources/img/search24.png"/>" />
							</button>
						</span>
					</div>
				</form>
			</div>
		</div>
		<div class="row imgfont text-center">
			<c:set var="topg" value="${requestScope.topCateGory}" />
			<c:if test="${topg != null}">
				<!-- c:forEach items="${topg}"  var="category" varStatus="status" -->
				<div class="col-xs-3">
					<a
						href='<c:url value="/shop/category/category/${topg[0].id}.html"/>'>
						<div class="">
							<span aria-hidden="true"
								class="carticon fa fa-flask fa-3x icon icon-style"
								style="background: rgb(208, 101, 3);width:50px;height:50px;"></span>
						</div>
						<div class="">
							<h5 class="nav-ezybio-font" style="color:rgb(208, 101, 3)">${topg[0].description.name}</h5>
						</div>
					</a>
				</div>
				<div class="col-xs-3">
					<a
						href='<c:url value="/shop/category/category/${topg[1].id}.html"/>'>
						<div class="">
							<span aria-hidden="true"
								class="carticon fa fa-book fa-3x icon icon-style"
								style="background: rgb(233, 147, 26);width:50px;height:50px;"></span>
						</div>
						<div class="">
							<h5 class="nav-ezybio-font" style="color: rgb(233, 147, 26)">${topg[1].description.name}</h5>
						</div>
					</a>
				</div>
				<div class="col-xs-3">
					<a
						href='<c:url value="/shop/category/category/${topg[2].id}.html"/>'>
						<div class="">
							<span aria-hidden="true"
								class="carticon fa fa-camera-retro fa-3x icon icon-style"
								style="background:#20B2AA;width:50px;height:50px;"></span>
						</div>
						<div class="">
							<h5 class="nav-ezybio-font" style="color:#20B2AA">${topg[2].description.name}</h5>
						</div>
					</a>
				</div>
				<div class="col-xs-3">
					<a
						href='<c:url value="/shop/category/category/${topg[3].id}.html"/>'>
						<div class="">
							<span aria-hidden="true"
								class="carticon fa fa-globe fa-3x icon icon-style"
								style="background:#1E90FF;width:50px;height:50px;"></span>
						</div>
						<div class="">
							<h5 class="nav-ezybio-font" style="color:#1E90FF">${topg[3].description.name}</h5>
						</div>
					</a>
				</div>
				<!-- /c:forEach-->
			</c:if>
		</div>
		<div class="row imgfont text-center">
			<div class="col-xs-3">
				<a href="javascript:void(0);"
					onclick="$('#weixincode').modal('show')">
					<div class="">
						<span aria-hidden="true"
							class="carticon fa fa-barcode fa-3x icon icon-style"
							style="background:#800080;width:50px;height:50px;"></span>
					</div>
					<div class="">
						<h5 class="nav-ezybio-font" style="color:#800080">
							<s:message code="label.content.weixin" text="Common software" /><s:message code="label.content.scan.code" text="Common software" />
						</h5>
					</div>
				</a>
			</div>
			<div class="col-xs-3">
				<a href='<c:url value="/shop/customer/registration.html" />'>
					<div class="">
						<span aria-hidden="true"
							class="carticon fa fa-users fa-3x icon icon-style"
							style="background: rgb(22, 145, 190);width:50px;height:50px;"></span>
					</div>
					<div class="">
						<h5 class="nav-ezybio-font" style="color:rgb(22, 145, 190)">
							<s:message code="label.content.registernow"
								text="Common software" />
						</h5>
					</div>
				</a>
			</div>
			<div class="col-xs-3">
				<a href='<c:url value="/shop/news/list.html"/>'>
					<div class="">
						<span aria-hidden="true" class="carticon fa fa-coffee fa-3x icon icon-style"
							style="background: #B22222;width:50px;height:50px;"></span>
					</div>
					<div class="">
						<h5 class="nav-ezybio-font" style="color:#B22222">
							<s:message code="label.common.news" text="Common software" />
						</h5>
					</div>
				</a>
			</div>
			<div class="col-xs-3">
				<a href='<c:url value="/shop/marketpoints/list.html"/>'>
					<div class="">
						<span aria-hidden="true"
							class="carticon fa fa-gift fa-3x icon icon-style"
							style="background: #FF8C00;width:50px;height:50px;"></span>
					</div>
					<div class="">
						<h5 class="nav-ezybio-font" style="color:#FF8C00">
							<s:message code="label.points.market" text="Market Points" />
						</h5>
					</div>
				</a>
			</div>
			
		</div>
	</div>
</div>
<div id="home-footer">
	<div class="row text-center" style="padding-top:10px;">
		<div class="col-sm-12 col-md-12">
			<span class="nav-ezybio-copyright nav-ezybio-font"><s:message
					code="label.bettbio.baknum" text="buyer" /></span>
		</div>
	</div>
	<div class="row text-center">
		<div>
			<span class="nav-ezybio-copyright nav-ezybio-font"> Copyright<s:message
					code="label.copyright.sigh" text="All Rights Reserved" /> 2015-2016
				www.bettbio.com <s:message code="all.rights.reserved"
					text="All Rights Reserved" /></span>
		</div>
	</div>
</div>
<div class="modal fade" id="weixincode">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-body">
      	 <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button><br>
       <img src="<c:url value="/resources/img/weixincode.jpg" />"/>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<script>
	$('#weixincode').on('show.bs.modal', function() {
    $(this).css({
        'margin-top': function () {
            return ($(this).height()/2-127 );
        }
        
});	$(this).css({
        'margin-left': function () {
            return ($(this).width()/2-116.5 );
        }
        
})
});
</script>

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
<script src="<c:url value="/resources/templates/bootstrap3/js/typeahead.bundle.min.js" />"></script>
<script src="<c:url value="/resources/js/navbar.js" />"></script>
<link href="<c:url value="/resources/templates/bootstrap3/css/tabs.css" />" rel="stylesheet" type="text/css">
<script src="<c:url value="/resources/templates/bootstrap3/js/tabs.js" />"></script>
<style>


.twitter-typeahead, .tt-hint, .tt-input, .tt-menu,.tt-dropdown-menu { width: 100%; }

</style>
<script type="text/javascript">
//Search code
$(document).ready(function() { 	
	
    getNavMenu('<c:url value="/shop/category/"/>');
	
    //post search form
	$("#searchButton").click(function(){
			var searchQuery = $('#searchField').val();
			$('#hiddenQuery').val(searchQuery);
			log('Search string : ' + searchQuery);
	        $('#hiddenSearchForm').submit();
   });  
	


	var winHei = $(window).width(); //窗口高度
	var heig = winHei - 1200;	
	var dist = heig/2;
	dist = Math.round(dist);
	$("#sort_out").css("margin-left",dist+"px");
	var searchInput = $('#searchInput');
	var selectI = $('.search-group li').eq("${qt}");
	selectI.addClass('active').siblings().removeClass('active');
	searchInput.attr("placeholder", selectI.data("prop"));
	
	$('.search-group li').click(function(){
		$(this).addClass('active').siblings().removeClass('active');
		$("#queryType").val($(this).index());
		searchInput.attr("placeholder", $(this).data("prop"));
	})

	


		/**/
			var typeSuggests = new Bloodhound({
				datumTokenizer: function(d) { return Bloodhound.tokenizers.whitespace(d.name);},
				queryTokenizer: Bloodhound.tokenizers.whitespace,
				prefetch: '/sm-shop/services/public/search/default/zh/autocomplete.html?q=cck-8',
				remote: {
					url: '/sm-shop/services/public/search/default/zh/autocomplete.html',
					replace: function(url, uriEncodedQuery) {
						var searchInput = $("#searchInput").val()
						var queryType = $("#queryType").val()
						
						if(queryType == null || queryType.length == 0){
							queryType="0"
						}
						
						return url + '?q='+searchInput+'&queryType='+  queryType
					}
				}
			});

			typeSuggests.initialize();

			$('#searchInput').typeahead({
						hint: false,
						highlight: true,
						minLength: 1
					},{
						name: 'students',
						displayKey: 'name',
						source: typeSuggests.ttAdapter(),
					}).on('typeahead:selected', function (obj, datum) {
						console.log(obj);
						console.log(datum);
						doEzybioSearch();
			});
		


});

</script>
<c:set var="req" value="${request}" />
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<style type="text/css">
#nav-image{
	padding-top: 10px;
}
.search-group{
	text-align: left;
}
.search-group ul{
	list-style: none;
}
.search-group li{
	float: left;
}
.search-group li a{
	padding: 5px 20px;
	font-size:14px;
	color: #4285f4;
	float: left;
}
.search-group li.active a{
	color: #fafafa;
	background-color: #4285f4;
}
</style>
<div id="sort_out" class="head-navbar-left" style="height:80px;width:1200px;" >
	<div class="" >
		<div class="pull-left" id="nav-image"><a href="<c:url value='/shop'/>"><img style="height:56px;margin-top:-3px !important;" src="<c:url value="/resources/img/biglogo.png" />"></a></div>
		<div class="pull-left dropdown" aria-haspopup="true" aria-expanded="true" style="padding-left:120px;height:84px;font-size:18px;padding-top:20px;"><s:message code="label.generic.all" text="All" /><s:message code="label.bettbio.category" text="Category" /><b class="caret"></b>
			<div class="dropdown-menu " id="navMenu" style="z-index:9999;margin-left:-130px;border:0px ;padding:0px;background-color:transparent !important;  box-shadow: none;top:80px;">
			    
			 </div>
		</div>
		<form id="searchForm"  method="post" action="<c:url value="/shop/search/search.html"/>" >
			<div class="input-group search-group">
				<ul>
					<li class="active" data-prop="请输入产品中（英）文名称"><a href="javascript:;">产品</a></li>
				 	<li data-prop="请输入商家名称"><a href="javascript:;">商家</a></li>
					<li data-prop="请输入产品货号"><a href="javascript:;">货号</a></li>
				</ul>
			</div>
			<div class="input-group <!-- nav-navbar-right -->" style="padding:0 100px 0 40px;">				
				<input id ="searchInput" name="q" type="text" class="form-control" 
					style="border: 1px solid #3c78d8;font-size:16px;border-radius:0px;" placeholder="请输入产品中（英）文名称" autocomplete="off" data-field="BB02"  spellcheck="false" dir="auto" value="<c:out value="${q}"/>"/>
				<input id="queryType" type="hidden" name="queryType" value="${qt }">
				 <span class="input-group-btn">
					<button class="btn btn-default"  style="border: 1px solid #3c78d8;background-color: #4285f4;border-radius:0px;height:40px;width:80px;color:#fafafa;" type="button" onClick="doEzybioSearch()"><s:message code="label.bettbio.search" text="Search" /></button>
				  </span>
			</div>	  
		</form>	
	</div>
</div>
<div style="width:100%;margin-top:10px;border: 1px solid #eeeeee;"></div>


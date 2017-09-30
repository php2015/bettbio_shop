
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
 <script src="<c:url value="/resources/js/pagingation.js" />"></script> 
 <script src="<c:url value="/resources/js/shop-search.js" />"></script>
  <script src="<c:url value="/resources/js/shop-functions.js" />"></script>


<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>



<script>
var page=1;
var pageNum=20;
var categoryIds;
var manufacturers=new Array();
var more ='<s:message code="label.product.moreitems" text="More" />';
var ok ='<s:message code="button.label.submit2" text="ok" />';
var cancel ='<s:message code="button.label.cancel" text="cancel" />';
var searchAll='<s:message code="label.search.all" text="All" />';
var home ='<s:message code="menu.home" text="Home" />';
 $(function(){		
	 dosearch();
	 //writeBread();
	 //manBindOP("qua");
 });
 
 
 <jsp:include page="/pages/shop/templates/bootstrap3/sections/shop-listing.jsp" />
	 
 
	function callBackSearchProducts(productList) {		
		$('#pageContainer').hideLoading();		
	}
	function writeptile(offset,countByPage,totalCount){
		$("#ptitle").html('<s:message code="label.entitylist.paging" arguments="'+offset+';'+countByPage+';'+totalCount+'" htmlEscape="false" argumentSeparator=";" text=""/>');
		$("#ptitle1").html('<s:message code="label.entitylist.paging" arguments="'+offset+';'+countByPage+';'+totalCount+'" htmlEscape="false" argumentSeparator=";" text=""/>');
	}
	function loadEnd() {
		$("#loadmore").html('<s:message code="label.generic.loadend" text="Loading Finish"/>');
		//$("#loadmore").unbind('click'); //点击事件关闭
	}
</script>

<div class="col-xs-12 text-center" style="padding:7px;" id="total_panel"></div>
<div class="container-fluid" id="categoryBody">
	<div class="row">
		<!-- products list -->
		<div class="list-group" id="productsList">
		</div>
	</div>
	
	<div class="clear"></div>
	<div id="loadmore" class="row text-center alert alert-info" style="font-size:16px;cursor: pointer;" onclick='searchmore()'>
	<s:message code="label.generic.loadmore" text="Loading More"/></div>
</div>

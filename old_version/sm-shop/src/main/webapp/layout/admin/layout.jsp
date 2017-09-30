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
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ page import="java.util.Calendar" %>
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
 

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 
<c:set var="lang" scope="request" value="${requestScope.locale.language}"/> 
 <html xmlns="http://www.w3.org/1999/xhtml"> 
     <head>
       <meta charset="utf-8">
       <meta http-equiv="X-UA-Compatible" content="IE=edge">      
       <meta name="viewport" content="width=device-width, initial-scale=1">        	 		
	   <title><s:message code="label.storeadministration" text="Store administration" /></title>    			
	   <meta name="keywords" content='<s:message code="label.website.keywords" text="bettbio.com"/>'>
	   <meta name="description" content='<s:message code="label.website.descirption" text="bettbio.com"/>'>
	   <meta name="author" content="bettbio.com">
	   <meta name="renderer" content="webkit">
    			<script src="<c:url value="/resources/js/jquery-2.0.3.min.js" />"></script> 
    			<script src="<c:url value="/resources/js/jquery.friendurl.min.js" />"></script>
    			<script src="<c:url value="/resources/js/products.js" />"></script> 
                <jsp:include page="/common/adminLinks.jsp" />
 	</head>
 		<div class="container-fluid table" id="showloading"> 
				<tiles:insertAttribute name="body"/>
	</div> 
	<div class="container-fluid">

		<div class="navbar navbar-inverse navbar-fixed-top" style="padding-right:20px">

  			<!-- div class="pull-left"><a class="brand" href="#"><img src="<c:url value="/resources/img/shopizer_small.jpg" />"/></a></div-->
  			<ul class="nav navbar-nav navbar-right" >
				
				<c:forEach items="${requestScope.MENULIST}" var="menu">
					  			<sec:authorize access="hasRole('${menu.role}') and fullyAuthenticated">
					  			<li <c:if test="${activeMenus[menu.code]!=null}"> class="active"</c:if>>
									<a href="<c:url value="${menu.url}" />">
										<span class="glyphicon glyphicon-${menu.icon}" aria-hidden="true"></span>										
											<s:message code="menu.${menu.code}" text="${menu.code}"/>
									</a>
					  			</li>
					  			</sec:authorize>
					  </c:forEach>
					  <li class="dropdown">
									<a data-toggle="dropdown" class="dropdown-toggle" href="#">
										<span class="glyphicon glyphicon-user" aria-hidden="true"></span> 
										<sec:authentication property="principal.username" />
										<b class="caret"></b>
									</a>
									<ul class="dropdown-menu" style="z-index:999999">
										<li><a href="<c:url value="/admin/users/displayUser.html" />"><s:message code="label.my.profile" text="My profile" /></a></li>
										<!--<li><a href="javascript:;">TODO //Language</a></li>-->
										<li class="divider"></li>
										<li>
											<c:url value="/admin/j_spring_security_logout" var="logoutUrl"/>
											<a href="${logoutUrl}"><s:message code="button.label.logout" text="Logout" /></a>
										</li>
									</ul>
									
								</li>
				</ul>
		</div>
	</div> 
	<br/>
		<footer> 
		<hr style="height:2px;border:none;border-top:1px solid #3498DB; ">
			<sec:authorize access="hasRole('SUPERADMIN') and fullyAuthenticated">
				<div style="color:black; float:right; ">
					<label><%@include file="/layout/admin/version.jsp"%></label>
				</div>
			</sec:authorize>
 			<div class="box-width"><p>&copy; Bettbio 2015-<%=Calendar.getInstance().get(Calendar.YEAR)%></p></div> 
		</footer> 
		
<div class="modal fade" id="showReust" tabindex="-1" role="dialog"  aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="resultTitle"></h4>
      </div>
    </div>
  </div>
</div>
  
<script type="text/javascript" charset="utf-8">
	function activeFaild(msg){
		if(msg==undefined||msg=='') {
			$("#resultTitle").html('<img src="<c:url value="/resources/img/icon_error.png"/>" width="40"/>&nbsp;<strong><s:message code="label.entity.actived.failed" text="Failed" /></strong>');
		} else {
			$("#resultTitle").html('<img src="<c:url value="/resources/img/icon_error.png"/>" width="40"/>&nbsp;<strong>'+msg+'</strong>');
		}
		$('#showReust').modal('show');
	}
	function alertSuccess(){
		$("#resultTitle").html('<img src="<c:url value="/resources/img/icon_success.png"/>" width="40"/>&nbsp;<strong><s:message code="message.success" text="Success" /></strong>');
		$('#showReust').modal('show');
	}
	function alertPartSuccess(erroList, ids){
		//ids为指定删除的id集合
		var strs= new Array(); 
		var delArray = new Array();
		strs=erroList.split(",");
		//如果ids没有值，则说明是进行全部删除操作，如果有数值，则是进行所选删除操作
		if(ids!=null&&ids.length>0){
			$(ids).each(function(index, item){
				var id = item;
				var flag = false;
			 	for(var i=0;i<strs.length;i++){
			  		if(id == strs[i])flag = true;
			 	}
			 	if(flag==true){
			  		$("#result_name_"+id).attr("style", "BACKGROUND-COLOR: #cc6666");
			 	} else {
			  		delArray[delArray.length] = "result_name_"+id;
			 	}
			});
		} else {
			$("tr[class='everyTR']").each(function(index,item){
				var id = $(this).attr("id");
			 	var flag = false;
			 	for(var i=0;i<strs.length;i++){
			  		if(id == "result_name_"+strs[i])flag = true;
			 	}
			 	if(flag==true){
			  		$(this).attr("style", "BACKGROUND-COLOR: #cc6666");
			 	} else {
			  		delArray[delArray.length] = id;
			 	}
			});
		}
		for(var i=0;i<delArray.length;i++){
			$("#"+delArray[i]).remove();
		}
		  
		$("#resultTitle").html('<img src="<c:url value="/resources/img/important-icon.png"/>" width="40"/>&nbsp;<strong><s:message code="message.part.success" text="Success" /></strong>');
		$('#showReust').modal('show');
	}
</script>
    <script src="<c:url value="/resources/templates/bootstrap3/js/bootstrap.js" />"></script>
     <script>
		$(document).ready(function(){ 
			$("#catalogue-reagent-create-link").click(function() {
		         $("#producttitles").html("").attr('href','javascript:void();');
			     $("#sp_pro").html("").attr('href','javascript:void();');
				 window.location='javascript:createProduct(1)';
			});
			$("#catalogue-other-create-link").click(function() {
			 $("#producttitles").html("").attr('href','javascript:void();');
			    $("#sp_pro").html("").attr('href','javascript:void();');
				window.location='javascript:createProduct(2)';
			});
			$("#catalogue-services-create-link").click(function() {
			 $("#producttitles").html("").attr('href','javascript:void();');
			    $("#sp_pro").html("").attr('href','javascript:void();');
				window.location='javascript:createProduct(3)';
			});
			$("#catalogue-categories-list-link").click(function() {
				window.location='<c:url value="/admin/categories/list.html" />';
			});
			$("#catalogue-products-categories-link").click(function() {
				window.location='<c:url value="/admin/products/product-categories.html" />';
			});
			$("#catalogues-link").click(function() {
				window.location='<c:url value="/admin/products/products.html" />';
			});
			$("#catalogue-categories-hierarchy-link").click(function() {
				window.location='<c:url value="/admin/categories/hierarchy.html" />';
			});
			$("#catalogue-categories-create-link").click(function() {
  				window.location='<c:url value="/admin/categories/createCategory.html" />';
			});
			$("#catalogue-options-list-link").click(function() {
  				window.location='<c:url value="/admin/options/list.html" />';
			});
			$("#catalogue-options-create-link").click(function() {
  				window.location='<c:url value="/admin/options/createOption.html" />';
			});
			$("#catalogue-optionsvalues-list-link").click(function() {
  				window.location='<c:url value="/admin/optionvalues/list.html" />';
			});
			$("#catalogue-optionsvalues-create-link").click(function() {
  				window.location='<c:url value="/admin/options/createOptionValue.html" />';
			});
			$("#catalogue-featured-link").click(function() {
  				window.location='<c:url value="/admin/catalogue/featured/list.html" />';
			});
			$("#catalogue-products-custom-group-link").click(function() {
  				window.location='<c:url value="/admin/products/groups/list.html" />';
			});
			$("#manufacturer-list-link").click(function() {
  				window.location='<c:url value="/admin/catalogue/manufacturer/list.html" />';
			});
			$("#manufacturer-create-link").click(function() {
  				window.location='<c:url value="/admin/catalogue/manufacturer/create.html" />';
			});
			$("#myprofile-link").click(function() {
  				window.location='<c:url value="/admin/users/displayUser.html" />';
			});
			$("#user-link").click(function() {
  				window.location='<c:url value="/admin/users/displayUser.html" />';
			});
			$("#change-password-link").click(function() {
  				window.location='<c:url value="/admin/users/password.html" />';
			});
			$("#users-link").click(function() {
  				window.location='<c:url value="/admin/users/list.html" />';
			});
			$("#create-user-link").click(function() {
  				window.location='<c:url value="/admin/users/createUser.html" />';
			});
			$("#security-permissions-link").click(function() {
  				window.location='<c:url value="/admin/user/permissions.html" />';
			});
			$("#security-groups-link").click(function() {
  				window.location='<c:url value="/admin/user/groups.html" />';
			});
			$("#customer-list-link").click(function() {
  				window.location='<c:url value="/admin/customers/list.html" />';
			});
			$("#customer-create-link").click(function() {
  				window.location='<c:url value="/admin/customers/customer.html" />';
			});
			$("#customer-options-list-link").click(function() {
  				window.location='<c:url value="/admin/customers/options/list.html" />';
			});
			$("#customer-options-create-link").click(function() {
  				window.location='<c:url value="/admin/customers/options/create.html" />';
			});
			$("#customer-options-values-list-link").click(function() {
  				window.location='<c:url value="/admin/customers/options/values/list.html" />';
			});
			$("#customer-options-values-create-link").click(function() {
  				window.location='<c:url value="/admin/customers/options/values/create.html" />';
			});
			$("#customer-options-set-link").click(function() {
  				window.location='<c:url value="/admin/customers/optionsset/list.html" />';
			});
			$("#order-list-link").click(function() {
  				window.location='<c:url value="/admin/orders/list.html" />';
			});
			$("#storeDetails-link").click(function() {
  				window.location='<c:url value="/admin/store/store.html" />';
			});
			$("#authorization-link").click(function() {
  				window.location='<c:url value="/admin/store/authorization.html" />';
			});
			$("#agentAuthorization-link").click(function() {
  				window.location='<c:url value="/admin/store/agentAuthorization.html" />';
			});
			$("#create-store-link").click(function() {
  				window.location='<c:url value="/admin/store/storeCreate.html" />';
			});
			$("#store-list-link").click(function() {
  				window.location='<c:url value="/admin/store/list.html" />';
			});
			$("#storeBranding-link").click(function() {
  				window.location='<c:url value="/admin/store/storeBranding.html" />';
			});
			$("#storeLanding-link").click(function() {
  				window.location='<c:url value="/admin/store/storeLanding.html" />';
			});
			$("#storeImport-link").click(function() {
  				window.location='<c:url value="/admin/store/storeImport.html" />';
			});
			$("#storeMap-link").click(function() {
  				window.location='<c:url value="/admin/store/map.html" />';
			});
			$("#accounts-conf-link").click(function() {
  				window.location='<c:url value="/admin/configuration/accounts.html" />';
			});
			$("#email-conf-link").click(function() {
  				window.location='<c:url value="/admin/configuration/email.html" />';
			});
			$("#system-configurations-link").click(function() {
  				window.location='<c:url value="/admin/configuration/system.html" />';
			});
			$("#special-configurations-link").click(function() {
  				window.location='<c:url value="/admin/configuration/special.html" />';
			});
			$("#reagent-download-link").click(function() {
  				window.location='<c:url value="/resources/download/products-imp.xlsx" />';
			});
			$("#others-download-link").click(function() {
  				window.location='<c:url value="/resources/download/others-imp.xlsx" />';
			});
			$("#service-download-link").click(function() {
  				window.location='<c:url value="/resources/download/services-imp.xlsx" />';
			});			
			$("#product-import-link").click(function() {
				window.location='<c:url value="/admin/products/toUpload.html" />';
			});	
			$("#meberpoint-link").click(function() {
				window.location='<c:url value="/admin/store/memberpoint.html" />';
			});
			$("#diamond-product-link").click(function() {
				window.location='<c:url value="/admin/products/diamondproducts.html" />';
			});
			$("#price-brands-link").click(function() {
				window.location='<c:url value="/admin/price/brands.html" />';
			});
			$("#price-others-link").click(function() {
				window.location='<c:url value="/admin/price/others.html" />';
			});
		}); 
		
		function checkCode(code, id, url) {
			$.ajax({
					type: 'POST',
					dataType: "json",
					url: url,
					data: "code="+ code + "&id=" + id,
					success: function(response) { 
						var msg = response.response.statusMessage ;
						var status = response.response.status ;
						callBackCheckCode(msg,status);
					},
					error: function(jqXHR,textStatus,errorThrown) { 
					}
			});
		}

		function createProduct(type) {
			$("#new_pid").val(''); //清空产品id
			loadProductTab(type);	
			$("#newproduct").modal("show");
			$("#newproduct").on("hidden.bs.modal", function(e){
				$("#new_pid").val(''); //清空产品id
			  });
			$('#myProductTab a:first').tab("show");
			if(type==1){
				
				$("#new_details_iframe").attr("src",'<c:url value="/admin/products/createProduct.html" />');
			}else if(type==2){
				$("#new_details_iframe").attr("src",'<c:url value="/admin/instrument/createInstrument.html" />');
			}else{
				
				$("#new_details_iframe").attr("src",'<c:url value="/admin/services/createService.html" />');
			}
			
   }	
	
</script> 

	
<!-- Modal -->

<div class="modal fade" id="newproduct" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static">
  <div class="modal-dialog" role="document" style="width:95%">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">
          <span id="new_productlabel"></span>&nbsp;&nbsp;
          <a target="_blank" id="producttitles" style="color:green"></a><a  target="_blank" id="sp_pro" style="font-size:14px"></a>
        </h4>
      </div>
      <div class="modal-body">
        <jsp:include page="/pages/admin/products/newproduct.jsp" />
      </div>
    </div>
  </div>
</div>   	
 
 </html>
 

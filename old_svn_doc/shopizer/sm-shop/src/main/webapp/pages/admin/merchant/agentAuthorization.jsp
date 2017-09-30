<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
request.setCharacterEncoding("UTF-8");
%>
<%@ page session="false" %>				
<script src="<c:url value="/resources/js/selectcheckbox.js" />"></script> 
<script src="<c:url value="/resources/js/findCommon.js" />"></script> 	
<link href="<c:url value="/resources/css/image/dropify.css" />" rel="stylesheet"></link>
<script src="<c:url value="/resources/js/tools/dropify.js" />"></script>	
<script type="text/javascript">
$(function () {
	initCheckbox();
	$('#findName').attr('placeholder','');
});
var count=0;
var page=1;

var findholder ='<s:message	code="label.manufactureredit.find.holde" text="holde" />';

function addAuth() {
	/* $('#changeAddressForm input').val(""); 
	$('#submitAddress').val('<s:message code="menu.editaddress" text="Change address"/>'); 
	  */$('#addnew').modal('show');
	}
</script>

<div class="table" id="showloading">

 					<jsp:include page="/common/adminTabs.jsp" />
  					<%--<jsp:include page="/common/findControls.jsp"/> --%>
  	
  	
		<div id="shop">
			<div style="padding:8px;" id="div_p"><a  style="margin:10px;" href="javascript:void();" onclick="addAuth();">新增代理授权书</a></div>
			<input type="hidden" id="merchantId" value="${store.id}">
			  <c:if test="${not empty aList}">
			<!-- HISTORY TABLE -->
			<table id ="storelist" class="table table table-striped table-bordered table-hover">
				<!-- table head -->
				<thead>
					<tr >
						<!-- <th><input type="checkbox" id="selectall"> </th> -->
						<%-- <th ><s:message code="label.manufactureredit.manufacturername" text="Name"/></th>
						<th ><s:message code="label.manufactureredit.manufacturerurl" text="url"/></th> --%>
						
						<th>代理授权单位</th>
						<th>代理授权有效日期</th>
						<th>代理授权品牌</th>
						<th>代理授权简介</th>
						<!-- <th>图片位置</th> -->
						
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<%-- <c:forEach items="${manus.manus}" var="manu" varStatus="usersStatus">
						<script type="text/javascript">
						count++;
						</script>
						<tr id="result_name_${manu.id}" class="everyTR">
							<td> <input type="checkbox" class="everycheckbox" id="${manu.id}"></td>
							<td ><a href="<c:url value="/admin/catalogue/manufacturer/edit.html?id=" />${manu.id}">${manu.name}</a></td>
								<td >${manu.url}</td>
								<td ><a href="javascript:void(0);" onclick="delEntity(${manu.id})" ><s:message code="label.generic.remove" text="Del" /></a></td>
						</tr>
					</c:forEach> --%>
					<c:forEach items="${aList}" var="au" varStatus="usersStatus">
						<script type="text/javascript">
						count++;
						</script>
						<tr id="result_name_${au.id}" class="everyTR">
							<%-- <td> <input type="checkbox" class="everycheckbox" id="${au.id}"></td> --%>
								<td ><a href="/sm-shop/shop/authorizationDetail.html?id=${au.id}">${au.company}</a></td>
								<td >${au.startTime} 至 ${au.endTime}</td>
								<td >
									<c:forEach items="${au.manufacturer}" var="m">
									<c:forEach items="${m.descriptions}" var="d">
										${d.name }&nbsp;&nbsp;
									</c:forEach>
									</c:forEach>
								</td>
								<td >${au.introduce}</td>
								<%-- <td >${au.image}</td> --%>
								
								<td ><a href="javascript:void(0);" onclick="delAuth(${au.id})" ><s:message code="label.generic.remove" text="Del" /></a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<c:set var="paginationData" value="${paginationData}" scope="request"/>
			<%-- <jsp:include page="/common/paginationFind.jsp"/> --%>
			</c:if>
		</div>
	
</div>
	<script type="text/javascript" src="<c:url value="/resources/js/authorization.js" />"></script>
<c:set var="findAction" value="${pageContext.request.contextPath}/admin/catalogue/manufacturer/list.html" scope="request"/>
  <jsp:include page="/common/delForm.jsp"/>   
 <!--  新增授权书 -->
  <div class="modal fade" id="addnew" tabindex="-1" role="dialog" data-backdrop= 'static' aria-hidden="true" >
	<div class="modal-dialog" role="document" style="width:80%">  
    	<div class="modal-content">  
     		 <div class="modal-header"><button class="close" type="button" data-dismiss="modal">×</button>
				<h3 ><img src="<c:url value="/resources/img/important-icon.png"/>" width="40"/>&nbsp;<strong>代理授权书</strong></h3>
			</div>
			<div class="modal-body" >
				<%-- <jsp:include page="/pages/shop/common/customer/editAddress.jsp" />	 --%>

				    <jsp:include page="/pages/admin/merchant/agentAuth-model.jsp" />
		
						
			</div>
    	</div>
    </div>	 
</div>

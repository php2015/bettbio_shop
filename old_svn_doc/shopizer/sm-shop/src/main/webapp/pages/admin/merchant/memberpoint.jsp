
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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<script SRC="<c:url value="/resources/js/selectList.js" />"></script>
<script type="text/javascript" charset="utf-8">
  
 function findByStatus(status){
	$("#status").val(status)
	$('#ezybioForm').submit();
};
function doPage(page){
	 $('#page').val(page);
	$('#ezybioForm').submit();
}
function deliveryInfo(code,type){
	var url="http://api.kuaidi.com/openapi.html?id=e2c7412a9ebd75d93655b82b8c161ba6&com="+type+"&nu="+code;
	if(code==null){
		$('#deliveryInfomation').html('<s:message code="label.order.delivery.erro" text="erro" />');
		$('#deliveryInfo').modal('show');
		return;
	}
	$.ajax({  
		type: 'POST',
		  url: url,
		  success: function(defaultdata) {
			  if(defaultdata !=null){
				  if(defaultdata.success==true){
					  var info='<div class="row">'
						  $.each(defaultdata.data, function(i, p) {
							  info+='<div class="col-sm-4">'+p.time+'</div><div class="col-sm-8">'+p.context+'</div>';
						  });
						  info+='</div>'
							  $('#deliveryInfomation').html(info);
				  }else{
					  $('#deliveryInfomation').html('<s:message code="label.order.delivery.erro" text="erro" />');
				  }
			  }else{
				  $('#deliveryInfomation').html('<s:message code="label.order.delivery.erro" text="erro" />');
			  }
			  $('#deliveryInfo').modal('show');
			 } ,
		 error: function( textStatus, errorThrown) {
			 $('#deliveryInfomation').html('<s:message code="label.order.delivery.erro" text="erro" />');
			 $('#deliveryInfo').modal('show');
			 
		 }
		 	});
};
</script>
<div class="table" id="showloading">

 					<jsp:include page="/common/adminTabs.jsp" />

	<div>
		<ul class="nav nav-pills pull-right form-inline">
			<li <c:if test="${empty mpsStatus}"> class="active"</c:if>><a  href="javascript:void(0);" onclick="findByStatus('')"><s:message code="label.generic.all" text="All"/></a></li>
				<c:forEach items="${giftStatus}" var="stauts" varStatus="memberSpointStatus">
			<li <c:if test="${not empty mpsStatus && mpsStatus==stauts}">  class="active"</c:if>> <a href="javascript:void(0);" onclick="findByStatus('${stauts}')"><s:message code="label.giftorder.${stauts}" text="${stauts}"/></a></li>
				</c:forEach>
		</ul>
	</div>
	
	<div style="margin-top: 80px">
		<table class="table table-striped table-hover table-condensed " >
			<thead style="background-color: #E6E6FA">
			<tr class="cubox">
				<th width="25%" style="text-align: center;">礼品名称</th>
				<th width="10%" style="text-align: center;">礼品数量</th>
				<th width="10%" style="text-align: center;">兑换时间</th>
				<th width="10%" style="text-align: center;">收货人姓名</th>
				<th width="25%" style="text-align: center;">收货地址</th>				
				<th width="10%" style="text-align: center;">快递单号</th>
				<th width="10%" style="text-align: center;">状态</th>
			</tr>
		</thead>
		
			<c:forEach items="${emberPointList}" var="emberPoin"  >
			<tr style="text-align: center;" >
				<td  style="word-break: break-all; word-wrap:break-word;">${emberPoin.name}</td>
				<td>${emberPoin.qulity}</td>
				<td>${emberPoin.orderDate}</td>
				<td>${emberPoin.deliveryName}</td>
				<td  style="word-break: break-all; word-wrap:break-word;">${emberPoin.deliverAddress}</td>
				<td>
					<c:if test="${not empty emberPoin.deliverNumber}">
						<a href="javascript:void(0);"  onclick='deliveryInfo("${emberPoin.deliverNumber}","${emberPoin.deliverCompany}")' class="btn" >${emberPoin.deliverNumber}</a>
					</c:if>
				</td>
				<td>
					<c:choose>
						<c:when test="${emberPoin.status=='ORDERED' }" >
								<a href="javascript:void(0);" onclick="doDelivery(${emberPoin.id})"><s:message code="label.giftorder.${emberPoin.status}" text="REVOKE"/></a>
						</c:when>
						
						<c:otherwise>
							<s:message code="label.giftorder.${emberPoin.status}" text="REVOKE"/>
						</c:otherwise>
					</c:choose>
				</td>
				
			</tr>
			</c:forEach>
		</table>
	</div>
	<div style="padding-top: 20px">
	<div class="pull-left" >
		<span class="p-title-text"> <c:if
				test="${not empty emberPointList }">
				<s:message code="label.entitylist.paging"
					arguments="${(paginationData.offset)};${paginationData.countByPage};${paginationData.totalCount}"
					htmlEscape="false" argumentSeparator=";" text="" />
				<script>
					cuurentPage = ${paginationData.currentPage};
				</script>
			</c:if>
		</span>
	</div>
	<c:set var="pagesAction" value="${pageContext.request.contextPath}/admin/store/memberpoint.html" scope="request"/>
						<c:set var="paginationData" value="${paginationData}" scope="request"/>
						<jsp:include page="/common/pagination.jsp"/>
	</div>					
</div>
 	<form:form method="get" action="${pageContext.request.contextPath}/admin/store/memberpoint.html" id="ezybioForm" commandName="criteria" tyle="display:none">
 	<fieldset>
	<input type="hidden" name="page" id="page" value="1"/>
	<input type="hidden" path="status" name="status" id="status" <c:if test="${not empty criteria.status}"> value="${criteria.status}" </c:if>/>
		<!-- input type="hidden" name="splitSubOrder" id="splitSubOrder" /-->
	</fieldset>
</form:form>
<div class="modal fade" id="deliveryInfo" tabindex="-1" role="dialog"  data-backdrop= "static" aria-hidden="true">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 id="cartModalLabel"><img src="<c:url value="/resources/img/important-icon.png"/>" width="40"/>&nbsp;<strong id="modeltitle"><s:message code="label.order.delivery.info" text="Delivery"/></strong></h4>
	      </div>
	      <div class="modal-body" id="deliveryInfomation">	      		
	       						
	      </div>
	      <div class="modal-footer">	      		
				<a href="#" class="btn" data-dismiss="modal"><strong><s:message code="cart.close" text="Close"/></strong></a>
	      </div>
	    </div>
	  </div>
</div>
<div class="modal fade" id="delivery" tabindex="-1" role="dialog"  data-backdrop= "static" aria-hidden="true">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 id="cartModalLabel"><img src="<c:url value="/resources/img/important-icon.png"/>" width="40"/>&nbsp;<strong id="modeltitle"><s:message code="label.order.delivery" text="Delivery"/></strong></h4>
	      </div>
	      <div class="modal-body">	      		
	       		<div class="input-group"><span class="input-group-addon "><s:message code="label.order.delivery.no" text="No"/>:</span>
	       				<input type="text" class="form-control " id="deliveryNo" name="deliveryNo" />
						</div>
	       		<div class="input-group">
					<span class="input-group-addon "><s:message code="label.order.delivery.code" text="Code"/>:</span>
					<input type="text" class="form-control " id="searchstorename" name="searchstorename" readonly />

					<!-- form:select cssClass="form-control" items="${stores}" itemValue="id" itemLabel="storename" path="merchantStore.id"/-->

					<span class="input-group-addon dropdown-toggle" id="sns"
						data-toggle="collapse" aria-haspopup="true"
						href="#collapseExample" aria-expanded="false"
						aria-controls="collapseExample"> <span
						class="glyphicon glyphicon-th " aria-hidden="true"></span>
					</span>

				</div>
				<div class="collapse" id="collapseExample">
					<div class="well" id="storeNameList">
								</div>
							</div>
				
	      </div>
	      <div class="modal-footer">
	      		 <c:set var="delivery_url" value="${pageContext.request.contextPath}/admin/store/setdelivery.html" scope="request"/>
	       		 <a href="javascript:void(0);"  onclick='deliveryOrder("${delivery_url}")' id="deliverySubOrderSubmit" class="btn" ><strong><s:message code="button.label.submit2" text="Submit"/></strong></a>
				<a href="#" class="btn" data-dismiss="modal"><strong><s:message code="cart.close" text="Close"/></strong></a>
	      </div>
	    </div>
	  </div>
</div>

<script>
function doDelivery(oid){
	subOrderid=oid;
	$('#delivery').modal('show');
}
function deliveryOrder(_url){
	$('#delivery').modal('hide');
	//$('#orderlist').showLoading();
	$.ajax({  
		type: 'POST',
		  url: _url,
		  data:"suborderid="+ subOrderid+"&dCode="+$('#searchstorename').val()+"&dNo="+$('#deliveryNo').val(),
		  success: function(response) {
			  if(response.response.status>=1) {
				  alert("操作成功！");
				  $('#ezybioForm').submit();
				 // setStatus(response.response.statusMessage)
			  }else {
				  alert("操作失败！");
			  }
			  
			 } ,
		 error: function( textStatus, errorThrown) {
						 
			activeFaild();
		 }
		 	});
}

$(function() {	
	var url=getContextPath()+'/admin/store/delivery.html'
	getStores(url);
});
function getContextPath() {
   return "${pageContext.request.contextPath}";
}
</script>
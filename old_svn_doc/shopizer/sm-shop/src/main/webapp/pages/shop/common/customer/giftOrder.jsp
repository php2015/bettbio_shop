
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

<div class="customer-box" style="min-height:400px">
	<div>
		<ul class="nav nav-pills pull-right form-inline">
			<li <c:if test="${empty mpsStatus}"> class="active"</c:if>><a  href="javascript:void(0);" onclick="findByStatus('')"><s:message code="label.generic.all" text="All"/></a></li>
				<c:forEach items="${giftStatus}" var="stauts" varStatus="memberSpointStatus">
			<li <c:if test="${not empty mpsStatus && mpsStatus==stauts}">  class="active"</c:if>> <a href="javascript:void(0);" onclick="findByStatus('${stauts}')"><s:message code="label.giftorder.${stauts}" text="${stauts}"/></a></li>
				</c:forEach>
		</ul>
	</div>
	<div><a class="btn btn-primary" href='<c:url value="/shop/marketpoints/list.html"/>' style="font-size:20px"><s:message code="label.bettbio.buy.shop1" text="Contact" /></a></div>
	<div style="margin-top: 30px">
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
					<s:message code="label.giftorder.${emberPoin.status}" text="REVOKE"/>
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
	<c:set var="pagesAction" value="${pageContext.request.contextPath}/shop/customer/giftInfo.html" scope="request"/>
						<c:set var="paginationData" value="${paginationData}" scope="request"/>
						<jsp:include page="/common/pagination.jsp"/>
	</div>					
</div>
 	<form:form method="get" action="${pageContext.request.contextPath}/shop/customer/giftInfo.html" id="ezybioForm" commandName="criteria" tyle="display:none">
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

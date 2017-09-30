
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
	function findByStatus(status) {
		$("#status").val(status)
		$('#ezybioForm').submit();
	};
	function doPage(page) {
		$('#page').val(page);
		$('#ezybioForm').submit();
	}
	function deliveryInfo(code, type) {
		var url = "http://api.kuaidi.com/openapi.html?id=e2c7412a9ebd75d93655b82b8c161ba6&com="
				+ type + "&nu=" + code;
		if (code == null) {
			$('#deliveryInfomation')
					.html(
							'<s:message code="label.order.delivery.erro" text="erro" />');
			$('#deliveryInfo').modal('show');
			return;
		}
		$
				.ajax({
					type : 'POST',
					url : url,
					success : function(defaultdata) {
						if (defaultdata != null) {
							if (defaultdata.success == true) {
								var info = '<div class="row">'
								$.each(defaultdata.data, function(i, p) {
									info += '<div class="col-sm-4">' + p.time
											+ '</div><div class="col-sm-8">'
											+ p.context + '</div>';
								});
								info += '</div>'
								$('#deliveryInfomation').html(info);
							} else {
								$('#deliveryInfomation')
										.html(
												'<s:message code="label.order.delivery.erro" text="erro" />');
							}
						} else {
							$('#deliveryInfomation')
									.html(
											'<s:message code="label.order.delivery.erro" text="erro" />');
						}
						$('#deliveryInfo').modal('show');
					},
					error : function(textStatus, errorThrown) {
						$('#deliveryInfomation')
								.html(
										'<s:message code="label.order.delivery.erro" text="erro" />');
						$('#deliveryInfo').modal('show');

					}
				});
	};
</script>
<style type="text/css">
	.gift ul li {color: #000; list-style-type: none;}
	
</style>
<div class="customer-box" style="min-height:500px">
	<div class="pull-right">
		<a href="#"
			onClick="javascript:location.href='<c:url value="/shop/customer/dashboard.html" />';"><span
			aria-hidden="true" class="carticon fa fa-reply fa-2x icon icon-style"
			style="background:#1E90FF;"></span></a>
	</div>
	<div class="pull-left"
		style="font-size:17px;padding-top:10px;margin-left: 5%;">
		<s:message code="menu.gift-info" text="gift-info" />
	</div>
	<br> <br>
	<HR style="FILTER: alpha(opacity=100,finishopacity=0,style=3);"
		width="98%" color=#FCFCFC SIZE=3>
	<div>
		<ul class="nav nav-pills pull-right form-inline" style="line-height:20px;">
			<li <c:if test="${empty mpsStatus}"> class="active"</c:if>
				style=""><a href="javascript:void(0);"
				onclick="findByStatus('')"><s:message code="label.generic.all"
						text="All" /></a>
			</li>
			<c:forEach items="${giftStatus}" var="stauts"
				varStatus="memberSpointStatus">
				<li style=""
					<c:if test="${not empty mpsStatus && mpsStatus==stauts}"> class="active"</c:if>>
					<a href="javascript:void(0);" onclick="findByStatus('${stauts}')"><s:message
							code="label.giftorder.${stauts}" text="${stauts}" /></a>
				</li>
			</c:forEach>
		</ul>
	</div>
	<div>
		<HR style="FILTER: alpha(opacity=100,finishopacity=0,style=3);"
			width="98%" color=#FCFCFC SIZE=3>
	</div>
	<c:forEach items="${emberPointList}" var="emberPoin">
		<div style="background-color:#F8F8FF;">
			<div class="gift" style="margin-top: -10px;">
				<ul>
					<li
						style="font-size:15px;font-family:黑体;font-weight: bold;color:#1E90FF;line-height:30px">${emberPoin.name}（积分×${emberPoin.qulity}）</li>
					<li>&nbsp;&nbsp;姓名：${emberPoin.deliveryName}&nbsp;&nbsp;&nbsp;兑换时间：${emberPoin.orderDate}</li>
					<li>&nbsp;&nbsp;快递订单：<c:if
							test="${not empty emberPoin.deliverNumber}">
							<a href="javascript:void(0);"
								onclick='deliveryInfo("${emberPoin.deliverNumber}","${emberPoin.deliverCompany}")'
								class="btn">${emberPoin.deliverNumber} </a>
						</c:if>&nbsp; 状态：<s:message code="label.giftorder.${emberPoin.status}"
							text="REVOKE" />
					</li>
					<li>&nbsp;&nbsp;收货地址：${emberPoin.deliverAddress}</li>
				</ul>
			</div>
			<div>
				<HR style="FILTER: alpha(opacity=100,finishopacity=0,style=1;"
					width="98%" color=#987cb9 SIZE=3>
			</div>
		</div>
	</c:forEach>
	<HR style="FILTER: alpha(opacity=100,finishopacity=0,style=1;"
		width="98%" color=#FCFCFC SIZE=1>
	<!-- 分页 -->
	
		<div style="float:right;margin-top:40px;">
			<span class="p-title-text"> 
				<c:if
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
		<div style="float:right;margin-right:-100px;" >
				<c:set var="pagesAction"
					value="${pageContext.request.contextPath}/shop/customer/giftInfo.html"
					scope="request" />
				<c:set var="paginationData" value="${paginationData}"
					scope="request" />
				<jsp:include page="/common/pagination.jsp" />
		</div>
</div>

<form:form method="get"
	action="${pageContext.request.contextPath}/shop/customer/giftInfo.html"
	id="ezybioForm" commandName="criteria" tyle="display:none">
	<fieldset>
		<input type="hidden" name="page" id="page" value="1" /> <input
			type="hidden" path="status" name="status" id="status"
			<c:if test="${not empty criteria.status}"> value="${criteria.status}" </c:if> />
		<!-- input type="hidden" name="splitSubOrder" id="splitSubOrder" /-->
	</fieldset>
</form:form>

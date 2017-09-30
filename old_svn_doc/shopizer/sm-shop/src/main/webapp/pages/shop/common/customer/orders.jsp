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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<c:set var="ordersAction" value="${pageContext.request.contextPath}/shop/customer/orders.html"/>
<script src="<c:url value="/resources/js/orders.js" />"></script>
<script src="<c:url value="/resources/js/bootstrap/bootstrap-datepicker.js" />"></script>
<script type="text/javascript" charset="utf-8">
 var cuurentPage=1;
 var subOrderid;
 function addorder(tr,data){
		tr.id=data.id;	
		var tdname=	tr.insertCell(0);
		tdname.innerHTML =data.name;
		tdname.width= "80%";
		tr.insertCell(1).innerHTML=data.quantity;
		return tr;
	}
 function setStatus(strStatus){
	 switch(strStatus)
		{
	 	case "SHIPPED" :
	 		 $("#status_"+subOrderid).html('<s:message code="menu.order" text="Order"/><s:message code="label.entity.status" text="Status"/>:<s:message code="label.order.SHIPPED" text="Status"/>');
	 		break;
		}
	
 }
 var findholder ='<s:message	code="label.generic.find.holde" text="holde" />';
 </script>
<!--最外层div  -->
<div class="out">
<div class="account_title"><span>我的订单</span></div>
<div class="account_orders">
		<div class="order_sort">
			<span style="padding-left:34px;">所有订单</span>
			<span style="padding-left:80px;">待发货</span>
			<span style="padding-left:82px;">待收货</span>
			<span style="padding-left:82px;">待付款</span>
			<span style="padding-left:82px;">待评价</span>
			<div class="order_search">
				<input type="text" placeholder="请输入订单号" style="padding-left:15px;width:204px;height:28px;border:1px solid #d9d9d9;line-height:2;"/>
				<input type="button" class="order_search_button" value="搜索订单"/>
			</div>
		</div>
		<div class="table_head">
			<span style="padding-left:170px;">名称</span>
			<span style="padding-left:110px;">数量</span>
			<span style="padding-left:110px;">单价</span>
			<span style="padding-left:110px;">金额</span>
			<span style="padding-left:94px;">交易状态</span>
			<span style="padding-left:94px;">评价</span>
		</div>
		<div class="table_body"><div style="border:1px solid #d9d9d9;">
		<%-- 	<c:set var="customerStoreOrder" value="${pageContext.request.contextPath}/shop/customer/order.html" scope="request"/>
			<jsp:include page="/common/orderDetail.jsp"/> --%>
			 <div style="width:100%;height:38px;background-color: #d9d9d9;line-height:38px;">
				<span style="padding-left:24px;">2016-01-10</span>
				<span style="padding-left:32px;">订单号：64646464646464664</span>
				<span style="padding-left:32px;">您订单中的商品在不同库房或属不同商家，故拆分为以下订单分开配送</span>
				<span style="padding-left:32px;">订单金额：￥9999.00</span>
			</div>
			<div style="width:100%;height:36px;line-height:36px;">
				<span style="padding-left:24px;">2016-01-10</span>
				<span style="padding-left:32px;">订单号：64646464646464664</span>
				<span style="padding-left:56px;">上海中乔新舟生物科技有限公司</span>
				<a style="padding-left:16px;" href="#"><img alt="" src='<c:url value="/resources/img/redqq.png"/>'/></a>
			</div>
			<div style="width:100%;height:138px;border:1px solid #d9d9d9;font-size:14px;">
				<div style="margin-top:20px;margin-left:22px;float:left;">
					<a href="#"><img style="width:98px;height:98px;" src='<c:url value="/resources/img/prod_img.png" />'/></a>
				</div>
				<div style="width:120px;margin-top:50px;margin-left:28px;float:left;">
					<div>平滑肌细胞培养基</div>
					<div>500mL</div>
				</div>
				<div style="margin-top:60px;margin-left:64px;float:left;">2</div>
				<div style="margin-top:60px;margin-left:120px;float:left;">2222</div>
				<div style="margin-top:60px;margin-left:104px;float:left;">3102</div>
				<div style="margin-top:42px;margin-left:92px;float:left;">
					<div>已完成</div>
					<div><a href="#">订单详情</a></div>
					<div><a href="#">查看物流</a></div>
				</div>
				<div style="margin-top:60px;margin-left:72px;float:left;"><a href="#">查看评价</a></div>
			</div>
			<div style="width:100%;height:36px;line-height:36px;">
				<span style="padding-left:24px;">2016-01-10</span>
				<span style="padding-left:32px;">订单号：64646464646464664</span>
				<span style="padding-left:56px;">上海中乔新舟物流科技有限公司</span>
				<a style="padding-left:16px;" href="#"><img alt="" src='<c:url value="/resources/img/redqq.png"/>'/></a>
			</div>
			<div style="width:100%;height:138px;border:1px solid #d9d9d9;font-size:14px;">
				<div style="margin-top:20px;margin-left:22px;float:left;">
					<a href="#"><img style="width:98px;height:98px;" src='<c:url value="/resources/img/prod_img.png" />'/></a>
				</div>
				<div style="width:120px;margin-top:50px;margin-left:28px;float:left;">
					<div>平滑肌细胞培养基</div>
					<div>500mL</div>
				</div>
				<div style="margin-top:60px;margin-left:64px;float:left;">2</div>
				<div style="margin-top:60px;margin-left:120px;float:left;">2222</div>
				<div style="margin-top:60px;margin-left:104px;float:left;">3102</div>
				<div style="margin-top:42px;margin-left:92px;float:left;">
					<div>已完成</div>
					<div><a href="#">订单详情</a></div>
					<div><a href="#">查看物流</a></div>
				</div>
				<div style="margin-top:60px;margin-left:72px;float:left;"><a href="#">查看评价</a></div>
			</div>
			
			
		</div>
		<div style="width:100%;height:38px;background-color: #d9d9d9;line-height:38px;margin-top:20px;">
				<span style="padding-left:24px;">2016-01-10</span>
				<span style="padding-left:32px;">订单号：64646464646464664</span>
				<span style="padding-left:52px;">上海中乔新舟物流科技有限公司</span>
				<a style="padding-left:16px;" href="#"><img alt="" src='<c:url value="/resources/img/redqq.png"/>'/></a>
			</div>
			<div style="width:100%;height:138px;border:1px solid #d9d9d9;font-size:14px;">
				<div style="margin-top:20px;margin-left:22px;float:left;">
					<a href="#"><img style="width:98px;height:98px;" src='<c:url value="/resources/img/prod_img.png" />'/></a>
				</div>
				<div style="width:120px;margin-top:50px;margin-left:28px;float:left;">
					<div>平滑肌细胞培养基</div>
					<div>500mL</div>
				</div>
				<div style="margin-top:60px;margin-left:64px;float:left;">2</div>
				<div style="margin-top:60px;margin-left:120px;float:left;">2222</div>
				<div style="margin-top:60px;margin-left:104px;float:left;">3102</div>
				<div style="margin-top:42px;margin-left:92px;float:left;">
					<div>已完成</div>
					<div><a href="#">订单详情</a></div>
					<div><a href="#">查看物流</a></div>
				</div>
				<div style="margin-top:60px;margin-left:72px;float:left;"><a href="#">查看评价</a></div>
			</div>
			
			<div style="width:100%;height:38px;background-color: #d9d9d9;line-height:38px;margin-top:20px;">
				<span style="padding-left:24px;">2016-01-10</span>
				<span style="padding-left:32px;">订单号：64646464646464664</span>
				<span style="padding-left:52px;">上海中乔新舟物流科技有限公司</span>
				<a style="padding-left:16px;" href="#"><img alt="" src='<c:url value="/resources/img/redqq.png"/>'/></a>
			</div>
			<div style="width:100%;height:138px;border:1px solid #d9d9d9;font-size:14px;">
				<div style="margin-top:20px;margin-left:22px;float:left;">
					<a href="#"><img style="width:98px;height:98px;" src='<c:url value="/resources/img/prod_img.png" />'/></a>
				</div>
				<div style="width:120px;margin-top:50px;margin-left:28px;float:left;">
					<div>平滑肌细胞培养基</div>
					<div>500mL</div>
				</div>
				<div style="margin-top:60px;margin-left:64px;float:left;">2</div>
				<div style="margin-top:60px;margin-left:120px;float:left;">2222</div>
				<div style="margin-top:60px;margin-left:104px;float:left;">3102</div>
				<div style="margin-top:42px;margin-left:92px;float:left;">
					<div>已完成</div>
					<div><a href="#">订单详情</a></div>
					<div><a href="#">查看物流</a></div>
				</div>
				<div style="margin-top:60px;margin-left:72px;float:left;"><a href="#">查看评价</a></div>
			</div>
			<div style="width:100%;height:138px;border:1px solid #d9d9d9;font-size:14px;">
				<div style="margin-top:20px;margin-left:22px;float:left;">
					<a href="#"><img style="width:98px;height:98px;" src='<c:url value="/resources/img/prod_img.png" />'/></a>
				</div>
				<div style="width:120px;margin-top:50px;margin-left:28px;float:left;">
					<div>平滑肌细胞培养基</div>
					<div>500mL</div>
				</div>
				<div style="margin-top:60px;margin-left:64px;float:left;">2</div>
				<div style="margin-top:60px;margin-left:120px;float:left;">2222</div>
				<div style="margin-top:60px;margin-left:104px;float:left;">3102</div>
				<div style="margin-top:42px;margin-left:92px;float:left;">
					<div>已完成</div>
					<div><a href="#">订单详情</a></div>
					<div><a href="#">查看物流</a></div>
				</div>
				<div style="margin-top:60px;margin-left:72px;float:left;"><a href="#">查看评价</a></div>
			</div>
		</div>
	</div>
	</div>
			<%-- <div class="customer-box" >
					<!-- span class="box-title">
						<p class="p-title">
							<s:message
									code="menu.order-list" text="List of orders" />
							&nbsp;
							<span class="p-title-text">
							<c:if test="${not empty customerOrders && not empty customerOrders.ezybioOrder}">
							
								<s:message code="label.entitylist.paging"
							       arguments="${(paginationData.offset)};${paginationData.countByPage};${paginationData.totalCount}"
							       htmlEscape="false"
							       argumentSeparator=";" text=""/>
							
							</c:if>
						</p>
					</span-->
				        	<div class="">	
					<ul class="nav nav-pills pull-left">		
						<li class="dropdown"> 
							<c:choose><c:when test="${ empty criteria.beginDatePurchased || criteria.beginDatePurchased=='3'}">
								<a data-toggle="dropdown" class="dropdown-toggle" href="#" id="yearStatus">
								    	<s:message code="label.generic.year.three" text="thress"/><s:message code="label.order.title" text="Order"/>
								    <span class="caret"></span>
								  </a>
							</c:when>			  
							<c:otherwise>
								<a data-toggle="dropdown" class="dropdown-toggle" href="#" id="batchStatus">
								    	${criteria.beginDatePurchased}<s:message code="label.generic.year" text="Order"/><s:message code="label.generic.year.this" text="Order"/>
								    <span class="caret"></span>
								  </a>
							</c:otherwise></c:choose>	  
					 	 <ul class="dropdown-menu" aria-labelledby="yearStatus">
						    <li><a href="javascript:void(0);" onclick="findByYear('3')"><s:message code="label.generic.year.three" text="thress"/><s:message code="label.order.title" text="Order"/></a></li>
						     <c:if test="${not empty years}">
						    	<li class="divider"></li>
						    		<c:forEach items="${years}" var="year" varStatus="yaerStatus">
											<li><a href="javascript:void(0);" onclick="findByYear('${year}')">${year}<s:message code="label.order.title" text="Order"/></a></li>
									</c:forEach>
						    </c:if>
						  </ul>
						 </li>	
					</ul>  
	
					<ul class="nav nav-pills pull-right form-inline">
						<li <c:if test="${empty cstatus}"> class="active"</c:if>><a  href="javascript:void(0);" onclick="findByStatus('')"><s:message code="label.generic.all" text="All"/></a></li>
						<c:forEach items="${OrderStatus}" var="stauts" varStatus="orderStatus">
							<li <c:if test="${not empty cstatus && cstatus==stauts}"> class="active"</c:if>><a href="javascript:void(0);" onclick="findByStatus('${stauts}')"><s:message code="label.order.${stauts}" text="${stauts}"/></a></li>
						</c:forEach>
						 <li >
						     <div class="input-group" style="padding-top:3px;width:250px">      
							      <input  id="findbyname" type="text" class="form-control " placeholder=<s:message	code="label.generic.find.holde" text="holde" /> <c:if test="${not empty criteria.findName}"> value="${criteria.findName}" </c:if>>
							      <a  href="javascript:void(0);" onclick="findByName()" class="input-group-addon" ><span class="glyphicon glyphicon-search" aria-hidden="true"  ></span></a>
						    </div>
						</li>
						
					</ul>
	
				</div>	
                 <c:if test="${not empty customerOrders && not empty customerOrders.ezybioOrder}">
                        
                 	<div >

						<!-- HISTORY TABLE -->
						<table class="table table-bordered">
							<!-- table head -->
							<jsp:include page="/common/orderTitlel.jsp"/>
							<!-- /HISTORY TABLE -->
							<tbody>
							<c:set var="customerStoreOrder" value="${pageContext.request.contextPath}/shop/customer/order.html" scope="request"/>
							<jsp:include page="/common/orderDetail.jsp"/>
								
							</tbody>
						</table>
						<div class="pull-left">
						
								<span class="p-title-text">
								<c:if test="${not empty customerOrders && not empty customerOrders.ezybioOrder}">
								
									<s:message code="label.entitylist.paging"
								       arguments="${(paginationData.offset)};${paginationData.countByPage};${paginationData.totalCount}"
								       htmlEscape="false"
								       argumentSeparator=";" text=""/>
								<script>
									cuurentPage=${paginationData.currentPage};
								</script>
								</c:if>  </span>
							
						</div>
						<c:set var="pagesAction" value="${pageContext.request.contextPath}/shop/customer/orders.html" scope="request"/>
						<c:set var="paginationData" value="${paginationData}" scope="request"/>
						<jsp:include page="/common/pagination.jsp"/>

					</div>
				</c:if>
				<c:if test="${empty customerOrders || empty customerOrders.ezybioOrder}">
					<div class="container-fluid">
					<div class="row">
						<div class="col-sm-12"><h3><s:message code="message.no.items" text="No Items"/></h3></div>
						</div>
					</div>
				</c:if>
				<br><br>
	 </div>			 --%>	
				<form:form method="post" action="${pageContext.request.contextPath}/shop/customer/orders.html" id="ezybioForm" commandName="criteria" tyle="display:none">
	<fieldset>
	<input type="hidden" name="page" id="page" value="1"/>
	<input type="hidden" name="status" id="status" <c:if test="${not empty criteria.status}"> value="${criteria.status}" </c:if>/>
	<input type="hidden" name="findName" id="findName" <c:if test="${not empty criteria.findName}"> value="${criteria.findName}" </c:if>/>
	<input type="hidden" name="beginDatePurchased" id="beginDatePurchased" <c:if test="${not empty criteria.beginDatePurchased}"> value="${criteria.beginDatePurchased}" </c:if>/>
	<!-- input type="hidden" name="splitSubOrder" id="splitSubOrder" /-->
	</fieldset>
</form:form>
			  

			

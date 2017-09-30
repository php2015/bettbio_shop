
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
	$("#status").val(status);
	$('#ezybioForm').submit();
};

function doPage(page){
	 $('#page').val(page);
	$('#ezybioForm').submit();
}

</script>

<div class="customer-box" style="min-height:400px">
	<div>
		<ul class="nav nav-pills pull-right form-inline" id="ul">
			<li <c:if test="${empty mpsStatus}"> class="active"</c:if>>
			   <a  href="javascript:void(0);"  id="mp5" onclick="findByStatus('-1')">
			       <span>
			         <s:message code="label.generic.all" text="All"/>
			       </span>
			   </a>
			 </li>
			<c:forEach items="${MemberSpointStatus}" var="stauts" varStatus="memberSpointStatus">
				<li <c:if test="${not empty mpsStatus && mpsStatus==memberSpointStatus.index}">  class="active"</c:if>>
				  <a href="javascript:void(0);" id="mp${memberSpointStatus.index}" onclick="findByStatus('${memberSpointStatus.index}')">
				     <span>
				       <s:message code="label.customer.${stauts}" text="${stauts}"/>
				     </span>
				  </a>
				</li>
		   </c:forEach>
		</ul>
	</div>
	<div><a class="btn btn-primary" href='<c:url value="/shop/marketpoints/list.html"/>' style="font-size:20px"><s:message code="label.bettbio.buy.shop1" text="Contact" /></a></div>
	<div style="margin-top: 30px">
		<table class="table table-striped table-hover table-condensed " >
			<thead style="background-color: #E6E6FA">
			<tr class="cubox">
				<th width="7%" style="text-align: center;"><s:message code="label.customer.points.value" text="value"/></th>
				<th width="10%" style="text-align: center;"><s:message code="label.customer.points.type" text="type"/></th>
				<th width="7%" style="text-align: center;"><s:message code="label.customer.points.update" text="update"/></th>
				<th width="7%" style="text-align: center;"><s:message code="label.customer.points.valid" text="valid"/></th>
				<th width="10%" style="text-align: center;"><s:message code="label.customer.points.statas" text="statas"/></th>
			</tr>
		</thead>
		
			<c:forEach items="${emberPointList}" var="emberPoin"  >
			<tr style="text-align: center;" >
			<c:choose>
					<c:when test="${emberPoin.type =='EXCHANGE_SCORE'}">
						<td>-${emberPoin.value }</td>
					</c:when>
					 <c:otherwise>
					 <td>${emberPoin.value }</td>
					 </c:otherwise>
			</c:choose>
				<td>
					<c:choose>
							<c:when test="${emberPoin.type =='REGIST_SCORE'}">
								注册积分							
							</c:when>
							<c:when test="${emberPoin.type =='RATIO_BUTILE'}">
								下单积分
							</c:when>
							<c:when test="${emberPoin.type =='EXCHANGE_SCORE'}">
								兑换积分
							</c:when>
							<c:when test="${emberPoin.type =='USER_SCORE'}">
								用户推荐积分
							</c:when>
							<c:when test="${emberPoin.type =='FIRST_SCORE'}">
								首单积分
							</c:when>
							<c:when test="${emberPoin.type =='PERSONALNUMBER_NUM'}">
								客户推荐人数
							</c:when>
							<c:when test="${emberPoin.type =='PERSONALNUMBER_SCORE'}">
								完成客户推荐总数
							</c:when>
						</c:choose>
				</td>
				<td>${emberPoin.updateDate}</td>
				<td>${emberPoin.dateValid }</td>
				<td>
					<c:choose>
						<c:when test="${emberPoin.statas =='0'}">
							<s:message code="label.customer.ACCOUNT" text="REVOKE"/>							
						</c:when>
						<c:when test="${emberPoin.statas =='1'}">
							<s:message code="label.customer.NOTACCOUNT" text="REVOKE"/>
						</c:when>
						<c:when test="${emberPoin.statas =='2'}">
							<s:message code="label.customer.REVOKE" text="REVOKE"/>
						</c:when>
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
	<c:set var="pagesAction" value="${pageContext.request.contextPath}/shop/customer/memberPoint.html" scope="request"/>
						<c:set var="paginationData" value="${paginationData}" scope="request"/>
						<jsp:include page="/common/pagination.jsp"/>
	</div>					
</div>
 	<form:form method="get" action="${pageContext.request.contextPath}/shop/customer/memberPoint.html" id="ezybioForm" commandName="criteria" tyle="display:none">
 	<fieldset>
	<input type="hidden" name="page" id="page" value="1"/>
	<input type="hidden" path="status" name="status" id="status" <c:if test="${not empty criteria.status}"> value="${criteria.status}" </c:if>/>
		<!-- input type="hidden" name="splitSubOrder" id="splitSubOrder" /-->
	</fieldset>
</form:form>


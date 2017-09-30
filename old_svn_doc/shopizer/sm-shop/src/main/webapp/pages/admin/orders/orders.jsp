<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>


 <link href="<c:url value="/resources/css/bootstrap/css/datepicker.css" />" rel="stylesheet"></link>
<script src="<c:url value="/resources/js/bootstrap/bootstrap-datepicker.js" />"></script>
<script src="<c:url value="/resources/js/orders.js" />"></script>	
 <script type="text/javascript" charset="utf-8">
 	var findholder ='<s:message	code="label.generic.find.holde" text="holde" />';
 	 $(function(){
     	$('#findbyname').keyup(trimkeyup);            
     });
 	 function trimkeyup(e) {
	     /* lucene_objInput = $(this);
	     if (e.keyCode != 38 && e.keyCode != 40 && e.keyCode != 13) {
	         var im = $.trim(lucene_objInput.val());
	         lucene_objInput.val(im); 
	     } */
	     if(e.keyCode == 13){
	       findByName();
	     }
    }
    
 </script>
<br>
<div class="table">	
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
            	<div id="shop">

<!-- HISTORY TABLE -->
<table id ="orderlist" class="table table table-bordered">
	<!-- table head -->
	<jsp:include page="/common/consoleOrderTitlel.jsp"/>
	<!-- /HISTORY TABLE -->
	<tbody>
	<c:set var="customerStoreOrder" value="${pageContext.request.contextPath}/admin/orders/orderDetails.html" scope="request"/>
	<jsp:include page="/common/consoleOrderDetail.jsp"/>
		
	</tbody>
</table>
<div class="pull-left">
	<p class="p-title" >
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
	</p>	
</div>
<c:set var="pagesAction" value="${pageContext.request.contextPath}/admin/orders/list.html" scope="request"/>
<c:set var="paginationData" value="${paginationData}" scope="request"/>
<jsp:include page="/common/pagination.jsp"/>
</div>
</c:if>
  <jsp:include page="/common/orderModal.jsp" />
      				
     				      			     
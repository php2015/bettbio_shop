<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page session="false" %>
<script src="<c:url value="/resources/js/orders.js" />"></script>	
<script type="text/javascript">
 //初始化修改密码
 $(function(){
    $(window).load(function(){
       if(parseInt($("#user").val())==-1 || $("#mobile_change_flag").val()=="false"){
          $("#changepassword").modal('show');
       }
    });
 });
</script>
<br>

<input type="text" id="user" value="${user.auditSection.audit}" hidden="hidden"/>
<input type="text" id="mobile_change_flag" value="${correctMobile}" hidden="hidden"/>

<div class="main col-sm-12 col-sm-offset-0 col-md-12 col-md-offset-0 padding-bottom-admin" >
    <div class="row placeholders">
          <div class="col-xs-4 col-sm-4 placeholder btn btn-primary btn-lg text-shadow-admin height-100 "  onclick="document.location='<c:url value="/admin/orders/list.html" />';">
			 <div class="row">
			 	<div class="col-sm-4"><img src="<c:url value="/resources/img/address.png" />"></div>
			 		
			 	<div class="col-sm-8">  <h1><c:if test="${not empty customerOrders}"><c:out value="${customerOrders.total}"/></c:if></h1>
						<s:message code="label.order.titles" text="Total Orders" /></div>
			 </div>
          </div>
           <div class="col-xs-4 col-sm-4 placeholder btn btn-primary btn-lg text-shadow-admin height-100" onclick="document.location='<c:url value="/admin/orders/list.html" />';">
			 <div class="row">
			 	<div class="col-sm-4"><img  class="pull-left"src="<c:url value="/resources/img/coins.png" />"></div>
			 		
			 	<div class="col-sm-8">    <h1><c:if test="${not empty totalordermoney}"><sm:monetary value="${totalordermoney}"/></c:if></h1>
						<s:message code="label.order.totals" text="Total Money" /></div>
			 </div>
          </div>
           <div class="col-xs-4 col-sm-4 placeholder btn btn-primary btn-lg  height-100"  onclick="document.location='<c:url value="/admin/store/store.html" />';">
			  <div class="row">
			 	<div class="col-sm-4"> <img  class="pull-left"	src="<c:url value="/resources/img/custmerinfo.png" />"></div>
			 		
			 	<div class="col-sm-8 font-size-13 font-row-hieht" style="word-break: break-all;"> 
			 	            <strong><c:out value="${store.storename}"/></strong><br/>
							<c:if test="${not empty store.storecity}">
								<c:out value="${store.storecity}"/>,
							</c:if>
							<c:choose>
							<c:when test="${not empty store.zone}">
								<c:out value="${store.zone.code}"/>,
							</c:when>
							<c:otherwise>
								<c:if test="${not empty store.storestateprovince}">
									<c:out value="${store.storestateprovince}"/>,
								</c:if>
							</c:otherwise>
							</c:choose>
							<c:if test="${not empty store.storepostalcode}">
								<c:out value="${store.storepostalcode}"/>
							</c:if>
							<br/><!-- c:out value="${country.name}"/-->
							<c:if test="${not empty store.storephone}">
								<c:out value="${store.storephone}"/><br/>
							</c:if>						
							<span class="glyphicon glyphicon-user" aria-hidden="true"></span>							
							<sec:authentication property="principal.username" />	
							<span class="glyphicon glyphicon-calendar" aria-hidden="true"></span>
							<s:message code="label.profile.lastaccess" text="Last access"/>: <fmt:formatDate type="date" dateStyle="Default" value="${user.lastAccess}" /></div>
			 </div>
			 
          </div>
     </div>
</div>
<!-- div class="tabbable"-->
				<c:choose>
					 <c:when test="${not empty customerOrders && not empty customerOrders.ezybioOrder}">
					<!-- div class="cbox-title"><p></p></div-->
					<!-- HISTORY TABLE -->
					
					 <h4 class="sub-header "><s:message	code="menu.order-list-recent" text="Recent list of orders" /></h4>
					<table id ="orderlist" class="table table-striped table-bordered table-hover" >
						<!-- table head -->
						
						<!-- /HISTORY TABLE -->
						<tbody>
						<c:set var="customerStoreOrder" value="${pageContext.request.contextPath}/admin/orders/orderDetails.html" scope="request"/>
						<jsp:include page="/common/consoleOrderDetail.jsp"/>
						
							
						</tbody>
					</table>
					
				</c:when>
				<c:otherwise> 
					<img src='<c:url value="/resources/ad/c.jpg" />' style="width:100%">
				</c:otherwise>
	  		</c:choose>
<div style="font-style:italic;color:#aaa"><a href='<c:url value="/shop/software/list.html"/>' target="blank"><s:message code="label.common.software.tip" text="Common Software Download"/></a></div>
<jsp:include page="/common/orderModal.jsp" />				
<!-- /div-->
<div class="modal fade" id="changepassword" tabindex="-1" role="dialog"  data-backdrop= "static" aria-hidden="true">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			<c:if test="${user.auditSection.audit == -1}">
	        <h4 id="cartModalLabel"><img src="<c:url value="/resources/img/important-icon.png"/>" width="40"/>&nbsp;<strong id="modeltitle"><s:message code="message.user.password.change" text="Change password"/></strong></h4>
			</c:if>
			<c:if test="${correctMobile == false}">
			<h4><img src="<c:url value="/resources/img/important-icon.png"/>" width="40"/>&nbsp;<strong>您的手机号码不正确，请尽快修改！</strong></h4>
			</c:if>
	      </div>	      
	      <div class="modal-footer">
				<c:if test="${user.auditSection.audit == -1}">
	       		 <a href="${pageContext.request.contextPath}/admin/users/password.html"  class="btn" ><strong><s:message code="label.generic.changepassword" text="Change Password"/></strong></a>
				 </c:if>
				 <c:if test="${correctMobile == false}">
				<a href="${pageContext.request.contextPath}/admin/users/displayUser.html#mobile_number"  class="btn" ><strong>修改手机号码</strong></a>
				</c:if>
				<a href="#" class="btn" data-dismiss="modal"><strong><s:message code="cart.close" text="Close"/></strong></a>
	      </div>
	    </div>
	  </div>
</div>
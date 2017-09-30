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
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>


<script src="<c:url value="/resources/js/customers.js" />"></script>
<script src="<c:url value="/resources/js/selectcheckbox.js" />"></script>
<script src="<c:url value="/resources/js/bootstrap/bootstrap-modal.js" />"></script>

<script type="text/javascript" charset="utf-8">
var count=0;
$(document).ready(function(){	
	initCheckbox();
	 if(isFind()==true){
		 $('#collapseOne').collapse('show');
	 }
	 
});

function clearForm(){
	$(':input','#ezybioForm')  
	 .not(':button, :submit, :reset, :hidden')  
	 .val('')  
	 .removeAttr('checked')  
	 .removeAttr('selected');  
}
function isFind(){
	var findtype=false;
	$(".find").each(function(){
		if($(this).val() !=''){
			findtype=true;
			return false;
		} 
		});
	return findtype;
}
function setActived(code,status){
	switch(status)
	{
	case 1:
		$("#custmoer_"+code).html('<a href="javascript:void(0);" onclick="aCustmoer('+code+',&quot;active.html&quot;)"><s:message code="label.entity.reactive" text="Verify"/></a>');
		break;
	case 2:
		$("#custmoer_"+code).html('<a href="javascript:void(0);" onclick="aCustmoer('+code+',&quot;unfreeze.html&quot;)"><s:message code="label.entity.freezed" text="Unfreeze"/></a>');
		break;
	default:
		$("#custmoer_"+code).html('<a href="javascript:void(0);" onclick="aCustmoer('+code+',&quot;freeze.html&quot;)"><s:message code="label.entity.freeze" text="Freeze"/></a>');
	}	
	alertSuccess();
}

function getUserGrade(){
	return '<img alt="go to order" src="<c:url value="/resources/img/stars/star-on.png" />">';
}
function setUserGradeSubmit(custmoerID,custmoerGrade){
	$("#userGradeSubmit").html('<a href="javascript:void(0);" class="btn"  onclick="setGrade('+custmoerID+','+custmoerGrade+');"><strong><s:message code="button.label.submit2" text="submit"/></strong></a>');
	$("#grade_"+custmoerGrade).attr("checked","checked");
	$('input:radio').each(function(i,val){
		$(this).click(function(){
			$("#userGradeSubmit").html('<a href="javascript:void(0);" class="btn"  onclick="setGrade('+custmoerID+','+$(this).val()+');"><strong><s:message code="button.label.submit2" text="submit"/></strong></a>');
		});
		});
}
function showCustmoerGrade(custmoerid,grade){
	$("#show_grade_"+custmoerid).html('<a href="javascript:void(0);" onclick="showGrade('+custmoerid+','+grade+')"><s:message code="label.generic.set" text="Set"/><s:message code="label.generic.grade" text="Set"/></a>');
}
</script>
<div class="row table">
			<div class="row-fluid " style="padding-top:15px;">
			<ul class="nav nav-pills pull-right">
				 <li <c:if test="${empty cstatus}"> class="active"</c:if>><a href="javascript:void(0);" onclick="findByStatus('')"><s:message code="label.generic.all" text="All"/></a></li>
				 <li <c:if test="${not empty cstatus && cstatus=='0'}"> class="active"</c:if>><a href="javascript:void(0);" onclick="findByStatus('0')"><s:message code="label.generic.pre" text="Pre"/><s:message code="label.entity.active" text="Active"/></a></li>
				 <li <c:if test="${not empty cstatus && cstatus=='1'}"> class="active"</c:if>><a href="javascript:void(0);" onclick="findByStatus('1')"><s:message code="label.generic.pre" text="Pre"/><s:message code="label.entity.reactive" text="ReActive"/></a></li>
				 <li <c:if test="${not empty cstatus && cstatus=='2'}"> class="active"</c:if>><a href="javascript:void(0);" onclick="findByStatus('2')"><s:message code="label.generic.post" text="Pre"/><s:message code="label.entity.freeze" text="Freeze"/></a></li>
				 <li <c:if test="${not empty cstatus && cstatus=='3'}"> class="active"</c:if>><a href="javascript:void(0);" onclick="findByStatus('3')"><s:message code="label.generic.post" text="Pre"/><s:message code="label.entity.active" text="Active"/></a></li>
				 <li><span style="line-height:30px;font-size:25px;margin:3px;">|</span></li>
				<li><a href="javascript:void(0);" onclick="batchResetPasswor()" ><s:message code="button.label.batch" text="Batch"/><s:message code="button.label.resetpassword" text="Reset"/></a></li>
				<li class="dropdown"> 
						<a data-toggle="dropdown" class="dropdown-toggle" href="#" id="batchStatus">
						    	<s:message code="button.label.batch" text="Batch"/><s:message code="menu.customer" text="Custmoer"/><s:message code="label.entity.status" text="Status"/><s:message code="label.generic.operate" text="Operate"/>
						    <span class="caret"></span>
						  </a>
						  <ul class="dropdown-menu" aria-labelledby="batchStatus">
						    <li><a href="javascript:void(0);" onclick="batchActive('batchActive.html',1)"><s:message code="label.entity.active" text="Active"/></a></li>
						   <li class="divider"></li>
						    <li><a href="javascript:void(0);" onclick="batchActive('batchActive.html',2)"><s:message code="label.entity.freeze" text="Freeze"/></a></li>
						    <li class="divider"></li>
						    <li><a href="javascript:void(0);" onclick="batchActive('batchActive.html',3)"><s:message code="label.entity.unfreeze" text="Unfreeze"/></a></li>
						  </ul>
				 </li>
				<li><a data-toggle="collapse" href="#collapseOne" id="findPanel" ><s:message code="label.generic.detail.find" text="Find"/></a> </li>				 
			</ul>
				
			</div>
	</div>
	<div class="row " style="padding-left:20px;padding-right:20px;">
	<div id="collapseOne" class="panel-collapse collapse " style="position:relative;">
                <div class="panel panel-default">
                  	<form:form method="post" action="${pageContext.request.contextPath}/admin/customers/list.html" id="ezybioForm" class="form-horizontal" commandName="criteria">
					<fieldset>
					<input type="hidden" name="page" id="page" value="1"/>
					<input type="hidden" name="anonymous" id="anonymous" />
					<div class="col-sm-3 col-sm-offset-0 col-md-3 col-md-offset-0">
						<div class="form-group">
							<label class="control-label" for="nick"><s:message code="label.generic.firstname" text="User name" /></label>
							<div >
								<form:input path="nick" cssClass="userName find form-control" id="nick" />
								
							</div>
						</div>
						<div class="form-group">
							<label class="control-label" for="phone"><s:message code="label.generic.phone" text="Genre"/></label>
							<div >
								<form:input path="phone" cssClass="phone find form-control" id="phone" />
													</div>
						</div>
					</div>
					<div class="col-sm-3 col-sm-offset-1 col-md-3 col-md-offset-1">
						<div class="form-group">
							<label class="control-label" for="email"><s:message code="label.generic.email" text="Email address"/></label>
							<div class="">
							     <form:input path="emailAddress" cssClass="email find form-control" id="email"/>
							    
							</div>
						</div>
						<div class="form-group">
							<label class="control-label" for=compnay><s:message code="label.generic.user.company" text="Company Or School"/></label>
							<div class="">
							     <form:input path="compnay" cssClass="company find form-control" id="compnay" />
								
							</div>
						</div>
					</div>	
					<div class="col-sm-3 col-sm-offset-1 col-md-3 col-md-offset-1">
						<div class="form-group">
							<label class="control-label" for="project"><s:message code="label.generic.project" text="Project"/></label>
							<div class="">
							     <form:input path="project" cssClass="project find form-control" id="project" />
							</div>
						</div>
						<div class="form-group">
							<label class="control-label" for="grade"><s:message code="label.generic.grade" text="Grade"/></label>
							<div class="">
							 <form:select path="grade" cssClass="find form-control">
							    <form:option value=""></form:option>
							    <c:if test="${not empty gradeMap}">
							    	<c:forEach items="${gradeMap}" var="gradeMap" varStatus="customerStatus">
							    		 <form:option value="${gradeMap.key}">${gradeMap.key+1}<c:forEach begin="0" end="${gradeMap.key}"><img  src="<c:url value="/resources/img/stars/star-on.png" />"></c:forEach></form:option>										
									</c:forEach>
							    </c:if>
							 </form:select>
							</div>
						</div>
					</div>
					<div  class="pull-right " style="position:absolute;right:20px;top:20%;">
						<input id="Ezybiosubmit" type="submit" value="<s:message code="button.label.submit2" text="Sumit"/>" name="register" class="btn "><br><br>
						<input  type="button" onclick="clearForm()" value="<s:message code="button.label.clear" text="Clear"/>" class="btn ">					
					</div>	
					</fieldset>
				</form:form>
                </div>
  </div>
  </div>
			<div <c:if test="${empty customerList || empty customerList.customers}">style="display:none;"</c:if> >
				
				<table class="table table table-striped table-bordered table-hover" id="CustmoersTable">	
						<thead id ="addressthead">											 
							<tr class="cubox">
								<th><input type="checkbox" id="selectall"> </th>
								<th><s:message code="label.generic.firstname" text="User name" /></th>
								<th><s:message code="label.generic.user.company" text="Company Or School"/></th>
								<th><s:message code="label.generic.project" text="Project"/></th>
								<th><s:message code="label.generic.phone" text="Genre"/></th>
								<th><s:message code="label.generic.email" text="Email address"/></th>
								<th><s:message code="label.customer.points" text="Member Points"/></th>
								<th><s:message code="menu.user" text="User"/><s:message code="label.generic.grade" text="Grade"/></th>
								<th><s:message code="label.generic.genre" text="Genre"/></th>								
								<th><s:message code="label.entity.active" text="Active"/></th>
								<th><s:message code="button.label.resetpassword" text="Reset"/></th>
								<th><s:message code="label.generic.set" text="Set"/><s:message code="label.generic.grade" text="Set"/></th>
							</tr>
						</thead>
						<tbody>
								<c:forEach items="${customerList.customers}" var="custmoer" varStatus="customerStatus">
									<script>
										count++;
									</script>
									<tr id="result_name_${custmoer.id}" class="everyTR"><!-- item -->
										<td> <input type="checkbox" class="everycheckbox" id="${custmoer.id}"></td>
										<td >${custmoer.nick}</td>								
										<td>${custmoer.compnay}</td>										
										<td >${custmoer.project}</td>
										<td>${custmoer.phone}</td>
										<td>${custmoer.emailAddress}</td>
										<td>${custmoer.points}</td>
										<td id="custmoer_Grade_${custmoer.id}"><c:forEach begin="0" end="${custmoer.grade}"><img alt="go to order" src="<c:url value="/resources/img/stars/star-on.png" />"></c:forEach></td>	
										<td><c:choose><c:when test="${custmoer.gender=='M'}"><s:message code="label.generic.male" text="Male"/></c:when><c:otherwise><s:message code="label.generic.female" text="Female"/></c:otherwise></c:choose></td>
										<td id="custmoer_${custmoer.id}"><c:choose><c:when test="${custmoer.anonymous==0}"><a href="javascript:void(0);" onclick="aCustmoer(${custmoer.id},'active.html')"><s:message code="label.entity.active" text="Active"/></a></c:when>
										<c:when test="${custmoer.anonymous==1}"><a href="javascript:void(0);" onclick="aCustmoer(${custmoer.id},'active.html')"><s:message code="label.entity.reactive" text="Verify"/></a></c:when>
										<c:when test="${custmoer.anonymous==2}"><a href="javascript:void(0);" onclick="aCustmoer(${custmoer.id},'unfreeze.html')"><s:message code="label.entity.freezed" text="Unfreeze"/></a></c:when>
										<c:otherwise><a href="javascript:void(0);" onclick="aCustmoer(${custmoer.id},'freeze.html')"><s:message code="label.entity.freeze" text="Freeze"/></a></c:otherwise></c:choose></td>
										<td><a href="javascript:void(0);" onclick="resetPassword(${custmoer.id})"><s:message code="button.label.resetpassword" text="Reset Password"/></a></td>
										<td id="show_grade_${custmoer.id}"><a href="javascript:void(0);" onclick="showGrade(${custmoer.id},${custmoer.grade})"><s:message code="label.generic.set" text="Set"/><s:message code="label.generic.grade" text="Set"/></a></td>
									</tr>
								</c:forEach>
				</table>
					<c:set var="paginationData" value="${paginationData}" scope="request"/>
					<jsp:include page="/common/paginationFind.jsp"/>
	</div>
	<div class="modal fade" id="setGrade" tabindex="-1" role="dialog"  data-backdrop= "static" aria-hidden="true">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="cartModalLabel"><img src="<c:url value="/resources/img/important-icon.png"/>" width="40"/>&nbsp;<strong id="modeltitle">
			<s:message code="label.generic.current" text="Current"/><s:message code="menu.user" text="User"/><s:message code="label.generic.grade" text="Grade"/>:<span id="cuurentGrade"></span></strong></h4>
	      </div>
	      <div class="modal-body">
	        <c:choose><c:when test="${empty gradeMap}"><s:message code="message.information.getfailed" text="failed"/></c:when>
				<c:otherwise>
				<br>
					<c:forEach items="${gradeMap}" var="gradeMap" varStatus="customerStatus">
						<input type="radio" name="userGrade" value="${gradeMap.key}" id="grade_${gradeMap.key}"/><s:message code="label.customer.grade" text="Grade"/><sm:monetary value="${gradeMap.value}" />&nbsp;&nbsp;
						<c:forEach begin="0" end="${gradeMap.key}"><img alt="go to order" src="<c:url value="/resources/img/stars/star-on.png" />"></c:forEach>
						<br><br>
					</c:forEach>
				</c:otherwise>
			</c:choose>
	      </div>
	      <div class="modal-footer">
	        <div id="userGradeSubmit"></div>
	      </div>
	    </div>
	  </div>
</div>
</div>


			
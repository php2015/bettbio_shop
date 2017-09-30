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


<script src="<c:url value="/resources/js/check-functions.js" />"></script>


<script type="text/javascript">


$(document).ready(function() {
	$("input[type='text']").on("change keyup paste", function(){
		isEzybioFormValid();
		$(".alert-danger").hide();
	});
	
	if($('.alert-success').is(":visible")) {
		return true;
	}
	if($('.alert-error').is(":visible")) {
		return true;
	}
	isEzybioFormValid();
	
});

 </script>

				<div class="customer-box row">
					
						<div class="" style="text-align: left;line-height:20px;padding:10px 0px 20px 100px;border-bottom: solid 1px #0080ff;">
							<s:message code="label.generic.current" text="Current"/><s:message code="menu.user" text="User"/><s:message code="label.generic.grade" text="Grade"/>：
							<c:forEach begin="0" end="${customer.grade}">
								<img alt="go to order" src="<c:url value="/resources/img/stars/star-on.png" />">
								</c:forEach> &nbsp; &nbsp; &nbsp; &nbsp;
								<s:message code="label.generic.current" text="Current"/><s:message code="label.generic.grade" text="Grade"/><s:message code="message.order.limit" text="Limit" /><strong ><sm:monetary value="${grade}" /></strong> &nbsp; &nbsp; &nbsp; &nbsp;
								<s:message code="label.generic.current" text="Current"/><s:message code="message.order.left" text="Grade"/><strong ><sm:monetary value="${left}" /></strong>
								<c:if test="${not empty customer.points }">
									<br/>
									<s:message code="label.customer.points" text="Member Points"/>：<strong>${customer.points }</strong>
								</c:if><br/>
								<s:message code="message.order.change" text="Change"/>
											
						</div>	
							<br>	
				
	<div class="col-sm-8 col-sm-offset-0">
				<form:form method="post" action="${pageContext.request.contextPath}/shop/customer/updateBasicinfo.html" id="ezybioForm" class="form-horizontal" commandName="customer">
					<form:errors path="*" cssClass="alert alert-danger alert-danger form-group" element="div" />
					<fieldset>
					  <div class="form-group">
			                        <label class="col-sm-3 control-label"><s:message code="label.generic.firstname" text="User name" />(*):</label>
			                        <div class="col-sm-9">
			                        		<s:message code="NotEmpty.customer.userName" text="User name is required" var="msgUserName"/>
											<form:input path="nick" cssClass="required userName form-control" id="nick" title="${msgUserName}"/>
			                                <span class="help-inline"><form:errors path="nick" cssClass="error" /></span>
			                        </div>
				         </div>
				          <div class="form-group">
				          		<label class="col-sm-3 control-label"><s:message code="label.generic.phone" text="Phone"/>(*):</label>
			                        <div class="col-sm-9">			              		
			              					<input name="phone" class="form-control" readonly id="phone" value="${customer.phone}" type="text"/>
			              					 <span class="help-inline"><form:errors path="phone" cssClass="error" /></span>
				            		</div>
				          </div>  		
				         <div class="form-group">
			                        <label class="col-sm-3 control-label"><s:message code="label.generic.genre" text="Sex"/>:</label>
			                        <div class="col-sm-9">
			                        		 <form:select path="gender" value="${sex}">
							 				     <form:option value="M"><s:message code="label.generic.male" text="Male"/></form:option>
											     <form:option value="F"><s:message code="label.generic.female" text="Female"/></form:option>
											 </form:select>
											 <span class="help-inline"><form:errors path="gender" cssClass="error" /></span>
			                        </div>
				         </div>
				        <div class="form-group">
				        		<label class="col-sm-3 control-label"><s:message code="label.generic.email" text="Email address"/>(*):</label>
			                        <div class="col-sm-9">			              		
			              					<s:message code="NotEmpty.customer.emailAddress" text="Email address is required" var="msgEmail"/>
							     			<input name="emailAddress" class="form-control" readonly id="email" title="${msgEmail}" value="${customer.emailAddress}" type="text"/>
			              					 <span class="help-inline"><form:errors path="emailAddress" cssClass="error" /></span>
				            		</div>
				        </div> 
				         <div class="form-group">
			                        <label class="col-sm-3 control-label"><s:message code="label.generic.user.company" text="Company Or School"/>(*):</label>
			                        <div class="col-sm-9">
			                        		 <s:message code="NotEmpty.customer.company.school" text="Company is required" var="msgCompany"/>
							    			 <form:input path="compnay" cssClass="required company form-control" id="compnay" title="${msgCompany}"/>
											 <span class="help-inline"> <form:errors path="compnay" cssClass="error" /></span>
			                        </div>
				         </div>
				         <div class="form-group">
				         		 <label class="col-sm-3 control-label"><s:message code="label.generic.project" text="Project"/>(*):</label>
			                        <div class="col-sm-9">			              		
			              					<s:message code="NotEmpty.customer.company.project" text="Project is required" var="msgProject"/>
							   				  <form:input path="project" cssClass="required project form-control" id="project" title="${msgProject}"/>
			              					 <span class="help-inline"> <form:errors path="project" cssClass="error" /></span>
				            		</div>
				         </div>
						<div class="form-group">
			                        <label class="col-sm-3 control-label"><s:message code="label.generic.receive" text="Receive"/>:</label>
			                        <div class="col-sm-9">
			                        		<div style="float:left;line-height:25px;margin-right:10px;"><form:checkbox path="recieveEmail" /><s:message code="label.generic.receive.email" text="Visible"/></div>
			                        		<div style="float:left;line-height:25px;margin-right:10px;"><form:checkbox path="recievePhone" /><s:message code="label.generic.receive.phone" text="Visible"/></div>
			                        </div>
				         </div>
				         <div class="form-group">
				         		 <label class="col-sm-3 control-label"><s:message code="label.generic.validatecode" text="Validatecode"/>(*):</label>
				         		  <div class="col-sm-5">			              		
			              					 <s:message code="NotEmpty.store.validatecode" text="validatecode is required" var="validatecodeName"/>
										   <input type="text" Class="required company form-control " name="captchaResponseField" title="${validatecodeName}"/>
				            		</div>
				            		<div class="col-sm-4" style="line-height:35px;">	
				            			 <a style="cursor:point" onclick="changeValidateImg()"><img id="imgObj" src="<c:url value="/shop/store/code.html"/>" style="margin-top:7px;"/>&nbsp;&nbsp;<s:message code="label.generic.change.validatecode" text="Change"/></a>
				            		</div>
				         </div>
						 <div class="form-group">
    							<div class="col-sm-3 col-sm-offset-3">
     								<input id="Ezybiosubmit" type="submit" value="<s:message code="button.label.submit2" text="Register"/>" name="register" class="btn btn-info">
  							  </div>
  							  <div class="col-sm-3 col-sm-offset-3">
     								<input  type="reset" value="<s:message code="button.label.reset" text="Register"/>" class="btn btn-info">
  							  </div>
  						</div>
					</fieldset>
				</form:form>
	</div>		
	<div class="col-sm-3 ">
			<div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>
				<div id="store.error" class="alert alert-danger" style="display:none;"></div>	
				<!-- <div  class="alert alert-error alert-warning " style="display:none;"></div>	 -->
	</div>					
				</div>
			
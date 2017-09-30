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
<div class="container-fluid"> 	
				<div class="customer-box row">
					
						<div class="col-xs-12 col-sm-offset-0" style="padding-bottom:10px;">
							<div class="pull-left" style="font-size:15px;padding-top:10px;"><s:message code="label.customer.myaccount" text="Account"/></div>
							<div class="pull-right"><a href="#" onclick="javascript:window.history.go(-1);return false;"><span aria-hidden="true" class="carticon fa fa-reply fa-2x icon icon-style" style="background:#1E90FF;"></span></a></div>									
						</div>	
							
				<div class="col-xs-12 col-sm-offset-0"><s:message code="message.order.change" text="Change"/></div>
	<div class="col-xs-12 box main-padding-lr">
				<form:form method="post" action="${pageContext.request.contextPath}/shop/customer/updateBasicinfo.html" id="ezybioForm" class="form-horizontal" commandName="customer">
					<form:errors path="*" cssClass="alert alert-danger alert-danger form-group" element="div" />
					<fieldset>
					  <div class="form-group">
			                        <label class=" control-label"><s:message code="label.generic.firstname" text="User name" />(*):</label>
			                        <div class="">
			                        		<s:message code="NotEmpty.customer.userName" text="User name is required" var="msgUserName"/>
											<form:input path="nick" cssClass="required userName form-control" id="nick" title="${msgUserName}"/>
			                                <span class="help-inline"><form:errors path="nick" cssClass="error" /></span>
			                        </div>
				         </div>
				          <div class="form-group">
				          		<label class=" control-label"><s:message code="label.generic.phone" text="Phone"/>(*):</label>
			                        <div class="">			              		
			              					<input name="phone" class="form-control" readonly id="phone" value="${customer.phone}" type="text"/>
			              					 <span class="help-inline"><form:errors path="phone" cssClass="error" /></span>
				            		</div>
				          </div>  		
				         <div class="form-group">
			                        <label class=" control-label"><s:message code="label.generic.genre" text="Sex"/>:</label>
			                        <div class="">
			                        		 <form:select path="gender" value="${sex}">
							 				     <form:option value="M"><s:message code="label.generic.male" text="Male"/></form:option>
											     <form:option value="F"><s:message code="label.generic.female" text="Female"/></form:option>
											 </form:select>
											 <span class="help-inline"><form:errors path="gender" cssClass="error" /></span>
			                        </div>
				         </div>
				        <div class="form-group">
				        		<label class=" control-label"><s:message code="label.generic.email" text="Email address"/>(*):</label>
			                        <div class="">			              		
			              					<s:message code="NotEmpty.customer.emailAddress" text="Email address is required" var="msgEmail"/>
							     			<input name="emailAddress" class="form-control" readonly id="email" title="${msgEmail}" value="${customer.emailAddress}" type="text"/>
			              					 <span class="help-inline"><form:errors path="emailAddress" cssClass="error" /></span>
				            		</div>
				        </div> 
				         <div class="form-group">
			                        <label class=" control-label"><s:message code="label.generic.user.company" text="Company Or School"/>(*):</label>
			                        <div class="">
			                        		 <s:message code="NotEmpty.customer.company.school" text="Company is required" var="msgCompany"/>
							    			 <form:input path="compnay" cssClass="required company form-control" id="compnay" title="${msgCompany}"/>
											 <span class="help-inline"> <form:errors path="compnay" cssClass="error" /></span>
			                        </div>
				         </div>
				         <div class="form-group">
				         		 <label class=" control-label"><s:message code="label.generic.project" text="Project"/>(*):</label>
			                        <div class="">			              		
			              					<s:message code="NotEmpty.customer.company.project" text="Project is required" var="msgProject"/>
							   				  <form:input path="project" cssClass="required project form-control" id="project" title="${msgProject}"/>
			              					 <span class="help-inline"> <form:errors path="project" cssClass="error" /></span>
				            		</div>
				         </div>
						<div class="form-group">
			                        <label class="control-label"><s:message code="label.generic.receive" text="Receive"/>:</label>
			                        <div class="">
			                        		<div style="float:left;line-height:25px;margin-right:10px;"><form:checkbox path="recieveEmail" /><s:message code="label.generic.receive.email" text="Visible"/></div>
			                        		<div style="float:left;line-height:25px;margin-right:10px;"><form:checkbox path="recievePhone" /><s:message code="label.generic.receive.phone" text="Visible"/></div>
			                        </div>
				         </div>
				         <div class="form-group">
				         		 <label class="control-label"><s:message code="label.generic.validatecode" text="Validatecode"/>(*):</label>
				         		  <div class="">			              		
			              					 <s:message code="NotEmpty.store.validatecode" text="validatecode is required" var="validatecodeName"/>
										   <input type="text" Class="required company form-control " name="captchaResponseField" title="${validatecodeName}"/>
				            		</div>
				            		<div class="" style="line-height:35px;">	
				            			 <a style="cursor:point" onclick="changeValidateImg()"><img id="imgObj" src="<c:url value="/shop/store/code.html"/>" style="margin-top:7px;"/>&nbsp;&nbsp;<s:message code="label.generic.change.validatecode" text="Change"/></a>
				            		</div>
				         </div>
						 <div class="form-group " >
    							<div class="pull-left ">
     								<input id="Ezybiosubmit" type="submit" value="<s:message code="button.label.submit2" text="Register"/>" name="register" class="btn btn-info">
  							  </div>
  							  <div class="pull-right">
     								<input  type="reset" value="<s:message code="button.label.reset" text="Register"/>" class="btn btn-info">
  							  </div>
  						</div>
					</fieldset>
				</form:form>
	</div>	
	</div>	
	<div class="col-xs-12 ">
			<div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>
				<div id="store.error" class="alert alert-danger" style="display:none;"></div>	
				<div  class="alert alert-error alert-warning " style="position:fixed; top:90px;right:10px;display:none;"></div>	
	</div>					
				</div>
			
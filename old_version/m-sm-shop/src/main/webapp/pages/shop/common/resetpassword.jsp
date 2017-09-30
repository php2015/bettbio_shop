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
 <link href="<c:url value="/resources/templates/bootstrap3/css/path-css.css" />" rel="stylesheet" type="text/css">
 <script src="<c:url value="/resources/js/check-functions.js" />"></script>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<div class="container-fluid">
<c:url var="resetpassword" value="/shop/customer/resetPass.html"/>
<script type="text/javascript">

$(document).ready(function() {
	$("input[type='text']").on("change keyup paste", function(){
		isEzybioFormValid();
	});
	if($('.alert-error').is(":visible")) {
		return true;
	}
	isEzybioFormValid("${erroInfo}");
	
});

 </script>
 <div  style="padding:1%">
 <div class="box" >
	<div >
		<nav>
			<ol class="cd-multi-steps text-bottom count ">
				<li class="current font-normal"><em class="font-normal"><s:message code="label.custmoer.write" text="Write" /><s:message code="label.generic.amount" text="count" /></em></li>
				<li class="font-normal" ><em class="font-normal"><s:message code="label.custmoer.virity" text="count" /></em></li>
				<li class=" font-normal"><em class="font-normal"><s:message code="label.generic.set" text="Write" /><s:message code="label.generic.newpassword" text="count" /></em></li>
				<li ><em class="font-normal"><s:message code="label.custmoer.password.done" text="count" /></em></li>
			</ol>
		</nav>
	</div>
	<div style="padding-top:2%" class="row">
		<div class="col-sm-9 "  >
			<form action="${resetpassword}" method="POST" id="ezybioForm" class="form-horizontal" name="ezybioForm" >
	                <fieldset>
	                	<div class="form-group">
				             <label class="col-sm-2 control-label col-sm-offset-2" ><s:message code="label.longin.tips" text="Register" />:</label>
				           		 <div class="col-sm-6 ">			              		
				  				  <s:message code="NotEmpty.custmoer.account" text="account is required" var="accountName"/>
									<input id="signin_userName" class="required form-control" type="text" name="signin_userName" title="${accountName}" />
				 				  </div>
				      	</div>
				      	 <div class="form-group">
	                         <label class="col-sm-2 control-label col-sm-offset-2" ><s:message code="label.generic.validatecode" text="Validatecode"/>:</label>
	                        <div class="col-sm-2">			              		
	              				  <s:message code="NotEmpty.store.validatecode" text="validatecode is required" var="validatecodeName"/>
							  	 <input Class="required form-control " type ="text"  name ="captchaResponseField" id="captchaResponseField" title="${validatecodeName}"/>
		            		</div>
	                        <label class="col-sm-2 control-label " ><a href="#" onclick="changeValidateImg()"><s:message code="label.generic.change.validatecode" text="Change"/></a>:</label>
	                        <div class="col-sm-2">			              		
	              				 <a href="#" onclick="changeValidateImg()"><img id="imgObj" src="<c:url value="/shop/store/code.html"/>" /></a>
		            		</div>
	           			</div>
				      	<div class="form-group">
				  				<div class="col-sm-4 col-sm-offset-4">
								<input id="Ezybiosubmit" type="submit" value="<s:message code="button.label.submit2" text="Send"/>" name="register" class="btn btn-large">
			 				</div>
						</div>	
	                </fieldset>
	         </form>  
		</div>
			<div class="col-sm-2">
		         <div id="store.error" class="alert-error" style="display:none;background-color:#0080c0;min-height:20px;color:ffffff;padding:10px;"></div>
		   </div>
    </div>
</div>
</div>
		
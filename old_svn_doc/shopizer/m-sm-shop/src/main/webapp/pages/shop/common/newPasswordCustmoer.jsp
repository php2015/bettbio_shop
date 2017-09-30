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
   <script src="<c:url value="/resources/js/pwstrength.js" />"></script>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<c:url var="donePass" value="/shop/customer/donePassword.html"/>
<script type="text/javascript">

$(document).ready(function() {
	$("input[type='password']").on("change keyup paste", function(){
		isEzybioFormValid();
	});
	if($('.alert-error').is(":visible")) {
		return true;
	}
	isEzybioFormValid("${erroInfo}");
	
});

 </script>
<div  style="padding:1%">
 	<div class="box">
 	<div>
		<nav>
			<ol class="cd-multi-steps text-bottom count ">
				<li class="font-normal"><em class="font-normal"><s:message code="label.custmoer.write" text="Write" /><s:message code="label.generic.amount" text="count" /></em></li>
				<li class="font-normal" ><em class="font-normal"><s:message code="label.custmoer.virity" text="count" /></em></li>
				<li class="current font-normal"><em class="font-normal"><s:message code="label.generic.set" text="Write" /><s:message code="label.generic.newpassword" text="count" /></em></li>
				<li ><em class="font-normal"><s:message code="label.custmoer.password.done" text="count" /></em></li>
			</ol>
		</nav>
	</div>
	<div style="padding-top:2%" class="row">
		<div class="col-sm-9 "  >
			<form action="${donePass}" method="POST" id="ezybioForm" class="form-horizontal" name="ezybioForm" >
	                <fieldset>
	                	 <div class="form-group">
							<label class="col-sm-2 control-label col-sm-offset-2" for="password"><s:message code="label.customer.newpassword" text="New password"/>:</label>
							<div class="col-sm-4">
							   <s:message code="newpassword.not.empty" text="New password is required" var="msgPassword"/>
							   <input type="password"  class="required password form-control " name="passwords" title="${msgPassword}" onkeyup="pwStrength(this.value);" onblur="pwStrength(this.value);"/>
							    <span class="help-inline"><errors path="currentPassword" cssClass="error" /></span>								
							</div>
						 </div>
						 <div class="form-group">
							<label class="col-sm-2 control-label col-sm-offset-2" for="repeatPassword"><s:message code="label.customer.repeatpassword" text="Repeat password"/></label>
							<div class="col-sm-4">
							   <s:message code="repeatpassword.not.empty" text="Current password is required" var="msgRepeatPassword"/>
							   <input type="password"  class="required checkPassword form-control " name="checkPassword" title="${msgRepeatPassword}"/>
							    <span class="help-inline"><errors path="currentPassword" cssClass="error" /></span>								
							</div>
						 </div>
						 <div class="form-group">
			                        <label class="col-sm-2 col-sm-offset-2 control-label"></label>
			                        <div class="col-sm-6">
			                        	 <table width="180" border="1" cellspacing="0" cellpadding="1" bordercolor="#eeeeee" height="22" style='display:inline;'>  
											<tr align="center" bgcolor="#f5f5f5">  
											<td width="33%" id="strength_L" style="padding:3px"><s:message code="password.strendth.low" text="Strength Low"/></td>  
											<td width="33%" id="strength_M" style="padding:3px"><s:message code="password.strendth.middle" text="Strength Middle"/></td>  
											<td width="33%" id="strength_H" style="padding:3px"><s:message code="password.strendth.high" text="Strength High"/></td> 
											</tr>  
										</table> <br>
										<s:message code="password.strength.rule" text="Strength password"/>
			                        </div>			                       		                        
				            </div>
				      	<div class="form-group">
				  				<div class="col-sm-6 col-sm-offset-4">
								<input id="Ezybiosubmit" type="submit" value="<s:message code="button.label.submit2" text="Send"/>" name="register" class="btn btn-large">
			 				</div>
						</div>	
	                </fieldset>
	         </form> 
	      </div>
	      <div class="col-sm-2">
			        <div id="store.error" class="alert-error" style="display:none;background-color:#0080c0;min-height:20px;color:ffffff;paddingt:10px;"></div>
			   </div>
	</div>
</div>
</div>
		
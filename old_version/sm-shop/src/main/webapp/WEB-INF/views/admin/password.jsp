<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ page session="false" %>				
<c:set var="lang" scope="request" value="${requestScope.locale.language}"/> 
 <html xmlns="http://www.w3.org/1999/xhtml"> 
     <head>
       <meta charset="utf-8">
       <meta http-equiv="X-UA-Compatible" content="IE=edge">
       <meta name="viewport" content="width=device-width, initial-scale=1">        	 		
	   <title><s:message code="label.storeadministration" text="Store administration" /></title>    			
	   <meta name="keywords" content='<s:message code="label.website.keywords" text="bettbio.com"/>'>
	   <meta name="description" content='<s:message code="label.website.descirption" text="bettbio.com"/>'>
	   <meta name="author" content="bettbio.com">
    		            <!--[if lte IE 9]>		
		 <script type="text/javascript">
				window.location.href='<c:url value="/shop/nonsupport.html"/>';
		</script>	
	<![endif]-->
    			<script src="<c:url value="/resources/js/jquery-2.0.3.min.js" />"></script> 
    			<script src="<c:url value="/resources/js/jquery.friendurl.min.js" />"></script>
                <jsp:include page="/common/adminLinks.jsp" />
 	</head>

<script src="<c:url value="/resources/js/pwstrength.js" />"></script>
<script src="<c:url value="/resources/js/shop-customer.js" />"></script>

<script type="text/javascript">
</script>
<div class="container-fluid">
	<div class="col-sm-12 text-center">
				 <div class="col-sm-3 col-sm-offset-2">
							<p class="lead">
								<s:message code="button.label.resetpassword" text="Reset" />
							</p>
						</div>
						<div class="col-sm-5 col-sm-offset-2">
							<%-- <img alt="go to ezybio"
								src="<c:url value="/resources/img/shopizer_small.png" />"> --%>
								<p class="lead">
							<a onClick="javascript:location.href='<c:url value="/admin/logon.html"/>';" href="#"><s:message code="button.label.store.login" text="Login Now" /></a>
						</p>
						</div>
	</div>	
</div>
<hr style="height:2px;border:none;border-top:1px solid #3498DB; ">
<div class="container-fluid">
<div class="row">
<div class="col-sm-12 text-center">
				<c:url var="savePassword" value="/admin/savePassword.html"/>


				<form:form method="POST" commandName="password" action="${savePassword}" class="form-horizontal">

      							
      				<form:errors path="*" cssClass="alert alert-danger" element="div" />
					<div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>    
								
				                 
				                  
				                 <div class="form-group form-group-lg">
				                       <label class="col-sm-2 control-label"><s:message code="label.generic.newpassword" text="New password"/></label>
					                        <div class="col-sm-10">
					                        		<input type="password" name="newPassword" id="newPassword" class="form-control" onkeyup="pwStrength(this.value);" onblur="pwStrength(this.value);"/>
					                                <span class="help-inline"><form:errors path="newPassword" cssClass="error" /></span>
					                        </div>
				                  </div>
				                  
				                   <div class="form-group">
			                        <label class="col-sm-2 control-label"></label>
			                        <div class="col-sm-9">
			                        	 <table width="180" border="1" cellspacing="0" cellpadding="1" bordercolor="#eeeeee" height="22" style='display:inline;'>  
											<tr align="center" bgcolor="#f5f5f5">  
											<td width="33%" id="strength_L" style="padding:3px"><s:message code="password.strendth.low" text="Strength Low"/></td>  
											<td width="33%" id="strength_M" style="padding:3px"><s:message code="password.strendth.middle" text="Strength Middle"/></td>  
											<td width="33%" id="strength_H" style="padding:3px"><s:message code="password.strendth.high" text="Strength High"/></td> 
											</tr>  
										</table> 
										<s:message code="password.strength.rule" text="Strength password"/>
			                        </div>			                       		                        
				            </div>
				
				                 <div class="form-group form-group-lg">
				                       <label class="col-sm-2 control-label"><s:message code="label.generic.newpassword.repeat" text="Repeat new password"/></label>
					                        <div class="col-sm-10">
					                        		<input type="password" name="repeatPassword" id="passwordAgain" class="form-control" onkeyup="repeatPwd(this.value);" onblur="repeatPwd(this.value);"/>
					                                <span class="help-inline"><form:errors path="repeatPassword" cssClass="error" /></span>
					                        </div>
				                  </div>
				                  <div class="form-group form-group-lg" id="repeatpwd" style="display:none;">
				                       <label class="col-sm-2 control-label"></label>
					                        <div class="col-sm-10">
					                        		 <label style="color:red"><s:message code="password.notequal" text="Repeat new password"/></label>
					                        </div>
				                  </div>			
			      <div class="form-actions">

                  		<div class="pull-right">
                  			<button type="submit" class="btn btn-success"><s:message code="button.label.submit" text="Submit"/></button>
                  		</div>

            	 </div>

            	 </form:form>
            	 </div>
            	 </div>
	      			     
</div>
      					

      			     
      			     


      			     
      			     
    


   					
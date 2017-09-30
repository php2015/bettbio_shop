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
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ page import="java.util.Calendar" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
 

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><div class="modal fade" id="conformAudit" tabindex="-1" role="dialog"  aria-hidden="true" data-backdrop= 'static'>
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><img src="<c:url value="/resources/img/important-icon.png"/>" width="40"/>&nbsp;<strong ><s:message code="label.entity.audit.confirm" text="Conform"/>!</strong></h4>  
      </div>
      <div class="modal-body">  
        <p><s:message code="label.entity.audit.confirm" text="Confirm"/></p>  
      </div>
      <div class="modal-footer">  
         <input type="hidden" id="url"/>  
         <button type="button" class="btn btn-default" data-dismiss="modal"><s:message code="button.label.cancel" text="Cancel"/></button>  
        	<a href=""    class="btn btn-success" data-dismiss="modal" id="confirmHrefAudit"><s:message code="button.label.ok" text="Confirm"/></a>  
      </div>  
    </div>
  </div>
</div>  
  <script>	
	function confrom(pid){
		$('#confirmHrefAudit').bind('click', function(){
			//$('#conformAudit').modal('hide');
			
				
			});
		if(confirm('<s:message code="label.entity.audit.confirm" text="Confirm"/>')){
			$('#tabpanes').showLoading();
			$.ajax({  
					 type: 'POST',  
					 url:'audit.html',  
					 dataType: 'json', 
					 data:'pid='+pid,
					 cache:false,
					 error: function(e) { 
						 $('#tabpanes').hideLoading();
						console.log('Error while loading price');
						 alert("Operate failed");
						// doAction(page);
					 },
					 success: function(resp) {
						 $('#tabpanes').hideLoading();
						 if(resp.response.status>=0){
							 alert("Success");
							// $("#editAudit").hide(); 
						 }else{
							 alert("Operate failed");
						 }
						 //doAction(page);
					 } 
				});
		}
		
		//$('#conformAudit').modal('show');
		
	}
	</script>	
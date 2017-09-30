<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>


<div class="modal fade" id="delcfmModel" tabindex="-1" role="dialog"  aria-hidden="true" data-backdrop= 'static'>
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><img src="<c:url value="/resources/img/important-icon.png"/>" width="40"/>&nbsp;<strong ><s:message code="label.generic.remove" text="Remove"/><s:message code="label.generic.confirm" text="Confirm"/>!</strong></h4>  
      </div>
      <div class="modal-body">  
        <p><s:message code="label.entity.remove.confirm" text="Confirm"/></p>  
      </div>
      <div class="modal-footer">  
         <input type="hidden" id="url"/>  
         <button type="button" class="btn btn-default" data-dismiss="modal"><s:message code="button.label.cancel" text="Cancel"/></button>  
        	<a href="javascript:void(0);"   onclick="" class="btn btn-success" data-dismiss="modal" id="confirmHref"><s:message code="button.label.ok" text="Confirm"/></a>  
      </div>  
    </div>
  </div>
</div>   			
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %> 

 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>



 <div class="modal fade" id="delcfmModel" data-backdrop= 'static' tabindex="-1" role="dialog"  aria-hidden="true">  
  <div class="modal-dialog" role="document">  
    <div class="modal-content">  
      <div class="modal-header">  
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>  
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
    </div><!-- /.modal-content -->  
  </div><!-- /.modal-dialog -->  
</div>
	<div class="modal fade" id="addnewinvoice" tabindex="-1" role="dialog" data-backdrop= 'static'  aria-hidden="true">
		<div class="modal-dialog" role="document" style="width:80%">  
    	<div class="modal-content">  
     		 <div class="modal-header">  <button class="close" type="button" data-dismiss="modal">×</button>
				<h3 id="cartModalLabel"><img src="<c:url value="/resources/img/important-icon.png"/>" width="40"/>&nbsp;<strong ><s:message code="label.customer.billinginformation" text="Address"/></strong></h3>
			</div>
			<div class="modal-body" >
				<jsp:include page="/pages/shop/common/customer/editInvoice.jsp" />
    		</div> 
		</div>
	</div>
	</div>
<div class="modal fade" id="addnew" tabindex="-1" role="dialog" data-backdrop= 'static' aria-hidden="true" >
	<div class="modal-dialog" role="document" style="width:80%">  
    	<div class="modal-content">  
     		 <div class="modal-header"><button class="close" type="button" data-dismiss="modal">×</button>
				<h3 ><img src="<c:url value="/resources/img/important-icon.png"/>" width="40"/>&nbsp;<strong><s:message code="label.customer.address" text="Address"/></strong></h3>
			</div>
			<div class="modal-body" >
				<jsp:include page="/pages/shop/common/customer/editAddress.jsp" />	
			</div>
    	</div>
    </div>	 
</div>		

<div class="modal fade" id="checkoutModal" tabindex="-1" role="dialog" data-backdrop= 'static' aria-hidden="true">
	<div class="modal-dialog" role="document" style="width:80%">  
    	<div class="modal-content"> 
			<div class="modal-header" ><button class="close" type="button" data-dismiss="modal">×</button>
				<h3 id="cartModalLabel"><img src="<c:url value="/resources/img/important-icon.png"/>" width="40"/>&nbsp;<strong id="modeltitle"></strong></h3>
			</div>
			<div class="modal-body" >
		
				<table class="table table-hover" id="modaltable">
				</table>
			</div>	
			<div class="modal-footer">
					<a href="#" class="btn" data-dismiss="modal"><strong><s:message code="cart.close" text="Close"/></strong></a>
			</div>
		</div>
    </div> 
</div>


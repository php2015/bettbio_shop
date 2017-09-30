<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %> 
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<script type="text/javascript">


function modaladdresstbale(defaultdata){
	$('#modaltable').html(addresslist(defaultdata));	
	$('#modeltitle').text('<s:message code="message.order.billingaddress" text="Please choice address"/>');	
	bindingclick("selectaddress","addressid","invoicesendid");	
};

function modalinvoicetbale(defaultdata){	
	$('#modaltable').html(invoicelist(defaultdata))
	$('#modeltitle').text('<s:message code="message.order.invoiceinfo" text="Please choice address"/>');
	bindingclick("selectinvoice","invoiceid","invoicesendid");	 
};
function bindingclick(selectobj,selectid,otherid){
	 $('#pageContainer').hideLoading();
	 $('#checkoutModal').modal('show');
	
	 $(".selecttr").unbind();
	 $(".selecttr").bind("click",function(){
		 
		 if(otherid != null){
			 if(selectobj=='selectaddress'){
				 $("#addressid").val($(this).attr("id"));
				 if ($('#invoicesend').is(':checked')) {
					 $("#invoicesendid").val($(this).attr("id"));					 
				 }else{
					 if($("#invoicesendid").val()==$(this).attr("id")){
						 $('#invoicesend').prop("checked", true); 
							$('#sendbox').hide();
					 }else{
						 $('#sendbox').show();
					 }
				 }
			 }else{
				 $("#invoiceid").val($(this).attr("id"));
			 }
			 $("#"+selectobj).html($(this).html());			
			//发票不同邮寄地址操作
		 } else {			 
			 if($(this).attr("id")== $("#invoicesendid").val()){				
				 $('#invoicesend').prop("checked", true); 
				 $('#sendbox').hide();
			 }else{
				 $('#invoicesend').prop("checked", false); 
				 $("#"+selectobj).html($(this).html());
				 $("#invoicesendid").val($(this).attr("id"));	
				 $('#sendbox').show();
			 }
		 }
		 
		 $('#checkoutModal').modal('hide');
		
	 });
	 ischeckoutFormValid();
}

function modalsendadress(defaultdata){
	$('#modaltable').html(addresslist(defaultdata));	
	$('#modeltitle').text('<s:message code="message.order.invoiceaddress" text="Please choice address"/>');
	bindingclick("sendaddresstr","invoicesendid");
	
};


</script>

<div class="modal fade" id="addnewinvoice" tabindex="-1" role="dialog" data-backdrop= 'static'  aria-hidden="true">
		<div class="modal-dialog" role="document" >  
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
	<div class="modal-dialog" role="document" >  
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
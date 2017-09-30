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
	var table=addresslist(defaultdata);	
	if(table.getElementsByTagName('thead')[0] != null){
		table.removeChild(table.getElementsByTagName('thead')[0]);
	}
	var thead= addresshead();
	table.appendChild(thead);	
	$('#modeltitle').text('<s:message code="message.order.billingaddress" text="Please choice address"/>');	
	bindingclick("selectaddress","addressid","invoicesendid");	
};

function modalinvoicetbale(defaultdata){
	var table=invoicelist(defaultdata);	
	if(table.getElementsByTagName('thead')[0] != null){
		table.removeChild(table.getElementsByTagName('thead')[0]);
	}
	var thead= invoicehead();
	table.appendChild(thead);
	
	$('#modeltitle').text('<s:message code="message.order.invoiceinfo" text="Please choice address"/>');
	bindingclick("selectinvoice","invoiceid","invoicesendid");	 
};
function bindingclick(selectobj,selectid,otherid){
	 $('#pageContainer').hideLoading();
	 $('#checkoutModal').modal('show');
	 $("#modaltable tr").unbind();
	 $("#modaltable tr").bind("click",function(){
		 
		 if(otherid != null){
			 if(selectobj=='selectaddress'){
				 $("#addressid").val($(this).attr("id"));
				 if ($('#invoicesend').is(':checked')) {
					 $("#invoicesendid").val($(this).attr("id"));					 
				 }else{
					 if($("#invoicesendid").val()==$(this).attr("id")){
						 $('#invoicesend').prop("checked", true); 
							$('#sendaddress').hide();
					 }else{
						 $('#sendaddress').show();
					 }
				 }
			 }else{
				 $("#invoiceid").val($(this).attr("id"));
			 }			 
			 var selecta = document.getElementById(selectobj);
			 selecta.innerHTML="";
			 var index =0;
			 $(this).find("td").each(function(){
				   selecta.insertCell(index).innerHTML=$(this).text();
				   index ++;
				});
			//发票不同邮寄地址操作
		 } else {			 
			 if($(this).attr("id")== $("#invoicesendid").val()){				
				 $('#invoicesend').prop("checked", true); 
				 $('#sendaddress').hide();
			 }else{
				 $('#invoicesend').prop("checked", false); 
				 var selecta = document.getElementById(selectobj);
				 selecta.innerHTML="";
				 var index =0;
				 $(this).find("td").each(function(){
					   selecta.insertCell(index).innerHTML=$(this).text();
					   index ++;
					});
				 $("#invoicesendid").val($(this).attr("id"));	
				 $('#sendaddress').show();
			 }
		 }
		 
		 $('#checkoutModal').modal('hide');
		
	 });
	 ischeckoutFormValid();
}

function modalsendadress(defaultdata){
	var table=addresslist(defaultdata);
	if(table.getElementsByTagName('thead')[0] != null){
		table.removeChild(table.getElementsByTagName('thead')[0]);
	}
	var thead= addresshead();
	table.appendChild(thead);	
	$('#modeltitle').text('<s:message code="message.order.invoiceaddress" text="Please choice address"/>');
	bindingclick("sendaddresstr","invoicesendid");
	
};

function addresshead(){
	var thead = document.createElement("thead");	
	var tr = thead.insertRow(0) ;
	tr.insertCell(0).innerHTML='<s:message code="label.customer.shipping.firstname" text="Name"/>';	
	tr.insertCell(1).innerHTML='<s:message code="label.customer.companyname" text="Company"/>';	
	tr.insertCell(2).innerHTML='<s:message code="label.customer.zone" text="State / Province"/>';	
	tr.insertCell(3).innerHTML='<s:message code="label.customer.city" text="City"/>';
	tr.insertCell(4).innerHTML='<s:message code="label.customer.streetaddress" text="Street Address"/>';
	tr.insertCell(5).innerHTML='<s:message code="label.generic.postalcode" text="Postal code"/>';
	tr.insertCell(6).innerHTML='<s:message code="label.customer.telephone" text="Phone"/>';
	tr.insertCell(7).innerHTML='<s:message code="label.product.proof.title" text="Memo"/>';
	return thead;
};

function invoicehead(){
	var thead = document.createElement("thead");	
	var tr = thead.insertRow(0) ;
	tr.insertCell(0).innerHTML='<s:message code="label.customer.invoice.company" text="Company"/>';	
	tr.insertCell(1).innerHTML='<s:message code="label.customer.billingregister" text="Address"/>';	
	tr.insertCell(2).innerHTML='<s:message code="label.customer.billingphone" text="Phone"/>';
	tr.insertCell(3).innerHTML='<s:message code="label.customer.billingbank" text="Bank"/>';
	tr.insertCell(4).innerHTML='<s:message code="label.customer.billingaccount" text="Account"/>';
	tr.insertCell(5).innerHTML='<s:message code="label.customer.billingtaxnumber" text="Tax Number"/>';
	tr.insertCell(6).innerHTML='<s:message code="label.product.proof.title" text="Memo"/>';
	return thead;
}
</script>

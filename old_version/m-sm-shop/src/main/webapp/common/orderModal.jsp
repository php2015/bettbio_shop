<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>
<script SRC="<c:url value="/resources/js/selectList.js" />"></script>

<div class="modal fade" id="delivery" tabindex="-1" role="dialog"  data-backdrop= "static" aria-hidden="true">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 id="cartModalLabel"><img src="<c:url value="/resources/img/important-icon.png"/>" width="40"/>&nbsp;<strong id="modeltitle"><s:message code="label.order.delivery" text="Delivery"/></strong></h4>
	      </div>
	      <div class="modal-body">	      		
	       		<div class="input-group"><span class="input-group-addon "><s:message code="label.order.delivery.no" text="No"/>:</span>
	       				<input type="text" class="form-control " id="deliveryNo" name="deliveryNo" />
						</div>
	       		<div class="input-group">
					<span class="input-group-addon "><s:message code="label.order.delivery.code" text="Code"/>:</span>
					<input type="text" class="form-control " id="searchstorename" name="searchstorename" readonly />

					<!-- form:select cssClass="form-control" items="${stores}" itemValue="id" itemLabel="storename" path="merchantStore.id"/-->

					<span class="input-group-addon dropdown-toggle" id="sns"
						data-toggle="collapse" aria-haspopup="true"
						href="#collapseExample" aria-expanded="false"
						aria-controls="collapseExample"> <span
						class="glyphicon glyphicon-th " aria-hidden="true"></span>
					</span>

				</div>
				<div class="collapse" id="collapseExample">
					<div class="well" id="storeNameList">
								</div>
							</div>
				
	      </div>
	      <div class="modal-footer">
	      		 <c:set var="delivery_url" value="${pageContext.request.contextPath}/admin/orders/delivery.html" scope="request"/>
	       		 <a href="javascript:void(0);"  onclick='deliveryOrder("${delivery_url}")' id="deliverySubOrderSubmit" class="btn" ><strong><s:message code="button.label.submit2" text="Submit"/></strong></a>
				<a href="#" class="btn" data-dismiss="modal"><strong><s:message code="cart.close" text="Close"/></strong></a>
	      </div>
	    </div>
	  </div>
</div>
<div class="modal fade bs-example-modal-lg" id="split" tabindex="-1" role="dialog"  data-backdrop= "static" aria-hidden="true">
	  <div class="modal-dialog modal-lg" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>	        
	      	<h4 id="cartModalLabel"><img src="<c:url value="/resources/img/important-icon.png"/>" width="40"/>&nbsp;<strong id="modeltitle"><s:message code="label.order.split" text="Split"/></strong></h4>
	      </div>
	      <div class="modal-body">
	     	 <div  class="container-fluid">
	     	 <div class="col-sm-12 text-center" ><h5><strong ><s:message code="label.order.split.info" text="Split"/></strong></h5></div>
	       		<div class="col-sm-5 col-sm-offset-1 col-md-5 col-md-offset-1">
					<table id ="sourcetabel" class="droptarget  table-bordered "  width="100%"  border="1px" bordercolor="#000000" cellspacing="0px" style="border-collapse:collapse" >
						<caption><s:message code="label.generic.current" text="Current"/><s:message code="label.order.title" text="Order"/></caption>
						<thead>
							<tr>
								<th width="80%" align="left"><s:message code="label.product.details" text="Detail"/></th>
								<th width="20%" align="left"><s:message code="label.quantity" text="Quantity"/></th>
							</tr>
						</thead>
						<tbody></tbody>
						<!-- tr class="dropsource"> <td>aaa</td><td>aaa</td><td>aaa</td><td>aaa</td>
						<tr class="dropsource"> <td>bbb</td><td>bbb</td><td>bbb</td><td>bbb</td-->
					</table>
				</div>
				<div  class="col-sm-5 col-sm-offset-1 col-md-5 col-md-offset-1">
					<table id ="targettabel" class="droptarget  table-bordered " width="100%"  border="1px" bordercolor="#000000" cellspacing="0px" style="border-collapse:collapse">
					<caption><s:message code="label.generic.current" text="Current"/><s:message code="button.label.add" text="Order"/></caption>
						<!-- tr class="dropsource"> <td>aba</td><td>aaa</td><td>aaa</td><td>aaa</td>
						<tr class="dropsource"> <td>bcb</td><td>bbb</td><td>bbb</td><td>bbb</td-->
						<thead>
							<tr>
								<th width="80%" align="left"><s:message code="label.product.details" text="Detail"/></th>
								<th width="20%" align="left"><s:message code="label.quantity" text="Quantity"/></th>
							</tr>	
						</thead>
						<tbody></tbody>
					</table>
				</div>
				</div>
	      </div>
	      <div class="modal-footer">
	       		 <a href="javascript:void(0);"  onclick="splitSubOrder()" id="splitSubOrderSubmit" class="btn" ><strong><s:message code="button.label.submit2" text="Submit"/></strong></a>
					<a href="#" class="btn" data-dismiss="modal"><strong><s:message code="cart.close" text="Close"/></strong></a>
	      </div>
	    </div>
	  </div>
</div>
<form:form method="post" action="${pageContext.request.contextPath}/admin/orders/list.html" id="ezybioForm" commandName="criteria" tyle="display:none">
	<fieldset>
	<input type="hidden" name="page" id="page" value="1"/>
	<input type="hidden" name="status" id="status" <c:if test="${not empty criteria.status}"> value="${criteria.status}" </c:if>/>
	<input type="hidden" name="findName" id="findName" <c:if test="${not empty criteria.findName}"> value="${criteria.findName}" </c:if>/>
	<input type="hidden" name="beginDatePurchased" id="beginDatePurchased" <c:if test="${not empty criteria.beginDatePurchased}"> value="${criteria.beginDatePurchased}" </c:if>/>
	<input type="hidden" name="splitSubOrder" id="splitSubOrder" />
	</fieldset>
</form:form>
<script>
var self='<s:message code="label.bettbio.selfdifine" text="Edit product" />';
$(function() {	
	var url=getContextPath()+'/admin/orders/stores.html'
	getStores(url,self);
});
function getContextPath() {
   return "${pageContext.request.contextPath}";
}
function addorder(tr,data){
	tr.id=data.id;	
	var tdname=	tr.insertCell(0);
	tdname.innerHTML =data.name;
	tdname.width= "80%";
	tr.insertCell(1).innerHTML=data.quantity;
	return tr;
}
var cuurentPage=1;
var subOrderid;

function setStatus(strStatus){
	 switch(strStatus)
		{
	 	case "SHIPPED" :
	 		 $("#status_"+subOrderid).html('<s:message code="menu.order" text="Order"/><s:message code="label.entity.status" text="Status"/>:<s:message code="label.order.SHIPPED" text="Status"/>');
	 		break;
		}
	
}
</script>
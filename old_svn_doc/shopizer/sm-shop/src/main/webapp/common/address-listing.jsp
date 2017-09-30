<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %> 

<%@ page session="false" %>


<script type="text/javascript">


function buildProductsList(AddressList) {
		var table = document.getElementById("AddressTable");
		
		if(table.getElementsByTagName('tbody')[0] != null){
			table.removeChild(table.getElementsByTagName('tbody')[0]);
		}
		var tbody = document.createElement("tbody");	
		alert(AddressList.length);
		for (var i = 0; i < AddressList.length; i++) {			
			var tr = tbody.insertRow(i) ;
			var td = tr.insertCell(0) ;			
			
			td.innerHTML = '<a href="<c:url value="/shop/customer/editAddress.html?${address.id}"/>">${address.name}</a>';
			
			var tdname = tr.insertCell(1) ;
			tdname.innerHTML = '${address.company}';
			var tdqua = tr.insertCell(2);
			tdqua.innerHTML='${address.zone}';	
			var tdprice =tr.insertCell(3);
			tdprice.innerHTML = '${address.city}';
			var tdoperate = tr.insertCell(4);
			tdoperate.innerHTML = '${address.streetAdress}';
			var tdoperate1 = tr.insertCell(5);
			tdoperate1.innerHTML = '${address.postCode}';
			var tdoperate2 = tr.insertCell(6);
			tdoperate1.innerHTML = '${address.telephone}';
			var tdoperate3 = tr.insertCell(7);
			tdoperate1.innerHTML = '${address.memo}';
			var tdoperate4 = tr.insertCell(8);
			tdoperate1.innerHTML = '<a href="<c:url value="/shop/customer/removeAddress.html?${address.id}"/>"> <s:message code="label.generic.remove" text="Remove"/></a>';
			var tdoperate5 = tr.insertCell(9);
			tdoperate1.innerHTML = '<a href="<c:url value="/shop/customer/removeAddress.html?${address.id}"/>"> <s:message code="label.generic.default" text="Default"/><s:message code="label.customer.address" text="Address"/></a> ';
			var tdoperate6 = tr.insertCell(10);
			tdoperate1.innerHTML = '<a href="<c:url value="/shop/customer/removeAddress.html?${address.id}"/>"> <s:message code="label.customer.invoice.default" text="Default Invoice"/></a> </a> ';
		}
		
		if(tbody !=null){			
			table.appendChild(tbody); 
		}
		
}
</script>
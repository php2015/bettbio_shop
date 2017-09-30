<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %> 

<%@ page session="false" %>

/**
* Builds the product container div from the product list
**/
function buildProductsList(productList, divProductsContainer) {
		var table = document.getElementById("productsTable");
		//table.innerHTML='<thead><tr><th data-column-id="image" ><s:message code="label.product.image" text="Image" /></th>';
		if(table.getElementsByTagName('tbody')[0] != null){
			table.removeChild(table.getElementsByTagName('tbody')[0]);
		}
		var tbody = document.createElement("tbody");		
		for (var i = 0; i < productList.products.length; i++) {			
			var tr = tbody.insertRow(i) ;
			var td = tr.insertCell(0) ;			
			if(productList.products[i].image!=null) {
					    td.innerHTML = '<a href="<c:url value="/shop/product/" />' + productList.products[i].description.friendlyUrl + '.html<sm:breadcrumbParam/>">' + '<div style="min-width:100px;max-width:100px;min-height:100px;max-height:100px;"><img src="<c:url value="/"/>' + productList.products[i].image.imageUrl +'" height="100"></div></a>';
			}else{
					td.innerHTML = '<a href="<c:url value="/shop/product/" />' + productList.products[i].description.friendlyUrl + '.html<sm:breadcrumbParam/>">' + '<img src="<sm:storeLogo/>" style=" vertical-align:middle;"'+'></a>';
			}
			var tdname = tr.insertCell(1) ;
			tdname.innerHTML = productList.products[i].description.name;
			if(productList.products[i].description.simpleDescription != null){				
				if(productList.products[i].description.simpleDescription.length>300){
					productList.products[i].description.simpleDescription=productList.products[i].description.simpleDescription.substr(0,295)+"......";
				}
				tdname.innerHTML = tdname.innerHTML +'</br><div style="min-width:100px;max-width:500px;">'+productList.products[i].description.simpleDescription+'</div>';					
			}
			tdname.innerHTML = '<a href="<c:url value="/shop/product/" />' + productList.products[i].description.friendlyUrl + '.html<sm:breadcrumbParam/>">' + tdname.innerHTML + '</a>';
			var tdqua = tr.insertCell(2);
			if(productList.products[i].productCertificates == null && productList.products[i].productProofs  == null && productList.products[i].thirdProofs == null){
				tdqua.innerHTML='<s:message code="label.product.quality.none" text="None" />';			
			}else{
				if(productList.products[i].productCertificates!=null) {						
					var temphtml='<a href="javascript:void(0);" role="button" class="dropdown-toggle noboxshadow" data-toggle="dropdown"><s:message code="label.product.certificate" text="Certificate" /><b class="caret"></b></a>';
					temphtml = 	docinfo(productList.products[i].productCertificates,temphtml);		
					tdqua.innerHTML = temphtml;
				}
				if(productList.products[i].productProofs!=null) {						
					var temphtml='<a href="javascript:void(0);" role="button" class="dropdown-toggle noboxshadow" data-toggle="dropdown"><s:message code="label.product.proof" text="Proof" /><b class="caret"></b></a>';
					temphtml = 	proofinfo(productList.products[i].productProofs,temphtml);		
					tdqua.innerHTML = tdqua.innerHTML + temphtml;
				}
				if(productList.products[i].thirdProofs!=null) {						
					var temphtml='<a href="javascript:void(0);" role="button" class="dropdown-toggle noboxshadow" data-toggle="dropdown"><s:message code="label.product.thirdproof" text="ThirdProof" /><b class="caret"></b></a>';
					temphtml = 	thirdproof(productList.products[i].thirdProofs,temphtml);		
					tdqua.innerHTML = tdqua.innerHTML + temphtml;
				}
			}
			
			var tdprice =tr.insertCell(3);
			if(productList.products[i].discounted) {
					   tdprice.innerHTML =  '<h3><del>' + productList.products[i].originalPrice +'</del>&nbsp;<span class="specialPrice">' + productList.products[i].finalPrice + '</span></h3>';
			} else {
					    tdprice.innerHTML = '<h3>' + productList.products[i].finalPrice +'</h3>';
			}
			var tdoperate = tr.insertCell(4);
			tdoperate.innerHTML = '<a productid="' + productList.products[i].id + '" href="#" class="addToCart"><s:message code="button.label.addToCart" text="Add to cart" /></a>'
		}
		
		if(tbody !=null){			
			table.appendChild(tbody); 
			$('#total').html(productList.productCount);
		}
				
		initBindings();
}

	function docinfo(quaobj,objhtml){		
		var innertex='<div class="dropdown-menu">';
		innertex =innertex + '<table class="ezytable"><thead><tr><th><s:message code="label.product.certificate.name" text="Docment" /></th><th>';
		innertex =innertex +  '<s:message code="label.product.certificate.baseinfo" text="BasicInfo"/></th></tr></thead><tr><td>'+quaobj[0].name+'</td><td>'+quaobj[0].baseinfo+'</td></tr>';		
		for (var i = 1; i < quaobj.length; i++) {	
				innertex = innertex + '<tr><td>'+ quaobj[i].name+ '</td><td>'+ quaobj[i].baseinfo +'</td></tr>';
			}			
		objhtml =objhtml + innertex +'</table></div>' ;		
		objhtml = '<li class="dropdown" >'+ objhtml + '</li>';			
		return objhtml;
	}
	function proofinfo(quaobj,objhtml){		
		var innertex='<div class="dropdown-menu" >';
		innertex =innertex + '<table class="ezytable"><thead><tr><th><s:message code="label.product.proof.buyer" text="Buyer" /></th><th>';
		innertex =innertex +  '<s:message code="label.product.proof.dateBuyed" text="DateBuyed"/></th></tr></thead><tr><td>'+quaobj[0].buyer+'</td><td>'+quaobj[0].dateBuyed+'</td></tr>';		
		for (var i = 1; i < quaobj.length; i++) {				
				innertex = innertex + '<tr><td>'+ quaobj[i].buyer+ '</td><td>'+ quaobj[i].dateBuyed +'</td></tr>';
			}			
		objhtml =objhtml + innertex +'</table></div>' ;		
		objhtml = '<li class="dropdown" >'+ objhtml + '</li>';			
		return objhtml;
	}
	function thirdproof(quaobj,objhtml){		
		var innertex='<div class="dropdown-menu" >';
		innertex =innertex + '<table class="ezytable"><thead><tr><th><s:message code="label.product.thirdproof.thirddetection" text="Name" /></th><th>';
		innertex =innertex +  '<s:message code="label.product.thirdproof.description" text="Description"/></th></tr></thead><tr><td>'+quaobj[0].name+'</td><td>'+quaobj[0].description+'</td></tr>';		
		for (var i = 1; i < quaobj.length; i++) {				
				innertex = innertex + '<tr><td>'+ quaobj[i].name+ '</td><td>'+ quaobj[i].description +'</td></tr>';
			}			
		objhtml =objhtml + innertex +'</table></div>' ;		
		objhtml = '<li class="dropdown" >'+ objhtml + '</li>';			
		return objhtml;
	}
	function addtablerid(){		
		var innertex='<thead><tr><th data-column-id="image" ><s:message code="label.product.image" text="Image" /></th>';
		innertex = innertex +'<th data-column-id="name"><s:message code="label.productedit.productname" text="Name" /></th>';
		innertex = innertex +'<th data-column-id="quality"><s:message code="label.product.quality" text="Quality" /></th>';
		innertex = innertex +'<th data-column-id="price" data-type="numeric" data-order="desc"><s:message code="label.product.price" text="Price" /></th>';
		innertex = innertex +'<th data-column-id="operate"><s:message code="label.product.operate" text="Operate" /></th></tr></thead>';
		return innertex;
	}
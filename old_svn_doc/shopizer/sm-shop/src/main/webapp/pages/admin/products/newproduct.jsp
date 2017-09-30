<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>
<style>
.nodisplay {
	display:none;
}
</style>
<div>
  <!-- Nav tabs -->
	<ul class="nav nav-pills nav-tabs" role="tablist" id="myProductTab">
		<li role="presentation" class="active" id="li_details">
			<a href="#new_details" aria-controls="new_details" role="tab" data-toggle="tab"><s:message code="label.product.details" text="Product details" /></a>
		</li>
	    <li role="presentation" id="new_priceli">
	    	<a href="#new_prices" aria-controls="new_prices" role="tab" data-toggle="tab"><s:message code="label.product.prices" text="Product prices" /></a>
	    </li>
	    <li role="presentation" id="new_certsli">
	    	<a href="#new_certificates" aria-controls="new_certificates" role="tab" data-toggle="tab"><s:message code="label.product.certificates" text="Product certificates" /></a>
	    </li>
	    <li role="presentation">
	    	<a href="#new_proofs" aria-controls="new_proofs" role="tab" data-toggle="tab" id="new_proofsa"><s:message code="label.product.proofs" text="Product proofs" /></a>
	    </li>
	    <li role="presentation" id="new_thirdli">
	    	<a href="#new_thirdproofs" aria-controls="new_thirdproofs" role="tab" data-toggle="tab"><s:message code="label.product.thirdproofs" text="Product thirdproofs" /></a>
	    </li>
	    <li role="presentation" id="new_selfsli">
	    	<a href="#new_selfproofs" aria-controls="new_selfproofs" role="tab" data-toggle="tab"><s:message code="label.product.selfproofs" text="Product selfproofs" /></a>
	    </li>
	    <li role="presentation">
	    	<a href="#new_reviews" aria-controls="new_reviews" role="tab" data-toggle="tab"><s:message code="label.product.customer.reviews" text="Reviews" /></a>
	    </li>
	    <li role="presentation" id="editAudit">
	    	<a href="javascript:void(0);"    aria-controls="new_audit" role="tab" data-toggle="tab" id="newauditHref"><s:message code="button.label.submit2" text="Submit" /><s:message code="menu.audit" text="Audit" /></a>
	    </li>
	</ul> <!--End Nav tabs -->

<!-- Tab panes -->
  <div class="tab-content" id="tabpanes">
    <div role="tabpanel" class="tab-pane fade in active" id="new_details">
      <iframe src="" id="new_details_iframe" width="100%" height="450" isload="0" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="yes" allowtransparency="yes"></iframe>
    </div>
    <div role="tabpanel" class="tab-pane fade" id="new_prices">
      <iframe src="" id="new_prices_iframe" width="100%" height="450" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="yes" allowtransparency="yes"></iframe>
    </div>
    <div role="tabpanel" class="tab-pane fade" id="new_certificates">
      <iframe src="" id="new_certificates_iframe" width="100%" height="450" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="yes" allowtransparency="yes"></iframe>
    </div>
    <div role="tabpanel" class="tab-pane fade" id="new_proofs">
      <iframe src="" id="new_proofs_iframe" width="100%" height="450" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="yes" allowtransparency="yes"></iframe>
    </div>
    <div role="tabpanel" class="tab-pane fade" id="new_thirdproofs">
      <iframe src="" id="new_thirdproofs_iframe" width="100%" height="450" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="yes" allowtransparency="yes"></iframe>
    </div>
    <div role="tabpanel" class="tab-pane fade" id="new_selfproofs">
      <iframe src="" id="new_selfproofs_iframe" width="100%" height="450" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="yes" allowtransparency="yes"></iframe>
    </div>
    <div role="tabpanel" class="tab-pane fade" id="new_reviews">
      <iframe src="" id="new_reviews_iframe" width="100%" height="450" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="yes" allowtransparency="yes"></iframe>
    </div>
  </div><!-- End Tab panes -->
  <input type="hidden" id="new_pid"/>
  <jsp:include page="/pages/admin/products/productModal.jsp" />
</div>  

<script>

function loadProductTab(type) {
	var fid = $("#new_pid").val();
	var name = $("#producttitle").val();
	
	var uri = '<c:url value="/shop/product/'+fid+'.html"/>';
			
	var url = "";
	if(fid==null||fid==''){
		
		$("#myProductTab li[role='presentation']").each(function(){
			$(this).css("display", "none");
		});
		$("#myProductTab li[id='li_details']").css("display", "block");
	} else {		
		$("#myProductTab li[role='presentation']").each(function(){
			$(this).css("display", "block");
		});
		if(type==3){
			$("#new_certificates").css('display','');						
			$("#new_selfproofs").css('display','');
			$("#new_thirdproofs").css('display','none');
			$("#new_prices").css('display','none');
			$("#new_priceli").hide();			
			$("#new_certsli").show();
			$("#new_selfsli").show();
			$("#new_thirdli").hide();	
			$("#producttitles").html(""+name+"").attr('href',''+uri+'');
			$("#sp_pro").html("(预览)").attr('href',''+uri+'');	
			//$("#new_proofsa").html('<s:message code="label.service.proofs" text="Product proofs" />');
		}else if(type==2){
			$("#new_prices").css('display','');
			$("#new_certificates").css('display','none');
			$("#new_selfproofs").css('display','none');	
			$("#new_thirdproofs").css('display','');
			$("#new_certsli").hide();
			$("#new_selfsli").hide();
			$("#new_thirdli").show();
			$("#producttitles").html(""+name+"").attr('href',''+uri+'');
			$("#sp_pro").html("(预览)").attr('href',''+uri+'');
			//$("#new_proofsa").html('<s:message code="label.product.proofs" text="Product proofs" />');
		}else{
			$("#new_prices").css('display','');
			$("#new_certificates").css('display','');
			$("#new_selfproofs").css('display',''); 
			$("#new_thirdproofs").css('display','');
			$("#new_priceli").show();			
			$("#new_certsli").show();
			$("#new_selfsli").show();
			$("#new_thirdli").show();
			$("#producttitles").html(""+name+"").attr('href',''+uri+'');
			$("#sp_pro").html("(预览)").attr('href',''+uri+'');
			$("#new_proofsa").html('<s:message code="label.product.proofs" text="Product proofs" />');
		}
		$("#newauditHref").html('<a href="#"  onclick="confrom('+fid+')" aria-controls="new_audit" role="tab" data-toggle="tab" id="newauditHref"><s:message code="button.label.submit2" text="Submit" /><s:message code="menu.audit" text="Audit" /></a>');
		 

	}
	$('#myProductTab a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
		//e.target // newly activated tab
		//e.relatedTarget // previous active tab
		
		var o = $(this).attr("aria-controls");
		var fid = $("#new_pid").val();
		var url = "";
		if(fid==null||fid==''){
			$("#myProductTab li").addClass("nodisplay");
			$("#myProductTab li[id='li_details']").addClass("nodisplay");
		}
		if (o == "new_details") {
		  url = '<c:url value="/admin/products/editProduct.html" />';
		} else if (o == "new_prices"){
		  url = '<c:url value="/admin/products/prices.html" />'; 
		} else if (o == "new_certificates"){
		  url = '<c:url value="/admin/products/certificates.html" />'; 
		} else if (o == "new_proofs"){
		  url = '<c:url value="/admin/products/proofs.html" />'; 
		} else if (o == "new_thirdproofs"){
		  url = '<c:url value="/admin/products/thirdproofs.html" />'; 
		} else if (o == "new_selfproofs"){
		  url = '<c:url value="/admin/products/selfproofs.html" />'; 
		} else if (o == "new_reviews"){
		  url = '<c:url value="/admin/products/reviews.html" />'; 
		} 
		url += '?id=' + fid;		
		 $("#"+o+"_iframe").attr("src",url);
		 
	  });
}
function iFrameHeight(obj) {
  var ifm= document.getElementById(obj.id);
  var subWeb = document.frames ? document.frames[obj.id].document :ifm.contentDocument;
  if(ifm != null && subWeb != null && $(ifm).attr("isload")!="1") {
    ifm.height = subWeb.body.scrollHeight + 300;
    $(ifm).attr("isload","1");
  }
}

</script>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>
<div>
  <!-- Nav tabs -->
	<ul class="nav nav-pills nav-tabs" role="tablist" id="myTab">
		<li role="presentation" class="active">
			<a href="#details" aria-controls="details" role="tab" data-toggle="tab"><s:message code="label.product.details" text="Product details" /></a>
		</li>
	    <li role="presentation" id="pricesli">
	    	<a href="#prices" aria-controls="prices" role="tab" data-toggle="tab"><s:message code="label.product.prices" text="Product prices" /></a>
	    </li>
	    <li role="presentation" id="certsli">
	    	<a href="#certificates" aria-controls="certificates" role="tab" data-toggle="tab"><s:message code="label.product.certificates" text="Product certificates" /></a>
	    </li>
	    <li role="presentation">
	    	<a href="#proofs" aria-controls="proofs" role="tab" data-toggle="tab" id="proofsa"><s:message code="label.product.proofs" text="Product proofs" /></a>
	    </li>
	    <li role="presentation" id="thirdli">
	    	<a href="#thirdproofs" aria-controls="thirdproofs" role="tab" data-toggle="tab"><s:message code="label.product.thirdproofs" text="Product thirdproofs" /></a>
	    </li>
	    <li role="presentation" id="selfsli">
	    	<a href="#selfproofs" aria-controls="selfproofs" role="tab" data-toggle="tab"><s:message code="label.product.selfproofs" text="Product selfproofs" /></a>
	    </li>
	    <li role="presentation">
	    	<a href="#reviews" aria-controls="reviews" role="tab" data-toggle="tab"><s:message code="label.product.customer.reviews" text="Reviews" /></a>
	    </li>
        <li id="editAudit" role="presentation">
        </li>
      	</ul> <!--End Nav tabs -->

<!-- Tab panes -->
  <div class="tab-content" id="tabpanes">
    <div role="tabpanel" class="tab-pane fade in active" id="details">
      <iframe src="" id="details_iframe" width="100%" height="450" isload="0" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="yes" allowtransparency="yes"></iframe>
    </div>
    <div role="tabpanel" class="tab-pane fade" id="prices">
      <iframe src="" id="prices_iframe" width="100%" height="450" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="yes" allowtransparency="yes"></iframe>
    </div>
    <div role="tabpanel" class="tab-pane fade" id="certificates">
      <iframe src="" id="certificates_iframe" width="100%" height="450" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="yes" allowtransparency="yes"></iframe>
    </div>
    <div role="tabpanel" class="tab-pane fade" id="proofs">
      <iframe src="" id="proofs_iframe" width="100%" height="450" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="yes" allowtransparency="yes"></iframe>
    </div>
    <div role="tabpanel" class="tab-pane fade" id="thirdproofs">
      <iframe src="" id="thirdproofs_iframe" width="100%" height="450" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="yes" allowtransparency="yes"></iframe>
    </div>
    <div role="tabpanel" class="tab-pane fade" id="selfproofs">
      <iframe src="" id="selfproofs_iframe" width="100%" height="450" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="yes" allowtransparency="yes"></iframe>
    </div>
    <div role="tabpanel" class="tab-pane fade" id="reviews">
      <iframe src="" id="reviews_iframe" width="100%" height="450" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="yes" allowtransparency="yes"></iframe>
    </div>
  </div><!-- End Tab panes -->
  <input type="hidden" id="edit_pid"/>
  <input type="hidden" id="edit_audit"/>
  <jsp:include page="/pages/admin/products/productModal.jsp" />
</div>  
<script>
$(function(){
	$('#editproduct').on('hidden.bs.modal', function (e) {
		doAction(page);
		})
  //init product details info
	//active css of menu 
  $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
	//e.target // newly activated tab
	//e.relatedTarget // previous active tab
	var o = $(this).attr("aria-controls");
	var fid = $("#edit_pid").val();	
	var url = "";
	if (o == "details") {
	  url = '<c:url value="/admin/products/editProduct.html" />';
	} else if (o == "prices"){
	  url = '<c:url value="/admin/products/prices.html" />'; 
	} else if (o == "certificates"){
	  url = '<c:url value="/admin/products/certificates.html" />'; 
	} else if (o == "proofs"){
	  url = '<c:url value="/admin/products/proofs.html" />'; 
	} else if (o == "thirdproofs"){
	  url = '<c:url value="/admin/products/thirdproofs.html" />'; 
	} else if (o == "selfproofs"){
	  url = '<c:url value="/admin/products/selfproofs.html" />'; 
	} else if (o == "reviews"){
	  url = '<c:url value="/admin/products/reviews.html" />'; 
	} 
	url += '?id=' + fid;
	$("#"+o+"_iframe").attr("src",url);
  });
});
function iFrameHeight(obj) {
  var ifm= document.getElementById(obj.id);
  var subWeb = document.frames ? document.frames[obj.id].document :ifm.contentDocument;
  if(ifm != null && subWeb != null && $(ifm).attr("isload")!="1") {
    ifm.height = subWeb.body.scrollHeight + 300;
    $(ifm).attr("isload","1");
  }
}
function closemodal() {
	$("#edit_pid").val(''); //clear product id
	$("#edit_audit").val('');
	$("#editproduct").modal("hide");
}

</script>

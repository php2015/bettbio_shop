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
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %> 
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<script src="<c:url value="/resources/js/jquery.elevateZoom-3.0.8.min.js" />"></script>
<script src="<c:url value="/resources/js/product-view.js"/>"></script>
<script src="<c:url value="/resources/js/stickUp.min.js"/>"></script>
<script src="<c:url value="/resources/js/jquery.elevateZoom-3.0.8.min.js" />"></script>
<script src="<c:url value="/resources/js/jquery.raty.min.js" />"></script>
<script src="<c:url value="/resources/js/bootstrap-datetimepicker.min.js" />"></script>
<script src="<c:url value="/resources/js/bootstrap-datetimepicker.zh-CN.js" />"></script>
<link href="<c:url value="/resources/css/bootstrap-datetimepicker.min.css" />" rel="stylesheet"></link>
<br>
	<div class=" row">
	<div class="col-sm-12">非试剂类的产品有额外的加分，种子用户的商品加50分，非种子用户加30分</div>
	<div class="col-sm-12">
		<div class="col-sm-2 col-sm-offset-0"><a class="btn btn-info" href="<c:url value="/admin/audit/auditManagement.html"/>"><s:message code="label.bettbio.product.audit.back" text="Back" /></a></div>
		<div class="col-sm-8" id="audit-result" style="color:red"></div>
		<div class="col-sm-2"><a class="btn btn-info" href="<c:url value="/admin/audit/audit.html"/>"><s:message code="label.generic.next" text="Next" /></a></div>		
	</div>
	</div>
<hr style="height:2px;border:none;border-top:1px solid #3498DB; ">
<div  class=" product-header" id="view">

	<div class=" row">
	
	<c:if test="${product!=null}">
	<c:set value="${product.id}" var="productId" scope="request"/>
	<div id="img" class="col-sm-4 col-md-4 productMainImage" style="padding:0px;">
		<c:if test="${product.image!=null}">
			<span id="mainImg"><img class="product-img" style="width:100%" id="im-<c:out value="${product.image.id}"/>" alt="<c:out value="${product.description.name}"/>" src="<c:url value="${product.image.imageUrl}"/>" data-zoom-image="<sm:shopProductImage imageName="${product.image.imageName}" sku="${product.sku}" size="LARGE"/>"></span>												
			<script>
				$(function() {
					setImageZoom('im-<c:out value="${product.image.id}"/>');
				});	
			</script>
			<c:if test="${product.images!=null && fn:length(product.images)>1}">
				<div class="container-fluid">
				<div id="imageGallery" class="row">
					<c:forEach items="${product.images}" var="thumbnail">								
					<div class="col-md-3" style="padding:0px;">
						<a href="#img" class="thumbImg" title="<c:out value="${thumbnail.imageName}"/>">
							<img class="product-img" style="width:70px;" id="im-<c:out value="${thumbnail.id}"/>" src="<c:url value="${thumbnail.imageUrl}"/>" data-zoom-image="<sm:shopProductImage imageName="${thumbnail.imageName}" sku="${product.sku}" size="LARGE"/>" alt="<c:url value="${thumbnail.imageName}"/>"></a>
					</div>
					</c:forEach>								
				</div>
				</div>
			</c:if>
		</c:if>
	</div><!--end Image column -->
	<div class="col-sm-8 col-md-8">
				<div class="container-fluid">
					<div class="row">
						<div class="col-sm-12">
							<span style="margin-left:-10px;font-weight:bold;font-size:20px;color:#123;line-height:1.5em">${product.description.name}</span>
							<c:if test="${not empty product.description.enName }">
								<br/><s:message code="label.productedit.productenname" text="English Name"/>&nbsp;:&nbsp;${product.description.enName}
							</c:if>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12 product-title">
							<span style="color:#E3393C;word-break: break-all;line-height:20px">${product.description.simpleDescription}</span>
						</div>
					</div>					
					
					<div class="row">
						<div class="col-sm-12 product-title">
							<label><s:message code="label.bettbio.product.pattern" text="Contact Pattern"/>: <c:choose>
	                   	    	<c:when test="${product.productIsFree==true }">
	                   	    		<s:message code="label.bettbio.product.free" text="Product Free"/>
	                   	    	</c:when>
	                   	    	<c:otherwise>
	                   	    		<s:message code="label.bettbio.product.charge" text="Product Charge"/>,<span id="dateChargeBegin">${product.dateChargeBegin}</span>---<span id="dateChargeBegin">${product.dateChargeEnd}
	                   	    	</c:otherwise>
	                   	    	</c:choose>
                   	    	</label>
						</div>
					</div>
					<c:if test="${not empty product.description.storecondDescription }">
						<div class="row">
						<div class="col-sm-12 product-title">
							<label><s:message code="label.productedit.productstoreconddesc" text="Contact Pattern"/>:</label><span itemprop="brand" style="color:#000"> ${product.description.storecondDescription}</span>
                   	    	
						</div>
					</div>
					</c:if>
					<div class="row">
						<div class="col-sm-12 product-title">
							<address>
								<strong style="color:#000"><s:message code="label.product.brand" text="Brand"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong> <span itemprop="brand" style="color:#000"><c:out value="${product.manufacturer.description.name}" /></span><br>
								<strong style="color:#000"><s:message code="label.product.code" text="Product code"/></strong> <span itemprop="identifier" style="color:#000" content="mpn:${product.sku}">${product.sku}</span><br>								
							</address>
						</div>
					</div>
					<!-- begin product prices block -->
					<div class="row">
						<div class="col-sm-12">
							<table class="table table-bordered table-hover table-condensed product-table" style="font-size:12px;">
								<tr style="font-weight:bold;background-color: #3498db;color:#fff">
									<td><s:message code="label.product.specification" text="Specification"/></td>
									<td><s:message code="label.order.price" text="Price"/></td>
									<td><s:message code="label.bettbio.product.discount.price" text="Off"/></td>
									<td><s:message code="label.product.price.period" text="Period"/></td>
								</tr>
								<c:forEach items="${product.prices }" var="price" varStatus="vstatus">
									<c:choose>
						                <c:when test="${vstatus.index%2==0}">
						      				<tr class="info" >
						                </c:when>
						            </c:choose>
						            	<td style="line-height:25px!important">${price.name }</td>
										<td style="line-height:25px!important">
											<c:choose>
												<c:when test="${not empty price.special}">
													<s>${price.price }</s>
												</c:when>
												<c:when test="${price.priceValue == '0'}">
													<span style="color:blue;"><s:message code="label.bettbio.product.ask.price" text="Ask"/></span>
												</c:when>
												<c:otherwise>
													${price.price }
												</c:otherwise>
											</c:choose>
										</td>
										<td style="line-height:25px!important">${price.special }</td>
										<td style="line-height:25px!important">${price.period }</td>
									</tr>
								</c:forEach>
							</table>
						</div>
					</div><!-- end product prices block -->
					
				</div>
				
			</div>
	</div>		
	<div class="product-main">
		<!-- begin product-main navigator -->
		<div class="navbar-wrapper">	
			<div class="container-fluid">
				<ul class="nav nav-tabs nav-pills nav-justified">
				  <li role="presentation" class="menuItem active"><a href="#description"><s:message code="label.productedit.productdesc" text="Desciption"/></a></li>
				  <li role="presentation" class="menuItem"><a href="#test"><s:message code="label.productedit.producttestdesc" text="Desciption"/>/<s:message code="label.product.selfproof" text="Desciption"/></a></li>
				  <li role="presentation" class="menuItem"><a href="#certificate"><s:message code="label.product.certificate" text="Certificate"/></a></li>
				  <li role="presentation" class="menuItem"><a href="#thirdproof"><s:message code="label.product.thirdproof" text="Thirdproof"/></a></li>
				  <li role="presentation" class="menuItem"><a href="#proof"><s:message code="label.product.customer.reviews" text="Reviews"/></a></li>
				</ul>	
			</div>
		</div><!-- end product-main navigator -->
	<!-- begin product-main content -->
	<div>
		<!-- begin description block -->
		<div id="description" class="productdesc featurette">
			<div>
				<div class="productdesc-title"><s:message code="label.productedit.productdesc" text="Desciption"/></div>
			</div>
			<div>
			${product.description.description}
			</div>
		</div><!-- end description block -->
		
		<!-- begin test block -->
		<div id="test" class="productdesc featurette productdesc-even-background">
			<div>
				<div class="productdesc-title"><s:message code="label.productedit.producttestdesc" text="Desciption"/>/<s:message code="label.product.selfproof" text="Desciption"/></div>
			</div>
			<div>
			<c:if test="${not empty  product.selfProofs}">
				<div class="row">
					<c:forEach items="${product.selfProofs }" var="selfProof" varStatus="vstatus">
					<div class="col-sm-12" style="font-weight:bold;font-size:14px;">
					    <div><s:message code="label.generic.description" text="Desciption"/>：<br/><div style="font-weight:normal;">${selfProof.description }</div></div>
						<div>
							<a href="<c:url value="${selfProof.rimage}"/>" target="blank">
								<img style="width:80%" src="<c:url value="${selfProof.rimage}"/>"/>
							</a>
						</div>
					</div>
					</c:forEach>
				</div>
			</c:if>
			</div>
		</div><!-- end test block -->
		
		
		<!-- begin certificate block -->
		<div id="certificate" class="productdesc featurette productdesc-even-background">
			<div>
				<div class="productdesc-title"><s:message code="label.product.certificate" text="Certificate"/></div>
			</div>
			<div style="margin:0px 20px;">
				<c:choose>
					<c:when test="${not empty  product.productCertificates}">
						<div class="row">
							<c:forEach items="${product.productCertificates }" var="cert" varStatus="vstatus">
							<div class="col-sm-6" style="font-weight:bold;font-size:14px;">
								<div><a href="${cert.docUrl }" style="color:#FF6C00"><s:message code="label.product.certificate.name" text="Certificate"/>:${cert.displayName}</a></div>
								<div><s:message code="label.product.certificate.baseinfo" text="Certificate"/>:${cert.baseinfo }</div>
								<div><s:message code="label.product.certificate.description" text="Certificate"/>:<br/><div style="font-weight:normal;">${cert.displayDesc }</div></div>
								<div>
									<a href="<c:url value="${cert.rimage}"/>" target="blank">
										<img style="width:80%" alt="<c:out value="${cert.name}"/>" src="<c:url value="${cert.rimage}"/>"/>
									</a>
								</div>
							</div>
							</c:forEach>
						</div>
					</c:when>
					<c:otherwise>
						<div><s:message code="label.product.quality.none" text="None"/></div>
					</c:otherwise>
				</c:choose>
			</div>
		</div><!-- end certificate block -->
		
		<!-- begin thirdproof block -->
		<div id="thirdproof" class="productdesc featurette">
			<div>
				<div class="productdesc-title"><s:message code="label.product.thirdproof" text="Thirdproof"/></div>
			</div>
			<div style="margin:0px 20px;">
				<c:choose>
					<c:when test="${not empty  product.thirdProofs}">
						<div class="row">
							<c:forEach items="${product.thirdProofs }" var="thirdProof" varStatus="vstatus">
							<div class="col-sm-6" style="font-weight:bold;font-size:14px;">
								<div><s:message code="label.product.thirdproof.thirddetection" text="Thirdproof"/>:${thirdProof.displayName }</div>
								<div><s:message code="label.product.thirdproof.description" text="Thirdproof"/>:<br/><div style="font-weight:normal;">${thirdProof.description }</div></div>
								<div>
									<a href="<c:url value="${thirdProof.rimage}"/>" target="blank">
										<img style="width:80%" alt="<c:out value="${thirdProof.name}"/>" src="<c:url value="${thirdProof.rimage}"/>"/>
									</a>
								</div>
							</div>
							</c:forEach>
						</div>
					</c:when>
					<c:otherwise>
						<div><s:message code="label.product.quality.none" text="None"/></div>
					</c:otherwise>
				</c:choose>
				
			</div>
		</div><!-- end thirdproof block -->
		
		<!-- begin proof block -->
		<div id="proof" class="productdesc featurette productdesc-even-background">
			<div>
				<div class="productdesc-title"><s:message code="label.product.proof" text="Thirdproof"/></div>
			</div>
			<div style="margin:0px 20px;">
				<c:choose>
					<c:when test="${not empty  product.productProofs}">
						<div class="row">
							<c:forEach items="${product.productProofs }" var="proof" varStatus="vstatus">
							<div class="col-sm-6" style="font-weight:bold;font-size:14px;">
								<div><s:message code="label.product.proof.buyer" text="Thirdproof"/>:${proof.buyer }</div>
								<div><s:message code="label.product.proof.dateBuyed" text="Thirdproof"/>${proof.dateBuyed }</div>
								<div><s:message code="label.product.proof.description" text="Thirdproof"/>:<br/><div style="font-weight:normal;">${proof.description }</div></div>
								<div>
									<a href="<c:url value="${proof.rimage}"/>" target="blank">
										<img style="width:80%" alt="<c:out value="${proof.displayName}"/>" src="<c:url value="${proof.rimage}"/>"/>
									</a>
								</div>
							</div>
							</c:forEach>
						</div>
					</c:when>
					<c:otherwise>
						<div><s:message code="label.product.quality.none" text="None"/></div>
					</c:otherwise>
				</c:choose>
				
			</div>
		</div><!-- end proof block -->
		
	</div><!-- end product-main content -->
	
	</div>
	</c:if>
	<hr style="height:2px;border:none;border-top:1px solid #3498DB; ">
	<div class="pull-right col-sm-12 col-sm-offset-0"><button type="button" class="btn btn-info btn-lg btn-block" id="audit-button" onclick="getAuditInfo(${product.productIsFree },${product.audit })"><s:message code="menu.audit" text="Audit" /></button></div>		

</div>
<div class="modal fade" id="aduit" tabindex="-1" role="dialog"  data-backdrop= "static" aria-hidden="true">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 id="cartModalLabel"><img src="<c:url value="/resources/img/important-icon.png"/>" width="40"/>&nbsp;<strong id="modeltitle"><s:message code="menu.audit" text="Audit"/></strong></h4>
	      </div>
	      <div class="modal-body" id="modal-body">
	      	<div id="period">
	      	</div>
	      	<div id="quality">
	      	</div>
	      </div>
	      <div class="modal-footer">
	       		 <a href="javascript:void(0);"  onclick="doSubmit(${product.productIsFree },${product.audit })" id="splitSubOrderSubmit" class="btn" ><strong><s:message code="button.label.submit2" text="Submit"/></strong></a>
				<a href="#" class="btn" data-dismiss="modal"><strong><s:message code="cart.close" text="Close"/></strong></a>
	      </div>
	    </div>
	  </div>
</div>
<script>
jQuery(function($) {
	
  $(document).ready( function() {
      pid=${product.id};
	  
    $('.navbar-wrapper').stickUp({
       parts: {
         0:'description',
         1:'test',
         2: 'certificate',
         3: 'thirdproof',
         4: 'proof',
         5: 'reviews'
       },
       itemClass: 'menuItem',
       itemHover: 'active'
     });
  });
});	

function writeNonFree(isfree){
	var bdate = new Date('${product.dateChargeBegin}').pattern("yyyy-MM-dd");
	if(bdate=="NaN-aN-aN"){
		bdate = new Date().pattern("yyyy-MM-dd");
	}
	var edate =new Date('${product.dateChargeEnd}').pattern("yyyy-MM-dd");
	if(edate=="NaN-aN-aN"){
		edate = new Date().pattern("yyyy-MM-dd");
	}	
	var body='<div class="row"><div class="col-sm-12"><h3>';
	if(isfree==false){
		body +='<s:message code="label.bettbio.product.charge" text="Charge"/>';
	}else{
		body +='<s:message code="label.bettbio.product.free" text="Charge"/>';
	}
	body += '<s:message code="label.generic.confirm" text="Time"/><s:message code="label.bettbio.product.charge.perior" text="Time"/></h3>';
	body +='<label ><s:message code="label.bettbio.time.start" text="start"/></label><div class="input-group date form_date " data-date="" data-date-format="yyyy MM dd " data-link-field="dateStart" data-link-format="yyyy-mm-dd"><input id="dateStart" class="form-control" size="16" type="text" value='+bdate+' readonly><span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span><span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span></div>';
	body +='<label ><s:message code="label.bettbio.time.end" text="start"/></label><div class="input-group date form_date " data-date="" data-date-format="yyyy MM dd " data-link-field="dateEnd" data-link-format="yyyy-mm-dd"><input id="dateEnd" class="form-control" size="16" type="text" value='+edate+' readonly><span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span><span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span></div>';
	body +='</div>';	
	$('#period').html(body);
	$('#aduit').modal('show');
	$('.form_date').datetimepicker({
        language:  'zh-CN',
        weekStart: 1,
        todayBtn:  1,
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		minView: 2,
		forceParse: 0
    });
}
function writeChageResult(){
	var body='<h3><s:message code="label.bettbio.product.charge" text="charge"/><s:message code="label.bettbio.product.after.audit" text="audit"/><s:message code="label.bettbio.product.charge.perior" text="Time"/>:'+$("#dateStart").val()+'---'+$("#dateEnd").val();
	body +='<s:message code="label.product.quality" text="charge"/><s:message code="label.bettbio.product.quality.point" text="audit"/>:'+$('#score').html()+'</h3>';
	$('#audit-result').html(body);
	
}
function writeFreeResult(result){
	var re =parseInt(result)
	var per='';
	var body='';
	if(re==0){
		body='<h3><s:message code="label.bettbio.product.failed.audit"/>:';
		
	}else if(re==1){
		body='<h3><s:message code="label.bettbio.product.after.audit" text="charge"/>:';
		per='<s:message code="label.bettbio.product.charge.perior" text="Time"/>:'+$("#dateStart").val()+'---'+$("#dateEnd").val();
	}
	body +='<s:message code="label.product.quality" text="charge"/><s:message code="label.bettbio.product.quality.point" text="audit"/>:'+$('#score').html()+per+'</h3>';
	$('#audit-result').html(body);
	
}
function getEnable(point,qutext,qa){
	var text;
	if(parseInt(point)>0){
		text= '<div class="col-sm-3 "><label>'+qutext+'</label><input type="checkbox" id='+qa+' class="everycheckbox" checked="checked"/></div>';
	}else{		
		text='';
		lessName[lessName.length]=qa;
	}	
	return text;
}
function writeQuality(quality){
	var body='<div class="row"><div class="col-sm-12"><h3><s:message code="label.generic.confirm" text="Time"/><s:message code="menu.catalogue-products" text="Time"/><s:message code="label.product.quality" text="Time"/></h3>';
	body +='<div class="col-sm-5 col-sm-offset-1"><s:message code="label.product.certificate" text="Time"/><s:message code="label.bettbio.product.quality.point"/>:</div>';
	body +='<div class="col-sm-3 " id="score_doc">'+quality.doc+'</div>';
	body +=getEnable(quality.doc,'<s:message code="label.bettbio.product.quality.enable" text="Time"/>',"doc");
	body +='<div class="col-sm-5 col-sm-offset-1"><s:message code="label.product.thirdproof" text="Time"/><s:message code="label.bettbio.product.quality.point"/>:</div>';
	body +='<div class="col-sm-3" id="score_third">'+quality.third+'</div>';
	body +=getEnable(quality.third,'<s:message code="label.bettbio.product.quality.enable" text="Time"/>',"third");
	body +='<div class="col-sm-5 col-sm-offset-1"><s:message code="label.product.proof" text="Time"/><s:message code="label.bettbio.product.quality.point"/>:</div>';
	body +='<div class="col-sm-3" id="score_proof">'+quality.proof+'</div>';
	body +=getEnable(quality.proof,'<s:message code="label.bettbio.product.quality.enable" text="Time"/>',"proof");
	body +='<div class="col-sm-5 col-sm-offset-1"><s:message code="label.product.selfproof" text="Time"/><s:message code="label.bettbio.product.quality.point"/>:</div>';
	body +='<div class="col-sm-3 " id="score_self">'+quality.self+'</div>';
	if(quality.free !=null && quality.free==false){
		body += '<div class="col-sm-3 "><label>'+quality.self+'</label></div>';
	}else{
		body +=getEnable(quality.self,'<s:message code="label.bettbio.product.quality.enable" text="Time"/>',"self");
	}
	
	body +='<div class="col-sm-5 col-sm-offset-1"><s:message code="label.product.quality" text="Time"/><s:message code="label.bettbio.product.quality.point"/>:</div>';
	body +='<div class="col-sm-3 " id="score">'+quality.score+'</div><div class="col-sm-2 "></div>';
	$('#quality').html(body);
	$('#aduit').modal('show');
	 $('.everycheckbox').click(function(){
		 if(quality.free !=null && quality.free==false){
			 recuculc(quality.self); 
		 }else{
			 recuculc(0); 
		 }
		
	    }); 
}

Date.prototype.pattern=function(fmt) {        
    var o = {        
    "M+" : this.getMonth()+1, //月份        
    "d+" : this.getDate(), //日        
    "h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时        
    "H+" : this.getHours(), //小时        
    "m+" : this.getMinutes(), //分        
    "s+" : this.getSeconds(), //秒        
    "q+" : Math.floor((this.getMonth()+3)/3), //季度        
    "S" : this.getMilliseconds() //毫秒        
    };        
    var week = {        
    "0" : "\u65e5",        
    "1" : "\u4e00",        
    "2" : "\u4e8c",        
    "3" : "\u4e09",        
    "4" : "\u56db",        
    "5" : "\u4e94",        
    "6" : "\u516d"       
    };        
    if(/(y+)/.test(fmt)){        
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));        
    }        
    if(/(E+)/.test(fmt)){        
        fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "\u661f\u671f" : "\u5468") : "")+week[this.getDay()+""]);        
    }        
    for(var k in o){        
        if(new RegExp("("+ k +")").test(fmt)){        
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));        
        }        
    }        
    return fmt;        
}               

</script>
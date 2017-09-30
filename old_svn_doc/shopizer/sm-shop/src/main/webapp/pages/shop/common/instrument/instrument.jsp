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
<link href="<c:url value="/resources/templates/bootstrap3/css/spinner.css" />" rel="stylesheet">
<script src="<c:url value="/resources/templates/bootstrap3/js/spinner.js"/>"></script>
<script src="<c:url value="/resources/js/product.js"/>"></script>
<script src="<c:url value="/resources/js/jquery.raty.min.js" />"></script>
<script type="text/javascript">
    $(function(){
        
        $(window).scroll(function(){
        	var dtop=$("#detailMenu").offset().top;
        	var scroll_top = $(document).scrollTop();//滚动条到顶部的垂直高度
        	if(scroll_top>=dtop){
        		if($("#detailMenu").hasClass("removeDetailMenu")){
        			$("#detailMenu").removeClass("removeDetailMenu");
        			$("#detailMenu").addClass("detailMenu");	
        		}
        	}
        	var ctop=$("#tab-content").offset().top;
        	if(scroll_top<ctop){
        		if($("#detailMenu").hasClass("detailMenu")){
        			$("#detailMenu").removeClass("detailMenu");	
        			$("#detailMenu").addClass("removeDetailMenu");
        		}
        	}
        	if ($(window).scrollTop()>100){
                $("#back-to-top").fadeIn(1500);
            }
            else
            {
                $("#back-to-top").fadeOut(1500);
            }
        	
            
        });
        $("#back-to-top").fadeOut(1500);
        $("#back-to-top").click(function(){
            $('body,html').animate({scrollTop:0},1000);
            return false;
        });    
          
        $(window).load(function(){
            $(".breadcrumb>b:eq(0)").hide();
        });
        //show cookie
        
        var json = eval("("+$.cookie("history")+")");
        if(json !=null && json.length>0){
        	 var list = '<table>'; 
             for(var i=0; i<json.length;i++){         	
             	list +='<tr style="border-top:1px solid #d9d9d9;"><td valign="top" style="height:84px;"><img style="background-color:#fafafa;max-width:70px;max-height:70px;10px 10px;width:68px;height:68px;" src="/sm-shop'+json[i].img+'"></td>';
             	list +='<td valign="top" style="font-size:12px;padding:10px 10px;"><a target="_blank"  href="<c:url value="/shop/product/"/>' +json[i].id + '.html">'+json[i].name+'</a><br>';
             	var star =(json[i].quilty)/20;
             	var left=(json[i].quilty)%20
                for(var j=1;j<star;j++){
             		list +='<img style="max-width:15px;max-height:15px;padding:2px 0px;"  src="<c:url value="/resources/img/stars/star-on.png" />">';
             	}
             	if(left>0){
             		list +='<img style="max-width:15px;max-height:15px;padding:2px 0px;"  src="<c:url value="/resources/img/stars/star-half.png" />">	';
             	}
             	list +='</td></tr>';           
             }
             list +='</table>'
             $("#hisbrow").html(list); 
        }
    });
  function writeBread(bread){
	var tbBody='';
	if(bread != null){
		$.each(bread, function(i, p) {
			tbBody+='<b>&gt;</b> <li class="active" ><a href="'+p.url+'">'+p.label+'</a></li>';
		});
	}
	
	$("#breadcrumb").html(tbBody);
}	
</script>
<!-- begin body -->
<div style="width:1200px;border: 0px solid #804040;margin-left: auto;margin-right: auto;padding:20px 0px;">
    <div class="row" style="margin-top: -30px;width: 1200px;" >
		<ul class="breadcrumb" style="background-color:transparent !important; box-shadow: none;padding-top:30px;">
		  <c:forEach items="${requestScope.BREADCRUMB.breadCrumbs}" var="breadcrumb" varStatus="count">
			 <b>&gt;</b> <li class="active">
			    <a href="${breadcrumb.url}">${breadcrumb.label}</a>
			  </li>
		  </c:forEach>
		</ul>
	</div>
	<table style="width:100%;" border="0" cellpadding="0" cellspacing="0">
	   <tr>
	    <td>
			
	     </td>
	   </tr>
		<tr>
			<td valign="top">
			  <div id="iamges" style="width:352px;height: 352px;">
				<c:choose>
					<c:when test="${product.image!=null}">
					<a href="<c:url value="/shop/dispalyImage.html?ipath=${product.image.imageUrl}"/>" target="blank">
					 	 <img style="max-width:350;max-height:350px;padding-top:35px" alt="<c:out value="${product.description.name}"/>" src="<c:url value="${product.image.imageUrl}"/>"  id="imagUrl"/>
					</a>
					</c:when>
					<c:otherwise>
						<img  src="<c:url value="/resources/img/03.png" />"  style="max-width: 340px;max-height: 340px"/>
					</c:otherwise>
				</c:choose>	
				</div>
				<br/>
			
			 <div>
			   <br/>
			   <script type="text/javascript">
			     $(function(){
			        
			        $("#imgUrl").children().mouseover(function(){
			          $(this).css({"cursor":"pointer"});
			        }
			        ).mouseout(function(){
			           $(this).css({"cursor":"default"});
			        });
			        
			        $("#imgUrl a").mouseover(function(){
			           $("#imagUrl").attr({"src":"/sm-shop"+$(this).attr("title")+""});
			        });
			     });
			     
			   </script>
			   <div id="imgUrl">
			    <c:forEach var="images" items="${product.images}">
			      <a href="javascript:void();" title="${images.imageUrl}"><img style="max-width:60px;max-height:60px;"  id="imgUrl" src="<c:url value="${images.imageUrl}"/>" max-width="60px" max-height="60px"/></a>
			    </c:forEach>
			    <c:if test="${product.imagelength==0}">
			         <a href="javascript:void();" title="/resources/img/03.png""> <img id="" src="<c:url value="/resources/img/03.png"/>" style="max-width:60px;max-height:60px;margin-left: 2px" /></a>
			    </c:if>
			    </div>
			 </div>
			</td>
			<td style="width:550px;padding-left:30px;;" valign="top">
				<div>
					<div style="float:left;padding-right:24px;">
						<span style="color:#4285f4;font-size:18px;">${product.description.name}</span>
					</div>
					<div style="padding-top:4px;">
						
						<c:forEach begin="20" end="${product.qualitysocre}"  step="20">
							<img  src="<c:url value="/resources/img/stars/star-on.png" />">
						</c:forEach>		   
						<c:if test="${product.qualitysocre%20 > 0 }">			
							<img  src="<c:url value="/resources/img/stars/star-half.png" />">				
						</c:if>
						<c:if test="${product.qualitysocre>0}">
							   <span id="score" style="line-height:20px;padding-left:6px">
							   <script type="text/javascript">
								   var tmp = ${product.qualitysocre};
								   var p = (tmp/20).toFixed(1);
								   if(p==5.0)p=4.9;
								   $("#score").html(p);
							   </script>
							   </span>
						</c:if>	
					</div>
					
				</div>
				<c:if test="${not empty  product.description.enName}">
					<div style="padding-top:22px;font-size:14px;">
						${product.description.enName}
					</div>
				</c:if>
				<c:if test="${not empty  product.description.simpleDescription}">
					<div Style="padding-top:22px;font-size:14px;">
						${product.description.simpleDescription}
					</div>
				</c:if>
				<!-- 品牌和批次号 -->
				<c:choose>
					<c:when test="${not empty  product.manufacturer && not empty product.manufacturer.manName}">
						<div style="padding-top:22px;font-size:14px;">
							品&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;牌：<span> ${product.manufacturer.manName}</span>
							<span style="padding-left:30px;">
								<c:choose>
								  <c:when test="${auth.authtypeid == 1}">
								    <a href="/sm-shop/shop/authorizationDetail.html?id=${auth.authid}"><img src="/sm-shop/resources/img/stars/factory_cert.png" width="60px" height="auto"/></a>
								  </c:when>
								  <c:when test="${auth.authtypeid == 2}">
								    <a href="/sm-shop/shop/authorizationDetail.html?id=${auth.authid}"><img src="/sm-shop/resources/img/stars/agency_cert.png" width="60px" height="auto"/></a>
								  </c:when>
								</c:choose>
							</span>
							<c:if test="${not empty  product.batchnum}">
								型&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：<span class="pull-right">${product.batchnum}</span>
							</c:if>
						</div>
					</c:when>
					<c:otherwise>
						<c:if test="${not empty  product.batchnum}">
							<div style="padding-top:22px;font-size:14px;">
								型&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：<span>${product.batchnum}</span>
							</div>	
						</c:if>
					</c:otherwise>
				</c:choose>
				<!-- 商品编码和cas号 -->
				<c:choose>
					<c:when test="${not empty  product.code}">
						<div style="padding-top:22px;font-size:14px;">
							货&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：<span> ${product.code}</span>
							<c:if test="${not empty  product.cas}">
								<span style="width:300px;" class="pull-right">产&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;地：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${product.cas}</span>
							</c:if>
						</div>
					</c:when>
					<c:otherwise>
						<c:if test="${not empty  product.description.testDescription}">
								<div style="padding-top:22px;font-size:14px;">
									存储条件：<span>${product.description.testDescription}</span>
								</div>
						</c:if>
					</c:otherwise>
				</c:choose>
				<div style="font-size:12px;padding-top:22px;" id="rprice"></div>
				<div style="padding-top:22px;font-size:14px;" class="pull-right">
					<span style="padding-right:20px;">
							<button id="filedown" style="height:40px;width:120px;background: #4285f4;display:none" type="button"  class="btn btn-large " data-toggle="collapse" data-target="#collapseDwon" aria-expanded="false" aria-controls="#collapseDwon">资料下载</button>
						</span>
					<span><button style="height:40px;width:120px;" type="button" id="addCartButton" onclick="addcart()"; class="btn btn-large btn-danger " ><s:message code="button.label.addToCart" text="Add to Cart"/></button></span>
					<div class="collapse" id="collapseDwon"></div>
					
				</div>
								
			</td>
			<td style="width:246px;padding-left:34px;" valign="top" >
				<div style="border:solid 1px #d9d9d9;background-color: #f5f5f5;">
					<c:if test="${not empty  product.store.storeLogo}">
						<div style="text-align: center;background-color: #ffffff;">
							<img style="max-height:68px;max-width:246px;" src="${product.store.storeLogo}" >
						</div>
					</c:if>	
					<div style="padding:12px;color:#4285f4;font-size:14px;">
						${product.store.storename}
					</div>
					<c:if test="${not empty product.store.storecontacts}">
						<div style="padding:12px;font-size:12px;">
							联系人：${product.store.storecontacts}	
							<c:if test="${product.store.useQQ == true}">
							<c:if test="${product.store.qqNum != null}">
							 <a style="padding-left:16px;" target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=${product.store.qqNum}&site=qq&menu=yes">
							     <span style="line-height:25px;"></span>
							     <img border="0" src="http://wpa.qq.com/pa?p=2:${product.store.qqNum}:52" />
							 </a>
							</c:if>
						    </c:if>			
						</div>
						
					</c:if>
					<c:if test="${not empty product.store.storephone}">
						<div style="padding:12px;font-size:12px;">
							电话：${product.store.storephone}				
						</div>
					</c:if>
					<c:if test="${not empty product.store.storemobile}">
						<div style="padding:12px;padding-left:12px;font-size:12px;">
							手机：${product.store.storemobile}				
						</div>
					</c:if>
					<c:if test="${not empty product.store.storeUrl}">
						<div style="padding:12px;font-size:12px;word-break: break-all; word-wrap:break-word;">
							网址：${product.store.storeUrl}				
						</div>
					</c:if>
					<c:if test="${not empty product.store.storeEmailAddress}">
						<div style="padding:12px;font-size:12px;word-break: break-all; word-wrap:break-word;">
							邮箱：${product.store.storeEmailAddress}				
						</div>
					</c:if>
					<c:if test="${not empty product.store.storeaddress}">
						<div style="padding:12px;font-size:12px;">
							<span>地址：</span><span style="word-break: break-all; word-wrap:break-word;">${product.store.storeaddress}</span>				
						</div>
					</c:if>	
					<br>
				</div>				
			</td>
		</tr>
	</table>
	
	<hr style="height:2px;border:none;border-top:1px solid #d9d9d9; ">
	<table style="width:100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td valign="top" style="border:1px solid #d9d9d9;padding-bottom:20px;width:950px;">
				
				<ul class="nav nav-tabs removeDetailMenu" role="tablist" style="font-size:14px;font-weight:bold;padding:10 1px;background-color:#fafafa;" id="detailMenu" >
			    	<li role="presentation" class="active">
			    		<a href="#section-1" role="tab" data-toggle="tab"><s:message code="label.productedit.productdesc" text="Desc"/></a></li>
		               <c:if test="${reviews!=null}">
		               		<li role="presentation" ><a href="#section-2" role="tab" data-toggle="tab"> <s:message code="label.product.customer.reviews" text="Review"/></a></li>
		               </c:if>
		               <%--  <c:if test="${not empty product.productCertificates }">
		               		<li role="presentation" ><a href="#section-3" role="tab" data-toggle="tab"> <s:message code="label.product.certificate" text="Certifate"/></a> </li>
		               </c:if> --%>
		                <c:if test="${not empty product.thirdProofs }">
		              		 <li role="presentation" ><a href="#section-4" role="tab" data-toggle="tab">第三方认证</a></li>
		               </c:if>
		                <%--<c:if test="${not empty product.productProofs }">
		               		<li role="presentation" ><a href="#section-5" role="tab" data-toggle="tab">购买凭证</a></li>
		               </c:if>   --%> 
		                 <c:if test="${not empty  product.selfProofs}">
		               		<li role="presentation" ><a href="#section-6" role="tab" data-toggle="tab"><s:message code="label.productedit.producttestdesc" text="Report"/></a></li>
		               </c:if>            
				</ul>
				
				<div  class="tab-content" id="tab-content">
					 <div role="tabpanel" class="tab-pane active detailWidth"  id="section-1">
			            	     	
			            	<div class="container" style="padding:0px 20px;padding-bottom:10px;">
									 ${product.description.description}
			            	</div>
				      </div>
				      <c:if test="${reviews!=null}">
		            	<div role="tabpanel" class="tab-pane detailWidth" id="section-2">
				         <div class="container">
								<div class=" product-detail-box">	
								<table>          	
						           	<c:forEach items="${reviews}" var="review" varStatus="status">
										<tr>
											<td>
												<div class="row">
													<div class="col-sm-12">
														<div id="productRating<c:out value="${status.count}"/>" style="width: 160px!important;"></div>
													</div>
												</div>
												<div class="row">
													<div class="col-sm-12">
														<blockquote>
															<div style="color:#000"><c:out value="${review.description}" escapeXml="false" /></div>
															<small><c:out value="${review.customer.hidenick}" />&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${review.customer.project}" />&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${review.date}" /></small>
															</blockquote>
						 									<script>
														  	$(function() {
																$('#productRating<c:out value="${status.count}"/>').raty({ 
																	readOnly: true, 
																	half: true,
																	path : '<c:url value="/resources/img/stars/"/>',
																	score: <c:out value="${review.rating}" />
																});
														  	});
										  			   </script>
													</div>
												</div>
											</td>
										</tr>
									</c:forEach>
									</table>  
								</div>
							</div>
						</div>	
				      </c:if>     
				      <c:if test="${not empty product.productCertificates }">
			            <div role="tabpanel" class="tab-pane detailWidth" id="section-3">
				           <div class="container" style=" padding-top:20px;">
								<div class=" ">
				            	<c:forEach items="${product.productCertificates }" var="cert" varStatus="vstatus">
									<div class="col-sm-6" style="font-weight:bold;font-size:14px;">
										<div><a href="${cert.docUrl }" style="color:#FF6C00"><s:message code="label.product.certificate.name" text="Certificate"/>:${cert.displayName}</a></div>
										<div><s:message code="label.product.certificate.baseinfo" text="Certificate"/>:${cert.baseinfo }</div>
										 <c:if test="${not empty cert.docUrl }">
										 	<div style="word-break: break-all; word-wrap:break-word;"><s:message code="label.product.certificate.docurl" text="Doc Url"/>:${cert.docUrl }</div>
										 </c:if>
										 <c:if test="${not empty cert.title}">
									       <div><s:message code="label.product.certificate.title" text="title"/>:${cert.title }</div>
									    </c:if>
									    <c:if test="${not empty cert.description}">
										  <div style="word-break: break-all; word-wrap:break-word;"><s:message code="label.product.certificate.description" text="Certificate"/>:<br/><div style="font-weight:normal;">${cert.description }</div></div>
										</c:if>
										 <c:if test="${not empty cert.rimage }">
											<div>
												<a href="<c:url value="/shop/dispalyImage.html?ipath=${cert.rimage}"/>" target="blank">
													<img style="width:80%" alt="<c:out value="${cert.name}"/>" src="<c:url value="${cert.rimage}"/>"/>
													</a>
												
											</div>
										</c:if>
									</div>
								</c:forEach>
							</div>
							</div>
							</div>							 
			            </c:if>	
				          <c:if test="${not empty product.thirdProofs }">
				            <div role="tabpanel" class="tab-pane detailWidth" id="section-4">
				          	<div class="container" style=" padding-top:20px;">
									<div class=" ">
								<c:forEach items="${product.thirdProofs }" var="thirdProof" varStatus="vstatus">
								<div class="col-sm-6" style="font-size:14px;">
									<div style="padding-bottom:10px;"><s:message code="label.product.thirdproof.thirddetection" text="Thirdproof"/>：${thirdProof.displayName }</div>
									<div style="padding-bottom:10px;"><s:message code="label.product.thirdproof.description" text="Thirdproof"/>：<div id="temp" style="height:10px;padding-bottom:10px;"><div style="font-weight:normal;">${thirdProof.description }</div></div></div>
									<div style="padding-bottom:10px;">
										<a href="<c:url value="/shop/dispalyImage.html?ipath=${thirdProof.rimage}"/>" target="blank">
											<img style="width:80%" alt="<c:out value="${thirdProof.name}"/>" src="<c:url value="${thirdProof.rimage}"/>"/>
										</a>
									</div>
								</div>
								</c:forEach>
							</div>
							</div>
							</div>
			            </c:if> 
			             <c:if test="${not empty product.productProofs }">
			            	<div role="tabpanel" class="tab-pane detailWidth" id="section-5">
			            		<div class="container" style=" padding-top:20px;">
									<div class=" ">
										<c:forEach items="${product.productProofs }" var="proof" varStatus="vstatus">
										<div class="col-sm-6" style="font-size:14px;padding-left:1px;">
											<div style="padding-bottom:8px;"><s:message code="label.product.proof.buyer" text="Thirdproof"/>：${proof.buyer }</div>
											<div style="padding-bottom:8px;"><s:message code="label.product.proof.dateBuyed" text="Thirdproof"/>：${proof.dateBuyed}</div>
											<div style="padding-bottom:10px;"><s:message code="label.product.proof.description" text="Thirdproof"/>：<div id="temp" style="height: 10px;padding-bottom:10px;"><div style="font-weight:normal;">${proof.description }</div></div></div>
											<div>
												<a href="<c:url value="/shop/dispalyImage.html?ipath=${proof.rimage}"/>" target="blank">
													<img style="width:80%" alt="<c:out value="${proof.displayName}"/>" src="<c:url value="${proof.rimage}"/>"/>
												</a>
											</div>
										</div>
										</c:forEach>
									</div>
								</div>
							</div>
			            </c:if>   	
			            <c:if test="${not empty  product.selfProofs}">
			           		 <div role="tabpanel" class="tab-pane detailWidth" id="section-6">
			           		 	<div style="padding:20px;">
									<%-- <span class="label " style="padding:10px 30px;background-color:#ff313b;border-radius:10px;font-size:16px;">
										<s:message code="label.productedit.producttestdesc" text="Report"/>
									</span> --%>
								</div>	
			           		 	<div class="container " style=" padding-top:20px;">
									<div class=" ">
										<c:forEach items="${product.selfProofs }" var="selfProof" varStatus="vstatus">
											<div class="col-sm-6" style="font-weight:bold;font-size:14px;">
												<div> <s:message code="label.generic.description" text="Postalcode"/>:<br/><div style="font-weight:normal;">${selfProof.description }</div></div>
												<div>
													<a href="<c:url value="/shop/dispalyImage.html?ipath=${selfProof.rimage}"/>" target="blank">
														<img style="width:80%" src="<c:url value="${selfProof.rimage}"/>"/>
													</a>
												</div>
											</div>
										</c:forEach>
									</div>
								<div>
							</div>	
						</c:if>    
				   </div>	
			</td>
			<td style="padding-left:18px;width:232px;" valign="top">
				<div style="font-size:13px;font-weight:bold;background-color:#fafafa;">
					<div style="height:45px;padding:10px;">
						最近浏览
					</div>
					<div id="hisbrow"></div>
				</div>
				
			</td>
		</tr>
	</table>
	
		
<img id="back-to-top" style="z-index:9999;position:fixed;bottom:100px;right:10px;"  src="<c:url value="/resources/img/jiantou.png" />">		
</div>
<br>
<!-- end body -->            
<script>
jQuery(function($) {
	
  $(document).ready( function() {
	  getProductPrice(${product.id});
	  getProductFile(${product.id});
	//set cookie
  	var imageurl='/resources/img/03.png';
  	if(${product.image!=null}){
  		imageurl='${product.image.imageUrl}';
  	}
	  setCookie("${product.id}","${product.description.name}",imageurl,"${product.qualitysocre}"); 
  });
  
  //cookie     
  function setCookie(id,name,img,qulity)
  {
	  //
	 // $.cookie("history","aa",{expires:-1}); 
	  var hisArt = $.cookie("history"); 
  	var len = 0;
  	var canAdd = true;
  	if(hisArt){ 
  	    hisArt = eval("("+hisArt+")"); 
  	    len = hisArt.length; 
  	} 
  	$(hisArt).each(function(){ 
  	    if(this.id == id){ 
  	        canAdd = false; //已经存在，不能插入 
  	        return false; 
  	    } 
  	});
  	
  	if(canAdd==true){ 
  	    var json = "["; 
  	    
  	    //最多5条
  	    if(len>=5){
  	    	 for(var i=1;i<5;i++){ 
  	  	        json = json + "{\"id\":\""+hisArt[i].id+"\",\"name\":\""+hisArt[i].name+"\",\"quilty\":\""+hisArt[i].quilty+"\",\"img\":\""+hisArt[i].img+"\"},"; 
  	  	    } 
  	    }else{
  	    	for(var i=0;i<len;i++){ 
  	  	        json = json + "{\"id\":\""+hisArt[i].id+"\",\"name\":\""+hisArt[i].name+"\",\"quilty\":\""+hisArt[i].quilty+"\",\"img\":\""+hisArt[i].img+"\"},"; 
  	  	    } 
  	    }
  	   
  	    json = json + "{\"id\":\""+id+"\",\"name\":\""+name+"\",\"quilty\":\""+qulity+"\",\"img\":\""+img+"\"}]"; 
  	    $.cookie("history",json,{expires:30, path: "/"}); 
  	} 
  	
//      alert(cookieVal);
     
  }
 
});			
			
</script>


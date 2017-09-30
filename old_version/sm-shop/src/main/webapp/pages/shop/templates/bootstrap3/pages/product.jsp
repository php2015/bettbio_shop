<%
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
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
             	list +='<tr style="border-top:1px solid #d9d9d9;"><td valign="top" style="height:84px;"><img style="background-color:#fafafa;max-width:70px;max-height:70px;padding:10px 10px;width:68px;height:68px;" src="/sm-shop'+json[i].img+'"></td>';
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
			tbBody+='<li class="active" ><a href="'+p.url+'">'+p.label+'</a></li>';
		});
	}
	
	$("#breadcrumb").html(tbBody);
}
  $(function() {
	    
	    // Javascript to enable link to tab
	    var hash = document.location.hash;
	    var prefix = "tab_";
	    if (hash) {
	        $('.nav-tabs a[href='+hash.replace(prefix,"")+']').tab('show');
	    }

	    // Change hash for page-reload
	    $('.nav-tabs a').on('shown.bs.tab', function (e) {
	            window.location.hash = e.target.hash.replace("#", "#" + prefix);
	            });
	    
	    //选择
	    $('.nav-tabs a').on("click",function(){
			$(this).parent().addClass("active").siblings(".tab_li").removeClass("active");
	    });
	    $('.nav-tabs a').eq(0).parent().addClass("active").siblings(".tab_li").removeClass("active");;
		
	});
</script>
<style>
.nav-tabs > li.active > a, .nav-tabs > li.active > a:hover, .nav-tabs > li.active > a:focus{
	border-top:4px solid #4285f4;
	border-bottom: 0px;
	border-right: 0px;
	margin-right: 0px;
}
.nav-tabs{
 text-align: center;
}
.nav-tabs > li > a{
  line-height: 2;
  font-size: 14px;
  background-color: #fafafa;
}
.nav-tabs > li>a:HOVER{
 border-bottom: 0px;
 margin-right: 0px
}
.tab_hr{
 margin: 0px 0px 5px 0px;
}
.classDiv{
min-height:300px;
max-height:3000px;
overflow-y:auto;
}
</style>
<div style="padding:20px 0px;">
   <div class="row" style="margin-top: -30px;width: 100%;font-weight:600;">
		<ul class="breadcrumb" style="background-color:transparent !important; box-shadow: none;padding-top:30px;">
		  <c:forEach items="${requestScope.BREADCRUMB.breadCrumbs}" var="breadcrumb" varStatus="count">
			  <b>&gt;</b><li class="active" >
			      <a href="${breadcrumb.url}" >${breadcrumb.label}</a>
			  </li>
		  </c:forEach>
		</ul>
	</div>
	<table style="width:100%;" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td valign="top" style="width: 340px!important;height: 426px;">
			  <div id="iamges" style="width: 100%;min-height:340px;">
			  <%-- 上海源叶生物特别修改--%>
			   <c:if test="${product.store.id==4115}">
					<img src="<c:url value="/resources/img/brands/66.jpg" />"  style="width: 340px;height: 340px;border: 1px solid #d9d9d9;"/>
			   </c:if>
			   <c:if test="${product.store.id!=4115}">
  			     <c:choose>
					<c:when test="${product.image!=null}">
					 <a href="<c:url value="/shop/dispalyImage.html?ipath=${product.image.imageUrl}"/>" target="blank">
						 <img  style="width:340px;height:340px; border: 1px solid #d9d9d9;" alt="<c:out value="${product.description.name}"/>" src="<c:url value="${product.image.imageUrl}"/>"  id="imagUrl"/>
					 </a>
					</c:when>
					<c:otherwise>
						<img src="<c:url value="/resources/img/01.png" />"  style="max-width: 340px;max-height: 340px;border: 1px solid #d9d9d9;"/>
				 	</c:otherwise>
				</c:choose>
			   </c:if>
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
			        	var title=$(this).attr("title");
			           if(title!=''&&title!=undefined){
			        	   $("#imagUrl").attr({"src":"/sm-shop"+title+""}); 
			           }
			        });
			     });
			     
			   </script>
			   <div id="imgUrl" style="min-width:340px;max-width: 340px;position: relative;">
			      <a href="#" style="position: absolute;left: 0;top:25px;"><img src="<c:url value="/resources/img/icon_left.png"/>" width="14px" height="22px"/></a>
				  <%-- 上海源叶生物特别修改--%>
				  <c:if test="${product.store.id==4115}">
					<a href="javascript:void();" title="源叶生物" style="max-width:70px;max-height:70px;"> <img id="" src="<c:url value="/resources/img/brands/66.jpg"/>"  style="width:70px;height:70px;margin-left:20px;"/></a>
			   </c:if>
			   <c:if test="${product.store.id!=4115}">
		 	      <c:forEach var="images" items="${product.images}" varStatus="vs">
			 	      <a href="javascript:void();" title="${images.imageUrl}" style="margin-left: 2px;<c:if test="${vs.index>=4}">display: none;</c:if>"><img id="" src="<c:url value="${images.imageUrl}"/>" style="width:70px;height:70px;<c:if test="${vs.index==0}">margin-left:20px;</c:if>"/></a>
			      </c:forEach>
				
			      <c:if test="${product.imagelength==0}">
			      <a href="javascript:void();" title="/resources/img/01.png" style="max-width:70px;max-height:70px;"> <img id="" src="<c:url value="/resources/img/01.png"/>"  style="width:70px;height:70px;margin-left:20px;"/></a>
		   	      </c:if> 
			   </c:if>
 				 <a href="#" style="position: absolute;right: 0;top:25px"><img src="<c:url value="/resources/img/icon_right.png"/>" width="14px" height="22px"/></a>
			    </div>
			 </div>
			</td>
			<td style="width:550px;padding-left:30px;" valign="top">
				<div style="overflow:hidden;">
					<div style="float:left;padding-right:24px;font-weight:900;">
						<span style="color:#4285f4;font-size:18px;">${product.description.name}</span>
					</div>
					<div style="padding-top:4px;">
						
						<c:forEach begin="20" end="${product.qualitysocre}"  step="20">
							<img style="margin-top:-5px;" src="<c:url value="/resources/img/stars/star-on.png" />"/>
							
						</c:forEach>
						<c:if test="${product.qualitysocre%20 > 0 }">			
							<img style="margin-top:-5px;" src="<c:url value="/resources/img/stars/star-half.png" />">
						</c:if>
						
						<c:if test="${product.qualitysocre>0}">
							   <span id="score" style="line-height:20px;padding-left:6px;color:red;font-weight:700;font-size:14px;">
							   <script type="text/javascript">
								   var tmp = ${product.qualitysocre};
								   var p = (tmp/20).toFixed(1);
								   if(p==5.0)p=4.9;
								   $("#score").html(p);
							   </script>
							   </span><span style="color:red;font-weight:700;font-size:14px;">分</span>
						</c:if>	
					</div>
				</div>
				<c:if test="${product.diamond}">
					<div class="diamond" style="width:90px; height:20px; background:#ff313b; -moz-border-radius: 4px;-webkit-border-radius: 4px; " 
						title="<s:message code='lable.product.diamond.flagtext1' text='7天无条件退换货'/>">
						<img src="/sm-shop/resources/img/icon-diamond.gif" style=" margin-top:-3px; padding-left:5px; display:inline; vertical-align: middle;" />
                        <span style="display:inline-block; height:20px;line-height:20px;font-size:14px; color:#ffffff;" ><s:message code='lable.product.diamond.flagtext2' text='钻石产品'/></span>
					</div>
				</c:if>
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
						<div style="padding-top:22px;font-size:14px;">
							品&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;牌：<span> ${product.manufacturer.description.name}</span>
							<span style="padding-left:30px;">
							<c:choose>
							  <c:when test="${auth.authtypeid == 1}">
							    <a href="/sm-shop/shop/authorizationDetail.html?id=${auth.authid}"><img src="/sm-shop/resources/img/factory_cert.png"  style="width:92px;height:38px;margin-left:-20px;"/></a>
							  </c:when>
							  <c:when test="${auth.authtypeid == 2}">
							    <a href="/sm-shop/shop/authorizationDetail.html?id=${auth.authid}"><img src="/sm-shop/resources/img/agency.png" style="width:92px;height:38px;margin-left:-20px;" /></a>
							  </c:when>
							</c:choose>
							</span>
							<span class="pull-right" style="padding-top:12px;">批&nbsp;次&nbsp;号：
							<c:if test="${not empty  product.batchnum}">
								<c:out value="${product.batchnum}"/>
							</c:if>
							</span>
						</div>
				<!-- 商品编码和cas号 -->
				<div style="padding-top:22px;font-size:14px;">
					货&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：<span>${product.code}</span> 
					
					
						<span class="pull-right">CAS：<c:if test="${not empty  product.cas}"><c:out value="${product.cas}" /></c:if></span>
				</div>
				<div style="padding-top:22px;font-size:14px;">
					存储条件：
					<c:if test="${not empty  product.description.storecondDescription}">
						<span>${product.description.storecondDescription}</span>
					</c:if>

				</div>

				<div style="font-size:12px;padding-top:22px;" id="rprice"></div>
				<div style="padding-top:22px;font-size:14px;" class="pull-right">
					<span style="padding-right:20px;">
							<button id="filedown" style="height:40px;width:120px;background: #4285f4;background-color: #4285f4;color: #fafafa;font-size:14px;" type="button"  class="btn btn-large " data-toggle="collapse" data-target="#collapseDwon" aria-expanded="false" aria-controls="#collapseDwon">资料下载</button>
						</span>
					<span><button style="height:40px;width:120px;" type="button" id="addCartButton" onclick="addcart()"; class="btn btn-large btn-danger " ><s:message code="button.label.addToCart" text="Add to Cart"/></button></span>
					<div class="collapse" id="collapseDwon"></div>
					
				</div>
								
			</td>
			<td style="width:270px;padding-left:34px;" valign="top" >
				<div style="border:solid 1px #d9d9d9;background-color: #f5f5f5;">
					<c:if test="${not empty  product.store.storeLogo}">
						<div style="text-align: center;background-color: #ffffff;">
							<img style="max-height:68px;max-width:246px;" src="${product.store.storeLogo}" >
						</div>
					</c:if>	
					<div style="padding:12px;color:#4285f4;font-size:14px;text-align:center">
						${product.store.storename}
					</div>
					<%@include file="/pages/shop/common/merchant_contact.jsp" %>
					<%-- <c:if test="${not empty product.store.storecontacts}">
						<div style="padding:12px 20px;font-size:12px;">
							联系人：${product.store.storecontacts}
					 <c:if test="${product.store.useQQ == true}">
						<c:if test="${product.store.qqNum != null}">
						  <span style="line-height:25px;">Q &nbsp;&nbsp; Q：${product.store.qqNum}</span>
						 <a style="padding-left:16px;" target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=${product.store.qqNum}&site=qq&menu=yes">
						     <span style="line-height:25px;"></span>
						     <img border="0" src="http://wpa.qq.com/pa?p=2:${product.store.qqNum}:52" />
						 </a>
						</c:if>
					 </c:if>
						   
					</div>
					</c:if>  
					
					<c:if test="${not empty product.store.storephone}">
						<div style="padding:12px 20px;font-size:12px;color:red;">
							<div style="display:inline-block; vertical-align: top;">电&nbsp;&nbsp; 话：</div>
							<div style="display:inline-block;">
							021-61552750<br/>021-61552739 
							</div>				
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
						<div style="padding:12px 20px;font-size:12px;word-break: break-all; word-wrap:break-word;">
							<div style="display:inline-block; vertical-align: top;">邮&nbsp;&nbsp; 箱：</div>
							<div style="display:inline-block;">
							${product.store.storeEmailAddress}
							</div>			
						</div>
					</c:if>
					
					<c:if test="${not empty product.store.storeaddress}">
						<div style="padding:12px;font-size:12px;">
							<span>地址：</span><span style="word-break: break-all; word-wrap:break-word;">${product.store.storeaddress}</span>				
						</div>
					</c:if>	
					--%>
					<br>
				</div>					
			</td>
		</tr>
	</table>
	<table style="width:950px;margin-top: 20px;" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td valign="top" style="border:1px solid #d9d9d9;width:950px;">
				
				<ul class="nav nav-tabs removeDetailMenu" role="tablist" style="font-size:14px;font-weight:bold;padding:10 1px;background-color:#fafafa;" id="detailMenu" >
							<c:if test="${not empty product.description.description}">
			    			<li role="presentation" style="width: 16.6%" class="tab_li"><a href="#section-1" role="tab" >商品详情</a></li>
			    			</c:if>
			    			<c:if test="${not empty product.thirdProofs }">
		               		<li role="presentation" style="width: 16.6%" class="tab_li"><a href="#section-2" role="tab">第三方认证</a></li>
		               		</c:if>
		               		<c:if test="${not empty product.productCertificates }">
		               		<li role="presentation" style="width: 16.6%" class="tab_li"><a href="#section-3" role="tab" >文献引用</a> </li>
		               		</c:if>
		               		<c:if test="${not empty product.productProofs }">
		               		<li role="presentation" style="width: 16.6%" class="tab_li"><a href="#section-4" role="tab" >购买凭证</a></li>
		               		</c:if>
		               		<c:if test="${not empty  product.selfProofs}">
		               		<li role="presentation" style="width: 16.6%" class="tab_li"><a href="#section-5" role="tab">实验报告</a></li>
		               		</c:if>
		               		<c:if test="${reviews!=null}">
		               		<li role="presentation" style="width:16.6%"><a href="#section-6" role="tab" data-toggle="tab">客户评论</a></li>
		                  </c:if>
				</ul>
				
				<div  class="tab-content" id="tab-content"  style="width: 950px;">
				<c:if test="${not empty product.description.description}">
						 <div id="section-1">
						  <div style="padding:20px;"></div> 
		            	<div style="width: 161px;height: 40px;background-color: #ff313b;font-size: 16px;color: #fafafa;text-align:center;padding-top:10px;margin-left:30px;-moz-border-radius: 10px;  -webkit-border-radius: 10px;   border-radius:10px;margin-bottom: 20px;">
							 商品描述
		            	</div>
		            	<div style="margin-left: 30px;font-size: 12px;"> ${product.description.description}</div>
						</div>
						</c:if>
					    <c:if test="${not empty product.thirdProofs }">
						<hr class="tab_hr"/>
						<div id="section-2">
						<div style="padding:20px;">
						
						</div>
						
		            	<div style="width: 161px;height: 40px;background-color: #ff313b;font-size: 16px;color: #fafafa;text-align:center;padding-top:10px;margin-left:30px;-moz-border-radius: 10px;  -webkit-border-radius: 10px;   border-radius:10px;margin-bottom: 20px;">
							 第三方认证
		            	</div>
		            	<div class="container classDiv"  style="clear:both;height: 100%">
								<table>          	
									<c:forEach items="${product.thirdProofs }" var="thirdProof" varStatus="vstatus">
									<div class="col-sm-6" style="font-size:14px;">
										<div style="padding-bottom:10px;"><s:message code="label.product.thirdproof.thirddetection" text="Thirdproof"/>：${thirdProof.displayName }</div>
										<div style="padding-bottom:10px;"><s:message code="label.product.thirdproof.description" text="Thirdproof"/>：<div id="temp" style="height: 10px;padding-bottom:10px;"><div style="font-weight:normal;">${thirdProof.description }</div></div></div>
										<div style="padding-bottom:10px;">
											<a href="<c:url value="/shop/dispalyImage.html?ipath=${thirdProof.rimage}"/>" target="blank">
												<img style="width:80%" alt="<c:out value="${thirdProof.name}"/>" src="<c:url value="${thirdProof.rimage}"/>"/>
											</a>
										</div>
									</div>
									</c:forEach>
									</table>  
							</div>
						</div>
						 </c:if> 
						<c:if test="${not empty product.productCertificates }">
						<hr class="tab_hr"/> 
		            	<div id="section-3">
		            	 <div style="padding:20px;">
							
						</div>
		            	<div style="width: 161px;height: 40px;background-color: #ff313b;font-size: 16px;color: #fafafa;text-align:center;padding-top:10px;margin-left:30px;-moz-border-radius: 10px;  -webkit-border-radius: 10px;   border-radius:10px;margin-bottom: 20px;">
							 文献引用
		            	</div>
		            	 <div class="container classDiv"  style="margin-left:30px;">
								<div >
				            	<c:forEach items="${product.productCertificates }" var="cert" varStatus="vstatus">
									<div class="col-sm-6" style="font-size:14px;">
										<div style="padding-bottom:10px"><a href="${cert.docUrl }" style="color:#666;">期刊名称：${cert.displayName}</a></div>
										<div style="padding-bottom:10px">期刊编号：${cert.baseinfo }</div>
										 <c:if test="${not empty cert.docUrl }">
										 	<div style="word-break: break-all; word-wrap:break-word;padding-bottom:10px"><s:message code="label.product.certificate.docurl" text="Doc Url"/>：${cert.docUrl }</div>
										 </c:if>
										 <c:if test="${not empty cert.title}">
									       <div style="padding-bottom:10px"><s:message code="label.product.certificate.title" text="title"/>：${cert.title }</div>
									    </c:if>
									    <c:if test="${not empty cert.description}">
										  <div style="word-break: break-all; word-wrap:break-word;"><s:message code="label.product.certificate.description" text="Certificate"/>：<div id="temp" style="height:10px;padding-bottom:10px"></div><div style="font-weight:normal;">${cert.description }</div></div>
										</c:if>
										 <c:if test="${not empty cert.rimage }">
											<div style="padding-bottom:10px">
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
		            	<c:if test="${not empty product.productProofs }">
		            	<hr class="tab_hr"/> 
		            	<div id="section-4">
		            	 <div style="padding:20px;">
							
						</div> 
		            	<div style="width: 161px;height: 40px;background-color: #ff313b;font-size: 16px;color: #fafafa;text-align:center;padding-top:10px;margin-left:30px;-moz-border-radius: 10px;  -webkit-border-radius: 10px;   border-radius:10px;margin-bottom: 20px;">
							 购买凭证
		            	</div>
			            		<div class="container classDiv" style="margin-left: 30px;">
									<div class=" ">
										<c:forEach items="${product.productProofs }" var="proof" varStatus="vstatus">
										<div class="col-sm-6" style="font-size:14px;padding-left:1px;">
											<div style="padding-bottom:8px;"><s:message code="label.product.proof.buyer" text="Thirdproof"/>：${proof.buyer }</div>
											<div style="padding-bottom:8px;"><s:message code="label.product.proof.dateBuyed" text="Thirdproof"/>：${proof.dateBuyed}</div>
											<div style="padding-bottom:10px;"><s:message code="label.product.proof.description" text="Thirdproof"/>：<br/><div style="font-weight:normal;">${proof.description }</div></div>
											<div style="padding-bottom:10px;">
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
		            	<hr class="tab_hr"/>
		            	<div id="section-5">
		            		<div style="padding:20px;"></div> 
		            	<div style="width: 161px;height: 40px;background-color: #ff313b;font-size: 16px;color: #fafafa;text-align:center;padding-top:10px;margin-left:30px;-moz-border-radius: 10px;  -webkit-border-radius: 10px;   border-radius:10px;margin-bottom: 20px;">
							 实验报告
		            	</div>
			           		 	<div class="container classDiv" style="margin-left:30px;">
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
		            	</div>
		            </c:if> 
		            <c:if test="${reviews!=null}">
		            <hr class="tab_hr"/>
		            <div id="section-6">
		            	<div style="padding:20px;"></div> 
		            	<div style="width: 161px;height: 40px;background-color: #ff313b;font-size: 16px;color: #fafafa;text-align:center;padding-top:10px;margin-left:30px;-moz-border-radius: 10px;  -webkit-border-radius: 10px;   border-radius:10px;margin-bottom: 20px;">
							 客户评价
		            	</div>
		            	 <div class="container">
								<div class=" product-detail-box">	
								<table>          	
						           	<c:forEach items="${reviews}" var="review" varStatus="status">
										<tr>
											<td>
												<div class="row">
													<div>
														<div id="productRating<c:out value="${status.count}"/>" style=""></div>
													</div>
												</div>
												<div class="row">
													<div>
														<blockquote>
															<div style="white-space:normal;word-break:break-all;overflow:hidden;"><c:out value="${review.description}" escapeXml="false" /></div>
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
				      </div>
			</td>
			
			<td style="padding-left:18px;width:232px;" valign="top">
				<div style="font-size:13px;font-weight:bold;background-color:#fafafa;">
					<div style="height:45px;padding:10px;min-width: 226px">
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
<script>
jQuery(function($) {
	
  $(document).ready( function() {
	  getProductPrice(${product.id});
	  getProductFile(${product.id});
	//set cookie
  	var imageurl='/resources/img/01.png';
  	if(${product.image!=null}){
  		imageurl='${product.image.imageUrl}';
  	}
  	
	  setCookie("${product.id}","${product.description.name}",imageurl,"${product.qualitysocre}",'${product.price}'); 
  });
  
  //cookie     
  function setCookie(id,name,img,qulity,price)
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
  	  	        json = json + "{\"id\":\""+hisArt[i].id+"\",\"name\":\""+hisArt[i].name+"\",\"quilty\":\""+hisArt[i].quilty+"\",\"img\":\""+hisArt[i].img+"\",\"price\":\""+hisArt[i].price+"\"},"; 
  	  	    } 
  	    }else{
  	    	for(var i=0;i<len;i++){ 
  	  	        json = json + "{\"id\":\""+hisArt[i].id+"\",\"name\":\""+hisArt[i].name+"\",\"quilty\":\""+hisArt[i].quilty+"\",\"img\":\""+hisArt[i].img+"\",\"price\":\""+hisArt[i].price+"\"},"; 
  	  	    } 
  	    }
  	   
  	    json = json + "{\"id\":\""+id+"\",\"name\":\""+name+"\",\"quilty\":\""+qulity+"\",\"img\":\""+img+"\",\"price\":\""+price+"\"}]"; 
  	    $.cookie("history",json,{expires:30, path: "/"}); 
  	} 
  	
//      alert(cookieVal);
     
  }
 
});			
			
</script>
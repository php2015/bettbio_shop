
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
<link href="<c:url value="/resources/templates/bootstrap3/css/spinner.css" />" rel="stylesheet">
<link href="<c:url value='/resources/templates/bootstrap3/css/brandsv2.css' />" rel="stylesheet">
<script src="<c:url value="/resources/templates/bootstrap3/js/spinner.js"/>"></script>
<script src="<c:url value="/resources/js/product.js"/>"></script>
 <script src="<c:url value="/resources/js/pagingation.js" />"></script> 
 <script src="<c:url value="/resources/js/shop-search.js" />"></script>
<script src="<c:url value='/resources/js/shop-functions.js' />?v=<%=new java.util.Date().getTime() %>"></script>


<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<script>
var page=1;
var pageNum=20;
var categoryIds;
var orderBy = '';
var auth = '';
var manufacturers=new Array();
var more ='<s:message code="label.product.moreitems" text="More" />';
var ok ='<s:message code="button.label.submit2" text="ok" />';
var cancel ='<s:message code="button.label.cancel" text="cancel" />';
var searchAll='<s:message code="label.search.all" text="All" />';
var home ='<s:message code="menu.home" text="Home" />';
var loginPageUrl = '<c:url value="/shop/customer/customLogon.html" />';
 $(function(){		
	 dosearch('全部结果');
	 writeBread();
	 manBindOP("qua");
 });
 
 
 <jsp:include page="/pages/shop/templates/bootstrap3/sections/shop-listing.jsp" />
	 
 
	function callBackSearchProducts(productList) {		
		$('#pageContainer').hideLoading();
		if(productList==null || productList.productCount==0){
			var productQty = '0' + ' <s:message code="label.search.items.found" text="item(s) found" />';
			$('#total_panel').html(productQty);
		}else{
			var productQty = productList.productCount + ' <s:message code="label.search.items.found" text="item(s) found" />';
			$('#total_panel').html(productQty);
		}
	}
	function writeptile(offset,countByPage,totalCount){
		$("#ptitle").html('<s:message code="label.entitylist.paging" arguments="'+offset+';'+countByPage+';'+totalCount+'" htmlEscape="false" argumentSeparator=";" text=""/>');
		$("#ptitle1").html('共  <b>'+totalCount+'</b>  条');
	}
</script>
<script type="text/javascript">
    $(function(){
        //show cookie
        
        var json = eval("("+$.cookie("history")+")");
        if(json !=null && json.length>0){
        	 var list = '<table>'; 
             for(var i=0; i<json.length;i++){         	
             	list +='<tr style=""><td valign="top" style="height:84px;"><img style="background-color:#fafafa;max-width:70px;max-height:70px;padding:10px 10px;width:70px;height:64px;" src="/sm-shop'+json[i].img+'"></td>';
             	list +='<td valign="top" style="font-size:12px;padding:10px 10px;"><a target="_blank"  href="<c:url value="/shop/product/"/>' +json[i].id + '.html">'+json[i].name.substring(0,8)+'</a>';
             	/* var star =(json[i].quilty)/20;
             	var left=(json[i].quilty)%20
             	for(var j=1;j<star;j++){
             		list +='<img style="max-width:15px;max-height:15px;padding:2px 0px;"  src="<c:url value="/resources/img/stars/star-on.png" />">';
             	}
             	if(left>0){
             		list +='<img style="max-width:15px;max-height:15px;padding:2px 0px;"  src="<c:url value="/resources/img/stars/star-half.png" />">	';
             	} */
             	if(json[i].price && json[i].price != null)
             		list += '<p style="color: #999;font-weight: 400;font-size: 12px;">￥'+json[i].price+'元</p>';
             	list +='</td></tr>';           
             }
             list +='</table>'
             $("#hisbrow").html(list); 
        }
    });
</script>
	<div style="margin-left:-6px">
	   <jsp:include page="/pages/shop/templates/bootstrap3/sections/breadcrumb.jsp" />
	</div>
	<div style="border:1px solid #d9d9d9">
	 
	     <table  style="width:100%;height:100%;margin-top:10px" border="0" cellpadding="0" cellspacing="0" >
	 		<tr style="border-bottom:1px dashed #eeeeee;">
	 			<td style="padding: 25px 18px 28px 25px;" valign="top">
	 				<span ><s:message code="label.entity.type" text="Category" />:</span>
	 			</td>
	 			<td style="padding:23px 0;width:970px;" id="subCatePanel"  >
	 				  
	 			</td>
	 			<td style="width:200px;padding-top:30px ;" valign="top">
	 				<span id="catMore" class="pull-right" style="padding-right:20px;"></span>
	 			</td>
	 		</tr>
	 		<tr style="border-bottom:1px dashed #eeeeee;">
	 			<td style="width:100px;padding: 25px 18px 28px 25px;" class="search_padding_left"  valign="top">
	 				<span ><s:message code="label.product.quality" text="Quality" />:</span>
	 			</td>
	 			<td style="padding-top:6px;">
	 				 <div id="panel_qua" class="search_nav5" style="border:none;">
	 				 	
	 				 		<search_bar style="color:#333;margin-right: 65px;"><span class=" mutiMan qua choice-panel-span" id="0"><s:message code="label.product.certificate" text="Doc" /></span></search_bar>
	 				 		<search_bar style="color:#333;margin-right: 65px;"><span class=" mutiMan qua choice-panel-span" id="1"><s:message code="label.product.thirdproof" text="Third" /></span></search_bar>
	 				 		<search_bar style="color:#333;margin-right: 65px;"><span class=" mutiMan qua choice-panel-span" id="2"><s:message code="label.product.proof" text="Proof" /></span></search_bar>	
	 				 		<search_bar style="color:#333;margin-right: 65px;"><span class=" mutiMan qua choice-panel-span" id="3">实验报告</span></search_bar> 				 		
	 				 	
	 				 	<div class="row" style="padding:15px 0px;text-align:center;display: none;" id="confirmPanel_qua">
	 				 		<button type="button" id="okbutton_qua" class="btn btn-danger"  disabled="disabled" onclick="manMutiOk('qua')"><s:message code="button.label.submit2" text="ok" /></button>&nbsp;&nbsp;&nbsp;&nbsp;
	    					<button type="button" class="btn btn-info" onclick="manMutiCancel('qua')"><s:message code="button.label.cancel" text="cancel" /></button>
	    				</div>
	 				 </div>
	 			</td>
	 			<td style="width:200px;">
	 				 <span class="pull-right" style="padding-right:20px;">
	 				 	<a id="mutiButton_qua" style="border: 1px solid #E5E7E9;padding: 4px 6px;" href="javascript:void(0);" onclick="manMuti('qua');">
	 				 		<s:message code="label.category.mutichioce" text="Muti choice" />
	 				 		<span class="glyphicon glyphicon-plus ico-style " aria-hidden="true"></span>
	 				 	</a></span>
	 			</td>
	 		</tr>
	 		<tr>
	 			<td style="padding: 25px 18px 28px 25px;"  valign="top">
	 				<span ><s:message code="label.manufacturer.brand" text="Brands" />:</span>
	 			</td>
	 			<td  id="manCatePanel" style="width:900px;padding-top: 20px;"></td>
	 			<td style="width:200px;padding-top:20px;" valign="top">
		 			<span class="pull-right" style="margin-right:20px;" id="manMore">&nbsp;</span>
		 			<span class="pull-right" style="padding-right:0px;margin-top:5px;" >
		 				<a id="mutiButton_man" href="javascript:void(0);" style="border: 1px solid #E5E7E9;padding: 4px 6px;" onclick="manMuti('man')">
		 					<s:message code="label.category.mutichioce" text="Muti choice" />
		 					<span class="glyphicon glyphicon-plus ico-style" aria-hidden="true"/>
		 				</a>
		 			</span>
	 			</td>
	 		</tr>
	 	</table>
	  </div>
	  <br/>
	<!-- <div style="border: 1px solid #d9d9d9;width:100%">	
		<div class="" style=" line-height:22px;" >		
		<ul class="nav nav-pills" id="search_cateGory">		 	
		</ul>				 
		</div>
	</div> -->
	
	<div style="border: 1px solid #d9d9d9;width:100%">	
		<div class="" style=" line-height:22px;">		
		<ul class="nav nav-pills addFilter"> 
			<li role="presentation" id="li_0" class="nopadding-nomargin" style="padding-left:10px;"><a href="javascript:void(0);">排序：</a></li> 
			<li role="presentation" id="li_1" class="nopadding-nomargin active" data-order="0"><a href="javascript:void(0);" onclick="doSearchButton(0,'全部结果','0',this)">综合</a></li> 
			<li role="presentation" class="nopadding-nomargin" style="margin-top:10px;">	<search_bar style="color:#333;margin-right: 65px;"><span class=" mutiMan qua choice-panel-span" id="0">文献引用</span></search_bar></li>
			<li role="presentation" class="nopadding-nomargin" style="margin-top:10px;">	<search_bar style="color:#333;margin-right: 65px;"><span class=" mutiMan qua choice-panel-span" id="1">第三方认证</span></search_bar></li>
			<li role="presentation" class="nopadding-nomargin" style="margin-top:10px;">	<search_bar style="color:#333;margin-right: 65px;"><span class=" mutiMan qua choice-panel-span" id="2">购买凭证</span></search_bar></li>
			<li role="presentation" class="nopadding-nomargin" style="margin-top:10px;">	<search_bar style="color:#333;margin-right: 65px;"><span class=" mutiMan qua choice-panel-span" id="3">实验报告</span></search_bar></li>
		</ul>				 
		</div>
	</div>
	
	<br>
		<table style="width: 100%;">
			<tr>
				<td width="980"  style="vertical-align: top;">
					<table id="tr_td"  style="width:100%">
						<thead style="background-color: #f9f9f9;"> 
							<tr style="font-weight:bold;height:40px;">
								<td width="10%" data-column-id="image">
								  <%--    <s:message code="label.product.image" text="Image" /> --%>
								</td>
								<td width="20%" data-column-id="name" style="padding-left:10px;text-align:center;">
								   <s:message code="label.productedit.productname" text="Name"  />
								</td>
								
								<td width="12%" data-column-id="Brand" style="text-align:center;">
								    <s:message code="label.manufacturer.brand" text="Brand" />
								</td>
								<td width="7%" data-column-id="Brand" style="text-align:center;">
								    货号
								</td>
								<td width="42%" data-column-id="Brand" style="text-align:center;">
									<%-- <s:message code="label.product.price" text="Price" /> --%>
									<%-- 宽度分配：型号/计量：10%，目录价：9%，会员折扣：%9，会员价格：11% --%>
								    <label style="width:100px;">型号/计量</label>
									<label style="width:100px;">目录价</label>
									<label style="width:90px;">会员折扣</label>
									<label style="width:130px; padding-right: 20px;">会员价格</label>
								</td>	
								<td width="11%" data-column-id="quality" style="padding-left:35px;"><s:message code="label.product.quality" text="Quality" /></td>
							</tr>
						</thead>
						<tbody id="prodcutTbody" style="width: 100%"></tbody>
					</table>	
				</td>
				<%-- 新价格需求，测边框被移除
				<td  valign="top" style="min-width:210px;padding-left:10px;">
					<table>
						<tr>
						<td  valign="top" style="min-width:210px;padding-left:10px;padding-bottom: 10px;">
							<div style="font-size:14px;font-weight:bold;background-color:#fafafa;">
							<div style="height:50px;padding:10px;border-bottom:1px solid #d9d9d9;">
								推荐企业
							</div>
							<div id="recomdEn" style="padding: 15px 0 10px;">
								<ul style="list-style: none;text-align: center;overflow: hidden;padding: 0">
									<li style="float: left;width: 50%;margin-bottom: 10px;marigin: 14px 4px 0px 4px;"><img src="/sm-shop/resources/img/recomd1.png"/></li>
									<li style="float: left;width: 50%;margin-bottom: 10px;marigin: 14px 4px 0px 4px;"><img src="/sm-shop/resources/img/recomd2.png"/></li>
									<li style="float: left;width: 50%;margin-bottom: 10px;marigin: 14px 4px 0px 4px;"><img src="/sm-shop/resources/img/recomd3.png"/></li>
									<li style="float: left;width: 50%;margin-bottom: 10px;marigin: 14px 4px 0px 4px;"><img src="/sm-shop/resources/img/recomd4.png"/></li>
									<li style="float: left;width: 50%;margin-bottom: 10px;marigin: 14px 4px 0px 4px;"><img src="/sm-shop/resources/img/recomd5.png"/></li>
									<li style="float: left;width: 50%;margin-bottom: 10px;marigin: 14px 4px 0px 4px;"><img src="/sm-shop/resources/img/recomd6.png"/></li>
								</ul>
							</div>
							</div>					
						</td></tr>
						<tr>
						<td  valign="top" style="min-width:210px;padding-left:10px;">
							<div style="font-size:14px;font-weight:bold;background-color:#fafafa;">
							<div style="height:50px;padding:10px;border-bottom:1px solid #d9d9d9;">
								最近浏览
							</div>
							<div id="hisbrow"></div>
							</div>					
						</td>
						</tr>
					</table>				
				</td>
				--%>
			</tr>			
			
		</table>
		<br>
		<div style="border: 1px solid #d9d9d9;width:100%">	
		<div class=" row ">
		<div class="col-sm-3 col-md-3 col-md-offset-0 col-lg-3 col-lg-offset-0" style="padding-top:15px;">
			<span id="ptitle1" style="margin-left:20px" ></span>
		</div>
		<div class="col-sm-6 col-md-6 col-md-offset-0 col-lg-6 col-lg-offset-0 responsive" >
			<div id="pagination1" style="padding-top:5px;margin-bottom:-5px !important;padding-right:20px;text-align: center;"></div>
		</div>
		<div class="col-sm-3 col-md-3 col-md-offset-0 col-lg-3 col-lg-offset-0">
			<div id="ptitle2" style="padding-top: 10px;text-align: right;margin-right: 15px;">每页  
			<select style="height:24px;padding:0;border:1px solid #E5E7E9;border-radius:0px;"><option>20</option></select>  条</div>
		</div>
	</div>
</div>
<br>
<script>
jQuery(function($) {
  
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
  	    $.cookie("history",json,{expires:1}); 
  	} 
  	
//      alert(cookieVal);
     
  }
 
});			
			
</script>


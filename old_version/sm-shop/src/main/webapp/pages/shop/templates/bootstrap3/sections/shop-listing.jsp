<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %> 
<%@ page session="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
 request.setCharacterEncoding("UTF-8");
%>
var more = '<s:message code="label.product.moreitems" text="More" />';
var addCart='<s:message code="button.label.addToCart" text="Add to cart" />';
/**
* Builds the product container div from the product list
**/
function buildProductsList(productList) {
   products = new Array();
   services = new Array();
   typeid='';
   var tbBody ='';
	if(productList != null && productList.length>0){
		$("#prodcutTbody").html("");
		$.each(productList, function(i, p) {
		   	if(p.auth_type == '1'){
				typeid='1';
		    }else if(p.auth_type == '2'){
		        typeid='2';
		    }
			if(p.cateType<4){
				products[products.length]=p.id;	
			}else{
				services[services.length]=p.id;
			}
			
			var diamondStyle="";
			if (p.diamond){
				tbBody += '<tr style="height:120px;background-color: #fffacb; border: 5px solid #FFF;padding-top:15px; " diamond="true"><td width="10%"><a target="_blank"  href="/sm-shop/shop/product/' +p.id + '.html">';
			}else{
				tbBody += '<tr style="height:120px;background-color: #FFFFFF; border: 5px solid #FFF;padding-top:15px; "><td width="10%"><a target="_blank"  href="/sm-shop/shop/product/' +p.id + '.html">';
			}
			
			// <%-- 上海源叶生物科技有限公司 特别修改 --%>
			var searchedStoreName = p.storeName.replace(/<[^>]+>/g,"");
			if (searchedStoreName=="上海源叶生物科技有限公司"){
				tbBody += '<img src="/sm-shop/resources/img/brands/66.jpg" style="width:98px;height:92px;margin: 5px 5px">';
			}else{
			 if(p.imageUrl!=null) {
			 	tbBody += '<img src="/sm-shop' + p.imageUrl +'" style="width:98px;height:92px;margin: 5px 5px">';
			 }else{
					tbBody += '<img src="/sm-shop/resources/img/0'+p.cateType+'.png" style="width:98px;height:92px;margin: 5px 5px">';
			}
			}
			// <%-- end of 上海源叶生物科技有限公司 特别修改 --%>
			// <%-- 取消修改后，把if (searchedStoreName=="上海源叶生物科技有限公司")的else块留出来就好了 --%>
			
		    tbBody += '</a></td>';
        
			tbBody+='<td width="20%" style="word-break: break-all;padding-left:8px">'
		    tbBody+='<a target="_blank"  href="/sm-shop/shop/product/' +p.id + '.html?id='+p.auth_id+'&typename='+typeid+'">';
			if (p.diamond){
				tbBody+='<div class="diamond" style="width:90px; height:20px; background:#ff313b; -moz-border-radius: 4px;-webkit-border-radius: 4px;" \
						title="<s:message code='lable.product.diamond.flagtext1' text='7天无条件退换货'/>"> \
						<img src="/sm-shop/resources/img/icon-diamond.gif" style=" margin-top:-3px; padding-left:5px; display:inline; vertical-align: middle;" /> \
                        <span style="display:inline-block; height:20px;line-height:20px;font-size:14px; color:#ffffff;" ><s:message code='lable.product.diamond.flagtext2' text='钻石产品'/></span> \
					</div>';
			}
		    tbBody+='<span style="line-height:20px;font-weight:bold;color:#123" >'+p.productName+"</span>";

		    if (p.auth_type == '1') {
				tbBody += '&nbsp;&nbsp;<a href="${pageContext.request.contextPath}/shop/authorizationDetail.html?id='+p.auth_id+'"><img src="/sm-shop/resources/img/factory.png"  style="margin-left:0px;"/></a>'
			}
			if (p.auth_type == '2'||p.period == 1){
				tbBody += ''
			}
			if(p.productEnname!=null&&p.productEnname!=null) {
					tbBody += '<p>'+p.productEnname + '</p>';
			}
			tbBody += '</a>';
			//tbBody +='<p>'+ '<span style="color:#4285f4;">'+p.storeName+'</span></p>';
			tbBody +='<p>'+ '<span>'+p.storeName+'</span></p>';
			
			<%-- if (p.auth_type == '1') {
				tbBody += '&nbsp;&nbsp;<a href="${pageContext.request.contextPath}/shop/authorizationDetail.html?id='+p.auth_id+'"><img src="/sm-shop/resources/img/stars/factory.png" width="60px" height="auto"/></a>'
			} else if (p.auth_type == '2') {
				tbBody += '&nbsp;&nbsp;<a href="${pageContext.request.contextPath}/shop/authorizationDetail.html?id='+p.auth_id+'"><img src="/sm-shop/resources/img/stars/agency.png" width="60px" height="auto"/></a>'
			} --%>
			tbBody += '</td>'
			
			// 品牌和货号
			if(p.manufacturer==''||p.manufacturer==null) {
				if(p.code!='' || p.code!=null){
				   tbBody += '<td width="12%" style="text-align:center;padding:0px 10px;">' + '<td width="7%" style="text-align:center;padding:0px 10px;">'+p.code+'</td>'; 
				}else{
				   tbBody += '<td width="12%" style="text-align:center;padding:0px 10px;">'+'<td width="7%" style="text-align:center;padding:0px 10px;">'+'</td>'; 
				}
			} else {				
			    if(p.code!=null || p.code!='' || p.code !='null'){
			       tbBody += '<td width="12%" style="text-align:center;padding:0px 10px;color:#4285f4;">' + p.manufacturer+'<td width="7%" style="text-align:center;padding:0px 10px;">'+p.code+'</td>'; 
			    }else{
				  tbBody += '<td width="12%" style="text-align:center;padding:0px 10px;color:#4285f4;">' + p.manufacturer+'<td width="7%" style="text-align:center;padding:0px 10px;">'+'</td>';
			    }
			    
			}
			// 价格相关
			tbBody +='<td width="39%" id="price_'+p.id+'" style="text-align:center;padding:0 5px"></td>';
			tbBody += '<td width="12%" style="text-align:center;padding:0 5px" valign="middle">'
			if(p.quality >0){
				var modNum = p.quality%20;
				var intNum = parseInt(p.quality/20);
				
				tbBody +='<p style="margin-bottom:5px;">';
				for(var index=0;index<intNum;index++){
					tbBody += '<img src="/sm-shop/resources/img/stars/star-on.png" style="margin-top:-2px;margin-right:5px;" >'
				}
				if(modNum>0){
					tbBody +='<img src="/sm-shop/resources/img/stars/star-half.png" style="margin-top:-2px;margin-right:5px;">'
				}
				tbBody +='</p>';
				tbBody +='<p><span style="font-weight:500;display:inline;color:red;">'+(p.quality/20).toFixed(1)+'</span>'+'<span style="color:#999;font-weight:500;">分</span></p>';
			}		
			tbBody+='</td></tr>';
			tbBody+='<tr><td colspan="6" class="tr-search">'
			if(p.productDesc != null){
			 var len = 0;    
			    for (var i=0; i<p.productDesc.length; i++) {    
			        if (p.productDesc.charCodeAt(i)>127 || p.productDesc.charCodeAt(i)==94) {    
			             len += 2;    
			         } else {    
			             len ++;    
			         }    
			     }    
		    	<!-- tbBody += '<div id="content_tr_'+p.id+'" class="search_fixed_height" style="padding-top:7px;padding-left:80px;margin-top:-15px;width:820px;max-width:820px;">'+p.productDesc+'</div>'; -->
		    	if(len>140){
		    		<!-- tbBody += '<div style="padding-top:5px;margin-top:-15px;padding-left:80px;float:right"><a href= "javascript:void(0);" onclick="showArrow(&quot;content_tr_'+p.id+'&quot;,&quot;arrow_'+p.id+'&quot;)" ><img style="padding-top:5px;" width="15" src="/sm-shop/resources/img/downa.png"  id="arrow_'+p.id+'"/></a> </div>'; -->
		    	}
		    	
			}
			<!-- tbBody +='<div class="row" style="border-bottom:1px dashed #ddd;padding-top:0px;padding-left:80px;width:820px;max-width:820px;"></div>' -->
			tbBody+='</td></tr>'
		});
		getPrice();
		}
		
		$("#prodcutTbody").append(tbBody);
		$("#prodcutTbody tr").mouseover(function(){
	             $(this).css({"backgroundColor":"#ebf2fc"});
	     }).mouseout(function(){
				if ( $(this).attr("diamond") == "true"){
	             $(this).css({"backgroundColor":"#fffacb"});
				}else{
				 $(this).css({"backgroundColor":"#FFFFFF"});
				}
	    });
	    
	    
    
		
}


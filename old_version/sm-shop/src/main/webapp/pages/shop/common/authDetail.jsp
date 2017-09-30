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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<script src="<c:url value="/resources/js/jquery.printElement.min.js" />"></script>
<script src="<c:url value="/resources/js/browser.js" />"></script>
<style type="text/css">
.tdright{
padding-left:30px;
padding-top:30px;
}
.tdleft{
padding-top:30px;
}
.storeright{
padding-top:16px;
}
.storeleft{
padding-top:16px;
}
</style>
<script type="text/javascript">
$(function(){
    //show cookie
    
	var json = eval("("+$.cookie("history")+")");
    if(json !=null && json.length>0){
    	 var list = '<table style="width:100%">'; 
        for(var i=0; i<json.length;i++){         	
         	list +='<tr style="border-top:1px solid #d9d9d9;"><td valign="top" style="height:70px;"><img style="background-color:#fafafa;max-width:70px;max-height:70px;padding:10px 10px;width:60px;height:60px;" src="/sm-shop'+json[i].img+'"></td>';
         	list +='<td valign="top" style="font-size:12px;padding:10px 10px;"><a target="_blank"  href="<c:url value="/shop/product/"/>' +json[i].id + '.html">'+json[i].name+'</a><br>';
         	var star =(json[i].quilty)/20;
         	var left=(json[i].quilty)%20;
         	for(var j=1;j<star;j++){
         		list +='<img style="max-width:15px;max-height:15px;padding:2px 0px;"  src="<c:url value="/resources/img/stars/star-on.png" />">';
         	}
         	if(left>0){
         		list +='<img style="max-width:15px;max-height:15px;padding:2px 0px;"  src="<c:url value="/resources/img/stars/star-half.png" />">	';
         	}
         	
         	list +='</td></tr>';           
         }
         list +='</table>'
         $("#my_history").html(list); 
    }
});
</script>
<div class="detail_out" style="padding:0 120px;height:800px;">
<div style="margin-top:38px;margin-bottom:22px;"><a href="${pageContext.request.contextPath }">首页</a>>资质详情</div>
<div style="font-weight:600; width:706px;height:622px;border-top:1px solid #d9d9d9;float:left;">
	<div style="margin-top:20px;font-size:16px;">资质详情</div>
	<div style="padding-left:60px;font-size:14px;">
		<table>
		<tr><td class="tdleft">品牌：</td>
			<td class="tdright"><c:forEach items="${auth.manufacturer}" var="m">
					<c:forEach items="${m.descriptions}" var="d">
						${d.name }&nbsp;&nbsp;
					</c:forEach>
					</c:forEach>
		</td></tr>
		<tr><td class="tdleft">授权单位：</td><td class="tdright">${auth.company }</td></tr>
		<tr><td class="tdleft">有效时间：</td><td class="tdright">${auth.startTime} 至 ${auth.endTime}</td></tr>
		<tr><td  class="tdleft" valign="top">授权书快照：</td><td class="tdright"><div><img style="width:160px;height:180px;" src="${pageContext.request.contextPath}/file/showImage.html?path=${auth.image }"  /></div></td></tr>
		<tr><td class="tdleft">授权书介绍：</td><td class="tdright"><div style="width:470px;">${auth.introduce }</div></td></tr>
		
		</table>
	</div>
</div>
<div style="float:left;">
<div style="width:234px;height:338px;border:1px solid #d9d9d9;margin-left:20px;">
<div style="text-align: center;"><img style="width:100%;height:68px;" alt="卖家logo" src="${pageContext.request.contextPath}/file/showImage.html?path=${store.storeLogo }"  /></div>
<div style="width:100%;">
<div style="width:100%;text-align: center;padding-top:14px;color:blue;">${store.storename }</div>
<div style="width:100%;font-size:12px;padding-left:14px;"><table>
<tr><td class="storeleft">联系人：</td><td class="storeright">${store.storecontacts } &nbsp;&nbsp;&nbsp;&nbsp;
<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=${store.qqNum}&site=qq&menu=yes">
<img alt="" src="<c:url value="/resources/img/redqq.png"/>" /></a>
</td></tr>
<tr><td class="storeleft">电&nbsp;&nbsp;&nbsp;&nbsp;话：</td><td class="storeright">${store.storephone }</td></tr>
<tr><td class="storeleft">手&nbsp;&nbsp;&nbsp;&nbsp;机：</td><td class="storeright">${store.storemobile }</td></tr>
<tr><td class="storeleft">网&nbsp;&nbsp;&nbsp;&nbsp;址：</td><td class="storeright">${store.storeUrl }</td></tr>
<tr><td class="storeleft">邮&nbsp;&nbsp;&nbsp;&nbsp;箱：</td><td class="storeright">${store.storeEmailAddress }</td></tr>
<tr><td class="storeleft">地&nbsp;&nbsp;&nbsp;&nbsp;址：</td><td class="storeright">${store.storeaddress }</td></tr>
</table></div></div>
</div>
<div style="width:234px;border:1px solid #d9d9d9;margin-left:20px;margin-top:10px;">
<div style="padding-left:10px;padding-top:12px;padding-bottom:10px;">最近浏览</div>
<div id="my_history">

</div>
</div>

</div>
</div>
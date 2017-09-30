<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<div style="padding:12px 20px;font-size:12px;word-wrap:break-word;word-break: break-all;">
	<c:if test="${product.store.useQQ == true}">
		<c:if test="${product.store.qqNum != null}">
			
			<div style="display:inline-block; vertical-align: top;">Q &nbsp;&nbsp; Q：</div>
			<div style="display:inline-block; width: 100px;word-wrap:break-word;">${product.store.qqNum}</div>		
			<c:set var="merchant_qq_no" value="${product.store.qqNum}"/>
			<c:if test="${fn:contains(product.store.qqNum,',')}">
				<c:set var="commar_pos" value="${fn:indexOf(product.store.qqNum,',')}"/>
				<c:set var="merchant_qq_no" value="${fn:substring(product.store.qqNum,0,commar_pos)}"/>
				<c:set var="merchant_qq_no" value="${fn:replace(merchant_qq_no,'-','')}"/>
			</c:if>
			<a style="padding-left:16px;vertical-align: top;" target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=${merchant_qq_no}&site=qq&menu=yes">
				<span style="line-height:25px;"></span>
				<img border="0" src="http://wpa.qq.com/pa?p=2:${merchant_qq_no}:52" />
			</a>
		</c:if>
	</c:if>
</div>
<c:if test="${not empty product.store.storephone}">
	<div style="padding:12px 20px;font-size:12px;color:red;">
		<div style="display:inline-block; vertical-align: top;">电&nbsp;&nbsp; 话：</div>
		<div style="display:inline-block;width: 140px; word-wrap: break-word;">${product.store.storephone}</div>
	</div>
</c:if>
<c:if test="${not empty product.store.storemobile}">
	<div style="padding:12px 20px;font-size:12px;">
		手&nbsp;&nbsp; 机：${product.store.storemobile}				
	</div>
</c:if>
<c:if test="${not empty product.store.storeUrl}">
	<div style="padding:12px 20px;font-size:12px;word-break: break-all; word-wrap:break-word;">
		<div style="display:inline-block; vertical-align: top;">网&nbsp;&nbsp; 址：</div>
		<div style="display:inline-block; width: 140px;">${product.store.storeUrl}</div>		
	</div>
</c:if>
<c:if test="${not empty product.store.storeEmailAddress}">
	<div style="padding:12px 20px;font-size:12px;word-break: break-all; word-wrap:break-word;">
		<div style="display:inline-block; vertical-align: top;">邮&nbsp;&nbsp; 箱：</div>
		<div style="display:inline-block; width: 140px;">${product.store.storeEmailAddress}</div>				
	</div>
</c:if>
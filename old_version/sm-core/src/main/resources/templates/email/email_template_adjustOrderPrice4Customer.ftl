<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<!-- If you delete this meta tag, Earth will fall into the sun. -->
<meta name="viewport" content="width=device-width" />

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
	
<style type="text/css">

/* ------------------------------------- 
		GLOBAL 
------------------------------------- */
* { 
	margin:0;
	padding:0;
}
* { font-family: "Helvetica Neue", "Helvetica", Helvetica, Arial, sans-serif; }

img { 
	max-width: 100%; 
}
.collapse {
	margin:0;
	padding:0;
}
body {
	-webkit-font-smoothing:antialiased; 
	-webkit-text-size-adjust:none; 
	width: 100%!important; 
	height: 100%;
}


/* ------------------------------------- 
		ELEMENTS 
------------------------------------- */
a { color: #2BA6CB;}

.btn {
	text-decoration:none;
	color: #FFF;
	background-color: #666;
	padding:10px 16px;
	font-weight:bold;
	margin-right:10px;
	text-align:center;
	cursor:pointer;
	display: inline-block;
}

p.callout {
	padding:15px;
	background-color:#ECF8FF;
	margin-bottom: 15px;
}
.callout a {
	font-weight:bold;
	color: #2BA6CB;
}

table.social {
/* 	padding:15px; */
	background-color: #ebebeb;
	
}
.social .soc-btn {
	padding: 3px 7px;
	font-size:12px;
	margin-bottom:10px;
	text-decoration:none;
	color: #FFF;font-weight:bold;
	display:block;
	text-align:center;
}
a.fb { background-color: #3B5998!important; }
a.tw { background-color: #1daced!important; }
a.gp { background-color: #DB4A39!important; }
a.ms { background-color: #000!important; }

.sidebar .soc-btn { 
	display:block;
	width:100%;
}

/* ------------------------------------- 
		HEADER 
------------------------------------- */
table.head-wrap { width: 100%;background:#2672c8;height:70px;}

.header.container table td.logo { padding: 15px; }
.header.container table td.label { padding: 15px; padding-left:0px;}


/* ------------------------------------- 
		BODY 
------------------------------------- */
table.body-wrap { width: 100%;background:#fff;}


/* ------------------------------------- 
		FOOTER 
------------------------------------- */
table.footer-wrap { width: 100%;background:#2672c8;clear:both!important;}
.footer-wrap .container td.content  p { border-top: 1px solid rgb(215,215,215); padding-top:15px;}
.footer-wrap .container td.content p {
	font-size:10px;
	font-weight: bold;
	
}
/*----------------------------------
 BODY CONTENT-TABLE
------------------------------------*/

.content-table table{border-collapse:collapse; border-spacing:0; border-left:1px solid #aaa; border-top:1px solid #aaa;width:100% }
.content-table td{border-right:1px solid #aaa; border-bottom:1px solid #aaa; padding:3px 15px; text-align:left; color:#3C3C3C;}

/* ------------------------------------- 
		TYPOGRAPHY 
------------------------------------- */
h1,h2,h3,h4,h5,h6 {
font-family: "HelveticaNeue-Light", "Helvetica Neue Light", "Helvetica Neue", Helvetica, Arial, "Lucida Grande", sans-serif; line-height: 1.1; margin-bottom:15px; color:#000;
}
h1 small, h2 small, h3 small, h4 small, h5 small, h6 small { font-size: 60%; color: #6f6f6f; line-height: 0; text-transform: none; }

h1 { font-weight:200; font-size: 44px;}
h2 { font-weight:200; font-size: 37px;}
h3 { font-weight:500; font-size: 27px;}
h4 { font-weight:500; font-size: 23px;}
h5 { font-weight:900; font-size: 17px;}
h6 { font-weight:900; font-size: 14px; text-transform: uppercase; color:#444;}

.collapse { margin:0!important;}

p, ul { 
	margin-bottom: 10px; 
	font-weight: normal; 
	font-size:14px; 
	line-height:1.6;
}
p.lead { font-size:17px; }
p.last { margin-bottom:0px;}

ul li {
	margin-left:5px;
	list-style-position: inside;
}

/* ------------------------------------- 
		SIDEBAR 
------------------------------------- */
ul.sidebar {
	display:block;
	list-style-type: none;
}
ul.sidebar li { display: block; margin:0;}
ul.sidebar li a {
	text-decoration:none;
	color: #666;
	padding:10px 16px;
/* 	font-weight:bold; */
	margin-right:10px;
/* 	text-align:center; */
	cursor:pointer;
/**	border-bottom: 1px solid #777777;**/
/**	border-top: 1px solid #FFFFFF;**/
	display:block;
	margin:0;
}
ul.sidebar li a.last { border-bottom-width:0px;}
ul.sidebar li a h1,ul.sidebar li a h2,ul.sidebar li a h3,ul.sidebar li a h4,ul.sidebar li a h5,ul.sidebar li a h6,ul.sidebar li a p { margin-bottom:0!important;}



/* --------------------------------------------------- 
		RESPONSIVENESS
		Nuke it from orbit. It's the only way to be sure. 
------------------------------------------------------ */

/* Set a max-width, and make it display as block so it will automatically stretch to that width, but will also shrink down on a phone or something */
.container {
	display:block!important;
	max-width:100%!important;
	margin:0 auto!important; /* makes it centered */
	clear:both!important;
}

/* This should also be a block element, so that it will fill 100% of the .container */
.content {
	padding:15px;
	max-width:100%;
	margin:0 auto;
	display:block; 
}

/* Let's make sure tables in the content area are 100% wide */
.content table { width: 100%; }


/* Odds and ends */
.column {
	width: 300px;
	float:left;
}
.column tr td { padding: 15px; }
.column-wrap { 
	padding:0!important; 
	margin:0 auto; 
	max-width:600px!important;
}
.column table { width:100%;}
.social .column {
	width: 280px;
	min-width: 279px;
	float:left;
}

/* Be sure to place a .clear element after each set of columns, just to be safe */
.clear { display: block; clear: both; }

.border {border:1px solid}

/* ------------------------------------------- 
		PHONE
		For clients that support media queries.
		Nothing fancy. 
-------------------------------------------- */
@media only screen and (max-width: 600px) {
	
	a[class="btn"] { display:block!important; margin-bottom:10px!important; background-image:none!important; margin-right:0!important;}

	div[class="column"] { width: auto!important; float:none!important;}
	
	table.social div[class="column"] {
		width:auto!important;
	}

}

</style>

</head>
 
<body bgcolor="#FFFFFF" topmargin="0" leftmargin="0" marginheight="0" marginwidth="0">
<div style="width:100%;border:1px solid #eee">
<!-- HEADER -->
<table class="head-wrap">
	<tr>
		<td></td>
		<td class="header container" >
				<div>
				<table>
					<tr>
						<td>
							${LOGOPATH}
						</td>
					</tr>
				</table>
				</div>
		</td>
		<td></td>
	</tr>
</table>
<!-- /HEADER -->
<h4>${recieverName}，您好：</h4>
<div class="content">
	<!-- order summary -->
	<p>
	您的订单价格已被卖家修改，请确认。<br/>
	如有问题，请联系百图生物。E-mail:order@bettbio.com. Tel:021-61552750,021-61552739 
	</p>
	<!--<hr/>-->
	<br/>
	<!-- order dertail -->
	<div class="content-table">
		<h4>订单明细</h4>
		<table>
	<#list subOrders as subOrder>
			<tr>
				<td colspan="4">订单号：${subOrder.id?c}. &nbsp; 卖家：${subOrder.storeName!}</td>
				<td>
					<#if subOrder.finalTotal?has_content>
						小计：<b>¥${subOrder.finalTotal?string(',###.00')}</b>
						<s>¥${subOrder.total?string(',###.00')}</s>
					<#else>
						小计：¥${subOrder.total?string(',###.00')}
					</#if>
				</td>
			</tr>
		<#list subOrder.orderProducts as product>
			<tr>
				<td>
					<#if product.productImageUrl?has_content>
						<img width="100px" height="100px" src="http://www.bettbio.com/sm-shop${product.productImageUrl}"/>
					<#else>
						<img width="100px" height="100px" src="http://www.bettbio.com/sm-shop/resources/img/pimg.jpg"/>
					</#if>
				</td>
				<td>
					<#if product.productCode?has_content>${product.productCode}<br/></#if>
					<#if product.productName?has_content>${product.productName}<br/></#if>
					<#if product.productEnName?has_content>${product.productEnName}<br/></#if>
				</td>
				<td>
					${product.specifications!"无说明"}
				</td>
				<td>
					数量：${product.productQuantity}<br/>
					<#if product.finalPrice?has_content>
						单价：<b>¥${product.finalPrice?string(',###.00')}</b> <br/><s> 原价 ¥${product.price?string(',###.00')}</s>
					<#else>
						单价：¥${product.price?string(',###.00')} 
					</#if>
				</td>
				<td>
					<#if product.finalTotal?has_content>
						<b>¥${product.finalTotal?string(',###.00')}</b> <br/>
						<s> 原价 ¥${product.oneTimeCharge?string(',###.00')}</s>
					<#else>
						¥${product.oneTimeCharge?string(',###.00')} 
					</#if>
				</td>
			</tr>
		</#list>
			<tr><td  colspan="6"> </td></tr>
	</#list>
		</table>
	</div>
	<br/>
	<!--<hr/>-->
	<!-- shipping info  -->
	<div class="content">
		<table>
			<tbody>
				<tr valign="top">
					<td>
						<h5>收货信息 &raquo;</h5>
						<p>
						<#if shippingInfo.address.company?has_content>${shippingInfo.address.company}<br/></#if>
						<#if shippingInfo.address.name?has_content>${shippingInfo.address.name}<br/></#if>
						<#if shippingInfo.address.telephone?has_content>${shippingInfo.address.telephone}<br/></#if>
						<#if shippingInfo.address.streetAdress?has_content>${shippingInfo.address.streetAdress}<br/></#if>
						<#if shippingInfo.address.city?has_content>${shippingInfo.address.city}<br/></#if>
						<#if shippingInfo.address.zone?has_content>${shippingInfo.address.zone}<br/></#if>
						<#if shippingInfo.address.country?has_content>${shippingInfo.address.country}<br/></#if>
						<#if shippingInfo.address.postCode?has_content>${shippingInfo.address.postCode}<br/></#if>
						</p>
					</td>
					<td>
						<h5>开票信息 &raquo;</h5>
						<p>
							<#if shippingInfo.invoice.company?has_content>${shippingInfo.invoice.company}<br/></#if>
							<#if shippingInfo.invoice.companyAddress?has_content>${shippingInfo.invoice.companyAddress}<br/></#if>
							<#if shippingInfo.invoice.companyTelephone?has_content>${shippingInfo.invoice.companyTelephone}<br/></#if>
							<#if shippingInfo.invoice.bankName?has_content>${shippingInfo.invoice.bankName}<br/></#if>
							<#if shippingInfo.invoice.bankAccount?has_content>${shippingInfo.invoice.bankAccount}<br/></#if>
							<#if shippingInfo.invoice.taxpayerNumber?has_content>${shippingInfo.invoice.taxpayerNumber}<br/></#if>
						</p>
					</td>
					<td>
						<h5>发票邮寄信息 &raquo;</h5>
						<p>
						<#if shippingInfo.invoiceAddr.company?has_content>${shippingInfo.invoiceAddr.company}<br/></#if>
						<#if shippingInfo.invoiceAddr.name?has_content>${shippingInfo.invoiceAddr.name}<br/></#if>
						<#if shippingInfo.invoiceAddr.telephone?has_content>${shippingInfo.invoiceAddr.telephone}<br/></#if>
						<#if shippingInfo.invoiceAddr.streetAdress?has_content>${shippingInfo.invoiceAddr.streetAdress}<br/></#if>
						<#if shippingInfo.invoiceAddr.city?has_content>${shippingInfo.invoiceAddr.city}<br/></#if>
						<#if shippingInfo.invoiceAddr.zone?has_content>${shippingInfo.invoiceAddr.zone}<br/></#if>
						<#if shippingInfo.invoiceAddr.country?has_content>${shippingInfo.invoiceAddr.country}<br/></#if>
						<#if shippingInfo.invoiceAddr.postCode?has_content>${shippingInfo.invoiceAddr.postCode}<br/></#if>
						</p>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
<!-- FOOTER -->
<table class="footer-wrap">
	<tr>
		<td></td>
		<td class="container">
			
				<!-- content -->
				<div class="content" style="padding:5px!important">
				<table>
				<tr>
					<td align="center" style="font-size:12px;color:#fff;font-line:16px;">
我们的客户之一给了我们您的电子邮箱地址。如果您觉得您收到这封电子邮件是个错误，请发送一封电子邮件到service@bettbio.com以取消后续邮件。
本邮件是系统自动发送的。您可以在登录系统配置您的信息接收方式，我们将尊重您的选择。
版权@ 百图生物 2017，版权所有
					</td>
				</tr>
				</table>
				</div><!-- /content -->
		</td>
		<td></td>
	</tr>
</table><!-- /FOOTER -->
</div>
</body>
</html>






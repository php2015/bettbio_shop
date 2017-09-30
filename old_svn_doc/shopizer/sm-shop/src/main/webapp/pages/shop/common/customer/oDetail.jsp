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
<link href="<c:url value="/resources/css/personCenter.css" />" rel="stylesheet"></link>
<div class="detail_out">
<div style="margin-top:38px;margin-bottom:22px;"><a>首页</a>><a>我的订单</a>>订单详情</div>
<div style="width:100%;height:222px;border:1px solid #d9d9d9;">
<div style="float:left;width:372px;height:100%;font-size:14px;color:#333;">
<div class="order_detail">
	订单详情
</div>
<div style="padding:35px 28px;">
	<div>
		<div style="float:left;">收货地址：</div>
		<div style="float:left;padding-left:22px;width:242px;">上海市杨浦区国定东路创业园区1001</div>
	</div><br>
	<div style="margin-top:30px;"><div style="float:left;">订单号：</div><div style="float:left;padding-left:36px;">6666666666666666</div></div>
	<br>
	<div style="margin-top:30px;"><div style="float:left;">商家：</div><div style="float:left;padding-left:50px;width:274px;">上海中乔新舟生物科技有限公司</div></div>
	
</div>
</div>
<div style="border-left:1px solid #d9d9d9;height:100%;width:826px;float:left;background-color: #fff;color:#666;">
	<div style="float:left;text-align:center;padding-top:50px;">
	<img alt="" src="<c:url value='/resources/img/order.png' />" />
	<div style="padding-top:20px;">提交订单</div>
	<div style="width:100px;padding-top:10px;">2016-04-08 23:11:49</div>
	</div>
	<div style="float:left;padding-top:66px;">
	<img alt="" src="<c:url value='/resources/img/spe_arr.png' />" />
	</div>
	<div style="float:left;text-align:center;padding-top:50px;">
	<img alt="" src="<c:url value='/resources/img/songhuo.png' />" />
	<div style="padding-top:20px;">商品出货</div>
	<div style="width:100px;padding-top:10px;">2016-04-08 23:11:49</div>
	</div>
	<div style="float:left;padding-top:66px;">
	<img alt="" src="<c:url value='/resources/img/spe_arr.png' />" />
	</div>
	<div style="float:left;text-align:center;padding-top:50px;">
	<img alt="" src="<c:url value='/resources/img/fahuo.png' />" />
	<div style="padding-top:20px;">等待出货</div>
	<div style="width:100px;padding-top:10px;">2016-04-08 23:11:49</div>
	</div>
	<div style="float:left;padding-top:66px;">
	<img alt="" src="<c:url value='/resources/img/spe_arr.png' />" />
	</div>
	<div style="float:left;text-align:center;padding-top:50px;">
	<img alt="" src="<c:url value='/resources/img/closeorder.png' />" />
	<div style="padding-top:20px;">签收（完成）</div>
	<div style="width:100px;padding-top:10px;">2016-04-08 23:11:49</div>
	</div>
	
</div>
</div>
<div style="border:1px solid #d9d9d9;margin:22px 0;">
<div style="font-size: 12px;width:100%;height:34px;line-height: 34px;">
	<span style="padding-left:174px;">商品</span>
	<span style="padding-left:170px;">规格</span>
	<span style="padding-left:120px;">数量</span>
	<span style="padding-left:120px;">单价</span>
	<span style="padding-left:120px;">金额</span>
	<span style="padding-left:106px;">发货状态</span>
	<span style="padding-left:106px;">评价</span>
</div>
<div style="width:100%;height:36px;background-color: #eee;line-height:36px;font-size:12px;color:#666;">
	<span style="padding-left:24px;">2016-01-10</span>
	<span style="padding-left:80px;">顺丰快递</span>
	<span style="padding-left:32px;">运单号：54353454354353453</span>
</div>
</div>
<div style="width:100%;height:138px;border:1px solid #d9d9d9;font-size:12px;">
		<div style="margin-top:20px;margin-left:22px;float:left;">
			<a href="#"><img style="width:98px;height:98px;" src='<c:url value="/resources/img/prod_img.png" />'/></a>
		</div>
		<div style="margin-top:52px;margin-left:26px;float:left;">
			<div>平滑肌细胞培养基</div>
			<div>smooth muscle cell medium</div>
		</div>
		<div style="margin-top:63px;margin-left:60px;float:left;">500ml</div>
		<div style="margin-top:63px;margin-left:124px;float:left;">2</div>
		<div style="margin-top:63px;margin-left:126px;float:left;">￥1024</div>
		<div style="margin-top:63px;margin-left:112px;float:left;">￥2048</div>
		<div style="margin-top:63px;margin-left:105px;float:left;">
			<div>已完成</div>
		</div>
		<div style="margin-top:63px;margin-left:90px;float:left;"><a href="#">查看评价</a></div>
</div>
<div style="width:100%;height:318px;margin-top:22px;padding-top:30px;border:1px solid  #d9d9d9;">
	<div style="padding-left:140px;width:399px;height:100%;float:left;">
		<div style="font-size:14px;">收货信息</div>
		<div style="margin-top:18px;width:100%;font-size:12px;color:#333;">
			<div style="float:left;">收货人：</div><div style="float:left;color:#666;">venda</div>
		</div><br>
		<div class="min_info">
			<div style="float:left;">地址：</div><div style="float:left;color:#666;">上海市杨浦区国定东路创业园区1001号</div>
		</div><br>
		<div class="min_info">
			<div style="float:left;">手机号码：</div><div style="float:left;color:#666;">18511223344</div>
		</div>
		
	</div>
	<div class="order_info">
		<div style="font-size:14px;">开票信息</div>
		<div style="margin-top:18px;font-size:12px;color:#333;">
			<div style="float:left;">发票类型：</div><div style="float:left;color:#666;">增值税发票</div>
		</div><br>
		<div class="min_info">
			<div style="float:left;">发票抬头：</div><div style="float:left;color:#666;">上海抱团科技有限公司</div>
		</div><br>
		<div class="min_info">
			<div style="float:left;">纳税人识别码：</div><div style="float:left;color:#666;">22222222222333333333</div>
		</div><br>
		<div class="min_info">
			<div style="float:left;">注册地址：</div><div style="float:left;color:#666;">上海市杨浦区国定东路</div>
		</div><br>
		<div class="min_info">
			<div style="float:left;">注册电话：</div><div style="float:left;color:#666;">0434-85753432</div>
		</div><br>
		<div class="min_info">
			<div style="float:left;">银行账户：</div><div style="float:left;color:#666;">6677433333311111223</div>
		</div><br>
		<div class="min_info">
			<div style="float:left;">开户银行：</div><div style="float:left;color:#666;">上海市杨浦区国定东路建设银行</div>
		</div>
	</div>
	<div class="order_info">
		<div style="font-size:14px;">支付信息</div>
		<div style="margin-top:18px;font-size:12px;color:#333;">
			<div style="float:left;">支付类型：</div><div style="float:left;color:#666;">线下支付</div>
		</div><br>
		<div class="min_info">
			<div style="float:left;">支付时间：</div><div style="float:left;color:#666;">2016-05-29 17:18:22</div>
		</div><br>
		<div class="min_info">
			<div style="float:left;">商品总额：</div><div style="float:left;color:#666;">￥3456</div>
		</div><br>
		<div class="min_info">
			<div style="float:left;">支付优惠：</div><div style="float:left;color:#666;">￥30</div>
		</div><br>
		<div class="min_info">
			<div style="float:left;">积分类型：</div><div style="float:left;color:#666;">购物积分</div>
		</div><br>
		<div class="min_info">
			<div style="float:left;">已返积分：</div><div style="float:left;color:#666;">300</div>
		</div>
	</div>
</div>
</div>
<%
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<style>
.noviceguide {
	
}
noviceguide_head {
	margin:30px 0px;
}
.noviceguide_part {
	margin:10px 20px;
}
.noviceguide_title {
	font-size:16px;
	font-weight:600;
}
.noviceguide_msg {
}
.font_i {
	color:#FF6C00;
}
.noviceguide_part img {
	height: auto;
	max-width: 100%;
	padding:10px 0px;
}
</style>
<script>
</script>
<br>
<br>
<br>
<div class="head-navbar-left head-navbar-right" style="line-height:25px;">

	<div class="noviceguide">
		<h2 style="margin:0px">下单流程</h2>
		<hr/>
		
		<div class="noviceguide_head">
			<div class="noviceguide_title">1、访问<a href="wwww.bettbio.com" target="blank"><span class='font_i'>www.bettbio.com</span></a>网站，完成注册！
			</div>
			<div class="noviceguide_part">
				<em class="font_i">温馨提示：注册完成送50积分哦，积分可用于在平台的积分商城换购日常用品！</em><br/>
			    <img src='<c:url value="/resources/img/singleorder/01.jpg"/>'/>
		    </div>
		    
		    <div class="noviceguide_title">2、登录进入用户管理界面，可以编辑和查看用户、开票、发货和订单等信息。
			</div>
			<div class="noviceguide_part">
			    <img src='<c:url value="/resources/img/singleorder/02.jpg"/>'/><br/>
			    <img src='<c:url value="/resources/img/singleorder/03.jpg"/>'/><br/>
		    </div>
			
			<div class="noviceguide_title">3、平台首页可通过关键字匹配商品的中英文名称、商品简述搜索商品。
			</div>
			<div class="noviceguide_part">
			    <img src='<c:url value="/resources/img/singleorder/04.jpg"/>'/><br/>
			    <img src='<c:url value="/resources/img/singleorder/05.jpg"/>'/><br/>
		    </div>
		    
		    <div class="noviceguide_title">4、采用关键字搜索某个商品，会根据匹配度显示搜索结果，用户可以清楚的看到对应商品的供应情况，平台根据不同供应商提供的产品质量信息（文献引用、第三方认证、自检报告（实验数据）、购买凭证）权重加分得到产品的质量等级，科学家们可优先选择质量等级高的产品进行购买。
			</div>
			<div class="noviceguide_part">
			    <img src='<c:url value="/resources/img/singleorder/06.jpg"/>'/><br/>
		    </div>
		    
		    <div class="noviceguide_title">5、点击商品明细可以看到商品的详细信息，包括所有反映产品质量的信息。
			</div>
			<div class="noviceguide_part">
			    <img src='<c:url value="/resources/img/singleorder/07.jpg"/>'/><br/>
			    <img src='<c:url value="/resources/img/singleorder/08.jpg"/>'/><br/>
			    <img src='<c:url value="/resources/img/singleorder/09.jpg"/>'/><br/>
			    <img src='<c:url value="/resources/img/singleorder/10.jpg"/>'/><br/>
			    <img src='<c:url value="/resources/img/singleorder/11.jpg"/>'/><br/>
		    </div>
		    
		    <div class="noviceguide_title">6、选择想要购买的商品，点击“加入购物车”即可进行结算。
			</div>
			<div class="noviceguide_part">
			    <img src='<c:url value="/resources/img/singleorder/12.jpg"/>'/><br/>
		    </div>
		    
		    <div class="noviceguide_title">7、检查订单，再次确认产品数量、单价和总金额，确认订单。
			</div>
			<div class="noviceguide_part">
			    <img src='<c:url value="/resources/img/singleorder/13.jpg"/>'/><br/>
		    </div>
		    
		    <div class="noviceguide_title">8、选择或添加发货信息和开票信息，提交订单，目前平台支付方式为：月度结算。
			</div>
			<div class="noviceguide_part">
			    <img src='<c:url value="/resources/img/singleorder/14.jpg"/>'/><br/>
			    <img src='<c:url value="/resources/img/singleorder/15.jpg"/>'/><br/>
			    <img src='<c:url value="/resources/img/singleorder/16.jpg"/>'/><br/>
		    </div>
		    
		    <div class="noviceguide_title">9、提交订单后自动生成订单号，卖家会对订单进行发货处理。
			</div>
			<div class="noviceguide_part">
				<em class="font_i">温馨提示：首次成功下单即送150积分，积分可用于在平台的积分商城换购日常用品！</em><br/>
			    <img src='<c:url value="/resources/img/singleorder/17.jpg"/>'/><br/>
			    <img src='<c:url value="/resources/img/singleorder/18.jpg"/>'/><br/>
		    </div>
		    
		    <div class="noviceguide_title">10、点击用户界面的菜单“当前订单”，可以查看订单，并对商品进行评价或收货确认。
			</div>
			<div class="noviceguide_part">
			    <img src='<c:url value="/resources/img/singleorder/19.jpg"/>'/><br/>
		    </div>
		    
	    </div>
	    
	    
	</div>
</div>
<br>
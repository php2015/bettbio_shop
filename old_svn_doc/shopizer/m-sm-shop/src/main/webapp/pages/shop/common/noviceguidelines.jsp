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
.noviceguide img {
	height: auto;
	max-width: 100%;
	padding:10px 0px;
}
</style>
<script>
</script>
<div class="container-fluid box main-padding-lr" style="line-height:25px;">
	<div class="noviceguide">
		<h2 style="margin:0px">供应商入驻流程</h2>
		<hr/>
		<div class="noviceguide_msg alert alert-info">
		供应商与平台达成合作意向，签订合作协议后，平台将为供应商开通后台管理账户，以邮件的形式告之商家用户名及密码。<br/>
		<span style="color:red;font-weight:500;font-size:14px;">
				质量是产品第一核心竞争力。百图生物平台现推出，优质商品<mark>免费1年</mark>推广服务的优惠活动。</span>
				<br/>
				<span>
				如果协议商家能够提供以下维度反映产品质量的信息：<br/>
				1）文献引用；<br/>2）第三方认证；<br/>3）自检报告；<br/>4）权威购买凭证等。
				<br/>
				经平台审核通过，认定为优质商品，即可享受平台免费1年推广服务。
				</span>
		</div>
		
		<div class="noviceguide_head">
			<div class="noviceguide_title"><i class="glyphicon glyphicon-play"></i>&nbsp;&nbsp;商家登录</div>
			<div class="noviceguide_part">
				1、登录后台管理页面，进入Bettbio主页点击右上角“<a href='<c:url value="/admin"/>' target='blank'><span class='font_i'>商家管理</span></a>”。<br/>
			    <img src='<c:url value="/resources/img/guidelines/02.jpg"/>'/>
		    </div>
		    
		    <div class="noviceguide_part">
				2、输入平台分配给商家的用户名和密码，点击“<span class='font_i'>登录</span>”，进入商家主页面。<br/>
			    <img src='<c:url value="/resources/img/guidelines/03.jpg"/>'/>
		    </div>
			
			 <div class="noviceguide_part">
				3、进入商家主页面，可以管理<mark>店铺、品牌、商品、订单以及用户基本信息</mark>。<br/>
				<em class="font_i">温馨提示：初次登录，建议修改用户密码哟！</em>
				<br/>
			    <img src='<c:url value="/resources/img/guidelines/04.jpg"/>'/>
		    </div>
	    </div>
	    
	    <div class="noviceguide_head">
	    <div class="noviceguide_title"><i class="glyphicon glyphicon-play"></i>&nbsp;&nbsp;商品维护</div>
	    	
	     <div class="noviceguide_part">
			1、点击“<span class='font_i'>商品</span>”，进入商品管理界面。<br/>
		    <img src='<c:url value="/resources/img/guidelines/05.jpg"/>'/>
	    </div>
	    
	     <div class="noviceguide_part">
			2、单个创建商品：点击“<span class='font_i'>创建商品</span>”，进入创建商品页面。<br/>
		    <img src='<c:url value="/resources/img/guidelines/05-2.jpg"/>'/>
	    </div>
	    
	    <div class="noviceguide_part">
			3、录入基本信息后，点击“<span class='font_i'>保存</span>”，会自动跳出商品规格价格、文献引用、购买凭证、第三方认证、自检报告等其他商品子标签，商家可以点击相应标签进行相关信息的录入。<br/>
		    <img src='<c:url value="/resources/img/guidelines/05-3.jpg"/>'/>
	    </div>
	    
	    <div class="noviceguide_part">
			4、以标签“<span class='font_i'>文献引用集</span>”为例，展示文献引用信息录入界面。<br/>
		    <img src='<c:url value="/resources/img/guidelines/05-4.jpg"/>'/>
	    </div>
	   
	    <div class="noviceguide_part">
			5、 批量创建商品：点击“<span class='font_i'>商品数据导入</span>”按模板要求填写产品信息，完成产品信息填写后，点击“<span class='font_i'>提交</span>”即可。<br/>
			<em class="font_i">温馨提示：商品质量信息，我们希望能够以图片的形式，更加直观简洁的展现给科研人员，所以目前批量导入只支持商品基本信息，质量信息仍需要到产品目录，点击单个产品分别提交。</em><br/>
		    <img src='<c:url value="/resources/img/guidelines/05-5.jpg"/>'/><br/>
		    <img src='<c:url value="/resources/img/guidelines/05-6.jpg"/>'/>
	    </div>
	    
	    <div class="noviceguide_part">
			6、商品信息维护后，平台会及时审核，并邮件通知商家对应商品的审核结果，同时提示其产品质量信息还可进一步优化的环节。只有审核通过后，才可以在前台看到相应商品及其质量等级。<br/>
		    <img src='<c:url value="/resources/img/guidelines/06.jpg"/>'/><br/>
		    <img src='<c:url value="/resources/img/guidelines/07.jpg"/>'/>
	    </div>
	    
	    
	    </div>
	</div>
</div>
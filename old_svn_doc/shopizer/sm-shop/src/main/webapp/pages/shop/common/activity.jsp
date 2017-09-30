<%
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
/* out.clear();
out = pageContext.pushBody(); */
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<style>


.content{
	width:100%;
	padding : 0 250px;
}
p.put_center{
	text-align:center;
	font-size:16px;
	padding:20px 0;
	font-weight:bolder;
}
p.put_left{
	text-indent:2em;
	text-align:left;
	font-size:16px;
	padding:8px 0;
}
p.put_normal{
	text-align:left;
	font-size:14px;
	padding:10px 0 20px 0;
}
.code{
	width:130px;
	position:absolute;
	left : 1050px;
	top : 150px;
	border : 1px solid #d9dadc;
	text-align :center;
	padding : 15px;
}
#show a{color:#607fa6}
</style>
<script>
</script>
<br>
<br>
<div>
<div class="content">
	<h3>【质量 我们是认真的】百图生物质量展精彩回放</h2>
	<hr/>
	<p id="show" style="font-size:18px;padding-bottom:5px;padding-left:10px;">2016-05-18 
		<!-- <a href="javascript:void(0)" onclick="show();">白图生物bettbio</a> -->
	</p>
	<%-- <div id="weixin" style="width:500px;">
		<div style="width:20%;float:left;">
			<img src='<c:url value="/resources/img/activity/21.png"/>'/>
		</div>
		<div style="width:80%;float:left;">
			<p><span>百图生物bettbio</span></p>
			<p><span>微信号</span><span>bettbio</span></p>
			<p><span>功能介绍</span><span>生命科研最值得信赖的平台！</span></p>
		</div>
	<div class="code">
		<img src='<c:url value="/resources/img/activity/21.png"/>'/>
		<p>微信扫一扫</p>
		<p>关注该公众号</p>
		
	</div> --%>
	<div style="width:680px;padding:0 10px;">
		<img src='<c:url value="/resources/img/activity/1.png"/>'/>
		<p class="put_left">昨日，百小图和平台的七个优质商家小伙伴们一起走进了复旦大学，在这里举行百图生物质量展（第一季）。</p>
		<p class="put_left">来到复旦生科院，同学和老师们对于我们的到来非常开~森~，十分激动的围观我们的展会，百小图受宠若惊啊~</p>
		<p class="put_left" style="font-weight: bold">下面请看 小图报道~</p>
		<img src='<c:url value="/resources/img/activity/2.png"/>'/>
		<p class="put_center">小样铺满，领到手软~</p>
		<img src='<c:url value="/resources/img/activity/3.png"/>'/>
		<p class="put_normal">广州波柏贸易有限公司展台</p>
		<img src='<c:url value="/resources/img/activity/4.png"/>'/>
		<p class="put_normal">北京天恩泽基因科技有限公司展台</p>
		<img src='<c:url value="/resources/img/activity/5.png"/>'/>
		<p class="put_normal">海里纳米科技（苏州）有限公司展台</p>
		<img src='<c:url value="/resources/img/activity/6.png"/>'/>
		<p class="put_normal">杭州金源生物技术有限公司展台</p>
		<img src='<c:url value="/resources/img/activity/7.png"/>'/>
		<p class="put_normal">常州天地人和生物科技有限公司展台</p>
		<img src='<c:url value="/resources/img/activity/8.png"/>'/>
		<p class="put_normal">北京博晟思远生物科技有限公司展台</p>
		<img src='<c:url value="/resources/img/activity/9.png"/>'/>
		<p class="put_normal">上海潜亿贸易有限公司展台</p>
		<br>
		<img src='<c:url value="/resources/img/activity/10.png"/>'/>
		<br>
		<p class="put_center">小伙伴们和商家聊得high丫</p>
		<br>
		<img src='<c:url value="/resources/img/activity/11.png"/>'/>
		<img src='<c:url value="/resources/img/activity/12.png"/>'/>
		<br>
		<p class="put_center">许许多多的新伙伴也加入我们啦</p>
		<img src='<c:url value="/resources/img/activity/13.png"/>'/>
		<img src='<c:url value="/resources/img/activity/14.png"/>'/>
		<img src='<c:url value="/resources/img/activity/15.png"/>'/>
		<img src='<c:url value="/resources/img/activity/16.png"/>'/>
		<img src='<c:url value="/resources/img/activity/17.png"/>'/>
		
		<br>
		<p class="put_center">小图曾说过不会亏待我的小伙伴们</p>
		<img src='<c:url value="/resources/img/activity/18.png"/>'/>
		<p class="put_normal">抽奖台~</p>
		<img src='<c:url value="/resources/img/activity/19.png"/>'/>
		<p class="put_normal">漂亮的妹纸抽到特等奖迪士尼门票~</p>
		<img src='<c:url value="/resources/img/activity/20.png"/>'/>
		<p class="put_normal">准麻麻带着肚肚里的宝贝和我们一起玩耍</p>
		<img src='<c:url value="/resources/img/activity/10.png"/>'/>
		
		<p style="padding-top:20px;">光看图，不过瘾！   戳视频呐！！！</p>
		<p style="margin-bottom:0px;">找找有没有你的身影</p>
        <video id="ts" src='<c:url value="/resources/video/active.mp4"/>' 
              preload="auto" controls
             style="width:674px;height:508px;"></video>
	</div>
</div>
</div>
<br>

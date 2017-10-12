
<%
	response.setCharacterEncoding("UTF-8");
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", -1);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>
<%@page pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/cuxiao/cuxiao_KT.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/cuxiao/dacu.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/cuxiao/demo.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/cuxiao/cuxiao.css'/>">

<div class="cuxiao_body" style="margin-top: 50px;">
   
	  <div style="margin-bottom:5px;"><img  class="cx_img" src="/sm-shop/resources/img/cximages/img/cuxiao_wenan.png" /></div>

<div style="width: 1340px;margin: 0 auto;"> <!-- 1290=90+1200 -->
	<div style="width: 100%; display: inline-block;">

  <!-- 左边内容 -->
  <!-- 左右间隔90-70=20px -->
	<div class="cuxiao_nav">
		<div class="left_bar" style="position: fixed; min-width: 100px;width: 6%; display: inline-block;float: left; ">
			<ul style="position:relative; width: 90px;border: 1px #ccc solid;float: left; left: -95px; top: -975px;">
			  
			  
			 <!-- <a href="#1F" name="1F">锚点1</a> <div name="1F"> -->
			  
			  
				    <a href="#kangti"><li class="nav-icon1">抗体</li></a>
                    <a href="#xueqing"><li class="nav-icon2">血清</li></a>
                    <a href="#xibao"><li class="nav-icon3">细胞</li></a>
                    <a href="#shenghua"> <li class="nav-icon4">生化</li></a>
                    <a href="#shijihe"><li class="nav-icon5">试剂盒</li></a>
                    <a href="#haocai"><li class="nav-icon6">耗材</li></a>
                    <a href="#fuwu"><li class="nav-icon7">服务</li></a>
                    
		  </ul>
		</div>
	</div>
		<script>
//滚动后导航固定
$(function(){
	$('.cuxiao_nav').hide();
	$(window).scroll(function(){
          height = $(window).scrollTop();
		  if (height <600){
			 $('.cuxiao_nav').fadeOut();
   	  	  }else if(height <3000){
   	  	  	$('.cuxiao_nav').fadeIn();
   	  	  }else{
   	  	  	$('.cuxiao_nav').fadeOut();
   	  	  };
	});
});
	

</script>
 
<style>
.cx_more_brand_icon {
	margin-top: -5px; 
	float: right;

}
.Collection_txt {
	font-size: 14px;
}
</style>
 
 <script>
	/* @author:Romey
	 * 动态点赞
	 * 此效果包含css3，部分浏览器不兼容（如：IE10以下的版本）
	*/
	$(function(){
		$(".Collection").click(function(){
			var me = this;
			var Collection_img = $(me).find('.Collection_img');
			var Collection_txt = $(me).find('.Collection_txt');
			if(Collection_img.attr("done")=="yes"){
				Collection_img.html("<img src='/sm-shop/resources/img/cximages/like_normal.png' class='animation' />");
				Collection_txt.text("收藏品牌");
				$(me).parent().css("background-color","#333");
				Collection_txt.css("color", "white");
				Collection_img.attr("done", "no");
			}else{
				$(Collection_img).html("<img src='/sm-shop/resources/img/cximages/like_finish.png' class='animation' />");
				Collection_txt.text("收藏成功");
				$(me).parent().css("background-color","#dfbbbb");
				Collection_txt.css("color", "#c62e1d");
				Collection_img.attr("done", "yes");
			}
			removeAnimation(Collection_img.find('img'));
		});
		
		$(".Collection").blur(function(){
			var me = this;
			var Collection_img = $(me).find('.Collection_img');
			Collection_img.find('img').removeClass('animation');
		});
	})
	function removeAnimation(obj){
		setTimeout(function(){
			$(obj).removeClass('animation');
		}, 1600 )
	}
</script>

    <!-- 右边内容 -->
		<div style="min-width: 1210px; width: 92%; display: inline-block; ">
    <div class="b-box">
    
     <a name="kangti" ></a>
		<div class="box_red"><span>抗体</span> 精选</div> 
        <span style=" float: left; margin-top: 35px;margin-left:18px;color: #ececec;font-size: 14px;">ANTIBODY</span>
		<a href="#brand_table">
			<span style=" float: right; margin-top: 35px;color: #999999;font-size: 18px;">
				更多
				<img class="cx_more_brand_icon"  src="/sm-shop/resources/img/cximages/more_img.png" />
			</span>
		</a>
     <div class="border_line"></div>   
    
     
<!--   <科赛博>-->
     
   	
	<div class="big_box" style="float: left;margin-top: 20px;">
	
  <div class="left_box" style="float: left">
   <a href="<c:url value='/shop/promotions/kesaibo.html'/>">
     
     <span class="x_box1"><img class="erweima_img" style="margin-top: 30px;margin-left: 30px;" src="/sm-shop/resources/img/cximages/img/ksb_erweima.png" width="120" height="120" alt=""/><!--<div class="x_box1_hover" style="width: 120px; height: 120px; margin-top: 30px;line-height: 120px;margin-left: 40px;">识别图中二维码</div>--> </span></a><img style="margin-left: 60px; margin-top: 50px;" src="/sm-shop/resources/promotionImages/科赛博_02/图层-0-拷贝.png" width="54" height="81" alt=""/>
     
    
  </div>
  <div class="right_box"><span style="font-weight: lighter; text-align: center; font-size: 16px; display: block; color: #4c4c4c;">科赛博 抗体热销</span>
  	<div style="font-size: 20px; font-weight: normal;line-height: 40px; text-align: center; display: block; color: #333;">全线产品
      <p class="cuxiao_p" >8</p>
      折
      
    </div>
	<%--
    <div style="font-size: 20px; font-weight: normal;line-height: 40px; text-align: center; display: block; color: #333;">扫码立省
      <p class="cuxiao_p" >1200</p>
      元
      
    </div>
	--%>
    <span style="font-size: 14px; color: #666;">
		<img class="img_time" src="/sm-shop/resources/img/cximages/img/time.png" width="23" height="25" alt=""/>
		<span class="span_time">敬请期待</span>
	</span>
    <a href="<c:url value='/shop/promotions/kesaibo.html'/>" >
		<div class="click_nav">点击详情</div></a>
  </div>
</div>	
		
      
<!--<正能>-->
      	
		
   <div class="big_box" style="margin-top: 20px;">
  
  <div class="left_box" style="float: left">
    
      <span class="x_box1"><img class="zn_logo" src="/sm-shop/resources/img/cximages/img/zn_logo.png" width="110" height="80" alt=""/>
        <div class="x_box1_hover">
			<div class="Collection">
				<span class="Collection_img"><img src="/sm-shop/resources/img/cximages/img/like.png"/></span>
				<span class="Collection_txt">收藏品牌</span>
			</div>
		</div> 
      </span>
      
      <a href="<c:url value='/shop/promotions/正能_02.html'/>">
      <span class="x_box2"><!--<img class="tu_img1" src="img/zn_tu3.png" width="110" height="80" alt=""/>--> <!--<div class="x_box2_hover">--><!--<img  class="img_more" src="img/+.png" width="25" height="25" alt=""/>--><!--</div>-->
      
      
      
      
      <img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span>
      
      <span class="x_box3" style="margin-top: 5px"><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span>
      
      <span class="x_box4" style="margin-top: 5px"><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span></a>
    
  </div>
  <div class="right_box"><span style="font-weight: lighter; text-align: center; font-size: 16px; display: block; color: #4c4c4c;">正能一抗 产品大促</span>
    <div style="font-size: 20px; font-weight: normal;line-height: 40px; text-align: center; display: block; color: #333;">奖励
      <p style=" font-weight:bold; font-size:22px;">红包</p>
      及
      <p  style=" font-weight:bold; font-size:22px;">礼品</p>
    </div>
    <span style="font-size: 14px; color: #666;"><img class="img_time" src="/sm-shop/resources/img/cximages/img/time.png" width="23" height="25" alt=""/>
	<span class="span_time">敬请期待</span></span>
    <a href="<c:url value='/shop/promotions/正能_02.html'/>">
		<div class="click_nav">点击详情</div></a>
  </div>
</div>
    
      
      	<div class="box_red"><a name="xueqing"></a><span>血清</span> 精选</div> 
        <span style=" float: left; margin-top: 35px;margin-left:18px;color: #ececec;font-size: 14px;">BLOOD SERUM</span>
		<a href="#brand_table">
			<span style=" float: right; margin-top: 35px;color: #999999;font-size: 18px;">
				更多
				<img class="cx_more_brand_icon"  src="/sm-shop/resources/img/cximages/more_img.png" />
			</span>
		</a>
     <div class="border_line"></div>    
     
     
<!-- <富衡>-->
     
     
   <div class="big_box" style="margin-top: 20px; float: left;">
    
   
  <div class="left_box" style="float: left">
    
		<span class="x_box1">
			<img class="zn_logo" src="/sm-shop/resources/img/cximages/img/fuheng_logo.png" alt=""/>
			<div class="x_box1_hover">
				<div class="Collection">
					<span class="Collection_img"><img src="/sm-shop/resources/img/cximages/img/like.png"/></span>
					<span class="Collection_txt">收藏品牌</span>
				</div>
			</div> 
		</span>
     <!--   <div class="Collection">
	<span id="Collection"><img src="Images/zan.png" width="18" height="14" alt="" id="Collection_img"/> </span>
    <span id="Collection_txt">收藏品牌</span></div>
-->
      </span>
      
      <a href="<c:url value='/shop/promotions/fuheng.html'/>">
      <span class="fh_box2"><!--<img class="tu_img1" src="img/zn_tu3.png" width="110" height="80" alt=""/>--> <!--<div class="x_box2_hover">--><!--<img  class="img_more" src="img/+.png" width="25" height="25" alt=""/>--><!--</div>--><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span>
      
      <span class="fh_box3" style="margin-top: 5px"><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span>
      
      <span class="fh_box4" style="margin-top: 5px"><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span></a>
    
  </div>
  <div class="right_box"><span style="font-weight: lighter; text-align: center; font-size: 16px; display: block; color: #4c4c4c;">HyClone 品牌专场</span>
    <div style="font-size: 20px; font-weight: normal;line-height: 40px; text-align: center; display: block; color: #333;">低至
      <p style=" font-weight:bold; font-size:28px;">60</p>
    元
      
    </div>
    <span style="font-size: 14px; color: #666;"><img class="img_time" src="/sm-shop/resources/img/cximages/img/time.png" width="23" height="25" alt=""/>
	<span class="span_time">4/5-9/30</span></span>
    <a href="<c:url value='/shop/promotions/fuheng.html'/>">
		<div class="click_nav">点击详情</div></a>
  </div>
</div>
        
        
        
 <!--血清2-->
  
  
  <div class="big_box" style="margin-top: 20px; float: right;background-color: #FFFFFF;">
  
</div>
  
         
       	<div class="box_red"><a name="xibao"></a><span>细胞</span> 精选</div> 
        <span style=" float: left; margin-top: 35px;margin-left:18px;color: #ececec;font-size: 14px;">CELLS</span>
		<a href="#brand_table">
			<span style=" float: right; margin-top: 35px;color: #999999;font-size: 18px;">
				更多
				<img  class="cx_more_brand_icon"  src="/sm-shop/resources/img/cximages/more_img.png" />
			</span>
		</a>
		
     <div class="border_line"></div>    
         
         
         
 <!--《塞百慷》-->
      
      
  <div class="big_box" style="margin-top: 20px;">
  
  <div class="left_box" style="float: left">
    
      <span class="x_box1"><img class="zn_logo" src="/sm-shop/resources/img/cximages/img/sbk_logo.png" width="110" height="80" alt=""/>
        <div class="x_box1_hover">
				<div class="Collection">
					<span class="Collection_img"><img src="/sm-shop/resources/img/cximages/img/like.png"/></span>
					<span class="Collection_txt">收藏品牌</span>
				</div>
			</div>
      </span>
      
      <a href="<c:url value='/shop/promotions/saibaikang_02.html'/>">
      <span class="sbk_box2"><!--<img class="tu_img1" src="img/zn_tu3.png" width="110" height="80" alt=""/>--> <!--<div class="x_box2_hover">--><!--<img  class="img_more" src="img/+.png" width="25" height="25" alt=""/>--><!--</div>--><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span>
      
      <span class="sbk_box3" style="margin-top: 6px"><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span>
      
      <span class="sbk_box4" style="margin-top: 5px"><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span></a>
    
  </div>
  <div class="right_box"><span style="font-weight: lighter; text-align: center; font-size: 16px; display: block; color: #4c4c4c;">iCell 低价大促</span>
    <div style="font-size: 20px; font-weight: normal;line-height: 40px; text-align: center; display: block; color: #333;">折扣更新中 <p style=" font-weight:bold; font-size:28px; margin-top: 5px;fargin-top:5px;"></p> 
      
    </div>
    <span style="font-size: 14px; color: #666;"><img class="img_time" src="/sm-shop/resources/img/cximages/img/time.png" width="23" height="25" alt=""/>
	<span class="span_time">敬请期待</span></span>
  <a href="<c:url value='/shop/promotions/saibaikang_02.html'/>">
		<div class="click_nav">点击详情</div></a>
  </div>
</div>
      

          
<!--《中乔新舟》-->
     
      
      <div class="big_box" style="margin-top: 20px; float: left;">
  <div class="left_box" style="float: left">
    
      <span class="x_box1"><img class="zn_logo" src="/sm-shop/resources/img/cximages/img/zhongqiao_logo.png" width="110" height="80" alt=""/>
        <div class="x_box1_hover">
				<div class="Collection">
					<span class="Collection_img"><img src="/sm-shop/resources/img/cximages/img/like.png"/></span>
					<span class="Collection_txt">收藏品牌</span>
				</div>
			</div>
      </span>
      
     <a href="<c:url value='/shop/promotions/中乔新舟.html'/>">
      <span class="zq_box2"><!--<img class="tu_img1" src="img/zn_tu3.png" width="110" height="80" alt=""/>--> <!--<div class="x_box2_hover">--><!--<img  class="img_more" src="img/+.png" width="25" height="25" alt=""/>--><!--</div>--><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span>
      
      <span class="zq_box3" style="margin-top: 5px"><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span>
      
      <span class="zq_box4" style="margin-top: 5px"><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span></a>
    
  </div>
  <div class="right_box"><span style="font-weight: lighter; text-align: center; font-size: 16px; display: block; color: #4c4c4c;">ScienCell 品牌大促</span>
    <div style="font-size: 20px; font-weight: normal;line-height: 40px; text-align: center; display: block; color: #333;">细胞产品一律 <p style=" font-weight:bold; font-size:28px; margin-top: 5px;fargin-top:5px;">8</p> 折
      
    </div>
    <span style="font-size: 14px; color: #666;"><img class="img_time" src="/sm-shop/resources/img/cximages/img/time.png" width="23" height="25" alt=""/>
	<span class="span_time">敬请期待</span></span>
  <a href="<c:url value='/shop/promotions/中乔新舟.html'/>">
		<div class="click_nav">点击详情</div></a>
  </div>
</div>
      
<!--  <yishen>  -->
  
  
        <div class="box_red"><a name="shenghua"></a><span>生化</span> 精选</div> 
        <span style=" float: left; margin-top: 35px;margin-left:18px;color: #ececec;font-size: 14px;">BIOCHEMISTRY</span>
		<a href="#brand_table">
			<span style=" float: right; margin-top: 35px;color: #999999;font-size: 18px;">
				更多
				<img class="cx_more_brand_icon"  src="/sm-shop/resources/img/cximages/more_img.png" />
			</span>
		</a>
        <div class="border_line"></div>   
     
     
     
     
      <div class="big_box" style="margin-top: 20px; float: left;">
      
  <div class="left_box" style="float: left">
    
      <span class="x_box1"><img class="zn_logo" src="/sm-shop/resources/img/cximages/img/ys_log0.png" width="110" height="80" alt=""/>
        <div class="x_box1_hover">
				<div class="Collection">
					<span class="Collection_img"><img src="/sm-shop/resources/img/cximages/img/like.png"/></span>
					<span class="Collection_txt">收藏品牌</span>
				</div>
			</div>
      </span>
      
     <a href="<c:url value='/shop/promotions/yishen_02.html'/>">
      <span class="ys_box2"><!--<img class="tu_img1" src="img/zn_tu3.png" width="110" height="80" alt=""/>--> <!--<div class="x_box2_hover">--><!--<img  class="img_more" src="img/+.png" width="25" height="25" alt=""/>--><!--</div>--><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span>
      
      <span class="ys_box3" style="margin-top: 6px"><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span>
      
      <span class="ys_box4" style="margin-top: 5px"><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span></a>
    
  </div>
  <div class="right_box"><span style="font-weight: lighter; text-align: center; font-size: 16px; display: block; color: #4c4c4c;">整装待发 翊起GO</span>
    <div style="font-size: 20px; font-weight: normal;line-height: 40px; text-align: center; display: block; color: #333;">多买多送 领取 <p style=" font-weight:bold; font-size:22px; margin-top: 5px;fargin-top:5px;">苹果笔记本</p> 
      
    </div>
    <span style="font-size: 14px; color: #666;"><img class="img_time" src="/sm-shop/resources/img/cximages/img/time.png" width="23" height="25" alt=""/>
	<span class="span_time">敬请期待</span></span>
 <a href="<c:url value='/shop/promotions/yishen_02.html'/>">
		<div class="click_nav">点击详情</div></a>
  </div>
</div>
      
      
      
<!--《索莱宝》-->
        
     
      <div class="big_box" style="margin-top: 20px; float: right;">
  <div class="left_box" style="float: left">
    
      <span class="x_box1"><img class="zn_logo" src="/sm-shop/resources/img/cximages/img/slb_logo.png" width="110" height="80" alt=""/>
        <div class="x_box1_hover">
				<div class="Collection">
					<span class="Collection_img"><img src="/sm-shop/resources/img/cximages/img/like.png"/></span>
					<span class="Collection_txt">收藏品牌</span>
				</div>
			</div>
      </span>
      
    <a href="<c:url value='/shop/promotions/索莱宝.html'/>">
      <span class="slb_box2"><!--<img class="tu_img1" src="img/zn_tu3.png" width="110" height="80" alt=""/>--> <!--<div class="x_box2_hover">--><!--<img  class="img_more" src="img/+.png" width="25" height="25" alt=""/>--><!--</div>--><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span>
      
      <span class="slb_box3" style="margin-top: 6px"><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span>
      
      <span class="slb_box4" style="margin-top: 5px"><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span></a>
    
  </div>
  <div class="right_box"><span style="font-weight: lighter; text-align: center; font-size: 16px; display: block; color: #4c4c4c;">索莱宝 全线大促销</span>
    <div style="font-size: 20px; font-weight: normal;line-height: 40px; text-align: center; display: block; color: #333;">最低优惠买 <p style=" font-weight:bold; font-size:22px; margin-top: 5px;fargin-top:5px;">一</p> 送<p style=" font-weight:bold; font-size:22px; margin-top: 5px;fargin-top:5px;"> 一</p>
      
    </div>
    <span style="font-size: 14px; color: #666;"><img class="img_time" src="/sm-shop/resources/img/cximages/img/time.png" width="23" height="25" alt=""/>
	<span class="span_time">敬请期待</span></span>
 <a href="<c:url value='/shop/promotions/索莱宝.html'/>">
		<div class="click_nav">点击详情</div></a>
  </div>
</div>
      
      
        
    
     <div class="box_red"><a name="shijihe"></a><span>试剂盒</span> 精选</div> 
        <span style=" float: left; margin-top: 35px;margin-left:18px;color: #ececec;font-size: 14px;">REAGENT KIT</span>
		<a href="#brand_table">
			<span style=" float: right; margin-top: 35px;color: #999999;font-size: 18px;">
				更多
				<img class="cx_more_brand_icon"  src="/sm-shop/resources/img/cximages/more_img.png" />
			</span>
		</a>
        <div class="border_line"></div>
        
        
<!--   <neb>-->
     
      
<div class="big_box" style="margin-top: 20px; float: left;">
 
  <div class="left_box" style="float: left">
    
      <span class="x_box1"><img class="zn_logo" src="/sm-shop/resources/img/cximages/img/neb_logo.png" width="110" height="80" alt=""/>
        <div class="x_box1_hover">
				<div class="Collection">
					<span class="Collection_img"><img src="/sm-shop/resources/img/cximages/img/like.png"/></span>
					<span class="Collection_txt">收藏品牌</span>
				</div>
			</div>
      </span>
      
   <a href="<c:url value='/shop/promotions/NEB_02.html'/>">
      <span class="neb_box2"><!--<img class="tu_img1" src="img/zn_tu3.png" width="110" height="80" alt=""/>--> <!--<div class="x_box2_hover">--><!--<img  class="img_more" src="img/+.png" width="25" height="25" alt=""/>--><!--</div>--><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span>
      
      <span class="neb_box3" style="margin-top: 6px"><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span>
      
      <span class="neb_box4" style="margin-top: 5px"><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span></a>
    
  </div>
  <div class="right_box"><span style="font-weight: lighter; text-align: center; font-size: 16px; display: block; color: #4c4c4c;">NEB 春季促销</span>
    <div style="font-size: 20px; font-weight: normal;line-height: 40px; text-align: center; display: block; color: #333;">最低 <p style=" font-weight:bold; font-size:28px; margin-top: 5px;fargin-top:5px;">6</p> 折起 
      
    </div>
    <span style="font-size: 14px; color: #666;"><img class="img_time" src="/sm-shop/resources/img/cximages/img/time.png" width="23" height="25" alt=""/>
	<span class="span_time">敬请期待</span></span>
 <a href="<c:url value='/shop/promotions/NEB_02.html'/>">
		<div class="click_nav">点击详情</div></a>
  </div>
</div>

      
      
<!--《新景》-->
     
    	
      <div class="big_box" style="margin-top: 20px; float: right;">
  <div class="left_box" style="float: left">
    
      <span class="x_box1"><img class="zn_logo" src="/sm-shop/resources/img/cximages/img/xinjin_logo.png" width="110" height="80" alt=""/>
        <div class="x_box1_hover">
				<div class="Collection">
					<span class="Collection_img"><img src="/sm-shop/resources/img/cximages/img/like.png"/></span>
					<span class="Collection_txt">收藏品牌</span>
				</div>
			</div>
      </span>
      
 <a href="<c:url value='/shop/promotions/新景1.html'/>">
      <span class="xinjing_box2"><!--<img class="tu_img1" src="img/zn_tu3.png" width="110" height="80" alt=""/>--> <!--<div class="x_box2_hover">--><!--<img  class="img_more" src="img/+.png" width="25" height="25" alt=""/>--><!--</div>--><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span>
      
      <span class="xinjing_box3" style="margin-top: 6px"><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span>
      
      <span class="xinjing_box4" style="margin-top: 5px"><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span></a>
    
  </div>
  <div class="right_box"><span style="font-weight: lighter; text-align: center; font-size: 16px; display: block; color: #4c4c4c;">新景缤纷 大促销</span>
    <div style="font-size: 20px; font-weight: normal;line-height: 40px; text-align: center; display: block; color: #333;">买 <p style=" font-weight:bold; font-size:22px; margin-top: 5px;fargin-top:5px;">二</p> 送 <p style=" font-weight:bold; font-size:22px; margin-top: 5px;fargin-top:5px;">一</p> 
      
    </div>
    <span style="font-size: 14px; color: #666;"><img class="img_time" src="/sm-shop/resources/img/cximages/img/time.png" width="23" height="25" alt=""/>
	<span class="span_time">敬请期待</span></span>
 <a href="<c:url value='/shop/promotions/新景1.html'/>">
		<div class="click_nav">点击详情</div></a>
  </div>
</div>
      
        
        
        
                 
         <div class="box_red"><a name="haocai"></a><span>耗材</span> 精选</div> 
        <span style=" float: left; margin-top: 35px;margin-left:18px;color: #ececec;font-size: 14px;">SUPPLIES</span>
		<a href="#brand_table">
			<span style=" float: right; margin-top: 35px;color: #999999;font-size: 18px;">
				更多
				<img class="cx_more_brand_icon"  src="/sm-shop/resources/img/cximages/more_img.png" />
			</span>
		</a>
        <div class="border_line"></div>
        
        
        
        
<!-- 《威高斯》-->
      
      <div class="big_box" style="margin-top: 20px; float: left;">
      
  <div class="left_box" style="float: left">
    
      <span class="x_box1"><img class="zn_logo" src="/sm-shop/resources/img/cximages/img/weigaosi_logo1.png" width="110" height="80" alt=""/>
        <div class="x_box1_hover">
				<div class="Collection">
					<span class="Collection_img"><img src="/sm-shop/resources/img/cximages/img/like.png"/></span>
					<span class="Collection_txt">收藏品牌</span>
				</div>
			</div>
      </span>
      
<a href="<c:url value='/shop/promotions/weigaosi.html'/>">
      <span class="weigaosi_box2"><!--<img class="tu_img1" src="img/zn_tu3.png" width="110" height="80" alt=""/>--> <!--<div class="x_box2_hover">--><!--<img  class="img_more" src="img/+.png" width="25" height="25" alt=""/>--><!--</div>--><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span>
      
      <span class="weigaosi_box3" style="margin-top: 6px"><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span>
      
      <span class="weigaosi_box4" style="margin-top: 5px"><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span></a>
    
  </div>
  <div class="right_box"><span style="font-weight: lighter; text-align: center; font-size: 16px; display: block; color: #4c4c4c;">ABI 耗材促销</span>
    <div style="font-size: 20px; font-weight: normal;line-height: 40px; text-align: center; display: block; color: #333;">最低 <p style=" font-weight:bold; font-size:28px; margin-top: 5px;fargin-top:5px;">5</p> 折起  
      
    </div>
    <span style="font-size: 14px; color: #666;"><img class="img_time" src="/sm-shop/resources/img/cximages/img/time.png" width="23" height="25" alt=""/>
	<span class="span_time">敬请期待</span></span>
<a href="<c:url value='/shop/promotions/weigaosi.html'/>">
		<div class="click_nav">点击详情</div></a>
  </div>
</div>
      
<!--《fisherbrand》-->    
      <div class="big_box" style="margin-top: 20px; float: right;">
      
  <div class="left_box" style="float: left">
	<span class="x_box1"><img class="zn_logo" src="/sm-shop/resources/img/cximages/img/fisher_logo.png" width="110" height="80" alt=""/>
		<div class="x_box1_hover">
			<div class="Collection">
				<span class="Collection_img"><img src="/sm-shop/resources/img/cximages/img/like.png"/></span>
				<span class="Collection_txt">收藏品牌</span>
			</div>
		</div>
	</span>
      

	<span class="x_box1" style="float: right"><img class="zn_logo1" src="/sm-shop/resources/img/cximages/img/Nalgene-logo.png"/>
		<div class="x_box1_hover">
			<div class="Collection">
				<span class="Collection_img"><img src="/sm-shop/resources/img/cximages/img/like.png"/></span>
				<span class="Collection_txt">收藏品牌</span>
			</div>
		</div>
	</span>
	
	<a href="<c:url value='/shop/promotions/fisher.html'/>">
		<span class="fisher_box3" style="margin-top: 6px"><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span>
	</a>
	
	<span class="x_box1" style="float: left; margin-top: 6px;"><img class="zn_logo2" src="/sm-shop/resources/img/cximages/img/maidikang_logo.png"/>	
		<div class="x_box1_hover">
			<div class="Collection">
				<span class="Collection_img"><img src="/sm-shop/resources/img/cximages/img/like.png"/></span>
				<span class="Collection_txt">收藏品牌</span>
			</div>
		</div>
    </span>
      
      
        
      
    
  </div>
  <div class="right_box"><span style="font-weight: lighter; text-align: center; font-size: 16px; display: block; color: #4c4c4c;">三大品牌 耗材大促</span>
    <div style="font-size: 20px; font-weight: normal;line-height: 40px; text-align: center; display: block; color: #333;">尽享 <p style=" font-weight:bold; font-size:28px; margin-top: 5px;fargin-top:5px;">6.5</p> 折 
      
    </div>
    <span style="font-size: 14px; color: #666;"><img class="img_time" src="/sm-shop/resources/img/cximages/img/time.png" width="23" height="25" alt=""/>
	<span class="span_time">敬请期待</span></span>
<a href="<c:url value='/shop/promotions/fisher.html'/>">
		<div class="click_nav">点击详情</div></a>
  </div>
</div>


<div class="box_red"><a name="fuwu"></a><span>服务</span> 精选</div> 
<span style=" float: left; margin-top: 35px;margin-left:18px;color: #ececec;font-size: 14px;">SERVICE</span>
<a href="#brand_table">
	<span style=" float: right; margin-top: 35px;color: #999999;font-size: 18px;">
		更多
		<img class="cx_more_brand_icon"  src="/sm-shop/resources/img/cximages/more_img.png" />
	</span>
</a>
<div class="border_line"></div>
		
	          
		
<!--《强耀生物》-->
      
     <div class="big_box" style="margin-top: 20px; float: left">
  <div class="left_box" style="float: left">
    
      <span class="x_box1"><img class="zn_logo" src="/sm-shop/resources/img/cximages/img/qiaoyao_img.png" width="110" height="80" alt=""/>
        <div class="x_box1_hover">
				<div class="Collection">
					<span class="Collection_img"><img src="/sm-shop/resources/img/cximages/img/like.png"/></span>
					<span class="Collection_txt">收藏品牌</span>
				</div>
			</div>
      </span>
      
<a href="<c:url value='/shop/promotions/qiangyao.html'/>">
      <span class="qiangyao_box2"><!--<img class="tu_img1" src="img/zn_tu3.png" width="110" height="80" alt=""/>--> <!--<div class="x_box2_hover">--><!--<img  class="img_more" src="img/+.png" width="25" height="25" alt=""/>--><!--</div>--><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span>
      
      <span class="qiangyao_box3" style="margin-top: 6px"><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span>
      
      <span class="qiangyao_box4" style="margin-top: 5px"><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span></a>
    
  </div>
  <div class="right_box"><span style="font-weight: lighter; text-align: center; font-size: 16px; display: block; color: #4c4c4c;">强耀生物 感恩有礼</span>
    <div style="font-size: 20px; font-weight: normal;line-height: 40px; text-align: center; display: block; color: #333;">满购即 <p style=" font-weight:bold; font-size:22px; margin-top: 5px;fargin-top:5px;">送</p>  
      
    </div>
    <span style="font-size: 14px; color: #666;"><img class="img_time" src="/sm-shop/resources/img/cximages/img/time.png" width="23" height="25" alt=""/>
	<span class="span_time">敬请期待</span></span>
<a href="<c:url value='/shop/promotions/qiangyao.html'/>">
		<div class="click_nav">点击详情</div></a>
  </div>
</div>
    
	<!--《generay》-->    
      <div class="big_box" style="margin-top: 20px; float: right;">
      
  <div class="left_box" style="float: left">
    
      <span class="x_box1"><img class="zn_logo" src="/sm-shop/resources/img/cximages/img/generay_logo.png" width="110" height="80" alt=""/>
		<div class="x_box1_hover">
			<div class="Collection">
				<span class="Collection_img"><img src="/sm-shop/resources/img/cximages/img/like.png"/></span>
				<span class="Collection_txt">收藏品牌</span>
			</div>
		</div>
      </span>
      
<a href="<c:url value='/shop/promotions/generay.html'/>">
      <span class="generay_box2"><!--<img class="tu_img1" src="img/zn_tu3.png" width="110" height="80" alt=""/>--> <!--<div class="x_box2_hover">--><!--<img  class="img_more" src="img/+.png" width="25" height="25" alt=""/>--><!--</div>-->
	  <img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span>
      
      <span class="generay_box3" style="margin-top: 6px"><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span>
      
      <span class="generay_box4" style="margin-top: 5px"><img class="img_more" src="/sm-shop/resources/img/cximages/img/+.png" width="36" height="34" alt=""/></span></a>
    
  </div>
  <div class="right_box"><span style="font-weight: lighter; text-align: center; font-size: 16px; display: block; color: #4c4c4c;">GENERAY 服务促销</span>
    <div style="font-size: 20px; font-weight: normal;line-height: 40px; text-align: center; display: block; color: #333;">MGB分型 <p style=" font-weight:bold; font-size:22px; margin-top: 5px;fargin-top:5px;">送探针</p>
      
    </div>
    <span style="font-size: 14px; color: #666;">
		<img class="img_time" src="/sm-shop/resources/img/cximages/img/time.png" width="23" height="25" alt=""/>
		<span class="span_time">敬请期待</span>
	</span>
<a href="<c:url value='/shop/promotions/generay.html'/>">
		<div class="click_nav">点击详情</div></a>
  </div>
</div>


     
      </div>

		</div>
	</div>
  </div>
  
  
  
  
</div>
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
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %> 
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%-- <style>
	#left_foot li a{color:#fafafa !important;}
	#right_foot li a{color:#fafafa !important;}
</style> --%>
       <!-- 登录尾部-->
	  <nav id="footer" class="" style="margin-bottom:-7px;padding-top:0px;background-color: black;">
	  		<div style="width:1200px;margin-left: auto;margin-right: auto;">
	  		<div class="row">
	  			<div class="pull-left " style="text-align:center;">
	  				<ul id="left_foot" class="nav navbar-nav">
			  			<li ><a class="foot-li" href='<c:url value="/shop/customer/protocol.html"/>'><s:message code="label.content.userprotocol" text="protocol" /></a></li>
			  			<li ><a class="foot-li" href='http://v.youku.com/v_show/id_XMTUyNTM5NzE2NA==.html?from=y1.7-1.2 ' target="_blank"><s:message code="label.bettbio.buyer" text="buyer" /></a></li>
			  			<li ><a class="foot-li" href='http://v.youku.com/v_show/id_XMTU1MjM2MjkxNg==.html?from=y1.7-1.2' target="_blank"><s:message code="label.bettbio.justbuy" text="just buy" /></a></li>
			  			<li ><a class="foot-li" href='<c:url value="/shop/quality.html"/>'><s:message code="label.bettbio.buy.title2" text="Contact" /></a></li>
			  			<li ><a class="foot-li" href='<c:url value="/shop/detectService.html"/>'><s:message code="label.bettbio.buy.title3" text="Contact" /></a></li>
			  			<li ><a class="foot-li" href='<c:url value="/shop/marketpoints/list.html"/>'><s:message code="label.bettbio.buy.shop1" text="Contact" /></a></li>
			  			<li ><a class="foot-li" href='<c:url value="/shop/pointChange.html"/>'><s:message code="label.bettbio.buy.shop2" text="Contact" /></a></li>
			  			<li ><a class="foot-li" href='<c:url value="/shop/aboutUS.html"/>'><s:message code="label.bettbio.buy.about" text="Contact" /></a></li>
			  			<li ><a class="foot-li" href='<c:url value="/shop/store/contactus.html"/>'><s:message code="label.customer.contactus" text="Contact" /></a></li>
			  			<li ><a class="foot-li" href="http://www.zqxzbio.com/" target="_blank">中乔新舟</a></li>
			  			<li ><a class="foot-li" href="http://www.kuaidi.com/" target="_blank"><s:message code="label.product.delivery.link" text="Contact" /></a></li>
	  				</ul>
	  			</div>
	  			<div class="pull-right " style="text-align:center;">
	  				<ul id="right_foot" class="nav navbar-nav">
	  					<li ><a class="foot-li" href="<c:url value="/"/>"><s:message code="label.bettbio.baknum" text="Bak Num" /></a></li>	
	  				</ul>
	  			</div>
	  		</div>
	  	</div>
	  </nav>
   <script type="text/javascript">
   	$(document).ready(function(){
   		var winHei = $(window).height(); //窗口高度
   		var footHei = $("#footer").height(); //div高度
   		var footToTop =  document.getElementById("footer").offsetTop; //div距离屏幕顶部的距离
		if (footToTop + footHei < winHei) {
			//alert("if");
			document.getElementById("footer").className='fix'; //设置样式
			//$("#footer").addClass('fix');
		} else {
			//alert("else");
			document.getElementById("footer").className=''; 
		}
		
		$(window).scroll(function(){
			//alert("scroll");
       		document.getElementById("footer").className='';
       	});
       	
       	$("#left_foot li").mouseover(function(){
       		$(this).css({"color":"#ff313b"});
	    }).mouseout(function(){
	       	$(this).css({"color":"#fff"});
	    });

   	});
   </script>
   <style type="text/css">
   		.fix {
   			position : fixed;
   			bottom : 0;
   			width : 100%;
   		}
   		
   </style>
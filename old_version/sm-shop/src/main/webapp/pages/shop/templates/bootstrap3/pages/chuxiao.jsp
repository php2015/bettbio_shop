
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
<style>
@charset "utf-8";
/* CSS Document */

.b-box * {
	font-family:"微软雅黑";
	margin:0; 
	padding:0;
}

ul, ol {
	list-style: none
}

.b-box{ 
	width:1200px; 
	margin:0 auto ; 
	padding-top: 100px;
	overflow: hidden;
}

.b-box ul li{ float:left}

.b-box ul li:nth-child(2n) {
	margin-left:20px
}

a {
	text-decoration: none;
	color: #000;
}
.blur {
	-webkit-filter:blur(10px);
	-moz-filter:blur(10px);
	-ms-filter:blur(10px);
	filter: blur(10px);
}
.pic {
	width:590px;
	height: 350px;
}

.cx_img{ margin:0 auto ; display:block; width:375px; height:55px;}

.wkk {
	width: 590px;
	height: 350px;
	/*	background-image: url(t1.jpg);*/
			position: relative;
}
.nkk {
	width: 386px;
	height: 191px;
	background-color: #fff;
	border-radius:5px;
	position: absolute;
	top: 80px;
	left: 102px;
	display: none;
}
.wkk:hover .pic {
	-webkit-filter:blur(1px);
	-moz-filter:blur(1px);
	-ms-filter:blur(1px);
	filter: blur(1px);
}
.wkk:hover .nkk {
	display: block;
	z-index:1;
}
.nkk .n1 {
	width: 80px;
	height: 60px;
	position: absolute;
	top: 28px;
	left: 50px;
}
.nkk .n2 {
	width: 80px;
	height: 60px;
	position: absolute;
	top: 28px;
	left: 150px;
}
.nkk .n3 {
	width: 80px;
	height: 60px;
	position: absolute;
	top: 28px;
	left: 250px;
}
.nkk .n1:hover {
	opacity: 0.5;
}
.nkk .n2:hover {
	opacity: 0.5;
}
.nkk .n3:hover {
	opacity: 0.5;
}
.nkk .z1{ width:386px; height:80px;
	margin-top:100px; padding-bottom:5px ;/*background-color:#000*/
}

.nkk .z2 {
	margin-left: 45px;
	color:#333;
	display:block;
	font-size:20px;

	padding-bottom:10px;
}

.line{	border-bottom:dashed #333 1px;  }


.nkk .z3{font-size:20px; margin-left:45px}

.nkk .z4{  margin-left:50px;}

.nkk .z4:hover{ color:#F00}



.nkk .z5, .z4, .z3 {
	font-size:16px;
	color:#333;
	 display:inline-block;
	padding-top:15px
}

.nkk .z5 {
	border:#333 1px solid;
	padding:3px;
	border-radius:5px;
	margin-left:55px
}


.nkk .z5:hover{border:#F00 1px solid;} 
.nkk .z5:hover a{ color:#F00;}

.nkk a .img_1{  background-image:url(/sm-shop/resources/img/cximages/111.png)}

/*.nkk .z4:hover img_1{ background-image:url(/sm-shop/resources/img/cximages/img_1.png)}*/

.z2_1{margin-left: 65px;
	color:#333;
	display:block;
	font-size:20px;

	padding-bottom:10px;}
</style>

<div class="b-box"> 
	<div><img  class="cx_img" src="/sm-shop/resources/img/cximages/未标题-1.png" /></div>
	<ul>
		<li> <div class="wkk">
				<div class="nkk">
					<a href="<c:url value='/shop/promotions/NEB_02.html'/>">
						<img class="n1" src="/sm-shop/resources/img/cximages/img_3.png"/>
						<img class="n2" src="/sm-shop/resources/img/cximages/img_2.png"/> 
						<img class="n3" src="/sm-shop/resources/img/cximages/img_1.png"/>
					</a>
					<div class="z1"> 
						<span class="z2">NEB八大系列全线促销最低6折起</span><!--</br>-->
						<div class="line"></div>
						<span class="z3"><b>NEB</b></span>
						<a href="#">
							<span class="z4">收藏品牌</span><!--<img  class="img_1 "src="images/111.png" width="13" height="11">--><span class="img_1"></span>
						</a> 
						<span class="z5"><a href="<c:url value='/shop/promotions/NEB_02.html'/>">查看更多</a></span> 
					</div>
				</div>
				<img class="pic" src="/sm-shop/resources/img/cximages/neb.png"/></div></li>


		<li> <div class="wkk">
				<div class="nkk">
					<a href="<c:url value='/shop/promotions/yishen_02.html'/>">
						<img class="n1" src="/sm-shop/resources/img/cximages/翊圣_01.png"/>
						<img class="n2" src="/sm-shop/resources/img/cximages/翊圣_02.png"/>
						<img class="n3" src="/sm-shop/resources/img/cximages/翊圣_03.png"/></a>
					<div class="z1"> 
						<span class="z2_1">PCR Master Mix 单价30元</span><!--</br>-->
						<div class="line"></div>
						<span class="z3"><b>翊圣</b></span>
						<a href="#">
							<span class="z4">收藏品牌</span></a> 
						<span class="z5"><a href="<c:url value='/shop/promotions/yishen_02.html'/>">查看更多</a></span> 
					</div>
				</div>
				<img class="pic" src="/sm-shop/resources/img/cximages/yishen.png"/>
			</div>
		</li>

	</ul>
</div>
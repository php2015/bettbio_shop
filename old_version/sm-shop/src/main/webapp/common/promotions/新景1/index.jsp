<style>
/*
	[Destoon B2B System] Copyright (c) 2008-2013 Destoon.COM
	This is NOT a freeware, use is subject to license.txt
*/
/* global */
.big_box{ width: 1200px; background-color: #ececec; margin:0 auto;}
.main_body{ width:800px; margin:0 auto; background-color: #f5f5f5;}
.topmenubg{background:url('image/topmenubg.jpg');height:27px;line-height:27px;width:100%;margin-top:0px;color:#939393;position:fixed;z-index:1001;}
.topinfo{width:610px;height:27px;}.topinfo a{color:#848484}
.topmenu{height:27px;}.topmenu li{margin-right:10px;float:right}.topmenu li a{color:#939393;}
.siteinfo{padding-top:40px;height:80px;margin-top:20px;}
.siteinfo .logo{width:230px;height:63px;}
.siteinfo .logo h3{margin-top:90px;}
.siteinfo .banner{width:220px;padding-left:10px;height:45px;line-height:24px;;padding-top:10px;color:#afafaf;margin-left:40px;}
.siteinfo .banner b{font-size:18px;vertical-align:middle;color:#717171;letter-spacing:1px;}
.welcome{width:670px;}
.welcome a{padding:5px 5px 5px 5px; margin-right:5px;line-height:26px;color:#888}
.welcome a:link,a:visited,a:active{color:#888;}
.welcomey,.welcome a:hover{ background:#ffffff;border:1px solid #d2d2d2;border-bottom:#fff 1px solid;color:#dc2e2e;}
#shopcart{position:absolute;z-index:1;margin-left:855px;margin-top:-31px;height:30px;line-height:30px;font-size:14px;text-indent:38px;width:138px;display:none}
.webw{width:995px;margin:auto;margin-top:0px;height:auto;padding-left:8px;}/*网页内容固定标准宽度*/
.teceinfo{height:80px;padding-top:30px;}
.clearfix:after {content: "."; display: block; height:0; clear:both; visibility: hidden;}
.clearfix {*zoom:1;}
.xinjingbody * {word-break:break-all;font-family:Verdana,Arial;}
input,textarea {font-size:12px;}
.banner img{height:225px;}
img {border:none;}
.xinjingbody ul li {list-style-type:none;}
.xinjingbody ol li {list-style-type:decimal;}
.xinjingbody ul,form {margin:0px;padding:0px;}
/* links */
a:link,a:visited,a:active {color:#676767;}
a:hover {color:#FF6600;text-decoration:none;}
a.w:link,a.w:visited,a.w:active{text-decoration:none;color:#FFFFFF;}
a.w:hover{text-decoration:underline;}
a.t:link,a.t:visited,a.t:active{text-decoration:none;color:#225588;}
a.t:hover{text-decoration:underline;}
a.b:link,a.b:visited,a.b:active {color:#014CCC;text-decoration:none;}
a.b:hover {color:#FF6600;}
/* show */
.title {text-align:center;font-size:16px;font-weight:bold;color:#FF6600;line-height:40px;}
.info {margin:0 10px 0 10px;border-top:#C0C0C0 1px dotted;border-bottom:#C0C0C0 1px dotted;line-height:25px;text-align:center;}
.content {font-size:13px;line-height:180%;padding:10px;}
.content p {margin:0 0 15px 0;}
.property {border:#E6E6E6 1px solid;background:#F6F6F6;padding:10px;margin:0 10px 0 10px;}
.property li {float:left;width:30%;height:22px;line-height:22px;overflow:hidden;}
.foot {border:#DDDDDD 1px solid;text-align:center;padding:15px;line-height:28px;background:#FFFFFF;}
.pos {padding:5px 15px 5px 25px;background:url('../image/homepage.gif') no-repeat 5px 3px;}
.inp {border:#99C4D5 1px solid;padding:2px;color:#666666;}
.sbm {height:20px;border:#86A2C0 1px solid;color:#333333;background:url('../image/btn_bg.gif') repeat-x;}
/* sign */
.sign {z-index:1000;position:absolute;width:180px;height:50px;background:url('../image/vip_bg.gif') no-repeat;margin:-10px 0 0 780px;}
.sign div {padding:23px 0 0 73px;}
.sign strong {font-size:18px;color:red;}
.sign span {font-size:18px;color:red;font-weight:bold;padding:0 0 0 60px;}
/* thumb */
.thumb {padding:6px;}
.thumb img {border:#C0C0C0 1px solid;padding:3px;}
.thumb div {width:90%;height:26px;overflow:hidden;line-height:26px;font-size:13px;margin:6px 0 6px 0;}
.thumb p {font-size:10px;color:#888888;margin:2px;}
.thumb_on {background:#F1F1F1;border:#99C4D5 1px dotted;}
/* album */
#mid_pos {position:absolute;}
#mid_div {width:240px;height:180px;cursor:crosshair;padding:5px;border:#CCCCCC 1px solid;background:#F3F3F3;}
#zoomer {border:#D4D4D4 1px solid;width:120px;height:90px;background:#FFFFFF url('../image/zoom_bg.gif');position:absolute;opacity:0.5;filter:alpha(opacity=50);}
#big_div {width:320px;height:240px;border:#CCCCCC 1px solid;background:#FFFFFF;position:absolute;overflow:hidden;}
#big_pic {position:absolute;}
.ab_im {padding:2px;margin:10px 0 10px 13px;border:#C0C0C0 1px solid;}
.ab_on {padding:2px;margin:10px 0 10px 13px;border:#FF6600 1px solid;background:#FF6600;}
.bannerm{text-align:center;}
.bannerm .banner{margin:auto;text-align:center}
.bannerm .banner img{margin:auto;}
/* pages */
.pages {padding:10px 0 10px 0;text-align:center;font-size:12px;}
.pages a:link,.pages a:visited,.pages a:active  {background:#FFFFFF;border:#E6E6E6 1px solid;padding:1px;text-decoration:none;}
.pages a:hover  {background:#FFFFFF;border:#CCCCCC 1px solid;color:#FF6600;padding:1px;text-decoration:none;}
.pages strong {background:#2E6AB1;padding:1px;border:#CCCCCC 1px solid;color:#FFFFFF;}
.pages_inp {border:#CCCCCC 1px solid;padding:1px;width:30px;text-align:center;color:#666666;}
.pages_btn {width:26px;background:#F6F6F6;border:#CCCCCC 1px solid;font-weight:bold;font-size:11px;}
.pages label {background:#FFFFFF;border:#CCCCCC 1px solid;padding:1px 5px 1px 5px;}
.pages label em {color:red;font-weight:bold;font-style:normal;}
.pages label span {font-weight:bold;}
/* comment */
.stat {padding:15px 0 10px 0;}
.stat_p {height:10px;line-height:10px;background:#FFF7D2;font-size:1px;}
.stat_p div {height:10px;line-height:10px;background:#FFA40D;border-left:#FFA40D 2px solid;float:left;}
.comment {padding:15px 10px 15px 10px;border-top:#AACCEE 1px dotted;}
.comment_sp {background:#F9FCFE;}
.comment_content {color:#333333;font-size:14px;line-height:180%;clear:both;padding:5px;}
.comment_title {height:26px;line-height:26px;color:#1B4C7A;padding:0 5px 0 5px;}
.comment_reply {padding:5px 10px 5px 10px;margin:0 5px 0 5px;line-height:180%;border:#E6E6E6 1px solid;background:#FFFFE3;}
.comment_no,.order_no {text-align:center;padding:20px;font-size:13px;}

/* basic */
.f_l,.fl {float:left;}
.f_r,.fr {float:right;}
.t_r {text-align:right;}
.t_c {text-align:center;}
.f_b {font-weight:bold;}
.f_n {font-weight:normal;}
.f_white {color:white;}
.f_gray {color:#666666;}
.f_orange {color:#FF6600;}
.f_red {color:red;}
.f_green {color:green;}
.f_blue {color:blue;}
.f_dblue {color:#225588;}
.f_price {font-weight:bold;font-family:Arial;color:#FF6600;}
.px10 {font-size:10px;}
.px11 {font-size:11px;}
.px12 {font-size:12px;}
.px13 {font-size:13px;}
.px14 {font-size:14px;}
.lh18 {line-height:180%;}
.b10 {font-size:1px;height:10px;}
.pd10 {padding:10px;}
.c_b{clear:both;}
.dsn {display:none;}
.c_p {cursor:pointer;}
.highlight {color:red;}

				.blist{height:120px;background:#fff;padding-top:15px;margin:auto;}
				.brand img{padding:1px;background:#fff;}
				.brand a:link{color:#949494}
				.brand a:link img,.brand a:visited img,.brand a:active img{border:1px solid #ddd;}
				.brand a:hover img{border:1px solid #f59d0a;background:#fbda82}
				.brand  div{ display:block;display:-moz-inline-box; display:inline-block; white-space:nowrap;overflow: hidden; -o-text-overflow: ellipsis;text-overflow:ellipsis;word-break:keep-all;  text-align:center; font-size:12px;width:100px;}
				.brand  div{ display:block;display:-moz-inline-box; display:inline-block; white-space:nowrap;overflow: hidden; -o-text-overflow: ellipsis;text-overflow:ellipsis;word-break:keep-all;  text-align:center; font-size:12px;width:100px;}

.homebg .m .sign{padding:0}

.img_xinjing{margin: 0 auto; display: block;}

.txt_xj {
	margin: 0 auto;
	display: block;
	text-align: center;
	position: relative;
	bottom: 26px;
	color: #fff;
	font-size: 16px;
	height: 0px;
}
</style>
<div style=" width: 1200px; margin: 0 auto" class="xinjingbody">
	<img class="img_xinjing" src="/sm-shop/resources/promotionImages/新景1/图层-1.png" width="759" height="1019" alt=""/>
<span class="txt_xj">
	本活动最终解释权归杭州新景生物试剂开发有限公司所有
</span></div>

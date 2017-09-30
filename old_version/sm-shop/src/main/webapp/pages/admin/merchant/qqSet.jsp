<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
 request.setCharacterEncoding("UTF-8");
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>   
<link href="<c:url value="/resources/css/image/dropify.css" />" rel="stylesheet"></link>
<script src="<c:url value="/resources/js/tools/dropify.js" />"></script>
<%@ page session="false"%>

<div class="row text-center">
	<br>
	<h3>QQ在线客服设置-QQ开启临时会话教程</h3>
		如果您在点击在线客服以后出现下面提示框，“您需要添加对方好友，才能发送回话消息。”，表示您的客服QQ没有开启临时会话功能，需要开启以后才可以正常使用在线客服。<br>
		<img  src="<c:url value="/resources/img/qq/7c361401765390.png"/>"><br>
		<h4>设置方法一如下：	</h4>
		1、登陆QQ，
		2、在“安全设置”——“省份验证”——设置“允许任何人”和你交流对话，这个很有用，即使腾讯服务器出现故障，要加为好友才可以对话，设置这个后，对话点一下“加为好友”，马上可以和你发信息。<br>
		<img  src="<c:url value="/resources/img/qq/qqlogin123.jpg"/>"><br>
		3、在“安全设置”——“防骚扰设置”——把“不接收任何临时会话”的勾去掉。<br>		
		<img  src="<c:url value="/resources/img/qq/qqlogin223.jpg"/>"><br>
		特别提醒：
		对话时提示“QQ在线状态”服务尚未启用，您需要添加对方为好友才能与其进行会话，
		
		<br><br>
		<h4>设置方法二如下：</h4>
		1、登陆腾讯官方网站：http://shang.qq.com/index.php<br>
		<img  src="<c:url value="/resources/img/qq/1140925081027.jpg"/>"><br>
		2、用你的QQ账号登陆之后，点“推广工具”，按下图所示，点击“立即免费开通”。这个必须开通，否则不能临时会话，就会显示“未启用”。<strong>这一步是关键，必须开通。</strong><br>
		<img  src="<c:url value="/resources/img/qq/1140925081027.jpg"/>"><br>
		3、点击左边的“设置”，如下设置。<br>
		<img  src="<c:url value="/resources/img/qq/ssssdd3d34543.jpg"/>"><br>
		（1）看“会话能力设置”，都要打勾<br>
		<img  src="<c:url value="/resources/img/qq/204455555d1.jpg"/>"><br>
		（2）安全设置<br>
		<img  src="<c:url value="/resources/img/qq/qqnoset3.jpg"/>"><br>
		如果您设置过了，还是有不能临时会话的问题，请先（1）“停用服务”再点“启用服务”（2）取消全部的会话能力保存，然后再都打上勾，再保存。相当于取消之后，再重新设置。
</div>
	
				

				
				
				
				



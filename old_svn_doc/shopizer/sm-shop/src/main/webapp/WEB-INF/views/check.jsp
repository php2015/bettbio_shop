<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>用户校验界面</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script src="/sm-shop/resources/js/jquery-2.0.3.min.js"></script>
  </head>
  <script type="text/javascript">
  	$(function(){
  		$("#phone,#phone1").focus(function(){
  			$("#zhucejieguo").text("");
  			$("#xiadan").text("");
  		});
  		$("#btn").click(function(){
  			 $.ajax({
 	            url:"/sm-shop/shop/customer/validtion.html",
 	            type:'post',
 	            data:{
 	              phone:$("#phone").val(),
 	              emailAddress:"null"
 	            },
 	            dataType:'json',
 	            timeout:3000,
 	            cache:true,
 	            async:true,
 	            success:function(data){
 	               if(data.response.status != '0' || data.response.status !="0" || data.response.status != 0){
 	        			$("#zhucejieguo").text("此用户已经注册");
 	               }else{
 	            	   $("#zhucejieguo").text("此用户未注册");
 	               }
 	            }
 	         });
  		});
  		$("#btn2").click(function(){
  	  		
  	  			 $.ajax({
  	 	            url:"/sm-shop/checkOrder.html",
  	 	            type:'post',
  	 	            data:{
  	 	              phone:$("#phone1").val()
  	 	            },
  	 	            dataType:'text',
  	 	            timeout:3000,
  	 	            cache:true,
  	 	            async:true,
  	 	            success:function(data){
  	 	             	if(data=="true"){
  	 	             		$("#xiadanjieguo").text("该用户已下单");
  	 	             	}else{
  	 	             		$("#xiadanjieguo").text("该用户未下单");
  	 	             	}
  	 	            }
  	 	         });
  		});
  	})
  </script>
  <body>
    检查是否注册手机号码:<input id="phone" /><button type="button" id="btn">检验</button><br>
    <span style="color: red;padding-left:20px" id="zhucejieguo"></span><br>
    检查是否下单手机号码:<input id="phone1"/><button type="button" id="btn2">检验</button><br>
    <span style="color: red;padding-left:20px" id="xiadanjieguo"></span>
  </body>
</html>

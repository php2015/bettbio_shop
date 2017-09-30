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
.jfdh_top{
  border: 1px solid #ccc;
  border-radius: 4px 4px 0 0;
  width:100%;
  height:300px;
  border-bottom: 1px solid #d5d5d5;
  position: relative;
  text-align:center;
}
.jfdh_top img {
	width:100%;
	max-width:283px;
}
.jfdh_right {
	width: 79px;
	height: 77px;
	display: block;
	position: absolute;
	right: 12px;
	top: 0px;
	background-image: url(../../resources/img/market/jfdh_right.png);
	background-repeat: no-repeat;
}
.jfdh_rightSz {
display: block;
font-size: 20px;
color: #fff;
font-family: "Microsoft Yahei";
margin-top: 18px;
text-align: center;
}
.jfdh_rightJf {
display: block;
font-size: 16px;
color: #fff;
font-family: "Microsoft Yahei";
text-align: center;
}
.jfdh_bottom {
border: 1px solid #ccc;
border-top: 0px;
padding:5px;
}

.mcbg {
width: 100%;
height: 50px;
cursor: pointer;
}
.dn {
display: none;
}
.jfdh_mcbg {
width: 100%;
height: 50px;
background-color: #000;
position: absolute;
bottom: 0px;
left: 0px;
opacity: 0.5;
}
.jfdh_mc {
width: 100%;
color: #fff;
line-height: 50px;
text-align: center;
position: absolute;
bottom: 0px;
left: 0px;
}
</style>
<script>
$(function(){
	$(".jfdh_top").bind("mouseover",function(){
		$(this).find(".dn").css("display","block");
	}).bind("mouseout", function(){
		$(this).find(".dn").css("display","none");
	});
});

var curruntPoint;
function cash(gid,point) {
	$('#giftId').val(gid);
	 $('#cash').modal('show');
	 curruntPoint=point;
	};
	
	
</script>
<div class="container-fluid box main-padding-lr">
	<div class="row">
	<c:forEach items="${giftsList.gifts}" var="gift"  >
		<div class="col-md-3">
			<div class="jfdh_top">
				<img src='<c:url value="${gift.pictureSrc}"/>'/>
				<p class="jfdh_right">	
					<span class="jfdh_rightSz">${gift.points }</span>
					<span class="jfdh_rightJf">积分</span>
				</p>
				<div class="mcbg dn">
				  <span class="jfdh_mcbg"></span>
	              <span class="jfdh_mc">${gift.name }</span>
	            </div>
			</div>
			<div class="jfdh_bottom" style="border-left: none;border-bottom: none;border-right: none;">
					<span style="font-size:16px;color:#7a7a7a; top:15px;"><a style="margin-left:5% ">市场价&nbsp;￥${gift.price }</a>
						<c:choose>
							<c:when
								test="${not empty customer && queryByMemberPoints >= gift.points}">
								<a style="font-size:14px;color:#7a7a7a;float:right;margin-right:5%;"
									class="cashNow" href="javascript:void(0);"
									onclick="cash(${gift.id}, ${gift.points})">立即兑换</a>
							</c:when>
							<c:otherwise>
								<a
									style="font-size:14px;color:#7a7a7a;float:right;margin-right:5%;">积分不足</a>
							</c:otherwise>
						</c:choose>
					</span>
			</div>
		</div>
	</c:forEach>
	</div>
<br>
<jsp:include  page="/pages/shop/common/customer/memberpointExchange.jsp" />
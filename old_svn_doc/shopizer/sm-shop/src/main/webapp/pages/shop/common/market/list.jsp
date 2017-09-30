
<%
	response.setCharacterEncoding("UTF-8");
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", -1);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<script src="<c:url value="/resources/js/billing.js" />"></script>

<style>

.jfdh_top {
	border: 1px solid #ccc;
	border-radius: 4px 4px 0 0;
	width: 100%;
	height: 280px;
	border-bottom: 1px solid #d5d5d5;
	position: relative;
	text-align: center;
}

.jfdh_top img {
	width: 100%;
	max-width: 283px;
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
	padding: 5px;
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
/*用户信息*/
.userInfo, .helpInfo {
	border: 1px solid #d5d5d5;
	width: 100%;
	min-height: 136px;
}

.userInfo {
	border-bottom: 0px;
}

.userInfo .headImg {
	border-radius: 50%;
	overflow: hidden;
	width: 80px;
	height: 80px;
	margin: 10px;
	float: left;
}

.userInfo .userDetailInfo {
	float: left;
	width: 50%;
	height: 80px;
	margin-top: 10px;
	font-size: 12px;
	padding-top: 20px;
	line-height: 25px;
}

.userInfo .userDeatilInfoBottom {
	clear: left;
	width: 99%;
	font-size: 12px;
	margin-left: 10px;
}

.userInfo .userDeatilInfoBottom .jf {
	color: red;
	padding: 0px 20px 0px 5px;
	font-size: 14px;
}
/*帮助信息*/
.helpInfo .title {
	margin-right: 20px;
	font-weight: bold;
	color: #333333;
	font-size: 16px;
}

.helpInfo ul {
	list-style-type: none;
	margin-top: 22px;
	padding-left: 12px;
	line-height: 25px;
	font-size: 14px;
	color: #666666;
}
.helpInfo ul a{
text-decoration: underline!important;
}
/*tab导航信息*/
.nav-tabs>li.active>a, .nav-tabs>li.active>a:hover, .nav-tabs>li.active>a:focus
	{
	border-top: 3px solid #4285f4;
	/*border-bottom: 1px solid #ddd;*/
	border-bottom: 0px;
	border-left: 1px solid #d9d9d9;
	border-right: 1px solid #d9d9d9;
}

.nav-tabs {
	background-color: #eeeeee;
	height: 50px;
	border-left: 1px solid #ddd;
	min-width: 961px;
}

.nav-tabs>li>a {
	height: 50px;
}

.nav-tabs>li.active>a {
	height: 50px;
}

#myTab li a {
	padding-top: 17px;
	font-weight: bold;
	width:125px;
	text-align: center;
	font-size: 14px;
}
/*积分商城信息*/
#shopping, #exchange {
	width: 100%;
	min-height: 1008px;
	border: 1px solid #d9d9d9;
	border-top: 0px;
}

#myTabContent {
	min-width: 961px;
}
/*限时兑换*/
#myTabContent .timeLimit .title {
	font-size: 14px;
	font-weight: bold;
	padding: 45px 0px 18px 22px;
}

#myTabContent .timeLimit .content .row .col-md-2 {
	min-height: 212px;
	padding: 0px;
	min-width: 212px;
	margin-left: 22px;
}

#myTabContent .timeLimit .content .row .col-md-2 .theme {
	border: 1px solid #dbdbdb;
}

#myTabContent .timeLimit .content .row .col-md-2 .integral, .time {
	width: 100%;
	min-height: 30px;
	position: absolute;
	bottom: 40px;
	text-align: center;
	font-size: 14px;
}

#myTabContent .timeLimit .content .row .col-md-2 .time {
	bottom: 80px;
	min-height: 36px;
	background-color: #7f7f7f;
	padding-top: 8px;
	filter: alpha(opacity = 80);
	-moz-opacity: 0.8;
	-khtml-opacity: 0.8;
	opacity: 0.8;
	color: #fafafa;
}

#myTabContent .timeLimit .content .row .col-md-2 .describe {
	text-align: center;
	font-size: 14px;
	font-weight: bold;
	min-height: 20px;
	padding: 10px 0px;
	border-bottom: 0px;
}

#myTabContent .timeLimit .content .row .col-md-2 .time .timeVal {
	color: red;
}

#myTabContent .timeLimit .content .row .col-md-2 .integral .val {
	color: red;
}

#myTabContent .timeLimit .content .row {
	margin: 0px;
}
/*热门兑换*/
#myTabContent .hot .title {
	font-size: 14px;
	font-weight: bold;
	padding: 45px 0px 18px 22px;
}

#myTabContent .hot .content .row .col-md-2 {
	min-height: 212px;
	padding: 0px;
	min-width: 212px;
	margin-left: 22px;
}

#myTabContent .hot .content .row .col-md-2 .theme {
	border: 1px solid #dbdbdb;
}

#myTabContent .hot .content .row {
	margin: 0px;
}

#myTabContent .hot .content .row .col-md-2 .integral {
	font-size: 14px;
	font-weight: bold;
	margin: 10px 0px;
}

#myTabContent .hot .content .row .col-md-2 .integral .val {
	color: red;
}
/*兑换记录*/
#exchange .exchange {
	padding: 22px;
}

#exchange .exchange table thead {
	background-color: #eeeeee;
}

#exchange .exchange table thead tr td {
	line-height: 34px;
	text-align: center;
	font-size: 14px;
	font-weight: bold;
}

#exchange .exchange table tbody tr {
	border-top: 0px;
	border-bottom: 1px solid #eeeeee;
}

#exchange .exchange table tbody tr td {
	line-height: 34px;
	text-align: center;
	font-size: 14px;
	font-weight: bold;
}

.loginName {
	font-size: 14px;
	font-weight: bold;
}

</style>
<script>
    var queryByMemberPoints=0;
    var curruntPoint=0;
    var giftsid=0;
    var addressId=0;
	function shoppingChoice() {
		//兑换事件委托
		$("div").delegate(
				".col-md-2",
				"click",
				function() {
					if ($(".loginName").text() == "请登录") {
						alert("请先登录");
						return false;
					} else {
						queryByMemberPoints=$(".jf").text();
						curruntPoint=$(this).attr("curruntPoint");
						giftsid=$(this).attr("gifsid");
						if(parseInt(queryByMemberPoints)>parseInt(curruntPoint)){
							$.post(getContextPath()
									+ "/shop/customer/findAddress.html", {},
									function(data) {
										if (data != null) {
											if(data.defaultAddress!=null){
												addressId=data.defaultAddress.id;
												var addressList="";
													addressList+="<div  id='info_"+data.defaultAddress.id+"' style='word-break: break-all; word-wrap:break-word;cursor:pointer;font-size:14px;color:#7a7a7a; top:15px; left:0px;'>";
													addressList+="姓名："+data.defaultAddress.name+"&nbsp;&nbsp;&nbsp;";
													addressList+="电话："+data.defaultAddress.telephone+"&nbsp;&nbsp;&nbsp";
													addressList+="地址："+data.defaultAddress.streetAdress;
													addressList+="</div>";	
												$("#modaltable").html("");
												$("#modaltable").append(addressList);
												$("#cash").modal('show');
												return false;
											}
											var addressList="";
											$.each(data.othersAddresss, function(value) {
												addressList+="<div onclick='changeId("+data[value].id+")' id='info_"+data[value].id+"' style='word-break: break-all; word-wrap:break-word;cursor:pointer;font-size:14px;color:#7a7a7a; top:15px; left:0px;'>";
												addressList+="姓名："+data[value].name+"&nbsp;&nbsp;&nbsp;";
												addressList+="电话："+data[value].telephone+"&nbsp;&nbsp;&nbsp";
												addressList+="地址："+data[value].streetAdress;
												addressList+="</div>";	
											});
											$("#modaltable").html("");
											$("#modaltable").append(addressList);
											$("#cash").modal('show');
										}
									}, "json");
						}else{
							alert("积分不足");
							return false;
						}
					}
					return false;
				});
		    $.post(
						getContextPath() + "/shop/marketpoints/jfShopping.html",
						{},
						function(data) {
							//限時兌換
							var timeLimits = "<div class='row'>";
							for ( var index in data.timeLimitGifts.gifts) {
								if (index + 1 % 4 == 0 && index != 0) {
									timeLimits += "</div><div class='row'>";
									timeLimits += "<div class='col-md-2' gifsid="+data.timeLimitGifts.gifts[index].id+" curruntPoint="+data.timeLimitGifts.gifts[index].points+"><div class='theme'><img src="
											+ getContextPath()
											+ data.timeLimitGifts.gifts[index].pictureSrc
											+ " width='100%' /><div class='integral'>";
									timeLimits += "<span class='val'>"
											+ data.timeLimitGifts.gifts[index].points
											+ "</span><span>积分</span></div><div class='time'>";
									timeLimits += "距离结束:<span class='timeVal'>1</span>小时<span class='timeVal'>20</span>分<span class='timeVal'>30</span>秒</div></div>";
									timeLimits += "<div class='describe'>"
											+ data.timeLimitGifts.gifts[index].name
											+ "</div></div>";
								} else {
									timeLimits += "<div class='col-md-2'gifsid="+data.timeLimitGifts.gifts[index].id+" curruntPoint="+data.timeLimitGifts.gifts[index].points+" > <div class='theme'><img src="
											+ getContextPath()
											+ data.timeLimitGifts.gifts[index].pictureSrc
											+ " width='100%' /><div class='integral'>";
									timeLimits += "<span class='val'>"
											+ data.timeLimitGifts.gifts[index].points
											+ "</span><span>积分</span></div><div class='time'>";
									timeLimits += "距离结束:<span class='timeVal'>1</span>小时<span class='timeVal'>20</span>分<span class='timeVal'>30</span>秒</div></div>";
									timeLimits += "<div class='describe'>"
											+ data.timeLimitGifts.gifts[index].name
											+ "</div></div>";
								}

							}
							timeLimits += "</div>"
							$("#timeLimitGifts").text("");
							$("#timeLimitGifts").append(timeLimits);
							if (data.timeLimitGifts.gifts.length == 0) {
								$(".timeLimit").hide();
							}

							var hots = "<div class='row'>";
							for ( var index in data.hotGifts.gifts) {
								if (index + 1 % 4 == 0 && index != 0) {
									hots += "</div><div class='row'>";
									hots += "<div class='col-md-2' gifsid="+data.hotGifts.gifts[index].id+" curruntPoint="+data.hotGifts.gifts[index].points+"><div class='theme'><img src="
											+ getContextPath()
											+ data.hotGifts.gifts[index].pictureSrc
											+ " width='100%' /></div><div class='integral'>";
									hots += "<span class='val'>"
											+ data.hotGifts.gifts[index].points
											+ "</span><span>积分</span><br><span>"
											+ data.hotGifts.gifts[index].name
											+ "</span></div>";
								} else {
									hots += "<div class='col-md-2' gifsid="+data.hotGifts.gifts[index].id+" curruntPoint="+data.hotGifts.gifts[index].points+"><div class='theme'><img src="
											+ getContextPath()
											+ data.hotGifts.gifts[index].pictureSrc
											+ " width='100%' /></div><div class='integral'>";
									hots += "<span class='val'>"
											+ data.hotGifts.gifts[index].points
											+ "</span><span>积分</span><br><span>"
											+ data.hotGifts.gifts[index].name
											+ "</span></div></div>";
								}
							}
							hots += "</div>";
							$("#hotGifts").html("");
							$("#hotGifts").append(hots);
							if (data.timeLimitGifts.gifts.length == 0) {
								$(".hot").hide();
							}
							$("#exchange").hide();
							$("#shopping").show();
						}, "json");
	}
	//兑换记录选择
	function exchangeChoice() {
		$.post(getContextPath() + "/shop/marketpoints/jfRecord.html", {},
				function(data) {
					var str = "";
					for ( var index in data) {
						str += "<tr><td>" + getLocalTime(data[index].createDate)
								+ "</td><td>" + data[index].gifName
								+ "</td><td>" + data[index].gifPoint
								+ "积分</td><td>" + data[index].status
								+ data[index].deliveryCode
								+ data[index].deliveryNumber + "</td></tr>";
					}
					$(".tbody").html("");
					$(".tbody").append(str);
					$("#shopping").hide();
					$("#exchange").show();
				}, "json");
	}
	$(function() {
		$("#shopping").show();
		shoppingChoice();
		//选择事件
		$(".choice").click(function() {
			if ($(this).text() == "积分商城") {
			    $(this).css("border-left","0px");
				shoppingChoice();
			} else if ($(this).text() == "兑换记录") {
			    var qingdenglu=$(".loginName").text().replace(/(^\s*)|(\s*$)/g, "");
				if (qingdenglu== "请登录") {
					alert("请登录");
					return false;
				} else {
					exchangeChoice();
				}
			}
		});
	});
	//改变编号
	function changeId(id){
		addressId=id;
	}
	
	function getLocalTime(v) {     
	
                if (/^(-)?\d{1,10}$/.test(v)) {
                    v = v * 1000;
                } else if (/^(-)?\d{1,13}$/.test(v)) {
                    v = v * 1;
                } else {
                    /*alert("时间戳格式不正确");*/
                    return;
                }
                var dateObj = new Date(v);
                if (dateObj.format('yyyy') == "NaN") { /*alert("时间戳格式不正确");*/return; }
                var UnixTimeToDate = dateObj.getFullYear() + '/' + (dateObj.getMonth() + 1) + '/' + dateObj.getDate() + ' ' + dateObj.getHours() + ':' + dateObj.getMinutes() + ':' + dateObj.getSeconds();
                return UnixTimeToDate;
    
    }     
    
    Date.prototype.format = function (format) {
    var date = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S+": this.getMilliseconds()
    };
    if (/(y+)/i.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
    }
    for (var k in date) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
        }
    }
    return format;
};
</script>
<br>
<br>
<br>
<div class="head-navbar-left head-navbar-right">

	<div class="row"
		style="font-size: 14px;margin-top: -35px;margin-left: 0px;margin-bottom: 10px;">
		首页>积分商城</div>
	<div class="row">
		<div class="col-md-3">
			<div class="userInfo">
				<div class="headImg">
					<img src="<c:url value="/resources/img/userLogo.png"/>"
						width="100%">
				</div>
				<div class="userDetailInfo">
					<span>您好,</span><span class="loginName"> <c:choose>
						
							<c:when test="${loginName!=null}">
								${loginName}
						   </c:when>
							<c:otherwise>
							  请登录
						   </c:otherwise>
						</c:choose></span><br /> <span class="userMessage" <c:if test="${loginName==null}">style="visibility: hidden;"</c:if>>等级:
						<c:forEach begin="1" end="${grade}">
						<img src="<c:url value="/resources/img/quanxing.png"/>" width="11" heigth="11">
						</c:forEach>
						</span>
				</div>
				<div class="userDeatilInfoBottom userMessage"
					<c:if test="${loginName==null}">style="visibility: hidden;"</c:if>>
					<span>百图积分:</span><span class="jf">${points}</span><span><a
						href="#">积分明细</a></span>
				</div>
			</div>
			<div class="helpInfo">
				<ul>
					<li><span class="title">帮助中心</span><a href="<c:url value="/shop/pointChange.html"/>">更多</a></li>
					<li><a href="<c:url value="/shop/pointChange.html"/>">如何获得积分</a></li>
					<li><a href="<c:url value="/shop/pointChange.html"/>">积分兑换须知</a></li>
					<li><a href="<c:url value="/shop/pointChange.html"/>">用户等级明细及等级权限</a></li>
				</ul>
			</div>
		</div>
		<div class="col-md-9">
			<ul id="myTab" class="nav nav-tabs">
				<li class="active" ><a href="#" data-toggle="tab" class="choice" style="border-left: 0px">积分商城</a></li>
				<li><a href="#" data-toggle="tab" class="choice">兑换记录</a></li>
			</ul>
			<div id="myTabContent" class="tab-content">
				<div class="tab-pane fade in active" id="exchange"
					style="display: none">
					<div class="exchange">
						<table class="table table-hover">
							<thead>
								<tr>
									<td>兑换日期</td>
									<td>礼品名称</td>
									<td>积分消耗</td>
									<td>状态</td>
								</tr>
							</thead>
							<tbody class="tbody">

							</tbody>
						</table>
					</div>
				</div>
				<div class="tab-pane fade in active" id="shopping"
					style="display: none">
					<div class="timeLimit">
						<div class="title">限时兑换</div>
						<div class="content" id="timeLimitGifts"></div>
					</div>
					<div class="hot">
						<div class="title">热门兑换</div>
						<div class="content" id="hotGifts"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<br>
<jsp:include page="/pages/shop/common/customer/memberpointExchange.jsp" />
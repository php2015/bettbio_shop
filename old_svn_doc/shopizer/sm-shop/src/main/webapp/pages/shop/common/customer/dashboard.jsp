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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%> 
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<style>

</style>
<script type="text/javascript">
$(function(){
    //show cookie
    
    var json = eval("("+$.cookie("history")+")");
    if(json !=null && json.length>0){
    	 var list = ''; 
         for(var i=0; i<json.length;i++){         	
         	list +='<div style="margin-left:22px;float:left;"><div style="margin-top:20px;"><img style="width:142px;height:142px;" src="/sm-shop'+json[i].img+'"/></div>';
         	list +='<div style="margin-top:20px;color:#333;font-size:12px;width:142px;"><a target="_blank"  href="<c:url value="/shop/product/"/>' +json[i].id + '.html">'+json[i].name+'</a></div>';
         	var star =(json[i].quilty)/20;
         	var left=(json[i].quilty)%20;
         	list +='<div style="margin-top:10px;color:#999;font-size:12px;">';
         	for(var j=1;j<star;j++){
         		list +='<img  src="<c:url value="/resources/img/stars/star-on.png" />" />';
         	}
         	if(left>0){
         		list +='<img  src="<c:url value="/resources/img/stars/star-half.png" />" />';
         	}
         	list +='</div></div>'; 
         	if(i==4){
         		break;
         	}
         }
         $("#my_history").html(list); 
    }
});
</script>
<!--最外层div  -->
<div class="out">
	<!-- 基本信息层 -->
	<div class="account_baseInfo">
			<div class="per_info">
					<div class="" style="float:left;background-color:#fff;padding-left:22px;padding-top:20px;">
							<a href="<c:url value="/shop/customer/basicInfo.html" />">
								<img style="width:90px;height:90px;" src="<c:url value="/resources/img/custmerinfo.png" />"/>
							</a>
						<div style="font-size:14px;margin-top:20px;">百图会员</div>
					</div>
					<div style="float:left;width:100px;margin-top:24px;margin-left:40px;">
						<c:if test="${not empty requestScope.CUSTOMER.nick}">
							<span style="font-size:16px;color:#333333;">
								<c:out value="${sessionScope.CUSTOMER.nick}" />
							</span><br/>
							<div style=" margin-top:20px;""><span style="font-size:14px;color:#999;">完善更多资料将会获得积分大礼包</span></div>
						</c:if>
					</div>
			</div>
			<div style="float:left;font-size:14px;width:224px;text-align:center;">
				<div style="margin-top:42px;">等级</div>
				<div style="text-align:center;margin-top:20px;">
					<c:forEach begin="0" end="${customer.grade}">
						<img alt="go to order" src="<c:url value="/resources/img/stars/star-on.png" />">
					</c:forEach>
				</div>	
				<div style="margin-top:20px;text-align:center">
					<a href="javascritp:void(0);">
						等级明细<img src='<c:url value="/resources/img/arr.png" />'/>
					</a>
				</div>
			</div>
			<div style="float:left;height:100px;border:1px solid #d9d9d9;margin-top:30px;"></div>
			<div style="font-size:14px;float:left;width:224px;text-align:center;">
				<div style="margin-top:42px;">金额总计</div>
				<div style="text-align:center;color:#999;margin-top:20px;">
					￥99元
				</div>	
				<div style="margin-top:20px;text-align:center">
					<a href="javascritp:void(0);">
						查看详情<img src='<c:url value="/resources/img/arr.png" />'/>
					</a>
				</div>
			</div>
			<div style="float:left;height:100px;border:1px solid #d9d9d9;margin-top:30px;"></div>
			<div style="font-size:14px;float:left;width:224px;text-align:center;">
				<div style="margin-top:42px;">积分</div>
				<div style="text-align:center;color:#999;margin-top:20px;">
					<c:out value="${menberPoints}"/>
				</div>	
				<div style="margin-top:20px;text-align:center">
					<a href="javascritp:void(0);">
						去兑换<img src='<c:url value="/resources/img/arr.png" />'/>
					</a>
				</div>
			</div>
	</div>
	<!-- 我的订单层 -->
	<div class="account_title"><span>我的订单</span><a href="#" style="float:right;">查看更多</a></div>
	<div class="account_orders">
		<div class="order_sort">
			<span style="padding-left:34px;">所有订单</span>
			<span style="padding-left:80px;">待发货</span>
			<span style="padding-left:82px;">待收货</span>
			<span style="padding-left:82px;">待付款</span>
			<span style="padding-left:82px;">待评价</span>
			<div class="order_search">
				<input type="text" placeholder="请输入订单号" style="width:204px;height:28px;border:1px solid #d9d9d9;line-height:2;"/>
				<input type="button" class="order_search_button" value="搜索订单"/>
			</div>
		</div>
		<div class="table_head">
			<span style="padding-left:170px;">名称</span>
			<span style="padding-left:110px;">数量</span>
			<span style="padding-left:110px;">单价</span>
			<span style="padding-left:110px;">金额</span>
			<span style="padding-left:94px;">交易状态</span>
			<span style="padding-left:94px;">评价</span>
		</div>
		<div class="table_body">
			<c:set var="customerStoreOrder" value="${pageContext.request.contextPath}/shop/customer/order.html" scope="request"/>
			<jsp:include page="/common/orderDetail.jsp"/>
		</div>
	</div>
	<!-- 浏览记录层 -->
	<div class="account_title"><span>最近浏览过商品</span></div>
	<div class="account_history">
		<div style="margin-top:104px;margin-left:20px;float:left;">
			<a href="#"><img src='<c:url value="/resources/img/left-arr.png" />'/></a>
		</div><div id="my_history">
		<div style="margin-left:22px;float:left;">
		<div style="margin-top:20px;">
			<a href="#"><img style="width:142px;height:142px;" src='<c:url value="/resources/img/prod_img.png" />'/></a>
		</div>
		<div style="margin-top:20px;color:#333;font-size:12px;">平滑培养基</div>
		<div style="margin-top:10px;color:#999;font-size:12px;">￥1728</div>
		</div>
		<div style="margin-left:22px;float:left;">
		<div style="margin-top:20px;">
			<a href="#"><img style="width:142px;height:142px;" src='<c:url value="/resources/img/prod_img.png" />'/></a>
		</div>
		<div style="margin-top:20px;color:#333;font-size:12px;">平滑培养基</div>
		<div style="margin-top:10px;color:#999;font-size:12px;">￥1728</div>
		</div>
		<div style="margin-left:22px;float:left;">
		<div style="margin-top:20px;">
			<a href="#"><img style="width:142px;height:142px;" src='<c:url value="/resources/img/prod_img.png" />'/></a>
		</div>
		<div style="margin-top:20px;color:#333;font-size:12px;">平滑培养基</div>
		<div style="margin-top:10px;color:#999;font-size:12px;">￥1728</div>
		</div>
		<div style="margin-left:22px;float:left;">
		<div style="margin-top:20px;">
			<a href="#"><img style="width:142px;height:142px;" src='<c:url value="/resources/img/prod_img.png" />'/></a>
		</div>
		<div style="margin-top:20px;color:#333;font-size:12px;">平滑培养基</div>
		<div style="margin-top:10px;color:#999;font-size:12px;">￥1728</div>
		</div>
		<div style="margin-left:22px;float:left;">
		<div style="margin-top:20px;">
			<a href="#"><img style="width:142px;height:142px;" src='<c:url value="/resources/img/prod_img.png" />'/></a>
		</div>
		<div style="margin-top:20px;color:#333;font-size:12px;">平滑培养基</div>
		<div style="margin-top:10px;color:#999;font-size:12px;">￥1728</div>
		</div>
		</div>
		<div style="margin-top:104px;margin-left:92px;float:left;">
			<a href="#"><img src='<c:url value="/resources/img/right-arr.png" />'/></a>
		</div>
	</div>
</div>

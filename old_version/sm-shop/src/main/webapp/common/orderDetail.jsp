<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" %>
<%
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
%>

<link href="<c:url value="/resources/css/image/dropify.css" />" rel="stylesheet"></link>
<script src="<c:url value="/resources/js/tools/dropify.js" />"></script>
<script src="<c:url value="/resources/js/ajaxfileupload.js" />"></script>
<%@ page session="false" %>
<style>
.pay_first_order {
    color: red;
}
.ship_first_order {
    color: blue;
}
.adjust_price_affected {
    font-weight: bold;
    color: #23527c;
}

</style>
<script type="text/javascript">
var orderActionUrl="<c:url value='/shop/order/action/'/>";
function doOrderAction(subOrderId, actionName){
	if (actionName == "cancel_order"){
		cancelOrder(subOrderId);
	}else if (actionName == "confirm_receipt"){
		confirmReceiptOrder(subOrderId);
	}else{
		alert("系统目前暂不支持该项操作");
	}
}
function invokeOrderAction(subOrderId, actionName){
	$.ajax({  
		type: 'POST',
		url: orderActionUrl+"actionEvent.html",
		data:"soid="+subOrderId+"&actionName="+actionName,
		dataType:"json",
		success: function(response) {
			if (typeof response.response == 'undefined'){
				alert("操作失败，请重新登录");
				return;
			}
			response = response.response;
			if (response.status == 0){
				location.replace(location.href);
				return;
			}
			if (response.message != null){
				alert("操作失败: "+response.message);
				return;
			}
			if (response.statusMessage != null){
				alert("操作失败: "+response.statusMessage);
				return;
			}
			var errMsg="操作失败。\n  代码"+response.code+", 类型"+response.messageKey+"\n  参数[";
			for(var i=1;i<10;i++){
				if (typeof response['messageParam'+i] == "string"){
					errMsg+=response['messageParam'+i]+",";
				}
			}
			errMsg += "]";
			alert(errMsg);
		} ,
		error: function( textStatus, errorThrown) {
			alert("操作失败，请检查是否有权限操作此订单");
		}
	});
}

function cancelOrder(subOrderId){
	if (!confirm("确认取消订单 "+subOrderId+" 么？")){
		return;
	}
	invokeOrderAction(subOrderId, "cancel_order");
}
function confirmReceiptOrder(subOrderId){
	if (!confirm("确认订单 "+subOrderId+" 已收货么？")){
		return;
	}
	invokeOrderAction(subOrderId, "confirm_receipt");
}
</script>
<!-- From src\main\webapp\common\orderDetail.jsp -->
<table class="table table-striped table-hover">
    <thead style="text-align: center; font-weight: bolder;">
		<tr>
			<td>名称</td>
			<td>数量</td>
			<td>单价</td>
			<td>金额</td>
			<td width="20%" ></td>
			<td>交易状态</td>
			
		</tr>
    </thead>
    <tbody class="table_body">
    <c:forEach items="${customerOrders.ezybioOrder}" var="order" varStatus="orderStatus">
        <c:set var="rawOrder" value="${order.rawOrder}"/>
        <!-- 订单title -->
        <tr style="height:38px;background-color: #d9d9d9;line-height:38px;">
            <td colspan="6" >
				<c:forEach items= "${rawOrder.subOrders}" var="sorder" begin="0" end="0">
					<c:set var="soid" value="${sorder.id}"/>
				</c:forEach>
                <a href="${requestScope.customerStoreOrder}?orderId=${soid}&w=${order.orderID}"
                    <c:if test="${rawOrder.processType=='pay_first'}">
                            class="pay_first_order">
                        <span style="padding-left:24px;"><fmt:formatDate type="both" pattern="yyyy-MM-dd" value="${order.datePurchased}" /></span>
                        <span style="padding-left:32px;">订单类型：</span>
                        <s:message code="order.processType.pay_first" text="收货后付款" />
                    </c:if>
                    <c:if test="${order.rawOrder.processType!='pay_first'}">
                            class="ship_first_order">
                        <span style="padding-left:24px;"><fmt:formatDate type="both" pattern="yyyy-MM-dd" value="${order.datePurchased}" /></span>
                        <span style="padding-left:32px;">订单类型：</span>
                        <s:message code="order.processType.ship_first" text="付款后发货" />
                    </c:if>
                    <c:if test="${rawOrder.subOrders.size() > 1}">
                        <span style="padding-left:32px; color: rgb(33, 154, 65);">(您订单中的商品在不同库房或属不同卖家，故拆分为以下订单分开配送)</span>
                    </c:if>
					<c:if test="${rawOrder.priceType != 'NORMAL'}">
						<span class='adjust_price_affected' style="padding-left:32px;">总金额：<sm:monetary value="${order.total}"/></span>
						<c:set var="orgTotal" value="0.0"/>
						<c:forEach items="${rawOrder.subOrders}" var ="subOrder">
							<c:set var="orgTotal" value="${orgTotal + subOrder.total}"/>
						</c:forEach>
						<s style="color: gray;"><sm:monetary value="${orgTotal}"/></s>
					</c:if>
					<c:if test="${rawOrder.priceType == 'NORMAL'}">
						<span style="padding-left:32px;">总金额：<sm:monetary value="${order.total}"/></span>
					</c:if>
                    
                </a>
            </td>
        </tr>
        <!-- 订单详情 -->
        <c:forEach items="${rawOrder.subOrders}" var ="subOrder">
            <c:forEach items="${subOrder.orderProducts}" var ="orderProduct" varStatus="opState">
                <tr>
                    <td style="padding: 16px;">
                        <c:if test="${not empty orderProduct.productImageUrl}">
                            <img width="98px" height="98px" style="float: left; margin: 0px 16px;" src='<c:url value="${orderProduct.productImageUrl}"/>'/>
                        </c:if>
                        <a  href="<c:url value='/shop/product/' />${orderProduct.productID}.html">
                            <c:if test="${not empty orderProduct.productCode}">
                                ${orderProduct.productCode} <br/>
                            </c:if>
                            ${orderProduct.productName}
                        </a>
                        <c:if test="${not empty orderProduct.specifications }">
                            <br/>
                            <span style="color: rgb(33, 154, 65);">${orderProduct.specifications}</span>
                        </c:if>
                        <c:if test="${not empty orderProduct.productEnName }">
                            <br/><s:message code="label.productedit.productenname" text="English Name"/>&nbsp;:&nbsp;<span >${orderProduct.productEnName}</span>
                        </c:if>
                    </td>
                    <td style="padding: 16px;">${orderProduct.productQuantity}</td>
                    <td style="padding: 16px;"> <!-- 单价-->
                        <c:if test="${not empty orderProduct.finalPrice}">
                            <span class='adjust_price_affected'><sm:monetary value="${orderProduct.finalPrice}"/></span>
                            <br/>
                            <s style="color: gray;"><sm:monetary value="${orderProduct.price}"/></s>
                        </c:if>
                        <c:if test="${empty orderProduct.finalPrice}">
                            <sm:monetary value="${orderProduct.price}"/>
                        </c:if>
                    </td>
                    <td style="padding: 16px;"> <!-- 单条商品小计 -->
                        <c:if test="${not empty orderProduct.finalTotal}">
                            <span class='adjust_price_affected'><sm:monetary value="${orderProduct.finalTotal}"/></span>
                            <br/>
                            <s style="color: gray;"><sm:monetary value="${orderProduct.oneTimeCharge}"/></s>
                        </c:if>
                        <c:if test="${empty orderProduct.finalTotal}">
                            <sm:monetary value="${orderProduct.oneTimeCharge}"/>
                        </c:if>
                    </td>
					<c:if test="${fn:contains(subOrder.actionList,'review_order')}">
						<td style="padding: 16px 0px;">
							<c:choose>
								<c:when test="${not empty orderProduct.productReview}">
									<a class="btn btn-default btn-sm" target="_blank" 
										href="<c:url value="/shop/customer/review.html"/>?productId=${orderProduct.productID}&orderProductId=${orderProduct.id}">
										<s:message code="label.product.view.review" text="View Review"/>
									</a>
								</c:when>
								<c:otherwise>
									<a class="btn btn-warning btn-sm" target="_blank" 
										href="<c:url value="/shop/customer/review.html"/>?productId=${orderProduct.productID}&orderProductId=${orderProduct.id}">
										<s:message code="label.product.rate" text="Review"/><span class="badge" style="margin:2px;padding:3px;font-size:5px;min-width:5px;line-height:5px;">?</span>
									</a>
								</c:otherwise>
							</c:choose>
						</td>
					</c:if>
					<c:if test="${not fn:contains(subOrder.actionList,'review_order')}">
						<c:if test="${opState.index == 0}">
							<td rowspan="${subOrder.orderProducts.size()}" style="padding: 16px 0px;">
								<c:if test="${not empty subOrder.actionList}">
									<c:forEach items="${subOrder.actionList}" var="action" begin="0">
										<c:choose>
											<c:when test="${action == 'upload_pay_proof'}">
												<c:if test="${empty subOrder.payProof}">
													<button class="btn btn-info btn-sm"
														onclick="uploadPaymentProof(${rawOrder.id},${subOrder.id})">
														上传支付凭证
													</button>
												</c:if>
												<c:if test="${not empty subOrder.payProof}">
													<button class="btn btn-info btn-sm"
														onclick="uploadPaymentProof(${rawOrder.id},${subOrder.id}, '<c:url value='/orderstatic/'/>${store.payProof}')">
														更改支付凭证
													</button>
												</c:if>
											</c:when>
											<c:when test="${action == 'review_order'}">
											</c:when>
											<c:otherwise>
												<button class="btn btn-info btn-sm" onclick="doOrderAction(${subOrder.id}, '${action}')"/>
													<s:message code="order.action.${action}" text="${action}" />
												</button>
											</c:otherwise>
										</c:choose>
										&nbsp;
									</c:forEach>
								</c:if>
							</td>
						</c:if>
                    </c:if>
					<c:if test="${opState.index == 0}">
						<td rowspan="${subOrder.orderProducts.size()}" style="padding: 16px 0px;"> <!-- 子订单状态 -->
							<label>订单号: ${subOrder.id}</label><br/>
							<c:if test="${not empty subOrder.payProof}">
								<a href="<c:url value='/orderstatic/'/>${subOrder.payProof}" target="blank">
									<img src="<c:url value='/resources/img/pay_proof.png'/>" height="16px" />
								</a>
								<br/>
								<br/>
							</c:if>
							
							<c:if test="${rawOrder.processType=='pay_first'}">
								<s:message code="order.status.pay_first.${subOrder.status}" text="${status.status}" />&nbsp;
							</c:if>
							<c:if test="${rawOrder.processType!='pay_first'}">
								<s:message code="order.status.ship_first.${subOrder.status}" text="${status.status}" />&nbsp;
							</c:if>
							
							<c:if test="${not empty subOrder.deliveryNumber}">
								<div><a href="<c:url value='http://www.baidu.com/s' >
									<c:param name='wd' value='快递 ${subOrder.deliveryNumber} ${subOrder.deliveryCode}' />
									</c:url>" target="_blank" class="btn-link">查看物流
								</a></div>
							</c:if>

						</td>
                    </c:if>
					
                </tr>
            </c:forEach>
        </c:forEach>
     </c:forEach>
    </tbody>
</table>

<div class="modal fade" id="confirmReceived" tabindex="-1" role="dialog"  data-backdrop= "static" aria-hidden="true">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 id="cartModalLabel"><img src="<c:url value="/resources/img/important-icon.png"/>" width="40"/>&nbsp;<strong id="modeltitle"><s:message code="label.order.btn.confirm" text="Delivery"/></strong></h4>
          </div>
          <div class="modal-footer">
                 <a href="javascript:void(0);" id="ordersubmit" class="btn btn-info" ><strong><s:message code="button.label.submit2" text="Submit"/></strong></a>
                <a href="#" class="btn btn-default" data-dismiss="modal"><strong><s:message code="cart.close" text="Close"/></strong></a>
          </div>
        </div>
      </div>
</div>
<div class="modal fade" id="uploadPaymentProof" tabindex="-1" role="dialog"  data-backdrop= "static" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 id="cartModalLabel"><img src="<c:url value="/resources/img/important-icon.png"/>" width="40"/>&nbsp;
                    <strong id="modeltitle">
                        上传支付凭证
                    </strong>
                </h4>
            </div>
            <form:form method="POST" enctype="multipart/form-data" commandName="product" action="<c:url value='/shop/customer/uploadPayProof.html'/>" class="form-horizontal">
                <div>
                    <input type="hidden" id="input_elem_orderId" name="orderId" />
                    <input type="hidden" id="input_elem_subOrderId" name="subOrderId" />
                    <input type="file" name="image" id="input_elem_image"  class="dropify-event" data-default-file="/sm-shop/resources/img/02.png">
                </div>
                <div class="modal-footer">
                    <a href="javascript:confirmUploadPaymentProof()" class="btn btn-info" ><strong><s:message code="button.label.submit2" text="Submit"/></strong></i>
                    <a href="#" class="btn btn-default" data-dismiss="modal"><strong><s:message code="cart.close" text="Close"/></strong></a>
                </div>
            </form:form>
        </div>
    </div>
</div>
<div class="modal fade" id="confirmUpload" tabindex="-1" role="dialog"  data-backdrop= "static" aria-hidden="true">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 id="cartModalLabel"><img src="<c:url value="/resources/img/important-icon.png"/>" width="40"/>&nbsp;<strong id="modeltitle">确定上传支付凭证么？</strong></h4>
          </div>
          <div class="modal-footer">
                 <a href="javascript:doUploadPaymentProof()" class="btn btn-info" ><strong><s:message code="button.label.submit2" text="Submit"/></strong></a>
                <a href="#" class="btn btn-default" data-dismiss="modal"><strong><s:message code="cart.close" text="Close"/></strong></a>
          </div>
        </div>
      </div>
</div>
<script>
/**
 * upload subOrder pay-proof
 */
var drEvent = $('.dropify-event').dropify();
var selectedImages = {
}
function uploadPaymentProof(oid, soid, imgUrl){
    var dropifyObj = drEvent.get()[0].getDropify();
    //dropifyObj.resetPreview();
    dropifyObj.clearElement();
    //dropifyObj.resetPreview();
    if ( typeof imgUrl == "string"){
        dropifyObj.setPreview(imgUrl);
    }
    $('#input_elem_orderId').val(oid);
    $('#input_elem_subOrderId').val(soid);
    $('#uploadPaymentProof').modal('show');
}
function confirmUploadPaymentProof(){
    $('#uploadPaymentProof').modal('hide');
    $('#confirmUpload').modal('show');
}
function doUploadPaymentProof(){
    $('#confirmUpload').modal('hide');
    saveImage('input_elem_image', $('#input_elem_image').val(), $('#input_elem_orderId').val(), $('#input_elem_subOrderId').val());
}
function saveImage(file,filename,oid,soid){
    $.ajaxFileUpload({
        type: 'POST',
            url: '<c:url value="/shop/customer/uploadPayProof.html"/>',
            fileElementId:file,
            dataType: 'json',
            secureuri : false,
            data:{file:'image',fname:filename,oid:oid,soid:soid},
            success: function(is) {
                if(is.response.status == -2){
                    alert("图片的格式不正常，格式必须为 jpg|jpeg|bmp|gif|png");
                }else if(is.response.status == -1){
                    alert("图片保存失败，请再试一试");
                }else if (is.response.status == 0){
                    location.reload();
                }
            }
        }
    );
}
/**
 * confirm  product received
 */
function confirmReceived(oid, soid){
    $('#confirmReceived').modal('show');
    $('#ordersubmit').bind('click', function(){
        $('#confirmReceived').modal('hide');
        $.ajax({
            type: 'POST',
            url: '<c:url value="/shop/customer/confirmReceived.html"/>',
            data:{
                orderid:oid,
                suborderid:soid
            },
            success: function(response) {
                if(response.response.status==0) {
                    //alertSuccess();
                    alert("success");
                    window.location.href = '<c:url value="/shop/customer/orders.html"/>';
                }else {
                    activeFaild(response.response.statusMessage);
                }
                location.reload();
            } ,
            error: function( textStatus, errorThrown) {
                activeFaild(textStatus);
             }
        });
    });
}
</script>

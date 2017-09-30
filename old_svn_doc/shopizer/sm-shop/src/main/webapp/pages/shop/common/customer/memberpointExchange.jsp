
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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<link
	href="<c:url value="/resources/templates/bootstrap3/css/spinner.css" />"
	rel="stylesheet">
<script
	src="<c:url value="/resources/templates/bootstrap3/js/spinner.js"/>"></script>
<script src="<c:url value="/resources/js/product.js"/>"></script>
<script src="<c:url value="/resources/js/jquery.raty.min.js" />"></script>
<script type="text/javascript">
	function addadress() {
		$("addnew").modal('show');
	};
		
	function check(aid){
	$('#addressId').val(aid);
		$("#adressInfo").html($("#info_"+aid).html());
		$('.collapse').collapse('hide');
	}
	
	function doSubmit(){
		var num =$("#giftQuility").val();
		if(num<=0){
			return ;
		}
		$("#qulityGif").val(num);
		$("#giftId").val(giftsid);
		$("#addressId").val(addressId);
		num=num*curruntPoint;
		if(num>queryByMemberPoints){
			alert("积分不足");
		}else{
			$.ajax({  
				type: 'POST',
				  url: getContextPath() + '/shop/marketpoints/cashPoints.html',
				  data:$("#gifform").serialize(),
				  success: function(defaultdata) {					  	
						if(defaultdata==-2){
							alert("积分不足！");
						}else if(defaultdata==-1){
							alert("兑换失败，请重试或者联系管理员！");
						}else if(defaultdata==0){
							alert("兑换成功！");
							window.location='<c:url value="/shop/marketpoints/list.html" />';
						}
						
					 } ,
				 error: function( textStatus, errorThrown) {
					
					alert("兑换失败，请重试或者联系管理员！"); 
				 }
		 	});
		}
		
	}
	
/*form提交*/	

</script>
<style type="text/css">
	.ad li{width:130px;float: left;}
	.qq li{float:left};
</style>
<div class="modal fade" id="cash">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title"
					style="font-weight ;color:#7a7a7a; left:20px;">商品兑换</h3>
			</div>
			<div class="modal-body">
				<div  id="collapseExample" >
					<div class="well">
						<div>
							<div id="modaltable" >
								
							</div>
						</div>
					</div>
				</div>
				<div class="col-sm-12 col-md-12" style="padding-top:30px;">
					<div class="pull-left" style="padding-right:25px;">
						<s:message code="label.quantity" text="Quantity" />
						:
					</div>
					<div>
						<input type="text" 	class="spinnergif" id="giftQuility" />
					</div>

			</div>
			<form action="${pageContext.request.contextPath}/shop/marketpoints/cashPoints.html" method="post" id="gifform">
			 <input type="hidden" name="giftId" id="giftId" />			 
			 <input type="hidden"  name="addressId" id="addressId"/>
			 <input type="hidden"  name="qulityGif" id="qulityGif"/>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				<button type="button" class="btn btn-primary" onclick="doSubmit()">兑换</button>
			</div>
		</div>
		</form>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
</div>

<script>
	$(document).ready(function() {
		$('.spinnergif').spinner({});
	});
</script>

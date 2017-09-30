<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%> 
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>
<%@ page session="false" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<script src="<c:url value="/resources/js/ajaxfileupload.js" />"></script>
<script>
var url = '<c:url value='/admin/store/doStoreImport.html'/>';
$(function(){
	$("#btnupload").click(function() {
        ajaxFileUpload();
	});
	$('#delcfmModel').on('show.bs.modal', function(event){
		var button = $(event.relatedTarget); // Button that triggered the modal
		var modal = $(this);
		modal.find('.modal-title strong').text('上传确认');
		modal.find('.modal-body p').text('请保证批量导入的商户信息严格按照模板要求编辑，如果确认无误，请点击确定！');
	});
});
function ajaxFileUpload() {
	$("#message strong").text('');
	$("#confirmHref").unbind("click").bind("click",function(){
		// 开始上传文件时显示一个图片
	    $("#wait_loading").show();
	    $.ajaxFileUpload({
	        url: url, 
	        type: 'post',
	        secureuri: false, //一般设置为false
	        fileElementId: 'uploadfile', // 上传文件的id、name属性名
	        dataType: 'json', //返回值类型，一般设置为json、application/json
	        data: {}, //传递参数到服务器
	        success: function(data, status){  
	            if(data.response.status==-1){
		            $("#message strong").text(data.response.statusMessage);
	            } else {
	            	var msg = "";
	            	if(data.response.successlines!=null&&data.response.successlines!='') msg += "上传成功的数据集合：" + data.response.successlines + "<br/>";
	            	if(data.response.errorlines!=null&&data.response.errorlines!='') msg += "上传失败的数据包括：" + data.response.errorlines;
	            	$("#message strong").html(msg);
	            }
	            $("#wait_loading").hide();
	        },
	        error: function(data, status, e){ 
	        	$("#wait_loading").hide();
				//这里处理的是网络异常，返回参数解析异常，DOM操作异常
				alert("上传发生异常");
	        }
	    });
	});
	$('#delcfmModel').modal('show');
    //return false;
}
</script>
<style>
.row {
	margin: 15px 0px;
}
</style>
<jsp:include page="/common/adminTabs.jsp" />
<div style="padding:20px 50px">
	<div>
		<div class="bg-primary" style="border:1px solid #fff;padding-left:15px"><h4>商户信息批量导入说明</h4></div>
		<div style="padding-left:15px;padding-top:10px"><p>请下载<code style="font-size:16px"><a href='<c:url value="/resources/download/merchant-imp.xlsx"/>'>商户信息批量导入模板</a></code>，按照模板格式整理商户信息。
		<br/>在上传的过程中，请勿进行其他操作，根据反馈的信息查看批量导入结果。</p>
		</div>
	</div>
	<div class="center-block" style="padding-top:10px">
		<div class="row">
			<div class="col-md-12 text-center">
				<h2><label id="message" class="bg-danger lead"><strong></strong></label></h2>
			</div>
		</div>
		
		<div class="row">
		  <div class="col-md-6"><input type="file" class="form-control input" name="uploadfile" id="uploadfile" placeholder="上传文件"></div>
		  <div class="col-md-6"><input type="button" id="btnupload" class="btn btn-success form-control" value="提交"></div>
		</div>
	</div>
	<div id="wait_loading" style="padding: 50px 0 0 0;display:none;text-align:center">
        <div style="width: 103px;margin: 0 auto;"><img src='<c:url value="/resources/img/loading.gif"/>'/></div>
        <br></br>
        <div style="width: 103px;margin: 0 auto;"><span id="tips">请稍等...</span></div>
        <br></br>
    </div>
</div>
<jsp:include page="/common/delForm.jsp"/>

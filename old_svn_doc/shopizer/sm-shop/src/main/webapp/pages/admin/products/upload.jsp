<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags"	prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%> 
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>
<%@ page session="false" %>
<%@ page import="java.text.*" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<script src="<c:url value="/resources/js/ajaxfileupload.js" />"></script>
<script SRC="<c:url value="/resources/js/selectList.js" />"></script>
<script>
var url = '<c:url value='/admin/products/upload.html'/>';
$(function(){
	$("#btnupload").click(function() {
        ajaxFileUpload();
	});
	$('#delcfmModel').on('show.bs.modal', function(event){
		var button = $(event.relatedTarget); // Button that triggered the modal
		var modal = $(this);
		modal.find('.modal-title strong').text('上传确认');
		modal.find('.modal-body p').text('请保证上传的产品信息严格按照模板要求编辑，如果确认无误，请点击确定批量上传商品数据！');
	});
	getStores("storeslist.html");
});

function ajaxFileUpload() {
	$("#message strong").text('');
	$("#message").css("display", "none");
	$("#confirmHref").unbind("click").bind("click",function(){
		// 开始上传文件时显示一个图片
		
	    $("#wait_loading").show();
	    var modelVersion=$("#productVersion").html();
	    var updateType= $("select[name=uploadtype]").val();
	    if(updateType==1){
	    	modelVersion=$("#othersVersion").html();
	    }else if(updateType==2){
	    	modelVersion=$("#serveVersion").html();
	    }
	    
	    $.ajaxFileUpload({
	        url: url, 
	        type: 'post',
	        secureuri: false, //一般设置为false
	        fileElementId: 'uploadfile', // 上传文件的id、name属性名
	        dataType: 'json', //返回值类型，一般设置为json、application/json
	        data: {storeId:$("#storenamehidden").val(),
	        	uploadtype:$("select[name=uploadtype]").val(),
	        	modalVersion:modelVersion
	        	}, //传递参数到服务器
	        	
	        success: function(data, status){  
	        	var msg = "";
	            if(data.response.status==-1){
	            	$("#message").css("display", "block");
		            $("#message strong").text(data.response.statusMessage);
	            } else if(data.response.status == 0) {
	            	if(data.response.successlines!=null&&data.response.successlines!='') msg += "上传成功的数据集合：" + data.response.successlines + "<br/>";
	            	if(data.response.errorlines!=null&&data.response.errorlines!='') msg += "上传失败的数据包括：" + data.response.errorlines;
	            	$("#message").css("display", "block");
	            	$("#message strong").html("数据上传成功，请前往<a href='./products.html'>商品目录</a>继续完善产品质量信息");
	            } else if(data.response.status == 1){
	            	//部分成功
	            	msg += "部分数据上传成功，成功的数据请前往<a href='./products.html'>商品目录</a>继续完善产品质量信息<br/>";
	            	msg += "部分数据上传失败，请完善商品数据后继续上传。<br/><span style='color:#ff0000'>失败数据为Excel表格行数：";
	            	var lines;
	            	var info = "";
	            	if(data.response.errorlines!=null&&data.response.errorlines!='') {
	            		var lines = data.response.errorlines.split("##");
	            		$(lines).each(function(i){
	            			var tmp = this.split(";;");
	            			if (tmp[0]!=''){
		            			info += "第" + tmp[0] + "行，";
	            			}
	            		});
	            		
	            		if(info.length>0) info = info.substring(0, info.length-1);
	            		msg += info + "</span>";
	            	}
	            	$("#message").css("display", "block");
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
<%-- <%	String userName = request.setAttribute("uName","aaaa");%> --%>
<jsp:include page="/common/adminTabs.jsp" />
<div style="padding:20px 50px">
	<div>
		<div class="bg-primary" style="border:1px solid #fff;padding-left:15px"><h4>商品信息批量导入说明</h4></div>
		<div style="padding-left:15px;padding-top:10px">
			<p>请下载<code class="bg-danger" style="font-size:16px"><a href='<c:url value="/resources/download/products-imp.xlsx"/>'>试剂类商品批量导入模板<span style="color:red">(最新版本：<span id="productVersion">RVersion:2.2)</span></span></a></code>
		或者<code class="bg-danger" style="font-size:16px"><a href='<c:url value="/resources/download/others-imp.xlsx"/>'>耗材、仪器类商品批量导入模板<span style="color:red">(最新版本：<span id="othersVersion">OVersion:2.2)</span></span></a></code>或者<code class="bg-danger" style="font-size:16px"><a href='<c:url value="/resources/download/services-imp.xlsx"/>'>服务类商品批量导入模板<span style="color:red">(最新版本：<span id="serveVersion">SVersion:2.3)</span></span></a></code>，按照模板格式整理商品信息，建议一次最多只允许导入<code class="bg-danger" style="font-size:18px;color:red">1000</code>条记录。
		<br/>在上传的过程中，请勿进行其他操作，根据反馈的信息查看批量导入结果。</p>
		</div>
	</div>
	<div class="center-block" style="padding-top:10px">
		<div class="row">
			<div class="col-md-12 text-center">
				<div id="message" class="bg-info lead" style="display:none;padding:20px 40px;text-align:left"><strong></strong></div>
			</div>
		</div>
		<div class="row">
		<sec:authorize access="hasRole('ADMIN') and fullyAuthenticated">
			<div class="col-sm-12 ">
			<label>批量上传商品的所属商铺</label>
				<div class="input-group">
					<input type="text" class="form-control typeahead"
						id="searchstorename" readonly 
						value="<c:out value="${store.storename}"/>" />
						<input type="hidden"  name="storeId" id="storenamehidden" value="<c:out value="${store.id}"/>"/>
					<!-- form:select cssClass="form-control" items="${stores}" itemValue="id" itemLabel="storename" path="merchantStore.id"/-->
					<span class="input-group-addon dropdown-toggle" id="sns"
						data-toggle="collapse" aria-haspopup="true"
						href="#collapseExample" aria-expanded="false"
						aria-controls="collapseExample"> <span
						class="glyphicon glyphicon-th " aria-hidden="true"></span>
					</span>
				</div>
				<div class="collapse" id="collapseExample">
					<div class="well" id="storeNameList"></div>
				</div>
				<span class="help-inline"><form:errors path="merchantStore" cssClass="error" /></span>
				</div>
			</sec:authorize>
			<sec:authorize ifNotGranted="ADMIN">
				<div class="col-sm-12 ">
					<label>批量上传商品的所属商铺</label>
					<span >${store.storename}</span>
					<input type="hidden" name="storeId" id="storenamehidden" value='<c:out value="${store.id}"/>'/>
				</div>
			</sec:authorize>			
			<div class="col-md-12">
				<label>选择批量导入数据类型</label>
				<select class="form-control" name="uploadtype">
		  			<option value="0">试剂类商品批量导入模板</option>
		  			<option value="1">耗材、仪器类商品批量导入模板</option>
		  			<option value="2">服务类商品批量导入模板</option>
	       		</select>
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

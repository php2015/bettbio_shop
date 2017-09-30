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

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<script src="<c:url value="/resources/js/jquery.elevateZoom-3.0.8.min.js" />"></script>
<jsp:include page="/resources/js/functions.jsp" />
<script src="<c:url value="/resources/js/batchDataOperation.js"/>"></script>
<script src="<c:url value="/resources/js/jquery.elevateZoom-3.0.8.min.js" />"></script>
<script src="<c:url value="/resources/js/jquery.raty.min.js" />"></script>
<script src="<c:url value="/resources/js/bootstrap-datetimepicker.min.js" />"></script>
<script src="<c:url value="/resources/js/bootstrap-datetimepicker.zh-CN.js" />"></script>
<link href="<c:url value="/resources/css/bootstrap-datetimepicker.min.css" />" rel="stylesheet"></link>

<style>
.process_not_done, .process_doing, .process_done{
	display: inline-block;
	width: 240px;
	height: 40px;
	font-size: 24px;
	color: gray;
	padding: 0px 0px 0px 40px;
}
.process_doing {
	color: blue;
	font-weight: bold;
}
.process_done {
	color: green;
}

.batch_data_operation_step {
	margin: 20px 40px;
	font-size: 16px;
	text-align: center;
	
}

.operation_notes {
	font-size: 14px;
	color: #298156;
}

fieldset  label {
	font-size: 16px;
}

.operation_args_fieldset {
	text-align: left; 
	padding: 5px 20px; 
	display: inline-table;
	vertical-align: top;
}

</style>
<div style="text-align:center; font-size: 20px; color: red;">
注意! 批量数据操作是非常慎重的事情，请认真填写操作参数，并确认填写无误。
</div>
</div>
<div >
<div id="div_process_flow">
	<div id="div_step_1" class="process_doing">选择操作类型 &gt;&gt;</div>
	<div id="div_step_2" class="process_not_done">填写操作参数 &gt;&gt;</div>
	<div id="div_step_3" class="process_not_done">校验操作参数 &gt;&gt;</div>
	<div id="div_step_4" class="process_not_done">执行操作</div>
</div>
<hr/>
<div id="div_select_operation" class="batch_data_operation_step">
	<label>请选择要进行的操作</label>
	<select id="select_operation">
		<option value="invalid-products">商品下架</option>
		<option value="valid-product">商品上架</option>
		<option value="set-diamond">钻石产品操作</option>
		<option value="sql-invalid-products">使用SQL操作商品</option>
		<option value="full-index">全量更新索引</option>
		<option value="batch-remove-product">批量删除商品</option>
	</select>
	<br/>
	<br/>
	<br/>
	<button class="btn btn-lg btn-danger" onclick="BatchDataOp.showProcessStart()">下一步</button>
</div>

<div id="process_invalid-products" class="batch_data_operation_step" style="display:none;">
	您正在批量下架商品<br/>
	<span id="bip_mode_description" class="operation_notes"></span>
	<br/><br/>
	<form id="batch_invalid_products_form">
	<fieldset class="operation_args_fieldset">
		<legend><label>商家名称</label></legend>
		<div>
			<input name="bip_arg_merchant_action" type="radio" value="when" onchange="BatchInvalidProducts.showOptionType()"/><label>下架以下商家</label>
			<input name="bip_arg_merchant_action" type="radio" value="except" checked="checked" onchange="BatchInvalidProducts.showOptionType()"/><label>忽略以下商家</label>
			<br/>
			<textarea id="arg_remove_merchant" cols="40"></textarea><br/>
			<span class="operation_notes">(可以输入多个，换行分开)</span>
		</div>
	</fieldset>
	<fieldset class="operation_args_fieldset">
		<legend><label>品牌名称</label></legend>
		<div>
			<input name="bip_arg_brand_action" type="radio" value="when" checked="checked"/ onchange="BatchInvalidProducts.showOptionType()";><label>下架以下品牌</label>
			<input name="bip_arg_brand_action" type="radio" value="except" onchange="BatchInvalidProducts.showOptionType()"/><label>保留以下品牌</label>
			<br/>
			<textarea id="arg_remove_brands" cols="40"></textarea><br/>
			<span class="operation_notes">(可以输入多个，换行分开)</span>
		</div>
	</fieldset>
	</form>
	<br/>
	<br/>
	<br/>
	<button class="btn btn-lg btn-danger" onclick="BatchDataOp.restartProcess()">上一步</button>
	<button class="btn btn-lg btn-danger" onclick="BatchDataOp.verifyOperationSettings()">下一步</button>
</div>
<div id="check_invalid-products" class="batch_data_operation_step" style="display:none;">
	<div style="width: 365px; margin: 0 auto; text-align: left;">
		<div><label>操作类型: </label> <span id="sumary_biptype"></span></div>
		<div><label>错误: </label> <span id="sumary_error"></span></div>
		<div><label>符合品牌条件的产品总数: </label> <span id="sumary_totalNum"></span></div>
		<div><label>符合下架条件的产品总数: </label>  <span id="sumary_invalidNum"></span></div>
		<br/>
		<div><label>指定商家的产品数: </label></div>
		<div><table border="3" cellspacing="3">
			<tr><th>商家名称</th><th>该品牌的产品数</th></tr>
			<tbody id="sumary_each_merchant"></tbody>
			</table>
		</div>
	</div>
	<br/>
	<button class="btn btn-lg btn-danger" onclick="BatchDataOp.backtoProcessStart()">上一步</button>
	<button class="btn btn-lg btn-danger" onclick="BatchDataOp.executeBatchOperation()">下一步</button>
</div>
<div id="result_invalid-products" class="batch_data_operation_step" style="display:none;">
	<div style="width: 365px; margin: 0 auto; text-align: center;">
		<div><label id="status_title">正在执行</label></div>
		<div style="width: 365px;  height: 35px; background-color: gray; position: relative;">
			<div id="status_percent_bar" style="height: 35px; background-color: blue; position: absolute; top: 0px; left: 0px;"></div>
			<div style="width: 365px; height: 30px; position: absolute; top: 5px; left: 0px; color: yellow;">
				<label id="status_processed">0</label> of <label id="status_total">0</label>
			</div>
		</div>
	</div>
	<br/>
	<button class="btn btn-lg btn-danger" id="bipbtn_close" onclick="BatchDataOp.restartProcess()">结束</button>
</div>
<!---------------------------  SQL主导的操作  ------------------------------>
<div id="process_sql-invalid-products" class="batch_data_operation_step" style="display:none;">
	您正在使用SQL批量下架商品<br/>
	<span id="bip_mode_description" class="operation_notes" >
	注意：<ol>
		<li>需要在一个SQL中构建出过滤产品的所有条件。</li>
		<li>SQL的第一个字段必须是产品ID。&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</li>
		<li>换行不需要反斜线，结束不需要分号。&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</li>
		</ol>
	</span>
	<form id="batch_sql_invalid_products_form">
	<fieldset class="operation_args_fieldset">
		<legend><label>操作类型</label></legend>
		<div>
			<input name="sql_arg_operation" type="radio" value="invalid_product" checked="checked" /><label>下架商品</label><br/>
			<input name="sql_arg_operation" type="radio" value="valid_product" /><label>上架商品</label><br/>
			<input name="sql_arg_operation" type="radio" value="set_diamond" /><label>设置钻石产品</label><br/>
			<input name="sql_arg_operation" type="radio" value="unset_diamond" /><label>取消钻石产品</label><br/>
			<%-- <input name="sql_arg_operation" type="radio" value="delete_product" /><label>删除商品</label><br/> --%>
		</div>
	</fieldset>
	<fieldset class="operation_args_fieldset">
		<legend><label>SQL语句</label></legend>
		<div>
			<textarea id="sql_arg_statement" cols="80" rows="8">
select p.PRODUCT_ID 
from PRODUCT p
where p.PRODUCT_ID='12345'
			</textarea><br/>
		</div>
	</fieldset>
	
	</form>
	<br/>
	<br/>
	<br/>
	<button class="btn btn-lg btn-danger" onclick="BatchDataOp.restartProcess()">上一步</button>
	<button class="btn btn-lg btn-danger" onclick="BatchDataOp.verifyOperationSettings()">下一步</button>
</div>
<div id="check_sql-invalid-products" class="batch_data_operation_step" style="display:none;">
	<div style="width: 365px; margin: 0 auto; text-align: left;">
		<div><label>操作类型: </label> <span id="sql_sumary_optype"></span></div>
		<div><label>错误: </label> <span id="sql_sumary_error"></span></div>
		<div><label>符合条件的产品总数: </label> <span id="sql_sumary_totalNum"></span></div>
		<div style="text-align:left; margin: 0 auto;"><label>异常信息: </label></div>
		<br/>
	</div>
	<div style="text-align:left; margin: 0 auto;"><pre id="sql_sumary_exception"></pre></div>
	<br/>
	<button class="btn btn-lg btn-danger" onclick="BatchDataOp.backtoProcessStart()">上一步</button>
	<button class="btn btn-lg btn-danger" onclick="BatchDataOp.executeBatchOperation()">下一步</button>
</div>
<div id="result_sql-invalid-products" class="batch_data_operation_step" style="display:none;">
	<div style="width: 365px; margin: 0 auto; text-align: center;">
		<div><label id="sql_status_title">正在执行</label></div>
		<div style="width: 365px;  height: 35px; background-color: gray; position: relative;">
			<div id="sql_status_percent_bar" style="height: 35px; background-color: blue; position: absolute; top: 0px; left: 0px;"></div>
			<div style="width: 365px; height: 30px; position: absolute; top: 5px; left: 0px; color: yellow;">
				<label id="sql_status_processed">0</label> of <label id="sql_status_total">0</label>
			</div>
		</div>
	</div>
	<br/>
	<button class="btn btn-lg btn-danger" id="sqlbtn_close" onclick="BatchDataOp.restartProcess()">结束</button>
</div>
<!---------------------------  钻石产品操作  ------------------------------>
<div id="process_set-diamond" class="batch_data_operation_step" style="display:none;">
	您正在批量设置钻石产品<br/>
	<span id="bsd_mode_description" class="operation_notes"></span>
	<br/><br/>
	<form id="batch_set_diamond_form">
	<fieldset class="operation_args_fieldset">
		<legend><label>操作类型</label></legend>
		<div><input name="bsd_arg_isDiamond" type="radio" value="true" checked="checked" /><label>设置钻石产品</label></div>
		<div><input name="bsd_arg_isDiamond" type="radio" value="false" /><label>取消钻石产品</label></div>
	</fieldset>
	<fieldset class="operation_args_fieldset">
		<legend><label>商家名称</label></legend>
		<div>
			<textarea id="bsd_arg_merchants" cols="40"></textarea><br/>
			<span class="operation_notes">(可以输入多个，换行分开)</span>
		</div>
	</fieldset>
	<fieldset class="operation_args_fieldset">
		<legend><label>品牌名称</label></legend>
		<div>
			<textarea id="bsd_arg_brands" cols="40"></textarea><br/>
			<span class="operation_notes">(可以输入多个，换行分开)</span>
		</div>
	</fieldset>
	</form>
	<br/>
	<br/>
	<br/>
	<button class="btn btn-lg btn-danger" onclick="BatchDataOp.restartProcess()">上一步</button>
	<button class="btn btn-lg btn-danger" onclick="BatchDataOp.verifyOperationSettings()">下一步</button>
</div>
<div id="check_set-diamond" class="batch_data_operation_step" style="display:none;">
	<div style="width: 365px; margin: 0 auto; text-align: left;">
		<div><label>操作类型: </label> <span id="bsd_sumary_optype"></span></div>
		<div><label>错误: </label> <span id="bsd_sumary_error"></span></div>
		<div><label>符合条件的产品总数: </label> <span id="bsd_sumary_totalNum"></span></div>
		<br/>
		<div><label>指定商家的产品数: </label></div>
		<div><table border="3" cellspacing="3">
			<tr><th>商家名称</th><th>该商家的产品数</th></tr>
			<tbody id="bsd_sumary_each_merchant"></tbody>
			</table>
		</div>
	</div>
	<br/>
	<button class="btn btn-lg btn-danger" onclick="BatchDataOp.backtoProcessStart()">上一步</button>
	<button class="btn btn-lg btn-danger" onclick="BatchDataOp.executeBatchOperation()">下一步</button>
</div>
<div id="result_set-diamond" class="batch_data_operation_step" style="display:none;">
	<div style="width: 365px; margin: 0 auto; text-align: center;">
		<div><label id="bsd_status_title">正在执行</label></div>
		<div style="width: 365px;  height: 35px; background-color: gray; position: relative;">
			<div id="bsd_status_percent_bar" style="height: 35px; background-color: blue; position: absolute; top: 0px; left: 0px;"></div>
			<div style="width: 365px; height: 30px; position: absolute; top: 5px; left: 0px; color: yellow;">
				<label id="bsd_status_processed">0</label> of <label id="bsd_status_total">0</label>
			</div>
		</div>
	</div>
	<br/>
	<button class="btn btn-lg btn-danger" id="bsdbtn_close" onclick="BatchDataOp.restartProcess()">结束</button>
</div>
<!---------------------------  批量上架产品  ------------------------------>
<div id="process_valid-product" class="batch_data_operation_step" style="display:none;">
	您正在批量上架产品<br/>
	<span id="bvp_mode_description" class="operation_notes"></span>
	<br/><br/>
	<form id="batch_valid_product_form">
	<fieldset class="operation_args_fieldset">
		<legend><label>商家名称</label></legend>
		<div>
			<textarea id="bvp_arg_merchants" cols="40"></textarea><br/>
			<span class="operation_notes">(可以输入多个，换行分开)</span>
		</div>
	</fieldset>
	<fieldset class="operation_args_fieldset">
		<legend><label>品牌名称</label></legend>
		<div>
			<textarea id="bvp_arg_brands" cols="40"></textarea><br/>
			<span class="operation_notes">(可以输入多个，换行分开)</span>
		</div>
	</fieldset>
	</form>
	<br/>
	<br/>
	<br/>
	<button class="btn btn-lg btn-danger" onclick="BatchDataOp.restartProcess()">上一步</button>
	<button class="btn btn-lg btn-danger" onclick="BatchDataOp.verifyOperationSettings()">下一步</button>
</div>
<div id="check_valid-product" class="batch_data_operation_step" style="display:none;">
	<div style="width: 365px; margin: 0 auto; text-align: left;">
		<div><label>操作类型: </label> <span id="bvp_sumary_optype"></span></div>
		<div><label>错误: </label> <span id="bvp_sumary_error"></span></div>
		<div><label>符合条件的产品总数: </label> <span id="bvp_sumary_totalNum"></span></div>
		<br/>
		<div><label>指定商家的产品数: </label></div>
		<div><table border="3" cellspacing="3">
			<tr><th>商家名称</th><th>该商家的产品数</th></tr>
			<tbody id="bvp_sumary_each_merchant"></tbody>
			</table>
		</div>
	</div>
	<br/>
	<button class="btn btn-lg btn-danger" onclick="BatchDataOp.backtoProcessStart()">上一步</button>
	<button class="btn btn-lg btn-danger" onclick="BatchDataOp.executeBatchOperation()">下一步</button>
</div>
<div id="result_valid-product" class="batch_data_operation_step" style="display:none;">
	<div style="width: 365px; margin: 0 auto; text-align: center;">
		<div><label id="bvp_status_title">正在执行</label></div>
		<div style="width: 365px; text-align: left;">
			<div>已处理：<label id="bvp_status_indexed"></label></div>
			<div>有错误：<label id="bvp_status_failed"></label></div>
			<div>被忽略：<label id="bvp_status_ignored"></label></div>
			<div>已经运行时间：<label id="bvp_status_runtime"></label></div>
			<div>预计剩余时间：<label id="bvp_status_lefttime"></label></div>
		</div>
		<div style="width: 365px;  height: 35px; background-color: gray; position: relative;">
			<div id="bvp_status_percent_bar" style="height: 35px; background-color: blue; position: absolute; top: 0px; left: 0px;"></div>
			<div style="width: 365px; height: 30px; position: absolute; top: 5px; left: 0px; color: yellow;">
				<label id="bvp_status_processed">0</label> of <label id="bvp_status_total">0</label>
			</div>
		</div>
	</div>
	<br/>
	<button class="btn btn-lg btn-danger" id="bvp_close" onclick="BatchDataOp.restartProcess()">完成</button>
</div>
<!---------------------------  全量更新索引  ------------------------------>
<div id="process_full-index" class="batch_data_operation_step" style="display:none;">
	您正在全量更新索引<br/>
	<span id="bip_mode_description" class="operation_notes" >
	本操作期间，网站功能将会因为索引数据不全而有异常。请确定在维护窗口期进行本操作。
	</span>
	
	<br/>
	<br/>
	<button class="btn btn-lg btn-danger" onclick="BatchDataOp.restartProcess()">上一步</button>
	<button class="btn btn-lg btn-danger" onclick="BatchDataOp.verifyOperationSettings()">下一步</button>
</div>
<div id="check_full-index" class="batch_data_operation_step" style="display:none;">
	<div style="width: 365px; margin: 0 auto; text-align: left;">
		<div><label>产品总数: </label> <span id="fullindex_sumary_totalNum"></span></div>
		<div><label>预计时间: </label> <span id="fullindex_sumary_estime"></span></div>
		<div style="text-align:left; margin: 0 auto;"><label>错误信息: </label></div>
		<br/>
	</div>
	<div style="text-align:left; margin: 0 auto;"><pre id="fullindex_sumary_exception"></pre></div>
	<br/>
	<button class="btn btn-lg btn-danger" onclick="BatchDataOp.backtoProcessStart()">上一步</button>
	<button class="btn btn-lg btn-danger" onclick="BatchDataOp.executeBatchOperation()">下一步</button>
</div>
<div id="result_full-index" class="batch_data_operation_step" style="display:none;">
	<div style="width: 365px; margin: 0 auto; text-align: center;">
		<div><label id="fullindex_status_title">正在执行</label></div>
		<div style="width: 365px; text-align: left;">
			<div>已处理：<label id="fullindex_status_indexed"></label></div>
			<div>有错误：<label id="fullindex_status_failed"></label></div>
			<div>被忽略：<label id="fullindex_status_ignored"></label></div>
			<div>已经运行时间：<label id="fullindex_status_runtime"></label></div>
			<div>预计剩余时间：<label id="fullindex_status_lefttime"></label></div>
		</div>
		<div style="width: 365px;  height: 35px; background-color: gray; position: relative;">
			<div id="fullindex_status_percent_bar" style="height: 35px; background-color: blue; position: absolute; top: 0px; left: 0px;"></div>
			<div style="width: 365px; height: 30px; position: absolute; top: 5px; left: 0px; color: yellow;">
				<label id="fullindex_status_processed">0</label> of <label id="fullindex_status_total">0</label>
			</div>
		</div>
	</div>
	<br/>
	<button class="btn btn-lg btn-danger" id="fullindexbtn_close" onclick="BatchDataOp.restartProcess()">结束</button>
</div>

<!---------------------------  批量删除商品操作  ------------------------------>
<div id="process_batch-remove-product" class="batch_data_operation_step" style="display:none;">
	您正在使用SQL批量删除商品<br/>
	<div style="width: 365px; margin: 0 auto; text-align: left;">
		<span id="brp_mode_description" class="operation_notes" >
		注意：<ol>
			<li>需要在一个SQL中构建出过滤产品的所有条件。</li>
			<li>SQL的第一个字段必须是产品ID。</li>
			<li>换行不需要反斜线，结束不需要分号。</li>
			</ol>
		</span>
	</div>
	<div>
		<label style="width: 100px;">使用过的SQL</label>
		<select id="brp_select_usedSql" onchange="BatchRemoveProduct.fillWithUsedSql()">
			<option value="1">查询所有下架的商品</option>
		</select>
	</div>
	<hr/>
	<form id="batch_batch-remove-product">
		<div>
			<label style="width: 100px;">SQL语句说明</label>
			<input id="brp_arg_statement_note" style="width:700px;"></input>
		</div>
		<div style="display: inline-table;">
			<label style="width: 100px;vertical-align:top;">SQL语句</label>
			<textarea id="brp_arg_statement" style="width:700px; vertical-align:top;" rows="8">
select p.PRODUCT_ID 
from PRODUCT p
where p.AVALIABLE=0 limit 10
			</textarea>
		</div>
		
	
	</form>
	<br/>
	<br/>
	<br/>
	<button class="btn btn-lg btn-danger" onclick="BatchDataOp.restartProcess()">上一步</button>
	<button class="btn btn-lg btn-danger" onclick="BatchDataOp.verifyOperationSettings()">下一步</button>
</div>
<div id="check_batch-remove-product" class="batch_data_operation_step" style="display:none;">
	<div style="width: 365px; margin: 0 auto; text-align: left;">
		<div><label>操作类型: </label> <span id="brp_sumary_optype">批量删除商品</span></div>
		<div><label>符合条件的产品总数: </label> <span id="brp_sumary_totalNum"></span></div>
		<div style="text-align:left; margin: 0 auto;"><label>异常信息: </label></div>
		<br/>
	</div>
	<div style="text-align:left; margin: 0 auto;"><pre id="brp_sumary_exception"></pre></div>
	<br/>
	<button class="btn btn-lg btn-danger" onclick="BatchDataOp.backtoProcessStart()">上一步</button>
	<button class="btn btn-lg btn-danger" onclick="BatchDataOp.executeBatchOperation()">下一步</button>
</div>
<div id="result_batch-remove-product" class="batch_data_operation_step" style="display:none;">
	<div style="width: 565px; margin: 0 auto; text-align: left;">
		<div><label>当前使用的SQL: </label> <span id="brp_status_sql"></span></div>
		<div><label>开始时间: </label> <span id="brp_status_startTime"></span></div>
		<div><label>结束时间: </label> <span id="brp_status_endTime"></span></div>
		<div><label>正在运行: </label> <span id="brp_status_running"></span></div>
		<div><label>计划总数: </label> <span id="brp_status_total"></span></div>
		<div><label>已发起删除数: </label> <span id="brp_status_scheduled"></span></div>
		<div><label>已完成删除数: </label> <span id="brp_status_deleted"></span></div>
	</div>
	<br/>
	<button class="btn btn-lg btn-info" id="brpbtn_status" onclick="BatchRemoveProduct.queryStatistics()">刷新统计数据</button>
	<button class="btn btn-lg btn-info" id="brpbtn_stop" onclick="BatchRemoveProduct.stopCurrentJob()">终止当前任务</button>
	
	<button class="btn btn-lg btn-danger" id="brpbtn_close" onclick="BatchDataOp.restartProcess()">结束</button>
</div>


<!-- below are fixed infomation -->
<div id="debug_div" style="display:none;"></div>
<div id="modal-loading" class="modal fade" > 
	<div style="width:500px; margin: 100px auto; background-color: rgba(255,255,255,1); border:1px solid #000; padding:15px; text-align:center; z-index: 1100;">
		<div>正在从后台查询数据，请稍侯。。。</div>
		<div> <img src="<c:url value='/resources/img/loading.gif'/>"/> </div>
	</div>
</div>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>		

<style>
.config_table {
	width: 80%;
	padding: 20px auto;
}
td span {
	padding-right: 20px;
}
.changed {
	background-color: red;
	color: white;
	font-size: 16px;
}
</style>
<script type="text/javascript">
var SC = new function(){
	var me = this;
	var presetConfig = {
		"default":{
			emailOp1:"true",
			certifyRegisterCustomer:"false",
			activeRegisterCustomer:"false",
			orderNofifyForceCustomerCurrentSales:"true",
			adminSearchProductWay:"true"
		},
		"exhibition":{
			emailOp1:"false",
			certifyRegisterCustomer:"true",
			activeRegisterCustomer:"true",
			adminSearchProductWay:"true"
		}
	}
	
	function updateChangeState(elemObj, newValue){
		var tdObj = $(elemObj).parent().prev().prev();
		var oldValue = $(tdObj).data("oldvalue");
		//alert("new: "+typeof newValue+", old: "+typeof oldValue);
		if (String(oldValue) == String(newValue)){
			return;
		}
		tdObj.addClass("changed");
	}
	function setPresetValue(values){
		$("table tbody tr td").removeClass("changed");
		//alert(JSON.stringify(values));
		for(var varName in values){
			var elemName = "configuration." + varName;
			var newValue = values[varName];
			var inputElem = $("input[name='"+elemName+"']")[0];
			updateChangeState(inputElem, newValue);
			if(inputElem.type == "radio"){
				$("input[name='"+elemName+"'][value='"+newValue+"']").prop("checked", true);
			}else{
				$(inputElem).val(newValue);
			}
		}
	}
	me.setValuesForExhibition = function(){
		setPresetValue(presetConfig["exhibition"]);
	}
	me.setValuesForDefault = function(){
		setPresetValue(presetConfig["default"]);
	}
}
</script>
<div class="tabbable" >

  					
 	<jsp:include page="/common/adminTabs.jsp" />
  	<br/>
							
	<c:url var="saveConfiguration" value="/admin/configuration/saveSpecialConfiguration.html"/>
	<form:form method="POST" commandName="configuration" action="${saveConfiguration}" class="form-horizontal">
		<table class="config_table table table table-striped table-bordered table-hover">
			<thead>
				<tr>
					<th>参数名称</th><th>参数说明</th><th>参数值</th>
				</tr>
			</thead>
			<tbody>
				<!-- 订单邮件1 -->
				<tr> 
					<td data-oldValue="${configuration.emailOp1}">emailOp1</td>
					<td>是否在买家提交订单时，给商家发送一封提醒邮件。</td>
					<td>
						<input type="radio" name="configuration.emailOp1" value="true" <c:if test="${configuration.emailOp1 == 'true'}">checked='checked'</c:if>></input>
						<span>是</span>
						<input type="radio" name="configuration.emailOp1" value="false"<c:if test="${configuration.emailOp1 == 'false'}">checked='checked'</c:if>></input>
						<span>否</span>
					</td>
				</tr>
				<!-- 自动成为认证买家 -->
				<tr> 
					<td data-oldValue="${configuration.certifyRegisterCustomer}">certifyRegisterCustomer</td>
					<td>买家注册时，是否自动成为认证用户</td>
					<td>
						<input type="radio" name="configuration.certifyRegisterCustomer" value="true" <c:if test="${configuration.certifyRegisterCustomer == 'true'}">checked='checked'</c:if>></input>
						<span>买家注册时，自动被认证</span>
						<input type="radio" name="configuration.certifyRegisterCustomer" value="false"<c:if test="${configuration.certifyRegisterCustomer == 'false'}">checked='checked'</c:if>></input>
						<span>买家注册后，需要工作人员人工认证，然后更新认证标志</span>
					</td>
				</tr>
				<!-- 自动激活买家 -->
				<tr> 
					<td data-oldValue="${configuration.activeRegisterCustomer}">activeRegisterCustomer</td>
					<td>买家注册时，是否自动激活</td>
					<td>
						<input type="radio" name="configuration.activeRegisterCustomer" value="true" <c:if test="${configuration.activeRegisterCustomer == 'true'}">checked='checked'</c:if>></input>
						<span>买家注册时，自动激活账号</span>
						<input type="radio" name="configuration.activeRegisterCustomer" value="false"<c:if test="${configuration.activeRegisterCustomer == 'false'}">checked='checked'</c:if>></input>
						<span>买家注册后，需要通过邮件链接来激活账号</span>
					</td>
				</tr>
				<!-- 是否强制使用用户最新的销售人员信息 -->
				<tr> 
					<td data-oldValue="${configuration.orderNofifyForceCustomerCurrentSales}">orderNofifyForceCustomerCurrentSales</td>
					<td>订单状态变化时，如何决定通知哪个销售人员</td>
					<td>
						<input type="radio" name="configuration.orderNofifyForceCustomerCurrentSales" value="true" <c:if test="${configuration.orderNofifyForceCustomerCurrentSales == 'true'}">checked='checked'</c:if>></input>
						<span>每次都通知用户当前指定的销售人员</span>
						<input type="radio" name="configuration.orderNofifyForceCustomerCurrentSales" value="false"<c:if test="${configuration.orderNofifyForceCustomerCurrentSales == 'false'}">checked='checked'</c:if>></input>
						<span>订单发生变化时，通知下单时指定的销售人员</span>
					</td>
				</tr>
				<!-- 平台采购的邮件地址 -->
				<tr> 
					<td data-oldValue="${configuration.purchasingAgentEmail}">purchasingAgentEmail</td>
					<td>订单通知邮件所使用的平台采购人员邮件地址</td>
					<td>
						<input name="configuration.purchasingAgentEmail" value="${configuration.purchasingAgentEmail}"></input>
					</td>
				</tr>	
				<!-- 平台采购的手机号码 -->
				<tr> 
					<td data-oldValue="${configuration.purchasingAgentMobile}">purchasingAgentMobile</td>
					<td>订单通知邮件所使用的平台采购人员手机号码</td>
					<td>
						<input name="configuration.purchasingAgentMobile" value="${configuration.purchasingAgentMobile}"></input>
					</td>
				</tr>
				<!-- 管理界面搜索产品的方法 -->
				<tr> 
					<td data-oldValue="${configuration.adminSearchProductWay}">adminSearchProductWay</td>
					<td>管理员搜索产品的方法</td>
					<td>
						<input type="radio" name="configuration.adminSearchProductWay" value="true" <c:if test="${configuration.adminSearchProductWay == 'true'}">checked='checked'</c:if>></input>
						<span>使用JDBC查询</span>
						<input type="radio" name="configuration.adminSearchProductWay" value="false"<c:if test="${configuration.adminSearchProductWay == 'false'}">checked='checked'</c:if>></input>
						<span>使用JPQL查询</span>
					</td>
				</tr>	
			</tbody>
		</table>
      								
	                  				   
		<div class="form-actions">
			<div class="pull-left">
				<button type="button" class="btn btn-info" onclick="SC.setValuesForDefault()">默认值</button>
				<button type="button" class="btn btn-info" onclick="SC.setValuesForExhibition()">展会模式</button>
			</div>
			<div class="pull-right">
				<button type="reset" class="btn btn-success" onclick="$('table tbody tr td').removeClass('changed');return true;"><s:message code="button.label.reset" text="Submit"/></button>
				<button type="submit" class="btn btn-success"><s:message code="button.label.submit" text="Submit"/></button>
			</div>
		</div>
					                  

	</form:form>

</div>
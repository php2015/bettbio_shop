<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ page session="false"%>
  <script SRC="<c:url value="/resources/js/selectList.js" />"></script>


<script type="text/javascript">
	$(function() {
		if ($("#adminName").val() == "") {
			$('.btn').addClass('disabled');
		}
		getStores();
	});

	function validateCode() {
		$('#checkCodeStatus').html(
				'<img src="<c:url value="/resources/img/ajax-loader.gif" />');
		$('#checkCodeStatus').show();
		var adminName = $("#adminName").val();
		var id = $("#id").val();
		checkCode(adminName, id,
				'<c:url value="/admin/users/checkUserCode.html" />');
	}

	function callBackCheckCode(msg, code) {

		if (code == 0) {
			$('.btn').removeClass('disabled');
		}
		if (code == 9999) {

			$('#checkCodeStatus')
					.html(
							'<font color="green"><s:message code="message.code.available" text="This code is available"/></font>');
			$('#checkCodeStatus').show();
			$('.btn').removeClass('disabled');
		}
		if (code == 9998) {

			$('#checkCodeStatus')
					.html(
							'<font color="red"><s:message code="message.code.exist" text="This code already exist"/></font>');
			$('#checkCodeStatus').show();
			$('.btn').addClass('disabled');
		}

	}
</script>



<jsp:include page="/common/adminTabs.jsp" />
<div class="row">

	<c:url var="userSave" value="/admin/users/save.html" />


	<form:form method="POST" commandName="user" action="${userSave}"
		class="form-horizontal">
		<form:errors path="*" cssClass="alert alert-danger" element="div" />
		<div id="store.success" class="alert alert-success"
			style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>">
			<s:message code="message.success" text="Request successfull" />
		</div>
	<form:input type="hidden" path="defaultLanguage.id" value="${user.defaultLanguage.id}" />
		
		<div class="form-group form-group-lg">
			<label class="col-sm-2 control-label"><s:message
					code="label.generic.username" text="User name" /></label>
			<div class="col-sm-10">
				<form:input cssClass="form-control" path="adminName"
					onblur="validateCode()" />
				<span class="help-inline"><div id="checkCodeStatus"
						style="display:none;"></div>
					<form:errors path="adminName" cssClass="error" /></span>
			</div>
		</div>

		<div class="form-group form-group-lg">
			<label class="col-sm-2 control-label"><s:message
					code="label.store.title" text="Store" /> </label>
			<sec:authorize access="hasRole('ADMIN') and fullyAuthenticated">
			<div class="col-sm-10 ">
				<div class="input-group">
					<input type="text" class="form-control typeahead"
						id="searchstorename" readonly
						value="<c:out value="${user.merchantStore.storename}"/>" />
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
				<div class="col-sm-10 ">
					<span style="font-size:18px;line-height:46px;height:46px;padding:10px 16px;">${user.merchantStore.storename}</span>
					<input type="hidden" name="searchstorename" value='<c:out value="${user.merchantStore.storename}"/>'/>
				</div>
			</sec:authorize>
			</div>

					<div class="form-group form-group-lg">
						<label class="col-sm-2 control-label"><s:message
								code="label.generic.email" text="Email" /></label>
						<div class="col-sm-10">
							<form:input cssClass="form-control" path="adminEmail" onkeyup="value=value.replace( /(^\s*)|(\s*$)/g,'')" onafterpaste="value=value.replace(/(^\s*)|(\s*$)/g,'')"/>
							<span class="help-inline"><form:errors path="adminEmail"
									cssClass="error" /></span>
						</div>
					</div>
					<div class="form-group form-group-lg">
						<label class="col-sm-2 control-label"><s:message
								code="label.generic.firstname" text="First name" /> </label>
						<div class="col-sm-10">
							<form:input cssClass="form-control" path="firstName" />
							<span class="help-inline"><form:errors path="firstName"
									cssClass="error" /></span>
						</div>
					</div>
					<c:if test="${user.id==null || user.id==0}">
						<div class="form-group form-group-lg">
							<label class="col-sm-2 control-label"><s:message
									code="label.user.password" text="Password" /></label>
							<div class="col-sm-10">
								<form:password cssClass="form-control" path="adminPassword" />
								<span class="help-inline"><form:errors
										path="adminPassword" cssClass="error" /></span>
							</div>

						</div>
					</c:if>

					<sec:authorize access="hasRole('ADMIN') and fullyAuthenticated">
						<div class="form-group form-group-lg">
							<label class="col-sm-2 control-label"><strong></strong>
							<s:message code="label.entity.active" text="Active" /></strong></label>
							<div class="col-sm-10">
								<form:checkbox cssClass="form-control" path="active" />
							</div>
						</div>
					</sec:authorize>
					<sec:authorize access="hasRole('ADMIN') and fullyAuthenticated">

						<div class="form-group form-group-lg">
							<label class="col-sm-2 control-label"><s:message
									code="label.groups.title" text="Groups" /></label>
							<div class="col-sm-10">
								<form:checkboxes cssClass="form-control" items="${groups}"
									itemValue="id" itemLabel="groupName" path="groups" />
								<span class="help-inline"><form:errors path="groups"
										cssClass="error" /></span>
							</div>
						</div>
					</sec:authorize>
					<form:hidden path="id" />
					<form:input type="hidden" path="merchantStore.id" name="storename"
						id="storenamehidden" />
					<c:if test="${user.id!=null && user.id>0}">
						<form:hidden path="adminPassword" />
					</c:if>

					<div class="form-actions">

						<div class="pull-right">
							<button type="submit" class="btn btn-success">
								<s:message code="button.label.submit" text="Submit" />
							</button>
						</div>

					</div>
	</form:form>


</div>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>
<script type="text/javascript">
var priceFormatMessage = '<s:message code="message.price.cents" text="Wrong format" />';
</script>
<style>
.row {
	margin: 15px 0px;
}
</style>
<link href="<c:url value="/resources/css/bootstrap/css/datepicker.css" />" rel="stylesheet"></link>
<script src="<c:url value="/resources/js/bootstrap/bootstrap-datepicker.js" />"></script>			
<script src="<c:url value="/resources/js/jquery.formatCurrency-1.4.0.js" />"></script>
<script src="<c:url value="/resources/js/jquery.alphanumeric.pack.js" />"></script>
<script src="<c:url value="/resources/js/adminFunctions.js" />"></script>
<script type="text/javascript">
	$(function(){
			$('#productSpecialPriceAmount').blur(function() {
				$('#help-price').html(null);
				$(this).formatCurrency({ roundToDecimalPlace: 2, eventOnDecimalsEntered: true, symbol: ''});
			})
			.keyup(function(e) {
					var e = window.event || e;
					var keyUnicode = e.charCode || e.keyCode;
					if (e !== undefined) {
						switch (keyUnicode) {
							case 16: break; // Shift
							case 17: break; // Ctrl
							case 18: break; // Alt
							case 27: this.value = ''; break; // Esc: clear entry
							case 35: break; // End
							case 36: break; // Home
							case 37: break; // cursor left
							case 38: break; // cursor up
							case 39: break; // cursor right
							case 40: break; // cursor down
							case 78: break; // N (Opera 9.63+ maps the "." from the number key section to the "N" key too!) (See: http://unixpapa.com/js/key.html search for ". Del")
							case 110: break; // . number block (Opera 9.63+ maps the "." from the number block to the "N" key (78) !!!)
							case 190: break; // .
							default: $(this).formatCurrency({ colorize: true, negativeFormat: '-%s%n', roundToDecimalPlace: -1, eventOnDecimalsEntered: true, symbol: ''});
						}
					}
				})
			.bind('decimalsEntered', function(e, cents) {
				if (String(cents).length > 2) {
					var errorMsg = priceFormatMessage + ' (0.' + cents + ')';
					$('#help-special-price').html(errorMsg);
				}
			});
			$('#productSpecialPriceAmount').numeric({allow:"."});
	});
</script>
<c:url var="saveProductPrice" value="/admin/products/price/save.html" />
<c:set value="${product.id}" var="productId" scope="request"/>
<div class="row">

	<div id="showloading"></div>
	<!-- begin middle main content -->
	<div id="p-title">
		<h3>
			<s:message code="label.product.price" text="Product price" />
		</h3>
		  <a class="btn btn-default" href="<c:url value="/admin/products/prices.html?id=${product.id}"/>"><s:message code="button.label.returnlist" text="Return List" /></a>
		<c:if test="${price.price!=null&&price.price.id!=null}">
			<a class="btn btn-success" href="<c:url value="/admin/products/price/create.html?productId=${product.id}"/>"><s:message code="label.product.price.create" text="Create price" /></a>	
		</c:if>
		<br/>

		<!-- begin edit block -->
		<form:form method="POST" commandName="price"
			action="${saveProductPrice}" cssClass="form-horizontal">
			<form:errors path="*" cssClass="alert alert-danger" element="div" />
			<div id="store.success" class="alert alert-success"
				style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>">
				<s:message code="message.success" text="Request successfull" />
			</div>
			
			<div class="row">
				<div class="col-sm-6">
					<label class="required"><s:message
						code="label.product.price" text="Price" /></label>
					<form:input id="productPriceAmount" cssClass="form-control highlight" path="priceText"/>
					<span id="help-price" class="help-inline"><form:errors
							path="priceText" cssClass="error" /></span>
				</div>
				<div class="col-sm-6">
					<label><s:message code="label.product.price.default"
						text="Default price" /></label><br/>
					<form:checkbox path="price.defaultPrice" />
				</div>
			</div>
			
			<c:forEach items="${price.descriptions}" var="description" varStatus="counter">
				<div class="row">
					<div class="col-sm-6">
						<label class="required"><s:message
							code="label.product.price.name" text="Product price name" /> </label>
						<form:input cssClass="form-control highlight"
							id="name${counter.index}"
							path="descriptions[${counter.index}].name" />
						<span class="help-inline"><form:errors
								path="descriptions[${counter.index}].name" cssClass="error" /></span>
					</div>
					<div class="col-sm-6">
						<label><s:message code="label.product.price.code"
						text="Product price code" /></label>
						<form:input cssClass="highlight form-control" path="price.code" />
						<span class="help-inline"><form:errors path="price.code"
								cssClass="error" /></span>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<label><s:message code="label.product.price.period"
								text="Product price period" /></label>
						<form:input cssClass="form-control" path="price.productPricePeriod" />
					</div>
					<div class="col-sm-6">
						<label class="required"><s:message
							code="label.product.price.special" text="Special price" /></label>
						<form:input id="productSpecialPriceAmount" cssClass="form-control"
							path="specialPriceText" />
						<span id="help-special-price" class="help-inline"><form:errors
								path="specialPriceText" cssClass="error" /></span>
					</div>
				</div>
				<form:hidden path="descriptions[${counter.index}].language.id" />
				<form:hidden path="descriptions[${counter.index}].language.code" />
				<form:hidden path="descriptions[${counter.index}].id" />
			</c:forEach>
			
			<div class="row">
				<div style="width:50%;float:left;padding:0px 15px;">
					<label><s:message
						code="label.product.price.special.startdate"
						text="Special start date" /></label>
					<input name="productPriceSpecialStartDate"
						id="productPriceSpecialStartDate"
						value="${price.productPriceSpecialStartDate}" class="form-control"
						type="text" data-datepicker="datepicker" data-date-format="yyyy-MM-dd"> <span
						class="help-inline"><form:errors
							path="productPriceSpecialStartDate" cssClass="error" /></span>
				</div>
				<div style="width:50%;float:left;padding:0px 15px;">
					<label><s:message code="label.product.price.special.enddate"
						text="Special end date" /></label>
					<input name="productPriceSpecialEndDate"
						id="productPriceSpecialEndDate"
						value="${price.productPriceSpecialEndDate}" class="form-control"
						type="text" data-datepicker="datepicker" data-date-format="yyyy-MM-dd"> 
						<span class="help-inline"><form:errors
							path="productPriceSpecialEndDate" cssClass="error" /></span>
				</div>
			</div>	
			
			<!-- default one time -->
			<form:hidden path="price.productPriceType" />
			<form:hidden path="price.id" />
			<form:hidden path="productAvailability.region" />
			<form:hidden path="productAvailability.id" />
			<form:hidden path="product.id" />
			<div class="row">
				<div class="pull-right" style="padding-right:20px;">
					<button type="submit" class="btn btn-primary">
						<s:message code="button.label.submit" text="Submit" />
					</button>
				</div>
			</div>
		</form:form>
		<!-- end edit block -->
	</div>
	<!-- end middle main content -->
</div>

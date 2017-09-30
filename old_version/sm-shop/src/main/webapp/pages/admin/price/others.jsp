<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>	
<script type="text/javascript">
var priceFormatMessage = '<s:message code="message.price.cents" text="Wrong format" />';
</script>

<script src="<c:url value="/resources/js/jquery.formatCurrency-1.4.0.js" />"></script>
<script src="<c:url value="/resources/js/jquery.alphanumeric.pack.js" />"></script>			
				
<script>
	$(document).ready(function() {
	
	});

</script>

<div class="tabbable">
	<jsp:include page="/common/adminTabs.jsp" />
  	<div class="tab-content">
		<div class="tab-pane active" id="others_price_div">
			<div class="sm-ui-component">	
				<h3><s:message code="menu.price-others" text="Other Discounts" /></h3>	
				<br/>
				<div>
					目前仅支持品牌折扣。登录后用户将会享受此折扣。
					<br/>
					更多价格策略，请联系管理员。
				</div>
		</div>
	</div>
</div>   
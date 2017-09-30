<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %> 

<%@ page session="false" %>

<script>

function getContextPath() {
   return "${pageContext.request.contextPath}";
}

function getMerchantStore() {
   return "${requestScope.MERCHANT_STORE.id}";
}

function getMerchantStoreCode() {
   return "${requestScope.MERCHANT_STORE.code}";
}

function getLanguageCode() {
   return "${requestScope.LANGUAGE.code}";
}

function getItemLabel(quantity) {
	var labelItem = '<s:message code="label.generic.item" text="item" />';
	if (quantity > 1) {
		labelItem = '<s:message code="label.generic.items" text="items" />';
	}
	return labelItem;
}

function getLoginErrorLabel() {
	return '<s:message code="message.username.password" text="Login Failed. Username or Password is incorrect."/>';
}
function getFreezeErrorLabel() {
	return '<s:message code="message.username.freezed" text="Login Failed. Freezed"/>';
}
function emptyCartLabel(){
	
	$("#cartMessage").html('<s:message code="cart.empty" text="Your Shopping cart is empty" />');	
	$("#cartinfo").html('');
	$('#shoppingcart').hide();
	$('#cartMessage').show();
}

function getInvalidEmailMessage() {
	return '<s:message code="messages.invalid.email" text="Invalid email address"/>';
}

function getInvalidUserNameMessage() {
	return '<s:message code="registration.username.length.invalid" text="User name must be at least 6 characters long"/>';
}

function getInvalidPhoneMessage() {
	return '<s:message code="messages.invalid.phone" text="Phone num must be at least 11 characters long"/>';
}

function getInvalidPasswordMessage() {
	return '<s:message code="message.password.length" text="Password must be at least 6 characters long"/>';
}

function getWeekPasswordMessage() {
	return '<s:message code="newpassword.not.week" text="Password is too week"/>';
}

function getInvalidCheckPasswordMessage() {
	return '<s:message code="message.password.checkpassword.identical" text="Both password must match"/>';
}

function cartInfoLabel(cart){
	 var labelItem = getItemLabel(cart.quantity);
	 <!-- A configuration is required to display quantity and price -->
	 <c:choose>
	 <c:when test="${requestScope.CONFIGS['displayFullMiniCartInfo'] == true}">
	 $("#cartinfo").html('<span id="cartqty" class="badge">' + cart.quantity + '</span>&nbsp;<span id="cartprice">' + cart.total + '</span>');
	 </c:when>
	 <c:otherwise>
	 $("#cartinfo").html('<span id="cartqty" class="badge">' + cart.quantity + '</span>');
	 </c:otherwise>
	 </c:choose>
}

function cartSubTotal(cart) {
	return '<div class="pull-right"><font class="total-box-label"><s:message code="order.total.total" text="Sub-total" /> : <font class="total-box-price"><strong><span id="checkout-total">' + cart.total + '</span></strong></font></font></div>';
}
function isPC() //函数：获取尺寸 
{ 
	var winWidth = 0; 
	//获取窗口宽度 
	if (window.innerWidth) 
		winWidth = window.innerWidth; 
	else if ((document.body) && (document.body.clientWidth)) 
		winWidth = document.body.clientWidth; 
	
	//通过深入Document内部对body进行检测，获取窗口大小 
	if (document.documentElement && document.documentElement.clientHeight && document.documentElement.clientWidth) 
	{ 
		winWidth = document.documentElement.clientWidth; 
	} 
	
	if (winWidth>768) {
		return true;
	} else return false;
}
function GetUrlRelativePath()
{
	var url = document.location.toString();
	var arrUrl = url.split("//");
	var start = arrUrl[1].indexOf("/");
	var relUrl = arrUrl[1].substring(start);
	if(relUrl.indexOf("?") != -1){
	relUrl = relUrl.split("?")[0];
	}
	var basePath = '${pageContext.request.scheme}'+'://'+'${pageContext.request.serverName}'+':'+ '${pageContext.request.serverPort}';
	start=relUrl.indexOf("/m/");
	relUrl = relUrl.substring(start+2);
	return basePath+'/sm-shop'+relUrl;
}

</script>
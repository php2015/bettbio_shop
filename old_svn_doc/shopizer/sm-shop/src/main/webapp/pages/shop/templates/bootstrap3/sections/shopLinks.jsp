<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>

	<!-- required common scripts -->
    <jsp:include page="/resources/js/functions.jsp" />
    <script src="<c:url value="/resources/js/jquery-1.10.2.min.js" />"></script>
    <script src="<c:url value="/resources/js/json2.js" />"></script>
    <script src="<c:url value="/resources/js/jquery-cookie.js" />"></script>
    <script src="<c:url value="/resources/js/shopping-cart.js" />"></script>
    <script src="<c:url value="/resources/js/login.js" />"></script>
    <script src="<c:url value="/resources/js/jquery.showLoading.min.js" />"></script>
    <script src="<c:url value="/resources/js/hogan.js" />"></script>
    
    	<!-- specific css -->
    	<!-- do only change the list of css files -->
    	<!-- ////////////// -->
    
        <!--<link href='http://fonts.googleapis.com/css?family=Lato:100' rel='stylesheet' type='text/css'>-->
  

		<!-- WEB FONTS -->
		<link href="<c:url value="/resources/templates/bootstrap3/css/css.css" />" rel="stylesheet" type="text/css">
		
		<!-- CORE CSS -->
		<link href="<c:url value="/resources/templates/bootstrap3/css/bootstrap.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/css/font-awesome/css/font-awesome.min.css" />" rel="stylesheet" type="text/css">

		<!--  Theme -->
		<link href="<c:url value="/resources/templates/bootstrap3/css/theme.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/templates/bootstrap3/css/orange.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/templates/bootstrap3/css/essentials.css" />" rel="stylesheet">

    
    	<!-- generic and common css file -->
    	<link href="<c:url value="/resources/css/sm.css" />" rel="stylesheet">
    	<link href="<c:url value="/resources/css/showLoading.css" />" rel="stylesheet">
    	<style>

</style>
    
    	<!-- ////////////// -->

    <!-- mini shopping cart template -->
    <script type="text/html" id="miniShoppingCartTemplate">
		{{#shoppingCartItems}}
			<tr id="{{productId}}" class="cart-product">
				<td>
			{{#image}}
					<img width="30" src="<c:url value="/"/>{{image}}">
			{{/image}}
			{{^image}}
					&nbsp
			{{/image}}
				</td>
				<td>{{name}}</td>
				<td>{{quantity}}</td>
				<td>{{specs}}</td>
				<td>{{price}}</td>
				<td><button productid="{{productId}}" class="close removeProductIcon" onclick="removeItemFromMinicart('{{id}}')">x</button></td>
			</tr>
		{{/shoppingCartItems}}
	</script>
	
	
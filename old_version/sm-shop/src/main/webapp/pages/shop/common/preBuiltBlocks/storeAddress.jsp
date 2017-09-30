<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>



 <address> 
 		<!-- div itemscope itemtype="http://schema.org/Organization"--> 
 		<br/>
 	<strong><span><c:out value="${requestScope.MERCHANT_STORE.storename}"/></span></strong>  <br/>
 	<!-- div itemprop="address" itemscope itemtype="http://schema.org/PostalAddress"--> 
 	<span ><c:out value="${requestScope.MERCHANT_STORE.storeaddress}"/> <c:out value="${requestScope.MERCHANT_STORE.storecity}"/></span>
 	<span ><c:choose><c:when test="${not empty requestScope.MERCHANT_STORE.storestateprovince}"><c:out value="${requestScope.MERCHANT_STORE.storestateprovince}"/></c:when><c:otherwise><script>$.ajax({url: "<c:url value="/shop/reference/zoneName"/>",type: "GET",data: "zoneCode=${requestScope.MERCHANT_STORE.zone.code}",success: function(data){$('#storeZoneName').html(data)}})</script><span id="storeZoneName"><c:out value="${requestScope.MERCHANT_STORE.zone.code}"/></span></c:otherwise></c:choose>,
 	<span ><script>$.ajax({url: "<c:url value="/shop/reference/countryName"/>",type: "GET",data: "countryCode=${requestScope.MERCHANT_STORE.country.isoCode}",success: function(data){$('#storeCountryName').html(data)}})</script></span></span>
 	<span ><c:out value="${requestScope.MERCHANT_STORE.storepostalcode}"/></span>
 	<span ><s:message code="label.generic.phone" text="Phone" /></abbr>: <span itemprop="telephone"><c:out value="${requestScope.MERCHANT_STORE.storephone}"/></span>
 	<!-- /div>
 	</div-->
 </address> 

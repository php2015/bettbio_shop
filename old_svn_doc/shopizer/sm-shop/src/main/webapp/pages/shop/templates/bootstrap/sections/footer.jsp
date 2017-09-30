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
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %> 
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

	  <div style="overflow:auto; height:50px; overflow-y: hidden;">
	  <table width="100%" border="0" style="background:#000000;" >	  
	  <tr style="height:15px;">
	    <td valign="middle" >
	    	<c:if test="${requestScope.CONFIGS['displayStoreAddress'] == true}"> 
	    		<font color="#ffffff"><jsp:include page="/pages/shop/common/preBuiltBlocks/storeAddress.jsp"/></font>
	    	</c:if>   
	    </td>
	    <td align="right" valign="middle"><font color="#ffffff"> &copy;&nbsp;<s:message code="label.generic.providedby" /><a href="http://http://121.40.61.42:8080/sm-shop/shop" class="footer-href" target="_blank">EzyBio</a>
	    	<c:if test="${requestScope.CONFIGS['displayContactUs']==true}">
	    		<a href="<c:url value="/shop/store/contactus.html"/>" class=""><s:message code="label.customer.contactus" text="Contact us"/></a>
	       </c:if></font>
	    </td>		   
	  </tr>	  
	</table>
	</div>
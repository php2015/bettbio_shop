<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>  

<%@ page session="false" %>




<div style="height:100%">


				<jsp:include page="/common/adminTabs.jsp" />
					<iframe id="frameHeight" width="100%" frameborder='0' scrolling='no' marginheight='0' marginwidth='0' src='http://yuntu.amap.com/share/7vaU7j'></iframe>
	
				
		
</div>

<script>


$(document).ready(function() {
	var imageHeight = $(window).height();
	imageHeight = imageHeight-250;	
	 $("#frameHeight").height(imageHeight);  
	//$('#frameHeight').css({'height':imageHeight});
	
});
</script>
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

	  
     <div  class="navbar-fixed-bottom" >
     	<div class="container-fluid">
     	<div class="row">
     		<div class="col-xs-3 customer-background text-center" style="padding:3px 0px;">
   				<a href="<c:url value="/"/>">
						  <span aria-hidden="true" class="carticon fa fa-home fa-2x icon icon-style" style="background: rgba(255,255,255,0.3);width:38px;height:38px;"></span>
						  </a>
			 </div>
				 <div class="col-xs-3 customer-background text-center" style="padding:3px 0px;">
				 		<a href="#" onclick="displayMiniCart();">
					 	<span aria-hidden="true" class="carticon fa fa-shopping-cart fa-2x icon icon-style" style="background: rgba(255,255,255,0.3);width:38px;height:38px;">					 	
					 	</span><span class="badge" id="footBadge" style="position:absolute; z-index:10;"></span>
					  </a>
				  </div>
				   <div class="col-xs-3 customer-background text-center" style="padding:3px 0px;">
				   		<sec:authorize access="hasRole('AUTH_CUSTOMER') and fullyAuthenticated">									
						<c:if test="${requestScope.CUSTOMER!=null}">
								<a href='<c:url value="/shop/customer/dashboard.html" />'>
					 	<span aria-hidden="true" class="carticon fa fa-user fa-2x icon icon-style" style="background: rgba(255,255,255,0.3);width:38px;height:38px;"></span>
					  </a>
						</c:if>
					</sec:authorize>
					<sec:authorize access="!hasRole('AUTH_CUSTOMER') and fullyAuthenticated">
						
						<a  href="javascript:void(0);"   onclick=""> <span aria-hidden="true" class="carticon fa fa-user fa-2x icon icon-style" style="background: rgba(255,255,255,0.3);"></span>
						</a>
					</sec:authorize>
					<sec:authorize access="!hasRole('AUTH_CUSTOMER') and !fullyAuthenticated">
						<a href="#" 	 onclick="longinPage()"><span class="carticon fa fa-user fa-2x icon icon-style" style="background: rgba(255,255,255,0.3);width:38px;height:38px;"></span>
						</a>
					</sec:authorize>				   
				  </div>
				  <div class="col-xs-3 customer-background text-center" style="padding:3px 0px;">
				   <a href="javascript:scroll(0,0)">
						  <span aria-hidden="true" class="carticon fa fa-arrow-up fa-2x icon icon-style" style="background: rgba(255,255,255,0.3);width:38px;height:38px;"></span>
						  </a>
				  </div>				  
     		</div>
     	</div>
     </div>
     <script>

(function() {
	displayfootCart();
})();
</script>
<!-- QiDianDA -->
<script type="text/javascript">
(function(w, a, m){m='__qq_qidian_da';w[m]=a;w[a]=w[a]||function(){(w[a][m]=w[a][m]||[]).push(arguments);};})(window,'qidianDA');
qidianDA('create', '2852132394', '4eaaf0647379b54d722daaba0fecd39f', {
    mtaId: '500384701'
});
qidianDA('set', 't1', new Date());
</script>
<script async src="//bqq.gtimg.com/da/i.js"></script>
<!-- End QiDianDA -->
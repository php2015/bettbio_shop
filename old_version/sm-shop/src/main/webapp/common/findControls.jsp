<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<div class="pull-left" id="showpagedata">
	
			<div style="padding-top:10px;color:#FF6C00">
			<c:choose>
						<c:when test="${not empty requestScope.paginationData}">
							<s:message code="label.entitylist.paging"
		       arguments="${(requestScope.paginationData.offset)};${requestScope.paginationData.countByPage};${requestScope.paginationData.totalCount}"
		       htmlEscape="false"
		       argumentSeparator=";" text=""/>
						</c:when>
						<c:otherwise>
								<s:message code="label.entitylist.paging"  arguments="0;0;0"   htmlEscape="false"   argumentSeparator=";" text=""/>
						</c:otherwise>
 			</c:choose>
		<script>
			cuurentPage=${requestScope.paginationData.currentPage};
		</script>
		 </div>
	
</div>
<ul class="nav nav-pills pull-right form-inline">
	<sec:authorize access="hasRole('ADMIN') and fullyAuthenticated">
		<li><a href="javascript:void(0);" onclick="deleteall()"><s:message code="label.generic.remove" text="Delete"/><s:message code="label.product.list.all" text="All"/></a></li>
		<li><a href="javascript:void(0);" onclick="deletes()"><s:message code="label.generic.remove" text="Delete"/><s:message code="label.product.list.selecte" text="Selected"/></a></li>
	</sec:authorize>
				 <li >
				 		<form:form method="post" action="${requestScope.findAction}" id="ezybioForm" commandName="criteria">
							<fieldset>
								<div class="input-group" style="padding-top:3px;width:250px">  
					      			<form:input path="findName" cssClass="form-control "  id="findName"/>
					     			 <a  href="javascript:void(0);" onclick="findByName()" class="input-group-addon" ><span class="glyphicon glyphicon-search" aria-hidden="true"  ></span></a>
					     			 <input type="hidden" name="page" id="page" value="1"/>
					      			 <form:input type="hidden" path="avaiable" name="avaiable" id="avaiable"/>
				    			</div>
							</fieldset>
						</form:form>   
				</li>
</ul>
<div class="modal fade" id="showprogress" tabindex="-1" role="dialog"  aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
      </div>
      <div class="modal-body">  
        <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 2%;" id="precent">0% </div>  
      </div>
    </div>
  </div>
</div>
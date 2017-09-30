<%
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
	<div class="container-fluid">
		<div class="row">
			<div class="col-sm-12">
				<table class="table table-bordered table-condensed table-striped" style="font-size:12px;">
				<c:if test="${reviews!=null}">
					<c:forEach items="${reviews}" var="review" varStatus="status">
						<tr>
							<td>
								<div class="row">
									<div class="col-sm-12">
										<div id="productRating<c:out value="${status.count}"/>" style="width: 160px!important;"></div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-12">
										<blockquote>
											<div style="color:#000"><c:out value="${review.description}" escapeXml="false" /></div>
											<small><c:out value="${review.customer.hidenick}" />&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${review.customer.project}" />&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${review.date}" /></small>
	 									</blockquote>
 	 									<script>
										  	$(function() {
												$('#productRating<c:out value="${status.count}"/>').raty({ 
													readOnly: true, 
													half: true,
													path : '<c:url value="/resources/img/stars/"/>',
													score: <c:out value="${review.rating}" />
												});
										  	});
						  			   </script>
									</div>
								</div>
							</td>
						</tr>
					</c:forEach>
				</c:if>						
				</table>
				<c:if test="${reviews==null}">
					暂无评论
				</c:if>
			</div>
		</div>	
		
	</div>	


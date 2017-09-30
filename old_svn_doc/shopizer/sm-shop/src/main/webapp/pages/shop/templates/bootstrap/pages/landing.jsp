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
 <script src="<c:url value="/resources/templates/bootstrap/js/bootstrap-carousel.js" />"></script>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
 <script type="text/javascript">
$(document).ready(function() { 	
	$('#myCarousel').carousel({
  interval: 2000
});
});

</script>

			
			<div id="myCarousel" class="carousel slide" data-ride="carousel" data-interval="2000">			   
			   <ol class="carousel-indicators"  style="display: none;">
			      <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
			      <li data-target="#myCarousel" data-slide-to="1"></li>
			      <li data-target="#myCarousel" data-slide-to="2"></li>
			      <li data-target="#myCarousel" data-slide-to="3"></li>
			      <li data-target="#myCarousel" data-slide-to="4"></li>
			   </ol>   			   
			   <div class="carousel-inner">
			      <div class="item active">
			      <img src="<c:url value="/resources/ad/a.jpg" />" style="width:100%;height:240px" alt="First slide"/>			      	
			      </div>
			      <div class="item">
				      
				         				 <img src="<c:url value="/resources/ad/b.jpg" />" style="width:100%;height:240px" alt="Second slide"/>
				         		
			      </div>
			      <div class="item">
			      		 
				         				 <img src="<c:url value="/resources/ad/c.jpg" />" style="width:100%;height:240px" alt="Third slide"/>
				         		
			      </div>
			      <div class="item">
			      		
				         				 <img src="<c:url value="/resources/ad/d.jpg" />" style="width:100%;height:240px" alt="Forth slide"/>
				         
			      </div>
			      <div class="item">
					     
						         				  <img src="<c:url value="/resources/ad/e.jpg" />" style="width:100%;height:240px" alt="Fifth slide"/>
			        
			      </div>
			   </div>			  
			   <a class="carousel-control left" href="#myCarousel" 
			      data-slide="prev">&lsaquo;</a>
			   <a class="carousel-control right" href="#myCarousel" 
			      data-slide="next">&rsaquo;</a>
			</div> 

			<table width="100%" frame="vsides" style="margin-top: -20px;"	class="product-list-table">
				<tr>
					<td id="TOP_Reagent"><c:set var="topCategory"
							value="${requestScope.TOP_Reagent}" />
							<c:if test="${topCategory != null}">
								<div class="dropdown dropup" style="z-index:9999;">
									<a	href="<c:url value="/shop/category/${topCategory.description.seUrl}.html"/>" class="btn-transparent dropdown-toggle " data-toggle="dropdown"
										data-hover="dropdown" data-delay="1000" data-close-others="false" style="background-color:transparent;"> <img src="<c:url value="/resources/ad/${topCategory.code}.png" />" />
										<br> ${topCategory.description.name}
									</a>	
									<c:if	test="${topCategory.categories != null && fn:length(topCategory.categories)>0 }">
										<div class="dropdown-menu" style="z-index:9999;">
										<c:forEach items="${topCategory.categories}" var="sonCategory">
											<table style="width:620px;">
												<tr>
													<td ><div class="second_menu"><a href="#"
														onClick="javascript:location.href='<c:url value="/shop/category/${sonCategory.description.seUrl}.html"/>';"
														role="button" data-toggle="modal" style="color: #0080c0;">${sonCategory.description.name}</a></div>
													</td>
													<c:if	test="${sonCategory.categories != null && fn:length(sonCategory.categories)>0 }">
														<td style="width:500px">
														<ul style="list-style:none;">
														<c:forEach items="${sonCategory.categories}" var="thirdCategory" varStatus="status">																																		
															<li class="ezy_menu_li"><a href="#" 
															 onClick="javascript:location.href='<c:url value="/shop/category/${thirdCategory.description.seUrl}.html"/>';" role="button" data-toggle="modal">${thirdCategory.descinfo}</a>
															 </li>
														</c:forEach>
													</ul>	
														</td>	
													</c:if>
												</tr>
											</table>
										</c:forEach>
										</div>
									</c:if>									
								</div>				
							</c:if>
						</td>	
						<td id="TOP_Instrument"><c:set var="topInstrument"
							value="${requestScope.TOP_Instrument}" />
							<c:if test="${topInstrument != null}">
								<div class="dropdown dropup">
									<a	href="<c:url value="/shop/category/${topInstrument.description.seUrl}.html"/>" class="btn-transparent dropdown-toggle " data-toggle="dropdown"
										data-hover="dropdown" data-delay="1000" data-close-others="false" style="background-color:transparent;"> <img src="<c:url value="/resources/ad/${topInstrument.code}.png" />" />
										<br> ${topInstrument.description.name}
									</a>	
									<c:if	test="${topInstrument.categories != null && fn:length(topInstrument.categories)>0 }">
										<div class="dropdown-menu">
										<c:forEach items="${topInstrument.categories}" var="sonCategory">
											<table>
												<tr>
													<td><div class="second_menu"><a href="#"
														onClick="javascript:location.href='<c:url value="/shop/category/${sonCategory.description.seUrl}.html"/>';"
														role="button"data-toggle="modal" style="color: #0080c0;">${sonCategory.description.name}</a></div>
													</td>
													<c:if	test="${sonCategory.categories != null && fn:length(sonCategory.categories)>0 }">
														<c:forEach items="${sonCategory.categories}" var="thirdCategory">
															<td>															
															<a href="#"
																onClick="javascript:location.href='<c:url value="/shop/category/${thirdCategory.description.seUrl}.html"/>';"
																role="button" data-toggle="modal">|&nbsp;${thirdCategory.descinfo}</a>															
															</td>	
														</c:forEach>
													</c:if>
												</tr>
											</table>
										</c:forEach>
										</div>
									</c:if>									
								</div>				
							</c:if>
						</td>	
						<td id="TOP_Supplies"><c:set var="topSupplies"
							value="${requestScope.TOP_Supplies}" />
							<c:if test="${topSupplies != null}">
								<div class="dropdown dropup">
									<a	href="<c:url value="/shop/category/${topSupplies.description.seUrl}.html"/>" class="btn-transparent dropdown-toggle " data-toggle="dropdown"
										data-hover="dropdown" data-delay="1000" data-close-others="false" style="background-color:transparent;"> <img src="<c:url value="/resources/ad/${topSupplies.code}.png" />" />
										<br> ${topSupplies.description.name}
									</a>	
									<c:if	test="${topSupplies.categories != null && fn:length(topSupplies.categories)>0 }">
										<div class="dropdown-menu">
										<c:forEach items="${topOthers.categories}" var="sonCategory">
											<table>
												<tr>
													<td><div class="second_menu"><a href="#"
														onClick="javascript:location.href='<c:url value="/shop/category/${sonCategory.description.seUrl}.html"/>';"
														role="button" data-toggle="modal" style="color: #0080c0;">${sonCategory.description.name}</a></div>
													</td>
													<c:if	test="${sonCategory.categories != null && fn:length(sonCategory.categories)>0 }">
														<c:forEach items="${sonCategory.categories}" var="thirdCategory">
															<td>															
															<a href="#"
																onClick="javascript:location.href='<c:url value="/shop/category/${thirdCategory.description.seUrl}.html"/>';"
																role="button" data-toggle="modal">|&nbsp;${thirdCategory.descinfo}</a>															
															</td>	
														</c:forEach>
													</c:if>
												</tr>
											</table>
										</c:forEach>
										</div>
									</c:if>									
								</div>				
							</c:if>
						</td>	
						<td id="TOP_Others" width="100px"><c:set var="topOthers"
							value="${requestScope.TOP_Others}" />
							<c:if test="${topOthers != null}">
								<div class="dropdown dropup">
									<a	href="<c:url value="/shop/category/${topOthers.description.seUrl}.html"/>" class="btn-transparent dropdown-toggle " data-toggle="dropdown"
										data-hover="dropdown" data-delay="1000" data-close-others="false" style="background-color:transparent;"> <img src="<c:url value="/resources/ad/${topOthers.code}.png" />" />
										<br> ${topOthers.description.name}
									</a>	
									<c:if	test="${topOthers.categories != null && fn:length(topOthers.categories)>0 }">
										<div class="dropdown-menu">
										<c:forEach items="${topOthers.categories}" var="sonCategory">
											<table>
												<tr>
													<td><div class="second_menu"><a href="#"
														onClick="javascript:location.href='<c:url value="/shop/category/${sonCategory.description.seUrl}.html"/>';"
														role="button" data-toggle="modal" style="color: #0080c0;">${sonCategory.description.name}</a></div>
													</td>
													<c:if	test="${sonCategory.categories != null && fn:length(sonCategory.categories)>0 }">
														<c:forEach items="${sonCategory.categories}" var="thirdCategory">
															<td>															
															<a href="#"
																onClick="javascript:location.href='<c:url value="/shop/category/${thirdCategory.description.seUrl}.html"/>';"
																role="button" data-toggle="modal">${thirdCategory.descinfo}</a>															
															</td>	
														</c:forEach>
													</c:if>
												</tr>
											</table>
										</c:forEach>
										</div>
									</c:if>									
								</div>				
							</c:if>
						</td>	
							</tr>
				</table> 
		
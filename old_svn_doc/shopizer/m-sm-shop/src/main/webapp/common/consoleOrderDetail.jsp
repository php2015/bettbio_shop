<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page session="false" %>
					<c:forEach items="${customerOrders.ezybioOrder}" var="order" varStatus="orderStatus">
							<tr class="order-detail-title" ><!-- order -->
								<td colspan="5" style="padding-left: 5px; margin-left: 0px;font-weight:bold;background-color:#F1F5FB"><a href="${requestScope.customerStoreOrder}?orderId=${order.orderID}"><fmt:formatDate type="date" pattern="yyyy-MM-dd HH:mm:ss" dateStyle="default" value="${order.datePurchased}" /> &nbsp;<s:message code="menu.order" text="Order"/><s:message code="menu.order.no" text="No"/>:
								${order.orderID}&nbsp;<s:message code="menu.order" text="Order"/><s:message code="label.entity.status" text="Status"/>:<s:message code="label.order.${order.orderStatus}" text="label [${order.orderStatus}] not found"/>&nbsp;<s:message code="label.customer.shipping.firstname" text="Name"/>:${order.billName}</a></td>
							</tr>
							<c:forEach items="${order.subOrders}" var ="store">
								<c:forEach items="${store.cartItems}" var ="product" begin="0" end="0">
								<tr><!-- order -->
									<td width="59%" style="word-break: break-all; word-wrap:break-word;"><a  href="<c:url value="/shop/product/" />${product.friendlyUrl}.html<sm:breadcrumbParam/>">${product.name}</a>
									<c:if test="${not empty product.enName }">
										<br/><s:message code="label.productedit.productenname" text="English Name"/>&nbsp;:&nbsp;<span >${product.enName}</span>
									</c:if>
									</td>
										<td width="7%"><sm:monetary value="${product.price}"/></td>
										<td width="7%">${product.quantity}</td>
										<td width="7%"><sm:monetary value="${product.subTotal}"/></td>
										<td width="20%"rowspan="${store.cartItems.size()}">
									<s:message code="label.subtotal" text="Store"/>:<sm:monetary value="${store.total}"/><br>
									<span id="status_${store.id}">
										<s:message code="menu.order" text="Order"/><s:message code="label.entity.status" text="Status"/>:<s:message code="label.order.${store.status}" text="label [${order.orderStatus}] not found"/>
										<c:if test="${store.status=='ORDERED' }">
											<c:if test="${store.cartItems.size()>1}">
												<br>
													<a href="javascript:void(0);" onclick="showSplit(${store.id})"><s:message code="label.order.split" text="Spilt"/></a>
											</c:if>
												<br>
											<a href="javascript:void(0);" onclick="doDelivery(${store.id})"><s:message code="label.order.delivery" text="Delivery"/></a>
										</c:if>
									</span>
									</td>	
								</tr>
								</c:forEach>
								<c:forEach items="${store.cartItems}" var ="product" begin="1">
									<tr>
										<td width="59%"><a href="<c:url value="/shop/product/" />${product.friendlyUrl}.html<sm:breadcrumbParam/>">${product.name}</a>
											<c:if test="${not empty product.enName }">
												<br/><s:message code="label.productedit.productenname" text="English Name"/>&nbsp;:&nbsp;<span>${product.enName}</span>
											</c:if>
										</td>
										<td width="7%"><sm:monetary value="${product.price}"/></td>
										<td width="7%">${product.quantity}</td>
										<td width="7%"><sm:monetary value="${product.subTotal}"/></td>																				
									</tr>
								</c:forEach>
							</c:forEach>
						</c:forEach>
						
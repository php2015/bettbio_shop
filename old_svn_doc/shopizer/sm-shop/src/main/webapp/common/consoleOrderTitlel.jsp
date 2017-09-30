<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>
					<thead>
							<tr class="cubox">
								<th width="59%"><s:message code="label.product.details" text="Detail"/></th>
								<th width="7%"><s:message code="label.generic.price" text="Price"/></th>
								<th width="7%"><s:message code="label.quantity" text="Quantity"/></th>
								<th width="7%"><s:message code="label.order.total" text="Total"/><s:message code="order.total.subtotal" text="Subtotal"/></th>
								<th width="20%"><s:message code="label.order.split" text="Spilt"/></th>
							</tr>
						</thead>
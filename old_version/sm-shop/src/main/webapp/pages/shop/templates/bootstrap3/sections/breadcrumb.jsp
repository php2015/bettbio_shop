
<%
	response.setCharacterEncoding("UTF-8");
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", -1);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>

<div class="row "
	style="padding-bottom:0px;border: 1px solid transparent;margin: 0px">
	<table style="width:100%;height:100%;z-index:-1;" border="0"
		cellpadding="0" cellspacing="0">
		<tr>
			<td style="padding-left:5px;"><ul class=" breadcrumb "
					style="margin-bottom:0px;background:transparent;font-weight:700;"
					id="breadcrumb"></ul></td>
			<td>
				<div style="padding-top:7px;display: none;" id="chiocedPanel_man">
					<span
						style="float:left;border-top: 1px dashed #4285f4;border-left: 1px dashed #4285f4;border-bottom: 1px dashed #4285f4;">
						<s:message code="label.manufacturer.brand" text="Brands" />:
					</span><span id="name_man"
						style="float:left;border-top: 1px dashed #4285f4;border-right: 1px dashed #4285f4;border-bottom: 1px dashed #4285f4;"></span>
				</div>
			</td>
			<td>
				<div style="padding-top:7px;display: none;" id="chiocedPanel_qua">
					<span
						style="float:left;border-top: 1px dashed #4285f4;border-left: 1px dashed #4285f4;border-bottom: 1px dashed #4285f4;">
						<s:message code="label.product.quality" text="Quality" />:
					</span><span id="name_qua"
						style="float:left;border-top: 1px dashed #4285f4;border-right: 1px dashed #4285f4;border-bottom: 1px dashed #4285f4;"></span>
				</div>
			</td>
		</tr>
	</table>
	<div class="col-sm-5 col-md-5 col-lg-5 "></div>

</div>





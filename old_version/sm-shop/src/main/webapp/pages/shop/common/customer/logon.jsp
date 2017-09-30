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
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>


<br>
<br>
<br>
<div class="head-navbar-left head-navbar-right row" >
			<div class="col-md-4" id="adlogon" style="padding:20px 0px;">
				<img src="<c:url value="/resources/ad/b.png"/>" style="width:310px;height:248px;"/>
			</div>
			<div class="col-md-4" style="padding-top:120px;" >
			    <span style="font-size:23px;color:#4285f4;font-weight:blod;">让科研人员</span>
			    <span style="font-size:32px;color:#ff313b;margin-left:4px">放心</span>
			    <span style="font-size:25px;color:#4285f4;margin-left:4px">购买！</span>
			    <br/><span style="font-size:23px;color:#4285f4">好用，</span>
			    <span style="font-size:32px;color:#4285f4">看</span>
			    <span style="font-size:23px;color:#4285f4">得见！</span>
			</div>
			<div class="col-md-1" style="height: 340px;;width: 1px;border-left:1px solid #d9d9d9;"></div>
			<div class="col-md-3">
			  <div id="logform" class=" login" style="padding:15px 10px;"/>
				  <jsp:include page="/pages/shop/common/logonFormSms.jsp" />
			  </div>
			</div>
	</div>
	<br>
</div>
	<br>				



		

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
			
            <div class="box main-padding-lr">

               
                <table class="container">
			<tr>
				<td width="60"><img src="<c:url value="/resources/img/important-icon.png"/>" width="50"></td>
				<td ><h3 style="padding-top:40px;line-height:150%;word-wrap: break-word;"><c:out value="${information}" /></h3></td>
			</tr>
			<c:if test="${fromRegisterUserAction}">
				<c:if test="${autoCreateBuyer}">
					<td width="60"></td>
					<td ><h3 style="padding-top:40px;line-height:150%;word-wrap: break-word;">系统同时为您创建了买家账户，并已获得50积分。验证邮件已发送至<c:out value="${buyerEmail}" /> ,请登录该邮箱激活账户，谢谢！</h3></td>
				</c:if>
				<c:if test="${empty autoCreateBuyer}">
					<td width="60"></td>
					<td ><h3 style="padding-top:40px;line-height:150%;word-wrap: break-word;">系统检测到您使用的邮箱或者手机已经注册了买家账号，祝您购物愉快！</h3></td>
				</c:if>
			</c:if>
		</table>
					
              
            </div>
            

        		
    

    

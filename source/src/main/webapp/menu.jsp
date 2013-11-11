<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<fmt:setBundle basename="application" />
<%
pageContext.setAttribute("ContextPath", request.getContextPath());
%>
<%@ taglib prefix="umt" uri="WEB-INF/tld/umt.tld"%>
<div class="nav">		
			<ul>
				<li class="current">
					
					<a href="${ContextPath}/user/updateUser.do?act=show"><fmt:message key="menu.userinfo"/></a>
				</li>
				<!-- 
				<li>
					
					<a href="${ContextPath}/user/userSessions.do?act=load"><fmt:message key="menu.usersession"/></a>
				</li> -->
				
				<!-- <li>
					
					<a href="${ContextPath}/user/closeuser.jsp"><fmt:message key="menu.closeuser"/></a>
				</li> -->
			</ul>

		<umt:IsAdmin>
		
				<ul class="fileType">
					<!--<li>
						
						<a href="${ContextPath}/admin/manageApplication.do?act=showApps"><fmt:message key="menu.appmanage"/></a>
					</li>
					 <li>
						
						<a href="${ContextPath}/admin/manageRequests.do?act=showRequests">
							<fmt:message key="menu.requests"/>
							(<font style="color:red"><umt:RequestCount/></font>)</a>
					</li> -->
					<li id="manageUserMenu">
					
						<a href="${ContextPath}/admin/manageUser.do?act=showUsers"><fmt:message key="menu.usermanage"/></a>
					</li>
					<!--<li>
						
						<a href="${ContextPath}/admin/manageRole.do?act=load"><fmt:message key="menu.privilege"/></a>
					</li>
					 <li>
						
						<a href="${ContextPath}/admin/batch.jsp"><fmt:message key="menu.batch"/></a>
					</li> -->
					<li id="emailTemplateMenu">
						
						<a href="${ContextPath}/admin/editTemplate.do?act=setParameter"><fmt:message key="menu.emailtemplate"/></a>
					</li>
					<li id="accessIpMenu">
					
						<a href="${ContextPath}/admin/accessIps.do?act=showAccessIps">Access Ips</a>
					</li>
					<li id="oauthMenu">
						
						<a href="${ContextPath}/admin/addClient?act=setParameter"><fmt:message key="admin.oauth.menu"/></a>
					</li>
				</ul>
				
		</umt:IsAdmin>
	
</div>

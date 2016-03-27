<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<fmt:setBundle basename="application" />
<umt:AppList/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	request.setAttribute("msg", request.getParameter("msg"));
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title><fmt:message key="userinfo.title"/></title>
		<link href="<%= request.getContextPath() %>/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
	</head>
	<body class="login">
		<jsp:include page="../../../banner2013.jsp"/>
		<div class="container login gray">
				<h2 class="login-title"><fmt:message key="develop.ldap.member.list"/>-${bean.clientName }(<fmt:message key="develop.ldap.permision.${bean.priv }"/>)
					<a href="<umt:url value="/user/developer.do?act=display&viewType=ldap"/>" style="font-size:15px;cursor:pointer"><fmt:message key='common.login.returnBack'/></a> 
				</h2>
				<c:if test="${empty secrets }">
					<h3 style="color:gray;text-align:center">
						<fmt:message key='empty.member'/> 
					</h3>
				</c:if>
				<c:if test="${!empty secrets }">
				<table class="table" style="margin-left:60px;width:70%;margin-top:20px">
					<thead> 
					<tr>
						<td><fmt:message key='app.ldap.account'/></td>
						<td><fmt:message key='userinfo.truename'/></td>
						<td><fmt:message key='admin.oauth.status'/></td>
						<td><fmt:message key='admin.oauth.operation'/></td>
					</tr>
					</thead>
					<c:forEach items="${secrets }" var="secret">
					
					<tr>
						<td>
							<c:if test="${bean.wifiApp}">${secret.userCstnetId }</c:if>
							<c:if test="${!bean.wifiApp}">${secret.userLdapName }</c:if>
						</td>  
						<td>${secret.userName }</td>
						<td><fmt:message key="app.secret.userStatus.${secret.userStatus }"/></td>
						<td>
							<c:if test="${secret.userStatus=='apply'&&bean.priv=='needApply' }">
								<a class="btn btn-small" href="<umt:url value="/user/developer.do?act=openMember&ldapId=${secret.appId }&secretId=${secret.id }"/>"><fmt:message key='app.access.pwd.open'></fmt:message></a>
							</c:if>
							<a class="btn btn-small btn-danger delete" href="<umt:url value="/user/developer.do?act=deleteMember&ldapId=${secret.appId }&secretId=${secret.id }"/>"><fmt:message key='appmanage.btn.remove'/></a>
						</td>
					</tr>
					</c:forEach>
				</table>
				</c:if>
				
			</div>
		<jsp:include flush="true" page="../../../bottom2013.jsp"></jsp:include>
		
	</body>
	<script>
	$(document).ready(function(){
		$('.delete').on('click',function(){
			
			return confirm("<fmt:message key='delete.member.list'><fmt:param>${bean.clientName }</fmt:param></fmt:message>");
		});
	});
	</script>
</html>

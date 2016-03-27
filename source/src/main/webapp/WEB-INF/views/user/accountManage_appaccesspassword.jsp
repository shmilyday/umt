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
		<title><fmt:message key='app.access.pwd'></fmt:message></title>
		<link href="<%= request.getContextPath() %>/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
	</head>
	<body class="login">
		<jsp:include flush="true" page="../../../banner2013.jsp">
			<jsp:param name="DecludeMenu" value="true"/>
		</jsp:include>
		
		<div class="container login gray">
			<ul class="sub-nav">
				<li id="accountManage"><a href="<umt:url value="/user/manage.do?act=showManage"/>"><fmt:message key='accountManage.accountInfo'/></a></li>
				<c:if test="${'qq'!=loginInfo.user.type||'weibo'!=loginInfo.user.type }">
					<li id="changePsw"><a href="<umt:url value="/user/manage.do?act=showChangePassword"/>"><fmt:message key='accountManage.changePassword.title'/></a></li>
				</c:if>
				<li class="active" id="accountApp"><a><fmt:message key='app.access.pwd'></fmt:message></a></li>
				<li id="accountBind"><a href="<umt:url value="/user/manage.do?act=showBindAccount"/>"><fmt:message key='accountManage.bind.title'/></a></li>
				<div class="clear"></div>
			</ul>
			<div class="sub-content" id="accountBindShow" >
				<h4 class="sub-title"><fmt:message key='app.access.pwd'></fmt:message></h4>
				<p class="sub-text"><fmt:message key='app.access.pwd.hint'/></p>
				<div style="margin-left:50px" class="sub-app-btn"> 
				<ul class="nav nav-pills">
				  <li id="allView">
				    <a href="<umt:url value="/user/manage.do?act=appAccessPwd&viewType=all"/>" >
				    	<fmt:message key='develop.search.nav.all'></fmt:message>
				    </a>
				  </li>
				  <li id="oauthView">
				  	<a  href="<umt:url value="/user/manage.do?act=appAccessPwd&viewType=oauth"/>" >
				  		<fmt:message key='develop.search.nav.web'/>
				  	</a>
				  </li>
				  <li id="ldapView"><a  href="<umt:url value="/user/manage.do?act=appAccessPwd&viewType=ldap"/>">
				  <fmt:message key='develop.oauth.modify.appType.ldap'/></a></li>
				  <li id="wifiView"><a  href="<umt:url value="/user/manage.do?act=appAccessPwd&viewType=wifi"/>">
				  <fmt:message key='develop.oauth.modify.appType.wifi'/></a></li>
				</ul>
					<div class="clear"></div>
				</div>
				<c:if test="${viewType=='all' }">
					<c:if test="${!empty oauths}">
						<h4 class="accountManage tittle" ><fmt:message key='develop.search.nav.web'/></h4>
						<jsp:include page="app_password_oauth.jsp" />
					</c:if>
					<c:if test="${!empty ldaps}">
						<h4 class="accountManage tittle" style="margin-left:60px"><fmt:message key='develop.oauth.modify.appType.ldap'/></h4>
						<jsp:include page="app_password_ldap.jsp" />
					</c:if>
					<c:if test="${!empty wifis}">
						<h4 class="accountManage tittle" style="margin-left:60px"><fmt:message key='develop.oauth.modify.appType.wifi'/></h4>
						<jsp:include page="app_password_wifi.jsp" />
					</c:if>
				</c:if>
				<c:if test="${viewType=='oauth'}">
					<c:if test="${empty oauths}">
						<p class="accountManage no"><fmt:message key='empty.oauths'/></p>
					</c:if>
					<c:if test="${!empty oauths}">
						<jsp:include page="app_password_oauth.jsp" />
					</c:if>
				</c:if>
				<c:if test="${viewType=='ldap'}">
					<c:if test="${empty ldaps}">
						<p class="accountManage no"><fmt:message key='empty.ldaps'/></p>
					</c:if>
					<c:if test="${!empty ldaps}">
						<jsp:include page="app_password_ldap.jsp" />
					</c:if>
				</c:if>
				<c:if test="${viewType=='wifi'}">
					<c:if test="${empty wifis}">
						<p class="accountManage no"><fmt:message key='empty.wifis'/></p>
					</c:if>
					<c:if test="${!empty wifis}">
						<jsp:include page="app_password_wifi.jsp" />
					</c:if>
				</c:if>
			</div>
		</div>
		<jsp:include flush="true" page="../../../bottom2013.jsp"></jsp:include>
		
		<script type="text/javascript" > 
			$(document).ready(function(){
				$('#banner_user_manage').addClass("active");
				$('#${viewType}View').addClass('active');
				$('.deletePwd').on('click',function(){
					if(confirm("<fmt:message key='app.access.remove.pwd.hint'/>")){
						$.get('<umt:url value="/user/manage.do?act=deletePwd"/>',$(this).data()).done(function(){
							window.location.reload();
						});
					}
				});
			});
		</script>
	</body>
</html>
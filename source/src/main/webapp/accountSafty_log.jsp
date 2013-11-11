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
		<title><fmt:message key="accountSafty.title"/></title>
		<link href="<umt:url value="/images/favicon.ico"/>" rel="shortcut icon" type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
	</head>
	<body class="login">
		<jsp:include flush="true" page="banner2013.jsp">
			<jsp:param name="DecludeMenu" value="true"/>
		</jsp:include>
		
		<div class="container login gray">
			<ul class="sub-nav">
				<c:if test="${'weibo' != loginInfo.user.type }">
					<li><a href="<umt:url value="/user/safe.do?act=showSecurity"/>"><fmt:message key="accountSafty.setting"/></a></li>
				</c:if>
				<li class="active"><a href="<umt:url value="/user/safe.do?act=showLog"/>"><fmt:message key="accountSafty.logInfo"/></a></li>
				<div class="clear"></div>
			</ul>
			<div id="operListShow">
				<ul class="oper">
					<li class="log_item active" target="loginList"><a><fmt:message key="accountSafty.logitem.login"/></a></li>
					<li class="log_item" target="changePswList"><a><fmt:message key="accountSafty.logitem.changePsw"/></a></li>
					<li class="log_item"  target="securityEmailList"><a><fmt:message key="accountSafty.logitem.changeSecuirtyEmail"/></a></li>
					<!-- <li class="log_item"><a><fmt:message key="accountSafty.logitem.changeUsername"/></a></li> -->
					<!--li class="log_item"><a>手机绑定修改记录</a></li-->
				</ul>
				<div id="changePswList" class="loginList targetBoard" style="display:none">
					<table class="saftyList" >
						<tr>
							<th><fmt:message key="accountSafty.securityEmailList.data"/></th>
							<th><fmt:message key="accountSafty.securityEmailList.time"/></th>
							<th><fmt:message key="accountSafty.securityEmailList.ip"/></th>
						</tr>
						<c:forEach items="${changePasswordMessage }" var="msg">
							<tr>
								<td><fmt:formatDate value="${msg.occurTime }" type="date"/></td>
								<td><fmt:formatDate value="${msg.occurTime }" type="time"/></td>
								<td>${msg.userIp }</td>
							</tr>
						</c:forEach>
					</table>
				</div>
				<div id="loginList" class="loginList targetBoard" >
					<table class="saftyList" >
						<tr>
							<th><fmt:message key="accountSafty.loginList.date"/></th>
							<th><fmt:message key="accountSafty.loginList.time"/></th>
							<th><fmt:message key="accountSafty.loginList.ip"/></th>
							<th><fmt:message key="accountSafty.loginList.loginType"/></th>
						</tr>
						<c:forEach items="${loginMessage }" var="msg">
							<tr>
								<td><fmt:formatDate value="${msg.occurTime }" type="date"/></td>
								<td><fmt:formatDate value="${msg.occurTime }" type="time"/></td>
								<td>${msg.userIp }</td>
								<td>
								<c:choose>
									<c:when test="${msg.appName=='weibo' }">
										<fmt:message key='thirdParty.weibo'/>
									</c:when>
									<c:when test="${msg.appName=='qq' }">
										<fmt:message key='thirdParty.qq'/>
									</c:when>
									<c:when test="${msg.appName=='cashq' }">
										<fmt:message key='thirdParty.cashq'/>
									</c:when>
									<c:when test="${msg.appName=='umt' }">
										<fmt:message key='login.username.placeholder'/>
									</c:when>
									<c:otherwise>
										${msg.appName}
									</c:otherwise>
								</c:choose>
								
								</td>
							</tr>
						</c:forEach>
						
					</table>
				</div>
				<div id="securityEmailList" style="display:none" class="loginList targetBoard">
					<table class="saftyList" >
						<tr>
							<th><fmt:message key="accountSafty.securityEmailList.data"/></th>
							<th><fmt:message key="accountSafty.securityEmailList.time"/></th>
							<th><fmt:message key="accountSafty.securityEmailList.ip"/></th>
						</tr>
						<c:forEach items="${changeSecurityEmailMessage }" var="msg">
							<tr>
								<td><fmt:formatDate value="${msg.occurTime }" type="date"/></td>
								<td><fmt:formatDate value="${msg.occurTime }" type="time"/></td>
								<td>${msg.userIp }</td>
							</tr>
						</c:forEach>
					</table>
				</div>
				
				<div class="clear"></div>
			</div>
		</div>
		
		<jsp:include flush="true" page="bottom2013.jsp"></jsp:include>
		
	</body>
	<script type="text/javascript" >
		$(document).ready(function(){
			$(".log_item").live("click",function(){
				$(".log_item").each(function(i,n){
					$(n).removeClass("active");
				});
				$(".targetBoard").each(function(i,n){
					$(n).hide();
				});
				$(this).addClass("active");
				$('#'+$(this).attr("target")).show();
			});
			
			$('#banner_user_safe').addClass("active");
		});
	</script>
</html>
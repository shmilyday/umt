<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<c:if test="${empty sessionScope.thirdParty_type }">
	<jsp:forward page="login" />
</c:if>
<fmt:setBundle basename="application" />
<umt:AppList />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	request.setAttribute("msg", request.getParameter("msg"));
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><fmt:message key='thirdParty.bind.title' /></title>
<link href="<%=request.getContextPath()%>/images/favicon.ico"
	rel="shortcut icon" type="image/x-icon" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
</head>
<body class="login">
	<jsp:include flush="true" page="/banner2013.jsp">
		<jsp:param name="DecludeMenu" value="true" />
	</jsp:include>
	<div class="container login gray">
		<fmt:setBundle basename="application" />
		<h2 class="total-title">
			<fmt:message key="accountBind.welcome" />
			<span class="success-text">${thirdParty_user}</span>
		</h2>
		<p class="congratulation small-font gray-text">
			<fmt:message key="accountBind.hasLogined" />
			${thirdParty.name}
			<fmt:message key="accountBind.hasLoginedwith" />
		</p>
		<p class="congratulation small-font">
			<strong> 
				您已有中国科技网通行证中的同名帐号，请输入密码绑定帐号
			</strong>
		</p>
		<form id="bindForm" class="form-horizontal" method="post"
			action="<umt:url value="/callback/${thirdParty_type}"/>?act=bindUser">
			<!-- 登陆 -->
			<div id="bind_table">
				<div class="control-group">
					<label class="control-label" for="loginName"> <span
						class="ness">*</span> <fmt:message key="regist.form.username" />
					</label>
					<div class="controls">
						<input id="loginName" name="loginName" type="text" readonly value="${thirdParty_openId}"/>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="loginPassword"> <span
						class="ness">*</span> <fmt:message key="regist.form.password" />
					</label>
					<div class="controls">
						<input id="loginPassword" name="loginPassword" type="password"></input>
						<span id="loginPassword_error_place" class="error help-inline">
							<c:if test="${!empty loginPassword_error }">
								<fmt:message key="${loginPassword_error }" />
							</c:if>
						</span>
					</div>
				</div>
				<div class="control-group">
					<div class="controls">
						<input type="submit" class="btn long btn-primary"
							value="<fmt:message key="accountBind.bindnow"/>" />
					</div>
				</div>
			</div>
			<input type="hidden" name="screenName" value="${thirdParty_user}" />
			<input type="hidden" name="openId" value="${thirdParty_openId}" />
			<input type="hidden" name="type" value="${thirdParty_type }" />
		</form>
	</div>
	<jsp:include flush="true" page="/bottom2013.jsp"></jsp:include>
</body>
</html>
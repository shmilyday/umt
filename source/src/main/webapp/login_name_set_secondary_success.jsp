<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<fmt:setBundle basename="application" />
<umt:AppList/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title><fmt:message key='sendActiveEmail.title'/></title>
		<link href="<umt:url value="/images/favicon.ico"/>" rel="shortcut icon" type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
	</head>
	<body class="login">
		<jsp:include flush="true" page="banner2013.jsp"></jsp:include>
		<div class="container login gray">
			<div id="sendSuccessShow">
				<h2 class="login-title"><fmt:message key="${title }"/></h2>
				<div class="send_mail">
					<div class="mail_left"></div>
					<div class="mail_right">
						<h4><fmt:message key="accountManage.secondaryMail.emailNotice"/><span class="success-text" id="success_email">${email }</span></h4>
						<p><fmt:message key="accountManage.secondaryMail.emailNoticeHint"/></p>
						<umt:iKnowUrl email="${email }">
							<a id="toEmailLink" target="_blank" href="<umt:emailUrl email="${email }"/>"><button type="button" class="btn long btn-primary"><fmt:message key="remindpass.emailSentNoticeGo"/></button></a>&nbsp;&nbsp;
						</umt:iKnowUrl>
						<a href="<umt:url value='/'/>"><fmt:message key="common.login.returnBack"/></a>
					</div>
				</div>
			</div>
		</div>
		<jsp:include flush="true" page="bottom2013.jsp"></jsp:include>
	</body>
</html>
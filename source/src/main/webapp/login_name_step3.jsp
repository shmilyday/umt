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
			<div class="processBarContainer">
	              <ul class="processBar">
	                  <li class="current">
	                  	<span class="step-num span1">1</span>
	                  	<span class="step-text em1">验证身份 </span> 
	                  </li>
	                  <li class="current">
	                  	<span class="step-num span2">2</span>
	                  	<span class="step-text em2">输入新账号</span>
	                  </li>
	                  <li class="current">
	                  	<span class="step-num span3">3</span>
	                  	<span class="step-text em3">激活新账号</span>
	                  </li>
	                  <li class="">
	                  	<span class="step-num span4">4</span>
	                  	<span class="step-text em4">成功</span>
	                  </li>
	              </ul>
	            </div>
				<div class="congratulation">
					<h3><fmt:message key="remindpass.emailSentNotice"/><span class="success-text" id="success_email">${newPrimaryEmailShow }</span></h3>
					<p class="sub-text">请您在24小时内登录邮箱，按邮件中的指示激活新账号。新账号未激活之前，您还可以使用原有账号登录。</p>
					<umt:iKnowUrl email="${newPrimaryEmail }">
						<a id="toEmailLink" target="_blank" href="<umt:emailUrl email="${newPrimaryEmail }"/>"><button type="button" class="btn long btn-primary"><fmt:message key="remindpass.emailSentNoticeGo"/></button></a>
					</umt:iKnowUrl>
				</div>
		</div>
		<jsp:include flush="true" page="bottom2013.jsp"></jsp:include>
	</body>
</html>
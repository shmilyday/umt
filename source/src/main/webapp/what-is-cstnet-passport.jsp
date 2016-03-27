<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<fmt:setBundle basename="application" />
<%
        pageContext.setAttribute("contextPath", request.getContextPath());
%>
<fmt:setBundle basename="application" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
		<title><fmt:message key='common.duckling.QA'/></title>
		<link href="<%= request.getContextPath() %>/images/favicon.ico" rel="shortcut icon"	type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
</head>
<body class="login">
	<jsp:include flush="true" page="banner2013.jsp">
		<jsp:param name="DecludeMenu" value="true"/>
	</jsp:include>
  	<div class="container gray login">
	  	<h2 class="login-title">
			<fmt:message key='login.username.placeholder'/>
			<span class="sub-title"></span>
		</h2>
		<div class="help-content">
			
			<div class="right-content" style="min-height:100px;">
				<h3 class="quote"><span class="quoteBegin">“</span><fmt:message key='common.duckling.description'/><a href="http://www.escience.cn" target="_blank"><fmt:message key='common.escience'/></a><fmt:message key='common.quote'/><a href="http://ddl.escience.cn" target="_blank"><fmt:message key='common.ddl.escience'/></a><fmt:message key='common.quote'/><a href="http://csp.escience.cn" target="_blank"><fmt:message key='common.csp'/></a><fmt:message key='common.quote'/><a href="http://www.escience.cn/people" target="_blank"><fmt:message key='common.scholarPage'/></a><fmt:message key='common.quote'/><a href="http://mail.escience.cn" target="_blank"><fmt:message key='common.casMail'/></a><fmt:message key='common.duckling.description.more'/><span class="quoteEnd">”</span></h3>
				<p class="gray-text"><strong><fmt:message key='common.casMailLogin.hint'/></strong></p>
			</div>
			<div class="left-nav">
				<a href="http://passport.escience.cn" target="_blank"><img src="<%= request.getContextPath() %>/images/cstnet-code.png" /></a>
			</div>
			<h4 style="padding-top:30px; clear:both; border-top:1px solid #eee;"><fmt:message key='common.cstnetRoal'/></h4>
			<p><fmt:message key='common.cstnetRoalDetail'/></p>
			<jsp:include flush="true" page="/app_list.jsp"/>
		</div>
	</div>
<jsp:include flush="true" page="bottom2013.jsp"></jsp:include>

</body>


</html>
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
				<li class="active"><a href="<umt:url value="/user/safe.do?act=showSecurity"/>"><fmt:message key="accountSafty.setting"/></a></li>
				<li ><a href="<umt:url value="/user/safe.do?act=showLog"/>"><fmt:message key="accountSafty.logInfo"/></a></li>
				<div class="clear"></div>
			</ul>
			<jsp:include page="safeItems.jsp" flush="true">
				<jsp:param name="from" value="security"/>
			</jsp:include>
		</div>
		
		<jsp:include flush="true" page="bottom2013.jsp"></jsp:include>
		
	</body>
	<script type="text/javascript" >
		$(document).ready(function(){
			$('#banner_user_safe').addClass("active");
		});
	</script>
</html>
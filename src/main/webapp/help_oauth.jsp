<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<fmt:setBundle basename="application" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
		<title><fmt:message key="banner.help"/></title>
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
				帮助中心
				<span class="sub-title"></span>
			</h2>
		<div class="help-content">
   		<div class="left-nav">
			<ul class="nav">
				<li><a href="help.jsp" class="commonQA">常见问题</a></li>
				<li ><a href="help_https.jsp" class="proIntro">HTTPS安全连接</a></li>  
				<!-- <li><a href="help_tendays.jsp" class="tendays">十天内免登录简介</a></li> -->
				<li><a href="help_browser_password.jsp" class="browserPassword">浏览器记住密码</a></li>
				<li ><a href="help_updatelog.jsp" class="changeLog">更新日志</a></li>
				<li class="active"><a href="help_oauth.jsp" class="proIntro">应用接入</a></li>
				<li><a href="help_download.jsp" class="download">资源下载</a></li>
			</ul>
		</div>
		
		<div class="right-content">
			<div class="proIntro">
				<jsp:include page="oauthDoc.jsp"/>
			</div>
		</div>
		</div>
	</div>
<jsp:include flush="true" page="bottom2013.jsp"></jsp:include>

</body>
<script>
$(document).ready(function(){
	$('#banner_help').addClass("active");
});
</script>

</html>
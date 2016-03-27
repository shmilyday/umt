<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<fmt:setBundle basename="application" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title><fmt:message key="banner.help"/></title>
<link href="<%= request.getContextPath() %>/images/favicon.ico"
	rel="shortcut icon" type="image/x-icon" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
</head>
<body class="login">
	<jsp:include flush="true" page="banner2013.jsp">
		<jsp:param name="DecludeMenu" value="true" />
	</jsp:include>
	<div class="container gray login">
		<h2 class="login-title">
			帮助中心 <span class="sub-title"></span>
		</h2>
		<div class="help-content">
			<div class="left-nav">
				<ul class="nav">
					<li><a href="help.jsp" class="commonQA">常见问题</a></li>
					<!-- <li><a href="help_tendays.jsp" class="tendays">十天内免登录简介</a></li> -->

					<li class="active"><a href="help_https.jsp" class="proIntro">HTTPS安全连接</a></li>
					<li><a href="help_browser_password.jsp"
						class="browserPassword">浏览器记住密码</a></li>
					<li><a href="help_updatelog.jsp" class="changeLog">更新日志</a></li>
					<li><a href="help_oauth.jsp" class="proIntro">应用接入</a></li>
					<li><a href="help_download.jsp" class="download">资源下载</a></li>
				</ul>
			</div>

			<div class="right-content">
				<div class="commonQA">
					<h4>如何确认通行证正在使用https安全连接登录？</h4>
					<p>
						中国科技网通行证使用https安全连接来保护账号在网络传输过程中的安全性。
					</p>
					<p>
						中国科技网通行证所有登录界面，对于是否正在受到https安全连接保护均给予明确提示。如下图中图标 <img src="images/lock-16.png"/> 所示，表明当前登录受到https安全连接保护。
					</p>
					<p>
						<img style="width:auto"  src="images/https_https.png"/>
					</p>
					<p>
						如下图中图标 <img src="images/unlock-16.png" /> 所示，表明当前登录为普通http连接，登录信息在网络传输过程中存在被窃取的风险。
					</p>
					<p>
						<img style="width:auto;"  src="images/https_http.png"/>
					</p>
					<p class="red"><b>我们强烈建议所有应用使用https安全连接进行登录。</b></p>
					<div class="sub">
						<p class="relativeQA">HTTPS安全连接相关问题</p>
						<ol>
							<li>
								<h4>什么是https安全连接？</h4>
								<p>https，即Hypertext Transfer Protocol over Secure Socket
									Layer（http安全连接），可以说是http协议的安全版。众所周知，我们在互联网上冲浪，一般都是使用的http协议（超文本传输协议），默认情况下数据是明文传送的，这些数据在传输过程中都可能会被捕获和窃听，因此是不安全的。https是互联网服务的标准加密通讯方案，就是为了满足对安全性要求比较高的用户而设计的。</p>
							</li>
	
							<li>
								<h4>为什么要使用https安全连接？</h4>
								<p>
									使用https安全连接，可以从以下几个方面确保数据传输的安全性：<br />
									1）认证用户和服务器，确保数据发送到正确的客户机和服务器<br /> 
									2）加密数据以防止数据中途被窃取<br />
									3）维护数据的完整性，确保数据在传输过程中不被改变。<br />
									中国科技网通行证为确保用户账号安全，使用了https安全连接，以保证用户账号在网络传输过程中的安全性。<br />
								</p>
							</li>
							<li>
								<h4>中国科技网通行证对https安全连接支持如何？</h4>
								<p>
									中国科技网通行证全程提供https安全连接支持，包括中国科技网通行证网站本身以及所有接入应用使用的通行证登录界面。
									支持https安全连接的中国科技网通行证网站如下图所示：</p>
								<p>
									<img style="width:500px" src="images/https_link.png"></img></p>
							</li>
							<li>
								<h4>应用如何从http切换到https安全连接？</h4>
								<p>
									应用从http连接切换到https安全连接，只需要将原有嵌入或者跳转到通行证的链接由http://更改为：https://即可，其它信息无须变更。
								</p>
							</li>
						</ol>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include flush="true" page="bottom2013.jsp"></jsp:include>

</body>
<script>
	$(document).ready(function() {
		$('#banner_help').addClass("active");
	});
</script>

</html>
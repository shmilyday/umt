<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<fmt:setBundle basename="application" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
		<title>帮助</title>
		<link href="images/favicon.ico" rel="shortcut icon"	type="image/x-icon" />
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
				<li class="active"><a href="help_tendays.jsp" class="tendays">十天内免登录简介</a></li>
				<li><a href="help_browser_password.jsp" class="browserPassword">浏览器记住密码</a></li>
				<li><a href="help_updatelog.jsp" class="changeLog">更新日志</a></li>
				<li><a href="help_oauth.jsp" class="proIntro">应用接入</a></li>
			</ul>
		</div>
		
		<div class="right-content">
			<div class="tendays">
				<p>中国科技网通行证为了方便用户使用而推出此快捷登录方式。设置“十天内免登录”功能后，您在本电脑重新打开支持中国科技网通行证登录的应用时，就能直接登录应用，不需要再次输入帐号和密码。</p>
				<h4>所需条件：</h4>
				<ol>
					<li>登录前已勾选“十天内免登录”；</li>
					<li>关闭应用时，必须直接关闭浏览器。(请勿点击通行证界面右上角的“退出”操作，否则自动登录失效)。</li>
				</ol>
				<h4>您可能还会遇到以下问题：</h4>
				<ol>
					<li>
						<p>我已设置“十天内免登录”了， 为何每次打开还要输入密码？</p>
						<p>答：自动登录失效有三种原因：</p>	
						<ol>
							<li>
								<p>您上次点击了通行证的“退出”操作。这样就导致勾选的免登录功能失效。</p>
								<p>解决方法：当您不使用时，直接关闭浏览器即可。</p>
							</li>
							<li>
								<p>或者是清空浏览器 的cookies，也会取消自动登录。 </p>
								<p>解决方法：设置了免登录后，不使用清空浏览器。若已清空了，请重新设置“十天内免登录”。</p>
							</li>
							<li>
								<p>本电脑的IP地址更换后，基于安全考虑，也会导致十天内免登录功能失效，请关注登录界面提醒。 </p>
							</li>
						</ol>
					</li>
					<li>
						<p>我不想再使用“自动登录”，怎么取消？</p>
						<p>答：取消自动登录可通过以下两种方式：</p>
						<ol>
							<li>点击右上方“退出”操作。这样下次登录时，就需输入密码。</li>
							<li>或者清空浏览器 的cookies，也可取消自动登录。 </li>
						</ol>
					</li>
				</ol>
				
				<h4>设置自动登录的操作步骤如下：</h4>
				<p>在登录界面勾选“十天内免登录”并成功登录，未来十天内您都可以快捷登录，无需再次输入账号与密码。</p>
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
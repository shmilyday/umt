<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
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
				<li><a href="help_tendays.jsp" class="tendays">十天内免登录简介</a></li>
				<li class="active"><a href="help_browser_password.jsp" class="browserPassword">浏览器记住密码</a></li>
				<li><a href="help_updatelog.jsp" class="changeLog">更新日志</a></li>
				<li><a href="help_oauth.jsp" class="proIntro">应用接入</a></li>
			</ul>
		</div>
		
		<div class="right-content">
			<div class="commonQA">
				<ol>
					<li>
						<h4>IOS Safari浏览器</h4>
						<p>在“设置”中找到Safari浏览器，开启“自动填充”中的“名称和密码”功能。操作如下：</p>
						<p>
							<img src="images/remeberPsw/safari1.png" />
							<img src="images/remeberPsw/safari2.png" />
							<img src="images/remeberPsw/safari3.png" />
						</p>
					</li>
					<li>
						<h4>Android浏览器及UC浏览器</h4>				
						<p>在登陆时，浏览器会自动弹出“是否需要保存密码”的提示，选择“记住”。</p>
						<p class="center">
							<img src="images/remeberPsw/uc.png" />	
						</p>	
					</li>
					<li>
						<h4>IE浏览器</h4>				
						<p>在第一次登陆时，浏览器会自动弹出窗口，询问是否保存密码。</p>
						<p>	
							<img class="whole" src="images/remeberPsw/ie1.jpg" />
						</p>	
						<p>如果没有询问，需要更改浏览器设置：打开浏览器，在菜单“Internet 选项”对话框中，点击“内容”标签，再点击“自动完成”按钮，选定“表单上的用户名和密码”和“提示我保存密码”前面的钩，确定。</p>
						<p>
							<img class="whole" src="images/remeberPsw/ie.jpg" />
						</p>				
					</li>
					<li>
						<h4>Firefox浏览器</h4>				
						<p>在第一次登陆时，浏览器会自动弹出窗口，询问是否保存密码。</p>
						<p>
							<img class="whole" src="images/remeberPsw/ff1.png" />
						</p>	
						<p>如果没有询问，需要更改浏览器设置：打开浏览器，在菜单“工具”-“选项”中，点击“安全”标签，勾选“记住网站密码”选项。</p>
						<p class="center">
							<img class="large" src="images/remeberPsw/ff.jpg" />
						</p>			
					</li>
				</ol>
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
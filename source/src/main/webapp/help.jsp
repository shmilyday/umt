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
				<li class="active"><a href="help.jsp" class="commonQA">常见问题</a></li>  
				<li ><a href="help_https.jsp" class="proIntro">HTTPS安全连接</a></li>
				<!-- <li><a href="help_tendays.jsp" class="tendays">十天内免登录简介</a></li> -->
				<li><a href="help_browser_password.jsp" class="browserPassword">浏览器记住密码</a></li>
				<li><a href="help_updatelog.jsp" class="changeLog">更新日志</a></li>
				<li><a href="help_oauth.jsp" class="proIntro">应用接入</a></li>
				<li><a href="help_download.jsp" class="download">资源下载</a></li>
			</ul>
		</div>
		
		<div class="right-content">
			<div class="commonQA">
				<ol>
					<li>
						<h4>什么是中国科技网通行证？</h4>
						<p>中国科技网通行证是基于中国科技网的统一账号系统，可以用于登录各类科研应用服务，包括：<a href="http://www.escience.cn" target="_blank">科研在线</a>、<a href="http://ddl.escience.cn" target="_blank">文档库</a>、<a href="http://csp.escience.cn" target="_blank">国际会议服务平台</a>、<a href="http://www.escience.cn/people" target="_blank">科研主页</a>、<a href="http://mail.escience.cn" target="_blank">中科院邮件系统</a>等，以及今后将逐步扩展的更多应用服务。</p>
						<p class="red"><strong>中科院邮件系统账号可作为中国科技网通行证账号直接登录。</strong></p>
					</li>
					<li>
						<h4>我是中科院邮件系统用户，曾用中科院邮件系统账号注册过原Duckling通行证，现在我如何登录？</h4>				
						<p>原Duckling通行证升级为中国科技网通行证。现在，中科院邮件系统账号可作为中国科技网通行证直接登录。中国科技网通行证账号密码与中科院邮件系统账号密码相同。</p>
						<p>如果您曾经使用中科院邮件系统账号注册过原Duckling通行证账号，为了方便您的使用，请您按照系统提示进行账号合并操作。合并后，您就可以只使用中科院邮件系统账号和对应的密码登录通行证，畅通使用中科院邮件系统以及各类科研应用服务。原Duckling通行证账号和中科院邮件系统账号对应的各项服务及数据不受影响。</p>	
					</li>
					<li>
						<h4>添加辅助登录邮箱</h4>				
						<p>您可以向自己的账号添加辅助登录邮箱，进行登录、找回密码等操作。</p>
						<p>您要添加辅助登录邮箱，可按以下步骤进行：</p>
						<ol>
							<li>在账号管理页面点击“添加辅助登录邮箱” </li>
							<li>输入常用邮箱和通行证登录密码 </li>
							<li>点击保存</li>
						</ol>
						<p>我们会向您的辅助登录邮箱地址发送一封包含验证链接的邮件。点击该链接，验证您的辅助登录邮箱。辅助登录邮箱只有验证后才能使用。</p>
						<p>注：添加辅助登录邮箱时，不能使用已有通行证账号或者跟其它账号关联的辅助登录邮箱。</p>					
					</li>
					<li>
						<h4>忘记密码，如何找回？</h4>				
						<p>如果您忘记了密码，请按照如下操作进行密码重置：</p>
						<ol>
							<li>在登录页面点击<a target="_blank" href="<umt:url value="/findPsw.do?act=stepOne"/>">找回密码</a>链接</li>
							<li>在找回密码页面输入通行证账号或辅助登录邮箱 </li>
							<li>选择通过哪个邮箱找回，你可以通过通行证账号、辅助登录邮箱或密保邮箱找回</li>
						</ol>
						<p>注：中科院邮件系统用户，建议通过设置密保邮箱或者辅助登录邮箱进行找回，否则将无法找回密码。</p>				
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
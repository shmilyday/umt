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
				<li><a href="help_browser_password.jsp" class="browserPassword">浏览器记住密码</a></li>
				<li class="active"><a href="help_updatelog.jsp" class="changeLog">更新日志</a></li>
				<li><a href="help_oauth.jsp" class="proIntro">应用接入</a></li>
			</ul>
		</div>
		
		<div class="right-content">
			<div class="changeLog">
				<p>2013年11月1日</p>
				
				<ul>
					<li>发布版本7.0.11</li>
					<li>登陆页面自动聚焦到输入框</li>
					<li>后台管理界面美化</li>
					<li>OAuth支持接入类型区分</li>
				</ul>
				<p>2013年9月26日</p>
				<ul>
					<li>发布版本7.0.10</li>
					<li>支持科信客户端登陆科研在线应用</li>  
				</ul>
				<p>2013年9月23日</p>
				<ul>
					<li>发布版本7.0.9</li>
					<li>支持UAF</li>  
				</ul>
				<p>2013年8月27日</p>
				<ul>
					<li>发布版本7.0.8</li>
					<li>增加无线wifi单点登录定制界面</li>
					<li>应用管理增加联系人信息</li>
					<li>优化操作记录读取速度</li>  
					<li>操作记录增加登陆方式</li>  
				</ul>
				<p>2013年7月8日</p>
				<ul>
					<li>发布版本7.0.7</li>
					<li>新增开发者自助申请应用接入</li>
					<li>禁用js时，Coremail应用接入界面添加用户帮助链接</li>
					<li>OAuth应用管理界面功能完善</li>  
				</ul>
				<p>2013年6月28日</p>
				<ul>
					<li>发布版本7.0.6</li>
					<li>优化查看操作记录读取速度</li>
					<li>设置CoreMail API 超时时间</li>
					<li>Coremail OAuth接入界面，在浏览器禁用js时，添加相关提示</li>
					<li>Coremail OAuth接入界面,修复IE及Chrome下记住用户名功能不正常的bug</li>
				</ul>
				<p>2013年5月31日</p>
				<ul>
					<li>发布版本7.0.5p4</li>
					<li>账号输入框自动填充上次登录账号</li>
					<li>更改后台jar包API接口:搜索用户（支持用户姓名模糊查询）</li>
					<li>Coremail OAuth接入界面优化</li>
				</ul>
				<p>2013年5月17日</p>
				<ul>
					<li>发布版本7.0.5p1</li>
					<li>增加后台jar包API接口:搜索用户（支持VMT7.0.0）</li>
					<li>OAuth接口UserInfo中添加passwordType字段</li>
					<li>OAuth-CoreMail接入界面，修改错误提示信息</li>
					<li>OAuth-CoreMail接入界面，添加院统一认证方式</li>
				</ul>
				<p>2013年5月8日</p>
					<ul>
						<li>发布版本7.0.5</li>
						<li>新增Coremail OAuth接入界面</li>	
						<li>保持登录状态更改为十天免登录</li>
						<li>在OAuth接入界面添加应用程序链接</li>
					</ul>
				<p>2013年4月22日</p>
					<ul>
						<li>发布版本7.0.4p1</li>
						<li>修改自动跳转逻辑</li>	
						<li>移动终端web界面优化</li>
					</ul>
				<p>2013年4月3日</p>
					<ul>
						<li>发布版本7.0.4</li>
						<li>Oauth客户端支持https协议</li>		
					</ul>
				<p>2013年4月1日</p>
					<ul>
						<li>发布版本7.0.3</li>
						<li>修复OAuth2.0 bug</li>	
						<li>修复老应用接口与vmt兼容bug</li>
						<li>增加密码强度校验</li>		
					</ul>
				<p>2013年3月29日</p>
				<ul>
					<li>发布版本7.0.2</li>
					<li>支持OAuth2.0应用接入</li>	
				</ul>
			  	<p>2013年3月25日</p>
				<ul>
					<li>发布版本7.0.1</li>
					<li>完善“保持登录状态”功能</li>	
					<li>完善验证码刷新</li>	
				</ul>
				<p>2013年3月22日</p>
				<ul>
					<li>发布版本7.0.0，发布中国科技网通行证</li>
					<li>增加辅助登录邮箱，用于登录、找回密码</li>
					<li>增加密保邮箱作为账号安全保护，支持通过密保邮箱找回密码</li>				
					<li>添加账号安全提醒，支持用户查看操作记录</li>
					<li>支持个人资料修改</li>
					<li>支持新浪微博、QQ账号的第三方账号登录</li>
				</ul>
				<p>2013年1月23日</p>
				<ul>
					<li>发布版本6.0.1</li>
					<li>修复Bug</li>
				</ul>
				<p>2013年1月18日</p>
				<ul>
					<li>发布版本6.0.0</li>
					<li>支持中科院邮件系统用户登录</li>
					<li>支持注册@escience.cn邮箱</li>
					<li>集成各类应用入口</li>
				</ul>
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
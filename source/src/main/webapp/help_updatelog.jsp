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
				<li class="active"><a href="help_updatelog.jsp" class="changeLog">更新日志</a></li>
				<li><a href="help_oauth.jsp" class="proIntro">应用接入</a></li>
				<li><a href="help_download.jsp" class="download">资源下载</a></li>
			</ul>
		</div>
		
		<div class="right-content">
			<div class="changeLog">
			<p>2016年03月21日</p>
				<ul>
					<li>发布版本8.0.1</li>
					<li>新增Oauth登录界面皮肤</li>
				</ul>
			<p>2016年01月18日</p>
				<ul>
					<li>发布版本8.0.0</li>
					<li>系统管理添加单位管理功能</li>
					<li>修改新添加WIFI应用时发送给管理员的邮件标题</li>
					<li>支持SAML2协议认证</li>
				</ul>
			<p>2015年12月10日</p>
			<ul>
				<li>发布版本7.1.8p8</li>
				<li>更新eduroam cn成员及管理单位列表</li>
			</ul>
			<p>2015年12月03日</p>
				<ul>
					<li>发布版本7.1.8p7</li>
					<li>后台管理添加IP限制范围和备注</li>
					<li>UMT-API添加认证接口</li>
				</ul>
			<p>2015年11月03日</p>
				<ul>
					<li>发布版本7.1.8p5</li>
					<li>OAuth应用登录页面多次输入错误时添加安全验证码</li>
				</ul>
			<p>2015年09月25日</p>
				<ul>
					<li>发布版本7.1.8p4</li>
					<li>修复密码修改时误打开WIFI应用的bug</li>
				</ul>
			<p>2015年09月01日</p>
				<ul>
					<li>发布版本7.1.8p3</li>
					<li>修改LDAP/WIFI应用权限更改时，某些特殊情况下导致误删用户的bug</li>
					<li>更新WIFI应用审核与管理操作时的操作提示与邮件提示内容</li>
				</ul>
			<p>2015年08月14日</p>
				<ul>
					<li>发布版本7.1.8p2</li>
					<li>新增应用接入规范及相关资源下载</li>
				</ul>
			<p>2015年07月31日</p>
				<ul>
					<li>发布版本7.1.8p1</li>
					<li>修改密码修改页面相关提示</li>
				</ul>
			<p>2015年07月24日</p>
				<ul>
					<li>发布版本7.1.8</li>
					<li>支持维护模式</li>
					<li>优化系统存储</li>
					<li>修改部分未登录链接在登录后跳转不正确的bug</li>
					<li>修改科信快捷访问应用时返回信息不准确的bug</li>
				</ul>
			<p>2015年06月25日</p>
				<ul>
					<li>发布版本7.1.7p1</li>
					<li>修改删除短用户名时同时会删除wifi应用账号的bug</li>
					<li>修改启用第三方Web应用独立密码时，必须具有短用户名的bug</li>
					<li>修改在Firefox里，选择第三方登录时，登录完成时提示安全问题的bug</li>
					<li>通行证页尾添加VeriSign SSL验证链接</li>
				</ul>
			<p>2015年06月03日</p>
				<ul>
					<li>发布版本7.1.7</li>
					<li>新增支持GEODATA账号登录</li>
				</ul>
			<p>2015年05月15日</p>
				<ul>
					<li>发布版本7.1.6p1</li>
					<li>优化WiFi设置流程</li>
				</ul>
			<p>2015年04月16日</p>
				<ul>
					<li>发布版本7.1.6</li>
					<li>新增WiFi应用接入类型</li>
					<li>修改了公网安备信息</li>
					<li>新增新的认证服务类型支持（UMT）</li>
				</ul>
			<p>2014年11月28日</p>
				<ul>
					<li>发布版本7.1.2</li>
					<li>新增用户关闭用户异地登录提醒</li>
					<li>新增返回地址范围限制</li>
					<li>新增账号激活邮件频率限制</li>
				</ul>
			<p>2014年10月17日</p>
				<ul>
					<li>发布版本7.1.1</li>
					<li>应用登录界面关于通行证的解释使用专门的页面</li>
					<li>直接访问授权页面登录后目标地址的正确跳转</li>
					<li>密码强度提示</li>
					<li>登录位置变化邮件提醒</li>
					<li>CoreMail强制使用中等强度密码</li>
				</ul>
			<p>2014年7月25日</p>
				<ul>
					<li>发布版本7.1.0p2</li>
					<li>增加中国科技通行证说明页面</li>
					<li>修改独立密码保存出错的bug</li>
					<li>修复API查询接口过滤特殊字符有误的bug</li>
				</ul>
			<p>2014年7月18日</p>
				<ul>
					<li>发布版本7.1.0p1</li>
					<li>LDAP应用接入时授权机制与流程的功能完善</li>
					<li>增加LDAP相关应用审核流程邮件通知功能</li>
				</ul>
			<p>2014年7月1日</p>
				<ul>
					<li>发布版本7.1.0</li>
					<li>支持应用独立密码设置</li>
					<li>支持LDAP应用接入</li>
					<li>支持用户名登录</li>
				</ul>
				<p>2014年5月23日</p>
				<ul>
					<li>发布版本7.0.17p2</li>
					<li>新增应用图标尺寸支持（20*20）</li>
				</ul>
			
				<p>2014年5月21日</p>
				<ul>
					<li>发布版本7.0.17p1</li>
					<li>新增OAuth PC&Mobile定制界面</li>
				</ul>
				<p>2014年5月9日</p>
				<ul>
					<li>发布版本7.0.17</li>
					<li>新增通行证应用图标管理</li>
				</ul>
				<p>2014年04月23日</p>
				<ul>
					<li>发布版本7.0.16</li>
					<li>支持个人用户证书存储</li>
					<li>国际化改进</li>
				</ul>
				<p>2014年04月15日</p>
				<ul>
					<li>发布版本7.0.15</li>
					<li>OAuth支持应用自定义语言</li>
					<li>https安全连接的显示与改进</li>
					<li>coremail登录记住用户名的bug修复</li>
				</ul>
				<p>2014年04月11日</p>
				<ul>
					<li>发布版本7.0.14</li>
					<li>登录界面支持是否https安全连接提醒</li>
					<li>通行证用户注册流程改进</li>
					<li>OAuth返回用户信息可支持返回与组织通讯录相关的属性</li>
				</ul>
				<p>2014年02月13日</p>
				<ul>
					<li>发布版本7.0.13p2</li>
					<li>修改验证码验证失败时已填写数据丢失的bug</li>
				</ul>
				<p>2013年12月20日</p>
				<ul>
					<li>发布版本7.0.13p1</li>
					<li>登陆时添加对CoreMail到期时间的验证</li>
				</ul>
				
				<p>2013年11月22日</p>
				<ul>
					<li>发布版本7.0.13</li>
					<li>UMT日志记录分表存储</li>
					<li>OAuth新增crypt算法</li>
					<li>管理界面新添账号的锁定与解除</li>
					<li>新增OAuth日志记录</li>
					<li>采取新的验证码算法</li>
				</ul>
				<p>2013年11月12日</p>
				<ul>
					<li>发布版本7.0.12</li>
					<li>新增OAuth密码加密方式选项</li>
					<li>添加账号锁定/停用的支持</li>
				</ul>
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
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>

<fmt:setBundle basename="application" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<head>
		<title><fmt:message key="login.title" /></title>
		<link rel="stylesheet" type="text/css" href="themes/aone/css/umt-aone.css" />
		<link href="<%= request.getContextPath() %>/images/favicon.ico" rel="shortcut icon"
			type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<script type="text/javascript" src="js/ValidateCode.js"></script>
		<script type="text/javascript" src="js/string.js"></script>
		<script type="text/javascript" src="js/formcheck.js"></script>
	</head>

<body>
	<div class="ui-wrap">
		<div id="aoneBanner" class="std">
			<a id="ROL" href="http://www.escience.cn/"></a>
		</div>
	
		<div id="content">
			<div id="content-title">
				<h1>登录</h1>
				<div class="ui-RTCorner" style="margin-top:-1em; margin-right:10px;">
					还没有帐户？请 <a href="http://www.escience.cn/regist">注册</a>
				</div>
			</div>
			<div class="content-through">
				<div class="content-major">
			<form action="login" method="post" onsubmit="return checkform(this)">
				<input type="hidden" value="Validate" name="act" />
				<umt:SsoParams />
				<table class="ui-table-form" style="margin-top:3em">
				<tbody>
					<tr><th>用户名（邮箱）：</th>
						<td style="padding-right:5px;">
							<input type="text" required='true'
									datatype="email"
									message="<fmt:message key="login.username.required"/>"
									maxlength="255" name="username"
									onblur="checkRememberPass()" />
						</td>
						<td>
							<span class="duckling ui-text-note">使用Duckling通行证</span>
						</td>
					</tr>
					<tr><th>密码：</th>
						<td>
							<input type="password" maxlength="255" class="logininput"
									name="password" />
						</td>
						<td></td>
					</tr>
					
					<c:if test="${showValidCode!=null || WrongValidCode!=null }">
					<tr><th><fmt:message key="login.imagetext" /></th>
						<td>
							<input type="hidden" name="requireValid" value="true" />
							<input type="text" maxlength="255" class="logininput"
									name="ValidCode" required='true'
									message="<fmt:message key="login.imagetext.required"/>" />
						</td>
						<td></td>
					</tr>
					<tr><th></th>
						<td>
							<img id="ValidCodeImage" src="" />
							<a class="small_link" href="#"
								onclick="imageObj.changeImage()"><fmt:message
								key="login.imagetext.changeit" /></a>
						</td>
						<td></td>
					</tr>
					</c:if>
					
					<tr><th></th>
						<td><label>
							<input type="checkbox" name="PersistentCookie"
									id="PersistentCookie" value="yes" checked="checked" />
							<fmt:message key="login.password.rememberme" />
							</label>
						</td>
						<td></td>
					</tr>
				</tbody>
				
				<tfoot>	
					<tr><th></th>
						<td class="largeButtonHolder" style="padding-top:1em">
							<input type="hidden" name="theme" value="aone"/>
							<input type="submit" value="<fmt:message key="login.submit"/>" />
							<a href="password.jsp" class="small_link">
								<fmt:message key="login.forgetpassword" /> </a>
						</td>
						<td></td>
					</tr>
					<tr>
						<td></td>
						<td>其它账号登陆：<a href="${pageContext.request.contextPath}/weiboLoginServlet"><img src="images/login/sina.gif" alt="用新浪微博登录" /></a>
						<a href="${pageContext.request.contextPath}/qqLoginServlet"><img src="images/login/qqhao.gif" alt="用腾讯QQ登录" /></a></td>
						<td></td>
					</tr>
				</tfoot>
				</table>
				</form>
				
				<hr/>
				
				<table class="ui-table-form" style="margin-top:1em; width:100%;">
				<tbody>
					<tr>
						<td style="text-align:center">
							<p>注册Duckling通行证，即刻使用科研在线创建自己的科研站点</p>
						</td>
					</tr>
					<tr>
						<td class="largeButtonHolder" style="text-align:center">
							<input type="button" value="立即注册" onclick="window.location.href='http://www.escience.cn/regist'"/>
							<a class="largeButton dim ui-RTCorner" href="/">返回</a>
						</td>
					</tr>
				</tbody>
				</table>
				<div class="toolHolder holderMerge"></div>
				</div>
				
				<div id="content-side">
					<div class="sideBlock" id="dPassport-intro">
						<h4><span class="duckling">Duckling 通行证</span></h4>
						<p>Duckling通行证，是协同工作环境套件 Duckling的统一登录帐号，包含科研在线、国际会议服务平台等多项服务。
						<p>协同工作环境套件 Duckling，是专为团队协作提供的综合性资源共享和协同平台。集成网络环境中的硬件、软件、数据、信息等各类资源，集协同编辑、信息发布、文档管理、即时通讯、网络电话、短信通知、组织结构、文献共享、数据计算等为一体，为团队提供先进的信息化平台。</p>
					</div>
				</div>
				
				<div class="ui-clear"></div>
			</div>
		</div>

		<div id="footer">
			Powered by
			<a href="http://duckling.escience.cn/" target="_blank">Duckling 2.1</a>
		</div>
	</div>
	
		<script type="text/javascript">
var imageObj = ValidateImage("ValidCodeImage");
function RequestCookies(cookieName, dfltValue) {
	var lowerCookieName = cookieName.toLowerCase();
	var cookieStr = document.cookie;

	if (cookieStr == "") {
		return dfltValue;
	}

	var cookieArr = cookieStr.split("; ");
	var pos = -1;
	for ( var i = 0; i < cookieArr.length; i++) {
		pos = cookieArr[i].indexOf("=");
		if (pos > 0) {
			if (cookieArr[i].substring(0, pos).toLowerCase() == lowerCookieName) {
				return unescape(cookieArr[i].substring(pos + 1,
						cookieArr[i].length));
			}
		}
	}

	return dfltValue;
}

function getCookie() {
	if (RequestCookies("remember", "") == "on") {
		document.getElementsByName("password")[0].value = RequestCookies("password", "");
		document.getElementsByName("remember")[0].checked = true;
	}
}
function checkRememberPass(){
   var username = document.getElementsByName("username")[0].value;
   var oldusername =  RequestCookies("username", "");
   if(oldusername!=""&&oldusername==username)
   {
       getCookie();
   }
}
  </script>

	</body>
</html>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>

<fmt:setBundle basename="application" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>补充用户信息</title>
		<link rel="stylesheet" type="text/css" href="themes/aone/css/umt-aone.css" />
		<link rel="stylesheet" type="text/css" href="css/umt-aone.css" />

		<script type="text/javascript">
			function checkEmail() {
				var trueName = document.getElementById("user_truename").value;
				trueName = trueName.replace(/\s/ig,'');
				if(""==trueName) {
					alert("姓名不能为空！");
					return false;
				}
				var emailValue = document.getElementById("user_email").value;
				var pattern=/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
				var isvalid = pattern.exec(emailValue.trim())!=null;
				if(!isvalid) {
					alert("请输入正确的邮件信息！");
				}
				return isvalid;
			}
		</script>
	</head>

	<body>
	
	<div class="ui-wrap">
		<div id="aoneBanner" class="std">
			<a id="ROL" href=""></a>
		</div>
		<div id="content">
			<div id="content-title">
				<h1>补充用户信息</h1>
				<div class="ui-RTCorner" style="margin-top:-1em; margin-right:10px;">
				</div>
			</div>
			<div class="content-through">
				<div class="content-major">
				<form action="login" method="post">
				<table class="ui-table-form" style="margin-top:3em">
				<tbody>
					<tr><th>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</th>
						<td>
							<input type="text" class="logininput" id="user_truename" name="user_truename" value="${trueName}" />
						</td>
					</tr>
					<tr><th>邮箱(用户名)：</th>
						<td>
							<input type="text" class="logininput" id="user_email" name="user_email" />
						</td>
					</tr>
					
					<c:if test="${error!=null}">
					<tr><th></th>
						<td style="color: red"><c:out value="${error}" escapeXml="false"></c:out></td>
					</tr>
					</c:if>
					
				</tbody>
				
				<tfoot>	
					<tr><th>
						<input type="hidden" value="Validate" name="act" />
					   	<c:choose><c:when test="${\"weibo\".equals(param.type)}">
							<input type="hidden" value="weibo" name="type" />
							<input type="hidden" value="weibo" name="authBy" />
							<input type="hidden" value="${param.oauth_verifier}" name="oauth_verifier" />
						</c:when><c:when test="${\"qq\".equals(param.type)}">
							<input type="hidden" value="qq" name="type" />
							<input type="hidden" value="qq" name="authBy" />
						</c:when></c:choose></th>
						<td class="largeButtonHolder" style="padding-top:1em">
							<input type="hidden" value="${openId}" name="openId" />
							<input type="hidden" value="${accessToken}" name="accessToken" />
							<input type="submit" value="提交" onclick="return checkEmail()" />
						</td>
						<td></td>
					</tr>
				</tfoot>
				</table>
				</form>
				
				<table class="ui-table-form" style="margin-top:1em; width:100%;">
				<tbody>
					<tr>
						<td class="largeButtonHolder" style="text-align:center">
						</td>
					</tr>
				</tbody>
				</table>
				</div>
				
				<div id="content-side">
					<div class="sideBlock" id="dPassport-intro">
						<h4><span class="duckling">温馨提示</span></h4>
						<p>社会化登录能够简化用户登录步骤，减少用户注册操作的一种便捷工具。为了您能够接收到来自团队朋友和系统的消息，请填写真实有效的邮箱信息。</p>
					</div>
				</div>
				
				<div class="ui-clear"></div>
			</div>
			
		</div>
		
		
		<div id="footer">
			<p>Powered by <a href="http://duckling.escience.cn/" target="_blank">Duckling 2.1</a></p>
		</div>
	</div>
		
	</body>
</html>

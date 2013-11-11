<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>

<fmt:setBundle basename="application" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<title><fmt:message key="login.title" /></title>
<link rel="stylesheet" type="text/css"
	href="themes/iframe/css/umt-aone.css" />
<link href="images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<script type="text/javascript" src="js/ValidateCode.js"></script>
<script type="text/javascript" src="js/string.js"></script>
<script type="text/javascript" src="js/formcheck.js"></script>
</head>

<body>
	<div class="ui-wrap">
		<div class="content-major">
			<form action="login" method="post" onsubmit="checkform(this)">
				<umt:SsoParams />
				<input type="hidden" value="Validate" name="act" />
				<table class="ui-table-form" style="margin-top: 3em">
					<tbody>
						<tr>
							<th>邮箱：</th>
							<td style="padding-right: 5px;"><input type="text"
								required='true' datatype="email"
								message="<fmt:message key="login.username.required"/>"
								maxlength="255" name="username" onblur="checkRememberPass()" />
							</td>
						</tr>
						<tr>
							<th>密码：</th>
							<td><input type="password" maxlength="255"
								class="logininput" name="password" /></td>
						</tr>

						<c:if test="${showValidCode!=null || WrongValidCode!=null }">
							<tr>
								<th><fmt:message key="login.imagetext" /></th>
								<td><input type="hidden" name="requireValid" value="true" />
									<input type="text" maxlength="255" class="logininput"
									name="ValidCode" required='true'
									message="<fmt:message key="login.imagetext.required"/>" /></td>
							</tr>
							<tr>
								<th></th>
								<td><img id="ValidCodeImage" src="" /> <a
									class="small_link" href="#" onclick="imageObj.changeImage()"><fmt:message
											key="login.imagetext.changeit" /></a></td>
							</tr>
						</c:if>

						<tr>
							<th></th>
							<td><label> <input type="checkbox"
									name="PersistentCookie" id="PersistentCookie" value="yes"
									checked="checked" /> <fmt:message
										key="login.password.rememberme" />
							</label></td>
						</tr>
					</tbody>

					<tfoot>
						<tr>
							<th></th>
							<td class="largeButtonHolder" style="padding-top: 1em"><input
								type="hidden" name="theme" value="aone" /> <input type="submit"
								value="<fmt:message key="login.submit"/>" /> <a
								href="password.jsp" target="blank" class="small_link"> <fmt:message
										key="login.forgetpassword" />
							</a></td>
						</tr>
					</tfoot>
				</table>
			</form>
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
				document.getElementsByName("password")[0].value = RequestCookies(
						"password", "");
				document.getElementsByName("remember")[0].checked = true;
			}
		}
		function checkRememberPass() {
			var username = document.getElementsByName("username")[0].value;
			var oldusername = RequestCookies("username", "");
			if (oldusername != "" && oldusername == username) {
				getCookie();
			}
		}
	</script>
</body>
</html>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<fmt:setBundle basename="application" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title><fmt:message key="login.title" />
		</title>
		<link rel="stylesheet" type="text/css" href="css/umt.css" />
		<link href="images/favicon.ico" rel="shortcut icon"
			type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<script type="text/javascript" src="js/ValidateCode.js"></script>
		<script type="text/javascript" src="js/string.js"></script>
		<script type="text/javascript" src="js/formcheck.js"></script>
	</head>

	<body>
		<jsp:include flush="true" page="banner.jsp">
			<jsp:param name="DecludeMenu" value="true" />
		</jsp:include>
		<div class="content">
			<div class="left_content">
				<div class="left_content_img"></div>
				<div class="left_content_font">
					<div class="left_content_title">
						<fmt:message key='banner.welcome' />
						<strong><fmt:message key='banner.title' /></strong>
					</div>
				</div>
			</div>
			<div class="right_content">
				<form action="login" method="post" onsubmit="return checkform(this)">
					<input type="hidden" value="passport" name="theme" />
					<umt:SsoParams />
					<div class="loginbox">
						<table class="form_table">
							<tr>
								<td class="td_background">
									<div class="innerbox">
										<table>
											<tr>
												<th colspan="2" style="font-size: 10.5pt; font-style: bold;height:30px;">
													<fmt:message key="login.title" />
												</th>
											</tr>
											<tr>
												<td></td>
											</tr>
											<tr>
												<td>
													<strong><fmt:message key="login.username" /></strong>
													<input type="hidden" value="Validate" name="act" />
												</td>
												<td>
													<input type="text" class="logininput" required='true'
														datatype="email"
														message="<fmt:message key="login.username.required"/>"
														maxlength="255" name="username" onblur="checkRememberPass()" />
												</td>
											</tr>
											<tr>
												<td>
													<strong><fmt:message key="login.password" /></strong>
												</td>
												<td>
													<input type="password" maxlength="255" class="logininput"
														name="password" />
													<a target="_blank" href="password.jsp" class="small_link">
														<fmt:message key="login.forgetpassword" /> </a>
												</td>
											</tr>
											<c:if test="${showValidCode!=null || WrongValidCode!=null }">
												<tr>
													<td>
														<strong><fmt:message key="login.imagetext" /></strong>
													</td>
													<td>
														<input type="hidden" name="requireValid" value="true" />
														<input type="text" maxlength="255" class="logininput"
															name="ValidCode" required='true'
															message="<fmt:message key="login.imagetext.required"/>" />
													</td>
												</tr>
												<tr>
													<td></td>
													<td>
														<img id="ValidCodeImage" src="" />
														<a class="small_link" href="#"
															onclick="imageObj.changeImage()"><fmt:message
																key="login.imagetext.changeit" /> </a>
													</td>
												</tr>
											</c:if>
											<tr>
												<td></td>
												<td><input type="checkbox" name="PersistentCookie" id="PersistentCookie" value="yes" checked="checked" />
												<fmt:message key="login.password.rememberme" /></td>
											</tr>
											<tr>
												<td></td>
												<td>
													&nbsp;
												</td>
											</tr>
											<tr>
												<td align="left"></td>
												<td>
													<input type="submit"
														value="<fmt:message key="login.submit"/>" />
													<fmt:message key="login.newuser" />

													<umt:registerLink>
														<fmt:message key="login.regist" />
													</umt:registerLink>
												</td>
											</tr>
										</table>
									</div>
								</td>
							</tr>
						</table>
					</div>
				</form>
			</div>
			<div class="clear"></div>
		</div>

		<jsp:include flush="true" page="../../bottom.jsp"></jsp:include>
		<script type="text/javascript">
			var imageObj = ValidateImage("ValidCodeImage");
  function RequestCookies(cookieName, dfltValue)
{
    var lowerCookieName = cookieName.toLowerCase();
    var cookieStr = document.cookie;
    
    if (cookieStr == "")
    {
        return dfltValue;
    }
    
    var cookieArr = cookieStr.split("; ");
    var pos = -1;
    for (var i=0; i<cookieArr.length; i++)
    {
        pos = cookieArr[i].indexOf("=");
        if (pos > 0)
        {
            if (cookieArr[i].substring(0, pos).toLowerCase() == lowerCookieName)
            {
                return unescape(cookieArr[i].substring(pos+1, cookieArr[i].length));
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

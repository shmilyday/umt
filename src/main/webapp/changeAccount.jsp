<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>

<fmt:setBundle basename="application" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<head>
		<title>更改账号</title>
		<link href="<%= request.getContextPath() %>/images/favicon.ico" rel="shortcut icon"	type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<script type="text/javascript" src="js/ValidateCode.js"></script>
		<script type="text/javascript" src="js/string.js"></script>
	</head>

	<body class="login">
		<jsp:include flush="true" page="banner2013.jsp">
			<jsp:param name="DecludeMenu" value="true"/>
		</jsp:include>
		
		<div class="container gray login">
			<h2 class="login-title">更改账号<span class="mail-title"> <fmt:message key='common.duckling'/> </span></h2>
				<form id="loginForm" action="login" method="post">
					<table class="form_table">
						<tr>
							<th>当前账号</th>
							<td class="mail">xxx@163.com</td>
						</tr>
						<tr>
							<th>登录密码</th>
							<td>
								<input id="username" type="text" />
								<span class="error"></span>
							</td>
						</tr>
						<tr>
							<th>新账号</th>
							<td>
								<input id="password" type="password" class="logininput" placeholder="邮箱格式的账号" />
								<span id="password_error_place" class="error"></span>
							</td>
						</tr>
						<tr>
							<th></th>
							<td>
								<input type="submit" class="btn long btn-primary" value="确定"/>
							</td>
						</tr>
					</table>
					<input type="hidden" name="theme" value=""/>
				</form>
			</div>
			
		<jsp:include flush="true" page="bottom2013.jsp"></jsp:include>
		<script type="text/javascript">
			var imageObj = ValidateImage("ValidCodeImage");
		</script>
		<script type="text/javascript">
		$(document).ready(function(){
			$('#banner_login').addClass("active");
			$('#loginForm').validate({
				 submitHandler:function(form){
					 form.submit();
				 },
				 rules: {
					 username:{required:true,email:true},
					 password:{required:true/*,minlength:6*/},
					 ValidCode:{
						 required:true
				 },
				 messages: {
					 username: {
						 required:toRed("<fmt:message key='common.validate.email.required'/>"),
						 email:toRed('<fmt:message key="common.validate.email.invalid"/>')
					 },
					 password:{
						 required:toRed('<fmt:message key="common.validate.password.required"/>')
						 /*minlength:toRed('<fmt:message key="common.validate.password.minlength"/>')*/
					 },
					 ValidCode:{
						 required:toRed('<fmt:message key="common.validate.validateCode.required"/>')
					 }
					 
				 },
				 errorPlacement: function(error, element){
					 var sub="_error_place";
					 var errorPlaceId="#"+$(element).attr("name")+sub;
					 	$(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));
				}
			});
			function toRed(str){
				return "<font color='#cc0000'>"+str+'</font>';
			}
		});
		</script>
		<script language="javascript">
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

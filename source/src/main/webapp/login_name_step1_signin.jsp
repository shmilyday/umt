<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<fmt:setBundle basename="application" />
<umt:AppList/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title><fmt:message key='sendActiveEmail.title'/></title>
		<link href="<umt:url value="/images/favicon.ico"/>" rel="shortcut icon" type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
	</head>
	<body class="login">
		<jsp:include flush="true" page="banner2013.jsp"></jsp:include>
			<div class="container login gray">
			<h2 class="login-title">验证您的账号(登录)</h2>
			<form id="loginForm" action="<umt:url value="/primary/activation.do?act=validPasswordPrimary"/>" method="post">
				<table class="form_table">
				<tr>
					<th>
						主要电子邮件地址：
					</th>
					<td>
					${_primaryEmail}
					<span id="loginName_error_place" class="error">
							<c:if test="${!empty loginName_error }">
								<fmt:message key='${loginName_error }'/>
							</c:if>
					</span>
					</td>
				</tr>
				<tr>
					<th>
						通行证登录密码：
					</th>
					<td>
						<input type="password" name="password" id="password"/>
						<span id="password_error_place" class="error">
							<c:if test="${!empty password_error }">
								<fmt:message key='${password_error }'/>
							</c:if>
						</span>
					</td>
				</tr>
				<tr>
					<th>
					</th>
					<td>
					<input type="hidden" value="${_primaryEmail }" name="loginName"/>
					<button class="btn long btn-primary" type="submit">确定</button>
					</td>
				</tr>
			</table>
			
		</form>
		</div>
		<jsp:include flush="true" page="bottom2013.jsp"></jsp:include>
	</body>
		<script>
	$(document).ready(function(){
		$('#loginForm').validate({
				rules: {
					 password:{required:true},
				 },
				 messages: {
					 password:{
						 required:'<fmt:message key="common.validate.password.required"/>'
					 }
				 },
				 errorPlacement: function(error, element){
					 var sub="_error_place";
					 var errorPlaceId="#"+$(element).attr("name")+sub;
					 	$(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));
				 }
			});
	});
	</script>
</html>
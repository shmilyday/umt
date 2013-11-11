<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录成功！正在跳转...</title>
</head>
<body>
	<form action="${WebServerURL}" name="redirectform" method="post" target="${target}">
		<input type="hidden" name="signedCredential" value="${signedCredential}"/>
		<c:if test="${loginResult!=null}">
		    <input type="hidden" name="loginResult" value="${loginResult}"/>
		</c:if>
	</form>
	<script type="text/javascript">
		document.redirectform.submit();
	</script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>正在跳转...</title>
</head>
<body>
	<form action="${loginThirdpartyAppURL}" name="redirectform" method="post">
		<input type="hidden" name="${userName}" value="${userNameValue}"/>
		<input type="hidden" name="${password}" value="${passwordValue}"/>
		<input type="hidden" name="face" value="H"/>
		<input type="hidden" name="action:login" value=""/>
		
	</form>
	<script type="text/javascript">
		document.redirectform.submit();
	</script>
</body>
</html>
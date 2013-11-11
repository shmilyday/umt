<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<form action="${LoginURL}" name="redirectform" method="post">
		<input type="hidden" name="encrypted" value="${Encrypted}"/>
		<input type="hidden" name="appname" value="${appname}"/>
		<input type="hidden" name="WrongValidCode" value="${WrongValidCode}"/>
	</form>
	<script type="text/javascript">
		document.redirectform.submit();
	</script>
</body>
</html>
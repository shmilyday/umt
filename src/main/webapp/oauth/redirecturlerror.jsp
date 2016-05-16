<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<fmt:setBundle basename="application" />
<umt:AppList/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>回调地址错误</title>
</head>
<body id="login" class="login">
	<jsp:include flush="true" page="../banner2013.jsp"></jsp:include>
	<div class="container login">
		<div class="errorText">
			<c:choose>
				<c:when test="${not empty redirect_uri}">
					<h3>应用提供的redirect_uri:${redirect_uri} 不符合规范，请联系应用管理员！</h3>
				</c:when>
				<c:otherwise>
					<h3>用户提供的redirect_uri不符合格式，请重新确认</h3>
				</c:otherwise>
			</c:choose>
			<p>客户端id：${client_id}</p>
			<p>错误码：${errorCode }</p>
			<p>错误描述：${errorDescription}</p>
		</div>
	</div>
	<jsp:include flush="true" page="../bottom2013.jsp"></jsp:include>
</body>
</html>
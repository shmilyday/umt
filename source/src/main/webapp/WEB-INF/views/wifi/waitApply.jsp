<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="umt" uri="WEB-INF/tld/umt.tld" %>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<%
	pageContext.setAttribute("contextPath", request.getContextPath());
%>
<fmt:setBundle basename="application" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html>
	<head>
		<title><fmt:message key="message.title"/></title>
		<link href="<umt:url value="/images/favicon.ico"/>" rel="shortcut icon"	type="image/x-icon" />
		<f:css href="${contextPath }/thirdparty/bootstrap/css/bootstrap.min.css"/>
		<f:css href="${contextPath }/thirdparty/bootstrap/css/bootstrap-responsive.min.css"/>
		<f:css href="${contextPath }/css/wifi.css"/>
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta name="viewport" content="width=device-width, initial-scale=1" />
	</head>

	<body class="wifi">
		<jsp:include flush="true" page="/banner2013.jsp"></jsp:include>
		<div class="wifi container">
			<h3 class="title">请等待管理员审核！</h3> 
			<p class="subTitle">欢迎使用${clientName}无线服务，请等待管理员审核通过后，您可以使用中国科技网通行证账号（${loginname}）和密码登录无线漫游服务。</p>
			<h4 class="related">相关链接</h4>
			<ul class="wifi-links">
				<li><a href="http://eduroam.cstnet.cn/">eduroam CN无线网漫游交换中心</a></li>
				<li><a href="http://eduroam.cstnet.cn/total/283.jhtml">eduroam CN漫游研究所和大学列表</a></li>
			</ul>
			<p class="center">
				<a href="<umt:url value=""/>" class="btn btn-primary">返回首页</a>
			</p>
		</div>
		<jsp:include flush="true" page="/bottom2013.jsp"></jsp:include>
	</body>
</html>

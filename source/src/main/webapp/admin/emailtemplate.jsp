<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>

<fmt:setBundle basename="application" />
<%
        pageContext.setAttribute("contextPath", request.getContextPath());
        pageContext.setAttribute("DecludeMenu", request.getParameter("DecludeMenu"));
        String showLogin = request.getParameter("showLogin");
%>
<fmt:setBundle basename="application" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key="menu.emailtemplate"/> </title>  
<link href="../css/umt.css" rel="stylesheet" type="text/css"/>
<f:css href="${contextPath }/thirdparty/bootstrap/css/bootstrap.min.css"/>
<f:css href="${contextPath }/thirdparty/bootstrap/css/bootstrap-responsive.min.css"/>
<f:css href="${contextPath }/css/umt2013.css"/>
<f:css href="${contextPath }/css/umt2013-responsive.css"/>
<f:css href="${contextPath }/css/umt-admin.css"/>
</head>

<body class="login">
<jsp:include flush="true" page="../banner.jsp"></jsp:include>


<script type="text/javascript" src="../js/jquery.corner.js"></script>
<div class="container login gray">
	<jsp:include flush="true" page="../menu.jsp"></jsp:include>
	<jsp:include flush="true" page="parts/emailtemplatepart.jsp"></jsp:include>
</div>
<jsp:include flush="true" page="../bottom2013.jsp"></jsp:include>
</body>
</html>
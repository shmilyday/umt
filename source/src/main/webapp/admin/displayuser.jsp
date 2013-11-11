<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<fmt:setBundle basename="application" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='batch.title'/></title>
<link href="../css/umt.css" rel="stylesheet" type="text/css"/>
</head>

<body>
<jsp:include flush="true" page="../banner.jsp"></jsp:include>


<div class="content">
	<jsp:include flush="true" page="../menu.jsp"></jsp:include>
	<jsp:include flush="true" page="parts/displaypart.jsp"></jsp:include>
</div>
<jsp:include flush="true" page="../bottom2013.jsp"></jsp:include>
</body>
</html>
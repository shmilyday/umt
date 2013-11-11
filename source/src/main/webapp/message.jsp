<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	pageContext.setAttribute("ContextPath", request.getContextPath());
%>
<fmt:setBundle basename="application" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html>
	<head>
		<title><fmt:message key="message.title"/></title>
		<link href="/images/favicon.ico" rel="shortcut icon"	type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
	</head>

	<body class="login">
		<jsp:include flush="true" page="/banner2013.jsp"></jsp:include>
		<div class="container login gray">
			<table class="form_table">
				<tr>
					<td>
					<%--  
						<c:if test="${WithoutMenu==null}">
							<jsp:include flush="true" page="/menu.jsp"></jsp:include>
						</c:if>
						--%> 
					</td>
				</tr>
				<tr>
					<td>
						<h3><fmt:message key="${message}"/>  </h3> 
						<a href="${empty ContextPath?'/':ContextPath}"><fmt:message key="common.return.index"/></a>
					</td>
				</tr>
			</table>
		</div>
		<jsp:include flush="true" page="/bottom2013.jsp"></jsp:include>
	</body>
</html>

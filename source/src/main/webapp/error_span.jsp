<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<fmt:setBundle basename="application" />
<%
	String input=request.getParameter("input");
	pageContext.setAttribute("input", input);
	pageContext.setAttribute("fmtKey", request.getAttribute(input+"_error"));
	pageContext.setAttribute("spanId", input+"_error_place");
%>
<span id="${spanId }" class="error help-inline">
	<c:if test="${!empty fmtKey}">
		<fmt:message key="${fmtKey }"/>
	</c:if>
</span>
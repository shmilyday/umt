<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@ taglib prefix="umt" uri="WEB-INF/tld/umt.tld"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>

<fmt:setBundle basename="application" />
<div class="dface container footer">
	<fmt:message key="copyright.declare">
		<umt:system var="system">
			<fmt:param value="${system.appName}"/>
			<fmt:param value="${system.version}"/>		
		</umt:system>
	</fmt:message>
</div>
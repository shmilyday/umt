<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<fmt:setBundle basename="application" />
<table class="table accountManage">
	<thead>
		<tr>
			<th style="width: 10em;"><fmt:message
					key='app.access.pwd.app.name' /></th>
			<th style="width: 35em;"><fmt:message
					key='app.access.pwd.app.description' /></th>
			<th style="width: 10em;"><fmt:message
					key='develop.ldap.duli.pwd' /></th>
		</tr>
	</thead>
	<c:forEach items="${oauths }" var="app">
		<tr>
			<td class="left"><c:out value="${app.data.clientName }" /></td>
			<td class="left"><c:out value="${app.data.description }" /></td>
			<td><c:choose>
					<c:when test="${ empty app.secret}">
						<a class=" btn btn-small btn-info"
							href="<umt:url value="/user/manage.do?act=createPassword&viewType=${app.data.appType }&appId=${ app.appId}"/>"><fmt:message
								key='app.access.pwd.open' /></a>
					</c:when>
					<c:otherwise>
						<a data-pwd-id="${app.secret.id }"
							data-app-id="${app.secret.appId }"
							data-view-type="${app.data.appType }"
							class=" btn btn-small btn-info deletePwd"><fmt:message
								key='app.access.pwd.close' /></a>
						<a
							href="<umt:url value="/user/manage.do?act=createPassword&viewType=${app.data.appType }&appId=${ app.appId}"/>&op=updatePwd"><fmt:message
								key='usermanage.btn.resetpassword' /></a>
					</c:otherwise>
				</c:choose></td>
		</tr>
	</c:forEach>
</table>
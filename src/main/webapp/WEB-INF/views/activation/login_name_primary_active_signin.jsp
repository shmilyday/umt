<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<fmt:setBundle basename="application" />
<umt:AppList />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><fmt:message key='sendActiveEmail.title' /></title>
<link href="<%= request.getContextPath() %>/images/favicon.ico"
	rel="shortcut icon" type="image/x-icon" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
</head>
<body class="login">
	<jsp:include flush="true" page="/banner2013.jsp"></jsp:include>
	<div class="container login gray">
		<h2 class="login-title">
			<fmt:message key="login_primary_active" />
		</h2>
		<form id="loginForm"
			action="<umt:url value="/primary/activation.do?act=validPasswordPrimary&requestAct=${_requestAct }&loginName=${_primaryEmail }"/>"
			method="post">
			<table class="form_table">
				<tr>
					<th><fmt:message key="common.userInfo.cstnetID" /></th>
					<td>${_primaryEmail}</td>
					<td><span id="loginName_error_place" class="error"> <c:if
								test="${!empty loginName_error }">
								<fmt:message key='${loginName_error }' />
							</c:if>
					</span></td>
				</tr>
				<tr>
					<th><fmt:message key="common.userInfo.password" /></th>
					<td><input type="password" name="password" id="password" /></td>
					<td><span id="password_error_place" class="error"> <c:if
								test="${!empty password_error }">
								<fmt:message key='${password_error }' />
							</c:if>
					</span></td>
				</tr>
				<tr>
					<th></th>
					<td><input type="hidden" value=${_requestAct }
						name="requestAct" /> <input type="hidden"
						value="${_primaryEmail }" name="loginName" /> <input type="hidden"
						value="${token.tokenid }" name="tokenid" /> <input type="hidden"
						value="${token.random }" name="random" /> <input type="hidden"
						value="${token.changeLoginName }" name="changeLoginName" /> <input
						type="hidden" value="${token.loginNameInfoId }"
						name="loginNameInfoId" />
						<button class="btn long btn-primary" type="submit">
							<fmt:message key="common.confirm" />
						</button></td>
					<td></td>
				</tr>
			</table>

		</form>
	</div>
	<jsp:include flush="true" page="/bottom2013.jsp"></jsp:include>
	<script type="text/javascript">
	$(document).ready(function(){
		$('#loginForm').validate({
				rules: {
					 password:{required:true},
				 },
				 messages: {
					 password:{
						 required:'<fmt:message key="common.validate.password.required"/>'
					 }
				 },
				 errorPlacement: function(error, element){
					 var sub="_error_place";
					 var errorPlaceId="#"+$(element).attr("name")+sub;
					 	$(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));
				 }
			});
	});
	</script>
</body>
</html>
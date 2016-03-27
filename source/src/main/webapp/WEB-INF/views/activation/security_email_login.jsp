<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<fmt:setBundle basename="application" />
<umt:AppList/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title><fmt:message key='sendActiveEmail.title'/></title>
		<link href="<%= request.getContextPath() %>/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
	</head>
	<body class="login">
		<jsp:include flush="true" page="/banner2013.jsp"></jsp:include>
			<div class="container login gray">
			<h2 class="login-title"><fmt:message key="securityEmail.login.title"/></h2>
			<p class="hint">
				<fmt:message key="securityEmail.login.hint">
					<fmt:param>${_securityEmail }</fmt:param>
				</fmt:message>
			</p>
			<form id="securityForm" class="form-horizontal" style="padding:0 5%" action="<umt:url value="/security/activation.do?act=validPasswordSecurity"/>" method="post">
				<div class="control-group">
	              	<label class="control-label nopadding">
						<fmt:message key="common.userInfo.cstnetID"/>
					</label>
	              	<div class="controls">
	              		<input type="hidden" name="loginName" id="loginName" value="${_primaryEmail }"/>
						${_primaryEmail }
	              	</div>
	            </div>
				<div class="control-group">
	              	<label class="control-label" for="password">
						<fmt:message key='common.userInfo.password'/>
					</label>
	              	<div class="controls">
	              		<input name="password" type="password" id="password"/>
		              	<span id="password_error_place" class="error help-inline">
		              		<jsp:include page="/error_span.jsp" flush="true">
								<jsp:param value="password" name="input"/>
							</jsp:include>
						</span>
						<input type="hidden" name="securityEmail" value="${_securityEmail }"/>
						<input type="hidden" name="tokenid" value="${token.tokenid }"/>
						<input type="hidden" name="random" value="${token.random }"/>
						<input type="hidden" name="changeLoginName" value="${token.changeLoginName }"/>
						<input type="hidden" name="loginNameInfoId" value="${token.loginNameInfoId }"/>
					</div>
	            </div>
	            <div class="control-group">
	            	<div class="controls">
	            		<button type="submit" class="btn long btn-primary"><fmt:message key='usermanage.form.submit'/></button>
	            	</div>
	            </div>
		</form>
		</div>
		<jsp:include flush="true" page="/bottom2013.jsp"></jsp:include>
		<script type="text/javascript">
		$(document).ready(function(){
			$('#securityForm').validate({
					rules: {
						password:{
							 required:true,
						 }
					 },
					 messages: {
						 password:{
							 required:"<fmt:message key='common.validate.password.required'/>"
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
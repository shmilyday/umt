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
			<h2 class="login-title"><fmt:message key="accountManage.secondaryMail.add"/></h2>
			<p class="hint"><fmt:message key="accountManage.addSecondaryMail.hint"/></p>
			<form id="secondaryForm" class="form-horizontal" action="<umt:url value="/user/secondary/loginName.do?act=addSecondaryEmail"/>" method="post">
				<div class="control-group">
	              	<label class="control-label nopadding" for="password">
						<fmt:message key="accountManage.newSecondaryMail"/>
					</label>
	              	<div class="controls">
						<input name="newSecondaryEmail" id="newSecondaryEmail" value="${newSecondaryEmail }" type="text"/>
	              		<span id="newSecondaryEmail_error_place" class="error help-inline">
							<c:if test="${!empty newSecondaryEmail_error }">
								<fmt:message key="${newSecondaryEmail_error }"/>
							</c:if>
						</span>
	              	</div>
	            </div>
				<div class="control-group">
	              	<label class="control-label nopadding" for="password">
						<fmt:message key="changeSaftyMail.form.passportPassword"/>
					</label>
	              	<div class="controls">
						<input type="password" name="password" id="password"/>
	              		<span id="password_error_place" class="error help-inline">
							<c:if test="${!empty password_error }">
								<fmt:message key="${password_error }"/>
							</c:if>
						</span>																		
	              	</div>
	            </div>
				<div class="control-group">
	              	<div class="controls">
						<input type="hidden" value="${loginInfo.user.cstnetId }" name="loginName"/>
						<button class="btn long btn-primary" type="submit"><fmt:message key="common.confirm"/></button>
	              		<a href="<umt:url value="/user/manage.do?act=showManage"/>"><fmt:message key='common.login.returnBack'/></a>
	              	</div>
	            </div>
		</form>
		</div>
		<jsp:include flush="true" page="/bottom2013.jsp"></jsp:include>
		<script type="text/javascript">
		$(document).ready(function(){
			$('#secondaryForm').validate({
					rules: {
						newSecondaryEmail:{
							 required:true,
							 email:true,
							 remote:{
								 type: "GET",
								 url: '<umt:url value="/user/primary/loginName.do"/>',
								 data:{ 
									 'act':'isPrimaryEmailUsed',
									 "primaryEmail":function(){
														return $("#newSecondaryEmail").val();
													}	   		
						  	 		 }
							 }
						 },
						 password:{
							 required:true
						 }
						 
					 },
					 messages: {
						 newSecondaryEmail:{
							 required:"<fmt:message key='accountManage.error.secondaryMail.required'/>",
							 email:"<fmt:message key='regist.username.format'/>",
							 remote:'<fmt:message key="primaryEmail.used"/>'
						 },
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
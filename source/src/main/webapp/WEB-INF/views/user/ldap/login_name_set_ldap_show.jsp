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
		<title><fmt:message key='app.add.account'/></title>
		<link href="<%= request.getContextPath() %>/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
	</head>
	<body class="login">
		<jsp:include flush="true" page="/banner2013.jsp"></jsp:include>
			<div class="container login gray">
			<h2 class="login-title"><fmt:message key='app.add.account'/></h2>
			<p class="hint"><fmt:message key='app.set.account.hint'/></p>
			<form id="secondaryForm" class="form-horizontal" action="<umt:url value="/user/ldap/loginName.do?act=saveLdapName"/>" method="post">
				<div class="control-group">
	              	<label class="control-label nopadding" for="password">
						<fmt:message key='app.ldap.account'/>
					</label>
	              	<div class="controls">
						<input name="ldapName" id="ldapName" value="<c:out value="${ldapNameStr }"/>" type="text"/>
	              		<span id="ldapName_error_place" class="error help-inline">
							<c:if test="${!empty ldapName_error }">
								<fmt:message key="${ldapName_error }"/>
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
			$('#banner_user_manage').addClass('active');
			$.validator.addMethod("ldapNameRegix", 
					function(value, element, params) {    
						var regix=/^[a-zA-Z0-9_\-]+$/;
					    return regix.test(value); 
				 	},
				'<fmt:message key="ldap.validate.loginname"/>');
			$('#secondaryForm').validate({
					rules: {
						ldapName:{
							 required:true,
							 minlength:3,
							 ldapNameRegix:true,
							 remote:{
								 type: "GET",
								 url: '<umt:url value="/user/ldap/loginName.do"/>',
								 data:{ 
									 'act':'validateLdapNameUsed',
									 "ldapName":function(){
														return $("#ldapName").val();
													}	   		
						  	 		 }
							 }
						 },
						 password:{
							 required:true
						 }
						 
					 },
					 messages: {
						 ldapName:{
							 minlength:'<fmt:message key="app.validate.minlength.three"/>',
							 required:"<fmt:message key='app.validate.new.account.required'/>",
							 remote:'<fmt:message key="app.validate.new.account.used"/>'
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
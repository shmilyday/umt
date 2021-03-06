<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<fmt:setBundle basename="application" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title><fmt:message key='setSaftyMail.title'/></title>
		<link href="<umt:url value="/images/favicon.ico"/>" rel="shortcut icon"	type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
	</head>
	<body class="login">
		<jsp:include flush="true" page="banner2013.jsp">
			<jsp:param name="DecludeMenu" value="true"/>
		</jsp:include>
		<f:script src="${contextPath }/js/ValidateCode.js"/>   
		<f:script src="${contextPath }/js/string.js"/>
		
		<div class="container gray login">
			<h2 class="login-title"><fmt:message key='setSaftyMail.title'/></h2>
				<p class="hint"><fmt:message key="setSaftyMail.hint"/></p>
				<form id="securityEmailForm" class="form-horizontal" action="securityEmail.do?act=saveSecurityEmail&oper=set" method="post">
					<div class="control-group">
		              	<label class="control-label" for="password">
							<fmt:message key='common.securityEmail'/>
						</label>
		              	<div class="controls">
		              		<input id="securityEmail" name="securityEmail" type="text" />
							<span id="securityEmail_error_place" class="error help-inline"></span>		               		
		              	</div>
		            </div>
					<div class="control-group">
		              	<label class="control-label" for="password">
							<fmt:message key='changeSaftyMail.form.passportPassword'/>
						</label>
		              	<div class="controls">
		              		<input name="password" id="password" type="password" class="logininput" />	              		
							<span id="password_error_place" class="error help-inline">
								<c:if test="${!empty password_error}">
									<fmt:message key="${password_error }"/>
								</c:if>
							</span>
		              	</div>
		            </div>
		            <div class="control-group">
		              	<label class="control-label" for="password">
						</label>
		              	<div class="controls">
		              		<input type="submit" class="btn long btn-primary" value="<fmt:message key='changeSaftyMail.form.confirm'/>"/>
							<a href="<umt:url value="/user/safe.do?act=showSecurity"/>"/><fmt:message key='inputpassword.cancel'/></a>
		              	</div>
		            </div>
		            
				</form>
			</div>
			
		<jsp:include flush="true" page="bottom2013.jsp"></jsp:include>
		<script type="text/javascript">
		$(document).ready(function(){
			 $.validator.addMethod("canotEualsTo", function(value, element){
			    	return this.optional(element)||'${loginInfo.user.cstnetId}'!=value;
			  }, "<fmt:message key='changeSaftyMail.form.validate.DifferentSecurityEmail'/>");
			$('#securityEmailForm').validate({
				rules:{
					securityEmail:{
						required:true,
						<c:if test="${loginInfo.user.type=='coreMail'||loginInfo.user.type=='uc'}">
							canotEualsTo:true,
						</c:if>
						email:true
						},
					password:{
						required:true
					}
				},
				messages:{
					securityEmail:{
						 required:"<fmt:message key='setSaftyMail.form.validate.saftyMail.required'/>",
						 email:'<fmt:message key="common.validate.email.invalid"/>'
					 },
					 password:{
						 required:'<fmt:message key="common.validate.password.required"/>',
						 minlength:'<fmt:message key="common.validate.password.minlength"/>'
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
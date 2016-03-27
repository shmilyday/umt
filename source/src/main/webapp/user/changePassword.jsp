<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<fmt:setBundle basename="application" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key="remindpass.input.title"/></title> 
</head>
<body class="login">
<jsp:include flush="true" page="../banner2013.jsp"></jsp:include>


<div class="container login">
	<h1 class="login-title"><fmt:message key="remindpass.input.title"/></h1>
		<div class="login-left">
		    <form action="password.do?act=saveChangePassword" id="changePasswordForm" method="post">
				<table class="form_table">
					<tr>
						<th>
						</th>
						<td>
							<span class="text-quote"><fmt:message key='inputpassword.hint'/></span>
						</td>
					</tr>
					<tr>
						<th>
							<fmt:message key="regist.form.username"/>：
						</th>
						<td class="mail">
							${ email}
						</td>
					</tr>
					<tr>
						<th>
							<fmt:message key="current.password"/>：
						</th>
						<td>
							<input type="password"  class="logininput"
									id="oldpassword" name="oldpassword"/>
							<span id="oldpassword_error_place" class="error">
								<c:choose>
									<c:when test="${!empty oldpassword_error }">
										<fmt:message key="${oldpassword_error}"/>
									</c:when>
									<c:when test="${oldpasswordError=='true'}">
										<fmt:message key="current.password.error"/>
									</c:when>
								</c:choose>
							</span>
						</td>
					</tr>
					<tr>
						<th>
							<fmt:message key="inputpassword.newPassword"/>：
						</th>
						<td>
								<input type="password" class="logininput"
									name="password" id="password"/>
									<span id="password_error_place" class="error">
										<c:if test="${!empty password_error }">
											<fmt:message key='${password_error }'/>
										</c:if>
									</span>
						<div class="text-quote"><fmt:message key='inputpassword.newPassword.hint'/></div>
						</td>
					</tr>
					<tr>
						<th>
							<fmt:message key="remindpass.retype"/>：
						</th>
						<td>
							<input type="password" class="logininput"
								 id="retype" name="retype"/>
								<span id="retype_error_place" class="error">
									<c:if test="${!empty retype_error }">
											<fmt:message key='${retype_error }'/>
									</c:if>
								</span>
						</td>
					</tr>
					<tr>
						<th></th>
						<td>
							<input type="submit" class="btn long btn-primary" value="<fmt:message key="remindpass.submit"/>" />
							<a style="display:none" class="cancle"><fmt:message key="inputpassword.cancel"/></a>
						</td>
					</tr>
				</table>
				</form>
			</div>
			<div class="login-right">
				<h3><fmt:message key='common.duckling.QA'/></h3>
				<p><fmt:message key='common.duckling.defination'/></p>
				<p><fmt:message key='common.duckling.description'/><a href="http://www.escience.cn" target="_blank"><fmt:message key='common.escience'/></a>、<a href="http://ddl.escience.cn" target="_blank"><fmt:message key='common.ddl.escience'/></a>、<a href="http://csp.escience.cn" target="_blank"><fmt:message key='common.csp'/></a>、<a href="http://www.escience.cn/people" target="_blank"><fmt:message key='common.scholarPage'/></a>、<a href="http://mail.escience.cn" target="_blank"><fmt:message key='common.casMail'/></a><fmt:message key='common.duckling.description.more'/></p>
				<p><strong><fmt:message key='common.casMailLogin.hint'/></strong></p>
			</div>
			<div class="clear"></div>
		</div>
</div>
<jsp:include flush="true" page="../bottom2013.jsp"></jsp:include>
<script type="text/javascript">
$(document).ready(function(){
	$('#changePasswordForm').validate({
		 submitHandler:function(form){
			 form.submit();
		 },
		 rules: {
			 oldpassword:{required:true},
			 password:{required:true,minlength:8},
			 retype:{required:true,minlength:8,equalTo:"#password"}
		 },
		 messages: {
			 oldpassword:{
				 required:toRed('<fmt:message key="common.validate.oldpassword.required"/>'),
				 minlength:toRed('<fmt:message key="common.validate.password.minlength"/>')
			 },
			 password:{
				 required:toRed('<fmt:message key="common.validate.newpassword.required"/>'),
				 minlength:toRed('<fmt:message key="common.validate.password.minlength"/>')
			 },
			 retype:{
				 required:toRed('<fmt:message key="common.validate.renewpassword.required"/>'),
				 minlength:toRed('<fmt:message key="common.validate.password.minlength"/>'),
				 equalTo:toRed('<fmt:message key="common.validate.password.retype.not.equals"/>')
			 }
			 
		 },
		 errorPlacement: function(error, element){
			 var sub="_error_place";
			 var errorPlaceId="#"+$(element).attr("name")+sub;
			 	$(errorPlaceId).html("");
			 	error.appendTo($(errorPlaceId));
		}
	});
	function toRed(str){
		return "<font color='#cc0000'>"+str+'</font>';
	}
});	
</script>
</body>
</html>
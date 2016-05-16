<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<jsp:forward  page="findPswGuide.jsp" />
<fmt:setBundle basename="application" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html>
	<head>
		<title><fmt:message key="remindpass.title"/></title>
		<!-- <link href="css/umt.css" rel="stylesheet" type="text/css"/> -->
		<link href="<%= request.getContextPath() %>/images/favicon.ico" rel="shortcut icon"
			type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<f:script src="js/ValidateCode.js"/>
		<f:script src="js/string.js"/>
	</head>

	<body class="login">
		<jsp:include flush="true" page="banner2013.jsp"></jsp:include>
		<div class="container login">
			<h1 class="login-title"><fmt:message key="remindpass.title"/></h1>
			<div class="login-left">
				<form action="remindpass" method="post" id="passwordForm">
					<table class="form_table">
						<tr>
							<th>
								<fmt:message key='login.username'/>
							</th>
							<td>
								<input type="text" maxlength="255" name="username" id="username" />
								<span id="username_error_place" class="error">
								<c:choose>
									<c:when test="${!empty username_error}">
										<fmt:message key='${username_error }'/>
									</c:when>
									<c:when test="${!empty UserNameError}">
											<fmt:message key="remindpass.usernotfound"/>
									</c:when>
								</c:choose>
								</span>
								<div class="text-quote"><fmt:message key="remindpass.emailNotice"/></div>
							</td>
						</tr>
						<tr>
							<th>
								<fmt:message key="remindpass.imagetext"/>
							</th>
							<td>
									<input type="text" maxlength="255" class="logininput" id="ValidCode"
										name="ValidCode"/>
								<img id="ValidCodeImage" src="" />
								<a class="small_link" href="#" onclick="imageObj.changeImage()"><fmt:message key="remindpass.changeit"/></a>
								<span id="ValidCode_error_place" class="error">
								<c:choose>
									<c:when test="${!empty ValidCode_error}">
										<fmt:message key='${ValidCode_error }'/>
									</c:when>
									<c:when test="${!empty ValideCodeError}">
										<fmt:message key="remindpass.imagetext.wrong"/>
									</c:when>
								</c:choose>
								</span>
							</td>
						</tr>
						<tr>
							<th></th>
							<td>
								<input type="submit" class="btn long btn-primary" value="<fmt:message key="remindpass.submit"/>" />
								<a style="display:none" class="cancle"><fmt:message key='inputpassword.cancel'/></a>
							</td>
						</tr>
					</table>
					<div class="login-b">
						<p>
							<fmt:message key='common.login.noDuckling'/>
							 <umt:registerLink> 
							    <fmt:message key='common.login.registNow'/>
							 </umt:registerLink>
						</p> 
					</div>
				</form>
			</div>
			<div class="login-right">
				<h3><fmt:message key='common.duckling.QA'/></h3>
				<p><fmt:message key='common.duckling.defination'/></p>
				<p><fmt:message key='common.duckling.description'/><a href="http://www.escience.cn" target="_blank"><fmt:message key='common.escience'/></a><fmt:message key='common.quote'/><a href="http://ddl.escience.cn" target="_blank"><fmt:message key='common.ddl.escience'/></a><fmt:message key='common.quote'/><a href="http://csp.escience.cn" target="_blank"><fmt:message key='common.csp'/></a><fmt:message key='common.quote'/><a href="http://www.escience.cn/people" target="_blank"><fmt:message key='common.scholarPage'/></a><fmt:message key='common.quote'/><a href="http://mail.escience.cn" target="_blank"><fmt:message key='common.casMail'/></a><fmt:message key='common.duckling.description.more'/></p>
				<p><strong><fmt:message key='common.casMailLogin.hint'/></strong></p>
			</div>
			<div class="clear"></div>
		</div>
		
		<jsp:include flush="true" page="bottom2013.jsp"></jsp:include>
		<script type="text/javascript">
			var imageObj;
			$(document).ready(function(){
				imageObj = ValidateImage("ValidCodeImage");
				$('#passwordForm').validate({
					 submitHandler:function(form){
						 form.submit();
					 },
					 rules: {
						 username:{required:true,email:true},
						 ValidCode:{required:true}
					 },
					 messages: {
						 username: {
							 required:toRed("<fmt:message key='common.validate.email.required'/>"),
							 email:toRed('<fmt:message key="common.validate.email.invalid"/>')
						 },
						 ValidCode:{
							 required:toRed('<fmt:message key="common.validate.validateCode.required"/>')
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

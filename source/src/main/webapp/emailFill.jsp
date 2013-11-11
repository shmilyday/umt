<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>

<fmt:setBundle basename="application" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title><fmt:message key="weibo.fill.email"/></title>
		<link href="images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
	</head>

	<body class="login">
	
		<jsp:include flush="true" page="banner2013.jsp">
			<jsp:param name="DecludeMenu" value="true"/>
			<jsp:param name="showLogin" value="false"/>
		</jsp:include>
		<div class="container gray login">
			<h2 class="login-title"><fmt:message key="weibo.fill.email.add"/><span class="mail-title"> <span class="cstnet"><fmt:message key='email.fill.hint'/></span></span></h2>
				<form id="submitForm" action="login" method="post">
					<table class="form_table">
						<tr>
							<th><fmt:message key='userinfo.truename'/></th>
							<td>
								<input maxlength="30" type="text" id="user_truename" name="user_truename" value="${trueName}" />
								<span id="user_truename_error_place" class="error">
								
								</span>
							</td>
						</tr>
						<tr>
						   <th><fmt:message key='common.email'/></th>
						   <td>
						   <input type="text" id="user_email" name="user_email" />
							   <span id="user_email_error_place" class="error">
							   
							   </span>
						   </td>
						</tr>
						<c:if test="${error!=null}">
						<tr><th></th>
							<td style="color: red">
								<fmt:message key="weibo.fill.error">
									<fmt:param value="<a href='${ error}'>"/>
									<fmt:param value="</a>"/>
								</fmt:message>
							</td>
						</tr>
						</c:if>
						<tr>
						   <th>
						   		<input type="hidden" value="Validate" name="act" />
						   		<c:choose><c:when test="${\"weibo\".equals(param.type)}">
									<input type="hidden" value="weibo" name="type" />
									<input type="hidden" value="weibo" name="authBy" />
									<input type="hidden" value="${param.oauth_verifier}" name="oauth_verifier" />
								</c:when><c:when test="${\"qq\".equals(param.type)}">
									<input type="hidden" value="qq" name="type" />
									<input type="hidden" value="qq" name="authBy" />
								</c:when></c:choose>
							</th>
							<td><input type="hidden" value="${openId}" name="openId" />
							<input type="hidden" value="${accessToken}" name="accessToken" />
							<input type="submit" class="btn long btn-primary" value="<fmt:message key='userinfo.submit'/>" onclick="" /></td>
						</tr>
					</table>
				</form>
		</div>
		<jsp:include flush="true" page="bottom2013.jsp"></jsp:include>
	</body>
			<script>
		$(document).ready(function(){
			$('#submitForm').validate({
				 submitHandler:function(form){
					 form.submit();
				 },
				 rules: {
					 user_truename:{required:true},
					 user_email:{required:true,email:true}
				 },
				 messages: {
					 user_truename:{
						 required:'<fmt:message key="common.validate.truename.required"/>'
					 },
					 user_email:{
						 required:'<fmt:message key="common.validate.email.required"/>',
						 email:'<fmt:message key="common.validate.email.invalid"/>'
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
</html>

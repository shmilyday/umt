<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<fmt:setBundle basename="application" />
<umt:AppList/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	request.setAttribute("msg", request.getParameter("msg"));
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title><fmt:message key='accountManage.changePassword.title'/></title>
		<link href="<umt:url value="/images/favicon.ico"/>" rel="shortcut icon" type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
	</head>
	<body class="login">
		<jsp:include flush="true" page="banner2013.jsp">
			<jsp:param name="DecludeMenu" value="true"/>
		</jsp:include>
		
		<div class="container login gray">
			<ul class="sub-nav">
				<li id="accountManage"><a href="<umt:url value="/user/manage.do?act=showManage"/>"><fmt:message key='accountManage.accountInfo'/></a></li>
				<c:if test="${'qq'!=loginInfo.user.type||'weibo'!=loginInfo.user.type }">
					<li class="active" id="changePsw"><a href="<umt:url value="/user/manage.do?act=showChangePassword"/>"><fmt:message key='accountManage.changePassword.title'/></a></li>
				</c:if>
				<li id="accountBind"><a href="<umt:url value="/user/manage.do?act=showBindAccount"/>"><fmt:message key='accountManage.bind.title'/></a></li>
				<div class="clear"></div>
			</ul>
			<div class="sub-content" id="changePswShow">
				<h4 class="sub-title"><fmt:message key='accountManage.changePassword.title'/></h4>
				<p class="sub-text"><fmt:message key='accountManage.changePassword.hint'/></p>
				<form action="<umt:url value="/user/password.do?act=saveChangePassword"/>" class="form-horizontal" id="changePasswordForm" method="post">
					<div class="control-group" style="position:relative;">
		              	<label class="control-label" for="password">
							<fmt:message key="current.password"/>
						</label>
		              	<div class="controls" style="position:relative">
		              		<input type="password"  class="logininput" id="oldpassword" name="oldpassword"/>
		              		<span id="oldpassword_error_place" class="error help-inline">
		              			<c:choose>
									<c:when test="${!empty oldpassword_error }">
										<fmt:message key="${oldpassword_error}"/>
									</c:when>
									<c:when test="${oldpasswordError=='true'}">
										<fmt:message key="current.password.error"/>
									</c:when>
								</c:choose>
							</span>
		              	</div>
		            </div>
		            <div class="control-group">
		              	<label class="control-label" for="password">
							<fmt:message key="inputpassword.newPassword"/>
						</label>
		              	<div class="controls">
		              		<input type="password" class="logininput" name="password" id="password"/>
		              		<span id="password_error_place" class="error help-inline">
								<c:if test="${!empty password_error }">
									<fmt:message key='${password_error }'/>
								</c:if>
							</span>
		              		<div class="help-block text-quote"><fmt:message key='inputpassword.newPassword.hint'/></div>
		              	</div>
		            </div>
					<div class="control-group">
		              	<label class="control-label" for="password">
							<fmt:message key="inputpassword.retype"/>
						</label>
		              	<div class="controls">
		              		<input type="password" class="logininput" id="retype" name="retype"/>
		              		<span id="retype_error_place" class="error help-inline">
								<c:if test="${!empty retype_error }">
										<fmt:message key='${retype_error }'/>
								</c:if>
							</span>
							<div id="" class="help-block text-quote">
								<fmt:message key="accountManage.changePassword.hint2"/>
							</div>
		              	</div>
		            </div>
					<div class="control-group">
		              	<div class="controls">
		              		<button type="submit" class="btn long btn-primary"><fmt:message key='userinfo.submit'/></button>
		              	</div>
		            </div>		
				</form>
			</div>
		</div>
		
		<jsp:include flush="true" page="bottom2013.jsp"></jsp:include>
		
	</body>
	
	<script type="text/javascript" >
		$(document).ready(function(){
			//validate
			$('#changePasswordForm').validate({
				 submitHandler:function(form){
					 form.submit();
				 },
				 rules: {
					 oldpassword:{required:true},
					 password:{required:true,minlength:8,passwordNotEquals:{
						 notEquals:function(){
							 return '${ email}';
						 }},
						 passwordAllSmall:true,
						 passwordAllNum:true,
						 passwordAllBig:true,
						 passwordHasSpace:true},
					 retype:{required:true,minlength:8,equalTo:"#password"}
				 },
				 messages: {
					 oldpassword:{
						 required:toRed('<fmt:message key="common.validate.oldpassword.required"/>'),
						 minlength:toRed('<fmt:message key="common.validate.password.minlength"/>')
					 },
					 password:{
						 required:toRed('<fmt:message key="common.validate.newpassword.required"/>'),
						 minlength:toRed('<fmt:message key="common.validate.password.minlength"/>'),
						 passwordNotEquals:'<fmt:message key="common.validate.password.notEuals"/>',
						 passwordAllSmall:'<fmt:message key="common.validate.password.allSmall"/>',
						 passwordAllNum:'<fmt:message key="common.validate.password.allNumber"/>',
						 passwordAllBig:'<fmt:message key="common.validate.password.allBig"/>',
						 passwordHasSpace:'<fmt:message key="common.validate.password.hasSpace"/>'
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
			$('#banner_user_manage').addClass("active");
			$("#showpopover").live("click",function(){
				$(".popover.right").show();
			})
		});
	</script>
</html>
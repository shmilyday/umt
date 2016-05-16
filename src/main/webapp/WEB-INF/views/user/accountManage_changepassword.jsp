<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<fmt:setBundle basename="application" />
<umt:AppList />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	request.setAttribute("msg", request.getParameter("msg"));
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><fmt:message key='accountManage.changePassword.title' /></title>
<link href="<%= request.getContextPath() %>/images/favicon.ico"
	rel="shortcut icon" type="image/x-icon" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<style type="text/css">
span.pass-info {
	font-size: 11px;
	color: #777;
	background: #ddd;
	display: inline;
	padding: 3px 20px;
}

span.pass-info.red {
	background: #f66;
	color: #fff;
}

span.pass-info.orange {
	background: #fc6;
	color: #fff;
}

span.pass-info.green {
	background: #A3F756;
	color: #fff;
}
</style>
</head>
<body class="login">
	<jsp:include flush="true" page="../../../banner2013.jsp">
		<jsp:param name="DecludeMenu" value="true" />
	</jsp:include>

	<div class="container login gray">
		<ul class="sub-nav">
			<li id="accountManage"><a
				href="<umt:url value="/user/manage.do?act=showManage"/>"><fmt:message
						key='accountManage.accountInfo' /></a></li>
			<c:if
				test="${'qq'!=loginInfo.user.type||'weibo'!=loginInfo.user.type }">
				<li class="active" id="changePsw"><a
					href="<umt:url value="/user/manage.do?act=showChangePassword"/>"><fmt:message
							key='accountManage.changePassword.title' /></a></li>
			</c:if>
			<li id="accountApp"><a
				href="<umt:url value="/user/manage.do?act=appAccessPwd"/>"><fmt:message
						key='app.access.pwd'></fmt:message></a></li>
			<li id="accountBind"><a
				href="<umt:url value="/user/manage.do?act=showBindAccount"/>"><fmt:message
						key='accountManage.bind.title' /></a></li>
		<div class="clear" />
		</ul>
		
		<div class="sub-content" id="changePswShow">
			<c:if test="${showCoremailTip=='true' }">
				<div class="alert" style="margin:0px 2%">
					<p><fmt:message key='changepwd.prompt.coremail.1'/></p>
					
					<p><fmt:message key='changepwd.prompt.coremail.2'/></p>
					<p><a href="${ returnUrl}" class="btn btn-small btn-warning"><fmt:message key='changepwd.prompt.goto.coremail'/></a></p>
				</div>
			</c:if> 
			<h4 class="sub-title">
				<fmt:message key='accountManage.changePassword.title' />
				<c:if test="${loginInfo.weak }">
					<span style="color: red; margin-left: 50px; font-size: 15px"><fmt:message
							key='account.manage.weak.password.hint' /></span>
				</c:if>
			</h4>

			<p class="sub-text">
				<fmt:message key='accountManage.changePassword.hint' />
			</p>
			<form
				action="<umt:url value="/user/password.do?act=saveChangePassword"/>"
				class="form-horizontal" id="changePasswordForm" method="post">
				<div class="control-group" style="position: relative;">
					<label class="control-label" for="password"> <fmt:message
							key="current.password" />
					</label>
					<div class="controls" style="position: relative">
						<input type="password" class="logininput" id="oldpassword"
							name="oldpassword" /> <span id="oldpassword_error_place"
							class="error help-inline"> <c:choose>
								<c:when test="${!empty oldpassword_error }">
									<fmt:message key="${oldpassword_error}" />
								</c:when>
								<c:when test="${oldpasswordError=='true'}">
									<fmt:message key="current.password.error" />
								</c:when>
							</c:choose>
						</span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="password"> <fmt:message
							key="inputpassword.newPassword" />
					</label>
					<div class="controls">
						<input type="password" class="logininput" name="password"
							id="password" /> <span id="password_error_place"
							class="error help-inline"> <c:if
								test="${!empty password_error }">
								<fmt:message key='${password_error }' />
							</c:if>
						</span>
						<div>
							<span style="color: #999; margin-top: 7px; font-size: 13px;"><fmt:message
									key='inputpassword.newPassword.hint'></fmt:message></span> <span
								id="passwordWeak" class="pass-info "><fmt:message
									key='regist.passwd.weak' /></span> <span id="passwordMiddle"
								class="pass-info "><fmt:message
									key='regist.passwd.middle' /></span> <span id="passwordStrong"
								class="pass-info "><fmt:message
									key='regist.passwd.strong' /></span>
						</div>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="password"> <fmt:message
							key="inputpassword.retype" />
					</label>
					<div class="controls">
						<input type="password" class="logininput" id="retype"
							name="retype" /> <span id="retype_error_place"
							class="error help-inline"> <c:if
								test="${!empty retype_error }">
								<fmt:message key='${retype_error }' />
							</c:if>
						</span>
						<div id="" class="help-block text-quote">
							<fmt:message key="accountManage.changePassword.hint2" />
						</div>
					</div>
				</div>
				<div class="control-group">
					<div class="controls">
						<button type="submit" class="btn long btn-primary">
							<fmt:message key='userinfo.submit' />
						</button>
					</div>
				</div>
			</form>
			<p class="sub-text" style="color:#333;"><strong><fmt:message key='changepwd.prompt.password.tips'/></strong></p>
			
			<ul class="sub-text" style="margin-left:10%">
				<li><fmt:message key='changepwd.prompt.weak'/></li>
				
				<li><fmt:message key='changepwd.prompt.middle'/></li>
				
				<li><fmt:message key='changepwd.prompt.strong'/></li>
				
			</ul>
		</div>
	</div>

	<jsp:include flush="true" page="../../../bottom2013.jsp"></jsp:include>

	<script type="text/javascript">
		$(document).ready(function(){
			$('#password').on('keyup',function(){
				var passwd=$.trim($(this).val());
				var allSmall=passWordAllSmall(passwd);
				var allNum=passWordAllNum(passwd);
				var allBig=passWordAllBig(passwd);
				if(passwd==''||passwd.length<8||allSmall||allNum||allBig){
					toColor('red');
					return;
				}
				var count=0;
				if(passWordHasSmall(passwd)){
					count++;
				}
				if(passWordHasBig(passwd)){
					count++;
				}
				if(passWordHasNum(passwd)){
					count++;
				}
				if(passWordHasSpecial(passwd)){
					count++;
				}
				if(count<3){
					toColor('orange');
				}else{
					toColor('green');
				}
			});
			function toColor(color){
				$('.pass-info').attr('class','pass-info');
				switch(color){
				case 'red':{
					$('#passwordWeak').addClass('red');
					return;
				}
				case 'orange':{
					$('#passwordMiddle').addClass('orange');
					return;
				}
				case 'green':{
					$('#passwordStrong').addClass('green');
					return;
				}
				}
			}
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
						 passwordHasSpace:true,
						 remote:{
							url:'<umt:url value="/user/password.do?act=isPasswordUsed"/>',
							type:'post'
						 }
					},
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
						 passwordHasSpace:'<fmt:message key="common.validate.password.hasSpace"/>',
						 remote:'<fmt:message key="password.validate.not.equals.to.app.pwd"/>'
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
</body>
</html>
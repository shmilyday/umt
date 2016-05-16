<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<fmt:setBundle basename="application" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html>
	<head>
		<title><fmt:message key="inputpassword.title"/></title>
		<link href="<%= request.getContextPath() %>/images/favicon.ico" rel="shortcut icon"	type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
			<style type="text/css">
		span.pass-info{
		font-size:11px;
		color:#777;
		background:#ddd;
		display:inline;
		padding:3px 20px;
		}
		span.pass-info.red{background:#f66;color:#fff;}
		span.pass-info.orange{background:#fc6;color:#fff;}
		span.pass-info.green{background:#A3F756;color:#fff;}
	</style>
	</head>

	<body class="login">
		<jsp:include flush="true" page="banner2013.jsp"></jsp:include>
		<div class="container gray login">
			<h2 class="login-title"><fmt:message key="usermanage.reset.title"/></h2>
				<form id="changepassForm" class="form-horizontal" style="padding:30px 5%" action="changepass?act=updatepass" method="post">
					<div class="control-group">
		              	<label class="control-label nopadding">
		              		<input type="hidden" name="act" value="updatepass"/>
		              		<fmt:message key="remindpass.username"/>
						</label>
		              	<div class="controls">
		              		${username}
		              	</div>
		            </div>
					<div class="control-group">
		              	<label class="control-label nopadding">
		              		<fmt:message key="inputpassword.newPassword"/>
						</label>
		              	<div class="controls">
		              		<input id="password" type="password" size="255" class="logininput"
								name="password"/>
							<span id="password_error_place" class="error help-inline">
								<c:if test="${!empty password_error }">
									<fmt:message key="${password_error }"/>
								</c:if>
							</span>
							<div>
						<span style="color: #999;margin-top: 7px;font-size: 13px;"><fmt:message key='inputpassword.newPassword.hint'></fmt:message></span>
						<span id="passwordWeak" class="pass-info "><fmt:message key='regist.passwd.weak'/></span>
						<span id="passwordMiddle" class="pass-info "><fmt:message key='regist.passwd.middle'/></span>
						<span id="passwordStrong" class="pass-info "><fmt:message key='regist.passwd.strong'/></span>
					</div>
		              	</div>
		            </div>
		            <div class="control-group">
		              	<label class="control-label nopadding">
		              		<fmt:message key="remindpass.retype"/>
						</label>
		              	<div class="controls">
		              		<input type="password" size="255" class="logininput"
							   id="retype"	name="retype"/>
							<span id="retype_error_place" class="error help-inline">
								<c:if test="${!empty retype_error }">
									<fmt:message key="${retype_error }"/>
								</c:if>
							</span>
		              	</div>
		            </div>
		            <div class="control-group">
		              	<div class="controls">
		              		<input type="submit" value="<fmt:message key="remindpass.submit"/>" class="btn long btn-primary" />
		              	</div>
		            </div>
				</form>
		</div>

		<jsp:include flush="true" page="bottom2013.jsp"></jsp:include>
		<script type="text/javascript">
		$(document).ready(function(){
			$('#banner_forgot_password').addClass("active");
			$("#changepassForm").validate({
				 submitHandler:function(form){
					 form.submit();
				 },
				 rules: {
					 password:{required:true,minlength:8,passwordNotEquals:{
						 notEquals:function(){
							 return '${username}';
						 }},
						 remote:{
							 url:'<umt:url value="/changepass?act=isPasswordCanUse"/>',
							 type:'post',
							 data:{
								'tokenId':'${TokenObject.id}'								 
							 }
							 
						 },
						 passwordAllSmall:true,
						 passwordAllNum:true,
						 passwordAllBig:true,
						 passwordHasSpace:true},
					 retype:{required:true,minlength:8,equalTo:'#password'}
				 },
				 messages: {
					 password:{
						 remote:'<fmt:message key="password.validate.not.equals.to.app.pwd"/>',
						 required:toRed('<fmt:message key="common.validate.newpassword.required"/>'),
						 minlength:toRed('<fmt:message key="common.validate.password.minlength"/>'),
						 passwordNotEquals:'<fmt:message key="common.validate.password.notEuals"/>',
						 passwordAllSmall:'<fmt:message key="common.validate.password.allSmall"/>',
						 passwordAllNum:'<fmt:message key="common.validate.password.allNumber"/>',
						 passwordAllBig:'<fmt:message key="common.validate.password.allBig"/>',
						 passwordHasSpace:'<fmt:message key="common.validate.password.hasSpace"/>'
					 },
					 retype:{
						 required:toRed('<fmt:message key="common.validate.repassword.required"/>'),
						 minlength:toRed('<fmt:message key="common.validate.repassword.minlength"/>'),
						 equalTo:toRed('<fmt:message key="common.validate.password.retype.not.equals"/>')
					 },
					 
				 },
				 errorPlacement: function(error, element){
					 var sub="_error_place";
					 var errorPlaceId="#"+$(element).attr("name")+sub;
					 	$(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));
				}
			});
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
			function toRed(str){
				return "<font color='#cc0000'>"+str+'</font>';
			}
		});
		</script>
	</body>
</html>

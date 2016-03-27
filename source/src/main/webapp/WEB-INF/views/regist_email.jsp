<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<fmt:setBundle basename="application" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='regist.email.title'/></title>
<link href="<%= request.getContextPath() %>/images/favicon.ico" rel="shortcut icon"	type="image/x-icon" />
<link rel="stylesheet" type="text/css" href="<umt:url value="/css/umt2013-responsive.css"/>" />
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

<jsp:include flush="true" page="/banner2013.jsp"></jsp:include>
<f:script src="js/ValidateCode.js"/>
<f:script  src="js/string.js"/>
<div class="container login">
		<div class="login-left span8">
			<form action="createRequest.do?act=saveToEmail" method="post" id="createRequestForm" class="form-horizontal">
				<input type="hidden" name="act" value="saveToEmail"/>
				<h2 class="login-title both">
				<fmt:message key='regist.email.title'/>   
				<span class="mail-title"> 
					<fmt:message key='common.duckling'/> 
				</span>
				<a href="<umt:url value='/'/>">
				 <span class="btn btn-success hide768">
				   <fmt:message key='common.regist.signinNow'/>
				 </span>
			 </a>
			</h2>
				<div class="control-group">
	              	<label class="control-label" for="username">
						<span class="ness">*</span><fmt:message key='login.username'/>
					</label>
	              	<div class="controls">
	               		<input type="text" style="width:10em;" id="username" value="${form.username }" maxlength="255" name="username" tabindex="1"/> 
						<span class="escience-mail"> @ <umt:config key="umt.coremail.api.email.domain"/></span>
						<span id="username_error_place" class="error help-inline">
						<c:if test="${!empty username_error }">
							<fmt:message key="${username_error }"/>
						</c:if>
						</span>
	               		<span class="help-block text-quote">
	               			<a href="${empty isFromApp?'regist.jsp':(isFromApp?registURL:'regist.jsp') }"><fmt:message key='regist.toUmt'/></a>
						</span>
	              	</div>
	            </div>
	            <div class="control-group">
	              	<label class="control-label" for="truename">
						<span class="ness">*</span><fmt:message key='regist.form.truename'/>
					</label>
	              	<div class="controls">
	               		<input type="text" maxlength="30" name="truename" id="truename" value="${form.truename }" tabindex="2"/>
						<span id="truename_error_place" class="error help-inline">
						<c:if test="${!empty truename_error }">
							<fmt:message key="${truename_error }"/>
						</c:if>
						</span>
	              	</div>
	            </div>
	            <div class="control-group">
	              	<label class="control-label" for="password">
						<span class="ness">*</span><fmt:message key='regist.form.password'/>
					</label>
	              	<div class="controls">
	               		<input type="password" maxlength="255" name="password" id="password" tabindex="3"/>
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
	              	<label class="control-label" for="repassword">
						<span class="ness">*</span><fmt:message key='regist.form.retype'/>
					</label>
	              	<div class="controls">
	               		<input type="password" maxlength="255" name="repassword" id="repassword" tabindex="4"/>
						<span id="repassword_error_place" class="error help-inline">
						<c:if test="${!empty repassword_error }">
							<fmt:message key="${repassword_error }"/>
						</c:if>
						</span>
	              	</div>
	            </div>
	            <div class="control-group">
	              	<label class="control-label" for="tempSecurityEmail">
						<fmt:message key='common.securityEmail'/>
					</label>
	              	<div class="controls">
	               		<input type="text" maxlength="255" value="${form.tempSecurityEmail }" name="tempSecurityEmail" id="tempSecurityEmail" tabindex="5"/>
						<span id="tempSecurityEmail_error_place" class="error help-inline">
						<c:if test="${!empty tempSecurityEmail_error }">
							<fmt:message key="${tempSecurityEmail_error }"/>
						</c:if>
						</span>
	              	</div>
	            </div>
	            <div class="control-group">
	              	<label class="control-label" for="validcode">
						<span class="ness">*</span><fmt:message key='regist.form.imagetext'/>
					</label>
	              	<div class="controls">
	               		<input type="text" class="logininput" style="width:5em;" maxlength="255" id="validcode" name="validcode" tabindex="7"/>
						<img id="ValidCodeImage" src="" />
						<a class="small_link" onclick="imageObj.changeImage();$('#validcode').val('');"><fmt:message key='regist.form.changeit'/></a>
						<span id="validcode_error_place" class="error help-inline">
							<c:if test="${!empty validcode_error }">
								<fmt:message key="${validcode_error }"/>
							</c:if>
						</span>
	              	</div>
	            </div>
	            <div class="control-group">
	              	<div class="controls">
	               		<input type="submit" class="btn btn-large btn-primary long" value="<fmt:message key='regist.form.submit'/>" tabindex="8"/>
	              	</div>
	            </div>
			<input type="hidden" name="loginUrl" value="${loginURL}">
			<input type="hidden" name="appName" value="${appName}"/>
		</form>
		</div>
			<div class="login-right span4">
				<p class="header">
					<fmt:message key='common.regist.hasDucklingPassport'/>
					<br></br>
					<a href="<umt:url value='/'/>">
					<span class="btn btn-success">
					   		<fmt:message key='common.regist.signinNow'/>
					</span>
					 </a>
				</p>
				<p class="sub-header">
					<fmt:message key="common.login.userOtherName"/>
					<a href="${pageContext.request.contextPath}/thirdParty/login?type=weibo"><img src="images/login/weibo.png" alt="用新浪微博登录" /></a>
					<a href="${pageContext.request.contextPath}/thirdParty/login?type=uaf"><img src="images/login/uaf.png" alt="使用UAF登录" /></a>
				</p>
				<h3><fmt:message key='common.duckling.QA'/></h3>
				<p class="sub-gray-text"><fmt:message key='common.duckling.description'/><a href="http://www.escience.cn" target="_blank"><fmt:message key='common.escience'/></a><fmt:message key='common.quote'/><a href="http://ddl.escience.cn" target="_blank"><fmt:message key='common.ddl.escience'/></a><fmt:message key='common.quote'/><a href="http://csp.escience.cn" target="_blank"><fmt:message key='common.csp'/></a><fmt:message key='common.quote'/><a href="http://www.escience.cn/people" target="_blank"><fmt:message key='common.scholarPage'/></a><fmt:message key='common.quote'/><a href="http://mail.escience.cn" target="_blank"><fmt:message key='common.casMail'/></a><fmt:message key='common.duckling.description.more'/></p>
				<p><strong><fmt:message key='common.casMailLogin.hint1'/><a href="login"><fmt:message key='regist.login'/></a>  <fmt:message key='common.casMailLogin.hint2'/></strong></p>
				<p><strong><fmt:message key='common.duckling.defination'/></strong></p>
			</div>
		<div class="clear"></div>
</div>
<jsp:include flush="true" page="/bottom2013.jsp"></jsp:include>
<script type="text/javascript">
	var imageObj;
	$(document).ready(function(){
		
		$('#banner_regist').addClass("active");
		imageObj = ValidateImage("ValidCodeImage");
		
		
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
		
	    $.validator.addMethod("emailRegex", function(value, element){
	    	var regex = /^[a-zA-Z0-9\-]+$/;
	    	return this.optional(element)||(regex.test(value));
	    }, toRed("<fmt:message key='common.validate.escience.email.invalid'/>"));
	    
	    
	    $.validator.addMethod("canotEualsTo", function(value, element){
	    	return this.optional(element)||$('#username').val()+"@"+'<umt:config key="umt.coremail.api.email.domain"/>'!=value;
	    }, toRed("<fmt:message key='changeSaftyMail.form.validate.DifferentSecurityEmail'/>"));
	    
	    $('#createRequestForm').validate({
			 submitHandler:function(form){
				 form.submit();
			 },
			 rules: {
				 username:{
					 required:true,
					 emailRegex:true,
					 minlength:3,
					 remote:{
						 type: "GET",
						 url: 'createRequest.do',
						 data:{ 
							 'act':'usercheck',
							 "username":function(){
											return $("#username").val();
									    }	   		
				  		}
					 }
			 	 },
				 truename:{required:true},
				 password:{required:true,minlength:8,passwordNotEquals:{
					 notEquals:function(){
						 return $('#username').val()+'@<umt:config key="umt.coremail.api.email.domain"/>';
					 }},
				 passwordAllSmall:true,
				 passwordAllNum:true,
				 passwordAllBig:true,
				 passwordHasSpace:true
				 },
				 repassword:{required:true,equalTo:"#password"},
				 tempSecurityEmail:{email:true,canotEualsTo:true},
				 validcode:{
					 required:true,
					 remote:{
						 type: "GET",
						 url: 'jq/validate.do?act=validateCode',
					 }}
			 },
			 messages: {
				 username: {
					 required:toRed('<fmt:message key="common.validate.email.required"/>'),
					 remote:toRed('<fmt:message key="regist.user.exist"/>'),
					 minlength:"<fmt:message key='input.email.length'/>"
				 },
				 truename:{required:toRed('<fmt:message key="common.validate.truename.required"/>')},
				 password:{
					 required:toRed('<fmt:message key="common.validate.password.required"/>'),
					 minlength:toRed('<fmt:message key="common.validate.password.minlength"/>'),
					 passwordNotEquals:'<fmt:message key="common.validate.password.notEuals"/>',
					 passwordAllSmall:'<fmt:message key="common.validate.password.allSmall"/>',
					 passwordAllNum:'<fmt:message key="common.validate.password.allNumber"/>',
					 passwordAllBig:'<fmt:message key="common.validate.password.allBig"/>',
					 passwordHasSpace:'<fmt:message key="common.validate.password.hasSpace"/>'
				 },
				 repassword:{
					 required:toRed('<fmt:message key="common.validate.repassword.required"/>'),
					 minlength:toRed('<fmt:message key="common.validate.repassword.minlength"/>'),
					 equalTo:toRed('<fmt:message key="common.validate.password.retype.not.equals"/>')
				},
				tempSecurityEmail:{
					required:'<fmt:message key="common.validate.securityEmail.required"/>',
					email:'<fmt:message key="common.validate.email.invalid"/>'
				},
				validcode:{
					 required:toRed('<fmt:message key="common.validate.validateCode.required"/>'),
					 remote:toRed('<fmt:message key="common.validate.validateCode.wrong"/>')
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
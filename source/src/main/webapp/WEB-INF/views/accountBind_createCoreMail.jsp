<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<c:if test="${empty sessionScope.thirdParty_type }">
	<jsp:forward page="login"/>"/>
</c:if>
<fmt:setBundle basename="application" />
<umt:AppList/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	request.setAttribute("msg", request.getParameter("msg"));
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title><fmt:message key='thirdParty.bind.title'/></title>
		<link href="<%= request.getContextPath() %>/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
	</head>
	<body class="login">
		<jsp:include flush="true" page="/banner2013.jsp">
			<jsp:param name="DecludeMenu" value="true"/>
		</jsp:include>
		
		<div class="container login gray">
			<jsp:include page="accountBind_login.jsp"/>
			<!-- 注册 -->
			<p class="hasMarign small-font"><strong><fmt:message key='thirdParty.bind.or.create.hint'/></strong></p>
			<form action="<umt:url value="/bind.do"/>?act=createEmailAndBindUmt" class="form-horizontal" method="post" id="createAndBindForm">
				<input type="hidden" name="needValidCode" value="false"/>
				<input type="hidden" name="screenName" value="${thirdParty_user}"/>
				<input type="hidden" name="openId" value="${thirdParty_openId}"/>
				<input type="hidden" name="type" value="${thirdParty_type }"/>
				<div class="control-group">
		             <label class="control-label" for="username">
						<span class="ness">*</span><fmt:message key='login.username'/>
					 </label>
	             	<div class="controls">
	              		<input type="text" style="width:10em;" id="username" maxlength="255" name="username" tabindex="1"/> 
						<span class="escience-mail"> @ <umt:config key="umt.coremail.api.email.domain"/></span>
						<span id="username_error_place" class="error help-inline">
							<c:if test="${!empty username_error }">
								<fmt:message key="${username_error }"/>
							</c:if>
						</span>
						<div class="text-quote help-block"><a href="accountBind_createUmt.jsp"><fmt:message key='regist.toUmt'/></a></div>
	             	</div>
	           </div>
	           <div class="control-group">
		             <label class="control-label" for="truename">
						<span class="ness">*</span><fmt:message key='regist.form.truename'/>
					 </label>
	             	<div class="controls">
	             		<input type="text" maxlength="30" name="truename" id="truename" tabindex="2" value="${thirdParty_user}"/>
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
						<div class="text-quote help-block"><fmt:message key='regist.passpord.hint'/></div>
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
	             		<input type="text" maxlength="255" name="tempSecurityEmail" id="tempSecurityEmail" tabindex="5"/>
						<span id="tempSecurityEmail_error_place" class="error">
							<c:if test="${!empty tempSecurityEmail_error }">
								<fmt:message key="${tempSecurityEmail_error }"/>
							</c:if>
						</span>
	             	</div>
	           </div>
	           <div class="control-group">
	             	<div class="controls">
	             		<input type="submit" class="btn long btn-primary" value="<fmt:message key='regist.form.submit'/>" tabindex="6"/>
	             	</div>
	           </div>
			</form>
		</div>
		<jsp:include flush="true" page="/bottom2013.jsp"></jsp:include>
		<script type="text/javascript">
		$.validator.addMethod("emailRegex", function(value, element){
	    	var regex = /^[a-zA-Z0-9\-]+$/;
	    	return this.optional(element)||(regex.test(value));
	    }, "<fmt:message key='common.validate.escience.email.invalid'/>");
	    
	    
	    $.validator.addMethod("canotEualsTo", function(value, element){
	    	return this.optional(element)||$('#username').val()+"@"+'<umt:config key="umt.coremail.api.email.domain"/>'!=value;
	    },"<fmt:message key='changeSaftyMail.form.validate.DifferentSecurityEmail'/>");
	    
		$('#createAndBindForm').validate({
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
						 return $('#username').val();
					 }},
					 passwordAllSmall:true,
					 passwordAllNum:true,
					 passwordAllBig:true,
					 passwordHasSpace:true},
				 repassword:{required:true,equalTo:"#password"},
				 tempSecurityEmail:{email:true,canotEualsTo:true}
			 },
				 messages: {
					 username: {
						 required:'<fmt:message key="common.validate.email.required"/>',
						 remote:'<fmt:message key="regist.user.exist"/>',
						 minlength:"<fmt:message key='input.email.length'/>"
					 },
					 truename:{required:'<fmt:message key="common.validate.truename.required"/>'},
					 password:{
						 required:'<fmt:message key="common.validate.password.required"/>',
						 minlength:'<fmt:message key="common.validate.password.minlength"/>',
						 passwordNotEquals:'<fmt:message key="common.validate.password.notEuals"/>',
						 passwordAllSmall:'<fmt:message key="common.validate.password.allSmall"/>',
						 passwordAllNum:'<fmt:message key="common.validate.password.allNumber"/>',
						 passwordAllBig:'<fmt:message key="common.validate.password.allBig"/>',
						 passwordHasSpace:'<fmt:message key="common.validate.password.hasSpace"/>'
					 },
					 repassword:{
						 required:'<fmt:message key="common.validate.repassword.required"/>',
						 minlength:'<fmt:message key="common.validate.repassword.minlength"/>',
						 equalTo:'<fmt:message key="common.validate.password.retype.not.equals"/>'
					},
					tempSecurityEmail:{
						required:'<fmt:message key="common.validate.securityEmail.required"/>',
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
		</script>
	</body>
</html>
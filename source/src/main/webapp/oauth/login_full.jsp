<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<%@taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<fmt:setBundle basename="application" />
<!DOCTYPE html>
<% pageContext.setAttribute("context", request.getContextPath()); %>
<html>
	<head>
		<title><fmt:message key="login.title"/></title>
		<link href="images/favicon.ico" rel="shortcut icon"	type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<f:script src="${context }/js/ValidateCode.js"/>
		<f:script src="${context }/js/string.js"/>
	</head>

	<body class="login" id="login">
		<jsp:include flush="true" page="../banner2013.jsp">
			<jsp:param name="DecludeMenu" value="true"/>
		</jsp:include>
		
		<div class="container login">
			<div class="login-left span8">
				<form class="form-horizontal" id="loginForm" action="authorize?${user_oauth_request.url}" method="post">
					<umt:SsoParams />
					<input type="hidden" id="login" value="${login }">
					<input type="hidden" name="pageinfo" value="userinfo">
					<h2 class="login-title">
						<fmt:message key='login.title'/> 
						<span class="mail-title"> 	
							<fmt:message key="common.duckling.oauth.loginbegin"/>
							<c:choose>
								<c:when test="${! empty client_website }">
								<a class="app_name" href="${client_website}" target="_blank">${client_name }</a>
							</c:when>
							<c:otherwise>
								<a class="app_name" >${client_name }</a>
							</c:otherwise>			
							</c:choose>
							<fmt:message key="common.duckling.oauth.loginend"/>							
						</span>
						<umt:registerLink> 
						   <span class="btn btn-success hide768"><fmt:message key='common.login.registNow'/></span>
						 </umt:registerLink>
					</h2>
					<div class="control-group">
		              	<label class="control-label" for="username">
							<fmt:message key='login.username'/>
							<input type="hidden" value="Validate" name="act" />
						</label>
		              	<div class="controls">
		               		<input id="userName" type="text" 
									placeholder="<fmt:message key='login.username.placeholder'/>"
									message="<fmt:message key="login.username.required"/>"
									maxlength="355" name="userName" value="${username}" />
									<span id="username_error_place" class="error help-inline">
										<c:if test="${userNameNull}">
											<fmt:message key="remindpass.username.required"/>
										</c:if>
										<c:if test="${loginerror}">
											<fmt:message key="login.password.wrong"/>
										</c:if>
									</span>
		              	</div>
		            </div>
		            <div class="control-group">
		              	<label class="control-label" for="password">
							<fmt:message key="login.password" />
						</label>
		              	<div class="controls">
		               		<input id="password" type="password" maxlength="355" class="logininput"
									name="password"  placeholder="<fmt:message key='inputpassword.password'/>" />
									<%-- <a class="help-quote hide480" href="<umt:url value="/help.jsp"/>" title="<fmt:message key='common.passwordInputHelp'/>" target="_blank"></a> --%>
									<span id="password_error_place" class="error help-inline">
										<c:if test="${!empty password_error }">
											<fmt:message key='${password_error }'/>
										</c:if>
									</span>
		               		<span class="help-block text-quote" id="passwordHint"><fmt:message key='common.login.password.hint.duckling'/></span>
		              	</div>
		            </div>
		            <c:if test="${showValidCode!=null || WrongValidCode!=null }">
			             <div class="control-group">
			              	<label class="control-label" for="ValidCode">
								<fmt:message key="login.imagetext" />
							</label>
			              	<div class="controls">
			               		<input type="hidden" name="requireValid" value="true" />
									<input id="ValidCode" style="width:5em;" type="text" maxlength="255" class="logininput ValidCode"
										name="ValidCode" 
										message="<fmt:message key="login.imagetext.required"/>" />
									<img id="ValidCodeImage" src="" />
									<a class="small_link" href="#" onclick="imageObj.changeImage()"><fmt:message
											key="login.imagetext.changeit" />
									</a>
									<span id="ValidCode_error_place" class="error help-inline">
									<c:choose>
										<c:when test="${lastErrorValidCode=='error'}">
											<fmt:message key='login.imagetext.wrong'/>
										</c:when>
										<c:when test="${lastErrorValidCode=='required'}">
											<fmt:message key='common.ValidCode.required'/>
										</c:when>
										<c:otherwise>
										</c:otherwise>
									</c:choose>
									</span>
			              	</div>
			            </div>
		            </c:if>
		           
		            <div class="control-group" style="display:none">
		              	<div class="controls small-font">
		              		<input type="checkbox" name="remember" id="remember" value="yes"/>
								<fmt:message key='login.password.rememberme'/>
							<a class="tendaysHelp" href="<umt:url value="/help_tendays.jsp"/>" target="_blank"></a>
		               	</div>
		            </div>
		           
		            <div class="control-group">
		              	<div class="controls">
		              		<input type="submit" class="btn btn-large btn-primary long" value="<fmt:message key="login.submit"/>"/>
								<a target="_blank" href="<umt:url value="/findPsw.do?act=stepOne"/>" class="small_link forgetpsw small-font">
									<fmt:message key="login.forgetpassword" />
								</a>
		              	</div>
		            </div>
		            <c:if test="${!empty thirdPartyList }">
			            <div class="control-group thirdLogin">
			              	<div class="controls small-font">
			              		<p><fmt:message key='common.login.userOtherName'/>  
			              			<c:if test="${!empty thirdPartyList['weibo'] }">
			              				<a href="${pageContext.request.contextPath}/thirdParty/login?type=weibo"><img src="../images/login/weibo.png" alt="使用新浪微博登录" title="使用新浪微博登录" /></a>
			              			</c:if>
			              			<c:if test="${!empty thirdPartyList['qq'] }">
										<a href="${pageContext.request.contextPath}/thirdParty/login?type=qq"><img src="../images/login/qq.png" alt="使用QQ账号登录" title="使用QQ账号登录" /></a>
									</c:if>
									<c:if test="${!empty thirdPartyList['cashq'] }">
										<a href="${pageContext.request.contextPath}/thirdParty/login?type=cashq"><img src="../images/login/cas.png" alt="使用院机关认证平台账号登录" title="使用院机关认证平台账号登录" /></a>
									</c:if>
									<c:if test="${!empty thirdPartyList['uaf'] }">
										<a href="${pageContext.request.contextPath}/thirdParty/login?type=uaf"><img src="<umt:url value="/images/login/uaf.png"/>" alt="使用UAF登录" title="使用UAF登录" /></a>
									</c:if>
								 	<a class="btn cancle bit right" style='display:none'><fmt:message key='common.login.returnBack'/></a>
								 </p>
			              	</div>
			            </div>
		            </c:if>
				</form>
			</div>
			
			<div class="login-right span4">
				<p class="header">
					<fmt:message key='common.login.noDuckling'/>
					<br></br>
					 <umt:registerLink> 
					   <span class="btn btn-success"><fmt:message key='common.login.registNow'/></span>
					 </umt:registerLink>
				</p>
				<h3><fmt:message key='common.duckling.QA'/></h3>
				<p class="sub-gray-text"><fmt:message key='common.duckling.description'/><a href="http://www.escience.cn" target="_blank"><fmt:message key='common.escience'/></a><fmt:message key='common.quote'/><a href="http://ddl.escience.cn" target="_blank"><fmt:message key='common.ddl.escience'/></a><fmt:message key='common.quote'/><a href="http://csp.escience.cn" target="_blank"><fmt:message key='common.csp'/></a><fmt:message key='common.quote'/><a href="http://www.escience.cn/people" target="_blank"><fmt:message key='common.scholarPage'/></a><fmt:message key='common.quote'/><a href="http://mail.escience.cn" target="_blank"><fmt:message key='common.casMail'/></a><fmt:message key='common.duckling.description.more'/></p>
				<p><strong><fmt:message key='common.duckling.defination'/></strong></p>
				<p><strong><fmt:message key='common.casMailLogin.hint'/></strong></p>
			</div>
			<div class="clear"></div>
		</div>
		
		<jsp:include flush="true" page="../bottom2013.jsp"></jsp:include>
		<script type="text/javascript">
			var imageObj = ValidateImage("ValidCodeImage");
		</script>
		<script type="text/javascript">
		$(document).ready(function(){
			//auto fill
			(function autoFill(selector){
				var $input=$(selector);
				var cookie=new Cookie();
				var loginName=cookie.getCookie('AUTO_FILL');
				if(loginName!=null&&$.trim(loginName)!=''){
					$input.val(loginName.replace(/\"/gm,""));
					$('#password').focus(); 
				}else{
					$input.focus();
				}
			})('#userName');
			
			
			islogin();
			function islogin(){
				var login = $("#login").val();
				if(login=='true'){
					$(".cst-container").hide();
					$(".cst-container-logined").show();
					$("#loginForm").submit();
				}
			};
			$('#username').live("blur",function(){
				if($(this).val()==''){
					return;
				}
				$.ajax({
					url:'<umt:url value="/signin/hint.do?act=isCoreMail"/>',
					data:"email="+$(this).val(),
					type:"post",
					success:function(data){
						if(data){
							$('#passwordHint').html("<fmt:message key='common.login.password.hint.casmail'/>");
						}else{
							$('#passwordHint').html("<fmt:message key='common.login.password.hint.duckling'/>");
						}
					}
				});
			});
			$('#banner_login').addClass("active");
			$('#loginForm').validate({
				 submitHandler:function(form){
					 var ff = new Object();
						ff.userName=$("input[name='userName']").val();
						ff.password=$("input[name='password']").val();
						ff.pageinfo="checkPassword";
						$.ajax({
							url:"authorize" ,
							data:ff,
							type:"post",
							dataType:"json", 
							success : function(data){
								if(data.status=='true'){
									form.submit();
								}else{
									$("input[name='userName']").next(".error").html("<label class='error' for='userName' generated='true' style='display: inline;'>用户名或者密码错误</label>");
								}
							}
						});
				 },
				 rules: {
					 userName:{required:true,email:true},
					 password:{required:true/*,minlength:6*/},
					 ValidCode:{required:true,number:true}
				 },
				 messages: {
					 userName: {
						 required:toRed("<fmt:message key='common.validate.email.required'/>"),
						 email:toRed('<fmt:message key="common.validate.email.invalid"/>')
					 },
					 password:{
						 required:toRed('<fmt:message key="common.validate.password.required"/>')
						 /*, minlength:toRed('<fmt:message key="common.validate.password.minlength"/>')*/
					 },
					 ValidCode:{
						 required:toRed('<fmt:message key="common.validate.validateCode.required"/>'),
						 number:toRed('<fmt:message key="common.validate.validateCode.number"/>')
					 }
					 
				 },
				 errorPlacement: function(error, element){
					 error.appendTo(element.next('.error'));
				}
			});
			function toRed(str){
				return "<font color='#cc0000'>"+str+'</font>';
			}
		});
		</script> 
	</body>
</html>
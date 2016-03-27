<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<fmt:setBundle basename="application" />
<!DOCTYPE html>
<umt:oauthContext/>
<html>
	<head>
		<title><fmt:message key="login.title"/></title>
		<link href="${context }/images/favicon.ico" rel="shortcut icon"	type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta name="viewport" content="width=device-width, initial-scale=1" />
		<f:css href="${context }/thirdparty/bootstrap/css/bootstrap.min.css"/>
		<f:css href="${context }/thirdparty/bootstrap/css/bootstrap-responsive.min.css"/>
		<f:css href="${context }/css/umt2013.css"/>
		<f:css href="${context }/css/umt2013-responsive.css"/>
		   
		<f:script  src="${context }/js/jquery-1.7.2.min.js"/>
		<f:script  src="${context }/js/jquery.validate.min.js"/>
		<f:script  src="${context }/js/ext_validate.js"/>
		<f:script  src="${context }/js/menu.js"/>
		<f:script  src="${context }/js/cookie.js"/>
		<f:script  src="${context }/thirdparty/bootstrap/js/bootstrap.min.js"/>
		<meta http-equiv="cache-control" content="no-cache" />
		<f:script src="js/ValidateCode.js"/>
		<f:script src="js/string.js"/>
		<f:css href="${context }/css/oauth.css"/>
		<f:script  src="${context }/thirdparty/respond.src.js"/>
	</head>

	<body class="login" id="login">
		<jsp:include flush="true" page="banner2013HtmlOnly.jsp">
			<jsp:param name="DecludeMenu" value="true"/>
		</jsp:include>
		
		<div class="container login">
			<div class="login-left span8">
				
				<form class="form-horizontal" id="loginForm" action="login" method="post">
					<input type="hidden" name="returnUrl" value="<c:out value="${returnUrl }"/>"/>
					<umt:SsoParams />
					<h2 class="login-title">
						<umt:registerLink> 
						   <span class="btn btn-success hide768"><fmt:message key='common.login.registNow'/></span>
						 </umt:registerLink>
						<fmt:message key='login.title'/> 
						<span class="mail-title"> 	
							<fmt:message key='common.duckling.use'/><fmt:message key='common.login.escience.email'/> 							
						</span>
						
					</h2>
					
					<div class="control-group">
						<c:if test="${!empty cookieError }">
					    	<span style="text-align:center" class=" help-block text-quote" id="passwordHint">系统检测到您的登陆IP地址发生了变化，为保证您的账号安全，请重新登录</span>
		              	</c:if>
		              	<label class="control-label" for="username">
							<fmt:message key='login.username'/>
							<input type="hidden" value="Validate" name="act" />
						</label>
		              	<div class="controls">
		              	<div class="input-prepend">
								<span class="add-on" id="user"> </span>
		               		<input id="username" tabindex="1" type="text" 
									placeholder="<fmt:message key='login.username.placeholder'/>"
									message="<fmt:message key="login.username.required"/>"
									maxlength="355" name="username" value="${ username}" />
									
		               		<!-- span class="help-block text-quote"><fmt:message key='login.username.hint'/></span-->
		              	</div>
		              	<span id="username_error_place" class="error help-inline">
									<c:choose>
										<c:when test="${! empty username_error }">
											<fmt:message key='${username_error}'/>
										</c:when>
										<c:when test="${WrongPassword=='user.expired'}">
											<fmt:message key='login.user.expired'/>
										</c:when>
										<c:when test="${WrongPassword=='user.locked'}">
											<fmt:message key='login.user.locked'/>
										</c:when>
										<c:when test="${WrongPassword=='user.stop'}">
											<fmt:message key='login.user.stop'/>
										</c:when>
										<c:when test="${ !empty WrongPassword}">
											<fmt:message key='login.password.wrong'/>
										</c:when>
									</c:choose>
									</span>
		              	</div>
		            </div>
		            <div class="control-group">
		              	<label class="control-label" for="password">
							<fmt:message key="login.password" />
						</label>
		              	<div class="controls">
		              		<div class="input-append">
								<span class="add-on preparePopover" data-html='true' data-animation='false' data-trigger="click" data-placement="top" 
								data-content="<a tabindex='-1' href='${context }/help_https.jsp' target='blank' ><fmt:message key='${isHttps?"oauth.https.hint":"oauth.http.hint" }'/></a>" id="${preSpanId }"> 
									<a tabindex=-1 target="_blank" href="${context }/help_https.jsp"></a>
									</span>
		              	
		               		<input id="password" tabindex="1" type="password" maxlength="355" class="logininput"
									name="password"  placeholder="<fmt:message key='inputpassword.password'/>" />
									
									<span id="password_error_place" class="error help-inline">
										<c:if test="${!empty password_error }">
											<fmt:message key='${password_error }'/>
										</c:if>
									</span>
		               		<span class="help-block text-quote" id="passwordHint"><fmt:message key='common.login.password.hint.duckling'/></span>
		              		</div>
		              	</div>
		            </div>
		            <c:if test="${showValidCode!=null || WrongValidCode!=null }">
			             <div class="control-group">
			              	<label class="control-label" for="ValidCode">
								<fmt:message key="login.imagetext" />
							</label>
			              	<div class="controls">
			               		<input type="hidden" name="requireValid" value="true" />
									<input tabindex=1 id="ValidCode" style="width:5em;" type="text" maxlength="255" class="logininput ValidCode"
										name="ValidCode" 
										message="<fmt:message key="login.imagetext.required"/>" />
									<img id="ValidCodeImage" src="" />
									<a class="small_link" style="cursor:pointer;"  onclick="imageObj.changeImage();$('#ValidCode').val('');"><fmt:message
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
		              		<input type="checkbox"  name="remember" id="remember" value="yes"/>
								<fmt:message key='login.password.rememberme'/>
							<a class="tendaysHelp" href="<umt:url value="/help_tendays.jsp"/>" target="_blank"></a>
		               	</div>
		            </div>
		            <div class="control-group">
		              	<div class="controls IE7">
		              		<input tabindex=3 type="submit" class="btn btn-large btn-primary long" value="<fmt:message key="login.submit"/>"/>
								<a tabindex=4 target="_blank" href="<umt:url value="/findPsw.do?act=stepOne"/>" class="small_link forgetpsw small-font">
									<fmt:message key="login.forgetpassword" />
								</a> 
		              	</div>
		            </div>
		            <div class="control-group thirdLogin">
		              	<div class="controls small-font IE7">
		              		<p><fmt:message key='common.login.userOtherName'/>  
		              			<!-- <a tabindex=5 href="<umt:url value="/thirdParty/login?type=weibo"/>"><img src="images/login/weibo.png" alt="使用新浪微博登录" title="使用新浪微博登录" /></a> -->
		              			<a tabindex=6 href="<umt:url value="/thirdParty/login?type=uaf"/>"><img src="images/login/uaf.png" alt="使用UAF登录" title="使用UAF登录"/></a>
								<a tabindex=7 href="<umt:url value="/thirdParty/login?type=geo"/>"><img src="images/login/cas_geo.png" alt="使用国家地球系统科学数据共享平台账号登录" title="使用国家地球系统科学数据共享平台账号登录" /></a>
		              			<umt:showInLoginAuths var="auths">
		              				<c:forEach items="${auths}" var="auth" varStatus="count">
	              						<a class="thirdParty" tabindex="${count.index+7}" href="<umt:url value="/thirdParty/login?type=${auth.code}"/>">${auth.name}</a>
		              				</c:forEach>
		              			</umt:showInLoginAuths>
								<!-- <a tabindex=7 href="<umt:url value="/thirdParty/login?type=cashq"/>"><img src="images/login/cas.png" alt="使用院机关工作平台账号登录" title="使用院机关工作平台账号登录" /></a> -->
							 	<a class="btn cancle bit right" style='display:none'><fmt:message key='common.login.returnBack'/></a>
							 </p>
		              	</div>
		            </div>
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
				<p><strong><fmt:message key='common.casMailLogin.hint'/></strong></p>
				<p><strong><fmt:message key='common.duckling.defination'/></strong></p>
				
			</div>
			<div class="clear"></div>
		</div>
		
		<jsp:include flush="true" page="bottom2013.jsp"></jsp:include>
		<script type="text/javascript">
			var imageObj = ValidateImage("ValidCodeImage");
		</script>
		<f:script src="${context }/js/oauth.js"/>
		<script type="text/javascript">
		$(document).ready(function(){
			$('#banner_login').addClass("active");
			$('#loginForm').validate({
				 submitHandler:function(form){
					 form.submit();
				 },
				 rules: {
					 username:{required:true},
					 password:{required:true/*,minlength:6*/},
					 ValidCode:{required:true,remote:{
						 type: "GET",
						 url: 'jq/validate.do?act=validateCode',
						 data:{ 
							 "validcode":function(){
									return $('#ValidCode').val();
							}	   		
				  		}
					 }}
				 },
				 messages: {
					 username: {
						 required:toRed("<fmt:message key='common.validate.email.required'/>"),
						 email:toRed('<fmt:message key="common.validate.email.invalid"/>')
					 },
					 password:{
						 required:toRed('<fmt:message key="common.validate.password.required"/>')
						 /*minlength:toRed('<fmt:message key="common.validate.password.minlength"/>')*/
					 },
					 ValidCode:{
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
			(function autoFill(selector){
				var $input=$(selector);
				var cookie=new Cookie();
				if($input.val()==''){
					var loginName=cookie.getCookie('AUTO_FILL');
					if(loginName!=null&&$.trim(loginName)!=''){
						$input.val(loginName.replace(/\"/gm,""));
						$('#password').focus(); 
					}else{
						$input.focus();
					}
				}
			})('#username');
			function toRed(str){
				return "<font color='#cc0000'>"+str+'</font>';
			}
		});
		</script>
	</body>
</html>
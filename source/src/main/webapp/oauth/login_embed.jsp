<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<%@taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<fmt:setBundle basename="application" />
<umt:AppList/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<% pageContext.setAttribute("context", request.getContextPath()); %>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta name="viewport" content="width=device-width, initial-scale=1" />
	<f:script src="${context }/thirdparty/respond.src.js"/>
	<f:script src="${context }/js/cookie.js"/>
	<f:css href="${context }/css/embed.css"/>
	<title>iframe page</title>
</head>
<body>
	<div class="cst-container-logined"><fmt:message key="common.duckling.redirect"/></div>
	<div class="cst-container">
		<p class="cst-title">
			<fmt:message key="common.duckling.use.login"/>
			<span class="sup"><a href="http://passport.escience.cn/help.jsp" target="_blank">[?]</a></span>
		</p>
		<form id="loginForm" action="authorize?${user_oauth_request.url}" method="post" class="cst-oauth"  target="_parent">
			<input type="hidden" id="login" value="${login }">
			<input type="hidden" name="pageinfo" value="userinfo">
			<div class="cst-controls">
              	<label class="control-label showBig" for="userName">
					<fmt:message key="login.username"/>
				</label>
				<label class="control-label hideBig" for="userName">
					<fmt:message key="common.duckling.cstnetuser"/>
					<span class="sup"><a href="http://passport.escience.cn/help.jsp" target="_blank">[?]</a></span>
				</label>
				<div class="controls">
	           		<input tabindex=1 type="text" id="userName" placeholder="<fmt:message key='login.username.placeholder'/>" name="userName" value="${userName }"/>
	           		<span class="error">
						<c:if test="${userNameNull}">
						<fmt:message key="remindpass.username.required"/>
						</c:if>
						<c:if test="${loginerror}">
						<fmt:message key="login.password.wrong"/>
						</c:if>
					</span>
				</div>
            </div>
		    <div class="cst-controls">
              	<label class="control-label" for="password">
					<fmt:message key="login.password"/>
				</label>
				<div class="controls">
	           		<input tabindex=2 type="password" id="password" placeholder="<fmt:message key="login.password"/>" name="password" value="${password }">
	           		<span class="error">
						<c:if test="${passwordNull}">
							<fmt:message key="common.validate.password.required"/>
						</c:if>
					</span>
				</div>
				<input type="checkbox"  name="remember" id="remember" value="yes" style="display:none"/>
            </div>   
            <div class="cst-controls">
            	<div class="controls">
              		<input type="submit" class="btn long btn-primary" value='<fmt:message key="common.banner.login"/>'>
              		<a class="cst-forget" href="<umt:config key="umt.this.base.url"/>/findPsw.do?act=stepOne" target="_blank"><fmt:message key="login.forgetpassword"/></a>
              	</div>
            </div>
            <c:if test="${!empty thirdPartyList }">
	            <div class="cst-controls thirdLogin">
	            	<div class="controls">
		           		<span class="third">
		           			<span class="thirdText"><fmt:message key='common.login.userOtherName'/></span>  
		           			<c:if test="${!empty thirdPartyList['weibo'] }">
		           				<a href	="<umt:url value="/thirdParty/login?type=weibo"/>" target="_parent"><img src="<umt:url value="/images/login/weibo.png"/>" alt="使用新浪微博登录" title="使用新浪微博登录" /></a>
		           			</c:if>
		           			<c:if test="${!empty thirdPartyList['qq'] }">
								<a href="<umt:url value="/thirdParty/login?type=qq"/>" target="_parent"><img src="<umt:url value="/images/login/qq.png"/>" alt="使用QQ账号登录" title="使用QQ账号登录" /></a>
							</c:if>
							<c:if test="${!empty thirdPartyList['cashq'] }">
								<a href="<umt:url value="/thirdParty/login?type=cashq"/>" target="_parent"><img src="<umt:url value="/images/login/cas.png"/>" alt="使用院机关认证平台账号登录" title="使用院机关认证平台账号登录" /></a>
							</c:if>
							<c:if test="${!empty thirdPartyList['uaf'] }">
								<a href="<umt:url value="/thirdParty/login?type=uaf"/>" target="_parent"><img src="<umt:url value="/images/login/uaf.png"/>" alt="使用UAF登录" title="使用UAF登录" /></a>
							</c:if>
					 		<a class="btn cancle bit right" style='display:none'><fmt:message key='common.login.returnBack'/></a>
						</span>
					</div>
			    </div>
		    </c:if>
		</form>
	</div>
	<script type="text/javascript" src="<umt:url value="/js/jquery-1.7.2.min.js"/>"></script>
	<script type="text/javascript" src="<umt:url value="/js/jquery.validate.min.js"/>"></script>
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
		
		$("#loginForm").validate({
			submitHandler: function(form){
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
			onsubmit:true,
			rules:{
				userName:{required: true},
				password:{required:true/*,minlength:8*/ }
			},
			messages:{
				userName:{required:"<fmt:message key='common.validate.email.required'/>" },
				password:{required:"<fmt:message key='common.validate.password.required'/>"
					/*,	minlength:"<fmt:message key='common.validate.password.minlength'/>"*/ 
					}
			},
			errorPlacement:function(error, element){
				error.appendTo(element.next('.error'));
			}
		});
		respond.update();
	});
	</script>
</body>
</html>
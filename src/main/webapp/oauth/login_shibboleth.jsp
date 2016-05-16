<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<%@taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<fmt:setBundle basename="application" />
<umt:AppList/>
<umt:oauthContext/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta name="viewport" content="width=device-width, initial-scale=1" />
	<f:css href="${context }/thirdparty/bootstrap/css/bootstrap.min.css"/>
	<f:script src="${context }/js/cookie.js"/>
	<f:script src="${context }/thirdparty/respond.src.js"/>
	<title>iframe page</title>
	<f:css href="${context }/css/oauth.css"/>
	<f:css href="${context }/css/embed.css"/>
	<!--[if IE 8]> 
		<f:css href="${context }/css/embedIE8.css"/>
	<![endif]-->
</head>
<body class="embed" style="background:transparent">
	<!--[if IE 8]> 
		<f:css href="${context }/css/embedIE8.css"/>
	<![endif]-->
	<div class="cst-container-logined"><fmt:message key="common.duckling.redirect"/></div>
	<div class="cst-container embed">
		<form id="loginForm" action="authorize?${user_oauth_request.url}" method="post" class="cst-oauth">
			<input type="hidden" id="login" value="${login }">
			<input type="hidden" name="pageinfo" value="userinfo">
			<div class="cst-controls">
              	<label class="control-label showBig" for="userName">
					<fmt:message key="login.username"/>
				</label>
				<label class="control-label hideBig" for="userName">
					<fmt:message key="common.duckling.cstnetuser"/>
					<span class="sup"><a href="https://passport.escience.cn/what-is-cstnet-passport.jsp" target="_blank">[?]</a></span>
				</label>
				<div class="controls">
				 <div class="input-prepend">
					<span class="add-on" id="user">
					 </span>
	           		<input tabindex=1 type="text" id="userName" placeholder="<fmt:message key='login.username.placeholder'/>" name="userName" value="${!empty userName?userName:autoFill }"/>
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
            </div>
		    <div class="cst-controls">
              	<label class="control-label" for="password">
					<fmt:message key="login.password"/>
				</label>
				<div class="controls">
				<div class="input-append">
					<span class="add-on preparePopover" data-html='true' data-animation='false' data-trigger="click" data-placement="top" 
								data-content="<a tabindex='-1' href='${context }/help_https.jsp' target='blank' ><fmt:message key='${isHttps?"oauth.https.hint":"oauth.http.hint" }'/></a>" id="${preSpanId }">
							<a tabindex=-1 target="_blank" href="${context }/help_https.jsp"></a>	
					</span>
	           		<input tabindex=2 type="password" id="password" placeholder="<fmt:message key="login.password"/>" name="password" value="${password }">
	           		<span class="error">
						<c:if test="${passwordNull}">
							<fmt:message key="common.validate.password.required"/>
						</c:if>
					</span>
					</div>
					<span class="help-block text-quote" id="passwordHint" style="color:#999"><fmt:message key='common.login.password.hint.duckling'/></span>
				</div>
				<input type="checkbox"  name="remember" id="remember" value="yes" style="display:none"/>
            </div>   
            <div class="cst-controls">
            	<div class="controls">
              		<input tabindex=3 type="submit" class="btn long btn-primary" value='<fmt:message key="common.banner.login"/>'>
              		<a tabindex=4 class="cst-forget" href="<umt:config key="umt.this.base.url"/>/findPsw.do?act=stepOne" target="_blank"><fmt:message key="login.forgetpassword"/></a>
              	</div>
            </div>
            <c:if test="${!empty thirdPartyList }">
	            <div class="cst-controls thirdLogin">
	            	<div class="controls">
		           		<span class="third">
		           			<span class="thirdText"><fmt:message key='common.login.userOtherName'/></span>  
		           			<c:if test="${!empty thirdPartyList['weibo'] }">
		           				<a tabindex=5 href	="<umt:url value="/thirdParty/login?type=weibo"/>" target="_parent"><img src="<umt:url value="/images/login/weibo.png"/>" alt="<fmt:message key='oauth.3pt.sina.login'/>" title="<fmt:message key='oauth.3pt.sina.login'/>" /></a>
		           			</c:if>
		           			<c:if test="${!empty thirdPartyList['qq'] }">
								<a tabindex=6 href="<umt:url value="/thirdParty/login?type=qq"/>" target="_parent"><img src="<umt:url value="/images/login/qq.png"/>" alt="<fmt:message key='oauth.3pt.qq.login'/>" title="<fmt:message key='oauth.3pt.qq.login'/>" /></a>
							</c:if>
							<c:if test="${!empty thirdPartyList['cashq'] }">
								<a tabindex=7 href="<umt:url value="/thirdParty/login?type=cashq"/>" target="_parent"><img src="<umt:url value="/images/login/cas.png"/>" alt="<fmt:message key='oauth.coremail.use.cashq'/>" title="<fmt:message key='oauth.coremail.use.cashq'/>" /></a>
							</c:if>
							<c:if test="${!empty thirdPartyList['uaf'] }">
								<a tabindex=8 href="<umt:url value="/thirdParty/login?type=uaf"/>" target="_parent"><img src="<umt:url value="/images/login/uaf.png"/>" alt="<fmt:message key='oauth.coremail.use.uaf'/>" title="<fmt:message key='oauth.coremail.use.uaf'/>" /></a>
							</c:if>
							<c:if test="${!empty thirdPartyList['geo'] }">
								<a  tabindex=9 href="<umt:url value="/thirdParty/login?type=geo"/>" target="_parent"><img src="<umt:url value="/images/login/cas_geo.png"/>" alt="<fmt:message key='oauth.3pt.geodata.login'/>" title="<fmt:message key='oauth.3pt.geodata.login'/>" /></a>
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
	<f:script  src="${context }/thirdparty/bootstrap/js/bootstrap.min.js"/>
	<f:script src="${context }/js/oauth.js"/> 
	<script type="text/javascript">
	var showValidCode="${showValidCode}";
	if(showValidCode=="true"||showValidCode==true){
		var oauthUrl=window.location.href;
		oauthUrl=oauthUrl.replace("theme=embed","theme=full");
		window.parent.window.location.href=oauthUrl;
	}
	
	
	
	$(document).ready(function(){
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
				ff.clientId=request('client_id');
				ff.clientName='${client_name }';
				$.ajax({
					url:"authorize" ,
					data:ff,
					type:"post",
					dataType:"json", 
					success : function(data){
						if(data.status=='true'){
							form.submit();
							return;
						}
						
						if(data.showValidCode==true){
							var oauthUrl=window.location.href;
							oauthUrl=oauthUrl.replace("theme=embed","theme=full");
							window.parent.window.location.href=oauthUrl;
							return;
						}
						
						
						if(data.status=='user.expired'){
							errorMsg='<fmt:message key="login.user.expired"/>';
						}else if(data.status=='user.locked'){
							errorMsg='<fmt:message key="login.user.locked"/>';
						}else if(data.status=='user.stop'){
							errorMsg='<fmt:message key="login.user.stop"/>';
						}else{
							errorMsg='<fmt:message key="login.password.wrong"/>';
						}
						
						
						$("input[name='userName']").next(".error").html("<label class='error' for='userName' generated='true' style='display: inline;'>"+errorMsg+"</label>");
					}
				});
			},
			onsubmit:true,
			rules:{
				userName:{required: true},
				password:{required:true/*,minlength:8*/ }
			},
			messages:{
				userName:{required:"<fmt:message key='common.validate.email.required'/>", email:'<fmt:message key="common.validate.email.invalid"/>'},
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
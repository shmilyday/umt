<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<%@taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<fmt:setBundle basename="application" />
<umt:AppList/>
<umt:oauthContext/>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1" />
	<f:script src="${context }/js/jquery-1.7.2.min.js"/>
	<f:script src="${context }/js/cookie.js"/>
	<f:script src="${context }/js/oauth.js"/>
	<c:choose>
		<c:when test="${ipadFlag }">
			<f:css href="${context }/css/coremail_pad.css"/>
		</c:when>
		<c:otherwise>
			<f:css href="${context }/css/coremail_mobile.css"/>
		</c:otherwise>
	</c:choose>
	<f:css href="${context }/thirdparty/bootstrap/css/bootstrap.min.css"/>
	<f:css href="${context }/css/oauth.css"/>
	<f:css href="${context }/css/embed.css"/>
	<title>coremail mobile page</title>
</head>
<body class="mobile">
	<div class="MainR">
		<div id="logArea">
			<form  id="loginForm" action="authorize?${user_oauth_request.url}" method="post" class="cst-oauth"  target="_parent">
				<input type="hidden" id="login" value="${login }">
				<input type="hidden" name="pageinfo" value="userinfo">
				<input type="hidden" name="themeinfo" value="coremail_mobile">
				<div class="inptr">
	               
	                <label for="userName" style="margin-top:-6px; *margin-top:-13px;"><fmt:message key="oauth.coremail.passport"/><span class="sup"> <a tabindex="-1" href="https://passport.escience.cn/what-is-cstnet-passport.jsp " target="_blank">[?]</a></span></label>
	                 <div class="input-prepend">
					<span class="add-on" id="user"> </span>
	                <input type="text" value="${userName }" class="input" name="userName" id="userName" placeholder="<fmt:message key='oauth.coremail.username.placeholder'/>">
	            	</div>
	            </div>
	            <div class="inptr">
	                <label for="password"><fmt:message key='oauth.coremail.password.lable'/></label>
	                <div class="input-append">
								<span class="add-on preparePopover" data-html='true' data-animation='false' data-trigger="click" data-placement="top" 
								data-content="<a tabindex='-1' href='${context }/help_https.jsp' target='blank' ><fmt:message key='${isHttps?"oauth.https.hint":"oauth.http.hint" }'/></a>" id="${preSpanId }"> 
								<a tabindex="-1" target="_blank" href="${context }/help_https.jsp"></a>
								</span>
	                
	                <input type="password" id="password" name="password" value="${password }" class="input" placeholder="<fmt:message key='login.password'/>">
	            	</div>
	            </div>
	            <div class="inptr">
	                <label class="for wholeLine">
	                   	<input type="checkbox" checked="checked" name=rememberUserName value="yes" style="margin:0 5px 0 0"><fmt:message key="oauth.coremail.mobile.remember.me"/>
	                </label>
	            </div>
			</form>
		</div>
	</div>
	<div class="loginBtn">
        <input id="submitButton" type="button" class="Button" value='<fmt:message key="oauth.coremail.button"/>'>
    </div>
<f:script  src="${context }/thirdparty/bootstrap/js/bootstrap.min.js"/>
	<script type="text/javascript">
	
	var showValidCode="${showValidCode}";
	if(showValidCode=="true"||showValidCode==true){
		var oauthUrl=window.location.href;
		oauthUrl=oauthUrl.replace("theme=coremail_mobile","theme=full");
		oauthUrl=oauthUrl.replace("theme=coremail_mobile_ipad","theme=full");
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
		$("input[name='password']").live("keypress",function(event){
			var key = event.which;
			if(key==13){
				$("#submitButton").trigger("click");
			}
		});
		$("#submitButton").live("click",function(){
			$('div.Error').html("");
			var userName = $("input[name='userName']").val();
			var password = $("input[name='password']").val();
			if((userName==null||userName=="")&&(password==""||password==null)){
				alert("<fmt:message key='oauth.coremail.password.usrname.required'/>");
				$("input[name='userName']").focus();
				return;
			}
			if(userName==null||userName==''){
				alert("<fmt:message key='oauth.coremail.username.required'/>");
				$("input[name='userName']").focus();
				return;
			}
			if(password==null||password==''){
				alert("<fmt:message key='oauth.coremail.passwored.required'/>");
				$("input[name='password']").focus();
				return;
			}
			var ff={'userName':$("input[name='userName']").val()
					,'password':$("input[name='password']").val()
					,'pageinfo':'checkPassword'};
			ff.clientId=request('client_id');
			ff.clientName='${client_name }';
			$.ajax({
				url:"authorize" ,
				data:ff,
				type:"post",
				dataType:"json", 
				success : function(data){
					if(data.status=='true'){
						$("#loginForm").submit();
						return;
					}
					
					if(data.showValidCode==true){
						var oauthUrl=window.location.href;
						oauthUrl=oauthUrl.replace("theme=coremail_mobile","theme=full");
						oauthUrl=oauthUrl.replace("theme=coremail_mobile_ipad","theme=full");
						window.parent.window.location.href=oauthUrl;
						return;
					}
					
					var errorMsg='';
					if(data.status=='user.expired'){
						errorMsg='<fmt:message key="login.user.expired"/>';
					}else if(data.status=='user.locked'){
						errorMsg='<fmt:message key="login.user.locked"/>';
					}else if(data.status=='user.stop'){
						errorMsg='<fmt:message key="login.user.stop"/>';
					}else{
						errorMsg='<fmt:message key="login.password.wrong"/>';
					}
					alert(errorMsg);
				}
			});
		});
	});
	</script>
	
</body>
</html>
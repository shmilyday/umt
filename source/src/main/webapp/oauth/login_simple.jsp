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
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title><fmt:message key="common.duckling.oauth.authorization"/></title>
</head>
<body class="login oauth">
	<jsp:include flush="true" page="../banner2013s.jsp"></jsp:include>
	<div class="container login">
		<form id="loginForm" action="authorize?${user_oauth_request.url}" method="post" class="form-horizontal oauth">
			<input type="hidden" id="login" value="${login }">
			<input type="hidden" name="pageinfo" value="userinfo">
			<p class="oauth_log"><fmt:message key="common.duckling.oauth.loginbegin"/>
			<c:choose>
				<c:when test="${! empty client_website }">
					<a class="app_name" href="${client_website}" target="_blank">${client_name }</a>
				</c:when>
				<c:otherwise>
					<a class="app_name" >${client_name }</a>
				</c:otherwise>			
			</c:choose>
			<fmt:message key="common.duckling.oauth.loginend"/></p>
			<div class="control-group">
              	<label class="control-label" for="password">
					<fmt:message key="login.username"/>
				</label>
              	<div class="controls">
              		<input type="text" id="userName" name="userName" value="${userName }"/>
              		<span class="error help-inline">
						<c:if test="${userNameNull}">
							<fmt:message key="remindpass.username.required"/>
						</c:if>
					</span>
					<span class="error help-inline">
						<c:if test="${loginerror}">
							<fmt:message key="login.password.wrong"/>
						</c:if>
					</span>
              	</div>
            </div>
		    <div class="control-group">
              	<label class="control-label" for="password">
					<fmt:message key="login.password"/>
				</label>
              	<div class="controls">
              		<input type="password" id="password" name="password" value="${password }">
              		<span class="error help-inline">
						<c:if test="${passwordNull}">
							<fmt:message key="common.validate.password.required"/>
						</c:if>
					</span>
              	</div>
            </div>
            <div class="control-group" style="display:none">
              	<div class="controls small-font">
              		<input type="checkbox"  name="remember" id="remember" value="yes"/>
						<fmt:message key='login.password.keepTenDay'/>
					<a class="tendaysHelp" href="<umt:url value="/help_tendays.jsp"/>" target="_blank"></a>
               	</div>
            </div>
            <div class="control-group">
              	<div class="controls">
              		<input type="submit" class="btn long btn-primary" value="<fmt:message key='common.banner.login'/>">
              	</div>
            </div>
            <c:if test="${!empty thirdPartyList }">
	            <div class="control-group thirdLogin">
	              	<div class="controls small-font">
	              		<p><fmt:message key='common.login.userOtherName'/>  
	              			<c:if test="${!empty thirdPartyList['weibo'] }">
	              				<a href="<umt:url value="/thirdParty/login?type=weibo"/>"><img src="<umt:url value="/images/login/weibo.png"/>" alt="使用新浪微博登录" title="使用新浪微博登录" /></a>
	              			</c:if>
	              			<c:if test="${!empty thirdPartyList['qq'] }">
								<a href="<umt:url value="/thirdParty/login?type=qq"/>"><img src="<umt:url value="/images/login/qq.png"/>" alt="使用QQ账号登录" title="使用QQ账号登录" /></a>
							</c:if>
							<c:if test="${!empty thirdPartyList['cashq'] }">
								<a href="<umt:url value="/thirdParty/login?type=cashq"/>"><img src="<umt:url value="/images/login/cas.png"/>" alt="使用院机关工作平台账号登录" title="使用院机关工作平台账号登录" /></a>
							</c:if>
							<c:if test="${!empty thirdPartyList['uaf'] }">
								<a href="<umt:url value="/thirdParty/login?type=uaf"/>"><img src="<umt:url value="/images/login/uaf.png"/>" alt="使用UAF登录" title="使用UAF登录" /></a>
							</c:if>
						 	<a class="btn cancle bit right" style='display:none'><fmt:message key='common.login.returnBack'/></a>
						 </p>
	              	</div>
			    </div> 
		    </c:if>
		</form>
	</div>
	
	<f:script src="${context }/js/jquery-1.7.2.min.js"/>
	<f:script src="${context }/js/jquery.validate.min.js"/>
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
		
	});
	</script>
	<script language="javascript">
  function RequestCookies(cookieName, dfltValue)
{
    var lowerCookieName = cookieName.toLowerCase();
    var cookieStr = document.cookie;
    
    if (cookieStr == "")
    {
        return dfltValue;
    }
    
    var cookieArr = cookieStr.split("; ");
    var pos = -1;
    for (var i=0; i<cookieArr.length; i++)
    {
        pos = cookieArr[i].indexOf("=");
        if (pos > 0)
        {
            if (cookieArr[i].substring(0, pos).toLowerCase() == lowerCookieName)
            {
                return unescape(cookieArr[i].substring(pos+1, cookieArr[i].length));
            }
        }
    }
    
    return dfltValue;
}
  
  function getCookie() {
	if (RequestCookies("remember", "") == "on") {
		document.getElementsByName("password")[0].value = RequestCookies("password", "");
		document.getElementsByName("remember")[0].checked = true;
	}
}

  </script> 
	
	
</body>
</html>
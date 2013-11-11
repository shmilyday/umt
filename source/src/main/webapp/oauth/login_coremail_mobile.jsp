<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<%@taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<fmt:setBundle basename="application" />
<umt:AppList/>
<html>
<head>
<% pageContext.setAttribute("context", request.getContextPath()); %>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1" />
	<f:script src="${context }/js/jquery-1.7.2.min.js"/>
	<f:script src="${context }/js/cookie.js"/>
	<c:choose>
		<c:when test="${ipadFlag }">
			<link rel="stylesheet" type="text/css" href="<umt:url value="/css/coremail_pad.css"/>" />
		</c:when>
		<c:otherwise>
			<link rel="stylesheet" type="text/css" href="<umt:url value="/css/coremail_mobile.css"/>" />
		</c:otherwise>
	</c:choose>
	<link rel="stylesheet" type="text/css" href="<umt:url value="/css/embed.css"/>" />
	<title>coremail mobile page</title>
</head>
<body>
	<div class="MainR">
		<div id="logArea">
			<form  id="loginForm" action="authorize?${user_oauth_request.url}" method="post" class="cst-oauth"  target="_parent">
				<input type="hidden" id="login" value="${login }">
				<input type="hidden" name="pageinfo" value="userinfo">
				<input type="hidden" name="themeinfo" value="coremail_mobile">
				<div class="inptr">
	                <label for="userName" style="margin-top:-6px; *margin-top:-13px;">通行证<span class="sup"><a href="http://passport.escience.cn/help.jsp" target="_blank">[?]</a></span></label>
	                <input type="text" value="${userName }" class="input" name="userName" id="userName" placeholder="邮件地址/中国科技网通行证">
	            </div>
	            <div class="inptr">
	                <label for="password">密&#12288;&#12288;码</label>
	                <input type="password" id="password" name="password" value="${password }" class="input" placeholder="密码">
	            </div>
	            <div class="inptr">
	                <label class="for wholeLine">
	                   	<input type="checkbox" checked="checked" name=rememberUserName value="yes">记住我
	                </label>
	            </div>
			</form>
		</div>
	</div>
	<div class="loginBtn">
        <input id="submitButton" type="button" class="Button" value='登&#12288;录'>
    </div>

	<script type="text/javascript">
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
				alert("必须填写用户名和密码");
				$("input[name='userName']").focus();
				return;
			}
			if(userName==null||userName==''){
				alert("必须填写用户名");
				$("input[name='userName']").focus();
				return;
			}
			if(password==null||password==''){
				alert("必须填写密码");
				$("input[name='password']").focus();
				return;
			}
			var ff={'userName':$("input[name='userName']").val()
					,'password':$("input[name='password']").val()
					,'pageinfo':'checkPassword'};
			$.ajax({
				url:"authorize" ,
				data:ff,
				type:"post",
				dataType:"json", 
				success : function(data){
					if(data.status=='true'){
						$("#loginForm").submit();
					}else{
						alert("认证失败, 请仔细检查你输入的用户名和密码");
					}
				}
			});
		});
	});
	</script>
	
</body>
</html>
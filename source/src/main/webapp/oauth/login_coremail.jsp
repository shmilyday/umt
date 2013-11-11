<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<fmt:setBundle basename="application" />
<umt:AppList/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<% pageContext.setAttribute("context", request.getContextPath()); %>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<meta http-equiv="x-ua-compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1" />
	<f:css href="${context }/css/coremail.css"/>
	<f:css href="${context }/css/embed.css"/>
	<f:script src="${context }/js/jquery-1.7.2.min.js"/>
	<f:script src="${context }/js/jquery.validate.min.js"/>
	<title>登录邮箱</title>
</head>
<body>
	<div class="MainR">
		<div class="Header">
	        <div left="0" class="title"><img src="<umt:url value="/images/title01.gif"/>"></div>
	    </div>
		<div id="logArea">
	        <div class="Error">
	        	<c:if test="${userNameNull}">
					必须填写用户名
				</c:if>
				<c:if test="${loginerror}">
					认证失败, 请仔细检查你输入的用户名和密码
				</c:if>
				<c:if test="${passwordNull}">
					必须填写密码
				</c:if>
	        </div>
			<noscript><div class="cst-oauth" style="color: red;font-size: 12px;margin-left: 60px;margin-bottom: 10px;">您的浏览器不支持或已经禁止网页脚本，<br/>您无法正常登录。
			<a href="http://help.cstnet.cn/redianwenti/anquan.html" target="_blank">如何解除脚本限制？</a></div></noscript>
	        <form id="loginForm" action="authorize?${user_oauth_request.url}" method="post" class="cst-oauth"  target="_parent">
	            <input type="hidden" id="login" value="${login }">
				<input type="hidden" name="pageinfo" value="userinfo">
				<input type="hidden" name="themeinfo" value="coremail">
	            <div class="inptr">
	                <label  for="userName" style="margin-top:-6px; *margin-top:-13px;">通行证<span class="sup"><a href="http://passport.escience.cn/help.jsp" target="_blank">[?]</a></span></label>
	                <input tabindex="1" type="text" value="${userName }" class="input" name="userName" id="userName" placeholder="邮件地址/中国科技网通行证">
	            </div>
	            <div class="inptr">
	                <label for="password">密&#12288;&#12288;码</label>
	                <input tabindex="2" type="password" id="password" placeholder="密码" name="password" value="${password }" class="input">
	            </div>
	            <div class="inptr indent">
	                <div>
	                    <label class="for">
	                        <input type="checkbox" checked="checked" name="secureLogon" value="yes">安全登录
	                    </label>
	                </div>
	                <div>
	                    <label class="for">
	                       	<input type="checkbox" checked="checked" name="rememberUserName" value="yes">记住用户名
	                    </label>
	                </div>
	                <div style="display:none">
	                	<label class="for">
	                		<input type="checkbox"  name="remember" id="remember" value="yes" />十天内免登录
	                		<a class="tendaysHelp" href="<umt:url value="/help_tendays.jsp"/>" target="_blank"></a>
	                	</label>
	                </div>
	            </div>
	            
	            <div class="inptr indent">
	                <div>
	                    <input id="submitButton" type="button" class="Button" value='登&#12288;录'>
	                </div>
	            </div>
	            <c:if test="${!empty thirdPartyList }">
					<c:if test="${!empty thirdPartyList['cashq'] }">
			            <%
			            	String s = request.getHeader("referer");
			            	if(s!=null&&s.length()>0&&s.contains("cashq.ac.cn")){
			            		%>
				           		 <div class="inptr indent cst-controls thirdLogin">
					            	<div>
						           		<span class="third">
						           			<span class="thirdText">院统一认证平台登录：</span>  
												<a href="<umt:url value="/thirdParty/login?type=cashq"/>" target="_parent"><img src="<umt:url value="/images/login/cas.png"/>" alt="使用院机关认证平台账号登录" title="使用院机关认证平台账号登录" /></a>
									 		<a class="btn cancle bit right" style='display:none'><fmt:message key='common.login.returnBack'/></a>
										</span>
									</div>
							    </div>
			            		<% 
			            	}
			            %>
					</c:if>
					<c:if test="${!empty thirdPartyList['uaf'] }">
						<div class="inptr indent cst-controls thirdLogin">
					            	<div>
						           		<span class="third">
						           			<span class="thirdText">登陆联盟登陆：</span>  
												<a href="<umt:url value="/thirdParty/login?type=uaf"/>" target="_parent"><img src="<umt:url value="/images/login/uaf.png"/>" alt="使用UAF登录" title="使用UAF登录" /></a>
											</span>
									</div>
							    </div>
					</c:if>
		    	</c:if>
	        </form>
	        <div id="loginInfo" style="display: none"></div>
	    </div>
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
		var haveChecked = false;
		$("#loginForm").live("keypress",function(event){
			if(!haveChecked){
				haveChecked=true;
				var ff = new Object();
				ff.pageinfo="checkLogin";
				$.ajax({
					url:"authorize",
					data:ff,
					type:'post',
					dataType:"json",
					success:function(data){
						if(data.status){
							$("#loginForm").hide();
							$('#loginInfo').html("");
							$('#loginInfo').html("<div class='loginHint'>账号"+data.userName+"已经登录！<br/>您可以"+"<a href='#' id='reload'>进入邮箱</a>|"+"<a href='"+data.logoutURL+encodeURIComponent(location.href)+"'>退出登录</a></div>");
							$('#loginInfo').show();
						}
					}
				});		
			}
		});
		
		$("#reload").live("click",function(){
			window.location.reload();
		});
		
		
		$("#submitButton").live("click",function(){
			$('div.Error').html("");
			var userName = $("input[name='userName']").val();
			var password = $("input[name='password']").val();
			if((userName==null||userName=="")&&(password==""||password==null)){
				$('div.Error').html("必须填写用户名和密码");
				return;
			}
			if(userName==null||userName==''){
				$('div.Error').html("必须填写用户名");
				return;
			}
			if(password==null||password==''){
				$('div.Error').html("必须填写密码");
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
						$('div.Error').html("认证失败, 请仔细检查你输入的用户名和密码");
					}
				}
			});
		});
		if(!$("#userName").val()){
			$("#userName").focus();
		}else{
			$('#password').focus(); 
		}
	});
	</script>
</body>
</html>
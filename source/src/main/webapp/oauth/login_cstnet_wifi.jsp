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
	<%-- <f:css href="${context }/css/coremail.css"/>
	<f:css href="${context }/css/embed.css"/> --%>
	<f:css href="${context }/css/embed_wifi.css"/>
	<f:script src="${context }/js/jquery-1.7.2.min.js"/>
	<f:script src="${context }/js/jquery.validate.min.js"/>
	<title>cstnet wifi</title>
</head>
<body>
	<div class="MainR">
		<div class="Header">
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
	                <label for="userName" style="margin-top:-6px; *margin-top:-13px;">通行证<span class="sup"><a href="http://passport.escience.cn/help.jsp" target="_blank">[?]</a></span></label>
	                <input tabindex=1 type="text" value="${userName }" class="input" name="userName" id="userName" placeholder="中国科技网通行证">
	            </div>
	            <div class="inptr">
	                <label for="password">密码</label>
	                <input tabindex=2 type="password" id="password" placeholder="密码" name="password" value="${password }" class="input">
	            </div>
	           
	          
	            <div class="inptr indent">
                	<label class="for">
                		<a target="_blank" href="../help_browser_password.jsp">如何记住用户名密码?</a>
                	</label>
	            </div>
	            
	            <div style="margin-bottom: 20px;margin-top:-25px" class="inptr indent">
                	<label for="useMacAddress"><input type="checkbox" id="useMacAddress" name="useMacAddress" />使用Mac地址登陆</label>
	            </div>
	            
	            <div class="inptr indent">
                    <a id="submitButton" class="btn btn-primary">登录</a> 
                    <input type="hidden" id="pcVisitors" value="http://159.226.190.5:8800/?action=visitors"/>
                    <a class="btn btn-success" id="visitorHref" href="http://159.226.190.5/visitors.php" target="_blank">访客</a>
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
		    	</c:if>
	        </form>
	        <div id="loginInfo" style="display: none"></div>
	    </div>
	</div>
	<iframe style="display:none" id="macBind"></iframe>
	
	<script type="text/javascript">
	$(document).ready(function(){
		var browser={
			version :function(){
				var u = navigator.userAgent;
			//	var app = navigator.appVersion;
				return {
					trident:u.indexOf('Trident')>-1,
					presto:u.indexOf('Presto')>-1,
					webKit:u.indexOf('AppleWebKit')>-1,
					gecko:u.indexOf('Gecko')>-1&&u.indexOf('KHTML')==-1,
					mobile:!!u.match(/AppleWebkit.*Mobile.*/)||!!u.match(/AppleWebKit/),
					ios:!!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/),
					android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1,
					iPhone: u.indexOf('iPhone') > -1 || u.indexOf('Mac') > -1, 
					iPad: u.indexOf('iPad') > -1,
					webApp: u.indexOf('Safari') == -1
				};
			}()
		};
		function browserType(){
			if(!browser.version.mobile){
				var href = $('#pcVisitors').val();
				$('#visitorHref').attr('href',href);
			}
		};
		browserType();
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
							$('#loginInfo').html("<div class='loginHint'>账号"+data.userName+"已经登录！<br/>您可以"+"<a href='#' id='reload'>点击登录</a>|"+"<a href='"+data.logoutURL+encodeURIComponent(location.href)+"'>退出登录</a></div>");
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
		
		$('#useMacAddress').on('click',function(){
			var checked=$(this).attr("checked");
			var baseUrl='<umt:config key="wifi.base.url"/>/oauthapi/callback.php?action=bind_mac&useMacAddress='+(checked?'1':'0');
			$('#macBind').attr("src",baseUrl);
		});
		
	});
	</script>
</body>
</html>
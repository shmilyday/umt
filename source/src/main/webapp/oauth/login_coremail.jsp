<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<fmt:setBundle basename="application" />
<umt:AppList/>
<umt:oauthContext/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<meta http-equiv="x-ua-compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1" />
	<f:script src="${context }/js/jquery-1.7.2.min.js"/>
	<f:script src="${context }/js/jquery.validate.min.js"/>
	<f:css href="${context }/thirdparty/bootstrap/css/bootstrap.min.css"/>
	<f:css href="${context }/css/oauth.css"/>
	<f:css href="${context }/css/embed.css"/>
	<f:css href="${context }/css/coremail.css"/>
	<title><fmt:message key="oauth.coremail.title"/></title>
</head>
<body>
	<div class="MainR">
		<div class="Header">
	        <div left="0" class="title"><img src="<umt:url value="/images/title01.gif"/>"></div>
	    </div>
		<div id="logArea">
	        <div class="Error">
	        	<c:if test="${userNameNull}">
					<fmt:message key="oauth.coremail.username.required"/>
				</c:if>
				<c:if test="${loginerror}">
					<fmt:message key="oauth.coremail.auth.false"/>
				</c:if>
				<c:if test="${passwordNull}">
					<fmt:message key="oauth.coremail.passwored.required"/>
				</c:if>
	        </div>
			<noscript><div class="cst-oauth" style="color: red;font-size: 12px;margin-left: 60px;margin-bottom: 10px;"><fmt:message key="oauth.coremail.your.client.js.not.work"/><br/><fmt:message key="oauth.coremail.cant.login.normal"/>
			<a href="http://help.cstnet.cn/redianwenti/anquan.html" target="_blank"><fmt:message key="oauth.coremail.how.js.work"/></a></div></noscript>
	        <form id="loginForm" action="authorize?${user_oauth_request.url}" method="post" class="cst-oauth"  target="_parent">
	            <input type="hidden" id="login" value="${login }">
				<input type="hidden" name="pageinfo" value="userinfo">
				<input type="hidden" name="themeinfo" value="coremail">
	            <div class="inptr">
	               
	                <label  for="userName" style="margin-top:-6px; *margin-top:-13px;"><fmt:message key="oauth.coremail.passport"/><span class="sup"><a tabindex=-1 href="https://passport.escience.cn/what-is-cstnet-passport.jsp" target="_blank">[?]</a></span></label>
	               <div class="input-prepend">
								<span class="add-on" id="user"> </span>
	                <input tabindex="1" type="text" value="${userName }" class="input" name="userName" id="userName" placeholder="<fmt:message key="oauth.coremail.username.placeholder"/>">
	            	</div>
	            </div>
	            <div class="inptr">
	                <label for="password"><fmt:message key="oauth.coremail.password.lable"/></label>
	                <div class="input-append">
								<span class="add-on preparePopover" data-html='true' data-animation='false' data-trigger="click" data-placement="top" data-title="false"
								data-content="<a tabindex='-1' href='${context }/help_https.jsp' target='blank' ><fmt:message key='${isHttps?"oauth.https.hint":"oauth.http.hint" }'/></a>" id="${preSpanId }"> 
								<a tabindex=-1 target="_blank" href="${context }/help_https.jsp"></a>
								</span>
	                <input tabindex="2" type="password" id="password" placeholder="<fmt:message key="oauth.coremail.password.placeholder"/>" name="password" value="${password }" class="input">
	           	 </div>
	            </div>
	            <div class="inptr indent">
	                <div>
	                    <label class="for">
	                        <input style="width:auto" tabindex=3 type="checkbox"  checked="checked" name="secureLogon" value="yes"><fmt:message key="oauth.coremail.security.login"/>
	                    </label>
	                </div>
	                <div>
	                    <label class="for">
	                       	<input style="width:auto" tabindex=4 type="checkbox"  checked="checked" name="rememberUserName" value="yes"><fmt:message key="oauth.coremail.remember.me"/>
	                    </label>
	                </div>
	                <div style="display:none">
	                	<label class="for">
	                		<input type="checkbox" tabindex=5 name="remember" id="remember" value="yes" /><fmt:message key="oauth.coremail.ten.day"/>
	                		<a class="tendaysHelp" href="<umt:url value="/help_tendays.jsp"/>" target="_blank"></a>
	                	</label>
	                </div>
	            </div>
	             
	            <div class="inptr indent">
	                <div>
	                    <input id="submitButton" type="button" tabindex=5 class="Button" value='<fmt:message key="oauth.coremail.button"/>'>
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
						           			<span class="thirdText"><fmt:message key="oauth.coremail.cashq"/></span>  
												<a tabindex=6 href="<umt:url value="/thirdParty/login?type=cashq"/>" target="_parent"><img src="<umt:url value="/images/login/cas.png"/>" alt="<fmt:message key="oauth.coremail.use.cashq"/>" title="<fmt:message key="oauth.coremail.use.cashq"/>" /></a>
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
						           			<span class="thirdText"><fmt:message key="oauth.coremail.uaf"/></span>  
												<a tabindex=7 href="<umt:url value="/thirdParty/login?type=uaf"/>" target="_parent"><img src="<umt:url value="/images/login/uaf.png"/>" alt="<fmt:message key="oauth.coremail.use.uaf"/>" title="<fmt:message key="oauth.coremail.use.uaf"/>" /></a>
											</span>
									</div>
							    </div>
					</c:if>
		    	</c:if>
	        </form>
	        <div id="loginInfo" style="display: none"></div>
	    </div>
	</div>
	<f:script  src="${context }/thirdparty/bootstrap/js/bootstrap.min.js"/>
	<f:script src="${context }/js/oauth.js"/>
	<script type="text/javascript">
	
	var showValidCode="${showValidCode}";
	if(showValidCode=="true"||showValidCode==true){
		var oauthUrl=window.location.href;
		oauthUrl=oauthUrl.replace("theme=coremail","theme=full");
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
							$('#loginInfo').html("<div class='loginHint'><fmt:message key='login.username'/>"+data.userName+"<fmt:message key='oauth.coremail.haslogin'/><br/><fmt:message key='oauth.coremail.you.can'/>"+"<a href='#' id='reload'><fmt:message key='oauth.coremail.enter.email'/></a>|"+"<a href='"+data.logoutURL+encodeURIComponent(location.href)+"'><fmt:message key='oauth.coremail.logout'/></a></div>");
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
				$('div.Error').html("<fmt:message key='oauth.coremail.password.usrname.required'/>");
				return;
			}
			if(userName==null||userName==''){
				$('div.Error').html("<fmt:message key='oauth.coremail.username.required'/>");
				return;
			}
			if(password==null||password==''){
				$('div.Error').html("<fmt:message key='oauth.coremail.passwored.required'/>");
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
						oauthUrl=oauthUrl.replace("theme=coremail","theme=full");
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
					
					$('div.Error').html(errorMsg);
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
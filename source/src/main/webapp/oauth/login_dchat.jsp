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
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title><fmt:message key="common.duckling.oauth.authorization"/></title>
	<link href="${context }/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
</head>
<body class="login oauth simpleView">
	<jsp:include flush="true" page="../banner2013s.jsp"></jsp:include>
	<f:script  src="${context }/js/jquery.tmpl.min.js"/>
	<f:script src="${context }/js/ValidateCode.js"/>
	<f:css href="${context }/css/oauth.css"/>
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
              		<div class="input-append">
						<span class="add-on" id="user"> </span>
              			<input tabindex=1 type="text" id="userName" name="userName" value="${!empty userName?userName:autoFill }"/>
              			<span class="error help-inline">
						<c:if test="${userNameNull}">
							<fmt:message key="remindpass.username.required"/>
						</c:if>
					</span>
					<span class="error help-inline" id="userName_error_place">
						<c:if test="${loginerror}">
							<fmt:message key="login.password.wrong"/>
						</c:if>
					</span>
					</div>
              	</div>
            </div>
		    <div class="control-group" id="mainDiv">
              	<label class="control-label" for="password">
					<fmt:message key="login.password"/>
				</label>
              	<div class="controls">
              		<div class="input-append">
						<span class="add-on preparePopover" data-html='true' data-animation='false' data-trigger="click" data-placement="top" 
								data-content="<a tabindex='-1' href='${context }/help_https.jsp' target='blank' >
								<fmt:message key='${isHttps?"oauth.https.hint":"oauth.http.hint" }'/></a>" id="${preSpanId }"> 
								<a tabindex='-1'  target="_blank" href="${context }/help_https.jsp"></a>
						</span>
              			<input tabindex=2 type="password" id="password" name="password" value="${password }">
              			<span class="error help-inline" id="password_error_place">
							<c:if test="${passwordNull}">
								<fmt:message key="common.validate.password.required"/>
							</c:if>
						</span>
					</div>
              	</div>
            </div>
            
             <c:if test="${showValidCode}">
		            <div id="validCodeDiv" class="control-group">
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
              		<input type="checkbox"  name="remember" id="remember" value="yes"/>
						<fmt:message key='login.password.keepTenDay'/>
					<a class="tendaysHelp" href="<umt:url value="/help_tendays.jsp"/>" target="_blank"></a>
               	</div>
            </div>
            <div class="control-group">
              	<div class="controls">
              		<input tabindex=3 type="submit" class="btn long btn-primary" value="<fmt:message key='common.banner.login'/>">
              	</div>
            </div>
            <c:if test="${!empty thirdPartyList }">
	            <div class="control-group thirdLogin">
	              	<div class="controls small-font">
	              		<p><fmt:message key='common.login.userOtherName'/>  
	              			<c:if test="${!empty thirdPartyList['weibo'] }">
	              				<a tabindex=4 href="<umt:url value="/thirdParty/login?type=weibo"/>"><img src="<umt:url value="/images/login/weibo.png"/>" alt="<fmt:message key='oauth.3pt.sina.login'/>" title="<fmt:message key='oauth.3pt.sina.login'/>" /></a>
	              			</c:if>
	              			<c:if test="${!empty thirdPartyList['qq'] }">
								<a tabindex=5 href="<umt:url value="/thirdParty/login?type=qq"/>"><img src="<umt:url value="/images/login/qq.png"/>" alt="<fmt:message key='oauth.3pt.qq.login'/>" title="<fmt:message key='oauth.3pt.qq.login'/>" /></a>
							</c:if>
							<c:if test="${!empty thirdPartyList['cashq'] }">
								<a tabindex=6 href="<umt:url value="/thirdParty/login?type=cashq"/>"><img src="<umt:url value="/images/login/cas.png"/>" alt="<fmt:message key='oauth.coremail.use.cashq'/>" title="<fmt:message key='oauth.coremail.use.cashq'/>"  /></a>
							</c:if>
							<c:if test="${!empty thirdPartyList['uaf'] }">
								<a tabindex=7 href="<umt:url value="/thirdParty/login?type=uaf"/>"><img src="<umt:url value="/images/login/uaf.png"/>" alt="<fmt:message key='oauth.coremail.use.uaf'/>" title="<fmt:message key='oauth.coremail.use.uaf'/>" /></a>
							</c:if>
						 	<a class="btn cancle bit right" style='display:none'><fmt:message key='common.login.returnBack'/></a>
						 </p>
	              	</div>
			    </div> 
		    </c:if>
		</form>
	</div>
	<f:script src="${context }/js/oauth.js"/>
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
		
		$("#loginForm").validate({
			submitHandler: function(form){
				var ff = new Object();
				ff.userName=$("input[name='userName']").val();
				ff.password=$("input[name='password']").val();
				ff.pageinfo="checkPassword";
				ff.clientId=request('client_id');
				ff.clientName='${client_name }';
				ff.ValidCode=$("#ValidCode").val();
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
							$("#validCodeDiv").remove();
							$('#mainDiv').after($('#codeDiv').tmpl(data));
							
							
							imageObj = ValidateImage("ValidCodeImage","<%= request.getContextPath() %>");
							if(data.status=="validCode.error"){
								return;
							}
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
						
						
						$("input[name='userName']").next(".error").html("<label class='error' for='userName' generated='true' style='display: inline;'>"+errorMsg+"</label>");
					}
				});
			},
			onsubmit:true,
			rules:{
				userName:{required: true},
				password:{required:true/*,minlength:8*/ },
				ValidCode:{required:true,remote:{
					 type: "GET",
					 url: '/jq/validate.do?act=validateCode',
					 data:{ 
						 "validcode":function(){
								return $('#ValidCode').val();
						}	   		
			  		}
				 }}
			},
			messages:{
				userName:{required:"<fmt:message key='common.validate.email.required'/>" },
				password:{required:"<fmt:message key='common.validate.password.required'/>"
					/*,	minlength:"<fmt:message key='common.validate.password.minlength'/>"*/ 
					},
				 ValidCode:{
					 required:toRed('<fmt:message key="common.validate.validateCode.required"/>'),
					 remote:toRed('<fmt:message key="common.validate.validateCode.wrong"/>')
				 }
			},
			errorPlacement:function(error, element){
			/* 	error.appendTo(element.next('.error')); */
				var sub="_error_place";
				 var errorPlaceId="#"+$(element).attr("name")+sub;
				 	$(errorPlaceId).html("");
				 	error.appendTo($(errorPlaceId));
			}
		});
		
	});
	</script>
	<script language="javascript">
	var imageObj = ValidateImage("ValidCodeImage","<%= request.getContextPath() %>");
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

<script id="codeDiv" type="text/x-jquery-tmpl" >
{{if showValidCode }}
<div id="validCodeDiv" class="control-group">
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
										{{if lastErrorValidCode=='error'}}
											<fmt:message key='login.imagetext.wrong'/>
										{{/if}}
										{{if lastErrorValidCode=='required'}}
											<fmt:message key='common.ValidCode.required'/>
										{{/if}}
									</span>
			              	</div>
	</div>
{{/if}}
</script>

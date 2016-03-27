<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@ taglib prefix="umt" uri="WEB-INF/tld/umt.tld"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<fmt:setBundle basename="application" />
<%
        pageContext.setAttribute("contextPath", request.getContextPath());
        pageContext.setAttribute("DecludeMenu", request.getParameter("DecludeMenu"));
        String showLogin = request.getParameter("showLogin");
%>
<c:choose>
<c:when test="${loginInfo.loginNameInfo.status=='temp' }">
<div class="navbar navbar-inverse navbar-fixed-top nav-bar fix-top">
	<div class="navbar-inner">
		<div class="container">
			<button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	        </button>
	        <div class="logoDiv">
				<a class="duckling-logo logo"></a>
				<a class="logo" href="<umt:url value=""/>"><span class="logo-title"><fmt:message key='common.duckling'/></span></a>
			</div>
			<div class="nav-collapse collapse">
				<ul class="nav-right">
					<c:choose>
						<c:when test="${user_locale=='en_US'||user_locale=='en'}">
							<li><a href="#" onclick="changeLocale('zh_CN')"><fmt:message key='banner.language'/></a></li>
						</c:when>
						<c:when test="${user_locale=='zh_CN'||user_locale=='zh'}">
							<li><a href="#" onclick="changeLocale('en_US')"><fmt:message key='banner.language'/></a></li>
						</c:when>
						<c:otherwise>
							<li><a href="#" onclick="changeLocale('en_US')"><fmt:message key='banner.language'/></a></li>
						</c:otherwise>
					</c:choose>
		                	<umt:HasLogin>
		                		<li id="banner_username">
		                			<a href="<umt:url value="/"/>">${loginInfo.user.trueName }</a>
		                        </li>
		                        <li>
		                            <a href="<umt:url value="/logout"/>"><fmt:message key='banner.logout' /> </a>
		                        </li>
		                	</umt:HasLogin>
				</ul>
				<ul class="nav">
					<li id="banner_index"><a href="<umt:url value="/"/>"><fmt:message key="banner.index"/></a></li>
					<li id="banner_help"><a href="<umt:url value="/help.jsp"/>"><fmt:message key="banner.help"/></a></li>
				</ul>
			</div>
		</div>
	</div>
</div>
</c:when>
<c:when test="${!empty isStatic}">
<div class="navbar navbar-inverse navbar-fixed-top nav-bar fix-top">
	<div class="navbar-inner">
		<div class="container">
			<button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	        </button>
	        <div class="logoDiv">
				<a class="duckling-logo logo"></a>
				<a class="logo" href="<umt:url value=""/>"><span class="logo-title"><fmt:message key='common.duckling'/></span></a>
			</div>
			<div class="nav-collapse collapse">
				<ul class="nav-right">
					<c:choose>
						<c:when test="${user_locale=='en_US'||user_locale=='en'}">
							<li><a href="#" onclick="changeLocale('zh_CN')"><fmt:message key='banner.language'/></a></li>
						</c:when>
						<c:when test="${user_locale=='zh_CN'||user_locale=='zh'}">
							<li><a href="#" onclick="changeLocale('en_US')"><fmt:message key='banner.language'/></a></li>
						</c:when>
						<c:otherwise>
							<li><a href="#" onclick="changeLocale('en_US')"><fmt:message key='banner.language'/></a></li>
						</c:otherwise>
					</c:choose>
		            <li id="banner_login"><a href="<umt:url value="/login"/>"><fmt:message key='banner.login' /></a></li>
		            <li id="banner_regist"> <umt:registerLink><fmt:message key='login.regist'/></umt:registerLink></li>
				</ul>
				<ul class="nav">
					<li id="banner_index"><a href="<umt:url value=""/>"><fmt:message key="banner.index"/></a></li>
					<li id="banner_forgot_password"><a  href="<umt:url value="/findPsw.do?act=stepOne"/>" class="small_link forgetpsw"><fmt:message key="remindpass.title" /></a></li>
					<li id="banner_help"><a href="<umt:url value="/help.jsp"/>"><fmt:message key="banner.help"/></a></li>
				</ul>
			</div> 
		</div>
	</div>
</div>
</c:when>
<c:otherwise>
	<div class="navbar navbar-inverse navbar-fixed-top nav-bar fix-top">
		<div class="navbar-inner">
			<div class="container">
				<button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
		            <span class="icon-bar"></span>
		            <span class="icon-bar"></span>
		            <span class="icon-bar"></span>
		        </button>
		        <div class="logoDiv">
					<a class="duckling-logo logo"></a>
					<a class="logo" href="<umt:url value=""/>"><span class="logo-title"><fmt:message key='common.duckling'/></span></a>
				</div>
				<div class="nav-collapse collapse">
					<ul class="nav-right">
						<c:choose>
							<c:when test="${user_locale=='en_US'||user_locale=='en'}">
								<li><a href="#" onclick="changeLocale('zh_CN')"><fmt:message key='banner.language'/></a></li>
							</c:when>
							<c:when test="${user_locale=='zh_CN'||user_locale=='zh'}">
								<li><a href="#" onclick="changeLocale('en_US')"><fmt:message key='banner.language'/></a></li>
							</c:when>
							<c:otherwise>
								<li><a href="#" onclick="changeLocale('en_US')"><fmt:message key='banner.language'/></a></li>
							</c:otherwise>
						</c:choose>
								<%
			                		if(showLogin == null || !"false".equals(showLogin)) {
			                	%>
			                	<umt:HasLogin>
			                		<li id="banner_username">
			                			<a href="<umt:url value="/"/>">${loginInfo.user.trueName }</a>
			                        </li>
			                        <li>
			                            <a href="<umt:url value="/logout"/>"><fmt:message key='banner.logout' /> </a>
			                        </li>
			                	</umt:HasLogin>
			                	<umt:NotLogin>
			                       	<li id="banner_login"><a href="<umt:url value="/login"/>"><fmt:message key='banner.login' /></a></li>
			                		<li id="banner_regist"> <umt:registerLink><fmt:message key='login.regist'/></umt:registerLink></li>
			                	</umt:NotLogin>
			                	<%
			                		}
			                	%>
					</ul>
					<ul class="nav">
						<li id="banner_index"><a href="<umt:url value=""/>"><fmt:message key="banner.index"/></a></li>
						<umt:HasLogin>
							<li id="banner_user_info"><a href="<umt:url value="/user/info.do?act=show"/>"><fmt:message key="banner.userinfo"/></a></li>
<%-- 							<li id="banner_user_digitalCertificate"><a href="<umt:url value="/user/digitalCertificate.do?act=index"/>"><fmt:message key="banner.digitalCertificateSetting"/></a></li> --%>	
						<li id="banner_user_manage"><a href="<umt:url value="/user/manage.do?act=showManage"/>"><fmt:message key="banner.accountSetting"/></a></li>
							<li id="banner_user_safe">
								<c:choose>
									<c:when test="${'weibo'==loginInfo.user.type }">
										<a href="<umt:url value="/user/safe.do?act=showLog"/>"><fmt:message key="banner.accountSecurity"/></a>
									</c:when>
									<c:otherwise>
										<a href="<umt:url value="/user/safe.do?act=showSecurity"/>"><fmt:message key="banner.accountSecurity"/></a>
									</c:otherwise>
								</c:choose>
							</li>
						</umt:HasLogin>
						<umt:NotLogin>
							<li id="banner_forgot_password"><a  href="<umt:url value="/findPsw.do?act=stepOne"/>" class="small_link forgetpsw"><fmt:message key="remindpass.title" /></a></li>
						</umt:NotLogin>
						<li id="banner_help"><a href="<umt:url value="/help.jsp"/>"><fmt:message key="banner.help"/></a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</c:otherwise>
</c:choose>

<script type="text/javascript">
        function changeLocale(lang){
                var cookie=new Cookie();
                cookie.setCookie('umt.locale', lang, {expireDays:365, path:'${empty contextPath?"/":contextPath}'});
                removeURILocale();
        };
        function removeURILocale(){
        	var url=location.href;
        	if(url.indexOf('locale=en_US')>-1||url.indexOf('locale=zh_CN')>-1){
        		url=url.replace('locale=en_US','locale=').replace('locale=zh_CN','locale=');
        	}
        	location.href=url;
        }
        $(document).ready(function(){
                $('#lang').menu({menuid:'#langmenu'});
                $('.slide').each(function (i,n){
                	setTimeout(function(){
                		$(n).hide('slow');	
                	},2000);                	
                });
                $(".closeTooltip").click(function(){
    				$(this).parent().parent().remove();
    			});
        });
</script>

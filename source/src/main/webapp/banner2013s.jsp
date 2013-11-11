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
<meta name="viewport" content="width=device-width, initial-scale=1" /><!-- for responsive design  -->
<f:css href="${contextPath }/thirdparty/bootstrap/css/bootstrap.min.css"/>
<f:css href="${contextPath }/thirdparty/bootstrap/css/bootstrap-responsive.min.css"/>
<f:css href="${contextPath }/css/umt2013.css"/>
<f:css href="${contextPath }/css/umt2013-responsive.css"/>
   
<f:script  src="${contextPath }/js/jquery-1.7.2.min.js"/>
<f:script  src="${contextPath }/js/jquery.validate.min.js"/>
<f:script  src="${contextPath }/js/ext_validate.js"/>
<f:script  src="${contextPath }/js/menu.js"/>
<f:script  src="${contextPath }/js/cookie.js"/>
<f:script  src="${contextPath }/thirdparty/bootstrap/js/bootstrap.min.js"/>
<f:script  src="${contextPath }/thirdparty/respond.src.js"/>
     
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
				<a class="help-quote" href="<umt:url value="/help.jsp"/>" title="<fmt:message key='common.passwordInputHelp'/>" target="_blank"></a>
			</div>
			<div class="nav-collapse collapse">
				<ul class="nav-right">
		            <li id="banner_regist"> <umt:registerLink><fmt:message key='login.regist'/></umt:registerLink></li>
				</ul>
			</div>
		</div>
	</div>
</div>
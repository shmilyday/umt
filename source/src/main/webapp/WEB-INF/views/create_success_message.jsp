<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<%
	pageContext.setAttribute("ContextPath", request.getContextPath());
%>
<umt:AppList/>
<fmt:setBundle basename="application" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html>
	<head>
		<title><fmt:message key="message.title"/></title>
		<link href="<%= request.getContextPath() %>/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
	</head>
	<c:set var="requireUpgrade" value="${loginInfo.requireUpgrade}"></c:set>
	<body class="login">
		<jsp:include flush="true" page="/banner2013.jsp"></jsp:include>
		<div class="content container login gray">
			
				<h2 class="login-title">
				<c:choose>
					<c:when test="${requireUpgrade }">
						<fmt:message key='common.loginInfo'/>
					</c:when>
					<c:when test="${oper=='login' }">
						<fmt:message key="temp.filter.login.title"/>
					</c:when>
					<c:when test="${oper=='regist' }">
						<fmt:message key="temp.filter.regist.title"/>
					</c:when>
					<c:otherwise>
						微博验证
					</c:otherwise>
				</c:choose>
				</h2>
			<c:choose>
				<c:when test="${requireUpgrade }">
					<div class="thirdPartyLogin">
						<div class="thirdPartyLeft">
							<c:choose>
								<c:when test="${loginInfo.user.type=='weibo'}">
									<img src="<umt:url value="/images/login/weibo-b.png"/>"/>
								</c:when>
								<c:when test="${loginInfo.user.type=='qq'}">
									<img src="<umt:url value="/images/login/qq-b.jpg"/>"/>
								</c:when>
							</c:choose>
						</div>
						<div class="thirdPartyRight">
							<h2> ${loginInfo.user.trueName}</h2>
							<p class="hint"><fmt:message key='common.you.loging'/>
							<c:if test="${otherUmt==null}"><fmt:message key="thirdParty.${loginInfo.user.type }"/></c:if>
							<c:if test="${otherUmt!=null}">${otherUmt.name}</c:if>
							<fmt:message key='common.you.loging.end'/>
								,<c:if test="${otherUmt==null}">
									<a href="<umt:url value="/accountBind_createUmt.jsp"/>"><fmt:message key='thirdParty.update.update.userinfo.or.upgrate'/></a>
								</c:if>
								<c:if test="${otherUmt!=null}">
									<a href="<umt:url value="/findPsw.do?act=stepOne&loginEmail=${loginInfo.user.cstnetId}"/>"><fmt:message key='thirdParty.update.update.userinfo.or.upgrate'/></a>
								</c:if>
							</p>
						</div>
						<div class="clear"></div>
					</div>
					<jsp:include page="/app_list.jsp"></jsp:include>
				</c:when>
				<c:otherwise>
					<div class="create_success">
						<div class="success_left"></div>
						<div class="success_right">
							<h4><fmt:message key="create_sucess_message.accountToVerify"/></h4>
							<p><fmt:message key="create_sucess_message.signInEmail"/><span class="email success-text">${sendEmail }</span><fmt:message key="create_sucess_message.verifyInstruct"/></p>
							<p>
								<umt:iKnowUrl email=" ${sendEmail }">
									<a target="_blank" href="<umt:emailUrl email="${sendEmail }"/>"><button type="button" class="btn long btn-primary"><fmt:message key="create_sucess_message.signInEmailNow"/></button></a>
								</umt:iKnowUrl>
							</p>
							<p class="small-font no_reply"><fmt:message key="create_sucess_message.notYetReceive"/></p>
							<p class="small-font">1. 
								<c:if test="${type=='umt'||type=='uc' }">
									<a href="<umt:url value="/temp/activation.do?act=sendLoginAndSecurityEmail&loginEmail=${sendEmail }&notReturn=true"/>"><fmt:message key="create_sucess_message.reSentEmail"/></a>
								</c:if>
								<umt:isThirdParty type="${type}">
									<a href="<umt:url value="/temp/activation.do?act=sendLoginEmail&loginEmail=${sendEmail }&type=${type }&notReturn=true"/>"><fmt:message key="create_sucess_message.reSentEmail"/></a>
									<fmt:message key='create_sucess_message.or'/><a href="<umt:url value="/needLogin/operation.do?act=deleteBind"/>"><fmt:message key='create_sucess_message.unbind'/></a>
								</umt:isThirdParty>
							</p>
							<p class="small-font">2.<fmt:message key="create_sucess_message.findInSpam"/></p>
						</div>
					</div>
				</c:otherwise>
			</c:choose>
			
		</div>
		<jsp:include flush="true" page="/bottom2013.jsp"></jsp:include>
	</body>
</html>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<fmt:setBundle basename="application" />
<%
	request.setAttribute("from", request.getParameter("from"));
%>

<div class="send_mail safty" id="setSaftyShow" >
	<div class="mail_left"></div>
	<div class="mail_right">
		<c:choose>
			<c:when test="${!empty loginInfo.user.securityEmail&&empty tempSecurityEmail}">
				<!--最理想状态，设置成功，也未提交新状态  -->
				<span class="safty-ok"></span>
				<strong><fmt:message key="common.securityEmail"/></strong>
				<span class="success-text small-font"><fmt:message key="safeItems.securityEmail.isSet"/></span>
				<p class="sub-text inner">
					<fmt:message key="safeItems.securityEmail.description"/>
				</p>
				<p class="mail_detail">
					<span><strong>${ loginInfo.user.securityEmail}</strong></span>
					<a class="small-font"  href="<umt:url value="/user/securityEmail.do?act=show&from=${from }"/>"><fmt:message key="safeItems.change"/></a>
				</p>
			</c:when>
			<c:when test="${!empty loginInfo.user.securityEmail&&!empty tempSecurityEmail}">
				<!-- 原本已经设置成功，但是提交了新的邮箱 -->
				<span class="safty-ok"></span>
				<strong><fmt:message key="common.securityEmail"/></strong>
				<span class="success-text small-font"><fmt:message key="safeItems.securityEmail.isSet"/></span>
				<p class="sub-text inner">
					<fmt:message key="safeItems.securityEmail.description"/>
				</p>
				<p class="mail_detail">
					<strong>${loginInfo.user.securityEmail }</strong>
					<a class="small-font" href="<umt:url value="/user/securityEmail.do?act=show&from=${from }"/>"><fmt:message key="safeItems.change"/></a>
					<br>
					<span class="sub-text inner"><fmt:message key="safeItems.changeSecurityEmail.history"/>${tempSecurityEmail }<fmt:message key="safeItems.changeSecurityEmail.toVerify"/></span>
					<a class="small-font" href="<umt:url value="/temp/activation.do?act=sendSecurityEmail&securityEmail=${tempSecurityEmail }"/>"><fmt:message key="safeItems.verify"/></a>
				</p>
			</c:when>
			<c:when test="${empty loginInfo.user.securityEmail&&!empty tempSecurityEmail}">
				<!-- 从来没设置过，但是提交了新的邮箱，未激活 -->
				<span class="safty-sorry"></span>
				<strong><fmt:message key="common.securityEmail"/></strong>
				<span class="warnning-text small-font"><fmt:message key="safeItems.securityEmail.isUnVerified"/></span>
				<p class="sub-text inner">
					<fmt:message key="safeItems.securityEmail.description"/>
				</p>
				<p class="mail_detail">
					<span><strong>${tempSecurityEmail }</strong></span>
					<span>
						<a class="small-font" href="<umt:url value="/temp/activation.do?act=sendSecurityEmail&securityEmail=${tempSecurityEmail }"/>"><fmt:message key="safeItems.verify"/></a>
						<a class="small-font" href="<umt:url value="/user/securityEmail.do?act=show&from=${from }"/>"><fmt:message key="safeItems.change"/></a>
					</span>
				</p>
				<c:if test="${loginInfo.user.type=='coreMail'}">
					<div style="position:relative;  word-break:normal;" class="small-font">
						<div class="popover right" style="display:inline-block; right:-50%; left:50%; top:-70px">
				            <div class="arrow"></div>
				            <h3 class="popover-title"><fmt:message key="passport.merge.hintTitle"/><a class="closeTooltip" href="#" style="float:right;margin:0px 5px;font-size:12px"><fmt:message key="passport.merge.close"/></a></h3>
				            <div class="popover-content">
				              <p><fmt:message key="safeItems.activeSecurityEmail.hint"/></p>
				            </div>
				        </div>
					</div>
				</c:if>
			</c:when>
			<c:when test="${empty loginInfo.user.securityEmail&&empty tempSecurityEmail}">
				<!-- 从来未设置过，也没提交密保邮箱 -->
				<span class="safty-sorry"></span>
				<strong><fmt:message key="common.securityEmail"/></strong>
				<span class="warnning-text small-font"><fmt:message key="safeItems.securityEmail.isUnset"/></span>
				<p class="sub-text inner">
					<fmt:message key="safeItems.securityEmail.description"/>
					<a class="small-font" href="<umt:url value="/user/securityEmail.do?act=show&from=${from }"/>"><fmt:message key="safeItems.setting"/></a>
				</p>
				<div style="position:relative;  word-break:normal;" class="small-font">
					<div class="popover right" style="display:inline-block; right:-50%; left:50%; top:-70px">
			            <div class="arrow"></div>
			            <h3 class="popover-title"><fmt:message key="passport.merge.hintTitle"/><a class="closeTooltip" href="#" style="float:right;margin:0px 5px;font-size:12px"><fmt:message key="passport.merge.close"/></a></h3>
			            <div class="popover-content">
			              <p><fmt:message key="safeItems.setSecurityEmail.hint"/></p>
			            </div>
			        </div>
				</div>
			</c:when>
		</c:choose>
	</div>
	<div class="clear"></div>
</div>
			
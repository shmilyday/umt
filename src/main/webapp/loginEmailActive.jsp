<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<fmt:setBundle basename="application" />
<c:choose>
	<c:when test="${!isLoginEmailActive }">
		<div class="control-group">
			<label class="control-label nopadding">
				<span class="safty-sorry"></span><strong><fmt:message key='common.userInfo.cstnetID'/></strong>
			</label>
			<div class="controls">
				<strong>${loginInfo.user.cstnetId }</strong>
				<span class="warnning-text small-font"><fmt:message key='common.leftBracket'/><fmt:message key='common.userInfo.username.unverified'/><fmt:message key='common.comma'/><a href="<umt:url value="/user/activation.do?act=sendLoginEmail&loginEmail=${loginInfo.user.cstnetId }"/>"><fmt:message key='common.userInfo.username.toVerify'/></a><fmt:message key='common.rightBracket'/></span>
		    	<c:if test="${false }">
					<a href="<umt:url value="/user/primary/loginName.do?act=showPrimaryLoginEmailStep1"/>"><fmt:message key='common.change'/></a>
				</c:if>
		    </div>
	    </div>
	</c:when>
	<c:otherwise>
		<div class="control-group">
			<label class="control-label nopadding">
				<span class="safty-ok"></span><strong><fmt:message key='common.userInfo.cstnetID'/></strong>
			</label>
			<div class="controls">
				<strong>${loginInfo.user.cstnetId }</strong>
				<span class="success-text small-font"><fmt:message key='common.leftBracket'/><fmt:message key='common.userInfo.username.verified'/><fmt:message key='common.rightBracket'/></span>
		    </div>
	    </div>
	    <c:if test="${loginInfo.user.type=='uc' }">
			<div style="padding:0; position:relative; word-break:normal; font-size:13px">
				<div class="popover right" style="display:inline-block; right:-50%; left:50%; top:-100px">
		            <div class="arrow"></div>
		            <h3 class="popover-title"><fmt:message key="passport.merge.hintTitle"/><a class="closeTooltip" href="#" style="float:right;margin:0px 5px;font-size:12px"><fmt:message key="passport.merge.close"/></a></h3>
		            <div class="popover-content">
		             <p><fmt:message key="passport.merge.hint"/><a href="<umt:url value="/user/merge.do?act=show"/>"><fmt:message key="passport.merge.toKnow"/></a><fmt:message key="passport.merge.useOnePassword"/></p>
		            </div>
		        </div>
			</div>
		</c:if>
	</c:otherwise>
</c:choose>

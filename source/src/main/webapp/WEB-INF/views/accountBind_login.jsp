<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<fmt:setBundle basename="application" />
<h2 class="total-title"><fmt:message key="accountBind.welcome"/><span class="success-text">${thirdParty_user}</span></h2>
<p class="congratulation small-font gray-text"><fmt:message key="accountBind.hasLogined"/>
	<c:if test="${thirdParty!=null}">${thirdParty.name}</c:if>
	<c:if test="${thirdParty==null}">
	<fmt:message key='thirdParty.${thirdParty_type}'>
		<fmt:param><sup>[<a target="_blank" href="${thirdParty_url }">?</a>]</sup></fmt:param>
	</fmt:message>
	</c:if>
<fmt:message key="accountBind.hasLoginedwith"/></p>
<p class="congratulation small-font"><strong>
<fmt:message key="accountBind.hasPassport">
			<fmt:param><sup>[<a target="_blank" href="<umt:url value="/help.jsp"/>">?</a>]</sup></fmt:param>
</fmt:message>
<c:choose>
	<c:when test="${thirdParty_type=='cashq' }">
		<fmt:message key="accountBind.cashq.hint"/>  
	</c:when> 
	<c:otherwise>
		<fmt:message key="accountBind.hasPassport.youcan"/>
		<a id="bind_link" href="#" onclick="$('#bind_table').toggle();"><fmt:message key="accountBind.bindPassport"/></a>
	</c:otherwise>
</c:choose>


<c:if test="${isQQ }">
	<fmt:message key='thirdParty.bind.qq.hint'/>
</c:if>
</strong></p>
<form id="bindForm" class="form-horizontal" method="post" action="<umt:url value="/bind.do"/>?act=bindUmt">
	<!-- 登陆 -->
	<div id="bind_table" <c:if test="${thirdParty_type!='cashq'&&empty hidden}"> style="display:none"</c:if>>
		<div class="control-group">
		    <label class="control-label" for="loginName">
				<span class="ness">*</span><fmt:message key="regist.form.username"/>
			</label>
	        <div class="controls">
	          	<input id="loginName" name="loginName" type="text"></input>
				<span id="loginName_error_place" class="error help-inline">
					<c:if test="${!empty loginName_error }">
						<fmt:message key="${loginName_error }"/>
					</c:if>
				</span>
	         </div>
	     </div>
	     <div class="control-group">
		    <label class="control-label" for="loginPassword">
				<span class="ness">*</span><fmt:message key="regist.form.password"/>
			</label>
	        <div class="controls">
	          	<input id="loginPassword" name="loginPassword" type="password"></input>
				<span id="loginPassword_error_place" class="error help-inline">
					<c:if test="${!empty loginPassword_error }">
						<fmt:message key="${loginPassword_error }"/>
					</c:if>
				</span>
	         </div>
	     </div>
	     <div class="control-group">
	        <div class="controls">
	          	<input type="submit" class="btn long btn-primary" value="<fmt:message key="accountBind.bindnow"/>" />
	         </div>
	     </div>
     </div>
	<input type="hidden" name="screenName" value="${thirdParty_user}"/>
	<input type="hidden" name="openId" value="${thirdParty_openId}"/>
	<input type="hidden" name="type" value="${thirdParty_type }"/>
</form>

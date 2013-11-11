<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>   
<fmt:setBundle basename="application" />
<umt:AppList/>
<umt:refreshUser/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title><fmt:message key='banner.accountSetting'/></title>
		<link href="<umt:url value="/images/favicon.ico"/>" rel="shortcut icon" type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
	</head>
	<body class="login">
		<jsp:include flush="true" page="banner2013.jsp">
			<jsp:param name="DecludeMenu" value="true"/>
		</jsp:include>
		
		<div class="container login gray">
			<ul class="sub-nav">
				<li class="active" id="accountManage"><a href="<umt:url value="/user/manage.do?act=showManage"/>"><fmt:message key='accountManage.accountInfo'/></a></li>
				<c:if test="${'qq'!=loginInfo.user.type||'weibo'!=loginInfo.user.type }">
					<li id="changePsw"><a href="<umt:url value="/user/manage.do?act=showChangePassword"/>"><fmt:message key='accountManage.changePassword.title'/></a></li>
				</c:if>
				<li id="accountBind"><a href="<umt:url value="/user/manage.do?act=showBindAccount"/>"><fmt:message key='accountManage.bind.title'/></a></li>
				<div class="clear"></div>
			</ul>
			<div class="sub-content" id="accountManageShow">
				<h4 class="sub-title">
					<fmt:message key='accountManage.account.title'/>
					<span class="sub-text"><fmt:message key='accountManage.account.hint'/></span>
				</h4>
				<div class="send_mail manage">
					<div class="mail_left"></div>
					<div class="mail_right">
						<span class="safty-ok"></span><strong><fmt:message key="accountManage.cstnetId"></fmt:message></strong>
						<div class="sub-text inner">
						<c:if test="${loginInfo.user.type=='umt'}">
							<fmt:message key='accountManage.cstnetId.hint'/>
						</c:if>
						<c:if test="${loginInfo.user.type=='coreMail'||loginInfo.user.type=='uc'}">
							<fmt:message key='accountManage.cstnetIdisCoreMail.hint'/>
						</c:if>
						</div>
						<c:if test="${loginInfo.user.type=='uc'}">
							<div style="padding:0; position:relative;word-break:normal;">
								<div class="popover right small-font" style="display:inline-block; right:-50%; left:50%; top:-50px">
						            <div class="arrow"></div>
						            <h3 class="popover-title"><fmt:message key="passport.merge.hintTitle"/><a id="closeTooltip" href="#" style="float:right;margin:0px 5px;font-size:12px"><fmt:message key="passport.merge.close"/></a></h3>
						            <div class="popover-content">
						              <p><fmt:message key="passport.merge.hint"/><a href="<umt:url value="/user/merge.do?act=show"/>"><fmt:message key="passport.merge.toKnow"/></a><fmt:message key="passport.merge.useOnePassword"/></p>
						            </div>
						        </div>
							</div>
						</c:if>
						
						<p class="mail_detail">
							<strong>${primaryEmail.loginName }</strong>
							<span class="success-text"><fmt:message key='common.userInfo.username.verified'/></span>
							<c:if test="${false }">
								<span class="success-text">
									<a href="<umt:url value="/user/primary/loginName.do?act=showPrimaryLoginEmailStep1"/>">更改账号</a>
								</span>
							</c:if>
						</p>
						<c:if test="${loginInfo.user.type=='coreMail'||loginInfo.user.type=='uc' }">
							<c:choose>
								<c:when test="${fn:endsWith(loginInfo.user.cstnetId,'@escience.cn') }">
									<p class="sub-text inner"><fmt:message key='accountManage.escience.cn'/></p>
								</c:when>
								<c:otherwise>
									<p class="sub-text inner"><fmt:message key='accountManage.coremail.hint'/></p>
								</c:otherwise>
							</c:choose>
						</c:if>
						<c:if test="${!empty primaryEmail.tmpLoginName }">
							<p>
								<span class="sub-text inner"><fmt:message key='accountManage.modify.history'/>${primaryEmail.tmpLoginName }</span>
								<span class="sub-text"><fmt:message key='accountManage.modify.notVerify'/><a href="<umt:url value="/temp/activation.do?act=sendLoginEmail&loginEmail=${primaryEmail.tmpLoginName }"/>"><fmt:message key='common.userInfo.username.toVerify'/></a></span>
							</p>
						</c:if>
					</div>
					<div class="clear"></div>
				</div>
				<div class="send_mail dashBorder manage second">
					<div class="mail_left"></div>
					<div class="mail_right">
						<strong><fmt:message key='accountManage.secondaryMail'/></strong>
						<div class="sub-text inner"><fmt:message key='accountManage.secondaryMail.hint'/></div>
						<c:forEach items="${secondaryEmail}" var="loginName">
						<p class="mail_detail">
							<strong>${loginName.loginName}</strong>
							<c:choose>
								<c:when test="${loginName.status=='active' }">
									<span class="success-text">
										<fmt:message key='accountManage.account.isVerified'/>
									</span>
									<span class="success-text">
										<a href="<umt:url value="/user/secondary/loginName.do?act=updateSecondary&email=${loginName.loginName}&loginNameInfoId=${loginName.id }"/>"><fmt:message key="safeItems.change"/></a>
										<a class="delete" url="<umt:url value="/user/secondary/loginName.do?act=deleteSecondary&loginNameInfoId=${loginName.id}&from=manage"/>" onclick="confirmDelete(this);"/><fmt:message key="appmanage.btn.remove"/></a>
									</span>
								</c:when>
								<c:when test="${loginName.status=='temp' }">
									<span class="small-font">
										<fmt:message key='accountManage.account.isNotVerified'/>
										<a class="delete" href="<umt:url value="/temp/activation.do?act=sendSecondaryEmail&loginNameInfoId=${loginName.id }"/>"><fmt:message key='common.userInfo.username.toVerify'/></a>
									</span>
									<span class="success-text">
										<a href="<umt:url value="/user/secondary/loginName.do?act=updateSecondary&email=${loginName.loginName}&loginNameInfoId=${loginName.id }"/>"><fmt:message key="safeItems.change"/></a>
										<a class="delete" url="<umt:url value="/user/secondary/loginName.do?act=deleteSecondary&loginNameInfoId=${loginName.id}&from=manage"/>" onclick="confirmDelete(this);"/><fmt:message key="appmanage.btn.remove"/></a>
									</span>
								</c:when>
							</c:choose>
						</p>
						
						<c:if test="${!empty loginName.tmpLoginName }">
						 	<p>
								<span class="sub-text inner"><fmt:message key='accountManage.modify.history'/>${loginName.tmpLoginName }</span>
								<span class="sub-text"><fmt:message key='accountManage.modify.notVerify'/><a href="<umt:url value="/temp/activation.do?act=sendSecondaryEmail&loginNameInfoId=${loginName.id }"/>"><fmt:message key='common.userInfo.username.toVerify'/></a></span>
							</p>
						</c:if>
					</c:forEach>
					<p class="small-font">
						<a href="<umt:url value="/user/secondary/loginName.do?act=setSecondaryEmail"/>"><fmt:message key='accountManage.secondaryMail.add'/></a>
					</p>
					</div>
					<div class="clear"></div>
				</div>
			</div>
		</div>
		
		<jsp:include flush="true" page="bottom2013.jsp"></jsp:include>
		
	</body>
	
	<script type="text/javascript" >
		function confirmDelete(obj){
			var result=confirm("<fmt:message key='accountManage.secondaryMail.delete'/>");
			if(result){
				window.location.href=$(obj).attr("url");
			}
		}
		$(document).ready(function(){
			$('#banner_user_manage').addClass("active");
			$("#closeTooltip").live("click",function(){
				$(this).parent().parent().hide();
			})
		});
	</script>
</html>
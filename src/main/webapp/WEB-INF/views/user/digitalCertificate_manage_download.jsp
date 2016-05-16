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
		<title><fmt:message key='banner.digitalCertificateSetting'/></title>
		<link href="<%= request.getContextPath() %>/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		
		<style type="text/css">
		table.table.accountManage td {border-top: 0;}
		table.table.accountManage {margin-top:0px;margin-bottom:0px;text-align:left;}
		</style>
	</head>
	<body class="login">
		<jsp:include flush="true" page="../../../banner2013.jsp">
			<jsp:param name="DecludeMenu" value="true"/>
		</jsp:include>
		
		<div class="container login gray">
			<ul class="sub-nav">
				<li id="accountManage"><a href="<umt:url value="/user/digitalCertificate.do?act=index"/>"><fmt:message key='digitialManage.title.digitialManage' /></a></li>
				<li class="active" id="accountApp"><a href="<umt:url value="/user/digitalCertificate.do?act=manage"/>"><fmt:message key='digitialManage.title.manage' /></a></li>
				<%-- <li id="accountBind"><a href="<umt:url value="/user/digitalCertificate.do?act=record"/>">登录记录</a></li> --%>
				<li id="accountBind"><a href="<umt:url value="/user/digitalCertificate.do?act=help"/>"><fmt:message key='digitialManage.title.help' /></a></li>
				<div class="clear"/>
			</ul>
			<div class="sub-content" id="accountManageShow">
			
				<h4 class="sub-title"><fmt:message key="digitialManage.manage.download.title"/></h4>
				<p class="sub-text blackfont"><strong><fmt:message key="digitialManage.manage.download.title.dn"/></strong></p>
				<p class="sub-text blackfont">${ca.dn }</p>
				<p class="sub-text blackfont"><strong><fmt:message key="digitialManage.manage.download.title.type"/></strong></p>
				<p class="sub-text blackfont"><fmt:message key="digitialManage.manage.type.eduroma"/></p>
				<p class="sub-text blackfont"><strong><fmt:message key="digitialManage.manage.download.title.sysType"/></strong></p>
				<table class="table accountManage">
				<tbody>
					<tr>
						<td style="padding:0;"><p class="sub-text blackfont"><fmt:message key="digitialManage.manage.download.sys"/></p></td>
					<td>
					<p style="margin:0;">
						<select id="systemType" style="margin:0;"> 
							<option selected value="WindowsXP">Windows XP</option>
							<option value="Windows7">Windows 7</option> 
							<option value="Windows8">Windows 8</option> 
							<option value="WindowsVista">Windows Vista</option> 
							<option value="Windows10">Windows 10</option> 
							<option value="Linux">Linux</option> 
							<option value="MacOsX">Mac OS X</option> 
							<option value="IOS">IOS(Iphone,Ipad)</option> 
							<option value="Android">Android</option> 
						</select>
						<!--<select > 
							<option selected>Internet Explorer(IE)</option>
							<option>Firefox</option> 
							<option>Chrome</option> 
							<option>Safari</option> 
							<option>Edge</option> 
						</select>   -->
					</p>
					</td>
					</tr>
					<tr>
						<td></td>
						<td>
						<p>
						<a class="useHelp WindowsXP"><fmt:message key="digitialManage.manage.download.howUse"><fmt:param>Windows XP</fmt:param></fmt:message></a>
						<a class="useHelp Windows7 " style="display:none"><fmt:message key="digitialManage.manage.download.howUse"><fmt:param>Windows 7</fmt:param></fmt:message></a>
						<a class="useHelp Windows8 " style="display:none"><fmt:message key="digitialManage.manage.download.howUse"><fmt:param>Windows 8</fmt:param></fmt:message></a>
						<a class="useHelp WindowsVista " style="display:none"><fmt:message key="digitialManage.manage.download.howUse"><fmt:param>Windows Vista</fmt:param></fmt:message></a>
						<a class="useHelp Windows10 " style="display:none"><fmt:message key="digitialManage.manage.download.howUse"><fmt:param>Windows 10</fmt:param></fmt:message></a>
						<a class="useHelp Linux " style="display:none"><fmt:message key="digitialManage.manage.download.howUse"><fmt:param>Linux</fmt:param></fmt:message></a>
						<a class="useHelp MacOsX " style="display:none"><fmt:message key="digitialManage.manage.download.howUse"><fmt:param>Mac OS X</fmt:param></fmt:message></a>
						<a class="useHelp IOS " style="display:none"><fmt:message key="digitialManage.manage.download.howUse"><fmt:param>IOS(Iphone,Ipad)</fmt:param></fmt:message></a>
						<a class="useHelp Android " style="display:none"><fmt:message key="digitialManage.manage.download.howUse"><fmt:param>Android</fmt:param></fmt:message></a>
						</p>
						<P>
						<button id="download" class="btn long btn-primary general"><fmt:message key="digitialManage.manage.download.start"/></button>
						<button id="downloadCert" class="btn long btn-primary linux" style="display:none"><fmt:message key="digitialManage.manage.download.linux.persionCert"/></button>
						<button id="downloadKey" class="btn long btn-primary linux"style="display:none"><fmt:message key="digitialManage.manage.download.linux.keypari"/></button>
						</P>
						</td>
					
					</tr>
					<tr>
						<td style="border-top: 1px solid #ddd;"></td>
						<td style="border-top: 1px solid #ddd;"><button id="downloadAll"  class="btn long btn-primary"><fmt:message key="digitialManage.manage.download.btn.downAll"/></button><span style="float:right;"><button id="applyNew" class="btn long btn-primary"><fmt:message key="digitialManage.btn.apply.new"/></button></span></td>
					</tr>
				</tbody>
					
					
				
				</table>
				
				
			</div>
		</div>
		
		<jsp:include flush="true" page="../../../bottom2013.jsp"></jsp:include>
		<script type="text/javascript" >
			$(document).ready(function(){
				
				
				
				$("#systemType").live("change",function(){
					if($(this).val()=="Linux"){
						$(".linux").show();
						$(".general").hide();
					}else{
						$(".linux").hide();
						$(".general").show();
					}
					
					$(".useHelp").hide();
					$(".useHelp."+$(this).val()).show();
					
				});
				
				var platformType="${userAgent.platformType}";
				var platformSeries="${userAgent.platformSeries}";
				$("#systemType").val(platformType+platformSeries);
				$("#systemType").trigger("change");
				
				
				
				$("#download").live("click",function(){
					window.open("<umt:url value='/user/digitalCertificate.do?act=download'/>&caId=${ca.id}");
				});
				
				$("#downloadCert").live("click",function(){
					window.open("<umt:url value='/user/digitalCertificate.do?act=download'/>&caId=${ca.id}&type=cert");
				});
				
				$("#downloadKey").live("click",function(){
					window.open("<umt:url value='/user/digitalCertificate.do?act=download'/>&caId=${ca.id}&type=key");
				});
				
				$("#downloadAll").live("click",function(){
					window.open("<umt:url value='/user/digitalCertificate.do?act=download'/>&caId=${ca.id}&type=all");
				});
				
				$("#applyNew").on("click",function(){
					window.location.href='<umt:url value="/user/digitalCertificate.do?act=applyView"/>';
				});
				
				
			});
			
			
		</script>
	</body>
</html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<fmt:setBundle basename="application" />
<umt:AppList/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	request.setAttribute("msg", request.getParameter("msg"));
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title><fmt:message key="accountSafty.title"/></title>
		<link href="<%= request.getContextPath() %>/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
	</head>
	<body class="login">
		<jsp:include flush="true" page="../../../banner2013.jsp">
			<jsp:param name="DecludeMenu" value="true"/>
		</jsp:include>
		
		<div class="container login gray">
			<ul class="sub-nav">
				<c:if test="${'weibo' != loginInfo.user.type }">
					<li><a href="<umt:url value="/user/safe.do?act=showSecurity"/>"><fmt:message key="accountSafty.setting"/></a></li>
				</c:if>
				<li class="active"><a href="<umt:url value="/user/safe.do?act=showLog"/>"><fmt:message key="accountSafty.logInfo"/></a></li>
				<div class="clear"></div>
			</ul>
			<div id="operListShow">
				<ul style="min-height:590px" class="oper"> 
					<li class="log_item active" target="loginList"><a><fmt:message key="accountSafty.logitem.login"/></a></li>
					<li class="log_item" target="changePswList"><a><fmt:message key="accountSafty.logitem.changePsw"/></a></li>
					<li class="log_item"  target="securityEmailList"><a><fmt:message key="accountSafty.logitem.changeSecuirtyEmail"/></a></li>
					<!-- <li class="log_item"><a><fmt:message key="accountSafty.logitem.changeUsername"/></a></li> -->
					<!--li class="log_item"><a>手机绑定修改记录</a></li-->
				</ul>
				<div id="changePswList" class="loginList targetBoard" style="display:none">
					<table class="saftyList" style="table-layout:fixed" >
						<tr>
							<th><fmt:message key="accountSafty.securityEmailList.data"/></th>
							<th><fmt:message key="accountSafty.securityEmailList.time"/></th>
							<th><fmt:message key="accountSafty.securityEmailList.ip"/></th>
						</tr>
						<c:forEach items="${changePasswordMessage }" var="msg">
							<tr>
								<td><fmt:formatDate value="${msg.occurTime }" type="date"/></td>
								<td><fmt:formatDate value="${msg.occurTime }" type="time"/></td>
								<td>${msg.userIp }</td>
							</tr>
						</c:forEach> 
					</table>
				</div>
				<div id="loginList" class="loginList targetBoard" >
					
					<c:choose>
						<c:when test="${empty warnLog }">
						<h3 style="color:green;margin-left:20px">
							<fmt:message key='index.diff.regist.none'/>
							</h3>
						</c:when>
						<c:otherwise>
						<h3 style="color:red;margin-left:20px">
							<fmt:message key='index.diff.regist.has'/>
							</h3>
						</c:otherwise>
					</c:choose>
					<div style="margin:15px;color:#999"><fmt:message key='diff.regist.hint'/>,
					<fmt:message key='diff.regist.hint.2'/><a href="<umt:url value="/user/manage.do?act=showChangePassword"/>"><fmt:message key='accountManage.changePassword.title'/></a>
					</div>
					<c:choose>
						<c:when test="${loginInfo.user.sendGEOEmailSwitch }">
						<span style="margin:15px"><b>您已开启邮件提醒！</b></span>  <a class="switchGEOEmail" style="cursor: pointer" href="<umt:url value="/user/safe.do?act=switchGEOEmail&value=false"/>"  >关闭邮件提醒</a>  
						
							<div style="margin:15px"><fmt:message key='not.email.me'/>
														
							</div>
							<ul class="COMMMONGEO">
							<c:forEach items="${commonGEOs }" var="geo">
								<li>${geo.displayGEO() } <a class="removeCommonGEO" data-id="${geo.id }">×</a></li>
							</c:forEach>
								<li><a id="addCommmonGEO"  class="addCOMMMONGEO"><fmt:message key="appmanage.btn.add"/><span>+</span></a></li>
							</ul>
						</c:when>
						<c:otherwise>
							<div > 
								<span style="margin:15px"><b>您已关闭邮件提醒！</b></span>  
								<a class="switchGEOEmail" href="<umt:url value="/user/safe.do?act=switchGEOEmail&value=true"/>" style="cursor: pointer" data-value="false" >开启邮件提醒</a>
							</div>
						</c:otherwise>
					</c:choose>
					
					<table class="saftyList" >
						<tr>
							<th style="width:90px"><fmt:message key="accountSafty.loginList.date"/></th>
							<th style="width:90px"><fmt:message key="accountSafty.loginList.time"/></th>
							<th style="width:120px"><fmt:message key="accountSafty.loginList.ip"/></th>
							<th style="width:150px"><fmt:message key='accountSafty.loginList.city'/></th>
							<th style="width:120px"><fmt:message key="accountSafty.loginList.loginType"/></th>
						</tr>
						<c:forEach items="${loginMessage }" var="msg">
							<tr style="<c:if test="${msg.sendWarnEmail }">color:red</c:if>">
								<td><fmt:formatDate value="${msg.occurTime }" type="date"/></td>
								<td><fmt:formatDate value="${msg.occurTime }" type="time"/></td>
								<td>${msg.userIp }</td>
								<td>
									${msg.displayGEO() }
								</td>
								<td>
								<c:choose>
									<c:when test="${msg.appName=='weibo' }">
										<fmt:message key='thirdParty.weibo'/>
									</c:when>
									<c:when test="${msg.appName=='qq' }">
										<fmt:message key='thirdParty.qq'/>
									</c:when>
									<c:when test="${msg.appName=='cashq' }">
										<fmt:message key='thirdParty.cashq'/>
									</c:when>
									<c:when test="${msg.appName=='umt' }">
										<fmt:message key='login.username.placeholder'/>
									</c:when>
									<c:otherwise>
										${msg.appName}
									</c:otherwise>
								</c:choose>
								
								</td>

							</tr>
						</c:forEach>
						
						
					</table>
				</div>
				<div id="securityEmailList" style="display:none" class="loginList targetBoard">
					<table class="saftyList" >
						<tr>
							<th><fmt:message key="accountSafty.securityEmailList.data"/></th>
							<th><fmt:message key="accountSafty.securityEmailList.time"/></th>
							<th><fmt:message key="accountSafty.securityEmailList.ip"/></th>
						</tr>
						<c:forEach items="${changeSecurityEmailMessage }" var="msg">
							<tr>
								<td><fmt:formatDate value="${msg.occurTime }" type="date"/></td>
								<td><fmt:formatDate value="${msg.occurTime }" type="time"/></td>
								<td>${msg.userIp }</td>
							</tr>
						</c:forEach>
					</table>
				</div>
				
				<div class="clear"></div>
			</div>
		</div>
		<div id="addCommonGEODialog" class="modal hide fade" tabindex="-1" role="dialog" data-backdrop='true' aria-hidden="true">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			<h4 id="title"><fmt:message key='geo.dialog.title'/></h4>
		</div>
		<div class="modal-body">
			<p id="content">
				<select id="commonSelect">
				</select>
				<span style="color:red;display:none" id="commonError" ></span>
			</p>
		</div>
		<div class="modal-footer">
			<a class="btn btn-primary"  id="yes"><fmt:message key='remindpass.submit'/></a>
			<a class="btn" id="no" data-dismiss="modal" aria-hidden="true"><fmt:message key='inputpassword.cancel'/></a>
		</div> 
	</div>
		<jsp:include flush="true" page="../../../bottom2013.jsp"></jsp:include>
		
		<script type="text/javascript" >
			$(document).ready(function(){
				$(".log_item").live("click",function(){
					$(".log_item").each(function(i,n){
						$(n).removeClass("active");
					});
					$(".targetBoard").each(function(i,n){
						$(n).hide();
					});
					$(this).addClass("active");
					$('#'+$(this).attr("target")).show();
				});
				//点保存
				$('#yes').on('click',function(){
					var $option=$('#commonSelect>option:selected');
					if($option.length==0){
						$('#commonError').show().html('<fmt:message key="geo.no.common.place"/>').fadeOut(3000);
						return;
					}else{
						var $data=$option.data();
						$.post('safe.do?act=addCommon&_='+Math.random(),$data).done(function(result){
							switch(result){
								case "param.error":{
									$('#commonError').show().html('<fmt:message key="geo.param.miss"/>').fadeOut(3000);
									return;
								}
								case "geo.exists":{
									$('#commonError').show().html('<fmt:message key="geo.place.has"/>').fadeOut(3000);
									return;
								}
								case "too.many":{
									$('#commonError').show().html('<fmt:message key="geo.place.three"/>').fadeOut(3000);
									return;
								}
								case "add.success":{
									window.location.reload();
									return;
								}
								
							}
						});
					}
				});
				//删除常用地
				$('.removeCommonGEO').live('click',function(){
					var id=	$(this).data('id');
					var $self=$(this);
					$.post('safe.do?act=deleteCommon&_='+Math.random(),{id:id}).done(function(result){
						if(result=='true'){
							$self.closest('li').remove();
						}else{
							alert('<fmt:message key="geo.remove.error"/>');
						}
					});
				});
				//添加常用地
				$('#addCommmonGEO').on('click',function(){
					$('#addCommonGEODialog').modal('show');
					$.get("safe.do?act=searchCommon&_="+Math.random()).done(function(result){
						if(result.length==0){
							$('#commonSelect').empty();
						}else{
							var html='';
							for(var index in result){
								html+='<option data-country="'+result[index].country+'" data-province="'+result[index].province+'" data-city="'+result[index].city+'">'+result[index].display+'</option>';
							}
							$('#commonSelect').html(html);
						}
					});
				});
				$('#banner_user_safe').addClass("active");
				$('.oper').height($('#loginList').height());
			});
		</script>
	</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@ taglib prefix="umt" uri="WEB-INF/tld/umt.tld"%>
<fmt:setBundle basename="application" />
<%
        pageContext.setAttribute("contextPath", request.getContextPath());
        pageContext.setAttribute("DecludeMenu", request.getParameter("DecludeMenu"));
        String showLogin = request.getParameter("showLogin");
%>
<meta name="viewport" content="width=device-width, initial-scale=1" /><!-- for responsive design  -->
<script type="text/javascript" src="${contextPath}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${contextPath}/js/menu.js"></script>
<script type="text/javascript" src="${contextPath}/js/cookie.js"></script>
<div class="navbar navbar-inverse navbar-fixed-top nav-bar fix-top">
<div class="navbar-inner">
<div class="container">
        <div class="logoDiv">
        <div class="logo logo-title"><fmt:message key='banner.title' /></div></div>
        <div class="nav-collapse collapse">
        		<ul style="margin:-2px 0px;">
					<li><a href="<umt:url value="/"/>"><fmt:message key="banner.index"/></a></li>
				</ul>
                <ul class="nav-right">

                	<%
                		if(showLogin == null || !"false".equals(showLogin)) {
                	%>
                	
                	<umt:HasLogin>
                        <li>
                            <umt:CurrentUserName />
                        </li>
                      
                	</umt:HasLogin>

                	<%
                		}
                	%>
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
					  <li>
                            <a href="${contextPath}/logout"><fmt:message key='banner.logout' /> </a>
                        </li>
                </ul>
                <div  id="langmenu" style="display:none">
                        <ul>
                                <li>
                                        <a onclick="changeLocale('zh_CN')">中文</a>
                                        <a onclick="changeLocale('en_US')">English</a>
                                </li>
                        </ul>
                </div>
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
                                });
                        </script>
        </div></div></div>
</div>
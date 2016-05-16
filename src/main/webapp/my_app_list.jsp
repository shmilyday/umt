<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<fmt:setBundle basename="application" />
<umt:AppList/>
<ul class="headerBar" id="indexList">
				<c:forEach var="app" items="${myAppList }">
					<c:choose>
						<c:when test="${app=='escience' }">
							<li>
								<a class="header-block escience" href="${escience_loginUrl }" target="_blank">
									<span class="logo"></span> 
									<span class="header-text"><fmt:message key='common.escience'/></span>
								</a>
							</li>
						</c:when>
						<c:when test="${app=='escience' }">
							<li>
								<a class="header-block escience" href="${escience_loginUrl }" target="_blank">
									<span class="logo"></span> 
									<span class="header-text"><fmt:message key='common.escience'/></span>
								</a>
							</li>
						</c:when>
						<c:when test="${app=='ddl' }">
							<li>
								<a class="header-block ddl" href="${ddl_loginUrl }" target="_blank">
									<span class="logo"></span> 
									<span class="header-text"><fmt:message key='common.ddl.escience'/></span>
								</a>
							</li>
						</c:when>
						<c:when test="${app=='dhome' }">
							<li>
								<a class="header-block dhome" href="${dhome_loginUrl }" target="_blank">
									<span class="logo"></span> 
									<span class="header-text"><fmt:message key='common.scholarPage'/></span>
								</a>
							</li>
						</c:when>
						<c:when test="${app=='csp' }">
							<li>
								<a class="header-block csp" href="${csp_loginUrl }" target="_blank">
									<span class="logo "></span> 
									<span class="header-text"><fmt:message key='common.csp'/></span>
								</a>
							</li>
						</c:when>
						<c:when test="${app=='mail' }">
							<li>
								<a class="header-block mail" href="${mail_loginUrl }" target="_blank">
									<span class="logo"></span> 
									<span class="header-text"><fmt:message key='common.email'/></span>
								</a>
							</li>
						</c:when>
						<c:when test="${app=='site' }">
							<li>
								<a class="header-block nav" href="${site_loginUrl }" target="_blank">
									<span class="logo"></span> 
									<span class="header-text"><fmt:message key='common.science.source'/></span>
								</a>
							</li>
						</c:when>
						<c:when test="${app=='rol' }">
							<li>
								<a class="header-block rol" href="${rol_loginUrl }" target="_blank">
									<span class="logo"></span> 
									<span class="header-text"><fmt:message key='common.test.info'/></span>
								</a>
							</li>
						</c:when>
					</c:choose>
				</c:forEach>
				<div class="clear"></div>
			</ul>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<fmt:setBundle basename="application" />
<umt:AppList/>
<ul class="headerBar" id="indexList">
				<li class="${!empty appName&&appName=='escience'?'active':''}">
					<a class="header-block escience" href="${escience_loginUrl }" target="_blank">
						<span class="logo header-escience"></span> 
						<span class="header-text"><fmt:message key='common.escience'/></span>
					</a>
				</li>
				<li class="${!empty appName&&appName=='ddl'?'active':''}">
					<a class="header-block ddl" href="${ddl_loginUrl }" target="_blank">
						<span class="logo"></span> 
						<span class="header-text"><fmt:message key="common.ddl.escience"/></span>
					</a>
				</li>
				<li class="${!empty appName&&appName=='dhome'?'active':''}">
					<a class="header-block dhome" href="${dhome_loginUrl }" target="_blank">
						<span class="logo"></span> 
						<span class="header-text"><fmt:message key="common.scholarPage"/></span>
					</a>
				</li>
				<li class="${!empty appName&&appName=='csp'?'active':''}">
					<a class="header-block csp" href="${csp_loginUrl }" target="_blank">
						<span class="logo"></span> 
						<span class="header-text"><fmt:message key="common.csp"/></span>
					</a>
				</li>
				<li class="${!empty appName&&appName=='mail'?'active':''}">
					<a class="header-block email" href="${mail_loginUrl }" target="_blank">
						<span class="logo"></span> 
						<span class="header-text"><fmt:message key="common.email"/></span>
					</a>
				</li>
				<li class="${!empty appName&&appName=='site'?'active':''}">
					<a class="header-block nav" href="${site_loginUrl }" target="_blank">
						<span class="logo"></span> 
						<span class="header-text" style="font-size:12px"><fmt:message key='common.science.source'/></span>
					</a>
				</li>
				<li class="${!empty appName&&appName=='dc'?'active':''}">
					<a class="header-block dc" href="${dc_loginUrl }" target="_blank">
						<span class="logo"></span> 
						<span class="header-text"><fmt:message key="common.test.info"/></span>
					</a>
				</li>
				<li class="${!empty appName&&appName=='dchat'?'active':''}">
					<a class="header-block dchat" href="http://dchat.escience.cn/" target="_blank">
						<span class="logo"></span> 
						<span class="header-text"><fmt:message key="common.dchat"/></span>
					</a>
				</li>
				<li class="${!empty appName&&appName=='vmt'?'active':''}">
					<a class="header-block vmt" href="http://vmt.escience.cn" target="_blank">
						<span class="logo"></span> 
						<span class="header-text"><fmt:message key="common.vmt"/></span>
					</a>
				</li>
				<li>
					<a class="header-block cos" href="http://service.cstnet.cn" target="_blank">
						<span class="logo"></span> 
						<span class="header-text"><fmt:message key="common.cos"/></span>
					</a>
				</li>
				<div class="clear"></div>
			</ul>
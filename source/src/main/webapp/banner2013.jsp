<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@ taglib prefix="umt" uri="WEB-INF/tld/umt.tld"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<fmt:setBundle basename="application" />
<%
        pageContext.setAttribute("contextPath", request.getContextPath());
%>
<meta  name="viewport" content="width=device-width, initial-scale=1" /><!-- for responsive design  -->
<link href="<%= request.getContextPath() %>/images/favicon.ico?v=<f:version/>" rel="shortcut icon"  type="image/x-icon" />

<f:css href="${contextPath }/thirdparty/bootstrap/css/bootstrap.min.css"/>
<f:css href="${contextPath }/thirdparty/bootstrap/css/bootstrap-responsive.min.css"/>
<f:css href="${contextPath }/css/umt2013.css"/>
<f:css href="${contextPath }/css/umt2013-responsive.css"/>
   
<f:script  src="${contextPath }/js/jquery-1.7.2.min.js"/>
<f:script  src="${contextPath }/js/jquery.validate.min.js"/>
<f:script  src="${contextPath }/js/ext_validate.js"/>
<f:script  src="${contextPath }/js/menu.js"/>
<f:script  src="${contextPath }/js/cookie.js"/>
<f:script  src="${contextPath }/thirdparty/bootstrap/js/bootstrap.min.js"/>
<f:script  src="${contextPath }/thirdparty/respond.src.js"/>
     
<jsp:include page="banner2013HtmlOnly.jsp"></jsp:include>

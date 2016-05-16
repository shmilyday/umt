<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="umt" uri="WEB-INF/tld/umt.tld" %>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<!DOCTYPE html>
<%
        pageContext.setAttribute("contextPath", request.getContextPath());
%>

<html>
	<head>
		<title>科技网无线WiFi联盟</title>
		<f:css href="${contextPath }/thirdparty/bootstrap/css/bootstrap.min.css"/>
		<f:css href="${contextPath }/thirdparty/bootstrap/css/bootstrap-responsive.min.css"/>
		<f:css href="${contextPath }/css/wifi.css"/>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1" />
	</head>

	<body class="wifi"> 
		<jsp:include flush="true" page="/banner2013.jsp">
			<jsp:param name="DecludeMenu" value="true"/>
		</jsp:include>
		<div class="wifi container">
			<h3 class="title">欢迎使用CSTNET/eduroam联盟</h3>
			<p class="subTitile">您所在的域${userDomain}暂未加入CSTNET/eduroam联盟，无法开通CSTNET/eduroam无线服务。</p>
			<p class="subHint">eduroam是一种全球学术网络WiFi漫游服务，目前已经覆盖全球七十多个国家和地区的大学及科研机构。参与该联盟的机构只需在本单位设立eduroam账户，即可使用原机构提供的合法账号，在全球已参与eduroam计划的上千家学校和研究机构内免费使用无线漫游服务。</p>
			<ul class="wifi-links">
				<li><a href="http://eduroam.cstnet.cn/">eduroam CN无线网漫游交换中心</a></li>
				<li><a href="http://eduroam.cstnet.cn/total/283.jhtml">eduroam CN漫游研究所和大学列表</a></li>
			</ul>
			<div class="control-group">
	       		<div class="controls">
					<a href="<umt:url value=""/>" class="btn btn-primary">返回首页</a>
				</div>
			</div>
		</div>
		<jsp:include flush="true" page="/bottom2013.jsp"></jsp:include>
	</body>
</html>
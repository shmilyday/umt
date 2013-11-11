<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

</body>
<script type="text/javascript" src="<umt:url value="/js/jquery-1.7.2.min.js"/>"></script>
<script type="text/javascript">
	$(document).ready(function(){
		getAccessToken();
		function getAccessToken(){
			var hashParam = window.location.hash.substring(1);
			var accessToken = '';
			var params=hashParam.split("&");
			$.each(params,function(index,item){
				var param = item.split("=");
				if(param[0]=='access_token'){
					accessToken=param[1];
				}
			});
			alert(accessToken);
		}
	});
</script>
</html>
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
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<title>正在退出...</title>
<f:script  src="${contextPath }/js/jquery-1.7.2.min.js"/>
</head>
<body>

</body>
<script>
	$(document).ready(function(){
		//return json array like ["https://www.baidu.com","https://qq.com"]
		$.get('${uafLogOutUrl}').done(function(data){
			if(data||data.length>0){
				data=eval(data);
				var result=[];
				for(var i=0;i<data.length;i++){
					result.push(false);
					var iframe = document.createElement("iframe");
					iframe.onload = function() {
						result[this.index]=true;
						checkResult(result);
					};
					iframe.src = data[i];
					iframe.index=i;
					iframe.style.display='none';
					document.body.appendChild(iframe);
				}
			}
			//if all true ,then href
			function checkResult(result){
				var r=true;
				for(var i in result){
					r&=result[i];
				}
				if(r){
					setTimeout(function(){
						window.location.href='${WebServerURL}';
					},200);
				}
			}
		});
		
	});
</script>
</html>
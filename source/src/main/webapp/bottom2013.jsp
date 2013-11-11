<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@ taglib prefix="umt" uri="WEB-INF/tld/umt.tld"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<%pageContext.setAttribute("contextPath", request.getContextPath()); %>
<f:css href="${contextPath }/dface/css/dface.simple.footer.css"/>
<f:script src="${contextPath }/dface/js/dface.simple.footer.js"/>
<script type="text/javascript">
$(document).ready(function(){
	$(".dface.footer p span#app-version").html("(UMT <f:version/>)");
});
</script>
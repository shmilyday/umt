<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@ taglib prefix="umt" uri="WEB-INF/tld/umt.tld"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<%pageContext.setAttribute("contextPath", request.getContextPath()); %>
<script type="text/javascript">
$(document).ready(function(){
	window.umtLocale='${user_locale}';
});
</script>
<f:css href="${contextPath }/dface/css/dface.simple.footer.css"/>
<f:script src="${contextPath }/dface/js/dface.simple.footer.js"/>
<div id="footer">

</div>
<div class='dface container footer'>
		<div title="Click to Verify - This site chose Symantec SSL for secure e-commerce and confidential communications.">
			<script type="text/javascript" src="https://seal.websecurity.norton.com/getseal?host_name=passport.escience.cn&amp;size=S&amp;use_flash=NO&amp;use_transparent=YES&amp;lang=zh_cn"></script>	
		</div>
</div>
<script>
$(document).ready(function(){
	$(".dface.footer p span#app-version").html("(UMT <f:version/>)");
});
</script>

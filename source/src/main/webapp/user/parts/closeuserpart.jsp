<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<fmt:setBundle basename="application" />
<div class="content-container">
	<div class="toolBar">
		
		<fmt:message key="closeuser.title"/>
	</div>
	
		 <div class="clearboth red">
					<fmt:message key="closeuser.prompt"/>
	      </div>
	      <form action="closeUser.do" method="post" onsubmit="removeCookie()">
			<div class="clearboth but-center">
						<input type="submit"  class="but  btn-primary" value="<fmt:message key='closeuser.submit'/>" />
					
			</div>
			</form>
	
</div>
<script type="text/javascript">
	function removeCookie(){
		var cookie = new Cookie();
		cookie.deleteCookie("PCookie", "<%=request.getContextPath()%>/");
	}
</script>
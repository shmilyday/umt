<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<fmt:setBundle basename="application" />	
<div class="content-container">
	<div class="toolBar">	
		<fmt:message key="batch.title"/>
	</div>
	
		<form action="batchCreate.do" method="post" enctype="multipart/form-data">
		<input type="hidden" name="act" value="save"/>
		<div class="clearboth red">
					<fmt:message key="batch.message.prompt">
						<fmt:param value="template.xls"/>
					</fmt:message>
			</div>
			<div class="clearboth">
					<fmt:message key="batch.form.file"/>
					<input type="file" name="file" class="" />
				
			</div>
           <div class="clearboth but-center">
					<input type="submit"  class="but  btn-primary" value="<fmt:message key='batch.form.submit'/>" />
					<input type="reset"  class="but" value="<fmt:message key='batch.form.cancel'/>" />
		</div>
		</form>
	
</div>

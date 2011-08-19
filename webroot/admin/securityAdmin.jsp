<div xmlns:ui="http://java.sun.com/jsf/facelets"
xmlns:f="http://java.sun.com/jsf/core">
<ui:composition
	template="/admin/mainStyleTemplate.xhtml">
		<ui:define name="Title">
		<title>Configuration of Users and Roles</title>
	</ui:define>
	<ui:define name="IncludedContent">
		<ui:include src="/admin/securityAdmin/user.jsp" />
		<br></br>
		<br></br>
		<ui:include src="/admin/securityAdmin/role.jsp" />
	</ui:define>
</ui:composition>
</div>

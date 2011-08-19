<div xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:ice="http://www.icesoft.com/icefaces/component">
<ice:dataTable
	binding="#{securityAdminUserBean.dataTable}" id="tableUser"
	value="#{securityAdminUserBean.dataList}" var="dataItem"
	rendered="#{!empty securityAdminUserBean.dataList}">
	<f:facet name="header">
		<ice:outputText value="Config User and associated role|group pairs" />
	</f:facet>
	<ice:column>
		<ice:selectBooleanCheckbox
			value="#{securityAdminUserBean.selectedRow}"
			disabled="#{securityAdminUserBean.editMode}"
			rendered="#{securityAdminUserBean.selectMultiple}" />
	</ice:column>
	<ice:column>
		<f:facet name="header">
			<ice:panelGroup>
				<ice:commandLink
					actionListener="#{securityAdminUserBean.actionSort}"
					disabled="#{securityAdminUserBean.editMode}" title="Sort by ID">
					<f:attribute name="sortField" value="id" />
					<ice:outputText value="ID" />
				</ice:commandLink>
				<ice:outputText value="&#0160;&#9650;" escape="false"
					rendered="#{securityAdminUserBean.sortField == 'id' &amp;&amp; !securityAdminUserBean.sortAscending}" />
				<ice:outputText value="&#0160;&#9660;" escape="false"
					rendered="#{securityAdminUserBean.sortField == 'id' &amp;&amp; securityAdminUserBean.sortAscending}" />
			</ice:panelGroup>
		</f:facet>
		<ice:outputText value="#{dataItem.id}" rendered="#{dataItem.id!=null}" />
		<ice:outputText value="New" rendered="#{dataItem.id==null}"
			styleClass="italic" />
	</ice:column>

	<ice:column>
		<f:facet name="header">
			<ice:panelGroup>
				<ice:commandLink
					actionListener="#{securityAdminUserBean.actionSort}"
					disabled="#{securityAdminUserBean.editMode}"
					title="Sort by user names">
					<f:attribute name="sortField" value="username" />
					<ice:outputText value="User" />
				</ice:commandLink>

			</ice:panelGroup>
		</f:facet>
		<ice:outputText value="#{dataItem.username}"
			rendered="#{!securityAdminUserBean.editModeRow &amp;&amp;(!usersAssociatedRoleGroupEditable)}" />

		<ice:inputText value="#{dataItem.username}"
			rendered="#{securityAdminUserBean.editModeRow}"
			required="#{!empty param['mainform:tableUser:save']}">

		</ice:inputText>
	</ice:column>


	<ice:column>

		<ice:panelGrid columns="2"
			rendered="#{securityAdminUserBean.editModeRow &amp;&amp;(!usersAssociatedRoleGroupEditable)}">
			<f:facet name="header">
				<ice:outputText value="Psw  |  Confirm" />
			</f:facet>
			<ice:inputSecret value="#{dataItem.password}"
				required="#{!empty param['mainform:tableUser:save']}" size="15"
				id="password" redisplay="true" />


			<ice:inputSecret value="#{dataItem.password}"
				required="#{!empty param['mainform:tableUser:save']}" size="15"
				redisplay="true">
				<f:validator validatorId="pswValidator" />
				<f:attribute name="passwordID"
					value="mainform:tableUser:password" />
			</ice:inputSecret>
		</ice:panelGrid>
	</ice:column>

	<ice:column>
		<f:facet name="header">
			<ice:outputText value="Role|Group" />
		</f:facet>
		<ice:outputText value="NONE" rendered="#{empty dataItem.userRoles}" />
		<ice:dataTable value="#{dataItem.userRoles}" var="role"
			id="embedd_table">
			<ice:column>
				<ice:outputText value="#{role.role} | #{role.roleGroup}"
					rendered="#{!securityAdminUserBean.editModeRow}" />
			</ice:column>
		</ice:dataTable>
		<ice:commandButton
			actionListener="#{securityAdminUserBean.fetchAssociatedRoleGroups}"
			value="Config" disabled="#{securityAdminUserBean.editMode}"
			rendered="#{!securityAdminUserBean.editModeRow &amp;&amp; !securityAdminUserBean.editMode}" />
		<ice:commandButton
			actionListener="#{securityAdminUserBean.saveAssociatedUsers}"
			value="Save" rendered="#{securityAdminUserBean.editModeRow}"
			disabled="#{securityAdminUserBean.editMode}" />
		<ice:commandButton action="#{securityAdminUserBean.actionRefresh}"
			value="Cancel " rendered="#{securityAdminUserBean.editModeRow}"
			disabled="#{securityAdminUserBean.editMode}" />
	</ice:column>


	<f:facet name="footer">
		<ice:panelGrid columns="4">
			<ice:panelGroup>

				<ice:commandButton value="Add a User"
					action="#{securityAdminUserBean.actionAdd}"
					disabled="#{securityAdminUserBean.editMode||securityAdminUserBean.usersAssociatedRoleGroupEditable}" />

				<ice:commandButton value="Edit"
					action="#{securityAdminUserBean.actionEdit}"
					disabled="#{securityAdminUserBean.editMode||securityAdminUserBean.usersAssociatedRoleGroupEditable}" />
				<ice:commandButton value="Delete"
					action="#{securityAdminUserBean.actionDelete}"
					disabled="#{securityAdminUserBean.editMode||securityAdminUserBean.usersAssociatedRoleGroupEditable}" />
				<ice:commandButton value="Save"
					action="#{securityAdminUserBean.actionSave}"
					disabled="#{!securityAdminUserBean.editMode||securityAdminUserBean.usersAssociatedRoleGroupEditable}"
					id="save" />
				<ice:commandButton value="Cancel"
					action="#{securityAdminUserBean.actionRefresh}" immediate="true"
					disabled="#{!securityAdminUserBean.editMode}" />


			</ice:panelGroup>
		</ice:panelGrid>
	</f:facet>
</ice:dataTable> 
<ice:panelGroup
	rendered="#{securityAdminUserBean.usersAssociatedRoleGroupEditable}">
	<ice:outputText value="----------Available Role|Group Pairs---------" />
	<ice:selectManyCheckbox id="userListBox1" layout="pageDirection"
		value="#{securityAdminUserBean.associatedRoleGroupRoleNames}">
		<f:selectItems value="#{securityAdminUserBean.allRoles}" />
	</ice:selectManyCheckbox>
</ice:panelGroup> 
</div>
<div xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:ice="http://www.icesoft.com/icefaces/component"><ice:panelGrid
	columns="2">
	<ice:dataTable binding="#{securityAdminRoleBean.dataTable}"
		id="tableRole" value="#{securityAdminRoleBean.dataList}"
		var="dataItem" rendered="#{!empty securityAdminRoleBean.dataList}">
		<f:facet name="header">
			<ice:outputText value="Config Role|Group pair" />
		</f:facet>
		<ice:column>
			<ice:selectBooleanCheckbox
				value="#{securityAdminRoleBean.selectedRow}"
				disabled="#{securityAdminRoleBean.editMode}"
				rendered="#{securityAdminRoleBean.selectMultiple}" />
		</ice:column>
		<ice:column>
			<f:facet name="header">
				<ice:panelGroup>
					<ice:commandLink
						actionListener="#{securityAdminRoleBean.actionSort}"
						disabled="#{securityAdminRoleBean.editMode}" title="Sort by ID">
						<f:attribute name="sortField" value="id" />
						<ice:outputText value="ID" />
					</ice:commandLink>
					<ice:outputText value="&#0160;&#9650;" escape="false"
						rendered="#{securityAdminRoleBean.sortField == 'id' &amp;&amp; !securityAdminRoleBean.sortAscending}" />
					<ice:outputText value="&#0160;&#9660;" escape="false"
						rendered="#{securityAdminRoleBean.sortField == 'id' &amp;&amp; securityAdminRoleBean.sortAscending}" />
				</ice:panelGroup>
			</f:facet>
			<ice:outputText value="#{dataItem.id}"
				rendered="#{dataItem.id!=null}" />
			<ice:outputText value="New" rendered="#{dataItem.id==null}"
				styleClass="italic" />
		</ice:column>

		<ice:column>
			<f:facet name="header">
				<ice:panelGroup>
					<ice:commandLink
						actionListener="#{securityAdminRoleBean.actionSort}"
						disabled="#{securityAdminRoleBean.editMode}">
						<f:attribute name="sortField" value="role" />
						<ice:outputText value="Role" />
					</ice:commandLink>

				</ice:panelGroup>
			</f:facet>
			<ice:outputText value="#{dataItem.role}"
				rendered="#{!securityAdminRoleBean.editModeRow}" />
			<ice:inputText value="#{dataItem.role}"
				rendered="#{securityAdminRoleBean.editModeRow}"
				required="#{!empty param['mainform:tableRole:save']}" size="15"
				id="role">
				<f:validator validatorId="roleGroupNonDuplicatValidator" />
				<f:attribute name="groupID" value="mainform:tableRole:group" />
			</ice:inputText>
		</ice:column>
		<ice:column>
			<f:facet name="header">
				<ice:panelGroup>
					<ice:commandLink
						actionListener="#{securityAdminRoleBean.actionSort}"
						disabled="#{securityAdminRoleBean.editMode}">
						<f:attribute name="sortField" value="role" />
						<ice:outputText value="Group" />
					</ice:commandLink>

				</ice:panelGroup>
			</f:facet>
			<ice:outputText value="#{dataItem.roleGroup}"
				rendered="#{!securityAdminRoleBean.editModeRow}" />
			<ice:inputText value="#{dataItem.roleGroup}"
				rendered="#{securityAdminRoleBean.editModeRow}"
				label="Row ##{securityAdminRoleBean.dataTable.rowIndex + 1} roleGroup"
				required="#{!empty param['mainform:tableRole:save']}" size="15"
				id="group" />
		</ice:column>
		<ice:column>
			<f:facet name="header">
				<ice:outputText value="Associated users"></ice:outputText>
			</f:facet>
			<ice:dataTable value="#{dataItem.users}" var="user"
				styleClass="dataTable" rowClasses="rowOdd,rowEven"
				columnClasses="centered,centered,width25,width25,width25">
				<ice:column>
					<ice:outputText value="#{user.username}"></ice:outputText>
				</ice:column>
			</ice:dataTable>
		</ice:column>

		<f:facet name="footer">
			<ice:panelGrid columns="4">
				<ice:panelGroup>
					<ice:commandButton value="Add"
						action="#{securityAdminRoleBean.actionAdd}"
						disabled="#{securityAdminRoleBean.editMode}" />
					<ice:commandButton value="Edit" id="editButton"
						action="#{securityAdminRoleBean.actionEdit}"
						disabled="#{securityAdminRoleBean.editMode}" />
					<ice:commandButton value="Delete"
						action="#{securityAdminRoleBean.actionDelete}"
						disabled="#{securityAdminRoleBean.editMode}" />
					<ice:commandButton value="Save"
						action="#{securityAdminRoleBean.actionSave}"
						disabled="#{!securityAdminRoleBean.editMode}" id="save" />
					<ice:commandButton value="Cancel"
						action="#{securityAdminRoleBean.actionRefresh}" immediate="true"
						disabled="#{!securityAdminRoleBean.editMode}" />


				</ice:panelGroup>
			</ice:panelGrid>
		</f:facet>
	</ice:dataTable>
</ice:panelGrid> 
</div>
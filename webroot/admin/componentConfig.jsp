<div xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:ice="http://www.icesoft.com/icefaces/component">
<ui:composition
	template="/admin/mainStyleTemplate.xhtml">
	<ui:define name="Title">
		<title>Component Config View</title>
	</ui:define>

	<ui:define name="IncludedContent">
		<!-- Searching component -->
		<ice:panelGrid columns="1" id="searchPart">		
			<f:facet name="header">
			<ice:outputText value="Searching">
			</ice:outputText>
			</f:facet>
			<ice:panelGroup>
				<ice:outputLabel value="Component" />
				<ice:selectOneListbox id="ComponentNameList" value="#{componentConfigBean.keyword_componentName_picker}"
				valueChangeListener="#{componentConfigBean.filterPickerListener}"
				partialSubmit="true">
					<f:selectItems value="#{componentConfigBean.componentNameList}" />
				</ice:selectOneListbox>
			</ice:panelGroup>
			<f:facet name="footer">
			<ice:panelGroup>
			<ice:commandButton id="searchCC" value="Search"
				action="#{componentConfigBean.actionSearch}"
				disabled="#{componentConfigBean.editMode}" />
			<ice:commandButton value="RESET"
				action="#{componentConfigBean.actionRefresh}" 
				rendered="#{componentConfigBean.multiSearchMode}"/>
			</ice:panelGroup>
			
				</f:facet>
		</ice:panelGrid>
	
		
		<!-- Data Table -->
		<ice:dataTable id="componentConfigBean"
			binding="#{componentConfigBean.dataTable}"
			value="#{componentConfigBean.dataList}" var="dataItem">
			<!-- Check box for row selection -->
			<ice:column>
				<f:facet name="header">
					<ice:outputText value="SELECT" />
				</f:facet>
				<ice:selectBooleanCheckbox
					value="#{componentConfigBean.selectedRow}"
					disabled="#{componentConfigBean.editMode}"/>
			</ice:column>

			<!-- ID column-->
			<ice:column>
				<f:facet name="header">
					<ice:panelGroup>
						<ice:commandLink
							actionListener="#{componentConfigBean.actionSort}">
							<f:attribute name="sortField"
								value="#{componentConfigBean.idValue}" />
							<ice:outputText value="ID" />
						</ice:commandLink>
						<!-- desc/asec indicator symbol  -->
						<ice:outputText value="&#0160;&#9650;" escape="false"
							rendered="#{componentConfigBean.sortField == componentConfigBean.idValue &amp;&amp; !componentConfigBean.sortAscending}" />
						<ice:outputText value="&#0160;&#9660;" escape="false"
							rendered="#{componentConfigBean.sortField == componentConfigBean.idValue &amp;&amp; componentConfigBean.sortAscending}" />
					</ice:panelGroup>
				</f:facet>
				<ice:outputText value="#{dataItem.id}" rendered="#{dataItem.id!=0}" />
				<!-- ID of new new entry is auto assigned, a "New" symbol is shown instead of input column -->
				<ice:outputText value="New" rendered="#{dataItem.id==0}"
					styleClass="italic" />
			</ice:column>

			<!-- Component Name column -->
			<ice:column>
				<f:facet name="header">
					<!-- desc/asec indicator symbol  -->
					<ice:panelGroup>
						<ice:commandLink
							actionListener="#{componentConfigBean.actionSort}">
							<f:attribute name="sortField"
								value="#{componentConfigBean.componentName}" />
							<ice:outputText value="Component" />
						</ice:commandLink>
						<!-- desc/asec indicator symbol  -->
						<ice:outputText value="&#0160;&#9650;" escape="false"
							rendered="#{componentConfigBean.sortField == componentConfigBean.componentName &amp;&amp; !componentConfigBean.sortAscending}" />
						<ice:outputText value="&#0160;&#9660;" escape="false"
							rendered="#{componentConfigBean.sortField == componentConfigBean.componentName &amp;&amp; componentConfigBean.sortAscending}" />
					</ice:panelGroup>
				</f:facet>
				<ice:outputText value="#{dataItem.component}"
					rendered="#{!componentConfigBean.editModeRow}" />
				<ice:inputText value="#{dataItem.component}"
					rendered="#{componentConfigBean.editModeRow}" required="true"
					id="ComponentName" />
				<ice:message errorClass="errors" for="ComponentName" />
			</ice:column>

			<!-- Property name  -->
			<ice:column>
				<f:facet name="header">
					<!-- desc/asec indicator symbol  -->
					<ice:panelGroup>
						<ice:commandLink
							actionListener="#{componentConfigBean.actionSort}">
							<f:attribute name="sortField"
								value="#{componentConfigBean.propertyName}" />
							<ice:outputText value="Property" />
						</ice:commandLink>
						<!-- desc/asec indicator symbol  -->
						<ice:outputText value="&#0160;&#9650;" escape="false"
							rendered="#{componentConfigBean.sortField == componentConfigBean.propertyName &amp;&amp; !componentConfigBean.sortAscending}" />
						<ice:outputText value="&#0160;&#9660;" escape="false"
							rendered="#{componentConfigBean.sortField == componentConfigBean.propertyName &amp;&amp; componentConfigBean.sortAscending}" />
					</ice:panelGroup>
				</f:facet>
				<ice:outputText value="#{dataItem.propName}"
					rendered="#{!componentConfigBean.editModeRow}" />
				<ice:inputText value="#{dataItem.propName}"
					rendered="#{componentConfigBean.editModeRow}" required="true"
					id="PropertyName" />
				<ice:message errorClass="errors" for="PropertyName" />
			</ice:column>

			<!-- Property Value -->
			<ice:column>
				<f:facet name="header">
					<!-- desc/asec indicator symbol  -->
					<ice:panelGroup>
						<ice:commandLink
							actionListener="#{componentConfigBean.actionSort}">
							<f:attribute name="sortField"
								value="#{componentConfigBean.propertyValue}" />
							<ice:outputText value="Value" />
						</ice:commandLink>
						<!-- desc/asec indicator symbol  -->
						<ice:outputText value="&#0160;&#9650;" escape="false"
							rendered="#{componentConfigBean.sortField == componentConfigBean.propertyValue &amp;&amp; !componentConfigBean.sortAscending}" />
						<ice:outputText value="&#0160;&#9660;" escape="false"
							rendered="#{componentConfigBean.sortField == componentConfigBean.propertyValue &amp;&amp; componentConfigBean.sortAscending}" />
					</ice:panelGroup>
				</f:facet>
				<ice:outputText value="#{dataItem.value}"
					rendered="#{!componentConfigBean.editModeRow}" />
				<ice:inputText value="#{dataItem.value}"
					rendered="#{componentConfigBean.editModeRow}" required="true"
					id="PropertyValue" />
				<ice:message errorClass="errors" for="PropertyValue" />
			</ice:column>


			<!-- controling buttons -->
			<f:facet name="footer">
				<ice:panelGrid columns="8">
					
					<ice:commandButton value="ADD"
						action="#{componentConfigBean.actionAdd}"
						rendered="#{!componentConfigBean.editMode &amp;&amp; !componentConfigBean.selectAll}" />
					<ice:commandButton value="EDIT"
						action="#{componentConfigBean.actionEdit}"
						rendered="#{!componentConfigBean.editMode &amp;&amp; !componentConfigBean.selectAll}" />
					<ice:commandButton value="SAVE"
						action="#{componentConfigBean.actionSave}"
						rendered="#{componentConfigBean.editMode }" id="saveCC" />
					<ice:commandButton value="DELETE"
						action="#{componentConfigBean.actionDelete}"
						rendered="#{!componentConfigBean.editMode}" />
					<ice:commandButton value="CANCEL"
						action="#{componentConfigBean.actionRefresh}" immediate="true"
						rendered="#{componentConfigBean.editMode }" />					
				</ice:panelGrid>
			</f:facet>
		</ice:dataTable>

		<!-- Paginator with page controls for data table -->
		<ice:dataPaginator id="dataScroll_3" for="componentConfigBean"
			paginator="true" fastStep="3" paginatorMaxPages="4"
			disabled="#{componentConfigBean.selectAll}">
			<f:facet name="first">
				<ice:graphicImage url="/xmlhttp/css/rime/css-images/arrow-first.gif"
					style="border:none;" title="First Page" />
			</f:facet>
			<f:facet name="last">
				<ice:graphicImage url="/xmlhttp/css/rime/css-images/arrow-last.gif"
					style="border:none;" title="Last Page" />
			</f:facet>
			<f:facet name="previous">
				<ice:graphicImage
					url="/xmlhttp/css/rime/css-images/arrow-previous.gif"
					style="border:none;" title="Previous Page" />
			</f:facet>
			<f:facet name="next">
				<ice:graphicImage url="/xmlhttp/css/rime/css-images/arrow-next.gif"
					style="border:none;" title="Next Page" />
			</f:facet>
			<f:facet name="fastforward">
				<ice:graphicImage url="/xmlhttp/css/rime/css-images/arrow-ff.gif"
					style="border:none;" title="Fast Forward" />
			</f:facet>
			<f:facet name="fastrewind">
				<ice:graphicImage url="/xmlhttp/css/rime/css-images/arrow-fr.gif"
					style="border:none;" title="Fast Backwards" />
			</f:facet>
		</ice:dataPaginator>

		<!-- Display counts about the data table and the currently displayed page -->
		<ice:dataPaginator id="dataScroll_2" for="componentConfigBean"
			rowsCountVar="rowsCount" displayedRowsCountVar="displayedRowsCount"
			firstRowIndexVar="firstRowIndex" lastRowIndexVar="lastRowIndex"
			pageCountVar="pageCount" pageIndexVar="pageIndex">
			<ice:outputFormat
				value="{0} records found, displaying {1} record(s), from {2} to {3}. Page {4} / {5}."
				styleClass="standard">
				<f:param value="#{rowsCount}" />
				<f:param value="#{displayedRowsCount}" />
				<f:param value="#{firstRowIndex}" />
				<f:param value="#{lastRowIndex}" />
				<f:param value="#{pageIndex}" />
				<f:param value="#{pageCount}" />
			</ice:outputFormat>
		</ice:dataPaginator>
		
	</ui:define>
	</ui:composition>
</div>
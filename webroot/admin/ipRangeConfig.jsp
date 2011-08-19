<div xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:ice="http://www.icesoft.com/icefaces/component">
	<ui:composition
	template="/admin/mainStyleTemplate.xhtml">
	<ui:define name="Title">
		<title>IP Range Config</title>
	</ui:define>
	<ui:define name="IncludedContent">
		<ice:panelGrid columns="2">
			<ice:selectOneListbox id="filterIpRange" value="all"
				valueChangeListener="#{iPRangeConfigBean.filtering_Listener}"
				partialSubmit="true">
				<f:selectItem itemLabel="Local" itemValue="true" />
				<f:selectItem itemLabel="Remote" itemValue="false"/>
				<f:selectItem itemLabel="ALL" itemValue="all"/>
			</ice:selectOneListbox>
			<ice:commandButton value="Reset"
				action="#{iPRangeConfigBean.actionRefresh}"
				disabled="#{iPRangeConfigBean.editMode}" />
		</ice:panelGrid>
	
		<ice:dataTable binding="#{iPRangeConfigBean.dataTable}"
			value="#{iPRangeConfigBean.dataList}" var="dataItem" id="IPRangeListTable">
			<f:facet name="header">
				<ice:outputText value="IP Range List" />
			</f:facet>
			<ice:column>
				<ice:selectBooleanCheckbox
					value="#{iPRangeConfigBean.selectedRow}"
					disabled="#{iPRangeConfigBean.editModeRow}"
					rendered="#{iPRangeConfigBean.selectMultiple}" />
			</ice:column>
			<ice:column>
				<f:facet name="header">
					<ice:outputText value="ID" />
						<ice:commandSortHeader columnName="#{iPRangeConfigBean.idValue}"
							arrow="true">
							<ice:outputText value="#{iPRangeConfigBean.idValue}"/>
						</ice:commandSortHeader>
				</f:facet>
				<ice:outputText value="#{dataItem.id}" rendered="#{dataItem.id != 0}" />
				<ice:outputText value="new" rendered="#{dataItem.id == 0}" />
			</ice:column>
			<ice:column>
				<f:facet name="header">
				<ice:outputText value="Prefix"></ice:outputText>
				</f:facet>
				<ice:outputText value="#{dataItem.prefix}"
					rendered="#{!iPRangeConfigBean.editModeRow}" />
				<ice:inputText value="#{dataItem.prefix}"
					rendered="#{iPRangeConfigBean.editModeRow}" required="true"
					id="prefix"/>
					<ice:message errorClass="errors"  style="color: red" for="prefix" />
			</ice:column>
	
			<ice:column>
				<f:facet name="header">
					<ice:outputText value="Prefix Length" />
				</f:facet>
				<ice:outputText value="#{dataItem.prefix_len}"
					rendered="#{!iPRangeConfigBean.editModeRow}" />
				<ice:inputText value="#{dataItem.prefix_len}"
					rendered="#{iPRangeConfigBean.editModeRow}" required="true"
					id="prefixLength"/>
					<ice:message errorClass="errors"   style="color: red" for="prefixLength" />
			</ice:column>
			<ice:column>
				<f:facet name="header">
					<ice:outputText value="AS Path Length" />
				</f:facet>
				<ice:outputText value="#{dataItem['ASPathLength']}"
					rendered="#{!iPRangeConfigBean.editModeRow}" />
				<ice:inputText value="#{dataItem['ASPathLength']}"
					rendered="#{iPRangeConfigBean.editModeRow}" id="ASPathLength"
					 />
					 <ice:message errorClass="errors"   style="color: red" for="ASPathLength" />
			</ice:column>
			<ice:column>
				<f:facet name="header">
					<ice:outputText value="MED" />
				</f:facet>
				<ice:outputText value="#{dataItem['MED']}"
					rendered="#{!iPRangeConfigBean.editModeRow}" />
				<ice:inputText value="#{dataItem['MED']}"
					rendered="#{iPRangeConfigBean.editModeRow}" id="MED"/>
					<ice:message errorClass="errors"  style="color: red"  for="MED" />
			</ice:column>
			<ice:column>
				<f:facet name="header">
					<ice:outputText value="Local Preference" />
				</f:facet>
				<ice:outputText value="#{dataItem.localPreference}"
					rendered="#{!iPRangeConfigBean.editModeRow}" />
				<ice:inputText value="#{dataItem.localPreference}"
					rendered="#{iPRangeConfigBean.editModeRow}"
					id="localPreference"/>
					<ice:message errorClass="errors"  style="color: red" for="localPreference" />
			</ice:column>
			
			<ice:column>
				<f:facet name="header">
					<ice:outputText value="IsLocal" />
				</f:facet>
				<ice:outputText value="#{dataItem.local}"
					rendered="#{!iPRangeConfigBean.editModeRow}" />
				<ice:selectBooleanCheckbox value="#{dataItem.local}"
					rendered="#{iPRangeConfigBean.editModeRow}" />	
			</ice:column>
			
		<!-- Control buttons -->
			<f:facet name="footer">
				<ice:panelGrid columns="4">
						<ice:commandButton value="ADD"
							action="#{iPRangeConfigBean.actionAdd}" 
							rendered="#{!iPRangeConfigBean.editMode}"/>
						<ice:commandButton value="EDIT"
							action="#{iPRangeConfigBean.actionEdit}"
							rendered="#{!iPRangeConfigBean.editMode}"/>
						<ice:commandButton value="DELETE"
							action="#{iPRangeConfigBean.actionDelete}"
							rendered="#{!iPRangeConfigBean.editMode}"/>
						<ice:commandButton value="SAVE"
							action="#{iPRangeConfigBean.actionSave}"
							rendered="#{iPRangeConfigBean.editMode}"/>
						<ice:commandButton value="CANCEL"
							action="#{iPRangeConfigBean.actionRefresh}"
							immediate="true"
							rendered="#{iPRangeConfigBean.editMode}" />
				</ice:panelGrid>
			</f:facet>
		</ice:dataTable>
	
		
		<!-- Paginator with page controls for data table -->
			<ice:dataPaginator id="dataScroll_3" for="IPRangeListTable"
				paginator="true" fastStep="3" paginatorMaxPages="4">
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
			<ice:dataPaginator id="dataScroll_2" for="IPRangeListTable"
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
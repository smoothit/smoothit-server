<div xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:ice="http://www.icesoft.com/icefaces/component">
	
<ui:composition
	template="/admin/mainStyleTemplate.xhtml">
	<ui:define name="Title">
		<title>Log Viewer</title>
	</ui:define>
	<ui:define name="IncludedContent">
<!--	searching part-->
	 
<!--		display table part-->
	<ice:dataTable id="tableLog" binding="#{logBean.dataTable}"
		value="#{logBean.dataList}" var="dataItem"
		rendered="#{!empty logBean.dataList}">
		<f:facet name="header">
			<ice:outputText value="Log View" />
		</f:facet>
		<ice:column>
			<f:facet name="header">
				<ice:panelGroup>
					<ice:commandLink actionListener="#{logBean.actionSort}"
						disabled="#{logBean.editMode}" title="Sort on ID">
						<f:attribute name="sortField" value="id" />
						<ice:outputText value="ID" />
					</ice:commandLink>
					<ice:outputText value="&#0160;&#9650;" escape="false"
						rendered="#{logBean.sortField == 'id' &amp;&amp; !logBean.sortAscending}" />
					<ice:outputText value="&#0160;&#9660;" escape="false"
						rendered="#{logBean.sortField == 'id' &amp;&amp; logBean.sortAscending}" />
				</ice:panelGroup>
			</f:facet>
			<ice:outputText value="#{dataItem.id}"
				rendered="#{!empty dataItem.id}" />
			<ice:outputText value="new" rendered="#{empty dataItem.id}"
				styleClass="italic" />
		</ice:column>

		<ice:column>
			<f:facet name="header">
				<ice:panelGroup>
					<ice:commandLink actionListener="#{logBean.actionSort}"
						disabled="#{logBean.editMode}" title="Sort on user name">
						<f:attribute name="sortField" value="username" />
						<ice:outputText value="User" />
					</ice:commandLink>
					<ice:outputText value="&#0160;&#9650;" escape="false"
						rendered="#{logBean.sortField == 'username' &amp;&amp; !logBean.sortAscending}" />
					<ice:outputText value="&#0160;&#9660;" escape="false"
						rendered="#{logBean.sortField == 'username' &amp;&amp; logBean.sortAscending}" />
				</ice:panelGroup>
			</f:facet>
			<ice:outputText value="#{dataItem.username}"
				rendered="#{!logBean.editModeRow}" />
			<ice:inputText id="username" value="#{dataItem.username}"
				rendered="#{logBean.editModeRow}"
				label="Row ##{logBean.dataTable.rowIndex + 1} Name"
				required="#{!empty param['crud:table:save']}" 
				 />
		</ice:column>

		<ice:column>
			<f:facet name="header">
				<ice:panelGroup>
					<ice:commandLink actionListener="#{logBean.actionSort}"
						disabled="#{logBean.editMode}" title="sort on Event">
						<f:attribute name="sortField" value="event" />
						<ice:outputText value="Event" />
					</ice:commandLink>
					<ice:outputText value="&#0160;&#9650;" escape="false"
						rendered="#{logBean.sortField == 'event' &amp;&amp; !logBean.sortAscending}" />
					<ice:outputText value="&#0160;&#9660;" escape="false"
						rendered="#{logBean.sortField == 'event' &amp;&amp; logBean.sortAscending}" />
				</ice:panelGroup>
			</f:facet>
			<ice:outputText value="#{dataItem.event}"
				rendered="#{!logBean.editModeRow}" />
			<ice:inputText value="#{dataItem.event}"
				rendered="#{logBean.editModeRow}"
				label="Row ##{logBean.dataTable.rowIndex + 1} Value"
				required="#{!empty param['crud:table:save']}" />
		</ice:column>
		<ice:column>
			<f:facet name="header">
				<ice:panelGroup>
					<ice:commandLink actionListener="#{logBean.actionSort}"
						disabled="#{logBean.editMode}" title="sort on result">
						<f:attribute name="sortField" value="result" />
						<ice:outputText value="Result" />
					</ice:commandLink>
					<ice:outputText value="&#0160;&#9650;" escape="false"
						rendered="#{logBean.sortField == 'result' &amp;&amp; !logBean.sortAscending}" />
					<ice:outputText value="&#0160;&#9660;" escape="false"
						rendered="#{logBean.sortField == 'result' &amp;&amp; logBean.sortAscending}" />
				</ice:panelGroup>
			</f:facet>
			<ice:outputText value="#{dataItem.result}" />
		</ice:column>

		<ice:column>
			<f:facet name="header">
				<ice:panelGroup>
					<ice:commandLink actionListener="#{logBean.actionSort}"
						title="Sort by Date">
						<f:attribute name="sortField" value="date" />
						<ice:outputText value="Date" />
					</ice:commandLink>
					<ice:outputText value="&#0160;&#9650;" escape="false"
						rendered="#{logBean.sortField == 'date' &amp;&amp; !logBean.sortAscending}" />
					<ice:outputText value="&#0160;&#9660;" escape="false"
						rendered="#{logBean.sortField == 'date' &amp;&amp; logBean.sortAscending}" />
				</ice:panelGroup>
			</f:facet>
			<ice:outputText value="#{dataItem.date}" />
		</ice:column>

</ice:dataTable>

<!-- Paginator with page controls for data table -->
		<ice:dataPaginator id="dataScroll_1" for="tableLog"
			paginator="true" fastStep="3" paginatorMaxPages="4"
			disabled="#{logBean.selectAll}">
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
		<ice:dataPaginator id="dataScroll_0" for="tableLog"
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

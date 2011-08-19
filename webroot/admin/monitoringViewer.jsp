<div xmlns:f="http://java.sun.com/jsf/core"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:ice="http://www.icesoft.com/icefaces/component">
<ui:composition
	template="/admin/mainStyleTemplate.xhtml">
	<ui:define name="Title">
		<title>Monitoring View</title>
	</ui:define>

	<ui:define name="IncludedContent">	
	
	<ice:dataTable id="tableMonitoring"
		binding="#{MonitoringViewBean.dataTable}"
		value="#{MonitoringViewBean.dataList}" var="dataItem">
		<f:facet name="header">
			<ice:outputText value="Monitoring View" />
		</f:facet>
		<ice:column>
			<f:facet name="header">
				<ice:panelGroup>
					<ice:commandLink actionListener="#{MonitoringViewBean.actionSort}"
						title="Sort on swarm.torrentHash">
						<f:attribute name="sortField" value="infohash" />
						<ice:outputText value="infohash" />
					</ice:commandLink>
				</ice:panelGroup>
			</f:facet>
			<ice:outputText value="#{dataItem.infohash}" />
		</ice:column>
	
		<ice:column>
			<f:facet name="header">
				<ice:panelGroup>
					<ice:commandLink actionListener="#{MonitoringViewBean.actionSort}"
						title="Sort on downloadRatePerSwarm">
						<f:attribute name="sortField" value="sum_of_down_rate" />
						<ice:outputText value="Total Download Volume(Kb)" />
					</ice:commandLink>
				</ice:panelGroup>
			</f:facet>
			<ice:outputText value="#{dataItem.sum_of_down_rate}" />
		</ice:column>
		
		<ice:column>
			<f:facet name="header">
				<ice:panelGroup>
					<ice:commandLink actionListener="#{MonitoringViewBean.actionSort}"
						title="Sort on downloadRatePerSwarm">
						<f:attribute name="sortField" value="sum_of_up_rate" />
						<ice:outputText value="Total UpLoad Volume(Kb)" />
					</ice:commandLink>
				</ice:panelGroup>
			</f:facet>
			<ice:outputText value="#{dataItem.sum_of_up_rate}" />
		</ice:column>
		<ice:column>
			<f:facet name="header">
				<ice:panelGroup>
					<ice:commandLink actionListener="#{MonitoringViewBean.actionSort}"
						title="Sort on NrOfAssociatedLocalPeers">
						<f:attribute name="sortField" value="number_of_local_peers" />
						<ice:outputText value="No. Of Peers" />
					</ice:commandLink>
				</ice:panelGroup>
			</f:facet>
			<ice:outputText value="#{dataItem.number_of_local_peers}" />
		</ice:column>
	
		<ice:column>
			<f:facet name="header">
				<ice:panelGroup>
					<ice:commandLink actionListener="#{MonitoringViewBean.actionSort}"
						title="Sort on NrOfLeecher">
						<f:attribute name="sortField" value="noOfAssociatedIop" />
						<ice:outputText value="No. Of Leechers" />
					</ice:commandLink>
				</ice:panelGroup>
			</f:facet>
			<ice:outputText value="#{dataItem.number_of_leechers}" />
		</ice:column>
		
		<ice:column>
			<f:facet name="header">
				<ice:panelGroup>
					<ice:commandLink actionListener="#{MonitoringViewBean.actionSort}"
						title="Sort on NrOfSeeders">
						<f:attribute name="sortField" value="noOfSeeders" />
						<ice:outputText value="No. Of Seeders" />
					</ice:commandLink>
				</ice:panelGroup>
			</f:facet>
			<ice:outputText value="#{dataItem.number_of_seeder}" />
		</ice:column>
		
		<ice:column>
			<f:facet name="header">
				<ice:panelGroup>
					<ice:commandLink actionListener="#{MonitoringViewBean.actionSort}"
						title="Sort on NrOfIoPs">
						<f:attribute name="sortField" value="noOfIoPs" />
						<ice:outputText value="No. Of IoPs" />
					</ice:commandLink>
				</ice:panelGroup>
			</f:facet>
			<ice:outputText value="#{dataItem.number_of_IoPs}" />
		</ice:column>
		
				<ice:column>
			<f:facet name="header">
				<ice:panelGroup>
						<ice:outputText value="Details" />
				</ice:panelGroup>
			</f:facet>
			<ice:commandLink action="#{MonitoringViewBean.showDetails}" >
				<ice:outputText value="Show/Refresh"/>
			</ice:commandLink>
		</ice:column>
	</ice:dataTable>
<!--	Select time offset-->
	<ice:panelGroup>
		<ice:outputLabel value="Within"/>
		<ice:inputText value="#{MonitoringViewBean.timeoffset}" />
		<ice:outputLabel value=" Minutes"/>
		<ice:commandButton value="Save"
		 partialSubmit="true" action="#{MonitoringViewBean.saveTimeoffset }"/>
	</ice:panelGroup>
<!--	Details for each peer-->
	<ice:panelGroup rendered="#{MonitoringViewBean.showDetailsTag}">
	<ice:outputLabel value="Details for Torrent with infoHash= #{MonitoringViewBean.infoHash_selectedSwarm}"/>
	<ice:dataTable value="#{MonitoringViewBean.localPeers_list}" var="dataItem">
		<f:facet name="header">
			<ice:panelGroup>
					<ice:outputText value="Associated Local Peers" />
			</ice:panelGroup>
		</f:facet>
		<ice:column>
			<f:facet name="header">
			<ice:panelGroup>
					<ice:outputText value="IP Address" />
			</ice:panelGroup>
			</f:facet>
			<ice:outputText value="#{dataItem.ipAddress}"/>
		</ice:column>
		<ice:column>
			<f:facet name="header">
			<ice:panelGroup>
					<ice:outputText value="Port" />
			</ice:panelGroup>
			</f:facet>
			<ice:outputText value="#{dataItem.port}"/>
		</ice:column>
		<ice:column>
			<f:facet name="header">
			<ice:panelGroup>
					<ice:outputText value="Progress(%)" />
			</ice:panelGroup>
			</f:facet>
			<ice:outputText value="#{dataItem.progress}"/>
		</ice:column>
		<ice:column>
			<f:facet name="header">
			<ice:panelGroup>
					<ice:outputText value="Down Volume(Kb)" />
			</ice:panelGroup>
			</f:facet>
			<ice:outputText value="#{dataItem.down_rate}"/>
		</ice:column>
		<ice:column>
			<f:facet name="header">
			<ice:panelGroup>
					<ice:outputText value="Up Volume(Kb)" />
			</ice:panelGroup>
			</f:facet>
			<ice:outputText value="#{dataItem.up_rate}"/>
		</ice:column>
	</ice:dataTable>
<!--	Details for each associated IoP-->
	<ice:dataTable value="#{MonitoringViewBean.ioP_list}" var="dataItem">
		<f:facet name="header">
			<ice:panelGroup>
					<ice:outputText value="Associated IoPs" />
			</ice:panelGroup>
		</f:facet>
		<ice:column>
			<f:facet name="header">
			<ice:panelGroup>
					<ice:outputText value="IP Address" />
			</ice:panelGroup>
			</f:facet>
			<ice:outputText value="#{dataItem.ipAddress}"/>
		</ice:column>
		<ice:column>
			<f:facet name="header">
			<ice:panelGroup>
					<ice:outputText value="Port" />
			</ice:panelGroup>
			</f:facet>
			<ice:outputText value="#{dataItem.port}"/>
		</ice:column>
		<ice:column>
			<f:facet name="header">
			<ice:panelGroup>
					<ice:outputText value="Progress(%)" />
			</ice:panelGroup>
			</f:facet>
			<ice:outputText value="#{dataItem.progress}"/>
		</ice:column>
		<ice:column>
			<f:facet name="header">
			<ice:panelGroup>
					<ice:outputText value="Down Volume(Kb)" />
			</ice:panelGroup>
			</f:facet>
			<ice:outputText value="#{dataItem.down_rate}"/>
		</ice:column>
		<ice:column>
			<f:facet name="header">
			<ice:panelGroup>
					<ice:outputText value="Up Volume(Kb)" />
			</ice:panelGroup>
			</f:facet>
			<ice:outputText value="#{dataItem.up_rate}"/>
		</ice:column>
	</ice:dataTable>
	</ice:panelGroup>
	
	</ui:define>
	</ui:composition>
</div>

<div xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:ice="http://www.icesoft.com/icefaces/component">
	<ui:composition
	template="/admin/mainStyleTemplate.xhtml">
	<ui:define name="Title">
		<title>IoP and Controller Configuration</title>
	</ui:define>

	<ui:define name="IncludedContent">
	<ice:outputText value="IoP" style="font-style: italic"/>
		<ice:panelGroup>
			<ice:outputLabel value="Operation Mode" />
			<ice:selectOneRadio value="#{ioPConfigBean.operationMode}" 
			id="operationMode">
				<f:selectItems value="#{ioPConfigBean.operationModeOptions}" />
			</ice:selectOneRadio>
		</ice:panelGroup>
		
		<ice:panelGroup>
			<ice:outputLabel value="Connect to remote" />			
			<ice:selectOneRadio value="#{ioPConfigBean.remoteConnectionFlag}"
			 id="RemoteConnectionFlag">
				<f:selectItems value="#{ioPConfigBean.remoteConnectionOptions}" />
			</ice:selectOneRadio>
		</ice:panelGroup>
		
		<ice:panelGroup>
			<ice:outputLabel value="Number of unchoking slots per swarm" />
			<ice:inputText value="#{ioPConfigBean.numberOfUnchokingSlots}" id="NumberOfUnchokingSlots">
				<f:validateLongRange minimum="0" />
			</ice:inputText>
			<ice:message errorClass="errors"  for="NumberOfUnchokingSlots" />
		</ice:panelGroup>

		<ice:panelGroup>
			<ice:outputLabel
				value="time period to ask the SIS for new torrent statistics" />
			<ice:inputText value="#{ioPConfigBean.timePeriodforStatistics }" id="TimePeriod">
			<f:validateDoubleRange minimum = "0" />
			</ice:inputText>
			<ice:outputLabel value="seconds" />
			<ice:message errorClass="errors"  for="TimePeriod" />
		</ice:panelGroup>
		
		<ice:panelGroup>
			<ice:outputLabel value="Lower bound for upload bandwidth" />
			<ice:inputText value="#{ioPConfigBean.lowerBoundsForUploadBandwidth}" id="LowerBoundsForUploadBandWidth">
				<f:validateDoubleRange minimum = "0" />
			</ice:inputText>
			<ice:outputLabel value="Kbps(KiloByte per Second)" />
			<ice:message errorClass="errors"  for="LowerBoundsForUploadBandWidth" />
		</ice:panelGroup>

		<ice:panelGroup>
			<ice:outputLabel value="Lower bound for download bandwidth" />
			<ice:inputText
				value="#{ioPConfigBean.lowerBoundsForDownloadBandwidth}" id="LowerBoundsForDownloadBandWidth">
				<f:validateDoubleRange minimum = "0" />
			</ice:inputText>
			<ice:outputLabel value="Mbps(megabits per second)" />
			<ice:message errorClass="errors"  for="LowerBoundsForDownloadBandWidth" />
		</ice:panelGroup>

		<ice:panelGroup>
			<ice:outputLabel value="Total upload bandwidth" />
			<ice:inputText value="#{ioPConfigBean.totalUploadBandwidth}" id="TotalUploadBandwidth">
				<f:validateDoubleRange minimum = "0" />
			</ice:inputText>
			<ice:outputLabel value="Mbps(megabits per second)" />
			<ice:message errorClass="errors"  for="TotalUploadBandwidth" />
		</ice:panelGroup>

		<ice:panelGroup>
			<ice:outputLabel value="Total download bandwidth" />
			<ice:inputText value="#{ioPConfigBean.totalDownloadBandwidth}" id="TotalDownloadBandwidth">
				<f:validateDoubleRange minimum = "0" />
			</ice:inputText>
			<ice:outputLabel value="Mbps(megabits per second)" />
			<ice:message errorClass="errors"  for="TotalDownloadBandwidth" />
		</ice:panelGroup>

		<ice:panelGroup>
			<ice:outputLabel value="The percentage of new swarms the IoP will join" />
			<ice:inputText value="#{ioPConfigBean.thePercentageValue}" id="thePercentageValue">
				<f:validateDoubleRange minimum = "0" maximum="100" />
			</ice:inputText>
			<ice:outputLabel value="%" />
			<ice:message errorClass="errors"  for="thePercentageValue" />
		</ice:panelGroup>
		<br/>
		<br/>
		<ice:outputText value="Controller" style="font-style: italic"/>
				<ice:panelGroup>
			<ice:outputLabel
				value="Time period to remove outdated statistics" />
			<ice:inputText value="#{ioPConfigBean.timePeriodforOut }" id="TimeOut">
			<f:validateDoubleRange minimum = "0" />
			</ice:inputText>
			<ice:outputLabel value="seconds" />
			<ice:message errorClass="errors"  for="TimeOut" />
		</ice:panelGroup>
		<ice:panelGroup>
			<ice:outputLabel
				value="Time threshold for outdated statistics" />
			<ice:inputText value="#{ioPConfigBean.timePeriodforAge}" id="TimeAge">
			<f:validateDoubleRange minimum = "0" />
			</ice:inputText>
			<ice:outputLabel value="seconds" />
			<ice:message errorClass="errors"  for="TimeAge" />
		</ice:panelGroup>
		

		<ice:panelGroup>
			<ice:commandButton value="SAVE" action="#{ioPConfigBean.saveParameters}"/>
			<ice:commandButton value="CANCEL"
				action="#{ioPConfigBean.cancelChange}"
				immediate="true" />
		</ice:panelGroup>
	</ui:define>
	</ui:composition>
</div>
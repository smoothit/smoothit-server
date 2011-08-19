<div xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:ice="http://www.icesoft.com/icefaces/component">
	<ui:composition
	template="/admin/mainStyleTemplate.xhtml">
	<ui:define name="Title">
		<title>HAP Configuration</title>
	</ui:define>

	<ui:define name="IncludedContent">
		
		<ice:panelGroup>
			<ice:outputLabel value=" HAP Controller" />			
			<ice:selectOneRadio value="#{hapConfigBean.controllerSwitcher}"
			 id="ControllerSwitcher">
				<f:selectItems value="#{hapConfigBean.controllerSwitcherMode}" />
			</ice:selectOneRadio>
		</ice:panelGroup>
		
		<ice:panelGroup>
			<ice:outputLabel value="The URL( WS interface) of the Billing system" />
			<ice:inputText value="#{hapConfigBean.billURL}" id="BillingURL">
			</ice:inputText>
			<ice:message errorClass="errors"  for="BillingURL" />
		</ice:panelGroup>

		<ice:panelGroup>
			<ice:outputLabel
				value="Time period to calculate the HAP ratings by the Controller " />
			<ice:inputText value="#{hapConfigBean.timeToUpdate}" id="timeToUpdate">
			<f:validateDoubleRange minimum = "0" />
			</ice:inputText>
			<ice:outputLabel value="Seconds[s]" />
			<ice:message errorClass="errors"  for="timeToUpdate" />
		</ice:panelGroup>
		
		<ice:panelGroup>
			<ice:outputLabel value="The point in time when the Controller shall start the calculation" />
			<ice:inputText value="#{hapConfigBean.timeToStartCalculation}" id="timeToStartCalculation">
				<f:validateLongRange minimum = "0" />
			</ice:inputText>
			<ice:outputLabel value="Minutes" />
			<ice:message errorClass="errors"  for="timeToStartCalculation" />
		</ice:panelGroup>

		<ice:panelGroup>
			<ice:outputLabel value="The number of HAPs" />
			<ice:inputText
				value="#{hapConfigBean.numberOfHAPs}" id="numberOfHAPs">
				<f:validateLongRange minimum = "0" />
			</ice:inputText>
			<ice:message errorClass="errors"  for="numberOfHAPs" />
		</ice:panelGroup>

		<ice:panelGroup>
			<ice:outputLabel value="HAP rating calculation parameter P1" />
			<ice:inputText value="#{hapConfigBean.ratingCalculationParameter_P1}" id="ratingCalculationParameter_P1">
			<f:validateDoubleRange minimum = "0" />
			</ice:inputText>
			<ice:message errorClass="errors"  for="ratingCalculationParameter_P1" />
		</ice:panelGroup>
		
		<ice:panelGroup>
			<ice:outputLabel value="HAP rating calculation parameter P2" />
			<ice:inputText value="#{hapConfigBean.ratingCalculationParameter_P2}" id="ratingCalculationParameter_P2">
			<f:validateDoubleRange minimum = "0" />
			</ice:inputText>
			<ice:message errorClass="errors"  for="ratingCalculationParameter_P2" />
		</ice:panelGroup>
		
		<ice:panelGroup>
			<ice:outputLabel value="HAP rating calculation parameter P3" />
			<ice:inputText value="#{hapConfigBean.ratingCalculationParameter_P3}" id="ratingCalculationParameter_P3">
			<f:validateDoubleRange minimum = "0" />
			</ice:inputText>
			<ice:message errorClass="errors"  for="ratingCalculationParameter_P3" />
		</ice:panelGroup>

		<ice:panelGroup>
			<ice:outputLabel value="The available download bandwidth" />
			<ice:inputText value="#{hapConfigBean.availableDownloadBandwidth}" id="availableDownloadBandwidth">
				<f:validateLongRange minimum = "0" />
			</ice:inputText>
			<ice:outputLabel value="Kilobits/second [Kbps]" />
			<ice:message errorClass="errors"  for="availableDownloadBandwidth" />
		</ice:panelGroup>
		
		<ice:panelGroup>
			<ice:outputLabel value="The available upload bandwidth" />
			<ice:inputText value="#{hapConfigBean.availableUploadBandwidth}" id="availableUploadBandwidth">
				<f:validateLongRange minimum = "0" />
			</ice:inputText>
			<ice:outputLabel value="Kilobits/second [Kbps]" />
			<ice:message errorClass="errors"  for="availableUploadBandwidth" />
		</ice:panelGroup>

		<ice:panelGroup>
			<ice:outputLabel value="The increase in download bandwidth" />
			<ice:inputText value="#{hapConfigBean.increaseDownloaddBandwidth}" id="increaseDownloaddBandwidth">
				<f:validateLongRange minimum = "0" />
			</ice:inputText>
			<ice:outputLabel value="Kilobits/second [Kbps]" />
			<ice:message errorClass="errors"  for="increaseDownloaddBandwidth" />
		</ice:panelGroup>

		<ice:panelGroup>
			<ice:outputLabel value="The increase in upload bandwidth" />
			<ice:inputText value="#{hapConfigBean.increaseUploadBandwidth}" id="increaseUploadBandwidth">
				<f:validateLongRange minimum = "0" />
			</ice:inputText>
			<ice:outputLabel value="Kilobits/second [Kbps]" />
			<ice:message errorClass="errors"  for="increaseUploadBandwidth" />
		</ice:panelGroup>

		<ice:panelGroup>
			<ice:outputLabel value="The HAP rating threshold" />
			<ice:inputText value="#{hapConfigBean.ratingThreshold}" id="ratingThreshold">
				<f:validateDoubleRange minimum = "0" />
			</ice:inputText>
			<ice:message errorClass="errors"  for="ratingThreshold" />
		</ice:panelGroup>				


		<ice:panelGroup>
			<ice:commandButton value="SAVE" action="#{hapConfigBean.saveChange}"/>
			<ice:commandButton value="CANCEL"
				action="#{hapConfigBean.cancelChange}"
				immediate="true" />
		</ice:panelGroup>
	</ui:define>
	</ui:composition>
</div>
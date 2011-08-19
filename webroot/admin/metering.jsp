<div xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:ice="http://www.icesoft.com/icefaces/component">
	<ui:composition
	template="/admin/mainStyleTemplate.xhtml">
	<ui:define name="Title">
			<title>BGP/Metering Config</title>
	</ui:define>
		
	<ui:define name="IncludedContent">	
			<!--source selections and router, file config  -->
	<ice:panelGrid columns="1">
			<!-- refresh rate -->
		<ice:panelGroup>
			<ice:panelGrid columns="1">
				<ice:panelGroup>
					<ice:outputText value="Refresh Rate"/>
					<ice:inputText value="#{MeteringConfigBean.refreshRate}"
					id="Refresh_rate">
						<f:validateDoubleRange minimum="0.00" />
					</ice:inputText>
					<ice:outputText value="Second"/>
				</ice:panelGroup>
				<f:facet name="footer">
					<ice:panelGroup>
						<ice:commandButton value="SAVE" 
						action="#{MeteringConfigBean.saveRefreshRate}"/>
						<ice:commandButton value="CANCEL"  
							 action="#{MeteringConfigBean.cancelChange}"			
							immediate="true" />
					</ice:panelGroup>
				</f:facet>
			</ice:panelGrid>			
		</ice:panelGroup>
	<!-- source options -->
		<ice:panelGroup>
		<ice:panelGrid columns="2">
			<f:facet name="header">
			<ice:panelGroup>
			<ice:outputText value="Source selection for option remote/local"/>
			</ice:panelGroup>
			</f:facet>
			<ice:panelGroup>
				<ice:selectOneListbox id="SlctOption"
					value="#{MeteringConfigBean.selectIpRangeOption}" partialSubmit="true"
					style="overflow: auto;height:75px"
					valueChangeListener="#{MeteringConfigBean.ipRangeOptionChangeListener}">
					<f:selectItems id="SlctOptionItms"
						value="#{MeteringConfigBean.ipRangeOptions}" />
				</ice:selectOneListbox>
			</ice:panelGroup>
			<ice:panelGroup>
			<!--set true, refresh and reset before make a new selection  -->
				<ice:selectOneListbox id="SourcesListBox" partialSubmit="true"
					value="#{MeteringConfigBean.selectIpRangeSource}"
					valueChangeListener="#{MeteringConfigBean.ipRangeSourceChangeListener}"
					style="overflow: auto;width:110px;height:75px">
					<f:selectItems id="SlctCtyItms"
						value="#{MeteringConfigBean.ipRangeSources}" />
				</ice:selectOneListbox>
			</ice:panelGroup>
			<f:facet name="footer">
				<ice:panelGroup>
				<ice:commandButton value="Save"
					action="#{MeteringConfigBean.saveIpRangOptionWithIpRangeSource}"
					 />
				<ice:commandButton value="Cancel"
					action="#{MeteringConfigBean.cancelIpRangOptionWithIpRangeSource}"
					immediate="false" />
				</ice:panelGroup>
			</f:facet>
		</ice:panelGrid>
		</ice:panelGroup>
	
	<ice:panelGroup>
	<ice:outputText value="Config for Option: #{MeteringConfigBean.selectIpRangeOption}" />
	<ice:outputText value="Source:#{MeteringConfigBean.selectIpRangeSource}" />
	</ice:panelGroup>
		<!-- Router config-->
		<ice:panelGroup>
		<ice:panelGrid columns="1"  rendered="#{MeteringConfigBean.routerFlag}">
			<ice:panelGroup>
				<ice:outputLabel value="Address" />
				<ice:inputText value="#{MeteringConfigBean.routerAddress}">
				</ice:inputText>
			</ice:panelGroup>
			<ice:panelGroup>
				<ice:outputLabel value="Port" />
				<ice:inputText value="#{MeteringConfigBean.routerPort}"
					id="Port_Remote">
					<f:validateLongRange minimum="0" maximum="255"></f:validateLongRange>
				</ice:inputText>
			</ice:panelGroup>
			<ice:panelGroup>
				<ice:outputLabel value="SNMP" />
				<ice:inputText value="#{MeteringConfigBean.routerSNMP}"/>						
			</ice:panelGroup>
			</ice:panelGrid>
		</ice:panelGroup>
			
		<!-- File Name -->
		<ice:panelGroup>
			<ice:panelGrid columns="1" rendered="#{MeteringConfigBean.fileFlag}">
				<ice:panelGroup>
					<ice:inputText value="#{MeteringConfigBean.fileName}"/>
				</ice:panelGroup>
			</ice:panelGrid>
		</ice:panelGroup>
				<!-- DB -->
		<ice:panelGroup>
			<ice:panelGrid columns="1" rendered="#{MeteringConfigBean.dbFlag}">
				<ice:commandLink target="popupWindow" action="navigateToIPRangeConfig" value="open popup"/>
			</ice:panelGrid>
		</ice:panelGroup>
		<f:facet name="footer">	
			<ice:panelGroup>
				<ice:commandButton value="SAVE" 
				rendered="#{!MeteringConfigBean.dbFlag}" action="#{MeteringConfigBean.saveParameters}"/>
				<ice:commandButton value="CANCEL"  
					rendered="#{!MeteringConfigBean.dbFlag}" action="#{MeteringConfigBean.cancelChange}"			
					immediate="true" />
			</ice:panelGroup>
		</f:facet>
	</ice:panelGrid>
   </ui:define>
   </ui:composition>
</div>


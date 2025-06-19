<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="assistanceAgent.TrackingLog.list.label.step" path="step" width="25%"/>
	<acme:list-column code="assistanceAgent.TrackingLog.list.label.resolutionPercentage" path="resolutionPercentage" width="25%"/>
	<acme:list-column code="assistanceAgent.TrackingLog.list.label.claim" path="claim" width="25%"/>
	<acme:list-column code="assistanceAgent.TrackingLog.list.label.accepted" path="accepted" width="25%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<acme:button code="assistanceAgent.TrackingLog.button.create" action="/assistance-agent/tracking-log/create?masterId=${masterId}"/>

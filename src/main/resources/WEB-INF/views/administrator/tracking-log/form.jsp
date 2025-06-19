<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-moment code="assistanceAgent.trackingLog.form.label.createdMoment" path="createdMoment" readonly="true"/>
	<acme:input-moment code="assistanceAgent.trackingLog.form.label.lastUpdateMoment" path="lastUpdateMoment" readonly="true"/>
	<acme:input-textarea code="assistanceAgent.trackingLog.form.label.step" path="step" readonly="true"/>
	<acme:input-double code="assistanceAgent.trackingLog.form.label.resolutionPercentage" path="resolutionPercentage" readonly="true"/>
	<acme:input-textbox code="assistanceAgent.trackingLog.form.label.accepted" path="accepted" readonly="true"/>
	<acme:input-textbox code="assistanceAgent.trackingLog.form.label.resolution" path="resolution" readonly="true"/>
	<acme:input-textbox code="assistanceAgent.trackingLog.form.label.claim" path="claim" readonly="true"/>
	<jstl:if test="${resolutionPercentage == 100.00}">
		<acme:input-checkbox code="assistanceAgent.trackingLog.form.label.secondTrackingLog" path="secondTrackingLog" readonly="true"/>
	</jstl:if>
</acme:form>
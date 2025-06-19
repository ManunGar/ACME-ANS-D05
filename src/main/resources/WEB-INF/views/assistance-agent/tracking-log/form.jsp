<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<jstl:if test="${_command != 'create' }">
		<acme:input-moment code="assistanceAgent.trackingLog.form.label.createdMoment" path="createdMoment" readonly="true"/>
		<acme:input-moment code="assistanceAgent.trackingLog.form.label.lastUpdateMoment" path="lastUpdateMoment" readonly="true"/>
	</jstl:if> 
	<acme:input-textarea code="assistanceAgent.trackingLog.form.label.step" path="step"/>
	<acme:input-double code="assistanceAgent.trackingLog.form.label.resolutionPercentage" path="resolutionPercentage"/>
	<acme:input-select code="assistanceAgent.trackingLog.form.label.accepted" path="accepted" choices="${status}"/>
	<acme:input-textbox code="assistanceAgent.trackingLog.form.label.resolution" path="resolution"/>
	<acme:input-textbox code="assistanceAgent.trackingLog.form.label.claim" path="claim" readonly="${true}"/>
	<jstl:if test="${_command == 'create' || resolutionPercentage == 100.00}">
		<acme:input-checkbox code="assistanceAgent.trackingLog.form.label.secondTrackingLog" path="secondTrackingLog" readonly="${secondTrackingLogReadOnly}"/>
	</jstl:if>
	
	<jstl:choose>	 
		<jstl:when test="${_command != 'create' && draftMode == true}">
			<acme:submit code="assistanceAgent.trackingLog.form.button.update" action="/assistance-agent/tracking-log/update"/>
			<jstl:if test="${claimDraftMode == false}">
				<acme:submit code="assistanceAgent.trackingLog.form.button.publish" action="/assistance-agent/tracking-log/publish"/>
			</jstl:if>
			<acme:submit code="assistanceAgent.trackingLog.form.button.delete" action="/assistance-agent/tracking-log/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="assistanceAgent.trackingLog.form.button.create" action="/assistance-agent/tracking-log/create?masterId=${masterId}"/>
		</jstl:when>
	</jstl:choose>
</acme:form>
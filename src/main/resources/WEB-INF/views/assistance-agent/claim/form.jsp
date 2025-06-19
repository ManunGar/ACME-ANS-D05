<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<jstl:if test="${_command != 'create' }">
		<acme:input-moment code="assistanceAgent.Claim.form.label.registrationMoment" path="registrationMoment" readonly="true"/>
	</jstl:if>
	<acme:input-email code="assistanceAgent.Claim.form.label.passengerEmail" path="passengerEmail"/>	
	<acme:input-textarea code="assistanceAgent.Claim.form.label.description" path="description"/>
	<acme:input-select code="assistanceAgent.Claim.form.label.claimTypes" path="claimType" choices="${claimTypes}"/>
	<acme:input-textbox code="assistanceAgent.Claim.form.label.accepted" path="accepted" readonly="true"/>
	<jstl:if test="${undergoing == true || undergoing == null}">
		<acme:input-select code="assistanceAgent.Claim.form.label.leg" path="leg" choices="${legs}"/>
		<jstl:if test="${_command != 'create' }">
			<acme:input-moment code="assistanceAgent.Claim.form.leg.label.departure" path="departure" readonly="true"/>
			<acme:input-moment code="assistanceAgent.Claim.form.leg.label.arrival" path="arrival" readonly="true"/>
			<acme:input-textbox code="assistanceAgent.Claim.form.leg.label.status" path="status" readonly="true"/>
		</jstl:if>
	</jstl:if>
	
	<jstl:if test="${_command != 'create' }">
		<acme:button code="assistanceAgent.Claim.form.button.trackingLogs" action="/assistance-agent/tracking-log/listclaim?masterId=${id}"/>
	</jstl:if>
	<jstl:choose>	 
		<jstl:when test="${_command != 'create' && draftMode == true}">
			<acme:submit code="assistanceAgent.Claim.form.button.update" action="/assistance-agent/claim/update"/>
			<acme:submit code="assistanceAgent.Claim.form.button.publish" action="/assistance-agent/claim/publish"/>
			<acme:submit code="assistanceAgent.Claim.form.button.delete" action="/assistance-agent/claim/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="assistanceAgent.Claim.form.button.create" action="/assistance-agent/claim/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-moment code="assistanceAgent.Claim.form.label.registrationMoment" path="registrationMoment" readonly="true"/>
	<acme:input-email code="assistanceAgent.Claim.form.label.passengerEmail" path="passengerEmail" readonly="true"/>	
	<acme:input-textarea code="assistanceAgent.Claim.form.label.description" path="description" readonly="true"/>
	<acme:input-textbox code="assistanceAgent.Claim.form.label.claimTypes" path="claimType" readonly="true"/>
	<acme:input-textbox code="assistanceAgent.Claim.form.label.accepted" path="accepted" readonly="true"/>
	<jstl:if test="${undergoing == true || undergoing == null}">
		<acme:input-textbox code="assistanceAgent.Claim.form.label.leg" path="leg" readonly="true"/>
		<acme:input-moment code="assistanceAgent.Claim.form.leg.label.departure" path="departure" readonly="true"/>
		<acme:input-moment code="assistanceAgent.Claim.form.leg.label.arrival" path="arrival" readonly="true"/>
		<acme:input-textbox code="assistanceAgent.Claim.form.leg.label.status" path="status" readonly="true"/>
	</jstl:if>
	
	<acme:button code="assistanceAgent.Claim.form.button.trackingLogs" action="/administrator/tracking-log/listclaim?masterId=${id}"/>
</acme:form>
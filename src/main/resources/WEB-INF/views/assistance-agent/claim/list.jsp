<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="assistanceAgent.claim.list.label.registrationMoment" path="registrationMoment" width="25%"/>
	<acme:list-column code="assistanceAgent.claim.list.label.passengerEmail" path="passengerEmail" width="25%"/>
	<acme:list-column code="assistanceAgent.claim.list.label.claimTypes" path="claimType" width="25%"/>
	<acme:list-column code="assistanceAgent.claim.list.label.accepted" path="accepted" width="25%"/>
	<acme:list-payload path="payload"/>
</acme:list>
<jstl:if test="${_command != 'listResolved'}">
	<acme:button code="assistanceAgent.claim.button.create" action="/assistance-agent/claim/create"/>
</jstl:if>

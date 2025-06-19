<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
		<acme:input-select code="manager.legs.list.label.flight" path="flight" choices="${flights}" readonly="true"/>
		<acme:input-textarea code="manager.legs.list.label.flightNumber" path="flightNumber" readonly="true"/>
		<acme:input-moment code="manager.legs.list.label.departure" path="departure" />
		<acme:input-moment code="manager.legs.list.label.arrival" path="arrival"/>
		<acme:input-select code="manager.legs.list.label.status" path="status" choices="${status}"/>
		<acme:input-select code="manager.legs.list.label.departure-airport" path="departureAirport" choices="${departureAirports}"/>
		<acme:input-select code="manager.legs.list.label.arrival-airport" path="arrivalAirport" choices="${arrivalAirports}"/>
		<acme:input-select code="manager.legs.list.label.aircraft" path="aircraft" choices="${aircrafts}"/>
		<jstl:choose>
		<jstl:when test="${(_command == 'update' || _command == 'show' || _command == 'publish' || _command == 'delete') && draftMode == true}">
				<acme:input-checkbox code="manager.legs.form.label.confirmation.update" path="confirmation"/>
				<acme:submit code="manager.legs.form.button.update" action="/airline-manager/legs/update"/>
				<acme:submit code="manager.legs.form.button.publish" action="/airline-manager/legs/publish"/>
				<acme:submit code="manager.legs.form.button.delete" action="/airline-manager/legs/delete"/>
				
			</jstl:when>
			<jstl:when test="${_command == 'create'}">
				<acme:input-checkbox code="manager.legs.form.label.confirmation.create" path="confirmation"/>	
				<acme:submit code="manager.legs.form.button.create" action="/airline-manager/legs/create?flightId=${flightId}"/>
			</jstl:when>		
	</jstl:choose>
</acme:form>
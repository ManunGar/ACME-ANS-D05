<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
		<acme:input-textarea code="manager.flight.list.label.description" path="description" />
		<acme:input-textarea code="manager.flight.list.label.highlights" path="highlights"/>
		<acme:input-checkbox code="manager.flight.list.label.self-transfer" path="selfTransfer"/>
		<acme:input-textarea code="manager.flight.list.label.cost" path="cost"/>
		<acme:input-textarea code="manager.flight.list.label.origin" path="origin" readonly="true"/>
		<acme:input-textarea code="manager.flight.list.label.destination" path="destination" readonly="true"/>
		<acme:input-textarea code="manager.flight.list.label.departure" path="departure" readonly="true"/>
		<acme:input-textarea code="manager.flight.list.label.arrival" path="arrival" readonly="true"/>
		<acme:input-textarea code="manager.flight.list.label.layovers" path="layovers" readonly="true"/>
		<jstl:choose>
			<jstl:when test="${(_command == 'update' || _command == 'show' || _command == 'publish' || _command == 'delete') && draftMode == true}">
				<acme:input-checkbox code="manager.flight.form.label.confirmation.update" path="confirmation"/>
				<acme:submit code="manager.flight.form.button.update" action="/airline-manager/flight/update"/>
				<acme:submit code="manager.flight.form.button.publish" action="/airline-manager/flight/publish"/>
				<acme:submit code="manager.flight.form.button.delete" action="/airline-manager/flight/delete"/>
			</jstl:when>
			<jstl:when test="${_command == 'create'}">
				<acme:input-checkbox code="manager.flight.form.label.confirmation.create" path="confirmation"/>	
				<acme:submit code="manager.flight.form.button.create" action="/airline-manager/flight/create"/>
			</jstl:when>		
	</jstl:choose>		
	<jstl:if test="${(_command == 'update' || _command == 'show' || _command == 'publish')}">
			<acme:button code="manager.flight.form.button.legs" action="/airline-manager/legs/list?flightId=${flightId}"/>
	</jstl:if>
</acme:form>
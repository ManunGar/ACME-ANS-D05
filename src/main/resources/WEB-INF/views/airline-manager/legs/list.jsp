<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="manager.legs.list.label.departure" path="departure" width="40%"/>
	<acme:list-column code="manager.legs.list.label.arrival" path="arrival" width="40%"/>
	<acme:list-column code="manager.legs.list.label.status" path="status" width="20%" sortable="false"/>
</acme:list>

<acme:button code="manager.legs.list.button.create" action="/airline-manager/legs/create?flightId=${flightId}"/>
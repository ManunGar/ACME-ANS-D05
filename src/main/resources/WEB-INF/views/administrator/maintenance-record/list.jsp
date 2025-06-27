<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="technician.maintenance-record.list.label.aircraft-model" path="aircraft.model" width="25%"/>
	<acme:list-column code="technician.maintenance-record.list.label.maintenanceMoment" path="maintenanceMoment" width="25%"/>
	<acme:list-column code="technician.maintenance-record.list.label.status" path="status" width="25%"/>
	<acme:list-column code="technician.maintenance-record.list.label.nextInspection" path="nextInspection" width="25%"/>
	<acme:list-payload path="payload"/>
</acme:list>
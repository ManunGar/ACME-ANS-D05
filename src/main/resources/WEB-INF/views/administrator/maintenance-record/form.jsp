<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textarea code="administrator.maintenance-record.form.label.technician" path="technician" readonly="true"/>
	<acme:input-select code="administrator.maintenance-record.form.label.aircraft" path="aircraft" choices="${aircrafts}" readonly="${acme:anyOf(_command, 'show|update|publish')}"/>
	<acme:input-moment code="administrator.maintenance-record.form.label.maintenanceMoment" path="maintenanceMoment"/>
	<acme:input-select code="administrator.maintenance-record.form.label.status" path="status" choices="${statuses}"/>
	<acme:input-moment code="administrator.maintenance-record.form.label.nextInspection" path="nextInspection"/>
	<acme:input-money code="administrator.maintenance-record.form.label.estimatedCost" path="estimatedCost"/>
	<acme:input-textarea code="administrator.maintenance-record.form.label.notes" path="notes"/>
	
	
	<jstl:choose>	 
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="administrator.maintenance-record.form.button.tasks" action="/administrator/task/list?maintenanceRecordId=${id}"/>			
		</jstl:when>	
	</jstl:choose>
</acme:form>
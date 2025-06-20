<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
		<acme:input-textbox code="customer.booking-record.list.label.booking" path="locatorCode" readonly="true"/>
		<acme:input-select code="customer.booking-record.list.label.passenger" path="passenger" choices="${passengers}"/>
		
	<jstl:choose>
			
			<jstl:when test="${_command == 'create'}">
				<acme:submit code="authenticated.booking-record.form.button.create" action="/customer/booking-record/create?bookingId=${bookingId}"/>
			</jstl:when>
			
			<jstl:when test="${_command == 'delete'}">
				<acme:submit code="authenticated.booking-record.form.button.delete" action="/customer/booking-record/delete?bookingId=${bookingId}"/>
			</jstl:when>
					
	</jstl:choose>	
		
</acme:form>
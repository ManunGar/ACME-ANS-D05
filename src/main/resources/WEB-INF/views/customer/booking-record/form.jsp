<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
		<acme:input-select code="customer.booking-record.list.label.booking" path="booking" choices="${bookings}"/>
		<acme:input-select code="customer.booking-record.list.label.passenger" path="passenger" choices="${passengers}"/>
		
	<jstl:choose>
			
			<jstl:when test="${_command == 'create'}">
				<acme:submit code="authenticated.booking-record.form.button.create" action="/customer/booking-record/create"/>
			</jstl:when>
					
	</jstl:choose>	
		
</acme:form>
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
		<acme:input-select code="authenticated.booking.list.label.flight" path="flight" choices="${flights}"/>
		<acme:input-textbox code="authenticated.booking.list.label.locatorCode" path="locatorCode"/>
		<acme:input-moment code="authenticated.booking.list.label.purchaseMoment" path="purchaseMoment" readonly="true"/>
		<acme:input-select code="authenticated.booking.list.label.travelClass" path="travelClass" choices="${travelClass}"/>
		<acme:input-money code="authenticated.booking.list.label.price" path="price" readonly="true"/>
		<acme:input-textbox code="authenticated.booking.list.label.lastNibble" path="lastNibble"/>
		<acme:input-textarea code="authenticated.booking.list.label.passenger" path="passengers" readonly="true"/>
		
</acme:form>
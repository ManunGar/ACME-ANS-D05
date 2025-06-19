<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
		<acme:input-textbox code="authenticated.customer.list.label.identifier" path="identifier"/>
		<acme:input-textbox code="authenticated.customer.list.label.phone-number" path="phoneNumber"/>
		<acme:input-textbox code="authenticated.customer.list.label.physical-address" path="physicalAddress"/>
		<acme:input-textbox code="authenticated.customer.list.label.city" path="city"/>
		<acme:input-textbox code="authenticated.customer.list.label.country" path="country"/>
		<acme:input-textbox code="authenticated.customer.list.label.earned-points" path="earnedPoints" readonly="true" />
		
	<jstl:if test="${_command == 'create'}">	
		<acme:submit code="authenticated.customer.form.button.create" action="/authenticated/customer/create"/>
	</jstl:if>
	<jstl:if test="${_command == 'update'}">
		<acme:submit code="authenticated.consumer.form.button.update" action="/authenticated/customer/update"/>
	</jstl:if>
</acme:form>
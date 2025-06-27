<%--
- menu.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:menu-bar>
	<acme:menu-left>
		<acme:menu-option code="master.menu.anonymous" access="isAnonymous()">
			<acme:menu-suboption code="master.menu.anonymous.favourite-student1" action="https://mouredev.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-student2" action="https://www.realbetisbalompie.es/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-student3" action="https://caja87baloncesto.es/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-student4" action="https://tetr.io/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-student5" action="https://www.mitele.es/directo/"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.customer" access="hasRealm('Customer')">
			<acme:menu-suboption code="master.menu.passenger" action="/customer/passenger/list-menu"/>
			<acme:menu-suboption code="master.menu.booking" action="/customer/booking/list"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.maintenance-record" access="hasRealm('Technician')">
			<acme:menu-suboption code="master.menu.maintenance-record.list-my-maintenance-records" action="/technician/maintenance-record/list?mine=true" />			
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.maintenance-record.list-published-maintenance-records" action="/technician/maintenance-record/list" />
			
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.task" access="hasRealm('Technician')">			
			<acme:menu-suboption code="master.menu.task.list-my-tasks" action="/technician/task/list?mine=true" />
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.task.list-published-tasks" action="/technician/task/list" />
			
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.assistanceAgent" access="hasRealm('AssistanceAgent')">
			<acme:button code="assistanceAgent.claim.list.button.list.resolved" action="/assistance-agent/claim/listResolved"/>
			<acme:button code="assistanceAgent.claim.list.button.list.notResolved" action="/assistance-agent/claim/listNotResolved"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.administrator" access="hasRealm('Administrator')">
			<acme:menu-suboption code="master.menu.administrator.list-user-accounts" action="/administrator/user-account/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.list-airport" action="/administrator/airport/list"/>
			<acme:menu-suboption code="master.menu.administrator.list-airlines" action="/administrator/airline/list"/>
			<acme:menu-suboption code="master.menu.administrator.list-aircrafts" action="/administrator/aircraft/list"/>
			
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-initial" action="/administrator/system/populate-initial"/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-sample" action="/administrator/system/populate-sample"/>			
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.shut-system-down" action="/administrator/system/shut-down"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.booking.published" access="hasRealm('Administrator')" action="/administrator/booking/list"/>
		<acme:menu-option code="master.menu.claim" access="hasRealm('Administrator')" action="/administrator/claim/list"/>
 		<acme:menu-option code="master.menu.maintenance-record.published" access="hasRealm('Administrator')" action="/administrator/maintenance-record/list"/>
		
		<acme:menu-option code="master.menu.manager" access="hasRealm('AirlineManager')">
			<acme:menu-suboption code="master.menu.manager.list-flight" action="/airline-manager/flight/list"/>
			<acme:menu-separator/>
			
		</acme:menu-option>

		<acme:menu-option code="master.menu.provider" access="hasRealm('Provider')">
			<acme:menu-suboption code="master.menu.provider.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.consumer" access="hasRealm('Consumer')">
			<acme:menu-suboption code="master.menu.consumer.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.flight-crew-member" access="hasRealm('FlightCrewMember')">
			<acme:menu-suboption code="master.menu.flight-crew-member.flight-assignment.completed-list" action="/flight-crew-member/flight-assignment/completed-list"/>
			<acme:menu-suboption code="master.menu.flight-crew-member.flight-assignment.not-completed-list" action="/flight-crew-member/flight-assignment/not-completed-list"/>
		</acme:menu-option>
	</acme:menu-left>
	
	

	<acme:menu-right>		
		<acme:menu-option code="master.menu.user-account" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.user-account.general-profile" action="/authenticated/user-account/update"/>
			<acme:menu-suboption code="master.menu.user-account.become-provider" action="/authenticated/provider/create" access="!hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.provider-profile" action="/authenticated/provider/update" access="hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.become-consumer" action="/authenticated/consumer/create" access="!hasRealm('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.consumer-profile" action="/authenticated/consumer/update" access="hasRealm('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.become-customer" action="/authenticated/customer/create" access="!hasRealm('Customer')"/>
			<acme:menu-suboption code="master.menu.user-account.customer-profile" action="/authenticated/customer/update" access="hasRealm('Customer')"/>
			<acme:menu-suboption code="master.menu.user-account.become-assistance-agent" action="/authenticated/assistance-agent/create" access="!hasRealm('AssistanceAgent')"/>
			<acme:menu-suboption code="master.menu.user-account.assistance-agent-profile" action="/authenticated/assistance-agent/update" access="hasRealm('AssistanceAgent')"/>
			<acme:menu-suboption code="master.menu.user-account.become-technician" action="/authenticated/technician/create" access="!hasRealm('Technician')"/>
			<acme:menu-suboption code="master.menu.user-account.technician-profile" action="/authenticated/technician/update" access="hasRealm('Technician')"/>
		</acme:menu-option>
	</acme:menu-right>
</acme:menu-bar>


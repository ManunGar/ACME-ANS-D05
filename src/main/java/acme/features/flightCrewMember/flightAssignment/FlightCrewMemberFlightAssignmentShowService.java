
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.FlightAssignments.Duty;
import acme.entities.FlightAssignments.FlightAssignment;
import acme.entities.FlightAssignments.Status;
import acme.entities.Legs.Legs;
import acme.realms.AvailabilityStatus;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentShowService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	// Internal state

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;

	// AbstractGuiService interface


	@Override
	public void authorise() {
		boolean status;
		int flightCrewMemberId;
		Collection<FlightAssignment> flightAssignments;

		flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getUserAccount().getId();
		flightAssignments = this.repository.findFlightAssignmentByFlightCrewMember(flightCrewMemberId);
		status = flightAssignments.stream().allMatch(fa -> fa.getFlightCrewMember().getId() == flightCrewMemberId) && super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		FlightAssignment flightAssignment;
		int id;

		id = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(id);

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset;

		SelectChoices legChoices;
		SelectChoices flightCrewMemberChoices;
		SelectChoices dutyChoices;
		SelectChoices statusChoices;

		Collection<Legs> legs = this.repository.findAllLegs();
		Collection<FlightCrewMember> flightCrewMembers = this.repository.findFlightCrewMembersByAvailabilityStatus(AvailabilityStatus.AVAILABLE);
		legChoices = SelectChoices.from(legs, "flightNumber", flightAssignment.getLeg());
		flightCrewMemberChoices = SelectChoices.from(flightCrewMembers, "employeeCode", flightAssignment.getFlightCrewMember());
		dutyChoices = SelectChoices.from(Duty.class, flightAssignment.getDuty());
		statusChoices = SelectChoices.from(Status.class, flightAssignment.getStatus());

		dataset = super.unbindObject(flightAssignment, "moment", "status", "remarks");
		dataset.put("flightAssignment", flightAssignment.getId());
		dataset.put("duty", dutyChoices);
		dataset.put("status", statusChoices);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("flightCrewMember", flightCrewMemberChoices.getSelected().getKey());
		dataset.put("flightCrewMembers", flightCrewMemberChoices);

		dataset.put("legCompleted", this.repository.legCompletedByFlightAssignmentId(flightAssignment.getId(), MomentHelper.getCurrentMoment()));

		super.getResponse().addData(dataset);
	}
}

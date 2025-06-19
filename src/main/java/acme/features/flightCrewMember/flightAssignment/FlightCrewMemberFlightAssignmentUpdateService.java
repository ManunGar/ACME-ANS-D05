
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
public class FlightCrewMemberFlightAssignmentUpdateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	// Internal state

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;

	// AbstractGuiService interface


	@Override
	public void authorise() {
		boolean status;
		int flightCrewMemberId;
		FlightAssignment flightAssignment;
		FlightCrewMember flightCrewMember;

		flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getUserAccount().getId();
		flightAssignment = this.repository.findFlightAssignmentById(super.getRequest().getData("flightAssignmentId", int.class));
		flightCrewMember = this.repository.findFlightCrewMembersById(flightCrewMemberId);
		boolean isLeadAttendant = flightAssignment != null && flightAssignment.getDuty() == Duty.LEAD_ATTENDANT;

		status = flightAssignment != null && isLeadAttendant && super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class) && flightCrewMember.getId() == flightAssignment.getFlightCrewMember().getId();

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
	public void bind(final FlightAssignment flightAssignment) {
		super.bindObject(flightAssignment, "draftMode", "duty", "moment", "status", "remarks");
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		if (flightAssignment.isDraftMode() == false)
			super.state(false, "draftMode", "acme.validation.confirmation.message.update");

		if (flightAssignment.getFlightCrewMember().getAvailabilityStatus() != AvailabilityStatus.AVAILABLE)
			super.state(false, "flightCrewMember", "acme.validation.FlightAssignment.notAvailable.message");

		if (MomentHelper.isPast(flightAssignment.getLeg().getArrival()))
			super.state(false, "leg", "acme.validation.FlightAssignment.legAlreadyOccurred.message");

		Collection<FlightAssignment> existingAssignments = this.repository.findFlightAssignmentByFlightCrewMember(flightAssignment.getFlightCrewMember().getId());
		for (FlightAssignment existingAssignment : existingAssignments)
			if (existingAssignment.getLeg() != null && existingAssignment.getLeg().getId() == flightAssignment.getLeg().getId()) {
				super.state(false, "flightCrewMember", "acme.validation.FlightAssignment.memberAssignedToMultipleLegs.message");
				break;
			}

		if (flightAssignment.getDuty() == Duty.PILOT || flightAssignment.getDuty() == Duty.CO_PILOT) {
			boolean hasPilot = this.repository.existsFlightCrewMemberWithDutyInLeg(flightAssignment.getLeg().getId(), Duty.PILOT);
			boolean hasCopilot = this.repository.existsFlightCrewMemberWithDutyInLeg(flightAssignment.getLeg().getId(), Duty.CO_PILOT);

			if (flightAssignment.getDuty() == Duty.PILOT && hasPilot)
				super.state(false, "duty", "acme.validation.FlightAssignment.havePilot.message");

			if (flightAssignment.getDuty() == Duty.CO_PILOT && hasCopilot)
				super.state(false, "duty", "acme.validation.FlightAssignment.haveCopilot.message");
		}
	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		this.repository.save(flightAssignment);
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

		dataset = super.unbindObject(flightAssignment, "draftMode", "moment", "status", "remarks");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("flightAssignment", flightAssignment.getId());
		dataset.put("duty", dutyChoices);
		dataset.put("status", statusChoices);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("flightCrewMember", flightCrewMemberChoices.getSelected().getKey());
		dataset.put("flightCrewMembers", flightCrewMemberChoices);

		super.getResponse().addData(dataset);
	}

}

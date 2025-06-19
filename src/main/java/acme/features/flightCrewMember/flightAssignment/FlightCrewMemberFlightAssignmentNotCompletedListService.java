
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.FlightAssignments.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentNotCompletedListService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

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
		Collection<FlightAssignment> flightAssignments;

		Date currentMoment = MomentHelper.getCurrentMoment();
		flightAssignments = this.repository.findNotCompletedAssignments(currentMoment);

		super.getBuffer().addData(flightAssignments);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset;

		dataset = super.unbindObject(flightAssignment, "duty", "moment", "status", "remarks");
		super.getResponse().addData(dataset);
	}

}

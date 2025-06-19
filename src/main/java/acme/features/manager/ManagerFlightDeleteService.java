
package acme.features.manager;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.ActivityLogs.ActivityLog;
import acme.entities.Flight.Flight;
import acme.entities.FlightAssignments.FlightAssignment;
import acme.entities.Legs.Legs;
import acme.features.flightCrewMember.flightAssignment.FlightCrewMemberFlightAssignmentRepository;
import acme.features.manager.legs.ManagerLegsRepository;
import acme.realms.AirlineManager;

@GuiService
public class ManagerFlightDeleteService extends AbstractGuiService<AirlineManager, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerFlightRepository						repository;

	@Autowired
	private ManagerLegsRepository						legRepository;

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository	flightAssignmentRepository;


	@Override
	public void authorise() {
		int userId;
		int flightId;
		Flight flight;
		boolean autorhorise;

		flightId = super.getRequest().getData("id", int.class);
		userId = super.getRequest().getPrincipal().getActiveRealm().getUserAccount().getId();
		flight = this.repository.findOne(flightId);
		autorhorise = flight.getManager().getUserAccount().getId() == userId;
		boolean draftMode = flight.getDraftMode();
		super.getResponse().setAuthorised(draftMode && autorhorise);
	}

	@Override
	public void load() {
		int flightId;
		Flight flight;

		flightId = super.getRequest().getData("id", int.class);
		flight = this.repository.findOne(flightId);

		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {
		super.bindObject(flight, "description", "highlights", "selfTransfer", "cost");
	}

	@Override
	public void validate(final Flight flight) {
		Collection<Legs> legs;
		boolean confirmation = true;

		legs = this.legRepository.findAllByFlightId(flight.getId());

		for (Legs l : legs)
			if (!l.getDraftMode())
				confirmation = false;

		super.state(confirmation, "*", "acme.validation.draftMode.message");
	}

	@Override
	public void perform(final Flight flight) {
		List<Legs> legs = (List<Legs>) this.legRepository.findAllByFlightId(flight.getId());

		List<FlightAssignment> flightAssigments = (List<FlightAssignment>) this.flightAssignmentRepository.findAssignmentsByFlightId(flight.getId());
		List<ActivityLog> logs = (List<ActivityLog>) this.repository.findActivityLogsByFlightId(flight.getId());
		this.repository.deleteAll(logs);
		this.repository.deleteAll(flightAssigments);
		this.repository.deleteAll(legs);
		this.repository.delete(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		dataset = super.unbindObject(flight, "description", "highlights", "selfTransfer", "cost", "draftMode");
		dataset.put("departure", flight.getDeparture());
		dataset.put("arrival", flight.getArrival());
		dataset.put("origin", flight.getOrigin());
		dataset.put("destination", flight.getDestination());
		dataset.put("layovers", flight.getLayovers());
		dataset.put("flightId", flight.getId());
		super.getResponse().addData(dataset);
	}

}

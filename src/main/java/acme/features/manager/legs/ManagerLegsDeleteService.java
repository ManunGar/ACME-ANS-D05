
package acme.features.manager.legs;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.ActivityLogs.ActivityLog;
import acme.entities.Aircrafts.Aircraft;
import acme.entities.Aircrafts.AircraftRepository;
import acme.entities.Airports.Airport;
import acme.entities.Airports.AirportRepository;
import acme.entities.Flight.Flight;
import acme.entities.Flight.FlightRepository;
import acme.entities.FlightAssignments.FlightAssignment;
import acme.entities.Legs.Legs;
import acme.entities.Legs.LegsStatus;
import acme.features.flightCrewMember.flightAssignment.FlightCrewMemberFlightAssignmentRepository;
import acme.features.manager.ManagerFlightRepository;
import acme.realms.AirlineManager;

@GuiService
public class ManagerLegsDeleteService extends AbstractGuiService<AirlineManager, Legs> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerLegsRepository						repository;

	@Autowired
	private FlightRepository							flightRepository;

	@Autowired
	private ManagerFlightRepository						managerFlightRepository;

	@Autowired
	private AirportRepository							airportRepository;

	@Autowired
	private AircraftRepository							aircraftRepository;

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository	flightAssignmentRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int userId;
		int legId;
		Legs legs;
		boolean autorhorise;

		legId = super.getRequest().getData("id", int.class);
		userId = super.getRequest().getPrincipal().getActiveRealm().getUserAccount().getId();
		legs = this.repository.findLegById(legId);
		autorhorise = legs.getFlight().getManager().getUserAccount().getId() == userId;
		boolean draftMode = legs.getDraftMode();
		super.getResponse().setAuthorised(draftMode && autorhorise);
	}

	@Override
	public void load() {
		int legId;
		Legs leg;

		legId = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(legId);

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Legs leg) {
		super.bindObject(leg, "departure", "arrival", "status", "departureAirport", "arrivalAirport", "aircraft", "flight", "flightNumber");
	}

	@Override
	public void validate(final Legs leg) {

	}

	@Override
	public void perform(final Legs leg) {
		List<FlightAssignment> flightAssigments = (List<FlightAssignment>) this.flightAssignmentRepository.findAssignmentsByFlightId(leg.getFlight().getId());
		List<ActivityLog> logs = (List<ActivityLog>) this.managerFlightRepository.findActivityLogsByFlightId(leg.getFlight().getId());
		this.repository.deleteAll(logs);
		this.repository.deleteAll(flightAssigments);
		this.repository.delete(leg);
	}

	@Override
	public void unbind(final Legs leg) {
		Dataset dataset;
		SelectChoices choices;
		SelectChoices flightChoices;
		SelectChoices departureAirportChoices;
		SelectChoices arrivalAirportChoices;
		SelectChoices aircraftChoices;

		Collection<Flight> flights = this.flightRepository.findAllFlight();
		Collection<Airport> airports = this.airportRepository.findAllAirport();
		Collection<Aircraft> aircrafts = this.aircraftRepository.findAllAircarftByAirlineId(leg.getFlight().getManager().getAirline().getId());
		flightChoices = SelectChoices.from(flights, "description", leg.getFlight());
		departureAirportChoices = SelectChoices.from(airports, "city", leg.getDepartureAirport());
		arrivalAirportChoices = SelectChoices.from(airports, "city", leg.getArrivalAirport());
		aircraftChoices = SelectChoices.from(aircrafts, "model", leg.getAircraft());
		choices = SelectChoices.from(LegsStatus.class, leg.getStatus());

		dataset = super.unbindObject(leg, "departure", "arrival", "draftMode", "flightNumber");
		dataset.put("status", choices);
		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("flights", flightChoices);
		dataset.put("departureAirport", departureAirportChoices.getSelected().getKey());
		dataset.put("departureAirports", departureAirportChoices);
		dataset.put("arrivalAirport", arrivalAirportChoices.getSelected().getKey());
		dataset.put("arrivalAirports", arrivalAirportChoices);
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("legId", leg.getId());

		super.getResponse().addData(dataset);
	}

}

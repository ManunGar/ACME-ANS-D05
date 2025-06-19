
package acme.features.manager;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Flight.Flight;
import acme.realms.AirlineManager;

@GuiService
public class ManagerFlightUpdateService extends AbstractGuiService<AirlineManager, Flight> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerFlightRepository repository;


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
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");

		confirmation = flight.getDraftMode();
		super.state(confirmation, "*", "acme.validation.draftMode.message");
	}

	@Override
	public void perform(final Flight flight) {
		this.repository.save(flight);
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


package acme.features.manager.legs;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Flight.Flight;
import acme.entities.Legs.Legs;
import acme.features.manager.ManagerFlightRepository;
import acme.realms.AirlineManager;

@GuiService
public class ManagerLegsListService extends AbstractGuiService<AirlineManager, Legs> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerLegsRepository	repository;

	@Autowired
	private ManagerFlightRepository	flightRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int id;
		Flight flight;
		int managerId = super.getRequest().getPrincipal().getActiveRealm().getUserAccount().getId();

		id = super.getRequest().getData("flightId", int.class);
		flight = this.flightRepository.findOne(id);
		boolean status = flight != null && (!flight.getDraftMode() || flight.getManager().getUserAccount().getId() == managerId);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int flightId = super.getRequest().getData("flightId", int.class);
		super.getResponse().addGlobal("flightId", flightId);
		Flight flight = this.flightRepository.findOne(flightId);
		super.getResponse().addGlobal("flightDraftMode", flight.getDraftMode());
		Collection<Legs> legs = this.repository.findAllByFlightId(flightId);
		super.getBuffer().addData(legs);
	}

	@Override
	public void unbind(final Legs leg) {
		Dataset dataset;

		dataset = super.unbindObject(leg, "departure", "arrival", "status");
		dataset.put("flightId", leg.getFlight().getId());
		super.getResponse().addData(dataset);
	}

}

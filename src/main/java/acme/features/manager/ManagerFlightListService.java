
package acme.features.manager;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Flight.Flight;
import acme.realms.AirlineManager;

@GuiService
public class ManagerFlightListService extends AbstractGuiService<AirlineManager, Flight> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerFlightRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
		boolean isManager = super.getRequest().getPrincipal().hasRealmOfType(AirlineManager.class);
		super.getResponse().setAuthorised(isManager);
	}

	@Override
	public void load() {
		Collection<Flight> flight;
		int id;
		id = super.getRequest().getPrincipal().getActiveRealm().getUserAccount().getId();
		flight = this.repository.findAllByManagerId(id);

		super.getBuffer().addData(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		dataset = super.unbindObject(flight, "description", "cost");

		super.getResponse().addData(dataset);
	}

}

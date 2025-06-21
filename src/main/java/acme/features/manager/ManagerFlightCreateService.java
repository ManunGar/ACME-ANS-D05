
package acme.features.manager;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Flight.Flight;
import acme.realms.AirlineManager;

@GuiService
public class ManagerFlightCreateService extends AbstractGuiService<AirlineManager, Flight> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerFlightRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int userId;
		boolean autorhorise;
		autorhorise = super.getRequest().getPrincipal().hasRealmOfType(AirlineManager.class);
		super.getResponse().setAuthorised(autorhorise);
	}

	@Override
	public void load() {
		Flight flight;
		Money money = new Money();
		money.setAmount(0.0);
		money.setCurrency("EUR");
		AirlineManager manager = (AirlineManager) super.getRequest().getPrincipal().getActiveRealm();

		flight = new Flight();
		flight.setDescription("");
		flight.setHighlights("");
		flight.setCost(money);
		flight.setDraftMode(true);
		flight.setManager(manager);
		flight.setSelfTransfer(false);

		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {
		System.out.println("bind: " + flight.getDraftMode());
		super.bindObject(flight, "description", "highlights", "cost", "selfTransfer");
	}

	@Override
	public void perform(final Flight flight) {

		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;
		System.out.println("unbind: " + flight);
		dataset = super.unbindObject(flight, "description", "highlights", "cost", "selfTransfer", "draftMode");
		dataset.put("manager", flight.getManager());
		System.out.println("dataset: " + dataset);
		super.getResponse().addData(dataset);
	}

}

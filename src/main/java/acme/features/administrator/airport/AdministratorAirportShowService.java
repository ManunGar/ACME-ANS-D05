
package acme.features.administrator.airport;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Airports.Airport;
import acme.entities.Airports.OperationalScope;

@GuiService
public class AdministratorAirportShowService extends AbstractGuiService<Administrator, Airport> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAirportRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = true;
		Airport airport;
		boolean isAdministrator = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);

		try {
			Integer id = super.getRequest().getData("id", Integer.class);
			airport = this.repository.findAirportById(id);
			if (airport == null)
				status = false;
		} catch (Throwable E) {
			status = false;
		}
		super.getResponse().setAuthorised(isAdministrator && status);
	}

	@Override
	public void load() {
		int id;
		Airport airport;

		id = super.getRequest().getData("id", int.class);
		airport = this.repository.findAirportById(id);
		super.getBuffer().addData(airport);
	}

	@Override
	public void unbind(final Airport airport) {
		Dataset dataset;
		SelectChoices choices;
		choices = SelectChoices.from(OperationalScope.class, airport.getOperationalScope());

		dataset = super.unbindObject(airport, "name", "IATAcode", "city", "country", "website", "email", "contactPhoneNumber");
		dataset.put("operationalScopes", choices);

		super.getResponse().addData(dataset);
	}
}

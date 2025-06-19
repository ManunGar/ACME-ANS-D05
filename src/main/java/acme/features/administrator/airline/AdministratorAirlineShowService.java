
package acme.features.administrator.airline;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Airlines.Airline;
import acme.entities.Airlines.Type;

@GuiService
public class AdministratorAirlineShowService extends AbstractGuiService<Administrator, Airline> {

	@Autowired
	private AdministratorAirlineRepository repository;


	@Override
	public void authorise() {
		boolean status = true;
		Airline airline;
		boolean isAdministrator = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);

		try {
			Integer id = super.getRequest().getData("id", Integer.class);
			airline = this.repository.findAirlineById(id);
			if (airline == null)
				status = false;
		} catch (Throwable E) {
			status = false;
		}
		super.getResponse().setAuthorised(isAdministrator && status);
	}

	@Override
	public void load() {
		int id;
		Airline airline;

		id = super.getRequest().getData("id", int.class);
		airline = this.repository.findAirlineById(id);
		super.getBuffer().addData(airline);
	}

	@Override
	public void unbind(final Airline airline) {
		Dataset dataset;
		SelectChoices choices;
		choices = SelectChoices.from(Type.class, airline.getType());

		dataset = super.unbindObject(airline, "name", "IATAcode", "website", "type", "foundationMoment", "emailAddress", "phoneNumber");
		dataset.put("types", choices);

		super.getResponse().addData(dataset);
	}

}

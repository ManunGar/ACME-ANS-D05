
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
public class AdministratorAirlineUpdateService extends AbstractGuiService<Administrator, Airline> {

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
		Airline airline;
		int id;

		id = super.getRequest().getData("id", int.class);
		airline = this.repository.findAirlineById(id);

		super.getBuffer().addData(airline);
	}

	@Override
	public void bind(final Airline airline) {
		super.bindObject(airline, "name", "IATAcode", "website", "type", "foundationMoment", "emailAddress", "phoneNumber");
	}

	@Override
	public void validate(final Airline airline) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");

		Airline a = this.repository.findAirlineByIataCode(airline.getIATAcode());
		if (a != null && a.getId() != airline.getId())
			super.state(false, "IATAcode", "acme.validation.airline.IATACode.message");

	}

	@Override
	public void perform(final Airline airline) {
		this.repository.save(airline);
	}

	@Override
	public void unbind(final Airline airline) {
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(Type.class, airline.getType());

		dataset = super.unbindObject(airline, "name", "IATAcode", "website", "type", "foundationMoment", "emailAddress", "phoneNumber");
		dataset.put("types", choices);

		super.getResponse().addData(dataset);

	}

}

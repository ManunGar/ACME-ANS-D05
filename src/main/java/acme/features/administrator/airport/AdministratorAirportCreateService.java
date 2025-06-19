
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
public class AdministratorAirportCreateService extends AbstractGuiService<Administrator, Airport> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAirportRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Airport airport;

		airport = new Airport();
		airport.setName("");
		airport.setIATAcode("");
		airport.setOperationalScope(null);
		airport.setCity("");
		airport.setCountry("");
		airport.setWebsite("");
		airport.setContactPhoneNumber("");

		super.getBuffer().addData(airport);
	}

	@Override
	public void bind(final Airport airport) {
		super.bindObject(airport, "name", "IATAcode", "city", "country", "website", "email", "contactPhoneNumber", "operationalScope");
	}

	@Override
	public void validate(final Airport airport) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");

		Airport iataCode = this.repository.findAirportByIataCode(airport.getIATAcode());
		if (iataCode != null)
			super.state(false, "IATAcode", "acme.validation.confirmation.message.aiport.IATAcode");

		Airport email = this.repository.findAirportByEmail(airport.getEmail());
		if (email != null)
			super.state(false, "email", "acme.validation.confirmation.message.aiport.email");
	}

	@Override
	public void perform(final Airport airport) {

		this.repository.save(airport);
	}

	@Override
	public void unbind(final Airport airport) {
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(OperationalScope.class, airport.getOperationalScope());

		dataset = super.unbindObject(airport, "name", "IATAcode", "city", "country", "website", "email", "contactPhoneNumber");
		dataset.put("operationalScopes", choices);
		dataset.put("confirmation", false);
		dataset.put("readonly", false);

		super.getResponse().addData(dataset);
	}

}

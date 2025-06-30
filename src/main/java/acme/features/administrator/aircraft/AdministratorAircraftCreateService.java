
package acme.features.administrator.aircraft;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Aircrafts.Aircraft;
import acme.entities.Aircrafts.AircraftStatus;
import acme.entities.Airlines.Airline;

@GuiService
public class AdministratorAircraftCreateService extends AbstractGuiService<Administrator, Aircraft> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAircraftRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);
		try {

			if (super.getRequest().hasData("id")) {

				Integer airlineId = super.getRequest().getData("airline", int.class);

				if (airlineId != 0) {
					Airline airline = this.repository.findAirlineById(airlineId);
					if (airline == null)
						status = false;
				}
			}
		} catch (Throwable E) {
			status = false;

		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Aircraft aircraft;

		aircraft = new Aircraft();
		aircraft.setModel("");
		aircraft.setRegistrationNumber("");
		aircraft.setCapacity(1);
		aircraft.setCargoWeight(2000);
		aircraft.setStatus(null);
		aircraft.setDetails("");

		super.getBuffer().addData(aircraft);
	}

	@Override
	public void bind(final Aircraft aircraft) {
		int airlineId;
		Airline airline;

		airlineId = super.getRequest().getData("airline", int.class);
		airline = this.repository.findAirlineById(airlineId);

		super.bindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "details", "status");
		aircraft.setAirline(airline);
	}

	@Override
	public void validate(final Aircraft aircraft) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");

		Aircraft a = this.repository.findAircraftByRegistrationNumber(aircraft.getRegistrationNumber());
		if (a != null)
			super.state(false, "registrationNumber", "acme.validation.confirmation.message.aircraft.registrationNumber");
	}

	@Override
	public void perform(final Aircraft aircraft) {
		this.repository.save(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		SelectChoices choices;
		SelectChoices airlineChoices;
		Dataset dataset;

		Collection<Airline> airlines = this.repository.findAllAirline();
		airlineChoices = SelectChoices.from(airlines, "id", aircraft.getAirline());
		choices = SelectChoices.from(AircraftStatus.class, aircraft.getStatus());

		dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details");
		dataset.put("aircraftStatus", choices);
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("airlines", airlineChoices);
		dataset.put("airline", airlineChoices.getSelected().getKey());
		dataset.put("readOnlyStatus", false);

		super.getResponse().addData(dataset);
	}
}

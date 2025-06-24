
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerShowService extends AbstractGuiService<Customer, Passenger> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int id;
		Passenger passenger;
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		boolean status = true;
		try {

			id = super.getRequest().getData("id", int.class);
			passenger = this.repository.findPassengerById(id);
			status = passenger.getCustomer().getId() == customerId && super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		} catch (Throwable E) {
			status = false;
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id;
		Passenger passenger;

		id = super.getRequest().getData("id", int.class);
		passenger = this.repository.findPassengerById(id);
		super.getBuffer().addData(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "email", "passport", "dateOfBirth", "draftMode", "specialNeeds");

		super.getResponse().addData(dataset);
	}
}

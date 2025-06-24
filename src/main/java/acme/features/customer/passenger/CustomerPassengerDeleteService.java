
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Bookings.BookingRecord;
import acme.entities.Passengers.Passenger;
import acme.features.customer.bookingRecord.CustomerBookingRecordRepository;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerDeleteService extends AbstractGuiService<Customer, Passenger> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRecordRepository	bookingRecordrepository;

	@Autowired
	private CustomerPassengerRepository		repository;

	// AbstractGuiService interfaced ------------------------------------------


	@Override
	public void authorise() {
		int id;
		Passenger passenger = null;
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getUserAccount().getId();
		boolean status = true;
		try {
			id = super.getRequest().getData("id", int.class);
			passenger = this.repository.findPassengerById(id);
			status = passenger.getCustomer().getUserAccount().getId() == customerId && super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		} catch (Throwable e) {
			status = false;
		}
		super.getResponse().setAuthorised(status && passenger.isDraftMode());
	}

	@Override
	public void load() {
		Passenger passenger;
		int id;

		id = super.getRequest().getData("id", int.class);
		passenger = this.repository.findPassengerById(id);

		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {

		super.bindObject(passenger, "fullName", "email", "passport", "dateOfBirth", "specialNeeds");
	}

	@Override
	public void validate(final Passenger passenger) {
		;

	}

	@Override
	public void perform(final Passenger passenger) {
		for (BookingRecord bk : this.bookingRecordrepository.findBookingRecordByPassengerId(passenger.getId()))
			this.bookingRecordrepository.delete(bk);
		this.repository.delete(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "email", "passport", "dateOfBirth", "draftMode", "specialNeeds");

		super.getResponse().addData(dataset);
	}
}

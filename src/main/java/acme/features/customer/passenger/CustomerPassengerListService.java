
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Bookings.Booking;
import acme.entities.Passengers.Passenger;
import acme.features.customer.booking.CustomerBookingRepository;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerListService extends AbstractGuiService<Customer, Passenger> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository bookingRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean isCustomer;
		int id;
		Booking booking;
		boolean status;

		isCustomer = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		try {
			id = super.getRequest().getData("bookingId", int.class);
			booking = this.bookingRepository.findBookingById(id);
			status = booking.getCustomer().getId() == customerId;
		} catch (Throwable e) {
			status = false;
		}

		super.getResponse().setAuthorised(isCustomer && status);
	}

	@Override
	public void load() {
		Collection<Passenger> passengers;
		int id;

		id = super.getRequest().getData("bookingId", int.class);
		super.getResponse().addGlobal("bookingId", id);
		passengers = this.bookingRepository.findPassengersByBooking(id);

		super.getBuffer().addData(passengers);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "email");

		super.getResponse().addData(dataset);
	}
}

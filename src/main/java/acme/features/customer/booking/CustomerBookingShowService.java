
package acme.features.customer.booking;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Bookings.Booking;
import acme.entities.Bookings.TravelClass;
import acme.entities.Flight.Flight;
import acme.entities.Legs.LegRepository;
import acme.realms.Customer;

@GuiService
public class CustomerBookingShowService extends AbstractGuiService<Customer, Booking> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository	repository;

	@Autowired
	private LegRepository				legRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int id;
		Booking booking;
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		boolean status = true;
		boolean isCustomer = true;
		try {

			id = super.getRequest().getData("id", int.class);
			booking = this.repository.findBookingById(id);
			status = booking.getCustomer().getId() == customerId;
			isCustomer = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		} catch (Throwable E) {
			status = false;
		}
		super.getResponse().setAuthorised(status && isCustomer);
	}

	@Override
	public void load() {
		int id;
		Booking booking;

		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(id);
		super.getBuffer().addData(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices choices;
		SelectChoices flightChoices;

		Date today = MomentHelper.getCurrentMoment();
		Collection<Flight> flights = this.repository.findAllPublishedFlights();
		Collection<Flight> flightsInFuture = flights.stream().filter(f -> this.legRepository.findDepartureByFlightId(f.getId()).get(0).after(today)).toList();
		flightChoices = SelectChoices.from(flightsInFuture, "Destination", booking.getFlight());
		choices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		Collection<String> passengers = this.repository.findPassengersNameByBooking(booking.getId());

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "price", "lastNibble", "draftMode");
		dataset.put("travelClass", choices);
		dataset.put("passengers", passengers);
		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("flights", flightChoices);
		dataset.put("bookingId", booking.getId());

		super.getResponse().addData(dataset);
	}
}

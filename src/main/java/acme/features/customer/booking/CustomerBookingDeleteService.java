
package acme.features.customer.booking;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.SpringHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Bookings.Booking;
import acme.entities.Bookings.BookingRecord;
import acme.entities.Bookings.TravelClass;
import acme.entities.Flight.Flight;
import acme.entities.Legs.LegRepository;
import acme.features.customer.bookingRecord.CustomerBookingRecordRepository;
import acme.realms.Customer;

@GuiService
public class CustomerBookingDeleteService extends AbstractGuiService<Customer, Booking> {
	// Internal state --------------------------------------------------------

	@Autowired
	private CustomerBookingRepository		repository;

	@Autowired
	private CustomerBookingRecordRepository	bookingRecordrepository;

	// AbstractGuiService interfaced -----------------------------------------


	@Override
	public void authorise() {
		int id;
		Booking booking;
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getUserAccount().getId();

		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(id);
		boolean isCustomer = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		boolean status = booking.getCustomer().getUserAccount().getId() == customerId;

		super.getResponse().setAuthorised(status && booking.isDraftMode() && isCustomer);
	}

	@Override
	public void load() {
		Booking booking;
		int id;

		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(id);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {

		super.bindObject(booking, "locatorCode", "purchaseMoment", "price", "lastNibble", "travelClass", "flight");
	}

	@Override
	public void validate(final Booking booking) {
		;

	}

	@Override
	public void perform(final Booking booking) {
		for (BookingRecord bk : this.bookingRecordrepository.findBookingRecordByBookingId(booking.getId()))
			this.bookingRecordrepository.delete(bk);
		this.repository.delete(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices choices;
		SelectChoices flightChoices;

		Date today = MomentHelper.getCurrentMoment();
		Collection<Flight> flights = this.repository.findAllPublishedFlights();
		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);
		Collection<Flight> flightsInFuture = flights.stream().filter(f -> legRepository.findDepartureByFlightId(f.getId()).get(0).after(today)).toList();
		flightChoices = SelectChoices.from(flightsInFuture, "Destination", booking.getFlight());
		choices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		Collection<String> passengers = this.repository.findPassengersNameByBooking(booking.getId());

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "price", "lastNibble", "draftMode");
		dataset.put("travelClass", choices);
		dataset.put("passengers", passengers);
		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("flights", flightChoices);

		super.getResponse().addData(dataset);
	}
}

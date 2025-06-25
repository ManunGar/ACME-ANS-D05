
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
import acme.entities.Flight.FlightRepository;
import acme.entities.Legs.LegRepository;
import acme.realms.Customer;

@GuiService
public class CustomerBookingUpdateService extends AbstractGuiService<Customer, Booking> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository	repository;

	@Autowired
	private FlightRepository			flightRepository;

	@Autowired
	private LegRepository				legRepository;

	// AbstractGuiService interfaced ------------------------------------------


	@Override
	public void authorise() {
		int id;

		boolean isFlightInList = true;
		boolean status = true;
		boolean travelClass = true;
		boolean isIndDraftMode = false;
		boolean isCustomer = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		try {
			Booking booking;
			id = super.getRequest().getData("id", int.class);
			booking = this.repository.findBookingById(id);
			status = booking.getCustomer().getId() == customerId;
			isIndDraftMode = booking.isDraftMode() == true;
			if (booking.isDraftMode() != false && status && isCustomer) {

				Date today = MomentHelper.getCurrentMoment();
				Integer flightId = super.getRequest().getData("flight", int.class);
				Collection<Flight> flights = this.repository.findAllPublishedFlights();
				Collection<Flight> flightsInFuture = flights.stream().filter(f -> this.legRepository.findDepartureByFlightId(f.getId()).get(0).after(today)).toList();
				Flight flight;

				if (flightId != 0) {
					flight = this.flightRepository.findFlightById(flightId);
					isFlightInList = flightsInFuture.contains(flight);
				}

			}

			if (super.getRequest().hasData("travelClass", String.class)) {
				String travelClassData = super.getRequest().getData("travelClass", String.class);
				if (!"0".equals(travelClassData))
					try {
						TravelClass.valueOf(travelClassData);
					} catch (IllegalArgumentException e) {
						travelClass = false;
					}
			}
		} catch (Throwable E) {
			isFlightInList = false;
		}

		super.getResponse().setAuthorised(status && isIndDraftMode && isFlightInList && isCustomer && travelClass);
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

		super.bindObject(booking, "locatorCode", "lastNibble", "travelClass", "flight");
	}

	@Override
	public void validate(final Booking booking) {

		;

	}

	@Override
	public void perform(final Booking booking) {
		this.repository.save(booking);
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

		super.getResponse().addData(dataset);
	}
}

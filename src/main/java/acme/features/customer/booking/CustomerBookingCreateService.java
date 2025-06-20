
package acme.features.customer.booking;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.basis.AbstractRealm;
import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.SpringHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Bookings.Booking;
import acme.entities.Bookings.TravelClass;
import acme.entities.Flight.Flight;
import acme.entities.Flight.FlightRepository;
import acme.entities.Legs.LegRepository;
import acme.realms.Customer;

@GuiService
public class CustomerBookingCreateService extends AbstractGuiService<Customer, Booking> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository	repository;

	@Autowired
	private FlightRepository			flightRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean isCustomer = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		boolean travelClass = true;

		Date today = MomentHelper.getCurrentMoment();
		boolean isFlightInList = true;
		Flight flight;

		try {
			if (super.getRequest().hasData("id")) {
				int flightId = super.getRequest().getData("flight", int.class);
				Collection<Flight> flights = this.repository.findAllPublishedFlights();
				LegRepository legRepository = SpringHelper.getBean(LegRepository.class);
				Collection<Flight> flightsInFuture = flights.stream().filter(f -> legRepository.findDepartureByFlightId(f.getId()).get(0).after(today)).toList();

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

		super.getResponse().setAuthorised(isCustomer && isFlightInList && travelClass);
	}

	@Override
	public void load() {
		Booking booking;
		AbstractRealm principal = super.getRequest().getPrincipal().getActiveRealm();
		int customerId = principal.getId();
		Customer customer = this.repository.findCustomerById(customerId);
		Date today = MomentHelper.getCurrentMoment();

		booking = new Booking();
		booking.setCustomer(customer);
		booking.setPurchaseMoment(today);
		booking.setDraftMode(true);

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

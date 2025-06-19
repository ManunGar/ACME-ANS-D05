
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Bookings.Booking;
import acme.entities.Bookings.BookingRecord;
import acme.entities.Passengers.Passenger;
import acme.features.customer.booking.CustomerBookingRepository;
import acme.features.customer.passenger.CustomerPassengerRepository;
import acme.realms.Customer;

@GuiService
public class CustomerBookingRecordCreateService extends AbstractGuiService<Customer, BookingRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRecordRepository	repository;

	@Autowired
	private CustomerBookingRepository		bookingRepository;

	@Autowired
	private CustomerPassengerRepository		passengerRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean isCustomer = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getUserAccount().getId();
		Collection<Booking> bookings = this.bookingRepository.findBookingByCustomer(customerId);
		Collection<Passenger> passengers = this.repository.findAllPublishedPassengersByCustomerId(customerId);
		boolean isInBookings = true;
		boolean isInPassengers = true;

		try {

			if (super.getRequest().hasData("id")) {
				int bookingId = super.getRequest().getData("booking", int.class);
				if (bookingId != 0) {
					Booking booking = this.bookingRepository.findBookingById(bookingId);
					isInBookings = bookings.contains(booking);
				}

				int passengerId = super.getRequest().getData("passenger", int.class);
				if (passengerId != 0) {
					Passenger passenger = this.passengerRepository.findPassengerById(passengerId);
					isInPassengers = passengers.contains(passenger);
				}

			}
		} catch (Throwable E) {
			isInBookings = false;
			isInPassengers = false;
		}
		super.getResponse().setAuthorised(isCustomer && isInPassengers && isInBookings);
	}

	@Override
	public void load() {
		BookingRecord booking;

		booking = new BookingRecord();

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final BookingRecord bookingRecord) {

		super.bindObject(bookingRecord, "booking", "passenger");

	}

	@Override
	public void validate(final BookingRecord bookingRecord) {

		if (bookingRecord.getBooking() == null && bookingRecord.getPassenger() == null) {
			super.state(false, "booking", "acme.validation.confirmation.message.booking-record.create.booking");
			super.state(false, "passenger", "acme.validation.confirmation.message.booking-record.create.passenger");

		} else if (bookingRecord.getPassenger() == null)
			super.state(false, "passenger", "acme.validation.confirmation.message.booking-record.create.passenger");
		else if (bookingRecord.getBooking() == null)
			super.state(false, "booking", "acme.validation.confirmation.message.booking-record.create.booking");
		else {
			BookingRecord br = this.repository.findBookingRecordBybookingIdpassengerId(bookingRecord.getBooking().getId(), bookingRecord.getPassenger().getId());
			if (br != null)
				super.state(false, "*", "acme.validation.confirmation.message.booking-record.create");
		}

	}

	@Override
	public void perform(final BookingRecord bookingRecord) {
		this.repository.save(bookingRecord);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Dataset dataset;
		SelectChoices passengerChoices;
		SelectChoices bookingChoices;

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getUserAccount().getId();
		Collection<Booking> bookings = this.repository.findAllDraftBookingsByCustomerId(customerId);
		Collection<Passenger> passengers = this.repository.findAllPublishedPassengersByCustomerId(customerId);
		bookingChoices = SelectChoices.from(bookings, "locatorCode", bookingRecord.getBooking());
		passengerChoices = SelectChoices.from(passengers, "fullName", bookingRecord.getPassenger());

		dataset = super.unbindObject(bookingRecord);
		dataset.put("booking", bookingChoices.getSelected().getKey());
		dataset.put("bookings", bookingChoices);
		dataset.put("passenger", passengerChoices.getSelected().getKey());
		dataset.put("passengers", passengerChoices);

		super.getResponse().addData(dataset);
	}
}

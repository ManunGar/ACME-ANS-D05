
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
public class CustomerBookingRecordDeleteService extends AbstractGuiService<Customer, BookingRecord> {
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
		Collection<Passenger> passengers = this.repository.findAllPassengersByCustomerId(customerId);

		boolean status = true;
		boolean isInPassengers = true;
		boolean validLocatorCode = true;

		try {
			int id = super.getRequest().getData("bookingId", int.class);
			Booking booking = this.bookingRepository.findBookingById(id);
			status = booking.getCustomer().getUserAccount().getId() == customerId;
			if (super.getRequest().hasData("id")) {
				String locatorCode = super.getRequest().getData("locatorCode", String.class);
				validLocatorCode = locatorCode.equals(booking.getLocatorCode());
				int passengerId = super.getRequest().getData("passenger", int.class);
				if (passengerId != 0) {
					Passenger passenger = this.passengerRepository.findPassengerById(passengerId);
					isInPassengers = passengers.contains(passenger);
				}

			}
		} catch (Throwable E) {
			isInPassengers = false;
		}
		super.getResponse().setAuthorised(isCustomer && isInPassengers && status && validLocatorCode);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.bookingRepository.findBookingById(id);
		BookingRecord bookingRecord = new BookingRecord();
		bookingRecord.setBooking(booking);

		super.getBuffer().addData(bookingRecord);
	}

	@Override
	public void bind(final BookingRecord bookingRecord) {

		super.bindObject(bookingRecord, "passenger");

	}

	@Override
	public void validate(final BookingRecord bookingRecord) {

		if (bookingRecord.getPassenger() == null)
			super.state(false, "passenger", "acme.validation.confirmation.message.booking-record.create.passenger");
	}

	@Override
	public void perform(final BookingRecord bookingRecord) {
		BookingRecord br = this.repository.findBookingRecordBybookingIdpassengerId(bookingRecord.getBooking().getId(), bookingRecord.getPassenger().getId());
		this.repository.delete(br);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Dataset dataset;
		SelectChoices passengerChoices;

		int id = super.getRequest().getData("bookingId", int.class);

		Collection<Passenger> passengers = this.repository.findAllPassengersByBookingId(id);
		passengerChoices = SelectChoices.from(passengers, "fullName", bookingRecord.getPassenger());

		dataset = super.unbindObject(bookingRecord);
		dataset.put("passenger", passengerChoices.getSelected().getKey());
		dataset.put("passengers", passengerChoices);
		dataset.put("bookingId", id);
		dataset.put("locatorCode", bookingRecord.getBooking().getLocatorCode());

		super.getResponse().addData(dataset);
	}
}

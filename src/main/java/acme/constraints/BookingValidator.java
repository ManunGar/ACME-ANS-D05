
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.Bookings.Booking;
import acme.features.customer.booking.CustomerBookingRepository;

@Validator
public class BookingValidator extends AbstractValidator<ValidBooking, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public boolean isValid(final Booking booking, final ConstraintValidatorContext context) {
		boolean result;

		Booking b = this.repository.findBookingByLocatorCode(booking.getLocatorCode());
		if (b != null && b.getId() != booking.getId())
			super.state(context, false, "locatorCode", "acme.validation.confirmation.message.booking.locator-code");

		result = !super.hasErrors(context);

		return result;
	}

}

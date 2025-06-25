
package acme.entities.Bookings;

import java.beans.Transient;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidBooking;
import acme.entities.Flight.Flight;
import acme.entities.Flight.FlightRepository;
import acme.entities.Passengers.Passenger;
import acme.realms.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidBooking
@Table(indexes = {
	@Index(columnList = "draftMode"), @Index(columnList = "locatorCode")
})
public class Booking extends AbstractEntity {

	//Serialisation version 

	private static final long	serialVersionUID	= 1L;

	//Relationships

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Customer			customer;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight				flight;

	//Attributes

	@Mandatory
	@ValidString(pattern = "^[A-Z0-9]{6,8}$", message = "{acme.validation.confirmation.message.booking.locator-code.pattern}")
	@Column(unique = true)
	private String				locatorCode;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				purchaseMoment;

	@Mandatory
	@Valid
	@Automapped
	private TravelClass			travelClass;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	@Optional
	@ValidString(pattern = "^\\d{4}$", message = "{acme.validation.text.message.patternLastNibble}")
	@Automapped
	private String				lastNibble;


	@Transient
	public Money getPrice() {
		Money result;
		FlightRepository flightRepository = SpringHelper.getBean(FlightRepository.class);
		BookingRepository bookingRepository = SpringHelper.getBean(BookingRepository.class);
		if (this.getFlight() == null) {
			Money withoutResult = new Money();
			withoutResult.setAmount(0.0);
			withoutResult.setCurrency("EUR");
			return withoutResult;
		} else {
			result = flightRepository.findCostByFlight(this.flight.getId());
			Collection<Passenger> pg = bookingRepository.findPassengersByBooking(this.getId());
			Double amount = result.getAmount() * pg.size();
			result.setAmount(amount);
			return result;
		}

	}

}

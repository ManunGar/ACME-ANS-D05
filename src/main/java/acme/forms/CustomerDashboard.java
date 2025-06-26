
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.client.components.datatypes.Money;
import acme.entities.Bookings.TravelClass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	List<String>				lastFiveDestinations;
	Money						moneySpentInBookingsLastYear;
	Map<TravelClass, Integer>	numberOfBookingsByTravelClass;
	List<Statictics>			bookingStatsLastFiveYears;
	Statictics					passengerStatsInBooking;

}

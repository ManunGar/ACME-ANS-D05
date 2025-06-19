
package acme.constraints;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.Legs.LegRepository;
import acme.entities.Legs.Legs;

public class LegsValidator extends AbstractValidator<ValidLegs, Legs> {

	@Autowired
	private LegRepository repository;


	@Override
	public boolean isValid(final Legs leg, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (leg == null)
			super.state(context, false, "*", "acme.validation.NotNull.message");
		/*
		 * else {
		 * boolean correctLeg = true;
		 * List<Legs> legs = this.repository.findAllByFlightId(leg.getFlight().getId());
		 * if (legs.size() > 0)
		 * legs.removeIf(l -> l.getId() == leg.getId());
		 * legs.add(leg);
		 * legs = LegsValidator.sortLegsByDeparture(legs);
		 * for (int i = 0; i < legs.size() - 1 && correctLeg && legs.size() >= 2; i++) {
		 * if (legs.get(i).getArrival().after(legs.get(i + 1).getDeparture()))
		 * correctLeg = false;
		 * super.state(context, correctLeg, "departure", "acme.validation.legs.message");
		 * if (!legs.get(i).getArrivalAirport().getCity().equals(legs.get(i + 1).getDepartureAirport().getCity()))
		 * correctLeg = false;
		 * super.state(context, correctLeg, "departureAirport", "acme.validation.legs.message");
		 * }
		 * 
		 * }
		 */
		result = !super.hasErrors(context);

		return result;
	}

	public static List<Legs> sortLegsByDeparture(final List<Legs> legs) {
		List<Legs> sortedLegs = new ArrayList<>(legs);
		sortedLegs.sort(Comparator.comparing(Legs::getDeparture));
		return sortedLegs;
	}
}

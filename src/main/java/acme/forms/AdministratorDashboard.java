
package acme.forms;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministratorDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	Integer						totalNumberOfAirportsByOperationalScope;
	Integer						numberOfAirlinesByType;
	Double						airlinesWithBothEmailAndPhone;
	Double						activeAircrafts;
	Double						nonActiveAircrafts;
	Double						reviewsWithScoreAboveFive;
	Integer						numberOfReviewsLastTenWeeks;
	Double						averageReviewsLastTenWeeks;
	Double						minimumReviewsLastTenWeeks;
	Double						maximumReviewsLastTenWeeks;
	Double						standardDeviationLastTenWeeks;

}

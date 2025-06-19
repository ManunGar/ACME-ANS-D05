
package acme.forms;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssistanceAgentDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	Double						ratioOfClaimsResolvedSuccesfully;
	Double						ratioOfClaimsRejected;
	Double						topThreeMonthsWithHighestNumberOfClaims;
	Double						averageDeviationOfLogsTheirClaimsHave;
	Double						minimumDeviationOfLogsTheirClaimsHave;
	Double						maximumDeviationOfLogsTheirClaimsHave;
	Double						standardDeviationOfLogsTheirClaimsHave;
	Double						averageNumberOfClaimsTheyAssistedTheLastMonth;
	Double						minimumNumberOfClaimsTheyAssistedTheLastMonth;
	Double						maximumNumberOfClaimsTheyAssistedTheLastMonth;
	Double						standardNumberOfClaimsTheyAssistedTheLastMonth;

}

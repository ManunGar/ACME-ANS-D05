
package acme.forms;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightCrewMemberDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	// Attributes

	String[]					lastFiveDestinations;
	Integer						numberOfLegsWithIncidentsSeverity0to3;
	Integer						numberOfLegsWithIncidentsSeverity4to7;
	Integer						numberOfLegsWithIncidentsSeverity8to10;
	String[]					crewMembersLastLeg;
	Integer						countOfAssignmentsPending;
	Integer						countOfAssignmentsConfirmed;
	Integer						countOfAssignmentsCancelled;
	Double						averageAssignmentsLastMonth;
	Double						minimumAssignmentsLastMonth;
	Double						maximumAssignmentsLastMonth;
	Double						standardDeviationAssignmentsLastMonth;

}

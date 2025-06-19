
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnicianDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	Map<String, Integer>		maintenanceRecordsByStatus;
	Integer						maintenanceRecordWithNearestInspectionDue;

	List<String>				topFiveAircraftsWithMostTasks;

	Double						averageEstimatedCostLastYear;
	Double						minimumEstimatedCostLastYear;
	Double						maximumEstimatedCostLastYear;
	Double						standardDeviationEstimatedCostLastYear;

	Double						averageEstimatedDurationOfTasks;
	Double						minimumEstimatedDurationOfTasks;
	Double						maximumEstimatedDurationOfTasks;
	Double						standardDeviationEstimatedDurationOfTasks;
}

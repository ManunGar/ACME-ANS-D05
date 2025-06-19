
package acme.forms;

import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.entities.Airports.Airport;
import acme.entities.Legs.LegsStatus;

public class ManagerDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	Integer						rankingExperience;
	Integer						yearsToRetire;
	Double						ratioOnTimeLegs;
	Double						ratioDelayedLegs;
	Airport						mostPopularAirport;
	Airport						lessPopularAirport;
	Map<LegsStatus, Integer>	numberOfLegs;
	Map<Double, String>			averageCostFlights;
	Map<Double, String>			minimumCostFlights;
	Map<Double, String>			maximumCostFlights;
	Map<Double, String>			standardDeviationCostFlights;
}

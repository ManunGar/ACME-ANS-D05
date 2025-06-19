
package acme.entities.Flight;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.client.components.datatypes.Money;
import acme.client.repositories.AbstractRepository;

public interface FlightRepository extends AbstractRepository {

	@Query("select f.cost from Flight f where f.id = :flightId")
	Money findCostByFlight(@Param("flightId") Integer flightId);

	@Query("select f from Flight f where f.manager.id = :managerId")
	Collection<Flight> findFlightsByAirlineManagerId(@Param("managerId") Integer managerId);

	@Query("select f from Flight f")
	Collection<Flight> findAllFlight();

	@Query("select f from Flight f where f.id = :flightId")
	Flight findFlightById(@Param("flightId") Integer flightId);

	@Query("select l.departure from Legs l where l.flight.id = :flightId order by l.departure")
	List<Date> findDepartureByFlightId(@Param("flightId") Integer flightId);
}

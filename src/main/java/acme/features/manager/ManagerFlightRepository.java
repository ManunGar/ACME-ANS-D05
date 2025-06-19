
package acme.features.manager;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.ActivityLogs.ActivityLog;
import acme.entities.Flight.Flight;

@Repository
public interface ManagerFlightRepository extends AbstractRepository {

	@Query("select f from Flight f where f.manager.userAccount.id = :managerId")
	Collection<Flight> findAllByManagerId(@Param("managerId") Integer managerId);

	@Query("select f from Flight f")
	Collection<Flight> findAllFlight();

	@Query("select f from Flight f where f.id = :flightId")
	Flight findOne(@Param("flightId") Integer flightId);

	@Query("select al from ActivityLog al where al.flightAssignment.id in (select fa.id from FlightAssignment fa where fa.leg.id in (select l.id from Legs l where l.flight.id = :flightId))")
	Collection<ActivityLog> findActivityLogsByFlightId(int flightId);
}

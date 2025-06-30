
package acme.features.manager.legs;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Legs.Legs;

@Repository
public interface ManagerLegsRepository extends AbstractRepository {

	@Query("select l from Legs l")
	Collection<Legs> findAll(@Param("flightId") Integer FlightId);

	@Query("select l from Legs l where l.flight.id = :flightId")
	Collection<Legs> findAllByFlightId(@Param("flightId") Integer flightId);

	@Query("select l from Legs l where l.arrival >= :start and l.departure <= :end and l.aircraft.id = :aircraftId and l.flight.manager.airline.id = :airlineId")
	Collection<Legs> findAllOverlappingIntervalAndAirline(@Param("start") Date start, @Param("end") Date end, @Param("aircraftId") Integer aircraftId, @Param("airlineId") Integer airlineId);

	@Query("select l from Legs l where l.id = :legId")
	Legs findLegById(@Param("legId") Integer legId);

	@Query("select l from Legs l where l.flightNumber = :flightNumber")
	Legs findLegByFlightNumber(@Param("flightNumber") String flightNumber);

}

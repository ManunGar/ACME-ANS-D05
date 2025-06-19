
package acme.entities.Legs;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.client.repositories.AbstractRepository;

public interface LegRepository extends AbstractRepository {

	@Query("select t from Legs t where t.flight.id = :flightId")
	List<Legs> findAllByFlightId(@Param("flightId") Integer flightId);

	@Query("select l.departure from Legs l where l.flight.id = :flightId order by l.departure")
	List<Date> findDepartureByFlightId(@Param("flightId") Integer flightId);

	@Query("select l.arrival from Legs l where l.flight.id = :flightId order by l.departure desc")
	List<Date> findArrivalByFlightId(@Param("flightId") Integer flightId);

	@Query("select l.departureAirport.city from Legs l where l.flight.id = :flightId order by l.departure")
	List<String> findOriginCityByFlightId(@Param("flightId") Integer flightId);

	@Query("select l.arrivalAirport.city from Legs l where l.flight.id = :flightId order by l.departure desc")
	List<String> findDestinationCityByFlightId(@Param("flightId") Integer flightId);

	@Query("select count(l) from Legs l where l.flight.id = :flightId")
	Integer numberOfLayours(Integer flightId);
}

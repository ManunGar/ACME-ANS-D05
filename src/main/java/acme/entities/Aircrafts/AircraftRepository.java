
package acme.entities.Aircrafts;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.client.repositories.AbstractRepository;

public interface AircraftRepository extends AbstractRepository {

	@Query("select a from Aircraft a WHERE a.airline.id = :airlineId")
	List<Aircraft> findAllAircarftByAirlineId(@Param("airlineId") Integer airlineId);

	@Query("select a from Aircraft a where a.id = :aircraftId")
	Aircraft findAircraftById(@Param("aircraftId") Integer aircraftId);
}

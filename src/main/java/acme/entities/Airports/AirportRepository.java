
package acme.entities.Airports;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.client.repositories.AbstractRepository;

public interface AirportRepository extends AbstractRepository {

	@Query("select a from Airport a")
	List<Airport> findAllAirport();

	@Query("select a from Airport a where a.id = :airportId")
	Airport findAirportById(@Param("airportId") Integer airportId);
}

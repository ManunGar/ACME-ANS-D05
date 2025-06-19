
package acme.entities.Airlines;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.client.repositories.AbstractRepository;

public interface AirlineRepository extends AbstractRepository {

	@Query("SELECT t FROM Airline t WHERE t.id = :airlineId")
	Airline findAirlineById(@Param("airlineId") Integer airlineId);

}

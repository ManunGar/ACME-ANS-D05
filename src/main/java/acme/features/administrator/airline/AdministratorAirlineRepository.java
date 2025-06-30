
package acme.features.administrator.airline;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Airlines.Airline;

@Repository
public interface AdministratorAirlineRepository extends AbstractRepository {

	@Query("select a from Airline a")
	Collection<Airline> findAllAirlines();

	@Query("select a from Airline a where a.id = :id")
	Airline findAirlineById(int id);

	@Query("select a from Airline a WHERE a.IATAcode = :IATAcode")
	Airline findAirlineByIataCode(@Param("IATAcode") String IATAcode);

}

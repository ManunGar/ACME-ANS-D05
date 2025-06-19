
package acme.features.administrator.airport;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Airports.Airport;

@Repository
public interface AdministratorAirportRepository extends AbstractRepository {

	@Query("select a from Airport a")
	Collection<Airport> findAllAirport();

	@Query("select a from Airport a where a.id = :id")
	Airport findAirportById(int id);

	@Query("select a from Airport a WHERE a.IATAcode = :IATAcode")
	Airport findAirportByIataCode(@Param("IATAcode") String IATAcode);

	@Query("select a from Airport a WHERE a.email = :email")
	Airport findAirportByEmail(@Param("email") String email);
}

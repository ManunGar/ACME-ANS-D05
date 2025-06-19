
package acme.features.authenticated.assistanceAgent;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.entities.Airlines.Airline;
import acme.realms.Customer;
import acme.realms.AssistanceAgent.AssistanceAgent;

@Repository
public interface AuthenticatedAssistanceAgentRepository extends AbstractRepository {

	@Query("select ua from UserAccount ua where ua.id = :id")
	UserAccount findUserAccountById(int id);

	@Query("select a from AssistanceAgent a where a.userAccount.id = :id")
	Customer findCustomerByUserAccountId(int id);

	@Query("select a from AssistanceAgent a where a.employeeCode = :employeeCode")
	Customer findCustomerByIdentifier(String employeeCode);

	@Query("select ar from Airline ar where ar.id = :id")
	Airline findAirlineById(int id);

	@Query("select ar from Airline ar")
	List<Airline> findAllAirlines();

	@Query("select a from AssistanceAgent a where a.userAccount.id = :id")
	AssistanceAgent findOneAssistanceAgentByUserAccountId(int id);
}

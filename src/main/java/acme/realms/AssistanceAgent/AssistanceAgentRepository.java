
package acme.realms.AssistanceAgent;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface AssistanceAgentRepository extends AbstractRepository {

	@Query("SELECT COUNT(a) > 0 FROM AssistanceAgent a WHERE a.employeeCode = :employeeCode AND a.id <> :id")
	boolean existsOtherWithEmployeeCode(String employeeCode, int id);

}


package acme.features.administrator.claim;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Claims.Claim;
import acme.entities.Legs.Legs;
import acme.entities.TrackingLogs.TrackingLog;
import acme.realms.AssistanceAgent.AssistanceAgent;

@Repository
public interface AdministratorClaimRepository extends AbstractRepository {

	@Query("select j from Claim j where j.id = :claimId")
	Claim findClaimById(int claimId);

	@Query("select j from Claim j where j.draftMode = false")
	Collection<Claim> findClaimsPublished();

	@Query("select l from Legs l WHERE l.arrival < :currentMoment AND l.draftMode = false")
	Collection<Legs> findAvailableLegs(@Param("currentMoment") Date currentMoment);

	@Query("select l from Legs l where l.id = :legId")
	Legs findLegById(int legId);

	@Query("select c.leg from Claim c where c.id = :claimId")
	Legs findLegByClaimId(int claimId);

	@Query("select a from AssistanceAgent a where a.id = :agentId")
	AssistanceAgent findAgentById(int agentId);

	@Query("select t from TrackingLog t where t.claim.id = :claimId")
	Collection<TrackingLog> findTrackingLogsByClaimId(int claimId);

}

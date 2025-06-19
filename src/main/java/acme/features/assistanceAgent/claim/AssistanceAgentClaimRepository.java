
package acme.features.assistanceAgent.claim;

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
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("select j from Claim j where j.id = :claimId")
	Claim findClaimById(int claimId);

	@Query("select j from Claim j where j.assistanceAgent.id = :assistanceAgentId")
	Collection<Claim> findClaimsByAssistanceAgentId(int assistanceAgentId);

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

	@Query("""
			SELECT COUNT(c) > 0
			FROM Claim c
			WHERE c.id = :claimId
			AND c.assistanceAgent.id = :agentId
			AND c.draftMode = true
		""")
	boolean isDraftClaimOwnedByAgent(@Param("claimId") int claimId, @Param("agentId") int agentId);

	@Query("""
			SELECT COUNT(c) > 0
			FROM Claim c
			WHERE c.id = :claimId
			AND c.assistanceAgent.id = :agentId
		""")
	boolean isClaimOwnedByAgent(@Param("claimId") int claimId, @Param("agentId") int agentId);

	@Query("""
			SELECT c
			FROM Claim c
			WHERE c.assistanceAgent.id = :agentId
			AND NOT EXISTS (
				SELECT t
				FROM TrackingLog t
				WHERE t.claim.id = c.id
				AND t.draftMode = false
				AND t.accepted <> acme.entities.Claims.AcceptedIndicator.PENDING
			)
		""")
	Collection<Claim> findNotResolvedClaimsByAssistanceAgentId(@Param("agentId") int agentId);

	@Query("""
			SELECT c
			FROM Claim c
			WHERE c.assistanceAgent.id = :agentId
			AND EXISTS (
				SELECT t
				FROM TrackingLog t
				WHERE t.claim.id = c.id
				AND t.draftMode = false
				AND t.accepted <> acme.entities.Claims.AcceptedIndicator.PENDING
			)
		""")
	Collection<Claim> findResolvedClaimsByAssistanceAgentId(@Param("agentId") int agentId);

}


package acme.features.assistanceAgent.TrackingLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Claims.Claim;
import acme.entities.TrackingLogs.TrackingLog;

@Repository
public interface AssistanceAgentTrackingLogRepository extends AbstractRepository {

	@Query("select j from Claim j where j.assistanceAgent.id = :assistanceAgentId")
	Collection<Claim> findClaimsByAssistanceAgentId(int assistanceAgentId);

	@Query("select tl from TrackingLog tl where tl.claim.id = :claimId")
	Collection<TrackingLog> findAllTrackingLogsByclaimId(int claimId);

	@Query("select tl.claim from TrackingLog tl where tl.id = :trackingLogId")
	Claim findClaimByTrackingLogId(int trackingLogId);

	@Query("select tl from TrackingLog tl where tl.id = :trackingLogId")
	TrackingLog findTrackingLogById(int trackingLogId);

	@Query("select c from Claim c where c.id = :claimId")
	Claim findClaimById(int claimId);

	@Query("""
			SELECT COUNT(t) > 0
			FROM TrackingLog t
			WHERE t.claim.id = :claimId
			AND t.resolutionPercentage = 100.0
			AND t.draftMode = false
		""")
	boolean existsPublishedFullResolutionTrackingLog(@Param("claimId") int claimId);

	@Query("""
			SELECT COUNT(t) > 0
			FROM TrackingLog t
			WHERE t.id = :trackingLogId
			AND t.draftMode = true
			AND t.claim.assistanceAgent.id = :agentId
		""")
	boolean isDraftTrackingLogOwnedByAgent(@Param("trackingLogId") int trackingLogId, @Param("agentId") int agentId);

	@Query("""
			SELECT COUNT(c) > 0
			FROM Claim c
			WHERE c.id = :claimId
			AND c.assistanceAgent.id = :agentId
		""")
	boolean isClaimOwnedByAgent(@Param("claimId") int claimId, @Param("agentId") int agentId);

	@Query("""
			SELECT COUNT(t) > 0
			FROM TrackingLog t
			WHERE t.id = :trackingLogId
			AND t.draftMode = true
			AND t.claim.draftMode = false
			AND t.claim.assistanceAgent.id = :agentId
		""")
	boolean isPublishableTrackingLogOwnedByAgent(@Param("trackingLogId") int trackingLogId, @Param("agentId") int agentId);

}

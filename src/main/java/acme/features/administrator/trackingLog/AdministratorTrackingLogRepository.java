
package acme.features.administrator.trackingLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Claims.Claim;
import acme.entities.TrackingLogs.TrackingLog;

@Repository
public interface AdministratorTrackingLogRepository extends AbstractRepository {

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

}

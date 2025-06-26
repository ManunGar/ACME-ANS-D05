
package acme.entities.TrackingLogs;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface TrackingLogRepository extends AbstractRepository {

	@Query("select tl from TrackingLog tl where tl.claim.id = :claimId order by tl.resolutionPercentage desc, tl.createdMoment desc")
	Collection<TrackingLog> findTrackingLogsOrderedByCreatedMoment(int claimId);

	@Query("select tl from TrackingLog tl where tl.id = :trackingLogId")
	TrackingLog findTrackingLogById(int trackingLogId);

}

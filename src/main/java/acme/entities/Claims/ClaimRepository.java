
package acme.entities.Claims;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.client.repositories.AbstractRepository;

public interface ClaimRepository extends AbstractRepository {

	@Query("""
		    SELECT COUNT(t)
		    FROM TrackingLog t
		    WHERE t.claim.id = :claimId
		    AND t.resolutionPercentage = 100.0
		    AND t.id <> :excludeId
		""")
	int countOtherCompletedTrackingLogs(int claimId, int excludeId);

	@Query("""
		    SELECT t.indicator
		    FROM TrackingLog t
		    WHERE t.claim.id = :claimId AND t.draftMode = false
		    ORDER BY t.resolutionPercentage DESC, t.createdMoment DESC
		""")
	List<AcceptedIndicator> findAcceptedIndicatorsByClaimId(@Param("claimId") Integer claimId, Pageable pageable);

}


package acme.constraints;

import java.util.Collection;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.Claims.AcceptedIndicator;
import acme.entities.Claims.ClaimRepository;
import acme.entities.TrackingLogs.TrackingLog;
import acme.entities.TrackingLogs.TrackingLogRepository;

@Validator
public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	@Autowired
	private ClaimRepository			claimRepository;

	@Autowired
	private TrackingLogRepository	trackingLogRepository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidTrackingLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {
		// HINT: job can be null
		assert context != null;

		boolean result;

		if (trackingLog == null)
			super.state(context, false, "*", "acme.validation.NotNull.message");
		else {

			// Validation for attribute resolution in relation to resolutionPercentage
			{
				boolean resolutionValid;

				if (trackingLog.getResolutionPercentage() == 100.00)
					resolutionValid = trackingLog.validResolution();
				else
					resolutionValid = trackingLog.getResolution() == null || trackingLog.getResolution().trim().isEmpty();

				super.state(context, resolutionValid, "resolution", "acme.validation.trackinLog.resolutionMandatory.message");
			}

			//Validation for attribute accepted is logical with resolutionPercentage
			{
				if (trackingLog.getIndicator() != null) {
					boolean acceptedPending;
					boolean isPending = trackingLog.getIndicator().equals(AcceptedIndicator.PENDING);
					boolean isComplete = trackingLog.getResolutionPercentage() == 100.0;

					acceptedPending = !isComplete && isPending || isComplete && !isPending;

					super.state(context, acceptedPending, "indicator", "acme.validation.trackingLog.acceptedPending.message");
				}
			}

			//Validation of the maximum number of trackingLogs with resolutionPercentage == 100.
			{
				if (trackingLog.getClaim() != null) {
					boolean maximumNumberOfTrackingLogsCompleted;
					int completedCount = this.claimRepository.countOtherCompletedTrackingLogs(trackingLog.getClaim().getId(), trackingLog.getId());
					maximumNumberOfTrackingLogsCompleted = trackingLog.getResolutionPercentage() != 100.00 || completedCount <= 1;

					super.state(context, maximumNumberOfTrackingLogsCompleted, "claim", "acme.validation.trackingLog.numberOfTrackingLogsCompleted.message");
				}

			}

			//Validation of both trackingLogs with resolutionPercentage == 100. have the same indicator
			{
				if (trackingLog.getClaim() != null && trackingLog.getResolutionPercentage() == 100.) {
					boolean sameIndicatorForTrackingLogs;

					List<TrackingLog> completedTrackingLogs = this.trackingLogRepository.findTrackingLogsOfClaimResolved(trackingLog.getClaim().getId(), trackingLog.getId()).stream().toList();
					sameIndicatorForTrackingLogs = completedTrackingLogs.size() < 1 || completedTrackingLogs.get(0).getIndicator().equals(trackingLog.getIndicator());

					super.state(context, sameIndicatorForTrackingLogs, "indicator", "acme.validation.trackingLog.completedTrackingLogsWithSameIndicator.message");
				}
			}

			//Validation claim of this trackingLog is published
			{
				if (trackingLog.getClaim() != null) {
					boolean claimIsPublished;

					claimIsPublished = !trackingLog.getClaim().isDraftMode();

					super.state(context, claimIsPublished, "*", "acme.validation.trackingLog.draftModeLogical.message");
				}

			}

			//Validation of attribute resolutionPercentage is always higher than the last created and the last trackingLog is published
			{
				if (trackingLog.getClaim() != null) {

					boolean resolutionPercentageHigher;
					boolean lastTrackingLogPublished;

					TrackingLog existingTrackingLog = this.trackingLogRepository.findTrackingLogById(trackingLog.getId());

					Collection<TrackingLog> trackingLogs = this.trackingLogRepository.findTrackingLogsOrderedByCreatedMoment(trackingLog.getClaim().getId());
					List<TrackingLog> listTrackingLogs = trackingLogs.stream().toList();

					if (existingTrackingLog == null) { //Create
						TrackingLog lastTrackingLog = listTrackingLogs.size() > 0 ? listTrackingLogs.get(0) : null;
						if (trackingLog.getResolutionPercentage() == 100.) {
							resolutionPercentageHigher = lastTrackingLog == null || lastTrackingLog.getResolutionPercentage() <= trackingLog.getResolutionPercentage();
							lastTrackingLogPublished = lastTrackingLog == null || !lastTrackingLog.isDraftMode();
						}

						else {
							resolutionPercentageHigher = lastTrackingLog == null || lastTrackingLog.getResolutionPercentage() < trackingLog.getResolutionPercentage();
							lastTrackingLogPublished = lastTrackingLog == null || !lastTrackingLog.isDraftMode();
						}

					} else { //Update
						int indexOfCurrentTrackingLog = listTrackingLogs.indexOf(trackingLog);

						TrackingLog previousTrackingLog = indexOfCurrentTrackingLog + 1 < listTrackingLogs.size() ? listTrackingLogs.get(indexOfCurrentTrackingLog + 1) : null;

						if (trackingLog.getResolutionPercentage() == 100.) {
							resolutionPercentageHigher = previousTrackingLog == null || previousTrackingLog.getResolutionPercentage() <= trackingLog.getResolutionPercentage();
							lastTrackingLogPublished = previousTrackingLog == null || !previousTrackingLog.isDraftMode();
						} else {
							resolutionPercentageHigher = previousTrackingLog == null || previousTrackingLog.getResolutionPercentage() < trackingLog.getResolutionPercentage();
							lastTrackingLogPublished = previousTrackingLog == null || !previousTrackingLog.isDraftMode();
						}

					}

					super.state(context, resolutionPercentageHigher, "resolutionPercentage", "acme.validation.trackingLog.resolutionPercentage.message");
					super.state(context, lastTrackingLogPublished, "claim", "acme.validation.trackingLog.lastTrackingLogPublished.message");
				}
			}

		}

		result = !super.hasErrors(context);

		return result;
	}

}

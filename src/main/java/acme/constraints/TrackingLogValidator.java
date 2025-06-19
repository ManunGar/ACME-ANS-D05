
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
				if (trackingLog.getAccepted() != null) {
					boolean acceptedPending;
					boolean isPending = trackingLog.getAccepted().equals(AcceptedIndicator.PENDING);
					boolean isComplete = trackingLog.getResolutionPercentage() == 100.0;

					acceptedPending = !isComplete && isPending || isComplete && !isPending;

					super.state(context, acceptedPending, "accepted", "acme.validation.trackingLog.acceptedPending.message");
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

			//Validation of attribute draftMode is logical with its claim
			{
				if (trackingLog.getClaim() != null) {
					boolean draftModeLogical;

					draftModeLogical = trackingLog.isDraftMode() || !trackingLog.getClaim().isDraftMode();

					super.state(context, draftModeLogical, "draftMode", "acme.validation.trackingLog.draftModeLogical.message");
				}

			}

			//Validation of attribute resolutionPercentage is always higher than the last created and lower than the next created
			{
				if (trackingLog.getClaim() != null) {

					boolean resolutionPercentageHigher;

					TrackingLog existingTrackingLog = this.trackingLogRepository.findTrackingLogById(trackingLog.getId());

					Collection<TrackingLog> trackingLogs = this.trackingLogRepository.findTrackingLogsOrderedByCreatedMoment(trackingLog.getClaim().getId());
					List<TrackingLog> listTrackingLogs = trackingLogs.stream().toList();

					if (existingTrackingLog == null) { // Create
						TrackingLog lastTrackingLog = listTrackingLogs.size() > 0 ? listTrackingLogs.get(0) : null;
						resolutionPercentageHigher = lastTrackingLog == null || lastTrackingLog.getResolutionPercentage() <= trackingLog.getResolutionPercentage();

					} else { // Update
						int indexOfCurrentTrackingLog = listTrackingLogs.indexOf(trackingLog);

						TrackingLog previousTrackingLog = indexOfCurrentTrackingLog + 1 < listTrackingLogs.size() ? listTrackingLogs.get(indexOfCurrentTrackingLog + 1) : null;
						TrackingLog nextTrackingLog = indexOfCurrentTrackingLog - 1 >= 0 ? listTrackingLogs.get(indexOfCurrentTrackingLog - 1) : null;

						boolean higherOrEqualThanPrevious = previousTrackingLog == null || previousTrackingLog.getResolutionPercentage() <= trackingLog.getResolutionPercentage();
						boolean lowerOrEqualThanNext = nextTrackingLog == null || trackingLog.getResolutionPercentage() <= nextTrackingLog.getResolutionPercentage();

						resolutionPercentageHigher = higherOrEqualThanPrevious && lowerOrEqualThanNext;
					}

					super.state(context, resolutionPercentageHigher, "resolutionPercentage", "acme.validation.trackingLog.resolutionPercentage.message");
				}
			}

			//Validation that if there are 2 trackingLogs with resolutionPercentage = 100.00, one of them has secondTrackingLog = true

			{
				if (trackingLog.getClaim() != null) {
					List<Boolean> otherSecondStatuses = this.claimRepository.findOtherSecondTrackingStatus(trackingLog.getClaim().getId(), trackingLog.getId());

					boolean attributeSecondTrackingLog = true;

					if (trackingLog.getResolutionPercentage() == 100.00)
						if (otherSecondStatuses.size() == 1) {
							boolean other = otherSecondStatuses.get(0);
							attributeSecondTrackingLog = other ^ trackingLog.isSecondTrackingLog();
						} else
							attributeSecondTrackingLog = !trackingLog.isSecondTrackingLog();

					super.state(context, attributeSecondTrackingLog, "secondTrackingLog", "acme.validation.trackingLog.secondTrackingLog.numberOfTrackingLogs.message");

				}

			}

			// Validation: secondTrackingLog can only be true if resolutionPercentage == 100.00
			{
				if (trackingLog.getClaim() != null) {
					boolean secondTrackingLogValid;

					secondTrackingLogValid = !trackingLog.isSecondTrackingLog() || trackingLog.getResolutionPercentage() == 100.00;

					super.state(context, secondTrackingLogValid, "secondTrackingLog", "acme.validation.trackingLog.secondTrackingLog.resolutionPercentage.message");
				}
			}

		}

		result = !super.hasErrors(context);

		return result;
	}

}

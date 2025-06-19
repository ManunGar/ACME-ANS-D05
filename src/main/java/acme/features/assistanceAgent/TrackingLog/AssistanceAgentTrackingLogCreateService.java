
package acme.features.assistanceAgent.TrackingLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Claims.AcceptedIndicator;
import acme.entities.Claims.Claim;
import acme.entities.TrackingLogs.TrackingLog;
import acme.realms.AssistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogCreateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean status = true;
		if (super.getRequest().hasData("accepted", String.class)) {
			String accepted = super.getRequest().getData("accepted", String.class);

			if (!"0".equals(accepted))
				try {
					AcceptedIndicator.valueOf(accepted);
				} catch (IllegalArgumentException | NullPointerException e) {
					status = false;
				}
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrackingLog trackingLog;

		Integer claimId = super.getRequest().getData("masterId", int.class);
		Claim claim = this.repository.findClaimById(claimId);

		trackingLog = new TrackingLog();
		trackingLog.setAccepted(AcceptedIndicator.PENDING);
		trackingLog.setDraftMode(true);
		trackingLog.setResolutionPercentage(0.);
		trackingLog.setSecondTrackingLog(false);
		trackingLog.setClaim(claim);

		super.getBuffer().addData(trackingLog);

	}

	@Override
	public void bind(final TrackingLog trackingLog) {

		super.bindObject(trackingLog, "step", "resolutionPercentage", "resolution", "accepted", "secondTrackingLog");
		trackingLog.setLastUpdateMoment(MomentHelper.getCurrentMoment());
		trackingLog.setCreatedMoment(MomentHelper.getCurrentMoment());

	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		if (trackingLog.getResolutionPercentage() == 100.00 && trackingLog.isSecondTrackingLog()) {
			int claimId = super.getRequest().getData("masterId", int.class);
			boolean hasAnotherCompleted = this.repository.existsPublishedFullResolutionTrackingLog(claimId);
			super.state(hasAnotherCompleted, "secondTrackingLog", "acme.validation.confirmation.message.trackingLog.condition");
		}
	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		trackingLog.setLastUpdateMoment(MomentHelper.getCurrentMoment());
		trackingLog.setCreatedMoment(MomentHelper.getCurrentMoment());

		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		SelectChoices statusChoices;
		Dataset dataset;

		statusChoices = SelectChoices.from(AcceptedIndicator.class, trackingLog.getAccepted());

		dataset = super.unbindObject(trackingLog, "step", "resolutionPercentage", "accepted", "resolution", "createdMoment", "secondTrackingLog");
		dataset.put("claim", trackingLog.getClaim().getDescription());
		dataset.put("status", statusChoices);
		dataset.put("secondTrackingLogReadOnly", false);
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));

		super.getResponse().addData(dataset);

	}

}

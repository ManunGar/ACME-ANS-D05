
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
public class AssistanceAgentTrackingLogPublishService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		try {
			int trackingLogId = super.getRequest().getData("id", int.class);
			int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
			status = this.repository.isPublishableTrackingLogOwnedByAgent(trackingLogId, agentId);

			if (super.getRequest().hasData("indicator", String.class)) {
				String accepted = super.getRequest().getData("indicator", String.class);

				if (!"0".equals(accepted))
					try {
						AcceptedIndicator.valueOf(accepted);
					} catch (IllegalArgumentException | NullPointerException e) {
						status = false;
					}
			}
		} catch (Throwable e) {
			status = false;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		int trackingLogId;
		TrackingLog trackingLog;

		trackingLogId = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(trackingLogId);

		claim = this.repository.findClaimByTrackingLogId(trackingLogId);
		trackingLog.setClaim(claim);

		super.getBuffer().addData(trackingLog);

	}

	@Override
	public void bind(final TrackingLog trackingLog) {

		super.bindObject(trackingLog, "step", "resolutionPercentage", "indicator", "resolution");

	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		;
	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		trackingLog.setLastUpdateMoment(MomentHelper.getCurrentMoment());
		trackingLog.setDraftMode(false);

		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		SelectChoices statusChoices;
		boolean claimDraftMode;
		Dataset dataset;

		statusChoices = SelectChoices.from(AcceptedIndicator.class, trackingLog.getIndicator());

		claimDraftMode = trackingLog.getClaim().isDraftMode();

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "step", "resolutionPercentage", "indicator", "draftMode", "resolution", "createdMoment");
		dataset.put("claim", trackingLog.getClaim().getDescription());
		dataset.put("status", statusChoices);
		dataset.put("claimDraftMode", claimDraftMode);

		super.getResponse().addData(dataset);

	}

}

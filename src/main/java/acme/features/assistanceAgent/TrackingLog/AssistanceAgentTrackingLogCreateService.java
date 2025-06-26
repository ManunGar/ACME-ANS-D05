
package acme.features.assistanceAgent.TrackingLog;

import java.util.Collection;
import java.util.List;

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
		try {

			int claimId = super.getRequest().getData("masterId", int.class);
			int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
			status = this.repository.isClaimOwnedByAgent(claimId, agentId) && !this.repository.findClaimById(claimId).isDraftMode();

			List<TrackingLog> trackingLogsOrdered = this.repository.findTrackingLogsOrderedByCreatedMoment(claimId).stream().toList();
			Collection<TrackingLog> trackingLogsCompleted = this.repository.findAllTrackingLogsByclaimIdWithResolutionPercentageCompleted(claimId);

			if (trackingLogsOrdered.get(0).isDraftMode() || trackingLogsCompleted.size() >= 2)
				status = false;

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
		TrackingLog trackingLog;

		Integer claimId = super.getRequest().getData("masterId", int.class);
		Claim claim = this.repository.findClaimById(claimId);

		trackingLog = new TrackingLog();
		trackingLog.setIndicator(AcceptedIndicator.PENDING);
		trackingLog.setDraftMode(true);
		trackingLog.setResolutionPercentage(0.);
		trackingLog.setClaim(claim);

		super.getBuffer().addData(trackingLog);

	}

	@Override
	public void bind(final TrackingLog trackingLog) {

		super.bindObject(trackingLog, "step", "resolutionPercentage", "resolution", "indicator");
		trackingLog.setLastUpdateMoment(MomentHelper.getCurrentMoment());
		trackingLog.setCreatedMoment(MomentHelper.getCurrentMoment());

	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		;
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

		statusChoices = SelectChoices.from(AcceptedIndicator.class, trackingLog.getIndicator());

		dataset = super.unbindObject(trackingLog, "step", "resolutionPercentage", "indicator", "resolution", "createdMoment");
		dataset.put("claim", trackingLog.getClaim().getDescription());
		dataset.put("status", statusChoices);
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));

		super.getResponse().addData(dataset);

	}

}

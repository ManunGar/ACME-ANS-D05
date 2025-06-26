
package acme.features.assistanceAgent.TrackingLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Claims.AcceptedIndicator;
import acme.entities.Claims.Claim;
import acme.entities.TrackingLogs.TrackingLog;
import acme.realms.AssistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogDeleteService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		try {
			int trackingLogId = super.getRequest().getData("id", int.class);
			int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
			status = this.repository.isDraftTrackingLogOwnedByAgent(trackingLogId, agentId);
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
		this.repository.delete(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Collection<Claim> claimsOfThisAssistanceAgent;
		SelectChoices claimChoices;
		int assistanceAgentId;
		SelectChoices statusChoices;
		boolean claimDraftMode;
		Dataset dataset;

		assistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		claimsOfThisAssistanceAgent = this.repository.findClaimsByAssistanceAgentId(assistanceAgentId);
		claimChoices = SelectChoices.from(claimsOfThisAssistanceAgent, "description", trackingLog.getClaim());

		statusChoices = SelectChoices.from(AcceptedIndicator.class, trackingLog.getIndicator());

		claimDraftMode = trackingLog.getClaim().isDraftMode();

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "step", "resolutionPercentage", "indicator", "draftMode", "resolution", "createdMoment");
		dataset.put("claim", trackingLog.getClaim().getDescription());
		dataset.put("status", statusChoices);
		dataset.put("claims", claimChoices);
		dataset.put("readOnlyClaim", true);
		dataset.put("claimDraftMode", claimDraftMode);

		super.getResponse().addData(dataset);

	}

}

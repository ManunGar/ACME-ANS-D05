
package acme.features.assistanceAgent.TrackingLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.TrackingLogs.TrackingLog;
import acme.realms.AssistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogListOfClaimService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		try {
			int claimId = super.getRequest().getData("masterId", int.class);
			int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
			status = this.repository.isClaimOwnedByAgent(claimId, agentId);
		} catch (Throwable e) {
			status = false;
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int claimId;
		Collection<TrackingLog> trackingLogs;

		claimId = this.getRequest().getData("masterId", int.class);
		trackingLogs = this.repository.findAllTrackingLogsByclaimId(claimId);

		super.getBuffer().addData(trackingLogs);

	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;

		dataset = super.unbindObject(trackingLog, "step", "resolutionPercentage", "accepted");

		dataset.put("claim", trackingLog.getClaim().getDescription());
		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<TrackingLog> trackingLogs) {
		int claimId;

		claimId = super.getRequest().getData("masterId", int.class);

		super.getResponse().addGlobal("masterId", claimId);

	}

}

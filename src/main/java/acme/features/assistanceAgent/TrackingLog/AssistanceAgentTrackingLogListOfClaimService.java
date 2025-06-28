
package acme.features.assistanceAgent.TrackingLog;

import java.util.Collection;
import java.util.List;

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
			status = this.repository.isClaimOwnedByAgent(claimId, agentId) && !this.repository.findClaimById(claimId).isDraftMode();
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

		dataset = super.unbindObject(trackingLog, "step", "resolutionPercentage", "indicator");

		dataset.put("claim", trackingLog.getClaim().getDescription());
		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<TrackingLog> trackingLogs) {
		int claimId;

		claimId = super.getRequest().getData("masterId", int.class);

		List<TrackingLog> trackingLogsOrdered = this.repository.findTrackingLogsOrderedByCreatedMoment(claimId).stream().toList();

		Collection<TrackingLog> trackingLogsCompleted = this.repository.findAllTrackingLogsByclaimIdWithResolutionPercentageCompleted(claimId);

		boolean createVisible;
		if (trackingLogsOrdered.size() != 0)
			createVisible = !trackingLogsOrdered.get(0).isDraftMode() && trackingLogsCompleted.size() < 2;
		else
			createVisible = true;

		super.getResponse().addGlobal("masterId", claimId);
		super.getResponse().addGlobal("createVisible", createVisible);

	}

}

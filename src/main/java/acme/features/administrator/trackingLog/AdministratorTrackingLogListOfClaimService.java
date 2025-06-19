
package acme.features.administrator.trackingLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.TrackingLogs.TrackingLog;

@GuiService
public class AdministratorTrackingLogListOfClaimService extends AbstractGuiService<Administrator, TrackingLog> {

	@Autowired
	private AdministratorTrackingLogRepository repository;


	@Override
	public void authorise() {

		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);

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

}

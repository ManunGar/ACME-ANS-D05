
package acme.features.administrator.trackingLog;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.TrackingLogs.TrackingLog;

@GuiController
public class AdministratorTrackingLogController extends AbstractGuiController<Administrator, TrackingLog> {

	@Autowired
	private AdministratorTrackingLogListOfClaimService	listOfClaimService;

	@Autowired
	private AdministratorTrackingLogShowService			showService;


	@PostConstruct
	protected void initialise() {

		super.addBasicCommand("show", this.showService);

		super.addCustomCommand("listclaim", "list", this.listOfClaimService);

	}

}

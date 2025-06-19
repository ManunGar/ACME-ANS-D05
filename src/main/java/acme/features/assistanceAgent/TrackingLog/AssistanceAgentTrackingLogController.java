
package acme.features.assistanceAgent.TrackingLog;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.TrackingLogs.TrackingLog;
import acme.realms.AssistanceAgent.AssistanceAgent;

@GuiController
public class AssistanceAgentTrackingLogController extends AbstractGuiController<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogListOfClaimService	listOfClaimService;

	@Autowired
	private AssistanceAgentTrackingLogShowService			showService;

	@Autowired
	private AssistanceAgentTrackingLogCreateService			createService;

	@Autowired
	private AssistanceAgentTrackingLogUpdateService			updateService;

	@Autowired
	private AssistanceAgentTrackingLogPublishService		publishService;

	@Autowired
	private AssistanceAgentTrackingLogDeleteService			deleteService;


	@PostConstruct
	protected void initialise() {

		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("listclaim", "list", this.listOfClaimService);
		super.addCustomCommand("publish", "update", this.publishService);

	}

}

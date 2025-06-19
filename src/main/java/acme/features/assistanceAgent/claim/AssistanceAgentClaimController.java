
package acme.features.assistanceAgent.claim;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.Claims.Claim;
import acme.realms.AssistanceAgent.AssistanceAgent;

@GuiController
public class AssistanceAgentClaimController extends AbstractGuiController<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimListResolvedService		listResolvedService;

	@Autowired
	private AssistanceAgentClaimListNotResolvedService	listNotResolvedService;

	@Autowired
	private AssistanceAgentClaimShowService				showService;

	@Autowired
	private AssistanceAgentClaimCreateService			createService;

	@Autowired
	private AssistanceAgentClaimUpdateService			updateService;

	@Autowired
	private AssistanceAgentClaimPublishService			publishService;

	@Autowired
	private AssistanceAgentClaimDeleteService			deleteService;


	@PostConstruct
	protected void initialise() {

		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("listResolved", "list", this.listResolvedService);
		super.addCustomCommand("listNotResolved", "list", this.listNotResolvedService);
		super.addCustomCommand("publish", "update", this.publishService);

	}

}


package acme.features.manager.legs;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.Legs.Legs;
import acme.realms.AirlineManager;

@GuiController
public class ManagerLegsController extends AbstractGuiController<AirlineManager, Legs> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerLegsListService		listService;

	@Autowired
	private ManagerLegsShowService		showService;

	@Autowired
	private ManagerLegsUpdateService	updateService;

	@Autowired
	private ManagerLegsCreateService	createService;

	@Autowired
	private ManagerLegsPublishService	publishService;

	@Autowired
	private ManagerLegsDeleteService	deleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}

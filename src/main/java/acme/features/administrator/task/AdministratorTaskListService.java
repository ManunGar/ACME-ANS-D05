
package acme.features.administrator.task;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.MaintenanceRecords.MaintenanceRecord;
import acme.entities.Tasks.Task;

@GuiService
public class AdministratorTaskListService extends AbstractGuiService<Administrator, Task> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorTaskRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean status;
		Integer maintenanceRecordId;
		MaintenanceRecord maintenanceRecord = null;

		maintenanceRecordId = super.getRequest().hasData("maintenanceRecordId") ? super.getRequest().getData("maintenanceRecordId", int.class) : null;

		if (maintenanceRecordId != null) {
			maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);
			status = maintenanceRecord != null && //
				(!maintenanceRecord.isDraftMode() || //
					super.getRequest().getPrincipal().hasRealm(maintenanceRecord.getTechnician()));

		} else
			status = true;

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {

		Collection<Task> tasks = null;
		Integer maintenanceRecordId;

		maintenanceRecordId = super.getRequest().hasData("maintenanceRecordId") ? super.getRequest().getData("maintenanceRecordId", int.class) : null;

		if (maintenanceRecordId != null)
			tasks = this.repository.findTasksByMasterId(maintenanceRecordId);

		super.getBuffer().addData(tasks);

	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;

		dataset = super.unbindObject(task, "type", "priority", "description");
		super.addPayload(dataset, task, "estimatedDuration", "draftMode");

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<Task> tasks) {
		Integer maintenanceRecordId;

		maintenanceRecordId = super.getRequest().hasData("maintenanceRecordId") ?//
			super.getRequest().getData("maintenanceRecordId", int.class) : null;

		super.getResponse().addGlobal("maintenanceRecordId", maintenanceRecordId);
	}

}

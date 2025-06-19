
package acme.features.technicians.maintenanceRecordTask;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.MaintenanceRecords.MaintenanceRecord;
import acme.entities.MaintenanceRecords.MaintenanceRecordTask;
import acme.entities.Tasks.Task;
import acme.features.technicians.maintenanceRecord.TechnicianMaintenanceRecordRepository;
import acme.features.technicians.task.TechnicianTaskRepository;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordTaskDeleteService extends AbstractGuiService<Technician, MaintenanceRecordTask> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordTaskRepository	repository;

	@Autowired
	private TechnicianMaintenanceRecordRepository		maintenanceRecordRepository;

	@Autowired
	private TechnicianTaskRepository					taskRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean statusTask = true;
		boolean status = false;
		int taskId;
		Task task;
		int maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;
		Collection<Task> tasks;

		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		maintenanceRecord = this.maintenanceRecordRepository.findMaintenanceRecordById(maintenanceRecordId);

		tasks = this.taskRepository.findValidTasksToUnlink(maintenanceRecord);

		if (super.getRequest().hasData("task", int.class)) {
			taskId = super.getRequest().getData("task", int.class);
			task = this.taskRepository.findTaskById(taskId);

			if (!tasks.contains(task) && taskId != 0)
				statusTask = false;
		}

		status = maintenanceRecord != null && maintenanceRecord.isDraftMode() && super.getRequest().getPrincipal().hasRealm(maintenanceRecord.getTechnician());

		super.getResponse().setAuthorised(status && statusTask);
	}

	@Override
	public void load() {
		MaintenanceRecordTask mrTask;
		Integer maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;

		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		maintenanceRecord = this.maintenanceRecordRepository.findMaintenanceRecordById(maintenanceRecordId);

		mrTask = new MaintenanceRecordTask();
		mrTask.setMaintenanceRecord(maintenanceRecord);
		super.getBuffer().addData(mrTask);
	}

	@Override
	public void bind(final MaintenanceRecordTask mrTask) {
		;
	}

	@Override
	public void validate(final MaintenanceRecordTask mrTask) {

		Task task = super.getRequest().getData("task", Task.class);
		super.state(task != null, "task", "technician.involves.form.error.no-task-to-unlink");
	}

	@Override
	public void perform(final MaintenanceRecordTask mrTask) {
		Task task = super.getRequest().getData("task", Task.class);
		MaintenanceRecord maintenanceRecord = mrTask.getMaintenanceRecord();
		int taskId = task.getId();
		int maintenanceRecordId = maintenanceRecord.getId();

		this.repository.delete(this.repository.findMaintenanceRecordTaskBymaintenanceRecordIdTaskId(maintenanceRecordId, taskId));

	}

	@Override
	public void unbind(final MaintenanceRecordTask mrTask) {
		Collection<Task> tasks;
		int maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;
		SelectChoices choices;
		Dataset dataset;

		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		maintenanceRecord = this.maintenanceRecordRepository.findMaintenanceRecordById(maintenanceRecordId);

		tasks = this.taskRepository.findValidTasksToUnlink(maintenanceRecord);
		choices = SelectChoices.from(tasks, "description", mrTask.getTask());

		dataset = super.unbindObject(mrTask, "maintenanceRecord");
		dataset.put("maintenanceRecordId", mrTask.getMaintenanceRecord().getId());
		dataset.put("task", choices.getSelected().getKey());
		dataset.put("tasks", choices);
		dataset.put("aircraftRegistrationNumber", mrTask.getMaintenanceRecord().getAircraft().getRegistrationNumber());

		super.getResponse().addData(dataset);
	}
}

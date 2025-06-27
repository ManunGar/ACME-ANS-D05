
package acme.features.administrator.task;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Tasks.Task;
import acme.entities.Tasks.TaskType;

@GuiService
public class AdministratorTaskShowService extends AbstractGuiService<Administrator, Task> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorTaskRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean status;
		int masterId;
		Task task;

		status = super.getRequest().hasData("id", int.class);
		if (status) {
			masterId = super.getRequest().getData("id", int.class);
			task = this.repository.findTaskById(masterId);
			status = task != null && !task.isDraftMode();
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Task task;
		int id;

		id = super.getRequest().getData("id", int.class);
		task = this.repository.findTaskById(id);

		super.getBuffer().addData(task);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;
		SelectChoices choices;

		choices = SelectChoices.from(TaskType.class, task.getType());

		dataset = super.unbindObject(task, "description", "priority", "estimatedDuration", "draftMode");
		dataset.put("types", choices);
		dataset.put("type", choices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}

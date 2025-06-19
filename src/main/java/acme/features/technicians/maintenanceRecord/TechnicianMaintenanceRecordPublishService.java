
package acme.features.technicians.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Aircrafts.Aircraft;
import acme.entities.MaintenanceRecords.MaintenanceRecord;
import acme.entities.MaintenanceRecords.MaintenanceStatus;
import acme.entities.Tasks.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordPublishService extends AbstractGuiService<Technician, MaintenanceRecord> {

	// Internal state ------------------------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;

	// AbstractGuiService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status = false;
		boolean statusAircraft = true;
		int maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;
		boolean isDraft;
		boolean isTechnician;
		int aircraftId;
		Aircraft aircraft;

		if (super.getRequest().hasData("id", int.class)) {
			maintenanceRecordId = super.getRequest().getData("id", int.class);
			maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

			if (maintenanceRecord != null) {
				Technician technician = maintenanceRecord.getTechnician();
				isDraft = maintenanceRecord.isDraftMode();
				isTechnician = super.getRequest().getPrincipal().hasRealm(technician);

				status = isDraft && isTechnician;
			}
		}

		if (super.getRequest().hasData("aircraft", int.class)) {
			aircraftId = super.getRequest().getData("aircraft", int.class);
			aircraft = this.repository.findAircraftById(aircraftId);

			if (aircraft == null && aircraftId != 0)
				statusAircraft = false;
		}

		super.getResponse().setAuthorised(status && statusAircraft);
	}

	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;
		int maintenanceRecordId;

		maintenanceRecordId = super.getRequest().getData("id", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

		super.getBuffer().addData(maintenanceRecord);
	}

	@Override
	public void bind(final MaintenanceRecord maintenanceRecord) {

		int aircraftId;
		Aircraft aircraft;

		aircraftId = super.getRequest().getData("aircraft", int.class);
		aircraft = this.repository.findAircraftById(aircraftId);

		super.bindObject(maintenanceRecord, "maintenanceMoment", "status", "nextInspection", "estimatedCost", "notes");
		maintenanceRecord.setAircraft(aircraft);
	}

	@Override
	public void validate(final MaintenanceRecord maintenanceRecord) {

		Collection<Task> tasks = this.repository.findTasksByMaintenanceRecordId(maintenanceRecord.getId());

		super.state(!tasks.isEmpty(), "*", "technician.maintenance-record.form.error.zero-tasks");

		boolean hasUnpublishedTask = tasks.stream().anyMatch(Task::isDraftMode);
		super.state(!hasUnpublishedTask, "*", "technician.maintenance-record.form.error.not-all-tasks-published");
		if (maintenanceRecord.getStatus() != null)
			super.state(maintenanceRecord.getStatus().equals(MaintenanceStatus.COMPLETED), "status", "technician.maintenance-record.form.error.not-completed-status");

	}

	@Override
	public void perform(final MaintenanceRecord maintenanceRecord) {

		maintenanceRecord.setDraftMode(false);
		this.repository.save(maintenanceRecord);

	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		Collection<Aircraft> aircrafts;
		SelectChoices aircraftChoices;
		SelectChoices statusChoices;
		Dataset dataset;

		aircrafts = this.repository.findAllAircrafts();

		statusChoices = SelectChoices.from(MaintenanceStatus.class, maintenanceRecord.getStatus());
		aircraftChoices = SelectChoices.from(aircrafts, "model", maintenanceRecord.getAircraft());

		dataset = super.unbindObject(maintenanceRecord, "maintenanceMoment", "nextInspection", "estimatedCost", "notes", "draftMode");
		dataset.put("technician", maintenanceRecord.getTechnician().getIdentity().getFullName());
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("status", statusChoices.getSelected().getKey());
		dataset.put("statuses", statusChoices);

		super.getResponse().addData(dataset);
	}
}


package acme.features.administrator.task;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.MaintenanceRecords.MaintenanceRecord;
import acme.entities.MaintenanceRecords.MaintenanceRecordTask;
import acme.entities.Tasks.Task;

@Repository
public interface AdministratorTaskRepository extends AbstractRepository {

	@Query("SELECT t FROM Task t WHERE t.draftMode = false")
	Collection<Task> findPublishedTasks();

	@Query("SELECT t FROM Task t WHERE t.technician.id = :id")
	Collection<Task> findTasksByTechnicianId(int id);

	@Query("SELECT t FROM Task t WHERE t.id = :id")
	Task findTaskById(int id);

	@Query("select mr from MaintenanceRecord mr where mr.id = :id")
	MaintenanceRecord findMaintenanceRecordById(int id);

	@Query("select mrt.task from MaintenanceRecordTask mrt where mrt.maintenanceRecord.id = :masterId")
	Collection<Task> findTasksByMasterId(int masterId);

	@Query("select mrt.maintenanceRecord from MaintenanceRecordTask mrt where mrt.task.id = :taskId")
	Collection<MaintenanceRecord> findMaintenanceRecordsByTaskId(int taskId);

	@Query("SELECT mrt FROM MaintenanceRecordTask mrt WHERE mrt.task.id = :id")
	Collection<MaintenanceRecordTask> findMaintenanceRecordTasksFromTaskId(int id);
}

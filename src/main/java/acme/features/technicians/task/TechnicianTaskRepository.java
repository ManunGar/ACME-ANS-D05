
package acme.features.technicians.task;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.MaintenanceRecords.MaintenanceRecord;
import acme.entities.MaintenanceRecords.MaintenanceRecordTask;
import acme.entities.Tasks.Task;
import acme.realms.Technician;

@Repository
public interface TechnicianTaskRepository extends AbstractRepository {

	@Query("SELECT t FROM Task t WHERE t.draftMode = false")
	Collection<Task> findPublishedTasks();

	@Query("SELECT t FROM Task t WHERE t.technician.id = :id")
	Collection<Task> findTasksByTechnicianId(int id);

	@Query("SELECT t FROM Task t WHERE t.id = :id")
	Task findTaskById(int id);

	@Query("SELECT mrt FROM MaintenanceRecordTask mrt WHERE mrt.task.id = :id")
	Collection<MaintenanceRecordTask> findMaintenanceRecordTasksFromTaskId(int id);

	@Query("SELECT t FROM Technician t")
	Collection<Technician> findAllTechnicians();

	@Query("SELECT t FROM Technician t WHERE t.id = :id")
	Technician findTechnicianById(int id);

	@Query("select mrt.task from MaintenanceRecordTask mrt where mrt.maintenanceRecord.id = :masterId")
	Collection<Task> findTasksByMasterId(int masterId);

	@Query("SELECT mrt.task FROM MaintenanceRecordTask mrt WHERE mrt.maintenanceRecord.id = :maintenanceRecordId")
	Collection<Task> findTasksByMaintenanceRecordId(int maintenanceRecordId);

	@Query("select t from Task t where t not in (select mrt.task from MaintenanceRecordTask mrt where mrt.maintenanceRecord = :maintenanceRecord) and (t.draftMode = false or t.technician = :technician)")
	Collection<Task> findValidTasksToLink(MaintenanceRecord maintenanceRecord, Technician technician);

	@Query("select t from Task t where t in (select mrt.task from MaintenanceRecordTask mrt where mrt.maintenanceRecord = :maintenanceRecord)")
	Collection<Task> findValidTasksToUnlink(MaintenanceRecord maintenanceRecord);

}

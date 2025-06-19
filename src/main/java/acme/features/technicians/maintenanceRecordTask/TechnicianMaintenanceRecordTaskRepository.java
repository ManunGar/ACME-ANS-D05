
package acme.features.technicians.maintenanceRecordTask;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.MaintenanceRecords.MaintenanceRecordTask;

@Repository
public interface TechnicianMaintenanceRecordTaskRepository extends AbstractRepository {

	@Query("SELECT mrt FROM MaintenanceRecordTask mrt WHERE mrt.maintenanceRecord.id = :maintenanceRecordId AND mrt.task.id = :taskId")
	MaintenanceRecordTask findMaintenanceRecordTaskBymaintenanceRecordIdTaskId(Integer maintenanceRecordId, Integer taskId);

	@Query("SELECT mrt FROM MaintenanceRecordTask mrt WHERE mrt.id = :id")
	MaintenanceRecordTask findMaintenanceRecordTaskById(Integer id);

	@Query("SELECT mrt FROM MaintenanceRecordTask mrt WHERE mrt.maintenanceRecord.id = :id")
	Collection<MaintenanceRecordTask> findMaintenanceRecordTaskByMaintenanceRecordId(Integer id);

	@Query("SELECT mrt FROM MaintenanceRecordTask mrt WHERE mrt.task.id = :id")
	Collection<MaintenanceRecordTask> findMaintenanceRecordTaskByTaskId(Integer id);
}


package acme.constraints;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.MaintenanceRecords.MaintenanceRecord;

@Validator
public class MaintenanceRecordValidator extends AbstractValidator<ValidMaintenanceRecord, MaintenanceRecord> {

	@Override
	protected void initialise(final ValidMaintenanceRecord maintenanceRecord) {
		assert maintenanceRecord != null;
	}

	@Override
	public boolean isValid(final MaintenanceRecord maintenanceRecord, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		Date minimumNextInspection;
		boolean futureNextInspection;
		Date currentMoment;
		boolean correctNextInspection;

		if (maintenanceRecord.isDraftMode() && maintenanceRecord.getMaintenanceMoment() != null && maintenanceRecord.getNextInspection() != null) {

			minimumNextInspection = MomentHelper.deltaFromMoment(maintenanceRecord.getMaintenanceMoment(), 1, ChronoUnit.MINUTES);
			correctNextInspection = MomentHelper.isAfterOrEqual(maintenanceRecord.getNextInspection(), minimumNextInspection);

			super.state(context, correctNextInspection, "NextInspection", "acme.validation.maintenance-record.inspection-due-date.message");
		}
		if (maintenanceRecord.isDraftMode() && maintenanceRecord.getNextInspection() != null) {
			currentMoment = MomentHelper.deltaFromMoment(MomentHelper.getCurrentMoment(), 1, ChronoUnit.MINUTES);

			futureNextInspection = MomentHelper.isAfterOrEqual(maintenanceRecord.getNextInspection(), currentMoment);
			super.state(context, futureNextInspection, "NextInspection", "acme.validation.maintenance-record.future-inspection-due-date.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}


package acme.constraints;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.AssistanceAgent.AssistanceAgent;
import acme.realms.AssistanceAgent.AssistanceAgentRepository;

@Validator
public class AssistanceAgentValidator extends AbstractValidator<ValidAssistanceAgent, AssistanceAgent> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentRepository	repository;

	// ConstraintValidator interface ------------------------------------------

	private static final String			LANGUAGE_REGEX	= "^[A-ZÁÉÍÓÚÑ][a-záéíóúñ]+(, [A-ZÁÉÍÓÚÑ][a-záéíóúñ]+)*$";
	private static Pattern				PATTERN			= Pattern.compile(AssistanceAgentValidator.LANGUAGE_REGEX);


	@Override
	protected void initialise(final ValidAssistanceAgent annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AssistanceAgent assistanceAgent, final ConstraintValidatorContext context) {
		// HINT: job can be null
		assert context != null;

		boolean result;

		if (assistanceAgent == null)
			super.state(context, false, "*", "acme.validation.NotNull.message");
		else {
			//Validation of EmployeeCode is unique
			{
				boolean uniqueEmployeeCode = !this.repository.existsOtherWithEmployeeCode(assistanceAgent.getEmployeeCode(), assistanceAgent.getId());

				super.state(context, uniqueEmployeeCode, "employeeCode", "acme.validation.assistanceAgent.employeeCode.message");
			}
			//Validation of the format of property spokenLanguages
			{
				boolean correctFormatSpokenLanguages;
				if (assistanceAgent.getSpokenLanguages() == null || assistanceAgent.getSpokenLanguages().trim().isEmpty())
					correctFormatSpokenLanguages = false;
				else
					correctFormatSpokenLanguages = AssistanceAgentValidator.PATTERN.matcher(assistanceAgent.getSpokenLanguages()).matches();

				super.state(context, correctFormatSpokenLanguages, "spokenLanguages", "acme.validation.assistanceAgent.spokenLanguages.message");
			}

		}

		result = !super.hasErrors(context);

		return result;
	}

}

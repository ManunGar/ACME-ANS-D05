
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.realms.AirlineManager;

public class ManagerValidator extends AbstractValidator<ValidManager, AirlineManager> {

	@Override
	protected void initialise(final ValidManager annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AirlineManager manager, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (manager == null)
			super.state(context, false, "*", "acme.validation.NotNull.message");
		else if (manager.getIdentifier() == null || !manager.getIdentifier().matches("^[A-Z]{2,3}\\d{6}$"))
			super.state(context, false, "Identifier", "acme.validation.Customer.identifier.message");
		else {
			String identifierManager = "";
			int count = 0;
			String[] nameList = manager.getUserAccount().getIdentity().getName().split(" ");
			count += nameList.length;
			for (Integer i = 0; i < nameList.length; i++)
				identifierManager += String.valueOf(nameList[i].charAt(0));

			String[] surnameList = manager.getUserAccount().getIdentity().getSurname().split(" ");
			for (Integer i = 0; i < surnameList.length && count < 3; i++) {
				identifierManager += String.valueOf(surnameList[i].charAt(0));
				count++;
			}

			if (manager.getIdentifier().substring(0, 2).equals(identifierManager) || manager.getIdentifier().substring(0, 3).equals(identifierManager))
				result = true;
			else
				super.state(context, false, "Identifier", "acme.validation.Manager.identifier.name.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}

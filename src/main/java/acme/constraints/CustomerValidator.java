
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.features.authenticated.customer.AuthenticatedCustomerRepository;
import acme.realms.Customer;

@Validator
public class CustomerValidator extends AbstractValidator<ValidCustomer, Customer> {

	@Autowired
	private AuthenticatedCustomerRepository repository;


	@Override
	protected void initialise(final ValidCustomer annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Customer customer, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (customer == null)
			super.state(context, false, "*", "acme.validation.NotNull.message");
		else if (customer.getIdentifier() == null || !customer.getIdentifier().matches("^[A-Z]{2,3}\\d{6}$"))
			super.state(context, false, "identifier", "acme.validation.text.identifier.pattern");
		else {
			String identifierCustomer = "";
			int count = 0;
			String[] nameList = customer.getUserAccount().getIdentity().getName().split(" ");
			count += nameList.length;
			for (Integer i = 0; i < nameList.length; i++)
				identifierCustomer += String.valueOf(nameList[i].charAt(0));

			String[] surnameList = customer.getUserAccount().getIdentity().getSurname().split(" ");
			for (Integer i = 0; i < surnameList.length && count < 3; i++) {
				identifierCustomer += String.valueOf(surnameList[i].charAt(0));
				count++;
			}
			String a = customer.getIdentifier().substring(0, 3);
			if (customer.getIdentifier().substring(0, 2).equals(identifierCustomer.toUpperCase()) || customer.getIdentifier().substring(0, 3).equals(identifierCustomer.toUpperCase()))
				result = true;
			else
				super.state(context, false, "identifier", "acme.validation.text.identifier.letters");
		}

		Customer c = this.repository.findCustomerByIdentifier(customer.getIdentifier());
		if (c != null && c.getId() != customer.getId())
			super.state(context, false, "identifier", "acme.validation.text.identifier.duplicated");

		result = !super.hasErrors(context);

		return result;
	}

}

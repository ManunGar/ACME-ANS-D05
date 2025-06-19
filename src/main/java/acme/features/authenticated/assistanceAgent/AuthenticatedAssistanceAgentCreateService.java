
package acme.features.authenticated.assistanceAgent;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.principals.UserAccount;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Airlines.Airline;
import acme.realms.AssistanceAgent.AssistanceAgent;

@GuiService
public class AuthenticatedAssistanceAgentCreateService extends AbstractGuiService<Authenticated, AssistanceAgent> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedAssistanceAgentRepository repository;

	// AbstractService<Authenticated, Consumer> ---------------------------


	@Override
	public void authorise() {
		boolean status;

		status = !super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AssistanceAgent object;
		int userAccountId;
		UserAccount userAccount;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		userAccount = this.repository.findUserAccountById(userAccountId);

		object = new AssistanceAgent();
		object.setUserAccount(userAccount);
		object.setMoment(MomentHelper.getCurrentMoment());

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final AssistanceAgent object) {
		assert object != null;
		int airlineId;
		Airline airline;

		super.bindObject(object, "employeeCode", "spokenLanguages", "briefBio", "photo");
		airlineId = super.getRequest().getData("airline", int.class);
		airline = this.repository.findAirlineById(airlineId);
		object.setAirline(airline);

	}

	@Override
	public void validate(final AssistanceAgent object) {
		;
	}

	@Override
	public void perform(final AssistanceAgent object) {
		object.setMoment(MomentHelper.getCurrentMoment());
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final AssistanceAgent object) {
		Dataset dataset;
		List<Airline> airlines;
		SelectChoices choices;

		airlines = this.repository.findAllAirlines();
		choices = SelectChoices.from(airlines, "IATAcode", object.getAirline());

		dataset = super.unbindObject(object, "employeeCode", "spokenLanguages", "briefBio", "photo");
		dataset.put("airline", object.getAirline());
		dataset.put("airlines", choices);

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}

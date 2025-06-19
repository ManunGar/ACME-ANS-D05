/*
 * AuthenticatedAssistanceAgentUpdateService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.authenticated.assistanceAgent;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Airlines.Airline;
import acme.realms.AssistanceAgent.AssistanceAgent;

@GuiService
public class AuthenticatedAssistanceAgentUpdateService extends AbstractGuiService<Authenticated, AssistanceAgent> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedAssistanceAgentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AssistanceAgent object;
		int userAccountId;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		object = this.repository.findOneAssistanceAgentByUserAccountId(userAccountId);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final AssistanceAgent employer) {
		super.bindObject(employer, "employeeCode", "spokenLanguages", "briefBio", "photo", "airline");
	}

	@Override
	public void validate(final AssistanceAgent employer) {
		;
	}

	@Override
	public void perform(final AssistanceAgent employer) {
		this.repository.save(employer);
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

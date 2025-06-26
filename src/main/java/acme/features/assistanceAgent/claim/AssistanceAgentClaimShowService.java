/*
 * AssistanceAgentTrackingLogShowService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Claims.AcceptedIndicator;
import acme.entities.Claims.Claim;
import acme.entities.Claims.ClaimTypes;
import acme.entities.Legs.Legs;
import acme.realms.AssistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimShowService extends AbstractGuiService<AssistanceAgent, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentClaimRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		try {
			int claimId = super.getRequest().getData("id", int.class);
			int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
			status = this.repository.isClaimOwnedByAgent(claimId, agentId);
		} catch (Throwable e) {
			status = false;
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		Legs leg;
		int claimId;

		claimId = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegByClaimId(claimId);
		claim = this.repository.findClaimById(claimId);
		claim.setLeg(leg);

		super.getBuffer().addData(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Collection<Legs> legs;
		SelectChoices typesChoices;
		SelectChoices legsChoices;
		Dataset dataset;
		boolean undergoing;

		undergoing = claim.indicator().equals(AcceptedIndicator.PENDING);

		legs = this.repository.findAvailableLegs(MomentHelper.getCurrentMoment());
		legsChoices = SelectChoices.from(legs, "flightNumber", claim.getLeg());

		typesChoices = SelectChoices.from(ClaimTypes.class, claim.getClaimType());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "claimType");

		dataset.put("indicator", claim.indicator());
		dataset.put("leg", claim.getLeg());
		dataset.put("legs", legsChoices);
		dataset.put("claimTypes", typesChoices);
		dataset.put("draftMode", claim.isDraftMode());
		dataset.put("undergoing", undergoing);
		//Related to leg:
		dataset.put("departure", claim.getLeg().getDeparture());
		dataset.put("arrival", claim.getLeg().getArrival());
		dataset.put("status", claim.getLeg().getStatus());

		super.getResponse().addData(dataset);
	}

}

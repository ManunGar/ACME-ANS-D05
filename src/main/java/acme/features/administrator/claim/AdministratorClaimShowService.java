/*
 * AdministratorTrackingLogShowService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.administrator.claim;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Claims.AcceptedIndicator;
import acme.entities.Claims.Claim;
import acme.entities.Legs.Legs;

@GuiService
public class AdministratorClaimShowService extends AbstractGuiService<Administrator, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorClaimRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int claimId;
		Claim claim;

		claimId = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(claimId);

		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class) && !claim.isDraftMode();
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
		Dataset dataset;
		boolean undergoing;

		undergoing = claim.indicator().equals(AcceptedIndicator.PENDING);

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "claimType");

		dataset.put("accepted", claim.indicator());
		dataset.put("leg", claim.getLeg());
		dataset.put("undergoing", undergoing);
		//Related to leg:
		dataset.put("departure", claim.getLeg().getDeparture());
		dataset.put("arrival", claim.getLeg().getArrival());
		dataset.put("status", claim.getLeg().getStatus());

		super.getResponse().addData(dataset);
	}

}

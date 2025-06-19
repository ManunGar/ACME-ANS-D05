
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
public class AssistanceAgentClaimPublishService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		int claimId = super.getRequest().getData("id", int.class);
		int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		boolean status = this.repository.isDraftClaimOwnedByAgent(claimId, agentId);

		if (super.getRequest().hasData("leg", int.class)) {
			int legId = super.getRequest().getData("leg", int.class);

			if (legId != 0) {
				Legs leg = this.repository.findLegById(legId);
				Collection<Legs> availableLegs = this.repository.findAvailableLegs(MomentHelper.getCurrentMoment());
				status = status && availableLegs.contains(leg);
			}
		}

		if (super.getRequest().hasData("claimType", String.class)) {
			String claimType = super.getRequest().getData("claimType", String.class);

			if (!"0".equals(claimType))
				try {
					ClaimTypes.valueOf(claimType);
				} catch (IllegalArgumentException | NullPointerException e) {
					status = false;
				}
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		int claimId;
		Legs leg;

		claimId = super.getRequest().getData("id", int.class);

		claim = this.repository.findClaimById(claimId);
		leg = this.repository.findLegByClaimId(claimId);

		claim.setLeg(leg);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		int legId;
		Legs leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		super.bindObject(claim, "passengerEmail", "description", "claimType");
		claim.setLeg(leg);

	}

	@Override
	public void validate(final Claim claim) {
		int legId;
		Legs leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		if (leg == null)
			super.state(false, "leg", "acme.validation.confirmation.message.claim.leg");
	}

	@Override
	public void perform(final Claim claim) {
		claim.setDraftMode(false);

		this.repository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Collection<Legs> legs;
		SelectChoices typesChoices;
		SelectChoices legsChoices;
		Dataset dataset;
		boolean undergoing;

		undergoing = claim.accepted().equals(AcceptedIndicator.PENDING);

		legs = this.repository.findAvailableLegs(MomentHelper.getCurrentMoment());
		legsChoices = SelectChoices.from(legs, "flightNumber", claim.getLeg());

		typesChoices = SelectChoices.from(ClaimTypes.class, claim.getClaimType());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "claimType");

		dataset.put("accepted", claim.accepted());
		dataset.put("leg", claim.getLeg());
		dataset.put("legs", legsChoices);
		dataset.put("claimTypes", typesChoices);
		dataset.put("draftMode", claim.isDraftMode());
		dataset.put("undergoing", undergoing);
		//Related to leg:
		if (claim.getLeg() != null) {
			dataset.put("departure", claim.getLeg().getDeparture());
			dataset.put("arrival", claim.getLeg().getArrival());
			dataset.put("status", claim.getLeg().getStatus());
		}

		super.getResponse().addData(dataset);
	}

}

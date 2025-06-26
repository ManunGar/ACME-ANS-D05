
package acme.features.assistanceAgent.claim;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Claims.Claim;
import acme.entities.Claims.ClaimTypes;
import acme.entities.Legs.Legs;
import acme.realms.AssistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimCreateService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		boolean status = true;
		try {
			if (super.getRequest().hasData("leg", int.class)) {
				int legId = super.getRequest().getData("leg", int.class);

				Legs leg = this.repository.findLegById(legId);
				if (legId != 0) {
					Collection<Legs> availableLegs = this.repository.findAvailableLegs(MomentHelper.getCurrentMoment());
					status = availableLegs.contains(leg);
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
		} catch (Throwable e) {
			status = false;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		int assistanceAgentId;
		AssistanceAgent assistanceAgent;
		Date registrationMoment;

		assistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		assistanceAgent = this.repository.findAgentById(assistanceAgentId);

		registrationMoment = MomentHelper.getCurrentMoment();

		claim = new Claim();
		claim.setAssistanceAgent(assistanceAgent);
		claim.setRegistrationMoment(registrationMoment);
		claim.setDraftMode(true);

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
		if (this.repository.findLegById(super.getRequest().getData("leg", int.class)) == null)
			super.state(false, "leg", "acme.validation.confirmation.message.claim.leg");
	}

	@Override
	public void perform(final Claim claim) {
		claim.setRegistrationMoment(MomentHelper.getCurrentMoment());

		this.repository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Collection<Legs> legs;
		SelectChoices typesChoices;
		SelectChoices legsChoices;
		Dataset dataset;

		legs = this.repository.findAvailableLegs(MomentHelper.getCurrentMoment());
		legsChoices = SelectChoices.from(legs, "flightNumber", claim.getLeg());

		typesChoices = SelectChoices.from(ClaimTypes.class, claim.getClaimType());

		dataset = super.unbindObject(claim, "passengerEmail", "description", "claimType");

		dataset.put("indicator", claim.indicator());
		dataset.put("leg", claim.getLeg());
		dataset.put("legs", legsChoices);
		dataset.put("claimTypes", typesChoices);
		dataset.put("readonlyAccepted", "true");

		super.getResponse().addData(dataset);
	}

}

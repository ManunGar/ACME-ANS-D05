
package acme.entities.Claims;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import org.springframework.data.domain.PageRequest;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidClaim;
import acme.constraints.ValidLongText;
import acme.entities.Legs.Legs;
import acme.realms.AssistanceAgent.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidClaim
@Table(indexes = {
	@Index(columnList = "assistance_agent_id, draftMode"), @Index(columnList = "leg_id"), @Index(columnList = "registrationMoment")
})
public class Claim extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@Mandatory
	@ValidEmail
	@Automapped
	private String				passengerEmail;

	@Mandatory
	@ValidLongText
	@Automapped
	private String				description;

	@Mandatory
	@Valid
	@Automapped
	private ClaimTypes			claimType;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	// Relationships

	@Mandatory
	@Valid
	@ManyToOne
	private AssistanceAgent		assistanceAgent;

	@Mandatory
	@Valid
	@ManyToOne
	private Legs				leg;


	@Transient
	public AcceptedIndicator indicator() {
		ClaimRepository repository = SpringHelper.getBean(ClaimRepository.class);
		List<AcceptedIndicator> indicators = repository.findAcceptedIndicatorsByClaimId(this.getId(), PageRequest.of(0, 1));
		return indicators.isEmpty() ? AcceptedIndicator.PENDING : indicators.get(0);
	}

}

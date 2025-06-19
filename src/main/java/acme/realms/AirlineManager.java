
package acme.realms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidIdentifier;
import acme.constraints.ValidManager;
import acme.entities.Airlines.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidManager
public class AirlineManager extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	// Attributes

	@Mandatory
	@ValidIdentifier
	@Column(unique = true)
	private String				identifier;

	@Mandatory
	@ValidNumber(max = 120)
	@Automapped
	private Integer				experience;

	@Optional
	@ValidUrl
	@Automapped
	private String				picture;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				dateOfBirth; // QUEST

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline				airline;

}

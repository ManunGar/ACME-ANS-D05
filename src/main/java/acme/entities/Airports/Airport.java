
package acme.entities.Airports;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidIATACode;
import acme.constraints.ValidShortText;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "IATAcode"), @Index(columnList = "email")
})
public class Airport extends AbstractEntity {

	//Serialisation version

	private static final long	serialVersionUID	= 1L;

	//Attributes

	@Mandatory
	@ValidShortText
	@Automapped
	private String				name;

	@Mandatory
	@ValidIATACode
	@Column(unique = true)
	private String				IATAcode;

	@Mandatory
	@Valid
	@Automapped
	private OperationalScope	operationalScope;

	@Mandatory
	@ValidShortText
	@Automapped
	private String				city;

	@Mandatory
	@ValidShortText
	@Automapped
	private String				country;

	@Optional
	@ValidUrl
	@Automapped
	private String				website;

	@Optional
	@ValidEmail
	@Column(unique = true)
	private String				email;

	@Optional
	@ValidString(pattern = "^\\+?\\d{6,15}$", message = "{acme.validation.text.message.phone}")
	@Automapped
	private String				contactPhoneNumber;

	@Mandatory
	@Automapped
	private boolean				draftMode;

}

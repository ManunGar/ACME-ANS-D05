
package acme.entities.TrackingLogs;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidScore;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidShortText;
import acme.constraints.ValidTrackingLog;
import acme.entities.Claims.AcceptedIndicator;
import acme.entities.Claims.Claim;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidTrackingLog
@Table(indexes = {
	@Index(columnList = "claim_id, draftMode"), @Index(columnList = "createdMoment"), @Index(columnList = "resolutionPercentage"), @Index(columnList = "secondTrackingLog")
})
public class TrackingLog extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				lastUpdateMoment;

	@Mandatory
	@ValidShortText
	@Automapped
	private String				step;

	@Mandatory
	@ValidScore
	@Automapped
	private double				resolutionPercentage;

	@Mandatory
	@Valid
	@Automapped
	private AcceptedIndicator	accepted;	//Indicator

	@Mandatory
	@Automapped
	private boolean				draftMode;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				createdMoment;

	@Mandatory
	@Automapped
	private boolean				secondTrackingLog;

	@Optional
	@ValidString
	@Automapped
	private String				resolution;

	/*
	 * The `createdMoment` attribute is used to chronologically
	 * sort the different trackingLogs in the database and
	 * to perform all checks and validations, whereas
	 * `lastUpdateMoment` is the attribute that informs the user
	 * about the last time this trackingLog was modified.
	 */

	// Derived attributes -----------------------------------------------------


	@Transient
	public boolean validResolution() {
		return this.resolution != null && !this.resolution.trim().isEmpty();
	}

	// Relationships


	@Mandatory
	@Valid
	@ManyToOne
	private Claim claim;

}

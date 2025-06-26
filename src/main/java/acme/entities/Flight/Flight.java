
package acme.entities.Flight;

import java.beans.Transient;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import org.hibernate.validator.constraints.Length;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidShortText;
import acme.entities.Legs.LegRepository;
import acme.realms.AirlineManager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "manager_id")
})
public class Flight extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// Attributes

	@Mandatory
	@Automapped
	private Boolean				draftMode;

	@Mandatory
	@ValidShortText
	@Automapped
	private String				highlights;

	@Mandatory
	@Automapped
	private Boolean				selfTransfer;

	@Mandatory
	@ValidMoney(min = 1, max = 1000000)
	@Automapped
	private Money				cost;

	@Optional
	@Length(min = 0, max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AirlineManager		manager;


	@Transient
	public String getDeparture() {
		String result;
		List<Date> departures;
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		departures = repository.findDepartureByFlightId(this.getId());

		result = !departures.isEmpty() ? departures.get(0).toString() : "None";
		return result;
	}

	@Transient
	public String getArrival() {
		String result;
		List<Date> arrivals;
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		arrivals = repository.findArrivalByFlightId(this.getId());
		result = !arrivals.isEmpty() ? arrivals.get(0).toString() : "None";
		return result;
	}

	@Transient
	public String getOrigin() {
		String result;
		List<String> origins;
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		origins = repository.findOriginCityByFlightId(this.getId());
		result = !origins.isEmpty() ? origins.get(0).toString() : "None";
		return result;
	}

	@Transient
	public String getDestination() {
		String result;
		List<String> destinations;
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		destinations = repository.findDestinationCityByFlightId(this.getId());
		result = !destinations.isEmpty() ? destinations.get(0).toString() : "None";
		return result;
	}

	@Transient
	public Integer getLayovers() {
		Integer result;
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		result = repository.numberOfLayours(this.getId()) - 1;

		return result;
	}

}

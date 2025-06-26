
package acme.forms;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Statictics extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	Integer						count;
	Double						average;
	Double						max;
	Double						min;
	Double						standardDeviation;

}

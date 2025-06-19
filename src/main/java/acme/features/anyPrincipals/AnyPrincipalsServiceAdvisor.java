
package acme.features.anyPrincipals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import acme.entities.Services.Service;

@ControllerAdvice
public class AnyPrincipalsServiceAdvisor {

	@Autowired
	private AnyPrincipalsServiceRepository repository;


	@ModelAttribute("service")
	public Service getService() {
		Service result;
		try {
			result = this.repository.findRandomService();
		} catch (final Throwable oops) {
			result = null;
		}
		return result;
	}

}

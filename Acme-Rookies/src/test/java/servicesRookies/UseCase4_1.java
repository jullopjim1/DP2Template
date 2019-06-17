
package servicesRookies;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ConfigurationService;
import services.MessageService;
import utilities.AbstractTest;
import domain.Configuration;

@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UseCase4_1 extends AbstractTest {

	// Un usuario no autenticado en el sistema puede registrarse como proveedor

	@Autowired
	private MessageService			messageService;
	@Autowired
	private ConfigurationService	configurationService;


	@Test
	public void registerNotifyRebrandingTest() {
		// Un administrador notifica el cambio de marca de la aplicación (POSITIVO)
		//b) Positive test
		//c) analysis of sentence coverage: 98.3%
		//d) An administrator notifies the users about the rebranding
		this.registerNotifyRebrandingDriver("admin1", null);
		// Un usuario no autenticado notifica el cambio de marca de la aplicación (NEGATIVO)
		//b) Negative test
		//c) analysis of sentence coverage: 98.3%
		//d) An unauthenticated user can't notify the users about the rebranding because he/she doesn't have the necessary permissions
		this.registerNotifyRebrandingDriver(null, IllegalArgumentException.class);
	}

	public void registerNotifyRebrandingDriver(final String username, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			final Integer numberMessagesBefore = this.messageService.findAll().size();
			this.configurationService.saveWhenRebranding();
			this.messageService.notificationRebranding();
			final Configuration configuration = this.configurationService.findOne();
			Assert.isTrue(configuration.isHasRebranded());
			Assert.isTrue(numberMessagesBefore < this.messageService.findAll().size());
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}

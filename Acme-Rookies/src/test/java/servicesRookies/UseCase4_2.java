
package servicesRookies;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.AuditorService;
import services.CreditCardService;
import utilities.AbstractTest;
import domain.Auditor;
import domain.CreditCard;

@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UseCase4_2 extends AbstractTest {

	// 4.2 Un usuario autenticado como administrador debe ser capaz de crear cuentas de usuario
	// para nuevos auditors

	@Autowired
	private AuditorService		auditorService;

	@Autowired
	private CreditCardService	creditCardService;

	private static Integer		randomInt	= 10;


	@Test
	public void registerAuditorTest() {
		// Un administrador registra un auditor (POSITIVO)
		//b) Positive test
		//c) analysis of sentence coverage: 100%
		//d) An administrator can create a new auditor account
		this.registerAuditorDriver("admin1", null);
		// Un usuario no autenticado registra un auditor (NEGATIVO)
		//b) Negative test
		//c) analysis of sentence coverage: 100%
		//d) An unauthenticated user can't create a new auditor account because he/she doesn't have the required permissions
		this.registerAuditorDriver(null, IllegalArgumentException.class);
	}

	public void registerAuditorDriver(final String username, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			final Auditor auditor = this.auditorService.create();
			final CreditCard creditCard = this.creditCardService.create();
			creditCard.setCVVCode(100);
			creditCard.setExpirationMonth(12);
			creditCard.setExpirationYear(2024);
			creditCard.setHolderName("holderName");
			creditCard.setMakeName("makeName");
			creditCard.setNumber("1111222233334444");
			auditor.getUserAccount().setPassword("newAuditor" + UseCase4_2.randomInt);
			auditor.getUserAccount().setUsername("newAuditor" + UseCase4_2.randomInt);
			auditor.setAddress("address");
			auditor.setCreditCard(creditCard);
			auditor.setEmail("email@email");
			auditor.setName("anme");
			auditor.setPhone("1234");
			auditor.setPhoto("http://photo");
			auditor.setSurname("surname");
			auditor.setVATNumber("100");
			final Auditor savedAuditor = this.auditorService.save(auditor);
			Assert.notNull(this.auditorService.findOne(savedAuditor.getId()));
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
		UseCase4_2.randomInt++;
	}
}

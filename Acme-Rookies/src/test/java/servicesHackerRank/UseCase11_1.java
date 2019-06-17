
package servicesHackerRank;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.AdministratorService;
import services.CreditCardService;
import utilities.AbstractTest;
import domain.Administrator;
import domain.CreditCard;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UseCase11_1 extends AbstractTest {

	// An actor who is authenticated as administrator must be able to register new accounts for administrators

	// Services

	private static Integer			uniqueness	= 10;

	@Autowired
	private AdministratorService	administratorService;
	@Autowired
	private CreditCardService		creditCardService;


	// Tests

	@Test
	public void testUseCase11_1() {
		// Un usuario autenticado como administrador crea una cuenta de admnistrador (POSITIVO)
		//b) Positive test
		//c) analysis of sentence coverage: 100%
		//d) An administrator can create new administrator accounts
		this.registerAsAdminDriver("admin1", null);
		// Un usuario no autenticado crea una cuenta como administrador, no puede porque tiene que ser un administrador (NEGATIVO)
		//b) Negative test
		//c) analysis of sentence coverage: 100%
		//d) An user who is not an administrator can't register new administrator accounts
		this.registerAsAdminDriver(null, IllegalArgumentException.class);
	}

	// Drivers

	private void registerAsAdminDriver(final String username, final Class<?> expected) {
		super.authenticate(username);
		Class<?> caught = null;
		try {
			final Administrator administrator = this.administratorService.create();
			final CreditCard creditCard = this.creditCardService.create();
			administrator.setAddress("address");
			administrator.setEmail("email@email.com");
			administrator.setName("name");
			administrator.setPhone("phone");
			administrator.setPhoto("http://photo");
			administrator.setSurname("surname");
			administrator.setVATNumber("vATNumber");
			administrator.getUserAccount().setPassword("newAdministrator" + UseCase11_1.uniqueness);
			administrator.getUserAccount().setUsername("newAdministrator" + UseCase11_1.uniqueness);
			creditCard.setCVVCode(123);
			creditCard.setExpirationMonth(12);
			creditCard.setExpirationYear(2021);
			creditCard.setHolderName("holderName");
			creditCard.setMakeName("makeName");
			creditCard.setNumber("1111222233334444");
			UseCase11_1.uniqueness++;
			administrator.setCreditCard(creditCard);
			final Administrator savedAdministrator = this.administratorService.save(administrator);
			Assert.notNull(this.administratorService.findOne(savedAdministrator.getId()));
		} catch (final Throwable t) {
			caught = t.getClass();
			super.checkExceptions(expected, caught);
		}
		super.unauthenticate();
	}

}

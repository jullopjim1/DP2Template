
package servicesRookies;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.CreditCardService;
import services.ProviderService;
import utilities.AbstractTest;
import domain.CreditCard;
import domain.Provider;

@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UseCase9_3 extends AbstractTest {

	// Un usuario no autenticado en el sistema puede registrarse como proveedor

	@Autowired
	private ProviderService		providerService;

	@Autowired
	private CreditCardService	creditCardService;

	private static Integer		randomInt	= 10;


	@Test
	public void registerProviderTest() {
		// Un usuario sin autenticar se registra como proveedor (POSITIVO)
		//b) Positive test
		//c) analysis of sentence coverage: 100%
		//d) An unauthenticated user can register in the system as a provider
		this.registerProviderDriver(null, null);
		// Un usuario autenticado se registra como provedor (NEGATIVO)
		//b) Negative test
		//c) analysis of sentence coverage: 100%
		//d) An authenticated user can't register in the system as a provider because he/she already has an account
		this.registerProviderDriver("admin1", IllegalArgumentException.class);
	}

	public void registerProviderDriver(final String username, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			final Provider provider = this.providerService.create();
			final CreditCard creditCard = this.creditCardService.create();
			creditCard.setCVVCode(100);
			creditCard.setExpirationMonth(12);
			creditCard.setExpirationYear(2024);
			creditCard.setHolderName("holderName");
			creditCard.setMakeName("makeName");
			creditCard.setNumber("1111222233334444");
			provider.getUserAccount().setPassword("newProvider" + UseCase9_3.randomInt);
			provider.getUserAccount().setUsername("newProvider" + UseCase9_3.randomInt);
			provider.setAddress("address");
			provider.setMake("make");
			provider.setCreditCard(creditCard);
			provider.setEmail("email@email");
			provider.setName("anme");
			provider.setPhone("1234");
			provider.setPhoto("http://photo");
			provider.setSurname("surname");
			provider.setVATNumber("100");
			final Provider savedProvider = this.providerService.save(provider);
			Assert.notNull(this.providerService.findOne(savedProvider.getId()));
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
		UseCase9_3.randomInt++;
	}
}

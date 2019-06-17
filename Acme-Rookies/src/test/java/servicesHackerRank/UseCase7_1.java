
package servicesHackerRank;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ActorService;
import services.CompanyService;
import services.CreditCardService;
import services.RookieService;
import services.ServiceUtils;
import utilities.AbstractTest;
import domain.Company;
import domain.CreditCard;
import domain.Rookie;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UseCase7_1 extends AbstractTest {

	// An actor who is not authenticated must be able to register to the system as a company or a rookie.

	// Services

	private static Integer		uniqueness	= 10;

	@Autowired
	private CompanyService		companyService;
	@Autowired
	private RookieService		rookieService;
	@Autowired
	private ActorService		actorService;
	@Autowired
	private CreditCardService	creditCardService;
	@Autowired
	private ServiceUtils		serviceUtils;


	// Tests

	@Test
	public void testUseCase7_1() {
		// Un usuario anónimo se registra como company (POSITIVO)
		//b) Positive test
		//c) analysis of sentence coverage: 100%
		//d) An unauthenticated user can register in the system as a company
		this.registerAsCompanyDriver(null, null);
		// Un usuario registrado se registra como company, no puede porque ya tiene una cuenta (NEGATIVO)
		//b) Negative test
		//c) analysis of sentence coverage: 100%
		//d) An authenticated user can't register in the system as a company because he/she already has an account
		this.registerAsCompanyDriver("company1", IllegalArgumentException.class);
		// Un usuario anónimo se registra como rookie (POSITIVO)
		//b) Positive test
		//c) analysis of sentence coverage: 100%
		//d) An unauthenticated user can register in the system as a rookie
		this.registerAsRookieDriver(null, null);
		// Un usuario registrado se registra como rookie, no puede porque ya tiene una cuenta (NEGATIVO)
		//b) Negative test
		//c) analysis of sentence coverage: 100%
		//d) An authenticated user can't register in the system as a rookie because he/she already has an account
		this.registerAsRookieDriver("rookie1", IllegalArgumentException.class);
	}

	// Drivers

	private void registerAsCompanyDriver(final String username, final Class<?> expected) {
		super.authenticate(username);
		Class<?> caught = null;
		try {
			final Company company = this.companyService.create();
			final CreditCard creditCard = this.creditCardService.create();
			company.setAddress("address");
			company.setComercialName("comercialName");
			company.setEmail("email@email.com");
			company.setName("name");
			company.setPhone("phone");
			company.setPhoto("http://photo");
			company.setSurname("surname");
			company.setVATNumber("vATNumber");
			company.getUserAccount().setPassword("newCompany" + UseCase7_1.uniqueness);
			company.getUserAccount().setUsername("newCompany" + UseCase7_1.uniqueness);
			creditCard.setCVVCode(123);
			creditCard.setExpirationMonth(12);
			creditCard.setExpirationYear(2021);
			creditCard.setHolderName("holderName");
			creditCard.setMakeName("makeName");
			creditCard.setNumber("1111222233334444");
			UseCase7_1.uniqueness++;
			company.setCreditCard(creditCard);
			final Company savedCompany = this.companyService.save(company);
			Assert.notNull(this.companyService.findOne(savedCompany.getId()));
		} catch (final Throwable t) {
			caught = t.getClass();
			super.checkExceptions(expected, caught);
		}
		super.unauthenticate();
	}

	private void registerAsRookieDriver(final String username, final Class<?> expected) {
		super.authenticate(username);
		Class<?> caught = null;
		try {
			final Rookie rookie = this.rookieService.create();
			final CreditCard creditCard = this.creditCardService.create();
			rookie.setAddress("address");
			rookie.setEmail("email@email.com");
			rookie.setName("name");
			rookie.setPhone("phone");
			rookie.setPhoto("http://photo");
			rookie.setSurname("surname");
			rookie.setVATNumber("vATNumber");
			rookie.getUserAccount().setPassword("newRookie" + UseCase7_1.uniqueness);
			rookie.getUserAccount().setUsername("newRookie" + UseCase7_1.uniqueness);
			UseCase7_1.uniqueness++;
			creditCard.setCVVCode(123);
			creditCard.setExpirationMonth(12);
			creditCard.setExpirationYear(2021);
			creditCard.setHolderName("holderName");
			creditCard.setMakeName("makeName");
			creditCard.setNumber("1111222233334444");
			rookie.setCreditCard(creditCard);
			final Rookie savedRookie = this.rookieService.save(rookie);
			this.serviceUtils.flush();
			Assert.notNull(this.rookieService.findOne(savedRookie.getId()));
		} catch (final Throwable t) {
			caught = t.getClass();
			super.checkExceptions(expected, caught);
		}
		super.unauthenticate();
	}

}

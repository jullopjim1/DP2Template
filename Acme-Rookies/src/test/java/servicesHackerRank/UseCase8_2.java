
package servicesHackerRank;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.AdministratorService;
import services.AuditorService;
import services.CompanyService;
import services.ProviderService;
import services.RookieService;
import services.ServiceUtils;
import utilities.AbstractTest;
import domain.Administrator;
import domain.Auditor;
import domain.Company;
import domain.CreditCard;
import domain.Provider;
import domain.Rookie;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UseCase8_2 extends AbstractTest {

	// An actor who is not authenticated must be able to edit to the system as a company or a rookie.

	// Services

	private static Integer			uniqueness	= 10;

	@Autowired
	private CompanyService			companyService;
	@Autowired
	private RookieService			rookieService;
	@Autowired
	private AuditorService			auditorService;
	@Autowired
	private ProviderService			providerService;
	@Autowired
	private AdministratorService	administratorService;
	@Autowired
	private ServiceUtils			serviceUtils;


	// Tests

	@Test
	public void testUseCase8_2() {
		// Una company edita sus datos personales (POSITIVO)
		//b) Positive test
		//c) analysis of sentence coverage: 98.4%
		//d) A company can edit its personal data
		this.editCompanyDriver("company1", null);
		// Una company edita los datos personales de otra company (NEGATIVO)
		//b) Negative test
		//c) analysis of sentence coverage: 98.4%
		//d) A company cannot edit the personal data of other company
		this.editCompanyDriver("company2", IllegalArgumentException.class);
		// Un rookie edita sus datos personales (POSITIVO)
		//b) Positive test
		//c) analysis of sentence coverage: 98.4%
		//d) A rookie can edit his/her personal data
		this.editRookieDriver("rookie1", null);
		// Un rookie edita los datos personales de otro rookie (NEGATIVO)
		//b) Negative test
		//c) analysis of sentence coverage: 98.4%
		//d) A rookie cannot edit the personal data of other rookie
		this.editRookieDriver("rookie2", IllegalArgumentException.class);
		// Un administrator edita sus datos personales (POSITIVO)
		//b) Positive test
		//c) analysis of sentence coverage: 98.4%
		//d) An administrator can edit his/her personal data
		this.editAdminDriver("admin1", null);
		// Un administrator edita los datos personales de otro administrator (NEGATIVO)
		//b) Negative test
		//c) analysis of sentence coverage: 98.4%
		//d) An administrator cannot edit the personal data of other administrator
		this.editAdminDriver("admin2", IllegalArgumentException.class);
		// Un auditor edita sus datos personales (POSITIVO)
		//b) Positive test
		//c) analysis of sentence coverage: 98.4%
		//d) An auditor can edit his/her personal data
		this.editAuditorDriver("auditor1", null);
		// Un auditor edita los datos personales de otro auditor (NEGATIVO)
		//b) Negative test
		//c) analysis of sentence coverage: 98.4%
		//d) An auditor can't edit other auditor's personal data
		this.editAuditorDriver("auditor2", IllegalArgumentException.class);
		// Un provider edita sus datos personales (POSITIVO)
		//b) Positive test
		//c) analysis of sentence coverage: 98.5%
		//d) A provider can edit his/her personal data
		this.editProviderDriver("provider1", null);
		// Un provider edita los datos personales de otro provider (NEGATIVO)
		//b) Negative test
		//c) analysis of sentence coverage: 98.5%
		//d) A provider can't edit other provider's personal data
		this.editProviderDriver("provider2", IllegalArgumentException.class);
	}

	// Drivers

	private void editCompanyDriver(final String username, final Class<?> expected) {
		super.authenticate(username);
		Class<?> caught = null;
		try {
			final Company company = this.companyService.findOne(super.getEntityId("company1"));
			final CreditCard creditCard = company.getCreditCard();
			final Integer companyVersionBefore = company.getVersion();
			final Integer creditCardVersionBefore = creditCard.getVersion();
			company.setAddress("address");
			company.setComercialName("comercialName");
			company.setEmail("email@email.com");
			company.setName("name");
			company.setPhone("phone");
			company.setPhoto("http://photo");
			company.setSurname("surname");
			company.setVATNumber("vATNumber");
			company.getUserAccount().setPassword("newCompany" + UseCase8_2.uniqueness);
			UseCase8_2.uniqueness++;
			creditCard.setCVVCode(123);
			creditCard.setExpirationMonth(12);
			creditCard.setExpirationYear(2021);
			creditCard.setHolderName("holderName");
			creditCard.setMakeName("makeName");
			creditCard.setNumber("1111222233334444");
			company.setCreditCard(creditCard);
			final Company savedCompany = this.companyService.save(company);
			Assert.notNull(this.companyService.findOne(savedCompany.getId()));
			Assert.isTrue(savedCompany.getVersion() > companyVersionBefore);
			Assert.isTrue(savedCompany.getCreditCard().getVersion() > creditCardVersionBefore);
		} catch (final Throwable t) {
			caught = t.getClass();
			super.checkExceptions(expected, caught);
		}
		super.unauthenticate();
	}
	private void editRookieDriver(final String username, final Class<?> expected) {
		super.authenticate(username);
		Class<?> caught = null;
		try {
			final Rookie rookie = this.rookieService.findOne(super.getEntityId("rookie1"));
			final CreditCard creditCard = rookie.getCreditCard();
			final Integer rookieVersionBefore = rookie.getVersion();
			final Integer creditCardVersionBefore = creditCard.getVersion();
			rookie.setAddress("address");
			rookie.setEmail("email@email.com");
			rookie.setName("name");
			rookie.setPhone("phone");
			rookie.setPhoto("http://photo");
			rookie.setSurname("surname");
			rookie.setVATNumber("vATNumber");
			rookie.getUserAccount().setPassword("newRookie" + UseCase8_2.uniqueness);
			UseCase8_2.uniqueness++;
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
			Assert.isTrue(savedRookie.getVersion() > rookieVersionBefore);
			Assert.isTrue(savedRookie.getCreditCard().getVersion() > creditCardVersionBefore);
		} catch (final Throwable t) {
			caught = t.getClass();
			super.checkExceptions(expected, caught);
		}
		super.unauthenticate();
	}

	private void editAdminDriver(final String username, final Class<?> expected) {
		super.authenticate(username);
		Class<?> caught = null;
		try {
			final Administrator administrator = this.administratorService.findOne(super.getEntityId("administrator1"));
			final CreditCard creditCard = administrator.getCreditCard();
			final Integer administratorVersionBefore = administrator.getVersion();
			final Integer creditCardVersionBefore = creditCard.getVersion();
			administrator.setAddress("address");
			administrator.setEmail("email@email.com");
			administrator.setName("name");
			administrator.setPhone("phone");
			administrator.setPhoto("http://photo");
			administrator.setSurname("surname");
			administrator.setVATNumber("vATNumber");
			administrator.getUserAccount().setPassword("newAdministrator" + UseCase8_2.uniqueness);
			UseCase8_2.uniqueness++;
			creditCard.setCVVCode(123);
			creditCard.setExpirationMonth(12);
			creditCard.setExpirationYear(2021);
			creditCard.setHolderName("holderName");
			creditCard.setMakeName("makeName");
			creditCard.setNumber("1111222233334444");
			administrator.setCreditCard(creditCard);
			final Administrator savedAdministrator = this.administratorService.save(administrator);
			this.serviceUtils.flush();
			Assert.notNull(this.administratorService.findOne(savedAdministrator.getId()));
			Assert.isTrue(savedAdministrator.getVersion() > administratorVersionBefore);
			Assert.isTrue(savedAdministrator.getCreditCard().getVersion() > creditCardVersionBefore);
		} catch (final Throwable t) {
			caught = t.getClass();
			super.checkExceptions(expected, caught);
		}
		super.unauthenticate();
	}

	private void editAuditorDriver(final String username, final Class<?> expected) {
		super.authenticate(username);
		Class<?> caught = null;
		try {
			final Auditor auditor = this.auditorService.findOne(super.getEntityId("auditor1"));
			final CreditCard creditCard = auditor.getCreditCard();
			final Integer auditorVersionBefore = auditor.getVersion();
			final Integer creditCardVersionBefore = creditCard.getVersion();
			auditor.setAddress("address");
			auditor.setEmail("email@email.com");
			auditor.setName("name");
			auditor.setPhone("phone");
			auditor.setPhoto("http://photo");
			auditor.setSurname("surname");
			auditor.setVATNumber("vATNumber");
			auditor.getUserAccount().setPassword("newAuditor" + UseCase8_2.uniqueness);
			UseCase8_2.uniqueness++;
			creditCard.setCVVCode(123);
			creditCard.setExpirationMonth(12);
			creditCard.setExpirationYear(2021);
			creditCard.setHolderName("holderName");
			creditCard.setMakeName("makeName");
			creditCard.setNumber("1111222233334444");
			auditor.setCreditCard(creditCard);
			final Auditor savedAuditor = this.auditorService.save(auditor);
			this.serviceUtils.flush();
			Assert.notNull(this.auditorService.findOne(savedAuditor.getId()));
			Assert.isTrue(savedAuditor.getVersion() > auditorVersionBefore);
			Assert.isTrue(savedAuditor.getCreditCard().getVersion() > creditCardVersionBefore);
		} catch (final Throwable t) {
			caught = t.getClass();
			super.checkExceptions(expected, caught);
		}
		super.unauthenticate();
	}

	private void editProviderDriver(final String username, final Class<?> expected) {
		super.authenticate(username);
		Class<?> caught = null;
		try {
			final Provider provider = this.providerService.findOne(super.getEntityId("provider1"));
			final CreditCard creditCard = provider.getCreditCard();
			final Integer providerVersionBefore = provider.getVersion();
			final Integer creditCardVersionBefore = creditCard.getVersion();
			provider.setAddress("address");
			provider.setEmail("email@email.com");
			provider.setName("name");
			provider.setPhone("phone");
			provider.setPhoto("http://photo");
			provider.setSurname("surname");
			provider.setVATNumber("vATNumber");
			provider.setMake("make");
			provider.getUserAccount().setPassword("newProvider" + UseCase8_2.uniqueness);
			UseCase8_2.uniqueness++;
			creditCard.setCVVCode(123);
			creditCard.setExpirationMonth(12);
			creditCard.setExpirationYear(2021);
			creditCard.setHolderName("holderName");
			creditCard.setMakeName("makeName");
			creditCard.setNumber("1111222233334444");
			provider.setCreditCard(creditCard);
			final Provider savedProvider = this.providerService.save(provider);
			this.serviceUtils.flush();
			Assert.notNull(this.providerService.findOne(savedProvider.getId()));
			Assert.isTrue(savedProvider.getVersion() > providerVersionBefore);
			Assert.isTrue(savedProvider.getCreditCard().getVersion() > creditCardVersionBefore);
		} catch (final Throwable t) {
			caught = t.getClass();
			super.checkExceptions(expected, caught);
		}
		super.unauthenticate();
	}

}

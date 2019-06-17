
package servicesRookies;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import security.UserAccount;
import services.ActorService;
import services.PositionService;
import services.ProviderService;
import services.SponsorshipService;
import utilities.AbstractTest;
import domain.Actor;
import domain.Provider;
import domain.Sponsorship;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UseCase13_1 extends AbstractTest {

	//9. An actor who is authenticated as a company must be able to:
	/*
	 * 13. An actor who is authenticated as a provider must be able to:
	 * 1. Manage his or her sponsorships, which includes listing, showing, creating, updating,
	 * and deleting them.
	 */
	//Services---------------------------------------------------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private SponsorshipService	sponsorshipService;

	@Autowired
	private ProviderService		providerService;

	@Autowired
	PositionService				positionService;


	@Test
	public void sponsorshipTest() {
		final Object AccessDashBoardTest[][] = {
			{
				"FalseProvider", "TEST", java.lang.IllegalArgumentException.class

			//Probamos con un user "Provider" que no exista y que no debia editar los "Sponsorships"
			//b) Negative test
			//c) analysis of sentence coverage: 99.2%
			//d) This user doesn't exists, so it cannot manage it sponsorships
			},

			{
				"provider2", "TEST", null
			//Este "Provider" si esta registrado en el sistema y puede editar sus "Sponsorships"(CASO POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage: 99.2%
			//d) A Provider should manage their sponsorships, which includes listing, showing, creating, updating, and deleting them
			},

		};
		for (int i = 0; i < AccessDashBoardTest.length; i++)
			this.sponsorshipTemplate((String) AccessDashBoardTest[i][0], (String) AccessDashBoardTest[i][1], (Class<?>) AccessDashBoardTest[i][2]);
	}

	private void sponsorshipTemplate(final String username, final String name, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		final String result = "https://www.googler.com";
		try {
			//Nos autenticamos
			this.authenticate(username);
			//Encontramos el actor cuyo nombre de usuario es igual a que pasamos por parámetros

			final Actor a = this.actorService.findActorByUsername(username);

			final UserAccount uA = a.getUserAccount();
			final Provider h = this.providerService.findProviderByUserAcountId(uA.getId());

			final Sponsorship sponsorship = this.sponsorshipService.create();

			//Create a sponsorship
			sponsorship.setTarget("https://www.googler.com");
			sponsorship.setBanner("https://www.googler.com");

			//Guardo un sponsorship
			this.sponsorshipService.save(sponsorship);

			Assert.isTrue(result.equals(sponsorship.getBanner()));
			Assert.isTrue(result.equals(sponsorship.getTarget()));

			//Borro una sponsorship
			final List<Sponsorship> spons = (List<Sponsorship>) this.sponsorshipService.findSponsorshipsByProviderId(h.getId());
			this.sponsorshipService.delete(spons.get(0));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}

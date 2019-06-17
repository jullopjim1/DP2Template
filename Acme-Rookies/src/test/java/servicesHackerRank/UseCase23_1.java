
package servicesHackerRank;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.ActorService;
import services.SocialProfileService;
import utilities.AbstractTest;
import domain.Actor;
import domain.SocialProfile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UseCase23_1 extends AbstractTest {

	//23. An actor who is authenticatedmust be able to:
	/*
	 * 1. Manage his or her social profiles, which includes listing, showing, creating, updating,
	 * and deleting them
	 */
	//Services---------------------------------------------------------------------

	@Autowired
	private ActorService			actorService;

	@Autowired
	private SocialProfileService	socialProfileService;


	@Test
	public void authorityTest() {
		final Object AccessDashBoardTest[][] = {
			{

				"company1", "TEST", null
			//Este usuario si esta registrado en el sistema y puede editar sus applications(CASO POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage: 96.8%
			//d) A company should manage their positions, which includes listing, showing, creating, updating, and deleting them
			}, {
				"rookie1", "TEST", null
			//Este usuario si esta registrado en el sistema y puede editar sus applications(CASO POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage: 96.8%
			//d) A company shouldn't manage others positions.
			},

		};
		for (int i = 0; i < AccessDashBoardTest.length; i++)
			this.applicationTemplate((String) AccessDashBoardTest[i][0], (String) AccessDashBoardTest[i][1], (Class<?>) AccessDashBoardTest[i][2]);
	}
	private void applicationTemplate(final String username, final String name, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			//Nos autenticamos
			this.authenticate(username);
			//Encontramos el actor autenticado
			final Actor actor = this.actorService.findActorByUsername(username);
			//Encontramos los socialProfile que pertenecen a ese actor
			final List<SocialProfile> socialProfiles = (List<SocialProfile>) this.socialProfileService.findProfileByActorId(actor.getId());

			// Cogemos un socialProfile
			final SocialProfile sp = socialProfiles.get(0);

			// Edito un socialProfile
			sp.setNameSN(name);
			//Creo una application
			final SocialProfile spCreated = this.socialProfileService.create(actor.getId());
			spCreated.setLinkSN("https://link1.com");
			spCreated.setNameSN(name);
			spCreated.setNick(name);

			//Guardo un socialProfile
			this.socialProfileService.save(sp);
			this.socialProfileService.save(spCreated);

			// Borro un socialProfile
			this.socialProfileService.delete(sp);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}

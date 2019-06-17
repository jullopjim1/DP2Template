
package servicesHackerRank;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.ActorService;
import services.ApplicationService;
import services.PositionService;
import services.RookieService;
import utilities.AbstractTest;
import domain.Application;
import domain.Company;
import domain.Position;
import domain.Rookie;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UseCase9_3 extends AbstractTest {

	//9. An actor who is authenticated as a company must be able to:
	/*
	 * 3. Manage the applications to their positions, which includes listing them grouped by
	 * status, showing them, and updating them. Updating an application amounts to
	 * making a decision on it: an application whose status is SUBMITTED may change to
	 * status ACCEPTED or REJECTED.
	 */
	//Services---------------------------------------------------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private PositionService		positionService;

	@Autowired
	private RookieService		rookieService;


	@Test
	public void authorityTest() {
		final Object AccessDashBoardTest[][] = {
			{
				"FalseBoy", "TEST", IllegalArgumentException.class
			//				InvalidDataAccessResourceUsageException.class

			//Probamos con un user company que no exista y que no debia editar sus datos(CASO NEGATIVO)
			//b) Negative test
			//c) analysis of sentence coverage: 100%
			//d) This user doesn't exists, so it cannot manage it positions
			}, {
				"company1", "TEST", null
			//Este usuario si esta registrado en el sistema y puede editar sus applications(CASO POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage: 100%
			//d) A company should manage their positions, which includes listing, showing, creating, updating, and deleting them
			}, {
				"company3", "TEST", null
			//Este usuario si esta registrado en el sistema y puede editar sus applications(CASO POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage: 100%
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
			//Encontramos el actor cuyo nombre de usuario es igual a que pasamos por parámetros
			final Company a = (Company) this.actorService.findActorByUsername(username);

			//			Sacamos la lista de las application que pertenecen a esa company
			final List<Application> apps = (List<Application>) this.applicationService.findApplicationByStatusAndCompany(a.getId());
			//			Sacamos la lista de las position que pertenecen a esa company
			final List<Position> pos = (List<Position>) this.positionService.findPositionsByCompanyId(a.getId());

			//			Elegimos una application
			final Application app = apps.get(0);
			//			Sacamos el id de una position
			final int positionId = pos.get(0).getId();
			//			Cogemos un rookie
			final Rookie rookie = this.rookieService.findAll().get(0);

			// Edito una application
			app.setStatus("SUBMITTED");
			//Creo una application
			final Application appCreated = this.applicationService.create(positionId);
			appCreated.setRookie(rookie);

			//Guardo una application
			this.applicationService.save(app);
			this.applicationService.save(appCreated);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}

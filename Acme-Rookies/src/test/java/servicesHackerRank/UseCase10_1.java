
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
import services.CompanyService;
import services.PositionService;
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
public class UseCase10_1 extends AbstractTest {

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
	private CompanyService		companyService;


	@Test
	public void authorityTest() {
		final Object AccessDashBoardTest[][] = {
			{
				"FalseBoy", "TEST", IllegalArgumentException.class
			//				InvalidDataAccessResourceUsageException.class

			//Probamos con un user rookie que no exista y que no debia editar sus datos(CASO NEGATIVO)
			//b) Negative test
			//c) analysis of sentence coverage: 100%
			//d) This user doesn't exists, so it cannot manage it positions
			}, {
				"rookie1", "TEST", null
			//Este usuario si esta registrado en el sistema y puede editar sus applications(CASO POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage: 100%
			//d) A company should manage their positions, which includes listing, showing, creating, updating, and deleting them
			}, {
				"rookie2", "TEST", null
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
			final Rookie a = (Rookie) this.actorService.findActorByUsername(username);

			//			Sacamos una company
			final Company company = this.companyService.findAll().get(0);

			//			Sacamos la lista de las application que pertenecen a esa company
			final List<Application> apps = this.applicationService.findApplicationByRookie(a);
			//			Sacamos la lista de las position que pertenecen a esa company
			final List<Position> pos = (List<Position>) this.positionService.findPositionsByCompanyId(company.getId());

			//			Elegimos una application
			final Application app = apps.get(0);
			//			Sacamos el id de una position
			final int positionId = pos.get(0).getId();

			// Edito una application
			app.setStatus("SUBMITTED");
			//Creo una application
			final Application appCreated = this.applicationService.create(positionId);

			//Guardo una application
			this.applicationService.save(app);
			this.applicationService.save(appCreated);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}

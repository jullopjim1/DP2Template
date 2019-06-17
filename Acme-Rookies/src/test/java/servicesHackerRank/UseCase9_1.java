
package servicesHackerRank;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ActorService;
import services.PositionService;
import utilities.AbstractTest;
import domain.Company;
import domain.Position;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UseCase9_1 extends AbstractTest {

	//9. An actor who is authenticated as a company must be able to:
	/*
	 * 1. Manage their positions, which includes listing, showing, creating, updating, and deleting them. Positions can be saved in draft mode; they are not available publicly
	 * until they are saved in final mode. Once a position is saved in final mode, it cannot
	 * be further edited, but it can be cancelled. A position cannot be saved in final mode
	 * unless there are at least two problems associated with it
	 */
	//Services---------------------------------------------------------------------

	@Autowired
	private ActorService	actorService;

	@Autowired
	private PositionService	positionService;


	@Test
	public void positionTest() {
		final Object AccessDashBoardTest[][] = {
			{
				"FalseBoy", "TEST", java.lang.IllegalArgumentException.class
			//Probamos con un user company que no exista y que no debia editar sus datos(CASO NEGATIVO)
			//b) Negative test
			//c) analysis of sentence coverage: 97.1%
			//d) This user doesn't exists, so it cannot manage it positions
			}, {
				"NoExistingUser", "TEST", java.lang.IllegalArgumentException.class
			//Probamos con un user company que no exista y que no debia editar sus datos(CASO NEGATIVO)
			//b) Negative test
			//c) analysis of sentence coverage: 97.1%
			//d) This user doesn't exists, so it cannot manage it positions
			}, {
				"company199", "TEST", java.lang.IllegalArgumentException.class
			//Este admin si esta registrado en el sistema y puede editar sus datos personales(CASO NEGATIVO)
			//b) Negative test
			//c) analysis of sentence coverage: 97.1%
			//d) A company should manage their positions, which includes listing, showing, creating, updating, and deleting them
			}, {
				"company1", "TEST", null
			//Este admin si esta registrado en el sistema y puede editar sus datos personales(CASO POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage: 97.1%
			//d) A company should manage their positions, which includes listing, showing, creating, updating, and deleting them
			}, {
				"company2", "TEST", null
			//Este admin si esta registrado en el sistema y puede editar sus datos personales(CASO POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage: 97.1%
			//d) A company should manage their positions, which includes listing, showing, creating, updating, and deleting them
			}, {
				"company3", "TEST", null
			//Este admin si esta registrado en el sistema y puede editar sus datos personales(CASO POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage: 97.1%
			//d) A company should manage their positions, which includes listing, showing, creating, updating, and deleting them
			}, {
				"company4", "TEST", java.lang.IllegalArgumentException.class
			//Este admin si esta registrado en el sistema y puede editar sus datos personales(CASO NEGATIVO)
			//b) Negative test
			//c) analysis of sentence coverage: 97.1%
			//d) A company should manage their positions, which includes listing, showing, creating, updating, and deleting them
			},

		};
		for (int i = 0; i < AccessDashBoardTest.length; i++)
			this.positionTemplate((String) AccessDashBoardTest[i][0], (String) AccessDashBoardTest[i][1], (Class<?>) AccessDashBoardTest[i][2]);
	}

	private void positionTemplate(final String username, final String name, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		final String result = "TEST";
		try {
			//Nos autenticamos
			this.authenticate(username);
			//Encontramos el actor cuyo nombre de usuario es igual a que pasamos por parámetros
			final Company a = (Company) this.actorService.findActorByUsername(username);
			//
			final List<Position> res = (List<Position>) this.positionService.findPositionsByCompanyId(a.getId());
			final Position p = res.get(0);
			//Guardamos el cambio
			final Position pos = this.positionService.create();

			final Date date = new Date(12, 12, 2019);
			//
			//			// Edito una position
			p.setDescription("TEST");
			//			//Creo una position
			pos.setDeadLine(date);
			pos.setDescription("TEST");
			pos.setProfile("TEST");
			pos.setSalary(14000.0);
			pos.setSkills("TEST");
			pos.setTechnologies("TEST");
			pos.setTitle("TEST");
			//
			if (username.equals("company1"))
				Assert.isTrue(result.equals(p.getDescription()));
			Assert.isTrue(result.equals(pos.getDescription()));
			final int id = pos.getId();
			Assert.isTrue((pos.isFinalMode() == false));
			//
			//			//Borro una position
			this.positionService.delete(pos);
			Assert.isTrue(0 == pos.getId());

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	@Test
	public void positionTest2() {
		final Object AccessDashBoardTest[][] = {
			{
				"FalseBoy", "TEST", java.lang.IllegalArgumentException.class
			//Probamos con un user company que no exista y que no debia editar sus datos(CASO NEGATIVO)
			//b) Negative test
			//c) analysis of sentence coverage: 96.3%
			//d) This user doesn't exists, so it cannot manage it positions
			}, {
				"company1", "TEST", null
			//Este admin si esta registrado en el sistema y puede editar sus datos personales(CASO POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage: 96.3%
			//d) A company should manage their positions, which includes listing, showing, creating, updating, and deleting them
			}, {
				"company2", "TEST", null
			//Este admin si esta registrado en el sistema y puede editar sus datos personales(CASO POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage: 96.3%
			//d) A company should manage their positions, which includes listing, showing, creating, updating, and deleting them
			},

		};
		for (int i = 0; i < AccessDashBoardTest.length; i++)
			this.positionTemplate2((String) AccessDashBoardTest[i][0], (String) AccessDashBoardTest[i][1], (Class<?>) AccessDashBoardTest[i][2]);
	}

	private void positionTemplate2(final String username, final String name, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		final String result = "TEST";
		try {
			//Nos autenticamos
			this.authenticate(username);
			//Encontramos el actor cuyo nombre de usuario es igual a que pasamos por parámetros
			final Company a = (Company) this.actorService.findActorByUsername(username);
			//
			//Guardamos el cambio
			final Position aBorrar = this.positionService.create();

			final Date date = new Date(12, 12, 2019);
			//
			//Creo una position
			aBorrar.setDeadLine(date);
			aBorrar.setDescription("TEST");
			aBorrar.setProfile("TEST");
			aBorrar.setSalary(14000.0);
			aBorrar.setSkills("TEST");
			aBorrar.setTechnologies("TEST");
			aBorrar.setTitle("TEST");
			//
			Assert.isTrue(result.equals(aBorrar.getDescription()));
			final int id = aBorrar.getId();
			Assert.isTrue((aBorrar.isFinalMode() == false));
			//
			//Borro una position
			this.positionService.delete(aBorrar);
			Assert.isTrue(0 == aBorrar.getId());

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}

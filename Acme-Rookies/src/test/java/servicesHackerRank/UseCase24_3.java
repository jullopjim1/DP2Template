
package servicesHackerRank;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ActorService;
import services.RookieService;
import utilities.AbstractTest;
import domain.Rookie;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UseCase24_3 extends AbstractTest {

	/*
	 * 24 An actor who is authenticated as an administrator must be able to:
	 * 3. Ban an actor with the spammer flag.
	 */
	//Services---------------------------------------------------------------------

	@Autowired
	private ActorService	actorService;

	@Autowired
	private RookieService	rookieService;


	@Test
	public void BanTest() {
		final Object AccessDashBoardTest[][] = {
			{
				"NonAdmin", java.lang.IllegalArgumentException.class
			//Probamos con un user admin que no exista y que no debia editar sus datos(CASO NEGATIVO)
			//b) Negative test
			//c) analysis of sentence coverage: 100%
			//d) This user doesn't exists, so it cannot manage it positions
			}, {
				"admin1", null
			//Este admin si esta registrado en el sistema y puede banear usuarios sospechosos(CASO POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage: 100%
			//d) Ban an actor with the spammer flag.
			},

		};
		for (int i = 0; i < AccessDashBoardTest.length; i++)
			this.banTemplate((String) AccessDashBoardTest[i][0], (Class<?>) AccessDashBoardTest[i][1]);
	}

	private void banTemplate(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		final Rookie rookie1 = (Rookie) this.actorService.findActorByUsername("rookie1");

		try {
			//Nos autenticamos
			this.authenticate(username);
			//Lo baneamos

			rookie1.setBanned(true);

			//	this.rookieService.save(rookie1);

			this.unauthenticate();
			Assert.isTrue(rookie1.getBanned() == true);

			this.authenticate("rookie1");

			//Guardamos el cambio

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	//------------------------------------------

	@Test
	public void BanAccessTest() {
		final Object AccessDashBoardTest[][] = {
			{
				"rookie2", java.lang.IllegalArgumentException.class
			//Probamos con un rookie que no es spammer por lo que no debe dejar banear(CASO NEGATIVO)
			//b) Negative test
			//c) analysis of sentence coverage: 100%
			//d) This user doesn't has spammer true, so it cannot be banned
			}, {
				"rookie1", null
			//Probamos con un rookie que es spammer por lo que no debe dejar banear(CASO POSITIVO)
			//b) Positove test
			//c) analysis of sentence coverage: 100%
			//d) This user does has spammer true, so it can be banned
			},

		};
		for (int i = 0; i < AccessDashBoardTest.length; i++)
			this.banTemplate2((String) AccessDashBoardTest[i][0], (Class<?>) AccessDashBoardTest[i][1]);
	}

	private void banTemplate2(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		final Rookie rookie1 = (Rookie) this.actorService.findActorByUsername("rookie1");

		try {
			//Nos autenticamos
			this.authenticate(username);
			//Lo baneamos
			final Rookie a = (Rookie) this.actorService.findActorByUsername(username);
			a.setBanned(true);
			//guardamos
			this.rookieService.save(a);
			this.unauthenticate();
			// comprobamos que esta baneado
			Assert.isTrue(rookie1.getBanned() == true);
			//tratamos de autenticarnos
			this.authenticate(username);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}

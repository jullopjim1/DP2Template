
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
public class UseCase24_4 extends AbstractTest {

	/*
	 * 24 An actor who is authenticated as an administrator must be able to:
	 * 4. Unban an actor who was banned previously
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
			//Probamos con un user admin que no exista y que no debia editar estos datos(CASO NEGATIVO)
			//b) Negative test
			//c) analysis of sentence coverage: 97.9%
			//d) This user doesn't exists, so it cannot manage and unban spammers actors
			}, {
				"admin1", null
			//Este admin si esta registrado en el sistema y puede banear usuarios sospechosos(CASO POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage: 97.9%
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
			//Lo desbaneamos

			//	this.rookieService.save(rookie1);
			rookie1.setBanned(false);

			this.unauthenticate();
			Assert.isTrue(rookie1.getBanned() == false);

			this.authenticate("rookie1");

			//Guardamos el cambio

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	//------------------------------------------

	@Test
	public void UnBanAccessTest() {
		final Object AccessDashBoardTest[][] = {
			{
				"rookie1", null
			//Probamos con un rookie que no es spammer por lo que no debe dejar banear(CASO POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage: 96%
			//d) This user is spammer  and we should be able to ban and unban him
			}, {
				"USERNOEXISTENTE", java.lang.IllegalArgumentException.class
			//Probamos con un rookie que no es spammer por lo que no debe dejar banear(CASO NEGATIVO)
			//b) Negative test
			//c) analysis of sentence coverage: 96%
			//d) This user doesn´t exist and can´t be banned or unbanned 
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
			//lo desbanemos
			a.setBanned(false);

			//guardamos
			this.rookieService.save(a);
			this.unauthenticate();
			// comprobamos que esta desbaneado
			Assert.isTrue(a.getBanned() == false);
			//tratamos de autenticarnos
			this.authenticate(username);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}

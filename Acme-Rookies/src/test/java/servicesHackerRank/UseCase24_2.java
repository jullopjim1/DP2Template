
package servicesHackerRank;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.ActorService;
import services.AdministratorService;
import utilities.AbstractTest;
import domain.Actor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UseCase24_2 extends AbstractTest {

	//24. An actor who is authenticated as an administrator must be able to:
	//2. Launch a process that flags the actors of the system as spammers or not-spammers. 
	//A user is considered to be a spammer if at least 10% of the messages that 
	//he or she sent contain at least one spam word.

	//Service----------------------------------------------------------------------

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private ActorService			actorService;


	//Driver-----------------------------------------------------------------------

	@Test
	public void DisplayTest() {
		System.out.println("=====DISPLAY=====");
		final Object testingData[][] = {
			{
				"rookie1", IllegalArgumentException.class
			//Probamos con un usuario que no es un admin(CASO NEGATIVO)
			//b) Negative test
			//c) analysis of sentence coverage: 100%
			//d) This user isn´t a admin, so it cannot manage the spammers actors
			}, {
				"admin1", null
			//Este admin si esta registrado en el sistema y puede actualizar los actores spammers(CASO POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage: 100%
			//d) This user is a admin, so it can manage the spammers actors
			},

		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.templateSpammer((String) testingData[i][0], (Class<?>) testingData[i][1]);
			j++;
		}
	}

	private void templateSpammer(final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			//Nos autenticamos con el username pasado por parámetro
			this.authenticate(username);
			//Queries dashboard
			this.administratorService.generateAllSpammers();
			final Collection<Actor> actors = this.actorService.findAllExceptMe();
			//Mostramos la lista de actores spammer actualizada
			for (final Actor a : actors)
				System.out.println(a.getName());
			//Nos desautenticamos
			this.unauthenticate();

			System.out.println("\n");
			System.out.println("Mostrados correctamente.");
			System.out.println("-----------------------------");

		} catch (final Throwable oops) {
			caught = oops.getClass();
			System.out.println(caught);
			System.out.println("-----------------------------");
		}

		this.checkExceptions(expected, caught);
	}
}

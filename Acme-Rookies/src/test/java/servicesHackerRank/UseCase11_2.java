
package servicesHackerRank;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.ApplicationService;
import services.PositionService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UseCase11_2 extends AbstractTest {

	//11. An actor who is authenticated as an administrator must be able to:
	//	2. Display a dashboard 

	//Service----------------------------------------------------------------------

	@Autowired
	private PositionService		positionService;

	@Autowired
	private ApplicationService	applicationService;


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
			//d) This user isn´t a admin, so it cannot manage the dashboard

			}, {
				"admin1", null
			//Este admin si esta registrado en el sistema y puede ver el dashboard(CASO POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage: 100%
			//d) This user is a admin, so it can manage the dashboard
			},

		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.templateDisplayDashboard((String) testingData[i][0], (Class<?>) testingData[i][1]);
			j++;
		}
	}

	private void templateDisplayDashboard(final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			//Nos autenticamos con el username pasado por parámetro
			this.authenticate(username);
			//Queries dashboard
			System.out.println(this.positionService.queryC1AVG() + "\n");
			System.out.println(this.positionService.queryC1MAX() + "\n");
			System.out.println(this.positionService.queryC1MIN() + "\n");
			System.out.println(this.positionService.queryC1STDDEV() + "\n");
			System.out.println(this.positionService.queryC3() + "\n");
			System.out.println(this.positionService.queryC5() + "\n");
			System.out.println(this.positionService.queryC6Best() + "\n");
			System.out.println(this.positionService.queryC6Worst() + "\n");
			System.out.println(this.applicationService.queryC2AVG() + "\n");
			System.out.println(this.applicationService.queryC2MAX() + "\n");
			System.out.println(this.applicationService.queryC2MIN() + "\n");
			System.out.println(this.applicationService.queryC2STDDEV() + "\n");
			System.out.println(this.applicationService.queryC4() + "\n");

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

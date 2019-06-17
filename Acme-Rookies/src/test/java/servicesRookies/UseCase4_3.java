
package servicesRookies;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.ActorService;
import services.AdministratorService;
import services.CompanyService;
import utilities.AbstractTest;
import domain.Company;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UseCase4_3 extends AbstractTest {

	//24. An actor who is authenticated as an administrator must be able to:
	//	Launch a process to compute an audit score for every company. The audit score is
	//	computed as the average of the audit scores that the positions offered by a company has got, but normalised to range 0.00 up to +1.00 using a linear homothetic transformation. Note that the audit score of a company that hasn’t got any audits is not
	//	0.00, but nil

	//Service----------------------------------------------------------------------

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private CompanyService			companyService;


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
			this.administratorService.generateCompanyScores();
			final Collection<Company> companies = this.companyService.findAll();
			//Mostramos la lista de companies actualizada
			for (final Company a : companies) {
				System.out.println("Name->" + a.getName() + "\n");
				System.out.println("Score->" + a.getScore() + "\n\n");
			}
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

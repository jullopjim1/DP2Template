
package servicesHackerRank;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.CompanyService;
import services.PositionService;
import utilities.AbstractTest;
import domain.Company;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UseCase7_3 extends AbstractTest {

	//7. An actor who is not authenticated must be able to:
	//	3.	List the companies available and navigate to the corresponding positions.

	//Service----------------------------------------------------------------------

	@Autowired
	private PositionService	positionService;
	@Autowired
	private CompanyService	companyService;


	//Driver-----------------------------------------------------------------------

	@Test
	public void findTest() {
		final Object AccessListAndDisplayPositionsTest[][] = {
			{
				"company56", java.lang.IllegalArgumentException.class
			//Probamos con un usuario que no exista y por lo tanto no debe mostrar las positions(CASO NEGATIVO)
			//b) Negative test
			//c) analysis of sentence coverage: 100%
			//d) Using a non existent actor, who is a company, so the user cannot see any position
			}, {
				"company1", null
			//Este hermandad si esta registrado en el sistema y deberia ver la historia(CASO POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage: 100%
			//d) The user is a registered company
			}, {
				"rookie1", null
			//Este rookie si esta registrado en el sistema y deberia ver las positions(CASO POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage: 100%
			//d) The user is a registered rookie
			},

		};
		for (int i = 0; i < AccessListAndDisplayPositionsTest.length; i++)
			this.findTemplate((String) AccessListAndDisplayPositionsTest[i][0], (Class<?>) AccessListAndDisplayPositionsTest[i][1]);
	}

	//Metodo para comprobar el display
	private void findTemplate(final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			//Nos autenticamos
			this.authenticate(username);
			//Cogemos una position y todas tambien
			this.companyService.findAll();
			this.positionService.findAll();
			final List<Company> res1 = this.companyService.findAll();
			final Company p1 = res1.get(0);
			final List positions = (List) this.positionService.findPositionsByCompanyId(p1.getId());
			positions.get(0);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}


package servicesHackerRank;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.PositionService;
import utilities.AbstractTest;
import domain.Position;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UseCase7_2 extends AbstractTest {

	//7. An actor who is not authenticated must be able to:
	//	4.	List the positions available and navigate to the corresponding companies

	//Service----------------------------------------------------------------------

	@Autowired
	private PositionService	positionService;


	//Driver-----------------------------------------------------------------------

	@Test
	public void authorityTest() {
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
			this.AuthorityMethod((String) AccessListAndDisplayPositionsTest[i][0], (Class<?>) AccessListAndDisplayPositionsTest[i][1]);
	}

	//Metodo para comprobar el display
	private void AuthorityMethod(final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			//Nos autenticamos
			this.authenticate(username);
			//Cogemos una position y todas tambien
			//this.positionService.findAll().get(0);
			this.positionService.findAll();
			final List<Position> res = (List<Position>) this.positionService.findAll();
			final Position p = res.get(0);
			p.getCompany();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}

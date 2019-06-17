
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
public class UseCase7_4 extends AbstractTest {

	//7. An actor who is not authenticated must be able to:
	//	4.	Search for a position using a single key word that must be contained in its title, its
	//		description, its profile, its skills, its technologies, or the name of the corresponding
	//		company.

	//Service----------------------------------------------------------------------

	@Autowired
	private PositionService	positionService;


	//Driver-----------------------------------------------------------------------

	@Test
	public void driverListing() {
		System.out.println("=====SEARCHING=====");
		final Object testingData[][] = {
			{
				null, "aux", null

			}, {
				null, "Aux", null
			}, {
				null, "google", null
			}

		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.templateSearching((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			j++;
		}
	}

	//Template------------------------------------------------------------------------

	protected void templateSearching(final String username, final String keyword, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Buscamos las pocisiones
			final List<Position> positions = this.positionService.searchingPositions(keyword);
			for (final Position position : positions)
				System.out.println(position.getTitle() + ", " + position.getCompany().getName());

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

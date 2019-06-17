
package servicesHackerRank;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.FinderService;
import utilities.AbstractTest;
import domain.Finder;
import domain.Position;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UseCase17_2 extends AbstractTest {

	//17. An actor who is authenticated as a rookie must be able to:
	//	2.	Manage his or her finder, which involves updating the
	//		search criteria, listing its contents, and clearing it.

	//Service----------------------------------------------------------------------

	@Autowired
	private FinderService	finderService;


	//Driver-----------------------------------------------------------------------

	@Test
	public void driverUpdating() {
		System.out.println("=====FINDER UPDATING=====");
		final Object testingData[][] = {
			{
				"rookie1", "", null

			}, {
				"rookie1", "Aux", null
			}, {
				null, "google", IllegalArgumentException.class
			}

		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.templateUpdating((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			j++;
		}
	}

	//Template------------------------------------------------------------------------

	protected void templateUpdating(final String username, final String keyword, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			this.authenticate(username);
			Finder finder = this.finderService.findOneByPrincipal();

			if (finder != null) {
				finder.setKeyword(keyword);
				final Finder saved = this.finderService.save(finder);

				//Buscamos las pocisiones
				final List<Position> positions = saved.getPositions();
				for (final Position position : positions)
					System.out.println(position.getTitle() + ", " + position.getCompany().getName());

				System.out.println("\n");
				System.out.println("Mostrados correctamente.");
				System.out.println("-----------------------------");
			} else {
				finder = this.finderService.create();
				finder.setKeyword(keyword);
				final Finder saved = this.finderService.save(finder);

				final List<Position> positions = saved.getPositions();
				for (final Position position : positions)
					System.out.println(position.getTitle() + ", " + position.getCompany().getName());

				System.out.println("\n");
				System.out.println("Mostrados correctamente.");
				System.out.println("-----------------------------");

			}
		} catch (final Throwable oops) {
			caught = oops.getClass();

			System.out.println(caught);
			System.out.println("-----------------------------");
		}
		this.checkExceptions(expected, caught);
	}

}

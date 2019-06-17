
package servicesRookies;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.Item;
import services.ItemService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UseCase9_2 extends AbstractTest {

	//9. An actor who is not authenticated must be able to:
	//	2.	Browse the list of items and navigate to their providers.

	//Service----------------------------------------------------------------------

	@Autowired
	private ItemService itemService;


	//Driver-----------------------------------------------------------------------

	@Test
	public void authorityTest() {
		final Object testingData[][] = {
			{
				"company56", java.lang.IllegalArgumentException.class
			//Probamos con un usuario que no exista y por lo tanto no debe mostrar los items(CASO NEGATIVO)
			//b) Negative test
			//c) analysis of sentence coverage: 100
			//d) Using a non existent actor, who is a company, so the user cannot see any items
			}, {
				null, null
			//Un actor que no esta registrado deberia ver los items(CASO POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage:100
			//d) The user is a not registered user
			}, {
				"rookie1", null
			//Este rookie si esta registrado en el sistema y deberia ver los items(CASO POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage:100
			//d) The user is a registered rookie
			},

		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.AuthorityMethod((String) testingData[i][0], (Class<?>) testingData[i][1]);
			j++;
		}
	}

	//Metodo para comprobar el display
	private void AuthorityMethod(final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			//Nos autenticamos
			this.authenticate(username);
			final List<Item> res = this.itemService.findAll();
			for (final Item item : res)
				System.out.println(item.getName());

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

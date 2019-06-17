
package servicesHackerRank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ActorService;
import services.ProblemService;
import utilities.AbstractTest;
import domain.Company;
import domain.Problem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UseCase9_2 extends AbstractTest {

	//9. An actor who is authenticated as a company must be able to:
	/*
	 * 2. Manage their database of problems, which includes listing,
	 * showing, creating, updating, and deleting them. Problems can be saved in draft mode;
	 * once they are saved in final mode, they cannot not be edited.
	 */
	//Services---------------------------------------------------------------------

	@Autowired
	private ActorService	actorService;

	@Autowired
	private ProblemService	problemService;


	@Test
	public void authorityTest() {
		final Object AccessDashBoardTest[][] = {
			{
				"FalseBoy", "TEST", java.lang.IllegalArgumentException.class

			//Probamos con un user company que no exista y que no debia editar sus datos(CASO NEGATIVO)
			//b) Negative test
			//c) analysis of sentence coverage: 99.4%
			//d) This user doesn't exists, so it cannot manage it positions
			},

			{
				"company1", "TEST", null
			//			//Este admin si esta registrado en el sistema y puede editar sus datos personales(CASO POSITIVO)
			//			//b) Positive test
			//			//c) analysis of sentence coverage: 99.4%
			//			//d) A company should manage their positions, which includes listing, showing, creating, updating, and deleting them
			},

		};
		for (int i = 0; i < AccessDashBoardTest.length; i++)
			this.problemTemplate((String) AccessDashBoardTest[i][0], (String) AccessDashBoardTest[i][1], (Class<?>) AccessDashBoardTest[i][2]);
	}
	private void problemTemplate(final String username, final String name, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		final String result = "TEST";
		try {
			//Nos autenticamos
			this.authenticate(username);
			//Encontramos el actor cuyo nombre de usuario es igual a que pasamos por parámetros
			final Company a = (Company) this.actorService.findActorByUsername(username);

			final List<Problem> res = (List<Problem>) this.problemService.findProblemsByCompanyId(a.getId());
			final Problem p = res.get(0);
			System.out.println(p.getTitle() + "-->" + p.isFinalMode());

			final Problem pro = this.problemService.create();

			// Edito un problema
			p.setStatement("TEST");
			//Creo un problem
			final Collection<String> att = new ArrayList<>();
			pro.setTitle("TEST");
			pro.setStatement("TEST");
			pro.setHint("TEST");
			pro.setAttachments(att);

			//Guardo un problem
			this.problemService.save(pro);
			this.problemService.save(p);
			//if (username.equals("company1"))
			Assert.isTrue(result.equals(p.getStatement()));
			Assert.isTrue(result.equals(pro.getTitle()));

			//Borro una position
			this.problemService.delete(pro);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}

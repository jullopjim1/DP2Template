
package servicesHackerRank;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import security.LoginService;
import security.UserAccount;
import services.ActorService;
import services.CurriculaService;
import services.EducationDataService;
import services.MiscellaneousDataService;
import services.PersonalDataService;
import services.PositionDataService;
import services.ProblemService;
import services.RookieService;
import utilities.AbstractTest;
import domain.Actor;
import domain.Curricula;
import domain.EducationData;
import domain.MiscellaneousData;
import domain.PersonalData;
import domain.PositionData;
import domain.Rookie;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UseCase17_1 extends AbstractTest {

	//A rookie may register one or more curricula. Curricula consist of the following data: personal
	//	data, which includes a full name, a statement, a phone number, a GitHub profile, and a
	//	LinkedIn profile; position data, which includes the title, the description, the start date, and
	//	the optional end date of every position that a rookie has had; education data, which includes
	//	the degree, the institution, the mark, the start date, and the optional end date of every
	//	degree that a rookie has; and miscellaneous data, which is free text with optional attachments.

	//Services---------------------------------------------------------------------

	@Autowired
	private ActorService				actorService;

	@Autowired
	private ProblemService				problemService;

	@Autowired
	private CurriculaService			curriculaService;

	@Autowired
	private RookieService				rookieService;

	@Autowired
	private PersonalDataService			personalDataService;

	@Autowired
	private PositionDataService			positionDataService;

	@Autowired
	private EducationDataService		educationDataService;

	@Autowired
	private MiscellaneousDataService	miscellaneousDataService;


	@Test
	public void authorityTest() {
		final Object AccessDashBoardTest[][] = {
			{
				"FalseBoy", "TEST", java.lang.IllegalArgumentException.class

			//Probamos con un user que no exista y que no puede crear curriculas(CASO NEGATIVO)
			//b) Negative test
			//c) analysis of sentence coverage: 99.4%
			//d) This user doesn't exists, so it cannot manage it curriculas
			}, {
				"rookie1", "Juan", null
			//Este rookie si esta registrado en el sistema (CASO POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage: 99.4%
			//d) A rookie should manage their curriculas, which includes listing, showing, creating, updating, and deleting them
			}, {
				"rookie5", "TEST", java.lang.IllegalArgumentException.class
			//			//Este rookie si esta registrado en el sistema, da fallo porque este actor no tiene ninguna curricula y una de las cosas que hacemos es listar y editar una de ellas por lo que este es un (CASO NEGATIVO)
			//			//b) Negative test
			//			//c) analysis of sentence coverage: 99.4%
			//			//d) A rookie should manage their curriculas, which includes listing, showing, creating, updating, and deleting them
			},

		};
		for (int i = 0; i < AccessDashBoardTest.length; i++)
			this.curriculaTemplate((String) AccessDashBoardTest[i][0], (String) AccessDashBoardTest[i][1], (Class<?>) AccessDashBoardTest[i][2]);
	}
	private void curriculaTemplate(final String username, final String name, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		final String result = name;
		try {
			//Nos autenticamos
			this.authenticate(username);
			//Encontramos el actor cuyo nombre de usuario es igual a que pasamos por parámetros
			//final Rookie h = this.rookieService.findRookieByUserAcountId(LoginService.getPrincipal().getId());
			//(Rookie) this.actorService.findActorByUsername(username);

			final Actor a = this.actorService.findActorByUsername(username);
			System.out.println(LoginService.getPrincipal());

			final UserAccount uA = a.getUserAccount();
			System.out.println(uA.getId());
			final Rookie h = this.rookieService.findRookieByUserAcountId(uA.getId());

			System.out.println(h);

			final List<Curricula> res = (List<Curricula>) this.curriculaService.findCurriculasByRookieId(h.getId());
			final Curricula c = res.get(0);
			System.out.println(c);

			//Creo una curricula
			final Curricula curr = this.curriculaService.createAndSave(h);
			final Curricula curr1 = this.curriculaService.save(curr);
			this.curriculaService.flush();
			System.out.println(curr1);

			final PersonalData pD = this.personalDataService.create(curr1.getId());
			//
			//			//El personalData ya se crea con la curricula personalData
			//final PersonalData pD = this.personalDataService.getPersonalDataByCurriculaId(curr.getId());
			pD.setFullName(result);
			this.personalDataService.save(pD);
			//this.personalDataService.save(pD);
			//
			//			//Creo un positionData
			final PositionData posD = this.positionDataService.create(curr1.getId());
			//
			//			//			//Creo un educationData
			final EducationData eD = this.educationDataService.create(curr1.getId());
			//			//
			//			//			//Creo un MiscellaneousData
			final MiscellaneousData mD = this.miscellaneousDataService.create(curr1.getId());
			//			//
			//			//			// Edito un curricula
			c.setOriginal(false);
			//			//
			//			//
			//			//			//Borro una curricula
			this.curriculaService.delete(curr1.getId());

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}

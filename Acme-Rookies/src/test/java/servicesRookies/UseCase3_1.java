
package servicesRookies;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ActorService;
import services.AuditService;
import services.AuditorService;
import services.PositionService;
import utilities.AbstractTest;
import domain.Audit;
import domain.Auditor;
import domain.Position;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UseCase3_1 extends AbstractTest {

	//3. An actor who is authenticated as an auditor must be able to:
	//1. Self-assign a position to audit it

	//Services---------------------------------------------------------------------

	@Autowired
	private ActorService	actorService;

	@Autowired
	private AuditorService	auditorService;

	@Autowired
	private AuditService	auditService;

	@Autowired
	private PositionService	positionService;


	@Test
	public void auditTest() {
		final Object AccessDashBoardTest[][] = {
			{
				"NonExistAuditor", "TEST", java.lang.IllegalArgumentException.class

			//Probamos con un user "Auditor" que no exista y que no debia asignarse un puesto para revisar(CASO NEGATIVO)
			//b) Negative test
			//c) analysis of sentence coverage: 99.2%
			//d) This user doesn't exists, so it cannot manage it audits
			},

			{
				"auditor1", "TEST", null
			//Este "Auditor" si esta registrado en el sistema y puede asignarse  un  "Audits"(CASO POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage: 99.2%
			//d) A Auditor should assign and create an audit audits.
			},

		};
		for (int i = 0; i < AccessDashBoardTest.length; i++)
			this.auditTemplate((String) AccessDashBoardTest[i][0], (String) AccessDashBoardTest[i][1], (Class<?>) AccessDashBoardTest[i][2]);
	}

	private void auditTemplate(final String username, final String name, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		final String result = "TEST";
		try {
			//Nos autenticamos
			this.authenticate(username);
			//Encontramos el actor cuyo nombre de usuario es igual a que pasamos por parámetros
			final Auditor a = (Auditor) this.actorService.findActorByUsername(username);

			//Listamos las puestos que son asignables para revisarlas
			final List<Position> positionsToAudit = (List<Position>) this.positionService.getPositionsToAudit();
			final Position p = positionsToAudit.get(0);

			//Nos la asignamos y creamos un revision para ese puesto
			final Audit audit = this.auditService.create(p.getId());
			//Create a audit
			audit.setScore(5);
			audit.setText("TEST");
			//Guardo la revision
			this.auditService.save(audit);
			//comprobamos que los valores coinciden
			Assert.isTrue(result.equals(audit.getText()));
			Assert.isTrue(audit.getScore() == 5);

			//Borro una revision
			this.auditService.delete(audit);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}


package examen;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.AuditService;
import services.FlugotService;
import utilities.AbstractTest;
import domain.Flugot;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ExamText extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private FlugotService	flugotService;

	@Autowired
	private AuditService	auditService;


	// Tests ------------------------------------------------------------------

	@Test
	public void SampleDriver() {
		final Object testingData[][] = {
			{
				"auditor1", null
			}, {
				null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.SampleTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	// Ancillary methods ------------------------------------------------------

	protected void SampleTemplate(final String beanName, final Class<?> expected) {
		Class<?> caught = null;

		try {
			this.authenticate(beanName);

			//Creamos el flugot
			final Flugot flugot = this.flugotService.create();

			//Rellenamod los campos del flugot
			flugot.setBody("TEST");
			flugot.setPicture("http://test.com/test.png");
			flugot.setAudit(new ArrayList<>(this.auditService.findAuditsFinalByAuditorId(flugot.getAuditor().getId())).get(0));

			//Guardamos el flugot

			final Flugot saved = this.flugotService.save(flugot);

			//Comprobamos que se encuentra entre todos los flugots del sistema

			Assert.isTrue(this.flugotService.findAll().contains(saved));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}

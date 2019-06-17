
package servicesHackerRank;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ActorService;
import services.MessageService;
import utilities.AbstractTest;
import domain.Message;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UseCase24_1 extends AbstractTest {

	// Broadcast  a  notification  message  to  the  actors  of  the  system.    
	// The  message  must have tag "SYSTEM" by default

	// Services 

	@Autowired
	private MessageService	messageService;
	@Autowired
	private ActorService	actorService;


	// Tests

	@Test
	public void testsUseCase23_2() {
		// Un adminstrador manda un mensaje de broadcast (POSITIVO)
		//b) Positive test
		//c) analysis of sentence coverage: 98.8%
		//d) An administrator can send broadcast messages
		this.driverBroadcast("admin1", null);
		// Un usuario no administrador manda un mensaje de broadcast (NEGATIVO)
		//b) Negative test
		//c) analysis of sentence coverage: 98.8%
		//d) An non administrator user can't send broadcast messages because he/she doesn't have the needed permissions
		this.driverBroadcast("rookie1", IllegalArgumentException.class);
	}

	// Driver

	private void driverBroadcast(final String username, final Class<?> expected) {
		super.authenticate(username);
		Class<?> caught = null;
		try {
			final Message newMessage = this.messageService.create();
			final Collection<String> tags = newMessage.getTags();
			final Integer actorsSize = this.actorService.findAll().size();
			tags.add("newTag");
			newMessage.setBody("body");
			newMessage.setSubject("subject");
			newMessage.setTags(tags);
			final Collection<Message> savedMessages = this.messageService.saveBroadcast(newMessage);
			Assert.isTrue(savedMessages.size() == actorsSize - 1);
			for (final Message m : savedMessages) {
				Assert.notNull(this.messageService.findOne(m.getId()));
				Assert.isTrue(m.getTags().contains("SYSTEM"));
			}
		} catch (final Throwable oops) {
			caught = oops.getClass();
			super.checkExceptions(expected, caught);
		}
		super.unauthenticate();
	}
}

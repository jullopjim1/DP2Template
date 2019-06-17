
package servicesRookies;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.ItemService;
import services.ProviderService;
import utilities.AbstractTest;
import domain.Item;
import domain.Provider;

@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UseCase9_1 extends AbstractTest {

	// Un usuario puede navegar a la lista de proveedores y navegar a sus items

	@Autowired
	private ProviderService	providerService;
	@Autowired
	private ItemService		itemService;


	@Test
	public void listProviderThenItemsTest() {
		// Un usuario lista los proveedores, después lista los items de uno de los proveedores (POSITIVO)
		//b) Positive test
		//c) analysis of sentence coverage: 100%
		//d) A user lists the providers and then, he/she lists the items of one of them
		this.listProviderThenItemsDriver(null, null, null);
		// Un usuario lista los proveedores, después lista los items de un proveedor que no existe (NEGATIVO)
		//b) Negative test
		//c) analysis of sentence coverage: 100%
		//d) A user lists the providers and then, he/she lists the items of one of them that doesn't exist
		this.listProviderThenItemsDriver(1, null, NullPointerException.class);
	}

	public void listProviderThenItemsDriver(final Integer providerId, final String username, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			final List<Provider> providers = this.providerService.findAll();
			Provider provider = null;
			if (providerId == null)
				provider = providers.get(0);
			else
				provider = this.providerService.findOne(providerId);
			final Collection<Item> items = this.itemService.findItemsByProviderId(provider.getId());
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}


package servicesRookies;

import java.util.ArrayList;
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

@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UseCase10_1Test extends AbstractTest {

	//a)	10. An actor who is authenticated as a provider must be able to:
	//			1.	Manage his or her catalogue of items, which includes listing,
	//				showing, creating, up-dating, and deleting them.

	// System under test ------------------------------------------------------

	@Autowired
	private ItemService itemService;


	// Tests ------------------------------------------------------------------

	@Test
	public void driverListing() {
		System.out.println("=====LISTING=====");
		final Object testingData[][] = {
			{
				"provider1", null
			//Provider puede ver sus items (POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage:
			//d) Using one of the actors that is the provider1, who is a provider

			}, {
				null, IllegalArgumentException.class
			//Un actor no autenticado no deberia tener items (NEGATIVO)
			//b) Negative test
			//c) analysis of sentence coverage:
			//d) Not using any actor, so the user cannot list own items
			}
		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.templateListing((String) testingData[i][0], (Class<?>) testingData[i][1]);
			j++;
		}
	}

	@Test
	public void driverCreating() {
		System.out.println("=====CREATING=====");
		final Object testingData[][] = {
			{
				"provider1", null
			//Provider puede crear sus items (POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage:
			//d) Using one of the actors that is the provider1, who is a provider
			}, {
				null, IllegalArgumentException.class
			//Un actor no autenticado no deberia crear items (NEGATIVO)
			//b) Negative test
			//c) analysis of sentence coverage:
			//d) Not using any actor, so the user cannot create a item
			}
		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.templateCreating((String) testingData[i][0], (Class<?>) testingData[i][1]);
			j++;
		}
	}

	@Test
	public void driverUpdating() {
		System.out.println("=====UPDATING=====");
		final Object testingData[][] = {
			{
				"provider1", null
			//Provider puede editar sus items (POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage:
			//d) Using one of the actors that is the provider1, who is a provider
			}, {
				null, IllegalArgumentException.class
			//Un actor no autenticado no deberia editar items (NEGATIVO)
			//b) Negative test
			//c) analysis of sentence coverage:
			//d) Not using any actor, so the user cannot edit any item
			}
		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.templateUpdating((String) testingData[i][0], (Class<?>) testingData[i][1]);
			j++;
		}

	}

	@Test
	public void driverDeleting() {
		System.out.println("=====DELETING=====");
		final Object testingData[][] = {
			{
				"provider1", null
			//Sponsor puede borrar sus items (POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage:
			//d) Using one of the actors that is the provider1, who is a sponsor
			}, {
				null, IllegalArgumentException.class
			//Un actor no autenticado no deberia editar items (NEGATIVO)
			//b) Negative test
			//c) analysis of sentence coverage:
			//d) Not using any actor, so the user cannot edit any item
			}
		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.templateDeleting((String) testingData[i][0], (Class<?>) testingData[i][1]);
			j++;
		}

	}

	// Ancillary methods ------------------------------------------------------

	protected void templateListing(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Nos autenticamos
			this.authenticate(username);

			//Buscamos los items
			final List<Item> items = this.itemService.findAllByPrincipal();
			for (final Item item : items)
				System.out.println(item.getName());
			//Nos desautenticamos
			this.unauthenticate();

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

	protected void templateCreating(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Nos autenticamos
			this.authenticate(username);

			//Creamos el item
			final Item item = this.itemService.create();
			item.setName("Test Name Create");
			item.setDescription("Test Description");
			item.setLink(new ArrayList<String>());
			item.setPictures(new ArrayList<String>());

			//Guardamos
			final Item saved = this.itemService.save(item);

			//Mostramos el item guardado
			System.out.println(this.itemService.findOne(saved.getId()).getName());

			//Nos desautenticamos
			this.unauthenticate();

			System.out.println("\n");
			System.out.println("Creados correctamente.");
			System.out.println("-----------------------------");
		} catch (final Throwable oops) {
			caught = oops.getClass();

			System.out.println(caught);
			System.out.println("-----------------------------");
		}
		this.checkExceptions(expected, caught);
	}

	protected void templateUpdating(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Nos autenticamos
			this.authenticate(username);

			//Buscamos los itemss del provider con el que nos logueamos
			final List<Item> items = this.itemService.findAllByPrincipal();

			//Cogemos y editamos un sponsorship
			final Item item = items.get(0);

			System.out.println(item.getName());

			//Modificamos la propiedad name
			item.setName("Test Name Update");
			;

			//Gruadamos
			final Item saved = this.itemService.save(item);

			System.out.println(this.itemService.findOne(saved.getId()).getName());

			//Nos desautenticamos
			this.unauthenticate();

			System.out.println("\n");
			System.out.println("Editado correctamente.");
			System.out.println("-----------------------------");
		} catch (final Throwable oops) {
			caught = oops.getClass();

			System.out.println(caught);
			System.out.println("-----------------------------");
		}
		this.checkExceptions(expected, caught);
	}

	protected void templateDeleting(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Nos autenticamos
			this.authenticate(username);

			//Buscamos los items
			final List<Item> items = this.itemService.findAllByPrincipal();

			//Cogemos un item
			final Item item = items.get(0);
			System.out.println(item.getName());

			//Borramos el item
			this.itemService.delete(item);

			//Volvemos a buscar todos los items
			final List<Item> items2 = this.itemService.findAllByPrincipal();

			for (final Item item2 : items2)
				System.out.println(item2.getName());

			//Nos desautenticamos
			this.unauthenticate();

			System.out.println("\n");
			System.out.println("Editado correctamente.");
			System.out.println("-----------------------------");
		} catch (final Throwable oops) {
			caught = oops.getClass();

			System.out.println(caught);
			System.out.println("-----------------------------");
		}
		this.checkExceptions(expected, caught);
	}

}

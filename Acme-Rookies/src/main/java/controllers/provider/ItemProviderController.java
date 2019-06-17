
package controllers.provider;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;
import domain.Item;
import services.ConfigurationService;
import services.ItemService;
import services.ProviderService;

@Controller
@RequestMapping("/item/provider")
public class ItemProviderController extends AbstractController {

	//-----------------Services-------------------------
	@Autowired
	private ItemService		itemService;

	@Autowired
	private ProviderService	providerService;

	@Autowired
	ConfigurationService	configurationService;


	//-------------------------- List ----------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView modelAndView;

		final List<Item> items = this.itemService.findAllByPrincipal();

		modelAndView = new ModelAndView("item/list");
		modelAndView.addObject("items", items);
		modelAndView.addObject("requestURI", "items/provider/list.do");
		modelAndView.addObject("banner", this.configurationService.findOne().getBanner());

		return modelAndView;

	}

	//Create------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final Item item;

		item = this.itemService.create();

		result = this.createEditModelAndView(item);

		return result;

	}

	//Edit------------------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int itemId) {
		ModelAndView result;
		Item item;
		item = this.itemService.findOne(itemId);
		Assert.notNull(item);

		result = this.createEditModelAndView(item);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Item item, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			if (binding.getAllErrors().toString().contains("URL"))
				result = this.createEditModelAndView(item, "item.commit.error.url");
			else
				result = this.createEditModelAndView(item);
		} else
			try {
				this.itemService.save(item);
				result = new ModelAndView("redirect:/item/provider/list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(item, "item.commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Item item, final BindingResult binding) {
		ModelAndView result;
		try {
			this.itemService.delete(item);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(item, "item.commit.error");

		}
		return result;
	}

	//Auxiliary----------------------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Item item) {
		ModelAndView result;

		result = this.createEditModelAndView(item, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Item item, final String messageCode) {
		final ModelAndView result;

		result = new ModelAndView("item/edit");
		result.addObject("item", item);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "item/provider/edit.do?itemId=" + item.getId());
		result.addObject("banner", this.configurationService.findOne().getBanner());

		return result;
	}

}

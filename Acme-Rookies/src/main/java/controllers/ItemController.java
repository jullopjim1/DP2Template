
package controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationService;
import services.ItemService;
import domain.Item;

@Controller
@RequestMapping("/item")
public class ItemController extends AbstractController {

	//-----------------Services-------------------------

	@Autowired
	private ItemService				itemService;

	@Autowired
	private ConfigurationService	configurationService;


	//-------------------------- List ----------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView modelAndView;
			final List<Item> items = this.itemService.findAll();

			modelAndView = new ModelAndView("item/list");
			modelAndView.addObject("items", items);
			modelAndView.addObject("requestURI", "item/list.do");
			modelAndView.addObject("banner", this.configurationService.findOne().getBanner());
		
		return modelAndView;

	}

	@RequestMapping(value = "/listByProvider", method = RequestMethod.GET)
	public ModelAndView listByProvider(@RequestParam(required = true) final Integer providerId) {
		ModelAndView modelAndView;
		final List<Item> items = this.itemService.findItemsByProviderId(providerId);

		modelAndView = new ModelAndView("item/list");
		modelAndView.addObject("items", items);
		modelAndView.addObject("requestURI", "item/list.do");
		modelAndView.addObject("banner", this.configurationService.findOne().getBanner());

		return modelAndView;

	}

}

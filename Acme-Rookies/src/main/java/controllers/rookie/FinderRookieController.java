/*
 * CustomerController.java
 *
 * Copyright (C) 2018 Universidad de Sevilla
 *
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.rookie;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;
import domain.Finder;
import domain.Position;
import services.ConfigurationService;
import services.FinderService;

@Controller
@RequestMapping("/finder/rookie")
public class FinderRookieController extends AbstractController {

	@Autowired
	private FinderService			finderService;

	@Autowired
	private ConfigurationService	configurationService;


	// Constructors -----------------------------------------------------------

	public FinderRookieController() {
		super();
	}

	// Update finder ---------------------------------------------------------------

	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public ModelAndView updateFinder() {
		ModelAndView result;
		Finder finder = this.finderService.create();

		if (this.finderService.findOneByPrincipal() != null)
			finder = this.finderService.findOneByPrincipal();

		result = this.createEditModelAndView(finder);

		return result;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, params = "save")
	public ModelAndView updateFinder(@Valid final Finder finder, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(finder);
		else
			try {
				this.finderService.save(finder);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Exception e) {
				System.out.println(e.getMessage());
				result = this.createEditModelAndView(finder, "finder.commit.error");
			}
		return result;
	}

	// List result finder ---------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView modelAndView = new ModelAndView("position/list");
		final Finder finder = this.finderService.findOneByPrincipal();

		final List<Position> positions = this.finderService.updateCache(finder);

		modelAndView.addObject("positions", positions);
		modelAndView.addObject("banner", this.configurationService.findOne().getBanner());
		modelAndView.addObject("numResults", this.configurationService.findOne().getNumResults());
		modelAndView.addObject("requestURI", "finder/rookie/list.do");

		return modelAndView;
	}

	//ModelAndView--------------------------------------------------------------------
	protected ModelAndView createEditModelAndView(final Finder finder) {
		ModelAndView result;

		result = this.createEditModelAndView(finder, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Finder finder, final String message) {
		ModelAndView result;

		result = new ModelAndView("finder/update");
		result.addObject("finder", finder);
		result.addObject("message", message);
		result.addObject("banner", this.configurationService.findOne().getBanner());

		return result;
	}

}

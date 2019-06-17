/*
 * AdministratorController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AdministratorService;
import services.ConfigurationService;
import services.PositionService;
import services.WarningService;
import domain.Actor;
import domain.Administrator;
import domain.Warning;

@Controller
@RequestMapping("/administrator")
public class AdministratorController extends AbstractController {

	@Autowired
	private PositionService			positionService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private WarningService			warningService;


	// Constructors -----------------------------------------------------------

	public AdministratorController() {
		super();
	}

	@RequestMapping("/adviseTrue")
	public ModelAndView adviseTrue() {
		ModelAndView result;
		Warning war = this.warningService.giveWarning();
		war = this.warningService.setWarningTrue();
		result = new ModelAndView("welcome/index");
		System.out.println("Se ha alertado de una brecha?" + war.getIsWarning());

		return result;
	}

	@RequestMapping("/adviseFalse")
	public ModelAndView adviseFalse() {
		ModelAndView result;
		Warning war = this.warningService.giveWarning();
		war = this.warningService.setWarningFalse();
		result = new ModelAndView("welcome/index");
		System.out.println("Se ha alertado de una brecha?" + war.getIsWarning());

		return result;
	}
	//
	protected ModelAndView createEditModelAndView2(final Warning war) {
		ModelAndView result;

		result = this.createEditModelAndView2(war, null);

		return result;
	}

	protected ModelAndView createEditModelAndView2(final Warning warning, final String messageCode) {
		final ModelAndView result;

		result = new ModelAndView("welcome/index");
		result.addObject("warning", warning);
		result.addObject("message", messageCode);

		return result;
	}

	@RequestMapping(value = "/deleteAdmin", method = RequestMethod.GET)
	public ModelAndView deleteAllData() {

		ModelAndView result;
		final Actor s = this.actorService.findPrincipal();
		try {
			this.administratorService.deleteAdmin((Administrator) s);
			result = new ModelAndView("redirect:/j_spring_security_logout");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			if (oops.getMessage().equals("UniqueAdmin"))
				result.addObject("message", "error.admin.unique");

		}
		return result;
	}

}

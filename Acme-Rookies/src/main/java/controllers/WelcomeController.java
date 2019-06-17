/*
 * WelcomeController.java
 *
 * Copyright (C) 2019 Universidad de Sevilla
 *
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import domain.Configuration;
import services.ConfigurationService;

@Controller
@RequestMapping("/welcome")
public class WelcomeController extends AbstractController {

	//Service------------------------------------------------------------------

	@Autowired
	private ConfigurationService configurationService;


	// Constructors -----------------------------------------------------------

	public WelcomeController() {
		super();
	}

	// Index ------------------------------------------------------------------

	@RequestMapping(value = "/index")
	public ModelAndView index() {
		ModelAndView result;
		SimpleDateFormat formatter;
		String moment;
		final Configuration configuration = this.configurationService.findOne();

		final String nameSys = configuration.getNameSys();
		final String banner = configuration.getBanner();

		final String welcomeMessage = this.configurationService.internacionalizcionWelcome();
		final boolean isFailSystemA = configuration.isFailSystem();
		final boolean isFailSystemO = configuration.isFailSystem();

		formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		moment = formatter.format(new Date());

		result = new ModelAndView("welcome/index");
		result.addObject("nameSys", nameSys);
		result.addObject("welcomeMessage", welcomeMessage);
		result.addObject("banner", banner);
		result.addObject("moment", moment);
		result.addObject("isFailSystemA", isFailSystemA);

		if (isFailSystemO == true) {
			result.addObject("isFailSystemO", isFailSystemO);
			final String securityMessage = this.configurationService.internacionalizcionSecurityMessage();
			result.addObject("securityMessage", securityMessage);
		}

		return result;
	}

	@RequestMapping(value = "/index", params = "activate")
	public ModelAndView activateSecurityMessage() {
		ModelAndView result = null;

		try {
			this.configurationService.active();
			result = new ModelAndView("redirect:/welcome/index.do");
		} catch (final Exception e) {
			throw e;
		}

		return result;
	}

	@RequestMapping(value = "/index", params = "desactivate")
	public ModelAndView desactivateSecurityMessage() {
		ModelAndView result = null;

		try {
			this.configurationService.desactive();
			result = new ModelAndView("redirect:/welcome/index.do");
		} catch (final Exception e) {
			throw e;
		}

		return result;
	}

	@RequestMapping(value = "/indexCerrar")
	public ModelAndView cerrarIndex() {
		ModelAndView result;
		SimpleDateFormat formatter;
		String moment;
		final Configuration configuration = this.configurationService.findOne();

		final String nameSys = configuration.getNameSys();
		final String banner = configuration.getBanner();

		final String welcomeMessage = this.configurationService.internacionalizcionWelcome();

		formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		moment = formatter.format(new Date());

		result = new ModelAndView("welcome/indexCerrar");
		result.addObject("nameSys", nameSys);
		result.addObject("welcomeMessage", welcomeMessage);
		result.addObject("banner", banner);
		result.addObject("moment", moment);

		return result;
	}
}

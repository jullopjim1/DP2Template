/*
 * AbstractController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationService;

@Controller
public class AbstractController {

	@Autowired
	private ConfigurationService	configurationService;


	// Panic handler ----------------------------------------------------------

	@ExceptionHandler(Throwable.class)
	public ModelAndView panic(final Throwable oops) {
		ModelAndView result;

		result = new ModelAndView("misc/panic");
		result.addObject("name", ClassUtils.getShortName(oops.getClass()));
		result.addObject("exception", oops.getMessage());
		result.addObject("stackTrace", ExceptionUtils.getStackTrace(oops));
		result.addObject("banner", this.configurationService.findOne().getBanner());

		return result;
	}

	//	@ExceptionHandler(Throwable.class)
	//	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	//	public ModelAndView panic(final Throwable oops) {
	//		ModelAndView result;
	//
	//		result = new ModelAndView("misc/ourPanic");
	//		result.addObject("banner", this.configurationService.findOne().getBanner());
	//
	//		return result;
	//	}

}

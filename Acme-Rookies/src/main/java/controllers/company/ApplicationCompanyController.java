/*
 * CustomerController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.company;

import java.util.ArrayList;
import java.util.Collection;
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

import security.LoginService;
import services.ApplicationService;
import services.ConfigurationService;
import services.CurriculaService;
import services.MessageService;
import controllers.AbstractController;
import domain.Application;
import domain.PersonalData;

@Controller
@RequestMapping("/application/company")
public class ApplicationCompanyController extends AbstractController {

	// Constructors -----------------------------------------------------------

	public ApplicationCompanyController() {
		super();
	}


	//-----------------Services-------------------------

	@Autowired
	private ApplicationService		applicationService;

	@Autowired
	private CurriculaService		curriculaService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private ConfigurationService	configurationService;


	//-------------------------- List ----------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView modelAndView;
		final int companyId, userAccountId;

		userAccountId = LoginService.getPrincipal().getId();
		companyId = this.applicationService.findCompanyIdByUserAccountId(userAccountId);
		final Collection<Application> applications = this.applicationService.findApplicationByStatusAndCompany(companyId);

		modelAndView = new ModelAndView("application/list");
		modelAndView.addObject("applications", applications);
		modelAndView.addObject("requestURI", "application/company/list.do");
		modelAndView.addObject("banner", this.configurationService.findOne().getBanner());

		return modelAndView;

	}
	// Edit ---------------------------------------------------------------		

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int applicationId) {
		ModelAndView result;
		final Application application;

		application = this.applicationService.findOne(applicationId);
		Assert.notNull(application);

		result = this.createEditModelAndView(application);

		return result;

	}
	// Show ---------------------------------------------------------------		

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int applicationId) {
		ModelAndView result;
		final Application application;

		application = this.applicationService.findOne(applicationId);
		Assert.notNull(application);

		final List<PersonalData> pds = new ArrayList<PersonalData>();
		final PersonalData pd = this.curriculaService.findPersonalFromCurricula(application.getCurricula().getId());
		pds.add(pd);
		result = this.createEditModelAndView(application);
		result.addObject("pds", pds);
		result.addObject("isRead", true);
		return result;

	}

	//Save--------------------------------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Application application, final BindingResult bindingResult) {
		ModelAndView modelAndView;

		if (bindingResult.hasErrors())
			modelAndView = this.createEditModelAndView(application);
		else
			try {
				final String oldStatus = this.applicationService.findOne(application.getId()).getStatus();
				final Application savedApplication = this.applicationService.save(application);

				// Si se cambia el estado de la solicitud se manda un mensaje
				if (!oldStatus.equals(savedApplication.getStatus()))
					this.messageService.notificationApplication(savedApplication);

				modelAndView = this.list();
			} catch (final Exception e) {
				modelAndView = this.createEditModelAndView(application, "application.commit.error");
			}
		return modelAndView;
	}

	//ModelAndView-----------------------------------------------------------------
	protected ModelAndView createEditModelAndView(final Application application) {
		ModelAndView result;

		result = this.createEditModelAndView(application, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Application application, final String message) {
		ModelAndView result;

		result = new ModelAndView("application/edit");
		result.addObject("application", application);
		result.addObject("message", message);
		result.addObject("isRead", false);
		result.addObject("banner", this.configurationService.findOne().getBanner());

		result.addObject("requestURI", "application/company/edit.do");

		return result;
	}
}

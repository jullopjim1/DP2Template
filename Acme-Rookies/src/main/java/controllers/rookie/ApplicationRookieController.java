/*
 * CustomerController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.rookie;

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
import security.UserAccount;
import services.ApplicationService;
import services.ConfigurationService;
import services.CurriculaService;
import services.RookieService;
import services.PositionService;
import controllers.AbstractController;
import domain.Application;
import domain.Curricula;
import domain.Rookie;
import domain.PersonalData;

@Controller
@RequestMapping("/application/rookie")
public class ApplicationRookieController extends AbstractController {

	// Constructors -----------------------------------------------------------

	public ApplicationRookieController() {
		super();
	}


	//-----------------Services-------------------------

	@Autowired
	private ApplicationService		applicationService;

	@Autowired
	private PositionService			positionService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private CurriculaService		curriculaService;

	@Autowired
	private RookieService			rookieService;


	//-------------------------- List ----------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView modelAndView;
		final Rookie rookie;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		rookie = this.applicationService.findRookieByUserAccount(userAccount);
		final Collection<Application> applications = this.applicationService.findApplicationByRookie(rookie);

		modelAndView = new ModelAndView("application/list");
		modelAndView.addObject("applications", applications);
		modelAndView.addObject("requestURI", "application/rookie/list.do");
		modelAndView.addObject("banner", this.configurationService.findOne().getBanner());

		return modelAndView;

	}

	// Edit ---------------------------------------------------------------		

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int applicationId) {
		ModelAndView result;
		final Application application;

		final int userAccountId = LoginService.getPrincipal().getId();
		final Rookie rookie = this.rookieService.findRookieByUserAcountId(userAccountId);
		final int rookieId = rookie.getId();
		application = this.applicationService.findOne(applicationId);
		Assert.notNull(application);
		final List<Curricula> curriculas = (List) this.curriculaService.findCurriculasByRookieId(rookieId);
		final List<Curricula> curriculasOriginales = new ArrayList<Curricula>();
		final List<PersonalData> pds = new ArrayList<PersonalData>();
		for (final Curricula c : curriculas) {
			final int originalCurriculaId = c.getId();
			final PersonalData pd = this.curriculaService.findPersonalFromCurricula(originalCurriculaId);
			pds.add(pd);
			final Curricula originalCurricula = this.curriculaService.makeACopy(originalCurriculaId);
			curriculasOriginales.add(originalCurricula);
		}
		result = this.createEditModelAndView(application);
		result.addObject("curriculas", curriculasOriginales);
		result.addObject("pds", pds);

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
		if (application.getStatus().equals("PENDING,SUBMITTED")) {
			application.setStatus(null);
			application.setStatus("SUBMITTED");
		}
		if (application.getAnswerCode().contains("https://") || (application.getStatus().equals("PENDING") && application.getAnswerCode().equals(""))) {
			this.applicationService.save(application);
			modelAndView = new ModelAndView("redirect:/application/rookie/list.do");
			//			modelAndView = this.list();
		} else {
			modelAndView = this.createEditModelAndView(application, "application.commit.error");
			final List<PersonalData> pds = new ArrayList<PersonalData>();
			final PersonalData pd = this.curriculaService.findPersonalFromCurricula(application.getCurricula().getId());
			pds.add(pd);
			modelAndView.addObject("pds", pds);
		}
		return modelAndView;
	}
	//	Create-----------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int positionId) {
		final ModelAndView result;
		Application application;
		application = this.applicationService.create(positionId);
		application.setStatus("PENDING");

		final int userAccountId = LoginService.getPrincipal().getId();
		final Rookie rookie = this.rookieService.findRookieByUserAcountId(userAccountId);
		final int rookieId = rookie.getId();
		Assert.notNull(application);
		final List<Curricula> curriculas = (List) this.curriculaService.findCurriculasByRookieId(rookieId);
		final List<Curricula> curriculasOriginales = new ArrayList<Curricula>();
		final List<PersonalData> pds = new ArrayList<PersonalData>();
		for (final Curricula c : curriculas) {
			final int originalCurriculaId = c.getId();
			final PersonalData pd = this.curriculaService.findPersonalFromCurricula(originalCurriculaId);
			pds.add(pd);
			final Curricula originalCurricula = this.curriculaService.makeACopy(originalCurriculaId);
			curriculasOriginales.add(originalCurricula);
		}
		result = this.createEditModelAndView(application);
		result.addObject("curriculas", curriculasOriginales);
		result.addObject("pds", pds);
		return result;

	}
	// Delete ------------------------------------
	//	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	//	public ModelAndView delete(@Valid final SocialProfile socialProfile, final BindingResult binding) {
	//		ModelAndView result;
	//
	//		try {
	//			this.socialProfileService.delete(socialProfile);
	//			result = new ModelAndView("redirect:/socialProfile/list.do");
	//		} catch (final Throwable oops) {
	//			result = this.createEditModelAndView(socialProfile, "profile.commit.error");
	//		}
	//		return result;
	//	}

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

		result.addObject("requestURI", "application/rookie/edit.do");

		return result;
	}
}

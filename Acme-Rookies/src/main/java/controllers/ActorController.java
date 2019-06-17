/*
 * ProfileController.java
 * 
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ActorService;
import services.ConfigurationService;
import domain.Actor;
import forms.ActorForm;

@Controller
@RequestMapping("/actor")
public class ActorController extends AbstractController {

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ConfigurationService	configurationService;


	// Edit ---------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;

		final Actor a = this.actorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(a);
		final ActorForm actorForm = this.actorService.construct(a);

		result = this.createEditModelAndView(actorForm);

		return result;
	}
	// Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ActorForm actorForm, final BindingResult binding) {

		ModelAndView result;

		this.actorService.validateForm(actorForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(actorForm);
		else
			try {
				final Actor actor = this.actorService.deconstruct(actorForm, binding);
				final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
				actor.getUserAccount().setPassword(encoder.encodePassword(actor.getUserAccount().getPassword(), null));
				this.actorService.update(actor);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				final Actor test = this.actorService.findActorByUsername(actorForm.getUsername());
				final Actor principal = this.actorService.findPrincipal();
				if (test != null && !principal.equals(test))
					result = this.createEditModelAndView(actorForm, "actor.userExists");
				else
					result = this.createEditModelAndView(actorForm, "actor.commit.error");
			}
		return result;
	}
	//PDF Exort------------------------------------------------------------------------

	@RequestMapping(value = "export", method = RequestMethod.GET)
	public ModelAndView exportData(@RequestParam final int actorId) {
		final ModelAndView modelAndView = this.edit();

		try {
			this.actorService.exportPDF(actorId);
		} catch (final Exception e) {
			throw e;
		}

		return modelAndView;
	}

	// CreateModelAndView

	protected ModelAndView createEditModelAndView(final ActorForm actorForm) {
		ModelAndView result;

		result = this.createEditModelAndView(actorForm, null);

		return result;

	}

	protected ModelAndView createEditModelAndView(final ActorForm actorForm, final String message) {
		ModelAndView result;

		result = new ModelAndView("actor/edit");
		result.addObject("actorForm", actorForm);
		result.addObject("message", message);
		result.addObject("isRead", false);
		result.addObject("actorId", this.actorService.findPrincipal().getId());
		result.addObject("requestURI", "actor/edit.do");
		result.addObject("isPrincipalAuthorizedEdit", this.isPrincipalAuthorizedEdit(actorForm));
		result.addObject("banner", this.configurationService.findOne().getBanner());
		result.addObject("makes", this.configurationService.findOne().getMakeName());

		return result;
	}

	private Boolean isPrincipalAuthorizedEdit(final ActorForm actorForm) {
		Boolean res = false;
		Actor principal = null;
		try {
			principal = this.actorService.findPrincipal();
		} catch (final IllegalArgumentException e) {
			principal = null;
		}
		if (actorForm.getId() > 0 && principal != null)
			res = principal.getId() == actorForm.getId();
		else if (actorForm.getId() == 0)
			res = principal == null;
		return res;
	}

}

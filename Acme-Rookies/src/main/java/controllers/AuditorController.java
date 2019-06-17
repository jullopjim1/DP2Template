
package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.ActorService;
import services.AuditorService;
import services.ConfigurationService;
import domain.Actor;
import domain.Administrator;
import domain.Auditor;
import forms.ActorForm;

@Controller
@RequestMapping("/auditor")
public class AuditorController extends AbstractController {

	@Autowired
	private AuditorService			auditorService;
	@Autowired
	private ActorService			actorService;
	@Autowired
	private ConfigurationService	configurationService;


	// Register
	@RequestMapping(value = "administrator/register", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView modelAndView;
		try {
			final Actor actor = this.auditorService.create();

			final ActorForm actorForm = this.actorService.construct(actor);
			modelAndView = this.createEditModelAndView(actorForm);

		} catch (final Exception e) {
			modelAndView = new ModelAndView("redirect:/welcome/index.do");
		}

		return modelAndView;
	}

	// Save
	@RequestMapping(value = "administrator/register", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ActorForm actorForm, final BindingResult binding) {
		ModelAndView result;
		this.actorService.validateForm(actorForm, binding);
		if (binding.hasErrors()) {
			System.out.println(binding.getAllErrors());
			result = this.createEditModelAndView(actorForm);
		} else
			try {
				final Actor actor = this.actorService.deconstruct(actorForm, binding);
				final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
				actor.getUserAccount().setPassword(encoder.encodePassword(actor.getUserAccount().getPassword(), null));
				actor.getUserAccount().setEnabled(true);
				this.actorService.update(actor);

				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				System.out.println("=======" + oops.getMessage() + "=======");
				final Actor test = this.actorService.findActorByUsername(actorForm.getUsername());

				if (test != null)
					result = this.createEditModelAndView(actorForm, "actor.userExists");
				else
					result = this.createEditModelAndView(actorForm, "actor.commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/deleteAuditor", method = RequestMethod.GET)
	public ModelAndView deleteAllData() {

		ModelAndView result;
		final Actor s = this.actorService.findPrincipal();
		try {
			this.auditorService.deleteAuditor((Auditor) s);
			result = new ModelAndView("redirect:/j_spring_security_logout");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			System.out.println("NO SE HA PODIDO BORRAR EL USUARIO");
		}
		return result;
	}

	protected ModelAndView createEditModelAndView(final ActorForm actorForm) {
		return this.createEditModelAndView(actorForm, null);
	}

	protected ModelAndView createEditModelAndView(final ActorForm actorForm, final String message) {
		ModelAndView result = null;

		// TODO faltan actores

		if (actorForm.getAuthority().equals(Authority.AUDITOR))
			result = new ModelAndView("register/auditor");
		else
			throw new NullPointerException();

		result.addObject("actorForm", actorForm);
		result.addObject("message", message);
		result.addObject("isRead", false);
		result.addObject("isPrincipalAuthorizedEdit", this.isPrincipalAuthorizedEdit(actorForm));
		result.addObject("banner", this.configurationService.findOne().getBanner());
		result.addObject("makes", this.configurationService.findOne().getMakeName());
		result.addObject("action", "auditor/administrator/register.do");
		return result;
	}

	private Boolean isPrincipalAuthorizedEdit(final ActorForm form) {
		Boolean res = false;
		Actor principal = null;
		try {
			principal = this.actorService.findPrincipal();
		} catch (final Throwable oops) {
			principal = null;
		}
		if (principal instanceof Administrator && form.getAuthority().equals(Authority.AUDITOR))
			res = true;
		return res;
	}

}

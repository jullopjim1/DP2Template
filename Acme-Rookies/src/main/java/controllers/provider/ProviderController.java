
package controllers.provider;

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

import services.ActorService;
import services.ConfigurationService;
import services.ProviderService;
import controllers.AbstractController;
import domain.Actor;
import domain.Configuration;
import domain.Provider;
import forms.ProviderForm;

@Controller
@RequestMapping("provider")
public class ProviderController extends AbstractController {

	@Autowired
	private ProviderService			providerService;
	@Autowired
	private ActorService			actorService;
	@Autowired
	private ConfigurationService	configurationService;


	@RequestMapping("any/list")
	public ModelAndView list() {
		final ModelAndView res = new ModelAndView("provider/list");
		final Collection<Provider> providers = this.providerService.findAll();
		res.addObject("providers", providers);
		res.addObject("requestURL", "/provider/any/list.do");
		res.addObject("banner", this.configurationService.findOne().getBanner());
		return res;
	}

	@RequestMapping("none/register")
	public ModelAndView create() {
		final Provider provider = this.providerService.create();
		final ProviderForm providerForm = this.providerService.construct(provider);

		return this.createEditModelAndView(providerForm);
	}

	@RequestMapping("provider/edit")
	public ModelAndView edit() {
		final Actor principal = this.actorService.findPrincipal();
		final Provider provider = this.providerService.findOne(principal.getId());
		Assert.notNull(provider);
		final ProviderForm providerForm = this.providerService.construct(provider);
		return this.createEditModelAndView(providerForm);
	}

	@RequestMapping(value = "provider/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ProviderForm providerForm, final BindingResult binding) {
		ModelAndView res = null;
		this.providerService.validateForm(providerForm, binding);
		if (binding.hasErrors())
			res = this.createEditModelAndView(providerForm);
		else
			try {
				final Provider provider = this.providerService.deconstruct(providerForm);
				this.providerService.save(provider);
				res = new ModelAndView("redirect:/provider/provider/display.do");
			} catch (final Throwable oops) {
				final Actor test = this.actorService.findActorByUsername(providerForm.getUsername());
				if (test != null)
					if (providerForm.getId() == 0)
						res = this.createEditModelAndView(providerForm, "actor.userExists");
					else if (!test.equals(this.actorService.findPrincipal()))
						res = this.createEditModelAndView(providerForm, "actor.userExists");
					else
						res = this.createEditModelAndView(providerForm, "cannot.commit.error");
			}

		return res;
	}

	@RequestMapping(value = "none/register", method = RequestMethod.POST, params = "save")
	public ModelAndView saveRegister(@Valid final ProviderForm providerForm, final BindingResult binding) {
		ModelAndView res = null;
		this.providerService.validateForm(providerForm, binding);
		if (binding.hasErrors())
			res = this.createEditModelAndView(providerForm);
		else
			try {
				final Provider provider = this.providerService.deconstruct(providerForm);
				this.providerService.save(provider);
				res = new ModelAndView("redirect:/provider/provider/display.do");
			} catch (final Throwable oops) {
				final Actor test = this.actorService.findActorByUsername(providerForm.getUsername());
				if (test != null)
					if (providerForm.getId() == 0)
						res = this.createEditModelAndView(providerForm, "actor.userExists");
					else if (!test.equals(this.actorService.findPrincipal()))
						res = this.createEditModelAndView(providerForm, "actor.userExists");
					else
						res = this.createEditModelAndView(providerForm, "cannot.commit.error");
			}

		return res;
	}

	@RequestMapping("any/display")
	public ModelAndView display(@RequestParam(required = true) final Integer providerId) {
		final ModelAndView res = new ModelAndView("provider/display");
		final Provider provider = this.providerService.findOne(providerId);
		res.addObject("provider", provider);
		res.addObject("isPrincipalAuthorizedEdit", this.isPrincipalAuthorizedEdit(provider));
		res.addObject("banner", this.configurationService.findOne().getBanner());
		return res;
	}

	@RequestMapping("provider/display")
	public ModelAndView display() {
		final ModelAndView res = new ModelAndView("provider/display");
		final Provider provider = (Provider) this.actorService.findPrincipal();
		res.addObject("provider", provider);

		res.addObject("isPrincipalAuthorizedEdit", this.isPrincipalAuthorizedEdit(provider));
		res.addObject("banner", this.configurationService.findOne().getBanner());
		return res;
	}

	private ModelAndView createEditModelAndView(final ProviderForm providerForm) {
		return this.createEditModelAndView(providerForm, null);
	}

	private ModelAndView createEditModelAndView(final ProviderForm providerForm, final String message) {
		final ModelAndView res = new ModelAndView("provider/edit");
		final Boolean isPrincipalAuthorizedEdit = this.isPrincipalAuthorizedEdit(providerForm);
		res.addObject("providerForm", providerForm);
		res.addObject("message", message);
		res.addObject("banner", this.configurationService.findOne().getBanner());
		res.addObject("isPrincipalAuthorizedEdit", isPrincipalAuthorizedEdit);
		if (providerForm.getId() == 0)
			res.addObject("action", "provider/none/register.do");
		else
			res.addObject("action", "provider/provider/edit.do");
		final Configuration configuration = this.configurationService.findOne();
		final List<String> makes = configuration.getMakeName();
		res.addObject("makes", makes);
		Integer actorId = null;
		try {
			actorId = this.actorService.findPrincipal().getId();
		} catch (final Throwable t) {
			actorId = 0;
		}
		res.addObject("actorId", actorId);
		return res;
	}
	private Boolean isPrincipalAuthorizedEdit(final ProviderForm providerForm) {
		Boolean res = false;
		Actor principal = null;
		try {
			principal = this.actorService.findPrincipal();
		} catch (final IllegalArgumentException e) {
			principal = null;
		}
		if (providerForm.getId() > 0)
			res = principal.getId() == providerForm.getId();
		else if (providerForm.getId() == 0)
			res = principal == null;
		return res;
	}

	private Boolean isPrincipalAuthorizedEdit(final Provider provider) {
		Boolean res = false;
		Actor principal = null;
		try {
			principal = this.actorService.findPrincipal();
		} catch (final IllegalArgumentException e) {
			principal = null;
		}
		if (provider.getId() > 0 && principal != null)
			res = principal.getId() == provider.getId();
		else if (provider.getId() == 0)
			res = principal == null;
		return res;
	}

	@RequestMapping(value = "/deleteProvider", method = RequestMethod.GET)
	public ModelAndView deleteAllData() {

		ModelAndView result;
		final Actor s = this.actorService.findPrincipal();
		try {
			this.providerService.deleteProvider((Provider) s);
			result = new ModelAndView("redirect:/j_spring_security_logout");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			System.out.println("NO SE HA PODIDO BORRAR EL USUARIO");
		}
		return result;
	}

}

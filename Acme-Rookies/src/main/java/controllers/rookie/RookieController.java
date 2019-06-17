
package controllers.rookie;

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
import services.ActorService;
import services.ConfigurationService;
import services.RookieService;
import controllers.AbstractController;
import domain.Actor;
import domain.Rookie;
import forms.RookieForm;

@Controller
@RequestMapping("/rookie")
public class RookieController extends AbstractController {

	// Constructors -----------------------------------------------------------

	public RookieController() {
		super();
	}


	//-----------------Services-------------------------

	@Autowired
	RookieService					rookieService;

	@Autowired
	ActorService					actorService;

	//	@Autowired
	//	SocialProfileService			socialProfileService;

	@Autowired
	private ConfigurationService	configurationService;


	//	@Autowired
	//	UserAccount				userAccountService;

	// Action-1 ---------------------------------------------------------------

	@RequestMapping("/action-1")
	public ModelAndView action1() {
		ModelAndView result;

		result = new ModelAndView("rookie/action-1");

		return result;
	}

	// Action-2 ---------------------------------------------------------------

	@RequestMapping("/action-2")
	public ModelAndView action2() {
		ModelAndView result;

		result = new ModelAndView("rookie/action-2");

		return result;
	}

	//-----------------Display-------------------------

	//display creado para mostrar al rookie logueado
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result;
		Rookie rookie;

		rookie = (Rookie) this.actorService.findByUserAccount(LoginService.getPrincipal());
		result = new ModelAndView("rookie/edit");
		result.addObject("rookie", rookie);
		result.addObject("banner", this.configurationService.findOne().getBanner());

		return result;
	}

	//------------------Edit---------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int rookieId) {
		ModelAndView result;
		Rookie rookie;

		rookie = this.rookieService.findOne(rookieId);
		Assert.notNull(rookie);
		result = this.createEditModelAndView(rookie);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final RookieForm rookieForm, final BindingResult binding) {
		ModelAndView result;
		Rookie rookie = null;

		this.rookieService.validateForm(rookieForm, binding);
		if (binding.hasErrors()) {
			rookie = this.rookieService.deconstruct(rookieForm);
			System.out.println(binding.getAllErrors());
			result = this.createEditModelAndView(rookie);
			result.addObject("message", "rookie.commit.error");
		} else

			try {
				rookie = this.rookieService.deconstruct(rookieForm);
				this.rookieService.save(rookie);
				result = new ModelAndView("redirect:display.do");

			} catch (final Throwable oops) {
				final Actor test = this.actorService.findActorByUsername(rookieForm.getUsername());
				if (test != null)
					result = this.createEditModelAndView(rookie, "actor.userExists");
				else
					result = this.createEditModelAndView(rookie, "rookie.commit.error");

			}

		return result;
	}

	//---------------------------create------------------------------------------------------
	// Creation
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final Rookie rookie;

		rookie = this.rookieService.create();

		result = new ModelAndView("rookie/create");
		result.addObject("rookie", rookie);
		return result;
	}

	@RequestMapping(value = "/deleteRookie", method = RequestMethod.GET)
	public ModelAndView deleteAllData() {

		ModelAndView result;
		final Actor s = this.actorService.findPrincipal();
		try {
			this.rookieService.deleteRookie((Rookie) s);
			result = new ModelAndView("redirect:/j_spring_security_logout");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			System.out.println("NO SE HA PODIDO BORRAR EL USUARIO");
		}
		return result;
	}

	//-----------------List----------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int brotherhoodId) {
		final ModelAndView result;
		//		final List<Rookie> rookies = this.rookieService.listRookiesByBrotherhood(brotherhoodId);
		//
		result = new ModelAndView("rookie/list");
		//		result.addObject("requestURI", "rookie/list.do");
		//		result.addObject("rookies", rookies);

		return result;
	}
	//---------------------------------------------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Rookie rookie) {
		ModelAndView result;

		result = this.createEditModelAndView(rookie, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Rookie rookie, final String messageCode) {
		final ModelAndView result;
		UserAccount userAccount = new UserAccount();
		userAccount = rookie.getUserAccount();

		result = new ModelAndView("rookie/edit");
		result.addObject("rookie", rookie);
		result.addObject("userAccount", userAccount);
		result.addObject("message", messageCode);

		return result;
	}

}

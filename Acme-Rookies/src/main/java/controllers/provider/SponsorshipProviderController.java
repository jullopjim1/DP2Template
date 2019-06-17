
package controllers.provider;

import java.util.ArrayList;
import java.util.Collection;

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
import services.ConfigurationService;
import services.PositionService;
import services.ProviderService;
import services.SponsorshipService;
import controllers.AbstractController;
import domain.Position;
import domain.Provider;
import domain.Sponsorship;

@Controller
@RequestMapping("/sponsorship/provider")
public class SponsorshipProviderController extends AbstractController {

	//-----------------Services-------------------------
	@Autowired
	SponsorshipService		sponsorshipService;

	@Autowired
	ProviderService			providerService;

	@Autowired
	ConfigurationService	configurationService;

	@Autowired
	PositionService			positionService;


	//-------------------------- List ----------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView modelAndView;
		final Provider provider = this.providerService.findProviderByUserAcountId(LoginService.getPrincipal().getId());

		final Collection<Sponsorship> sponsorships = this.sponsorshipService.findSponsorshipsByProviderId(provider.getId());

		modelAndView = new ModelAndView("sponsorship/list");
		modelAndView.addObject("sponsorships", sponsorships);
		modelAndView.addObject("requestURI", "sponsorships/list.do");
		modelAndView.addObject("banner", this.configurationService.findOne().getBanner());

		return modelAndView;

	}

	//	//------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final Sponsorship sponsorship;

		sponsorship = this.sponsorshipService.create();

		result = this.createEditModelAndView(sponsorship);

		return result;

	}

	protected ModelAndView createEditModelAndView(final Sponsorship sponsorship) {
		ModelAndView result;

		result = this.createEditModelAndView(sponsorship, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Sponsorship sponsorship, final String messageCode) {
		final ModelAndView result;
		Collection<Position> positions = new ArrayList<>();
		positions = this.positionService.findPositionsFinals();

		result = new ModelAndView("sponsorship/edit");
		result.addObject("sponsorship", sponsorship);
		result.addObject("positions", positions);
		result.addObject("message", messageCode);
		result.addObject("banner", this.configurationService.findOne().getBanner());

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Sponsorship sponsorship, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(sponsorship);
			System.out.println(binding.getAllErrors());

			if (binding.getAllErrors().toString().contains("URL"))
				result = this.createEditModelAndView(sponsorship, "sponsorship.URL.error");
			else
				result = this.createEditModelAndView(sponsorship);

		} else
			try {

				this.sponsorshipService.save(sponsorship);
				result = new ModelAndView("redirect:/sponsorship/provider/list.do");

			} catch (final Throwable oops) {
				System.out.println(oops.getMessage() + "--" + oops.getLocalizedMessage() + "--" + oops.getCause());
				result = this.createEditModelAndView(sponsorship, "sponsorship.commit.error");
			}
		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int sponsorshipId) {
		ModelAndView result;
		Sponsorship sponsorship;
		sponsorship = this.sponsorshipService.findOne(sponsorshipId);
		Assert.notNull(sponsorship);

		result = this.createEditModelAndView(sponsorship);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Sponsorship sponsorship, final BindingResult binding) {
		ModelAndView result;
		try {
			this.sponsorshipService.delete(sponsorship);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(sponsorship, "sponsorship.commit.error");

		}
		return result;
	}

}

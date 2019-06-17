
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CompanyService;
import services.ConfigurationService;
import services.PositionService;
import services.SponsorshipService;
import domain.Sponsorship;

@Controller
@RequestMapping("/sponsorship")
public class SponsorshipController extends AbstractController {

	//-----------------Services-------------------------
	@Autowired
	SponsorshipService		sponsorshipService;

	@Autowired
	CompanyService			companyService;

	@Autowired
	ConfigurationService	configurationService;

	@Autowired
	PositionService			positionService;


	//-------------------------- List ----------------------------------
	//	@RequestMapping(value = "/list", method = RequestMethod.GET)
	//	public ModelAndView list() {
	//		ModelAndView modelAndView;
	//
	//		final Collection<Sponsorship> sponsorships = this.sponsorshipService.findAll();
	//
	//		modelAndView = new ModelAndView("sponsorship/list");
	//		modelAndView.addObject("sponsorships", sponsorships);
	//		modelAndView.addObject("requestURI", "sponsorships/list.do");
	//		modelAndView.addObject("banner", this.configurationService.findOne().getBanner());
	//
	//		return modelAndView;
	//
	//	}

	//	//-------------------------- List ----------------------------------
	//	@RequestMapping(value = "/listone", method = RequestMethod.GET)
	//	public ModelAndView list(@RequestParam final int applicationId) {
	//		ModelAndView modelAndView;
	//
	//		final Sponsorship sponsorship = this.sponsorshipService.findSponsorshipByApplicationId(applicationId);
	//		System.out.println(sponsorship);
	//		modelAndView = new ModelAndView("sponsorship/list");
	//		modelAndView.addObject("sponsorships", sponsorship);
	//		modelAndView.addObject("requestURI", "sponsorship/listone.do");
	//		modelAndView.addObject("banner", this.configurationService.findOne().getBanner());
	//
	//		return modelAndView;
	//
	//	}

	//-----------------Display-------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int sponsorshipId) {
		ModelAndView result;
		Sponsorship sponsorship;
		sponsorship = this.sponsorshipService.findOne(sponsorshipId);
		result = new ModelAndView("sponsorship/display");
		result.addObject("sponsorship", sponsorship);
		result.addObject("banner", this.configurationService.findOne().getBanner());

		return result;
	}
}


package controllers;

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
import services.CurriculaService;
import services.RookieService;
import services.PersonalDataService;
import domain.PersonalData;

@Controller
@RequestMapping("/personalData")
public class PersonalDataController extends AbstractController {

	//-----------------Services-------------------------
	@Autowired
	PersonalDataService		personalDataService;

	@Autowired
	RookieService			hacekrService;

	@Autowired
	CurriculaService		curriculaService;

	@Autowired
	ActorService			actorService;

	@Autowired
	ConfigurationService	configurationService;


	//	//------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int personalDataId) {
		ModelAndView result;
		final PersonalData personalData;

		personalData = this.personalDataService.findOne(personalDataId);
		Assert.notNull(personalData);
		Assert.isTrue(personalData.getOriginal() == true);

		result = this.createEditModelAndView(personalData);
		Assert.isTrue(personalData.getOriginal() == true);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final PersonalData personalData, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			if (binding.getAllErrors().toString().contains("URL"))
				result = this.createEditModelAndView(personalData, "curricula.URL.error");
			else
				result = this.createEditModelAndView(personalData);
		} else
			try {

				this.personalDataService.save(personalData);
				result = new ModelAndView("redirect:/curricula/display.do?curriculaId=" + personalData.getCurricula().getId());

			} catch (final Throwable oops) {

				result = this.createEditModelAndView(personalData, "curricula.commit.error");
			}
		return result;

	}
	protected ModelAndView createEditModelAndView(final PersonalData personalData, final String message) {
		final ModelAndView result;

		result = new ModelAndView("personalData/edit");

		result.addObject("personalData", personalData);
		result.addObject("banner", this.configurationService.findOne().getBanner());
		result.addObject("message", message);
		result.addObject("requestURI", "personalData/edit.do");

		return result;
	}

	protected ModelAndView createEditModelAndView(final PersonalData personalData) {
		ModelAndView result;

		result = this.createEditModelAndView(personalData, null);

		return result;
	}

}

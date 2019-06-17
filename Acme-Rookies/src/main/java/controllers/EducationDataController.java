
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
import services.EducationDataService;
import services.RookieService;
import domain.EducationData;

@Controller
@RequestMapping("/educationData")
public class EducationDataController extends AbstractController {

	//-----------------Services-------------------------
	@Autowired
	EducationDataService	educationDataService;

	@Autowired
	RookieService			hacekrService;

	@Autowired
	CurriculaService		curriculaService;

	@Autowired
	ActorService			actorService;

	@Autowired
	ConfigurationService	configurationService;


	//	//------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int curriculaId) {
		ModelAndView result;
		EducationData educationData;
		educationData = this.educationDataService.create(curriculaId);
		result = this.createEditModelAndView(educationData);
		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int educationDataId) {
		ModelAndView result;
		final EducationData educationData;

		educationData = this.educationDataService.findOne(educationDataId);
		Assert.notNull(educationData);
		Assert.isTrue(educationData.getOriginal() == true);

		result = this.createEditModelAndView(educationData);

		return result;

	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final EducationData educationData, final BindingResult binding) {
		ModelAndView result;
		final Exception dateErr = new Exception("fechas MAL");
		System.out.println(educationData.getEndDate());

		if (binding.hasErrors()) {
			if (binding.getAllErrors().toString().contains("URL"))
				result = this.createEditModelAndView(educationData, "curricula.URL.error");
			else
				result = this.createEditModelAndView(educationData);
		} else
			try {

				if (educationData.getEndDate() != null && (!educationData.getStartDate().before(educationData.getEndDate())))
					throw dateErr;
				else {
					this.educationDataService.save(educationData);
					result = new ModelAndView("redirect:/curricula/display.do?curriculaId=" + educationData.getCurricula().getId());
				}
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("fechas MAL"))
					result = this.createEditModelAndView(educationData, "educationData.date.error");
				else
					result = this.createEditModelAndView(educationData, "curricula.commit.error");
			}
		return result;

	}
	protected ModelAndView createEditModelAndView(final EducationData educationData, final String message) {
		final ModelAndView result;

		result = new ModelAndView("educationData/edit");

		result.addObject("educationData", educationData);
		result.addObject("banner", this.configurationService.findOne().getBanner());
		result.addObject("message", message);
		result.addObject("requestURI", "educationData/edit.do");

		return result;
	}

	protected ModelAndView createEditModelAndView(final EducationData educationData) {
		ModelAndView result;

		result = this.createEditModelAndView(educationData, null);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final EducationData educationData, final BindingResult binding) {
		ModelAndView result;
		try {
			this.educationDataService.delete(educationData);
			result = new ModelAndView("redirect:/curricula/display.do?curriculaId=" + educationData.getCurricula().getId());
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(educationData, "educationData.commit.error");

		}
		return result;
	}

}

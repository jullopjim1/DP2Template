
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
import services.PositionDataService;
import domain.PositionData;

@Controller
@RequestMapping("/positionData")
public class PositionDataController extends AbstractController {

	//-----------------Services-------------------------
	@Autowired
	PositionDataService		positionDataService;

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
		PositionData positionData;
		positionData = this.positionDataService.create(curriculaId);
		result = this.createEditModelAndView(positionData);
		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int positionDataId) {
		ModelAndView result;
		final PositionData positionData;

		positionData = this.positionDataService.findOne(positionDataId);
		Assert.notNull(positionData);

		result = this.createEditModelAndView(positionData);
		Assert.isTrue(positionData.getOriginal() == true);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final PositionData positionData, final BindingResult binding) {
		ModelAndView result;
		final Exception dateErr = new Exception("fechas MAL");

		if (binding.hasErrors()) {
			if (binding.getAllErrors().toString().contains("URL"))
				result = this.createEditModelAndView(positionData, "curricula.URL.error");
			else
				result = this.createEditModelAndView(positionData);
		} else
			try {

				if (positionData.getEndDate() != null && (!positionData.getStartDate().before(positionData.getEndDate())))
					throw dateErr;
				else {
					this.positionDataService.save(positionData);
					result = new ModelAndView("redirect:/curricula/display.do?curriculaId=" + positionData.getCurricula().getId());
				}
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("fechas MAL"))
					result = this.createEditModelAndView(positionData, "positionData.date.error");
				else
					result = this.createEditModelAndView(positionData, "curricula.commit.error");
			}
		return result;

	}
	protected ModelAndView createEditModelAndView(final PositionData positionData, final String message) {
		final ModelAndView result;

		result = new ModelAndView("positionData/edit");

		result.addObject("positionData", positionData);
		result.addObject("banner", this.configurationService.findOne().getBanner());
		result.addObject("message", message);
		result.addObject("requestURI", "positionData/edit.do");

		return result;
	}

	protected ModelAndView createEditModelAndView(final PositionData positionData) {
		ModelAndView result;

		result = this.createEditModelAndView(positionData, null);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final PositionData positionData, final BindingResult binding) {
		ModelAndView result;
		try {
			this.positionDataService.delete(positionData);
			result = new ModelAndView("redirect:/curricula/display.do?curriculaId=" + positionData.getCurricula().getId());
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(positionData, "positionData.commit.error");

		}
		return result;
	}

}


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
import services.MiscellaneousDataService;
import domain.MiscellaneousData;

@Controller
@RequestMapping("/miscellaneousData")
public class MiscellaneousDataController extends AbstractController {

	//-----------------Services-------------------------
	@Autowired
	MiscellaneousDataService	miscellaneousDataService;

	@Autowired
	RookieService				hacekrService;

	@Autowired
	CurriculaService			curriculaService;

	@Autowired
	ActorService				actorService;

	@Autowired
	ConfigurationService		configurationService;


	//	//------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int curriculaId) {
		ModelAndView result;
		MiscellaneousData miscellaneousData;
		miscellaneousData = this.miscellaneousDataService.create(curriculaId);
		result = this.createEditModelAndView(miscellaneousData);
		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int miscellaneousDataId) {
		ModelAndView result;
		final MiscellaneousData miscellaneousData;

		miscellaneousData = this.miscellaneousDataService.findOne(miscellaneousDataId);
		Assert.notNull(miscellaneousData);
		Assert.isTrue(miscellaneousData.getOriginal() == true);

		result = this.createEditModelAndView(miscellaneousData);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final MiscellaneousData miscellaneousData, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			if (binding.getAllErrors().toString().contains("URL"))
				result = this.createEditModelAndView(miscellaneousData, "curricula.URL.error");
			else
				result = this.createEditModelAndView(miscellaneousData);
		} else
			try {

				this.miscellaneousDataService.save(miscellaneousData);
				result = new ModelAndView("redirect:/curricula/display.do?curriculaId=" + miscellaneousData.getCurricula().getId());

			} catch (final Throwable oops) {
				if (oops.getMessage().equals("fechas MAL"))
					result = this.createEditModelAndView(miscellaneousData, "miscellaneousData.date.error");
				else
					result = this.createEditModelAndView(miscellaneousData, "curricula.commit.error");
			}
		return result;

	}
	protected ModelAndView createEditModelAndView(final MiscellaneousData miscellaneousData, final String message) {
		final ModelAndView result;

		result = new ModelAndView("miscellaneousData/edit");

		result.addObject("miscellaneousData", miscellaneousData);
		result.addObject("banner", this.configurationService.findOne().getBanner());
		result.addObject("message", message);
		result.addObject("requestURI", "miscellaneousData/edit.do");

		return result;
	}

	protected ModelAndView createEditModelAndView(final MiscellaneousData miscellaneousData) {
		ModelAndView result;

		result = this.createEditModelAndView(miscellaneousData, null);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final MiscellaneousData miscellaneousData, final BindingResult binding) {
		ModelAndView result;
		try {
			this.miscellaneousDataService.delete(miscellaneousData);
			result = new ModelAndView("redirect:/curricula/display.do?curriculaId=" + miscellaneousData.getCurricula().getId());
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(miscellaneousData, "miscellaneousData.commit.error");

		}
		return result;
	}

}

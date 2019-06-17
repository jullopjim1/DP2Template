
package controllers.rookie;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ActorService;
import services.ConfigurationService;
import services.CurriculaService;
import services.EducationDataService;
import services.RookieService;
import services.MiscellaneousDataService;
import services.PersonalDataService;
import services.PositionDataService;
import controllers.AbstractController;
import domain.Curricula;
import domain.EducationData;
import domain.Rookie;
import domain.MiscellaneousData;
import domain.PersonalData;
import domain.PositionData;

@Controller
@RequestMapping("/curricula")
public class CurriculaRookieController extends AbstractController {

	//-----------------Services-------------------------
	@Autowired
	CurriculaService			curriculaService;

	@Autowired
	RookieService				rookieService;

	@Autowired
	ActorService				actorService;

	@Autowired
	PersonalDataService			personalDataService;

	@Autowired
	PositionDataService			positionDataService;

	@Autowired
	MiscellaneousDataService	miscellaneousDataService;

	@Autowired
	EducationDataService		educationDataService;

	@Autowired
	ConfigurationService		configurationService;


	//-----------------Display-------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int curriculaId) {
		ModelAndView result;
		Curricula curricula;

		curricula = this.curriculaService.findOne(curriculaId);

		final PersonalData personalData = this.personalDataService.getPersonalDataByCurriculaId(curricula.getId());

		final Collection<PositionData> positionDatas = this.positionDataService.findPositionDatasByCurriculaId(curriculaId);
		final Collection<EducationData> educationDatas = this.educationDataService.findEducationDatasByCurriculaId(curriculaId);
		final Collection<MiscellaneousData> miscellaneousDatas = this.miscellaneousDataService.findMiscellaneousDatasByCurriculaId(curriculaId);

		result = new ModelAndView("curricula/display");
		result.addObject("curricula", curricula);
		result.addObject("personalData", personalData);
		result.addObject("positionDatas", positionDatas);
		result.addObject("educationDatas", educationDatas);
		result.addObject("miscellaneousDatas", miscellaneousDatas);

		result.addObject("banner", this.configurationService.findOne().getBanner());

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		Collection<Curricula> curriculas = new ArrayList<>();

		final Rookie rookie = this.rookieService.findRookieByUserAcountId(LoginService.getPrincipal().getId());

		final Collection<PersonalData> personalDatas = this.personalDataService.findAllPersonalDatasByRookieId(rookie.getId());
		curriculas = this.curriculaService.findCurriculasByRookieId(rookie.getId());

		result = new ModelAndView("curricula/list");
		result.addObject("curriculas", curriculas);
		result.addObject("personalDatas", personalDatas);
		result.addObject("banner", this.configurationService.findOne().getBanner());
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		final Rookie rookie = (Rookie) this.actorService.findActorByUsername(LoginService.getPrincipal().getUsername());
		final Curricula curricula = this.curriculaService.createAndSave(rookie);
		final Curricula nCurricula = this.curriculaService.save(curricula);
		this.curriculaService.flush();
		System.out.println(nCurricula.getId());

		final PersonalData personalData = this.personalDataService.create(nCurricula.getId());

		result = new ModelAndView("personalData/edit");
		result.addObject("curricula", curricula);
		result.addObject("personalData", personalData);
		result.addObject("banner", this.configurationService.findOne().getBanner());
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final int curriculaId) {
		ModelAndView result;
		final Curricula curricula = this.curriculaService.findOne(curriculaId);
		try {
			this.curriculaService.delete(curricula.getId());
			result = new ModelAndView("redirect:/curricula/list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/curricula/list.do");

		}
		return result;
	}

}

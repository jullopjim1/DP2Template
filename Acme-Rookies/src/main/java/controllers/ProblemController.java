
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
import services.ProblemService;
import domain.Problem;

@Controller
@RequestMapping("/problem")
public class ProblemController extends AbstractController {

	//-----------------Services-------------------------
	@Autowired
	ProblemService			problemService;

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
	//		final Collection<Problem> problems = this.problemService.findAll();
	//
	//		modelAndView = new ModelAndView("problem/list");
	//		modelAndView.addObject("problems", problems);
	//		modelAndView.addObject("requestURI", "problems/list.do");
	//		modelAndView.addObject("banner", this.configurationService.findOne().getBanner());
	//
	//		return modelAndView;
	//
	//	}

	//-------------------------- List ----------------------------------
	@RequestMapping(value = "/listone", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int applicationId) {
		ModelAndView modelAndView;

		final Problem problem = this.problemService.findProblemByApplicationId(applicationId);
		System.out.println(problem);
		modelAndView = new ModelAndView("problem/list");
		modelAndView.addObject("problems", problem);
		modelAndView.addObject("requestURI", "problem/listone.do");
		modelAndView.addObject("banner", this.configurationService.findOne().getBanner());

		return modelAndView;

	}

	//-----------------Display-------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int problemId) {
		ModelAndView result;
		Problem problem;
		problem = this.problemService.findOne(problemId);
		result = new ModelAndView("problem/display");
		result.addObject("problem", problem);
		result.addObject("banner", this.configurationService.findOne().getBanner());

		return result;
	}
}

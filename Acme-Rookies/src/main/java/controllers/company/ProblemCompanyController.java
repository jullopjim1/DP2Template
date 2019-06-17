
package controllers.company;

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
import services.CompanyService;
import services.ConfigurationService;
import services.PositionService;
import services.ProblemService;
import controllers.AbstractController;
import domain.Company;
import domain.Position;
import domain.Problem;

@Controller
@RequestMapping("/problem/company")
public class ProblemCompanyController extends AbstractController {

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
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView modelAndView;
		final Company company = this.companyService.findCompanyByUserAcountId(LoginService.getPrincipal().getId());

		final Collection<Problem> problems = this.problemService.findProblemsByCompanyId(company.getId());

		modelAndView = new ModelAndView("problem/list");
		modelAndView.addObject("problems", problems);
		modelAndView.addObject("requestURI", "problems/list.do");
		modelAndView.addObject("banner", this.configurationService.findOne().getBanner());

		return modelAndView;

	}

	//------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final Problem problem;

		problem = this.problemService.create();

		result = this.createEditModelAndView(problem);

		return result;

	}

	protected ModelAndView createEditModelAndView(final Problem problem) {
		ModelAndView result;

		result = this.createEditModelAndView(problem, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Problem problem, final String messageCode) {
		final ModelAndView result;
		Company company;
		company = this.companyService.findCompanyByUserAcountId(LoginService.getPrincipal().getId());
		Collection<Position> positions = new ArrayList<>();
		positions = this.positionService.findPositionsByCompanyIdNotCanceled(company.getId());

		result = new ModelAndView("problem/edit");
		result.addObject("problem", problem);
		result.addObject("positions", positions);
		result.addObject("message", messageCode);
		result.addObject("banner", this.configurationService.findOne().getBanner());

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Problem problem, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(problem);
			System.out.println(binding.getAllErrors());
			problem.setFinalMode(false);

			if (binding.getAllErrors().toString().contains("URL"))
				result = this.createEditModelAndView(problem, "problem.URL.error");
			else
				result = this.createEditModelAndView(problem);

		} else
			try {
				if (this.problemService.checkEquals(problem)) {
					problem.setFinalMode(false);
					result = this.createEditModelAndView(problem, "problem.equalsRecord.error");
				} else {
					this.problemService.save(problem);
					result = new ModelAndView("redirect:/problem/company/list.do");
				}
			} catch (final Throwable oops) {
				problem.setFinalMode(false);
				System.out.println(oops.getMessage() + "--" + oops.getLocalizedMessage() + "--" + oops.getCause());
				result = this.createEditModelAndView(problem, "problem.commit.error");
			}
		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int problemId) {
		ModelAndView result;
		Problem problem;
		problem = this.problemService.findOne(problemId);
		Assert.notNull(problem);
		if (this.problemService.isFinalMode(problem))
			result = new ModelAndView("redirect:/");
		else
			result = this.createEditModelAndView(problem);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Problem problem, final BindingResult binding) {
		ModelAndView result;
		try {
			Assert.isTrue(!this.problemService.isFinalMode(problem));
			this.problemService.delete(problem);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(problem, "problem.commit.error");

		}
		return result;
	}

}

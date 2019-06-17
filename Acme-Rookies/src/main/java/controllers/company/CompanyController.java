
package controllers.company;

import java.util.Collection;
import java.util.List;

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
import services.CompanyService;
import services.ConfigurationService;
import controllers.AbstractController;
import domain.Actor;
import domain.Company;
import domain.Configuration;
import forms.CompanyForm;

@Controller
@RequestMapping("company")
public class CompanyController extends AbstractController {

	@Autowired
	private CompanyService			companyService;
	@Autowired
	private ActorService			actorService;
	@Autowired
	private ConfigurationService	configurationService;


	@RequestMapping("any/list")
	public ModelAndView list() {
		final ModelAndView res = new ModelAndView("company/list");
		final Collection<Company> companys = this.companyService.findAll();
		res.addObject("companys", companys);
		res.addObject("requestURL", "/company/any/list.do");
		res.addObject("banner", this.configurationService.findOne().getBanner());
		return res;
	}
	@RequestMapping("none/create")
	public ModelAndView create() {
		final Company company = this.companyService.create();
		final CompanyForm companyForm = this.companyService.construct(company);

		return this.createEditModelAndView(companyForm);
	}
	@RequestMapping("company/edit")
	public ModelAndView edit() {
		final Actor principal = this.actorService.findPrincipal();
		final Company company = this.companyService.findOne(principal.getId());
		Assert.notNull(company);
		final CompanyForm companyForm = this.companyService.construct(company);
		return this.createEditModelAndView(companyForm);
	}

	@RequestMapping(value = "company-none/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final CompanyForm companyForm, final BindingResult binding) {
		ModelAndView res = null;
		this.companyService.validateForm(companyForm, binding);
		if (binding.hasErrors())
			res = this.createEditModelAndView(companyForm);
		else
			try {
				final Company company = this.companyService.deconstruct(companyForm);
				this.companyService.save(company);
				res = new ModelAndView("redirect:/company/company/display.do");
			} catch (final Throwable oops) {
				final Actor test = this.actorService.findActorByUsername(companyForm.getUsername());
				if (test != null)
					if (companyForm.getId() == 0)
						res = this.createEditModelAndView(companyForm, "actor.userExists");
					else if (!test.equals(this.actorService.findPrincipal()))
						res = this.createEditModelAndView(companyForm, "actor.userExists");
					else
						res = this.createEditModelAndView(companyForm, "cannot.commit.error");
			}

		return res;
	}
	@RequestMapping("any/display")
	public ModelAndView display(@RequestParam(required = true) final Integer companyId) {
		final ModelAndView res = new ModelAndView("company/display");
		final Company company = this.companyService.findOne(companyId);
		res.addObject("company", company);
		res.addObject("isPrincipalAuthorizedEdit", this.isPrincipalAuthorizedEdit(company));
		res.addObject("banner", this.configurationService.findOne().getBanner());
		return res;
	}

	@RequestMapping("company/display")
	public ModelAndView display() {
		final ModelAndView res = new ModelAndView("company/display");
		final Company company = (Company) this.actorService.findPrincipal();
		res.addObject("company", company);

		res.addObject("isPrincipalAuthorizedEdit", this.isPrincipalAuthorizedEdit(company));
		res.addObject("banner", this.configurationService.findOne().getBanner());
		return res;
	}

	private ModelAndView createEditModelAndView(final CompanyForm companyForm) {
		return this.createEditModelAndView(companyForm, null);
	}

	private ModelAndView createEditModelAndView(final CompanyForm companyForm, final String message) {
		final ModelAndView res = new ModelAndView("company/edit");
		final Boolean isPrincipalAuthorizedEdit = this.isPrincipalAuthorizedEdit(companyForm);
		res.addObject("companyForm", companyForm);
		res.addObject("message", message);
		res.addObject("banner", this.configurationService.findOne().getBanner());
		res.addObject("isPrincipalAuthorizedEdit", isPrincipalAuthorizedEdit);
		final Configuration configuration = this.configurationService.findOne();
		final List<String> makes = configuration.getMakeName();
		res.addObject("makes", makes);
		Integer actorId = null;
		try {
			actorId = this.actorService.findPrincipal().getId();
		} catch (final Throwable t) {
			actorId = 0;
		}
		res.addObject("actorId", actorId);
		return res;
	}
	private Boolean isPrincipalAuthorizedEdit(final CompanyForm companyForm) {
		Boolean res = false;
		Actor principal = null;
		try {
			principal = this.actorService.findPrincipal();
		} catch (final IllegalArgumentException e) {
			principal = null;
		}
		if (companyForm.getId() > 0)
			res = principal.getId() == companyForm.getId();
		else if (companyForm.getId() == 0)
			res = principal == null;
		return res;
	}

	private Boolean isPrincipalAuthorizedEdit(final Company company) {
		Boolean res = false;
		Actor principal = null;
		try {
			principal = this.actorService.findPrincipal();
		} catch (final IllegalArgumentException e) {
			principal = null;
		}
		if (company.getId() > 0 && principal != null)
			res = principal.getId() == company.getId();
		else if (company.getId() == 0)
			res = principal == null;
		return res;
	}

	@RequestMapping(value = "/deleteCompany", method = RequestMethod.GET)
	public ModelAndView deleteAllData() {

		ModelAndView result;
		final Actor s = this.actorService.findPrincipal();
		try {
			this.companyService.deleteCompany((Company) s);
			result = new ModelAndView("redirect:/j_spring_security_logout");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			System.out.println("NO SE HA PODIDO BORRAR EL USUARIO");
		}
		return result;
	}

}


package controllers.auditor;

import java.util.ArrayList;
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

import security.LoginService;
import services.AuditService;
import services.AuditorService;
import services.ConfigurationService;
import services.FlugotService;
import controllers.AbstractController;
import domain.Audit;
import domain.Auditor;
import domain.Flugot;

@Controller
@RequestMapping("/flugot/auditor")
public class FlugotAuditorController extends AbstractController {

	//Services---------------------------------------------------

	@Autowired
	private FlugotService			flugotService;

	@Autowired
	private AuditorService			auditorService;

	@Autowired
	private AuditService			auditService;

	@Autowired
	private ConfigurationService	configurationService;


	//List-------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView modelAndView = new ModelAndView("flugot/list");

		final Auditor auditor = this.auditorService.findAuditorByUserAcountId(LoginService.getPrincipal().getId());
		final List<Flugot> flugots = this.flugotService.findFlugotByAuditor(auditor.getId());

		modelAndView.addObject("flugots", flugots);
		modelAndView.addObject("flugotService", this.flugotService);
		modelAndView.addObject("requestURI", "/flugot/auditor/list.do");
		modelAndView.addObject("banner", this.configurationService.findOne().getBanner());

		return modelAndView;
	}
	//List audit
	@RequestMapping(value = "/listAudit", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int auditId) {
		ModelAndView modelAndView;

		final List<Flugot> flugots = this.flugotService.findFlugotByAudit(auditId);

		modelAndView = new ModelAndView("flugot/list");
		modelAndView.addObject("flugots", flugots);
		modelAndView.addObject("requestURI", "flugot/company/list.do");
		modelAndView.addObject("banner", this.configurationService.findOne().getBanner());

		return modelAndView;

	}

	//Show---------------------------------------------------------

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int flugotId) {
		final ModelAndView result = new ModelAndView("flugot/display");

		final Flugot flugot = this.flugotService.findOne(flugotId);

		result.addObject("flugot", flugot);
		result.addObject("banner", this.configurationService.findOne().getBanner());

		return result;
	}

	//Create--------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;

		final Flugot flugot = this.flugotService.create();

		result = this.createEditModelAndView(flugot);
		result.addObject("requestURI", "flugot/auditor/create.do");

		return result;
	}

	//Edit------------------------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int flugotId) {
		ModelAndView result;

		final Flugot flugot = this.flugotService.findOne(flugotId);
		Assert.notNull(flugot);

		result = this.createEditModelAndView(flugot);

		return result;

	}

	//Save-------------------------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Flugot flugot, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(flugot);
		else
			try {
				flugot = this.flugotService.reconstruct(flugot, binding);
				this.flugotService.save(flugot);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(flugot, "flugot.commit.error");
			}
		return result;
	}

	//Delete--------------------------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Flugot flugot, final BindingResult binding) {
		ModelAndView result;
		try {
			this.flugotService.delete(flugot);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(flugot, "flugot.commit.error");
		}
		return result;
	}

	//Auxiliary----------------------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Flugot flugot) {
		ModelAndView result;

		result = this.createEditModelAndView(flugot, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Flugot flugot, final String messageCode) {
		final ModelAndView result;

		final Auditor auditor = this.auditorService.findAuditorByUserAcountId(LoginService.getPrincipal().getId());
		final List<Audit> audits = new ArrayList<>(this.auditService.findAuditsFinalByAuditorId(auditor.getId()));

		result = new ModelAndView("flugot/edit");
		result.addObject("flugot", flugot);
		result.addObject("audits", audits);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "flugot/auditor/edit.do?flugotId=" + flugot.getId());
		result.addObject("banner", this.configurationService.findOne().getBanner());

		return result;
	}
}

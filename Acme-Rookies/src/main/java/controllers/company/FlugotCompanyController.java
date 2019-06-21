
package controllers.company;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AuditService;
import services.CompanyService;
import services.ConfigurationService;
import services.FlugotService;
import controllers.AbstractController;
import domain.Flugot;

@Controller
@RequestMapping("/flugot/company")
public class FlugotCompanyController extends AbstractController {

	//Services---------------------------------------------------

	@Autowired
	private FlugotService			flugotService;

	@Autowired
	private CompanyService			companyService;

	@Autowired
	private AuditService			auditService;

	@Autowired
	private ConfigurationService	configurationService;


	//List-------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int auditId) {
		ModelAndView modelAndView;

		final List<Flugot> flugots = this.flugotService.findFlugotFinalsByAudit(auditId);

		modelAndView = new ModelAndView("flugot/list");
		modelAndView.addObject("flugots", flugots);
		modelAndView.addObject("requestURI", "flugot/company/list.do");
		modelAndView.addObject("banner", this.configurationService.findOne().getBanner());

		return modelAndView;

	}

}


package controllers.company;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;

import security.LoginService;
import services.CompanyService;
import services.ConfigurationService;
import services.PositionService;
import domain.Company;
import domain.Position;

@Controller
@RequestMapping("/position")
public class PositionCompanyController extends AbstractController {

	//-----------------Services-------------------------

	@Autowired
	PositionService			positionService;

	@Autowired
	CompanyService			companyService;

	@Autowired
	ConfigurationService	configurationService;


	//-------------------------- List ----------------------------------
	@RequestMapping(value = "/myList", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView modelAndView;
		Company comp;
		comp = this.companyService.findCompanyByUserAcountId(LoginService.getPrincipal().getId());

		final Collection<Position> positions = this.positionService.findPositionsByCompanyId(comp.getId());

		modelAndView = new ModelAndView("position/myList");
		modelAndView.addObject("positions", positions);

		modelAndView.addObject("requestURI", "position/myList.do");
		modelAndView.addObject("banner", this.configurationService.findOne().getBanner());

		return modelAndView;

	}

	//	//Searching-------------------------------------------------------------
	//
	//	@RequestMapping(value = "/searchResult", method = RequestMethod.POST, params = "search")
	//	public ModelAndView search(@RequestParam final String keyword) {
	//		final ModelAndView modelAndView = this.list();
	//
	//		final List<Position> positions = this.positionService.searchingPositions(keyword);
	//
	//		modelAndView.addObject("positions", positions);
	//
	//		return modelAndView;
	//	}

	//	//-----------------Display-------------------------
	//
	//	@RequestMapping(value = "/display", method = RequestMethod.GET)
	//	public ModelAndView display(@RequestParam final int periodRecordId) {
	//		ModelAndView result;
	//		PeriodRecord periodRecord;
	//		periodRecord = this.periodRecordService.findOne(periodRecordId);
	//		result = new ModelAndView("periodRecord/display");
	//		result.addObject("periodRecord", periodRecord);
	//		result.addObject("banner", this.configurationService.findOne().getBanner());
	//
	//		return result;
	//	}
	//	//
	//	//	//------------------------------------------
	//	@RequestMapping(value = "/create", method = RequestMethod.GET)
	//	public ModelAndView create() {
	//		ModelAndView result;
	//		PeriodRecord periodRecord;
	//		periodRecord = this.periodRecordService.create();
	//		result = this.createEditModelAndView(periodRecord);
	//		return result;
	//
	//	}
	//
	//	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	//	public ModelAndView edit(@RequestParam final int periodRecordId) {
	//		ModelAndView result;
	//		final PeriodRecord periodRecord;
	//
	//		periodRecord = this.periodRecordService.findOne(periodRecordId);
	//		Assert.notNull(periodRecord);
	//
	//		result = this.createEditModelAndView(periodRecord);
	//
	//		return result;
	//
	//	}
	//
	//	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	//	public ModelAndView save(@Valid final PeriodRecord periodRecord, final BindingResult binding) {
	//		ModelAndView result;
	//		final Exception dateErr = new Exception("fechas MAL");
	//
	//		if (binding.hasErrors()) {
	//			if (binding.getAllErrors().toString().contains("URL"))
	//				result = this.createEditModelAndView(periodRecord, "history.URL.error");
	//			else
	//				result = this.createEditModelAndView(periodRecord);
	//		} else
	//			try {
	//				if (!periodRecord.getStartYear().before(periodRecord.getEndYear()))
	//					throw dateErr;
	//				else if (this.periodRecordService.checkEquals(periodRecord))
	//					result = this.createEditModelAndView(periodRecord, "history.equalsRecord.error");
	//				else {
	//					this.periodRecordService.save(periodRecord);
	//					result = new ModelAndView("redirect:list.do?historyId=" + periodRecord.getHistory().getId());
	//				}
	//			} catch (final Throwable oops) {
	//				if (oops.getMessage().equals("fechas MAL"))
	//					result = this.createEditModelAndView(periodRecord, "periodRecord.date.error");
	//				else
	//					result = this.createEditModelAndView(periodRecord, "periodRecord.commit.error");
	//			}
	//		return result;
	//	}
	//	protected ModelAndView createEditModelAndView(final PeriodRecord periodRecord, final String message) {
	//		final ModelAndView result;
	//
	//		result = new ModelAndView("periodRecord/edit");
	//
	//		result.addObject("periodRecord", periodRecord);
	//		result.addObject("banner", this.configurationService.findOne().getBanner());
	//		result.addObject("message", message);
	//		result.addObject("requestURI", "periodRecord/brotherhood/edit.do");
	//
	//		return result;
	//	}
	//
	//	protected ModelAndView createEditModelAndView(final PeriodRecord periodRecord) {
	//		ModelAndView result;
	//
	//		result = this.createEditModelAndView(periodRecord, null);
	//
	//		return result;
	//	}
	//	//
	//	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	//	public ModelAndView delete(final PeriodRecord periodRecord, final BindingResult binding) {
	//		ModelAndView result;
	//		try {
	//			this.periodRecordService.delete(periodRecord);
	//			result = new ModelAndView("redirect:list.do?historyId=" + periodRecord.getHistory().getId());
	//		} catch (final Throwable oops) {
	//			result = this.createEditModelAndView(periodRecord, "periodRecord.commit.error");
	//
	//		}
	//		return result;
	//	}

	//}

}


package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import services.CompanyService;
import services.ConfigurationService;
import services.RookieService;
import services.MessageService;
import services.PositionService;
import services.ProblemService;
import services.ServiceUtils;
import services.SponsorshipService;
import domain.Rookie;
import domain.Position;
import domain.Problem;
import domain.Sponsorship;

@Controller
@RequestMapping("/position")
public class PositionController extends AbstractController {

	//-----------------Services-------------------------

	@Autowired
	PositionService			positionService;

	@Autowired
	CompanyService			companyService;

	@Autowired
	ConfigurationService	configurationService;

	@Autowired
	ProblemService			problemService;

	@Autowired
	MessageService			messageService;

	@Autowired
	RookieService			rookieService;

	@Autowired
	ServiceUtils			serviceUtils;

	@Autowired
	SponsorshipService		sponsorshipService;


	//-------------------------- List ----------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final Integer companyId) {
		ModelAndView modelAndView;
		Date now;
		now = new Date(System.currentTimeMillis() - 1000);
		List<Position> positions2 = new ArrayList<Position>();
		if (LoginService.getPrincipal2().getAuthentication().getPrincipal() != "anonymousUser") {
			final int userAccountId = LoginService.getPrincipal().getId();
			final Rookie rookie = this.rookieService.findRookieByUserAcountId(userAccountId);
			if (rookie != null) {
				final int rookieId = rookie.getId();
				positions2 = this.positionService.findPositionByRookieId(rookieId);
			}
		}
		modelAndView = new ModelAndView("position/list");
		if (companyId == null) {
			final List<Position> positions = this.positionService.finalPositions();
			positions.removeAll(positions2);
			modelAndView.addObject("positions", positions);
			modelAndView.addObject("requestURI", "position/list.do");
		} else {
			final Collection<Position> positions = this.positionService.findPositionsByCompanyIdFinals(companyId);
			modelAndView.addObject("positions", positions);
			modelAndView.addObject("requestURI", "position/list.do?companyId=" + companyId.toString());
		}
		modelAndView.addObject("now", now);
		modelAndView.addObject("banner", this.configurationService.findOne().getBanner());

		return modelAndView;

	}
	//-------------------------- List ----------------------------------
	@RequestMapping(value = "/listtwo", method = RequestMethod.GET)
	public ModelAndView listtwo() {
		ModelAndView modelAndView;
		Date now;

		now = new Date(System.currentTimeMillis() - 1000);
		final Collection<Position> positions = this.positionService.findFinalPositionsWithoutDeadline();
		final int userAccountId = LoginService.getPrincipal().getId();
		final Rookie rookie = this.rookieService.findRookieByUserAcountId(userAccountId);
		final int rookieId = rookie.getId();
		final List<Position> positions2 = this.positionService.findPositionByRookieId(rookieId);
		positions.removeAll(positions2);

		modelAndView = new ModelAndView("position/list");
		modelAndView.addObject("positions", positions);
		modelAndView.addObject("now", now);
		modelAndView.addObject("requestURI", "position/listtwo.do");
		modelAndView.addObject("banner", this.configurationService.findOne().getBanner());

		return modelAndView;

	}

	//-------------------------- ListToAudit ----------------------------------
	@RequestMapping(value = "/listToAudit", method = RequestMethod.GET)
	public ModelAndView listToAudit() {
		ModelAndView modelAndView;

		//		final Collection<Position> positions = this.positionService.getPositionsWithoutAuditor();
		final Collection<Position> positions = this.positionService.getPositionsToAudit();

		modelAndView = new ModelAndView("position/listToAudit");
		modelAndView.addObject("positions", positions);
		modelAndView.addObject("requestURI", "position/listToAudit.do");
		modelAndView.addObject("banner", this.configurationService.findOne().getBanner());

		return modelAndView;

	}

	//Searching-------------------------------------------------------------

	@RequestMapping(value = "/searchResult", method = RequestMethod.POST, params = "search")
	public ModelAndView search(@RequestParam final String keyword) {
		final ModelAndView modelAndView = this.list(null);

		final List<Position> positions = this.positionService.searchingPositions(keyword);

		modelAndView.addObject("positions", positions);
		modelAndView.addObject("banner", this.configurationService.findOne().getBanner());

		return modelAndView;
	}

	//	//-----------------Display-------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int positionId) {
		ModelAndView result;
		final Position position;
		result = new ModelAndView("position/display");
		position = this.positionService.findOne(positionId);
		final Collection<Problem> problems = this.positionService.findProblemsByPositionId(positionId);

		try {
			final Sponsorship sponsorship = this.sponsorshipService.getRandomSponsorshipByPositionId(positionId);
			result.addObject("sponsorship", sponsorship);
		} catch (final Exception e) {
			// TODO: handle exception
		}

		result.addObject("position", position);
		result.addObject("problems", problems);

		result.addObject("banner", this.configurationService.findOne().getBanner());

		return result;
	}
	//
	//	//------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Position position;
		position = this.positionService.create();
		result = this.createEditModelAndView(position);
		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int positionId) {
		ModelAndView result;
		final Position position;

		position = this.positionService.findOne(positionId);
		Assert.notNull(position);

		result = this.createEditModelAndView(position);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Position position, final BindingResult binding) {
		ModelAndView result;

		final Position oldPosition = this.positionService.findOne(position.getId());
		Boolean oldPositionFinal = null;
		if (oldPosition != null)
			oldPositionFinal = oldPosition.isFinalMode();

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(position);
			position.setFinalMode(false);
			System.out.println(binding.getAllErrors());

		} else
			try {

				final Position savedPosition = this.positionService.save(position);
				result = new ModelAndView("redirect:myList.do");

				// Si se crea una nueva posición se notifica a los rookies interesados
				if (oldPositionFinal != null)
					if (oldPositionFinal != savedPosition.isFinalMode()) {
						this.positionService.flush();
						System.out.println(this.positionService.findAll());
						this.messageService.notificationPosition(savedPosition);
					}

			} catch (final Throwable oops) {
				if (oldPositionFinal != null)
					position.setFinalMode(oldPositionFinal);
				if (oops.getMessage().toString().contains("faltanProblemas"))
					result = this.createEditModelAndView(position, "position.faltanProblemas");
				else if (oops.getMessage().toString().contains("fechaIncorrecta"))
					result = this.createEditModelAndView(position, "position.fechaIncorrecta");

				else {

					result = this.createEditModelAndView(position, "position.commit.error");
					System.out.println(oops.getMessage());
					System.out.println(oops);
				}

				System.out.println(position.getDescription() + "\n" + position.getId() + "\n" + position.getProfile() + "\n" + position.getSalary() + "\n" + position.getSkills() + "\n" + position.getTechnologies() + "\n" + position.getTicker() + "\n"
					+ position.getTitle() + "\n" + position.getVersion() + "\n" + position.getCompany() + "\n" + position.getDeadLine() + "\n" + position.isFinalMode() + "\n" + position.isCancel());

				System.out.println("\n La compania es ---->" + position.getCompany());

			}
		return result;
	}
	protected ModelAndView createEditModelAndView(final Position position, final String message) {
		final ModelAndView result;
		final Boolean tieneApps;
		Boolean readonly = null;

		if (position.isFinalMode() == true)
			readonly = true;

		result = new ModelAndView("position/edit");
		tieneApps = this.positionService.tieneApps(position);
		result.addObject("position", position);
		result.addObject("banner", this.configurationService.findOne().getBanner());
		result.addObject("message", message);
		result.addObject("readonly", readonly);

		result.addObject("tieneApps", tieneApps);

		System.out.println("---->" + tieneApps);

		result.addObject("requestURI", "periodRecord/brotherhood/edit.do");

		return result;
	}
	//
	protected ModelAndView createEditModelAndView(final Position position) {
		ModelAndView result;

		result = this.createEditModelAndView(position, null);

		return result;
	}

	//

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Position position, final BindingResult binding) {
		ModelAndView result;
		try {
			this.positionService.delete(position);
			result = new ModelAndView("redirect:myList.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(position, "position.commit.error");
			System.out.println("El error es  --->" + binding.hasErrors());

		}
		return result;
	}
}

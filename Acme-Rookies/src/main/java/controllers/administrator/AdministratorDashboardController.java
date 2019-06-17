/*
 * AdministratorController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.administrator;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ApplicationService;
import services.AuditService;
import services.ConfigurationService;
import services.CurriculaService;
import services.FinderService;
import services.ItemService;
import services.PositionService;
import services.SponsorshipService;
import controllers.AbstractController;
import domain.Provider;

@Controller
@RequestMapping("/administrator")
public class AdministratorDashboardController extends AbstractController {

	@Autowired
	private PositionService			positionService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private ApplicationService		applicationService;

	@Autowired
	private CurriculaService		curriculaService;

	@Autowired
	private FinderService			finderService;

	@Autowired
	private AuditService			auditService;

	@Autowired
	private ItemService				itemService;

	@Autowired
	private SponsorshipService		sponsorshipService;


	// Constructors -----------------------------------------------------------

	public AdministratorDashboardController() {
		super();
	}

	// Dashboard---------------------------------------------------------------

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public ModelAndView dashboard() throws ParseException {
		ModelAndView result;
		result = new ModelAndView("administrator/dashboard");
		final DecimalFormat df = new DecimalFormat("0.00");

		result.addObject("banner", this.configurationService.findOne().getBanner());

		//DASHBOARD ACME-RookieRank
		//QueryC1
		final Double queryC1AVG = this.positionService.queryC1AVG();
		final Double queryC1MAX = this.positionService.queryC1MAX();
		final Double queryC1MIN = this.positionService.queryC1MIN();
		final Double queryC1STDDEV = this.positionService.queryC1STDDEV();

		if (queryC1AVG != null)
			result.addObject("queryC1AVG", df.format(queryC1AVG));
		else
			result.addObject("queryC1AVG", 0.0);

		if (queryC1MAX != null)
			result.addObject("queryC1MAX", df.format(queryC1MAX));
		else
			result.addObject("queryC1MAX", 0.0);

		if (queryC1MIN != null)
			result.addObject("queryC1MIN", df.format(queryC1MIN));
		else
			result.addObject("queryC1MIN", 0.0);

		if (queryC1STDDEV != null)
			result.addObject("queryC1STDDEV", df.format(queryC1STDDEV));
		else
			result.addObject("queryC1STDDEV", 0.0);

		//QUERY C2
		final Double queryC2AVG = this.applicationService.queryC2AVG();
		final Double queryC2MAX = this.applicationService.queryC2MAX();
		final Double queryC2MIN = this.applicationService.queryC2MIN();
		final Double queryC2STDDEV = this.applicationService.queryC2STDDEV();

		if (queryC2AVG != null)
			result.addObject("queryC2AVG", df.format(queryC2AVG));
		else
			result.addObject("queryC2AVG", 0.0);

		if (queryC2MAX != null)
			result.addObject("queryC2MAX", df.format(queryC2MAX));
		else
			result.addObject("queryC2MAX", 0.0);

		if (queryC2MIN != null)
			result.addObject("queryC2MIN", df.format(queryC2MIN));
		else
			result.addObject("queryC2MIN", 0.0);

		if (queryC2STDDEV != null)
			result.addObject("queryC2STDDEV", df.format(queryC2STDDEV));
		else
			result.addObject("queryC2STDDEV", 0.0);

		//QUERY C3
		final List<String> queryC3 = this.positionService.queryC3();
		if (!queryC3.isEmpty())
			result.addObject("queryC3", queryC3);

		//QUERY C4
		final List<String> queryC4 = this.applicationService.queryC4();
		if (!queryC4.isEmpty())
			result.addObject("queryC4", queryC4);

		//Query C5
		final Object[] queryC5 = this.positionService.queryC5();

		final Double avgC5 = (Double) queryC5[0];
		final Double maxC5 = (Double) queryC5[1];
		final Double minC5 = (Double) queryC5[2];
		final Double stddevC5 = (Double) queryC5[3];

		if (avgC5 != null)
			result.addObject("avgC5", df.format(avgC5));
		else
			result.addObject("avgC5", 0.0);

		if (maxC5 != null)
			result.addObject("maxC5", df.format(maxC5));
		else
			result.addObject("maxC5", 0.0);

		if (minC5 != null)
			result.addObject("minC5", df.format(minC5));
		else
			result.addObject("minC5", 0.0);

		if (stddevC5 != null)
			result.addObject("stddevC5", df.format(stddevC5));
		else
			result.addObject("stddevC5", 0.0);

		//QUERY C3
		final String queryC6Best = this.positionService.queryC6Best().get(0);
		if (!queryC6Best.isEmpty())
			result.addObject("queryC6Best", queryC6Best);

		//QUERY C4
		final String queryC6Worst = this.positionService.queryC6Worst().get(0);
		if (!queryC6Worst.isEmpty())
			result.addObject("queryC6Worst", queryC6Worst);

		//QUERY B1
		final Double queryB1AVG = this.curriculaService.queryB1AVG();
		final Double queryB1MAX = this.curriculaService.queryB1MAX();
		final Double queryB1MIN = this.curriculaService.queryB1MIN();
		final Double queryB1STDDEV = this.curriculaService.queryB1STDDEV();

		if (queryB1AVG != null)
			result.addObject("queryB1AVG", df.format(queryB1AVG));
		else
			result.addObject("queryB1AVG", 0.0);

		if (queryB1MAX != null)
			result.addObject("queryB1MAX", df.format(queryB1MAX));
		else
			result.addObject("queryB1MAX", 0.0);

		if (queryB1MIN != null)
			result.addObject("queryB1MIN", df.format(queryB1MIN));
		else
			result.addObject("queryB1MIN", 0.0);

		if (queryC2STDDEV != null)
			result.addObject("queryB1STDDEV", df.format(queryB1STDDEV));
		else
			result.addObject("queryB1STDDEV", 0.0);

		//QUERY B2
		final Double queryB2AVG = this.finderService.queryB2AVG();
		final Double queryB2MAX = this.finderService.queryB2MAX();
		final Double queryB2MIN = this.finderService.queryB2MIN();
		final Double queryB2STDDEV = this.finderService.queryB2STDDEV();

		if (queryB2AVG != null)
			result.addObject("queryB2AVG", df.format(queryB2AVG));
		else
			result.addObject("queryB2AVG", 0.0);

		if (queryB2MAX != null)
			result.addObject("queryB2MAX", df.format(queryB2MAX));
		else
			result.addObject("queryB2MAX", 0.0);

		if (queryB2MIN != null)
			result.addObject("queryB2MIN", df.format(queryB2MIN));
		else
			result.addObject("queryB2MIN", 0.0);

		if (queryB2STDDEV != null)
			result.addObject("queryB2STDDEV", df.format(queryB2STDDEV));
		else
			result.addObject("queryB2STDDEV", 0.0);

		final Double queryB3 = this.finderService.queryB3();
		if (queryB3 != null)
			result.addObject("queryB3", df.format(queryB3));
		else
			result.addObject("queryB3", 0.0);

		//DASHBOARD ACME-ROOKIES
		//QUERY C1
		final Double queryRookiesC1AVG = this.auditService.queryRookiesC1AVG();
		final Double queryRookiesC1MAX = this.auditService.queryRookiesC1MAX();
		final Double queryRookiesC1MIN = this.auditService.queryRookiesC1MIN();
		final Double queryRookiesC1STDDEV = this.auditService.queryRookiesC1STDDEV();

		if (queryRookiesC1AVG != null)
			result.addObject("queryRookiesC1AVG", df.format(queryRookiesC1AVG));
		else
			result.addObject("queryRookiesC1AVG", 0.0);

		if (queryRookiesC1MAX != null)
			result.addObject("queryRookiesC1MAX", df.format(queryRookiesC1MAX));
		else
			result.addObject("queryRookiesC1MAX", 0.0);

		if (queryRookiesC1MIN != null)
			result.addObject("queryRookiesC1MIN", df.format(queryRookiesC1MIN));
		else
			result.addObject("queryRookiesC1MIN", 0.0);

		if (queryRookiesC1STDDEV != null)
			result.addObject("queryRookiesC1STDDEV", df.format(queryRookiesC1STDDEV));
		else
			result.addObject("queryRookiesC1STDDEV", 0.0);

		//QUERY C2
		final Double queryRookiesC2AVG = this.auditService.queryRookiesC2AVG();
		final Double queryRookiesC2MAX = this.auditService.queryRookiesC2MAX();
		final Double queryRookiesC2MIN = this.auditService.queryRookiesC2MIN();
		final Double queryRookiesC2STDDEV = this.auditService.queryRookiesC2STDDEV();

		if (queryRookiesC2AVG != null)
			result.addObject("queryRookiesC2AVG", df.format(queryRookiesC2AVG));
		else
			result.addObject("queryRookiesC2AVG", 0.0);

		if (queryRookiesC2MAX != null)
			result.addObject("queryRookiesC2MAX", df.format(queryRookiesC2MAX));
		else
			result.addObject("queryRookiesC2MAX", 0.0);

		if (queryRookiesC2MIN != null)
			result.addObject("queryRookiesC2MIN", df.format(queryRookiesC2MIN));
		else
			result.addObject("queryRookiesC2MIN", 0.0);

		if (queryRookiesC2STDDEV != null)
			result.addObject("queryRookiesC2STDDEV", df.format(queryRookiesC2STDDEV));
		else
			result.addObject("queryRookiesC2STDDEV", 0.0);

		//QUERY C3
		final List<String> queryRookiesC3 = this.auditService.queryRookiesC3();
		if (!queryRookiesC3.isEmpty())
			result.addObject("queryRookiesC3", queryRookiesC3);

		//QUERY C4
		final Double queryRookiesC4 = this.auditService.queryRookiesC4();
		if (queryRookiesC4 != null)
			result.addObject("queryRookiesC4", df.format(queryRookiesC4));
		else
			result.addObject("queryRookiesC4", 0.0);

		//QUERY B1
		final Double queryRookiesB1AVG = this.itemService.queryRookiesB1AVG();
		final Double queryRookiesB1MAX = this.itemService.queryRookiesB1MAX();
		final Double queryRookiesB1MIN = this.itemService.queryRookiesB1MIN();
		final Double queryRookiesB1STDDEV = this.itemService.queryRookiesB1STDDEV();

		if (queryRookiesB1AVG != null)
			result.addObject("queryRookiesB1AVG", df.format(queryRookiesB1AVG));
		else
			result.addObject("queryRookiesB1AVG", 0.0);

		if (queryRookiesB1MAX != null)
			result.addObject("queryRookiesB1MAX", df.format(queryRookiesB1MAX));
		else
			result.addObject("queryRookiesB1MAX", 0.0);

		if (queryRookiesB1MIN != null)
			result.addObject("queryRookiesB1MIN", df.format(queryRookiesB1MIN));
		else
			result.addObject("queryRookiesB1MIN", 0.0);

		if (queryRookiesB1STDDEV != null)
			result.addObject("queryRookiesB1STDDEV", df.format(queryRookiesB1STDDEV));
		else
			result.addObject("queryRookiesB1STDDEV", 0.0);

		//QUERY B2
		final List<String> queryRookiesB2 = this.itemService.queryRookiesB2();
		if (!queryRookiesB2.isEmpty())
			if (queryRookiesB2.size() < 5)
				result.addObject("queryRookiesB2", queryRookiesB2);
		if (queryRookiesB2.size() >= 5)
			result.addObject("queryRookiesB2", queryRookiesB2.subList(0, 5));

		//QUERY A1
		final Double queryRookiesA1AVG = this.sponsorshipService.queryRookiesA1AVG();
		final Double queryRookiesA1MAX = this.sponsorshipService.queryRookiesA1MAX();
		final Double queryRookiesA1MIN = this.sponsorshipService.queryRookiesA1MIN();
		final Double queryRookiesA1STDDEV = this.sponsorshipService.queryRookiesA1STDDEV();

		if (queryRookiesA1AVG != null)
			result.addObject("queryRookiesA1AVG", df.format(queryRookiesA1AVG));
		else
			result.addObject("queryRookiesA1AVG", 0.0);

		if (queryRookiesA1MAX != null)
			result.addObject("queryRookiesA1MAX", df.format(queryRookiesA1MAX));
		else
			result.addObject("queryRookiesA1MAX", 0.0);

		if (queryRookiesA1MIN != null)
			result.addObject("queryRookiesA1MIN", df.format(queryRookiesA1MIN));
		else
			result.addObject("queryRookiesA1MIN", 0.0);

		if (queryRookiesA1STDDEV != null)
			result.addObject("queryRookiesA1STDDEV", df.format(queryRookiesA1STDDEV));
		else
			result.addObject("queryRookiesA1STDDEV", 0.0);

		//QUERY A2
		final Double queryRookiesA2AVG = this.sponsorshipService.queryRookiesA2AVG();
		final Double queryRookiesA2MAX = this.sponsorshipService.queryRookiesA2MAX();
		final Double queryRookiesA2MIN = this.sponsorshipService.queryRookiesA2MIN();
		final Double queryRookiesA2STDDEV = this.sponsorshipService.queryRookiesA2STDDEV();

		if (queryRookiesA2AVG != null)
			result.addObject("queryRookiesA2AVG", df.format(queryRookiesA2AVG));
		else
			result.addObject("queryRookiesA2AVG", 0.0);

		if (queryRookiesA2MAX != null)
			result.addObject("queryRookiesA2MAX", df.format(queryRookiesA2MAX));
		else
			result.addObject("queryRookiesA2MAX", 0.0);

		if (queryRookiesA2MIN != null)
			result.addObject("queryRookiesA2MIN", df.format(queryRookiesA2MIN));
		else
			result.addObject("queryRookiesA2MIN", 0.0);

		if (queryRookiesA2STDDEV != null)
			result.addObject("queryRookiesA2STDDEV", df.format(queryRookiesA2STDDEV));
		else
			result.addObject("queryRookiesA2STDDEV", 0.0);

		//QUERY A3
		final Collection<Provider> queryRookiesA3 = this.sponsorshipService.queryRookiesA3();
		if (!queryRookiesA3.isEmpty())
			result.addObject("queryRookiesA3", queryRookiesA3);

		return result;
	}
}

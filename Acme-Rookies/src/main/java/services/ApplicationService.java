
package services;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestParam;

import repositories.ApplicationRepository;
import security.LoginService;
import security.UserAccount;
import domain.Application;
import domain.Position;
import domain.Problem;
import domain.Rookie;

@Service
@Transactional
public class ApplicationService {

	//--------------Managed repository---------------------------

	@Autowired
	private ApplicationRepository	applicationRepository;

	//-------------- Supporting Services-----------------------

	@Autowired
	private PositionService			positionService;

	@Autowired
	private ServiceUtils			serviceUtils;


	// --------------------------Constructor-----------------------

	public ApplicationService() {
		super();
	}

	// --------------------CRUD methods----------------------------
	public Date getFechaCreacion1(final Date fechaActual) {
		final Date fechaCreacion = fechaActual;
		return fechaCreacion;
	}

	public Application create(@RequestParam final int positionId) {
		final Application result;

		final Position position = this.positionService.findOne(positionId);
		final Date fechaActual = new Date(System.currentTimeMillis() - 1000);
		final List<Problem> problems = this.applicationRepository.findRandomFinalProblem(position);
		final Problem problem = problems.get(0);
		final UserAccount userAccount = LoginService.getPrincipal();
		final Rookie rookie = this.applicationRepository.findRookieByUserAccount(userAccount);

		result = new Application();
		result.setPublishMoment(fechaActual);
		result.setStatus("PENDING");
		result.setPosition(position);
		result.setProblem(problem);
		result.setRookie(rookie);
		return result;

	}

	public Application save(final Application application) {
		final Application oldApplication = this.applicationRepository.findOne(application.getId());
		Assert.notNull(application);
		if (application.getStatus().equals("SUBMITTED")) {
			application.setSubmitMoment(new Date(System.currentTimeMillis() - 1000));
			application.setPublishMoment(oldApplication.getPublishMoment());
			if (oldApplication != null)
				application.setCurricula(oldApplication.getCurricula());
		}
		if (application.getStatus().equals("ACCEPTED") || application.getStatus().equals("REJECTED")) {
			application.setSubmitMoment(oldApplication.getSubmitMoment());
			application.setPublishMoment(oldApplication.getPublishMoment());
			application.setAnswerCode(oldApplication.getAnswerCode());
			application.setAnswerExplanation(oldApplication.getAnswerExplanation());
			if (oldApplication != null)
				application.setCurricula(oldApplication.getCurricula());
		}
		if (application.getStatus().equals("PENDING")) {
			application.setPublishMoment(new Date(System.currentTimeMillis() - 1000));
			if (oldApplication != null)
				application.setCurricula(oldApplication.getCurricula());
		}
		this.applicationRepository.save(application);

		return application;
	}
	public Application saveCompany(final Application application) {
		Application res = null;
		Assert.notNull(application);

		res = this.applicationRepository.save(application);
		return res;

	}

	public Application findOne(final int applicationId) {
		return this.applicationRepository.findOne(applicationId);
	}

	public Collection<Application> findAll() {
		Collection<Application> res;
		res = this.applicationRepository.findAll();
		Assert.notNull(res);

		return res;
	}

	public void delete1(final Application application) {
		Assert.notNull(application);
		this.serviceUtils.checkActor(application.getRookie());

		this.applicationRepository.delete(application);

	}

	public void delete(final Application application) {
		Assert.notNull(application);
		this.serviceUtils.checkActor(application.getPosition().getCompany());

		this.applicationRepository.delete(application);

	}

	//OTHER METHODS------------------------------

	public List<Application> findApplicationByRookie(final Rookie rookie) {
		return this.applicationRepository.findApplicationByRookie(rookie);
	}

	public Collection<Application> getAppsByPosition(final Integer positionId) {
		final Collection<Application> res = this.applicationRepository.findApplicationByPositionId(positionId);
		return res;
	}

	public Rookie findRookieByUserAccount(final UserAccount userAccount) {
		Rookie rookie;
		rookie = this.applicationRepository.findRookieByUserAccount(userAccount);
		return rookie;
	}

	public int findCompanyIdByUserAccountId(final int userAccountId) {
		return this.applicationRepository.findCompanyIdByUserAccountId(userAccountId);
	}

	public Collection<Application> findApplicationByStatusAndCompany(final int companyId) {
		return this.applicationRepository.findApplicationByStatusAndCompany(companyId);
	}

	public Collection<Application> findApplicationByCompany(final int companyId) {
		return this.applicationRepository.findApplicationByCompany(companyId);
	}

	public Double queryC2AVG() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.applicationRepository.queryC2AVG();
	}

	public Double queryC2MAX() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.applicationRepository.queryC2MAX();
	}

	public Double queryC2MIN() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.applicationRepository.queryC2MIN();
	}

	public Double queryC2STDDEV() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.applicationRepository.queryC2STDDEV();
	}

	public List<String> queryC4() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.applicationRepository.queryC4();
	}

}

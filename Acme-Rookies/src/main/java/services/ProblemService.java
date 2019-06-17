
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ProblemRepository;
import security.LoginService;
import domain.Company;
import domain.Problem;

@Service
@Transactional
public class ProblemService {

	//Repository-----------------------------------------------------------------

	@Autowired
	private ProblemRepository	problemRepository;

	//Services-------------------------------------------------------------------

	@Autowired
	private CompanyService		companyService;

	@Autowired
	private ServiceUtils		serviceUtils;


	public Problem create() {
		final Problem problem = new Problem();
		final Company company = this.companyService.findCompanyByUserAcountId(LoginService.getPrincipal().getId());
		//this.serviceUtils.checkAuthority("COMPANY");
		problem.setCompany(company);
		problem.setFinalMode(false);
		return problem;
	}

	public Problem save(final Problem problem) {
		Assert.notNull(problem);

		this.serviceUtils.checkActor(problem.getCompany());
		this.serviceUtils.checkAuthority("COMPANY");
		this.serviceUtils.checkIdSave(problem);

		final Problem res = this.problemRepository.save(problem);
		return res;
	}

	public void delete(final Problem problem) {
		//	this.serviceUtils.checkActor(problem.getCompany());
		this.problemRepository.delete(problem);

	}

	public void delete1(final Problem problem) {
		this.serviceUtils.checkActor(problem.getCompany());
		this.problemRepository.delete(problem);

	}

	public Problem findOne(final int problemId) {
		return this.problemRepository.findOne(problemId);
	}

	public Collection<Problem> findAll() {
		return this.problemRepository.findAll();
	}

	public Collection<Problem> findProblemsByCompanyId(final int companyId) {
		return this.problemRepository.findProblemsByCompanyId(companyId);
	}

	public Collection<Problem> findProblemsByPositionId(final int positionId) {
		return this.problemRepository.findProblemsByPositionId(positionId);
	}

	public Problem findProblemByApplicationId(final int applicationId) {
		return this.problemRepository.findProblemsByApplicationId(applicationId);
	}

	public Collection<Problem> finfFinalProblems() {
		return this.problemRepository.findFinalProblem();
	}

	public boolean isFinalMode(final Problem problem) {
		return problem.isFinalMode();
	}
	public Boolean checkEquals(final Problem problem) {
		Boolean res = false;
		final Collection<Problem> todos = this.problemRepository.findAll();
		for (final Problem r : todos)
			if (r.getId() != problem.getId() && r.getTitle().equals(problem.getTitle()) && r.getStatement().equals(problem.getStatement())) {
				res = true;
				break;
			}
		return res;
	}

}

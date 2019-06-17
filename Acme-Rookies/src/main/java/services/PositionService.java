
package services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.PositionRepository;
import security.Authority;
import security.LoginService;
import domain.Application;
import domain.Audit;
import domain.Auditor;
import domain.Company;
import domain.Position;
import domain.Problem;

@Service
@Transactional
public class PositionService {

	//Repository-----------------------------------------------------------------

	@Autowired
	private PositionRepository	positionRepository;

	//Service-----------------------------------------------------------------

	@Autowired
	private CompanyService		companyService;

	@Autowired
	private ServiceUtils		serviceUtils;

	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private AuditService		auditService;

	@Autowired
	private AuditorService		auditorService;


	public Position create() {
		final Position Position = new Position();
		final Company company = this.companyService.findCompanyByUserAcountId(LoginService.getPrincipal().getId());
		System.out.println(company.getId());
		this.checkAuthority("COMPANY");
		Position.setCompany(company);
		Position.setFinalMode(false);
		Position.setTicker(this.createTicker());

		Position.setFinalMode(false);
		return Position;
	}

	public Position save(Position position) {
		Assert.notNull(position);
		final Company comp = position.getCompany();

		//		final Position positionDB = this.positionRepository.findOne(position.getId());
		//		Assert.isTrue(positionDB.getId() > 0);
		//		final Boolean bol = positionDB.isFinalMode();
		//
		//		//this.serviceUtils.checkIdSave(position);
		//		if (bol == true)
		//			throw new IllegalArgumentException("No se puede editar un puesto ya en modo final");
		//		if (this.compruebaMinimo(position) == false && bol == true && bol == false)
		//			throw new IllegalArgumentException("Para guardase en final mode tiene que tener al menos dos problemas asignados");
		//		if (this.compruebaMinimo(position) == true && bol == false)
		if (this.compruebaMinimo(position) == false && position.isFinalMode() == true)
			throw new IllegalAccessError("faltanProblemas");

		final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		final Date now = new Date();

		if (position.getDeadLine().before(now) && position.getDeadLine().before(now))
			throw new IllegalAccessError("fechaIncorrecta");

		final Collection<Application> resultado = this.applicationService.getAppsByPosition(position.getId());

		final Position positionDB = (Position) this.serviceUtils.checkObjectSave(position);
		this.serviceUtils.checkIdSave(position);

		position = this.positionRepository.save(position);

		if (!(resultado.isEmpty()) && position.isCancel() == true)
			for (final Application a : resultado) {
				final Application app = this.applicationService.findOne(a.getId());

				app.setStatus("REJECTED");
				this.applicationService.saveCompany(app);
			}

		return position;
	}
	public void delete(final Position position) {
		position.setFinalMode(false);
		Assert.notNull(position);
		this.serviceUtils.checkActor(position.getCompany());
		Assert.isTrue(position.isFinalMode() == false);
		Assert.isTrue(this.tieneApps(position) == false);

		//
		//		//this.serviceUtils.checkActor(Position.getCompany());

		this.positionRepository.delete(position);

	}

	public void delete1(final Position position) {
		Assert.notNull(position);
		this.serviceUtils.checkActor(position.getCompany());
		this.positionRepository.delete(position);

	}

	public Position findOne(final int PositionId) {
		return this.positionRepository.findOne(PositionId);
	}

	public Collection<Position> findAll() {
		return this.positionRepository.findAll();
	}

	public Collection<Position> findPositionsFinals() {
		return this.positionRepository.findPositionsFinals();
	}

	public Collection<Position> findPositionsByCompanyId(final int companyId) {
		return this.positionRepository.findPositionsByCompanyId(companyId);
	}

	public Collection<Position> findPositionsByCompanyIdFinals(final int companyId) {
		return this.positionRepository.findPositionsByCompanyIdFinals(companyId);
	}
	public Collection<Position> findPositionsByCompanyIdNotCanceled(final int companyId) {
		return this.positionRepository.findPositionsByCompanyIdNotCanceled(companyId);
	}

	public List<Position> findPositionByRookieId(final int rookieId) {
		return this.positionRepository.findPositionByRookieId(rookieId);
	}

	public Boolean tieneApps(final Position p) {
		Boolean res = false;
		final Collection<Application> apps = this.applicationService.getAppsByPosition(p.getId());
		if (!(apps.isEmpty()))
			res = true;
		return res;
	}

	public void checkAuthority(final String auth) {
		if (auth != null) {
			Boolean res = false;
			Assert.notNull(LoginService.getPrincipal());
			Assert.notNull(auth);
			for (final Authority a : LoginService.getPrincipal().getAuthorities())
				if (a.getAuthority().equals(auth)) {
					res = true;
					break;
				}
			Assert.isTrue(res);
		}
	}

	public String createTicker() {
		final Company company = this.companyService.findCompanyByUserAcountId(LoginService.getPrincipal().getId());
		final String name = company.getComercialName();
		final String letters = this.extractLETTERS2(name);
		String result = "";
		final String alphas = "1234567890";
		final Random rnd = new Random();
		int a, b, c, d;
		a = rnd.nextInt(alphas.length());
		b = rnd.nextInt(alphas.length());
		c = rnd.nextInt(alphas.length());
		d = rnd.nextInt(alphas.length());

		result += letters;
		result += "-" + alphas.charAt(a) + alphas.charAt(b) + alphas.charAt(c) + alphas.charAt(d);

		this.compruebaRepetidos(result);

		return result;
	}

	private void compruebaRepetidos(final String result) {
		final Collection<Position> positions = this.positionRepository.findAll();
		for (final Position p : positions)
			if (result.equals(p.getTicker()))
				this.createTicker();

	}

	private String extractLETTERS2(final String name) {
		String res = "";
		final String letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		final Random rnd = new Random();
		int a, b, c;
		final int d, e;
		a = rnd.nextInt(letras.length());
		b = rnd.nextInt(letras.length());
		c = rnd.nextInt(letras.length());

		int i = 0;
		for (i = 0; i < name.length(); i++)
			if (Character.isLetter(name.charAt(i)) == true)
				res = res + String.valueOf(name.charAt(i));
		;
		if (res.length() == 1 || res.length() == 2 || res.length() == 3)
			//res = res + rnd.nextInt(numeros.length() + rnd.nextInt(numeros.length() + rnd.nextInt(numeros.length())));
			res = res + letras.charAt(a) + letras.charAt(b) + letras.charAt(c) + letras.charAt(b) + letras.charAt(c);

		final String result = res.substring(0, 4);

		return result;
	}

	public Boolean compruebaMinimo(final Position position) {
		boolean res = false;
		final List<Problem> problems = (List<Problem>) this.positionRepository.findProblemsByPositionId(position.getId());
		if (problems.size() >= 2)
			res = true;
		return res;
	}

	public Collection<Problem> findProblemsByPositionId(final int positionId) {
		return this.positionRepository.findProblemsByPositionId(positionId);
	}

	public List<Position> finalPositions() {
		final List<Position> res = this.positionRepository.findFinalPosition();
		return res;
	}

	public Collection<Position> findFinalPositionsWithoutDeadline() {
		final Collection<Position> res = this.positionRepository.findFinalPositionWithoutDeadline();
		return res;

	}

	public Double queryC1AVG() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.positionRepository.queryC1AVG();
	}

	public Double queryC1MAX() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.positionRepository.queryC1MAX();
	}

	public Double queryC1MIN() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.positionRepository.queryC1MIN();
	}

	public Double queryC1STDDEV() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.positionRepository.queryC1STDDEV();
	}

	public List<String> queryC3() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.positionRepository.queryC3();
	}

	public Object[] queryC5() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.positionRepository.queryC5();
	}

	public List<String> queryC6Best() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.positionRepository.queryC6Best();
	}

	public List<String> queryC6Worst() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.positionRepository.queryC6Worst();
	}

	//Searching----------------------------------------------------------------------

	public List<Position> searchingPositions(String keyword) {
		final List<Position> res = new ArrayList<>();
		final List<Position> positions = this.finalPositions();
		keyword = keyword.toLowerCase();
		for (final Position position : positions)
			if (position.getTitle().toLowerCase().contains(keyword) || position.getDescription().toLowerCase().contains(keyword) || position.getProfile().toLowerCase().contains(keyword) || position.getSkills().toLowerCase().contains(keyword)
				|| position.getTechnologies().toLowerCase().contains(keyword) || position.getCompany().getName().toLowerCase().contains(keyword))
				res.add(position);
		return res;
	}

	public void flush() {
		this.positionRepository.flush();
	}

	//------------

	public Collection<Position> getPositionsWithoutAuditor() {
		final Collection<Position> all = this.positionRepository.findFinalPosition();
		final Collection<Audit> allAudits = this.auditService.findAll();
		final Collection<Position> res = new ArrayList<>();

		for (final Position p : all)
			for (final Audit a : allAudits) {
				int count = 0;
				if (a.getPosition().equals(p)) {
					count++;
					if (count == 0) {
						res.add(p);
						count = 0;
					}

				}
			}

		return res;

	}

	public Collection<Position> getPositionsToAudit() {
		final Collection<Position> all = this.positionRepository.findFinalPosition();
		final Collection<Audit> allAudits = this.auditService.findAll();
		final Auditor auditor = this.auditorService.findAuditorByUserAcountId(LoginService.getPrincipal().getId());

		for (final Audit a : allAudits)
			if (a.getAuditor().equals(auditor))
				all.remove(a.getPosition());

		return all;

	}

	public Position saveForAudits(Position position) {
		Assert.notNull(position);

		final Position positionDB = (Position) this.serviceUtils.checkObjectSave(position);
		this.serviceUtils.checkIdSave(position);

		position = this.positionRepository.save(position);

		return position;
	}
}

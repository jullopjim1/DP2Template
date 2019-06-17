
package services;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AuditRepository;
import security.LoginService;
import domain.Audit;
import domain.Auditor;
import domain.Position;

@Service
@Transactional
public class AuditService {

	//Repository---------------------------------------------------------------

	@Autowired
	private AuditRepository	auditRepository;

	//Services------------------------------------------------------------------
	@Autowired
	ServiceUtils			serviceUtils;

	@Autowired
	private CompanyService	companyService;

	@Autowired
	private AuditorService	auditorService;

	@Autowired
	private PositionService	positionService;


	//CRUD----------------------------------------------------------------------

	public Audit create(final int positionId) {
		final Audit audit = new Audit();
		final Auditor auditor = this.auditorService.findAuditorByUserAcountId(LoginService.getPrincipal().getId());
		this.serviceUtils.checkAuthority("AUDITOR");
		audit.setPosition(this.positionService.findOne(positionId));
		audit.setAuditor(auditor);
		audit.setFinalMode(false);
		audit.setMoment(new Date(System.currentTimeMillis() - 1000));

		return audit;
	}

	public Audit save(Audit audit) {
		Assert.notNull(audit);
		final Auditor comp = audit.getAuditor();
		final Position pos = audit.getPosition();

		//		if (audit.isFinalMode() == true)
		//			throw new IllegalAccessError("No se puede editar si no esta en borrador");

		final Audit auditDB = (Audit) this.serviceUtils.checkObjectSave(audit);
		this.serviceUtils.checkIdSave(audit);

		if (auditDB.isFinalMode() == false || audit.getId() == 0)
			audit = this.auditRepository.save(audit);
		return audit;
	}

	public void delete(final Audit audit) {
		audit.setFinalMode(false);

		Assert.notNull(audit);

		//final Position a = this.auditRepository.findPositionByAuditId(audit.getId());
		//a.setAudit(null);
		//this.positionService.saveForAudits(a);
		this.serviceUtils.checkActor(audit.getAuditor());
		Assert.isTrue(audit.isFinalMode() == false);

		this.auditRepository.delete(audit);

	}

	public void delete1(final Audit audit) {
		Assert.notNull(audit);
		this.auditRepository.delete(audit);

	}

	public Audit findOne(final int auditId) {
		return this.auditRepository.findOne(auditId);
	}

	public Collection<Audit> findAll() {
		return this.auditRepository.findAll();
	}

	//Others------------------------------------------------------------------------

	//DASHBOARD QUERIES--------------------------------------------------------------
	public Double queryRookiesC1AVG() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.auditRepository.queryRookiesC1AVG();
	}

	public Double queryRookiesC1MAX() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.auditRepository.queryRookiesC1MAX();
	}

	public Double queryRookiesC1MIN() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.auditRepository.queryRookiesC1MIN();
	}

	public Double queryRookiesC1STDDEV() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.auditRepository.queryRookiesC1STDDEV();
	}

	public Double queryRookiesC2AVG() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.auditRepository.queryRookiesC2AVG();
	}

	public Double queryRookiesC2MAX() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.auditRepository.queryRookiesC2MAX();
	}

	public Double queryRookiesC2MIN() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.auditRepository.queryRookiesC2MIN();
	}

	public Double queryRookiesC2STDDEV() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.auditRepository.queryRookiesC2STDDEV();
	}

	public List<String> queryRookiesC3() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.auditRepository.queryRookiesC3();
	}

	public Double queryRookiesC4() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.auditRepository.queryRookiesC4();
	}

	//metodos útiles

	public Audit findAuditById(final int auditId) {
		final Audit res = this.auditRepository.findAuditById(auditId);

		return res;

	}

	public List<Audit> findAuditsByPosition(final int positionId) {
		return this.auditRepository.findAuditsByPosition(positionId);
	}

	public List<Audit> findAuditsByAuditor(final Auditor auditor) {
		final List<Audit> res = this.auditRepository.findAuditsByAuditor(auditor);
		return res;

	}
	//	public Audit findAuditByPositionId(final int positionId) {
	//		final Audit res = this.auditRepository.findAuditByPositionId(positionId);
	//		return res;
	//	}

	public Position findPositionByAuditId(final int auditId) {
		final Position res = this.auditRepository.findPositionByAuditId(auditId);
		return res;
	}

	public Collection<Audit> findAuditsFinalByAuditorId(final int auditorId) {
		final Collection<Audit> res = this.auditRepository.findAuditsFinalByAuditorId(auditorId);
		return res;

	}
	public Collection<Audit> findAuditsByAuditorId(final int auditorId) {
		final Collection<Audit> res = this.auditRepository.findAuditsByAuditorId(auditorId);
		return res;

	}
	public boolean getIsAudit(final int positionId) {
		boolean res = false;
		final Auditor auditor = this.auditorService.findAuditorByUserAcountId(LoginService.getPrincipal().getId());

		final Collection<Audit> auditsByAuditor = this.findAuditsByAuditorId(auditor.getId());
		for (final Audit a : auditsByAuditor)
			if (a.getPosition().getId() == positionId) {
				res = true;
				break;
			}
		if (res == true)
			throw new IllegalAccessError("Ya ha sido creado por ti ese puesto");

		return res;
	}
}


package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Audit;
import domain.Auditor;
import domain.Position;

@Repository
public interface AuditRepository extends JpaRepository<Audit, Integer> {

	//Query Dashboard (ACME-ROOKIES)
	@Query("select avg(1.0 * (select avg(a.score) from Audit a where a.position.id = p.id)) from Position p")
	Double queryRookiesC1AVG();

	@Query("select max(1.0 * (select avg(a.score) from Audit a where a.position.id = p.id)) from Position p")
	Double queryRookiesC1MAX();

	@Query("select min(1.0 * (select avg(a.score) from Audit a where a.position.id = p.id)) from Position p")
	Double queryRookiesC1MIN();

	@Query("select stddev(1.0 * (select avg(a.score) from Audit a where a.position.id = p.id)) from Position p")
	Double queryRookiesC1STDDEV();

	@Query("select avg(1.0 * (select avg(a.score) from Audit a where a.position.company.id = c.id)) from Company c")
	Double queryRookiesC2AVG();

	@Query("select max(1.0 * (select avg(a.score) from Audit a where a.position.company.id = c.id)) from Company c")
	Double queryRookiesC2MAX();

	@Query("select min(1.0 * (select avg(a.score) from Audit a where a.position.company.id = c.id)) from Company c")
	Double queryRookiesC2MIN();

	@Query("select stddev(1.0 * (select avg(a.score) from Audit a where a.position.company.id = c.id)) from Company c")
	Double queryRookiesC2STDDEV();

	@Query("select a.position.company.comercialName from Audit a  group by a.position.company.id order by a.score desc")
	List<String> queryRookiesC3();

	@Query("select avg(p.salary) from Audit a join a.position p where a.score>(select avg(1.0*(select avg(b.score) from Audit b where b.position.id=p.id))from Position p)")
	Double queryRookiesC4();

	//----------- Queries de utilidad -------------------------------------

	@Query("select a from Audit a where a.id = ?1")
	Audit findAuditById(int auditId);

	@Query("select a from Audit a where a.position.id = ?1")
	List<Audit> findAuditsByPosition(int positionId);

	@Query("select a from Audit a where a.auditor = (select h.id from Auditor h where h = ?1)")
	List<Audit> findAuditsByAuditor(Auditor auditor);

	//	@Query("select p from Audit p where p.position.id = ?1")
	//	Audit findAuditByPositionId(int positionId);

	@Query("select p from Audit p where p.position.id = ?1")
	Position findPositionByAuditId(int auditId);

	@Query("select p from Audit p where p.auditor.id = ?1 and p.finalMode = true")
	Collection<Audit> findAuditsFinalByAuditorId(Integer auditorId);

	@Query("select p from Audit p where p.auditor.id = ?1")
	Collection<Audit> findAuditsByAuditorId(Integer auditorId);

}

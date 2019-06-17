
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Flugot;

@Repository
public interface FlugotRepository extends JpaRepository<Flugot, Integer> {

	@Query("select f from Flugot f where f.finalMode = true")
	List<Flugot> findFlugotFinals();

	@Query("select f from Flugot f where f.finalMode = true and f.audit.id = ?1")
	List<Flugot> findFlugotFinalsByAudit(Integer auditId);

	@Query("select f from Flugot f where f.auditor.id = ?1")
	List<Flugot> findFlugotByAuditor(Integer auditorId);
}

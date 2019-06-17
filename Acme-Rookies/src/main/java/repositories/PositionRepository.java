
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Position;
import domain.Problem;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {

	@Query("select p from Position p where p.company.id = ?1")
	Collection<Position> findPositionsByCompanyId(Integer companyId);

	@Query("select p from Position p where p.finalMode = true and p.cancel=false")
	Collection<Position> findPositionsFinals();

	@Query("select p from Position p where p.company.id = ?1 and p.finalMode = true and p.cancel=false")
	Collection<Position> findPositionsByCompanyIdFinals(Integer companyId);

	@Query("select p from Position p where p.company.id = ?1 and p.cancel=false")
	Collection<Position> findPositionsByCompanyIdNotCanceled(Integer companyId);

	@Query("select p from Problem p where p.position.id = ?1 and p.finalMode = true")
	Collection<Problem> findProblemsByPositionId(Integer positionId);

	@Query("select a from Position a where a.finalMode = true")
	List<Position> findFinalPosition();

	@Query("select distinct a.position from Application a where a.rookie.id = ?1")
	List<Position> findPositionByRookieId(final int rookieId);

	@Query("select a from Position a where a.deadLine > NOW() and a.finalMode = true")
	Collection<Position> findFinalPositionWithoutDeadline();
	//QUERY DASHBOARD---------------------------------------------------------------------------
	@Query("select avg(1.0 * (select count(p) from Position p where p.company.id = c.id)) from Company c")
	Double queryC1AVG();

	@Query("select max(1.0 * (select count(p) from Position p where p.company.id = c.id)) from Company c")
	Double queryC1MAX();

	@Query("select min(1.0 * (select count(p) from Position p where p.company.id = c.id)) from Company c")
	Double queryC1MIN();

	@Query("select stddev(1.0 * (select count(p) from Position p where p.company.id = c.id)) from Company c")
	Double queryC1STDDEV();

	@Query("select avg(p.salary), max(p.salary),min(p.salary), stddev(p.salary) from Position p")
	public Object[] queryC5();

	@Query("select p.title from Position p group by p.salary order by max(p.salary) desc")
	List<String> queryC6Best();

	@Query("select p.title from Position p group by p.salary order by max(p.salary) asc")
	List<String> queryC6Worst();

	@Query("select p.company.comercialName from Position p  group by p.company.id order by count(p) desc")
	List<String> queryC3();

}

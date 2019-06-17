
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Problem;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Integer> {

	@Query("select p from Problem p where p.company.id = ?1")
	Collection<Problem> findProblemsByCompanyId(int companyId);

	@Query("select p from Problem p where p.position.id = ?1")
	Collection<Problem> findProblemsByPositionId(int positionId);

	@Query("select p from Problem p where p.finalMode = true")
	Collection<Problem> findFinalProblem();

	@Query("select a.problem from Application a where a.id = ?1")
	Problem findProblemsByApplicationId(final int applicationId);
}

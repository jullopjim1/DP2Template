
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.EducationData;

@Repository
public interface EducationDataRepository extends JpaRepository<EducationData, Integer> {

	@Query("select e from EducationData e where e.curricula.id = ?1")
	Collection<EducationData> findEducationDatasByCurriculaId(int curriculaId);

}

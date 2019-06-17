
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.PositionData;

@Repository
public interface PositionDataRepository extends JpaRepository<PositionData, Integer> {

	@Query("select p from PositionData p where p.curricula.id = ?1")
	Collection<PositionData> findPositionDatasByCurriculaId(int curriculaId);

}


package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.MiscellaneousData;

@Repository
public interface MiscellaneousDataRepository extends JpaRepository<MiscellaneousData, Integer> {

	@Query("select m from MiscellaneousData m where m.curricula.id = ?1")
	Collection<MiscellaneousData> findMiscellaneousDatasByCurriculaId(int curriculaId);

}

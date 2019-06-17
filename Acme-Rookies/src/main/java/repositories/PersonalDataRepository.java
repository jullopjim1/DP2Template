
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.PersonalData;

@Repository
public interface PersonalDataRepository extends JpaRepository<PersonalData, Integer> {

	@Query("select p from PersonalData p where p.curricula.id = ?1")
	PersonalData findPersonalDataByCurriculaId(int curriculaId);

	@Query("select p from PersonalData p where p.curricula.rookie.id = ?1 and p.original = true")
	Collection<PersonalData> findAllPersonalDatasByRookieId(int rookieId);

	@Query("select p from PersonalData p where p.curricula.rookie.id = ?1 and p.original = false")
	Collection<PersonalData> findAllCopiedPersonalDatasByRookieId(int rookieId);

}

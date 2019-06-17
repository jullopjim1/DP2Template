
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {

	@Query("select a from Actor a where a.userAccount.id = ?1")
	Actor findByUserAccount(int userAccountId);

	@Query("select a from Actor a where a.userAccount.username=?1")
	Actor findByUsername(String username);

	@Query("select a from Actor a where a.spammer = true")
	Collection<Actor> findSpammerActors();

	@Query("select a from Actor a where a.banned = true or a.spammer = true")
	Collection<Actor> findSpammersAndBannedActors();

}

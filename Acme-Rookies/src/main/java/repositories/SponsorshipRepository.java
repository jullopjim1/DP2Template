
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Sponsorship;

@Repository
public interface SponsorshipRepository extends JpaRepository<Sponsorship, Integer> {

	//Query Dashboard (ACME-ROOKIES)
	@Query("select avg(1.0 * (select count(*) from Sponsorship s where s.provider = p.id)) from Provider p")
	Double queryRookiesA1AVG();

	@Query("select min(1.0 * (select count(*) from Sponsorship s where s.provider = p.id)) from Provider p")
	Double queryRookiesA1MIN();

	@Query("select max(1.0 * (select count(*) from Sponsorship s where s.provider = p.id)) from Provider p")
	Double queryRookiesA1MAX();

	@Query("select stddev(1.0 * (select count(*) from Sponsorship s where s.provider = p.id)) from Provider p")
	Double queryRookiesA1STDDEV();

	@Query("select avg(1.0 * (select count(*) from Sponsorship s where s.position = p.id and p.finalMode=true)) from Position p")
	Double queryRookiesA2AVG();

	@Query("select min(1.0 * (select count(*) from Sponsorship s where s.position = p.id and p.finalMode=true)) from Position p")
	Double queryRookiesA2MIN();

	@Query("select max(1.0 * (select count(*) from Sponsorship s where s.position = p.id and p.finalMode=true)) from Position p")
	Double queryRookiesA2MAX();

	@Query("select stddev(1.0 * (select count(*) from Sponsorship s where s.position = p.id and p.finalMode=true)) from Position p")
	Double queryRookiesA2STDDEV();

	@Query("select p.name from Sponsorship s join s.provider p group by  p having count(s)>1.1*(select avg(1.0*(select count(t) from Sponsorship t where t.provider.id=x.id))from Provider x)")
	Collection<domain.Provider> queryRookiesA3();

	//-----------------------------------------------------------------

	@Query("select s from Sponsorship s where s.id = ?1")
	Sponsorship findSponsorShipById(int sponsorShipId);

	@Query("select s from Sponsorship s where s.provider.id = ?1")
	Collection<Sponsorship> findSponsorshipsByProviderId(int providerId);

	@Query("select s from Sponsorship s where s.position.id = ?1")
	Collection<Sponsorship> findSponsorshipsByPositionId(int positionId);
}


package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

	//Query Dashboard (ACME-ROOKIES)
	@Query("select avg(1.0 * (select count(*) from Item i where i.provider = p.id)) from Provider p")
	Double queryRookiesB1AVG();

	@Query("select min(1.0 * (select count(*) from Item i where i.provider = p.id)) from Provider p")
	Double queryRookiesB1MIN();

	@Query("select max(1.0 * (select count(*) from Item i where i.provider = p.id)) from Provider p")
	Double queryRookiesB1MAX();

	@Query("select stddev(1.0 * (select count(*) from Item i where i.provider = p.id)) from Provider p")
	Double queryRookiesB1STDDEV();

	@Query("select i.provider.userAccount.username from Item i group by i.provider.id order by count(i) desc")
	public List<String> queryRookiesB2();

	//Others-------------------------------------------------------------------------------

	@Query("select i from Item i where i.provider.id = ?1")
	List<Item> findItemsByProviderId(int providerId);
}

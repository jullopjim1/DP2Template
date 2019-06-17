
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Finder;

@Repository
public interface FinderRepository extends JpaRepository<Finder, Integer> {

	@Query("select f from Finder f where f.rookie.id = ?1")
	Finder findFinderByRookieId(int rookieId);

	//QUERY DASHBOARD---------------------------------------------------------------------------
	@Query("select avg(f.positions.size) from Finder f")
	Double queryB2AVG();

	@Query("select max(f.positions.size)*1.0 from Finder f")
	Double queryB2MAX();

	@Query("select min(f.positions.size)*1.0 from Finder f")
	Double queryB2MIN();

	@Query("select sqrt(((select sum(f1.positions.size) from Finder f1) - (select avg(f2.positions.size) from Finder f2)) * ((select sum(f1.positions.size) from Finder f1) - (select avg(f2.positions.size) from Finder f2)) / count(f)) from Finder f")
	Double queryB2STDDEV();

	@Query("select (count(*)*1.0)/(select count(*) from Finder ff where ff.keyword != '' or ff.deadline!=null or ff.minSalary!=null or ff.maxDeadline!=null) from Finder f where f.keyword='' and f.deadline=null and f.minSalary=null and f.maxDeadline=null")
	Double queryB3();

}

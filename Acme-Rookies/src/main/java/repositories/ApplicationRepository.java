
package repositories;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import security.UserAccount;
import domain.Application;
import domain.Rookie;
import domain.Position;
import domain.Problem;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

	@Query("select a from Application a where a.publishMoment < ?1")
	Application findApplicationByPublishMoment(Date fecha);

	@Query("select a from Application a where a.submitMoment < ?1")
	Application findApplicationBySubmitMoment(Date fecha);

	@Query("select a from Application a where a.id = ?1")
	Application findApplicationById(int applicationId);

	@Query("select a from Application a where a.rookie = (select h.id from Rookie h where h = ?1)")
	List<Application> findApplicationByRookie(Rookie rookie);

	@Query("select a from Application a where a.status != 'PENDING' and a.position.company.id = ?1")
	Collection<Application> findApplicationByStatusAndCompany(int companyId);

	@Query("select a from Application a where a.position.company.id = ?1")
	Collection<Application> findApplicationByCompany(int companyId);

	@Query("select p from Application p where p.position.id = ?1")
	Collection<Application> findApplicationByPositionId(int positionId);

	@Query("select c.id from Company c where c.userAccount.id = ?1")
	int findCompanyIdByUserAccountId(int userAccountId);

	@Query("select h from Rookie h where h.userAccount = (select u.id from UserAccount u where u = ?1)")
	Rookie findRookieByUserAccount(UserAccount userAccount);

	@Query("select p from Problem p where p.finalMode = true and p.position = (select p.id from Position p where p = ?1) order by rand()")
	List<Problem> findRandomFinalProblem(Position position);

	//QUERY DASHBOARD---------------------------------------------------------------------------
	@Query("select avg(1.0 * (select count(a) from 	Application a where a.rookie.id = h.id)) from Rookie h")
	Double queryC2AVG();

	@Query("select max(1.0 * (select count(a) from 	Application a where a.rookie.id = h.id)) from Rookie h")
	Double queryC2MAX();

	@Query("select min(1.0 * (select count(a) from 	Application a where a.rookie.id = h.id)) from Rookie h")
	Double queryC2MIN();

	@Query("select stddev(1.0 * (select count(a) from 	Application a where a.rookie.id = h.id)) from Rookie h")
	Double queryC2STDDEV();

	@Query("select a.rookie.name from Application a  group by a.rookie.id order by count(a) desc")
	List<String> queryC4();

}

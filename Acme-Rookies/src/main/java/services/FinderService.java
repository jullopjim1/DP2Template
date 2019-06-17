
package services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import domain.Finder;
import domain.Rookie;
import domain.Position;
import repositories.FinderRepository;
import security.LoginService;

@Service
@Transactional
public class FinderService {

	//Repository---------------------------------------------------------------

	@Autowired
	private FinderRepository		finderRepository;

	//Services------------------------------------------------------------------

	@Autowired
	private RookieService			rookieService;

	@Autowired
	private PositionService			positionService;

	@Autowired
	private ConfigurationService	configurationService;


	//CRUD----------------------------------------------------------------------

	public Finder create() {
		final Finder finder = new Finder();
		final Rookie rookie = this.rookieService.findRookieByUserAcountId(LoginService.getPrincipal().getId());
		final List<Position> positions = new ArrayList<>();

		finder.setRookie(rookie);
		finder.setLastUpdate(new Date(System.currentTimeMillis() - 100));
		finder.setPositions(positions);

		return finder;
	}

	public Finder findOneByPrincipal() {
		final Rookie rookie = this.rookieService.findRookieByUserAcountId(LoginService.getPrincipal().getId());
		return this.finderRepository.findFinderByRookieId(rookie.getId());
	}

	public Finder findOne(final Integer id) {
		return this.finderRepository.findOne(id);
	}

	public Collection<Finder> findAll() {
		return this.finderRepository.findAll();
	}

	public Finder save(final Finder finder) {
		Assert.notNull(finder);
		this.checkPrincipal(finder);

		final List<Position> positions = this.updateCache(finder);
		finder.setPositions(positions);

		final Finder saved = this.finderRepository.save(finder);

		return saved;
	}

	public void delete(final Finder finder) {
		this.checkPrincipal(finder);
		this.finderRepository.delete(finder);
	}

	//Others------------------------------------------------------------------------

	private boolean checkPrincipal(final Finder finder) {
		final Rookie rookie = finder.getRookie();
		final Rookie principal = this.rookieService.findRookieByUserAcountId(LoginService.getPrincipal().getId());

		Assert.isTrue(rookie.getId() == principal.getId());

		return true;
	}

	public List<Position> findPositionsByFinder(final Finder f) {
		final Finder finder = new Finder();

		finder.setKeyword(f.getKeyword());
		finder.setDeadline(f.getDeadline());
		finder.setMinSalary(f.getMinSalary());
		finder.setMaxDeadline(f.getMaxDeadline());

		List<Position> res = new ArrayList<>();

		if (finder.getKeyword() != null && finder.getDeadline() == null && finder.getMaxDeadline() == null && finder.getMinSalary() == null)
			res = this.positionService.searchingPositions(finder.getKeyword());
		else {

			final List<Position> positions = this.positionService.searchingPositions(finder.getKeyword());

			for (int i = 0; i < positions.size(); i++) {
				final SimpleDateFormat sde = new SimpleDateFormat("yyyy/MM/dd");

				if (f.getDeadline() != null && sde.format(positions.get(i).getDeadLine()).equals(sde.format(finder.getDeadline())) && !res.contains(positions.get(i)))
					res.add(positions.get(i));

				else if (f.getMaxDeadline() != null && positions.get(i).getDeadLine().before(finder.getMaxDeadline()) && !res.contains(positions.get(i)))
					res.add(positions.get(i));

				else if (finder.getMinSalary() != null && positions.get(i).getSalary() >= finder.getMinSalary() && !res.contains(positions.get(i)))
					res.add(positions.get(i));

				else {
					positions.remove(positions.get(i));
					res = positions;
				}
			}
		}

		return res;

	}

	public List<Position> updateCache(final Finder finder) {
		final Integer timeCache = this.configurationService.findOne().getCacheFinder();
		final Date dnow = new Date();

		List<Position> positions = new ArrayList<>();

		if (finder == null)
			return positions;

		if (finder != null && (finder.getLastUpdate() == null || dnow.getTime() - finder.getLastUpdate().getTime() > (timeCache * 3600000))) {
			positions = this.findPositionsByFinder(finder);
			finder.setLastUpdate(new Date(System.currentTimeMillis() - 1000));
		} else
			positions = finder.getPositions();

		return positions;

	}

	//DASHBOARD QUERIES--------------------------------------------------------------
	public Double queryB2AVG() {
		return this.finderRepository.queryB2AVG();
	}

	public Double queryB2MAX() {
		return this.finderRepository.queryB2MAX();
	}

	public Double queryB2MIN() {
		return this.finderRepository.queryB2MIN();
	}

	public Double queryB2STDDEV() {
		return this.finderRepository.queryB2STDDEV();
	}

	public Double queryB3() {
		return this.finderRepository.queryB3();
	}

}

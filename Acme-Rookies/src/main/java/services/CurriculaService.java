
package services;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CurriculaRepository;
import domain.Curricula;
import domain.EducationData;
import domain.Rookie;
import domain.MiscellaneousData;
import domain.PersonalData;
import domain.PositionData;

@Service
@Transactional
public class CurriculaService {

	// Managed repository and services----------------------------------------------------------------
	@Autowired
	private CurriculaRepository			curriculaRepository;

	//	@Autowired
	//	private RookieService		rookieService;

	@Autowired
	private PersonalDataService			personalDataService;

	@Autowired
	private PositionDataService			positionDataService;

	@Autowired
	private EducationDataService		educationDataService;

	@Autowired
	private MiscellaneousDataService	miscellaneousDataService;
	@Autowired
	private ServiceUtils				serviceUtils;


	//Services-----------------------------------------------------------------------------

	public Curricula createAndSave(final Rookie rookie) {
		Assert.notNull(rookie);
		final Curricula curricula = new Curricula();

		curricula.setRookie(rookie);
		curricula.setOriginal(true);

		this.curriculaRepository.saveAndFlush(curricula);
		this.curriculaRepository.flush();

		return curricula;
	}

	public Curricula makeACopy(final int originalCurriculaId) {
		final Curricula originalCurricula = this.curriculaRepository.findOne(originalCurriculaId);
		final Curricula newCurricula = new Curricula();

		newCurricula.setOriginal(false);
		newCurricula.setRookie(originalCurricula.getRookie());

		final Curricula nCurr = this.curriculaRepository.save(newCurricula);

		final PersonalData personalData = this.personalDataService.getPersonalDataByCurriculaId(originalCurricula.getId());

		this.personalDataService.makeACopy(personalData.getId(), nCurr.getId());
		this.positionDataService.makeACopy(originalCurriculaId, nCurr.getId());
		this.educationDataService.makeACopy(originalCurriculaId, nCurr.getId());
		this.miscellaneousDataService.makeACopy(originalCurriculaId, nCurr.getId());
		return newCurricula;

	}
	public void flush() {
		this.curriculaRepository.flush();
	}

	public Curricula findOne(final int CurriculaId) {
		return this.curriculaRepository.findOne(CurriculaId);
	}

	public Collection<Curricula> findCurriculasByRookieId(final int rookieId) {
		return this.curriculaRepository.findCurriculasByRookieId(rookieId);
	}

	public Collection<Curricula> findSimplyCurriculasByRookieId(final int rookieId) {
		return this.curriculaRepository.findSimplyCurriculasByRookieId(rookieId);
	}

	public PersonalData findPersonalFromCurricula(final int curriculaId) {
		return this.curriculaRepository.findPersonalFromCurricula(curriculaId);
	}

	public List<Curricula> findAll() {
		return this.curriculaRepository.findAll();
	}

	public Curricula save(final Curricula curricula) {
		Assert.notNull(curricula);
		//	final Curricula curriculaDB = (Curricula) this.serviceUtils.checkObjectSave(curricula);

		//compruebo que el rookie que esta intentando editar sea el el dueño del historial al que pertenece dicho Record
		this.serviceUtils.checkActor(curricula.getRookie());
		this.serviceUtils.checkAuthority("ROOKIE");

		//comprobamos que el id del objeto no sea nulo o negativo por seguridad
		this.serviceUtils.checkIdSave(curricula);

		final Curricula res = this.curriculaRepository.save(curricula);
		return res;

	}
	public void delete(final int curriculaId) {
		final Curricula curricula = this.curriculaRepository.findOne(curriculaId);
		this.serviceUtils.checkAuthority("ROOKIE");
		this.serviceUtils.checkActor(curricula.getRookie());
		Assert.isTrue(curricula.getOriginal() == true);

		final Collection<PositionData> positionDatas = this.positionDataService.findPositionDatasByCurriculaId(curriculaId);
		final Collection<EducationData> educationDatas = this.educationDataService.findEducationDatasByCurriculaId(curriculaId);
		final Collection<MiscellaneousData> miscellaneousDatas = this.miscellaneousDataService.findMiscellaneousDatasByCurriculaId(curriculaId);

		this.personalDataService.delete(this.personalDataService.getPersonalDataByCurriculaId(curriculaId));
		for (final PositionData d : positionDatas)
			this.positionDataService.delete(d);
		for (final EducationData e : educationDatas)
			this.educationDataService.delete(e);
		for (final MiscellaneousData m : miscellaneousDatas)
			this.miscellaneousDataService.delete(m);

		this.curriculaRepository.delete(curricula);
	}

	public void deleteCopy(final int curriculaId) {
		final Curricula curricula = this.curriculaRepository.findOne(curriculaId);

		this.serviceUtils.checkAuthority("ROOKIE");
		this.serviceUtils.checkActor(curricula.getRookie());
		//Assert.isTrue(curricula.getOriginal() == false);

		final Collection<PositionData> positionDatas = this.positionDataService.findPositionDatasByCurriculaId(curriculaId);
		final Collection<EducationData> educationDatas = this.educationDataService.findEducationDatasByCurriculaId(curriculaId);
		final Collection<MiscellaneousData> miscellaneousDatas = this.miscellaneousDataService.findMiscellaneousDatasByCurriculaId(curriculaId);

		this.personalDataService.delete(this.personalDataService.getPersonalDataByCurriculaId(curriculaId));
		for (final PositionData d : positionDatas)
			this.positionDataService.delete(d);
		for (final EducationData e : educationDatas)
			this.educationDataService.delete(e);
		for (final MiscellaneousData m : miscellaneousDatas)
			this.miscellaneousDataService.delete(m);

		this.curriculaRepository.delete(curricula);
	}

	public Double queryB1AVG() {
		return this.curriculaRepository.queryB1AVG();
	}

	public Double queryB1MAX() {
		return this.curriculaRepository.queryB1MAX();
	}

	public Double queryB1MIN() {
		return this.curriculaRepository.queryB1MIN();
	}

	public Double queryB1STDDEV() {
		return this.curriculaRepository.queryB1STDDEV();
	}

	public Collection<Curricula> findCopiedCurriculasByRookieId(final int rookieId) {
		return this.curriculaRepository.findCopiedCurriculasByRookieId(rookieId);
	}

	public Curricula findCurriculasByApplicationId(final int applicationId) {
		return this.curriculaRepository.findCurriculasByApplicationId(applicationId);
	}

}

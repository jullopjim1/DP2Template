
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.PositionDataRepository;
import domain.Curricula;
import domain.PositionData;

@Service
@Transactional
public class PositionDataService {

	//--------------------------Managed Repository----------------------------------------

	@Autowired
	private PositionDataRepository	positionDataRepository;

	// ----------------------Supporting Service------------------------------------

	@Autowired
	private ServiceUtils			serviceUtils;

	@Autowired
	private CurriculaService		curriculaService;


	public PositionDataService() {
		super();
	}
	// Simple CRUD methods

	public PositionData create(final int curriculaId) {
		final Curricula curricula = this.curriculaService.findOne(curriculaId);
		this.serviceUtils.checkObject(curricula);
		this.serviceUtils.checkActor(curricula.getRookie());
		Assert.notNull(curricula);
		final PositionData positionData = new PositionData();

		positionData.setCurricula(curricula);
		positionData.setOriginal(true);

		return positionData;
	}

	public Collection<PositionData> makeACopy(final int originalCurriculaId, final int newCurriculaId) {
		final Collection<PositionData> originalPositionDatas = this.positionDataRepository.findPositionDatasByCurriculaId(originalCurriculaId);
		final Collection<PositionData> newPositionDatas = new ArrayList<>();
		final Curricula curricula = this.curriculaService.findOne(newCurriculaId);

		for (final PositionData p : originalPositionDatas) {
			final PositionData n = new PositionData();
			n.setOriginal(false);
			n.setCurricula(curricula);
			n.setTitle(p.getTitle());
			n.setDescription(p.getDescription());
			n.setStartDate(p.getStartDate());
			n.setEndDate(p.getEndDate());

			final PositionData nS = this.positionDataRepository.save(n);
			newPositionDatas.add(nS);
		}
		return newPositionDatas;
	}
	public Collection<PositionData> findAll() {
		Collection<PositionData> res;
		res = this.positionDataRepository.findAll();
		return res;
	}

	public PositionData findOne(final int PositionDataId) {
		PositionData res;
		res = this.positionDataRepository.findOne(PositionDataId);
		return res;

	}

	public PositionData save(final PositionData positionData) {
		Assert.notNull(positionData);
		this.serviceUtils.checkObjectSave(positionData);

		//compruebo que el rookie que esta intentando editar sea el el dueño del historial al que pertenece dicho Record
		this.serviceUtils.checkActor(positionData.getCurricula().getRookie());
		this.serviceUtils.checkAuthority("ROOKIE");

		//comprobamos que el id del objeto no sea nulo o negativo por seguridad
		this.serviceUtils.checkIdSave(positionData);

		final PositionData res = this.positionDataRepository.save(positionData);
		return res;

	}

	public void delete(final PositionData p) {
		Assert.notNull(p);
		Assert.isTrue(p.getOriginal() == true);
		final PositionData positionData = (PositionData) this.serviceUtils.checkObject(p);
		this.serviceUtils.checkActor(positionData.getCurricula().getRookie());
		this.positionDataRepository.delete(p);
	}

	public Collection<PositionData> findPositionDatasByCurriculaId(final int curriculaId) {
		return this.positionDataRepository.findPositionDatasByCurriculaId(curriculaId);
	}

	public void flush() {
		this.positionDataRepository.flush();
	}

}


package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.EducationDataRepository;
import domain.Curricula;
import domain.EducationData;

@Service
@Transactional
public class EducationDataService {

	//--------------------------Managed Repository----------------------------------------

	@Autowired
	private EducationDataRepository	educationDataRepository;

	// ----------------------Supporting Service------------------------------------

	@Autowired
	private ServiceUtils			serviceUtils;

	@Autowired
	private CurriculaService		curriculaService;


	public EducationDataService() {
		super();
	}
	// Simple CRUD methods

	public EducationData create(final int curriculaId) {
		final Curricula curricula = this.curriculaService.findOne(curriculaId);
		this.serviceUtils.checkObject(curricula);
		this.serviceUtils.checkActor(curricula.getRookie());
		Assert.notNull(curricula);
		final EducationData educationData = new EducationData();

		educationData.setCurricula(curricula);
		educationData.setOriginal(true);
		educationData.setEndDate(null);

		return educationData;
	}

	public Collection<EducationData> makeACopy(final int originalCurriculaId, final int newCurriculaId) {
		final Collection<EducationData> originalEducationDatas = this.educationDataRepository.findEducationDatasByCurriculaId(originalCurriculaId);
		final Collection<EducationData> newEducationDatas = new ArrayList<>();
		final Curricula curricula = this.curriculaService.findOne(newCurriculaId);

		for (final EducationData p : originalEducationDatas) {
			final EducationData n = new EducationData();
			n.setOriginal(false);
			n.setCurricula(curricula);
			n.setDegree(p.getDegree());
			n.setInstitution(p.getInstitution());
			n.setMark(p.getMark());
			n.setStartDate(p.getStartDate());
			n.setEndDate(p.getEndDate());

			final EducationData nS = this.educationDataRepository.save(n);
			newEducationDatas.add(nS);
		}
		return newEducationDatas;
	}
	public Collection<EducationData> findAll() {
		Collection<EducationData> res;
		res = this.educationDataRepository.findAll();
		return res;
	}

	public EducationData findOne(final int EducationDataId) {
		EducationData res;
		res = this.educationDataRepository.findOne(EducationDataId);
		return res;

	}

	public EducationData save(final EducationData educationData) {
		Assert.notNull(educationData);
		this.serviceUtils.checkObjectSave(educationData);

		//compruebo que el rookie que esta intentando editar sea el el dueño del historial al que pertenece dicho Record
		this.serviceUtils.checkActor(educationData.getCurricula().getRookie());
		this.serviceUtils.checkAuthority("ROOKIE");

		//comprobamos que el id del objeto no sea nulo o negativo por seguridad
		this.serviceUtils.checkIdSave(educationData);

		final EducationData res = this.educationDataRepository.save(educationData);
		return res;

	}

	public void delete(final EducationData p) {
		Assert.notNull(p);
		Assert.isTrue(p.getOriginal() == true);
		final EducationData educationData = (EducationData) this.serviceUtils.checkObject(p);
		this.serviceUtils.checkActor(educationData.getCurricula().getRookie());
		this.educationDataRepository.delete(p);
	}

	public Collection<EducationData> findEducationDatasByCurriculaId(final int curriculaId) {
		return this.educationDataRepository.findEducationDatasByCurriculaId(curriculaId);
	}

	public void flush() {
		this.educationDataRepository.flush();
	}

}


package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.PersonalDataRepository;
import domain.Curricula;
import domain.PersonalData;

@Service
@Transactional
public class PersonalDataService {

	//--------------------------Managed Repository----------------------------------------

	@Autowired
	private PersonalDataRepository	personalDataRepository;

	// ----------------------Supporting Service------------------------------------

	@Autowired
	private ServiceUtils			serviceUtils;
	@Autowired
	private CurriculaService		curriculaService;


	public PersonalDataService() {
		super();
	}
	// Simple CRUD methods

	public PersonalData create(final int curriculaId) {
		final Curricula curricula = this.curriculaService.findOne(curriculaId);
		this.serviceUtils.checkObject(curricula);
		this.serviceUtils.checkActor(curricula.getRookie());
		Assert.notNull(curricula);
		final PersonalData personalData = new PersonalData();

		personalData.setCurricula(curricula);
		personalData.setOriginal(true);

		return personalData;
	}

	public PersonalData makeACopy(final int originalPersonalDataId, final int newCurriculaId) {
		final PersonalData originalPersonalData = this.personalDataRepository.findOne(originalPersonalDataId);
		Assert.isTrue(originalPersonalData.getOriginal() == true);
		final Curricula newCurricula = this.curriculaService.findOne(newCurriculaId);
		Assert.isTrue(newCurricula.getOriginal() == false);
		final PersonalData newPersonalData = new PersonalData();

		newPersonalData.setCurricula(newCurricula);
		newPersonalData.setOriginal(false);
		newPersonalData.setFullName(originalPersonalData.getFullName());
		newPersonalData.setStatement(originalPersonalData.getStatement());
		newPersonalData.setPhoneNumber(originalPersonalData.getPhoneNumber());
		newPersonalData.setGitHubProfile(originalPersonalData.getGitHubProfile());
		newPersonalData.setLinkedInProfile(originalPersonalData.getLinkedInProfile());

		this.personalDataRepository.save(newPersonalData);

		return newPersonalData;

	}
	public Collection<PersonalData> findAll() {
		Collection<PersonalData> res;
		res = this.personalDataRepository.findAll();
		return res;
	}

	public PersonalData findOne(final int PersonalDataId) {
		PersonalData res;
		res = this.personalDataRepository.findOne(PersonalDataId);
		return res;

	}

	public PersonalData save(final PersonalData personalData) {
		Assert.notNull(personalData);
		this.serviceUtils.checkObjectSave(personalData);

		//compruebo que el rookie que esta intentando editar sea el el dueño del historial al que pertenece dicho Record
		this.serviceUtils.checkActor(personalData.getCurricula().getRookie());
		this.serviceUtils.checkAuthority("ROOKIE");

		//comprobamos que el id del objeto no sea nulo o negativo por seguridad
		this.serviceUtils.checkIdSave(personalData);

		final PersonalData res = this.personalDataRepository.save(personalData);
		return res;

	}

	public void delete(final PersonalData p) {
		Assert.notNull(p);
		Assert.isTrue(p.getOriginal() == true);
		final PersonalData personalData = (PersonalData) this.serviceUtils.checkObject(p);
		this.serviceUtils.checkActor(personalData.getCurricula().getRookie());
		this.personalDataRepository.delete(p);
	}

	public PersonalData getPersonalDataByCurriculaId(final int curriculaId) {
		final PersonalData res = this.personalDataRepository.findPersonalDataByCurriculaId(curriculaId);

		return res;
	}
	public Collection<PersonalData> findAllPersonalDatasByRookieId(final int rookieId) {
		return this.personalDataRepository.findAllPersonalDatasByRookieId(rookieId);
	}

	public void flush() {
		this.personalDataRepository.flush();
	}

}

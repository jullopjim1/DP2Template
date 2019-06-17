
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.MiscellaneousDataRepository;
import domain.Curricula;
import domain.MiscellaneousData;

@Service
@Transactional
public class MiscellaneousDataService {

	//--------------------------Managed Repository----------------------------------------

	@Autowired
	private MiscellaneousDataRepository	miscellaneousDataRepository;

	// ----------------------Supporting Service------------------------------------

	@Autowired
	private ServiceUtils				serviceUtils;

	@Autowired
	private CurriculaService			curriculaService;


	public MiscellaneousDataService() {
		super();
	}
	// Simple CRUD methods

	public MiscellaneousData create(final int curriculaId) {
		final Curricula curricula = this.curriculaService.findOne(curriculaId);
		this.serviceUtils.checkObject(curricula);
		this.serviceUtils.checkActor(curricula.getRookie());
		Assert.notNull(curricula);
		final MiscellaneousData miscellaneousData = new MiscellaneousData();

		miscellaneousData.setCurricula(curricula);
		miscellaneousData.setOriginal(true);

		return miscellaneousData;
	}

	public Collection<MiscellaneousData> makeACopy(final int originalCurriculaId, final int newCurriculaId) {
		final Collection<MiscellaneousData> originalMiscellaneousDatas = this.miscellaneousDataRepository.findMiscellaneousDatasByCurriculaId(originalCurriculaId);
		final Collection<MiscellaneousData> newMiscellaneousDatas = new ArrayList<>();
		final Curricula curricula = this.curriculaService.findOne(newCurriculaId);

		for (final MiscellaneousData p : originalMiscellaneousDatas) {
			final MiscellaneousData n = new MiscellaneousData();
			n.setOriginal(false);
			n.setCurricula(curricula);
			n.setText(p.getText());
			final List<String> atts = new ArrayList<>(p.getAttachments());
			n.setAttachments(atts);

			final MiscellaneousData nS = this.miscellaneousDataRepository.save(n);
			newMiscellaneousDatas.add(nS);
		}
		return newMiscellaneousDatas;
	}

	public Collection<MiscellaneousData> findAll() {
		Collection<MiscellaneousData> res;
		res = this.miscellaneousDataRepository.findAll();
		return res;
	}

	public MiscellaneousData findOne(final int MiscellaneousDataId) {
		MiscellaneousData res;
		res = this.miscellaneousDataRepository.findOne(MiscellaneousDataId);
		return res;

	}

	public MiscellaneousData save(final MiscellaneousData miscellaneousData) {
		Assert.notNull(miscellaneousData);
		this.serviceUtils.checkObjectSave(miscellaneousData);

		//compruebo que el rookie que esta intentando editar sea el el dueño del historial al que pertenece dicho Record
		this.serviceUtils.checkActor(miscellaneousData.getCurricula().getRookie());
		this.serviceUtils.checkAuthority("ROOKIE");

		//comprobamos que el id del objeto no sea nulo o negativo por seguridad
		this.serviceUtils.checkIdSave(miscellaneousData);

		final MiscellaneousData res = this.miscellaneousDataRepository.save(miscellaneousData);
		return res;

	}

	public void delete(final MiscellaneousData p) {
		Assert.notNull(p);
		Assert.isTrue(p.getOriginal() == true);
		final MiscellaneousData miscellaneousData = (MiscellaneousData) this.serviceUtils.checkObject(p);
		this.serviceUtils.checkActor(miscellaneousData.getCurricula().getRookie());
		this.miscellaneousDataRepository.delete(p);
	}

	public Collection<MiscellaneousData> findMiscellaneousDatasByCurriculaId(final int curriculaId) {
		return this.miscellaneousDataRepository.findMiscellaneousDatasByCurriculaId(curriculaId);
	}

	public void flush() {
		this.miscellaneousDataRepository.flush();
	}

}

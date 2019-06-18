
package services;

import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FlugotRepository;
import security.LoginService;
import domain.Flugot;

@Service
@Transactional
public class FlugotService {

	//Repository-------------------------------------------------

	@Autowired
	private FlugotRepository	flugotRepository;

	//Services---------------------------------------------------

	@Autowired
	private AuditorService		auditorService;

	@Autowired
	private ServiceUtils		serviceUtils;

	@Autowired(required = false)
	private Validator			validator;


	//CRUD--------------------------------------------------------

	public Flugot create() {
		final Flugot flugot = new Flugot();

		flugot.setAuditor(this.auditorService.findAuditorByUserAcountId(LoginService.getPrincipal().getId()));
		flugot.setFinalMode(false);

		return flugot;
	}

	public Flugot findOne(final Integer flugotId) {
		return this.flugotRepository.findOne(flugotId);
	}

	public List<Flugot> findAll() {
		return this.flugotRepository.findAll();
	}

	public Flugot save(final Flugot flugot) {
		Assert.notNull(flugot);
		this.serviceUtils.checkActor(flugot.getAuditor());
		this.serviceUtils.checkAuthority("AUDITOR");
		this.serviceUtils.checkIdSave(flugot);

		if (flugot.isFinalMode() == true) {
			flugot.setPublicationDate(new Date(System.currentTimeMillis() - 1000));
			flugot.setTicker(this.isUniqueTicker(flugot));
		}

		final Flugot saved = this.flugotRepository.save(flugot);
		return saved;
	}

	public void delete(final Flugot flugot) {
		Assert.notNull(flugot);
		this.serviceUtils.checkActor(flugot.getAuditor());
		this.serviceUtils.checkAuthority("AUDITOR");
		this.serviceUtils.checkIdSave(flugot);

		this.flugotRepository.delete(flugot);
	}

	//Others------------------------------------------------------

	@SuppressWarnings("deprecation")
	public String generateTicker(final Flugot flugot) {
		final Date date = flugot.getPublicationDate();
		final Integer s1 = date.getDate();
		String day = s1.toString();
		if (day.length() == 1)
			day = "0" + day;
		final Integer s2 = date.getMonth() + 1;
		String month = s2.toString();
		if (month.length() == 1)
			month = "0" + month;
		final Integer s3 = date.getYear();
		final String year = s3.toString().substring(1);

		return year + month + day + "-" + this.generateStringAux();
	}

	private String generateStringAux() {
		final int length = 5;
		final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final Random rng = new Random();
		final char[] text = new char[length];
		for (int i = 0; i < 5; i++)
			text[i] = characters.charAt(rng.nextInt(characters.length()));
		return new String(text);
	}

	public String isUniqueTicker(final Flugot flugot) {
		String result = this.generateTicker(flugot);

		for (final Flugot flugot1 : this.findAll())
			if (flugot1.getTicker().equals(result))
				result = this.isUniqueTicker(flugot);

		return result;
	}

	public List<Flugot> findFlugotFinals() {
		return this.flugotRepository.findFlugotFinals();
	}

	public List<Flugot> findFlugotFinalsByAudit(final Integer auditId) {
		return this.flugotRepository.findFlugotFinalsByAudit(auditId);
	}

	public List<Flugot> findFlugotByAudit(final Integer auditId) {
		return this.flugotRepository.findFlugotByAudit(auditId);
	}

	public List<Flugot> findFlugotByAuditor(final Integer auditorId) {
		return this.flugotRepository.findFlugotByAuditor(auditorId);
	}

	public int diferenciaMeses(final int flugotId) {
		int meses = 0;
		Flugot flugot = null;
		if (flugotId == 0)
			meses = -1;
		else {
			flugot = this.findOne(flugotId);
			if (flugot.getPublicationDate() == null)
				meses = -1;
			else {

				final Date now = new Date();
				final Date fechaFlugot = flugot.getPublicationDate();

				final int start = (now.getYear() + 1900) * 12 + now.getMonth();
				final int end = (fechaFlugot.getYear() + 1900) * 12 + fechaFlugot.getMonth();
				System.out.println("start: " + start);
				System.out.println("end: " + end);
				meses = start - end;
			}
		}

		System.out.println("numero de meses: " + meses);
		return meses;
	}

	public Flugot reconstruct(final Flugot flugot, final BindingResult bindingResult) {
		Flugot result = this.create();

		if (flugot.getId() == 0) {

			result.setBody(flugot.getBody());
			result.setPicture(flugot.getPicture());
			result.setFinalMode(flugot.isFinalMode());
			result.setAudit(flugot.getAudit());

		}

		else {
			result = this.findOne(flugot.getId());

			result.setBody(flugot.getBody());
			result.setPicture(flugot.getPicture());
			result.setFinalMode(flugot.isFinalMode());
			result.setAudit(flugot.getAudit());

		}

		//this.validator.validate(flugot, bindingResult);

		return result;
	}

}

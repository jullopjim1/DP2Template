
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import repositories.AuditorRepository;
import security.Authority;
import security.UserAccount;
import security.UserAccountRepository;
import domain.Actor;
import domain.Audit;
import domain.Auditor;
import domain.Configuration;
import domain.CreditCard;
import domain.SocialProfile;
import forms.ActorForm;

@Service
@Transactional
public class AuditorService {

	//Repository-----------------------------------------------------------------

	@Autowired
	private AuditorRepository		auditorRepository;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private CreditCardService		creditCardService;

	@Autowired
	private AuditService			auditService;

	@Autowired
	private SocialProfileService	socialProfileService;

	@Autowired
	private ServiceUtils			serviceUtils;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private UserAccountRepository	userAccountRepository;

	@Autowired
	private MessageSource			messageSource;

	@Autowired
	private ConfigurationService	configurationService;


	// CRUD

	public Collection<Auditor> findAll(final Collection<Integer> ids) {
		this.serviceUtils.checkIds(ids);
		return this.auditorRepository.findAll(ids);
	}

	public List<Auditor> findAll() {
		return this.auditorRepository.findAll();
	}

	public Auditor create() {
		this.serviceUtils.checkAuthority(Authority.ADMIN);
		final Auditor res = new Auditor();
		res.setBanned(false);
		res.setUserAccount(new UserAccount());
		final Collection<Authority> authorities = new ArrayList<Authority>();
		final Authority authority = new Authority();
		authority.setAuthority(Authority.AUDITOR);
		authorities.add(authority);
		res.getUserAccount().setAuthorities(authorities);
		return res;
	}

	public Auditor save(final Auditor c) {
		final Auditor auditor = (Auditor) this.serviceUtils.checkObjectSave(c);

		if ((!auditor.getPhone().startsWith("+")) && StringUtils.isNumeric(auditor.getPhone()) && auditor.getPhone().length() > 3) {
			final Configuration configuration = this.configurationService.findOne();
			auditor.setPhone(configuration.getCountryCode() + auditor.getPhone());
		}

		if (c.getId() == 0) {
			this.serviceUtils.checkAuthority(Authority.ADMIN);
			c.setBanned(false);
			c.setSpammer(null);

		} else {
			this.serviceUtils.checkAnyAuthority(new String[] {
				Authority.ADMIN, Authority.AUDITOR
			});
			if (this.actorService.findPrincipal() instanceof Auditor) {
				this.serviceUtils.checkActor(auditor);
				c.setBanned(auditor.getBanned());
				c.setSpammer(auditor.getSpammer());
			} else {
				c.setEmail(auditor.getEmail());
				c.setName(auditor.getName());
				c.setPhone(auditor.getPhone());
				c.setPhoto(auditor.getPhoto());
				c.setSurname(auditor.getSurname());
				c.setUserAccount(auditor.getUserAccount());
				c.setAddress(auditor.getAddress());
			}
		}
		final UserAccount userAccount = this.userAccountRepository.save(c.getUserAccount());
		auditor.setUserAccount(userAccount);
		c.setCreditCard(auditor.getCreditCard());
		final CreditCard creditCard = this.creditCardService.save(c.getCreditCard());
		auditor.setCreditCard(creditCard);
		final Auditor res = this.auditorRepository.save(c);
		return res;
	}

	//Utiles----------------------------------------------------------------------

	public ActorForm construct(final Auditor b) {
		final ActorForm res = new ActorForm();
		res.setEmail(b.getEmail());
		res.setName(b.getName());
		res.setPhone(b.getPhone());
		res.setPhoto(b.getPhoto());
		res.setSurname(b.getSurname());
		res.setUsername(b.getUserAccount().getUsername());
		res.setId(b.getId());
		res.setVersion(b.getVersion());
		res.setAddress(b.getAddress());
		res.setVATNumber(b.getVATNumber());
		if (b.getCreditCard() != null) {
			res.setCVVCode(b.getCreditCard().getCVVCode());
			res.setExpirationMonth(b.getCreditCard().getExpirationMonth());
			res.setExpirationYear(b.getCreditCard().getExpirationYear());
			res.setHolderName(b.getCreditCard().getHolderName());
			res.setMakeName(b.getCreditCard().getMakeName());
			res.setNumber(b.getCreditCard().getNumber());
		}
		return res;
	}

	public void validateForm(final ActorForm form, final BindingResult binding) {
		if (form.getId() == 0 && !form.getAccept()) {
			/*
			 * binding.addError(new FieldError("auditorForm", "accept", form.getAccept(), false, new String[] {
			 * "auditorForm.accept", "accept"
			 * }, new Object[] {
			 * new DefaultMessageSourceResolvable(new String[] {
			 * "auditorForm.accept", "accept"
			 * }, new Object[] {}, "accept")
			 * }, "auditor.mustaccept"));
			 */
			final Locale locale = LocaleContextHolder.getLocale();
			final String errorMessage = this.messageSource.getMessage("auditor.mustaccept", new Object[] {
				form.getAccept()
			}, locale);
			binding.addError(new FieldError("auditorForm", "accept", errorMessage));
		}
		if (!form.getConfirmPassword().equals(form.getPassword())) {
			final Locale locale = LocaleContextHolder.getLocale();
			final String errorMessage = this.messageSource.getMessage("auditor.mustmatch", null, locale);
			binding.addError(new FieldError("actorForm", "confirmPassword", errorMessage));
		}
		if (form.getEmail().endsWith("@") || form.getEmail().endsWith("@>")) {
			final Locale locale = LocaleContextHolder.getLocale();
			final String errorMessage = this.messageSource.getMessage("actor.bademail", new Object[] {
				form.getEmail()
			}, locale);
			binding.addError(new FieldError("actorForm", "email", errorMessage));
		}
		final Date date = new Date(System.currentTimeMillis());
		final Integer year = date.getYear() + 1900;
		Boolean creditCardValid = form.getExpirationYear() >= year;
		if (form.getExpirationYear() == year)
			creditCardValid = form.getExpirationMonth() > date.getMonth();
		if (!creditCardValid) {
			final Locale locale = LocaleContextHolder.getLocale();
			final String errorMessageMonth = this.messageSource.getMessage("actor.creditcardexpired", new Object[] {
				form.getExpirationMonth()
			}, locale);
			final FieldError errorMonth = new FieldError("actorForm", "expirationMonth", form.getExpirationMonth(), true, new String[] {
				"actor.creditcardexpired"
			}, new Object[] {
				form.getExpirationMonth()
			}, errorMessageMonth);
			binding.addError(errorMonth);
			final String errorMessageYear = this.messageSource.getMessage("actor.creditcardexpired", new Object[] {
				form.getExpirationYear()
			}, locale);
			final FieldError errorYear = new FieldError("actorForm", "expirationYear", form.getExpirationYear(), true, new String[] {
				"actor.creditcardexpired"
			}, new Object[] {
				form.getExpirationYear()
			}, errorMessageYear);
			binding.addError(errorYear);
		}
	}
	public Auditor deconstruct(final ActorForm form) {
		Auditor res = null;
		if (form.getId() == 0)
			res = this.create();
		else {
			res = this.findOne(form.getId());
			Assert.notNull(res);
		}
		res.setAddress(form.getAddress());
		res.setEmail(form.getEmail());
		res.setName(form.getName());
		res.setPhone(form.getPhone());
		res.setPhoto(form.getPhoto());
		res.setSurname(form.getSurname());
		res.getUserAccount().setUsername(form.getUsername());
		res.getUserAccount().setPassword(form.getPassword());
		res.setVATNumber(form.getVATNumber());
		CreditCard creditCard = null;
		if (res.getId() == 0)
			creditCard = new CreditCard();
		else
			creditCard = res.getCreditCard();
		creditCard.setCVVCode(form.getCVVCode());
		creditCard.setExpirationMonth(form.getExpirationMonth());
		creditCard.setExpirationYear(form.getExpirationYear());
		creditCard.setHolderName(form.getHolderName());
		creditCard.setMakeName(form.getMakeName());
		creditCard.setNumber(form.getNumber());
		res.setCreditCard(creditCard);
		final Collection<Authority> authorities = new ArrayList<Authority>();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.AUDITOR);
		authorities.add(auth);
		res.getUserAccount().setAuthorities(authorities);
		return res;
	}

	public Auditor findAuditorByUserAcountId(final int userAccountId) {
		return this.auditorRepository.findAuditorByUserAcountId(userAccountId);
	}

	public void deleteAuditor(final Auditor auditor) {
		Assert.notNull(auditor);
		this.serviceUtils.checkActor(auditor);
		this.serviceUtils.checkAuthority("AUDITOR");

		final Collection<SocialProfile> socialProfiles = this.socialProfileService.findProfileByActorId(auditor.getId());
		final CreditCard creditCard = this.creditCardService.findCreditCardByActor(auditor.getId());
		final List<Audit> audits = this.auditService.findAuditsByAuditor(auditor);

		for (final Audit a : audits)
			this.auditService.delete(a);

		this.messageService.deleteMyMessages();

		for (final SocialProfile s : socialProfiles)
			this.socialProfileService.delete(s);

		this.auditorRepository.delete(auditor.getId());
		this.creditCardService.delete1(creditCard);
		this.auditorRepository.flush();
		final Collection<Actor> actors = this.actorService.findAll();
		Assert.isTrue(!(actors.contains(auditor)));
	}

	public Auditor findOne(final Integer id) {
		return this.auditorRepository.findOne(id);
	}

	public void flush() {
		this.auditorRepository.flush();
	}

}

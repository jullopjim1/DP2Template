
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
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import repositories.RookieRepository;
import security.Authority;
import security.UserAccount;
import security.UserAccountRepository;
import domain.Actor;
import domain.Application;
import domain.Configuration;
import domain.CreditCard;
import domain.Curricula;
import domain.Finder;
import domain.Rookie;
import domain.SocialProfile;
import forms.RookieForm;

@Service
@Transactional
public class RookieService {

	@Autowired
	private RookieRepository		repository;

	@Autowired
	private ActorService			actorService;
	@Autowired
	private CreditCardService		creditCardService;
	@Autowired
	private UserAccountRepository	userAccountRepository;
	@Autowired
	private ServiceUtils			serviceUtils;
	@Autowired
	private MessageSource			messageSource;
	@Autowired
	private SocialProfileService	socialProfileService;
	@Autowired
	private ApplicationService		applicationService;
	@Autowired
	private CurriculaService		curriculaService;
	@Autowired
	private FinderService			finderService;
	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private MessageService			messageService;


	public Rookie findOne(final Integer id) {
		this.serviceUtils.checkId(id);
		return this.repository.findOne(id);
	}

	public Collection<Rookie> findAll(final Collection<Integer> ids) {
		this.serviceUtils.checkIds(ids);
		return this.repository.findAll(ids);
	}

	public List<Rookie> findAll() {
		return this.repository.findAll();
	}

	public Rookie create() {
		this.serviceUtils.checkNoActor();
		final Rookie res = new Rookie();
		res.setBanned(false);
		res.setUserAccount(new UserAccount());
		return res;
	}

	public Rookie save(final Rookie h) {
		final Rookie rookie = (Rookie) this.serviceUtils.checkObjectSave(h);

		if ((!h.getPhone().startsWith("+")) && StringUtils.isNumeric(h.getPhone()) && h.getPhone().length() > 3) {
			final Configuration configuration = this.configurationService.findOne();
			h.setPhone(configuration.getCountryCode() + h.getPhone());
		}

		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		encoder.encodePassword(h.getUserAccount().getPassword(), null);
		if (h.getId() == 0) {
			this.serviceUtils.checkNoActor();
			h.setBanned(false);
			h.setSpammer(null);
			//h.getUserAccount().setPassword(hash);

		} else {
			this.serviceUtils.checkAnyAuthority(new String[] {
				Authority.ADMIN, Authority.ROOKIE
			});
			if (this.actorService.findPrincipal() instanceof Rookie) {
				this.serviceUtils.checkActor(rookie);
				h.setBanned(rookie.getBanned());
				h.setSpammer(rookie.getSpammer());
				//if (rookie.getUserAccount().getPassword() != hash)
				//h.getUserAccount().setPassword(hash);
			} else {
				h.setEmail(rookie.getEmail());
				h.setName(rookie.getName());
				h.setPhone(rookie.getPhone());
				h.setPhoto(rookie.getPhoto());
				h.setSurname(rookie.getSurname());
				h.setUserAccount(rookie.getUserAccount());
				h.setAddress(rookie.getAddress());
			}
		}
		final UserAccount userAccount = this.userAccountRepository.save(h.getUserAccount());
		rookie.setUserAccount(userAccount);
		h.setCreditCard(rookie.getCreditCard());
		final CreditCard creditCard = this.creditCardService.save(h.getCreditCard());
		rookie.setCreditCard(creditCard);
		final Rookie res = this.repository.save(h);
		/*
		 * if (h.getId() == 0) {
		 * this.boxService.addSystemBox(res);
		 * }
		 */
		return res;
	}

	public void delete(final Rookie h) {
		final Rookie rookie = (Rookie) this.serviceUtils.checkObject(h);
		this.serviceUtils.checkActor(rookie);
		this.repository.delete(rookie);
	}

	public RookieForm construct(final Rookie h) {
		final RookieForm res = new RookieForm();
		res.setEmail(h.getEmail());
		res.setName(h.getName());
		res.setPhone(h.getPhone());
		res.setPhoto(h.getPhoto());
		res.setSurname(h.getSurname());
		res.setUsername(h.getUserAccount().getUsername());
		res.setId(h.getId());
		res.setVersion(h.getVersion());
		res.setAddress(h.getAddress());
		res.setVATNumber(h.getVATNumber());
		res.setCVVCode(h.getCreditCard().getCVVCode());
		res.setExpirationMonth(h.getCreditCard().getExpirationMonth());
		res.setExpirationYear(h.getCreditCard().getExpirationYear());
		res.setHolderName(h.getCreditCard().getHolderName());
		res.setMakeName(h.getCreditCard().getMakeName());
		res.setNumber(h.getCreditCard().getNumber());
		return res;
	}

	public void validateForm(final RookieForm form, final BindingResult binding) {
		if (form.getId() == 0 && !form.getAccept()) {
			/*
			 * binding.addError(new FieldError("rookieForm", "accept", form.getAccept(), false, new String[] {
			 * "rookieForm.accept", "accept"
			 * }, new Object[] {
			 * new DefaultMessageSourceResolvable(new String[] {
			 * "rookieForm.accept", "accept"
			 * }, new Object[] {}, "accept")
			 * }, "rookie.mustaccept"));
			 */
			final Locale locale = LocaleContextHolder.getLocale();
			final String errorMessage = this.messageSource.getMessage("company.mustaccept", new Object[] {
				form.getAccept()
			}, locale);
			binding.addError(new FieldError("rookieForm", "accept", errorMessage));
		}
		if (!form.getConfirmPassword().equals(form.getPassword())) {
			final Locale locale = LocaleContextHolder.getLocale();
			final String errorMessage = this.messageSource.getMessage("company.mustmatch", null, locale);
			binding.addError(new FieldError("rookieForm", "confirmPassword", errorMessage));
		}
		if (form.getEmail().endsWith("@") || form.getEmail().endsWith("@>")) {
			final Locale locale = LocaleContextHolder.getLocale();
			final String errorMessage = this.messageSource.getMessage("actor.bademail", new Object[] {
				form.getEmail()
			}, locale);
			binding.addError(new FieldError("rookieForm", "email", errorMessage));
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
			final FieldError errorMonth = new FieldError("rookieForm", "expirationMonth", form.getExpirationMonth(), true, new String[] {
				"actor.creditcardexpired"
			}, new Object[] {
				form.getExpirationMonth()
			}, errorMessageMonth);
			binding.addError(errorMonth);
			final String errorMessageYear = this.messageSource.getMessage("actor.creditcardexpired", new Object[] {
				form.getExpirationYear()
			}, locale);
			final FieldError errorYear = new FieldError("rookieForm", "expirationYear", form.getExpirationYear(), true, new String[] {
				"actor.creditcardexpired"
			}, new Object[] {
				form.getExpirationYear()
			}, errorMessageYear);
			binding.addError(errorYear);
		}
	}

	public Rookie deconstruct(final RookieForm form) {
		Rookie res = null;
		if (form.getId() == 0)
			res = this.create();
		else {
			res = this.findOne(form.getId());
			Assert.notNull(res);
		}
		res.setVATNumber(form.getVATNumber());
		res.setAddress(form.getAddress());
		res.setEmail(form.getEmail());
		res.setName(form.getName());
		res.setPhone(form.getPhone());
		res.setPhoto(form.getPhoto());
		res.setSurname(form.getSurname());
		res.getUserAccount().setUsername(form.getUsername());
		res.getUserAccount().setPassword(form.getPassword());
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
		auth.setAuthority(Authority.ROOKIE);
		authorities.add(auth);
		res.getUserAccount().setAuthorities(authorities);
		return res;
	}

	public void deleteRookie(final Rookie rookie) {
		Assert.notNull(rookie);
		this.serviceUtils.checkActor(rookie);
		this.serviceUtils.checkAuthority("ROOKIE");
		final Collection<Application> applications = this.applicationService.findApplicationByRookie(rookie);
		final Collection<SocialProfile> socialProfiles = this.socialProfileService.findProfileByActorId(rookie.getId());
		final CreditCard creditCard = this.creditCardService.findCreditCardByActor(rookie.getId());

		final Finder f = this.finderService.findOneByPrincipal();

		this.messageService.deleteMyMessages();

		for (final Application a : applications) {
			if (a.getCurricula() != null)
				this.curriculaService.deleteCopy(this.curriculaService.findCurriculasByApplicationId(a.getId()).getId());
			this.applicationService.delete1(a);

			final Collection<Application> applications2 = this.applicationService.findAll();
			Assert.isTrue(!(applications2.contains(a)));
		}
		final Collection<Curricula> curriculas = this.curriculaService.findCurriculasByRookieId(rookie.getId());
		for (final Curricula c : curriculas)
			this.curriculaService.delete(c.getId());

		for (final SocialProfile s : socialProfiles)
			this.socialProfileService.delete(s);

		this.repository.delete(rookie.getId());
		if (f != null)
			this.finderService.delete(f);
		if (creditCard != null)
			this.creditCardService.delete1(creditCard);
		this.repository.flush();
		final Collection<Actor> actors = this.actorService.findAll();
		Assert.isTrue(!(actors.contains(rookie)));
	}

	public Rookie findRookieByUserAcountId(final int userAccountId) {
		return this.repository.findRookieByUserAcountId(userAccountId);
	}

}

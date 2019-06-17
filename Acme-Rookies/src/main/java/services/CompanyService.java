
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

import repositories.CompanyRepository;
import security.Authority;
import security.UserAccount;
import security.UserAccountRepository;
import domain.Actor;
import domain.Application;
import domain.Audit;
import domain.Company;
import domain.Configuration;
import domain.CreditCard;
import domain.Position;
import domain.Problem;
import domain.SocialProfile;
import domain.Sponsorship;
import forms.CompanyForm;

@Service
@Transactional
public class CompanyService {

	@Autowired
	private CompanyRepository		repository;

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
	private PositionService			positionService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private ProblemService			problemService;

	@Autowired
	private SocialProfileService	socialProfileService;

	@Autowired
	private ApplicationService		applicationService;

	@Autowired
	private SponsorshipService		sponsorshipService;

	@Autowired
	private AuditService			auditService;

	@Autowired
	private ConfigurationService	configurationService;


	public Company findOne(final Integer id) {
		this.serviceUtils.checkId(id);
		return this.repository.findOne(id);
	}

	public Collection<Company> findAll(final Collection<Integer> ids) {
		this.serviceUtils.checkIds(ids);
		return this.repository.findAll(ids);
	}

	public List<Company> findAll() {
		return this.repository.findAll();
	}

	public Company create() {
		this.serviceUtils.checkNoActor();
		final Company res = new Company();
		res.setBanned(false);
		res.setUserAccount(new UserAccount());
		return res;
	}

	public Company save(final Company c) {
		final Company company = (Company) this.serviceUtils.checkObjectSave(c);

		if ((!c.getPhone().startsWith("+")) && StringUtils.isNumeric(c.getPhone()) && c.getPhone().length() > 3) {
			final Configuration configuration = this.configurationService.findOne();
			c.setPhone(configuration.getCountryCode() + c.getPhone());
		}

		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hash = encoder.encodePassword(c.getUserAccount().getPassword(), null);
		if (c.getId() == 0) {
			this.serviceUtils.checkNoActor();
			c.setBanned(false);
			c.setSpammer(null);
			c.getUserAccount().setPassword(hash);

		} else {
			this.serviceUtils.checkAnyAuthority(new String[] {
				Authority.ADMIN, Authority.COMPANY
			});
			if (this.actorService.findPrincipal() instanceof Company) {
				this.serviceUtils.checkActor(company);
				c.setBanned(company.getBanned());
				c.setSpammer(company.getSpammer());
				if (company.getUserAccount().getPassword() != hash)
					c.getUserAccount().setPassword(hash);
			} else {
				c.setEmail(company.getEmail());
				c.setName(company.getName());
				c.setPhone(company.getPhone());
				c.setPhoto(company.getPhoto());
				c.setSurname(company.getSurname());
				c.setUserAccount(company.getUserAccount());
				c.setAddress(company.getAddress());
			}
		}
		final UserAccount userAccount = this.userAccountRepository.save(c.getUserAccount());
		company.setUserAccount(userAccount);
		c.setCreditCard(company.getCreditCard());
		final CreditCard creditCard = this.creditCardService.save(c.getCreditCard());
		company.setCreditCard(creditCard);
		final Company res = this.repository.save(c);
		/*
		 * if (b.getId() == 0) {
		 * this.boxService.addSystemBox(res);
		 * }
		 */
		return res;
	}

	public Company saveForAdmins(final Company c) {

		final Company res = this.repository.save(c);

		return res;
	}

	public void delete(final Company b) {
		final Company company = (Company) this.serviceUtils.checkObject(b);
		this.serviceUtils.checkActor(company);
		this.repository.delete(company);
	}

	public CompanyForm construct(final Company b) {
		final CompanyForm res = new CompanyForm();
		res.setEmail(b.getEmail());
		res.setName(b.getName());
		res.setPhone(b.getPhone());
		res.setPhoto(b.getPhoto());
		res.setSurname(b.getSurname());
		res.setUsername(b.getUserAccount().getUsername());
		res.setId(b.getId());
		res.setVersion(b.getVersion());
		res.setAddress(b.getAddress());
		res.setCompanyName(b.getComercialName());
		res.setVATNumber(b.getVATNumber());
		res.setScore(b.getScore());
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

	public void validateForm(final CompanyForm form, final BindingResult binding) {
		if (form.getId() == 0 && !form.getAccept()) {
			/*
			 * binding.addError(new FieldError("companyForm", "accept", form.getAccept(), false, new String[] {
			 * "companyForm.accept", "accept"
			 * }, new Object[] {
			 * new DefaultMessageSourceResolvable(new String[] {
			 * "companyForm.accept", "accept"
			 * }, new Object[] {}, "accept")
			 * }, "company.mustaccept"));
			 */
			final Locale locale = LocaleContextHolder.getLocale();
			final String errorMessage = this.messageSource.getMessage("company.mustaccept", new Object[] {
				form.getAccept()
			}, locale);
			binding.addError(new FieldError("companyForm", "accept", errorMessage));
		}
		if (!form.getConfirmPassword().equals(form.getPassword())) {
			final Locale locale = LocaleContextHolder.getLocale();
			final String errorMessage = this.messageSource.getMessage("company.mustmatch", null, locale);
			binding.addError(new FieldError("companyForm", "confirmPassword", errorMessage));
		}
		if (form.getEmail().endsWith("@") || form.getEmail().endsWith("@>")) {
			final Locale locale = LocaleContextHolder.getLocale();
			final String errorMessage = this.messageSource.getMessage("actor.bademail", new Object[] {
				form.getEmail()
			}, locale);
			binding.addError(new FieldError("companyForm", "email", errorMessage));
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
			final FieldError errorMonth = new FieldError("companyForm", "expirationMonth", form.getExpirationMonth(), true, new String[] {
				"actor.creditcardexpired"
			}, new Object[] {
				form.getExpirationMonth()
			}, errorMessageMonth);
			binding.addError(errorMonth);
			final String errorMessageYear = this.messageSource.getMessage("actor.creditcardexpired", new Object[] {
				form.getExpirationYear()
			}, locale);
			final FieldError errorYear = new FieldError("companyForm", "expirationYear", form.getExpirationYear(), true, new String[] {
				"actor.creditcardexpired"
			}, new Object[] {
				form.getExpirationYear()
			}, errorMessageYear);
			binding.addError(errorYear);
		}
	}
	public Company deconstruct(final CompanyForm form) {
		Company res = null;
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
		res.setComercialName(form.getCompanyName());
		res.setVATNumber(form.getVATNumber());
		res.setScore(form.getScore());
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
		auth.setAuthority(Authority.COMPANY);
		authorities.add(auth);
		res.getUserAccount().setAuthorities(authorities);
		return res;
	}

	public Company findCompanyByUserAcountId(final int userAccountId) {
		return this.repository.findCompanyByUserAcountId(userAccountId);
	}

	public void delete1(final Company company) {
		this.repository.delete(company);
	}

	public void deleteCompany(final Company company) {
		Assert.notNull(company);
		this.serviceUtils.checkActor(company);
		this.serviceUtils.checkAuthority("COMPANY");
		final Collection<Position> positions = this.positionService.findPositionsByCompanyId(company.getId());
		final Collection<Problem> problems = this.problemService.findProblemsByCompanyId(company.getId());
		final Collection<Application> applications = this.applicationService.findApplicationByCompany(company.getId());
		final Collection<SocialProfile> socialProfiles = this.socialProfileService.findProfileByActorId(company.getId());
		final CreditCard creditCard = this.creditCardService.findCreditCardByActor(company.getId());

		for (final Application a : applications) {
			this.applicationService.delete(a);
			final Collection<Application> applications2 = this.applicationService.findAll();
			Assert.isTrue(!(applications2.contains(a)));
		}

		for (final Problem p : problems) {
			this.problemService.delete1(p);
			final Collection<Problem> problems2 = this.problemService.findAll();
			Assert.isTrue(!(problems2.contains(p)));
		}
		this.messageService.deleteMyMessages();

		for (final Position p : positions) {
			final Collection<Audit> audits = this.auditService.findAuditsByPosition(p.getId());
			final Collection<Sponsorship> sponsorships = this.sponsorshipService.findSponsorshipsByPositionId(p.getId());
			for (final Audit a : audits)
				this.auditService.delete1(a);
			for (final Sponsorship s : sponsorships) {
				s.setPosition(null);
				this.sponsorshipService.save1(s);
			}
			this.positionService.delete1(p);
			final Collection<Position> positions2 = this.positionService.findAll();
			Assert.isTrue(!(positions2.contains(p)));
		}

		for (final SocialProfile s : socialProfiles)
			this.socialProfileService.delete(s);

		this.repository.delete(company.getId());

		if (creditCard != null)
			this.creditCardService.delete1(creditCard);

		this.repository.flush();
		final Collection<Actor> actors = this.actorService.findAll();
		Assert.isTrue(!(actors.contains(company)));
	}

}

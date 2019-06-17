
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

import repositories.ProviderRepository;
import security.Authority;
import security.UserAccount;
import security.UserAccountRepository;
import domain.Configuration;
import domain.CreditCard;
import domain.Item;
import domain.Provider;
import domain.SocialProfile;
import domain.Sponsorship;
import forms.ActorForm;
import forms.ProviderForm;

@Service
@Transactional
public class ProviderService {

	//Repository-----------------------------------------------------------------------

	@Autowired
	private ProviderRepository		providerRepository;

	@Autowired
	private SocialProfileService	socialProfileService;

	@Autowired
	private ServiceUtils			serviceUtils;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private CreditCardService		creditCardService;

	@Autowired
	private ItemService				itemService;

	@Autowired
	private SponsorshipService		sponsorshipService;

	@Autowired
	private UserAccountRepository	userAccountRepository;

	@Autowired
	private MessageSource			messageSource;

	@Autowired
	private ConfigurationService	configurationService;


	//CRUD----------------------------------------------------------------------------

	public Collection<Provider> findAll(final Collection<Integer> ids) {
		this.serviceUtils.checkIds(ids);
		return this.providerRepository.findAll(ids);
	}

	public List<Provider> findAll() {
		return this.providerRepository.findAll();
	}

	public Provider create() {
		this.serviceUtils.checkNoActor();
		final Provider res = new Provider();
		res.setBanned(false);
		res.setUserAccount(new UserAccount());
		final Collection<Authority> authorities = new ArrayList<Authority>();
		final Authority authority = new Authority();
		authority.setAuthority(Authority.PROVIDER);
		authorities.add(authority);
		res.getUserAccount().setAuthorities(authorities);
		return res;
	}
	public Provider save(final Provider c) {
		final Provider provider = (Provider) this.serviceUtils.checkObjectSave(c);

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
				Authority.ADMIN, Authority.PROVIDER
			});
			if (this.actorService.findPrincipal() instanceof Provider) {
				this.serviceUtils.checkActor(provider);
				c.setBanned(provider.getBanned());
				c.setSpammer(provider.getSpammer());
				if (provider.getUserAccount().getPassword() != hash)
					c.getUserAccount().setPassword(hash);
			} else {
				c.setEmail(provider.getEmail());
				c.setName(provider.getName());
				c.setPhone(provider.getPhone());
				c.setPhoto(provider.getPhoto());
				c.setSurname(provider.getSurname());
				c.setUserAccount(provider.getUserAccount());
				c.setAddress(provider.getAddress());
			}
		}
		final UserAccount userAccount = this.userAccountRepository.save(c.getUserAccount());
		provider.setUserAccount(userAccount);
		c.setCreditCard(provider.getCreditCard());
		final CreditCard creditCard = this.creditCardService.save(c.getCreditCard());
		provider.setCreditCard(creditCard);
		final Provider res = this.providerRepository.save(c);
		return res;
	}

	//Utiles----------------------------------------------------------------------

	public ProviderForm construct(final Provider b) {
		final ProviderForm res = new ProviderForm();
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
		res.setMake(b.getMake());
		res.setAuthority(Authority.PROVIDER);
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
			 * binding.addError(new FieldError("providerForm", "accept", form.getAccept(), false, new String[] {
			 * "providerForm.accept", "accept"
			 * }, new Object[] {
			 * new DefaultMessageSourceResolvable(new String[] {
			 * "providerForm.accept", "accept"
			 * }, new Object[] {}, "accept")
			 * }, "provider.mustaccept"));
			 */
			final Locale locale = LocaleContextHolder.getLocale();
			final String errorMessage = this.messageSource.getMessage("company.mustaccept", new Object[] {
				form.getAccept()
			}, locale);
			binding.addError(new FieldError("providerForm", "accept", errorMessage));
		}
		if (!form.getConfirmPassword().equals(form.getPassword())) {
			final Locale locale = LocaleContextHolder.getLocale();
			final String errorMessage = this.messageSource.getMessage("company.mustmatch", null, locale);
			binding.addError(new FieldError("providerForm", "confirmPassword", errorMessage));
		}
		if (form.getEmail().endsWith("@") || form.getEmail().endsWith("@>")) {
			final Locale locale = LocaleContextHolder.getLocale();
			final String errorMessage = this.messageSource.getMessage("actor.bademail", new Object[] {
				form.getEmail()
			}, locale);
			binding.addError(new FieldError("providerForm", "email", errorMessage));
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
			final FieldError errorMonth = new FieldError("providerForm", "expirationMonth", form.getExpirationMonth(), true, new String[] {
				"actor.creditcardexpired"
			}, new Object[] {
				form.getExpirationMonth()
			}, errorMessageMonth);
			binding.addError(errorMonth);
			final String errorMessageYear = this.messageSource.getMessage("actor.creditcardexpired", new Object[] {
				form.getExpirationYear()
			}, locale);
			final FieldError errorYear = new FieldError("providerForm", "expirationYear", form.getExpirationYear(), true, new String[] {
				"actor.creditcardexpired"
			}, new Object[] {
				form.getExpirationYear()
			}, errorMessageYear);
			binding.addError(errorYear);
		}
	}
	public Provider deconstruct(final ProviderForm form) {
		Provider res = null;
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
		res.setMake(form.getMake());
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
		auth.setAuthority(Authority.PROVIDER);
		authorities.add(auth);
		res.getUserAccount().setAuthorities(authorities);
		return res;
	}
	//CRUD----------------------------------------------------------------------------

	//Other methods--------------------------------------------------------------------------------

	public Provider findProviderByUserAcountId(final int userAccountId) {
		return this.providerRepository.findProviderByUserAcountId(userAccountId);
	}

	public Provider findOne(final Integer id) {
		return this.providerRepository.findOne(id);
	}

	public void deleteProvider(final Provider provider) {
		Assert.notNull(provider);
		this.serviceUtils.checkActor(provider);
		this.serviceUtils.checkAuthority("PROVIDER");

		final Collection<SocialProfile> socialProfiles = this.socialProfileService.findProfileByActorId(provider.getId());
		final CreditCard a = this.creditCardService.findCreditCardByActor(provider.getId());
		final List<Item> items = this.itemService.findAllByPrincipal();
		final Collection<Sponsorship> sponsorships = this.sponsorshipService.findSponsorshipsByProviderId(provider.getId());

		for (final Item i : items)
			this.itemService.delete(i);

		for (final Sponsorship s : sponsorships) {
			final CreditCard c = s.getCreditCard();
			this.sponsorshipService.delete(s);
			this.creditCardService.delete1(c);
		}
		this.messageService.deleteMyMessages();

		for (final SocialProfile s : socialProfiles)
			this.socialProfileService.delete(s);

		this.providerRepository.delete(provider.getId());

		if (a != null)
			this.creditCardService.delete1(a);

		this.providerRepository.flush();

	}

}


package services;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.SponsorshipRepository;
import security.LoginService;
import domain.CreditCard;
import domain.Provider;
import domain.Sponsorship;

@Service
@Transactional
public class SponsorshipService {

	//Repository---------------------------------------------------------------

	@Autowired
	private SponsorshipRepository	sponsorshipRepository;

	//Services------------------------------------------------------------------
	@Autowired
	ServiceUtils					serviceUtils;

	@Autowired
	ProviderService					providerService;

	@Autowired
	PositionService					positionService;

	@Autowired
	CreditCardService				creditCardService;


	//CRUD----------------------------------------------------------------------
	public Sponsorship create() {
		final Sponsorship sponsorship = new Sponsorship();
		final Provider provider = this.providerService.findProviderByUserAcountId(LoginService.getPrincipal().getId());
		final CreditCard creditCard = this.creditCardService.findCreditCardByActor(provider.getId());
		Assert.notNull(creditCard);
		this.serviceUtils.checkAuthority("PROVIDER");

		sponsorship.setProvider(provider);
		sponsorship.setCreditCard(creditCard);
		return sponsorship;
	}

	public Sponsorship save(final Sponsorship sponsorship) {
		Sponsorship res = new Sponsorship();
		Assert.notNull(sponsorship);

		if (sponsorship.getId() != 0) {
			this.sponsorshipRepository.flush();
			final Sponsorship sSave = this.sponsorshipRepository.findOne(sponsorship.getId());
			Assert.notNull(sSave);
		}

		this.serviceUtils.checkActor(sponsorship.getProvider());
		this.serviceUtils.checkAuthority("PROVIDER");
		this.serviceUtils.checkIdSave(sponsorship);

		res = this.sponsorshipRepository.save(sponsorship);
		return res;

	}

	public Sponsorship save1(final Sponsorship sponsorship) {
		Assert.notNull(sponsorship);

		final Sponsorship res = this.sponsorshipRepository.save(sponsorship);
		return res;

	}

	public Sponsorship findOne(final int sponsorshipId) {
		return this.sponsorshipRepository.findOne(sponsorshipId);
	}

	public Collection<Sponsorship> findAll() {
		return this.sponsorshipRepository.findAll();
	}

	public Collection<Sponsorship> findSponsorshipsByProviderId(final int providerId) {
		return this.sponsorshipRepository.findSponsorshipsByProviderId(providerId);
	}

	public Collection<Sponsorship> findSponsorshipsByPositionId(final int positionId) {
		return this.sponsorshipRepository.findSponsorshipsByPositionId(positionId);
	}

	public Sponsorship getRandomSponsorshipByPositionId(final int positionId) {
		final List<Sponsorship> sponsorships = (List<Sponsorship>) this.findSponsorshipsByPositionId(positionId);
		final Random rand = new Random();
		final int n = rand.nextInt(sponsorships.size());
		return sponsorships.get(n);
	}
	public void flush() {
		this.sponsorshipRepository.flush();
	}

	public void delete(final Sponsorship sponsorship) {
		Assert.notNull(sponsorship);
		this.sponsorshipRepository.flush();
		final Sponsorship sDelete = this.sponsorshipRepository.findOne(sponsorship.getId());
		this.serviceUtils.checkActor(sDelete.getProvider());
		this.serviceUtils.checkAuthority("PROVIDER");
		this.serviceUtils.checkIdSave(sDelete);
		this.sponsorshipRepository.delete(sDelete);
	}
	public void delete1(final Sponsorship sponsorship) {
		this.sponsorshipRepository.delete(sponsorship);
	}
	//DASHBOARD QUERIES--------------------------------------------------------------
	public Double queryRookiesA1AVG() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.sponsorshipRepository.queryRookiesA1AVG();
	}

	public Double queryRookiesA1MAX() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.sponsorshipRepository.queryRookiesA1MAX();
	}

	public Double queryRookiesA1MIN() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.sponsorshipRepository.queryRookiesA1MIN();
	}

	public Double queryRookiesA1STDDEV() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.sponsorshipRepository.queryRookiesA1STDDEV();
	}

	public Double queryRookiesA2AVG() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.sponsorshipRepository.queryRookiesA2AVG();
	}

	public Double queryRookiesA2MAX() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.sponsorshipRepository.queryRookiesA2MAX();
	}

	public Double queryRookiesA2MIN() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.sponsorshipRepository.queryRookiesA2MIN();
	}

	public Double queryRookiesA2STDDEV() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.sponsorshipRepository.queryRookiesA2STDDEV();
	}

	public Collection<Provider> queryRookiesA3() {
		this.serviceUtils.checkAuthority("ADMIN");
		return this.sponsorshipRepository.queryRookiesA3();
	}

}

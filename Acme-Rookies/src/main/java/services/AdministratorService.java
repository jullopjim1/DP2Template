
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AdministratorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Administrator;
import domain.Audit;
import domain.Company;
import domain.Configuration;
import domain.CreditCard;
import domain.Message;
import domain.Position;
import domain.SocialProfile;

@Service
@Transactional
public class AdministratorService {

	//--------------Managed repository---------------------------

	@Autowired
	private AdministratorRepository	administratorRepository;

	//-------------- Supporting Services-----------------------

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private CreditCardService		creditCardService;

	@Autowired
	private CompanyService			companyService;

	@Autowired
	private ServiceUtils			serviceUtils;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private SocialProfileService	socialProfileService;

	@Autowired
	private PositionService			positionService;

	@Autowired
	private AuditService			auditService;


	// --------------------------Constructor-----------------------

	public AdministratorService() {
		super();
	}

	// --------------------CRUD methods----------------------------

	//----------------------------

	public Administrator findOne(final int administratorId) {
		return this.administratorRepository.findOne(administratorId);
	}

	public Collection<Administrator> findAll() {
		Collection<Administrator> res;
		res = this.administratorRepository.findAll();
		Assert.notNull(res);

		return res;
	}

	public Administrator create() {
		Administrator result;
		result = new Administrator();
		result.setBanned(false);
		//establezco ya su tipo de userAccount porque no va a cambiar
		result.setUserAccount(new UserAccount());
		return result;

	}

	public Administrator save(final Administrator administrator) {
		//comprobamos que el customer que nos pasan no sea nulo
		Assert.notNull(administrator);
		Boolean isCreating = null;

		if ((!administrator.getPhone().startsWith("+")) && StringUtils.isNumeric(administrator.getPhone()) && administrator.getPhone().length() > 3) {
			final Configuration configuration = this.configurationService.findOne();
			administrator.setPhone(configuration.getCountryCode() + administrator.getPhone());
		}

		//Si el admin que estamos guardando es nuevo (no está en la base de datos) le ponemos todos sus atributos vacíos
		if (administrator.getId() == 0) {
			isCreating = true;
			administrator.setSpammer(null);

			//Comprobamos que el actor sea un admin
			this.serviceUtils.checkAuthority("ADMIN");

			//comprobamos que ningún actor resté autenticado (ya que ningun actor puede crear los customers)
			//this.serviceUtils.checkNoActor();

		} else {
			isCreating = false;
			//comprobamos que su id no sea negativa por motivos de seguridad
			this.serviceUtils.checkIdSave(administrator);

			//este admin será el que está en la base de datos para usarlo si estamos ante un admin que ya existe
			Administrator adminDB;
			Assert.isTrue(administrator.getId() > 0);

			//cogemos el admin de la base de datos
			adminDB = this.administratorRepository.findOne(administrator.getId());

			administrator.setSpammer(adminDB.getSpammer());
			administrator.setUserAccount(adminDB.getUserAccount());

			//Comprobamos que el actor sea un admin
			this.serviceUtils.checkAuthority("ADMIN");
			//esto es para ver si el actor que está logueado es el mismo que se está editando
			//this.serviceUtils.checkActor(administrator);

		}
		if ((!administrator.getPhone().startsWith("+")) && StringUtils.isNumeric(administrator.getPhone()) && administrator.getPhone().length() > 3) {
			final Configuration configuration = this.configurationService.findOne();
			administrator.setPhone(configuration.getCountryCode() + administrator.getPhone());
		}
		Administrator res;
		final CreditCard creditCard = this.creditCardService.save(administrator.getCreditCard());
		administrator.setCreditCard(creditCard);
		//le meto al resultado final el admin que he ido modificando anteriormente
		res = this.administratorRepository.save(administrator);
		this.flush();

		return res;
	}
	// -------------------------Other business methods ------------------------------

	public void flush() {
		this.administratorRepository.flush();
	}

	//	public void banActor(final Actor a) {
	//		Assert.notNull(a);
	//		Assert.isTrue(a.getSpammer() == true || a.getScore() < -0.5);
	//		this.serviceUtils.checkAuthority("ADMIN");
	//		a.setBanned(true);
	//		this.actorService.save(a);
	//
	//	}

	//	public void unBanActor(final Actor a) {
	//		Assert.notNull(a);
	//		this.serviceUtils.checkAuthority("ADMIN");
	//		a.setBanned(false);
	//		this.actorService.save(a);
	//	}

	public boolean isPrincipalAdmin() {
		boolean res = false;
		final UserAccount u = LoginService.getPrincipal();
		final Authority a = new Authority();
		a.setAuthority("ADMIN");
		if (u.getAuthorities().contains(a))
			res = true;
		return res;
	}

	public boolean checkNumAdmin() {
		final List<Administrator> a = this.administratorRepository.findAll();
		boolean res = false;
		if (a.size() > 1)
			res = true;
		Assert.isTrue(res == true, "UniqueAdmin");
		return res;
	}

	public void deleteAdmin(final Administrator admin) {
		Assert.notNull(admin);
		this.checkNumAdmin();
		this.serviceUtils.checkAuthority("ADMIN");
		this.serviceUtils.checkActor(admin);
		final Collection<SocialProfile> socialProfiles = this.socialProfileService.findProfileByActorId(admin.getId());
		final CreditCard creditCard = this.creditCardService.findCreditCardByActor(admin.getId());

		for (final SocialProfile s : socialProfiles) {
			Assert.isTrue(s.getActor().getId() == admin.getId());
			this.socialProfileService.delete(s);
		}
		this.messageService.deleteMyMessages();

		this.administratorRepository.delete(admin.getId());

		if (creditCard != null)
			this.creditCardService.delete1(creditCard);

		final Collection<Actor> actors = this.actorService.findAll();
		Assert.isTrue(!(actors.contains(admin)));
	}

	public void generateAllSpammers() {
		this.serviceUtils.checkAuthority(Authority.ADMIN);
		final Collection<Actor> res = this.actorService.findAllTypes();
		for (final Actor a : res) {
			if (a.getSpammer() == null)
				a.setSpammer(false);
			if (a.getSpammer() == false) {
				final Boolean spammer = this.isSpammer(a);
				a.setSpammer(spammer);
				this.actorService.save(a);

				this.flush();
			}
		}

	}

	public boolean isSpammer(final Actor a) {
		boolean res = false;
		Assert.notNull(a);
		this.serviceUtils.checkId(a.getId());

		Assert.notNull(a);

		if (!res)
			for (final Message m : this.messageService.findSendedMessages(a)) {
				res = this.containsSpam(m.getBody()) || this.containsSpam(m.getSubject());

				if (!res)
					for (final String tag : m.getTags())
						res = this.containsSpam(tag);

				else
					break;
			}

		return res;
	}

	public boolean containsSpam(final String s) {
		boolean res = false;
		final List<String> negativeWords = new ArrayList<>();
		negativeWords.addAll(this.configurationService.findOne().getSpamWordsEN());
		negativeWords.addAll(this.configurationService.findOne().getSpamWordsES());
		for (final String spamWord : negativeWords)
			if (s.contains(spamWord)) {
				res = true;
				break;
			}
		return res;
	}
	public void generateCompanyScores() {
		this.serviceUtils.checkAuthority(Authority.ADMIN);
		final Collection<Company> allCompanies = this.companyService.findAll();
		for (final Company c : allCompanies) {
			System.out.println("=====COMPANY===================>>" + c.getName());
			Double finalValue = null;
			Double value = 0.0;

			final Collection<Position> positionFinish = this.positionService.findPositionsByCompanyIdFinals(c.getId());
			for (final Position p : positionFinish) {
				System.out.println("=====Position===================>>" + p.getTitle());
				final Collection<Audit> audits = this.auditService.findAuditsByPosition(p.getId());
				if (audits.size() == 0)
					System.out.println(p.getTitle() + "-----> No tiene audits");
				else {
					for (final Audit a : audits)
						value += a.getScore();
					value = value / audits.size();

					//reescalamos al rango 0,1
					finalValue = ((value / 10) * 1) + 0;
				}

			}
			c.setScore(finalValue);
			this.companyService.saveForAdmins(c);

		}
	}
	//		public boolean containsSpam(final String s) {
	//			boolean res = false;
	//			final List<String> negativeWords = new ArrayList<>();
	//			negativeWords.addAll(this.configurationService.findOne().getNegativeWordsEN());
	//			negativeWords.addAll(this.configurationService.findOne().getNegativeWordsES());
	//			for (final String spamWord : negativeWords)
	//				if (s.contains(spamWord)) {
	//					System.out.println(spamWord);
	//					res = true;
	//					break;
	//				}
	//			return res;
	//		}

}

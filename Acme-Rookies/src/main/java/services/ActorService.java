
package services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

import repositories.ActorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import domain.Actor;
import domain.Administrator;
import domain.Application;
import domain.Audit;
import domain.Auditor;
import domain.Company;
import domain.Configuration;
import domain.CreditCard;
import domain.Item;
import domain.Problem;
import domain.Provider;
import domain.Rookie;
import domain.Sponsorship;
import forms.ActorForm;

@Service
@Transactional
public class ActorService {

	// Managed repository ----------------------------------------------------------------
	@Autowired
	private ActorRepository			actorRepository;

	@Autowired
	private UserAccountRepository	userAccountRepository;

	//Service-----------------------------------------------------------------------------

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private CompanyService			companyService;

	@Autowired
	private RookieService			rookieService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired(required = false)
	private Validator				validator;

	@Autowired
	private MessageSource			messageSource;

	@Autowired
	private ServiceUtils			serviceUtils;

	@Autowired
	private ApplicationService		applicationService;

	@Autowired
	private ProblemService			problemService;

	@Autowired
	private AuditorService			auditorService;

	@Autowired
	private ProviderService			providerService;

	@Autowired
	private ItemService				itemService;

	@Autowired
	private AuditService			auditService;

	@Autowired
	private SponsorshipService		sponsorshipService;


	public Actor create(final String authority) {
		final Actor actor = new Actor();
		final UserAccount userAccount = new UserAccount();
		final Collection<Authority> authorities = new ArrayList<Authority>();

		final Authority a = new Authority();
		a.setAuthority(authority);
		authorities.add(a);
		userAccount.setAuthorities(authorities);
		actor.setUserAccount(userAccount);
		actor.setBanned(false);
		return actor;
	}

	public Actor findOne(final int ActorId) {
		return this.actorRepository.findOne(ActorId);
	}

	public Actor findByUsername(final String username) {
		final Actor actor = this.actorRepository.findByUsername(username);
		return actor;
	}

	public Actor findByUserAccount(final UserAccount userAccount) {
		return this.actorRepository.findByUserAccount(userAccount.getId());
	}

	public Collection<Actor> findAll() {
		final Collection<Actor> actors = this.actorRepository.findAll();
		Assert.notNull(actors);

		return actors;
	}

	public Collection<Actor> findAllTypes() {
		final Collection<Actor> actors = new ArrayList<>();
		actors.addAll(this.administratorService.findAll());
		actors.addAll(this.companyService.findAll());
		actors.addAll(this.rookieService.findAll());

		return actors;
	}

	public Actor save(final Actor actor) {

		Assert.notNull(actor);

		if ((!actor.getPhone().startsWith("+")) && StringUtils.isNumeric(actor.getPhone()) && actor.getPhone().length() > 3) {
			final Configuration configuration = this.configurationService.findOne();
			actor.setPhone(configuration.getCountryCode() + actor.getPhone());
		}

		final Actor saved = this.actorRepository.save(actor);

		return saved;
	}

	//Other Methods---------------------------------------------------------------------

	public Collection<Actor> findSpammerActors() {
		return this.actorRepository.findSpammerActors();
	}

	public void ban(final Actor actor) {
		actor.setBanned(true);
		this.save(actor);
	}

	public void unban(final Actor actor) {
		actor.setBanned(false);
		actor.setSpammer(false);
		this.save(actor);

	}

	public void update(final Actor actor) {

		Assert.notNull(actor);

		final Collection<Authority> authorities = actor.getUserAccount().getAuthorities();
		final Authority com = new Authority();
		com.setAuthority(Authority.COMPANY);
		final Authority hack = new Authority();
		hack.setAuthority(Authority.ROOKIE);
		final Authority admin = new Authority();
		admin.setAuthority(Authority.ADMIN);
		final Authority audit = new Authority();
		audit.setAuthority(Authority.AUDITOR);
		final Authority prov = new Authority();
		prov.setAuthority(Authority.PROVIDER);

		if (authorities.contains(com)) {
			Company company = null;
			if (actor.getId() != 0)
				company = this.companyService.findOne(actor.getId());
			else
				company = this.companyService.create();
			company.setUserAccount(actor.getUserAccount());

			company.setEmail(actor.getEmail());
			company.setBanned(actor.getBanned());
			company.setSpammer(actor.getSpammer());
			company.setName(actor.getName());
			company.setPhone(actor.getPhone());
			company.setPhoto(actor.getPhoto());
			company.setSurname(actor.getSurname());
			company.setAddress(actor.getAddress());
			company.setCreditCard(actor.getCreditCard());
			company.setVATNumber(actor.getVATNumber());

			final Actor actor1 = this.companyService.save(company);
			//if (actor.getId() == 0)
			//this.boxService.addSystemBox(actor1);
		} else if (authorities.contains(hack)) {
			Rookie rookie = null;
			if (actor.getId() != 0)

				rookie = this.rookieService.findOne(actor.getId());
			else {
				rookie = this.rookieService.create();
				rookie.setUserAccount(actor.getUserAccount());
			}
			rookie.setEmail(actor.getEmail());
			rookie.setBanned(actor.getBanned());
			rookie.setSpammer(actor.getSpammer());
			rookie.setName(actor.getName());
			rookie.setPhone(actor.getPhone());
			rookie.setPhoto(actor.getPhoto());
			rookie.setSurname(actor.getSurname());
			rookie.setAddress(actor.getAddress());
			rookie.setCreditCard(actor.getCreditCard());
			rookie.setVATNumber(actor.getVATNumber());

			final Actor actor1 = this.rookieService.save(rookie);
			//if (actor.getId() == 0)
			//this.boxService.addSystemBox(actor1);

		} else if (authorities.contains(admin)) {
			Administrator administrator = null;
			if (actor.getId() != 0)
				administrator = this.administratorService.findOne(actor.getId());
			else {
				administrator = this.administratorService.create();
				administrator.setUserAccount(actor.getUserAccount());
			}

			administrator.setSurname(actor.getSurname());
			administrator.setEmail(actor.getEmail());
			administrator.setBanned(actor.getBanned());
			administrator.setSpammer(actor.getSpammer());
			administrator.setName(actor.getName());
			administrator.setPhone(actor.getPhone());
			administrator.setPhoto(actor.getPhoto());
			administrator.setAddress(actor.getAddress());
			administrator.setCreditCard(actor.getCreditCard());
			administrator.setVATNumber(actor.getVATNumber());

			final Actor actor1 = this.administratorService.save(administrator);
			//if (actor.getId() == 0)
			//this.boxService.addSystemBox(actor1);
		} else if (authorities.contains(audit)) {
			Auditor auditor = null;
			if (actor.getId() != 0)
				auditor = this.auditorService.findOne(actor.getId());
			else {
				auditor = this.auditorService.create();
				auditor.setUserAccount(actor.getUserAccount());
			}

			auditor.setSurname(actor.getSurname());
			auditor.setEmail(actor.getEmail());
			auditor.setBanned(actor.getBanned());
			auditor.setSpammer(actor.getSpammer());
			auditor.setName(actor.getName());
			auditor.setPhone(actor.getPhone());
			auditor.setPhoto(actor.getPhoto());
			auditor.setAddress(actor.getAddress());
			auditor.setCreditCard(actor.getCreditCard());
			auditor.setVATNumber(actor.getVATNumber());

			final Actor actor1 = this.auditorService.save(auditor);
		} else if (authorities.contains(prov)) {
			Provider provider = null;
			if (actor.getId() != 0)
				provider = this.providerService.findOne(actor.getId());
			else {
				provider = this.providerService.create();
				provider.setUserAccount(actor.getUserAccount());
			}

			provider.setSurname(actor.getSurname());
			provider.setEmail(actor.getEmail());
			provider.setBanned(actor.getBanned());
			provider.setSpammer(actor.getSpammer());
			provider.setName(actor.getName());
			provider.setPhone(actor.getPhone());
			provider.setPhoto(actor.getPhoto());
			provider.setAddress(actor.getAddress());
			provider.setCreditCard(actor.getCreditCard());
			provider.setVATNumber(actor.getVATNumber());

			final Actor actor1 = this.providerService.save(provider);
		}

	}
	public Actor findActorByUsername(final String username) {
		final Actor actor = this.actorRepository.findByUsername(username);
		return actor;
	}

	public Actor findPrincipal() {
		final UserAccount userAccount = LoginService.getPrincipal();
		return this.actorRepository.findByUserAccount(userAccount.getId());
	}

	public Collection<Actor> findAllExceptMe() {
		final Collection<Actor> res = this.findAll();
		res.remove(this.findPrincipal());
		return res;
	}

	public ActorForm construct(final Actor b) {
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
		final Authority auth = ((List<Authority>) b.getUserAccount().getAuthorities()).get(0);
		/*
		 * if (auth.getAuthority().equals(("CHAPTER"))) {
		 * final Chapter c = this.chapterService.findOne(b.getId());
		 * res.setTitle(c.getTitle());
		 * }
		 */
		res.setAuthority(auth.getAuthority());

		return res;
	}
	public void validateForm(final ActorForm form, final BindingResult binding) {
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
			binding.addError(new FieldError("actorForm", "accept", errorMessage));
		}
		if (!form.getConfirmPassword().equals(form.getPassword())) {
			final Locale locale = LocaleContextHolder.getLocale();
			final String errorMessage = this.messageSource.getMessage("company.mustmatch", null, locale);
			binding.addError(new FieldError("actorForm", "confirmPassword", errorMessage));
		}
		if ((form.getEmail().endsWith("@") || form.getEmail().endsWith("@>")) && !form.getAuthority().equals(Authority.ADMIN)) {
			final Locale locale = LocaleContextHolder.getLocale();
			final String errorMessage = this.messageSource.getMessage("actor.bademail", new Object[] {
				form.getEmail()
			}, locale);
			//final FieldError badEmailError = new FieldError("actorForm", "email", errorMessage);
			final FieldError badEmailError = new FieldError("actorForm", "email", form.getEmail(), true, new String[] {
				"actor.bademail"
			}, new Object[] {
				form.getEmail()
			}, errorMessage);
			binding.addError(badEmailError);
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
	public Actor deconstruct(final ActorForm form, final BindingResult binding) {
		Actor res = null;
		if (form.getId() == 0)
			res = this.create(form.getAuthority());
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
		auth.setAuthority(form.getAuthority());
		authorities.add(auth);
		res.getUserAccount().setAuthorities(authorities);
		this.validator.validate(form, binding);
		return res;
	}

	public boolean containsSpam(final String s) {
		boolean res = false;
		final List<String> negativeWords = new ArrayList<>();
		negativeWords.addAll(this.configurationService.findOne().getNegativeWordsEN());
		negativeWords.addAll(this.configurationService.findOne().getNegativeWordsES());
		for (final String spamWord : negativeWords)
			if (s.contains(spamWord)) {
				System.out.println(spamWord);
				res = true;
				break;
			}
		return res;
	}

	public boolean isSpammer(final Actor a) {
		final boolean res = false;
		Assert.notNull(a);
		this.serviceUtils.checkId(a.getId());
		final Actor actor = this.actorRepository.findOne(a.getId());
		Assert.notNull(actor);
		return res;
	}

	public void exportPDF(final int actorId) {
		final Actor actor = this.findOne(actorId);
		Document document = new Document();

		final Collection<Authority> authorities = actor.getUserAccount().getAuthorities();
		final Authority hack = new Authority();
		hack.setAuthority(Authority.ROOKIE);
		final Authority com = new Authority();
		com.setAuthority(Authority.COMPANY);
		final Authority admin = new Authority();
		admin.setAuthority(Authority.ADMIN);
		final Authority prov = new Authority();
		prov.setAuthority(Authority.PROVIDER);
		final Authority aud = new Authority();
		aud.setAuthority(Authority.AUDITOR);

		if (authorities.contains(com)) {
			final Company company = this.companyService.findOne(actorId);

			try {

				final OutputStream outputStream = new FileOutputStream("C:\\Users\\" + company.getUserAccount().getUsername() + "_data.pdf");
				final PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
				document = this.initDoc(actor, document);
				document = this.docCompany(document, company);
				document.close();

				pdfWriter.close();

			} catch (final IOException e) {
				e.printStackTrace();
			} catch (final DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (authorities.contains(hack)) {
			final Rookie rookie = this.rookieService.findOne(actor.getId());

			try {
				final OutputStream outputStream = new FileOutputStream("C:\\Users\\" + rookie.getUserAccount().getUsername() + "_data.pdf");
				final PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
				document = this.initDoc(actor, document);
				document = this.docRookie(document, rookie);
				document.close();
				pdfWriter.close();
			} catch (final IOException e) {
				e.printStackTrace();
			} catch (final DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (authorities.contains(admin)) {
			final Administrator administrator = this.administratorService.findOne(actor.getId());

			try {
				final OutputStream outputStream = new FileOutputStream("C:\\Users\\" + administrator.getUserAccount().getUsername() + "_data.pdf");
				final PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
				document = this.initDoc(actor, document);
				document.close();
				pdfWriter.close();
			} catch (final IOException e) {
				e.printStackTrace();
			} catch (final DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (authorities.contains(prov)) {
			final Provider provider = this.providerService.findOne(actor.getId());

			try {
				final OutputStream outputStream = new FileOutputStream("C:\\Users\\" + provider.getUserAccount().getUsername() + "_data.pdf");
				final PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
				document = this.initDoc(actor, document);
				document = this.docProvider(document, provider);
				document.close();
				pdfWriter.close();
			} catch (final IOException e) {
				e.printStackTrace();
			} catch (final DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (authorities.contains(aud)) {
			final Auditor auditor = this.auditorService.findOne(actor.getId());

			try {
				final OutputStream outputStream = new FileOutputStream("C:\\Users\\" + auditor.getUserAccount().getUsername() + "_data.pdf");
				final PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
				document = this.initDoc(actor, document);
				document = this.docAuditor(document, auditor);
				document.close();
				pdfWriter.close();
			} catch (final IOException e) {
				e.printStackTrace();
			} catch (final DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public Document initDoc(final Actor actor, final Document document) throws MalformedURLException, IOException {

		try {
			document.open();

			document.add(new Paragraph("USER DATA.", new Font(FontFactory.getFont("arial", 22, Font.UNDERLINE))));
			document.add(new Paragraph("\n"));
			if (StringUtils.isNotEmpty(actor.getPhoto())) {
				final URL url = new URL(actor.getPhoto());
				try {
					document.add(this.urlToImage(url));
				} catch (final Throwable t) {
					document.add(new Paragraph("Photo: " + actor.getPhoto()));
				}
			}
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("Name: " + actor.getName()));
			document.add(new Paragraph("Surname: " + actor.getSurname()));
			if (StringUtils.isNotEmpty(actor.getAddress()))
				document.add(new Paragraph("Address: " + actor.getAddress()));
			document.add(new Paragraph("Email: " + actor.getEmail()));
			if (StringUtils.isNotEmpty(actor.getPhone()))
				document.add(new Paragraph("Phone: " + actor.getPhone()));

		} catch (final DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return document;
	}

	public Image urlToImage(final URL url) throws IOException, BadElementException {
		BufferedImage c = null;
		try {
			final HttpURLConnection httpURLCon = (HttpURLConnection) url.openConnection();
			httpURLCon.addRequestProperty("User-Agent", "Mozilla/4.76");
			c = ImageIO.read(httpURLCon.getInputStream());
		} catch (final IOException ioexc) {
			final String urlPath = "http://" + url.getHost() + url.getPath();
			final URL newUrl = new URL(urlPath);
			final HttpURLConnection httpURLCon = (HttpURLConnection) newUrl.openConnection();
			httpURLCon.addRequestProperty("User-Agent", "Mozilla/4.76");
			c = ImageIO.read(httpURLCon.getInputStream());
		}
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(c, "png", baos);
		final Image iTextImage = Image.getInstance(baos.toByteArray());
		iTextImage.scaleToFit(100f, 200f);
		return iTextImage;
	}
	public Document docCompany(final Document document, final Company company) throws DocumentException, MalformedURLException, IOException {

		final List<Problem> problems = new ArrayList<>(this.problemService.findProblemsByCompanyId(company.getId()));
		if (problems != null)
			if (!problems.isEmpty()) {
				document.add(new Paragraph("PROBLEMS.", new Font(FontFactory.getFont("arial", 22, Font.UNDERLINE))));
				document.add(new Paragraph("\n"));
				int i = 1;
				for (final Problem problem : problems) {
					document.add(new Paragraph("Problem" + i + " --> " + "Hint: " + problem.getHint()));
					document.add(new Paragraph("\n"));
					i++;
				}
			}

		return document;
	}

	public Document docRookie(final Document document, final Rookie rookie) throws DocumentException, MalformedURLException, IOException {

		final List<Application> applications = new ArrayList<>(this.applicationService.findApplicationByRookie(rookie));
		if (applications != null)
			if (!applications.isEmpty()) {
				document.add(new Paragraph("APPLICATIONS.", new Font(FontFactory.getFont("arial", 22, Font.UNDERLINE))));
				document.add(new Paragraph("\n"));
				for (final Application application : applications) {
					document.add(new Paragraph("\n"));
					document.add(new Paragraph(application.getPosition().getTitle() + " --> " + application.getStatus().toUpperCase()));
					document.add(new Paragraph("\n"));
				}
			}

		return document;
	}

	public Document docProvider(final Document document, final Provider provider) throws DocumentException, MalformedURLException, IOException {

		final List<Item> items = new ArrayList<>(this.itemService.findItemsByProviderId(provider.getId()));
		if (items != null)
			if (!items.isEmpty()) {
				document.add(new Paragraph("ITEMS.", new Font(FontFactory.getFont("arial", 22, Font.UNDERLINE))));
				document.add(new Paragraph("\n"));
				for (final Item item : items) {
					document.add(new Paragraph("\n"));
					document.add(new Paragraph(item.getId() + ": " + item.getName() + ", " + item.getDescription()));
					document.add(new Paragraph("\n"));
				}
			}

		final List<Sponsorship> sponsorships = new ArrayList<>(this.sponsorshipService.findSponsorshipsByProviderId(provider.getId()));
		if (sponsorships != null)
			if (!sponsorships.isEmpty()) {
				document.add(new Paragraph("SPONSORSHIPS.", new Font(FontFactory.getFont("arial", 22, Font.UNDERLINE))));
				document.add(new Paragraph("\n"));
				for (final Sponsorship sponsorship : sponsorships) {
					if (StringUtils.isNotEmpty(sponsorship.getBanner()))
						document.add(this.urlToImage(new URL(sponsorship.getBanner())));
					document.add(new Paragraph("\n"));
				}
			}

		return document;
	}

	public Collection<Actor> findAllExceptMe(final Actor a) {
		Collection<Actor> result;
		result = this.actorRepository.findSpammersAndBannedActors();
		result.remove(a);
		Assert.notNull(result);

		return result;
	}

	public Document docAuditor(final Document document, final Auditor auditor) throws DocumentException, MalformedURLException, IOException {

		final List<Audit> audits = new ArrayList<>(this.auditService.findAuditsByAuditorId(auditor.getId()));
		if (audits != null)
			if (!audits.isEmpty()) {
				document.add(new Paragraph("AUDITS.", new Font(FontFactory.getFont("arial", 22, Font.UNDERLINE))));
				document.add(new Paragraph("\n"));
				for (final Audit audit : audits) {
					document.add(new Paragraph("\n"));
					document.add(new Paragraph(audit.getId() + ": " + audit.getText() + ", " + audit.getScore()));
					document.add(new Paragraph("\n"));
				}
			}

		return document;
	}

}

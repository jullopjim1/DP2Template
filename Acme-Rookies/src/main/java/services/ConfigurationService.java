
package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ConfigurationRepository;
import domain.Configuration;

@Service
@Transactional
public class ConfigurationService {

	//Repository-----------------------------------------------------------------

	@Autowired
	private ConfigurationRepository	configurationRepository;

	//Services-------------------------------------------------------------------

	@Autowired
	private AdministratorService	administratorService;


	//Metodos--------------------------------------------------------------------

	public Configuration findOne() {
		final List<Configuration> configurations = new ArrayList<>(this.configurationRepository.findAll());
		return configurations.get(0);
	}

	public Configuration save(final Configuration configuration) {
		Assert.notNull(configuration);
		final Configuration oldConfiguration = this.configurationRepository.findOne(configuration.getId());
		configuration.setHasRebranded(oldConfiguration.isHasRebranded());
		Assert.isTrue(this.administratorService.isPrincipalAdmin(), "noAdmin");
		configuration.setMakeName(this.makeNameUpper(configuration));

		final Configuration saved = this.configurationRepository.save(configuration);

		return saved;
	}

	public Configuration saveWhenRebranding() {
		final Configuration oldConfiguration = this.findOne();
		Assert.isTrue(this.administratorService.isPrincipalAdmin(), "noAdmin");
		Assert.isTrue(!oldConfiguration.isHasRebranded());
		oldConfiguration.setHasRebranded(true);
		final Configuration saved = this.save(oldConfiguration);
		return saved;
	}

	//Otros-----------------------------------------------------------------------

	//	@SuppressWarnings("deprecation")
	//	public String generateTicker(final Parade ) {
	//		final Date date = parade.getMomentOrganised();
	//		final Integer s1 = date.getDate();
	//		String day = s1.toString();
	//		if (day.length() == 1)
	//			day = "0" + day;
	//		final Integer s2 = date.getMonth() + 1;
	//		String month = s2.toString();
	//		if (month.length() == 1)
	//			month = "0" + month;
	//		final Integer s3 = date.getYear();
	//		final String year = s3.toString().substring(1);
	//
	//		return year + month + day + "-" + this.generateStringAux();
	//	}

	private String generateStringAux() {
		final int length = 5;
		final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final Random rng = new Random();
		final char[] text = new char[length];
		for (int i = 0; i < 5; i++)
			text[i] = characters.charAt(rng.nextInt(characters.length()));
		return new String(text);
	}

	//	public String isUniqueTicker(final Parade parade) {
	//		String result = this.generateTicker(parade);
	//
	//		for (final Parade parade1 : this.paradeService.findAll())
	//			if (parade1.getTicker().equals(result))
	//				result = this.isUniqueTicker(parade);
	//
	//		return result;
	//	}

	public String internacionalizcionWelcome() {
		String res = null;
		final Configuration configuration = this.findOne();

		if (LocaleContextHolder.getLocale().getLanguage().toLowerCase().equals("en"))
			res = configuration.getWelcomeMessageEN();
		else if (LocaleContextHolder.getLocale().getLanguage().toLowerCase().equals("es"))
			res = configuration.getWelcomeMessageES();

		return res;
	}

	public String internacionalizcionSecurityMessage() {
		String res = null;
		final Configuration configuration = this.findOne();

		if (LocaleContextHolder.getLocale().getLanguage().toLowerCase().equals("en"))
			res = configuration.getSecurityMessageEN();
		else if (LocaleContextHolder.getLocale().getLanguage().toLowerCase().equals("es"))
			res = configuration.getSecurityMessageES();

		return res;
	}

	public List<String> makeNameUpper(final Configuration configuration) {
		final List<String> result = new ArrayList<>();
		final List<String> makeNames = configuration.getMakeName();
		for (final String string : makeNames)
			result.add(string.toUpperCase());
		return result;
	}

	public Double flatFareWithVAT() {
		Double res = this.findOne().getFlatFare() + (this.findOne().getFlatFare() * this.findOne().getVat() / 100);
		res = Math.floor(res * 1e2) / 1e2;
		return res;
	}

	public void active() {
		final Configuration configuration = this.findOne();
		configuration.setFailSystem(true);
		this.save(configuration);
	}

	public void desactive() {
		final Configuration configuration = this.findOne();
		configuration.setFailSystem(false);
		this.save(configuration);
	}

}

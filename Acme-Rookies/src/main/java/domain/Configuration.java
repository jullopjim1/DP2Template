
package domain;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

import cz.jirutka.validator.collection.constraints.EachSafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class Configuration extends DomainEntity {

	// Properties

	private String			nameSys;
	private String			banner;
	private String			welcomeMessageEN;
	private List<String>	spamWordsEN;
	private List<String>	negativeWordsEN;
	private List<String>	positiveWordsEN;
	private String			welcomeMessageES;
	private List<String>	spamWordsES;
	private List<String>	negativeWordsES;
	private List<String>	positiveWordsES;
	private int				numResults;
	private int				cacheFinder;
	private int				countryCode;
	private List<String>	makeName;
	private int				vat;
	private Double			flatFare;
	private String			securityMessageES;
	private String			securityMessageEN;
	private boolean			failSystem;
	private boolean			hasRebranded;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getNameSys() {
		return this.nameSys;
	}
	public void setNameSys(final String nameSys) {
		this.nameSys = nameSys;
	}

	@URL
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getBanner() {
		return this.banner;
	}
	public void setBanner(final String banner) {
		this.banner = banner;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getWelcomeMessageEN() {
		return this.welcomeMessageEN;
	}
	public void setWelcomeMessageEN(final String welcomeMessageEN) {
		this.welcomeMessageEN = welcomeMessageEN;
	}
	@NotEmpty
	@ElementCollection
	@EachSafeHtml(whitelistType = WhiteListType.NONE)
	public List<String> getSpamWordsEN() {
		return this.spamWordsEN;
	}
	public void setSpamWordsEN(final List<String> spamWordsEN) {
		this.spamWordsEN = spamWordsEN;
	}
	@NotEmpty
	@ElementCollection
	@EachSafeHtml(whitelistType = WhiteListType.NONE)
	public List<String> getNegativeWordsEN() {
		return this.negativeWordsEN;
	}
	public void setNegativeWordsEN(final List<String> negativeWordsEN) {
		this.negativeWordsEN = negativeWordsEN;
	}
	@NotEmpty
	@ElementCollection
	@EachSafeHtml
	public List<String> getPositiveWordsEN() {
		return this.positiveWordsEN;
	}
	public void setPositiveWordsEN(final List<String> positiveWordsEN) {
		this.positiveWordsEN = positiveWordsEN;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getWelcomeMessageES() {
		return this.welcomeMessageES;
	}
	public void setWelcomeMessageES(final String welcomeMessageES) {
		this.welcomeMessageES = welcomeMessageES;
	}
	@NotEmpty
	@ElementCollection
	@EachSafeHtml(whitelistType = WhiteListType.NONE)
	public List<String> getSpamWordsES() {
		return this.spamWordsES;
	}
	public void setSpamWordsES(final List<String> spamWordsES) {
		this.spamWordsES = spamWordsES;
	}
	@NotEmpty
	@ElementCollection
	@EachSafeHtml(whitelistType = WhiteListType.NONE)
	public List<String> getNegativeWordsES() {
		return this.negativeWordsES;
	}
	public void setNegativeWordsES(final List<String> negativeWordsES) {
		this.negativeWordsES = negativeWordsES;
	}
	@NotEmpty
	@ElementCollection
	@EachSafeHtml(whitelistType = WhiteListType.NONE)
	public List<String> getPositiveWordsES() {
		return this.positiveWordsES;
	}
	public void setPositiveWordsES(final List<String> positiveWordsES) {
		this.positiveWordsES = positiveWordsES;
	}

	@Range(min = 10, max = 100)
	public int getNumResults() {
		return this.numResults;
	}
	public void setNumResults(final int numResults) {
		this.numResults = numResults;
	}

	@Range(min = 1, max = 24)
	public int getCacheFinder() {
		return this.cacheFinder;
	}
	public void setCacheFinder(final int cacheFinder) {
		this.cacheFinder = cacheFinder;
	}

	@Range(min = 1, max = 999)
	public int getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(final int countrtCode) {
		this.countryCode = countrtCode;
	}
	@NotEmpty
	@ElementCollection(fetch = FetchType.EAGER)
	@EachSafeHtml(whitelistType = WhiteListType.NONE)
	public List<String> getMakeName() {
		return this.makeName;
	}

	public void setMakeName(final List<String> makeName) {
		this.makeName = makeName;
	}

	@Range(min = 1, max = 100)
	public int getVat() {
		return this.vat;
	}

	public void setVat(final int vat) {
		this.vat = vat;
	}

	@NotNull
	public Double getFlatFare() {
		return this.flatFare;
	}

	public void setFlatFare(final Double flatFare) {
		this.flatFare = flatFare;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getSecurityMessageES() {
		return this.securityMessageES;
	}

	public void setSecurityMessageES(final String securityMessageES) {
		this.securityMessageES = securityMessageES;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getSecurityMessageEN() {
		return this.securityMessageEN;
	}

	public void setSecurityMessageEN(final String securityMessageEN) {
		this.securityMessageEN = securityMessageEN;
	}

	public boolean isFailSystem() {
		return this.failSystem;
	}

	public void setFailSystem(final boolean failSystem) {
		this.failSystem = failSystem;
	}

	public boolean isHasRebranded() {
		return this.hasRebranded;
	}
	public void setHasRebranded(final boolean hasRebranded) {
		this.hasRebranded = hasRebranded;
	}

}

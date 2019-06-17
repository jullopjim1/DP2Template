
package forms;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

import domain.DomainEntity;

public class CompanyForm extends DomainEntity {

	private String	name;
	private String	surname;
	private String	photo;
	private String	phone;
	private String	address;
	private boolean	spammer;
	private boolean	banned;
	private boolean	accept;
	private String	email;
	private String	VATNumber;
	private String	companyName;
	private String	username;
	private String	password;
	private String	confirmPassword;
	private String	holderName;
	private String	makeName;
	private String	number;
	private int		expirationMonth;
	private int		expirationYear;
	private int		CVVCode;
	private Double	score;


	public Double getScore() {
		return this.score;
	}
	public void setScore(final Double score) {
		this.score = score;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getHolderName() {
		return this.holderName;
	}

	public void setHolderName(final String holderName) {
		this.holderName = holderName;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getMakeName() {
		return this.makeName;
	}

	public void setMakeName(final String makeName) {
		this.makeName = makeName;
	}

	@CreditCardNumber
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getNumber() {
		return this.number;
	}

	public void setNumber(final String number) {
		this.number = number;
	}

	@Range(min = 1, max = 12)
	public int getExpirationMonth() {
		return this.expirationMonth;
	}

	public void setExpirationMonth(final int expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	@Range(min = 2018, max = 3000)
	public int getExpirationYear() {
		return this.expirationYear;
	}

	public void setExpirationYear(final int expirationYear) {
		this.expirationYear = expirationYear;
	}

	@Range(min = 100, max = 999)
	public int getCVVCode() {
		return this.CVVCode;
	}

	public void setCVVCode(final int cVVCode) {
		this.CVVCode = cVVCode;
	}

	public boolean getAccept() {
		return this.accept;
	}
	public void setAccept(final boolean accept) {
		this.accept = accept;
	}

	@NotBlank
	@Pattern(regexp = "^(\\w+@(\\w+(\\.\\w*)*))|(\\w+( \\w+)* <\\w+@(\\w+(\\.\\w*)*)>)$")
	public String getEmail() {
		return this.email;
	}
	public void setEmail(final String email) {
		this.email = email;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}
	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getSurname() {
		return this.surname;
	}
	public void setSurname(final String surname) {
		this.surname = surname;
	}

	@URL
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getPhoto() {
		return this.photo;
	}
	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getPhone() {
		return this.phone;
	}
	public void setPhone(final String phone) {
		this.phone = phone;
	}

	@Size(min = 5, max = 32)
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getUsername() {
		return this.username;
	}
	public void setUsername(final String username) {
		this.username = username;
	}

	@Size(min = 5, max = 32)
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getPassword() {
		return this.password;
	}
	public void setPassword(final String password) {
		this.password = password;
	}

	@Size(min = 5, max = 32)
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getConfirmPassword() {
		return this.confirmPassword;
	}
	public void setConfirmPassword(final String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getAddress() {
		return this.address;
	}
	public void setAddress(final String address) {
		this.address = address;
	}

	public boolean isSpammer() {
		return this.spammer;
	}
	public void setSpammer(final boolean spammer) {
		this.spammer = spammer;
	}

	public boolean isBanned() {
		return this.banned;
	}
	public void setBanned(final boolean banned) {
		this.banned = banned;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getVATNumber() {
		return this.VATNumber;
	}
	public void setVATNumber(final String vATNumber) {
		this.VATNumber = vATNumber;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getCompanyName() {
		return this.companyName;
	}
	public void setCompanyName(final String companyName) {
		this.companyName = companyName;
	}

}

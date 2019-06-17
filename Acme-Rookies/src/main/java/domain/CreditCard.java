
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
public class CreditCard extends DomainEntity {

	// Identification ---------------------------------------------------------
	// ATRIBUTOS
	private String	holderName;
	private String	makeName;
	private String	number;
	private int		expirationMonth;
	private int		expirationYear;
	private int		CVVCode;


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

	// Relationships ---------------------------------------------------------

}
